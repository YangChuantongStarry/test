package org.elastos.dma.base.wallet;


import com.alibaba.fastjson.JSONObject;
import org.elastos.dma.base.entity.eth.EthAccount;
import org.elastos.dma.base.entity.eth.TokenTransactionReceipt;
import org.elastos.dma.base.util.EthRpcImpl;
import org.elastos.dma.base.util.HdSupport;
import org.elastos.dma.base.util.Utility;
import org.elastos.dma.base.util.contract.TokenDMA;
import org.elastos.dma.utility.exception.ApiException;
import org.elastos.dma.utility.util.ObjectUtils;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.core.methods.response.EthBlock;
import org.web3j.protocol.core.methods.response.EthGetTransactionReceipt;
import org.web3j.protocol.core.methods.response.Transaction;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.tx.ClientTransactionManager;
import org.web3j.tx.TransactionManager;
import org.web3j.utils.Convert;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;
import java.util.concurrent.ExecutionException;


/**
 * @Author: yangchuantong
 * @Description:
 * @Date:Created in  2018/10/31 20:07
 */
public class EthWallet extends EthRpcImpl {

    public EthWallet(String nodeUrl) {
        super(nodeUrl);
    }



    /**
     * 创建eth钱包
     *
     * @return JSONObject
     */
    public EthAccount create(HdSupport.MnemonicType mnemonicType) {
        return EthAccount.init(mnemonicType);
    }

    /**
     * 通过私钥获取钱包
     *
     * @param privateKey 私钥
     * @return JSONObject
     */
    public EthAccount exportByPrivateKey(String privateKey) throws ApiException {
        ObjectUtils.checkEmpty(privateKey);
        return EthAccount.getEthAccountByPrivateKey(privateKey);
    }

    /**
     * 通过助记词获取钱包
     *
     * @param mnemonic 助记词
     * @return JSONObject
     */
    public EthAccount exportByMnemonic(String mnemonic) throws ApiException {
        ObjectUtils.checkEmpty(mnemonic);
        EthAccount ethAccount = EthAccount.getEthAccountByMnemonic(mnemonic);
        return ethAccount;
    }


    /**
     * 获取代币余额
     *
     * @param owner           地址
     * @param contractAddress 合约地址
     * @return String
     */
    public String tokenBalance(String owner, String contractAddress) throws Exception {

        ObjectUtils.checkEmpty(owner, contractAddress);
        TokenDMA tokenDMA = load(contractAddress);
        BigInteger balance = tokenDMA.balanceOf(owner).send();
        return fromWei(balance.toString());
    }

    /**
     * 获取代币名称
     *
     * @param contractAddress 合约地址
     * @return String
     */
    public String tokenName(String contractAddress) throws Exception {
        ObjectUtils.checkEmpty(contractAddress);
        TokenDMA tokenDMA = load(contractAddress);
        String name = tokenDMA.name().send();
        return name;
    }


    /**
     * 获取代币符号
     *
     * @param contractAddress 合约地址
     * @return String
     */
    public String tokenSymbol(String contractAddress) throws Exception {
        ObjectUtils.checkEmpty(contractAddress);
        TokenDMA tokenDMA = load(contractAddress);
        String symbol = tokenDMA.symbol().send();
        return symbol;
    }


    /**
     * 代币转账
     *
     * @param contractAddress 合约地址
     * @param privateKey      私钥
     * @param _to             接收地址
     * @param _value          数量
     * @param gasLimit        gas限制
     * @param gasPrice        gas价格
     * @return TransactionReceipt
     */
    public String tokenTransfer(String contractAddress, String privateKey, String _to, BigDecimal _value, BigInteger gasPrice, BigInteger gasLimit) throws ExecutionException, InterruptedException, ApiException {

        ObjectUtils.checkEmpty(contractAddress,privateKey,_value,gasLimit,gasPrice, checkAddr(_to));

        TokenDMA tokenDMA = load(contractAddress, privateKey, gasPrice, gasLimit);
        TransactionReceipt receipt = tokenDMA.transfer(_to, Convert.toWei(_value, Convert.Unit.ETHER).toBigInteger()).sendAsync().get();
        return receipt.getTransactionHash();
    }


    /**
     * 根据交易哈希获取erc20代币交易信息
     *
     * @param txHash 交易哈希
     * @return TransactionReceipt 交易信息
     */
    public TokenTransactionReceipt transactionInfo20(String txHash) throws IOException {
        TokenTransactionReceipt transactionInfo = new TokenTransactionReceipt();

        Transaction transaction = web3j.ethGetTransactionByHash(txHash).send().getTransaction().get();

        EthGetTransactionReceipt ethGetTransactionReceipt = web3j.ethGetTransactionReceipt(txHash).send();
        TransactionReceipt transactionReceipt = ethGetTransactionReceipt.getTransactionReceipt().get();

        EthBlock.Block block = web3j.ethGetBlockByHash(transaction.getBlockHash(), true).send().getBlock();

        transactionInfo.setBlockHash(transactionReceipt.getBlockHash());
        transactionInfo.setBlockNumber(transactionReceipt.getBlockNumber().toString());
        transactionInfo.setTransactionHash(transactionReceipt.getTransactionHash());
        transactionInfo.setTransactionIndex(transactionReceipt.getTransactionIndex().toString());
        transactionInfo.setContractAddress(transactionReceipt.getContractAddress());
        transactionInfo.setFrom(transactionReceipt.getFrom());
        transactionInfo.setTo(transactionReceipt.getTo());
        transactionInfo.setGasUsed(transactionReceipt.getGasUsed().toString());
        transactionInfo.setStatus(transactionReceipt.getStatus());
        transactionInfo.setLogs(transactionReceipt.getLogs());

        transactionInfo.setValue(Utility.fromWei(transaction.getValue().toString()));
        transactionInfo.setNonce(transaction.getNonce());
        transactionInfo.setGasLimit(transaction.getGas());
        transactionInfo.setGasPrice(Utility.fromWei(transaction.getGasPrice().toString()));

        transactionInfo.setTimestamp(block.getTimestamp());

        if (transactionReceipt.getLogs() != null && transactionReceipt.getLogs().size() > 0) {
            TransactionManager transactionManager = new ClientTransactionManager(web3j, null);
            //加载20合约
            TokenDMA tokenContract = TokenDMA.load("0x0000000000000000000000000000000000000000", web3j, transactionManager, BigInteger.valueOf(9000000000L), BigInteger.valueOf(60000));
            List<TokenDMA.TransferEventResponse> transferEventResponses = tokenContract.getTransferEvents(transactionReceipt);
            transactionInfo.setTransferEventResponses(transferEventResponses);
        }
        return transactionInfo;
    }


    private TokenDMA load(String contractAddress) {
        TransactionManager transactionManager = new ClientTransactionManager(web3j, null);
        TokenDMA tokenDMA = TokenDMA.load(contractAddress, web3j, transactionManager, BigInteger.valueOf(27000000000L), BigInteger.valueOf(250000));
        return tokenDMA;
    }


    private TokenDMA load(String contractAddress, String privateKey, BigInteger gasPrice, BigInteger gasLimit) {
        Credentials credentials = Credentials.create(privateKey);
        TokenDMA tokenDMA = TokenDMA.load(contractAddress, web3j, credentials, gasPrice, gasLimit);
        return tokenDMA;
    }

    private String fromWei(String _value) {
        return Convert.fromWei(_value, Convert.Unit.ETHER).toPlainString();
    }

   }
