package org.elastos.dma.base.assetManagement.ticket;


import org.elastos.dma.base.util.EthRpcImpl;
import org.elastos.dma.base.util.contract.ContractUtils;
import org.elastos.dma.base.util.contract.NFTokenDMA;
import org.elastos.dma.base.util.contract.NFTokenDMAFunction;
import org.elastos.dma.utility.dto.ContractInfoDto;
import org.elastos.dma.utility.dto.AssetInfoDto;
import org.web3j.abi.datatypes.Function;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.tuples.generated.Tuple5;
import org.web3j.tuples.generated.Tuple6;
import org.web3j.tx.ClientTransactionManager;
import org.web3j.tx.TransactionManager;

import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author: yangchuantong
 * @Description:
 * @Date:Created in  2018/12/29 13:14
 */
public class Asset extends EthRpcImpl {


    public Asset(String nodeUrl) {
        super(nodeUrl);
    }


    /**
     * 合约名称
     *
     * @param contractAddress 合约地址
     * @return String  合约名称
     */
    public String name(String contractAddress) throws Exception {
        NFTokenDMA nfTokenDMA = load(contractAddress, web3j);
        String name = nfTokenDMA.name().send();
        return name;
    }

    /**
     * 合约符号
     *
     * @param contractAddress 合约地址
     * @return String  符号
     */
    public String symbol(String contractAddress) throws Exception {
        NFTokenDMA nfTokenDMA = load(contractAddress, web3j);
        String symbol = nfTokenDMA.symbol().send().toString();
        return symbol;
    }


    /**
     * 合约拥有者
     *
     * @param contractAddress 合约地址
     * @return String  拥有者
     */
    public String owner(String contractAddress) throws Exception {
        NFTokenDMA nfTokenDMA = load(contractAddress, web3j);
        String owner = nfTokenDMA.owner().send();
        return owner;
    }

    /**
     * 根据地址查询资产数量
     *
     * @param contractAddress 合约地址
     * @param owner           所有者
     * @return BigInteger  资产数量
     */
    public BigInteger balanceOf(String contractAddress, String owner) throws Exception {
        NFTokenDMA nfTokenDMA = load(contractAddress, web3j);
        BigInteger balanceOf = nfTokenDMA.balanceOf(owner).send();
        return balanceOf;
    }


    /**
     * 根据合约地址查询拥有资产id列表
     *
     * @param contractAddress 合约地址
     * @param owner           所有者
     * @return List<BigInteger>  资产id
     */
    public List<BigInteger> tokenIds(String contractAddress, String owner) throws Exception {
        NFTokenDMA nfTokenDMA = load(contractAddress, web3j);
        List<BigInteger> tokenIds = new ArrayList<>();
        BigInteger send = nfTokenDMA.balanceOf(owner).send();
        Integer balanceOf = send.intValue();
        if (balanceOf > 0) {
            for (int i = 0; i < balanceOf; i++) {
                BigInteger tokenId = nfTokenDMA.tokenOfOwnerByIndex(owner, BigInteger.valueOf(i)).send();
                tokenIds.add(tokenId);
            }
        }
        return tokenIds;
    }


    /**
     * 根据资产id查询资产拥有者
     *
     * @param contractAddress 合约地址
     * @param tokenId         资产id
     * @return String 拥有者地址
     */
    public String ownerOf(String contractAddress, BigInteger tokenId) throws Exception {
        NFTokenDMA nfTokenDMA = load(contractAddress, web3j);
        String address = nfTokenDMA.ownerOf(tokenId).send();
        return address;
    }


    /**
     * 根据资产id查询资产信息
     *
     * @param contractAddress 合约地址
     * @param tokenId         资产id
     * @return String 资产信息
     */
    public String getTokenData(String contractAddress, BigInteger tokenId) throws Exception {
        NFTokenDMA nfTokenDMA = load(contractAddress, web3j);
        String tokenURI = nfTokenDMA.checkUri(tokenId).send();
        return tokenURI;
    }

    /**
     * 根据资产id查询资产详情
     *
     * @param contractAddress 合约地址
     * @param tokenId         资产id
     * @return String 资产信息
     */
    public AssetInfoDto getTokenInfo(String contractAddress, BigInteger tokenId) throws Exception {
        NFTokenDMA nfTokenDMA = load(contractAddress, web3j);
        Tuple6<String, Boolean, Boolean, String, BigInteger, String> tuple6 = nfTokenDMA.getTokenInfo(tokenId).send();
        AssetInfoDto tkoenInfoDto = new AssetInfoDto();
        tkoenInfoDto.setOwner(tuple6.getValue1());
        tkoenInfoDto.setIsTransfer(tuple6.getValue2());
        tkoenInfoDto.setIsBurn(tuple6.getValue3());
        tkoenInfoDto.setData(tuple6.getValue4());
        tkoenInfoDto.setStatus(tuple6.getValue5());
        tkoenInfoDto.setUser(tuple6.getValue6());
        return tkoenInfoDto;
    }


