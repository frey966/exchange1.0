package com.financia.system.crypto.tronAddress;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Setter
@Getter
public class Trc20Transaction implements Serializable {

    private String transactionId;
    private Long blockTimestamp;
    private String from;
    private String to;
    private String type;
    private String value;
    private Trc20TokenInfo tokenInfo;
}
