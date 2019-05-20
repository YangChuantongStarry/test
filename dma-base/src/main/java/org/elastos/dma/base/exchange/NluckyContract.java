package org.elastos.dma.base.exchange;


import org.elastos.dma.base.util.contract.ContractUtils;
import org.elastos.dma.base.util.contract.Nlucky;
import org.elastos.dma.base.util.contract.NluckyFunction;
import org.elastos.dma.utility.dto.BetInfoDto;
import org.web3j.abi.datatypes.Function;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.JsonRpc2_0Web3j;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.protocol.http.HttpService;
import org.web3j.tuples.generated.Tuple2;
import org.web3j.tuples.generated.Tuple3;
import org.web3j.tx.ClientTransactionManager;
import org.web3j.tx.TransactionManager;

import java.math.BigInteger;

/**
 * @Author: yangchuantong
 * @Description: n元购销售合约
 * @Date:Created in  2019/1/23 10:29
 */
public class NluckyContract {

    protected Web3j web3j;

    public NluckyContract(String nodeUrl) {
        HttpService web3jService = new HttpService(nodeUrl);
        this.web3j = new JsonRpc2_0Web3j(web3jService);
    }


    /**
     * 创建合约
     *
     * @param privateKey     私钥
     * @param gasPrice
     * @param gasLimit
     * @param token721       资产合约地址
     * @param token20        代币合约地址
     * @param _tokenId       tokenid
     * @param _totalPortions 总人参与数
     * @param _price         每次参与价格
     * @param _endTimestamp  结束时间戳
     * @return String 合约地址
     */
    public String deploy(String privateKey, BigInteger gasPrice, BigInteger gasLimit, String token721, String token20, BigInteger _tokenId, BigInteger _totalPortions, BigInteger _price, BigInteger _endTimestamp) throws Exception {
        Credentials credentials = Credentials.create(privateKey);
        Nlucky auction = Nlucky.deploy(web3j, credentials, gasPrice, gasLimit, token721, token20, _tokenId, _totalPortions, _price, _endTimestamp).send();
        String contractAddress = auction.getContractAddress();
        return contractAddress;
    }


    /**
     * 得到合约拥有者地址
     *
     * @param contractAddress 合约地址
     * @return String
     */
    public String owner(String contractAddress) throws Exception {
        NluckyFunction auctionFunction = load(contractAddress, web3j);
        String owner = auctionFunction.owner().send();
        return owner;
    }


    /**
     * 获取活动信息
     *
     * @param contractAddress 合约地址
     * @return String
     */
    public BetInfoDto getBetInfo(String contractAddress) throws Exception {
        NluckyFunction auctionFunction = load(contractAddress, web3j);
        Tuple3<BigInteger, Boolean, String> tuple = auctionFunction.getBetInfo().send();
        BetInfoDto betInfoDto = new BetInfoDto();
        betInfoDto.setCount(tuple.getValue1());
        betInfoDto.setIsFinished(tuple.getValue2());
        betInfoDto.setLuckyAddress(tuple.getValue3());
        return betInfoDto;
    }


    /**
     * 获取结束时间
     *
     * @param contractAddress 合约地址
     * @return String
     */
    public BigInteger getEndTimeStamp(String contractAddress) throws Exception {
        NluckyFunction auctionFunction = load(contractAddress, web3j);
        Tuple2<BigInteger, BigInteger> tuple = auctionFunction.getEndTimeStamp().send();
        BigInteger endTimeStamp = tuple.getValue2();
        return endTimeStamp;
    }

    /**
     * 参加活动
     *
     * @param contractAddress 合约地址
     * @param privateKey      私钥
     * @param gasPrice
     * @param gasLimit
     * @return TransactionReceipt
     */
    public TransactionReceipt betReceipt(String contractAddress, String privateKey, BigInteger gasPrice, BigInteger gasLimit) throws Exception {
        Credentials credentials = Credentials.create(privateKey);
        NluckyFunction auctionFunction = load(contractAddress, web3j, credentials, gasPrice, gasLimit);
        TransactionReceipt transactionReceipt = auctionFunction.bet().send();
        return transactionReceipt;
    }

    /**
     * 参加活动,达到目标自动完成交易
     *
     * @param contractAddress 合约地址
     * @param privateKey      私钥
     * @param gasPrice
     * @param gasLimit
     * @return String
     */
    public String bet(String contractAddress, String privateKey, BigInteger gasPrice, BigInteger gasLimit) throws Exception {
        Credentials credentials = Credentials.create(privateKey);
        NluckyFunction auctionFunction = load(contractAddress, web3j, credentials, gasPrice, gasLimit);
        Function function = auctionFunction.betFunction();
        String hash = ContractUtils.getHashByFunction(web3j, function, credentials, contractAddress, gasPrice, gasLimit);
        return hash;
    }


    /**
     * 参加未达标，退还金额
     *
     * @param contractAddress 合约地址
     * @param privateKey      私钥
     * @param gasPrice
     * @param gasLimit
     * @param count           退款数量
     * @return TransactionReceipt
     */
    public TransactionReceipt refundReceipt(String contractAddress, String privateKey, BigInteger gasPrice, BigInteger gasLimit, BigInteger count) throws Exception {
        Credentials credentials = Credentials.create(privateKey);
        NluckyFunction auctionFunction = load(contractAddress, web3j, credentials, gasPrice, gasLimit);
        TransactionReceipt transactionReceipt = auctionFunction.fails(count).send();
        return transactionReceipt;
    }

    /**
     * 参加未达标，退还金额
     *
     * @param contractAddress 合约地址
     * @param privateKey      私钥
     * @param gasPrice
     * @param gasLimit
     * @param count           退款数量
     * @return String
     */
    public String refund(String contractAddress, String privateKey, BigInteger gasPrice, BigInteger gasLimit, BigInteger count) throws Exception {
        Credentials credentials = Credentials.create(privateKey);
        NluckyFunction auctionFunction = load(contractAddress, web3j, credentials, gasPrice, gasLimit);
        Function function = auctionFunction.failsFunction(count);
        String hash = ContractUtils.getHashByFunction(web3j, function, credentials, contractAddress, gasPrice, gasLimit);
        return hash;
    }









    private NluckyFunction load(String contractAddress, Web3j web3j) {
        TransactionManager transactionManager = new ClientTransactionManager(web3j, null);
        NluckyFunction nluckyFunction = NluckyFunction.load(contractAddress, web3j, transactionManager, BigInteger.valueOf(9000000000L), BigInteger.valueOf(60000));
        return nluckyFunction;
    }

    private NluckyFunction load(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        NluckyFunction nluckyFunction = NluckyFunction.load(contractAddress, web3j, credentials, gasPrice, gasLimit);
        return nluckyFunction;
    }



}
