package org.elastos.dma.base.util;

import java.util.Base64;

/**
 * @author hb.nie
 * @description
 */
class Base64UtilsJava {

    public static byte[] decode(String salt) {

        return Base64.getDecoder().decode(salt );
    }

    public static String encodeToString(byte[] src) {
        Base64.Encoder encoder = Base64.getEncoder();
        return encoder.encodeToString(src );
    }


    public static byte[] encode(byte[] encryptedkey) {
        return Base64.getDecoder().decode(encryptedkey );
    }
}
