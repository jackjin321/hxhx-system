package me.zhengjie.service;

import me.zhengjie.domain.Channel;
import me.zhengjie.domain.ChannelLog;
import me.zhengjie.result.ResultModel;
import me.zhengjie.service.dto.AppQueryCriteria;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

public interface ChannelService {

   ResultModel<Map> getChannelTokenByPlatformV3(String channelToken,String platform, String deviceId,
                                                String channelCode, String ip, String osId,
                                                String osName, HttpServletRequest request);
   ResponseEntity<Object> getChannelTokenByPlatformV1(String uuid, String browser,String platform, String deviceId,
                                                      String channelCode, String ip, String osId,
                                                      String osName, HttpServletRequest request);
   String createChannelToken(ChannelLog channelLog, Channel channelVO);
   Optional<Channel> findByUrlCodeOfVo(String channelCode);
   Channel getChannelInfo(String channelCode,String uuid);

   Object queryAll(AppQueryCriteria criteria, Pageable pageable);
   /**
    * 创建
    * @param resources /
    */
   void create(Channel resources);

   /**
    * 编辑
    * @param resources /
    */
   void update(Channel resources);

   void delete(Set<Long> ids);

   boolean checkChannelAccessibleOnMatch(Channel channelConf, String city);
   /**
    * 获取渠道投放链接
    *
    * @param channelId 渠道id
    * @return 渠道链接
    */
   String getChannelLink(Long channelId);
}
