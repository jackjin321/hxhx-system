package me.zhengjie.modules.union.vo;

import lombok.Data;
import me.zhengjie.enums.HttpRespCode;

import java.io.Serializable;
import java.util.function.BooleanSupplier;
import java.util.function.IntSupplier;

@Data
public class ApiResult<T> implements Serializable {

    private static final long serialVersionUID = 1L;

    public static final int CODE_SUCCESS = 0;


    public ApiResult() {
    }

    public ApiResult(int code, String msg, T data) {
        this.code = code;
        this.message = msg;
        this.data = data;
    }
    /**
     * 响应码
     */
    private int code;
    /**
     * 响应消息
     */
    private String message;
    /**
     * 数据体
     */
    private T data;

    public static <T> ApiResult<T> ok() {
        return instance(null, CODE_SUCCESS, HttpRespCode.SUCCESS.getMsg());
    }

    public static <T> ApiResult<T> ok(T data) {
        return instance(data, CODE_SUCCESS, HttpRespCode.SUCCESS.getMsg());
    }

    public static <T> ApiResult<T> ok(String msg) {
        return instance(null, CODE_SUCCESS, msg);
    }

    public static <T> ApiResult<T> ok(String msg, T data) {
        return instance(data, CODE_SUCCESS, msg);
    }

    public static <T> ApiResult<T> fail() {
        return instance(null, HttpRespCode.FAIL.getCode(), HttpRespCode.FAIL.getMsg());
    }

    public static <T> ApiResult<T> fail(String msg) {
        return instance(null, HttpRespCode.FAIL.getCode(), msg);
    }

    public static <T> ApiResult<T> fail(T data) {
        return instance(data, HttpRespCode.FAIL.getCode(), HttpRespCode.FAIL.getMsg());
    }

    public static <T> ApiResult<T> fail(String msg, T data) {
        return instance(data, HttpRespCode.FAIL.getCode(), msg);
    }

    public static <T> ApiResult<T> fail(int code, String msg) {
        return instance(null, code, msg);
    }

    public static <T> ApiResult<T> fail(HttpRespCode respCode) {
        return fail(respCode.getCode(), respCode.getMsg());
    }

    /**
     * 当处理结果为true时返回成功, 否则失败
     *
     * @param supplier 只要提供一个布尔值, 如果为true则返回成功, 否则返回失败
     * @param <T>      泛型
     * @return 返回成功
     */
    public static <T> ApiResult<T> to(BooleanSupplier supplier) {
        if (Boolean.TRUE.equals(supplier.getAsBoolean())) {
            return ok();
        }
        return fail();
    }

    /**
     * 数据库操作
     *
     * @param effectRowsSupplier 提供数据库操作, 并返回整数, 该整数为操作成功影响的行数
     * @param <T>                泛型
     * @return 操作成功
     */
    public static <T> ApiResult<T> dbR(IntSupplier effectRowsSupplier) {
        if (effectRowsSupplier.getAsInt() <= 0) {
            return fail();
        }
        return ok();
    }

    private static <T> ApiResult<T> instance(T data, int code, String msg) {
        return new ApiResult<>(code, msg, data);
    }


    public boolean isSuccess() {
        return CODE_SUCCESS == this.code;
    }
}
