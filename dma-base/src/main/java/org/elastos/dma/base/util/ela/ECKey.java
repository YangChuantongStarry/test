//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package org.elastos.dma.base.util.ela;


import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.List;


import com.google.common.base.Preconditions;
import org.spongycastle.asn1.x9.X9ECParameters;
import org.spongycastle.crypto.AsymmetricCipherKeyPair;
import org.spongycastle.crypto.ec.CustomNamedCurves;
import org.spongycastle.crypto.generators.ECKeyPairGenerator;
import org.spongycastle.crypto.params.ECDomainParameters;
import org.spongycastle.crypto.params.ECKeyGenerationParameters;
import org.spongycastle.crypto.params.ECPrivateKeyParameters;
import org.spongycastle.crypto.params.ECPublicKeyParameters;
import org.spongycastle.math.ec.ECFieldElement;
import org.spongycastle.math.ec.ECPoint;
import org.spongycastle.math.ec.FixedPointCombMultiplier;
import org.spongycastle.math.ec.FixedPointUtil;

public class ECKey {
    private static final X9ECParameters CURVE_PARAMS = CustomNamedCurves.getByName("secp256r1");
    public static final ECDomainParameters CURVE;
    private static final SecureRandom secureRandom = new SecureRandom();
    private final BigInteger priv;
    private final LazyECPoint pub;

    public ECKey() {
        ECKeyPairGenerator generator = new ECKeyPairGenerator();
        ECKeyGenerationParameters keygenParams = new ECKeyGenerationParameters(CURVE, secureRandom);
        generator.init(keygenParams);
        AsymmetricCipherKeyPair keypair = generator.generateKeyPair();
        ECPrivateKeyParameters privParams = (ECPrivateKeyParameters)keypair.getPrivate();
        ECPublicKeyParameters pubParams = (ECPublicKeyParameters)keypair.getPublic();
        this.priv = privParams.getD();
        this.pub = new LazyECPoint(CURVE.getCurve(), pubParams.getQ().getEncoded(true));
    }

    public byte[] getPrivateKeyBytes() {
        return Utils.bigIntegerToBytes(this.priv, 32);
    }

    public byte[] getPubBytes() {
        return this.pub.getEncoded();
    }

    public ECFieldElement getPublickeyX() {
        return this.pub.getX();
    }

    public static byte[] generateKey(int len) {
        byte[] key = new byte[len];
        SecureRandom secureRandom = new SecureRandom();
        secureRandom.nextBytes(key);
        return key;
    }

    public static byte[] generateKey() {
        return generateKey(32);
    }

    public static byte[] publicBytesFromPrivate(byte[] priv) {
        BigInteger privKey = new BigInteger(1, priv);
        if (privKey.bitLength() > CURVE.getN().bitLength()) {
            privKey = privKey.mod(CURVE.getN());
        }

        return (new FixedPointCombMultiplier()).multiply(CURVE.getG(), privKey).getEncoded(true);
    }

    public static ECPoint publicPointFromPrivate(BigInteger privKey) {
        if (privKey.bitLength() > CURVE.getN().bitLength()) {
            privKey = privKey.mod(CURVE.getN());
        }

        return (new FixedPointCombMultiplier()).multiply(CURVE.getG(), privKey);
    }

    protected ECKey( BigInteger priv, ECPoint pub) {
        this(priv, new LazyECPoint((ECPoint) Preconditions.checkNotNull(pub)));
    }

    protected ECKey( BigInteger priv, LazyECPoint pub) {
        if (priv != null) {
            Preconditions.checkArgument(priv.bitLength() <= 256, "private key exceeds 32 bytes: %s bits", priv.bitLength());
            Preconditions.checkArgument(!priv.equals(BigInteger.ZERO));
            Preconditions.checkArgument(!priv.equals(BigInteger.ONE));
        }

        this.priv = priv;
        this.pub = (LazyECPoint)Preconditions.checkNotNull(pub);
    }

    public static ECKey fromPrivate(BigInteger privKey, boolean compressed) {
        ECPoint point = publicPointFromPrivate(privKey);
        return new ECKey(privKey, getPointWithCompression(point, compressed));
    }

    public static ECKey fromPrivate(byte[] privKeyBytes) {
        return fromPrivate(new BigInteger(1, privKeyBytes));
    }

    public static ECKey fromPrivate(BigInteger privKey) {
        return fromPrivate(privKey, true);
    }

    private static ECPoint getPointWithCompression(ECPoint point, boolean compressed) {
        if (point.isCompressed() == compressed) {
            return point;
        } else {
            point = point.normalize();
            BigInteger x = point.getAffineXCoord().toBigInteger();
            BigInteger y = point.getAffineYCoord().toBigInteger();
            return CURVE.getCurve().createPoint(x, y, compressed);
        }
    }

    public byte[] getProgram(int singType) {
        return Util.CreateSingleSignatureRedeemScript(this.getPubBytes(), singType);
    }

    public byte[] getSingleSignProgramHash(int signType) {
        return Util.ToCodeHash(this.getProgram(signType), signType);
    }

    public String toAddress() {
        return Util.ToAddress(this.getSingleSignProgramHash(1));
    }

    public String toIdentityID() {
        return Util.ToAddress(this.getSingleSignProgramHash(3));
    }

    public static byte[] getGenesisSignatureProgram(String GenesisBlockHash) throws SDKException {
        return Util.GenGenesisAddressRedeemScript(GenesisBlockHash);
    }

    public static byte[] getGenesisSignProgramHash(String GenesisBlockHash) throws SDKException {
        return Util.ToCodeHash(getGenesisSignatureProgram(GenesisBlockHash), 4);
    }

    public static String toGenesisSignAddress(String GenesisBlockHash) throws SDKException {
        return Util.ToAddress(getGenesisSignProgramHash(GenesisBlockHash));
    }

    public static byte[] getMultiSignatureProgram(List<PublicX> privateKeyList, int M) throws SDKException {
        return Util.CreatemultiSignatureRedeemScript(privateKeyList, M);
    }

    public byte[] getMultiSignProgramHash(List<PublicX> privateKeyList, int M) throws SDKException {
        return Util.ToCodeHash(getMultiSignatureProgram(privateKeyList, M), 2);
    }

    public String toMultiSignAddress(List<PublicX> privateKeyList, int M) throws SDKException {
        return Util.ToAddress(this.getMultiSignProgramHash(privateKeyList, M));
    }

    static {
        FixedPointUtil.precompute(CURVE_PARAMS.getG(), 12);
        CURVE = new ECDomainParameters(CURVE_PARAMS.getCurve(), CURVE_PARAMS.getG(), CURVE_PARAMS.getN(), CURVE_PARAMS.getH());
    }
}
