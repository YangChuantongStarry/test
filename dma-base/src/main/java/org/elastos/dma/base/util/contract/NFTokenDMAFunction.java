package org.elastos.dma.base.util.contract;

import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.generated.Uint256;
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
 * @Date:Created in  2019/1/14 17:44
 */
public class NFTokenDMAFunction extends  NFTokenDMA {
    public NFTokenDMAFunction(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        super(contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    public NFTokenDMAFunction(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        super(contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }


    public static NFTokenDMAFunction load(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        return new NFTokenDMAFunction(contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    public static NFTokenDMAFunction load(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        return new NFTokenDMAFunction(contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    public Function removeApproveWithArrayFunction(List<BigInteger> _array) {
        final Function function = new Function(
                "removeApproveWithArray",
                Arrays.<Type>asList(new org.web3j.abi.datatypes.DynamicArray<org.web3j.abi.datatypes.generated.Uint256>(
                        org.web3j.abi.Utils.typeMap(_array, org.web3j.abi.datatypes.generated.Uint256.class))),
                Collections.<TypeReference<?>>emptyList());
        return function;
    }

    public Function setIsTransferFunction(BigInteger _tokenId, Boolean _istransfer) {
        final Function function = new Function(
                "setIsTransfer",
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(_tokenId),
                        new org.web3j.abi.datatypes.Bool(_istransfer)),
                Collections.<TypeReference<?>>emptyList());
        return function;
    }


    public Function transferFromFunction(String _from, String _to, BigInteger _tokenId) {
        final Function function = new Function(
                "transferFrom",
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(_from),
                        new org.web3j.abi.datatypes.Address(_to),
                        new org.web3j.abi.datatypes.generated.Uint256(_tokenId)),
                Collections.<TypeReference<?>>emptyList());
        return function;
    }


    public Function transferFromWithAarryFunction(String _from, String _to, List<BigInteger> _tokenIds) {
        final Function function = new Function(
                "transferFromWithAarry",
                Arrays.<Type>asList(new Address(_from),
                        new Address(_to),
                        new org.web3j.abi.datatypes.DynamicArray<Uint256>(
                                org.web3j.abi.Utils.typeMap(_tokenIds, Uint256.class))),
                Collections.<TypeReference<?>>emptyList());
        return function;
    }

    public Function approveWithAarryFunction(String _approved, List<BigInteger> _tokenArr) {
        final Function function = new Function(
                "approveWithArray",
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(_approved),
                        new org.web3j.abi.datatypes.DynamicArray<org.web3j.abi.datatypes.generated.Uint256>(
                                org.web3j.abi.Utils.typeMap(_tokenArr, org.web3j.abi.datatypes.generated.Uint256.class))),
                Collections.<TypeReference<?>>emptyList());
        return function;
    }


    public Function killFunction() {
        final Function function = new Function(
                "kill",
                Arrays.<Type>asList(),
                Collections.<TypeReference<?>>emptyList());
        return function;
    }


    public Function revokeApproveFunction(BigInteger _tokenId) {
        final Function function = new Function(
                "revokeApprove",
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(_tokenId)),
                Collections.<TypeReference<?>>emptyList());
        return function;
    }


    public Function setStatusFunction(BigInteger _tokenId, BigInteger _status) {
        final Function function = new Function(
                "setStatus",
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(_tokenId),
                        new org.web3j.abi.datatypes.generated.Uint256(_status)),
                Collections.<TypeReference<?>>emptyList());
        return function;
    }


    public Function setUserFunction(BigInteger _tokenId, String _user) {
        final Function function = new Function(
                "setUser",
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(_tokenId),
                        new org.web3j.abi.datatypes.Utf8String(_user)),
                Collections.<TypeReference<?>>emptyList());
        return function;
    }


    public Function mintFunction(String _to, BigInteger _tokenId, String _uri, Boolean _isTransfer, Boolean _isBurn) {
        final Function function = new Function(
                "mintAsync",
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(_to),
                        new org.web3j.abi.datatypes.generated.Uint256(_tokenId),
                        new org.web3j.abi.datatypes.Utf8String(_uri),
                        new org.web3j.abi.datatypes.Bool(_isTransfer),
                        new org.web3j.abi.datatypes.Bool(_isBurn)),
                Collections.<TypeReference<?>>emptyList());
        return function;
    }

