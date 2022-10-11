package com.financia.exchange.dto;

import com.financia.swap.ContractOrderDirection;
import com.financia.swap.ContractOrderEntrustType;
import com.financia.swap.ContractOrderType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
@ApiModel(value = "OpenOrderDTO", description = "合约开仓DTO")
public class OpenOrderDTO implements Serializable {

    @ApiModelProperty(value = "会员Id", required = true)
    private Long memberId;

    @ApiModelProperty(value = "合约交易对Id", required = true)
    private Long contractCoinId;

    @ApiModelProperty(value = "买/卖 BUY:做多 SELL:做空", required = true)
    private ContractOrderDirection direction;

    @ApiModelProperty(value = "订单类型 0：市价 1：限价 2：计划委托", required = true)
    private ContractOrderType orderType;

    @ApiModelProperty(value = "交易金额", required = true)
    private BigDecimal tradePrice;

    @ApiModelProperty("委托价格")
    private BigDecimal entrustPrice;

    @ApiModelProperty(value = "杠杆倍数", required = true)
    private BigDecimal leverage;

    @ApiModelProperty(value = "开仓类型 OPEN-开仓，ADD-加仓", required = true)
    private ContractOrderEntrustType openType;

    @ApiModelProperty(value = "止盈价")
    private BigDecimal stopProfitPrice;

    @ApiModelProperty(value = "止损价")
    private BigDecimal stopLossPrice;

    @ApiModelProperty(value = "止盈百分比（传参为小数，比如25%就是0.25）",example = "0.25")
    private BigDecimal stopProfitPercentage;

    @ApiModelProperty(value = "止损百分比（传参为小数，比如25%就是0.25）",example = "0.25")
    private BigDecimal stopLossPercentage;

    @ApiModelProperty("止盈金额")
    private BigDecimal stopProfitAmount;

    @ApiModelProperty("止损金额")
    private BigDecimal stopLossAmount;


}
