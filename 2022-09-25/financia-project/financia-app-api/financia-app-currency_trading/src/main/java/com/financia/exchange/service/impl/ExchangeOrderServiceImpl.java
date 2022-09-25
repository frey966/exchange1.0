package com.financia.exchange.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.financia.common.core.enums.BusinessSubType;
import com.financia.common.core.utils.DateUtils;
import com.financia.common.core.web.domain.AjaxResult;
import com.financia.currency.*;
import com.financia.exchange.MemberBusinessWallet;
import com.financia.exchange.mapper.ExchangeOrderMapper;
import com.financia.exchange.service.ExchangeOrderService;
import com.financia.exchange.service.MemberBusinessWalletService;
import com.financia.exchange.service.MemberCurrencyWalletService;
import com.financia.exchange.util.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;


@Service
public class ExchangeOrderServiceImpl extends ServiceImpl<ExchangeOrderMapper, ExchangeOrder> implements ExchangeOrderService {

    @Autowired
    private MemberCurrencyWalletService memberCurrencyWalletService;

    @Autowired
    private MemberBusinessWalletService memberBusinessWalletService;

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public AjaxResult addOrder(MemberBusinessWallet memberBusinessWallet, MemberCryptoCurrencyWallet memberCryptoCurrencyWallet, ExchangeOrder order) {

        // 买入
        if(order.getDirection() == ExchangeOrderDirection.BUY) {
            // 检查用户余额是否足够
            if(order.getAmount().compareTo(memberBusinessWallet.getMoney()) > 0) {
                // return MessageResult.error(500, msService.getMessage("INSUFFICIENT_COIN") + order.getCoinSymbol());
                return AjaxResult.error("insufficient coin:" + order.getCoinSymbol());
            }
            int ret = memberBusinessWalletService.freezeBalance(memberBusinessWallet.getId(), order.getAmount(), memberBusinessWallet.getMoney().subtract(order.getAmount()), order.getOrderNo(), BusinessSubType.BB_BUY, "");
            if(ret == 0) {
                // return MessageResult.error(500, msService.getMessage("INSUFFICIENT_COIN") + order.getCoinSymbol());
                return AjaxResult.error("insufficient coin:" + order.getCoinSymbol());
            }
        }

        // 卖出
        if (order.getDirection() == ExchangeOrderDirection.SELL) {
            // 判断余额是否充足
            if (memberCryptoCurrencyWallet.getBalanceMoney().compareTo(order.getNum()) < 0) {
                // return MessageResult.error(500, msService.getMessage("INSUFFICIENT_COIN") + order.getCoinSymbol());
                return AjaxResult.error("insufficient coin:" + order.getCoinSymbol());
            }
            // 冻结余额
            int ret = memberCurrencyWalletService.freezeBalance(memberCryptoCurrencyWallet.getId(), order.getNum());
            if(ret == 0) {
                // return MessageResult.error(500, msService.getMessage("INSUFFICIENT_COIN") + order.getCoinSymbol());
                return AjaxResult.error("insufficient coin:" + order.getCoinSymbol());
            }
        }
        // 保存订单
        int insert = baseMapper.insert(order);

        // 发送消息至Exchange系统
        kafkaTemplate.send("trade-order-buy", JSON.toJSONString(order));


        // MessageResult result = MessageResult.success(msService.getMessage("ORDER_PLACED_SUCCESS"));
        return AjaxResult.success("Order placed successfully", order);
    }

    @Override
    public Page<ExchangeOrder> findCurrent(Long memberId, Long coinId, int pageNo, int pageSize) {
        LambdaQueryWrapper<ExchangeOrder> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ExchangeOrder::getMemberId, memberId);
        if(coinId != null) {
            queryWrapper.eq(ExchangeOrder::getCoinId, coinId);
        }
        queryWrapper.eq(ExchangeOrder::getStatus, ExchangeOrderStatus.TRADING);
        queryWrapper.orderByDesc(ExchangeOrder::getCreateTime);

        Page<ExchangeOrder> page = new Page<>(pageNo, pageSize);

