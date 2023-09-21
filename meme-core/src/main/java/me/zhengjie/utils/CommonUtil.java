package me.zhengjie.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Optional;
import java.util.Random;


public class CommonUtil {
    private static final Logger log = LoggerFactory.getLogger(me.zhengjie.utils.CommonUtil.class);

    public CommonUtil() {
    }

    public static String signIdCard(String idCard) {
        StringBuilder sb = new StringBuilder();
        sb.append(idCard.substring(0, 4));
        sb.append("******");
        sb.append(idCard.substring(idCard.length() - 4, idCard.length()));
        return sb.toString();
    }

    public static String signPhone(String phone) {
        StringBuilder sb = new StringBuilder();
        if(phone.length() < 9){
            return sb.toString();
        }
        sb.append(phone.substring(0, 3));
        sb.append("****");
        sb.append(phone.substring(7, phone.length()));
        return sb.toString();
    }

    public static String signName(String realName) {
        StringBuilder sb = new StringBuilder();
        if (realName.length() == 2) {
            sb.append(realName.substring(0, 1));
            sb.append("*");
        } else if (realName.length() == 3) {
            sb.append(realName.substring(0, 1));
            sb.append("**");
        } else if (realName.length() == 4) {
            sb.append(realName.substring(0, 2));
            sb.append("***");
        } else {
            int index;
            if ((index = realName.indexOf(".")) != -1) {
                sb.append(realName.substring(0, index));
                sb.append(".******");
            } else {
                sb.append(realName.substring(0, 2));
                sb.append("***");
            }
        }

        return sb.toString();
    }

    public static String getIdCard() {
        String[] provinces = new String[]{"11", "12", "13", "14", "15", "21", "22", "23", "31", "32", "33", "34", "35", "36", "37", "41", "42", "43", "44", "45", "46", "50", "51", "52", "53", "54", "61", "62", "63", "64", "65", "71", "81", "82"};
        String no = (new Random()).nextInt(899) + 100 + "";
        String[] checks = new String[]{"0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "X"};
        StringBuilder builder = new StringBuilder();
        builder.append(randomOne(provinces));
        builder.append(randomCityCode(18));
        builder.append(randomCityCode(28));
        builder.append(randomBirth(20, 50));
        builder.append(no).append(randomOne(checks));
        return builder.toString();
    }

    private static String randomOne(String[] s) {
        return s[(new Random()).nextInt(s.length - 1)];
    }

    private static String randomCityCode(int max) {
        int i = (new Random()).nextInt(max) + 1;
        return i > 9 ? i + "" : "0" + i;
    }

    private static String randomBirth(int minAge, int maxAge) {
        SimpleDateFormat dft = new SimpleDateFormat("yyyyMMdd");
        Calendar date = Calendar.getInstance();
        date.setTime(new Date());
        int randomDay = 365 * minAge + (new Random()).nextInt(365 * (maxAge - minAge));
        date.set(5, date.get(5) - randomDay);
        return dft.format(date.getTime());
    }

    public static String getAae(String idNo) {
        idNo = idNo.trim();
        int leh = idNo.length();
        String dates = "";
        if (leh == 15) {
            dates = "19" + idNo.substring(6, 8);
        } else {
            if (leh != 18) {
                throw new RuntimeException("年龄：" + idNo.length() + " ,idCard:" + idNo);
            }

            dates = idNo.substring(6, 10);
        }

        SimpleDateFormat df = new SimpleDateFormat("yyyy");
        String year = df.format(new Date());
        int age = Integer.parseInt(year) - Integer.parseInt(dates);
        return age + "";
    }

    public static Optional<Integer> getAgeOpt(String idNo) {
        idNo = idNo.trim();
        int leh = idNo.length();
        String dates = "";
        if (leh == 15) {
            dates = "19" + idNo.substring(6, 8);
        } else {
            if (leh != 18) {
                log.error("获取年龄年龄, 长度不符合：%s ,idCard:", idNo.length(), idNo);
                return Optional.empty();
            }

            dates = idNo.substring(6, 10);
        }

        SimpleDateFormat df = new SimpleDateFormat("yyyy");
        String year = df.format(new Date());
        int age = Integer.parseInt(year) - Integer.parseInt(dates);
        return Optional.ofNullable(age);
    }

    public static boolean getSex(String idNo) {
        idNo = idNo.trim();
        String sex;
        if (idNo.length() == 15) {
            sex = idNo.substring(14, 15);
        } else {
            if (idNo.length() != 18) {
                throw new RuntimeException("身份证号码长度无法识别：" + idNo.length() + " ,idCard:" + idNo);
            }

            sex = idNo.substring(16, 17);
        }

        return Integer.parseInt(sex) % 2 != 0;
    }

    public static String signPhoneDJ(String phone) {
        return phone.substring(0, 7) + "****";
    }

    public static String signPhoneKD(String phone) {
        return phone.substring(0, 3) + "********";
    }

    public static String signIdCardFDB(String idCard) {
        return "******" + idCard.substring(6, 14) + "**" + idCard.charAt(16) + "*";
    }

    public static String getBirthdayBySY(String idCard) {
        StringBuilder sb = new StringBuilder(idCard.substring(6, 14));
        sb.insert(4, '-');
        sb.insert(7, '-');
        return sb.toString();
    }

    public static void main(String[] args) {
        System.out.println(signPhone("13217327139"));
    }
}
