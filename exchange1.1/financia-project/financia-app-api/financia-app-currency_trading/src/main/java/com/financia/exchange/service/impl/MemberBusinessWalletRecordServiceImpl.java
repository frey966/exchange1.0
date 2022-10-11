package com.financia.exchange.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.financia.common.core.enums.BusinessSubType;
import com.financia.common.core.enums.TradeType;
import com.financia.exchange.MemberBusinessWalletRecord;
import com.financia.exchange.mapper.MemberBusinessWalletRecordMapper;
import com.financia.exchange.service.MemberBusinessWalletRecordService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;


@Service("memberBusinessWalletRecordService")
public class MemberBusinessWalletRecordServiceImpl extends ServiceImpl<MemberBusinessWalletRecordMapper, MemberBusinessWalletRecord> implements MemberBusinessWalletRecordService {


    /**
     * 添加钱包操作流水
     * @param businessWalletId
     * @param money
     * @param tradeType
     * @param businessId
     * @param type
     * @param remark
     * @return
     */
    @Override
    public boolean saveRecord(Integer businessWalletId, BigDecimal money,BigDecimal balance,
                              TradeType tradeType, String businessId, BusinessSubType type, String remark){
        MemberBusinessWalletRecord memberBusinessWalletRecord = new MemberBusinessWalletRecord();
        memberBusinessWalletRecord.setBalance(balance);
        memberBusinessWalletRecord.setMoney(money);
        memberBusinessWalletRecord.setBusinessWalletId(businessWalletId);
        memberBusinessWalletRecord.setBusinessId(businessId);
        memberBusinessWalletRecord.setRemark(remark);
        memberBusinessWalletRecord.setBusinessType(type.getParentTypeCode());
        memberBusinessWalletRecord.setBusinessSubType(type.getCode());
        memberBusinessWalletRecord.setTradeType(tradeType.getCode());
        memberBusinessWalletRecord.setCreateTime(new Date());
        return save(memberBusinessWalletRecord);
    }
}
