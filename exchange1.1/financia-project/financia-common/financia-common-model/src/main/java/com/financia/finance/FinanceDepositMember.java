package com.financia.finance;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.financia.common.core.annotation.Excel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 量化理财-会员存款对象 finance_deposit_member
 *
 * @author 花生
 * @date 2022-08-15
 */
@Data
@TableName("finance_deposit_member")
@ApiModel(value="量化理财-会员存款",description = "量化理财-会员存款")
public class FinanceDepositMember implements Serializable
{
    private static final long serialVersionUID = 1L;

    /** 主键 */
    @TableId(type = IdType.AUTO)
    @ApiModelProperty(value = "主键",required = false)
    private Long id;

    /** 会员ID */
    @Excel(name = "会员ID")
    @ApiModelProperty(value = "会员ID",required = false)
    private Long memberId;

    /** 理财项目ID */
    @Excel(name = "理财项目ID")
    @ApiModelProperty(value = "理财项目ID",required = false)
    private Long financeId;

    /** 存款金额 */
    @Excel(name = "存款金额")
    @ApiModelProperty(value = "存款金额",required = false)
    private BigDecimal depositAmount;

    /** 存款开始时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "存款开始时间", width = 30, dateFormat = "yyyy-MM-dd")
    @ApiModelProperty(value = "存款开始时间",required = false)
    private Date depositBeginTime;

    /** 存款到期时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "存款到期时间", width = 30, dateFormat = "yyyy-MM-dd")
    @ApiModelProperty(value = "存款到期时间",required = false)
    private Date depositEndTime;

    @ApiModelProperty(value = "锁仓时间",required = false)
    private Long depositCloseDay;

    @Excel(name = "每日利率")
    @ApiModelProperty(value = "每日利率",required = false)
    private BigDecimal everydayRate;

    @Excel(name = "利润分配客户")
    @ApiModelProperty(value = "利润分配客户",required = false)
    private BigDecimal profitCustomer;

    /** 总收益 */
    @Excel(name = "总收益")
    @ApiModelProperty(value = "总收益",required = false)
    private BigDecimal incomeAmount;

    /** 总收益 */
    @Excel(name = "预估")
    @ApiModelProperty(value = "预估总收益",required = false)
    private BigDecimal estimateIncomeAmount;

    /** 0：关闭 1:启动 */
    @Excel(name = "0：关闭 1:启动")
    @ApiModelProperty(value = "0：关闭 1:启动",required = false)
    private Long active;

    @Excel(name = "结算状态0未结算，1已结算")
    @ApiModelProperty(value = "结算状态0未结算，1已结算",required = false)
    private Integer settleAccountsStatus;

    @Excel(name = "结算时间")
    @ApiModelProperty(value = "结算时间",required = false)
    private Date settleAccountsTime;

    /** 创建时间 */
    @Excel(name = "创建时间")
    @ApiModelProperty(value = "创建时间",required = false)
    private String createTime;
    /** 修改时间 */
    @Excel(name = "修改时间")
    @ApiModelProperty(value = "修改时间",required = false)
    private String updateTime;

    @Excel(name = "量化产品名称")
    @ApiModelProperty(value = "量化产品名称",required = false)
    private String financeZhName;

    @ApiModelProperty(value = "量化产品名称",required = false)
    @TableField(exist = false)
    private String financeName;

    @ApiModelProperty(value = "会员名称",required = false)
    @TableField(exist = false)
    private String username;
}
