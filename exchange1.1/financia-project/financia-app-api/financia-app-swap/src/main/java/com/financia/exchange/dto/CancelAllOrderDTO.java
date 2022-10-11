package com.financia.exchange.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
@ApiModel(value = "CancelAllOrderDTO", description = "撤销所有合约委托单DTO")
public class CancelAllOrderDTO implements Serializable {

    @ApiModelProperty("会员Id")
    private Long memberId;

}
