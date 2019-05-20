package org.elastos.dma.base.util;


import org.apache.commons.lang.StringUtils;
import org.elastos.dma.base.entity.eth.EthAccount;
import org.elastos.dma.base.entity.eth.TransactionInfo;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.Keys;
import org.web3j.crypto.RawTransaction;
import org.web3j.crypto.TransactionEncoder;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.JsonRpc2_0Web3j;
import org.web3j.protocol.core.methods.response.EthBlock;
import org.web3j.protocol.core.methods.response.EthGetTransactionReceipt;
import org.web3j.protocol.core.methods.response.EthSendTransaction;
import org.web3j.protocol.core.methods.response.Transaction;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.protocol.http.HttpService;
import org.web3j.utils.Numeric;

import java.io.IOException;
import java.math.BigInteger;
import java.util.concurrent.ExecutionException;


/**
 * @Author: yangchuantong
 * @Description:
 * @Date:Created in  2018/11/2 15:10
 */

public class EthRpcImpl implements EthRpc {

    protected Web3j web3j;

    public EthRpcImpl(String nodeUrl) {
        HttpService web3jService = new HttpService(nodeUrl);
        this.web3j = new JsonRpc2_0Web3j(web3jService);
    }




    /**
     * 获取eth余额
     *
     * @param address 地址
     * @return String
     */
    public String balance(String address) throws IOException {
        BigInteger balance = web3j.ethGetBalance(address, DefaultBlockParameterName.LATEST).send().getBalance();
        return Utility.fromWei(balance.toString());
    }


    /**
     * 根据交易哈希获取交易信息
     *
     * @param txHash 交易哈希
     * @return TransactionReceipt 交易信息
     */
    public TransactionInfo transactionInfo(String txHash) throws IOException {
        TransactionInfo transactionInfo = new TransactionInfo();

        Transaction transaction = web3j.ethGetTransactionByHash(txHash).send().getTransaction().get();

        EthGetTransactionReceipt ethGetTransactionReceipt = web3j.ethGetTransactionReceipt(txHash).send();
        TransactionReceipt transactionReceipt = ethGetTransactionReceipt.getTransactionReceipt().get();

        EthBlock.Block block = web3j.ethGetBlockByHash(transaction.getBlockHash(), true).send().getBlock();

        transactionInfo.setBlockHash(transactionReceipt.getBlockHash());
        transactionInfo.setBlockNumber(transactionReceipt.getBlockNumber().toString());
        transactionInfo.setTransactionHash(transactionReceipt.getTransactionHash());
        transactionInfo.setTransactionIndex(transactionReceipt.getTransactionIndex().toString());
        transactionInfo.setContractAddress(transactionReceipt.getContractAddress());
        transactionInfo.setFrom(transaction.getFrom());
        transactionInfo.setTo(transaction.getTo());
        transactionInfo.setGasUsed(transactionReceipt.getGasUsed().toString());

        String status = transactionReceipt.getStatus();
        if (status != null || !status.trim().equals("")) {
            int sta = Integer.parseInt(status.replaceAll("^0[x|X]", ""), 16);//16进制转换
            transactionInfo.setStatus(String.valueOf(sta));
        }
        transactionInfo.setLogs(transactionReceipt.getLogs());
        transactionInfo.setValue(Utility.fromWei(transaction.getValue().toString()));
        transactionInfo.setNonce(transaction.getNonce());
        transactionInfo.setGasLimit(transaction.getGas());
        transactionInfo.setGasPrice(Utility.fromWei(transaction.getGasPrice().toString()));

        transactionInfo.setTimestamp(block.getTimestamp());
        return transactionInfo;
    }


    /**
     * eth转账
     *
     * @param privateKey 私钥
     * @param _to        接收地址
     * @param _value     数量
     * @param gasLimit   gas限制
     * @param gasPrice   gas价格
     * @return String 交易哈希
     */
    public String transfer(String privateKey, String _to, String _value, BigInteger gasPrice, BigInteger gasLimit) throws ExecutionException, InterruptedException, IOException {
        //加载钱包
        Credentials credentials = loadCredentials(privateKey);
        //获取随机数
        BigInteger nonce = web3j.ethGetTransactionCount(credentials.getAddress(), DefaultBlockParameterName.LATEST).send().getTransactionCount();
        //创建事务对象
        RawTransaction rawTransaction = RawTransaction.createTransaction(
                nonce, gasPrice, gasLimit, _to, Utility.toWei(_value), "");

        //交易进行签名和编码
        byte[] signedMessage = TransactionEncoder.signMessage(rawTransaction, credentials);
        String hexValue = Numeric.toHexString(signedMessage);
        //发送事务
        EthSendTransaction ethSendTransaction = web3j.ethSendRawTransaction(hexValue).sendAsync().get();
        return ethSendTransaction.getTransactionHash();
    }

    /**
     * 检查私钥是否有效
     *
     * @param privateKey 私钥
     * @return boolean
     */
    public Boolean checkPriK(String privateKey) {
        String cleanPrivateKey = Numeric.cleanHexPrefix(privateKey);
        return cleanPrivateKey.length() == Keys.PRIVATE_KEY_LENGTH_IN_HEX;
    }

    /**
     * 检查地址是否有效
     *
     * @param address 地址
     * @return boolean
     */
    public Boolean checkAddr(String address) {
        String cleanInput = Numeric.cleanHexPrefix(address);

        try {
            Numeric.toBigIntNoPrefix(cleanInput);
        } catch (NumberFormatException e) {
            return false;
        }

        return cleanInput.length() == Keys.ADDRESS_LENGTH_IN_HEX;
    }


    private Credentials loadCredentials(String privateKey) {

        Credentials credentials = Credentials.create(privateKey);
        return credentials;
    }


    /**
     * 根据hash获取交易状态
     *
     * @param hash 交易哈希
     * @return null:正在验证；ture:交易成功；false:交易失败
     * @throws IOException hash值错误
     */


    public Boolean getStatusByHash(String hash) throws IOException {
       try{
           if(hash==null || StringUtils.isEmpty(hash)){
               return null;
           }
           EthGetTransactionReceipt ethGetTransactionReceipt;

           ethGetTransactionReceipt = web3j.ethGetTransactionReceipt(hash).send();
           TransactionReceipt transactionReceipt = ethGetTransactionReceipt.getTransactionReceipt().get();
           String status = transactionReceipt.getStatus();
           if (!StringUtils.isBlank(status)) {
               int sta = Integer.parseInt(status.replaceAll("^0[x|X]", ""), 16);//16进制转换
               if (sta == 1) {
                   return true;
               } else {
                   return false;
               }
           }
       }catch (Exception e){
           System.err.println("hash查询失败");
       }

        return null;
    }


}
