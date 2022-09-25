package com.financia.system.crypto.bean;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
public class CryptoTokenInfo {

    public CryptoTokenInfo(String name, int decimals, String symbol) {
        this.name = name;
        this.decimals = decimals;
        this.symbol = symbol;
    }

    private String name;
    private int decimals;
    private String symbol;
}
