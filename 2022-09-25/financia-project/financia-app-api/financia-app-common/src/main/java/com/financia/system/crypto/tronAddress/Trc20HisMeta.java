package com.financia.system.crypto.tronAddress;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Setter
@Getter
public class Trc20HisMeta implements Serializable {

    private long at;
    private int pageSize;

}
