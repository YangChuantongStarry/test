//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package org.elastos.dma.base.util.ela;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import io.github.novacrypto.DatatypeConverter;

public class Tx {
    byte TxType;
    byte PayloadVersion;
    PayloadRecord parloadRecord;
    PayloadTransferCrossChainAsset[] CrossChainAsset;
    PayloadRegisterIdentification PayloadRegisterIdentification;
    TxAttribute[] Attributes;
    UTXOTxInput[] UTXOInputs;
    TxOutput[] Outputs;
    long LockTime;
    List<Program> Programs;
    long Fee;
    long FeePerKB;
    byte[] hash;
    Map<String, String> hashMapPriv = new HashMap();

    public Tx() {
    }

    public String toString() {
        return "Tx{TxType=" + this.TxType + ", PayloadVersion=" + this.PayloadVersion + ", parloadRecord=" + this.parloadRecord + ", CrossChainAsset=" + Arrays.toString(this.CrossChainAsset) + ", PayloadRegisterIdentification=" + this.PayloadRegisterIdentification + ", Attributes=" + Arrays.toString(this.Attributes) + ", UTXOInputs=" + Arrays.toString(this.UTXOInputs) + ", Outputs=" + Arrays.toString(this.Outputs) + ", LockTime=" + this.LockTime + ", Programs=" + this.Programs + ", Fee=" + this.Fee + ", FeePerKB=" + this.FeePerKB + ", hash=" + Arrays.toString(this.hash) + ", hashMapPriv=" + this.hashMapPriv + '}';
    }

