package com.financia.swap;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;

public enum ContractType {
    PERPETUAL(0, "金本位合约"),
    SECOND(1, "币本位合约");

    ContractType(int number, String description) {
        this.code = number;
        this.description = description;
    }
    @EnumValue
    private int code;
    @JsonValue
    private String description;
    public int getCode() {
        return code;
    }
    public String getDescription() {
        return description;
    }
}
