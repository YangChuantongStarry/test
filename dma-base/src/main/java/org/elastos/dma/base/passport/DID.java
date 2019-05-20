package org.elastos.dma.base.passport;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import io.github.novacrypto.DatatypeConverter;

import org.elastos.dma.base.conf.DidConstant;
import org.elastos.dma.base.conf.NodeConstant;
import org.elastos.dma.base.conf.RetCodeConstant;
import org.elastos.dma.base.entity.did.DIDAccount;
import org.elastos.dma.base.util.ElaRpcImpl;
import org.elastos.dma.base.util.HdSupport;
import org.elastos.dma.base.util.ela.*;
import org.elastos.dma.utility.entity.ela.ChainType;
import org.elastos.dma.utility.entity.ela.Errors;
import org.elastos.dma.utility.entity.ela.ReturnMsgEntity;
import org.elastos.dma.utility.entity.ela.SignDataEntity;
import org.elastos.dma.utility.entity.ela.TransferParamEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.Serializable;
import java.util.*;

@SuppressWarnings("unchecked")
public class DID extends ElaRpcImpl {


    private static Logger logger = LoggerFactory.getLogger(DID.class);


    public DID(String nodeUrl) {
        super(nodeUrl);
    }


    /**
     * 创建DID
     *
     * @return DIDAccount
     */
    public DIDAccount create(HdSupport.MnemonicType mnemonicType) {
        return DIDAccount.init(mnemonicType);
    }


    /**
     * 根据私钥获取DID
     *
     * @param mnemonic 助记词
     * @return DIDAccount
     */
    public DIDAccount exportByMnemonic(String mnemonic) {
        DIDAccount didAccount = DIDAccount.getDIDAccountByMnemonic(mnemonic);
        return didAccount;
    }


    /**
     * 根据私钥获取DID
     *
     * @param privateKey
     * @return DIDAccount
     */
    public DIDAccount exportByPrivateKey(String privateKey) {
        DIDAccount didAccount = DIDAccount.getDIDAccountByPrivateKey(privateKey);
        return didAccount;

    }


    /**
     * 设置DID信息
     *
     * @param privateKey 私钥
     * @param info        信息
     * @return hash
     */

    public <T extends Serializable> String setDIDInfo(String privateKey, T info) throws Exception {
        String data = JSONObject.toJSONString(info);
        String receveAddr = DidConstant.ADDRESS;
        String fee = DidConstant.FEE;
        TransferParamEntity transferParamEntity = new TransferParamEntity();
        SignDataEntity signDataEntity = new SignDataEntity();
        signDataEntity.setPrivateKey(privateKey);
        signDataEntity.setMsg(data);
        ReturnMsgEntity<SignDataEntity> respMap = sign(signDataEntity);
        Object result = respMap.getResult();
        String rawMemo = JSON.toJSONString(result);
        transferParamEntity.setMemo(rawMemo);
        String addr = Ela.getAddressFromPrivate(privateKey);
        List<Map> lstMap = new ArrayList<>();
        Map sm = new HashMap();
        sm.put("address", addr);
        sm.put("privateKey", privateKey);
        lstMap.add(sm);
        transferParamEntity.setSender(lstMap);
        List<Map> receiverList = new ArrayList<>();
        Map receiveMap = new HashMap();
        receiveMap.put("address", receveAddr);
        receiveMap.put("amount", fee);
        receiverList.add(receiveMap);
        transferParamEntity.setReceiver(receiverList);
        transferParamEntity.setType(ChainType.DID_SIDECHAIN);
        return transfer(transferParamEntity);
    }

    /**
     * 获取did信息
     *
     * @param txidList txid数组
     * @param key
     */

    public JSONObject getDIDInfo(List<String> txidList, String key) throws Exception {
        JSONObject resultM = new JSONObject();
        if (txidList.size() == 0) {
            resultM.put("status", RetCodeConstant.SUCC);
            resultM.put("result", Errors.DID_NO_SUCH_INFO.val());
            return resultM;
        }

        if (txidList.size() > 1) {
            txidList = filterTxByHeight(txidList);
        }

        for (int i = 0; i < txidList.size(); i++) {
            try {
                String txid = txidList.get(i);
                String txinfo = getTransactionByHash(txid);
                JSONObject txinfoMap = JSONObject.parseObject(txinfo);
                JSONObject resultMap = txinfoMap.getJSONObject("result");
                JSONArray attrList = resultMap.getJSONArray("attributes");
                String hexData = attrList.getJSONObject(0).getString("data");
                String text = new String(DatatypeConverter.parseHexBinary(hexData));
                JSONObject rawMap = JSON.parseObject(text);
                SignDataEntity signDataEntity = new SignDataEntity();
                String hexMsg = rawMap.getString("msg");
                String pub = rawMap.getString("pub");
                signDataEntity.setMsg(hexMsg);
                signDataEntity.setSig((String) rawMap.get("sig"));
                signDataEntity.setPub(pub);
                ReturnMsgEntity<Boolean> verify = verify(signDataEntity);
                Boolean verified = verify.getResult();
                if (!verified) {
                    continue;
                } else {
                    Map rawMsgMap = (Map) JSON.parse(new String(javax.xml.bind.DatatypeConverter.parseHexBinary(hexMsg)));
                    Object v = rawMsgMap.get(key);
                    if (v == null) {
                        continue;
                    }
                    resultM.put("data", v);
                    String did = Util.ToAddress(Util.ToCodeHash(Util.CreateSingleSignatureRedeemScript(DatatypeConverter.parseHexBinary(pub), 3), 3));
                    resultM.put("did", did);
                    return resultM;
                }
            } catch (Exception ex) {
                logger.warn(ex.getMessage());
            }

        }
        return resultM;
    }


