package com.financia.exchange.controller;

import cn.hutool.core.collection.IterUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.financia.common.core.enums.BusinessSubType;
import com.financia.common.core.utils.DateUtils;
import com.financia.common.core.web.domain.AjaxResult;
import com.financia.exchange.Member;
import com.financia.exchange.MemberBusinessWallet;
import com.financia.exchange.MemberSuperContractWallet;
import com.financia.exchange.dto.*;
import com.financia.exchange.engine.ContractCoinMatchFactory;
import com.financia.exchange.feign.client.MemberService;
import com.financia.exchange.feign.client.RemoteMemberWalletService;
import com.financia.exchange.service.MemberSuperContractWalletService;
import com.financia.exchange.service.SuperCoinFeeService;
import com.financia.exchange.service.SuperContractCoinService;
import com.financia.exchange.service.SuperContractOrderEntrustService;
import com.financia.exchange.util.DateUtil;
import com.financia.exchange.util.GeneratorUtil;
import com.financia.superleverage.SuperCoinFee;
import com.financia.superleverage.SuperContractCoin;
import com.financia.superleverage.SuperContractOrderEntrust;
import com.financia.swap.*;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 超级杠杆订单处理类
 */
@Slf4j
@RestController
@RequestMapping("/order")
@Api(tags = "超级杠杆订单处理")
public class SuperEntrustOrderController {

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    @Autowired
    private SuperContractCoinService superContractCoinService;

    @Autowired
    private MemberSuperContractWalletService memberContractWalletService;

    @Autowired
    private ContractCoinMatchFactory contractCoinMatchFactory;

    @Autowired
    private SuperContractOrderEntrustService contractOrderEntrustService;

    @Autowired
    private SuperCoinFeeService coinFeeService;

    @Autowired
    private MemberService memberService;

    @Autowired
    private RemoteMemberWalletService memberWalletService;

    /**
     * 超级杠杆下单（开仓）
     * 二种操作类型：买入做多，卖出做空
     *
     * @return
     */
    @PostMapping("open")
    @ApiOperation("超级杠杆下单（开仓）")
    public AjaxResult openOrder(@RequestBody OpenOrderDTO dto) {

        Long memberId = dto.getMemberId(); // 会员Id
        BigDecimal tradeAmount = dto.getTradeAmount(); // 前端输入的购买金额
        Long contractCoinId = dto.getContractCoinId(); // 合约交易对Id
        Integer direction = dto.getDirection(); // 买/卖
        Integer orderType = dto.getOrderType(); // 订单类型 0-市价、2-计划
        BigDecimal leverage = dto.getLeverage(); // 杠杆倍数


        // 输入合法性检查
        if (contractCoinId == null || direction == null || orderType == null || leverage == null || tradeAmount == null || memberId == null) {
            // return MessageResult.error(500, msService.getMessage("ILLEGAL_ARGUMENT"));
            return AjaxResult.error("illegal argument1");
        }
        // 必须是买或者卖
        if (!direction.equals(ContractOrderDirection.BUY.getCode()) && !direction.equals(ContractOrderDirection.SELL.getCode())) {
            // return MessageResult.error(500, msService.getMessage("ILLEGAL_ARGUMENT"));
            return AjaxResult.error("illegal argument2");
        }

        // 必须是市价或者计划委托
        if (!orderType.equals(ContractOrderType.MARKET_PRICE.getCode()) && !orderType.equals(ContractOrderType.SPOT_LIMIT.getCode())) {
            // return MessageResult.error(500, msService.getMessage("ILLEGAL_ARGUMENT"));
            return AjaxResult.error("illegal argument4");
        }
        // 单笔开仓金额需要大于10且小于50000
        if (tradeAmount.compareTo(BigDecimal.valueOf(10)) < 0 || tradeAmount.compareTo(BigDecimal.valueOf(50000)) > 0) {
            return AjaxResult.error("The order amount must be greater than or equal to 10 and less than or equal to 50000");
        }
        // 检查交易对是否存在
        SuperContractCoin contractCoin = superContractCoinService.getById(contractCoinId);
        if (contractCoin == null) {
            // return MessageResult.error(500, msService.getMessage("NONSUPPORT_COIN"));
            return AjaxResult.error("nonsupport coin!");
        }

        // 检查用户合法性
        Member member = memberService.getMemberInfo(memberId);
        if (member == null) {
            //return MessageResult.error(500, msService.getMessage("USER_PLACED_ORDER_NOT_EXIST"));
            return AjaxResult.error("The user who placed the order does not exist");
        }

        // 检查用户余额钱包是否存在
        MemberBusinessWallet memberBusinessWallet = memberWalletService.getMemberBusinessWalletByMemberId(memberId);
        if (memberBusinessWallet == null) {
            // return MessageResult.error(500,  msService.getMessage("WALLET_NOT_EXIST"));
            return AjaxResult.error("Transfer to or from wallet does not exist");
        }

        // 检查用户余额是否足够
        if (tradeAmount.compareTo(memberBusinessWallet.getMoney()) > 0) {
            // return MessageResult.error(500, msService.getMessage("ACCOUNT_INSUFFICIENT_BALANCE"));
            return AjaxResult.error("The contract account has insufficient funds");
        }

        // 委托单Id
        String orderId = GeneratorUtil.getOrderId("SUPER_CE");
        // 钱包
        MemberSuperContractWallet wallet = new MemberSuperContractWallet();
        wallet.setMemberId(memberId); // 会员Id
        wallet.setUsdtBalance(BigDecimal.ZERO); // 余额
        wallet.setUsdtBuyLeverage(leverage); // 杠杆倍数
        wallet.setStatus(1); // 状态
        wallet.setUsdtBuyPosition(BigDecimal.ZERO);  //持仓
        wallet.setUsdtBuyPrice(BigDecimal.ZERO);  // 开仓均价
        wallet.setUsdtBuyPrincipalAmount(BigDecimal.ZERO); // 保证金
        wallet.setUsdtProfit(BigDecimal.ZERO);  // 盈利
        wallet.setUsdtLoss(BigDecimal.ZERO);  // 亏损
        wallet.setUsdtPattern(ContractOrderPattern.FIXED); // 仓位模式
        wallet.setContractId(contractCoinId); // 杠杆交易币Id
        wallet.setEntrustOrder(orderId); // 委托订单Id
        wallet.setCreateTime(new Date()); // 创建时间

        // 获取委托价格
        BigDecimal entrustPrice = dto.getEntrustPrice();
        // 计划委托单需要输入委托价格
        if (orderType.equals(ContractOrderType.SPOT_LIMIT.getCode())) {
            if (entrustPrice == null) {
                // return MessageResult.error(500, msService.getMessage("ORDER_PRICE"));
                return AjaxResult.error("Please enter the order price");
            }
            if (entrustPrice.compareTo(BigDecimal.ZERO) < 0) {
                // return MessageResult.error(500, msService.getMessage("THE_PLANNED_ORDER_PRICE_LESS_THAN_0"));
                return AjaxResult.error("The planned order price cannot be less than 0");
            }
        }


        // 检查交易对是否可用
        if (contractCoin.getEnable() != 1 || contractCoin.getExchangeable() != 1) {
            // return MessageResult.error(500, msService.getMessage("COIN_FORBIDDEN"));
            return AjaxResult.error("the coin is forbidden now");
        }

        // 杠杆倍数是否在规定范围
        String[] leverageArr = contractCoin.getLeverage().split(",");
        String maxLeverage = leverageArr[leverageArr.length - 1];
        if (leverage.compareTo(new BigDecimal(maxLeverage)) > 0) {
            // return MessageResult.error(500, msService.getMessage("CURRENCY_LEVERAGE_WRONG"));
            return AjaxResult.error("Leverage exceeds the allowable range");
        }
        String minLeverage = leverageArr[0];
        if (leverage.compareTo(new BigDecimal(minLeverage)) < 0) {
            // return MessageResult.error(500, msService.getMessage("CURRENCY_LEVERAGE_WRONG"));
            return AjaxResult.error("Leverage exceeds the allowable range");
        }

        // 检查合约引擎是否存在
        if (!contractCoinMatchFactory.containsContractCoinMatch(contractCoin.getSymbol())) {
            // return MessageResult.error(500, msService.getMessage("CONTRACT_ENGINE_ABNORMAL"));
            return AjaxResult.error("The contract matching engine is abnormal, please place the order later1");
        }

        //当前价格
        BigDecimal currentPrice = contractCoinMatchFactory.getContractCoinMatch(contractCoin.getSymbol()).getNowPrice();
        // System.out.println("current price is :"+dto.toString()+","+currentPrice);
        if (currentPrice.compareTo(BigDecimal.ZERO) <= 0) {
            return AjaxResult.error("The contract matching engine is abnormal, please place the order later2");
        }

        // 计算开仓手续费率（需要根据杠杆倍数动态查询）
        BigDecimal openFee = BigDecimal.ZERO;

        SuperCoinFee coinFee = coinFeeService.getFee(leverage.toBigInteger().intValue());
        BigDecimal openFeeRate = coinFee.getRate();
        // 1、计算开仓手续费(开仓手续费=开仓本金*杠杆倍数*开仓手续费率，计入持仓盈亏）
        openFee = tradeAmount.multiply(leverage).multiply(openFeeRate);


        // 如果是市价，则委托价格=当前市价
        if (orderType.equals(ContractOrderType.MARKET_PRICE.getCode())) {
            entrustPrice = currentPrice; //市价成交
        }

        // 计算开仓数量
        BigDecimal volume = BigDecimal.ZERO;

        // 计算开仓数量: (交易金额-开仓手续费)*杠杆倍数 / 委托价格
        volume = (tradeAmount.subtract(openFee)).multiply(leverage).divide(entrustPrice, 8, BigDecimal.ROUND_DOWN);


        // 新建合约委托单
        SuperContractOrderEntrust orderEntrust = new SuperContractOrderEntrust();
        orderEntrust.setContractId(contractCoin.getId()); // 合约ID
        orderEntrust.setMemberId(memberId); // 用户ID
        orderEntrust.setSymbol(contractCoin.getSymbol()); // 交易对符号
        orderEntrust.setBaseSymbol(contractCoin.getSymbol().split("/")[1]); // 基币/结算币
        orderEntrust.setCoinSymbol(contractCoin.getSymbol().split("/")[0]); // 币种符号
        orderEntrust.setDirection(direction.equals(ContractOrderDirection.BUY.getCode()) ? ContractOrderDirection.BUY : ContractOrderDirection.SELL); // 开仓方向：0-做多/1-做空
        orderEntrust.setContractOrderEntrustId(orderId); //订单号
        orderEntrust.setVolume(volume); // 开仓数量
        orderEntrust.setTradedVolume(BigDecimal.ZERO); // 已交易数量
        orderEntrust.setTradedPrice(BigDecimal.ZERO); // 成交价格
        orderEntrust.setPrincipalUnit("USDT"); // 保证金单位
        orderEntrust.setPrincipalAmount(tradeAmount.subtract(openFee)); // 保证金数量 = 开仓金额-手续费
        orderEntrust.setCreateTime(System.currentTimeMillis()); // 开仓时间
        orderEntrust.setType(orderType.equals(ContractOrderType.MARKET_PRICE.getCode()) ? ContractOrderType.MARKET_PRICE : ContractOrderType.SPOT_LIMIT); // 0：市价 1：限价 2：计划委托
        orderEntrust.setTriggerPrice(BigDecimal.ZERO); // 触发价
        orderEntrust.setEntrustPrice(entrustPrice); // 委托价格
        orderEntrust.setEntrustType(ContractOrderEntrustType.OPEN); // 开仓或者加仓
        orderEntrust.setTriggeringTime(null); // 触发时间，暂时无效
        orderEntrust.setShareNumber(contractCoin.getShareNumber());
        orderEntrust.setProfitAndLoss(BigDecimal.ZERO); // 盈亏（仅平仓计算）
        orderEntrust.setPatterns(ContractOrderPattern.FIXED); // 仓位模式,默认都是逐仓
        orderEntrust.setOpenFee(openFee); // 开仓手续费
        orderEntrust.setCloseFee(openFee); //该笔费用的平仓手续费
        orderEntrust.setStatus(ContractOrderEntrustStatus.ENTRUST_ING); // 委托状态：委托中
        orderEntrust.setCurrentPrice(currentPrice);
        orderEntrust.setIsBlast(0); // 不是爆仓单
        if (orderType.equals(ContractOrderType.SPOT_LIMIT.getCode())) { // 是否是计划委托单
            orderEntrust.setIsFromSpot(1);
        } else {
            orderEntrust.setIsFromSpot(0);
        }
        orderEntrust.setValue(tradeAmount); // 下单金额
        orderEntrust.setLeverage(leverage); // 杠杆倍数
        // 保存委托单
        int saveResult = contractOrderEntrustService.saveOpenOrderAndFreezeBalance(orderEntrust, wallet);

        if (saveResult > 0) {
            // 发送消息到撮合引擎处理订单
            kafkaTemplate.send("super-swap-order-open", JSON.toJSONString(orderEntrust));
            log.info(">>>>>>>>>>订单提交完成>>>>>>>>>>");
            // 返回结果
            // MessageResult result = MessageResult.success(msService.getMessage("SUCCESSFULLY_ORDERED"));
            return AjaxResult.success("successfully ordered", orderEntrust);
        } else {
            // 返回结果
            // MessageResult result = MessageResult.error(msService.getMessage("FAILED_ORDERED"));
            return AjaxResult.error("Order failed");
        }

    }


