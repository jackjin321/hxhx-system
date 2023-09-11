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
package me.zhengjie.service.impl;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.RandomUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.zhengjie.access.ChannelObject;
import me.zhengjie.domain.ChannelLog;
import me.zhengjie.domain.HxUser;
import me.zhengjie.exception.BadRequestException;
import me.zhengjie.exception.EntityExistException;
import me.zhengjie.exception.EntityNotFoundException;
import me.zhengjie.repository.ChannelLogRepository;
import me.zhengjie.repository.HxUserRepository;
import me.zhengjie.service.HxUserService;
import me.zhengjie.service.IAccessService;
import me.zhengjie.service.dto.*;
import me.zhengjie.service.mapstruct.HxUserLoginMapper;
import me.zhengjie.service.mapstruct.HxUserMapper;
import me.zhengjie.service.mapstruct.HxUserV2Mapper;
import me.zhengjie.utils.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author me zhengjie
 * @website
 * @description 服务实现
 * @date 2023-06-12
 **/
@Slf4j
@Service
@RequiredArgsConstructor
public class HxUserServiceImpl implements HxUserService {
    private final PasswordEncoder passwordEncoder;
    private final HxUserRepository hxUserRepository;
    private final HxUserMapper hxUserMapper;
    private final HxUserV2Mapper hxUserV2Mapper;
    private final HxUserLoginMapper hxUserLoginMapper;
    private final IAccessService accessService;
    private final ChannelLogRepository channelLogRepository;

    @Override
    public Map<String, Object> queryAll(HxUserQueryCriteria criteria, Pageable pageable) {
        Page<HxUser> page = hxUserRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root, criteria, criteriaBuilder), pageable);
        return PageUtil.toPage(page.map(hxUserMapper::toDto));
    }

    @Override
    public List<HxUserDto> queryAll(HxUserQueryCriteria criteria) {
        return hxUserMapper.toDto(hxUserRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root, criteria, criteriaBuilder)));
    }

    @Override
    @Transactional
    public HxUserDto findById(Long userId) {
        HxUser hxUser = hxUserRepository.findById(userId).orElseGet(HxUser::new);
        ValidationUtil.isNull(hxUser.getUserId(), "HxUser", "userId", userId);
        HxUserDto hxUserDto = hxUserMapper.toDto(hxUser);
        return hxUserDto;
    }

    @Override
    @Transactional
    public HxUserDtoV2 findByIdV2(Long userId) {
        HxUser hxUser = hxUserRepository.findById(userId).orElseGet(HxUser::new);
        ValidationUtil.isNull(hxUser.getUserId(), "HxUser", "userId", userId);
        HxUserDtoV2 hxUserDto = hxUserV2Mapper.toDto(hxUser);
        //hxUserDto.setCycle("10");
        return hxUserDto;
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(HxUser resources) {
        HxUser hxUser = hxUserRepository.findById(resources.getUserId()).orElseGet(HxUser::new);
        ValidationUtil.isNull(hxUser.getUserId(), "HxUser", "id", resources.getUserId());
        HxUser hxUser1 = null;
        hxUser1 = hxUserRepository.findByUsername(resources.getUsername());
        if (hxUser1 != null && !hxUser1.getUserId().equals(hxUser.getUserId())) {
            throw new EntityExistException(HxUser.class, "username", resources.getUsername());
        }
        hxUser1 = hxUserRepository.findByPhone(resources.getPhone());
        if (hxUser1 != null && !hxUser1.getUserId().equals(hxUser.getUserId())) {
            throw new EntityExistException(HxUser.class, "phone", resources.getPhone());
        }
        hxUser.copy(resources);
        hxUserRepository.save(hxUser);
    }


    @Override
    public HxUserLoginDto getLoginData(String phone) {
        HxUser user = hxUserRepository.findByPhone(phone);
        if (user == null) {
            throw new EntityNotFoundException(HxUser.class, "phone", phone);
        } else {
            return hxUserLoginMapper.toDto(user);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String createUser(String phone, String password, String uuid, String channelCode) {
        if (ObjectUtil.isAllEmpty(channelCode)) {
            channelCode = "88888888";
        }
//        HxChannel channel = hxChannelRepository.findByChannelCode(channelCode);
//        if (ObjectUtil.isAllEmpty(channel)) {
//            throw new BadRequestException("123 is error");
//        }
        ChannelObject channelObject = null;
        if (ObjectUtil.isNotEmpty(uuid)) {
            //根据token信息获取渠道信息，最近登录渠道信息
            Optional<ChannelObject> optional = accessService.getChannelObject(uuid);
            if (optional.isPresent()) {
                channelObject = optional.get();
            } else {
                //异常
            }
        } else {
            //异常
        }
        if(ObjectUtil.isAllEmpty(channelObject)){
            throw new BadRequestException("请求异常");
        }
        //ChannelLog channelLog = new ChannelLog();
        //channelLog.setId(channelObject.getChannelLogId());
        HxUser user = hxUserRepository.findByPhone(phone);
        if (user == null) {

            HxUser hxUser = new HxUser();
            hxUser.setChannelId(channelObject.getChannelId());//注册渠道
            hxUser.setChannelName(channelObject.getChannelName());//注册渠道
            hxUser.setUsername(phone);
            hxUser.setPhone(phone);
            hxUser.setPassword(passwordEncoder.encode(password));
            hxUser.setEnabled(true);
            hxUser.setRegisterTime(new Date());
            hxUser.setLastLoginTime(new Date());
            hxUser.setLastOpTime(new Date());
            //分配客服

            hxUserRepository.save(hxUser);

//            channelLog.setUserId(hxUser.getUserId());
//            channelLog.setIsRegister(true);
//            channelLog.setIsLogin(true);
            channelLogRepository.updateSubCntById(hxUser.getUserId(),true,true, channelObject.getChannelLogId());
            //新用户
            //throw new EntityNotFoundException(HxUser.class, "phone", phone);
//            return AccessEventEnum.REGISTER.getCode();
        } else {
            //老用户
//            channelLog.setUserId(user.getUserId());
//            channelLog.setIsRegister(false);
//            channelLog.setIsLogin(true);
//            channelLogRepository.save(channelLog);
            channelLogRepository.updateSubCntById(user.getUserId(),false,true, channelObject.getChannelLogId());

        }
        //更新日志


        return channelObject.getProcess();
    }

    @Override
//    @Cacheable(key = "'hxauth:' + #p0.id")
    public List<HxAuthorityDto> mapToGrantedAuthorities(HxUserDto user) {
        Set<String> permissions = new HashSet<>();
        permissions.add("appFind");
//        // 如果是管理员直接返回
//        if (user.getIsAdmin()) {
//            permissions.add("admin");
//            return permissions.stream().map(AuthorityDto::new)
//                    .collect(Collectors.toList());
//        }
//        //Set<Role> roles = roleRepository.findByUserId(user.getId());
//        permissions = roles.stream().flatMap(role -> role.getMenus().stream())
//                .map(Menu::getPermission)
//                .filter(StringUtils::isNotBlank).collect(Collectors.toSet());
        return permissions.stream().map(HxAuthorityDto::new)
                .collect(Collectors.toList());
    }

}