    public String mainToDidCrossTransfer(String privateKey, String _to, String _value) throws Exception {
        TransferParamEntity transferParamEntity=  getTransferEntity(privateKey,_to,_value,ChainType.MAIN_DID_CROSS_CHAIN);
        return transfer(transferParamEntity);
    }

    public String didToMainCrossTransfer(String privateKey, String _to, String _value) throws Exception {
        TransferParamEntity transferParamEntity=  getTransferEntity(privateKey,_to,_value,ChainType.DID_MAIN_CROSS_CHAIN);
        return transfer(transferParamEntity);
    }


    private TransferParamEntity getTransferEntity(String privateKey, String _to, String _value, ChainType chainType){
        //发送方地址  私钥
        List<Map<String, String>> sender = new ArrayList<>();
        Map<String, String> senderMap = new HashMap<>();
        senderMap.put("address", Ela.getAddressFromPrivate(privateKey));
        senderMap.put("privateKey", privateKey);
        sender.add(senderMap);
        //接收方 地址 金额
        List<Map<String, String>> receiver = new ArrayList<>();
        Map<String, String> receiverMap = new HashMap<>();
        receiverMap.put("address", _to);
        receiverMap.put("amount", _value);
        receiver.add(receiverMap);

        TransferParamEntity transferParamEntity = new TransferParamEntity();

        transferParamEntity.setSender(sender);
        transferParamEntity.setReceiver(receiver);
        transferParamEntity.setType(chainType);
        return transferParamEntity;
    }


    private String getTransactionByHash(String hash) {
        String requestUrl = nodeUrl + NodeConstant.TRANSACTION + hash;
        return reqChainData(requestUrl);

    }


    private ReturnMsgEntity<SignDataEntity> sign(SignDataEntity entity) throws Exception {
        SignDataEntity signDataEntity = signInfo(entity);
        return new ReturnMsgEntity<>().setResult(signDataEntity).setStatus(RetCodeConstant.SUCC);
    }


    public SignDataEntity signInfo(SignDataEntity entity) throws Exception {
        return this.signInfo(entity.getPrivateKey(), entity.getMsg());
    }

    public SignDataEntity signInfo(String privateKey, String msg) throws Exception {
        byte[] privKeyBytes = DatatypeConverter.parseHexBinary(privateKey);
        ECKey ec = ECKey.fromPrivate(privKeyBytes);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(baos);
        dos.write(msg.getBytes(CHARSET));
        byte[] signature = SignTool.doSign(baos.toByteArray(), privKeyBytes);
        byte[] code = new byte[33];
        System.arraycopy(Util.CreateSingleSignatureRedeemScript(ec.getPubBytes(), 1), 1, code, 0, code.length);

        SignDataEntity signDataEntity = new SignDataEntity();
        signDataEntity.setMsg(DatatypeConverter.printHexBinary(msg.getBytes(CHARSET)));
        signDataEntity.setPub(DatatypeConverter.printHexBinary(code));
        signDataEntity.setSig(DatatypeConverter.printHexBinary(signature));
        return signDataEntity;
    }

    public ReturnMsgEntity<Boolean> verify(SignDataEntity entity) {

        ReturnMsgEntity returnMsgEntity1 = new ReturnMsgEntity<Boolean>();
        returnMsgEntity1.setResult(verifyInfo(entity));
        returnMsgEntity1.setStatus(RetCodeConstant.SUCC);
        return returnMsgEntity1;
    }

    public Boolean verifyInfo(SignDataEntity entity) {
        return this.verifyInfo(entity.getPub(), entity.getMsg(), entity.getSig());
    }


    public Boolean verifyInfo(String hexPub, String hexMsg, String hexSig) {
        try {
            byte[] msg = DatatypeConverter.parseHexBinary(hexMsg);
            byte[] sig = DatatypeConverter.parseHexBinary(hexSig);
            byte[] pub = DatatypeConverter.parseHexBinary(hexPub);
            boolean isVerify = ElaSignTool.verify(msg, sig, pub);
            logger.debug("isVerify : " + isVerify);
            return isVerify;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    private String reqChainData(String requestUrl) {

        String result = HttpKit.get(requestUrl);

        Map<String, Object> resultMap = JSON.parseObject(result);

        ReturnMsgEntity result1 = new ReturnMsgEntity().setResult(resultMap.get("Result")).setStatus(RetCodeConstant.SUCC);
        return JSON.toJSONString(result1);

    }


    private List<String> filterTxByHeight(List<String> txids) {

        StringBuilder txStr = new StringBuilder();
        String seperator = "-";
        for (int j = 0; j < txids.size(); j++) {
            String currTxid = txids.get(j);
            Integer currBlockTime = getBlockTime(currTxid);
            if (currBlockTime == null) {
                continue;
            }
            for (int i = j + 1; i < txids.size(); i++) {
                String txid = txids.get(i);
                Integer blocktime = getBlockTime(txid);
                if (blocktime == null) {
                    continue;
                }
                if (blocktime > currBlockTime) {
                    currTxid = txid;
                    currBlockTime = blocktime;
                }
            }
            txStr.append(currTxid).append(seperator);
        }

        txStr = new StringBuilder(txStr.substring(0, txStr.length() - 1));
        return Arrays.asList(txStr.toString().split(seperator));
    }

    private Integer getBlockTime(String txid) {
        String obj = getTransactionByHash(txid);
        JSONObject jsonResult = JSONObject.parseObject(obj);
        JSONObject result = jsonResult.getJSONObject("result");
        Integer blocktime = result.getInteger("blocktime");
        logger.debug("blockTime : " + blocktime);
        return blocktime;
    }


}
