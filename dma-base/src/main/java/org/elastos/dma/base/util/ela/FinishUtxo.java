//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package org.elastos.dma.base.util.ela;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.elastos.dma.base.util.common.ErrorCode;
import org.elastos.dma.base.util.ela.Verify.Type;
public class FinishUtxo {
    private static String RPCURL;
    private static int FEE;
    private static int CONFIRMATION;
    private static long utxoAmount;
    private static List<UTXOTxInput> inputList;
    public static String txHash;
    public static Boolean STATE = true;
    public static List<String> addrList;

    public FinishUtxo() {
    }

    public static String makeAndSignTx(List<String> privates, LinkedList<TxOutput> outputs, String ChangeAddress) throws Exception {
        List<String> availablePrivates = finishUtxo(privates, outputs, ChangeAddress);
        RawTx rawTx = SignTxAbnormal.makeSingleSignTx((UTXOTxInput[])inputList.toArray(new UTXOTxInput[inputList.size()]), (TxOutput[])outputs.toArray(new TxOutput[outputs.size()]), availablePrivates);
        txHash = rawTx.getTxHash();
        return rawTx.getRawTxString();
    }

    public static String makeAndSignTx(List<String> privates, LinkedList<TxOutput> outputs, String ChangeAddress, PayloadRecord payloadRecord) throws Exception {
        List<String> availablePrivates = finishUtxo(privates, outputs, ChangeAddress);
        RawTx rawTx = SignTxAbnormal.makeSingleSignTx((UTXOTxInput[])inputList.toArray(new UTXOTxInput[inputList.size()]), (TxOutput[])outputs.toArray(new TxOutput[outputs.size()]), availablePrivates, payloadRecord);
        txHash = rawTx.getTxHash();
        return rawTx.getRawTxString();
    }

    public static String makeAndSignTx(List<String> privates, LinkedList<TxOutput> outputs, String ChangeAddress, String memo) throws Exception {
        List<String> availablePrivates = finishUtxo(privates, outputs, ChangeAddress);
        RawTx rawTx = SignTxAbnormal.makeSingleSignTx((UTXOTxInput[])inputList.toArray(new UTXOTxInput[inputList.size()]), (TxOutput[])outputs.toArray(new TxOutput[outputs.size()]), availablePrivates, memo);
        txHash = rawTx.getTxHash();
        return rawTx.getRawTxString();
    }

    public static List<String> finishUtxo(List<String> privates, LinkedList<TxOutput> outputs, String ChangeAddress) throws Exception {
        ArrayList<String> privateList = new ArrayList(new HashSet(privates));
        Map<String, String[]> params = new HashMap();
        String[] addressList = new String[privateList.size()];

        for(int i = 0; i < privateList.size(); ++i) {
            String address = Ela.getAddressFromPrivate((String)privateList.get(i));
            addressList[i] = address;
        }

        params.put("addresses", addressList);
        getConfig_url();
        String utxo = Rpc.call_("listunspent", params, RPCURL);
        getUtxo(utxo, outputs, ChangeAddress);
        ArrayList<String> addrArray = new ArrayList(new HashSet(addrList));
        return availablePrivate(privates, addrArray);
    }

    public static void getUtxo(String utxo, LinkedList<TxOutput> outputs, String ChangeAddress) throws SDKException {
        JSONObject jsonObject = JSONObject.parseObject(utxo);
        String error = jsonObject.getString("error");
        if (error != "null") {
            JSONObject jsonError = JSONObject.parseObject(error);
            String message = jsonError.getString("msg");
            throw new SDKException(ErrorCode.ParamErr("Getting utxo failure , " + message));
        } else {
            Object resultObject = jsonObject.get("result");
            if (resultObject.equals((Object)null)) {
                throw new SDKException(ErrorCode.ParamErr("The address is not utxo , Please check the address"));
            } else {
                JSONArray jsonArray = jsonObject.getJSONArray("result");
                List<UTXOInputSort> UTXOInputList = new ArrayList();

                int j;
                for(int i = 0; i < jsonArray.size(); ++i) {
                    JSONObject result = (JSONObject)jsonArray.get(i);
                    String txid = result.getString("txid");
                    String address = result.getString("address");
                    j = result.getInteger("vout");
                    String blockHash = getBlockHash(txid);
                    if (blockHash != null) {
                        boolean boo = unlockeUtxo(blockHash, txid, j);
                        if (boo) {
                            UTXOInputList.add(new UTXOInputSort(txid, address, j, utxoAmount));
                        }
                    }
                }

                Collections.sort(UTXOInputList);
                inputList = new LinkedList();
                long outputValue = 0L;

                long ChangeValue;
                for(int k = 0; k < outputs.size(); ++k) {
                    TxOutput output = (TxOutput)outputs.get(k);
                    ChangeValue = output.getValue();
                    outputValue += ChangeValue;
                }

                long inputValue = 0L;
                addrList = new ArrayList();

                for(j = 0; j < UTXOInputList.size(); ++j) {
                    UTXOInputSort input = (UTXOInputSort)UTXOInputList.get(j);
                    String inputTxid = input.getTxid();
                    String inputAddress = input.getAddress();
                    int inputVont = input.getVont();
                    inputValue += input.getAmount();
                    inputList.add(new UTXOTxInput(inputTxid, inputVont, "", inputAddress));
                    addrList.add(inputAddress);
                    if (inputValue >= outputValue + (long)FEE) {
                        break;
                    }
                }

                if (inputValue >= outputValue + (long)FEE) {
                    ChangeValue = inputValue - outputValue - (long)FEE;
                    outputs.add(new TxOutput(ChangeAddress, ChangeValue));
                } else {
                    throw new SDKException(ErrorCode.ParamErr("Utxo deficiency , inputValue : " + inputValue + " , outputValue :" + outputValue));
                }
            }
        }
    }

