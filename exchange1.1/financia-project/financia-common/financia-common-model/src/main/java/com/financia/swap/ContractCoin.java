package com.financia.swap;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.financia.core.BooleanEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
@TableName("ex_contract_coin")
@ApiModel(value="ContractCoin",description = "合约交易币")
public class ContractCoin {

    @TableId(type = IdType.AUTO)
    @ApiModelProperty(value = "合约ID")
    private Long id;

    @ApiModelProperty(value = "合约名称（如：BTC/USDT永续合约）")
    private String name;

    @ApiModelProperty(value = "交易币种名称，格式：BTC/USDT")
    private String symbol;

    @ApiModelProperty(value = "交易币种符号，如BTC")
    private String coinSymbol;

    @ApiModelProperty(value = "结算币种符号，如USDT")
    private String baseSymbol;

    @ApiModelProperty(value = "排序，从小到大")
    private int sort;

    @ApiModelProperty(value = "交易币小数精度")
    private int coinScale;

    @ApiModelProperty(value = "基币小数精度")
    private int baseCoinScale;

    @ApiModelProperty(value = "0金本位合约 1币本位合约")
    private ContractType type;

    @ApiModelProperty(value = "状态：1启用 2禁止")
    private int enable;

    @ApiModelProperty(value = "前台可见状态，1：可见，2：不可见")
    private int visible;

    @ApiModelProperty(value = "//是否可交易，1：可交易，2：不可交易")
    private int exchangeable;

    @ApiModelProperty(value = "是否允许开仓做空")
    private BooleanEnum enableOpenSell = BooleanEnum.IS_TRUE;

    @ApiModelProperty(value = "是否允许开仓做多")
    private BooleanEnum enableOpenBuy = BooleanEnum.IS_TRUE;

    @ApiModelProperty(value = "是否启用市价开仓做空")
    private BooleanEnum enableMarketSell = BooleanEnum.IS_TRUE;

    @ApiModelProperty(value = "是否启用市价开仓做多")
    private BooleanEnum enableMarketBuy = BooleanEnum.IS_TRUE;

    @ApiModelProperty(value = "是否启用开仓计划委托")
    private BooleanEnum enableTriggerEntrust = BooleanEnum.IS_TRUE;

    @ApiModelProperty(value = "点差类型 1百分比 2固定额")
    private Integer spreadType;

    @ApiModelProperty(value = "点差(滑点）")
    private BigDecimal spread;

    @ApiModelProperty(value = "倍数类型 1: 分离倍数（20,30,50,100），只能选择指定倍数  2: 连续倍数（20,100)")
    private int leverageType;

    @ApiModelProperty(value = "杠杆倍数(10,20,30,50,100)")
    private String leverage;

    @ApiModelProperty(value = "合约面值（如 1手 = 100USDT）")
    private BigDecimal shareNumber;

    @ApiModelProperty(value = "最小手数")
    private BigDecimal minShare;

    @ApiModelProperty(value = "最大手数")
    private BigDecimal maxShare;

    @ApiModelProperty(value = "资金费用结算时间间隔，小时数")
    private Integer intervalHour;

    @ApiModelProperty(value = "资金费率(默认千分之一)")
    private BigDecimal feePercent;

    @ApiModelProperty(value = "维持保证金率（低于该比例将触发强制平仓，默认0.5%）")
    private BigDecimal maintenanceMarginRate;

    @ApiModelProperty(value = "开仓手续费(默认千分之一)")
    private BigDecimal openFee;

    @ApiModelProperty(value = "平仓手续费(默认千分之一)")
    private BigDecimal closeFee;

    @ApiModelProperty(value = "买入手续费（买入平空）(默认千分之一)")
    private BigDecimal takerFee;

    @ApiModelProperty(value = "卖出手续费（卖出平多）(默认千分之一)")
    private BigDecimal makerFee;

    @ApiModelProperty(value = "合约平台盈利")
    private BigDecimal totalProfit;

    @ApiModelProperty(value = "合约平台亏损")
    private BigDecimal totalLoss;

    @ApiModelProperty(value = "合约总开仓手续费")
    private BigDecimal totalOpenFee;

    @ApiModelProperty(value = "合约总平仓手续费")
    private BigDecimal totalCloseFee;

    @ApiModelProperty(value = "0:未热门 1:热门")
    private Integer popular;

    /**
     * 服务器当前时间戳
     */
    @TableField(exist = false)
    @ApiModelProperty(value = "服务器当前时间戳")
    private Long currentTime = System.currentTimeMillis();

    /**
     * 当前价格
     */
    @TableField(exist = false)
    @ApiModelProperty(value = "当前价格")
    private BigDecimal currentPrice;

    /**
     * usdt价格汇率
     */
    @TableField(exist = false)
    @ApiModelProperty(value = "usdt价格汇率")
    private BigDecimal usdtRate = BigDecimal.valueOf(7);

    @TableField(exist = false)
    @ApiModelProperty("资金费率触发时间")
    private List<String> percentFeeTriggerTime;

    @TableField(exist = false)
    @ApiModelProperty("资金费率触发倒计时(单位：秒)")
    private Integer percentFeeCountdown;

}