    /**
     * 获取合约信息
     *
     * @param contractAddress 合约地址
     * @return ContractInfoDto
     */
    public ContractInfoDto getContractInfo(String contractAddress) throws Exception {
        NFTokenDMA nfTokenDMA = load(contractAddress, web3j);
        Tuple5<String, String, String, String, Boolean> tuple5 = nfTokenDMA.getInfo().send();
        ContractInfoDto contractInfoDto = new ContractInfoDto();
        contractInfoDto.setName(tuple5.getValue1());
        contractInfoDto.setSymbol(tuple5.getValue2());
        contractInfoDto.setMetadata(tuple5.getValue3());
        contractInfoDto.setOwner(tuple5.getValue4());
        contractInfoDto.setBurn(tuple5.getValue5());
        return contractInfoDto;
    }


    /**
     * 根据tokenid查询token状态
     *
     * @param contractAddress 合约地址
     * @param tokenId         资产id
     * @return String 资产信息
     */
    public BigInteger getTokenStatus(String contractAddress, BigInteger tokenId) throws Exception {
        NFTokenDMA nfTokenDMA = load(contractAddress, web3j);
        BigInteger status = nfTokenDMA.getStatus(tokenId).send();
        return status;
    }


    /**
     * 根据tokenid查询user信息
     *
     * @param contractAddress 合约地址
     * @param tokenId         资产id
     * @return String 资产信息
     */
    public String getTokenUser(String contractAddress, BigInteger tokenId) throws Exception {
        NFTokenDMA nfTokenDMA = load(contractAddress, web3j);
        String user = nfTokenDMA.getUser(tokenId).send();
        return user;
    }


    /**
     * 判定是否为合约代理人
     *
     * @param contractAddress 合约地址
     * @param _owner          合约拥有者地址
     * @param _operator       代理者地址
     * @return String 资产信息
     */
    private Boolean isOperator(String contractAddress, String _owner, String _operator) throws Exception {
        NFTokenDMA nfTokenDMA = load(contractAddress, web3j);
        Boolean aBoolean = nfTokenDMA.isApprovedForAll(_owner, _operator).send();
        return aBoolean;
    }

    /**
     * 根据tokenid查询token是否可转移
     *
     * @param contractAddress 合约地址
     * @param tokenId         资产id
     * @return String 资产信息
     */
    public boolean getCanTransfer(String contractAddress, BigInteger tokenId) throws Exception {
        NFTokenDMA nfTokenDMA = load(contractAddress, web3j);
        return nfTokenDMA.getIsTransfer(tokenId).send();
    }


    /**
     * 获取合约单元数据
     *
     * @param contractAddress 合约地址
     * @return String 资产信息
     */
    public String getContractData(String contractAddress) throws Exception {
        NFTokenDMA nfTokenDMA = load(contractAddress, web3j);
        String isTransfer = nfTokenDMA.getMetadata().send();
        return isTransfer;
    }

    /**
     * 根据tokenid获取被授权者地址
     *
     * @param tokenid tokenid
     * @return address
     */
    public String getApproved(String contractAddress, BigInteger tokenid) throws Exception {
        NFTokenDMA nfTokenDMA = load(contractAddress, web3j);
        String address = nfTokenDMA.getApproved(tokenid).send();
        return address;
    }


    /**
     * 获取token发行量
     *
     * @param contractAddress 合约地址
     * @return BigInteger
     */
    public BigInteger totalSupply(String contractAddress) throws Exception {
        NFTokenDMA nfTokenDMA = load(contractAddress, web3j);
        BigInteger totalSupply = nfTokenDMA.totalSupply().send();
        return totalSupply;
    }


    /**
     * 发布资产
     *
     * @param contractAddress 合约地址
     * @param privateKey      私钥
     * @param gasPrice
     * @param gasLimit
     * @param _to             资产归属地址
     * @param _info           资产信息
     * @param _isTransfer     是否可转移
     * @param _isBurn         是否可销毁
     * @return String
     */
    public String mint(String contractAddress, String privateKey, BigInteger gasPrice, BigInteger gasLimit, String _to, String _info, BigInteger tokenid, Boolean _isTransfer, Boolean _isBurn) throws Exception {
        Credentials credentials = getCredentials(privateKey);
        NFTokenDMAFunction nfTokenDMA = load(contractAddress, web3j, credentials, gasPrice, gasLimit);
        Function function = nfTokenDMA.mintFunction(_to, tokenid, _info, _isTransfer, _isBurn);
        String hash = ContractUtils.getHashByFunction(web3j, function, credentials, contractAddress, gasPrice, gasLimit);
        return hash;
    }

