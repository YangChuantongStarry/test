//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package org.elastos.dma.base.util.ela;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.List;
import io.github.novacrypto.DatatypeConverter;

public class SignTxAbnormal {
    public SignTxAbnormal() {
    }

    public static RawTx makeSingleSignTx(UTXOTxInput[] inputs, TxOutput[] outputs, List<String> privateKeySign) throws IOException {
        Tx tx = Tx.NewTransferAssetTransaction(inputs, outputs);
        return SingleSignTx(tx, privateKeySign);
    }

    public static RawTx makeSingleSignTx(UTXOTxInput[] inputs, TxOutput[] outputs, List<String> privateKeySign, PayloadRecord payloadRecord) throws IOException {
        Tx tx = Tx.NewTransferAssetTransaction(inputs, outputs, payloadRecord);
        return SingleSignTx(tx, privateKeySign);
    }

    public static RawTx makeSingleSignTx(UTXOTxInput[] inputs, TxOutput[] outputs, List<String> privateKeySign, String memo) throws IOException {
        Tx tx = Tx.NewTransferAssetTransaction(inputs, outputs, memo);
        return SingleSignTx(tx, privateKeySign);
    }

    public static RawTx SingleSignTx(Tx tx, List<String> privateKeySign) throws IOException {
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
}