    /**
     * 超级杠杆下单（加仓）
     * 二种操作类型：买入做多，卖出做空
     *
     * @return
     */
    @PostMapping("add")
    @ApiOperation("超级杠杆下单（加仓）")
    public AjaxResult openOrder(@RequestBody AddOrderDTO dto) {

        Long memberId = dto.getMemberId(); // 会员Id
        BigDecimal tradeAmount = dto.getTradeAmount(); // 前端输入的购买金额
        Long walletId = dto.getWalletId();


        // 输入合法性检查
        if (tradeAmount == null || walletId == null || memberId == null) {
            // return MessageResult.error(500, msService.getMessage("ILLEGAL_ARGUMENT"));
            return AjaxResult.error("illegal argument1");
        }

        // 单笔开仓金额需要大于10且小于50000
        if (tradeAmount.compareTo(BigDecimal.valueOf(10)) < 0 || tradeAmount.compareTo(BigDecimal.valueOf(50000)) > 0) {
            return AjaxResult.error("illegal argument5");
        }

        // 检查用户合法性
        Member member = memberService.getMemberInfo(memberId);
        if (member == null) {
            //return MessageResult.error(500, msService.getMessage("USER_PLACED_ORDER_NOT_EXIST"));
            return AjaxResult.error("The user who placed the order does not exist");
        }

        // 检查用户余额钱包是否存在
        MemberBusinessWallet memberBusinessWallet = memberWalletService.getMemberBusinessWalletByMemberId(memberId);
        if (memberBusinessWallet == null) {
            // return MessageResult.error(500,  msService.getMessage("WALLET_NOT_EXIST"));
            return AjaxResult.error("Transfer to or from wallet does not exist");
        }

        // 检查用户持仓钱包是否存在
        MemberSuperContractWallet wallet = memberContractWalletService.getById(dto.getWalletId());
        if (wallet == null) {
            // return MessageResult.error(500,  msService.getMessage("WALLET_NOT_EXIST"));
            return AjaxResult.error("Transfer to or from wallet does not exist");
        }

        // 检查用户余额是否足够
        if (tradeAmount.compareTo(memberBusinessWallet.getMoney()) > 0) {
            // return MessageResult.error(500, msService.getMessage("ACCOUNT_INSUFFICIENT_BALANCE"));
            return AjaxResult.error("The contract account has insufficient funds");
        }

        // 委托单Id
        // 检查交易对是否存在
        SuperContractCoin contractCoin = superContractCoinService.getById(wallet.getContractId());
        if (contractCoin == null) {
            // return MessageResult.error(500, msService.getMessage("NONSUPPORT_COIN"));
            return AjaxResult.error("nonsupport coin!");
        }

        //当前价格
        BigDecimal currentPrice = contractCoinMatchFactory.getContractCoinMatch(contractCoin.getSymbol()).getNowPrice();
        // System.out.println("current price is :"+dto.toString()+","+currentPrice);
        if (currentPrice.compareTo(BigDecimal.ZERO) <= 0) {
            return AjaxResult.error("The contract matching engine is abnormal, please place the order later2");
        }

        // 计算开仓手续费率（需要根据杠杆倍数动态查询）
        BigDecimal openFee = BigDecimal.ZERO;


        // 新建合约委托单
        SuperContractOrderEntrust orderEntrust = new SuperContractOrderEntrust();

        orderEntrust.setContractId(wallet.getContractId()); // 合约ID
        orderEntrust.setMemberId(memberId); // 用户ID
        orderEntrust.setSymbol(contractCoin.getSymbol()); // 交易对符号
        orderEntrust.setBaseSymbol(contractCoin.getSymbol().split("/")[1]); // 基币/结算币
        orderEntrust.setCoinSymbol(contractCoin.getSymbol().split("/")[0]); // 币种符号
        orderEntrust.setDirection(wallet.getUsdtBuyPosition().compareTo(BigDecimal.ZERO) > 0 ? ContractOrderDirection.BUY : ContractOrderDirection.SELL); // 开仓方向：0-做多/1-做空
        orderEntrust.setContractOrderEntrustId(wallet.getEntrustOrder()); //订单号
        orderEntrust.setVolume(BigDecimal.ZERO); // 加仓时数量为0
        orderEntrust.setTradedVolume(BigDecimal.ZERO); // 已交易数量
        orderEntrust.setTradedPrice(BigDecimal.ZERO); // 成交价格
        orderEntrust.setPrincipalUnit("USDT"); // 保证金单位
        orderEntrust.setPrincipalAmount(tradeAmount.subtract(openFee)); // 保证金数量 = 开仓金额-手续费
        orderEntrust.setCreateTime(System.currentTimeMillis()); // 开仓时间
        orderEntrust.setType(ContractOrderType.MARKET_PRICE); // 0：市价 1：限价 2：计划委托
        orderEntrust.setTriggerPrice(BigDecimal.ZERO); // 触发价
        orderEntrust.setEntrustPrice(currentPrice); // 委托价格
        orderEntrust.setEntrustType(ContractOrderEntrustType.ADD); // 加仓
        orderEntrust.setTriggeringTime(null); // 触发时间，暂时无效
        orderEntrust.setProfitAndLoss(BigDecimal.ZERO); // 盈亏（仅平仓计算）
        orderEntrust.setPatterns(ContractOrderPattern.FIXED); // 仓位模式,默认都是逐仓
        orderEntrust.setStatus(ContractOrderEntrustStatus.ENTRUST_ING); // 委托状态：委托中
        orderEntrust.setCurrentPrice(currentPrice);
        orderEntrust.setIsBlast(0); // 不是爆仓单
        orderEntrust.setIsFromSpot(0);
        orderEntrust.setValue(tradeAmount); // 下单金额
        orderEntrust.setLeverage(wallet.getUsdtBuyLeverage()); // 杠杆倍数

        // 保存委托单
        int saveResult = contractOrderEntrustService.saveOpenOrderAndFreezeBalance(orderEntrust, wallet);

        if (saveResult > 0) {
            // 发送消息到撮合引擎处理订单
            kafkaTemplate.send("super-swap-order-add1", JSON.toJSONString(orderEntrust));
            // 返回结果
            // MessageResult result = MessageResult.success(msService.getMessage("SUCCESSFULLY_ORDERED"));
            return AjaxResult.success("successfully ordered", orderEntrust);
        } else {
            // 返回结果
            // MessageResult result = MessageResult.error(msService.getMessage("FAILED_ORDERED"));
            return AjaxResult.error("Order failed");
        }

    }

