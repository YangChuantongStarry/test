package org.elastos.dma.base.exchange;

import org.elastos.dma.base.util.contract.ContractUtils;
import org.elastos.dma.base.util.contract.TokenDMA;
import org.elastos.dma.base.util.contract.TokenDMAFunction;
import org.web3j.abi.datatypes.Function;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.JsonRpc2_0Web3j;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.protocol.http.HttpService;
import org.web3j.tx.ClientTransactionManager;
import org.web3j.tx.TransactionManager;
import org.web3j.utils.Convert;

import java.io.IOException;
import java.math.BigInteger;
import java.util.concurrent.ExecutionException;

/**
 * @Author: yangchuantong
 * @Description:
 * @Date:Created in  2019/1/2 18:24
 */
public class TokenContract {

    private Web3j web3j;

    public TokenContract(String nodeUrl) {
        HttpService web3jService = new HttpService(nodeUrl);
        this.web3j = new JsonRpc2_0Web3j(web3jService);
    }


    /**
     * 授权代币
     *
     * @param contractAddress 合约地址
     * @param privateKey      私钥
     * @param _to             接收地址
     * @param _value          数量
     * @param gasLimit        gas限制
     * @param gasPrice        gas价格
     * @return String
     */
    public String approve(String contractAddress, String privateKey, String _to, String _value, BigInteger gasPrice, BigInteger gasLimit) throws Exception {

        Credentials credentials = Credentials.create(privateKey);
        TokenDMAFunction tokenDMA = load(contractAddress, privateKey, gasPrice, gasLimit);
        Function function = tokenDMA.approveFunction(_to, Convert.toWei(_value, Convert.Unit.ETHER).toBigInteger());
        String hash = ContractUtils.getHashByFunction(web3j, function, credentials, contractAddress, gasPrice, gasLimit);

        return hash;

    }


    /**
     * 授权代币
     *
     * @param contractAddress 合约地址
     * @param privateKey      私钥
     * @param _to             接收地址
     * @param _value          数量
     * @param gasLimit        gas限制
     * @param gasPrice        gas价格
     * @return TransactionReceipt
     */
    public TransactionReceipt approveReceipt(String contractAddress, String privateKey, String _to, String _value, BigInteger gasPrice, BigInteger gasLimit) throws Exception {
        TokenDMA tokenDMA = load(contractAddress, privateKey, gasPrice, gasLimit);
        TransactionReceipt receipt = tokenDMA.approve(_to, Convert.toWei(_value, Convert.Unit.ETHER).toBigInteger()).send();
        return receipt;
    }


    /**
     * 解除授权代币
     *
     * @param contractAddress 合约地址
     * @param privateKey      私钥
     * @param approveAddress  授权地址
     * @param _value          数量
     * @param gasLimit        gas限制
     * @param gasPrice        gas价格
     * @return String
     */
    public String revokeApprove(String contractAddress, String privateKey, String approveAddress, String _value, BigInteger gasPrice, BigInteger gasLimit) throws Exception {

        Credentials credentials = Credentials.create(privateKey);
        TokenDMAFunction tokenDMA = load(contractAddress, privateKey, gasPrice, gasLimit);
        Function function = tokenDMA.revokeApproveFunction(approveAddress, Convert.toWei(_value, Convert.Unit.ETHER).toBigInteger());
        String hash = ContractUtils.getHashByFunction(web3j, function, credentials, contractAddress, gasPrice, gasLimit);

        return hash;

    }


    /**
     * 解除授权代币
     *
     * @param contractAddress 合约地址
     * @param privateKey      私钥
     * @param approveAddress  授权地址
     * @param _value          数量
     * @param gasLimit        gas限制
     * @param gasPrice        gas价格
     * @return TransactionReceipt
     */
    public TransactionReceipt revokeApproveReceipt(String contractAddress, String privateKey, String approveAddress, String _value, BigInteger gasPrice, BigInteger gasLimit) throws Exception {
        TokenDMA tokenDMA = load(contractAddress, privateKey, gasPrice, gasLimit);
        TransactionReceipt receipt = tokenDMA.revokeApprove(approveAddress, Convert.toWei(_value, Convert.Unit.ETHER).toBigInteger()).send();
        return receipt;
    }


