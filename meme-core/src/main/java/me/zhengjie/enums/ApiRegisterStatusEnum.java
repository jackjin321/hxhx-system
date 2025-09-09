package me.zhengjie.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

@Getter
@AllArgsConstructor
public enum ApiRegisterStatusEnum {

    /**
     * 联登结果
     * 0:注册成功成功
     * 1:注册失败
     * 2:注册超时
     */
    SUCCESS(1, "注册成功"),
    FAIL(0, "注册失败"),
    TIMEOUT(2, "注册超时"),
    ;

    private final Integer code;
    private final String msg;

    public static ApiRegisterStatusEnum getByCode(Integer code) {
        return Arrays.stream(values()).filter(e -> e.getCode().equals(code)).findFirst().orElse(null);
    }


}