    /**
     * 发布资产
     *
     * @param contractAddress 合约地址
     * @param privateKey      私钥
     * @param gasPrice
     * @param gasLimit
     * @param _to             资产归属地址
     * @param _info           资产信息
     * @param _isTransfer     是否可转移
     * @param _isBurn         是否可销毁
     * @return TransactionReceipt
     */
    public TransactionReceipt mintReceipt(String contractAddress, String privateKey, BigInteger gasPrice, BigInteger gasLimit, String _to, String _info, BigInteger tokenid, Boolean _isTransfer, Boolean _isBurn) throws Exception {
        Credentials credentials = getCredentials(privateKey);
        NFTokenDMA nfTokenDMA = load(contractAddress, web3j, credentials, gasPrice, gasLimit);
        TransactionReceipt transactionReceipt = nfTokenDMA.mint(_to, tokenid, _info, _isTransfer, _isBurn).sendAsync().get();
        return transactionReceipt;
    }



    /**
     * 批量发布资产
     *
     * @param contractAddress 合约地址
     * @param privateKey      私钥
     * @param gasPrice
     * @param gasLimit
     * @param tokenid         tokenid 数组
     * @param _to             资产归属地址
     * @param _info           资产信息
     * @return String 交易哈希
     */
    public String mintWithArray(String contractAddress, String privateKey, BigInteger gasPrice, BigInteger gasLimit, String _to, String _info, List<BigInteger> tokenid, Boolean _isTransfer, Boolean _isBurn) throws Exception {
        Credentials credentials = getCredentials(privateKey);
        NFTokenDMAFunction nfTokenDMA = load(contractAddress, web3j, credentials, gasPrice, gasLimit);
        Function function = nfTokenDMA.mintWithArrayFunction(_to, tokenid, _info, _isTransfer, _isBurn);
        String hash = ContractUtils.getHashByFunction(web3j, function, credentials, contractAddress, gasPrice, gasLimit);
        return hash;
    }


    /**
     * 批量发布资产 同步
     *
     * @param contractAddress 合约地址
     * @param privateKey      私钥
     * @param gasPrice
     * @param gasLimit
     * @param tokenid         tokenid 数组
     * @param _to             资产归属地址
     * @param _info           资产信息
     * @return TransactionReceipt
     */
    public TransactionReceipt mintWithArrayReceipt(String contractAddress, String privateKey, BigInteger gasPrice, BigInteger gasLimit, String _to, String _info, List<BigInteger> tokenid, Boolean _isTransfer, Boolean _isBurn) throws Exception {
        Credentials credentials = getCredentials(privateKey);
        NFTokenDMA nfTokenDMA = load(contractAddress, web3j, credentials, gasPrice, gasLimit);
        TransactionReceipt transactionReceipt = nfTokenDMA.mintWithArray(_to, tokenid, _info, _isTransfer, _isBurn).send();
        return transactionReceipt;
    }


    /**
     * 授权
     *
     * @param contractAddress 合约地址
     * @param privateKey      私钥
     * @param gasPrice
     * @param gasLimit
     * @param _to             授权地址
     * @param tokenid         tokenid
     * @return String 交易哈希
     */
    public String approve(String contractAddress, String privateKey, BigInteger gasPrice, BigInteger gasLimit, String _to, BigInteger tokenid) throws Exception {
        Credentials credentials = getCredentials(privateKey);
        NFTokenDMAFunction nfTokenDMA = load(contractAddress, web3j, credentials, gasPrice, gasLimit);
        Function function = nfTokenDMA.approveFunction(_to, tokenid);
        String hash = ContractUtils.getHashByFunction(web3j, function, credentials, contractAddress, gasPrice, gasLimit);
        return hash;
    }


    /**
     * 授权
     *
     * @param contractAddress 合约地址
     * @param privateKey      私钥
     * @param gasPrice
     * @param gasLimit
     * @param _to             授权地址
     * @param tokenid         tokenid
     * @return TransactionReceipt
     */
    public TransactionReceipt approveReceipt(String contractAddress, String privateKey, BigInteger gasPrice, BigInteger gasLimit, String _to, BigInteger tokenid) throws Exception {
        Credentials credentials = getCredentials(privateKey);
        NFTokenDMA nfTokenDMA = load(contractAddress, web3j, credentials, gasPrice, gasLimit);
        TransactionReceipt transactionReceipt = nfTokenDMA.approve(_to, tokenid).send();
        return transactionReceipt;
    }


