package me.zhengjie.utils;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
/**
 * 加密解密
 */
public class DianDianUtil {
//    public static final String KEY = "_@Ks`Y*9jLb.hvho}C;GwDpw";
    public static final String KEY = "_@Ks`Y*9jLb.hwqo}C;Gwmjk";
    public static final String IV = "2%2fTySe";
//    public static final String IV = "2%8iTpSi";
    public static final String DEFAULT_ENC_NAME = "UTF-8";

    public static String java_openssl_encrypt(String data) {
        return java_openssl_encrypt(data, IV);
    }


    /**
     * java_openssl_encrypt加密算法
     *
     * @param data
     * @param iv
     * @return
     * @throws Exception
     */
    public static String java_openssl_encrypt(String data, String iv) {
        try {
            Cipher cipher = createCipher(iv, Cipher.ENCRYPT_MODE);
            return URLEncoder.encode(Base64.getEncoder().encodeToString(cipher.doFinal(data.getBytes())), DEFAULT_ENC_NAME);
        } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | InvalidAlgorithmParameterException | IllegalBlockSizeException | BadPaddingException | UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String java_openssl_decrypt(String data) {
        return java_openssl_decrypt(data, IV);
    }

    /**
     * java_openssl_decrypt解密
     *
     * @param data
     * @param iv
     * @return
     */
    public static String java_openssl_decrypt(String data, String iv) {
        try {
            Cipher cipher = createCipher(iv, Cipher.DECRYPT_MODE);
            return new String(cipher.doFinal(Base64.getDecoder().decode(URLDecoder.decode(data, DEFAULT_ENC_NAME))));
        } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | InvalidAlgorithmParameterException | UnsupportedEncodingException | IllegalBlockSizeException | BadPaddingException e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * 创建密码器Cipher
     *
     * @param iv
     * @param mode 加/解密模式
     * @return
     * @throws NoSuchAlgorithmException
     * @throws NoSuchPaddingException
     * @throws InvalidKeyException
     * @throws InvalidAlgorithmParameterException
     */
    private static Cipher createCipher(String iv, int mode) throws NoSuchAlgorithmException, NoSuchPaddingException,
            InvalidKeyException, InvalidAlgorithmParameterException {
        byte[] key = KEY.getBytes();
        Cipher cipher = Cipher.getInstance("DESede/CBC/PKCS5Padding");
        IvParameterSpec ivParameterSpec = new IvParameterSpec(iv.getBytes());
        cipher.init(mode, new SecretKeySpec(key, "DESede"), ivParameterSpec);
        return cipher;
    }
}
