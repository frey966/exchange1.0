package com.financia.swap;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 会员打码量记录
 */
@Data
public class MemberRechargeRecord {

    private int id;
    private long memberId;//会员id
    private BigDecimal rechargeAmount;//充值金额
    private Integer rechargeType;//充值类型MemberRechargeRecordTypeEnum
    private Date rechargeTime;//充值时间
    private  BigDecimal betScale;//打码量比例
    private BigDecimal remainingRequiredBetAmount;//剩余打码量
    private BigDecimal amountCanBeWithdrawn;//可提现金额=总余额-剩余打码量
    private BigDecimal rechargeRatio;//打码量(正数为充值打码量，负数为扣除打码量) = 充值金额 * 打码比例
    private BigDecimal totalBalance;//总余额
    private Long rechargeTime2TimeStamp;
}
