//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package org.elastos.dma.base.util.ela;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;

import org.elastos.dma.base.util.common.ErrorCode;
import org.elastos.dma.base.util.ela.Verify.Type;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Basic {
    private static final Logger LOGGER = LoggerFactory.getLogger(Basic.class);

    public Basic() {
    }

    public static String genPrivateKey() {
        String privateKey = Ela.getPrivateKey();
        LOGGER.info(getSuccess("genPrivateKey", privateKey));
        return getSuccess("genPrivateKey", privateKey);
    }

    public static String genPublicKey(JSONObject jsonObject) {
        try {
            Verify.verifyParameter(Type.PrivateKeyUpper, jsonObject);
        } catch (Exception var3) {
            LOGGER.error(var3.toString());
            return var3.toString();
        }

        String privateKey = jsonObject.getString("PrivateKey");
        String publicKey = Ela.getPublicFromPrivate(privateKey);
        LOGGER.info(getSuccess("genPublicKey", publicKey));
        return getSuccess("genPublicKey", publicKey);
    }

    public static String genAddress(JSONObject jsonObject) {
        try {
            Verify.verifyParameter(Verify.Type.PrivateKeyUpper, jsonObject);
        } catch (Exception var3) {
            LOGGER.error(var3.toString());
            return var3.toString();
        }

        String privateKey = jsonObject.getString("PrivateKey");
        String address = Ela.getAddressFromPrivate(privateKey);
        LOGGER.info(getSuccess("genAddress", address));
        return getSuccess("genAddress", address);
    }

    public static String genIdentityID(JSONObject jsonObject) {
        try {
            Verify.verifyParameter(Type.PrivateKeyUpper, jsonObject);
        } catch (Exception var3) {
            LOGGER.error(var3.toString());
            return var3.toString();
        }

        String privateKey = jsonObject.getString("PrivateKey");
        String address = Ela.getIdentityIDFromPrivate(privateKey);
        LOGGER.info(getSuccess("genIdentityID", address));
        return getSuccess("genIdentityID", address);
    }

    public static String gen_priv_pub_addr() {
        String privateKey = Ela.getPrivateKey();
        String publicKey = Ela.getPublicFromPrivate(privateKey);
        String address = Ela.getAddressFromPrivate(privateKey);
        LinkedHashMap<String, Object> resultMap = new LinkedHashMap();
        resultMap.put("PrivateKey", privateKey);
        resultMap.put("PublicKey", publicKey);
        resultMap.put("Address", address);
        LOGGER.info(getSuccess("gen_priv_pub_addr", resultMap));
        return getSuccess("gen_priv_pub_addr", resultMap);
    }

    public static String checkAddress(JSONObject addresses) {
        LinkedHashMap<String, Object> resultMap = new LinkedHashMap();
        JSONArray addressesJSONArray = addresses.getJSONArray("Addresses");
        Object addressesObject = addressesJSONArray.get(0);
        int i;
        if (addressesObject instanceof String) {
            for(i = 0; i < addressesJSONArray.size(); ++i) {
                boolean boo = Util.checkAddress((String)addressesJSONArray.get(i));
                resultMap.put((String)addressesJSONArray.get(i), boo);
            }
        } else {
            for(i = 0; i < addressesJSONArray.size(); ++i) {
                JSONObject o = (JSONObject)addressesJSONArray.get(i);
                String address = o.getString("address");
                boolean boo = Util.checkAddress(address);
                resultMap.put(address, boo);
            }
        }

        LOGGER.info(getSuccess("checkAddress", resultMap));
        return getSuccess("checkAddress", resultMap);
    }

    public static String getSuccess(String action, Object resultMap) {
        HashMap map = new HashMap();
        map.put("Action", action);
        map.put("Desc", "SUCCESS");
        map.put("Result", resultMap);
        return JSON.toJSONString(map);
    }

    public static String genGenesisAddress(JSONObject jsonObject) {
        String address = null;

        try {
            Verify.verifyParameter(Verify.Type.BlockHashUpper, jsonObject);
            String blockHash = jsonObject.getString("BlockHash");
            address = ECKey.toGenesisSignAddress(blockHash);
        } catch (SDKException var3) {
            LOGGER.error(var3.toString());
            return var3.toString();
        }

        LOGGER.info(getSuccess("genGenesisAddress", address));
        return getSuccess("genGenesisAddress", address);
    }

    public static String genMultiSignAddress(JSONObject jsonObject) {
        String address = null;

        try {
            JSONArray PrivateKeys = jsonObject.getJSONArray("PrivateKeys");
            List<String> privateKeyList = new ArrayList();

            int M;
            for(M = 0; M < PrivateKeys.size(); ++M) {
                Verify.verifyParameter(Verify.Type.PrivateKeyLower, (JSONObject)PrivateKeys.get(M));
                JSONObject privateKeyJson = (JSONObject)PrivateKeys.get(M);
                String privatekey = privateKeyJson.getString("privateKey");
                privateKeyList.add(privatekey);
            }

            M = jsonObject.getInteger("M");
            address = Ela.getMultiSignAddress(privateKeyList, M);
            return getSuccess("genMultiSignAddress", address);
        } catch (SDKException var7) {
            LOGGER.error(var7.toString());
            return var7.toString();
        }
    }

    public static PayloadRecord parsePayloadRecord(JSONObject json_transaction) throws SDKException {
        Object payload = json_transaction.get("PayloadRecord");
        if (payload != null) {
            JSONObject PayloadObject = json_transaction.getJSONObject("PayloadRecord");
            Verify.verifyParameter(Verify.Type.RecordTypeLower, PayloadObject);
            Verify.verifyParameter(Verify.Type.RecordDataLower, PayloadObject);
            return new PayloadRecord(PayloadObject.getString("recordType"), PayloadObject.getString("recordData"));
        } else {
            return null;
        }
    }

    public static LinkedList<TxOutput> parseOutputs(JSONArray outputs) throws SDKException {
        LinkedList<TxOutput> outputList = new LinkedList();

        for(int t = 0; t < outputs.size(); ++t) {
            JSONObject output = (JSONObject)outputs.get(t);
            Verify.verifyParameter(Type.AddressLower, output);
            Verify.verifyParameter(Type.AmountLower, output);
            long amount = output.getLong("amount");
            String address = output.getString("address");
            outputList.add(new TxOutput(address, amount));
        }

        return outputList;
    }

    public static LinkedList<TxOutput> parseCrossChainOutputs(JSONArray outputs) throws SDKException {
        LinkedList<TxOutput> outputList = new LinkedList();

        for(int t = 0; t < outputs.size(); ++t) {
            JSONObject output = (JSONObject)outputs.get(t);
            Verify.verifyParameter(Type.AmountLower, output);
            long amount = output.getLong("amount");
            String address = output.getString("address");
            outputList.add(new TxOutput(address, amount));
        }

        return outputList;
    }

    public static List<UTXOTxInput> parseInputs(JSONArray utxoInputs) throws SDKException {
        List<UTXOTxInput> inputList = new LinkedList();

        for(int i = 0; i < utxoInputs.size(); ++i) {
            JSONObject utxoInput = (JSONObject)utxoInputs.get(i);
            Verify.verifyParameter(Type.TxidLower, utxoInput);
            Verify.verifyParameter(Type.IndexLower, utxoInput);
            Verify.verifyParameter(Type.PrivateKeyLower, utxoInput);
            String txid = utxoInput.getString("txid");
            int index = utxoInput.getInteger("index");
            String privateKey = utxoInput.getString("privateKey");
            String address = Ela.getAddressFromPrivate(privateKey);
            inputList.add(new UTXOTxInput(txid, index, privateKey, address));
        }

        return inputList;
    }

    public static List<UTXOTxInput> parseInputsAddress(JSONArray utxoInputs) throws SDKException {
        List<UTXOTxInput> inputList = new LinkedList();

        for(int i = 0; i < utxoInputs.size(); ++i) {
            JSONObject utxoInput = (JSONObject)utxoInputs.get(i);
            Verify.verifyParameter(Type.TxidLower, utxoInput);
            Verify.verifyParameter(Type.IndexLower, utxoInput);
            Verify.verifyParameter(Type.AddressLower, utxoInput);
            String txid = utxoInput.getString("txid");
            int index = utxoInput.getInteger("index");
            String address = utxoInput.getString("address");
            inputList.add(new UTXOTxInput(txid, index, "", address));
        }

        return inputList;
    }

    public static List<String> parsePrivates(JSONArray PrivateKeys) throws SDKException {
        List<String> privateList = new LinkedList();

        for(int i = 0; i < PrivateKeys.size(); ++i) {
            JSONObject utxoInput = (JSONObject)PrivateKeys.get(i);
            Verify.verifyParameter(Type.PrivateKeyLower, utxoInput);
            privateList.add(utxoInput.getString("privateKey"));
        }

        return privateList;
    }

    public static ArrayList<String> genPrivateKeySignByM(int M, JSONArray privateKeyScripte) throws SDKException {
        if (M > privateKeyScripte.size()) {
            throw new SDKException(ErrorCode.ParamErr("M cannot be greater than the number of privateKeys"));
        } else {
            ArrayList<String> privateKeySignList = new ArrayList();

            for(int n = 0; n < M; ++n) {
                JSONObject privateKeys = (JSONObject)privateKeyScripte.get(n);
                String privatekey = privateKeys.getString("privateKey");
                privateKeySignList.add(privatekey);
            }

            return privateKeySignList;
        }
    }

    public static ArrayList<PayloadTransferCrossChainAsset> parseCrossChainAsset(JSONArray CrossChainAsset) throws SDKException {
        ArrayList<PayloadTransferCrossChainAsset> CrossChainAssetList = new ArrayList();

        for(int n = 0; n < CrossChainAsset.size(); ++n) {
            JSONObject output = (JSONObject)CrossChainAsset.get(n);
            Verify.verifyParameter(Type.AmountLower, output);
            String address = output.getString("address");
            Long amount = output.getLong("amount");
            CrossChainAssetList.add(new PayloadTransferCrossChainAsset(address, amount, n));
        }

        return CrossChainAssetList;
    }
}
