package me.zhengjie.modules.util;


import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

/**
 * 闪钱加解密
 *
 * @author lhj on 2025/4/23
 */
public class ShanQianAESUtils {

    public static String encrypt(String data, String key, String iv) throws Exception {
        // 创建密钥和初始化向量
        SecretKeySpec secretKey = new SecretKeySpec(key.getBytes(), "AES");
        IvParameterSpec ivParameterSpec = new IvParameterSpec(iv.getBytes());

        // 获取Cipher实例并初始化
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding"); // 注意这里用的是PKCS5Padding，但通常与PKCS7Padding效果相同
        cipher.init(Cipher.ENCRYPT_MODE, secretKey, ivParameterSpec);

        // 加密数据
        byte[] encrypted = cipher.doFinal(data.getBytes());

        // 编码为Base64
        return Base64.getEncoder().encodeToString(encrypted);
    }

    // 添加解密方法
    public static String decrypt(String encryptedData, String key, String iv) throws Exception {
        // 创建密钥和初始化向量
        SecretKeySpec secretKey = new SecretKeySpec(key.getBytes(), "AES");
        IvParameterSpec ivParameterSpec = new IvParameterSpec(iv.getBytes());

        // 获取Cipher实例并初始化
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding"); // 注意这里用的是PKCS5Padding，但通常与PKCS7Padding效果相同
        cipher.init(Cipher.DECRYPT_MODE, secretKey, ivParameterSpec);

        // 解码Base64字符串
        byte[] decodedBytes = Base64.getDecoder().decode(encryptedData);

        // 解密数据
        byte[] decryptedBytes = cipher.doFinal(decodedBytes);
        return new String(decryptedBytes);
    }

    public static void main(String[] args) {
        try {
            String key = "TMh984xC8K2Ydzgs"; // 16 bytes key for AES-128
            String iv = "HmItDKuuVDx6FBJy"; // 16 bytes IV for AES-128-CBC
            String originalText = "Hello, World!";
            String encryptedText = ShanQianAESUtils.encrypt(originalText, key, iv);
            String decryptedText = ShanQianAESUtils.decrypt(encryptedText, key, iv);
            System.out.println("Original: " + originalText);
            System.out.println("Encrypted: " + encryptedText);
            System.out.println("Decrypted: " + decryptedText);

            String decrypt = ShanQianAESUtils.decrypt("I5WQmmQr3IMYrk5/QeWnykHlIJJ8lZnn987qYZICaVTg/IhZo7KpssF2revaNOawk5ZqoMs9EbPF2fielHuLhg==", key, iv);
            System.out.println("ceshi: " + decrypt);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
