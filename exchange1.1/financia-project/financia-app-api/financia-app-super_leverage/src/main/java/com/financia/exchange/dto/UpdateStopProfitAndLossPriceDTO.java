package com.financia.exchange.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
@ApiModel(value = "UpdateStopProfitAndLossPriceDTO", description = "修改止盈止损价DTO")
public class UpdateStopProfitAndLossPriceDTO implements Serializable {

    @ApiModelProperty("会员Id")
    private Long memberId;

    @ApiModelProperty("合约钱包Id")
    private Long walletId;

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
