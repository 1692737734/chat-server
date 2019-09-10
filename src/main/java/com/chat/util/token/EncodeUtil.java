package com.chat.util.token;

import sun.misc.BASE64Encoder;

public class EncodeUtil {
    public static String safeUrlBase64Encode(byte[] data){

        String encodeBase64 = new BASE64Encoder().encode(data);

        String safeBase64Str = encodeBase64.replace('+', '-');

        safeBase64Str = safeBase64Str.replace('/', '_');

        safeBase64Str = safeBase64Str.replaceAll("=", "");

        return safeBase64Str;

    }
}
