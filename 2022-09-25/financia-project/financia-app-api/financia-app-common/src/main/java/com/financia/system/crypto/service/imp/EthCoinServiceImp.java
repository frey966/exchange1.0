package com.financia.system.crypto.service.imp;

import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSONObject;
import com.financia.system.crypto.bean.CryptoBscTransaction;
import com.financia.system.crypto.bean.CryptoTokenInfo;
import com.financia.system.crypto.bean.CryptoTransaction;
import com.financia.system.crypto.service.ICoinService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.web3j.contracts.eip20.generated.ERC20;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
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

public class EthCoinServiceImp implements ICoinService {
    private final Logger log = LoggerFactory.getLogger(getClass());
    private final static String GET_LIST="https://api.etherscan.io/api\n" +
            "   ?module=account\n" +
            "   &action=tokentx\n" +
            "   &contractaddress=%s\n" +
            "   &address=%s\n" +
            "   &page=1\n" +
            "   &offset=%d\n" +
            "   &startblock=0\n" +
            "   &endblock=27025780\n" +
            "   &sort=asc\n" +
            "   &apikey=YourApiKeyToken";

    @Override
    public String transfer(String addressFrom, String privateKey, String addressTo, BigInteger value) {
        Web3j web3j=Web3j.build(new HttpService("https://mainnet.infura.io/v3/a4ab37cadd8c415fad4ae8b800db7f2f"));

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
        String url=String.format(GET_LIST,getUsdtContractAddress(),address,length);
        String res= HttpUtil.get(url);
        JSONObject jsonObject=JSONObject.parseObject(res);
        List<CryptoTransaction> transactions=new ArrayList<>();

        try{
            if(!jsonObject.getString("message").equals("OK")){
                log.error(jsonObject.toJSONString());
                return null;
            }

            List<CryptoBscTransaction> bscTransactions=JSONObject.parseArray(jsonObject.getString("result"),CryptoBscTransaction.class);

            for(CryptoBscTransaction bt:bscTransactions){
                CryptoTransaction transaction=new CryptoTransaction();
                transaction.setTransactionId(bt.getHash());
                transaction.setValue(bt.getValue().toString());
                transaction.setTo(bt.getTo());
                transaction.setFrom(bt.getFrom());
                transaction.setBlockTimestamp(bt.getTimeStamp());
//                transaction.setType();
            }

            return transactions;


        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public BigInteger queryBalance(String address) {
        Web3j web3j=Web3j.build(new HttpService("https://mainnet.infura.io/v3/1a6b268e52e5477bbe1da414113606a0"));
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
        return "0xdAC17F958D2ee523a2206206994597C13D831ec7";
    }

    @Override
    public CryptoTokenInfo getTokenInfo() {
        return new CryptoTokenInfo("USDT", 6,"USDT");
    }

    @Override
    public BigDecimal getBalance(String address) {
        BigInteger bigInteger=queryBalance(address);
        return new BigDecimal(bigInteger).divide(new BigDecimal(10L).pow(getTokenInfo().getDecimals()), RoundingMode.DOWN);
    }

    @Override
    public BigInteger transferUsdt2BigInteger(BigDecimal bigDecimal) {
        return bigDecimal.multiply(new BigDecimal(10L).pow(getTokenInfo().getDecimals())).toBigInteger();
    }

    @Override
    public BigDecimal transferUsdt2BigDecimal(BigInteger bigInteger) {
        return new BigDecimal(bigInteger).divide(new BigDecimal(10L).pow(getTokenInfo().getDecimals()), RoundingMode.DOWN);
    }

    @Override
    public BigDecimal getBalanceOfChain(String address) {
        Web3j web3j=Web3j.build(new HttpService("https://mainnet.infura.io/v3/1a6b268e52e5477bbe1da414113606a0"));
        try {
            EthGetBalance getBalance=web3j.ethGetBalance(address, DefaultBlockParameterName.LATEST).send();
            return new BigDecimal(getBalance.getBalance());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String transferChain(String addressFrom, String privateKey, String addressTo, BigInteger value) {
        Web3j web3 = Web3j.build(new HttpService("https://mainnet.infura.io/v3/1a6b268e52e5477bbe1da414113606a0"));  // defaults to http://localhost:8545/
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
