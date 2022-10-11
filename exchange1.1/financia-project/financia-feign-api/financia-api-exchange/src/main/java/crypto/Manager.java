package crypto;

import com.binance.dex.api.client.encoding.Crypto;
import com.google.common.base.Joiner;
import org.bitcoinj.core.Address;
import org.bitcoinj.core.ECKey;
import org.bitcoinj.core.LegacyAddress;
import org.bitcoinj.core.NetworkParameters;
import org.bitcoinj.wallet.DeterministicSeed;
import org.bitcoinj.wallet.Wallet;
import org.tron.trident.core.ApiWrapper;
import org.tron.trident.core.exceptions.IllegalException;
import org.tron.trident.core.key.KeyPair;
import org.tron.trident.proto.Chain;
import org.tron.trident.proto.Response;
import org.web3j.crypto.CipherException;
import org.web3j.crypto.ECKeyPair;
import org.web3j.crypto.Keys;
import org.web3j.crypto.WalletFile;

import java.math.BigInteger;
import java.security.InvalidAlgorithmParameterException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.util.List;

import static com.sun.org.apache.xalan.internal.xsltc.compiler.util.Util.println;

public class Manager {

    public enum Type{
        TRX, //波场
        HECO, //火币
        BSC, //币安
        ETH, //以太坊
        BTC //bitcoin
    }
    private static Manager manager=new Manager();
    private Manager(){

    }

    public static Manager getInstance(){
        return manager;
    }


    public CryptoBean get(Type type){
        switch (type){
            case TRX:
                return createTrcAddress();
            case HECO:
            case BSC:
            case  ETH:
                return createEthAddress();
            case BTC:
                return createBtcAddress();
        }
        return null;

    }



    protected CryptoBean createTrcAddress(){

        KeyPair keyPair=ApiWrapper.generateAddress();
        String privateKey=keyPair.getRawPair().getPrivateKey().toString();
        String publicKey=keyPair.getRawPair().getPublicKey().toString();
        String address=keyPair.toBase58CheckAddress();

        String mainPrivateKey="33383c8905c0013e980f264707bf15ebd94f9232c98373da4eb7d94e2fe340b9";
        ApiWrapper wrapper=ApiWrapper.ofMainnet(mainPrivateKey,NetworkParameters.ID_MAINNET);
        Response.TransactionExtention transaction = null;
        try {
            transaction = wrapper.createAccount(wrapper.keyPair.toBase58CheckAddress(),address);
        } catch (IllegalException e) {
            Chain.Transaction signedTxn = wrapper.signTransaction(transaction);
            String ret = wrapper.broadcastTransaction(signedTxn);
            //throw new RuntimeException(e);
            System.out.println("链上账户创建失败：address:"+address+";hash:"+ret);
        }


        return new CryptoBean(privateKey,address);
    }

    protected CryptoBean createBscAddress(){
        List<String> mnemonicCodeWords = Crypto.generateMnemonicCode();
        String privateKey = Crypto.getPrivateKeyFromMnemonicCode(mnemonicCodeWords);

        ECKey ecKey = ECKey.fromPrivate(new BigInteger(privateKey, 16));
        String address = Crypto.getAddressFromECKey(ecKey, "bnb");

//        try {
//            com.binance.dex.api.client.Wallet wallet = com.binance.dex.api.client.Wallet.createWalletFromMnemonicCode(mnemonicCodeWords, BinanceDexEnvironment.PROD);
//            String address=wallet.getAddress();
            return new CryptoBean(privateKey,address);
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }



    }

    protected CryptoBean createEthAddress(){
        try {
            ECKeyPair keyPair = Keys.createEcKeyPair();
            WalletFile wallet= org.web3j.crypto.Wallet.createStandard("",keyPair);
            return new CryptoBean(keyPair.getPrivateKey().toString(16),wallet.getAddress());
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

    protected CryptoBean createBtcAddress(){
        Wallet wallet= new Wallet(NetworkParameters.fromID(NetworkParameters.ID_MAINNET));
        DeterministicSeed seed = wallet.getKeyChainSeed();
        println("Seed words are: " + Joiner.on(" ").join(seed.getMnemonicCode()));
        println("Seed birthday is: " + seed.getCreationTimeSeconds());

        Address a=wallet.currentReceiveAddress();
        ECKey key=wallet.currentReceiveKey();
        LegacyAddress address= ((LegacyAddress) a);
        String btcAddress=address.toBase58();

        String privateKey=key.getPrivateKeyAsHex();

        return new CryptoBean(privateKey,btcAddress);

    }


    public static class CryptoBean{
        private String privateKey; //私钥
        private String address; //地址

        public CryptoBean(String privateKey, String address) {
            this.privateKey = privateKey;
            this.address = address;
        }

        public String getPrivateKey() {
            return privateKey;
        }

        public void setPrivateKey(String privateKey) {
            this.privateKey = privateKey;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        @Override
        public String toString() {
            return "CryptoBean{" +
                    "privateKey='" + privateKey + '\'' +
                    ", address='" + address + '\'' +
                    '}';
        }
    }
}
