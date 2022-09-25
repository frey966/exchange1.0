package com.financia.system.crypto;


import com.alibaba.fastjson.JSONObject;
import com.financia.system.crypto.bean.CryptoBean;
import com.financia.system.crypto.service.ICoinService;
import com.financia.system.crypto.service.imp.BscCoinServiceImp;
import com.financia.system.crypto.service.imp.EthCoinServiceImp;
import com.financia.system.crypto.service.imp.HecoCoinServiceImp;
import com.financia.system.crypto.service.imp.TrxCoinServiceImp;
import com.financia.system.crypto.tron.utils.HttpClientUtils;
import com.financia.system.crypto.tron.utils.Tron;
import com.financia.system.crypto.tron.utils.TronUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.security.NoSuchAlgorithmException;
import java.util.Map;

public class Test {

    public static void main(String[] args) throws Exception {

        queryHECO();
    }

    /**
     * 创建 TRC20 账号
     */
    public static void createAddress () {
       CryptoBean bean1= Manager.getInstance().get(Manager.Type.TRC20);
       System.out.println("success get private and address is "+bean1.toString());
    }


    /**
     * 创建 TRC20 账号
     */
    public static void createAddressERC20 () {
        CryptoBean bean1= Manager.getInstance().get(Manager.Type.ERC20);
        System.out.println("success get private and address is "+bean1.toString());
    }

    /**
     * 查询TRC20交易记录
     */
    public static void result () {
        TronUtils tronUtils = new TronUtils();
        Map map = tronUtils.transactionInfo("45449465b2480ad7fb639067ecd39ca0bd44370d15f3b3dfc33d67992f4be35c");
        if (map.size() == 0) {
            throw new IllegalArgumentException("hash值错误");
        }
        Boolean confirmed = (Boolean) map.get("confirmed");
        Integer contractType = (Integer) map.get("contractType");
        String contractRet = (String) map.get("contractRet");
        if (!confirmed || !"SUCCESS".equalsIgnoreCase(contractRet)) {
            throw new IllegalArgumentException("该笔转账失败或者正在确认中，请等待自动下单");
        }
        System.out.println("contractRet="+contractRet);
    }

    /**
     * 发起trc转账
     */
    public static void sendTrx () throws IOException, NoSuchAlgorithmException {
        TronUtils tronUtils = new TronUtils();
        String privateKey="33383c8905c0013e980f264707bf15ebd94f9232c98373da4eb7d94e2fe340b9";
        BigDecimal amount=new BigDecimal(10);
        String toAddress="TX9QQjVcPiAvBJsL4hLDe8hbwwzFbfy19k";
        String hash = tronUtils.sendTrx(toAddress, amount, privateKey);
        System.out.println("hash="+hash);
    }
    /**
     * 转账业务功能模块
     */
    public static void transfer () {
        ICoinService coinService=new TrxCoinServiceImp();
        BigDecimal amount = new BigDecimal(5);
        BigInteger value = amount.multiply(new BigDecimal(100000)).toBigInteger();
        String transfer = coinService.transfer("TZ2cqMLKxRsw5LV522SV4zuUiXMEsTb87i"
                , "33383c8905c0013e980f264707bf15ebd94f9232c98373da4eb7d94e2fe340b9"
                , "TNGTxFvP22QZruLaFdNZrXPWua2xDqmSPi", value);
        System.out.println("transfer-result="+transfer);
    }

    public static void transferEth(){
        ICoinService coinService=new EthCoinServiceImp();
        BigDecimal bigDecimal=new BigDecimal(5);
        BigInteger val=bigDecimal.multiply(new BigDecimal(10).pow(6)).toBigInteger();
//        String transfer=coinService.transfer();

    }

    /**
     * 查询TRC20 账号余额  TRX余额
     */
    public static void query() {
        String address = "TZ2cqMLKxRsw5LV522SV4zuUiXMEsTb87i";
        ICoinService coinService=new TrxCoinServiceImp();
        BigDecimal b=coinService.getBalance("TZ2cqMLKxRsw5LV522SV4zuUiXMEsTb87i");
        System.out.println("value is ="+b.toString());

        BigDecimal trx = coinService.getBalanceOfChain(address);
        System.out.println("trx="+trx);

    }

