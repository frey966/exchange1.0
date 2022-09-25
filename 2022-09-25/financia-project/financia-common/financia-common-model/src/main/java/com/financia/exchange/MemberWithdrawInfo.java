package com.financia.exchange;

import com.baomidou.mybatisplus.annotation.TableName;
import com.financia.common.core.annotation.Excel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 公共-会员提现汇总对象 member_withdraw_info
 * 
 * @author 花生
 * @date 2022-08-11
 */
@Data
@TableName("member_withdraw_info")
@ApiModel(value="公共-会员提现汇总表",description = "公共-会员提现汇总表")
public class MemberWithdrawInfo implements Serializable
{
    private static final long serialVersionUID = 1L;

    /** 主键 */
    private Long id;

    /** 会员id */
    @Excel(name = "会员id")
    @ApiModelProperty(value = "会员id",required = false)
    private Long memberId;

    /** 平台转账地址 */
    @Excel(name = "平台转账地址")
    @ApiModelProperty(value = "平台转账地址",required = false)
    private String fromAddress;

    /** 会员收款地址 */
    @Excel(name = "会员收款地址")
    @ApiModelProperty(value = "会员收款地址",required = false)
    private String toAddress;

    /** 累计转账次数 */
    @Excel(name = "累计转账次数")
    @ApiModelProperty(value = "累计转账次数",required = false)
    private Long tradeCount;

    /** 累计转账金额 */
    @Excel(name = "累计转账金额")
    @ApiModelProperty(value = "累计转账金额",required = false)
    private String sumWithdrawMoney;

    /** 1:btc链 比特币 2:eth 链（以太坊）3:trx链（波场） */
    @Excel(name = "1:btc链 比特币 2:eth 链", readConverterExp = "以=太坊")
    @ApiModelProperty(value = "1:btc链 比特币 2:eth 链",required = false)
    private Long chainId;

    /** 链名称 */
    @Excel(name = "链名称")
    @ApiModelProperty(value = "链名称",required = false)
    private String chainName;

    /** 0:删除 1：未删除 */
    @Excel(name = "0:删除 1：未删除")
    @ApiModelProperty(value = "0:删除 1：未删除",required = false)
    private Long status;

    @ApiModelProperty(value = "修改时间",required = false)
    private String createTime;
    @ApiModelProperty(value = "修改时间",required = false)
    private String updateTime;




}
