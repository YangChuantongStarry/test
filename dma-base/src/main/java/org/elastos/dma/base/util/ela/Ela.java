//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package org.elastos.dma.base.util.ela;

import io.github.novacrypto.DatatypeConverter;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class Ela {
    public Ela() {
    }

    public static RawTx makeAndSignTx(UTXOTxInput[] inputs, TxOutput[] outputs) throws IOException {
        Tx tx = Tx.NewTransferAssetTransaction(inputs, outputs);
        return SingleSignTx(tx);
    }

    public static RawTx makeAndSignTx(UTXOTxInput[] inputs, TxOutput[] outputs, PayloadRecord payloadRecord) throws IOException {
        Tx tx = Tx.NewTransferAssetTransaction(inputs, outputs, payloadRecord);
        return SingleSignTx(tx);
    }

    public static RawTx makeAndSignTx(UTXOTxInput[] inputs, TxOutput[] outputs, PayloadRegisterIdentification payloadRegisterIdentification) throws IOException {
        Tx tx = Tx.NewTransferAssetTransaction(inputs, outputs, payloadRegisterIdentification);
        return SingleSignTx(tx, payloadRegisterIdentification);
    }

    public static RawTx makeAndSignTx(UTXOTxInput[] inputs, TxOutput[] outputs, String memo) throws IOException {
        Tx tx = Tx.NewTransferAssetTransaction(inputs, outputs, memo);
        return SingleSignTx(tx);
    }

    public static RawTx SingleSignTx(Tx tx) throws IOException {
        byte[][] phashes = tx.getUniqAndOrdedProgramHashes();

        for(int i = 0; i < phashes.length; ++i) {
            String privateKey = (String)tx.hashMapPriv.get(DatatypeConverter.printHexBinary(phashes[i]));
            ECKey ec = ECKey.fromPrivate(DatatypeConverter.parseHexBinary(privateKey));
            byte[] code = Util.CreateSingleSignatureRedeemScript(ec.getPubBytes(), 1);
            tx.sign(privateKey, code);
        }

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(baos);
        tx.Serialize(dos);
        System.out.println("tx info is " + tx);
        String rawTxString = DatatypeConverter.printHexBinary(baos.toByteArray());
        String txHash = DatatypeConverter.printHexBinary(tx.getHash());
        return new RawTx(txHash, rawTxString);
    }

    public static RawTx SingleSignTx(Tx tx, PayloadRegisterIdentification payloadRegisterIdentification) throws IOException {
        byte[][] phashes = tx.getUniqAndOrdedProgramHashes();

        for(int i = 0; i < phashes.length; ++i) {
            String privateKey = (String)tx.hashMapPriv.get(DatatypeConverter.printHexBinary(phashes[i]));
            ECKey ec = ECKey.fromPrivate(DatatypeConverter.parseHexBinary(privateKey));
            byte[] code = Util.CreateSingleSignatureRedeemScript(ec.getPubBytes(), 1);
            tx.sign(privateKey, code);
        }

        String privateKey = payloadRegisterIdentification.getIdPrivKey();
        ECKey ec = ECKey.fromPrivate(DatatypeConverter.parseHexBinary(privateKey));
        byte[] code = Util.CreateSingleSignatureRedeemScript(ec.getPubBytes(), 3);
        tx.signPayload(privateKey, code);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(baos);
        tx.Serialize(dos);
        String rawTxString = DatatypeConverter.printHexBinary(baos.toByteArray());
        String txHash = DatatypeConverter.printHexBinary(tx.getHash());
        return new RawTx(txHash, rawTxString);
    }

    public static RawTx MultiSignTransaction(UTXOTxInput[] inputs, TxOutput[] outputs, List<String> privateKeyScript, List<String> privateKeySign, int M) throws Exception {
        Tx tx = Tx.NewTransferAssetTransaction(inputs, outputs);
        return MultiSignTx(tx, privateKeyScript, privateKeySign, M);
    }

    public static RawTx MultiSignTransaction(UTXOTxInput[] inputs, TxOutput[] outputs, List<String> privateKeyScript, List<String> privateKeySign, int M, String memo) throws Exception {
        Tx tx = Tx.NewTransferAssetTransaction(inputs, outputs, memo);
        return MultiSignTx(tx, privateKeyScript, privateKeySign, M);
    }

    public static RawTx MultiSignTransaction(UTXOTxInput[] inputs, TxOutput[] outputs, List<String> privateKeyScript, List<String> privateKeySign, int M, PayloadRecord payloadRecord) throws Exception {
        Tx tx = Tx.NewTransferAssetTransaction(inputs, outputs, payloadRecord);
        return MultiSignTx(tx, privateKeyScript, privateKeySign, M);
    }

    public static RawTx MultiSignTx(Tx tx, List<String> privateKeyScript, List<String> privateKeySign, int M) throws Exception {
        List<PublicX> privateKeyList = new ArrayList();

        for(int j = 0; j < privateKeyScript.size(); ++j) {
            privateKeyList.add(new PublicX((String)privateKeyScript.get(j)));
        }

        byte[] code = ECKey.getMultiSignatureProgram(privateKeyList, M);

        for(int i = 0; i < privateKeySign.size(); ++i) {
            tx.multiSign((String)privateKeySign.get(i), code);
        }

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(baos);
        tx.Serialize(dos);
        String rawTxString = DatatypeConverter.printHexBinary(baos.toByteArray());
        String txHash = DatatypeConverter.printHexBinary(tx.getHash());
        return new RawTx(txHash, rawTxString);
    }

    public static RawTx CrossChainSignTx(UTXOTxInput[] inputs, TxOutput[] outputs, PayloadTransferCrossChainAsset[] CrossChainAsset, List<String> privateKeySign) throws IOException {
        Tx tx = Tx.NewCrossChainTransaction(inputs, outputs, CrossChainAsset);

        for(int i = 0; i < privateKeySign.size(); ++i) {
            ECKey ec = ECKey.fromPrivate(DatatypeConverter.parseHexBinary((String)privateKeySign.get(i)));
            byte[] code = Util.CreateSingleSignatureRedeemScript(ec.getPubBytes(), 1);
            tx.sign((String)privateKeySign.get(i), code);
        }

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(baos);
        tx.Serialize(dos);
        String rawTxString = DatatypeConverter.printHexBinary(baos.toByteArray());
        String txHash = DatatypeConverter.printHexBinary(tx.getHash());
        return new RawTx(txHash, rawTxString);
    }

    public static RawTx CrossChainMultiSignTx(UTXOTxInput[] inputs, TxOutput[] outputs, PayloadTransferCrossChainAsset[] CrossChainAsset, List<String> privateKeyScript, List<String> privateKeySign, int M) throws Exception {
        Tx tx = Tx.NewCrossChainTransaction(inputs, outputs, CrossChainAsset);
        return MultiSignTx(tx, privateKeyScript, privateKeySign, M);
    }

    public static String getPrivateKey() {
        ECKey ec = new ECKey();
        return DatatypeConverter.printHexBinary(ec.getPrivateKeyBytes());
    }

    public static String getPublicFromPrivate(String privateKey) {
        ECKey ec = ECKey.fromPrivate(DatatypeConverter.parseHexBinary(privateKey));
        return DatatypeConverter.printHexBinary(ec.getPubBytes());
    }

    public static String getAddressFromPrivate(String privateKey) {
        ECKey ec = ECKey.fromPrivate(DatatypeConverter.parseHexBinary(privateKey));
        return ec.toAddress();
    }

    public static String getIdentityIDFromPrivate(String privateKey) {
        ECKey ec = ECKey.fromPrivate(DatatypeConverter.parseHexBinary(privateKey));
        return ec.toIdentityID();
    }

    public static String getMultiSignAddress(List<String> privateKey, int M) throws SDKException {
        List<PublicX> privateKeyList = new ArrayList();

        for(int i = 0; i < privateKey.size(); ++i) {
            privateKeyList.add(new PublicX((String)privateKey.get(i)));
        }

        ECKey ec = new ECKey();
        return ec.toMultiSignAddress(privateKeyList, M);
    }
}
