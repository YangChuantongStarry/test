package org.elastos.dma.base.exchange;

import org.elastos.dma.base.util.contract.ContractUtils;
import org.elastos.dma.base.util.contract.DMAPlatform;
import org.elastos.dma.base.util.contract.DMAPlatformFunction;
import org.elastos.dma.utility.dto.PlatFormApproveInfo;
import org.web3j.abi.datatypes.Function;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.JsonRpc2_0Web3j;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.protocol.http.HttpService;
import org.web3j.tuples.generated.Tuple3;
import org.web3j.tx.ClientTransactionManager;
import org.web3j.tx.TransactionManager;
import org.web3j.utils.Convert;

import java.math.BigInteger;
import java.util.List;

/**
 * @Author: yangchuantong
 * @Description:
 * @Date:Created in  2018/12/29 14:46
 */
public class PlatformContract {


    protected Web3j web3j;

    public PlatformContract(String nodeUrl) {
        HttpService web3jService = new HttpService(nodeUrl);
        this.web3j = new JsonRpc2_0Web3j(web3jService);
    }


    /**
     * 创建合约
     *
     * @param privateKey       私钥
     * @param gasPrice
     * @param gasLimit
     * @param token721         资产合约地址
     * @param token20          代币合约地址
     * @param _platformAddress 平台手续费接受地址
     * @param _firstExpenses   第一次交易费率
     * @param _secondExpenses  第二次以后交易费率
     * @return String 合约地址
     */
    public String deploy(String privateKey, BigInteger gasPrice, BigInteger gasLimit, String token721, String token20, String _platformAddress, BigInteger _firstExpenses, BigInteger _secondExpenses) throws Exception {
        Credentials credentials = Credentials.create(privateKey);
        DMAPlatform nfTokenDMA = DMAPlatform.deploy(web3j, credentials, gasPrice, gasLimit, token721, token20, _platformAddress, _firstExpenses, _secondExpenses).send();
        String contractAddress = nfTokenDMA.getContractAddress();
        return contractAddress;
    }

    /**
     * 上架
     *
     * @param contractAddress 合约地址
     * @param privateKey      私钥
     * @param gasPrice
     * @param gasLimit
     * @param value           总价值
     * @param _tokenId        资产id
     * @param owner           资产拥有者地址
     * @return
     */
    public String saveApprove(String contractAddress, String privateKey, BigInteger gasPrice, BigInteger gasLimit, BigInteger _tokenId, String value, String owner) throws Exception {
        Credentials credentials = Credentials.create(privateKey);

        DMAPlatformFunction dmaPlatform = load(contractAddress, web3j, credentials, gasPrice, gasLimit);
        Function function = dmaPlatform.saveApproveFunction(owner, _tokenId, Convert.toWei(value, Convert.Unit.ETHER).toBigInteger());
        String hash = ContractUtils.getHashByFunction(web3j, function, credentials, contractAddress, gasPrice, gasLimit);

        return hash;
    }


    /**
     * 上架
     *
     * @param contractAddress 合约地址
     * @param privateKey      私钥
     * @param gasPrice
     * @param gasLimit
     * @param value           总价值
     * @param _tokenId        资产id
     * @param owner           资产拥有者地址
     * @return
     */
    public TransactionReceipt saveApproveReceipt(String contractAddress, String privateKey, BigInteger gasPrice, BigInteger gasLimit, BigInteger _tokenId, String value, String owner) throws Exception {
        Credentials credentials = Credentials.create(privateKey);
        DMAPlatform dmaPlatform = load(contractAddress, web3j, credentials, gasPrice, gasLimit);
        TransactionReceipt transactionReceipt = dmaPlatform.saveApprove(owner, _tokenId, Convert.toWei(value, Convert.Unit.ETHER).toBigInteger()).sendAsync().get();
        return transactionReceipt;
    }


