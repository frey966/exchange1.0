package com.financia.system.crypto.service.imp;

import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.financia.system.crypto.bean.CryptoHecoTranscation;
import com.financia.system.crypto.bean.CryptoTokenInfo;
import com.financia.system.crypto.bean.CryptoTransaction;
import com.financia.system.crypto.service.ICoinService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.web3j.contracts.eip20.generated.ERC20;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.WalletUtils;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameter;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.response.EthGetBalance;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.protocol.http.HttpService;
import org.web3j.tx.ClientTransactionManager;
import org.web3j.tx.TransactionManager;
import org.web3j.tx.Transfer;
import org.web3j.tx.gas.DefaultGasProvider;
import org.web3j.utils.Convert;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

public class HecoCoinServiceImp implements ICoinService {

    private static String GET_BALANCE="https://api.hecoinfo.com/api?module=account&action=balance&address=%s&tag=latest&apikey=%s";

    private static String GET_LIST="https://api.hecoinfo.com/api?module=account&action=tokentx&contractaddress=%s&address=%s&page=1&offset=%d&sort=desc&apikey=%s\";";

    private static String apiKey="34EGUECBSY3VNBR236HZ34Z27M99HP4FRK";

    private final Logger log = LoggerFactory.getLogger(getClass());

    @Override
    public String transfer(String addressFrom, String privateKey, String addressTo, BigInteger value) {
        Web3j web3j=Web3j.build(new HttpService("https://http-mainnet.hecochain.com"));

        Credentials credentials=Credentials.create(privateKey);

        ERC20 token=ERC20.load(getUsdtContractAddress(),web3j,credentials, new DefaultGasProvider());
        try {
            TransactionReceipt receipt =token.transfer(addressTo,value).send();
            log.info("receipt is",receipt.toString());
            return receipt.getTransactionHash();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<CryptoTransaction> getLastTransactions(String address, int length) {
        String url=String.format(GET_LIST,getUsdtContractAddress(),address,length,apiKey);
        System.out.println("url is : "+url);
        String res=HttpUtil.get(url,10000);
        List<CryptoTransaction> transactions=new ArrayList<>();
        try{
            JSONObject jsonObject=JSONObject.parseObject(res);

            if(jsonObject.getString("status").equals("0")){
                log.info(jsonObject.getString("message"));
                return new ArrayList<>();
            }
            JSONArray arr=jsonObject.getJSONArray("result");

            List<CryptoHecoTranscation> hecoTranscations=JSONArray.parseArray(arr.toJSONString(), CryptoHecoTranscation.class);

            for(CryptoHecoTranscation h:hecoTranscations){
                CryptoTransaction transaction=new CryptoTransaction();
                transaction.setTransactionId(h.getHash());
                transaction.setValue(h.getValue());
                transaction.setTo(h.getTo());
                transaction.setFrom(h.getFrom());
                transaction.setBlockTimestamp(h.getTimestamp());
                transactions.add(transaction);
            }

            return transactions;

        }catch (Exception e){
            e.printStackTrace();
            log.error(e.toString());
        }

        return null;
    }

    @Override
    public BigInteger queryBalance(String address) {

        Web3j web3j=Web3j.build(new HttpService("https://http-mainnet.hecochain.com"));
        TransactionManager manager=new ClientTransactionManager(web3j,address);
        ERC20 token=ERC20.load(getUsdtContractAddress(),web3j,manager, new DefaultGasProvider());

        try {
            return token.balanceOf(address).send();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String getUsdtContractAddress() {
        return "0xa71edc38d189767582c38a3145b5873052c3e47a";
    }

    @Override
    public CryptoTokenInfo getTokenInfo() {
        return new CryptoTokenInfo("Heco-Peg USDT Token", 18,"USDT");
    }

    @Override
    public BigDecimal getBalance(String address) {
        BigInteger bigInteger=queryBalance(address);
        return new BigDecimal(bigInteger).divide(new BigDecimal(10L).pow(getTokenInfo().getDecimals()), RoundingMode.DOWN);
    }

    @Override
    public BigDecimal transferUsdt2BigDecimal(BigInteger bigInteger) {
        return new BigDecimal(bigInteger).divide(new BigDecimal(10L).pow(getTokenInfo().getDecimals()), RoundingMode.DOWN);
    }

    @Override
    public BigInteger transferUsdt2BigInteger(BigDecimal bigDecimal) {
        return bigDecimal.multiply(new BigDecimal(10L).pow(getTokenInfo().getDecimals())).toBigInteger();
    }

    @Override
    public BigDecimal getBalanceOfChain(String address) {
        Web3j web3j=Web3j.build(new HttpService("https://http-mainnet.hecochain.com"));
        try {
            EthGetBalance getBalance=web3j.ethGetBalance(address, DefaultBlockParameterName.LATEST).send();
            return new BigDecimal(getBalance.getBalance());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String transferChain(String addressFrom, String privateKey, String addressTo, BigInteger value) {
        Web3j web3 = Web3j.build(new HttpService("https://http-mainnet.hecochain.com"));  // defaults to http://localhost:8545/
        Credentials credentials = Credentials.create(privateKey);
        try {
            TransactionReceipt transactionReceipt = Transfer.sendFunds(
                    web3, credentials, addressTo,
                    new BigDecimal(value), Convert.Unit.WEI).send();
            return transactionReceipt.getTransactionHash();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }
}
