package com.financia.exchange.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.financia.common.core.enums.BusinessSubType;
import com.financia.common.core.enums.TradeType;
import com.financia.exchange.MemberBusinessWallet;
import com.financia.exchange.mapper.MemberBusinessWalletMapper;
import com.financia.exchange.service.MemberBusinessWalletRecordService;
import com.financia.exchange.service.MemberBusinessWalletService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;


@Service("memberBusinessWalletService")
public class MemberBusinessWalletServiceImpl extends ServiceImpl<MemberBusinessWalletMapper, MemberBusinessWallet> implements MemberBusinessWalletService {

    @Autowired
    private MemberBusinessWalletRecordService memberBusinessWalletRecordService;

    @Override
    public MemberBusinessWallet getMemberBusinessWalletByMemberId(Long memberId) {
        MemberBusinessWallet wallet = getOne(new QueryWrapper<MemberBusinessWallet>()
                .lambda()
                .eq(MemberBusinessWallet::getMemberId, memberId)
                .eq(MemberBusinessWallet::getStatus, 1));
        return wallet;
    }

    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    @Override
    public int freezeBalance(Integer memberWalletId, BigDecimal amount, BigDecimal endBalance, String busId, BusinessSubType businessSubType, String mark) {
        int ret = baseMapper.freezeBalance(memberWalletId, amount);
        if(ret > 0) {
            memberBusinessWalletRecordService.saveRecord(memberWalletId, amount, endBalance, TradeType.FREEZE, busId, businessSubType, mark);
        }
        return ret;
    }

    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    @Override
    public int unfreezeBalance(Integer memberWalletId, BigDecimal amount, BigDecimal endBalance, String busId, BusinessSubType businessSubType, String mark) {
        int ret = baseMapper.unfreezeBalance(memberWalletId, amount);
        if(ret > 0) {
            memberBusinessWalletRecordService.saveRecord(memberWalletId, amount, endBalance, TradeType.FREEZE_BACK, busId, businessSubType, mark);
        }
        return ret;
    }

    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    @Override
    public int decreaseFreezeBalance(Integer memberWalletId, BigDecimal amount, BigDecimal endBalance, String busId, BusinessSubType businessSubType, String mark) {
        int ret = baseMapper.decreaseFreezeBalance(memberWalletId, amount);
        if(ret > 0) {
            memberBusinessWalletRecordService.saveRecord(memberWalletId, amount, endBalance, TradeType.FREEZE_SUBTRACT, busId, businessSubType, mark);
        }
        return ret;
    }

    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    @Override
    public int increaseBalance(Integer memberWalletId, BigDecimal amount, BigDecimal endBalance, String busId, BusinessSubType businessSubType, String mark) {
        int ret = baseMapper.increaseBalance(memberWalletId, amount);
        if(ret > 0) {
            memberBusinessWalletRecordService.saveRecord(memberWalletId, amount, endBalance, TradeType.ADD, busId, businessSubType, mark);
        }
        return ret;
    }
}
