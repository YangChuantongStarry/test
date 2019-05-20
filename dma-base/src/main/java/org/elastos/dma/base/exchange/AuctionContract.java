package org.elastos.dma.base.exchange;

import org.elastos.dma.base.util.contract.Auction;
import org.elastos.dma.base.util.contract.AuctionFunction;
import org.elastos.dma.base.util.contract.ContractUtils;
import org.elastos.dma.utility.dto.BidInfoDto;
import org.web3j.abi.datatypes.Function;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.JsonRpc2_0Web3j;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.protocol.http.HttpService;
import org.web3j.tuples.generated.Tuple2;
import org.web3j.tuples.generated.Tuple4;
import org.web3j.tx.ClientTransactionManager;
import org.web3j.tx.TransactionManager;
import org.web3j.utils.Convert;

import java.math.BigInteger;

/**
 * @Author: yangchuantong
 * @Description:拍卖合约
 * @Date:Created in  2019/1/22 21:55
 */
public class AuctionContract {

    protected Web3j web3j;

    public AuctionContract(String nodeUrl) {
        HttpService web3jService = new HttpService(nodeUrl);
        this.web3j = new JsonRpc2_0Web3j(web3jService);
    }


    /**
     * 创建合约
     *
     * @param privateKey    私钥
     * @param gasPrice
     * @param gasLimit
     * @param token721      资产合约地址
     * @param token20       代币合约地址
     * @param _tokenId      tokenid
     * @param _lowestValue  起拍价
     * @param _closingValue 成交价
     * @param _endTimestamp 结束时间戳
     * @return String 合约地址
     */
    public String deploy(String privateKey, BigInteger gasPrice, BigInteger gasLimit, String token721, String token20, BigInteger _tokenId, BigInteger _lowestValue, BigInteger _closingValue, BigInteger _endTimestamp) throws Exception {
        Credentials credentials = Credentials.create(privateKey);
        Auction auction = Auction.deploy(web3j, credentials, gasPrice, gasLimit, token721, token20, _tokenId, _lowestValue, _closingValue, _endTimestamp).send();
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
        AuctionFunction auctionFunction = load(contractAddress, web3j);
        String owner = auctionFunction.owner().send();
        return owner;
    }

    /**
     * 获取拍卖结束时间戳
     *
     * @param contractAddress 合约地址
     * @return BigInteger
     */
    public BigInteger getEndTimeStamp(String contractAddress) throws Exception {
        AuctionFunction auctionFunction = load(contractAddress, web3j);
        Tuple2<BigInteger, BigInteger> tuple2 = auctionFunction.getEndTimeStamp().send();
        BigInteger endTimeStamp = tuple2.getValue2();
        return endTimeStamp;
    }

    /**
     * 获取拍卖信息
     *
     * @param contractAddress 合约地址
     * @return BidInfo
     */
    public BidInfoDto getBidInfo(String contractAddress) throws Exception {
        AuctionFunction auctionFunction = load(contractAddress, web3j);
        Tuple4<BigInteger, String, BigInteger, Boolean> tuple = auctionFunction.getBidInfo().send();
        BidInfoDto bidInfo = new BidInfoDto();
        bidInfo.setBidValue(Convert.fromWei(tuple.getValue1().toString(), Convert.Unit.ETHER).toPlainString());
        bidInfo.setBidAddress(tuple.getValue2());
        bidInfo.setEndTime(tuple.getValue3());
        bidInfo.setIsFinished(tuple.getValue4());
        return bidInfo;
    }


    /**
     * 设置拍卖结束时间戳
     *
     * @param contractAddress 合约地址
     * @param privateKey      私钥
     * @param gasPrice
     * @param gasLimit
     * @param endTime         结束时间
     * @return TransactionReceipt
     */
    public TransactionReceipt setEndTimestampStampReceipt(String contractAddress, String privateKey, BigInteger gasPrice, BigInteger gasLimit, BigInteger endTime) throws Exception {
        Credentials credentials = Credentials.create(privateKey);
        AuctionFunction auctionFunction = load(contractAddress, web3j, credentials, gasPrice, gasLimit);
        TransactionReceipt transactionReceipt = auctionFunction.setEndTimestamp(endTime).send();
        return transactionReceipt;
    }


