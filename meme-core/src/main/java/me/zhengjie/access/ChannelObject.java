package me.zhengjie.access;

import lombok.Data;
import lombok.experimental.Accessors;
import me.zhengjie.domain.Channel;
import me.zhengjie.domain.ChannelLog;

import java.io.Serializable;

@Data
@Accessors(chain = true)
public class ChannelObject implements Serializable {

    private String platform;
    private String deviceId;
    private String osId;
    private String osName;
    private Long channelLogId;
    private String accessIp;
    private Long channelId;
    private String channelName;
    /**
     * 注册来源
     */
    private String registerName;

    private Boolean status;
    private String process;

    public ChannelObject() {
    }

    public ChannelObject(ChannelLog channelLog, Channel channelVO) {
        this.channelId = channelLog.getChannelId();
        this.channelLogId = channelLog.getId();
        this.accessIp = channelLog.getAccessIp();
        this.osId = channelLog.getOsId();
        this.osName = channelLog.getOsName();
        this.platform = channelLog.getPlatform();
        this.deviceId = channelLog.getDeviceId();
        this.channelName = channelLog.getChannelName();
        this.process = channelVO.getProcess();
    }
}
