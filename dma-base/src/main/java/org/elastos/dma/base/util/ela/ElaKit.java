//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package org.elastos.dma.base.util.ela;


import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import org.elastos.dma.base.util.common.ErrorCode;

import java.util.LinkedHashMap;

import io.github.novacrypto.DatatypeConverter;

public class ElaKit {
    public ElaKit() {
    }


    public static String genRawTransaction(JSONObject inputsAddOutpus) {
        try {
            JSONArray transaction = inputsAddOutpus.getJSONArray("Transactions");
            JSONObject json_transaction = (JSONObject) transaction.get(0);
            JSONArray utxoInputs = json_transaction.getJSONArray("UTXOInputs");
            JSONArray outputs = json_transaction.getJSONArray("Outputs");
            UTXOTxInput[] utxoTxInputs = (UTXOTxInput[]) ((UTXOTxInput[]) Basic.parseInputs(utxoInputs).toArray(new UTXOTxInput[utxoInputs.size()]));
            TxOutput[] txOutputs = (TxOutput[]) ((TxOutput[]) Basic.parseOutputs(outputs).toArray(new TxOutput[outputs.size()]));
            PayloadRegisterIdentification payload = null;
            if (json_transaction.containsKey("Payload")) {
                payload = (PayloadRegisterIdentification) JsonUtil.jsonStr2Entity(json_transaction.getString("Payload"), PayloadRegisterIdentification.class);
                String privKey = payload.getIdPrivKey();
                String address = Ela.getAddressFromPrivate(privKey);
                String programHash = DatatypeConverter.printHexBinary(Util.ToScriptHash(address));
                payload.setProgramHash(programHash);
            }

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
                return Basic.getSuccess("genRawTransaction", resultMap);
            }
        } catch (Exception var12) {
            return var12.toString();
        }
    }

    public static boolean checkAddress(String addr) {
        return Util.checkAddress(addr);
    }
}
