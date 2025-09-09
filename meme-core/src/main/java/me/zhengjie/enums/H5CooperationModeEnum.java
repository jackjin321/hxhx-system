package me.zhengjie.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

/**
 * 合作方式枚举
 *
 * @author Eathon
 */
@Getter
@AllArgsConstructor
public enum H5CooperationModeEnum {


    NORMAL("h5", "普通贷超"),
    MATCH_LOGIN("union", "撞库联登");


    private final String code;

    private final String desc;


    public static H5CooperationModeEnum load(String code) {
        return Arrays.stream(values()).filter(e -> e.getCode().equals(code)).findFirst().orElse(null);
    }

    public static boolean needMatch(String code) {
        H5CooperationModeEnum load = load(code);
        return MATCH_LOGIN.equals(load);
    }

    public static boolean notMatch(String code) {
        return !needMatch(code);
    }


    public static boolean needLogin(String code){
        H5CooperationModeEnum load = load(code);
        return MATCH_LOGIN.equals(load);
    }
}
