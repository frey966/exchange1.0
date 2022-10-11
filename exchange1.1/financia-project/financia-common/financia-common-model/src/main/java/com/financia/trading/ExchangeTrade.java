package com.financia.trading;

import com.alibaba.fastjson2.JSON;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.financia.currency.ExchangeOrderDirection;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 撮合交易信息
 */
@Data
@TableName("ex_exchange_trade")
@ApiModel(value="ExchangeTrade",description = "单独交易信息")
public class ExchangeTrade implements Serializable{

    @TableId(type = IdType.AUTO)
    @ApiModelProperty(value = "id主键ID")
    private Integer id;
    private String symbol;
    private BigDecimal price;
    private BigDecimal amount;
    private BigDecimal buyTurnover;
    private BigDecimal sellTurnover;
    private ExchangeOrderDirection direction;
    private String buyOrderId;
    private String sellOrderId;
    private Date createTime;

    @Override
    public String toString() {
        return  JSON.toJSONString(this);
    }
}