    /**
     * 批量授权
     *
     * @param contractAddress 合约地址
     * @param privateKey      私钥
     * @param gasPrice
     * @param gasLimit
     * @param _to             授权地址
     * @param tokenids        tokenid数组
     * @return String 交易哈希
     */
    public String approveWithArray(String contractAddress, String privateKey, BigInteger gasPrice, BigInteger gasLimit, String _to, List<BigInteger> tokenids) throws Exception {
        Credentials credentials = getCredentials(privateKey);

        NFTokenDMAFunction nfTokenDMA = load(contractAddress, web3j, credentials, gasPrice, gasLimit);
        Function function = nfTokenDMA.approveWithAarryFunction(_to, tokenids);
        String hash = ContractUtils.getHashByFunction(web3j, function, credentials, contractAddress, gasPrice, gasLimit);
        return hash;
    }


    /**
     * 批量授权
     *
     * @param contractAddress 合约地址
     * @param privateKey      私钥
     * @param gasPrice
     * @param gasLimit
     * @param _to             授权地址
     * @param tokenids        tokenid数组
     * @return String 交易哈希
     */
    public TransactionReceipt approveWithArrayReceipt(String contractAddress, String privateKey, BigInteger gasPrice, BigInteger gasLimit, String _to, List<BigInteger> tokenids) throws Exception {
        Credentials credentials = getCredentials(privateKey);
        NFTokenDMA nfTokenDMA = load(contractAddress, web3j, credentials, gasPrice, gasLimit);
        TransactionReceipt transactionReceipt = nfTokenDMA.approveWithAarry(_to, tokenids).send();
        return transactionReceipt;
    }

    /**
     * 解除授权
     *
     * @param contractAddress 合约地址
     * @param privateKey      私钥
     * @param gasPrice
     * @param gasLimit
     * @param tokenid         tokenid
     * @return String 交易哈希
     */
    public String revokeApprove(String contractAddress, String privateKey, BigInteger gasPrice, BigInteger gasLimit, BigInteger tokenid) throws Exception {
        Credentials credentials = getCredentials(privateKey);

        NFTokenDMAFunction nfTokenDMA = load(contractAddress, web3j, credentials, gasPrice, gasLimit);
        Function function = nfTokenDMA.revokeApproveFunction(tokenid);
        String hash = ContractUtils.getHashByFunction(web3j, function, credentials, contractAddress, gasPrice, gasLimit);

        return hash;
    }


    /**
     * 解除授权
     *
     * @param contractAddress 合约地址
     * @param privateKey      私钥
     * @param gasPrice
     * @param gasLimit
     * @param tokenid         tokenid
     * @return String 交易哈希
     */
    public TransactionReceipt revokeApproveReceipt(String contractAddress, String privateKey, BigInteger gasPrice, BigInteger gasLimit, BigInteger tokenid) throws Exception {
        Credentials credentials = getCredentials(privateKey);
        NFTokenDMA nfTokenDMA = load(contractAddress, web3j, credentials, gasPrice, gasLimit);
        TransactionReceipt transactionReceipt = nfTokenDMA.revokeApprove(tokenid).sendAsync().get();
        return transactionReceipt;
    }


    /**
     * 批量解除授权
     *
     * @param contractAddress 合约地址
     * @param privateKey      私钥
     * @param gasPrice
     * @param gasLimit
     * @param tokenids        tokenid数组
     * @return String 交易哈希
     */
    public String removeApproveWithArray(String contractAddress, String privateKey, BigInteger gasPrice, BigInteger gasLimit, List<BigInteger> tokenids) throws Exception {
        Credentials credentials = getCredentials(privateKey);
        NFTokenDMAFunction nfTokenDMA = load(contractAddress, web3j, credentials, gasPrice, gasLimit);
        Function function = nfTokenDMA.removeApproveWithArrayFunction(tokenids);
        String hash = ContractUtils.getHashByFunction(web3j, function, credentials, contractAddress, gasPrice, gasLimit);

        return hash;
    }


    /**
     * 批量解除授权
     *
     * @param contractAddress 合约地址
     * @param privateKey      私钥
     * @param gasPrice
     * @param gasLimit
     * @param tokenids        tokenid数组
     * @return String 交易哈希
     */
    public TransactionReceipt removeApproveWithArrayReceipt(String contractAddress, String privateKey, BigInteger gasPrice, BigInteger gasLimit, List<BigInteger> tokenids) throws Exception {
        Credentials credentials = getCredentials(privateKey);
        NFTokenDMA nfTokenDMA = load(contractAddress, web3j, credentials, gasPrice, gasLimit);
        TransactionReceipt transactionReceipt = nfTokenDMA.removeApproveWithArray(tokenids).send();
        return transactionReceipt;
    }


