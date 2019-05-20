//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package org.elastos.dma.base.util.ela;

public class FormatTransfer {
    public FormatTransfer() {
    }

    public static byte[] toLH(int n) {
        byte[] b = new byte[]{(byte)(n & 255), (byte)(n >> 8 & 255), (byte)(n >> 16 & 255), (byte)(n >> 24 & 255)};
        return b;
    }

    public static byte[] toHH(int n) {
        byte[] b = new byte[]{(byte)(n >> 24 & 255), (byte)(n >> 16 & 255), (byte)(n >> 8 & 255), (byte)(n & 255)};
        return b;
    }

    public static byte[] toLH(short n) {
        byte[] b = new byte[]{(byte)(n & 255), (byte)(n >> 8 & 255)};
        return b;
    }

    public static byte[] toHH(short n) {
        byte[] b = new byte[]{(byte)(n >> 8 & 255), (byte)(n & 255)};
        return b;
    }

    public static byte[] toLH(float f) {
        return toLH(Float.floatToRawIntBits(f));
    }

    public static byte[] toHH(float f) {
        return toHH(Float.floatToRawIntBits(f));
    }

    public static byte[] stringToBytes(String s, int length) {
        while(s.getBytes().length < length) {
            s = s + " ";
        }

        return s.getBytes();
    }

    public static String bytesToString(byte[] b) {
        StringBuffer result = new StringBuffer("");
        int length = b.length;

        for(int i = 0; i < length; ++i) {
            result.append((char)(b[i] & 255));
        }

        return result.toString();
    }

    public static byte[] stringToBytes(String s) {
        return s.getBytes();
    }

    public static int hBytesToInt(byte[] b) {
        int s = 0;

        for(int i = 0; i < 3; ++i) {
            if (b[i] >= 0) {
                s += b[i];
            } else {
                s = s + 256 + b[i];
            }

            s *= 256;
        }

        if (b[3] >= 0) {
            s += b[3];
        } else {
            s = s + 256 + b[3];
        }

        return s;
    }

    public static int lBytesToInt(byte[] b) {
        int s = 0;

        for(int i = 0; i < 3; ++i) {
            if (b[3 - i] >= 0) {
                s += b[3 - i];
            } else {
                s = s + 256 + b[3 - i];
            }

            s *= 256;
        }

        if (b[0] >= 0) {
            s += b[0];
        } else {
            s = s + 256 + b[0];
        }

        return s;
    }

    public static short hBytesToShort(byte[] b) {
        int s = 0;
        if (b[0] >= 0) {
            s = s + b[0];
        } else {
            s = s + 256 + b[0];
        }

        s *= 256;
        if (b[1] >= 0) {
            s += b[1];
        } else {
            s = s + 256 + b[1];
        }

        short result = (short)s;
        return result;
    }

    public static short lBytesToShort(byte[] b) {
        int s = 0;
        if (b[1] >= 0) {
            s = s + b[1];
        } else {
            s = s + 256 + b[1];
        }

        s *= 256;
        if (b[0] >= 0) {
            s += b[0];
        } else {
            s = s + 256 + b[0];
        }

        short result = (short)s;
        return result;
    }

    public static float hBytesToFloat(byte[] b) {
        Float F = new Float(0.0D);
        int i = (((b[0] & 255) << 8 | b[1] & 255) << 8 | b[2] & 255) << 8 | b[3] & 255;
        return Float.intBitsToFloat(i);
    }

    public static float lBytesToFloat(byte[] b) {
        Float F = new Float(0.0D);
        int i = (((b[3] & 255) << 8 | b[2] & 255) << 8 | b[1] & 255) << 8 | b[0] & 255;
        return Float.intBitsToFloat(i);
    }

    public static byte[] bytesReverseOrder(byte[] b) {
        int length = b.length;
        byte[] result = new byte[length];

        for(int i = 0; i < length; ++i) {
            result[length - i - 1] = b[i];
        }

        return result;
    }

    public static void logBytes(byte[] bb) {
        int length = bb.length;
        String out = "";

        for(int i = 0; i < length; ++i) {
            out = out + bb + " ";
        }

    }

    public static int reverseInt(int i) {
        int result = hBytesToInt(toLH(i));
        return result;
    }

    public static short reverseShort(short s) {
        short result = hBytesToShort(toLH(s));
        return result;
    }

    public static float reverseFloat(float f) {
        float result = hBytesToFloat(toLH(f));
        return result;
    }
}
