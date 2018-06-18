package com.example.algeiba.iot2;

import android.util.Base64;

import java.security.MessageDigest;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

/**
 * Created by Johnny on 17/6/2018.
 */

public class SeguridadUtil
{
    public final static String PALABRACLAVE = "encriptUnaj2018";
    public final static String ALGORITMO = "AES";

    public static  SecretKeySpec generateKey(String password) throws  Exception {
        final MessageDigest digest= MessageDigest.getInstance("SHA-256");
        byte[] bytes = password.getBytes("UTF-8");
        digest.update(bytes,0,bytes.length);
        byte[] key = digest.digest();
        SecretKeySpec secretKeySpec = new SecretKeySpec(key,"AES");
        return secretKeySpec;
    }
    public static String encrypt(String user, String password) throws Exception {
        SecretKeySpec key = generateKey(password);
        Cipher c =  Cipher.getInstance(ALGORITMO);
        c.init(Cipher.ENCRYPT_MODE,key);
        byte[] encVal = c.doFinal(user.getBytes());
        String encriptedValue = Base64.encodeToString(encVal,Base64.DEFAULT);
        return encriptedValue;
    }

    public static String decrypt(String user, String password)throws Exception {
        SecretKeySpec key = generateKey(password);
        Cipher c = Cipher.getInstance(ALGORITMO);
        c.init(Cipher.DECRYPT_MODE, key);
        byte[] decodeValue = Base64.decode(user, Base64.DEFAULT);
        byte[] decValue = c.doFinal(decodeValue);
        String decryptedValue = new String(decValue);
        return decryptedValue;
    }

}
