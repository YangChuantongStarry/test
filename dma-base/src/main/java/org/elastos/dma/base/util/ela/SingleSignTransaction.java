//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package org.elastos.dma.base.util.ela;


import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import io.github.novacrypto.DatatypeConverter;
import org.elastos.dma.base.util.common.ErrorCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SingleSignTransaction {
    private static final Logger LOGGER = LoggerFactory.getLogger(SingleSignTransaction.class);

    public SingleSignTransaction() {
    }

    public static String genRawTransaction(JSONObject inputsAddOutpus) {
        try {
            JSONArray transaction = inputsAddOutpus.getJSONArray("Transactions");
            JSONObject json_transaction = (JSONObject)transaction.get(0);
            JSONArray utxoInputs = json_transaction.getJSONArray("UTXOInputs");
            JSONArray outputs = json_transaction.getJSONArray("Outputs");
            UTXOTxInput[] utxoTxInputs = (UTXOTxInput[])Basic.parseInputs(utxoInputs).toArray(new UTXOTxInput[utxoInputs.size()]);
            TxOutput[] txOutputs = (TxOutput[])Basic.parseOutputs(outputs).toArray(new TxOutput[outputs.size()]);
            PayloadRecord payload = Basic.parsePayloadRecord(json_transaction);
            boolean bool = json_transaction.containsKey("Memo");
            LinkedHashMap<String, Object> resultMap = new LinkedHashMap();
            new RawTx("", "");
            if (payload != null && bool) {
                return ErrorCode.ParamErr("PayloadRecord And Memo can't be used at the same time");
            } else {
                RawTx rawTx;
                if (payload == null && !bool) {
                    rawTx = Ela.makeAndSignTx(utxoTxInputs, txOutputs);
                } else if (bool) {
                    String memo = json_transaction.getString("Memo");
                    rawTx = Ela.makeAndSignTx(utxoTxInputs, txOutputs, memo);
                } else {
                    rawTx = Ela.makeAndSignTx(utxoTxInputs, txOutputs, payload);
                }

                resultMap.put("rawTx", rawTx.getRawTxString());
                resultMap.put("txHash", rawTx.getTxHash());
                LOGGER.info(Basic.getSuccess("genRawTransaction", resultMap));
                return Basic.getSuccess("genRawTransaction", resultMap);
            }
        } catch (Exception var12) {
            LOGGER.error(var12.toString());
            return var12.toString();
        }
    }

    public static String genRawTransactionByPrivateKey(JSONObject inputsAddOutpus) {
        try {
            JSONArray transaction = inputsAddOutpus.getJSONArray("Transactions");
            JSONObject json_transaction = (JSONObject)transaction.get(0);
            JSONArray PrivateKeys = json_transaction.getJSONArray("PrivateKeys");
            JSONArray outputs = json_transaction.getJSONArray("Outputs");
            List<String> privateList = Basic.parsePrivates(PrivateKeys);
            LinkedList<TxOutput> outputList = Basic.parseOutputs(outputs);
            PayloadRecord payload = Basic.parsePayloadRecord(json_transaction);
            Verify.verifyParameter(Verify.Type.ChangeAddress, json_transaction);
            String changeAddress = json_transaction.getString("ChangeAddress");
            LinkedHashMap<String, Object> resultMap = new LinkedHashMap();
            String rawTx = "";
            boolean bool = json_transaction.containsKey("Memo");
            if (payload != null && bool) {
                return ErrorCode.ParamErr("PayloadRecord And Memo can't be used at the same time");
            } else {
                if (payload == null && !bool) {
                    rawTx = FinishUtxo.makeAndSignTx(privateList, outputList, changeAddress);
                } else if (bool) {
                    String memo = json_transaction.getString("Memo");
                    rawTx = FinishUtxo.makeAndSignTx(privateList, outputList, changeAddress, memo);
                } else {
                    rawTx = FinishUtxo.makeAndSignTx(privateList, outputList, changeAddress, payload);
                }

                resultMap.put("rawTx", rawTx);
                resultMap.put("txHash", FinishUtxo.txHash);
                LOGGER.info(Basic.getSuccess("genRawTransactionByPrivateKey", resultMap));
                return Basic.getSuccess("genRawTransactionByPrivateKey", resultMap);
            }
        } catch (Exception var13) {
            LOGGER.error(var13.toString());
            return var13.toString();
        }
    }

    public static String genCrossChainRawTransaction(JSONObject inputsAddOutpus) {
        try {
            JSONArray transaction = inputsAddOutpus.getJSONArray("Transactions");
            JSONObject json_transaction = (JSONObject)transaction.get(0);
            JSONArray utxoInputs = json_transaction.getJSONArray("UTXOInputs");
            JSONArray outputs = json_transaction.getJSONArray("Outputs");
            JSONArray CrossChainAsset = json_transaction.getJSONArray("CrossChainAsset");
            JSONArray privateKeySign = json_transaction.getJSONArray("PrivateKeySign");
            UTXOTxInput[] utxoTxInputs = (UTXOTxInput[])Basic.parseInputsAddress(utxoInputs).toArray(new UTXOTxInput[utxoInputs.size()]);
            TxOutput[] txOutputs = (TxOutput[])Basic.parseCrossChainOutputs(outputs).toArray(new TxOutput[outputs.size()]);
            PayloadTransferCrossChainAsset[] payloadTransferCrossChainAssets = (PayloadTransferCrossChainAsset[])Basic.parseCrossChainAsset(CrossChainAsset).toArray(new PayloadTransferCrossChainAsset[CrossChainAsset.size()]);
            List<String> privateKeySignList = Basic.parsePrivates(privateKeySign);
            LinkedHashMap<String, Object> resultMap = new LinkedHashMap();
            RawTx rawTx = Ela.CrossChainSignTx(utxoTxInputs, txOutputs, payloadTransferCrossChainAssets, privateKeySignList);
            resultMap.put("rawTx", rawTx.getRawTxString());
            resultMap.put("txHash", rawTx.getTxHash());
            LOGGER.info(Basic.getSuccess("genCrossChainRawTransaction", resultMap));
            return Basic.getSuccess("genCrossChainRawTransaction", resultMap);
        } catch (Exception var13) {
            LOGGER.error(var13.toString());
            return var13.toString();
        }
    }

    public static String sendRawTransaction(String rawTx, String txUrl) {
        Map<String, Object> params = new HashMap();
        params.put("data", rawTx);
        Map<String, Object> map = new HashMap();
        map.put("method", "sendrawtransaction");
        map.put("params", params);
        JSONObject jsonParam = new JSONObject(map);
        System.out.println("url = " + txUrl);
        System.out.println("json = " + jsonParam);
        JSONObject responseJSONObject = HttpRequestUtil.httpPost(txUrl, jsonParam);
        LOGGER.info(responseJSONObject.toString());
        return responseJSONObject.toString();
    }

    public static String decodeRawTransaction(String rawTransaction) throws IOException {
        byte[] rawTxByte = DatatypeConverter.parseHexBinary(rawTransaction);
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(rawTxByte);
        DataInputStream dos = new DataInputStream(byteArrayInputStream);
        Map resultMap = Tx.DeSerialize(dos);
        LOGGER.info(Basic.getSuccess("decodeRawTransaction", resultMap));
        return Basic.getSuccess("decodeRawTransaction", resultMap);
    }
}
