package com.financia.swap;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum ContractOrderEntrustType {


    OPEN(0, "开仓"),
    CLOSE(1, "平仓"),

    ADD(2, "加仓"),
    SUB(3, "减仓"),
    STOP_PROFIT(4, "止盈"),
    STOP_LOSS(5, "止损"),

    FUNDING_FEE(6, "资金费"),
    SUB_OPPONENT(7, "对手仓减仓"),
    ADD_OPPONENT(8, "对手仓加仓");


    ContractOrderEntrustType(int number, String description) {
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
