package com.financia.exchange;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @desc 会员交易记录，包括充值、提现、转账等
 *
 */
@Data
@TableName("member_transaction")
@ApiModel(value = "MemberTransaction", description = "会员交易记录")
public class MemberTransaction {

    @TableId(type = IdType.AUTO)
    @ApiModelProperty("交易Id")
    private Long id;

    @ApiModelProperty("会员Id")
    private Long memberId;
    /**
     * 交易金额
     */
    @ApiModelProperty("交易金额")
    private BigDecimal amount;

    @TableField(exist = false)
    @ApiModelProperty("交易金额字符")
    private String amountStr;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @ApiModelProperty("创建时间")
    private Date createTime;

    /**
     * 交易类型
     */
    @ApiModelProperty("交易类型")
    private TransactionType type;

    /**
     * 账户类型
     */
    @ApiModelProperty("账户类型")
    private AccountWalletType accountWalletType;
    /**
     * 币种名称，如 BTC
     */
    @ApiModelProperty("币种名称，如 BTC")
    private String symbol;
    /**
     * 充值或提现地址、或转账地址
     */
    @ApiModelProperty("充值或提现地址、或转账地址")
    private String address;

    /**
     * 交易手续费
     * 提现和转账才有手续费，充值没有;如果是法币交易，只收发布广告的那一方的手续费
     */
    @ApiModelProperty("交易手续费")
    private BigDecimal fee = BigDecimal.ZERO ;

    /**
     * 标识位，特殊情况会用到，默认为0
     */
    @ApiModelProperty("标识位，特殊情况会用到，默认为0")
    private int flag = 0;
    /**
     * 实收手续费
     */
    @ApiModelProperty("实收手续费")
    private String realFee ;
    /**
     * 折扣手续费
     */
    @ApiModelProperty("折扣手续费")
    private String discountFee ;

//    /**
//     * 状态  0 进行中  1已完成 2 失败
//     */
//    private int status ;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    //挂单时间
    @TableField(exist = false)
    @ApiModelProperty("挂单时间")
    private Long createTime2TimeStamp;
    /**
     * 交易金额类型
     */
    @ApiModelProperty("交易金额类型")
    private TransactionAmountTypeEnum amountType;
    /**
     * 订单号
     */
    @ApiModelProperty("订单号")
    private String orderNumber;
}
