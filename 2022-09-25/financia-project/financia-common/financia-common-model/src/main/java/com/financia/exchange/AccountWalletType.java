package com.financia.exchange;

public enum AccountWalletType {
    COIN_ACCOUNT(0, "币币账户"),
    PERPETUAL_ACCOUNT(1, "合约账户"),
    TRADE_ACCOUNT(2, "游戏账户");

    AccountWalletType(int number, String description) {
        this.code = number;
        this.description = description;
    }
    private int code;
    private String description;
    public int getCode() {
        return code;
    }
    public String getDescription() {
        return description;
    }

    public static AccountWalletType valueOfOrdinal(int ordinal){
        switch (ordinal){
            case 0:return COIN_ACCOUNT;
            case 1:return PERPETUAL_ACCOUNT;
            case 2:return TRADE_ACCOUNT;
            default:return null;
        }
    }
}