    public static  void transferChain(){
        System.out.println("-----transferChain---------------------");
        ICoinService coinService=new TrxCoinServiceImp();
        //String addressFrom,String privateKey, String addressTo, BigInteger value
        String addressFrom="TFJF2KanxZzzEpVPPww9vzzUPXWQHpG73w";
        String privateKey="0xfa8900cf811f1c5f7127a2740f74674157177f1f4d960fb375ea1a29f7af75b3";
        String addressTo="TX9QQjVcPiAvBJsL4hLDe8hbwwzFbfy19k";
        BigInteger amount = new BigInteger("60");
        String result = coinService.transferChain(addressFrom,privateKey,addressTo,amount);
        System.out.println("send trx res is "+result);
    }

    public static  void transferChain1(){
        System.out.println("-----transferChain---------------------");
        //String owner_address="TFJF2KanxZzzEpVPPww9vzzUPXWQHpG73w";
        String privateKey="0xfa8900cf811f1c5f7127a2740f74674157177f1f4d960fb375ea1a29f7af75b3";
        String to_address ="TX9QQjVcPiAvBJsL4hLDe8hbwwzFbfy19k";
        BigInteger amount = new BigInteger("60");
        String tronUrl = "https://api.trongrid.io";
        String url = tronUrl + "/wallet/createtransaction";
        JSONObject param = new JSONObject();
        param.put("owner_address",Tron.toHexAddress(Tron.getAddressByPrivateKey(privateKey)));
        param.put("to_address", Tron.toHexAddress(to_address));
        BigDecimal decimal = new BigDecimal("1000000");
        param.put("amount", amount);
        try {
            String json = HttpClientUtils.postJson(url, param.toJSONString());
            String txid = null;//交易id
            if (StringUtils.isNotEmpty(json)) {
                JSONObject transaction = JSONObject.parseObject(json);
//			transaction.getJSONObject("raw_data").put("data", Hex.toHexString("这里是备注信息".getBytes()));
                txid = Tron.signAndBroadcast(tronUrl, privateKey, transaction);
                System.out.println("txid==="+txid);
            }
        }
        catch (Exception ex){
                ex.printStackTrace();
        }

    }


    /**
     * 查询ERC20 账号余额  USDT余额
     */
    public static void queryEth() throws Exception {

        String address = "0x0339849465f4a6f2e1f90407cdc2e605e606062f";
        ICoinService coinService=new EthCoinServiceImp();
        BigDecimal USDT = coinService.getBalance(address);
        System.out.println("USDT="+USDT);

    }


    /**
     * 创建 币安 账号
     */
    public static void createAddressBSC () {
        CryptoBean bean1= Manager.getInstance().get(Manager.Type.BSC);
        System.out.println("success get private and address is "+bean1.toString());
    }


    /**
     * 查询BSC 账号余额  USDT余额
     */
    public static void queryBSC() throws Exception {

        String address = "0x7573ce9e7f78c9db42f2f81a520e405d8ee56dac";
        ICoinService coinService=new BscCoinServiceImp();
        BigDecimal USDT = coinService.getBalance(address);
        System.out.println("USDT="+USDT);

    }


//    /**
//     * 创建 火币 账号
//     */
//    public static void createAddressHECO () {
//        org.ethereum.crypto.jce.
//        CryptoBean bean1= Manager.getInstance().get(Manager.Type.HECO);
//        System.out.println("success get private and address is "+bean1.toString());
//    }


    /**
     * 查询HECO 账号余额  USDT余额
     */
    public static void queryHECO() throws Exception {

        String address = "0x5353b8089e430def2260ae410209ce6b569044cd";
        ICoinService coinService=new HecoCoinServiceImp();
        BigDecimal USDT = coinService.getBalance(address);
        System.out.println("USDT="+USDT);

    }

}
