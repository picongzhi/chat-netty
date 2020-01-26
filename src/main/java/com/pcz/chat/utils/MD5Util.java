package com.pcz.chat.utils;

import org.apache.commons.codec.binary.Base64;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * @author picongzhi
 */
public class MD5Util {
    public static String getMD5String(String str) {
        MessageDigest messageDigest = null;
        try {
            messageDigest = MessageDigest.getInstance("MD5");
            return Base64.encodeBase64String(messageDigest.digest(str.getBytes()));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }
}
