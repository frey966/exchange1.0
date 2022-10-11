package com.financia.exchange;

import com.fasterxml.jackson.annotation.JsonValue;
import com.financia.core.BaseEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * 打码量记录类型枚举
 */
@AllArgsConstructor
@Getter
public enum MemberRechargeRecordTypeEnum implements BaseEnum {
    ARTIFICIAL_INCREASE_DECREASE(1,"人工充值"),
    FAST_PASSAGE(2,"快捷充值"),
    BIBI_RECHARGE(3,"币币充值"),
    BIBI_BUY(4,"币币交易"),
    OPTIONS(5,"二元期权"),
    CONTRACT(6,"合约交易"),
    WITHDRAWAL_APPLICATION(7,"提币审核"),
    AUDIT_FAIL(8,"审核失败"),
    GAME_BET(9,"游戏投注");
    private Integer code;
    @Setter
    private String name;

    @Override
    @JsonValue
    public int getOrdinal(){
        return this.ordinal();
    }

}
