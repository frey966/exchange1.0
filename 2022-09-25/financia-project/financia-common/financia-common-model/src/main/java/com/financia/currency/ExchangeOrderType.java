package com.financia.currency;

import com.baomidou.mybatisplus.annotation.EnumValue;

public enum ExchangeOrderType {
    MARKET_PRICE(0, "市价"),
    LIMIT_PRICE(1, "限价");

    ExchangeOrderType(int number, String description) {
        this.code = number;
        this.description = description;
    }

    @EnumValue
    private int code;
    private String description;
    public int getCode() {
        return code;
    }
    public String getDescription() {
        return description;
    }
}
