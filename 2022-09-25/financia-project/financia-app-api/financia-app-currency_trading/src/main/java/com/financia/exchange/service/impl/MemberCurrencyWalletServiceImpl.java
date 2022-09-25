package com.financia.exchange.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.financia.common.core.enums.BusinessSubType;
import com.financia.currency.ExchangeOrder;
import com.financia.currency.MemberCryptoCurrencyWallet;
import com.financia.exchange.MemberBusinessWallet;
import com.financia.exchange.mapper.MemberCurrencyWalletMapper;
import com.financia.exchange.service.MemberBusinessWalletService;
import com.financia.exchange.service.MemberCurrencyWalletService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;


@Service
public class MemberCurrencyWalletServiceImpl extends ServiceImpl<MemberCurrencyWalletMapper, MemberCryptoCurrencyWallet> implements MemberCurrencyWalletService {

    @Autowired
    private MemberBusinessWalletService memberBusinessWalletService;

    @Override
    public MemberCryptoCurrencyWallet findByCoinIdAndMemberId(Long memberId, Long coinId) {
        return baseMapper.findByCoinIdAndMemberId(memberId, coinId);
    }

    @Override
    public int freezeBalance(Long memberWalletId, BigDecimal amount) {
        int ret = baseMapper.freezeBalance(memberWalletId, amount);
        return ret;
    }

    @Override
    public int unfreezeBalance(Long memberWalletId, BigDecimal amount) {
        int ret = baseMapper.unfreezeBalance(memberWalletId, amount);
        return ret;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public int increaseBalanceByBuy(Long memberCurrencyWalletId, ExchangeOrder order) {
        MemberBusinessWallet memberBusinessWallet = memberBusinessWalletService.getMemberBusinessWalletByMemberId(order.getMemberId());
        // 扣减主钱包冻结余额
        int ret = memberBusinessWalletService.decreaseFreezeBalance(memberBusinessWallet.getId(), order.getAmount(), memberBusinessWallet.getMoney(), order.getOrderNo(), BusinessSubType.BB_BUY_SUCCESS,"");
        if(ret == 0 ) {
            return ret;
        }
        // 增加币币钱包余额
        baseMapper.increaseBalance(memberCurrencyWalletId, order.getTurnover());
        return ret;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public int decreaseFreezeBalanceBySell(Integer memberBusinessWalletId, Long memberCurrencyWalletId, ExchangeOrder order) {

        // 扣减币币钱包冻结余额
        int ret = baseMapper.decreaseFreezeBalance(memberCurrencyWalletId, order.getTurnover());
        if(ret == 0 ) {
            return ret;
        }
        // 增加主钱包余额
        MemberBusinessWallet memberBusinessWallet = memberBusinessWalletService.getMemberBusinessWalletByMemberId(order.getMemberId());
        memberBusinessWalletService.increaseBalance(memberBusinessWalletId, order.getTradedAmount(), order.getTradedAmount().add(memberBusinessWallet.getMoney()), order.getOrderNo(), BusinessSubType.BB_SELL_SUCCESS, "");
        return ret;
    }

    @Override
    public Page<MemberCryptoCurrencyWallet> findByMemberIdAndNotContainCoinId(Long memberId, Long coinId, Integer pageNo, Integer pageSize) {
        Page<MemberCryptoCurrencyWallet> page = new Page<>(pageNo, pageSize);
        return baseMapper.findByMemberIdAndNotContainCoinId(page, memberId, coinId);
    }

    @Override
    public List<MemberCryptoCurrencyWallet> findByMemberId(Long memberId) {
        return baseMapper.findByMemberId(memberId);
    }
}