    /**
     * 批量上架
     *
     * @param contractAddress 合约地址
     * @param privateKey      私钥
     * @param gasPrice
     * @param gasLimit
     * @param value           总价值
     * @param _tokenId        资产id
     * @param owner           资产拥有者地址
     * @param count           上架数量
     * @return
     */
    public String saveMultiApprove(String contractAddress, String privateKey, BigInteger gasPrice, BigInteger gasLimit, BigInteger _tokenId, String value, String owner, BigInteger count) throws Exception {
        Credentials credentials = Credentials.create(privateKey);

        DMAPlatformFunction dmaPlatform = load(contractAddress, web3j, credentials, gasPrice, gasLimit);
        Function function = dmaPlatform.saveMultiApproveFunction(owner, _tokenId, count, Convert.toWei(value, Convert.Unit.ETHER).toBigInteger());
        String hash = ContractUtils.getHashByFunction(web3j, function, credentials, contractAddress, gasPrice, gasLimit);

        return hash;
    }


    /**
     * 批量上架
     *
     * @param contractAddress 合约地址
     * @param privateKey      私钥
     * @param gasPrice
     * @param gasLimit
     * @param value           总价值
     * @param tokenIds        资产id数组
     * @param owner           资产拥有者地址
     * @return
     */
    public String saveApproveWithArray(String contractAddress, String privateKey, BigInteger gasPrice, BigInteger gasLimit, List<BigInteger> tokenIds, String value, String owner) throws Exception {
        Credentials credentials = Credentials.create(privateKey);
        DMAPlatformFunction dmaPlatform = load(contractAddress, web3j, credentials, gasPrice, gasLimit);
        Function function = dmaPlatform.saveApproveWithArrayFunction(owner, tokenIds, Convert.toWei(value, Convert.Unit.ETHER).toBigInteger());
        String hash = ContractUtils.getHashByFunction(web3j, function, credentials, contractAddress, gasPrice, gasLimit);

        return hash;
    }


    /**
     * 批量上架
     *
     * @param contractAddress 合约地址
     * @param privateKey      私钥
     * @param gasPrice
     * @param gasLimit
     * @param value           总价值
     * @param tokenIds        资产id数组
     * @param owner           资产拥有者地址
     * @return
     */
    public TransactionReceipt saveApproveWithArrayReceipt(String contractAddress, String privateKey, BigInteger gasPrice, BigInteger gasLimit, List<BigInteger> tokenIds, String value, String owner) throws Exception {
        Credentials credentials = Credentials.create(privateKey);
        DMAPlatform dmaPlatform = load(contractAddress, web3j, credentials, gasPrice, gasLimit);
        TransactionReceipt transactionReceipt = dmaPlatform.saveApproveWithArray(owner, tokenIds, Convert.toWei(value, Convert.Unit.ETHER).toBigInteger()).sendAsync().get();
        return transactionReceipt;
    }


    /**
     * 下架
     *
     * @param contractAddress 合约地址
     * @param privateKey      私钥
     * @param gasPrice
     * @param gasLimit
     * @param count           数量
     * @param _tokenId        资产id
     * @param _owner          资产拥有者地址
     * @return
     */
    public String revokeApprove(String contractAddress, String privateKey, BigInteger gasPrice, BigInteger gasLimit, BigInteger _tokenId, BigInteger count, String _owner) throws Exception {
        Credentials credentials = Credentials.create(privateKey);

        DMAPlatformFunction dmaPlatform = load(contractAddress, web3j, credentials, gasPrice, gasLimit);
        Function function = dmaPlatform.revokeApproveFunction(_owner, _tokenId, count);
        String hash = ContractUtils.getHashByFunction(web3j, function, credentials, contractAddress, gasPrice, gasLimit);

        return hash;
    }


