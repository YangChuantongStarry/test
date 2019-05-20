//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package org.elastos.dma.base.util.ela;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;
import io.github.novacrypto.DatatypeConverter;

public class TxOutput {
    private byte[] AssetID;
    private long Value;
    private long OutputLock;
    private byte[] ProgramHash;
    private String Address;
    private final String DESTROY_ADDRESS;

    public String toString() {
        return "TxOutput{AssetID=" + Arrays.toString(this.AssetID) + ", Value=" + this.Value + ", OutputLock=" + this.OutputLock + ", ProgramHash=" + Arrays.toString(this.ProgramHash) + ", Address='" + this.Address + '\'' + ", DESTROY_ADDRESS='" + "0000000000000000000000000000000000" + '\'' + '}';
    }

    public TxOutput(String address, long amount) {
        this.AssetID = Common.ELA_ASSETID;
        this.OutputLock = 0L;
        this.DESTROY_ADDRESS = "0000000000000000000000000000000000";
        this.Address = address;
        this.Value = amount;
        if (address.equals("0000000000000000000000000000000000")) {
            this.ProgramHash = new byte[21];
        } else {
            this.ProgramHash = Util.ToScriptHash(address);
        }

    }

    void Serialize(DataOutputStream o) throws IOException {
        o.write(this.AssetID);
        o.writeLong(Long.reverseBytes(this.Value));
        o.writeInt(Integer.reverseBytes((int)this.OutputLock));
        o.write(this.ProgramHash);
    }

    public static Map DeSerialize(DataInputStream o) throws IOException {
        byte[] buf = new byte[32];
        o.read(buf, 0, 32);
        DatatypeConverter.printHexBinary(Utils.reverseBytes(buf));
        long value = o.readLong();
        long v = Long.reverseBytes(value);
        long outputLock = (long)o.readInt();
        Long.reverseBytes(outputLock);
        byte[] program = new byte[21];
        o.read(program, 0, 21);
        String address = Util.ToAddress(program);
        Map<String, Object> outputMap = new LinkedHashMap();
        outputMap.put("Address:", address);
        outputMap.put("Value:", v);
        return outputMap;
    }

    public byte[] getAssetID() {
        return this.AssetID;
    }

    public long getValue() {
        return this.Value;
    }

    public long getOutputLock() {
        return this.OutputLock;
    }

    public byte[] getProgramHash() {
        return this.ProgramHash;
    }

    public String getAddress() {
        return this.Address;
    }
}
