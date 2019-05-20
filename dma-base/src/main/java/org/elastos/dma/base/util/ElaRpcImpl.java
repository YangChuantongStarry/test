package org.elastos.dma.base.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import io.github.novacrypto.DatatypeConverter;
import org.apache.commons.lang.StringUtils;
import org.elastos.dma.base.conf.BasicConstant;
import org.elastos.dma.base.conf.DidConstant;
import org.elastos.dma.base.conf.NodeConstant;
import org.elastos.dma.base.conf.RetCodeConstant;
import org.elastos.dma.base.exception.ApiRequestDataException;
import org.elastos.dma.base.util.ela.*;
import org.elastos.dma.base.util.ela.ElaUtil;
import org.elastos.dma.utility.entity.ela.*;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.*;

/**
 * @Author: yangchuantong
 * @Description:
 * @Date:Created in  2018/10/31 13:47
 */
public abstract class ElaRpcImpl<T> implements ElaRpc {

    private static String INVALIDTXID = "Invalid Txid";
    private static String INVALIDINDEX = "Invalid Index";
    private static String INVALIDPRPVATEKEY = "Invalid PrivateKey";
    private static String INVALIDADDRESS = "Invalid Address";
    private static String INVALIDCHANGEADDRESS = "Invalid ChangeAddress";
    private static String INVALIDAMOUNT = "Invalid Amount";
    public static String INVALFEE = "Invalid FEE";
    public static String INVALCONFIRMATION = "Invalid CONFIRMATION";
    private static String TXIDNOTNULL = "No Txid Field";
    private static String INDEXNOTNULL = "No Index Field";
    private static String PRPVATEKEYNOTNULL = "No PrivateKey Field";
    private static String ADDRESSNOTNULL = "No Address Field";
    private static String CHANGEADDRESSNOTNULL = "No ChangeAddress Field";
    private static String AMOUNTNOTNULL = "No Amount Field";
    public static String FEENOTNULL = "No FEE Field";
    public static String HOSTNOTNULL = "No Host Field";
    public static String CONFIRMATIONNOTNULL = "No CONFIRMATION Field";

    protected String nodeUrl;


    public static final String CHARSET = "UTF-8";

    public ElaRpcImpl(String nodeUrl) {
        this.nodeUrl = nodeUrl.endsWith("/") ? nodeUrl.substring(0, nodeUrl.length() - 1) : nodeUrl;
    }


    /**
     * 根据地址获取余额
     *
     * @param address 地址
     * @return 余额
     */
    @Override
    public String balance(String address) {
        checkAddr(address);
        String result = getUtxoByAddr(address);
        if (StringUtils.isEmpty(result)) {
            return "0.0";
        }
        Map<String, Object> resultMap = (Map<String, Object>) JSON.parse(result);
        Object resObj = resultMap.get("Result");
        if (resObj == null || StrKit.isBlank(resObj + "") || (resObj + "").equalsIgnoreCase("null")) {
            return "0.0";
        }
        Map m = ((List<Map>) resultMap.get("Result")).get(0);
        List<Map> lm = (List<Map>) m.get("Utxo");
        BigDecimal total = new BigDecimal("0.0");
        for (int i = 0; i < lm.size(); i++) {
            Map md = lm.get(i);
            BigDecimal v = new BigDecimal((String) md.get("Value"));
            total = total.add(v);
        }
        return total.toString();
    }

    /**
     * 转账
     *
     * @param privateKey 私钥
     * @param _to        接收方
     * @param _value     数量
     * @return
     */
    @Override
    public String transfer(String privateKey, String _to, BigDecimal _value) throws Exception {
        checkAddr(_to);
        //发送方地址  私钥
        List<Map<String, String>> sender = new ArrayList<>();
        Map<String, String> senderMap = new HashMap<>();
        senderMap.put("address", Ela.getAddressFromPrivate(privateKey));
        senderMap.put("privateKey", privateKey);
        sender.add(senderMap);
        //接收方 地址 金额
        List<Map<String, Object>> receiver = new ArrayList<>();
        Map<String, Object> receiverMap = new HashMap<>();
        receiverMap.put("address", _to);
        receiverMap.put("amount", _value);
        receiver.add(receiverMap);

        TransferParamEntity transferParamEntity = new TransferParamEntity();

        transferParamEntity.setSender(sender);
        transferParamEntity.setReceiver(receiver);
        transferParamEntity.setType(ChainType.MAIN_CHAIN);

        return transfer(transferParamEntity);
    }

