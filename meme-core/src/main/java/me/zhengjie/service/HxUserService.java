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
package me.zhengjie.service;

import me.zhengjie.domain.HxUser;
import me.zhengjie.service.dto.HxAuthorityDto;
import me.zhengjie.service.dto.HxUserDto;
import me.zhengjie.service.dto.HxUserDtoV2;
import me.zhengjie.service.dto.HxUserLoginDto;
import me.zhengjie.service.dto.HxUserQueryCriteria;
import me.zhengjie.vo.UserNumVO;
import org.springframework.data.domain.Pageable;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * @author me zhengjie
 * @website
 * @description 服务接口
 * @date 2023-06-12
 **/
public interface HxUserService {

    /**
     * 查询数据分页
     *
     * @param criteria 条件
     * @param pageable 分页参数
     * @return Map<String, Object>
     */
    Map<String, Object> queryAll(HxUserQueryCriteria criteria, Pageable pageable);

    /**
     * 查询所有数据不分页
     *
     * @param criteria 条件参数
     * @return List<HxUserDto>
     */
    List<HxUserDto> queryAll(HxUserQueryCriteria criteria);

    /**
     * 根据ID查询
     *
     * @param userId ID
     * @return HxUserDto
     */
    HxUserDto findById(Long userId);

    HxUserDtoV2 findByIdV2(Long userId);


    /**
     * 编辑
     *
     * @param resources /
     */
    void update(HxUser resources);


    /**
     * 根据用户名查询
     *
     * @param phone /
     * @return /
     */
    HxUserLoginDto getLoginData(String phone);

    String createUser(String phone, String password, String platform, String uuid, String channelCode, HttpServletRequest request);

    /**
     * 获取用户权限信息
     *
     * @param user 用户信息
     * @return 权限信息
     */
    List<HxAuthorityDto> mapToGrantedAuthorities(HxUserDto user);

//    HxCustomer findCustomerOnline();

    List<UserNumVO> statUserRegisterNum();
}