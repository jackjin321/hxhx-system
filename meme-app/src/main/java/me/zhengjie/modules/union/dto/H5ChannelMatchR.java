package me.zhengjie.modules.union.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author evans
 * @description
 * @date 2024/5/9
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
public class H5ChannelMatchR<T> extends RequestTxId {

    /**
     * 是否准入
     */
    private boolean access;
    /**
     * 消息
     */
    private String msg;
    /**
     * 准入凭证
     */
    private String accessTicket;


    private T data;


    public static <T> H5ChannelMatchR<T> success(T data) {
        H5ChannelMatchR<T> channelMatchR = new H5ChannelMatchR<>();
        channelMatchR.setRequestTxId();
        channelMatchR.setAccess(true);
        channelMatchR.setData(data);
        return channelMatchR;
    }


    public static H5ChannelMatchR<?> success(String accessTicket) {
        H5ChannelMatchR<?> matchAccessRespVO = new H5ChannelMatchR<>().setAccess(true).setAccessTicket(accessTicket);
        matchAccessRespVO.setRequestTxId();
        return matchAccessRespVO;
    }

    public static <T> H5ChannelMatchR<T> success(T data, String accessTicket) {
        H5ChannelMatchR<T> matchAccessRespVO = new H5ChannelMatchR<>();
        matchAccessRespVO.setAccess(true);
        matchAccessRespVO.setAccessTicket(accessTicket);
        matchAccessRespVO.setRequestTxId();
        matchAccessRespVO.setData(data);
        return matchAccessRespVO;
    }

    public static H5ChannelMatchR<?> fail() {
        H5ChannelMatchR<?> matchAccessRespVO = new H5ChannelMatchR<>().setAccess(false);
        matchAccessRespVO.setRequestTxId();
        return matchAccessRespVO;
    }

    public static H5ChannelMatchR<?> fail(String msg) {
        H5ChannelMatchR<?> matchAccessRespVO = new H5ChannelMatchR<>().setAccess(false);
        matchAccessRespVO.setRequestTxId();
        matchAccessRespVO.setMsg(msg);
        return matchAccessRespVO;
    }


    public static <T> H5ChannelMatchR<T> fail(String msg, T data) {
        H5ChannelMatchR<T> matchAccessRespVO = new H5ChannelMatchR<>();
        matchAccessRespVO.setAccess(false);
        matchAccessRespVO.setRequestTxId();
        matchAccessRespVO.setMsg(msg);
        matchAccessRespVO.setData(data);
        return matchAccessRespVO;
    }


    public static H5ChannelMatchR<?> fail(Throwable throwable) {
        H5ChannelMatchR<?> matchAccessRespVO = new H5ChannelMatchR<>().setAccess(false);
        matchAccessRespVO.setRequestTxId();
        matchAccessRespVO.setMsg(throwable.getMessage());
        return matchAccessRespVO;
    }
}

