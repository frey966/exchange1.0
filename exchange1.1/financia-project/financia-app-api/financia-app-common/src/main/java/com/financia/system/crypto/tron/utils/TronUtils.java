package com.financia.system.crypto.tron.utils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.bouncycastle.util.encoders.Hex;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.web3j.abi.FunctionEncoder;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.generated.Uint256;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * tron-utils
 *
 * @Autor TrickyZh 2021/1/20
 * @Date 2021-01-20 18:46:18
 */
public class TronUtils {

    //shasta测试网
    public static final String shastaTestNetUrl = "https://api.shasta.trongrid.io";
    private RestTemplate restTemplate = new RestTemplate();
    //主网
    public static final String mainNetUrl = "https://api.trongrid.io";
    private String tronUrl = mainNetUrl;
    private BigDecimal decimal = new BigDecimal("1000000");

    public TronUtils(String url) {
        tronUrl = url;
    }

    public TronUtils() {
    }

    /**
     * 查询trc 余额
     *
     * @throws IOException
     */
    public BigDecimal balanceOf(String queryAddress) throws IOException {
        String url = tronUrl + "/wallet/getaccount";
        JSONObject param = new JSONObject();
        param.put("address", Tron.toHexAddress(queryAddress));
        String result = HttpClientUtils.postJson(url, param.toJSONString());
        BigInteger balance = BigInteger.ZERO;
        if (!StringUtils.isEmpty(result)) {
            JSONObject obj = JSONObject.parseObject(result);
            BigInteger b = obj.getBigInteger("balance");
            if (b != null) {
                balance = b;
            }
        }
        return new BigDecimal(balance).divide(decimal, 6, RoundingMode.FLOOR);
    }

    /**
     * 发起TRX转账
     *
     * @throws Throwable
     */
    public String sendTrx(String toAddress, BigDecimal trxAmount, String privateKey) throws IOException, NoSuchAlgorithmException {
        String url = tronUrl + "/wallet/createtransaction";
        JSONObject param = new JSONObject();
        param.put("owner_address", Tron.toHexAddress(Tron.getAddressByPrivateKey(privateKey)));
        param.put("to_address", Tron.toHexAddress(toAddress));
        param.put("amount", trxAmount.multiply(decimal).toBigInteger());
        String json = HttpClientUtils.postJson(url, param.toJSONString());
        String txid = null;//交易id
        if (StringUtils.isNotEmpty(json)) {
            JSONObject transaction = JSONObject.parseObject(json);
//			transaction.getJSONObject("raw_data").put("data", Hex.toHexString("这里是备注信息".getBytes()));
            txid = Tron.signAndBroadcast(tronUrl, privateKey, transaction);
        }
        return txid;
    }

    /**
     * 查询trc20余额
     *
     * @throws IOException
     */
    public BigDecimal balanceOfTrc20(String queryAddress, String contract) throws IOException {

        String url = tronUrl + "/wallet/triggerconstantcontract";
        JSONObject param = new JSONObject();
        param.put("owner_address", Tron.toHexAddress(queryAddress));
        param.put("contract_address", Tron.toHexAddress(contract));
        param.put("function_selector", "balanceOf(address)");
        List<Type> inputParameters = new ArrayList<>();
        inputParameters.add(new Address(Tron.toHexAddress(queryAddress).substring(2)));
        param.put("parameter", FunctionEncoder.encodeConstructor(inputParameters));
        String result = HttpClientUtils.postJson(url, param.toJSONString());
        BigDecimal amount = BigDecimal.ZERO;
        if (StringUtils.isNotEmpty(result)) {
            JSONObject obj = JSONObject.parseObject(result);
            JSONArray results = obj.getJSONArray("constant_result");
            if (results != null && results.size() > 0) {
                BigInteger _amount = new BigInteger(results.getString(0), 16);
                amount = new BigDecimal(_amount).divide(decimal, 6, RoundingMode.FLOOR);
            }
        }
        return amount;
    }

    /**
     * 查询trc20USDT余额
     *
     * @throws IOException
     */
    public BigDecimal balanceOfTrc20Usdt(String queryAddress) throws IOException {
        String contract = "TR7NHqjeKQxGTCi8q8ZY4pL8otSzgjLj6t";
        return balanceOfTrc20(queryAddress, contract);
    }

