package org.elastos.dma.base.entity.eth;


import java.util.List;

/**
 * @Author: yangchuantong
 * @Description:
 * @Date:Created in  2018/11/19 15:29
 */
public class TokenTransactionReceipt<T> extends TransactionInfo {


   private List<T> transferEventResponses;


    public List<T> getTransferEventResponses() {
        return transferEventResponses;
    }

    public void setTransferEventResponses(List<T> transferEventResponses) {
        this.transferEventResponses = transferEventResponses;
    }


    @Override
    public String toString() {
        return "TokenTransactionReceipt{" +
                "transferEventResponses=" + transferEventResponses +
                ", transactionHash='" + transactionHash + '\'' +
                ", transactionIndex='" + transactionIndex + '\'' +
                ", blockHash='" + blockHash + '\'' +
                ", blockNumber='" + blockNumber + '\'' +
                ", contractAddress='" + contractAddress + '\'' +
                ", status='" + status + '\'' +
                ", from='" + from + '\'' +
                ", to='" + to + '\'' +
                ", value='" + value + '\'' +
                ", timestamp=" + timestamp +
                ", nonce=" + nonce +
                ", gasPrice='" + gasPrice + '\'' +
                ", gasLimit=" + gasLimit +
                ", gasUsed='" + gasUsed + '\'' +
                ", logs=" + logs +
                '}';
    }
}
