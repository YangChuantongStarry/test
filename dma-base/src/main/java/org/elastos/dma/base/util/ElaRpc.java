package org.elastos.dma.base.util;


import org.elastos.dma.utility.entity.ela.ChainType;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;

/**
 * @Author: yangchuantong
 * @Description:
 * @Date:Created in  2018/10/30 14:58
 */
public interface ElaRpc<T> {

    /**
     * 根据地址获取余额
     *
     * @param address 地址
     * @return 余额
     */
    String balance(String address);

    /**
     * 转账
     *
     * @param privatekey 私钥
     * @param _to        接收方
     * @param _value     数量
     * @return
     */
    T transfer(String privatekey, String _to, BigDecimal _value) throws Exception;
    /**
     * 交易详情
     *
     * @param txId 交易哈希
     * @return
     */
    T transactionInfo(String txId);
    /**
     * 地址校验
     *
     * @param address 地址
     * @return
     */
    Boolean checkAddr(String address);
    /**
     * 获取交易状态
     *
     * @param txid
     */
    Boolean getStatusByHash(String txid);

    /**
     * 获取本地交易签名
     *
     * @param privateKey 私钥
     * @param _to        接收者地址
     * @param _value     金额
     * @return
     * @throws Exception
     */
     String getRawTx(String privateKey, String _to, BigDecimal _value) throws Exception;

    /**
     * 反解析rawTransaction得到TXid,address,value
     *
     * @param rawTransaction
     * @return
     * @throws IOException
     */
      String decodeRawTx(String rawTransaction) throws IOException;

    /**
     * 发送交易签名
     *
     * @param rawData 签名
     * @param type    链类型
     * @return
     */
     String sendTx(String rawData, ChainType type);

}
