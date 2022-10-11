package com.financia.swap;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum ContractOrderEntrustCloseType {
    CLOSE_MEMBER(0, "用户平仓"),
    CLOSE_STOP_PROFIT(1, "止盈平仓"),

    CLOSE_STOP_LOSS(2,"止损平仓"),
    CLOSE_FORCE(3,"强制平仓");


    ContractOrderEntrustCloseType(int number, String description) {
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
