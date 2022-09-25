package com.financia.currency;

import com.baomidou.mybatisplus.annotation.EnumValue;

public enum ExchangeOrderDirection {
    BUY(0, "买入"),
    SELL(1, "卖出");

    ExchangeOrderDirection(int number, String description) {
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
