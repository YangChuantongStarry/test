package org.elastos.dma.base.entity.did;


import org.elastos.dma.base.util.HdSupport;
import org.elastos.dma.base.util.ela.ECKey;
import org.elastos.dma.base.util.ela.Ela;

import io.github.novacrypto.DatatypeConverter;

import java.io.Serializable;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

import static org.elastos.dma.base.conf.ChangeConstant.ELA_CHANGE;

/**
 * @Author: yangchuantong
 * @Description:
 * @Date:Created in  2018/11/19 11:52
 */
public class DIDAccount implements Serializable {


    private String address;  //钱包地址
    private String privateKey;//私钥
    private String publicKey;//公钥
    private String mnemonic;//助记词
    private String did;//did 地址


    public DIDAccount(String privateKey) {
        this.privateKey = privateKey;
        this.publicKey = Ela.getPublicFromPrivate(privateKey);
        this.address = Ela.getAddressFromPrivate(privateKey);
        ECKey ec = ECKey.fromPrivate(DatatypeConverter.parseHexBinary(privateKey));
        this.did = ec.toIdentityID();
    }


    public static DIDAccount init(HdSupport.MnemonicType mnemonicType) {
        String mnemonic = HdSupport.generateMnemonic(mnemonicType);
        return generate(mnemonic);
    }


    public static DIDAccount getDIDAccountByMnemonic(String mnemonic) {
        return generate(mnemonic);
    }

    public static DIDAccount getDIDAccountByPrivateKey(String privateKey) {
        return new DIDAccount(privateKey);
    }

    private static DIDAccount generate(String mnemonic) {
        try {
            String privateKey = HdSupport.generatePrivateKey(mnemonic, ELA_CHANGE, 0);
            DIDAccount didAccount = new DIDAccount(privateKey);
            didAccount.setMnemonic(mnemonic);
            return didAccount;

        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }


    public String getDid() {
        return did;
    }

    public void setDid(String did) {
        this.did = did;
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
        return "DIDAccount{" +
                "address='" + address + '\'' +
                ", privateKey='" + privateKey + '\'' +
                ", publicKey='" + publicKey + '\'' +
                ", mnemonic='" + mnemonic + '\'' +
                ", did='" + did + '\'' +
                '}';
    }
}
