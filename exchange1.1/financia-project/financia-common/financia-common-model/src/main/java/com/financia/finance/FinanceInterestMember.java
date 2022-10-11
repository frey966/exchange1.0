package com.financia.finance;

import com.baomidou.mybatisplus.annotation.IdType;
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
 * 量化理财-会员存款利息对象 finance_interest_member
 *
 * @author 花生
 * @date 2022-08-15
 */
@Data
@TableName("finance_interest_member")
@ApiModel(value="量化理财-会员存款利息",description = "量化理财-会员存款利息")
public class FinanceInterestMember implements Serializable
{
    private static final long serialVersionUID = 1L;

    /** 主键 */
    @TableId(type = IdType.AUTO)
    private Long id;

    /** 量化理财项目ID */
    @Excel(name = "量化理财项目ID")
    @ApiModelProperty(value = "量化理财项目ID",required = false)
    private Long financeId;

    /** 编号 */
    @Excel(name = "编号")
    @ApiModelProperty(value = "编号",required = false)
    private Long dateInterestNo;

    /** 会员ID */
    @Excel(name = "会员ID")
    @ApiModelProperty(value = "会员ID",required = false)
    private Long memberId;

    @Excel(name = "持仓ID")
    @ApiModelProperty(value = "持仓ID",required = false)
    private Long depositId;

    @ApiModelProperty(value = "本金（如果有复利情况，就是每日利息加本金）",required = false)
    private BigDecimal principal;

    /** 每日利息 */
    @Excel(name = "每日利息")
    @ApiModelProperty(value = "每日利息",required = false)
    private BigDecimal everydayInterest;

    /** 每日利率 */
    @Excel(name = "每日利率")
    @ApiModelProperty(value = "每日利率",required = false)
    private BigDecimal everydayRate;

    /** YYYY-MM-DD */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "YYYY-MM-DD", width = 30, dateFormat = "yyyy-MM-dd")
    @ApiModelProperty(value = "日期",required = false)
    private String timeInterest;

    @Excel(name = "利润分配客户")
    @ApiModelProperty(value = "利润分配客户",required = false)
    private BigDecimal profitCustomer;

    /** 0：删除1:有效 */
    @Excel(name = "0：删除1:有效")
    private Long state;
    /** 创建时间 */
    @Excel(name = "创建时间")
    @ApiModelProperty(value = "创建时间",required = false)
    private Date createTime;
    /** 修改时间 */
    @Excel(name = "修改时间")
    @ApiModelProperty(value = "修改时间",required = false)
    private Date updateTime;

}
