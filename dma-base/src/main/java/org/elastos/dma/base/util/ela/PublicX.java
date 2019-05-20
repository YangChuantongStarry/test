//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package org.elastos.dma.base.util.ela;

import java.math.BigInteger;
import io.github.novacrypto.DatatypeConverter;

public class PublicX implements Comparable<PublicX> {
    private BigInteger pubX;
    private String privateKey;

    public PublicX(String privateKey) {
        ECKey ec = ECKey.fromPrivate(DatatypeConverter.parseHexBinary(privateKey));
        this.privateKey = privateKey;
        this.pubX = ec.getPublickeyX().toBigInteger();
    }

    public int compareTo(PublicX o) {
        return this.pubX.compareTo(o.pubX);
    }

    public String toString() {
        return this.privateKey;
    }
}
