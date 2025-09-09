package me.zhengjie.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

/**
 * 起禁用枚举
 *
 * @author Eathon
 */

@Getter
@AllArgsConstructor
public enum AbleEnum {

    DISABLE(0),

    ENABLE(1);


    private final Integer code;


    public static AbleEnum get(Integer code) {
        return Arrays.stream(values()).filter(ableEnum -> ableEnum.getCode().equals(code)).findFirst().orElse(DISABLE);
    }

    public static boolean isEnable(Integer code) {
        return get(code).equals(ENABLE);
    }

    public static boolean isDisable(Integer code) {
        return get(code).equals(DISABLE);
    }

    public static AbleEnum flip(Integer status) {
        return DISABLE.equals(get(status)) ? ENABLE : DISABLE;
    }
}