        return baseMapper.selectPage(page, queryWrapper);
    }

    @Override
    public Page<ExchangeOrder> findHistory(Long memberId, Long coinId, Integer direction, Integer days,  int pageNo, int pageSize) {
        LambdaQueryWrapper<ExchangeOrder> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ExchangeOrder::getMemberId, memberId);
        if(coinId != null) {
            queryWrapper.eq(ExchangeOrder::getCoinId, coinId);
        }
        queryWrapper.ne(ExchangeOrder::getStatus, ExchangeOrderStatus.TRADING);
        if(direction != null) {
            queryWrapper.eq(ExchangeOrder::getDirection, direction);
        }
        if(days != null && days>0) {
            // 当前时间
            Date currDate = new Date();
            // 结束时间（次日零点）
            Date endDate = DateUtils.dayStart(DateUtils.addDays(currDate, 1));
            // 开始时间
            Date startDate = DateUtils.addDays(endDate, -days);
            queryWrapper.lt(ExchangeOrder::getCreateTime, endDate);
            queryWrapper.ge(ExchangeOrder::getCreateTime, startDate);
        }
        queryWrapper.orderByDesc(ExchangeOrder::getCreateTime);

        Page<ExchangeOrder> page = new Page<>(pageNo, pageSize);

        return baseMapper.selectPage(page, queryWrapper);
    }

    @Override
    public Page<ExchangeOrder> findCurrencyWalletBill(Long currencyWalletId, Integer status, Integer days, int pageNo, int pageSize) {
        LambdaQueryWrapper<ExchangeOrder> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ExchangeOrder::getCurrencyWalletId, currencyWalletId);
        if(status != null) {
            queryWrapper.eq(ExchangeOrder::getStatus, status);
        }
        if(days != null && days>0) {
            // 当前时间
            Date currDate = new Date();
            // 结束时间（次日零点）
            Date endDate = DateUtils.dayStart(DateUtils.addDays(currDate, 1));
            // 开始时间
            Date startDate = DateUtils.addDays(endDate, -days);
            queryWrapper.lt(ExchangeOrder::getCreateTime, endDate);
            queryWrapper.ge(ExchangeOrder::getCreateTime, startDate);
        }
        queryWrapper.eq(ExchangeOrder::getOrderResource, ExchangeOrderResource.CUSTOMER);
        queryWrapper.orderByDesc(ExchangeOrder::getCreateTime);
        Page<ExchangeOrder> page = new Page<>(pageNo, pageSize);
        return baseMapper.selectPage(page, queryWrapper);
    }


    @Override
    public List<ExchangeOrder> loadUnmatchedOrders(Long contractId) {
        LambdaQueryWrapper<ExchangeOrder> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ExchangeOrder::getCoinId, contractId);
        queryWrapper.eq(ExchangeOrder::getStatus, ExchangeOrderStatus.TRADING);
        queryWrapper.orderByDesc(ExchangeOrder::getCreateTime);
        return baseMapper.selectList(queryWrapper);
    }

    @Transactional
    @Override
    public void cancelOrder(Long orderId) {
        int ret = 0;
        ExchangeOrder order = baseMapper.selectById(orderId);
        if(order.getDirection() == ExchangeOrderDirection.BUY) {
            // 解冻主钱包余额
            MemberBusinessWallet memberBusinessWallet = memberBusinessWalletService.getMemberBusinessWalletByMemberId(order.getMemberId());
            ret = memberBusinessWalletService.unfreezeBalance(memberBusinessWallet.getId(), order.getAmount(), memberBusinessWallet.getMoney().add(order.getAmount()), order.getOrderNo(), BusinessSubType.BB_BUY_CANCEL, "");
        } else {
            Long currencyWalletId = order.getCurrencyWalletId();
            // 解冻币币钱包
            ret = memberCurrencyWalletService.unfreezeBalance(currencyWalletId, order.getNum());
        }
        if(ret > 0) {
            // 取消订单
            UpdateWrapper<ExchangeOrder> updateChainWrapper=new UpdateWrapper<>();
            updateChainWrapper.eq("id",orderId);
            updateChainWrapper.set("status",ExchangeOrderStatus.CANCELED.getCode());
            updateChainWrapper.set("canceled_time", DateUtil.getTimeMillis());
            baseMapper.update(null,updateChainWrapper);
        }
    }
}