    public Function burnFunction(String _owner, BigInteger _tokenId) {
        final Function function = new Function(
                "burn",
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(_owner),
                        new org.web3j.abi.datatypes.generated.Uint256(_tokenId)),
                Collections.<TypeReference<?>>emptyList());
        return function;
    }

    public Function removeMultiApproveFunction(BigInteger _tokenId, BigInteger _count) {
        final Function function = new Function(
                "removeMultiApprove",
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(_tokenId),
                        new org.web3j.abi.datatypes.generated.Uint256(_count)),
                Collections.<TypeReference<?>>emptyList());
        return function;
    }


    public Function setApprovalForAllFunction(String _operator, Boolean _approved) {
        final Function function = new Function(
                "setApprovalForAll",
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(_operator),
                        new org.web3j.abi.datatypes.Bool(_approved)),
                Collections.<TypeReference<?>>emptyList());
        return function;
    }

    public Function setMetadataFunction(String _metadata) {
        final Function function = new Function(
                "setMetadata",
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Utf8String(_metadata)),
                Collections.<TypeReference<?>>emptyList());
        return function;
    }

    public Function mintMultiFunction(String _to, BigInteger _tokenId, BigInteger _count, String _uri, Boolean _isTransfer, Boolean _isBurn) {
        final Function function = new Function(
                "mintMulti",
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(_to),
                        new org.web3j.abi.datatypes.generated.Uint256(_tokenId),
                        new org.web3j.abi.datatypes.generated.Uint256(_count),
                        new org.web3j.abi.datatypes.Utf8String(_uri),
                        new org.web3j.abi.datatypes.Bool(_isTransfer),
                        new org.web3j.abi.datatypes.Bool(_isBurn)),
                Collections.<TypeReference<?>>emptyList());
        return function;
    }


    public Function mintWithArrayFunction(String _to, List<BigInteger> _array, String _uri, Boolean _isTransfer, Boolean _isBurn) {
        final Function function = new Function(
                "mintWithArray",
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(_to),
                        new org.web3j.abi.datatypes.DynamicArray<org.web3j.abi.datatypes.generated.Uint256>(
                                org.web3j.abi.Utils.typeMap(_array, org.web3j.abi.datatypes.generated.Uint256.class)),
                        new org.web3j.abi.datatypes.Utf8String(_uri),
                        new org.web3j.abi.datatypes.Bool(_isTransfer),
                        new org.web3j.abi.datatypes.Bool(_isBurn)),
                Collections.<TypeReference<?>>emptyList());
        return function;
    }


    public Function approveFunction(String _approved, BigInteger _tokenId) {
        final Function function = new Function(
                "approve",
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(_approved),
                        new org.web3j.abi.datatypes.generated.Uint256(_tokenId)),
                Collections.<TypeReference<?>>emptyList());
        return function;
    }

    public Function approveMultiFunction(String _approved, BigInteger _tokenId, BigInteger _count) {
        final Function function = new Function(
                "approveMulti",
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(_approved),
                        new org.web3j.abi.datatypes.generated.Uint256(_tokenId),
                        new org.web3j.abi.datatypes.generated.Uint256(_count)),
                Collections.<TypeReference<?>>emptyList());
        return function;
    }


    public Function transferOwnershipFunction(String _newOwner) {
        final Function function = new Function(
                "transferOwnership",
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(_newOwner)),
                Collections.<TypeReference<?>>emptyList());
        return function;
    }
}
