package me.zhengjie.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.alibaba.fastjson.JSONObject;
import lombok.RequiredArgsConstructor;
import me.zhengjie.exception.BadRequestException;
import me.zhengjie.access.ChannelObject;
import me.zhengjie.domain.Channel;
import me.zhengjie.domain.ChannelLog;
import me.zhengjie.repository.ChannelLogRepository;
import me.zhengjie.repository.ChannelRepository;
import me.zhengjie.result.ResultBuilder;
import me.zhengjie.result.ResultModel;
import me.zhengjie.service.ChannelService;
import me.zhengjie.service.IAccessService;
import me.zhengjie.utils.DESUtil;
import me.zhengjie.service.dto.AppQueryCriteria;
import me.zhengjie.utils.PageUtil;
import me.zhengjie.utils.QueryHelp;
import me.zhengjie.utils.ValidationUtil;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class ChannelServiceImpl implements ChannelService {

    private final ChannelRepository channelRepository;
    private final ChannelLogRepository channelLogRepository;
    private final String DES_KEY = "5lp1YmsYp+Q=";
    private final IAccessService accessService;

    @Override
    public Channel getChannelInfo(String channelCode, String uuid) {
//        return null;
        if (ObjectUtil.isAllEmpty(uuid)) {
            //根据token信息获取渠道信息，最近登录渠道信息
            Optional<ChannelObject> optional = accessService.getChannelObject(uuid);
            if (optional.isPresent()) {
                //JSONObject simpleMap = new JSONObject();
                //simpleMap.put("token", uuid);
                Channel channel = channelRepository.getById(optional.get().getChannelId());
                return channel;
            } else {
                return null;
            }
        } else {
            Optional<Channel> optionalChannel = this.findByUrlCodeOfVo(channelCode);
            if (!optionalChannel.isPresent()) {
                return null;
            } else {
                return optionalChannel.get();
            }
        }
    }

    @Override
    public String createChannelToken(ChannelLog channelLog, Channel channelVO) {
        return null;
    }

    @Override
    @Transactional
    public ResultModel<Map> getChannelTokenByPlatformV3(String channelToken, String platform, String deviceId, String channelCode,
                                                        String realIp, String osId, String osName, HttpServletRequest request) {
        Optional<Channel> optionalChannel = this.findByUrlCodeOfVo(channelCode);
        if (!optionalChannel.isPresent()) {
            return ResultBuilder.fail("urlCode not must be null");
        }
        String channelTokenHeader = request.getHeader("channel-token");

        if (ObjectUtil.isAllNotEmpty(channelTokenHeader)) {
            //根据token信息获取渠道信息，最近登录渠道信息
            Optional<ChannelObject> optional = accessService.getChannelObject(channelTokenHeader);
            if (optional.isPresent()) {
                JSONObject simpleMap = new JSONObject();
                simpleMap.put("token", channelTokenHeader);
                //simpleMap.put("homePopupTime", 600);//单位秒
                //simpleMap.put("minePopupTime", 600);//单位秒
                Channel channel = channelRepository.getById(optional.get().getChannelId());
                if (ObjectUtil.isAllNotEmpty(channel)) {
                    simpleMap.put("process", channel.getProcess());
                } else {
                    simpleMap.put("process", "waitForOnShelves");
                }

                ChannelLog channelLog = new ChannelLog();
                channelLog.setChannelId(channel.getId());//登录渠道
                channelLog.setChannelName(channel.getChannelName());//登录渠道

                channelLog.setUuid(channelToken);
                channelLog.setAccessIp(realIp);

                channelLog.setIsLogin(false);
                channelLog.setIsRegister(false);

                channelLog.setPlatform(platform);//新增
                channelLog.setDeviceId(deviceId);//新增

                channelLog.setOsId(osId);//app的osId
                channelLog.setOsName(osName);//
                channelLogRepository.save(channelLog);

                return ResultBuilder.data(simpleMap);
            }
        }
        Channel channelVO = optionalChannel.get();

        ChannelLog channelLog = new ChannelLog();
        channelLog.setChannelId(channelVO.getId());//登录渠道
        channelLog.setChannelName(channelVO.getChannelName());//登录渠道

        channelLog.setUuid(channelToken);
        channelLog.setAccessIp(realIp);

        channelLog.setIsLogin(false);
        channelLog.setIsRegister(false);

        channelLog.setPlatform(platform);//新增
        channelLog.setDeviceId(deviceId);//新增

        channelLog.setOsId(osId);//app的osId
        channelLog.setOsName(osName);//
        channelLogRepository.save(channelLog);

        //渠道访问日志放到redis
//        String channelToken = accessService.createChannelToken(channelLog, channelVO);
        String abc = accessService.createChannelTokenV2(channelToken, channelLog, channelVO);

        JSONObject mapJson = new JSONObject();
        mapJson.put("process", channelVO.getProcess());
        mapJson.put("token", abc);
        return ResultBuilder.data(mapJson);
    }

    @Override
    @Transactional
    public ResponseEntity<Object> getChannelTokenByPlatformV1(String uuid, String browser,String platform, String deviceId, String channelCode,
                                                              String realIp, String osId, String osName, HttpServletRequest request) {
        Optional<Channel> optionalChannel = findByUrlCodeOfVo(channelCode);
        if (!optionalChannel.isPresent()) {
//            return ResultBuilder.fail("urlCode not must be null");
            return new ResponseEntity<>(ResultBuilder.fail("urlCode not must be null"), HttpStatus.OK);
        }
        //String channelTokenHeader = request.getHeader("channel-token");

        if (ObjectUtil.isAllEmpty(uuid)) {
            //根据token信息获取渠道信息，最近登录渠道信息
            Optional<ChannelObject> optional = accessService.getChannelObject(uuid);
            if (optional.isPresent()) {
                JSONObject simpleMap = new JSONObject();
                simpleMap.put("token", uuid);
                //simpleMap.put("homePopupTime", 600);//单位秒
                //simpleMap.put("minePopupTime", 600);//单位秒
                Channel channel = channelRepository.getById(optional.get().getChannelId());
                if (ObjectUtil.isAllNotEmpty(channel)) {
                    simpleMap.put("process", channel.getProcess());
                } else {
                    simpleMap.put("process", "waitForOnShelves");
                }

                ChannelLog channelLog = new ChannelLog();
                channelLog.setChannelId(channel.getId());//登录渠道
                channelLog.setChannelName(channel.getChannelName());//登录渠道
                channelLog.setRegChannelId(channel.getId());//登录渠道
                channelLog.setRegChannelName(channel.getChannelName());//登录渠道

                channelLog.setUuid(uuid);
                channelLog.setAccessIp(realIp);

                channelLog.setIsLogin(false);
                channelLog.setIsRegister(false);

                channelLog.setPlatform(platform);//新增
                channelLog.setDeviceId(deviceId);//新增

                channelLog.setOsId(osId);//app的osId
                channelLog.setOsName(osName);//
                channelLogRepository.save(channelLog);
                HttpHeaders responseHeaders = new HttpHeaders();
                responseHeaders.set("process", simpleMap.getString("process"));
                return new ResponseEntity<>(ResultBuilder.data(simpleMap), responseHeaders, HttpStatus.OK);

//                return ResultBuilder.data(simpleMap);
            }
        }
        Channel channel = optionalChannel.get();

        ChannelLog channelLog = new ChannelLog();
        channelLog.setChannelId(channel.getId());//登录渠道
        channelLog.setChannelName(channel.getChannelName());//登录渠道
        channelLog.setRegChannelId(channel.getId());//登录渠道
        channelLog.setRegChannelName(channel.getChannelName());//登录渠道
        channelLog.setUuid(uuid);
        channelLog.setAccessIp(realIp);

        channelLog.setIsLogin(false);
        channelLog.setIsRegister(false);

        channelLog.setPlatform(platform);//新增
        channelLog.setDeviceId(deviceId);//新增

        channelLog.setOsId(osId);//app的osId
        channelLog.setOsName(osName);//
        channelLogRepository.save(channelLog);

        //渠道访问日志放到redis
//        String channelToken = accessService.createChannelToken(channelLog, channelVO);
        String abc = accessService.createChannelTokenV2(uuid, channelLog, channel);

        JSONObject mapJson = new JSONObject();
        mapJson.put("process", channel.getProcess());
        mapJson.put("token", abc);
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.set("process", channel.getProcess());
        return new ResponseEntity<>(ResultBuilder.data(mapJson), responseHeaders, HttpStatus.OK);
//        return ResultBuilder.data(mapJson);
    }

    @Override
    public Optional findByUrlCodeOfVo(String channelCode) {
        try {
            if (ObjectUtil.isNotEmpty(channelCode) && !"{}".equals(channelCode)) {
                Channel channel = new Channel();
                channel.setChannelCode(channelCode);
                Example<Channel> example = Example.of(channel);
                return channelRepository.findOne(example);
            } else {
                return Optional.empty();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            return Optional.empty();
        }
    }

    @Override
    public Object queryAll(AppQueryCriteria criteria, Pageable pageable) {
        Page<Channel> page = channelRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root, criteria, criteriaBuilder), pageable);
        return PageUtil.toPage(page);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void create(Channel resources) {
        System.out.println(resources.toString());
        channelRepository.save(resources);
        String id = Long.toString(resources.getId());
        Optional<String> optionalS = DESUtil.encrypt(id, DES_KEY);
        if (!optionalS.isPresent()) {
            throw new BadRequestException("encrypt exception");
        }
        resources.setChannelCode(optionalS.get());
        channelRepository.save(resources);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(Channel resources) {
        Channel channel = channelRepository.findById(resources.getId()).orElseGet(Channel::new);
        ValidationUtil.isNull(channel.getId(), "Channel", "id", resources.getId());
        channel.copy(resources);
        channelRepository.save(channel);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Set<Long> ids) {
        for (Long id : ids) {
            channelRepository.deleteById(id);
        }
    }
}

