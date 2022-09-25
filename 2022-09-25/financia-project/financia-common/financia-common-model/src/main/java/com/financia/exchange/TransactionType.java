package com.financia.exchange;

import com.fasterxml.jackson.annotation.JsonValue;
import com.financia.core.BaseEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum TransactionType implements BaseEnum {
    RECHARGE("充币"),
    WITHDRAW("提币"),
    TRANS("划转"),
    EXCHANGE("币币交易"),
    OTC_TRANSACTION("法币交易"),
    PROMOTION_AWARD("推广佣金"),
    ADMIN_RECHARGE("人工充提"),
    CONTRACT_TRANSACTION("合约交易"),
    BINARY_OPTION("二元期权"),
    GAME_BET("游戏交易");

    private String cnName;
    @Override
    @JsonValue
    public int getOrdinal() {
        return this.ordinal();
    }

    public static TransactionType valueOfOrdinal(int ordinal){
        switch (ordinal){
            case 0:return RECHARGE;
            case 1:return WITHDRAW;
            case 2:return TRANS;
            case 3:return EXCHANGE;
            case 4:return OTC_TRANSACTION;
            case 5:return PROMOTION_AWARD;
            case 6:return ADMIN_RECHARGE;
            case 7:return CONTRACT_TRANSACTION;
            case 8:return BINARY_OPTION;
            default:return null;
        }
    }
    public static int parseOrdinal(TransactionType ordinal) {
        if (TransactionType.RECHARGE.equals(ordinal)) {
            return 0;
        } else if (TransactionType.WITHDRAW.equals(ordinal)) {
            return 1;
        } else if (TransactionType.TRANS.equals(ordinal)) {
            return 2;
        } else if (TransactionType.EXCHANGE.equals(ordinal)) {
            return 3;
        } else if (TransactionType.OTC_TRANSACTION.equals(ordinal)) {
            return 4;
        } else if (TransactionType.PROMOTION_AWARD.equals(ordinal)) {
            return 5;
        } else if (TransactionType.ADMIN_RECHARGE.equals(ordinal)) {
            return 6;
        }else if (TransactionType.CONTRACT_TRANSACTION.equals(ordinal)) {
            return 7;
        }else if(TransactionType.BINARY_OPTION.equals(ordinal)){
            return 8;
        }else{
            return 29;
        }
    }

}
