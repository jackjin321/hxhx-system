//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package me.zhengjie.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.Optional;

public class DESUtil {
    private static final Logger log = LoggerFactory.getLogger(DESUtil.class);
    private static final String DES = "DES";

    public DESUtil() {
    }

    public static void main(String[] args) {
        Optional<String> key = getKey();
        Optional<String> encrypt = encrypt("111", (String)key.get());
        Optional<String> decrypt = decrypt((String)encrypt.get(), (String)key.get());
        System.out.println((String)encrypt.get());
        System.out.println((String)decrypt.get());
    }

    public static Optional<String> getKey() {
        try {
            KeyGenerator keyGenerator = KeyGenerator.getInstance("DES");
            keyGenerator.init(56);
            SecretKey generateKey = keyGenerator.generateKey();
            byte[] encoded = generateKey.getEncoded();
            String key = Base64.getEncoder().encodeToString(encoded);
            return Optional.ofNullable(key);
        } catch (Exception var4) {
            var4.printStackTrace();
            return Optional.empty();
        }
    }

    public static Optional<String> encrypt(String data, String key) {
        Optional<byte[]> optionalBytes = encrypt(StringUtil.toByteArray(data), StringUtil.toByteArray(key));
        return optionalBytes.isPresent() ? Optional.of(HexUtil.byteArr2HexStr((byte[])optionalBytes.get())) : Optional.empty();
    }

    public static Optional<String> decrypt(String data, String key) {
        if (data == null) {
            return null;
        } else {
            byte[] buf = HexUtil.hexStr2ByteArr(data);
            Optional<byte[]> optionalBytes = decrypt(buf, StringUtil.toByteArray(key));
            return optionalBytes.isPresent() ? Optional.of(StringUtil.byteToString((byte[])optionalBytes.get())) : Optional.empty();
        }
    }

    public static Optional<String> decryptBase64(String data, String key) {
        if (data == null) {
            return null;
        } else {
            byte[] buf = StringUtil.decode(data);
            Optional<byte[]> optionalBytes = decrypt(buf, StringUtil.toByteArray(key));
            return optionalBytes.isPresent() ? Optional.of(StringUtil.byteToString((byte[])optionalBytes.get())) : Optional.empty();
        }
    }

    private static Optional<byte[]> encrypt(byte[] data, byte[] key) {
        try {
            SecureRandom sr = new SecureRandom();
            DESKeySpec dks = new DESKeySpec(key);
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
            SecretKey securekey = keyFactory.generateSecret(dks);
            Cipher cipher = Cipher.getInstance("DES");
            cipher.init(1, securekey, sr);
            return Optional.ofNullable(cipher.doFinal(data));
        } catch (Exception var7) {
            log.error("DSC encrypt exception ", var7);
            return Optional.empty();
        }
    }

    private static Optional<byte[]> decrypt(byte[] data, byte[] key) {
        try {
            SecureRandom sr = new SecureRandom();
            DESKeySpec dks = new DESKeySpec(key);
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
            SecretKey securekey = keyFactory.generateSecret(dks);
            Cipher cipher = Cipher.getInstance("DES");
            cipher.init(2, securekey, sr);
            return Optional.of(cipher.doFinal(data));
        } catch (Exception var7) {
            log.error("DSC decrypt exception ", var7);
            return Optional.empty();
        }
    }
}
