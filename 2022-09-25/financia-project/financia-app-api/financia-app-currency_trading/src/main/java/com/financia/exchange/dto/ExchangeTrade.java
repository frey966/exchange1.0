package com.financia.exchange.dto;

import com.alibaba.fastjson.JSON;
import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 成交记录
 * @author Nico
 */
@Data
@ApiModel(description = "最新成交记录")
public class ExchangeTrade implements Serializable{

    private String symbol;
    private BigDecimal price;
    private BigDecimal amount;
    private BigDecimal buyTurnover;
    private BigDecimal sellTurnover;
    private String direction;
    private String buyOrderId;
    private String sellOrderId;
    private Long time;
    @Override
    public String toString() {
        return  JSON.toJSONString(this);
    }
}
