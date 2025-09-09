package me.zhengjie.modules.union.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import me.zhengjie.enums.ApiRegisterStatusEnum;

@Data
@JsonIgnoreProperties(value = {"status", "reason"})
public class H5ChannelRegisterR {
    /**
     * 状态
     */
    private ApiRegisterStatusEnum status;

    /**
     * 原因
     */
    private String reason;

    /**
     * 跳转地址
     */
    private String redirectUrl;

    /**
     * 用户ID
     */
    private Long userId;


    public boolean isSuccess() {
        return ApiRegisterStatusEnum.SUCCESS.equals(status);
    }

    public static H5ChannelRegisterR success(String redirectUrl, Long userId) {
        H5ChannelRegisterR r = new H5ChannelRegisterR();
        r.status = ApiRegisterStatusEnum.SUCCESS;
        r.redirectUrl = redirectUrl;
        r.userId = userId;
        return r;
    }

    public static H5ChannelRegisterR fail(String reason,Long userId) {
        H5ChannelRegisterR r = new H5ChannelRegisterR();
        r.status = ApiRegisterStatusEnum.FAIL;
        r.reason = reason;
        r.userId = userId;
        return r;
    }


    public static H5ChannelRegisterR error(Throwable throwable) {
        H5ChannelRegisterR r = new H5ChannelRegisterR();
        r.status = ApiRegisterStatusEnum.FAIL;
        r.reason = throwable.getMessage();
        return r;
    }
}
