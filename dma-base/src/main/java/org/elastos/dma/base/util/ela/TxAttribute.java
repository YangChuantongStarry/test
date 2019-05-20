//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package org.elastos.dma.base.util.ela;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Random;

public class TxAttribute {
    byte Usage;
    byte[] Data;
    int Size;

    public TxAttribute() {
    }

    public void Serialize(DataOutputStream o) throws IOException {
        o.writeByte(this.Usage);
        Util.WriteVarBytes(o, this.Data);
    }

    public static void DeSerialize(DataInputStream o) throws IOException {
        o.readByte();
        int len = (int)Util.ReadVarUint(o);
        byte[] b = new byte[len];
        o.read(b, 0, len);
    }

    public static TxAttribute NewTxNonceAttribute() {
        Random r = new Random();
        TxAttribute ta = new TxAttribute();
        ta.Usage = 0;
        ta.Data = Long.toString(r.nextLong(), 10).getBytes();
        ta.Size = 0;
        return ta;
    }

    public static TxAttribute NewTxNonceAttribute(String memo) {
        new Random();
        TxAttribute ta = new TxAttribute();
        ta.Usage = -127;
        ta.Data = memo.getBytes();
        ta.Size = 0;
        return ta;
    }
}
