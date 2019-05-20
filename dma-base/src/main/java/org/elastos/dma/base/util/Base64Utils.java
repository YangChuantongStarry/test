package org.elastos.dma.base.util;


/**
 * @author hb.nie
 * @description
 */
public class Base64Utils {


    private static final String android_data = System.getenv("ANDROID_DATA");
    private static final boolean isAndroidSystem = android_data != null;

    public static byte[] decode(String salt) {
        if (isAndroidSystem) {
         return    Base64UtilsAndroid.decode(salt);
        }
        return Base64UtilsJava.decode(salt);
    }

    public static String encodeToString(byte[] src) {
        if (isAndroidSystem) {
        return     Base64UtilsAndroid.encodeToString(src);
        }
        return Base64UtilsJava.encodeToString(src);
    }


    public static byte[] encode(byte[] encryptedkey) {
        if (isAndroidSystem) {
            return Base64UtilsAndroid.encode(encryptedkey);
        }
        return Base64UtilsJava.encode(encryptedkey);
    }
}

