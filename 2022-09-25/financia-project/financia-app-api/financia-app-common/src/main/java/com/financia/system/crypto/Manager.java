package com.financia.system.crypto;

import com.binance.dex.api.client.encoding.Crypto;
import com.binance.dex.api.client.encoding.EncodeUtils;
import com.financia.system.crypto.bean.CryptoBean;
import com.financia.system.crypto.service.ICoinService;
import com.financia.system.crypto.service.imp.BscCoinServiceImp;
import com.financia.system.crypto.service.imp.EthCoinServiceImp;
import com.financia.system.crypto.service.imp.HecoCoinServiceImp;
import com.financia.system.crypto.service.imp.TrxCoinServiceImp;
import com.google.common.base.Joiner;
import lombok.extern.slf4j.Slf4j;
import org.bitcoinj.core.Address;
import org.bitcoinj.core.ECKey;
import org.bitcoinj.core.LegacyAddress;
import org.bitcoinj.core.NetworkParameters;
import org.bitcoinj.wallet.DeterministicSeed;
import org.bitcoinj.wallet.Wallet;
import org.tron.trident.core.ApiWrapper;
import org.tron.trident.core.key.KeyPair;
import org.web3j.crypto.CipherException;
import org.web3j.crypto.ECKeyPair;
import org.web3j.crypto.Keys;
import org.web3j.crypto.WalletFile;

import java.security.InvalidAlgorithmParameterException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.util.List;

import static com.sun.org.apache.xalan.internal.xsltc.compiler.util.Util.println;

@Slf4j
public class Manager {

    public enum Type {
        TRC20(1, "波场"),  //波场 TRC20 TRX
        HECO(2, "火币"), //火币
        BSC(3, "币安"),  //币安
        ERC20(4, "以太坊"); //以太坊 ERC20 ETH
        //BTC   //bitcoin BTC 充值
        private final int code;
        private final String info;

        Type(int code, String info) {
            this.code = code;
            this.info = info;
        }

        public int getCode() {
            return code;
        }

        public String getInfo() {
            return info;
        }
    }

    private static Manager manager = new Manager();

    private Manager() {

    }

    public static Manager getInstance() {
        return manager;
    }


    public CryptoBean get(Type type) {
        switch (type) {
            case TRC20:
                return createTrcAddress();
            case HECO:
            case ERC20:
                return createEthAddress();
            case BSC:
               return createBscAddress();
        }
        return null;

    }


    /**
     * 创建trc账号
     *
     * @return
     */
    protected CryptoBean createTrcAddress() {
        log.info("------创建trc账号------------");
        KeyPair keyPair = ApiWrapper.generateAddress();
        String privateKey = keyPair.getRawPair().getPrivateKey().toString();
        String publicKey = keyPair.getRawPair().getPublicKey().toString();
        String address = keyPair.toBase58CheckAddress();
        return new CryptoBean(privateKey, address);
    }


    /**
     * 创建Bsc账号
     *
     * @return
     */
    protected CryptoBean createBscAddress() {
        log.info("------创建bsc账号------------");
        List<String> mnemonicCodeWords = Crypto.generateMnemonicCode();
        String privateKey = Crypto.getPrivateKeyFromMnemonicCode(mnemonicCodeWords);
        String address = Crypto.getAddressFromPrivateKey(privateKey, "bnb");
        byte[] pubKey = Crypto.decodeAddress(address);
        String hex = EncodeUtils.bytesToPrefixHex(pubKey);
        return new CryptoBean(privateKey,hex);

    }

    /**
     * 创建Eth账号
     *
     * @return
     */
    protected CryptoBean createEthAddress() {
        log.info("------创建Eth账号------------");
        try {
            ECKeyPair keyPair = Keys.createEcKeyPair();
            WalletFile wallet = org.web3j.crypto.Wallet.createStandard("", keyPair);
            return new CryptoBean(keyPair.getPrivateKey().toString(16), "0x" + wallet.getAddress());
        } catch (InvalidAlgorithmParameterException e) {
            throw new RuntimeException(e);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        } catch (NoSuchProviderException e) {
            throw new RuntimeException(e);
        } catch (CipherException e) {
            throw new RuntimeException(e);
        }

    }

    /**
     * 创建btc账号
     *
     * @return
     */
    protected CryptoBean createBtcAddress() {
        log.info("------创建Btc账号------------");
        Wallet wallet = new Wallet(NetworkParameters.fromID(NetworkParameters.ID_MAINNET));
        DeterministicSeed seed = wallet.getKeyChainSeed();
        println("Seed words are: " + Joiner.on(" ").join(seed.getMnemonicCode()));
        println("Seed birthday is: " + seed.getCreationTimeSeconds());
        Address a = wallet.currentReceiveAddress();
        ECKey key = wallet.currentReceiveKey();
        LegacyAddress address = ((LegacyAddress) a);
        String btcAddress = address.toBase58();
        String privateKey = key.getPrivateKeyAsHex();
        return new CryptoBean(privateKey, btcAddress);

    }

    public ICoinService getService(int typeId) {
        if (Type.TRC20.getCode() == typeId) {
            return new TrxCoinServiceImp();
        } else if (Type.BSC.getCode() == typeId) {
            return new BscCoinServiceImp();
        } else if (Type.ERC20.getCode() == typeId) {
            return new EthCoinServiceImp();
        } else if (Type.HECO.getCode() == typeId) {
            return new HecoCoinServiceImp();
        }
        return null;
    }
}
