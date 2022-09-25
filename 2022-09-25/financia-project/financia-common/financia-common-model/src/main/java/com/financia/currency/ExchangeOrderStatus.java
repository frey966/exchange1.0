package com.financia.currency;

import com.baomidou.mybatisplus.annotation.EnumValue;

public enum ExchangeOrderStatus {

    TRADING(0, "委托中"),
    COMPLETED(1, "交易完成"),
    CANCELED(2, "交易取消"),
    OVERTIMED(3, "委托超时");

    ExchangeOrderStatus(int number, String description) {
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
