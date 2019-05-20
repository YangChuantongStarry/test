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
 * @Date:Created in  2019/1/22 21:57
 */
public class AuctionFunction extends Auction {
    public AuctionFunction(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        super(contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    protected AuctionFunction(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        super(contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }


    public static AuctionFunction load(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        return new AuctionFunction(contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    public static AuctionFunction load(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        return new AuctionFunction(contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }


    public Function bidFunction(BigInteger _bidValue) {
        final Function function = new Function(
                "bid",
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(_bidValue)),
                Collections.<TypeReference<?>>emptyList());
        return function;
    }

    public Function revokeTokenFunction() {
        final Function function = new Function(
                "revokeToken",
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

    public Function exchangeFunction() {
        final Function function = new Function(
                "exchange",
                Arrays.<Type>asList(),
                Collections.<TypeReference<?>>emptyList());
        return function;
    }


}