    /**
     * 超级杠杆平仓和减仓
     *
     * @return
     */
    @PostMapping("close")
    @ApiOperation("超级杠杆平仓和减仓")
    public AjaxResult closeOrder(@RequestBody CloseOrderDTO dto) {

        Long walletId = dto.getWalletId(); // 持仓Id
        BigDecimal volume = dto.getVolume(); // 平仓数量

        // 输入合法性检查
        if (walletId == null || volume == null) {
            // return MessageResult.error(500, msService.getMessage("ILLEGAL_ARGUMENT"));
            return AjaxResult.error("illegal argument");
        }

        // 获取超级杠杆持仓账户
        MemberSuperContractWallet memberContractWallet = memberContractWalletService.getById(walletId);
        if (memberContractWallet == null) {
            // return MessageResult.error(500, msService.getMessage("NONSUPPORT_COIN"));
            return AjaxResult.error("nonsupport coin!");
        }

        // 检查合约是否存在
        SuperContractCoin contractCoin = superContractCoinService.getById(memberContractWallet.getContractId());
        if (contractCoin == null) {
            // return MessageResult.error(500, msService.getMessage("NO_CONTRACT_ACCOUNT"));
            return AjaxResult.error("Contract does not exist");
        }

        // 检查用户合法性
        Member member = memberService.getMemberInfo(memberContractWallet.getMemberId());
        if (member == null) {
            //return MessageResult.error(500, msService.getMessage("USER_PLACED_ORDER_NOT_EXIST"));
            return AjaxResult.error("The user who placed the order does not exist");
        }


        // 平仓数量小于0
        if (volume.compareTo(BigDecimal.ZERO) < 0) {
            //return MessageResult.error(500, msService.getMessage("NUMBER_CLOSED_POSITIONS_NOT_COMPLY_REGULATIONS"));//小于1张
            return AjaxResult.error("The number of closed positions does not comply with the regulations");
        }

        // 账户持仓
        BigDecimal usdtBuyPosition = memberContractWallet.getUsdtBuyPosition();

        // 判断平仓方向
        ContractOrderDirection direction;
        if (usdtBuyPosition.compareTo(BigDecimal.ZERO) > 0) {
            direction = ContractOrderDirection.BUY;
        } else if (usdtBuyPosition.compareTo(BigDecimal.ZERO) < 0) {
            direction = ContractOrderDirection.SELL;
        } else {
            //return MessageResult.error(500, msService.getMessage("ORDER_AMOUNT_GREATER_AMOUNT_CLOSED"));
            return AjaxResult.error("The order amount is greater than the amount that can be closed");
        }

        // 检查仓位是否足够
        if (memberContractWallet.getUsdtBuyPosition().abs().compareTo(volume.abs()) < 0) {
            // return MessageResult.error(500, msService.getMessage("ORDER_AMOUNT_GREATER_AMOUNT_CLOSED"));
            return AjaxResult.error("The order amount is greater than the amount that can be closed");
        }

        // 检查合约引擎是否存在
        if (!contractCoinMatchFactory.containsContractCoinMatch(contractCoin.getSymbol())) {
            // return MessageResult.error(500, msService.getMessage("CONTRACT_ENGINE_ABNORMAL"));
            return AjaxResult.error("The contract matching engine is abnormal, please place the order later");
        }

        // 当前市价
        BigDecimal currentPrice = contractCoinMatchFactory.getContractCoinMatch(contractCoin.getSymbol()).getNowPrice();

        // 计算平仓手续费(平仓数量*当前价格*平仓手续费率）
        SuperCoinFee coinFee = coinFeeService.getFee(memberContractWallet.getUsdtBuyLeverage().toBigInteger().intValue());
        BigDecimal closeFeeRate = coinFee.getRate();
        BigDecimal closeFee = volume.multiply(currentPrice).multiply(closeFeeRate);

        // 判断是平仓还是减仓
        ContractOrderEntrustType orderType;
        if (memberContractWallet.getUsdtBuyPosition().abs().compareTo(volume.abs()) > 0) {
            orderType = ContractOrderEntrustType.SUB;
        } else {
            orderType = ContractOrderEntrustType.CLOSE;
        }

        // 新建合约委托单
        SuperContractOrderEntrust orderEntrust = new SuperContractOrderEntrust();
        orderEntrust.setContractId(contractCoin.getId()); // 合约ID
        orderEntrust.setMemberId(memberContractWallet.getMemberId()); // 用户ID
        orderEntrust.setSymbol(contractCoin.getSymbol()); // 交易对符号
        orderEntrust.setBaseSymbol(contractCoin.getSymbol().split("/")[1]); // 基币/结算币
        orderEntrust.setCoinSymbol(contractCoin.getSymbol().split("/")[0]); // 币种符号
        orderEntrust.setDirection(direction); // 平仓方向：平空/平多
        orderEntrust.setContractOrderEntrustId(memberContractWallet.getEntrustOrder());
        orderEntrust.setVolume(volume); // 平仓数量
        orderEntrust.setTradedVolume(BigDecimal.ZERO); // 已交易数量
        orderEntrust.setTradedPrice(BigDecimal.ZERO); // 成交价格
        orderEntrust.setPrincipalUnit("USDT"); // 保证金单位
        orderEntrust.setPrincipalAmount(BigDecimal.ZERO); // 保证金数量
        orderEntrust.setCreateTime(System.currentTimeMillis()); // 平仓时间
        orderEntrust.setType(ContractOrderType.MARKET_PRICE);
        orderEntrust.setTriggerPrice(currentPrice); // 触发价
        orderEntrust.setEntrustPrice(currentPrice); // 委托价格
        orderEntrust.setEntrustType(orderType); // 平仓
        orderEntrust.setTriggeringTime(0L); // 触发时间，暂时无效
        orderEntrust.setShareNumber(contractCoin.getShareNumber());
        orderEntrust.setProfitAndLoss(BigDecimal.ZERO); // 盈亏（仅平仓计算）
        orderEntrust.setPatterns(ContractOrderPattern.FIXED); // 仓位模式,默认都是逐仓
        orderEntrust.setCloseFee(closeFee); // 平仓手续费
        orderEntrust.setCurrentPrice(currentPrice); // 当前市价
        orderEntrust.setIsBlast(0); // 不是爆仓单
        orderEntrust.setIsFromSpot(0); // 不是计划委托单
        orderEntrust.setCloseOrderType(ContractOrderEntrustCloseType.CLOSE_MEMBER); // 用户平仓
        // 保存委托单
        orderEntrust.setStatus(ContractOrderEntrustStatus.ENTRUST_ING); // 委托状态：委托中
        boolean saveResult = contractOrderEntrustService.save(orderEntrust);

        if (saveResult) {
            // 发送消息至Exchange系统
            kafkaTemplate.send("super-swap-order-close25", JSON.toJSONString(orderEntrust));


//            //通知钱包变更
//            JSONObject jsonObj = new JSONObject();
//            jsonObj.put("symbol", contractCoin.getSymbol());
//            jsonObj.put("walletId", memberContractWallet.getId());
//            kafkaTemplate.send("super-member-wallet-change", JSON.toJSONString(jsonObj));

            log.info(">>>>>>>>>>订单提交完成>>>>>>>>>>");
            // 返回结果
            // MessageResult result = MessageResult.success(msService.getMessage("SUCCESSFULLY_ORDERED"));
            return AjaxResult.success("successfully ordered", orderEntrust);
        } else {
            // 返回结果
            // MessageResult result = MessageResult.error(msService.getMessage("FAILED_ORDERED"))
            return AjaxResult.success("Order failed", null);
        }
    }