    /**
     * 转移资产
     *
     * @param contractAddress 合约地址
     * @param privateKey      私钥
     * @param gasPrice
     * @param gasLimit
     * @param _from           资产拥有地址
     * @param _to             资产接收者地址
     * @param _tokenId        资产id
     * @return
     */
    public String transfer(String contractAddress, String privateKey, BigInteger gasPrice, BigInteger gasLimit, String _from, String _to, BigInteger _tokenId) throws Exception {
        Credentials credentials = getCredentials(privateKey);

        NFTokenDMAFunction nfTokenDMA = load(contractAddress, web3j, credentials, gasPrice, gasLimit);
        Function function = nfTokenDMA.transferFromFunction(_from, _to, _tokenId);
        String hash = ContractUtils.getHashByFunction(web3j, function, credentials, contractAddress, gasPrice, gasLimit);

        return hash;
    }


    /**
     * 转移资产
     *
     * @param contractAddress 合约地址
     * @param privateKey      私钥
     * @param gasPrice
     * @param gasLimit
     * @param _from           资产拥有地址
     * @param _to             资产接收者地址
     * @param _tokenId        资产id
     * @return
     */
    public TransactionReceipt transferReceipt(String contractAddress, String privateKey, BigInteger gasPrice, BigInteger gasLimit,String _from, String _to, BigInteger _tokenId) throws Exception {
        Credentials credentials = getCredentials(privateKey);
        NFTokenDMA nfTokenDMA = load(contractAddress, web3j, credentials, gasPrice, gasLimit);
        TransactionReceipt transactionReceipt = nfTokenDMA.transferFrom(_from, _to, _tokenId).send();
        return transactionReceipt;
    }


    /**
     * 批量转移资产
     *
     * @param contractAddress 合约地址
     * @param privateKey      私钥
     * @param gasPrice
     * @param gasLimit
     * @param _from           资产拥有地址
     * @param _to             资产接收者地址
     * @param tokenIds        资产id数组
     * @return
     */
    public String transferWithArray(String contractAddress, String privateKey, BigInteger gasPrice, BigInteger gasLimit,String _from, String _to, List<BigInteger> tokenIds) throws Exception {
        Credentials credentials = getCredentials(privateKey);

        NFTokenDMAFunction nfTokenDMA = load(contractAddress, web3j, credentials, gasPrice, gasLimit);
        Function function = nfTokenDMA.transferFromWithAarryFunction(_from, _to, tokenIds);
        String hash = ContractUtils.getHashByFunction(web3j, function, credentials, contractAddress, gasPrice, gasLimit);

        return hash;
    }


    /**
     * 批量转移资产
     *
     * @param contractAddress 合约地址
     * @param privateKey      私钥
     * @param gasPrice
     * @param gasLimit
     * @param _from           资产拥有地址
     * @param _to             资产接收者地址
     * @param tokenIds        资产id数组
     * @return
     */
    public TransactionReceipt transferWithArrayReceipt(String contractAddress, String privateKey, BigInteger gasPrice, BigInteger gasLimit,String _from, String _to, List<BigInteger> tokenIds) throws Exception {
        Credentials credentials = getCredentials(privateKey);
        NFTokenDMA nfTokenDMA = load(contractAddress, web3j, credentials, gasPrice, gasLimit);
        TransactionReceipt transactionReceipt = nfTokenDMA.transferFromWithAarry(credentials.getAddress(), _to, tokenIds).send();
        return transactionReceipt;
    }


    /**
     * 创建合约
     *
     * @param privateKey 私钥
     * @param gasPrice
     * @param gasLimit
     * @param _name      合约名称
     * @param _symbol    合约标志
     * @param _metadata  单元数据
     * @param _isBurn    是否可以销毁
     * @return String 合约地址
     */
    public String deploy(String privateKey, BigInteger gasPrice, BigInteger gasLimit, String _name, String _symbol, String _metadata, Boolean _isBurn) throws Exception {
        Credentials credentials = getCredentials(privateKey);
        NFTokenDMA nfTokenDMA = NFTokenDMA.deploy(web3j, credentials, gasPrice, gasLimit, _name, _symbol, _metadata == null ? "" : _metadata, _isBurn).send();
        String contractAddress = nfTokenDMA.getContractAddress();
        return contractAddress;
    }

    /**
     * 销毁tokenid
     *
     * @param contractAddress 合约地址
     * @param tokenid         tokenid
     * @return boolean
     */
    public String burn(String contractAddress, String privateKey, BigInteger gasPrice, BigInteger gasLimit, String _owner, BigInteger tokenid) throws Exception {
        Credentials credentials = getCredentials(privateKey);
        NFTokenDMAFunction nfTokenDMA = load(contractAddress, web3j, credentials, gasPrice, gasLimit);
        Function function = nfTokenDMA.burnFunction(_owner, tokenid);
        String hash = ContractUtils.getHashByFunction(web3j, function, credentials, contractAddress, gasPrice, gasLimit);

        return hash;
    }

