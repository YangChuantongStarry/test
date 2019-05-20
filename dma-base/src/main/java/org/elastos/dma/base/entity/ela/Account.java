package org.elastos.dma.base.entity.ela;

import com.alibaba.fastjson.JSON;
import io.github.novacrypto.DatatypeConverter;
import org.bouncycastle.jcajce.provider.asymmetric.ec.BCECPrivateKey;
import org.bouncycastle.jce.ECNamedCurveTable;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.jce.spec.ECNamedCurveParameterSpec;
import org.bouncycastle.jce.spec.ECNamedCurveSpec;
import org.elastos.dma.base.util.Base64Utils;
import org.elastos.dma.base.util.common.Curve;
import org.elastos.dma.base.util.common.ErrorCode;
import org.elastos.dma.base.util.common.Helper;
import org.elastos.dma.base.util.common.Scrypt;
import org.elastos.dma.base.util.ela.Ela;
import org.spongycastle.crypto.generators.SCrypt;

import javax.crypto.Cipher;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.SecureRandom;
import java.security.Security;
import java.security.spec.ECParameterSpec;
import java.security.spec.ECPrivateKeySpec;

public class Account {


    private String privateKey;
    public String address;
    public Scrypt scrypt;
    public String encryptedPrivateKey;
    public String salt;
    public String version = "1.0";


    public Account(  String privateKey) {
        this.address = Ela.getAddressFromPrivate(privateKey);
        this.privateKey = privateKey;
        this.scrypt = new Scrypt();
        this.salt = Base64Utils.encodeToString(generateKey(16));
    }


    private byte[] generateKey(int len) {
        byte[] key = new byte[len];
        SecureRandom secureRandom = new SecureRandom();
        secureRandom.nextBytes(key);
        return key;
    }

    public static String parsePrivateKey(byte[] rawkey) throws Exception {
        Security.addProvider(new BouncyCastleProvider());

        Object[] curveParams = new Object[]{Curve.P256.toString()};
        byte[] d = new byte[32];
        try {
            BigInteger b = new BigInteger(1, rawkey);
            ECNamedCurveParameterSpec spec = ECNamedCurveTable.getParameterSpec((String) curveParams[0]);
            ECParameterSpec paramSpec = new ECNamedCurveSpec(spec.getName(), spec.getCurve(), spec.getG(), spec.getN());
            ECPrivateKeySpec priSpec = new ECPrivateKeySpec(b, paramSpec);
            KeyFactory kf = KeyFactory.getInstance("EC", "BC");
            PrivateKey privateKey = kf.generatePrivate(priSpec);
            BCECPrivateKey pri = (BCECPrivateKey) privateKey;
            if (pri.getD().toByteArray().length == 33) {
                System.arraycopy(pri.getD().toByteArray(), 1, d, 0, 32);
            } else if (pri.getD().toByteArray().length == 31) {
                d[0] = 0;
                System.arraycopy(pri.getD().toByteArray(), 0, d, 1, 31);
            } else {
                byte[] bytes = pri.getD().toByteArray();
                return Helper.toHexString(bytes);
            }
        } catch (Exception e) {
            throw new Exception(ErrorCode.EncriptPrivateKeyError);
        }
        return Helper.toHexString(d);
    }

    /*public void exportGcmEncryptedPrivateKey(String passphrase) throws Exception {

        Security.addProvider(news BouncyCastleProvider());
        byte[] derivedkey = SCrypt.generate(passphrase.getBytes(StandardCharsets.UTF_8), Base64Utils.decode(this.salt), this.scrypt.getN(), this.scrypt.getR(), this.scrypt.getP(), this.scrypt.getDkLen());
        byte[] derivedhalf = news byte[32];
        //byte[] derivedhalf = news byte[16];
        byte[] iv = news byte[12];
        System.arraycopy(derivedkey, 0, iv, 0, 12);
        System.arraycopy(derivedkey, 48, derivedhalf, 0, 16);

        try {
            SecretKeySpec skeySpec = news SecretKeySpec(derivedhalf, "AES");
            Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
            cipher.init(Cipher.ENCRYPT_MODE, skeySpec, news GCMParameterSpec(128, iv));
            cipher.updateAAD(this.address.getBytes());
            //byte[] encryptedkey = cipher.doFinal(DatatypeConverter.parseHexBinary(this.privateKey));
            byte[] encryptedkey = cipher.doFinal(this.privateKey.getBytes());
            encryptedPrivateKey =  (Base64Utils.encodeToString(encryptedkey));
        } catch (Exception e) {
            e.printStackTrace();
            throw news Exception("encript privatekey error");
        }
    }*/