    /**
     * 一键平仓（市价全平）
     *
     * @param dto
     * @return
     */
    @PostMapping("close-all")
    @ApiOperation("一键平仓（市价全平）")
    public AjaxResult closeAll(@RequestBody CloseAllOrderDTO dto) {
        Long memberId = dto.getMemberId(); // 会员Id
        // 判断会员Id是否为空
        if (memberId == null) {
            // return MessageResult.error(500, msService.getMessage("ILLEGAL_ARGUMENT"));
            return AjaxResult.error("illegal argument");
        }
        // 检查会员合法性
        Member member = memberService.getMemberInfo(memberId);
        if (member == null) {
            //return MessageResult.error(500, msService.getMessage("USER_PLACED_ORDER_NOT_EXIST"));
            return AjaxResult.error("The user who placed the order does not exist");
        }

        // 查询会员所有持仓
        List<MemberSuperContractWallet> allMemberContractWallet = memberContractWalletService.findAllByMemberId(memberId);
        for (MemberSuperContractWallet wallet : allMemberContractWallet) {
            // 持仓量
            BigDecimal usdtBuyPosition = wallet.getUsdtBuyPosition();
            // 获取交易对
            SuperContractCoin contractCoin = superContractCoinService.getById(wallet.getContractId());
            // 获取当前市价
            BigDecimal currentPrice = contractCoinMatchFactory.getContractCoinMatch(contractCoin.getSymbol()).getNowPrice();
            // 判断是做多还是做空
            ContractOrderDirection direction;
            // 持仓数量
            BigDecimal volume;
            if (usdtBuyPosition.compareTo(BigDecimal.ZERO) > 0) {
                direction = ContractOrderDirection.BUY;
                volume = usdtBuyPosition;
            } else {
                volume = usdtBuyPosition.negate();
                direction = ContractOrderDirection.SELL;
            }
            // 计算平仓手续费(平仓数量*当前价格*平仓手续费率）
            SuperCoinFee coinFee = coinFeeService.getFee(wallet.getUsdtBuyLeverage().toBigInteger().intValue());
            BigDecimal closeFeeRate = coinFee.getRate();
            BigDecimal closeFee = volume.multiply(currentPrice).multiply(closeFeeRate);

            // 新建合约委托单
            SuperContractOrderEntrust orderEntrust = new SuperContractOrderEntrust();
            orderEntrust.setContractId(contractCoin.getId()); // 合约ID
            orderEntrust.setMemberId(memberId); // 用户ID
            orderEntrust.setSymbol(contractCoin.getSymbol()); // 交易对符号
            orderEntrust.setBaseSymbol(contractCoin.getSymbol().split("/")[1]); // 基币/结算币
            orderEntrust.setCoinSymbol(contractCoin.getSymbol().split("/")[0]); // 币种符号
            orderEntrust.setDirection(direction); // 平仓方向：平空/平多
            orderEntrust.setContractOrderEntrustId(wallet.getEntrustOrder());
            orderEntrust.setVolume(volume); // 平仓数量
            orderEntrust.setTradedVolume(BigDecimal.ZERO); // 已交易数量
            orderEntrust.setTradedPrice(BigDecimal.ZERO); // 成交价格
            orderEntrust.setPrincipalUnit("USDT"); // 保证金单位
            orderEntrust.setPrincipalAmount(BigDecimal.ZERO); // 保证金数量
            orderEntrust.setCreateTime(System.currentTimeMillis()); // 平仓时间
            orderEntrust.setType(ContractOrderType.MARKET_PRICE); // 市价平仓
            orderEntrust.setTriggerPrice(currentPrice); // 触发价
            orderEntrust.setEntrustPrice(currentPrice); // 委托价格
            orderEntrust.setEntrustType(ContractOrderEntrustType.CLOSE); // 平仓
            orderEntrust.setTriggeringTime(null); // 触发时间，暂时无效
            orderEntrust.setShareNumber(contractCoin.getShareNumber());
            orderEntrust.setProfitAndLoss(BigDecimal.ZERO); // 盈亏（仅平仓计算）
            orderEntrust.setPatterns(ContractOrderPattern.FIXED); // 仓位模式,默认都是逐仓
            orderEntrust.setCloseFee(closeFee); // 平仓手续费
            orderEntrust.setCurrentPrice(currentPrice); // 当前市价
            orderEntrust.setIsBlast(0); // 不是爆仓单
            orderEntrust.setIsFromSpot(0); // 不是计划委托单
            orderEntrust.setStatus(ContractOrderEntrustStatus.ENTRUST_ING); // 委托状态：委托中
            orderEntrust.setCloseOrderType(ContractOrderEntrustCloseType.CLOSE_MEMBER); // 用户平仓
            // 保存委托单
            contractOrderEntrustService.save(orderEntrust);

            // 发送消息至Exchange系统
            kafkaTemplate.send("super-swap-order-close25", JSON.toJSONString(orderEntrust));
            log.info("发送消息至kafka---当前时间是{},数据是{}", DateUtil.getDateTime(), JSON.toJSONString(orderEntrust));


//            //通知钱包变更
//            JSONObject jsonObj = new JSONObject();
//            jsonObj.put("symbol", contractCoin.getSymbol());
//            jsonObj.put("walletId", wallet.getId());
//            kafkaTemplate.send("super-member-wallet-change", JSON.toJSONString(jsonObj));

        }
        // MessageResult result = MessageResult.success(msService.getMessage("SUCCESSFULLY_ORDERED"));
        return AjaxResult.success("successfully ordered", null);

    }

