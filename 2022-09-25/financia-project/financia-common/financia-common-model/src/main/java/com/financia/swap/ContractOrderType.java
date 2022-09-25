package com.financia.swap;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum ContractOrderType {
    MARKET_PRICE(0, "市价"),
    LIMIT_PRICE(1, "限价"),
    SPOT_LIMIT(2, "计划委托");

    ContractOrderType(int number, String description) {
        this.code = number;
        this.description = description;
    }

    @EnumValue
    private int code;
    private String description;
    public int getCode() {
        return code;
    }
    @JsonValue
    public String getDescription() {
        return description;
    }

    @JsonCreator
    public static ContractOrderType getTypeByCode(int code) {
        for (ContractOrderType orderType : ContractOrderType.values()) {
            if (orderType.getCode()==code) {
                return orderType;
            }
        }
        return null;
    }
}
