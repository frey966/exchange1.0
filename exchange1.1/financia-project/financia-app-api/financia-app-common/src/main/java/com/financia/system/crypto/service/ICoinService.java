package com.financia.system.crypto.service;

import com.financia.system.crypto.bean.CryptoTokenInfo;
import com.financia.system.crypto.bean.CryptoTransaction;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;

public interface ICoinService {

    /**
     * 转账
     * addressFrom 转账地址
     * privateKey 转账私钥
     * addressTo 被转账地址
     * value 转账金额，大整数。
     * @return 链上转账的订单 hash。转账需要一定时间才会成功，后期可以通过这个hash值查询转账状态。
     * */
    String transfer(String addressFrom,String privateKey, String addressTo, BigInteger value);

    /**
     * 获取usdt转账记录
     *
     * address 地址
     * length 返回条数
     *
     * */
    List<CryptoTransaction> getLastTransactions(String address, int length);

    /**
     * 查询USDT余额
     * address 查询地址
     * @return 返回长整数。如果需要转换成常规的数字，需要调用 #transferUsdt2BigDecimal 方法
     * */
    BigInteger queryBalance(String address);

    /**
     * usdt的合约地址
     * */
    String getUsdtContractAddress();

    /**
     * usdt币种信息
     *
     * 不同链的usdt币，小数位不一致
     *
     * */
    CryptoTokenInfo getTokenInfo();

    /**
     * 查询余额 获取到余额的常规数值
     * */
    BigDecimal getBalance(String address);

    /**
     * 将usdt的常规数字转成长整数。
     * 比如 1u = 1 000 000 ，（trx 链）
     *     1u= 1 * 10^18 (eth,bsc,heco 链)
     * */
    BigInteger transferUsdt2BigInteger(BigDecimal bigDecimal);

    /**
     * 将usdt转成长整数转成常规数字
     * 比如 1 000 000 = 1u （trx 链）
     *     10*18=1u （eth,bsc,heco 链）
     * */
    BigDecimal transferUsdt2BigDecimal(BigInteger bigInteger);

    /**
     * 获取链上的资产，返回是整数
     * 1 trx= 10^6 sun
     * 1 eth=10^18 wei
     * 1 bnb=10^8
     *
     * */
    BigDecimal getBalanceOfChain(String address);

    /**
     * 转移链上资产，用于发现客户无法正常转账的时候使用
     * 返回交易hash
     * */
    String transferChain(String addressFrom,String privateKey, String addressTo, BigInteger value);


}
