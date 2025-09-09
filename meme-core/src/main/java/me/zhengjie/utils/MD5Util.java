package me.zhengjie.utils;

import cn.hutool.core.text.CharSequenceUtil;
import cn.hutool.crypto.digest.MD5;

import java.nio.charset.Charset;
import java.util.regex.Pattern;

/**
 * @author Eathon
 * @description: MD 工具类, 单例模式
 * @date 2024/1/23 19:31
 */
public class MD5Util {


    private static final MD5 INSTANT = MD5.create();

    private static final String MD5_PATTERN = "^[0-9a-fA-F]{32}$";

    public static String md5Hex(String content, String salt){
        if (CharSequenceUtil.isNotBlank(salt)){
            content = CharSequenceUtil.appendIfMissing(content, salt);
        }
        return INSTANT.digestHex(content, Charset.defaultCharset());
    }


    public static String md5Hex(String content){
        return md5Hex(content, null);
    }


    public static boolean isMd5(String content) {
        // 正则表达式匹配32个十六进制字符
        return Pattern.matches(MD5_PATTERN, content);
    }
}
