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
public enum ResultStatusEnum {

    //1 成功, 2 失败, 0 请求异常

    SUCCESS(1, "成功"),
    FAIL(2, "失败"),
    ExceptionM(0, "请求异常");


    private final Integer code;

    private final String desc;


    public static ResultStatusEnum load(Integer code) {
        return Arrays.stream(values()).filter(e -> e.getCode().equals(code)).findFirst().orElse(null);
    }

}