    /**
     * 设置拍卖结束时间戳
     *
     * @param contractAddress 合约地址
     * @param privateKey      私钥
     * @param gasPrice
     * @param gasLimit
     * @param endTime         结束时间
     * @return String
     */
    public String setEndTimestampStamp(String contractAddress, String privateKey, BigInteger gasPrice, BigInteger gasLimit, BigInteger endTime) throws Exception {
        Credentials credentials = Credentials.create(privateKey);
        AuctionFunction auctionFunction = load(contractAddress, web3j, credentials, gasPrice, gasLimit);
        Function function = auctionFunction.setEndTimestampFunction(endTime);
        String hash = ContractUtils.getHashByFunction(web3j, function, credentials, contractAddress, gasPrice, gasLimit);
        return hash;
    }


    /**
     * 竞拍
     *
     * @param contractAddress 合约地址
     * @param privateKey      私钥
     * @param gasPrice
     * @param gasLimit
     * @param _bidValue       竞拍价格
     * @return TransactionReceipt
     */
    public TransactionReceipt bidReceipt(String contractAddress, String privateKey, BigInteger gasPrice, BigInteger gasLimit, String _bidValue) throws Exception {
        Credentials credentials = Credentials.create(privateKey);
        AuctionFunction auctionFunction = load(contractAddress, web3j, credentials, gasPrice, gasLimit);
        TransactionReceipt transactionReceipt = auctionFunction.bid(Convert.toWei(_bidValue, Convert.Unit.ETHER).toBigInteger()).send();
        return transactionReceipt;
    }


    /**
     * 竞拍
     *
     * @param contractAddress 合约地址
     * @param privateKey      私钥
     * @param gasPrice
     * @param gasLimit
     * @param _bidValue       竞拍价格
     * @return String
     */
    public String bid(String contractAddress, String privateKey, BigInteger gasPrice, BigInteger gasLimit, String _bidValue) throws Exception {
        Credentials credentials = Credentials.create(privateKey);
        AuctionFunction auctionFunction = load(contractAddress, web3j, credentials, gasPrice, gasLimit);
        Function function = auctionFunction.bidFunction(Convert.toWei(_bidValue, Convert.Unit.ETHER).toBigInteger());
        String hash = ContractUtils.getHashByFunction(web3j, function, credentials, contractAddress, gasPrice, gasLimit);
        return hash;
    }




    /**
     * 竞拍结束，完成交易
     *
     * @param contractAddress 合约地址
     * @param privateKey      私钥
     * @param gasPrice
     * @param gasLimit
     * @return TransactionReceipt
     */
    public TransactionReceipt exchangeReceipt(String contractAddress, String privateKey, BigInteger gasPrice, BigInteger gasLimit) throws Exception {
        Credentials credentials = Credentials.create(privateKey);
        AuctionFunction auctionFunction = load(contractAddress, web3j, credentials, gasPrice, gasLimit);
        TransactionReceipt transactionReceipt = auctionFunction.exchange().send();
        return transactionReceipt;
    }


    /**
     * 竞拍结束，完成交易
     *
     * @param contractAddress 合约地址
     * @param privateKey      私钥
     * @param gasPrice
     * @param gasLimit
     * @return String
     */
    public String exchange(String contractAddress, String privateKey, BigInteger gasPrice, BigInteger gasLimit) throws Exception {
        Credentials credentials = Credentials.create(privateKey);
        AuctionFunction auctionFunction = load(contractAddress, web3j, credentials, gasPrice, gasLimit);
        Function function = auctionFunction.exchangeFunction();
        String hash = ContractUtils.getHashByFunction(web3j, function, credentials, contractAddress, gasPrice, gasLimit);
        return hash;
    }



    private AuctionFunction load(String contractAddress, Web3j web3j) {
        TransactionManager transactionManager = new ClientTransactionManager(web3j, null);
        AuctionFunction auctionFunction = AuctionFunction.load(contractAddress, web3j, transactionManager, BigInteger.valueOf(9000000000L), BigInteger.valueOf(60000));
        return auctionFunction;
    }

    private AuctionFunction load(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        AuctionFunction auctionFunction = AuctionFunction.load(contractAddress, web3j, credentials, gasPrice, gasLimit);
        return auctionFunction;
    }


}
