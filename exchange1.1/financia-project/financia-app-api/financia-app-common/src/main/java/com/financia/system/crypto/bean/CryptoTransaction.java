package com.financia.system.crypto.bean;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CryptoTransaction {

    public CryptoTransaction(String transactionId, Long blockTimestamp, String from, String to, String type, String value) {
        this.transactionId = transactionId;
        this.blockTimestamp = blockTimestamp;
        this.from = from;
        this.to = to;
        this.type = type;
        this.value = value;
    }

    public CryptoTransaction() {

    }

    private String transactionId;
    private Long blockTimestamp;
    private String from;
    private String to;
    private String type;
    private String value;

    private CryptoTokenInfo tokenInfo;


}