    /**
     * 获取本地交易签名
     *
     * @param privateKey 私钥
     * @param _to        接收者地址
     * @param _value     金额
     * @return
     * @throws Exception
     */
    @Override
    public String getRawTx(String privateKey, String _to, BigDecimal _value) throws Exception {
        checkAddr(_to);
        //发送方地址  私钥
        List<Map<String, String>> sender = new ArrayList<>();
        Map<String, String> senderMap = new HashMap<>();
        senderMap.put("address", Ela.getAddressFromPrivate(privateKey));
        senderMap.put("privateKey", privateKey);
        sender.add(senderMap);
        //接收方 地址 金额
        List<Map<String, Object>> receiver = new ArrayList<>();
        Map<String, Object> receiverMap = new HashMap<>();
        receiverMap.put("address", _to);
        receiverMap.put("amount", _value);
        receiver.add(receiverMap);

        TransferParamEntity transferParamEntity = new TransferParamEntity();

        transferParamEntity.setSender(sender);
        transferParamEntity.setReceiver(receiver);
        transferParamEntity.setType(ChainType.MAIN_CHAIN);
        return genRawTx(transferParamEntity);
    }

    /**
     * 批量转账
     *
     * @param privateKey 私钥
     * @param _to        接收方
     * @param _value     数量
     * @return
     */

    public String transferWithArray(String privateKey, ArrayList<String> _to, ArrayList<String> _value) throws Exception {
        //发送方地址  私钥
        List<Map<String, String>> sender = new ArrayList<>();
        Map<String, String> senderMap = new HashMap<>();
        senderMap.put("address", Ela.getAddressFromPrivate(privateKey));
        senderMap.put("privateKey", privateKey);
        sender.add(senderMap);
        //接收方 地址 金额
        List<Map<String, String>> receiver = new ArrayList<>();
        Map<String, String> receiverMap = new HashMap<>();
        for (int i = 0; i < _to.size(); i++) {
            receiverMap.put("address", _to.get(i));
            receiverMap.put("amount", _value.get(i));
            receiver.add(receiverMap);
        }
        TransferParamEntity transferParamEntity = new TransferParamEntity();
        transferParamEntity.setSender(sender);
        transferParamEntity.setReceiver(receiver);
        transferParamEntity.setType(ChainType.MAIN_CHAIN);

        return transfer(transferParamEntity);
    }


    /**
     * 交易详情
     *
     * @param txId 交易哈希
     * @return
     */
    @Override
    public TransactionInfo transactionInfo(String txId) {
        String requestUrl = nodeUrl + NodeConstant.TRANSACTION + txId;
        String result = HttpKit.get(requestUrl);
        Map<String, Object> resultMap = (Map<String, Object>) JSON.parse(result);
        if (resultMap == null) {
            return null;
        }
        String Desc = (String) resultMap.get("Desc");
        Integer Error = (Integer) resultMap.get("Error");
        if (!Desc.equals("Success") || Error != 0) {
            return null;
        }
        Map<String, Object> Result = (Map<String, Object>) resultMap.get("Result");

        TransactionInfo transactionInfo = new TransactionInfo();

        String txid = (String) Result.get("txid");
        String hash1 = (String) Result.get("hash");
        Integer size = (Integer) Result.get("size");
        Integer vsize = (Integer) Result.get("vsize");
        Integer version = (Integer) Result.get("version");
        Integer locktime = (Integer) Result.get("locktime");
        String blockhash = (String) Result.get("blockhash");
        Integer confirmations = (Integer) Result.get("confirmations");
        Integer time = (Integer) Result.get("time");


        BigDecimal outValue = new BigDecimal("0");//总输出金额
        List<Vout> voutList = new ArrayList<>();//输出Vout
        List<Map<String, Object>> vouts = (List<Map<String, Object>>) Result.get("vout");
        for (Map<String, Object> map : vouts) {
            Vout vout = new Vout();
            String address = (String) map.get("address");
            String value = (String) map.get("value");
            outValue = outValue.add(new BigDecimal(value));
            vout.setAddress(address);
            vout.setValue(value);
            voutList.add(vout);
        }

        //输入txid
        List<Vin> vinList = new ArrayList<>();
        List<Map<String, Object>> vins = (List<Map<String, Object>>) Result.get("vin");
        for (Map<String, Object> map : vins) {
            Vin vin = new Vin();
            String tid = (String) map.get("txid");
            vin.setTxid(tid);
            vinList.add(vin);
        }
        //输入address
        List<String> programList = new ArrayList<>();
        List<Map<String, Object>> programs = (List<Map<String, Object>>) Result.get("programs");
        for (int i = 0; i < programs.size(); i++) {
            String code = (String) programs.get(i).get("code");
            String addrsee = codeToAddress(code);
            programList.add(addrsee);
        }

        BigDecimal inValue = new BigDecimal("0");//总输入金额

        //输入address value
        for (int i = 0; i < vinList.size(); i++) {
            Vin vin = vinList.get(i);
            String vinTxid = vin.getTxid();
            List<Vout> list = getVoutByHash(vinTxid);
            for (Vout vout : list) {
                String outAddress = vout.getAddress();
                if (programList.contains(outAddress)) {
                    inValue = inValue.add(new BigDecimal(vout.getValue()));
                    vin.setAddress(outAddress);
                    vin.setValue(vout.getValue());
                    break;
                }
            }
            vinList.remove(i);
            vinList.add(i, vin);
        }

        String fee = BigDecimalUtils.subtract(inValue.toPlainString(), outValue.toPlainString()).toPlainString();
        transactionInfo.setBlockhash(blockhash);
        transactionInfo.setConfirmations(confirmations);
        transactionInfo.setHash(hash1);
        transactionInfo.setLocktime(locktime);
        transactionInfo.setSize(size);
        transactionInfo.setTime(Long.valueOf(time.toString()));
        transactionInfo.setTxid(txid);
        transactionInfo.setVin(vinList);
        transactionInfo.setVout(voutList);
        transactionInfo.setVersion(version);
        transactionInfo.setVsize(vsize);
        transactionInfo.setFee(fee);

        return transactionInfo;
    }


