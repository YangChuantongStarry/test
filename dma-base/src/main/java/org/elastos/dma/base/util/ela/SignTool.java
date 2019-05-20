package org.elastos.dma.base.util.ela;

import java.math.BigInteger;

import org.spongycastle.crypto.digests.SHA256Digest;
import org.spongycastle.crypto.params.ECPrivateKeyParameters;
import org.spongycastle.crypto.signers.ECDSASigner;
import org.spongycastle.crypto.signers.HMacDSAKCalculator;

public class SignTool {
    public SignTool() {
    }

    public static byte[] doSign(byte[] data, byte[] privateKey) {
        BigInteger privateKeyForSigning = new BigInteger(1, privateKey);
        ECDSASigner signer = new ECDSASigner(new HMacDSAKCalculator(new SHA256Digest()));
        ECPrivateKeyParameters privKey = new ECPrivateKeyParameters(privateKeyForSigning, ECKey.CURVE);
        signer.init(true, privKey);

        byte[] r;
        byte[] s;
        do {
            do {
                BigInteger[] components = signer.generateSignature(Sha256Hash.hash(data));
                r = Utils.bigIntegerToBytes(components[0], 32);
                s = Utils.bigIntegerToBytes(components[1], 32);
            } while(r.length > 32);
        } while(s.length > 32);

        byte[] signature = new byte[r.length + s.length];
        System.arraycopy(r, 0, signature, 0, r.length);
        System.arraycopy(s, 0, signature, r.length, s.length);
        return signature;
    }

    public static boolean verify(byte[] msg, byte[] sig, byte[] pub) {
        return false;
    }
}
