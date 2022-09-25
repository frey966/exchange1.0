package crypto.model;

import lombok.Data;

@Data
public class WalletBean {

    protected String privateKey; //私钥

    protected String address; //地址

}
