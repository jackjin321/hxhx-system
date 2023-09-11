/*
 *  Copyright 2019-2020 Zheng Jie
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package me.zhengjie.modules.security.service;

import lombok.extern.slf4j.Slf4j;
import me.zhengjie.modules.security.config.bean.SecurityProperties;
import me.zhengjie.service.dto.HxJwtUserDto;
import me.zhengjie.service.dto.HxOnlineUserDto;
import me.zhengjie.utils.*;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * @author Zheng Jie
 * @date 2019年10月26日21:56:27
 */
@Service
@Slf4j
public class OnlineUserService {

    private final SecurityProperties properties;
    private final RedisUtils redisUtils;

    public OnlineUserService(SecurityProperties properties, RedisUtils redisUtils) {
        this.properties = properties;
        this.redisUtils = redisUtils;
    }

    /**
     * 保存在线用户信息
     *
     * @param hxJwtUserDto /
     * @param token      /
     * @param request    /
     */
    public void save(HxJwtUserDto hxJwtUserDto, String token, HttpServletRequest request) {
        //String dept = jwtUserDto.getUser().getDept().getName();
        String ip = StringUtils.getIp(request);
        String browser = StringUtils.getBrowser(request);
        String address = StringUtils.getCityInfo(ip);
        HxOnlineUserDto hxOnlineUserDto = null;
        try {
            hxOnlineUserDto = new HxOnlineUserDto(hxJwtUserDto.getUsername(), hxJwtUserDto.getUser().getNickName(), browser, ip, address, EncryptUtils.desEncrypt(token), new Date());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        redisUtils.set(properties.getOnlineKey() + token, hxOnlineUserDto, properties.getTokenValidityInSeconds() / 1000);
    }

    /**
     * 查询全部数据
     *
     * @param filter   /
     * @param pageable /
     * @return /
     */
    public Map<String, Object> getAll(String filter, Pageable pageable) {
        List<HxOnlineUserDto> hxOnlineUserDtos = getAll(filter);
        return PageUtil.toPage(
                PageUtil.toPage(pageable.getPageNumber(), pageable.getPageSize(), hxOnlineUserDtos),
                hxOnlineUserDtos.size()
        );
    }

    /**
     * 查询全部数据，不分页
     *
     * @param filter /
     * @return /
     */
    public List<HxOnlineUserDto> getAll(String filter) {
        List<String> keys = redisUtils.scan(properties.getOnlineKey() + "*");
        Collections.reverse(keys);
        List<HxOnlineUserDto> hxOnlineUserDtos = new ArrayList<>();
        for (String key : keys) {
            HxOnlineUserDto hxOnlineUserDto = (HxOnlineUserDto) redisUtils.get(key);
            if (StringUtils.isNotBlank(filter)) {
                if (hxOnlineUserDto.toString().contains(filter)) {
                    hxOnlineUserDtos.add(hxOnlineUserDto);
                }
            } else {
                hxOnlineUserDtos.add(hxOnlineUserDto);
            }
        }
        hxOnlineUserDtos.sort((o1, o2) -> o2.getLoginTime().compareTo(o1.getLoginTime()));
        return hxOnlineUserDtos;
    }

    /**
     * 踢出用户
     *
     * @param key /
     */
    public void kickOut(String key) {
        key = properties.getOnlineKey() + key;
        redisUtils.del(key);
    }

    /**
     * 退出登录
     *
     * @param token /
     */
    public void logout(String token) {
        String key = properties.getOnlineKey() + token;
        redisUtils.del(key);
    }


    /**
     * 查询用户
     *
     * @param key /
     * @return /
     */
    public HxOnlineUserDto getOne(String key) {
        return (HxOnlineUserDto) redisUtils.get(key);
    }

    /**
     * 检测用户是否在之前已经登录，已经登录踢下线
     *
     * @param userName 用户名
     */
    public void checkLoginOnUser(String userName, String igoreToken) {
        List<HxOnlineUserDto> hxOnlineUserDtos = getAll(userName);
        if (hxOnlineUserDtos == null || hxOnlineUserDtos.isEmpty()) {
            return;
        }
        for (HxOnlineUserDto hxOnlineUserDto : hxOnlineUserDtos) {
            if (hxOnlineUserDto.getUserName().equals(userName)) {
                try {
                    String token = EncryptUtils.desDecrypt(hxOnlineUserDto.getKey());
                    if (StringUtils.isNotBlank(igoreToken) && !igoreToken.equals(token)) {
                        this.kickOut(token);
                    } else if (StringUtils.isBlank(igoreToken)) {
                        this.kickOut(token);
                    }
                } catch (Exception e) {
                    log.error("checkUser is error", e);
                }
            }
        }
    }

    /**
     * 根据用户名强退用户
     *
     * @param username /
     */
    @Async
    public void kickOutForUsername(String username) throws Exception {
        List<HxOnlineUserDto> onlineUsers = getAll(username);
        for (HxOnlineUserDto onlineUser : onlineUsers) {
            if (onlineUser.getUserName().equals(username)) {
                String token = EncryptUtils.desDecrypt(onlineUser.getKey());
                kickOut(token);
            }
        }
    }
}
