//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package org.elastos.dma.base.util.ela;

import org.elastos.dma.base.util.common.ErrorCode;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.lang.Character.UnicodeBlock;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import io.github.novacrypto.DatatypeConverter;

public class Util {
    public Util() {
    }

    public static void WriteVarUint(DataOutputStream writer, long value) throws IOException {
        byte[] buf = new byte[9];
        if (value < 253L) {
            writer.writeByte((byte)((int)value));
        } else if (value <= 65535L) {
            writer.writeByte(253);
            short s = FormatTransfer.reverseShort((short)((int)value));
            writer.writeShort(s);
        } else if (value <= 4294967295L) {
            writer.writeByte(254);
            int n = Integer.reverseBytes((int)value);
            writer.writeInt(n);
        } else {
            writer.writeByte(255);
            long l = Long.reverseBytes(value);
            writer.writeLong(l);
        }

    }




    public static byte[] ToScriptHash(String address) {
        byte[] decoded = Base58.decodeChecked(address);
        BigInteger bi = new BigInteger(decoded);
        byte[] ph = new byte[21];
        System.arraycopy(bi.toByteArray(), 0, ph, 0, 21);
        return ph;
    }

    public static boolean checkAddress(String address) {
        try {
            byte[] sh = ToScriptHash(address);
            return sh[0] == 33 || sh[0] == 18;
        } catch (Exception var2) {
            return false;
        }
    }

    public static byte[] ToCodeHash(byte[] code, int signType) {
        byte[] f = Utils.sha256hash160(code);
        byte[] g = new byte[f.length + 1];
        if (signType == 1) {
            g[0] = 33;
            System.arraycopy(f, 0, g, 1, f.length);
        } else if (signType == 2) {
            g[0] = 18;
        } else if (signType == 3) {
            g[0] = 103;
        } else {
            if (signType != 4) {
                return null;
            }

            g[0] = 75;
        }

        System.arraycopy(f, 0, g, 1, f.length);
        return g;
    }

    public static String ToAddress(byte[] programHash) {
        byte[] f = Sha256Hash.hashTwice(programHash);
        byte[] g = new byte[programHash.length + 4];
        System.arraycopy(programHash, 0, g, 0, programHash.length);
        System.arraycopy(f, 0, g, programHash.length, 4);
        return Base58.encode(g);
    }

    public static byte[] CreateSingleSignatureRedeemScript(byte[] pubkey, int signType) {
        byte[] script = new byte[35];
        script[0] = 33;
        System.arraycopy(pubkey, 0, script, 1, 33);
        if (signType == 1) {
            script[34] = -84;
        } else if (signType == 3) {
            script[34] = -83;
        }

        return script;
    }

    public static void sortByteArrayArrayUseRevertBytesSequence(byte[][] hashes) {
        Arrays.sort(hashes, new Comparator() {
            public int compare(Object o1, Object o2) {
                byte[] ba1 = (byte[])((byte[])o1);
                byte[] ba2 = (byte[])((byte[])o2);

                for(int i = ba1.length - 1; i >= 0; --i) {
                    int ret = (ba1[i] & 255) - (ba2[i] & 255);
                    if (ret != 0) {
                        return ret;
                    }
                }

                return 0;
            }
        });
    }

    public static long IntByString(String value) {
        String[] split = value.split("\\.");
        long front;
        if (split.length == 2) {
            front = (long)Integer.parseInt(split[0]) * 100000000L;
            String after = split[1];
            long afterInt;
            long Value;
            if (after.length() == 8) {
                afterInt = (long)Integer.parseInt(after);
                Value = front + afterInt;
                return Value;
            } else {
                after = after + 48 * (8 - after.length());
                afterInt = (long)Integer.parseInt(after);
                Value = front + afterInt;
                return Value;
            }
        } else {
            front = (long)Integer.parseInt(value) * 100000000L;
            return front;
        }
    }

