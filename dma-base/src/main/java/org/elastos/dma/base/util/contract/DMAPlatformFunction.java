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
import java.util.List;

/**
 * @Author: yangchuantong
 * @Description:
 * @Date:Created in  2019/1/14 17:35
 */
public class DMAPlatformFunction extends DMAPlatform {
    public DMAPlatformFunction(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        super(contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    protected DMAPlatformFunction(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        super(contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }


    public static DMAPlatformFunction load(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        return new DMAPlatformFunction(contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    public static DMAPlatformFunction load(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        return new DMAPlatformFunction(contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    public Function saveApproveFunction(String _owner, BigInteger _tokenId, BigInteger _value) {
        final Function function = new Function(
                "saveApprove",
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(_owner),
                        new org.web3j.abi.datatypes.generated.Uint256(_tokenId),
                        new org.web3j.abi.datatypes.generated.Uint256(_value)),
                Collections.<TypeReference<?>>emptyList());
        return function;
    }

    public Function saveApproveWithArrayFunction(String _owner, List<BigInteger> _tokenArr, BigInteger _value) {
        final Function function = new Function(
                "saveApproveWithArray",
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(_owner),
                        new org.web3j.abi.datatypes.DynamicArray<org.web3j.abi.datatypes.generated.Uint256>(
                                org.web3j.abi.Utils.typeMap(_tokenArr, org.web3j.abi.datatypes.generated.Uint256.class)),
                        new org.web3j.abi.datatypes.generated.Uint256(_value)),
                Collections.<TypeReference<?>>emptyList());
        return function;
    }

    public Function revokeApproveFunction(String _owner, BigInteger _tokenId, BigInteger _count) {
        final Function function = new Function(
                "revokeApprove",
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(_owner),
                        new org.web3j.abi.datatypes.generated.Uint256(_tokenId),
                        new org.web3j.abi.datatypes.generated.Uint256(_count)),
                Collections.<TypeReference<?>>emptyList());
        return function;
    }


    public Function transferWithArrayFunction(String _owner, List<BigInteger> _array, BigInteger _value) {
        final Function function = new Function(
                "transferWithArray",
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(_owner),
                        new org.web3j.abi.datatypes.DynamicArray<org.web3j.abi.datatypes.generated.Uint256>(
                                org.web3j.abi.Utils.typeMap(_array, org.web3j.abi.datatypes.generated.Uint256.class)),
                        new org.web3j.abi.datatypes.generated.Uint256(_value)),
                Collections.<TypeReference<?>>emptyList());
        return function;
    }



    public Function revokeApprovesWithArrayFunction(List<BigInteger> _tokenArr) {
        final Function function = new Function(
                "revokeApprovesWithArray",
                Arrays.<Type>asList(new org.web3j.abi.datatypes.DynamicArray<org.web3j.abi.datatypes.generated.Uint256>(
                        org.web3j.abi.Utils.typeMap(_tokenArr, org.web3j.abi.datatypes.generated.Uint256.class))),
                Collections.<TypeReference<?>>emptyList());
        return function;
    }


    public Function transferFunction(String _owner, BigInteger _tokenId, BigInteger _count, BigInteger _value) {
        final Function function = new Function(
                "transfer",
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(_owner),
                        new org.web3j.abi.datatypes.generated.Uint256(_tokenId),
                        new org.web3j.abi.datatypes.generated.Uint256(_count),
                        new org.web3j.abi.datatypes.generated.Uint256(_value)),
                Collections.<TypeReference<?>>emptyList());
        return function;
    }


    public Function saveMultiApproveFunction(String _owner, BigInteger _tokenId, BigInteger _count, BigInteger _value) {
        final Function function = new Function(
                "saveMultiApprove",
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(_owner),
                        new org.web3j.abi.datatypes.generated.Uint256(_tokenId),
                        new org.web3j.abi.datatypes.generated.Uint256(_count),
                        new org.web3j.abi.datatypes.generated.Uint256(_value)),
                Collections.<TypeReference<?>>emptyList());
        return function;
    }




}