    public static Boolean unlockeUtxo(String blockHash, String txid, int vout) {
        LinkedHashMap<String, Object> paramsMap = new LinkedHashMap();
        paramsMap.put("blockhash", blockHash);
        paramsMap.put("verbosity", 2);
        String block = Rpc.call_("getblock", paramsMap, RPCURL);
        JSONObject jsonObject = JSONObject.parseObject(block);
        String error = jsonObject.getString("error");
        JSONObject results;
        if (error != "null") {
            results = JSONObject.parseObject(error);
            String message = results.getString("msg");
            System.out.println("获取区块信息失败 ：" + message);
            return false;
        } else {
            results = jsonObject.getJSONObject("result");
            JSONArray txArray = results.getJSONArray("tx");

            for(int i = 0; i < txArray.size(); ++i) {
                JSONObject tx = (JSONObject)txArray.get(i);
                String txHash = tx.getString("txid");
                if (txHash.equals(txid)) {
                    long locktime = tx.getLong("locktime");
                    JSONArray voutJson = tx.getJSONArray("vout");
                    JSONObject output = (JSONObject)voutJson.get(vout);
                    long outputlock = output.getLong("outputlock");
                    String value = output.getString("value");
                    long valueLong = Util.IntByString(value);
                    utxoAmount = valueLong;
                    if (outputlock == 0L) {
                        return true;
                    }

                    System.out.println("锁仓 txid : " + txid);
                }
            }

            return false;
        }
    }

    public static String getBlockHash(String txid) {
        LinkedHashMap<String, Object> paramsMap = new LinkedHashMap();
        paramsMap.put("txid", txid);
        paramsMap.put("verbose", true);
        String Transcation = Rpc.call_("getrawtransaction", paramsMap, RPCURL);
        JSONObject jsonObject = JSONObject.parseObject(Transcation);
        String error = jsonObject.getString("error");
        JSONObject result;
        if (error != "null") {
            result = JSONObject.parseObject(error);
            String message = result.getString("msg");
            System.out.println("获取区块信息失败 ：" + message);
            return "";
        } else {
            result = jsonObject.getJSONObject("result");
            int confirmations = result.getInteger("confirmations");
            return confirmations >= CONFIRMATION ? result.getString("blockhash") : null;
        }
    }

    public static void getConfig_url() throws Exception {
        String content = "";

        String host;
        try {
            File directory = new File("");
            host = directory.getCanonicalPath();
            File file = new File(host + "/java-config.json");
            content = FileUtils.readFileToString(file,"UTF-8");
        } catch (Exception var4) {
            throw new SDKException(ErrorCode.ParamErr(var4.toString()));
        }


        JSONObject jsonObject = JSONObject.parseObject(content);
        Verify.verifyParameter(Type.Host, jsonObject);
        Verify.verifyParameter(Type.Confirmation, jsonObject);
        Verify.verifyParameter(Type.Fee, jsonObject);
        host = jsonObject.getString("Host");
        FEE = jsonObject.getInteger("Fee");
        RPCURL = "http://" + host;
        CONFIRMATION = jsonObject.getInteger("Confirmation");
        if (CONFIRMATION == 0) {
            CONFIRMATION = 16;
        }

    }

    public static List<String> availablePrivate(List<String> privateList, List<String> addressList) {
        if (privateList.size() == addressList.size()) {
            return privateList;
        } else {
            List<String> availablePrivate = new ArrayList();
            HashMap privateMap = new HashMap();

            for(int i = 0; i < privateList.size(); ++i) {
                privateMap.put(Ela.getAddressFromPrivate((String)privateList.get(i)), privateList.get(i));
            }

            Iterator keys = privateMap.keySet().iterator();

            while(keys.hasNext()) {
                String key = (String)keys.next();

                for(int j = 0; j < addressList.size(); ++j) {
                    if (key.equals(addressList.get(j))) {
                        availablePrivate.add((String)privateMap.get(key));
                    }
                }
            }

            return availablePrivate;
        }
    }
}
