package com.financia.currency;

import com.baomidou.mybatisplus.annotation.EnumValue;

/**
 * @description: ExchangeOrderResource
 * @author: MrGao
 * @create: 2019/05/07 15:46
 */
public enum ExchangeOrderResource {

    /**
     * 机器人
     */
    ROBOT(0,"机器人"),
    /**
     * 用户
     */
    CUSTOMER(1,"用户"),
    /**
     * API
     */
    API(2,"API接口");


    ExchangeOrderResource(int number, String description) {
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
