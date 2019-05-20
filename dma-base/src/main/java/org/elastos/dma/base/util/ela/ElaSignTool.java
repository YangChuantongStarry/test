//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package org.elastos.dma.base.util.ela;

import java.math.BigInteger;

import org.spongycastle.asn1.sec.SECNamedCurves;
import org.spongycastle.asn1.x9.X9ECParameters;
import org.spongycastle.crypto.digests.SHA256Digest;
import org.spongycastle.crypto.params.ECPublicKeyParameters;
import org.spongycastle.crypto.signers.ECDSASigner;
import org.spongycastle.crypto.signers.HMacDSAKCalculator;

public class ElaSignTool extends SignTool {
    private static final String CURVE_ALGO = "secp256r1";

    public ElaSignTool() {
    }

    public static boolean verify(byte[] msg, byte[] sig, byte[] pub) {
        if (sig.length != 64) {
            return false;
        } else {
            byte[] rb = new byte[sig.length / 2];
            byte[] sb = new byte[sig.length / 2];
            System.arraycopy(sig, 0, rb, 0, rb.length);
            System.arraycopy(sig, sb.length, sb, 0, sb.length);
            BigInteger r = parseBigIntegerPositive(new BigInteger(rb), rb.length * 8);
            BigInteger s = parseBigIntegerPositive(new BigInteger(sb), rb.length * 8);
            msg = Sha256Hash.hash(msg);
            X9ECParameters curve = SECNamedCurves.getByName("secp256r1");
            ECDSASigner signer = new ECDSASigner(new HMacDSAKCalculator(new SHA256Digest()));
            ECPublicKeyParameters publicKey = new ECPublicKeyParameters(curve.getCurve().decodePoint(pub), ECKey.CURVE);
            signer.init(false, publicKey);
            return signer.verifySignature(msg, r, s);
        }
    }

    public static BigInteger parseBigIntegerPositive(BigInteger b, int bitlen) {
        if (b.compareTo(BigInteger.ZERO) < 0) {
            b = b.add(BigInteger.ONE.shiftLeft(bitlen));
        }

        return b;
    }
}
