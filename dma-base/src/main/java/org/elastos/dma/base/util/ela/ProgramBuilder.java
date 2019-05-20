//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package org.elastos.dma.base.util.ela;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class ProgramBuilder {
    private static final byte PUSHBYTES75 = 75;
    private static final byte PUSHDATA1 = 76;
    private static final byte PUSHDATA2 = 77;
    private static final byte PUSHDATA4 = 78;
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    DataOutputStream dos;

    public ProgramBuilder() {
        this.dos = new DataOutputStream(this.baos);
    }

    public static ProgramBuilder NewProgramBuilder() {
        return new ProgramBuilder();
    }

    public void AddOp(byte op) throws IOException {
        this.dos.writeByte(op);
    }

    public void PushData(byte[] data) throws IOException {
        if (data != null) {
            if (data.length <= 75) {
                this.dos.writeByte(data.length);
                this.dos.write(data);
            } else if (data.length < 256) {
                this.AddOp((byte)76);
                this.dos.writeByte(data.length);
                this.dos.write(data);
            } else {
                short len;
                if (data.length < 65536) {
                    this.AddOp((byte)77);
                    len = (short)data.length;
                    len = Short.reverseBytes(len);
                    this.dos.writeShort(len);
                    this.dos.write(data);
                } else {
                    this.AddOp((byte)78);
                    len = (short)data.length;
                    int i = Integer.reverseBytes(len);
                    this.dos.writeInt(i);
                    this.dos.write(data);
                }
            }

        }
    }

    public byte[] ToArray() {
        return this.baos.toByteArray();
    }
}