    /**
     * 获取交易状态
     *
     * @param txid
     */
    public Boolean getStatusByHash(String txid) {
        String requestUrl = nodeUrl + NodeConstant.TRANSACTION + txid;
        String result = HttpKit.get(requestUrl);
        Map<String, Object> resultMap = (Map<String, Object>) JSON.parse(result);
        if (resultMap == null) {
            return null;
        }
        String Desc = (String) resultMap.get("Desc");
        Integer Error = (Integer) resultMap.get("Error");
        if (!Desc.equals("Success") || Error != 0) {
            return null;
        }
        Map<String, Object> Result = (Map<String, Object>) resultMap.get("Result");
        Integer confirmations = (Integer) Result.get("confirmations");
        if (confirmations > 0) {
            return true;
        } else {
            return false;
        }
    }


    /**
     * 反解析rawTransaction得到TXid,address,value
     *
     * @param rawTransaction
     * @return
     * @throws IOException
     */
    public String decodeRawTx(String rawTransaction) throws IOException {
        byte[] rawTxByte = DatatypeConverter.parseHexBinary(rawTransaction);
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(rawTxByte);
        DataInputStream dos = new DataInputStream(byteArrayInputStream);
        Map resultMap = Tx.DeSerialize(dos);
        return Basic.getSuccess("0", resultMap);
    }

    /**
     * 地址校验
     *
     * @param address 地址
     * @return
     */

    @Override
    public Boolean checkAddr(String address) {
        return ElaKit.checkAddress(address);
    }


    /**
     * 发送交易签名
     *
     * @param rawData 签名
     * @param type    链类型
     * @return
     */
    public String sendTx(String rawData, ChainType type) {
        RawTxEntity entity = new RawTxEntity();
        entity.setData(rawData);
        entity.setType(type);
        return sendRawTx(entity);
    }


    private List<Vout> getVoutByHash(String hash) {
        String requestUrl = nodeUrl + NodeConstant.TRANSACTION + hash;
        String result = HttpKit.get(requestUrl);
        if (StringUtils.isBlank(result)) {
            return null;
        }
        Map<String, Object> resultMap = (Map<String, Object>) JSON.parse(result);
        if (resultMap == null) {
            return null;
        }
        Map<String, Object> Result = (Map<String, Object>) resultMap.get("Result");
        if (Result == null) {
            return null;
        }

        List<Vout> voutList = new ArrayList<>();
        List<Map<String, Object>> vouts = (List<Map<String, Object>>) Result.get("vout");
        for (Map<String, Object> map : vouts) {
            Vout vout = new Vout();
            String address = (String) map.get("address");
            String value = (String) map.get("value");
            vout.setAddress(address);
            vout.setValue(value);
            voutList.add(vout);
        }
        return voutList;
    }


