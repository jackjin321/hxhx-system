package me.zhengjie.service.impl;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.RandomUtil;
import com.alibaba.fastjson.JSONObject;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.zhengjie.access.ChannelObject;
import me.zhengjie.access.RedisCacheKey;
import me.zhengjie.domain.Channel;
import me.zhengjie.domain.ChannelLog;
import me.zhengjie.repository.ChannelRepository;
import me.zhengjie.result.ResultBuilder;
import me.zhengjie.service.ChannelService;
import me.zhengjie.service.IAccessService;
import me.zhengjie.utils.RedisUtils;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.concurrent.TimeUnit;


@Service
@Slf4j
@RequiredArgsConstructor
public class AccessServiceImpl implements IAccessService {

    //    @Autowired
//    private StringRedisTemplate stringRedisTemplate;
    private final RedisUtils redisUtils;



    @Override
    public Optional<ChannelObject> getChannelObject(String channelToken) {
        Object cacheObject = redisUtils.get(RedisCacheKey.channelObjectKey(channelToken));
        ChannelObject channelObject = null;
        if (ObjectUtil.isAllNotEmpty(cacheObject)) {
            channelObject = JSONObject.parseObject(cacheObject.toString(), ChannelObject.class);
        }

        return Optional.ofNullable(channelObject);
    }

    @Override
    public String createChannelToken(ChannelLog channelLog, Channel channelVO) {
        ChannelObject channelObject = new ChannelObject(channelLog, channelVO);
        String channelToken = RandomUtil.randomString(16);
        String channelTokenKey = RedisCacheKey.channelObjectKey(channelToken);
        String orderJSon = JSONObject.toJSONString(channelObject);
        redisUtils.set(channelTokenKey, orderJSon, 1, TimeUnit.DAYS);

        return channelToken;
    }

    @Override
    public String createChannelTokenV2(String channelToken, ChannelLog channelLog, Channel channelVO) {
        ChannelObject channelObject = new ChannelObject(channelLog, channelVO);
        //String channelToken = RandomUtil.randomString(16);
        String channelTokenKey = RedisCacheKey.channelObjectKey(channelToken);
        String orderJSon = JSONObject.toJSONString(channelObject);
        redisUtils.set(channelTokenKey, orderJSon, 1, TimeUnit.DAYS);

        return channelToken;
    }

}
