//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package org.elastos.dma.base.util.ela;

import java.io.DataOutputStream;
import java.io.IOException;

public class PayloadTransferCrossChainAsset {
    String CrossChainAddress;
    int OutputIndex;
    long CrossChainAmount;

    public PayloadTransferCrossChainAsset(String address, long amount, int index) {
        this.CrossChainAddress = address;
        this.CrossChainAmount = amount;
        this.OutputIndex = index;
    }

    void Serialize(DataOutputStream o) throws IOException {
        o.write(this.CrossChainAddress.length());
        o.writeBytes(this.CrossChainAddress);
        Util.WriteVarUint(o, (long)this.OutputIndex);
        o.writeLong(Long.reverseBytes(this.CrossChainAmount));
    }
}