    /**
     * 获取授权金额
     *
     * @param contractAddress 合约地址
     * @param privateKey      私钥
     * @param owner           代币拥有者地址
     * @param spender         代币授权者地址
     * @param gasLimit        gas限制
     * @param gasPrice        gas价格
     * @return 授权金额
     */
    public String allowance(String contractAddress, String privateKey, String owner, String spender, BigInteger gasPrice, BigInteger gasLimit) throws Exception {
        TokenDMA tokenDMA = load(contractAddress, privateKey, gasPrice, gasLimit);
        BigInteger value = tokenDMA.allowance(owner, spender).send();
        return Convert.fromWei(value.toString(), Convert.Unit.ETHER).toPlainString();
    }


    /**
     * 授权代币转账
     *
     * @param contractAddress 合约地址
     * @param privateKey      私钥
     * @param _from           代币拥有者
     * @param _to             接收地址
     * @param _value          数量
     * @param gasLimit        gas限制
     * @param gasPrice        gas价格
     * @return String
     */
    public String transferFrom(String contractAddress, String privateKey, String _from, String _to, String _value, BigInteger gasPrice, BigInteger gasLimit) throws ExecutionException, InterruptedException, IOException {

        Credentials credentials = Credentials.create(privateKey);
        TokenDMAFunction tokenDMA = load(contractAddress, privateKey, gasPrice, gasLimit);
        Function function = tokenDMA.transferFromFunction(_from, _to, Convert.toWei(_value, Convert.Unit.ETHER).toBigInteger());
        String hash = ContractUtils.getHashByFunction(web3j, function, credentials, contractAddress, gasPrice, gasLimit);

        return hash;

    }


    /**
     * 授权代币转账
     *
     * @param contractAddress 合约地址
     * @param privateKey      私钥
     * @param _from           代币拥有者
     * @param _to             接收地址
     * @param _value          数量
     * @param gasLimit        gas限制
     * @param gasPrice        gas价格
     * @return TransactionReceipt
     */
    public TransactionReceipt transferFromReceipt(String contractAddress, String privateKey, String _from, String _to, String _value, BigInteger gasPrice, BigInteger gasLimit) throws ExecutionException, InterruptedException {
        TokenDMA tokenDMA = load(contractAddress, privateKey, gasPrice, gasLimit);
        TransactionReceipt receipt = tokenDMA.transferFrom(_from, _to, Convert.toWei(_value, Convert.Unit.ETHER).toBigInteger()).sendAsync().get();
        return receipt;
    }


    /**
     * 代币增发
     *
     * @param contractAddress 合约地址
     * @param privateKey      私钥
     * @param _to             接收地址
     * @param _value          数量
     * @param gasLimit        gas限制
     * @param gasPrice        gas价格
     * @return String
     */
    public String addIssue(String contractAddress, String privateKey, String _to, String _value, BigInteger gasPrice, BigInteger gasLimit) throws Exception {

        Credentials credentials = Credentials.create(privateKey);
        TokenDMAFunction tokenDMA = load(contractAddress, privateKey, gasPrice, gasLimit);
        Function function = tokenDMA.addIssueFunction(_to, Convert.toWei(_value, Convert.Unit.ETHER).toBigInteger());
        String hash = ContractUtils.getHashByFunction(web3j, function, credentials, contractAddress, gasPrice, gasLimit);

        return hash;

    }


    /**
     * 代币增发
     *
     * @param contractAddress 合约地址
     * @param privateKey      私钥
     * @param _to             接收地址
     * @param _value          数量
     * @param gasLimit        gas限制
     * @param gasPrice        gas价格
     * @return TransactionReceipt
     */
    public TransactionReceipt addIssueReceipt(String contractAddress, String privateKey, String _to, String _value, BigInteger gasPrice, BigInteger gasLimit) throws Exception {
        TokenDMA tokenDMA = load(contractAddress, privateKey, gasPrice, gasLimit);
        TransactionReceipt receipt = tokenDMA.addIssue(_to, Convert.toWei(_value, Convert.Unit.ETHER).toBigInteger()).send();
        return receipt;
    }

