package com.financia.exchange.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
@ApiModel(value = "CancelOrderDTO", description = "撤销订单DTO")
public class CancelOrderDTO implements Serializable {

    @ApiModelProperty("订单Id")
    private Long orderId;

}