    private String codeToAddress(String code) {
        byte[] program = DatatypeConverter.parseHexBinary(code);
        return Util.ToAddress(Util.ToCodeHash(program, 1));
    }


    private String getUtxoByAddr(String addr) {
        String result = HttpKit.get(nodeUrl + NodeConstant.UTXO_BY_ADDR + addr);
        return result;
    }


    private Map<String, Object> genHdTx(HdTxEntity hdTxEntity, List<List<Map>> utxoList) throws Exception {

        String data = hdTxEntity.getMemo();
        HdTxEntity.Output[] outputs = hdTxEntity.getOutputs();
        double smAmt = 0;
        for (int i = 0; i < outputs.length; i++) {
            smAmt += outputs[i].getAmt() / (BasicConstant.ONE_ELA * 1.0);
        }
        Map<String, Object> paraListMap = new HashMap<>();
        List txList = new ArrayList<>();
        paraListMap.put("Transactions", txList);
        Map txListMap = new HashMap();
        txList.add(txListMap);
        int index = -1;
        double spendMoney = 0.0;
        boolean hasEnoughFee = false;
        List utxoInputsArray = new ArrayList<>();
        txListMap.put("UTXOInputs", utxoInputsArray);
        for (int j = 0; j < utxoList.size(); j++) {
            List<Map> utxolm = utxoList.get(j);
            String addr = hdTxEntity.getInputs()[j];
            for (int i = 0; i < utxolm.size(); i++) {
                index = i;
                spendMoney += Double.valueOf(utxolm.get(i).get("Value") + "");
                if (Math.round(spendMoney * BasicConstant.ONE_ELA) >= Math.round((smAmt + BasicConstant.FEE) * BasicConstant.ONE_ELA)) {
                    hasEnoughFee = true;
                    break;
                }
            }
            for (int i = 0; i <= index; i++) {
                Map<String, Object> utxoInputsDetail = new HashMap<>();
                Map<String, Object> utxoM = utxolm.get(i);
                utxoInputsDetail.put("txid", utxoM.get("Txid"));
                utxoInputsDetail.put("index", utxoM.get("Index"));
                utxoInputsDetail.put("address", addr);
                utxoInputsArray.add(utxoInputsDetail);
            }
            if (hasEnoughFee) {
                break;
            }
        }

        if (!hasEnoughFee) {
            throw new ApiRequestDataException("Not Enough UTXO");
        }
        List utxoOutputsArray = new ArrayList<>();
        txListMap.put("Outputs", utxoOutputsArray);
        for (int i = 0; i < outputs.length; i++) {
            Map<String, Object> utxoOutputsDetail = new HashMap<>();
            utxoOutputsDetail.put("address", outputs[i].getAddr());
            utxoOutputsDetail.put("amount", outputs[i].getAmt());
            utxoOutputsArray.add(utxoOutputsDetail);
        }
        double leftMoney = (spendMoney - (BasicConstant.FEE + smAmt));
        if (Math.round(leftMoney * BasicConstant.ONE_ELA) > Math.round(BasicConstant.FEE * BasicConstant.ONE_ELA)) {
            Map<String, Object> utxoOutputsDetail = new HashMap<>();
            utxoOutputsDetail.put("address", hdTxEntity.getInputs()[0]);
            utxoOutputsDetail.put("amount", Math.round(leftMoney * BasicConstant.ONE_ELA));
            utxoOutputsArray.add(utxoOutputsDetail);
        }

        txListMap.put("Fee", BasicConstant.FEE * BasicConstant.ONE_ELA);
        return paraListMap;
    }


