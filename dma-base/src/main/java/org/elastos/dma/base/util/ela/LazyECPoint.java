//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package org.elastos.dma.base.util.ela;

import com.google.common.base.Preconditions;
import java.math.BigInteger;
import java.util.Arrays;

import org.spongycastle.math.ec.ECCurve;
import org.spongycastle.math.ec.ECFieldElement;
import org.spongycastle.math.ec.ECPoint;

public class LazyECPoint {
    private final ECCurve curve;
    private final byte[] bits;
    private ECPoint point;

    public LazyECPoint(ECCurve curve, byte[] bits) {
        this.curve = curve;
        this.bits = bits;
    }

    public LazyECPoint(ECPoint point) {
        this.point = (ECPoint)Preconditions.checkNotNull(point);
        this.curve = null;
        this.bits = null;
    }

    public ECPoint get() {
        if (this.point == null) {
            this.point = this.curve.decodePoint(this.bits);
        }

        return this.point;
    }

    public ECPoint getDetachedPoint() {
        return this.get().getDetachedPoint();
    }

    public byte[] getEncoded() {
        return this.bits != null ? Arrays.copyOf(this.bits, this.bits.length) : this.get().getEncoded();
    }

    public boolean isInfinity() {
        return this.get().isInfinity();
    }

    public ECPoint timesPow2(int e) {
        return this.get().timesPow2(e);
    }

    public ECFieldElement getYCoord() {
        return this.get().getYCoord();
    }

    public ECFieldElement[] getZCoords() {
        return this.get().getZCoords();
    }

    public boolean isNormalized() {
        return this.get().isNormalized();
    }

    public boolean isCompressed() {
        if (this.bits == null) {
            return this.get().isCompressed();
        } else {
            return this.bits[0] == 2 || this.bits[0] == 3;
        }
    }

    public ECPoint multiply(BigInteger k) {
        return this.get().multiply(k);
    }

    public ECPoint subtract(ECPoint b) {
        return this.get().subtract(b);
    }

    public boolean isValid() {
        return this.get().isValid();
    }

    public ECPoint scaleY(ECFieldElement scale) {
        return this.get().scaleY(scale);
    }

    public ECFieldElement getXCoord() {
        return this.get().getXCoord();
    }

    public ECPoint scaleX(ECFieldElement scale) {
        return this.get().scaleX(scale);
    }

    public boolean equals(ECPoint other) {
        return this.get().equals(other);
    }

    public ECPoint negate() {
        return this.get().negate();
    }

    public ECPoint threeTimes() {
        return this.get().threeTimes();
    }

    public ECFieldElement getZCoord(int index) {
        return this.get().getZCoord(index);
    }

    public byte[] getEncoded(boolean compressed) {
        return compressed == this.isCompressed() && this.bits != null ? Arrays.copyOf(this.bits, this.bits.length) : this.get().getEncoded(compressed);
    }

    public ECPoint add(ECPoint b) {
        return this.get().add(b);
    }

    public ECPoint twicePlus(ECPoint b) {
        return this.get().twicePlus(b);
    }

    public ECCurve getCurve() {
        return this.get().getCurve();
    }

    public ECPoint normalize() {
        return this.get().normalize();
    }

    public ECFieldElement getY() {
        return this.normalize().getYCoord();
    }

    public ECPoint twice() {
        return this.get().twice();
    }

    public ECFieldElement getAffineYCoord() {
        return this.get().getAffineYCoord();
    }

    public ECFieldElement getAffineXCoord() {
        return this.get().getAffineXCoord();
    }

    public ECFieldElement getX() {
        return this.normalize().getXCoord();
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        } else {
            return o != null && this.getClass() == o.getClass() ? Arrays.equals(this.getCanonicalEncoding(), ((LazyECPoint)o).getCanonicalEncoding()) : false;
        }
    }

    public int hashCode() {
        return Arrays.hashCode(this.getCanonicalEncoding());
    }

    private byte[] getCanonicalEncoding() {
        return this.getEncoded(true);
    }
}
