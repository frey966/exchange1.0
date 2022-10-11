package com.financia.business;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.financia.common.core.annotation.Excel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 合约币种列对象 ex_contract_coin
 *
 * @author 花生
 * @date 2022-08-03
 */
@Data
@TableName("ex_contract_coin")
@ApiModel(value="交易所-合约币种列表",description = "合约币种列表")
public class ExContractCoin implements Serializable
{
    private static final long serialVersionUID = 1L;

    /** $column.columnComment */
    @TableId(type = IdType.AUTO)
    private Long id;

//    /** 基准币种小数位 */
//    @Excel(name = "基准币种小数位")
//    @ApiModelProperty(value = "基准币种小数位",required = false,example = "1" )
//    private Long baseCoinScale;

    /** 基准币种，USDT */
    @Excel(name = "基准币种，USDT")
    @ApiModelProperty(value = "基准币种",required = false,example = "1" )
    private String baseSymbol;

    /** 平仓手续费 */
    @Excel(name = "平仓手续费")
    @ApiModelProperty(value = "平仓手续费",required = false,example = "1" )
    private BigDecimal closeFee;

    /** 币种小数位 */
    @Excel(name = "币种小数位")
    @ApiModelProperty(value = "币种小数位",required = false,example = "1" )
    private Long coinScale;

    /** 币种类型 */
    @Excel(name = "币种类型")
    @ApiModelProperty(value = "币种类型",required = false,example = "1" )
    private String coinSymbol;

    /** 状态 */
    @Excel(name = "状态")
    @ApiModelProperty(value = "状态",required = false,example = "1" )
    private Long enable;

    /** 是否启用市价开仓做多 */
    @Excel(name = "是否启用市价开仓做多")
    @ApiModelProperty(value = "是否启用市价开仓做多",required = false,example = "1" )
    private Long enableMarketBuy;

    /** 是否启用市价开仓做空 */
    @Excel(name = "是否启用市价开仓做空")
    @ApiModelProperty(value = "是否启用市价开仓做空",required = false,example = "1" )
    private Long enableMarketSell;

    /** 是否允许开仓做多 */
    @Excel(name = "是否允许开仓做多")
    @ApiModelProperty(value = "是否允许开仓做多",required = false,example = "1" )
    private Long enableOpenBuy;

    /** 是否允许开仓做空 */
    @Excel(name = "是否允许开仓做空")
    @ApiModelProperty(value = "是否允许开仓做空",required = false,example = "1" )
    private Long enableOpenSell;

    /** 是否启用开仓计划委托 */
    @Excel(name = "是否启用开仓计划委托")
    @ApiModelProperty(value = "是否启用开仓计划委托",required = false,example = "1" )
    private Long enableTriggerEntrust;

    /** 是否可交易 */
    @Excel(name = "是否可交易")
    @ApiModelProperty(value = "是否可交易",required = false,example = "1" )
    private Long exchangeable;

    /** 隔夜费率 */
    @Excel(name = "隔夜费率")
    @ApiModelProperty(value = "隔夜费率",required = false,example = "1" )
    private BigDecimal feePercent;

    /** 点差类型 */
    @Excel(name = "点差类型")
    @ApiModelProperty(value = "点差类型",required = false,example = "1" )
    private Long intervalHour;

    /** 允许的杠杆倍率 */
    @Excel(name = "允许的杠杆倍率")
    @ApiModelProperty(value = "允许的杠杆倍率",required = false,example = "1" )
    private String leverage;

    /** 点差类型 */
    @Excel(name = "点差类型")
    @ApiModelProperty(value = "点差类型",required = false,example = "1" )
    private Long leverageType;

    /** 维持保证金率 */
    @Excel(name = "维持保证金率")
    @ApiModelProperty(value = "维持保证金率",required = false,example = "1" )
    private BigDecimal maintenanceMarginRate;

    /** 平多手续费 */
    @Excel(name = "平多手续费")
    @ApiModelProperty(value = "平多手续费",required = false,example = "1" )
    private BigDecimal makerFee;

    /** 最大手数 */
    @Excel(name = "最大手数")
    @ApiModelProperty(value = "最大手数",required = false,example = "1" )
    private BigDecimal maxShare;

    /** 最小手数 */
    @Excel(name = "最小手数")
    @ApiModelProperty(value = "最小手数",required = false,example = "1" )
    private BigDecimal minShare;

    /** 名字 */
    @Excel(name = "名字")
    @ApiModelProperty(value = "名字",required = false,example = "1" )
    private String name;

    /** 开仓手续费 */
    @Excel(name = "开仓手续费")
    @ApiModelProperty(value = "开仓手续费",required = false,example = "1" )
    private BigDecimal openFee;

    /** 单位手数 */
    @Excel(name = "单位手数")
    @ApiModelProperty(value = "单位手数",required = false,example = "1" )
    private BigDecimal shareNumber;

    /** 排序 */
    @Excel(name = "排序")
    @ApiModelProperty(value = "排序",required = false,example = "1" )
    private Long sort;

    /** 点差 */
    @Excel(name = "点差")
    @ApiModelProperty(value = "点差",required = false,example = "1" )
    private BigDecimal spread;

    /** 点差类型 */
    @Excel(name = "点差类型")
    @ApiModelProperty(value = "点差类型",required = false,example = "1" )
    private Long spreadType;

    /** 交易对 */
    @Excel(name = "交易对")
    @ApiModelProperty(value = "交易对",required = false,example = "1" )
    private String symbol;

    /** 平空手续费 */
    @Excel(name = "平空手续费")
    @ApiModelProperty(value = "平空手续费",required = false,example = "1" )
    private BigDecimal takerFee;

    /** 合约总平仓手续费 */
    @Excel(name = "合约总平仓手续费")
    @ApiModelProperty(value = "合约总平仓手续费",required = false,example = "1" )
    private BigDecimal totalCloseFee;

    /** 合约平台亏损 */
    @Excel(name = "合约平台亏损")
    @ApiModelProperty(value = "合约平台亏损",required = false,example = "1" )
    private BigDecimal totalLoss;

    /** 合约总开仓手续费 */
    @Excel(name = "合约总开仓手续费")
    @ApiModelProperty(value = "合约总开仓手续费",required = false,example = "1" )
    private BigDecimal totalOpenFee;

    /** 合约平台盈利 */
    @Excel(name = "合约平台盈利")
    @ApiModelProperty(value = "合约平台盈利",required = false,example = "1" )
    private BigDecimal totalProfit;

    /** 合约类型 */
    @Excel(name = "合约类型")
    @ApiModelProperty(value = "合约类型",required = false,example = "1" )
    private Long type;

    /** 0:未热门 1:热门 */
    @Excel(name = "0:未热门 1:热门")
    @ApiModelProperty(value = "合约类型",required = false,example = "1" )
    private Long popular;


    /** 前台可见状态 */
    @Excel(name = "前台可见状态")
    @ApiModelProperty(value = "前台可见状态",required = false,example = "1" )
    private Long visible;

    /** 修改时间 */
    @Excel(name = "修改时间")
    @ApiModelProperty(value = "修改时间",required = false,example = "1" )
    private String updateTime;

    /** 合约id */
    @Excel(name = "合约id")
    @ApiModelProperty(value = "合约id",required = false )
    private String contractId;
    @ApiModelProperty(value = "0:删除 1：有效",required = false )
    private String status;
}
