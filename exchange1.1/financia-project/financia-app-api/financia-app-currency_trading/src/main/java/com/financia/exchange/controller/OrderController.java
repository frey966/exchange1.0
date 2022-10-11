package com.financia.exchange.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.financia.common.core.web.domain.AjaxResult;
import com.financia.currency.*;
import com.financia.currency.Currency;
import com.financia.currency.ExchangeOrderDirection;
import com.financia.exchange.MemberBusinessWallet;
import com.financia.exchange.dto.*;
import com.financia.exchange.engine.ContractCoinMatchFactory;
import com.financia.exchange.service.*;
import com.financia.exchange.util.DateUtil;
import com.financia.exchange.util.GeneratorUtil;
import com.financia.exchange.feign.client.MemberService;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.*;


/**
 * 委托订单处理类
 */
@Slf4j
@RestController
@RequestMapping("/order")
@Api(tags = "委托订单处理")
public class OrderController {

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    @Autowired
    private MemberService memberService;

    @Autowired
    private CurrencyService currencyService;

    @Autowired
    private MemberCurrencyWalletService memberCurrencyWalletService;

    @Autowired
    private MemberBusinessWalletService memberBusinessWalletService;

    @Autowired
    private ExchangeOrderService exchangeOrderService;

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private ContractCoinMatchFactory matchFactory;

    String TICKER_KEY = "ticker:%s";