    /**
     * 下架
     *
     * @param contractAddress 合约地址
     * @param privateKey      私钥
     * @param gasPrice
     * @param gasLimit
     * @param _tokenId        资产id
     * @return
     */
    public TransactionReceipt revokeApproveReceipt(String contractAddress, String privateKey, BigInteger gasPrice, BigInteger gasLimit, BigInteger _tokenId) throws Exception {
        Credentials credentials = Credentials.create(privateKey);
        DMAPlatform dmaPlatform = load(contractAddress, web3j, credentials, gasPrice, gasLimit);
        TransactionReceipt transactionReceipt = dmaPlatform.revokeApprove(_tokenId).sendAsync().get();
        return transactionReceipt;
    }


    /**
     * 批量下架
     *
     * @param contractAddress 合约地址
     * @param privateKey      私钥
     * @param gasPrice
     * @param gasLimit
     * @param tokenIds        资产id数组
     * @return
     */
    public String revokeApprovesWithArray(String contractAddress, String privateKey, BigInteger gasPrice, BigInteger gasLimit, List<BigInteger> tokenIds) throws Exception {
        Credentials credentials = Credentials.create(privateKey);
        DMAPlatformFunction dmaPlatform = load(contractAddress, web3j, credentials, gasPrice, gasLimit);
        Function function = dmaPlatform.revokeApprovesWithArrayFunction(tokenIds);
        String hash = ContractUtils.getHashByFunction(web3j, function, credentials, contractAddress, gasPrice, gasLimit);
        return hash;
    }


    /**
     * 批量下架
     *
     * @param contractAddress 合约地址
     * @param privateKey      私钥
     * @param gasPrice
     * @param gasLimit
     * @param tokenIds        资产id数组
     * @return
     */
    public TransactionReceipt revokeApprovesWithArrayReceipt(String contractAddress, String privateKey, BigInteger gasPrice, BigInteger gasLimit, List<BigInteger> tokenIds) throws Exception {
        Credentials credentials = Credentials.create(privateKey);
        DMAPlatform nfTokenDMA = load(contractAddress, web3j, credentials, gasPrice, gasLimit);
        TransactionReceipt transactionReceipt = nfTokenDMA.revokeApprovesWithArray(tokenIds).sendAsync().get();
        return transactionReceipt;
    }


    /**
     * 购买
     *
     * @param contractAddress 合约地址
     * @param privateKey      私钥
     * @param gasPrice
     * @param gasLimit
     * @param count           数量
     * @param _tokenId        资产id
     * @param value           金额
     * @param _owner          资产拥有者
     * @return
     */
    public String transfer(String contractAddress, String privateKey, BigInteger gasPrice, BigInteger gasLimit, BigInteger _tokenId, BigInteger count, String value, String _owner) throws Exception {
        Credentials credentials = Credentials.create(privateKey);
        DMAPlatformFunction dmaPlatform = load(contractAddress, web3j, credentials, gasPrice, gasLimit);
        Function function = dmaPlatform.transferFunction(_owner,
                _tokenId, count, Convert.toWei(value, Convert.Unit.ETHER).toBigInteger());
        String hash = ContractUtils.getHashByFunction(web3j, function, credentials, contractAddress, gasPrice, gasLimit);

        return hash;
    }


    /**
     * 购买
     *
     * @param contractAddress 合约地址
     * @param privateKey      私钥
     * @param gasPrice
     * @param gasLimit
     * @param _tokenId        资产id
     * @param value           金额
     * @param _owner          资产拥有者
     * @return
     */
    public TransactionReceipt transferReceipt(String contractAddress, String privateKey, BigInteger gasPrice, BigInteger gasLimit, BigInteger _tokenId, String value, String _owner) throws Exception {
        Credentials credentials = Credentials.create(privateKey);
        DMAPlatform nfTokenDMA = load(contractAddress, web3j, credentials, gasPrice, gasLimit);
        TransactionReceipt transactionReceipt = nfTokenDMA.transfer(_owner,
                _tokenId, Convert.toWei(value, Convert.Unit.ETHER).toBigInteger()).sendAsync().get();
        return transactionReceipt;
    }


