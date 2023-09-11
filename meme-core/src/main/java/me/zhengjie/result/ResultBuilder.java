//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package me.zhengjie.result;

import org.springframework.http.HttpStatus;

import java.util.HashMap;
import java.util.Map;

public class ResultBuilder {
    public ResultBuilder() {
    }

    public static <T> ResultModel<T> fail(String msg) {
        return new ResultModel(1, msg);
    }

    public static <T> ResultModel<T> limit() {
        return new ResultModel(9, "Access denied, maximum number of times reached");
    }

    public static <T> ResultModel<T> error(String msg) {
        return new ResultModel(2, msg);
    }

    public static <T> ResultModel<T> author() {
        return new ResultModel(3, "token is fail");
    }

    public static <T> ResultModel<T> authorInfo() {
        return new ResultModel(4, "Missing user information");
    }

    public static <T> ResultModel<T> okAuthor() {
        return new ResultModel(5, "User already registered");
    }

    public static <T> ResultModel<T> clientFail(HttpStatus status) {
        return new ResultModel(6, String.format("Client fail ,status is %s", status.value()));
    }

    public static <T> ResultModel<T> notVIP() {
        return new ResultModel(7, String.format("Member not is vip "));
    }

    public static <T> ResultModel<T> insufficient() {
        return new ResultModel(8, String.format("Insufficient use times"));
    }

    public static <T> ResultModel<T> risk() {
        return new ResultModel(-1, "risk ip");
    }

    public static <T> ResultModel<T> data(T data) {
        ResultModel<T> resultModel = new ResultModel(0, "success ");
        return resultModel.flashData(data);
    }

    public static <T> ResultModel<T> dataPage(T data, Long count) {
        ResultModel<T> resultModel = new ResultModel(0, count, "success ");
        return resultModel.flashDataV2(data, count);
    }

    public static ResultModel<Map> data(String key, Object val) {
        Map<String, Object> map = new HashMap();
        map.put(key, val);
        ResultModel<Map> resultModel = new ResultModel(0, "success ");
        return resultModel.flashData(map);
    }

    public static ResultModel<Map> outOfStatus(Boolean flag, String msg) {
        return flag ? new ResultModel(0, "SUCCESS") : new ResultModel(1, msg);
    }

    public static <T> ResultModel<T> ok(String msg) {
        return new ResultModel(0, msg);
    }

    public static <T> ResultModel<T> ok() {
        return new ResultModel(0, "success ");
    }
}