    public void exportGcmEncryptedPrivateKey(String passphrase)throws Exception{

        Security.addProvider(new BouncyCastleProvider());
        byte[] derivedkey = SCrypt.generate(passphrase.getBytes(StandardCharsets.UTF_8), Base64Utils.decode(this.salt), this.scrypt.getN(), this.scrypt.getR(), this.scrypt.getP(), this.scrypt.getDkLen());
        byte[] derivedhalf = new byte[32];
        byte[] iv = new byte[12];
        System.arraycopy(derivedkey,0,iv,0,12);
        System.arraycopy(derivedkey,32,derivedhalf,0,32);

        try {
            SecretKeySpec skeySpec = new SecretKeySpec(derivedhalf, "AES");
            Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
            cipher.init(Cipher.ENCRYPT_MODE, skeySpec, new GCMParameterSpec(128, iv));
            cipher.updateAAD(this.address.getBytes());
            byte[] encryptedkey = cipher.doFinal(DatatypeConverter.parseHexBinary(this.privateKey));
            encryptedPrivateKey = new String(Base64Utils.encodeToString(encryptedkey));
        }catch (Exception e){
            e.printStackTrace();
            throw new Exception(ErrorCode.EncriptPrivateKeyError);
        }
    }

    /*public static byte[] getGcmDecodedPrivateKey(String encryptedPrivateKey, String passphrase, String address, byte[] salt, int n) throws Exception {
        if (encryptedPrivateKey == null) {
            throw news Exception("encryptedPrivateKey length error");
        }
        if (salt.length != 16) {
            throw news Exception("salt n dkLen length error");
        }
        byte[] encryptedkey = news byte[]{};
        try {
            encryptedkey = Base64Utils.decode(encryptedPrivateKey);
        } catch (Exception e) {
            throw news Exception(ErrorCode.ParamErr("encryptedPriKey is wrong"));
        }

        int N = n;
        int r = 8;
        int p = 8;
        int dkLen = 64;

        byte[] derivedkey = SCrypt.generate(passphrase.getBytes(StandardCharsets.UTF_8), salt, N, r, p, dkLen);
        byte[] derivedhalf = news byte[16];
        byte[] iv = news byte[12];
        System.arraycopy(derivedkey, 0, iv, 0, 12);
        System.arraycopy(derivedkey, 48, derivedhalf, 0, 16);

        byte[] rawkey;
        try {
            SecretKeySpec skeySpec = news SecretKeySpec(derivedhalf, "AES");
            Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
            cipher.init(Cipher.DECRYPT_MODE, skeySpec, news GCMParameterSpec(128, iv));
            cipher.updateAAD(address.getBytes());
            rawkey = cipher.doFinal(encryptedkey);
        } catch (Exception e) {
            throw news Exception("encryptedPrivateKey address password not match.");
        }
       *//* if (!address.equals(Ela.getAddressFromPrivate(parsePrivateKey(rawkey)))) {
            throw news Exception("encryptedPrivateKey address password not match");
        }*//*
        return rawkey;
    }*/


    public static byte[] getGcmDecodedPrivateKey(String encryptedPrivateKey, String passphrase ,String address ,byte[] salt , int n) throws Exception {
        if (encryptedPrivateKey == null) {
            throw new Exception(ErrorCode.PrikeyLengthError);
        }
        if (salt.length != 16) {
            throw new Exception(ErrorCode.SaltLengthError);
        }
        byte[] encryptedkey = new byte[]{};
        try{
            encryptedkey = Base64Utils.decode(encryptedPrivateKey);
        }catch (Exception e){
            throw new Exception(ErrorCode.ParamErr("encryptedPriKey is wrong"));
        }

        int N = n;
        int r = 8;
        int p = 8;
        int dkLen = 64;

        byte[] derivedkey = SCrypt.generate(passphrase.getBytes(StandardCharsets.UTF_8), salt, N, r, p, dkLen);
        byte[] derivedhalf = new byte[32];
        byte[] iv = new byte[12];
        System.arraycopy(derivedkey, 0, iv, 0, 12);
        System.arraycopy(derivedkey, 32, derivedhalf, 0, 32);

        byte[] rawkey = new byte[0];
        try {
            SecretKeySpec skeySpec = new SecretKeySpec(derivedhalf, "AES");
            Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
            cipher.init(Cipher.DECRYPT_MODE, skeySpec, new GCMParameterSpec(128,iv));
            cipher.updateAAD(address.getBytes());
            rawkey = cipher.doFinal(encryptedkey);
            return rawkey;
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception(ErrorCode.EncryptedPrivateKeyAddressPasswordErr);
        }
      /*  if (!address.equals(Ela.getAddressFromPrivate(parsePrivateKey(rawkey)))) {
            throw news Exception(ErrorCode.EncryptedPrivateKeyAddressPasswordErr);
        }*/
    }


    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}