    /**
     * 销毁tokenid
     *
     * @param contractAddress 合约地址
     * @param tokenid         tokenid
     * @return boolean
     */
    public TransactionReceipt burnReceipt(String contractAddress, String privateKey, BigInteger gasPrice, BigInteger gasLimit, String _owner, BigInteger tokenid) throws Exception {
        Credentials credentials = getCredentials(privateKey);
        NFTokenDMA nfTokenDMA = load(contractAddress, web3j, credentials, gasPrice, gasLimit);
        TransactionReceipt transactionReceipt = nfTokenDMA.burn(_owner, tokenid).send();
        return transactionReceipt;
    }


    /**
     * 设置tokenid是否可转移
     *
     * @param contractAddress 合约地址
     * @param privateKey      私钥
     * @param gasPrice
     * @param gasLimit
     * @param tokenid         tokenid
     * @param _istransfer     是否可转移
     * @return String
     */
    public String setCanTransfer(String contractAddress, String privateKey, BigInteger gasPrice, BigInteger gasLimit, BigInteger tokenid, boolean _istransfer) throws Exception {
        Credentials credentials = getCredentials(privateKey);
        NFTokenDMAFunction nfTokenDMA = load(contractAddress, web3j, credentials, gasPrice, gasLimit);
        Function function = nfTokenDMA.setIsTransferFunction(tokenid, _istransfer);
        String hash = ContractUtils.getHashByFunction(web3j, function, credentials, contractAddress, gasPrice, gasLimit);
        return hash;
    }

    /**
     * 设置tokenid是否可转移
     *
     * @param contractAddress 合约地址
     * @param privateKey      私钥
     * @param gasPrice
     * @param gasLimit
     * @param tokenid         tokenid
     * @param _istransfer     是否可转移
     * @return TransactionReceipt
     */
    public TransactionReceipt setCanTransferReceipt(String contractAddress, String privateKey, BigInteger gasPrice, BigInteger gasLimit, BigInteger tokenid, boolean _istransfer) throws Exception {
        Credentials credentials = getCredentials(privateKey);
        NFTokenDMA nfTokenDMA = load(contractAddress, web3j, credentials, gasPrice, gasLimit);
        TransactionReceipt transactionReceipt = nfTokenDMA.setIsTransfer(tokenid, _istransfer).send();
        return transactionReceipt;
    }


    /**
     * 设置tokenid是状态
     *
     * @param contractAddress 合约地址
     * @param privateKey      私钥
     * @param gasPrice
     * @param gasLimit
     * @param tokenid         tokenid
     * @param status          状态
     * @return String
     */
    public String setStatus(String contractAddress, String privateKey, BigInteger gasPrice, BigInteger gasLimit, BigInteger tokenid, BigInteger status) throws Exception {
        Credentials credentials = getCredentials(privateKey);
        NFTokenDMAFunction nfTokenDMA = load(contractAddress, web3j, credentials, gasPrice, gasLimit);
        Function function = nfTokenDMA.setStatusFunction(tokenid, status);
        String hash = ContractUtils.getHashByFunction(web3j, function, credentials, contractAddress, gasPrice, gasLimit);
        return hash;
    }

    /**
     * 设置tokenid状态
     *
     * @param contractAddress 合约地址
     * @param privateKey      私钥
     * @param gasPrice
     * @param gasLimit
     * @param tokenid         tokenid
     * @param status          状态
     * @return TransactionReceipt
     */
    public TransactionReceipt setStatusReceipt(String contractAddress, String privateKey, BigInteger gasPrice, BigInteger gasLimit, BigInteger tokenid, BigInteger status) throws Exception {
        Credentials credentials = getCredentials(privateKey);
        NFTokenDMA nfTokenDMA = load(contractAddress, web3j, credentials, gasPrice, gasLimit);
        TransactionReceipt transactionReceipt = nfTokenDMA.setStatus(tokenid, status).send();
        return transactionReceipt;
    }


    /**
     * 设置tokenid的用户信息
     *
     * @param contractAddress 合约地址
     * @param privateKey      私钥
     * @param gasPrice
     * @param gasLimit
     * @param tokenid         tokenid
     * @param user            用户信息
     * @return String
     */
    public String setUser(String contractAddress, String privateKey, BigInteger gasPrice, BigInteger gasLimit, BigInteger tokenid, String user) throws Exception {
        Credentials credentials = getCredentials(privateKey);
        NFTokenDMAFunction nfTokenDMA = load(contractAddress, web3j, credentials, gasPrice, gasLimit);
        Function function = nfTokenDMA.setUserFunction(tokenid, user);
        String hash = ContractUtils.getHashByFunction(web3j, function, credentials, contractAddress, gasPrice, gasLimit);
        return hash;
    }

