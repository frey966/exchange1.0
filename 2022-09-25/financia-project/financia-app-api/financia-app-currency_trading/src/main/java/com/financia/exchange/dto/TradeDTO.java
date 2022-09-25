package com.financia.exchange.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
@ApiModel(description="最新交易")
public class TradeDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value ="价格")
    private BigDecimal price;

    @ApiModelProperty(value = "成交量(买或卖一方)")
    private BigDecimal amount;

    @ApiModelProperty(value = "成交时间(UNIX epoch time in millisecond)")
    private Long ts;

    @ApiModelProperty(value = "成交主动方 (taker的订单方向) : 'buy' or 'sell'")
    private String direction;

}
