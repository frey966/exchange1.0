package com.financia.exchange;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.financia.common.core.annotation.Excel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 基础业务-会员法币充值记录对象 member_national_currency_recharge_order
 * 
 * @author 花生
 * @date 2022-09-18
 */
@Data
@TableName("member_national_currency_recharge_order")
@ApiModel(value = "会员管理-法币充值记录", description = "会员管理-法币充值记录")
public class MemberNationalCurrencyRechargeOrder implements Serializable
{
    private static final long serialVersionUID = 1L;

    /** 主键id */
    @ApiModelProperty(value = "主键id",required = false)
    private Long id;

    /** 会员id */
    @Excel(name = "会员id")
    @ApiModelProperty(value = "会员id",required = false)
    private String memberId;

    /** 收款类型1银行卡 */
    @Excel(name = "收款类型1银行卡")
    @ApiModelProperty(value = "收款类型1银行卡",required = false)
    private Long type;

    /** 收款卡号 */
    @Excel(name = "收款卡号")
    @ApiModelProperty(value = "收款卡号",required = false)
    private String collectionNumber;

    /** 收款姓名 */
    @Excel(name = "收款姓名")
    @ApiModelProperty(value = "收款姓名",required = false)
    private String collectionName;

    /** 收款支行 */
    @Excel(name = "收款支行")
    @ApiModelProperty(value = "收款支行",required = false)
    private String bankName;

    /** 付款卡号 */
    @Excel(name = "付款卡号")
    @ApiModelProperty(value = "付款卡号",required = false)
    private String payNumber;

    /** 付款人 */
    @Excel(name = "付款人")
    @ApiModelProperty(value = "付款人",required = false)
    private String payName;

    /** 订单 */
    @Excel(name = "订单")
    @ApiModelProperty(value = "订单",required = false)
    private String orderNo;

    /** 金额 */
    @Excel(name = "金额")
    @ApiModelProperty(value = "金额",required = false)
    private BigDecimal money;

    /** 法币币种 */
    @Excel(name = "法币币种")
    @ApiModelProperty(value = "法币币种",required = false)
    private String coinId;

    /** 充值状态，0未成功,1:成功 */
    @Excel(name = "充值状态，1成功，-1:失败 0:未开始 2:充值中")
    @ApiModelProperty(value = "充值状态，1成功，-1:失败 0:未开始 2:充值中",required = false)
    private Long rechargeStatus;

    /** 转账状态：1成功，-1:失败 0:未开始 2:转账中 */
    @Excel(name = "转账状态：1成功，-1:失败 0:未开始 2:转账中")
    @ApiModelProperty(value = "转账状态：1成功，-1:失败 0:未开始 2:转账中",required = false)
    private Long orderStatus;

    @ApiModelProperty(value = "备注",required = false)
    private String remark;
    /** 创建时间 */
    @ApiModelProperty(value = "创建时间",required = false)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    /** 更新时间 */
    @ApiModelProperty(value = "更新时间",required = false)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;
    @ApiModelProperty(value = "更新者",required = false)
    private String updateBy;

    @ApiModelProperty(value = "币种符号",required = false)
    private String exchangeCoinName;
    @ApiModelProperty(value = "会员名称",required = false)
    private String username;

    @ApiModelProperty(value = "审核状态：0审核中1通过2拒绝",required = false)
    private String checkStatus;

    @ApiModelProperty(value = "法币中文名字",required = false)
    private String exchangeCoinZhName;

    @ApiModelProperty(value = "法币英文名字",required = false)
    private String exchangeCoinEnName;

    @ApiModelProperty(value = "bi币种log",required = false)
    private String exchangeCoinAppLogo;
}