    /**
     * 设置tokenid用户信息
     *
     * @param contractAddress 合约地址
     * @param privateKey      私钥
     * @param gasPrice
     * @param gasLimit
     * @param tokenid         tokenid
     * @param user            用户信息
     * @return TransactionReceipt
     */
    public TransactionReceipt setUserReceipt(String contractAddress, String privateKey, BigInteger gasPrice, BigInteger gasLimit, BigInteger tokenid, String user) throws Exception {
        Credentials credentials = getCredentials(privateKey);
        NFTokenDMA nfTokenDMA = load(contractAddress, web3j, credentials, gasPrice, gasLimit);
        TransactionReceipt transactionReceipt = nfTokenDMA.setUser(tokenid, user).send();
        return transactionReceipt;
    }


    /**
     * 设置合约代理人
     *
     * @param contractAddress 合约地址
     * @param privateKey      私钥
     * @param gasPrice
     * @param gasLimit
     * @param _operator       代理者地址
     * @param _isApproved     代理者状态
     * @return String
     */
    public String setOperator(String contractAddress, String privateKey, BigInteger gasPrice, BigInteger gasLimit, String _operator, Boolean _isApproved) throws Exception {
        Credentials credentials = getCredentials(privateKey);
        NFTokenDMAFunction nfTokenDMA = load(contractAddress, web3j, credentials, gasPrice, gasLimit);
        Function function = nfTokenDMA.setApprovalForAllFunction(_operator, _isApproved);
        String hash = ContractUtils.getHashByFunction(web3j, function, credentials, contractAddress, gasPrice, gasLimit);
        return hash;
    }

    /**
     * 设置合约代理人
     *
     * @param contractAddress 合约地址
     * @param privateKey      私钥
     * @param gasPrice
     * @param gasLimit
     * @param _operator       代理者地址
     * @param _isApproved     代理者状态
     * @return TransactionReceipt
     */
    public TransactionReceipt setOperatorReceipt(String contractAddress, String privateKey, BigInteger gasPrice, BigInteger gasLimit, String _operator, Boolean _isApproved) throws Exception {
        Credentials credentials = getCredentials(privateKey);
        NFTokenDMA nfTokenDMA = load(contractAddress, web3j, credentials, gasPrice, gasLimit);
        TransactionReceipt transactionReceipt = nfTokenDMA.setApprovalForAll(_operator, _isApproved).send();
        return transactionReceipt;
    }


    /**
     * 设置合约Metadata
     *
     * @param contractAddress 合约地址
     * @param privateKey      私钥
     * @param gasPrice
     * @param gasLimit
     * @param data            数据
     * @return String
     */
    public String setContractMetadata(String contractAddress, String privateKey, BigInteger gasPrice, BigInteger gasLimit, String data) throws Exception {
        Credentials credentials = getCredentials(privateKey);
        NFTokenDMAFunction nfTokenDMA = load(contractAddress, web3j, credentials, gasPrice, gasLimit);
        Function function = nfTokenDMA.setMetadataFunction(data);
        String hash = ContractUtils.getHashByFunction(web3j, function, credentials, contractAddress, gasPrice, gasLimit);
        return hash;
    }

    /**
     * 设置合约数据
     *
     * @param contractAddress 合约地址
     * @param privateKey      私钥
     * @param gasPrice
     * @param gasLimit
     * @param data            数据
     * @return TransactionReceipt
     */
    public TransactionReceipt setContractMetadataReceipt(String contractAddress, String privateKey, BigInteger gasPrice, BigInteger gasLimit, String data) throws Exception {
        Credentials credentials = getCredentials(privateKey);
        NFTokenDMA nfTokenDMA = load(contractAddress, web3j, credentials, gasPrice, gasLimit);
        TransactionReceipt transactionReceipt = nfTokenDMA.setMetadata(data).send();
        return transactionReceipt;
    }


    /**
     * 转让合约
     *
     * @param contractAddress 合约地址
     * @param privateKey      私钥
     * @param gasPrice
     * @param gasLimit
     * @param _newOwner       合约新的拥有者
     * @return String
     */
    public String transferContract(String contractAddress, String privateKey, BigInteger gasPrice, BigInteger gasLimit, String _newOwner) throws Exception {
        Credentials credentials = getCredentials(privateKey);
        NFTokenDMAFunction nfTokenDMA = load(contractAddress, web3j, credentials, gasPrice, gasLimit);
        Function function = nfTokenDMA.transferOwnershipFunction(_newOwner);
        String hash = ContractUtils.getHashByFunction(web3j, function, credentials, contractAddress, gasPrice, gasLimit);
        return hash;
    }

