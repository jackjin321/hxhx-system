package me.zhengjie.utils;

import me.zhengjie.utils.date.DateUtils;

import java.util.Random;

public class RandomUtil {
    private static Random random = new Random();
    private static String[] upper = new String[]{"A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z", "a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z"};
    private static String[] number = new String[]{"1", "2", "3", "4", "5", "6", "7", "8", "9", "0"};

    public RandomUtil() {
    }

    public static String randomCode() {
        return upper[random.nextInt(52)];
    }

    public static String randomCode(int length) {
        StringBuffer sb = new StringBuffer();

        for(int i = 0; i < length; ++i) {
            sb.append(randomCode());
        }

        return sb.toString();
    }

    public static String randomCodeSuffixDate(int length) {
        StringBuffer sb = new StringBuffer();

        for(int i = 0; i < length; ++i) {
            sb.append(randomCode());
        }

        sb.append(DateUtils.nowDateTimeMilliSecond());
        return sb.toString();
    }

    public static Integer randomNumber() {
        return random.nextInt(10);
    }

    public static String randomNumber(int length) {
        StringBuffer sb = new StringBuffer();

        for(int i = 0; i < length; ++i) {
            sb.append(randomNumber());
        }

        return sb.toString();
    }

    public static String randomOne(String[] s) {
        return s[random.nextInt(s.length - 1)];
    }

    public static Integer randomOneNumber(int max) {
        return random.nextInt(max);
    }

    public static String randomOrderNo(String prefix) {
        StringBuffer sb = new StringBuffer(prefix);
        sb.append("-");
        sb.append(DateUtils.nowDateTimeMilliSecond());
        sb.append(randomCode(13));
        return sb.toString();
    }

    public static String randomNumberCode(int length) {
        String val = "";
        String charStr = "char";
        String numStr = "num";
        Random random = new Random();

        for(int i = 0; i < length; ++i) {
            String charOrNum = random.nextInt(2) % 2 == 0 ? charStr : numStr;
            if (charStr.equalsIgnoreCase(charOrNum)) {
                int temp = random.nextInt(2) % 2 == 0 ? 65 : 97;
                val = val + (char)(random.nextInt(26) + temp);
            } else if (numStr.equalsIgnoreCase(charOrNum)) {
                val = val + String.valueOf(random.nextInt(10));
            }
        }

        return val;
    }
}
