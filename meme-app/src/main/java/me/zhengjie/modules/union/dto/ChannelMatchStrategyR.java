package me.zhengjie.modules.union.dto;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author evans
 * @description
 * @date 2024/5/9
 */
@Data
@Accessors(chain = true)
public class ChannelMatchStrategyR<T>  {

    /**
     * 是否准入
     */
    private boolean success;

    /**
     * 消息
     */
    private String msg;
    /**
     * 附带数据
     */
    private T data;


    public static <T> ChannelMatchStrategyR<T> success(T data) {
        ChannelMatchStrategyR<T> channelMatchR = new ChannelMatchStrategyR<>();
        channelMatchR.setSuccess(true);
        channelMatchR.setData(data);
        return channelMatchR;
    }

    public static <T> ChannelMatchStrategyR<T> success() {
        ChannelMatchStrategyR<T> channelMatchR = new ChannelMatchStrategyR<>();
        channelMatchR.setSuccess(true);
        return channelMatchR;
    }

    public static <T> ChannelMatchStrategyR<T> fail(String msg) {
        ChannelMatchStrategyR<T> matchAccessRespVO = new ChannelMatchStrategyR<>();
        matchAccessRespVO.setSuccess(false);
        matchAccessRespVO.setMsg(msg);
        return matchAccessRespVO;
    }

    public static <T> ChannelMatchStrategyR<T> fail(T data) {
        ChannelMatchStrategyR<T> matchAccessRespVO = new ChannelMatchStrategyR<>();
        matchAccessRespVO.setSuccess(false);
        matchAccessRespVO.setMsg("撞库失败");
        matchAccessRespVO.setData(data);
        return matchAccessRespVO;
    }

}

