package com.financia.system.crypto.bean;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigInteger;

@Getter
@Setter
public class CryptoBscTransaction implements Serializable {
    private BigInteger blockNumber;
    private long timeStamp;
    private String hash;
    private String blockHash;
    private String from;
    private String contractAddress;
    private String to;
    private BigInteger value;
    private String tokenName;
    private String tokenSymbol;
    private int tokenDecimal;
    private int transactionIndex;
    private BigInteger gas;
    private BigInteger gasPrice;
    private BigInteger gasUsed;
    private BigInteger cumulativeGasUsed;
    private String input;
    private long confirmations;
}