    /**
     * 修改止盈止损价
     *
     * @return
     */
    @PostMapping("updateStopProfitAndLossPrice")
    @ApiOperation("修改止盈止损价")
    public AjaxResult positionList(@RequestBody UpdateStopProfitAndLossPriceDTO dto) {

        Long walletId = dto.getWalletId();
        if (walletId == null) {
            // return MessageResult.error(500, msService.getMessage("ILLEGAL_ARGUMENT"));
            return AjaxResult.error("illegal argument");
        }
        MemberSuperContractWallet memberContractWallet = memberContractWalletService.getById(walletId);
        if (memberContractWallet == null) {
            // return MessageResult.error(500,  msService.getMessage("WALLET_NOT_EXIST"));
            return AjaxResult.error("Transfer to or from wallet does not exist");
        }
        // 买、卖
        ContractOrderDirection direction;
        if (memberContractWallet.getUsdtBuyPosition().compareTo(BigDecimal.ZERO) > 0) {
            direction = ContractOrderDirection.BUY;
        } else {
            direction = ContractOrderDirection.SELL;
        }

        SuperContractCoin contractCoin = superContractCoinService.getById(memberContractWallet.getContractId());

        // 构建合约委托单
        SuperContractOrderEntrust orderEntrust = new SuperContractOrderEntrust();
        orderEntrust.setContractId(memberContractWallet.getContractId()); // 合约ID
        orderEntrust.setMemberId(memberContractWallet.getMemberId()); // 用户ID
        orderEntrust.setSymbol(contractCoin.getSymbol()); // 交易对符号
        orderEntrust.setBaseSymbol(contractCoin.getSymbol().split("/")[1]); // 基币/结算币
        orderEntrust.setCoinSymbol(contractCoin.getSymbol().split("/")[0]); // 币种符号
        orderEntrust.setDirection(direction); // 开仓方向：0-做多/1-做空
        orderEntrust.setContractOrderEntrustId(memberContractWallet.getEntrustOrder()); //订单号
        orderEntrust.setVolume(BigDecimal.ZERO); // 开仓数量
        orderEntrust.setTradedVolume(BigDecimal.ZERO); // 已交易数量
        orderEntrust.setTradedPrice(BigDecimal.ZERO); // 成交价格
        orderEntrust.setPrincipalUnit("USDT"); // 保证金单位
        orderEntrust.setPrincipalAmount(BigDecimal.ZERO); // 保证金数量 = 开仓金额-手续费
        orderEntrust.setCreateTime(System.currentTimeMillis()); // 开仓时间
        orderEntrust.setType(null); // 0：市价 1：限价 2：计划委托
        orderEntrust.setTriggerPrice(BigDecimal.ZERO); // 触发价
        orderEntrust.setEntrustPrice(BigDecimal.ZERO); // 委托价格
        orderEntrust.setEntrustType(ContractOrderEntrustType.STOP_PROFIT); // 修改止盈止损
        orderEntrust.setTriggeringTime(null); // 触发时间，暂时无效
        orderEntrust.setShareNumber(BigDecimal.ZERO);
        orderEntrust.setProfitAndLoss(BigDecimal.ZERO); // 盈亏（仅平仓计算）
        orderEntrust.setPatterns(ContractOrderPattern.FIXED); // 仓位模式,默认都是逐仓
        orderEntrust.setOpenFee(BigDecimal.ZERO); // 开仓手续费
        orderEntrust.setStatus(ContractOrderEntrustStatus.ENTRUST_ING); // 委托状态：委托中
        orderEntrust.setCurrentPrice(BigDecimal.ZERO);
        orderEntrust.setIsBlast(0); // 不是爆仓单
        orderEntrust.setIsFromSpot(0);
        orderEntrust.setValue(BigDecimal.ZERO); // 下单金额
        // 计算止盈止损价
        BigDecimal stopProfitPrice = null;
        BigDecimal stopLossPrice = null;
        if (dto.getStopProfitPrice() != null && dto.getStopProfitPrice().compareTo(BigDecimal.ZERO) > 0) {
            stopProfitPrice = dto.getStopProfitPrice();
        }
        if (dto.getStopLossPrice() != null && dto.getStopLossPrice().compareTo(BigDecimal.ZERO) > 0) {
            stopLossPrice = dto.getStopLossPrice();
        }
        if (dto.getStopProfitPercentage() != null && dto.getStopProfitPercentage().compareTo(BigDecimal.ZERO) > 0) {
            stopProfitPrice = calcStopProfitPriceByPercentage(dto.getStopProfitPercentage(), memberContractWallet.getUsdtBuyPrice(), memberContractWallet.getUsdtBuyPrincipalAmount(), memberContractWallet.getUsdtBuyPosition());
        }
        if (dto.getStopLossPercentage() != null && dto.getStopLossPercentage().compareTo(BigDecimal.ZERO) > 0) {
            stopLossPrice = calcStopLossPriceByPercentage(dto.getStopLossPercentage(), memberContractWallet.getUsdtBuyPrice(), memberContractWallet.getUsdtBuyPrincipalAmount(), memberContractWallet.getUsdtBuyPosition());
        }
        if (dto.getStopProfitAmount() != null && dto.getStopProfitAmount().compareTo(BigDecimal.ZERO) > 0) {
            stopProfitPrice = calcStopProfitPriceByAmount(dto.getStopProfitAmount(), memberContractWallet.getUsdtBuyPrice(), memberContractWallet.getUsdtBuyPosition());
        }
        if (dto.getStopProfitAmount() != null && dto.getStopProfitAmount().compareTo(BigDecimal.ZERO) > 0) {
            stopLossPrice = calcStopLossPriceByAmount(dto.getStopLossAmount(), memberContractWallet.getUsdtBuyPrice(), memberContractWallet.getUsdtBuyPosition());
        }
        orderEntrust.setStopProfitPrice(stopProfitPrice); // 止盈价
        orderEntrust.setStopLossPrice(stopLossPrice); // 止损价


        // 发送消息至Exchange系统
        kafkaTemplate.send("super-swap-order-set-stop", JSON.toJSONString(orderEntrust));

        return AjaxResult.success();
    }


