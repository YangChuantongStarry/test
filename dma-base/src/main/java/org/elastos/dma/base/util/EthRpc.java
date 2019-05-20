package org.elastos.dma.base.util;

import java.io.IOException;
import java.math.BigInteger;

/**
 * @Author: yangchuantong
 * @Description:
 * @Date:Created in  2018/11/2 15:09
 */
public interface EthRpc<T> {


    T balance(String address) throws IOException;

    T transfer(String privateKey, String _to, String _value, BigInteger gasPrice, BigInteger gasLimit) throws Exception;

    T transactionInfo(String txId) throws IOException;

    Boolean checkAddr(String address);

    Boolean getStatusByHash(String hash) throws IOException;


}
