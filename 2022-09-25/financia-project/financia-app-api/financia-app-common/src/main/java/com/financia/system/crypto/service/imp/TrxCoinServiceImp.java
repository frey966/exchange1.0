package com.financia.system.crypto.service.imp;

import com.alibaba.fastjson.JSONObject;
import com.financia.system.crypto.bean.CryptoTokenInfo;
import com.financia.system.crypto.bean.CryptoTransaction;
import com.financia.system.crypto.bean.HttpHelper;
import com.financia.system.crypto.service.ICoinService;
import com.financia.system.crypto.tron.utils.TronUtils;
import com.financia.system.crypto.tronAddress.Trc20HisBo;
import com.financia.system.crypto.tronAddress.Trc20Transaction;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tron.trident.core.ApiWrapper;
import org.tron.trident.core.contract.Contract;
import org.tron.trident.core.contract.Trc20Contract;
import org.tron.trident.core.exceptions.IllegalException;
import org.tron.trident.core.transaction.TransactionBuilder;
import org.tron.trident.proto.Chain;
import org.tron.trident.proto.Response;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TrxCoinServiceImp implements ICoinService {


    private static final String TRC20_URL = "https://api.trongrid.io/v1/accounts/%s/transactions/trc20?limit=50&contract_address=TR7NHqjeKQxGTCi8q8ZY4pL8otSzgjLj6t&only_to=true&min_timestamp=%s";

    private static String apiKey="a91bbfec-3d85-46bf-8f23-092e83235edf";

    private static int MAX_TRY=3;
    private static final long feeLimit = 40000000;
    private final Logger log = LoggerFactory.getLogger(getClass());

    @Override
    public String transfer(String addressFrom, String privateKey, String addressTo, BigInteger value) {

        ApiWrapper wrapper=ApiWrapper.ofMainnet(privateKey,apiKey);
        int times = 1;


        while (times < MAX_TRY) {
            try {
                Contract contract = wrapper.getContract(getUsdtContractAddress());
                Trc20Contract token = new Trc20Contract(contract, addressFrom, wrapper);
                String result = token.transfer(addressTo, value.longValue(), 1, "TRC20", feeLimit);
                times = MAX_TRY;
                return result;

            } catch (Exception ex) {
                log.error("转账失败 【{}】to【{}】【{}】", addressFrom, addressTo, value.toString(), ex);
                times++;
            }
        }
        return null;
    }

    @Override
    public List<CryptoTransaction> getLastTransactions(String address, int length) {
        List<CryptoTransaction> latest = new ArrayList<>();
        if (apiKey != null) {
            Map<String, String> headerMap = new HashMap<>();
            headerMap.put("TRON-PRO-API-KEY", apiKey);
            headerMap.put("Accept", "application/json");
            final String url = String.format(TRC20_URL, address, 0);
            log.info("trc20地址 充值确认 请求：" + url);
            final String result = HttpHelper.getMethodByHeader(url, headerMap);
            if (StringUtils.isNotBlank(result) && isJSON2(result)) {
                Trc20HisBo hisBo = JSONObject.parseObject(result, Trc20HisBo.class);
                if (hisBo.isSuccess()) {
                    final List<Trc20Transaction> transactionList = hisBo.getData();
                    if (transactionList != null && transactionList.size() > 0) {
                        final long timeStamp = System.currentTimeMillis() - 6000;
                        for (Trc20Transaction transaction : transactionList) {
                            if (timeStamp > transaction.getBlockTimestamp()) {
                                CryptoTransaction transaction1=new CryptoTransaction(transaction.getTransactionId(),transaction.getBlockTimestamp(),transaction.getFrom(),transaction.getTo(),transaction.getType(),transaction.getValue());
                                transaction1.setTokenInfo(new CryptoTokenInfo("TRX-USDT",6,"USDT"));
                                latest.add(transaction1);
                            }
                        }
                        return latest;
                    }
                }
            }
        }
        return latest;
    }

    private static boolean isJSON2(String str) {
        boolean result = false;
        try {
            Object obj = JSONObject.parseObject(str);
            result = true;
        } catch (Exception e) {
            result = false;
        }
        return result;
    }

    @Override
    public BigInteger queryBalance(String address) {
        try{
            ApiWrapper wrapper = ApiWrapper.ofMainnet("","a91bbfec-3d85-46bf-8f23-092e83235edf");
            Contract contract = wrapper.getContract(getUsdtContractAddress());
            Trc20Contract token = new Trc20Contract(contract, address, wrapper);
            return token.balanceOf(address);
        }catch (Exception e){
            log.error("获取usdt余额失败,address: " + address, e);
        }
        return null;
    }

    @Override
    public String getUsdtContractAddress() {
        return "TR7NHqjeKQxGTCi8q8ZY4pL8otSzgjLj6t";
    }

    @Override
    public CryptoTokenInfo getTokenInfo() {
        return new CryptoTokenInfo("TRX-USDT",6,"USDT");
    }

    @Override
    public BigDecimal getBalance(String address) {
//        BigInteger bigInteger=queryBalance(address);
//        return new BigDecimal(bigInteger).divide(new BigDecimal(10L).pow(getTokenInfo().getDecimals()), 5,RoundingMode.DOWN);
        TronUtils tronUtils = new TronUtils();
        try {
            BigDecimal bigDecimal = tronUtils.balanceOfTrc20Usdt(address);
            return bigDecimal;
        }catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public BigDecimal transferUsdt2BigDecimal(BigInteger bigInteger) {
        return new BigDecimal(bigInteger).divide(new BigDecimal(10L).pow(getTokenInfo().getDecimals()),RoundingMode.DOWN);
    }

    @Override
    public BigInteger transferUsdt2BigInteger(BigDecimal bigDecimal) {
        return bigDecimal.multiply(new BigDecimal(getTokenInfo().getDecimals())).toBigInteger();
    }

    @Override
    public BigDecimal getBalanceOfChain(String address) {
//        ApiWrapper wrapper=ApiWrapper.ofMainnet("",apiKey);
//        BigDecimal bigInteger = new BigDecimal(wrapper.getAccountBalance(address));
//        return  bigInteger.divide(new BigDecimal(1000000),5,RoundingMode.DOWN);
        TronUtils tronUtils = new TronUtils();
        try {
            BigDecimal bigDecimal = tronUtils.balanceOf(address);
            return bigDecimal;
        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }

    @Override
    public String transferChain(String addressFrom, String privateKey, String addressTo, BigInteger value) {
        log.info("------transferChain----------------------");
        ApiWrapper wrapper=ApiWrapper.ofMainnet(privateKey,apiKey);
        try {
            Response.TransactionExtention transaction=wrapper.transfer(addressFrom,addressTo,value.longValue());
            TransactionBuilder builder = new TransactionBuilder(transaction.getTransaction());
            builder.setFeeLimit(1000000L);
            builder.setMemo("memo");
            builder.build();
            Chain.Transaction transaction1=wrapper.signTransaction(builder.getTransaction());
            return wrapper.broadcastTransaction(transaction1);
        } catch (IllegalException e) {
            throw new RuntimeException(e);
        }
    }
}
