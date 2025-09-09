package me.zhengjie.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum HttpRespCode {

    SUCCESS(200, "操作成功"),

    FAIL(500, "操作失败"),

    WARN(600, "警告"),

    UN_AUTH(401, "未授权"),

    UN_PERMISSION(403, "无资源访问权限"),

    UN_FOUND(404, "未找到"),

    UN_PROCESS(405, "不支持"),

    UNPROCESSABLE(422, "不支持"),

    UNPROCESSABLE_PARAM(432, "参数错误"),

    SYS_ERROR(5000, "系统错误"),
    ;

    private final int code;

    private final String msg;

}
