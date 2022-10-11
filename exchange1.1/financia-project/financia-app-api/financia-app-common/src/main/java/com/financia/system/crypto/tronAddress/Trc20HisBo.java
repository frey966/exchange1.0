package com.financia.system.crypto.tronAddress;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

@Setter
@Getter
public class Trc20HisBo implements Serializable {

    private boolean success;
    private Trc20HisMeta meta;
    private List<Trc20Transaction> data;
}