    /**
     * 币币交易下单
     * @return
     */
    @PostMapping("add")
    @ApiOperation("币币交易-添加订单")
    public AjaxResult addOrder(@RequestBody AddOrderDTO dto) {

        Long memberId = dto.getMemberId(); // 会员Id
        BigDecimal tradeAmount = dto.getTradeAmount(); // 前端输入的购买金额
        BigDecimal tradeNum = dto.getTradeNum(); // 前端输入的购买数量
        Long currencyId = dto.getCurrencyId(); // 交易对Id
        ExchangeOrderDirection direction = dto.getDirection(); // 买/卖
        ExchangeOrderType orderType = dto.getOrderType();// 订单类型 市价、限价
        BigDecimal entrustPrice = dto.getEntrustPrice(); // 委托价


        // 输入合法性检查
        if (memberId == null || currencyId == null || direction == null || orderType == null) {
            // return MessageResult.error(500, msService.getMessage("ILLEGAL_ARGUMENT"));
            return AjaxResult.error("illegal argument");
        }

        // 必须输入购买金额或者数量
        if(tradeAmount == null && tradeNum == null) {
            // return MessageResult.error(500, msService.getMessage("ILLEGAL_ARGUMENT"));
            return AjaxResult.error("illegal argument");
        }

        // 必须是买或者卖
        if (direction != ExchangeOrderDirection.BUY && direction != ExchangeOrderDirection.SELL) {
            // return MessageResult.error(500, msService.getMessage("ILLEGAL_ARGUMENT"));
            return AjaxResult.error("illegal argument");
        }

        // 必须是市价或限价
        if (orderType != ExchangeOrderType.MARKET_PRICE && orderType != ExchangeOrderType.LIMIT_PRICE) {
            // return MessageResult.error(500, msService.getMessage("ILLEGAL_ARGUMENT"));
            return AjaxResult.error("illegal argument");
        }

        //判断限价输入值是否小于零
        if (entrustPrice.compareTo(BigDecimal.ZERO) <= 0 && orderType == ExchangeOrderType.LIMIT_PRICE) {
            // return MessageResult.error(500, msService.getMessage("EXORBITANT_PRICES"));
            return AjaxResult.error("exorbitant prices!");
        }

        //根据交易对Id获取交易对信息
        Currency currency = currencyService.getById(currencyId);
        if (currency == null) {
            // return MessageResult.error(500, msService.getMessage("NONSUPPORT_COIN"));
            return AjaxResult.error("nonsupport coin!");
        }

        // 检查合约引擎是否存在
        if (!matchFactory.containsContractCoinMatch(currency.getPair())) {
            // return MessageResult.error(500, msService.getMessage("CONTRACT_ENGINE_ABNORMAL"));
            return AjaxResult.error("The contract matching engine is abnormal, please place the order later");
        }

        // 如果是市价购买，则委托价等于当前市价
        if(orderType == ExchangeOrderType.MARKET_PRICE){
            Object ticker = redisTemplate.opsForValue().get(String.format(TICKER_KEY, currency.getSymbol()));
            if(ObjectUtils.isEmpty(ticker)) {
                // return MessageResult.error(500, msService.getMessage("NOT_ALLOWED_TO_BUY"));
                return AjaxResult.error("Currently not allowed to buy");
            }
            CoinThumb coinThumb = JSONObject.parseObject((String)ticker, CoinThumb.class);
            entrustPrice = coinThumb.getClose();
            if(entrustPrice.compareTo(BigDecimal.ZERO) <= 0) {
                // return MessageResult.error(500, msService.getMessage("NOT_ALLOWED_TO_BUY"));
                return AjaxResult.error("Currently not allowed to buy");
            }
        }

        //设置价格精度
        entrustPrice.setScale(8, BigDecimal.ROUND_DOWN);

        // 计算购买金额和购买数量
        if(tradeAmount != null && tradeAmount.compareTo(BigDecimal.ZERO) > 0) {
            tradeNum = tradeAmount.divide(entrustPrice,8,BigDecimal.ROUND_DOWN);
        } else {
            tradeAmount = tradeNum.multiply(entrustPrice).setScale(8, BigDecimal.ROUND_DOWN);
        }

        // 购买金额和购买数量不能小于0
        if(tradeAmount.compareTo(BigDecimal.ZERO) <= 0 || tradeNum.compareTo(BigDecimal.ZERO) <= 0) {
            return AjaxResult.error("illegal argument");
        }


        // 检查用户合法性
//        Member member = memberService.getMemberInfo(memberId);
//        if (member == null) {
//            //return MessageResult.error(500, msService.getMessage("USER_PLACED_ORDER_NOT_EXIST"));
//            return AjaxResult.error("The user who placed the order does not exist");
//        }

        // 检查用户余额钱包是否存在
        MemberBusinessWallet memberBusinessWallet = memberBusinessWalletService.getMemberBusinessWalletByMemberId(memberId);
        if(memberBusinessWallet == null) {
            // return MessageResult.error(500,  msService.getMessage("WALLET_NOT_EXIST"));
            return AjaxResult.error("Transfer to or from wallet does not exist");
        }

        // 检查用户币币钱包是否存在
        MemberCryptoCurrencyWallet wallet = memberCurrencyWalletService.findByCoinIdAndMemberId(memberId, currencyId, null);
        if (wallet == null) {
            wallet = new MemberCryptoCurrencyWallet();
            wallet.setMemberId(memberId);
            wallet.setCoinId(currencyId);
            wallet.setCoinSymbol(currency.getPair().split("/")[0]);
            wallet.setBalanceMoney(BigDecimal.ZERO);
            wallet.setFrezzeMoney(BigDecimal.ZERO);
            wallet.setStatus(1);
            wallet.setCreateTime(new Date());
            memberCurrencyWalletService.save(wallet);
        }

        // 构建委托单
        ExchangeOrder order = new ExchangeOrder();
        order.setMemberId(memberId); // 会员Id
        order.setCoinId(currencyId); // 交易对Id
        order.setSymbol(currency.getPair()); // 交易对
        order.setBaseSymbol("USDT"); // 结算单位
        order.setCoinSymbol(currency.getPair().split("/")[0]); // 币单位
        order.setType(orderType); // 订单类型
        order.setDirection(direction); // 买卖方向
        order.setPrice(entrustPrice); // 委托价
        order.setAmount(tradeAmount); // 购买金额
        order.setNum(tradeNum); // 购买数量
        order.setCreateTime(DateUtil.getTimeMillis()); // 订单时间
        order.setStatus(ExchangeOrderStatus.TRADING); // 订单状态
        order.setTradedAmount(BigDecimal.ZERO); // 成交金额
        order.setTurnover(BigDecimal.ZERO); // 成交量
        order.setTradedPrice(BigDecimal.ZERO); // 成交价
        order.setOrderNo(GeneratorUtil.getOrderId("E")); // 订单号
        order.setCurrencyWalletId(wallet.getId()); // 币币钱包Id


        // 保存订单
        return exchangeOrderService.addOrder(memberBusinessWallet, wallet, order);
    }

