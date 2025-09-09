package me.zhengjie.modules.union.vo;

import lombok.Data;

import java.io.Serializable;

@Data
public class H5ChannelRegisterBaseRequest implements Serializable {

    /**
     * 明文手机号Ï
     */
    private String phone;

    /**
     * 城市字段
     */
    private String city;

    /**
     * 渠道请求进件ID
     */
    private String channelRequestId;

}
