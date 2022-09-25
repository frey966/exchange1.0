package com.financia.system.crypto.bean;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CryptoHecoTranscation {

    //{"blockNumber":"1384331","timeStamp":"1611046514","hash":"0x415723de4ede576f2c2838aed19ff397d0e3d1e152a8fc4813ff6a25eb86756c","nonce":"14","blockHash":"0xf0756d45284882d3d5f5f9720b5e94222ee86d48c7f3ff2e4587b1a4b7ed6d08","from":"0x06f46644d6e6d044ab008fb23bdc5bf3529bf3f0","to":"0xa3fd9758323c8a86292b55702f631c81283c9b79","contractAddress":"0x25d2e80cb6b86881fd7e07dd263fb79f4abe033c","value":"100000000000000000000000000","tokenName":"MDX Token","tokenSymbol":"MDX","tokenDecimal":"18","transactionIndex":"27","gas":"53281","gasPrice":"1000000000","gasUsed":"53281","cumulativeGasUsed":"4508366","input":"deprecated","confirmations":"15874202"}


    private Integer blockNumber;
    private long timestamp;
    private String hash;
    private String blockHash;
    private String from;
    private String to;
    private String contractAddress;
    private String value;
    private String tokenName;
    private String tokenSymbol;
    private int tokenDecimal;
    private int transactionIndex;
    private long gas;
    private long gasPrice;
    private long gasUsed;
    private long cumulativeGasUsed;
    private String input;
    private long confirmations;


}
