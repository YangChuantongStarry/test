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
 * @Date:Created in  2019/1/22 22:06
 */
public class PresaleFunction extends  Presale {


    public PresaleFunction(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        super(contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    protected PresaleFunction(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        super(contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }


    public static PresaleFunction load(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        return new PresaleFunction(contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    public static PresaleFunction load(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        return new PresaleFunction(contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }


    public Function mintByPlatformFunction(BigInteger _tokenId, BigInteger _count) {
        final Function function = new Function(
                "mintByPlatform",
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(_tokenId),
                        new org.web3j.abi.datatypes.generated.Uint256(_count)),
                Collections.<TypeReference<?>>emptyList());
        return function;
    }


    public Function registAssetFunction(String _owner, BigInteger _tokenId, BigInteger _amount, BigInteger _value, String _uri) {
        final Function function = new Function(
                "registAsset",
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(_owner),
                        new org.web3j.abi.datatypes.generated.Uint256(_tokenId),
                        new org.web3j.abi.datatypes.generated.Uint256(_amount),
                        new org.web3j.abi.datatypes.generated.Uint256(_value),
                        new org.web3j.abi.datatypes.Utf8String(_uri)),
                Collections.<TypeReference<?>>emptyList());
        return function;
    }


    public Function mintByCustomerFunction(BigInteger _tokenId) {
        final Function function = new Function(
                "mintByCustomer",
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(_tokenId)),
                Collections.<TypeReference<?>>emptyList());
        return function;
    }


    public Function refundFunction(BigInteger _tokenId, BigInteger _amount) {
        final Function function = new Function(
                "refund",
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(_tokenId),
                        new org.web3j.abi.datatypes.generated.Uint256(_amount)),
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
    public Function orderFunction(BigInteger _tokenId, BigInteger _amount, String _receiveAddress) {
        final Function function = new Function(
                "order",
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(_tokenId),
                        new org.web3j.abi.datatypes.generated.Uint256(_amount),
                        new org.web3j.abi.datatypes.Address(_receiveAddress)),
                Collections.<TypeReference<?>>emptyList());
        return function;
    }

}
