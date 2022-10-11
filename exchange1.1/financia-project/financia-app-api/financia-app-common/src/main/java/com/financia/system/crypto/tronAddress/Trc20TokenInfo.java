package com.financia.system.crypto.tronAddress;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Setter
@Getter
public class Trc20TokenInfo implements Serializable {

    private String symbol;
    private String address;
    private String name;
    private int decimals;

}
