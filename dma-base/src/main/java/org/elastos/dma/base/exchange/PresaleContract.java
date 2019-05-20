package org.elastos.dma.base.exchange;

import org.elastos.dma.base.util.contract.Presale;
import org.elastos.dma.base.util.contract.PresaleFunction;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.JsonRpc2_0Web3j;
import org.web3j.protocol.http.HttpService;
import org.web3j.tuples.generated.Tuple2;
import org.web3j.tx.ClientTransactionManager;
import org.web3j.tx.TransactionManager;

import java.math.BigInteger;

/**
 * @Author: yangchuantong
 * @Description:
 * @Date:Created in  2019/1/23 11:31
 */
public class PresaleContract {

    protected Web3j web3j;

    public PresaleContract(String nodeUrl) {
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
     * @param _endTimestamp 结束时间戳
     * @return String 合约地址
     */
    public String deploy(String privateKey, BigInteger gasPrice, BigInteger gasLimit, String token721, String token20,  BigInteger _endTimestamp) throws Exception {
        Credentials credentials = Credentials.create(privateKey);
        Presale auction = Presale.deploy(web3j, credentials, gasPrice, gasLimit, token721, token20, _endTimestamp).send();
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
        PresaleFunction auctionFunction = load(contractAddress, web3j);
        String owner = auctionFunction.owner().send();
        return owner;
    }

    /**
     * 获取某资产定购总量
     *
     * @param contractAddress 合约地址
     * @return String
     */
    public BigInteger getEndTimeStamp(String contractAddress) throws Exception {
        PresaleFunction auctionFunction = load(contractAddress, web3j);
        Tuple2<BigInteger, BigInteger> tuple = auctionFunction.getEndTimeStamp().send();
        BigInteger endTimeStamp=tuple.getValue2();
        return endTimeStamp;
    }

    /**
     * 得到结束时间
     *
     * @param contractAddress 合约地址
     * @return String
     */
    public BigInteger getOrderCount(String contractAddress,BigInteger tokenid) throws Exception {
        PresaleFunction auctionFunction = load(contractAddress, web3j);
        BigInteger orderCount = auctionFunction.getOrderCount(tokenid).send();
        return orderCount;
    }


    private PresaleFunction load(String contractAddress, Web3j web3j) {
        TransactionManager transactionManager = new ClientTransactionManager(web3j, null);
        PresaleFunction presaleFunction = PresaleFunction.load(contractAddress, web3j, transactionManager, BigInteger.valueOf(9000000000L), BigInteger.valueOf(60000));
        return presaleFunction;
    }

    private PresaleFunction load(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        PresaleFunction presaleFunction = PresaleFunction.load(contractAddress, web3j, credentials, gasPrice, gasLimit);
        return presaleFunction;
    }

}