    private RawTx genRawTransaction(JSONObject inputsAddOutpus) throws Exception {
        JSONArray transaction = inputsAddOutpus.getJSONArray("Transactions");
        JSONObject json_transaction = (JSONObject) transaction.get(0);
        JSONArray utxoInputs = json_transaction.getJSONArray("UTXOInputs");
        List<UTXOTxInput> inputList = new LinkedList();

        String state;
        /* String address;*/
        for (int i = 0; i < utxoInputs.size(); ++i) {
            JSONObject utxoInput = (JSONObject) utxoInputs.get(i);
            String state1 = checkInputs("genRawTransaction", utxoInput);
            if (state1 != null) {
                return null;
            }

            String txid = utxoInput.getString("txid");
            state = utxoInput.getString("index");
            String privateKey = utxoInput.getString("privateKey");
            String address = Ela.getAddressFromPrivate(privateKey);
            inputList.add(new UTXOTxInput(txid, Integer.parseInt(state), privateKey, address));
        }

        JSONArray outputs = json_transaction.getJSONArray("Outputs");
        List<TxOutput> outputList = new LinkedList();

        for (int j = 0; j < outputs.size(); ++j) {
            JSONObject output = (JSONObject) outputs.get(j);
            state = checkOutputs("genRawTransaction", output);
            if (state != null) {
                return null;
            }

            String address = output.getString("address");
            long amount = output.getLong("amount");
            outputList.add(new TxOutput(address, amount));
        }
        RawTx rawTx = Ela.makeAndSignTx((UTXOTxInput[]) inputList.toArray(new UTXOTxInput[utxoInputs.size()]), (TxOutput[]) outputList.toArray(new TxOutput[outputs.size()]));
        return rawTx;
    }


    private String checkOutputs(String action, JSONObject outputs) {
        Object address = outputs.get("address");
        if (address != null) {
            if (!ElaUtil.checkAddress((String) address)) {
                return error(action, INVALIDADDRESS);
            } else {
                Object amount = outputs.get("amount");
                if (amount != null) {
                    return !ElaUtil.checkAmount(amount) ? error(action, INVALIDAMOUNT) : null;
                } else {
                    return error(action, AMOUNTNOTNULL);
                }
            }
        } else {
            return error(action, ADDRESSNOTNULL);
        }
    }

    private String error(String action, String result) {
        LinkedHashMap<String, Object> map = new LinkedHashMap();
        map.put("Action", action);
        map.put("Desc", "ERROR");
        map.put("Result", result);
        JSONObject jsonParam = new JSONObject(map);
        return jsonParam.toString();
    }

    private String checkInputs(String action, JSONObject utxoInput) {
        Object txid = utxoInput.get("txid");
        if (txid != null) {
            if (!ElaUtil.checkPrivateKey((String) txid)) {
                return error(action, INVALIDTXID);
            } else {
                Object index = utxoInput.get("index");
                if (index != null) {
                    return !ElaUtil.checkAmount(index) ? error(action, INVALIDINDEX) : checkPrivateKey(action, utxoInput, "privateKey");
                } else {
                    return error(action, INDEXNOTNULL);
                }
            }
        } else {
            return error(action, TXIDNOTNULL);
        }
    }

    private String checkPrivateKey(String action, JSONObject utxoInput, String PrivateKey) {
        Object privateKey = utxoInput.get(PrivateKey);
        if (privateKey != null) {
            return !ElaUtil.checkPrivateKey((String) privateKey) ? error(action, INVALIDPRPVATEKEY) : null;
        } else {
            return error(action, PRPVATEKEYNOTNULL);
        }
    }


    public String transfer(TransferParamEntity param) throws Exception {
        String rawTx = genRawTx(param);
        ChainType type = param.getType();//链类型
        return sendTx(rawTx, type);
    }


    private String genRawTx(TransferParamEntity param) throws Exception {
        //接收方
        List<LinkedHashMap> rcv = (List<LinkedHashMap>) param.getReceiver();
        //发送方
        List<Map> sdr = (List<Map>) param.getSender();

        //接收方地址
        List<String> addrList = new ArrayList<>();
        //接收方金额
        List<Double> valList = new ArrayList<>();

        Double totalAmt = 0.0;//总金额

        for (int i = 0; i < rcv.size(); i++) {
            Map m = rcv.get(i);
            addrList.add((String) m.get("address"));
            Double tmpAmt = Double.valueOf(m.get("amount").toString());
            valList.add(tmpAmt);
            totalAmt += tmpAmt;
        }
        //发送方地址
        List<String> sdrAddrs = new ArrayList<>();
        //发送方私钥
        List<String> sdrPrivs = new ArrayList<>();
        for (int i = 0; i < sdr.size(); i++) {
            Map m = sdr.get(i);
            String address = (String) m.get("address");
            String privKey = (String) m.get("privateKey");
            sdrAddrs.add(address);
            sdrPrivs.add(privKey);
        }

        String memo = param.getMemo();//备注
        ChainType type = param.getType();//链类型
        String response = gen(totalAmt, sdrPrivs, sdrAddrs,
                addrList, valList, memo, type);
        Object orst = ((Map<String, Object>) JSON.parse(response)).get("Result");
        if ((orst instanceof Map) == false) {
            throw new ApiRequestDataException("Not valid request Data");
        }
        Map<String, Object> rawM = (Map<String, Object>) orst;
        String rawTx = (String) rawM.get("rawTx");
        String txHash = (String) rawM.get("txHash");
        return rawTx;
    }

