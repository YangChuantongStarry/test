//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package org.elastos.dma.base.util.ela;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import io.github.novacrypto.DatatypeConverter;

public class UTXOTxInput {
    private byte[] ReferTxID;
    private int ReferTxOutputIndex;
    private int Sequence = 0;
    private String privateKey;
    private String programHash;

    public byte[] getReferTxID() {
        return this.ReferTxID;
    }

    public int getReferTxOutputIndex() {
        return this.ReferTxOutputIndex;
    }

    public int getSequence() {
        return this.Sequence;
    }

    public String toString() {
        return "UTXOTxInput{ReferTxID=" + Arrays.toString(this.ReferTxID) + ", ReferTxOutputIndex=" + this.ReferTxOutputIndex + ", Sequence=" + this.Sequence + ", privateKey='" + this.privateKey + '\'' + ", programHash='" + this.programHash + '\'' + '}';
    }

    public String getPrivateKey() {
        return this.privateKey;
    }

    public String getProgramHash() {
        return this.programHash;
    }

    public UTXOTxInput(String txid, int index, String privateKey, String address) {
        this.ReferTxID = Utils.reverseBytes(DatatypeConverter.parseHexBinary(txid));
        this.privateKey = privateKey;
        this.ReferTxOutputIndex = index;
        this.programHash = DatatypeConverter.printHexBinary(Util.ToScriptHash(address));
    }

    public void Serialize(DataOutputStream o) throws IOException {
        o.write(this.ReferTxID);
        o.writeShort(Short.reverseBytes((short)this.ReferTxOutputIndex));
        o.writeInt(Integer.reverseBytes(this.Sequence));
    }

    public static Map DeSerialize(DataInputStream o) throws IOException {
        byte[] buf = new byte[32];
        o.read(buf, 0, 32);
        String Txid = DatatypeConverter.printHexBinary(Utils.reverseBytes(buf));
        Map<String, Object> inputMap = new HashMap();
        inputMap.put("Txid:", Txid);
        Short.reverseBytes(o.readShort());
        Integer.reverseBytes(o.readInt());
        return inputMap;
    }
}