    public static BigDecimal multiplyAmountELA(BigDecimal price, Integer decimal) {
        BigDecimal coefficient = new BigDecimal(Math.pow(10.0D, (double)decimal));
        return price.multiply(coefficient).setScale(8, 1);
    }

    public static BigDecimal divideAmountELA(BigDecimal price, Integer decimal) {
        BigDecimal coefficient = new BigDecimal(Math.pow(10.0D, (double)decimal));
        return price.divide(coefficient).setScale(8, 1);
    }

    public static BigDecimal multiplyAmountETH(BigDecimal price, Integer decimal) {
        BigDecimal coefficient = new BigDecimal(Math.pow(10.0D, (double)decimal));
        return price.multiply(coefficient).setScale(18, 1);
    }

    public static BigDecimal divideAmountETH(BigDecimal price, Integer decimal) {
        BigDecimal coefficient = new BigDecimal(Math.pow(10.0D, (double)decimal));
        return price.divide(coefficient).setScale(18, 1);
    }

    public static boolean isChinese(String strName) {
        char[] ch = strName.toCharArray();

        for(int i = 0; i < ch.length; ++i) {
            char c = ch[i];
            if (isChinese(c)) {
                return true;
            }
        }

        return false;
    }

    private static boolean isChinese(char c) {
        UnicodeBlock ub = UnicodeBlock.of(c);
        return ub == UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS || ub == UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS || ub == UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A || ub == UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_B || ub == UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION || ub == UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS || ub == UnicodeBlock.GENERAL_PUNCTUATION;
    }

    public static byte[] GenGenesisAddressRedeemScript(String genesisBlockHash) throws SDKException {
        byte[] reversedGenesisBlockBytes = Utils.reverseBytes(DatatypeConverter.parseHexBinary(genesisBlockHash));
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(baos);

        try {
            dos.write((byte)reversedGenesisBlockBytes.length);
            dos.write(reversedGenesisBlockBytes);
            dos.write(-81);
            return baos.toByteArray();
        } catch (Exception var5) {
            throw new SDKException(ErrorCode.ParamErr("create GenGenesisAddress redeem Script failure , " + var5));
        }
    }

    public static byte[] CreatemultiSignatureRedeemScript(List<PublicX> privateKeyList, int M) throws SDKException {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            DataOutputStream dos = new DataOutputStream(baos);
            if (M == 0) {
                M = privateKeyList.size() / 2 + 1;
            }

            dos.write((byte)(81 + M - 1));
            Collections.sort(privateKeyList);

            for(int i = 0; i < privateKeyList.size(); ++i) {
                dos.writeByte(33);
                dos.write(DatatypeConverter.parseHexBinary(Ela.getPublicFromPrivate(((PublicX)privateKeyList.get(i)).toString())));
            }

            dos.write((byte)(81 + privateKeyList.size() - 1));
            dos.write(-82);
            return baos.toByteArray();
        } catch (Exception var5) {
            throw new SDKException(ErrorCode.ParamErr("create multiSignature redeem Script failure , " + var5));
        }
    }

    public static void WriteVarBytes(DataOutputStream writer, byte[] value) throws IOException {
        WriteVarUint(writer, (long)value.length);
        writer.write(value);
    }

    public static long ReadVarUint(DataInputStream read) throws IOException {
        byte n = read.readByte();
        if ((n & 255) < 253) {
            return (long)(n & 255);
        } else if ((n & 255) == 253) {
            short shortNumber = read.readShort();
            short number = FormatTransfer.reverseShort(shortNumber);
            return (long)number;
        } else if ((n & 255) == 254) {
            int intNumber = read.readInt();
            int number = Integer.reverseBytes(intNumber);
            return (long)number;
        } else if ((n & 255) == 255) {
            long longNumber = read.readLong();
            long number = Long.reverseBytes(longNumber);
            return number;
        } else {
            return 0L;
        }
    }
}