    /**
     * send a transaction to blockchain.
     *
     * @param smAmt    总金额
     * @param prvKeys  发送方私钥
     * @param sdrAddrs 发送方地址
     * @param addrs    接收方地址
     * @param amts     接收方金额
     * @param data     备注
     * @param type     链类型
     * @return
     * @throws Exception
     */

    private String gen(double smAmt, List<String> prvKeys, List<String> sdrAddrs, List<String> addrs, List<Double> amts, String data, ChainType type) throws Exception {
        //获取总utxo
        List<String> utxoStrLst = getUtxoByAddr(sdrAddrs, type);
        //解析后的总utxo
        List<List<Map>> utxoTotal = new ArrayList<>();
        for (int i = 0; i < utxoStrLst.size(); i++) {
            String utxoStr = utxoStrLst.get(i);
            //解析
            List<Map> utxo = stripUtxo(utxoStr);
            utxoTotal.add(utxo);
        }

        if (utxoTotal == null) {
            throw new ApiRequestDataException(Errors.NOT_ENOUGH_UTXO.val());
        }

        //垮链转账
        if (type == ChainType.MAIN_DID_CROSS_CHAIN || type == ChainType.DID_MAIN_CROSS_CHAIN) {
            return genCrossTx(smAmt, utxoTotal, prvKeys, sdrAddrs, addrs, amts, data, type);
        }

        return genTx(smAmt, utxoTotal, prvKeys, sdrAddrs, addrs, amts, data);
    }

    /**
     * @param smAmt
     * @param utxoTotal
     * @param prvKeys
     * @param sdrAddrs
     * @param addrs
     * @param amts
     * @param data
     * @return
     * @throws Exception
     */
    private String genCrossTx(double smAmt, List<List<Map>> utxoTotal, List<String> prvKeys, List<String> sdrAddrs, List<String> addrs,
                              List<Double> amts, String data, ChainType type) throws Exception {

        if (addrs == null || addrs.size() == 0) {
            throw new RuntimeException("output can not be blank");
        }

        Map<String, Object> paraListMap = new HashMap<>();
        List txList = new ArrayList<>();
        paraListMap.put("Transactions", txList);
        Map<String, Object> txListMap = new HashMap<>();
        txList.add(txListMap);

        int index = -1;
        double spendMoney = 0.0;
        boolean hasEnoughFee = false;
        int utxoIndex = -1;
        out:
        for (int z = 0; z < utxoTotal.size(); z++) {
            List<Map> utxolm = utxoTotal.get(z);
            utxoIndex = z;
            for (int i = 0; i < utxolm.size(); i++) {
                index = i;
                spendMoney += Double.valueOf(utxolm.get(i).get("Value") + "");
                if (Math.round(spendMoney * BasicConstant.ONE_ELA) >= Math.round((smAmt + (BasicConstant.CROSS_CHAIN_FEE * 2)) * BasicConstant.ONE_ELA)) {
                    hasEnoughFee = true;
                    break out;
                }
            }
        }


        if (!hasEnoughFee) {
            throw new ApiRequestDataException(Errors.NOT_ENOUGH_UTXO.val());
        }

        List utxoInputsArray = new ArrayList<>();
        txListMap.put("UTXOInputs", utxoInputsArray);
        List privsArray = new ArrayList<>();
        for (int z = 0; z <= utxoIndex; z++) {
            List<Map> utxolm = utxoTotal.get(z);
            String privateKey = prvKeys.get(z);
            String addr = sdrAddrs.get(z);
            int subIndex = utxolm.size() - 1;
            if (z == utxoIndex) {
                subIndex = index;
            }
            for (int i = 0; i <= subIndex; i++) {
                Map<String, Object> utxoInputsDetail = new HashMap<>();
                Map<String, Object> utxoM = utxolm.get(i);
                Map<String, Object> privM = new HashMap<>();
                double utxoVal = Double.valueOf(utxoM.get("Value") + "");
                if (utxoVal == 0) {
                    continue;
                }
                utxoInputsDetail.put("txid", utxoM.get("Txid"));
                utxoInputsDetail.put("index", utxoM.get("Index"));
                utxoInputsDetail.put("address", addr);
                privM.put("privateKey", privateKey);
                utxoInputsArray.add(utxoInputsDetail);
                privsArray.add(privM);
            }
        }
        List utxoOutputsArray = new ArrayList<>();
        txListMap.put("Outputs", utxoOutputsArray);
        Map<String, Object> brokerOutputs = new HashMap<>();
        if (type == ChainType.MAIN_DID_CROSS_CHAIN) {
            brokerOutputs.put("address", DidConstant.MAIN_CHAIN_ADDRESS);
        } else if (type == ChainType.DID_MAIN_CROSS_CHAIN) {
            brokerOutputs.put("address", DidConstant.BURN_ADDRESS);
        } else {
            throw new ApiRequestDataException("no such transfer type");
        }
        brokerOutputs.put("amount", Math.round((smAmt + BasicConstant.CROSS_CHAIN_FEE) * BasicConstant.ONE_ELA));
        utxoOutputsArray.add(brokerOutputs);

        double leftMoney = (spendMoney - ((BasicConstant.CROSS_CHAIN_FEE * 2) + smAmt));
        String changeAddr = sdrAddrs.get(0);
        Map<String, Object> utxoOutputsDetail = new HashMap<>();
        utxoOutputsDetail.put("address", changeAddr);
        utxoOutputsDetail.put("amount", Math.round(leftMoney * BasicConstant.ONE_ELA));
        utxoOutputsArray.add(utxoOutputsDetail);

        txListMap.put("PrivateKeySign", privsArray);
        List crossOutputsArray = new ArrayList<>();
        txListMap.put("CrossChainAsset", crossOutputsArray);
        for (int i = 0; i < addrs.size(); i++) {
            utxoOutputsDetail = new HashMap<>();
            utxoOutputsDetail.put("address", addrs.get(i));
            utxoOutputsDetail.put("amount", Math.round(amts.get(i) * BasicConstant.ONE_ELA));
            crossOutputsArray.add(utxoOutputsDetail);
        }

        String rawTx = SingleSignTransaction.genCrossChainRawTransaction(new com.alibaba.fastjson.JSONObject(paraListMap));
        return rawTx;
    }

