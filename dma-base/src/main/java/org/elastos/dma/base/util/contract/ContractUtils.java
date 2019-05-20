package org.elastos.dma.base.util.contract;

import org.web3j.abi.FunctionEncoder;
import org.web3j.abi.datatypes.Function;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.methods.response.EthSendTransaction;
import org.web3j.tx.RawTransactionManager;

import java.io.IOException;
import java.math.BigInteger;

/**
 * @Author: yangchuantong
 * @Description:
 * @Date:Created in  2019/1/14 18:17
 */
public class ContractUtils {



    public static String getHashByFunction(Web3j web3j,Function function, Credentials credentials, String contractAddress, BigInteger gasPrice, BigInteger gasLimit) throws IOException {
        String encodeData = FunctionEncoder.encode(function);
        RawTransactionManager transactionManager = new RawTransactionManager(web3j, credentials);
        EthSendTransaction ethSendTransaction = transactionManager.sendTransaction(gasPrice, gasLimit, contractAddress,
                encodeData, BigInteger.ZERO);
        return ethSendTransaction.getTransactionHash();
    }



}
