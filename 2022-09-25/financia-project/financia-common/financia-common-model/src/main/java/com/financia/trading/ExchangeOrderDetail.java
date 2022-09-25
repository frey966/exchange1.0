package com.financia.trading;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class ExchangeOrderDetail {
    private String orderId;
    private BigDecimal price;
    private BigDecimal amount;
    private BigDecimal turnover;
    private BigDecimal fee;
    //成交时间
    private long time;
}