    /**
     * 购买
     *
     * @param contractAddress 合约地址
     * @param privateKey      私钥
     * @param gasPrice
     * @param gasLimit
     * @param tokenIds        资产id
     * @param value           金额
     * @param _owner          资产拥有者地址
     * @return
     */
    public String transferWithArray(String contractAddress, String privateKey, BigInteger gasPrice, BigInteger gasLimit, List<BigInteger> tokenIds, String value, String _owner) throws Exception {
        Credentials credentials = Credentials.create(privateKey);
        DMAPlatformFunction dmaPlatform = load(contractAddress, web3j, credentials, gasPrice, gasLimit);
        Function function = dmaPlatform.transferWithArrayFunction(_owner, tokenIds, Convert.toWei(value, Convert.Unit.ETHER).toBigInteger());
        String hash = ContractUtils.getHashByFunction(web3j, function, credentials, contractAddress, gasPrice, gasLimit);

        return hash;
    }


    /**
     * 购买
     *
     * @param contractAddress 合约地址
     * @param privateKey      私钥
     * @param gasPrice
     * @param gasLimit
     * @param tokenIds        资产id
     * @param value           金额
     * @param _owner          资产拥有者地址
     * @return
     */
    public TransactionReceipt transferWithArrayReceipt(String contractAddress, String privateKey, BigInteger gasPrice, BigInteger gasLimit, List<BigInteger> tokenIds, String value, String _owner) throws Exception {
        Credentials credentials = Credentials.create(privateKey);
        DMAPlatform nfTokenDMA = load(contractAddress, web3j, credentials, gasPrice, gasLimit);
        TransactionReceipt transactionReceipt = nfTokenDMA.transferWithArray(_owner, tokenIds, Convert.toWei(value, Convert.Unit.ETHER).toBigInteger()).sendAsync().get();
        return transactionReceipt;
    }


    /**
     * 获取上架资产数
     *
     * @param contractAddress 合约地址
     * @param _owner          上架人地址
     * @return BigInteger
     */
    public BigInteger getAssetCnt(String contractAddress, String _owner) throws Exception {
        DMAPlatformFunction nfTokenDMA = load(contractAddress, web3j);
        BigInteger cnt = nfTokenDMA.getAssetCnt(_owner).send();
        return cnt;

    }


    /**
     * 获取上架资产信息
     *
     * @param contractAddress 合约地址
     * @param tokenid         tokenid
     * @return BigInteger
     */
    public PlatFormApproveInfo getApproveinfo(String contractAddress, BigInteger tokenid) throws Exception {
        DMAPlatformFunction nfTokenDMA = load(contractAddress, web3j);
        Tuple3<String, BigInteger, BigInteger> tuple3 = nfTokenDMA.getApproveinfo(tokenid).send();
        PlatFormApproveInfo platFormApproveInfo = new PlatFormApproveInfo();
        platFormApproveInfo.setOwner(tuple3.getValue1());
        platFormApproveInfo.setTokenid(tuple3.getValue2());
        platFormApproveInfo.setValue(Convert.fromWei(tuple3.getValue3().toString(), Convert.Unit.ETHER).toPlainString());
        return platFormApproveInfo;

    }


    private DMAPlatformFunction load(String contractAddress, Web3j web3j) {
        TransactionManager transactionManager = new ClientTransactionManager(web3j, null);
        DMAPlatformFunction dmaPlatformFunction = DMAPlatformFunction.load(contractAddress, web3j, transactionManager, BigInteger.valueOf(9000000000L), BigInteger.valueOf(60000));
        return dmaPlatformFunction;
    }

    private DMAPlatformFunction load(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        DMAPlatformFunction dmaPlatformFunction = DMAPlatformFunction.load(contractAddress, web3j, credentials, gasPrice, gasLimit);
        return dmaPlatformFunction;
    }




}
