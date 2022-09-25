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

/**
 * 量化理财-理财产品信息对象 finance_trading_project
 *
 * @author 花生
 * @date 2022-08-15
 */
@Data
@TableName("finance_trading_project")
@ApiModel(value="量化理财-量化产品",description = "量化理财-理财产品信息")
public class FinanceTradingProject implements Serializable
{
    private static final long serialVersionUID = 1L;

    /** 主键 */
    @Excel(name = "主键")
    @TableId(type = IdType.AUTO)
    @ApiModelProperty(value = "主键ID",required = false)
    private Long financeId;

    /** 项目类型 */
    @Excel(name = "项目类型")
    @ApiModelProperty(value = "项目类型",required = false)
    private Long projectType;


    @ApiModelProperty(value = "项目类型名称",required = false)
    private String projectTypeName;

    /** 图片地址 */
    @Excel(name = "图片地址")
    @ApiModelProperty(value = "图片地址",required = false)
    private String imageUrl;

    /** 募集进度 */
    @Excel(name = "募集进度")
    @ApiModelProperty(value = "募集进度",required = false)
    private String financeProgress;

    /** 项目名称-中文 */
    @Excel(name = "项目名称-中文")
    @ApiModelProperty(value = "项目名称-中文",required = false)
    private String financeZhName;

    /** 项目名称-英文 */
    @Excel(name = "项目名称-英文")
    @ApiModelProperty(value = "项目名称-英文",required = false)
    private String financeEnName;

    /** 最小募集金额 */
    @Excel(name = "最小募集金额")
    @ApiModelProperty(value = "最小募集金额",required = false)
    private String depositMinAmount;

    /** 单笔存款限额 */
    @Excel(name = "单笔存款限额")
    @ApiModelProperty(value = "单笔存款限额",required = false)
    private BigDecimal depositSingleMaxAmount;

    /** 单笔存款限额 */
    @Excel(name = "单笔存款最小限额")
    @ApiModelProperty(value = "单笔存款最小限额",required = false)
    private BigDecimal depositSingleMinAmount;

    /** 项目启动时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "项目启动时间", width = 30, dateFormat = "yyyy-MM-dd")
    @ApiModelProperty(value = "项目启动时间",required = false)
    private String depositBeginTime;

    /** 项目关闭时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "项目关闭时间", width = 30, dateFormat = "yyyy-MM-dd")
    @ApiModelProperty(value = "项目关闭时间",required = false)
    private String depositEndTime;

    /** 锁仓时间 */
    @Excel(name = "锁仓时间")
    @ApiModelProperty(value = "锁仓时间",required = false)
    private Long depositCloseDay;

    /** 取现规则 */
    @Excel(name = "取现规则")
    @ApiModelProperty(value = "取现规则",required = false)
    private String withdrawRule;
    /** 取现规则 */
    @Excel(name = "收益规则")
    @ApiModelProperty(value = "收益规则",required = false)
    private String incomeRule;

    /** 收益计算 */
    @Excel(name = "收益计算")
    @ApiModelProperty(value = "收益计算",required = false)
    private String incomeAmount;

    /** 收益到账 */
    @Excel(name = "收益到账")
    @ApiModelProperty(value = "收益到账",required = false)
    private String incomeToAccount;

    /** 收益到账方式 */
    @Excel(name = "收益到账方式")
    @ApiModelProperty(value = "收益到账方式",required = false)
    private String incomeToAccountWay;

    /** 每日利息 */
    @Excel(name = "每日利息")
    @ApiModelProperty(value = "每日利息",required = false)
    private String incomeDailyInterest;

    @Excel(name = "每日利率")
    @ApiModelProperty(value = "每日利率",required = false)
    private BigDecimal everydayRate;

    @Excel(name = "年化利率")
    @ApiModelProperty(value = "年化利率",required = false)
    private BigDecimal showRate;

    @Excel(name = "固定年化，历史年化")
    @ApiModelProperty(value = "固定年化",required = false)
    private String annualType;
    @Excel(name = "标签")
    @ApiModelProperty(value = "标签",required = false)
    private String label;

    /** 利润分配客户 */
    @Excel(name = "利润分配客户")
    @ApiModelProperty(value = "利润分配客户",required = false)
    private BigDecimal profitCustomer;

    /** 利润运营人员 */
    @Excel(name = "利润运营人员")
    @ApiModelProperty(value = "利润运营人员",required = false)
    private BigDecimal profitOperator;

    /** 0：上架 1:上架 */
    @Excel(name = "0：上架 1:上架")
    @ApiModelProperty(value = "0：上架 1:上架",required = false)
    private Long active;

    /** 内容 */
    @Excel(name = "内容")
    @ApiModelProperty(value = "内容",required = false)
    private String content;

    /** 创建时间 */
    @Excel(name = "创建时间")
    @ApiModelProperty(value = "创建时间",required = false)
    private String createTime;
    /** 修改时间 */
    @Excel(name = "修改时间")
    @ApiModelProperty(value = "修改时间",required = false)
    private String updateTime;

    /** 是否展示首页 0是 1否 */
    @Excel(name = "是否展示首页 0是 1否")
    @ApiModelProperty(value = "是否展示首页 0是 1否",required = false)
    private String ishome;
}
