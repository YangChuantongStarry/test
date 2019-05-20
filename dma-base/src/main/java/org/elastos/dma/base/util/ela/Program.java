//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package org.elastos.dma.base.util.ela;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Arrays;

public class Program {
    byte[] Code;
    byte[] Parameter;

    public Program(byte[] Code, byte[] Paramter) throws IOException {
        this.Code = Code;
        ProgramBuilder pb = ProgramBuilder.NewProgramBuilder();
        pb.PushData(Paramter);
        this.Parameter = pb.ToArray();
    }

    public String toString() {
        return "Program{Code=" + Arrays.toString(this.Code) + ", Parameter=" + Arrays.toString(this.Parameter) + '}';
    }

    void Serialize(DataOutputStream o) throws IOException {
        Util.WriteVarBytes(o, this.Parameter);
        Util.WriteVarBytes(o, this.Code);
    }
}
