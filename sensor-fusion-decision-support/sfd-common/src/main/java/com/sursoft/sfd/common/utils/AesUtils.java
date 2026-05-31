package com.sursoft.sfd.common.utils;

import cn.hutool.crypto.symmetric.AES;
import cn.hutool.core.codec.Base64;
import java.nio.charset.StandardCharsets;

public final class AesUtils {

    private static final String SECRET_KEY = "SurSoft@SFD#2026";

    private AesUtils() {}

    public static String encrypt(String plaintext) {
        if (plaintext == null) return null;
        AES aes = new AES(SECRET_KEY.getBytes(StandardCharsets.UTF_8));
        return aes.encryptBase64(plaintext);
    }

    public static String decrypt(String ciphertext) {
        if (ciphertext == null) return null;
        AES aes = new AES(SECRET_KEY.getBytes(StandardCharsets.UTF_8));
        return aes.decryptStr(ciphertext);
    }
}
