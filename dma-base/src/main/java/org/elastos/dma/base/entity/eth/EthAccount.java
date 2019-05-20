package org.elastos.dma.base.entity.eth;


import org.elastos.dma.base.util.HdSupport;

import org.web3j.crypto.Credentials;
import org.web3j.crypto.ECKeyPair;
import org.web3j.crypto.Keys;
import org.web3j.utils.Numeric;


import java.io.Serializable;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

import static org.elastos.dma.base.conf.ChangeConstant.ETH_CHANGE;

/**
 * @Author: yangchuantong
 * @Description:
 * @Date:Created in  2018/11/19 15:08
 */
public class EthAccount implements Serializable {


    protected String address;  //钱包地址
    protected String privateKey;//私钥
    protected String publicKey;//公钥
    protected String mnemonic;//助记词


    public EthAccount(String privateKey) {
        Credentials credentials = Credentials.create(privateKey);
        ECKeyPair ecKeyPair = credentials.getEcKeyPair();
        this.privateKey = Numeric.toHexStringNoPrefix(ecKeyPair.getPrivateKey());
        this.publicKey = Numeric.toHexStringNoPrefix(ecKeyPair.getPublicKey());
        this.address = Numeric.prependHexPrefix(Keys.getAddress(ecKeyPair));
    }

    public static EthAccount init(HdSupport.MnemonicType mnemonicType) {
        String mnemonic = HdSupport.generateMnemonic(mnemonicType);
        return generate(mnemonic);
    }

    private static EthAccount generate(String mnemonic) {
        try {
            String privateKey = HdSupport.generatePrivateKey(mnemonic, ETH_CHANGE, 0);
            EthAccount ethAccount = new EthAccount(privateKey);
            ethAccount.setMnemonic(mnemonic);
            return ethAccount;

        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static EthAccount getEthAccountByMnemonic(String mnemonic) {
        EthAccount ethAccount = generate(mnemonic);
        ethAccount.setMnemonic(mnemonic);
        return ethAccount;
    }

    public static EthAccount getEthAccountByPrivateKey(String privateKey) {
        return new EthAccount(privateKey);
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPrivateKey() {
        return privateKey;
    }

    public void setPrivateKey(String privateKey) {
        this.privateKey = privateKey;
    }

    public String getPublicKey() {
        return publicKey;
    }

    public void setPublicKey(String publicKey) {
        this.publicKey = publicKey;
    }

    public String getMnemonic() {
        return mnemonic;
    }

    public void setMnemonic(String mnemonic) {
        this.mnemonic = mnemonic;
    }

    @Override
    public String toString() {
        return "EthAccount{" +
                "address='" + address + '\'' +
                ", privateKey='" + privateKey + '\'' +
                ", publicKey='" + publicKey + '\'' +
                ", mnemonic='" + mnemonic + '\'' +
                '}';
    }
}