    /**
     * 当前委托
     *
     * @param memberId
     * @param pageNo
     * @param pageSize
     * @return
     */
    @GetMapping("currentEntrust")
    @ApiOperation("当前委托列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name="memberId",value="会员Id",dataType="long", paramType = "query",example="1"),
            @ApiImplicitParam(name="coinId",value="交易对Id",dataType="long", paramType = "query",example="1"),
            @ApiImplicitParam(name="pageNo",value="当前分页",dataType="long", paramType = "query",example="1"),
            @ApiImplicitParam(name="pageSize",value="分页大小",dataType="long", paramType = "query",example="10"),
    })
    public AjaxResult currentOrder(@RequestParam(value = "memberId") Long memberId,
                                   @RequestParam(value = "coinId", required = false) Long coinId,
                                   @RequestParam(value = "pageNo") int pageNo,
                                   @RequestParam(value = "pageSize") int pageSize) {
        Page<ExchangeOrder> page = exchangeOrderService.findCurrent(memberId, coinId, pageNo, pageSize);
        return AjaxResult.success(page);
    }

    /**
     * 撤销订单
     * @return
     */
    @ApiOperation("撤销订单")
    @PostMapping(value = "cancel")
    public AjaxResult cancelOrder(@RequestBody CancelOrderDTO dto) {
        ExchangeOrder order = exchangeOrderService.getById(dto.getOrderId());

        // 判断委托单状态
        if (order.getStatus() != ExchangeOrderStatus.TRADING) {
            // return MessageResult.error(500, msService.getMessage("ORDER_STATUS_FAILED"));
            return AjaxResult.error("Order status error (filled or cancelled)");
        }

        // 发送消息至Exchange系统
        kafkaTemplate.send("trade-order-cancel",JSON.toJSONString(order));

        // MessageResult result = MessageResult.success(msService.getMessage("CANCELLATION_SUCCEEDED"));
        return AjaxResult.success("Cancellation is successful", null);
    }

    /**
     * 历史委托
     */
    @ApiOperation("历史委托列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name="memberId",value="会员Id",dataType="long", paramType = "query",example="1"),
            @ApiImplicitParam(name="coinId",value="交易对Id",dataType="long", paramType = "query",example="1"),
            @ApiImplicitParam(name = "direction", value = "0-买，1-卖", paramType = "query"),
            @ApiImplicitParam(name = "days", value = "查询日 1-今天 7-7天 30-30天 90-90天", paramType = "query"),
            @ApiImplicitParam(name="pageNo",value="当前分页",dataType="long", paramType = "query",example="1"),
            @ApiImplicitParam(name="pageSize",value="分页大小",dataType="long", paramType = "query",example="10"),
    })
    @GetMapping(value = "historyOrder")
    public AjaxResult historyOrder(Long memberId,
                                   @RequestParam(value = "coinId", required = false) Long coinId,
                                   @RequestParam(value = "direction", required = false) Integer direction,
                                   @RequestParam(value = "days", required = false) Integer days,
                                   int pageNo, int pageSize) {
        Page<ExchangeOrder> page = exchangeOrderService.findHistory(memberId, coinId, direction, days, pageNo, pageSize);
        return AjaxResult.success(page);
    }

    /**
     * 币币持仓账单
     */
    @ApiOperation("币币持仓账单")
    @ApiImplicitParams({
            @ApiImplicitParam(name="currencyWalletId",value="币币钱包Id",dataType="long", paramType = "query",example="1"),
            @ApiImplicitParam(name = "status", value = "账单状态 0-进行中,1-已完成，2-已取消，不传-全部账单", paramType = "query"),
            @ApiImplicitParam(name = "days", value = "查询日 1-今天 7-7天 30-30天 90-90天", paramType = "query"),
            @ApiImplicitParam(name="pageNo",value="当前分页",dataType="long", paramType = "query",example="1"),
            @ApiImplicitParam(name="pageSize",value="分页大小",dataType="long", paramType = "query",example="10"),
    })
    @GetMapping(value = "currencyWalletBill")
    public AjaxResult currencyWalletBill(Long currencyWalletId,
                                         @RequestParam(value = "status",required = false) Integer status,
                                         @RequestParam(value = "days", required = false) Integer days,
                                         int pageNo, int pageSize) {
        Page<ExchangeOrder> page = exchangeOrderService.findCurrencyWalletBill(currencyWalletId, status, days, pageNo, pageSize);
        return AjaxResult.success(page);
    }


}