    public void sign(String privateKey, byte[] code) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(baos);
        this.SerializeUnsigned(dos);
        byte[] signature = SignTool.doSign(baos.toByteArray(), DatatypeConverter.parseHexBinary(privateKey));
        this.Programs.add(new Program(code, signature));
    }

    public void signPayload(String privateKey, byte[] code) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(baos);
        this.PayloadRegisterIdentification.Serialize(dos);
        byte[] signature = SignTool.doSign(baos.toByteArray(), DatatypeConverter.parseHexBinary(privateKey));
        this.Programs.add(new Program(code, signature));
    }

    public void multiSign(String privateKey, byte[] code) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(baos);
        this.SerializeUnsigned(dos);
        byte[] signature = SignTool.doSign(baos.toByteArray(), DatatypeConverter.parseHexBinary(privateKey));
        Program program = new Program(code, signature);
        if (this.Programs.size() == 0) {
            this.Programs.add(program);
        } else {
            byte[] byte_buf = new byte[((Program)this.Programs.get(0)).Parameter.length + program.Parameter.length];
            System.arraycopy(((Program)this.Programs.get(0)).Parameter, 0, byte_buf, 0, ((Program)this.Programs.get(0)).Parameter.length);
            System.arraycopy(program.Parameter, 0, byte_buf, ((Program)this.Programs.get(0)).Parameter.length, program.Parameter.length);
            ((Program)this.Programs.get(0)).Parameter = byte_buf;
        }

    }

    public static Tx NewTransferAssetTransaction(UTXOTxInput[] inputs, TxOutput[] outputs) {
        Tx tx = new Tx();
        tx.UTXOInputs = inputs;
        tx.Outputs = outputs;
        tx.TxType = 2;
        tx.Attributes = new TxAttribute[1];
        tx.Programs = new ArrayList();
        TxAttribute ta = TxAttribute.NewTxNonceAttribute();
        tx.Attributes[0] = ta;
        UTXOTxInput[] var4 = tx.UTXOInputs;
        int var5 = var4.length;

        for(int var6 = 0; var6 < var5; ++var6) {
            UTXOTxInput txin = var4[var6];
            tx.hashMapPriv.put(txin.getProgramHash(), txin.getPrivateKey());
        }

        return tx;
    }

    public static Tx NewTransferAssetTransaction(UTXOTxInput[] inputs, TxOutput[] outputs, String memo) {
        Tx tx = new Tx();
        tx.UTXOInputs = inputs;
        tx.Outputs = outputs;
        tx.TxType = 2;
        tx.Attributes = new TxAttribute[1];
        tx.Programs = new ArrayList();
        TxAttribute ta = TxAttribute.NewTxNonceAttribute(memo);
        tx.Attributes[0] = ta;
        UTXOTxInput[] var5 = tx.UTXOInputs;
        int var6 = var5.length;

        for(int var7 = 0; var7 < var6; ++var7) {
            UTXOTxInput txin = var5[var7];
            tx.hashMapPriv.put(txin.getProgramHash(), txin.getPrivateKey());
        }

        return tx;
    }

    public static Tx NewTransferAssetTransaction(UTXOTxInput[] inputs, TxOutput[] outputs, PayloadRecord parloadRecord) {
        Tx tx = new Tx();
        tx.UTXOInputs = inputs;
        tx.Outputs = outputs;
        tx.TxType = 3;
        tx.Attributes = new TxAttribute[1];
        tx.parloadRecord = parloadRecord;
        tx.Programs = new ArrayList();
        TxAttribute ta = TxAttribute.NewTxNonceAttribute();
        tx.Attributes[0] = ta;
        UTXOTxInput[] var5 = tx.UTXOInputs;
        int var6 = var5.length;

        for(int var7 = 0; var7 < var6; ++var7) {
            UTXOTxInput txin = var5[var7];
            tx.hashMapPriv.put(txin.getProgramHash(), txin.getPrivateKey());
        }

        return tx;
    }

    public static Tx NewTransferAssetTransaction(UTXOTxInput[] inputs, TxOutput[] outputs, PayloadRegisterIdentification payloadRegisterIdentification) {
        Tx tx = new Tx();
        tx.UTXOInputs = inputs;
        tx.Outputs = outputs;
        tx.TxType = 9;
        tx.Attributes = new TxAttribute[1];
        tx.PayloadRegisterIdentification = payloadRegisterIdentification;
        tx.Programs = new ArrayList();
        TxAttribute ta = TxAttribute.NewTxNonceAttribute();
        tx.Attributes[0] = ta;
        UTXOTxInput[] var5 = tx.UTXOInputs;
        int var6 = var5.length;

        for(int var7 = 0; var7 < var6; ++var7) {
            UTXOTxInput txin = var5[var7];
            tx.hashMapPriv.put(txin.getProgramHash(), txin.getPrivateKey());
        }

        return tx;
    }

    public static Tx NewCrossChainTransaction(UTXOTxInput[] inputs, TxOutput[] outputs, PayloadTransferCrossChainAsset[] CrossChainAsset) {
        Tx tx = new Tx();
        tx.CrossChainAsset = CrossChainAsset;
        tx.UTXOInputs = inputs;
        tx.Outputs = outputs;
        tx.TxType = 8;
        tx.Attributes = new TxAttribute[1];
        tx.Programs = new ArrayList();
        TxAttribute ta = TxAttribute.NewTxNonceAttribute();
        tx.Attributes[0] = ta;
        UTXOTxInput[] var5 = tx.UTXOInputs;
        int var6 = var5.length;

        for(int var7 = 0; var7 < var6; ++var7) {
            UTXOTxInput txin = var5[var7];
            tx.hashMapPriv.put(txin.getProgramHash(), txin.getPrivateKey());
        }

        return tx;
    }

    public void Serialize(DataOutputStream o) throws IOException {
        this.SerializeUnsigned(o);
        Util.WriteVarUint(o, (long)this.Programs.size());
        if (this.Programs.size() > 0) {
            Iterator var2 = this.Programs.iterator();

            while(var2.hasNext()) {
                Program p = (Program)var2.next();
                p.Serialize(o);
            }
        }

    }

    public static Map DeSerialize(DataInputStream o) throws IOException {
        return DeSerializeUnsigned(o);
    }

    public byte[][] getUniqAndOrdedProgramHashes() {
        String[] keys = (String[])((String[])this.hashMapPriv.keySet().toArray(new String[0]));
        byte[][] rhashes = new byte[keys.length][];

        for(int i = 0; i < keys.length; ++i) {
            rhashes[i] = DatatypeConverter.parseHexBinary(keys[i]);
        }

        Util.sortByteArrayArrayUseRevertBytesSequence(rhashes);
        return rhashes;
    }

    public void SetPrograms(List<Program> programs) {
        this.Programs = programs;
    }

    public List<Program> GetPrograms() {
        return this.Programs;
    }

    public void SerializeUnsigned(DataOutputStream o) throws IOException {
        o.writeByte(this.TxType);
        o.writeByte(this.PayloadVersion);
        if (this.parloadRecord != null) {
            this.parloadRecord.Serialize(o);
        }

        if (this.PayloadRegisterIdentification != null) {
            this.PayloadRegisterIdentification.Serialize(o);
        }

        int var3;
        int var4;
        if (this.CrossChainAsset != null) {
            Util.WriteVarUint(o, (long)this.CrossChainAsset.length);
            PayloadTransferCrossChainAsset[] var2 = this.CrossChainAsset;
            var3 = var2.length;

            for(var4 = 0; var4 < var3; ++var4) {
                PayloadTransferCrossChainAsset ca = var2[var4];
                ca.Serialize(o);
            }
        }

        Util.WriteVarUint(o, (long)this.Attributes.length);
        if (this.Attributes.length > 0) {
            TxAttribute[] var6 = this.Attributes;
            var3 = var6.length;

            for(var4 = 0; var4 < var3; ++var4) {
                TxAttribute arr = var6[var4];
                arr.Serialize(o);
            }
        }

        Util.WriteVarUint(o, (long)this.UTXOInputs.length);
        if (this.UTXOInputs.length > 0) {
            UTXOTxInput[] var7 = this.UTXOInputs;
            var3 = var7.length;

            for(var4 = 0; var4 < var3; ++var4) {
                UTXOTxInput utxo = var7[var4];
                utxo.Serialize(o);
            }
        }

        Util.WriteVarUint(o, (long)this.Outputs.length);
        if (this.Outputs.length > 0) {
            TxOutput[] var8 = this.Outputs;
            var3 = var8.length;

            for(var4 = 0; var4 < var3; ++var4) {
                TxOutput output = var8[var4];
                output.Serialize(o);
            }
        }

        o.writeInt(Integer.reverseBytes((int)this.LockTime));
    }

    public static Map DeSerializeUnsigned(DataInputStream o) throws IOException {
        byte TxType = o.readByte();
        byte PayloadVersion = o.readByte();
        long len = 0L;
        len = Util.ReadVarUint(o);
        TxAttribute.DeSerialize(o);
        len = Util.ReadVarUint(o);
        List<Map> inputList = new LinkedList();

        for(int i = 0; (long)i < len; ++i) {
            Map inputMap = UTXOTxInput.DeSerialize(o);
            inputList.add(inputMap);
        }

        List<Map> outputList = new LinkedList();
        len = Util.ReadVarUint(o);

        for(int i = 0; (long)i < len; ++i) {
            Map outputMap = TxOutput.DeSerialize(o);
            outputList.add(outputMap);
        }

        Map<String, List<Map>> resultmap = new LinkedHashMap();
        resultmap.put("UTXOInputs:", inputList);
        resultmap.put("Outputs:", outputList);
        return resultmap;
    }

    public byte[] getHash() throws IOException {
        if (this.hash == null) {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            DataOutputStream dos = new DataOutputStream(baos);
            this.SerializeUnsigned(dos);
            byte[] txUnsigned = baos.toByteArray();
            Sha256Hash sh = Sha256Hash.twiceOf(txUnsigned);
            this.hash = sh.getReversedBytes();
        }

        return this.hash;
    }

    public byte[] GetMessage() {
        return new byte[0];
    }
}