    /**
     * 发起trc20转账
     *
     * @throws Throwable
     */
    public String sendTrc20(String toAddress, BigDecimal amount, String from, String contract) throws IOException, NoSuchAlgorithmException {

        String ownerAddress = Tron.getAddressByPrivateKey(from);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("contract_address", Tron.toHexAddress(contract));
        jsonObject.put("function_selector", "transfer(address,uint256)");
        List<Type> inputParameters = new ArrayList<>();
        inputParameters.add(new Address(Tron.toHexAddress(toAddress).substring(2)));
        inputParameters.add(new Uint256(amount.multiply(decimal).toBigInteger()));
        String parameter = FunctionEncoder.encodeConstructor(inputParameters);
        jsonObject.put("parameter", parameter);
        jsonObject.put("owner_address", Tron.toHexAddress(ownerAddress));
        jsonObject.put("call_value", 0);
        jsonObject.put("fee_limit", 9000000L);
        String trans1 = HttpClientUtils.postJson(tronUrl + "/wallet/triggersmartcontract", jsonObject.toString());
        JSONObject result = JSONObject.parseObject(trans1);
        if (result.containsKey("Error")) {
            return null;
        }
        JSONObject tx = result.getJSONObject("transaction");
        tx.getJSONObject("raw_data").put("data", Hex.toHexString("我是Tricky".getBytes()));//填写备注
        return Tron.signAndBroadcast(tronUrl, from, tx);
    }

    /**
     * 发起trc20usdt转账
     *
     * @throws Throwable
     */
    public String sendTrc20Usdt(String toAddress, BigDecimal amount, String from) throws IOException, NoSuchAlgorithmException {
        String contract = "TR7NHqjeKQxGTCi8q8ZY4pL8otSzgjLj6t";
        return sendTrc20(toAddress, amount, from, contract);
    }


    /**
     * 手续费查询
     *
     * @param hash 转账hash
     * @return trx
     */
    public BigDecimal transactionFee(String hash) {
        String url = "https://apilist.tronscan.org/api/transaction-info?hash=" + hash;
        ResponseEntity<Map> entity = restTemplate.getForEntity(url, Map.class);
        Map body = entity.getBody();
        assert body != null;
        Map cost = (Map) body.get("cost");
        BigDecimal net_fee = new BigDecimal(cost.get("net_fee").toString());
        BigDecimal energy_fee = new BigDecimal(cost.get("energy_fee").toString());
        BigDecimal amount = net_fee.add(energy_fee);
        return amount.divide(new BigDecimal(Math.pow(10, 6)), 6, RoundingMode.HALF_DOWN);
    }

    /**
     * 校验地址是否正确
     *
     * @param address 地址
     * @return true or false
     */
    public boolean validateAddress(String address) throws IOException {
        String url = tronUrl + "/wallet/validateaddress";
        JSONObject param = new JSONObject();
        param.put("address", address);
        boolean r = false;
        String result = HttpClientUtils.postJson(url, param.toJSONString());
        if (StringUtils.isNotEmpty(result)) {
            JSONObject obj = JSONObject.parseObject(result);
            r = (boolean) obj.get("result");

        }
        return r;
    }

    /**
     * 根据私钥获取地址
     *
     * @param privateKey
     * @return
     */
    public static String getAddressByPrivateKey(String privateKey) {
        return Tron.getAddressByPrivateKey(privateKey);
    }

    /**
     * 补单查询hash信息
     *
     * @param hash 转账hash
     * @return trx
     */
    public  Map transactionInfo(String hash) {
        String url = "https://apilist.tronscan.org/api/transaction-info?hash=" + hash;
        ResponseEntity<Map> entity = restTemplate.getForEntity(url, Map.class);
        return entity.getBody();
    }

    /**
     * 查询区块信息
     *
     * @param number 区块号
     * @return blockHash
     */
    public  String blockInfo(String number) {
        String url = "https://apilist.tronscan.org/api/block?number=" + number;
        ResponseEntity<Map> entity = restTemplate.getForEntity(url, Map.class);
        entity.getBody();
        Map body = entity.getBody();
        List<Map<String, Object>> data = (List<Map<String, Object>>) body.get("data");
        if (data != null && data.size() > 0) {
            return (String) data.get(0).get("hash");
        }
        return "";
    }

}
