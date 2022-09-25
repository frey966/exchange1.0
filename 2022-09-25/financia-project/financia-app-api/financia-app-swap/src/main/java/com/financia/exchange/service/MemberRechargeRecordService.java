package com.financia.exchange.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.financia.swap.MemberRechargeRecord;

import java.math.BigDecimal;

public interface MemberRechargeRecordService extends IService<MemberRechargeRecord> {


    /**
     * @param memberId 会员ID
     * @param rechargeAmount 充值金额 or 投注金额
     * @param rechargeType 充值类型 MemberRechargeRecordTypeEnum
     * @return
     */
    MemberRechargeRecord saveMemberRechargeRecord(Long memberId, BigDecimal rechargeAmount, Integer rechargeType);

}
