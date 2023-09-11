package me.zhengjie.service;

import me.zhengjie.access.ChannelObject;
import me.zhengjie.domain.Channel;
import me.zhengjie.domain.ChannelLog;

import java.util.Optional;

public interface IAccessService {
    Optional<ChannelObject> getChannelObject(String channelToken);


    String createChannelToken(ChannelLog channelLog, Channel channelVO);

    String createChannelTokenV2(String channelToken, ChannelLog channelLog, Channel channelVO);

}
