package me.zhengjie.access;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;

public class AppRedisKey {

    /**
     * 信息流用户登录信息redis key
     *
     * @param license
     * @return
     */
    public static String loginInfoKey(String license) {
        return String.format("information::app::info::%s", license);
    }

    public static String loginUionInfoKey(String action) {
        return String.format("information::union::action::%s", action);
    }

    public static String loginUnionKey(String phone, String channelCode) {
        return String.format("loginUnion::%s::%s", phone, channelCode);
    }

    /**
     * 信息流用户登录token值
     *
     * @param phone
     * @return
     */
    public static String loginLicenseKey(String phone) {
        return String.format("information::app::license::%s", phone);
    }

    /**
     * channel token
     *
     * @return
     */
    public static String channelTokenKey(String channelToken) {
        return String.format("channel-token::%s::%s", DateUtil.format(DateUtil.date(), DatePattern.NORM_DATE_PATTERN), channelToken);
    }

    /**
     * 注册验证码发送次数
     *
     * @param phone
     * @return
     */
    public static String regCodeCountKey(String phone) {
        return String.format("reg_code_count::%s", phone);
    }

    /**
     * 注册短信rdis key
     *
     * @param phone
     * @return
     */
    public static String regCodeKey(String phone) {
        return String.format("reg_code::%s", phone);
    }


    /**
     * 城市列表
     */
    public static String cityListKey() {
        return "city_list";
    }
}
