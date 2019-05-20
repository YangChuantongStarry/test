package org.elastos.dma.base.entity.ela;


import org.elastos.dma.base.util.HdSupport;
import org.elastos.dma.base.util.ela.Ela;

import java.io.Serializable;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

import static org.elastos.dma.base.conf.ChangeConstant.ELA_CHANGE;

/**
 * @Author: yangchuantong
 * @Description:
 * @Date:Created in  2018/11/19 13:45
 */
public class ElaAccount implements Serializable {

    private String address;  //钱包地址
    private String privateKey;//私钥
    private String publicKey;//公钥
    private String mnemonic;//助记词


    public ElaAccount(String privateKey) {
        this.privateKey = privateKey;
        this.publicKey = Ela.getPublicFromPrivate(privateKey);
        this.address = Ela.getAddressFromPrivate(privateKey);
    }

    public static ElaAccount init(HdSupport.MnemonicType mnemonicType) {
        String mnemonic = HdSupport.generateMnemonic(mnemonicType);
      return generate(mnemonic,null);
    }
    public static ElaAccount init(HdSupport.MnemonicType mnemonicType,String pwd) {
        String mnemonic = HdSupport.generateMnemonic(mnemonicType);
        return generate(mnemonic,pwd);
    }

    private static ElaAccount generate(String mnemonic,String pwd) {
        try {
            String privateKey = HdSupport.generatePrivateKey(mnemonic,pwd, ELA_CHANGE, 0);
            ElaAccount elaAccount = new ElaAccount(privateKey);
            elaAccount.setMnemonic(mnemonic);
            return elaAccount;

        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static ElaAccount getElaAccountByMnemonic(String mnemonic) {
        return generate(mnemonic,null);
    }
    public static ElaAccount getElaAccountByMnemonic(String mnemonic,String pwd) {
        return generate(mnemonic,pwd);
    }

    public static ElaAccount getElaAccountByPrivateKey(String privateKey) {
        return new ElaAccount(privateKey);
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
        return "{" +
                "address='" + address + '\'' +
                ", privateKey='" + privateKey + '\'' +
                ", publicKey='" + publicKey + '\'' +
                ", mnemonic='" + mnemonic + '\'' +
                '}';
    }
}
