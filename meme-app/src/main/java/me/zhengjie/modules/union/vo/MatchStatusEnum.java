package me.zhengjie.modules.union.vo;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum MatchStatusEnum {

    FAIL(0, "匹配失败"),
    SUCCESS(1, "撞库成功"),
    TIME_OUT(2, "撞库超时"),
    ERROR(3, "过程异常");


    private final Integer status;


    private final String describe;


}
