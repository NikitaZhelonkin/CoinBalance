package ru.nikitazhelonkin.coinbalance.utils;


import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

public class DigestUtil {


    public static byte[] hmac(byte[] msgBytes, byte[] keyBytes, String algo) {
        try {
            SecretKeySpec key = new SecretKeySpec(keyBytes, algo);
            Mac mac = Mac.getInstance(algo);
            mac.init(key);

            return mac.doFinal(msgBytes);
        } catch (InvalidKeyException | NoSuchAlgorithmException e) {
            return null;
        }
    }

    public static String hmacString(byte[] msgBytes, byte[] keyBytes, String algo) {
        return bytesToHex(hmac(msgBytes, keyBytes, algo));
    }

    public static String hmacString(String msg, String keyString, String algo) {
        try {
            return hmacString(msg.getBytes("ASCII"), (keyString).getBytes(StandardCharsets.UTF_8), algo);
        } catch (UnsupportedEncodingException e) {
            return null;
        }
    }

    public static byte[] hmac(String msg, String keyString, String algo) {
        try {
            return hmac(msg.getBytes("ASCII"), (keyString).getBytes(StandardCharsets.UTF_8), algo);
        } catch (UnsupportedEncodingException e) {
            return null;
        }
    }

    public static byte[] sha256(String msg) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            return digest.digest(msg.getBytes(StandardCharsets.UTF_8));
        } catch (NoSuchAlgorithmException e) {
            return null;
        }
    }

    public static String sha256String(String msg) {
        return bytesToHex(sha256(msg));
    }

    private static String bytesToHex(byte[] bytes) {
        if (bytes == null) {
            return null;
        }
        StringBuilder result = new StringBuilder();
        for (byte aByte : bytes) {
            String hex = Integer.toHexString(0xFF & aByte);
            if (hex.length() == 1) {
                result.append('0');
            }
            result.append(hex);
        }
        return result.toString();
    }

}
