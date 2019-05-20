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
 * @Date:Created in  2019/1/14 17:54
 */
public class TokenDMAFunction extends TokenDMA {
    public TokenDMAFunction(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        super(contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    public TokenDMAFunction(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        super(contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    public static TokenDMAFunction load(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        return new TokenDMAFunction(contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    public static TokenDMAFunction load(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        return new TokenDMAFunction(contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    public Function addIssueFunction(String _target, BigInteger _amount) {
        final Function function = new Function(
                "addIssue",
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(_target),
                        new org.web3j.abi.datatypes.generated.Uint256(_amount)),
                Collections.<TypeReference<?>>emptyList());
        return function;
    }


    public Function approveFunction(String _spender, BigInteger _value) {
        final Function function = new Function(
                "approve",
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(_spender),
                        new org.web3j.abi.datatypes.generated.Uint256(_value)),
                Collections.<TypeReference<?>>emptyList());
        return function;
    }


    public Function transferFromFunction(String _from, String _to, BigInteger _value) {
        final Function function = new Function(
                "transferFrom",
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(_from),
                        new org.web3j.abi.datatypes.Address(_to),
                        new org.web3j.abi.datatypes.generated.Uint256(_value)),
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


    public Function burnFunction(BigInteger _value) {
        final Function function = new Function(
                "burn",
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(_value)),
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


    public Function transferFunction(String _to, BigInteger _value) {
        final Function Function = new Function(
                "transfer",
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(_to),
                        new org.web3j.abi.datatypes.generated.Uint256(_value)),
                Collections.<TypeReference<?>>emptyList());
        return Function;
    }

    public Function revokeApproveFunction(String _owner, BigInteger _amount) {
        final Function function = new Function(
                "revokeApprove",
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(_owner),
                        new org.web3j.abi.datatypes.generated.Uint256(_amount)),
                Collections.<TypeReference<?>>emptyList());
        return function;
    }

}
