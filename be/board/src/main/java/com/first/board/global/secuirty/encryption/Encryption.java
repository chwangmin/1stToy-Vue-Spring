package com.first.board.global.secuirty.encryption;


import com.first.board.global.error.ErrorCode;
import com.first.board.global.error.exception.BusinessException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.Base64;

@Component
public class Encryption {

    private static final int SALT_SIZE = 16;

    @Value("{jwt.secretKey:test}")
    private String secretKey;

    public String hashing(byte[] password, String Salt) throws Exception {
        MessageDigest md = MessageDigest.getInstance("SHA-256");

        for (int i = 0 ; i < 7120; i++){
            String temp = byteToString(password) + Salt;
            md.update(temp.getBytes());
            password = md.digest();
        }

        return byteToString(password);
    }

    public String getSalt(){
        SecureRandom rnd = new SecureRandom();
        byte[] temp = new byte[SALT_SIZE];
        rnd.nextBytes(temp);

        return byteToString(temp);
    }

    private String byteToString(byte[] temp){
        StringBuilder sb = new StringBuilder();
        for(byte a : temp){
            sb.append(String.format("%02x",a));
        }
        return sb.toString();
    }

    public String encrypt(String value, String salt) {
        try {
            salt = salt.substring(0,16);
            IvParameterSpec iv = new IvParameterSpec(salt.getBytes(StandardCharsets.UTF_8));

            String secretKey = this.secretKey.substring(0,16);
            SecretKeySpec secretKeySpec = new SecretKeySpec(secretKey.getBytes(StandardCharsets.UTF_8), "AES");

            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
            cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, iv);

            byte[] encrypted = cipher.doFinal(value.getBytes());
            return Base64.getEncoder().encodeToString(encrypted);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public String decrypt(String encrypted, String salt) {
        try {
            salt = salt.substring(0,16);
            IvParameterSpec iv = new IvParameterSpec(salt.getBytes(StandardCharsets.UTF_8));

            String secretKey = this.secretKey.substring(0,16);
            SecretKeySpec secretKeySpec = new SecretKeySpec(secretKey.getBytes(StandardCharsets.UTF_8), "AES");

            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
            cipher.init(Cipher.DECRYPT_MODE, secretKeySpec, iv);

            byte[] original = cipher.doFinal(Base64.getDecoder().decode(encrypted));

            return new String(original);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public String encryptPassword(String password, String salt){
        try {
            return hashing(password.getBytes(), salt);
        } catch (Exception e) {
            throw new BusinessException(ErrorCode.INVALID_AES_KEY);
        }
    }
}