package com.financia.business;

import com.baomidou.mybatisplus.annotation.TableName;
import com.financia.common.core.annotation.Excel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * memberBank对象 member_bank
 * 
 * @author 花生
 * @date 2022-08-17
 */
@Data
@TableName("member_bank")
@ApiModel(value="会员管理-会员收款账户",description = "会员收款账户")
public class MemberBank implements Serializable
{
    private static final long serialVersionUID = 1L;

    /** 主键id */
    @ApiModelProperty(value = "主键id",required = false,example = "1" )
    private Long id;

    /** 会员id */
    @Excel(name = "会员id")
    @ApiModelProperty(value = "会员id",required = false,example = "1" )
    private Long memberId;

    /** 法币币种id */
    @Excel(name = "法币币种id")
    @ApiModelProperty(value = "法币币种id",required = false,example = "1" )
    private Long currencyId;

    /** 收款类型：银行卡，paypel */
    @ApiModelProperty(value = "收款类型：1银行卡，2paypel",required = false,example = "1" )
    private Long type;

    /** 卡类型1、Mastercard2、Visa3、银联 */
    @Excel(name = "卡类型1、Mastercard2、Visa3、银联")
    @ApiModelProperty(value = "卡类型1、Mastercard2、Visa3、银联",required = false,example = "1" )
    private Long cardType;

    /** 卡号 */
    @Excel(name = "卡号")
    @ApiModelProperty(value = "卡号",required = false )
    private String cardNumber;

    @ApiModelProperty(value = "创建时间",required = false )
    private String createTime;

    @ApiModelProperty(value = "修改时间",required = false )
    private String updateTime;

    @ApiModelProperty(value = "法币名称",required = false )
    private String exchangeCoinName;
    @ApiModelProperty(value = "会员名称",required = false )
    private String username;

    @ApiModelProperty(value = "持卡人",required = false )
    private String cardName;

    @ApiModelProperty(value = "法币图片",required = false )
    private String exchangeCoinAppLogo;

    @ApiModelProperty(value = "收款类型图片",required = false )
    private String imgurl;

    @ApiModelProperty(value = "收款类型名称",required = false )
    private String dictLabel;

    @ApiModelProperty(value = "银行名称",required = false )
    private String bankName;
}