    /**
     * 获取当前持仓列表
     *
     * @param memberId
     * @return
     */
    @GetMapping("position-list")
    @ApiOperation("获取当前持仓列表")
    @ApiImplicitParams({@ApiImplicitParam(name = "memberId", value = "会员Id", dataType = "long", paramType = "query", example = "1"),})
    public AjaxResult positionList(Long memberId) {
//         判断是否存在该会员
        Member member = memberService.getMemberInfo(memberId);
        if (member == null) {
            //return MessageResult.error(500, msService.getMessage("USER_PLACED_ORDER_NOT_EXIST"));
            return AjaxResult.error("The user who placed the order does not exist");
        }
        // 持仓
        List<MemberSuperContractWallet> list = memberContractWalletService.findAllByMemberId(memberId);
        if (!IterUtil.isEmpty(list)) {
            // return MessageResult.error(500, msService.getMessage("NO_CONTRACT_ACCOUNT"));
//            return AjaxResult.error("Contract does not exist");

            // 计算账户权益
            for (MemberSuperContractWallet wallet : list) {
                SuperContractCoin contractCoin = superContractCoinService.getById(wallet.getContractId());
                BigDecimal currentPrice = contractCoinMatchFactory.getContractCoinMatch(contractCoin.getSymbol()).getNowPrice();

                // 计算金本位权益（多仓 + 空仓）
                BigDecimal usdtTotalProfitAndLoss = BigDecimal.ZERO;
                // 多仓计算方法：当前持仓收益=（当前市价-开仓均价）* 持仓数量
                if (wallet.getUsdtBuyPosition().compareTo(BigDecimal.ZERO) > 0) {
                    usdtTotalProfitAndLoss = (currentPrice.subtract(wallet.getUsdtBuyPrice())).multiply(wallet.getUsdtBuyPosition()).setScale(4, BigDecimal.ROUND_DOWN);
                }

                // 空仓计算方法：当前持仓收益=（开仓均价-当前市价）*持仓数量
                if (wallet.getUsdtBuyPosition().compareTo(BigDecimal.ZERO) < 0) {
                    BigDecimal usdtBuyPosition = wallet.getUsdtBuyPosition().abs();
                    usdtTotalProfitAndLoss = (wallet.getUsdtBuyPrice().subtract(currentPrice)).multiply(usdtBuyPosition).setScale(4, BigDecimal.ROUND_DOWN);
                }

                // 计算持仓收益率：当前持仓收益/持仓本金*100%
                String usdtTotalProfitAndLossRate = "0.00%";
                if (usdtTotalProfitAndLoss.compareTo(BigDecimal.ZERO) != 0) {
                    DecimalFormat df = new DecimalFormat("0.00%");
                    // 多仓
                    if (wallet.getUsdtBuyPosition().compareTo(BigDecimal.ZERO) > 0) {
                        usdtTotalProfitAndLossRate = df.format(usdtTotalProfitAndLoss.divide(wallet.getUsdtBuyPrincipalAmount(), 8, BigDecimal.ROUND_DOWN));
                    }
                    // 空仓
                    if (wallet.getUsdtBuyPosition().compareTo(BigDecimal.ZERO) < 0) {
                        usdtTotalProfitAndLossRate = df.format(usdtTotalProfitAndLoss.divide(wallet.getUsdtBuyPrincipalAmount(), 8, BigDecimal.ROUND_DOWN));
                    }
                }

                // 设置交易对
                wallet.setSymbol(contractCoin.getSymbol());
                // 设置当前市价
                wallet.setCurrentPrice(currentPrice);
                // 当前持仓收益
                wallet.setUsdtTotalProfitAndLoss(usdtTotalProfitAndLoss);
                // 当前持仓收益率
                wallet.setUsdtTotalProfitAndLossRate(usdtTotalProfitAndLossRate);

            }
        }
        return AjaxResult.success(list);
    }


    /**
     * 获取当前委托列表
     *
     * @param memberId       会员Id
     * @param contractCoinId 合约交易对Id
     * @param type           类型 1-限价委托，2-计划委托
     * @param pageNo         当前分页编号
     * @param pageSize       分页大小
     * @return
     */
    @GetMapping("currentEntrustList")
    @ApiOperation("获取当前委托列表")
    @ApiImplicitParams({@ApiImplicitParam(name = "memberId", value = "会员Id", dataType = "long", paramType = "query", example = "1"), @ApiImplicitParam(name = "contractCoinId", value = "合约交易币Id", dataType = "long", paramType = "query", example = "1"), @ApiImplicitParam(name = "type", value = "类型 1-限价委托，2-计划委托", dataType = "string", paramType = "query", example = "1"), @ApiImplicitParam(name = "pageNo", value = "当前分页编号", dataType = "int", paramType = "query", example = "1"), @ApiImplicitParam(name = "pageSize", value = "分页大小", dataType = "int", paramType = "query", example = "1"),})
    public AjaxResult entrustList(@RequestParam(value = "memberId") Long memberId, @RequestParam(value = "contractCoinId",required = false) Long contractCoinId, @RequestParam(value = "type",required = false) Integer type, @RequestParam(value = "pageNo") int pageNo, @RequestParam(value = "pageSize") int pageSize) {
        Page<SuperContractOrderEntrust> contractOrderEntrustOrders = contractOrderEntrustService.queryPageEntrustingOrdersBySymbol(memberId, contractCoinId, type, pageNo, pageSize);
        return AjaxResult.success(contractOrderEntrustOrders);
    }