    /**
     * generate raw transaction.
     *
     * @param smAmt     总金额
     * @param utxoTotal
     * @param prvKeys   发送方私钥
     * @param sdrAddrs  发送方地址
     * @param addrs     接收方地址
     * @param amts      接收方金额
     * @param data      备注
     * @return
     * @throws Exception
     */

    private String genTx(double smAmt, List<List<Map>> utxoTotal, List<String> prvKeys, List<String> sdrAddrs, List<String> addrs, List<Double> amts, String data) throws Exception {

        if (addrs == null || addrs.size() == 0) {
            throw new RuntimeException("output can not be blank");
        }

        Map<String, Object> paraListMap = new HashMap<>();
        List txList = new ArrayList<>();
        paraListMap.put("Transactions", txList);
        Map<String, Object> txListMap = new HashMap<>();
        txList.add(txListMap);
        boolean isPayload = false;
        Map val = null;
        if (!StrKit.isBlank(data)) {
            try {
                val = (Map) JSON.parse(data);
                if (val.containsKey("Id") && val.containsKey("Contents")) {
                    txListMap.put("Payload", JSONObject.parseObject(data));
                    isPayload = true;
                } else {
                    txListMap.put("Memo", data);
                }
            } catch (Exception ex) {
                txListMap.put("Memo", data);
            }
        }

        int index = -1;
        double spendMoney = 0.0;
        boolean hasEnoughFee = false;
        int utxoIndex = -1;
        out:
        for (int z = 0; z < utxoTotal.size(); z++) {
            List<Map> utxolm = utxoTotal.get(z);
            utxoIndex = z;
            for (int i = 0; i < utxolm.size(); i++) {
                index = i;
                spendMoney += Double.valueOf(utxolm.get(i).get("Value") + "");
                if (Math.round(spendMoney * BasicConstant.ONE_ELA) >= Math.round((smAmt + BasicConstant.FEE) * BasicConstant.ONE_ELA)) {
                    hasEnoughFee = true;
                    break out;
                }
            }
        }


        if (!hasEnoughFee) {
            throw new ApiRequestDataException(Errors.NOT_ENOUGH_UTXO.val());
        }

        List utxoInputsArray = new ArrayList<>();
        txListMap.put("UTXOInputs", utxoInputsArray);
        for (int z = 0; z <= utxoIndex; z++) {
            List<Map> utxolm = utxoTotal.get(z);
            String privateKey = prvKeys.get(z);
            String addr = sdrAddrs.get(z);
            int subIndex = utxolm.size() - 1;
            if (z == utxoIndex) {
                subIndex = index;
            }
            for (int i = 0; i <= subIndex; i++) {
                Map<String, Object> utxoInputsDetail = new HashMap<>();
                Map<String, Object> utxoM = utxolm.get(i);
                double utxoVal = Double.valueOf(utxoM.get("Value") + "");
                if (utxoVal == 0) {
                    continue;
                }
                utxoInputsDetail.put("txid", utxoM.get("Txid"));
                utxoInputsDetail.put("index", utxoM.get("Index"));
                utxoInputsDetail.put("privateKey", privateKey);
                utxoInputsDetail.put("address", addr);
                utxoInputsArray.add(utxoInputsDetail);
            }
        }
        List utxoOutputsArray = new ArrayList<>();
        txListMap.put("Outputs", utxoOutputsArray);
        for (int i = 0; i < addrs.size(); i++) {
            Map<String, Object> utxoOutputsDetail = new HashMap<>();
            utxoOutputsDetail.put("address", addrs.get(i));
            utxoOutputsDetail.put("amount", Math.round(amts.get(i) * BasicConstant.ONE_ELA));
            utxoOutputsArray.add(utxoOutputsDetail);
        }
        if (isPayload) {
            Map<String, Object> utxoOutputsDetail = new HashMap<>();
            utxoOutputsDetail.put("address", ((Map) (val)).get("Id"));
            utxoOutputsDetail.put("amount", 0);
            utxoOutputsArray.add(utxoOutputsDetail);
        }
        double leftMoney = (spendMoney - (BasicConstant.FEE + smAmt));
        String changeAddr = sdrAddrs.get(0);
        Map<String, Object> utxoOutputsDetail = new HashMap<>();
        utxoOutputsDetail.put("address", changeAddr);
        utxoOutputsDetail.put("amount", Math.round(leftMoney * BasicConstant.ONE_ELA));
        utxoOutputsArray.add(utxoOutputsDetail);

        String rawTx = ElaKit.genRawTransaction(new com.alibaba.fastjson.JSONObject(paraListMap));
        return rawTx;
    }


