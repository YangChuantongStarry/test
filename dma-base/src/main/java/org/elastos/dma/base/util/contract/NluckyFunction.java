package org.elastos.dma.base.util.contract;

import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.Type;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.tx.TransactionManager;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.Collections;

/**
 * @Author: yangchuantong
 * @Description:
 * @Date:Created in  2019/1/22 22:03
 */
public class NluckyFunction extends Nlucky {

    public NluckyFunction(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        super(contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    protected NluckyFunction(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        super(contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }


    public static NluckyFunction load(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        return new NluckyFunction(contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    public static NluckyFunction load(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        return new NluckyFunction(contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }


    public Function betFunction() {
        final Function function = new Function(
                "bet",
                Arrays.<Type>asList(),
                Collections.<TypeReference<?>>emptyList());
        return function;
    }

    public Function setEndTimestampFunction(BigInteger _newTimestamp) {
        final Function function = new Function(
                "setEndTimestamp",
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(_newTimestamp)),
                Collections.<TypeReference<?>>emptyList());
        return function;
    }

    public Function successFunction(BigInteger _count) {
        final Function function = new Function(
                "success",
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(_count)),
                Collections.<TypeReference<?>>emptyList());
        return function;
    }


    public Function failsFunction(BigInteger _count) {
        final Function function = new Function(
                "fails",
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(_count)),
                Collections.<TypeReference<?>>emptyList());
        return function;
    }

}
