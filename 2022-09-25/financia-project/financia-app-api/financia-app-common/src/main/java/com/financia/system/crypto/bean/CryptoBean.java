package com.financia.system.crypto.bean;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CryptoBean {

    private String privateKey; //私钥

    private String address;    //地址

}