    /**
     * @param result
     * @return
     */
    private List<Map> stripUtxo(String result) {

        Map m = com.alibaba.fastjson.JSONObject.parseObject(result);
        List<Map> lm = null;
        try {
            lm = ((List<Map>) m.get("Result"));
        } catch (Exception ex) {
            return null;
        }
        List<Map> l = null;
        if (lm != null) {
            l = (List<Map>) lm.get(0).get("Utxo");
        }
        return l;
    }


    private List<String> getUtxoByAddr(List<String> addrs, ChainType type) {
        List<String> rstlist = new ArrayList<>();
        for (int i = 0; i < addrs.size(); i++) {
            String addr = addrs.get(i);
            // checkAddr(addr);
            String result = HttpKit.get(nodeUrl + NodeConstant.UTXO_BY_ADDR + addr);
            rstlist.add(result);
        }
        return rstlist;
    }

    private List<String> getUtxoByAddr(String addr, ChainType type) {
        List<String> addrLst = new ArrayList<>();
        addrLst.add(addr);
        return getUtxoByAddr(addrLst, type);
    }


    private String sendRawTx(RawTxEntity rawTxEntity) {
        String rawTx = JSON.toJSONString(rawTxEntity);
        ChainType type = rawTxEntity.getType();

        ReturnMsgEntity.ELAReturnMsg msg = JsonUtil.jsonStr2Entity(HttpKit.post(nodeUrl + NodeConstant.SEND_RAW_TRANSACTION, rawTx), ReturnMsgEntity.ELAReturnMsg.class);
        long status = 0;
        String rst;
        if (msg.getError() == null && msg.getDesc().equals("Success")) {
            status = RetCodeConstant.SUCC;
            rst = msg.getResult().toString();
        } else {
            status = RetCodeConstant.PROCESS_ERROR;
            rst = msg.getDesc();
        }
        return JSON.toJSONString(new ReturnMsgEntity().setResult(rst).setStatus(status));
    }


}