    /**
     * 转让合约
     *
     * @param contractAddress 合约地址
     * @param privateKey      私钥
     * @param gasPrice
     * @param gasLimit
     * @param _newOwner       合约新的拥有者
     * @return TransactionReceipt
     */
    public TransactionReceipt transferContractReceipt(String contractAddress, String privateKey, BigInteger gasPrice, BigInteger gasLimit, String _newOwner) throws Exception {
        Credentials credentials = getCredentials(privateKey);
        NFTokenDMA nfTokenDMA = load(contractAddress, web3j, credentials, gasPrice, gasLimit);
        TransactionReceipt transactionReceipt = nfTokenDMA.transferOwnership(_newOwner).send();
        return transactionReceipt;
    }


    /**
     * 销毁合约
     *
     * @param contractAddress 合约地址
     * @param privateKey      私钥
     * @param gasPrice
     * @param gasLimit
     * @return String
     */
    public String kill(String contractAddress, String privateKey, BigInteger gasPrice, BigInteger gasLimit) throws Exception {
        Credentials credentials = getCredentials(privateKey);
        NFTokenDMAFunction nfTokenDMA = load(contractAddress, web3j, credentials, gasPrice, gasLimit);
        Function function = nfTokenDMA.killFunction();
        String hash = ContractUtils.getHashByFunction(web3j, function, credentials, contractAddress, gasPrice, gasLimit);
        return hash;
    }

    /**
     * 销毁合约
     *
     * @param contractAddress 合约地址
     * @param privateKey      私钥
     * @param gasPrice
     * @param gasLimit
     * @return TransactionReceipt
     */
    public TransactionReceipt killReceipt(String contractAddress, String privateKey, BigInteger gasPrice, BigInteger gasLimit) throws Exception {
        Credentials credentials = getCredentials(privateKey);
        NFTokenDMA nfTokenDMA = load(contractAddress, web3j, credentials, gasPrice, gasLimit);
        TransactionReceipt transactionReceipt = nfTokenDMA.kill().send();
        return transactionReceipt;
    }


    /**
     * 检查tokenid是否有效
     *
     * @param contractAddress 合约地址
     * @param tokenid         tokenid
     * @return boolean
     */
    public boolean valid(String contractAddress, BigInteger tokenid) throws Exception {
        NFTokenDMA nfTokenDMA = load(contractAddress, web3j);
        return nfTokenDMA.valid(tokenid).send().booleanValue();
    }

    /**
     * 查询授权事件
     * @param hash
     * @param contractAddress
     * @return
     * @throws IOException
     */
    public List<NFTokenDMA.ApprovalEventResponse> getApprovalEvents(String hash, String contractAddress) throws IOException {
        NFTokenDMA nfTokenDMA = load(contractAddress, web3j);
        List<NFTokenDMA.ApprovalEventResponse> list = nfTokenDMA.getApprovalEvents(getTransactionReceipt(hash));
        return list;
    }

    /**
     * 查询授权事件
     * @param hash
     * @param contractAddress
     * @return
     * @throws IOException
     */
    public List<NFTokenDMA.TransferEventResponse> getTransferEvents(String hash, String contractAddress) throws IOException {
        NFTokenDMA nfTokenDMA = load(contractAddress, web3j);
        List<NFTokenDMA.TransferEventResponse> list = nfTokenDMA.getTransferEvents(getTransactionReceipt(hash));
        return list;
    }

    /**
     * 获取交易账单
     * @param hash
     * @return
     * @throws IOException
     */
    public TransactionReceipt getTransactionReceipt(String hash) throws IOException {
        TransactionReceipt transactionReceipt = web3j.ethGetTransactionReceipt(hash).send().getTransactionReceipt().get();
        return transactionReceipt;
    }


    private NFTokenDMAFunction load(String contractAddress, Web3j web3j) {
        TransactionManager transactionManager = new ClientTransactionManager(web3j, null);
        NFTokenDMAFunction asset = NFTokenDMAFunction.load(contractAddress, web3j, transactionManager, BigInteger.valueOf(9000000000L), BigInteger.valueOf(60000));
        return asset;
    }

    private NFTokenDMAFunction load(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        NFTokenDMAFunction nfTokenDMAFunctiom = NFTokenDMAFunction.load(contractAddress, web3j, credentials, gasPrice, gasLimit);
        return nfTokenDMAFunctiom;
    }


    private Credentials getCredentials(String privateKey) {
        Credentials credentials = Credentials.create(privateKey);
        return credentials;
    }

}