    /**
     * 发行总量
     *
     * @param contractAddress 合约地址
     * @return BigInteger
     */
    public BigInteger totalSupply(String contractAddress) throws Exception {
        TokenDMAFunction tokenDMA = load(contractAddress, web3j);
        BigInteger totalSupply = tokenDMA.totalSupply().send();
        return totalSupply;
    }


    /**
     * 销毁合约
     *
     * @param contractAddress 合约地址
     * @param privateKey      私钥
     * @param gasLimit        gas限制
     * @param gasPrice        gas价格
     * @return String
     */
    public String kill(String contractAddress, String privateKey, BigInteger gasPrice, BigInteger gasLimit) throws Exception {

        Credentials credentials = Credentials.create(privateKey);
        TokenDMAFunction tokenDMA = load(contractAddress, privateKey, gasPrice, gasLimit);
        Function function = tokenDMA.killFunction();
        String hash = ContractUtils.getHashByFunction(web3j, function, credentials, contractAddress, gasPrice, gasLimit);

        return hash;

    }


    /**
     * 销毁合约
     *
     * @param contractAddress 合约地址
     * @param privateKey      私钥
     * @param gasLimit        gas限制
     * @param gasPrice        gas价格
     * @return TransactionReceipt
     */
    public TransactionReceipt killReceipt(String contractAddress, String privateKey, BigInteger gasPrice, BigInteger gasLimit) throws Exception {
        TokenDMA tokenDMA = load(contractAddress, privateKey, gasPrice, gasLimit);
        TransactionReceipt receipt = tokenDMA.kill().send();
        return receipt;
    }


    /**
     * 销毁代币
     *
     * @param contractAddress 合约地址
     * @param privateKey      私钥
     * @param gasLimit        gas限制
     * @param gasPrice        gas价格
     * @param _value          数量
     * @return String
     */
    public String burn(String contractAddress, String privateKey, BigInteger gasPrice, BigInteger gasLimit, String _value) throws Exception {

        Credentials credentials = Credentials.create(privateKey);
        TokenDMAFunction tokenDMA = load(contractAddress, privateKey, gasPrice, gasLimit);
        Function function = tokenDMA.burnFunction(Convert.toWei(_value, Convert.Unit.ETHER).toBigInteger());
        String hash = ContractUtils.getHashByFunction(web3j, function, credentials, contractAddress, gasPrice, gasLimit);

        return hash;

    }


    /**
     * 销毁代币
     *
     * @param contractAddress 合约地址
     * @param privateKey      私钥
     * @param gasLimit        gas限制
     * @param gasPrice        gas价格
     * @param _value          数量
     * @return TransactionReceipt
     */
    public TransactionReceipt burnReceipt(String contractAddress, String privateKey, BigInteger gasPrice, BigInteger gasLimit, String _value) throws Exception {
        TokenDMA tokenDMA = load(contractAddress, privateKey, gasPrice, gasLimit);
        TransactionReceipt receipt = tokenDMA.burn(Convert.toWei(_value, Convert.Unit.ETHER).toBigInteger()).send();
        return receipt;
    }


    private TokenDMAFunction load(String contractAddress, String privateKey, BigInteger gasPrice, BigInteger gasLimit) {
        Credentials credentials = Credentials.create(privateKey);
        TokenDMAFunction tokenDMA = TokenDMAFunction.load(contractAddress, web3j, credentials, gasPrice, gasLimit);
        return tokenDMA;
    }

    private TokenDMAFunction load(String contractAddress, Web3j web3j) {
        TransactionManager transactionManager = new ClientTransactionManager(web3j, null);
        TokenDMAFunction tokenDMAFunction = TokenDMAFunction.load(contractAddress, web3j, transactionManager, BigInteger.valueOf(9000000000L), BigInteger.valueOf(60000));
        return tokenDMAFunction;
    }

}
