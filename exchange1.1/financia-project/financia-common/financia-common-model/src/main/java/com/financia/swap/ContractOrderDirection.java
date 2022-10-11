package com.financia.swap;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum ContractOrderDirection {
    BUY(0, "做多"),
    SELL(1, "做空");

    ContractOrderDirection(int number, String description) {
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
