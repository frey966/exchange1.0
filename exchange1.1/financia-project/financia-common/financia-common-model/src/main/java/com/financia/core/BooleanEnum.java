package com.financia.core;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * @author Jammy
 * @date 2019年01月10日
 */
@AllArgsConstructor
@Getter
public enum BooleanEnum implements BaseEnum {
    IS_FALSE(false, "否"),
    IS_TRUE(true, "是");

    @Setter
    @EnumValue
    private boolean is;

    @Setter
    private String nameCn;

    @Override
    @JsonValue
    public int getOrdinal() {
        return this.ordinal();
    }
}
