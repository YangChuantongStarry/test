package org.elastos.dma.base.util;


import org.elastos.dma.utility.util.UUIDUtils;
import org.web3j.utils.Convert;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * @Author: yangchuantong
 * @Description:
 * @Date:Created in  2018/11/2 9:19
 */
public class Utility {



    /**
     * 获取助记词
     *
     * @return 助记词
     */
    public static String getMnemonic(HdSupport.MnemonicType mnemonicType) {

        String mnemonic = HdSupport.generateMnemonic(mnemonicType);
        return mnemonic;

    }

    /**
     * 转换为eth单位
     *
     * @param _value 实际金额
     * @return wei   eth 单位
     */
    public static BigInteger toWei(String _value) {
        return Convert.toWei(_value, Convert.Unit.ETHER).toBigInteger();
    }

    /**
     * eth 单位转换字符串
     *
     * @param _value eth 单位金额
     * @return String 实际金额
     */
    public static String fromWei(String _value) {
        return Convert.fromWei(_value, Convert.Unit.ETHER).toPlainString();
    }


    /**
     * 将hash值进行MD5加密
     *
     * @param hash hash值
     * @return BigInteger
     */
    public static BigInteger genIdByHash(String hash) {
        BigInteger tokenId = BigInteger.valueOf(0);
        try {
            //md5加密
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(hash.getBytes());
            byte b[] = md.digest();
            int i;
            StringBuffer buf = new StringBuffer("");//转换为16进制字符
            for (int offset = 0; offset < b.length; offset++) {
                i = b[offset];
                if (i < 0)
                    i += 256;
                if (i < 16)
                    buf.append("0");
                buf.append(Integer.toHexString(i));
            }
            String result = buf.toString().substring(8, 24);//获取16位16进制字符
            tokenId = new BigInteger(result, 16);//装换为BigInteger

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return tokenId;
    }

    /**
     * 根据tokenId获取md5值
     *
     * @param tokenId tokenid
     * @return BigInteger
     */
    public static BigInteger getMD5ByTokenId(BigInteger tokenId) {
        tokenId = tokenId.divide(BigInteger.valueOf(1000000));
        return tokenId;
    }

    /**
     * 根据md5获取tokenId值
     *
     * @param md5 md5
     * @return BigInteger
     */
    public static BigInteger getTokenIdByMd5(BigInteger md5) {
        md5 = md5.multiply(BigInteger.valueOf(1000000));
        return md5;
    }

    public static void main(String[] args) {
        BigInteger bigInteger = Utility.genIdByHash(UUIDUtils.getUUID());
        System.out.println("bigInteger = " + bigInteger.longValue());
    }
}