    /**
     * 查询历史委托列表(分页)
     *
     * @param memberId       会员Id
     * @param type           类型 1-限价委托，2-计划委托
     * @param contractCoinId 交易对Id
     * @param direction      买（0）/卖（1）
     * @param days           查询日 1-今天 7-7天 30-30天 90-90天
     * @param pageNo         当前分页编号
     * @param pageSize       分页大小
     * @return
     */
    @GetMapping("historyEntrustList")
    @ApiOperation("获取历史委托列表")
    @ApiResponse(code = 200, message = "successful", response = ContractOrderEntrust.class)
    @ApiImplicitParams({@ApiImplicitParam(name = "memberId", value = "会员Id", paramType = "query", required = true), @ApiImplicitParam(name = "type", value = "订单类型 1-限价委托，2-计划委托", paramType = "query", required = true), @ApiImplicitParam(name = "contractCoinId", value = "交易对Id", paramType = "query"), @ApiImplicitParam(name = "direction", value = "0-买，1-卖", paramType = "query"), @ApiImplicitParam(name = "days", value = "查询日 1-今天 7-7天 30-30天 90-90天", paramType = "query"), @ApiImplicitParam(name = "pageNo", value = "当前页", paramType = "query", required = true), @ApiImplicitParam(name = "pageSize", value = "分页大小", paramType = "query", required = true),})
    public AjaxResult queryPageEntrustHistoryOrdersBySymbol(@RequestParam(value = "memberId") Long memberId, @RequestParam(value = "type") String type, @RequestParam(value = "contractCoinId", required = false) Long contractCoinId, @RequestParam(value = "direction", required = false) Integer direction, @RequestParam(value = "days", required = false) Integer days, @RequestParam(value = "pageNo") int pageNo, @RequestParam(value = "pageSize") int pageSize) {

        LambdaQueryWrapper<SuperContractOrderEntrust> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SuperContractOrderEntrust::getMemberId, memberId);
        queryWrapper.eq(SuperContractOrderEntrust::getType, type); // 类型 1-限价委托，2-计划委托
        queryWrapper.ne(SuperContractOrderEntrust::getStatus, ContractOrderEntrustStatus.ENTRUST_ING); // 不是委托中的
        queryWrapper.and(wrapper -> {
            wrapper.eq(SuperContractOrderEntrust::getEntrustType, ContractOrderEntrustType.OPEN.getCode()).or().eq(SuperContractOrderEntrust::getEntrustType, ContractOrderEntrustType.ADD.getCode()); // 开仓和加仓
        });
        if (contractCoinId != null) {
            queryWrapper.eq(SuperContractOrderEntrust::getContractId, contractCoinId);
        }
        if (direction != null) {
            queryWrapper.eq(SuperContractOrderEntrust::getDirection, direction);
        }
        if (days != null && days > 0) {
            // 当前时间
            Date currDate = new Date();
            // 结束时间（次日零点）
            Date endDate = DateUtils.dayStart(DateUtils.addDays(currDate, 1));
            // 开始时间
            Date startDate = DateUtils.addDays(endDate, -days);
            queryWrapper.lt(SuperContractOrderEntrust::getCreateTime, DateUtil.dateToString(endDate));
            queryWrapper.ge(SuperContractOrderEntrust::getCreateTime, DateUtil.dateToString(startDate));
        }
        queryWrapper.orderByDesc(SuperContractOrderEntrust::getCreateTime);
        Page<SuperContractOrderEntrust> page = new Page<>(pageNo, pageSize);
        Page<SuperContractOrderEntrust> contractOrderEntrustPage = contractOrderEntrustService.page(page, queryWrapper);
        return AjaxResult.success(contractOrderEntrustPage);
    }


    /**
     * 查询历史平仓记录列表(分页)
     *
     * @param memberId       会员Id
     * @param contractCoinId 交易对Id
     * @param direction      买（0）/卖（1）
     * @param days           查询日 1-今天 7-7天 30-30天 90-90天
     * @param pageNo         当前分页编号
     * @param pageSize       分页大小
     * @return
     */
    @GetMapping("historyCloseEntrustList")
    @ApiOperation("查询历史平仓记录列表")
    @ApiImplicitParams({@ApiImplicitParam(name = "memberId", value = "会员Id", paramType = "query", required = true), @ApiImplicitParam(name = "contractCoinId", value = "交易对Id", paramType = "query"), @ApiImplicitParam(name = "direction", value = "0-买，1-卖", paramType = "query"), @ApiImplicitParam(name = "days", value = "查询日 1-今天 7-7天 30-30天 90-90天", paramType = "query"), @ApiImplicitParam(name = "pageNo", value = "当前页", paramType = "query", required = true), @ApiImplicitParam(name = "pageSize", value = "分页大小", paramType = "query", required = true),})
    public AjaxResult queryPageHistoryCloseOrdersBySymbol(@RequestParam(value = "memberId") Long memberId, @RequestParam(value = "contractCoinId", required = false) Long contractCoinId, @RequestParam(value = "direction", required = false) Integer direction, @RequestParam(value = "days", required = false) Integer days, @RequestParam(value = "pageNo") int pageNo, @RequestParam(value = "pageSize") int pageSize) {

        LambdaQueryWrapper<SuperContractOrderEntrust> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SuperContractOrderEntrust::getMemberId, memberId);
        queryWrapper.eq(SuperContractOrderEntrust::getStatus, ContractOrderEntrustStatus.ENTRUST_SUCCESS); // 平仓成功
        queryWrapper.and(wrapper -> {
            wrapper.eq(SuperContractOrderEntrust::getEntrustType, ContractOrderEntrustType.CLOSE.getCode()).or().eq(SuperContractOrderEntrust::getEntrustType, ContractOrderEntrustType.SUB.getCode()); // 平仓或减仓
        });

        if (contractCoinId != null) {
            queryWrapper.eq(SuperContractOrderEntrust::getContractId, contractCoinId);
        }
        if (direction != null) {
            queryWrapper.eq(SuperContractOrderEntrust::getDirection, direction);
        }
        if (days != null && days > 0) {
            // 当前时间
            Date currDate = new Date();
            // 结束时间（次日零点）
            Date endDate = DateUtils.dayStart(DateUtils.addDays(currDate, 1));
            // 开始时间
            Date startDate = DateUtils.addDays(endDate, -days);
            queryWrapper.lt(SuperContractOrderEntrust::getCreateTime, endDate.getTime());
            queryWrapper.ge(SuperContractOrderEntrust::getCreateTime, startDate.getTime());
        }
        queryWrapper.orderByDesc(SuperContractOrderEntrust::getCreateTime);
        Page<SuperContractOrderEntrust> page = new Page<>(pageNo, pageSize);
        Page<SuperContractOrderEntrust> contractOrderEntrustPage = contractOrderEntrustService.page(page, queryWrapper);
        return AjaxResult.success(contractOrderEntrustPage);
    }

    /**
     * 撤销单个合约委托单
     *
     * @param dto
     * @return
     */
    @PostMapping("cancel")
    @ApiOperation("撤销单个合约委托单")
    public AjaxResult cancelEntrustOrder(@RequestBody CancelOrderDTO dto) {

        Long entrustId = dto.getEntrustId(); // 委托单Id
        Long memberId = dto.getMemberId(); // 会员Id
        if (entrustId == null || memberId == null) {
            return AjaxResult.error("illegal argument");
        }
        // 判断会员
        Member member = memberService.getMemberInfo(memberId);
        if (member == null) {
            //return MessageResult.error(500, msService.getMessage("USER_PLACED_ORDER_NOT_EXIST"));
            return AjaxResult.error("The user who placed the order does not exist");
        }


        SuperContractOrderEntrust entrustOrder = contractOrderEntrustService.getById(entrustId);
        if (entrustOrder == null) {
            // return MessageResult.error(500, msService.getMessage("DELEGATE_NOT_EXIST"));
            return AjaxResult.error("Delegate does not exist");
        }
        if (entrustOrder.getStatus() != ContractOrderEntrustStatus.ENTRUST_ING) {
            // return MessageResult.error(500, msService.getMessage("DELEGATE_ERROR"));
            return AjaxResult.error("Delegate status error");
        }

        // 发送消息至Exchange系统
        kafkaTemplate.send("super-swap-order-cancel", JSON.toJSONString(entrustOrder));

        log.info(">>>>>>>>>>订单提交完成>>>>>>>>>>");
        // 返回结果
        // MessageResult result = MessageResult.success(msService.getMessage("CANCELLATION_SUCCEEDED"));
        return AjaxResult.success("Cancellation is successful", entrustOrder);
    }

    /**
     * 撤销所有合约委托单(撤销所有委托，限价+计划+市价)
     *
     * @return
     */
    @PostMapping("cancel-all")
    @ApiOperation("撤销所有合约委托单")
    public AjaxResult cancelAllOrder(@RequestBody CloseAllOrderDTO closeAllOrderDTO) {
        List<SuperContractOrderEntrust> orderList = contractOrderEntrustService.findAllByMemberIdAndContractId(closeAllOrderDTO.getMemberId());
        if (orderList != null && orderList.size() > 0) {
            for (SuperContractOrderEntrust entrustOrder : orderList) {
                // 发送消息至Exchange系统
                kafkaTemplate.send("super-swap-order-cancel", JSON.toJSONString(entrustOrder));
            }
        }
        log.info(">>>>>>>>>>撤销所有委托成功>>>>>>>>>>");
        // 返回结果
        // MessageResult result = MessageResult.success(msService.getMessage("CANCELLATION_SUCCEEDED"));
        return AjaxResult.success("Cancellation is successful", null);
    }

    /**
     * 持仓详情操作记录
     *
     * @param memberId 会员Id
     * @param wallteId 合约钱包Id
     * @param pageNo   当前分页编号
     * @param pageSize 分页大小
     * @return
     */
    @GetMapping("entrustRecord")
    @ApiOperation("持仓详情操作记录")
    @ApiImplicitParams({@ApiImplicitParam(name = "memberId", value = "会员Id", paramType = "query", required = true), @ApiImplicitParam(name = "wallteId", value = "合约钱包Id", paramType = "query"), @ApiImplicitParam(name = "pageNo", value = "当前分页", paramType = "query"), @ApiImplicitParam(name = "pageSize", value = "分页大小", paramType = "query")})
    public AjaxResult entrustRecord(@RequestParam(value = "memberId") Long memberId, @RequestParam(value = "wallteId") Long wallteId, @RequestParam(value = "pageNo") int pageNo, @RequestParam(value = "pageSize") int pageSize) {
        MemberSuperContractWallet memberContractWallet = memberContractWalletService.getById(wallteId);
        LambdaQueryWrapper<SuperContractOrderEntrust> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SuperContractOrderEntrust::getMemberId, memberId);
        queryWrapper.eq(SuperContractOrderEntrust::getContractId, memberContractWallet.getContractId());
        queryWrapper.orderByDesc(SuperContractOrderEntrust::getCreateTime);
        Page<SuperContractOrderEntrust> page = new Page<>(pageNo, pageSize);
        return AjaxResult.success(contractOrderEntrustService.page(page, queryWrapper));
    }

    /**
     * 通过百分比计算止盈价
     *
     * @param stopProfitPercentage   止盈百分比
     * @param usdtBuyPrice           均价
     * @param usdtBuyPrincipalAmount 保证金
     * @param usdtBuyPosition        持仓数量
     * @return
     */
    public BigDecimal calcStopProfitPriceByPercentage(BigDecimal stopProfitPercentage, BigDecimal usdtBuyPrice, BigDecimal usdtBuyPrincipalAmount, BigDecimal usdtBuyPosition) {
        // Map<String, BigDecimal> map = new HashMap<>();
        // 开多仓
        if (usdtBuyPosition.compareTo(BigDecimal.ZERO) > 0) {
            // 止盈价
            BigDecimal stopProfitPrice = stopProfitPercentage.multiply(usdtBuyPrincipalAmount).divide(usdtBuyPosition, 8, BigDecimal.ROUND_DOWN).add(usdtBuyPrice);
            // 止盈额
            BigDecimal stopProfitAmount = (stopProfitPrice.subtract(usdtBuyPrice)).multiply(usdtBuyPosition);
            return stopProfitPrice;
        }
        // 开空仓
        if (usdtBuyPosition.compareTo(BigDecimal.ZERO) < 0) {
            // 取绝对值
            usdtBuyPosition = usdtBuyPosition.abs();
            // 止盈价
            BigDecimal stopProfitPrice = usdtBuyPrice.subtract(stopProfitPercentage.multiply(usdtBuyPrincipalAmount).divide(usdtBuyPosition, 8, BigDecimal.ROUND_DOWN));
            // 止盈额
            BigDecimal stopProfitAmount = (usdtBuyPrice.subtract(stopProfitPrice)).multiply(usdtBuyPosition);
            return stopProfitPrice;
        }
        return BigDecimal.ZERO;
    }

    /**
     * 通过百分比计算止损价
     *
     * @param stopLossPercentage     止损百分比
     * @param usdtBuyPrice           均价
     * @param usdtBuyPrincipalAmount 保证金
     * @param usdtBuyPosition        持仓数量
     * @return
     */
    public BigDecimal calcStopLossPriceByPercentage(BigDecimal stopLossPercentage, BigDecimal usdtBuyPrice, BigDecimal usdtBuyPrincipalAmount, BigDecimal usdtBuyPosition) {
        // 开多仓
        if (usdtBuyPosition.compareTo(BigDecimal.ZERO) > 0) {
            // 止损价
            BigDecimal stopLossPrice = usdtBuyPrice.subtract((stopLossPercentage.multiply(usdtBuyPrincipalAmount).divide(usdtBuyPosition, 8, BigDecimal.ROUND_DOWN)));
            // 止损额
            BigDecimal stopLossAmount = (usdtBuyPrice.subtract(stopLossPrice)).multiply(usdtBuyPosition);
            return stopLossPrice;
        }
        // 开空仓
        if (usdtBuyPosition.compareTo(BigDecimal.ZERO) < 0) {
            // 取绝对值
            usdtBuyPosition = usdtBuyPosition.abs();
            // 止损价
            BigDecimal stopLossPrice = usdtBuyPrice.add((stopLossPercentage.multiply(usdtBuyPrincipalAmount).divide(usdtBuyPosition, 8, BigDecimal.ROUND_DOWN)));
            // 止损额
            BigDecimal stopLossAmount = (stopLossPrice.subtract(usdtBuyPrice)).multiply(usdtBuyPosition);
            return stopLossPrice;
        }
        return BigDecimal.ZERO;
    }

    /**
     * 通过止盈额计算止盈价
     *
     * @param stopProfitAmoount 止盈额
     * @param usdtBuyPrice      均价
     * @param usdtBuyPosition   持仓数量
     * @return
     */
    public BigDecimal calcStopProfitPriceByAmount(BigDecimal stopProfitAmoount, BigDecimal usdtBuyPrice, BigDecimal usdtBuyPosition) {
        Map<String, BigDecimal> map = new HashMap<>();
        // 开多仓
        if (usdtBuyPosition.compareTo(BigDecimal.ZERO) > 0) {
            // 止盈价 = 止盈额/持仓数量+购买均价
            BigDecimal stopProfitPrice = stopProfitAmoount.divide(usdtBuyPosition, 8, RoundingMode.DOWN).add(usdtBuyPrice);
            //map.put("stopProfitPrice", stopProfitPrice);
            return stopProfitPrice;
        }
        // 开空仓
        if (usdtBuyPosition.compareTo(BigDecimal.ZERO) < 0) {
            // 取绝对值
            usdtBuyPosition = usdtBuyPosition.abs();

            // 止盈价
            BigDecimal stopProfitPrice = usdtBuyPrice.subtract(stopProfitAmoount.divide(usdtBuyPosition, 8, BigDecimal.ROUND_DOWN));
            //map.put("stopProfitPrice", stopProfitPrice);
            return stopProfitPrice;
        }
        return BigDecimal.ZERO;
    }

    /**
     * 通过止损额计算止损价
     *
     * @param stopLossAmount  止损额
     * @param usdtBuyPrice    均价
     * @param usdtBuyPosition 持仓数量
     * @return
     */
    public BigDecimal calcStopLossPriceByAmount(BigDecimal stopLossAmount, BigDecimal usdtBuyPrice, BigDecimal usdtBuyPosition) {
        Map<String, BigDecimal> map = new HashMap<>();
        // 开多仓
        if (usdtBuyPosition.compareTo(BigDecimal.ZERO) > 0) {
            // 止损价
            BigDecimal stopLossPrice = usdtBuyPrice.subtract(stopLossAmount.divide(usdtBuyPosition, 8, BigDecimal.ROUND_DOWN));
            map.put("stopLossPrice", stopLossPrice);
            return stopLossPrice;
        }
        // 开空仓
        if (usdtBuyPosition.compareTo(BigDecimal.ZERO) < 0) {
            // 取绝对值
            usdtBuyPosition = usdtBuyPosition.abs();
            // 止损价
            BigDecimal stopLossPrice = stopLossAmount.divide(usdtBuyPosition, 8, BigDecimal.ROUND_DOWN).add(usdtBuyPrice);
            map.put("stopLossPrice", stopLossPrice);
            return stopLossPrice;
        }
        return BigDecimal.ZERO;
    }


}
