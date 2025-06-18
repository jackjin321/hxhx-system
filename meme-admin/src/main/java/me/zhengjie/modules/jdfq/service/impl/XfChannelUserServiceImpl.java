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
package me.zhengjie.modules.jdfq.service.impl;

import cn.hutool.core.util.ObjectUtil;
import lombok.extern.slf4j.Slf4j;
import me.zhengjie.domain.XfChannelUser;
import me.zhengjie.exception.BadRequestException;
import me.zhengjie.exception.EntityExistException;
import me.zhengjie.modules.system.domain.Dept;
import me.zhengjie.modules.system.domain.Job;
import me.zhengjie.modules.system.domain.Role;
import me.zhengjie.modules.system.domain.User;
import me.zhengjie.modules.system.repository.DeptRepository;
import me.zhengjie.modules.system.repository.JobRepository;
import me.zhengjie.modules.system.repository.RoleRepository;
import me.zhengjie.modules.system.repository.UserRepository;
import me.zhengjie.utils.*;
import lombok.RequiredArgsConstructor;
import me.zhengjie.repository.XfChannelUserRepository;
import me.zhengjie.modules.jdfq.service.XfChannelUserService;
import me.zhengjie.service.dto.XfChannelUserDto;
import me.zhengjie.service.dto.XfChannelUserQueryCriteria;
import me.zhengjie.service.mapstruct.XfChannelUserMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.*;
import java.io.IOException;
import javax.servlet.http.HttpServletResponse;


/**
* @website https://eladmin.vip
* @description 服务实现
* @author zhengjie
* @date 2025-06-15
**/
@Slf4j
@Service
@RequiredArgsConstructor
public class XfChannelUserServiceImpl implements XfChannelUserService {

    private final XfChannelUserRepository xfChannelUserRepository;
    private final XfChannelUserMapper xfChannelUserMapper;

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    private final DeptRepository deptRepository;
    private final RoleRepository roleRepository;
    private final JobRepository jobRepository;

    @Override
    public Map<String,Object> queryAll(XfChannelUserQueryCriteria criteria, Pageable pageable){
        Page<XfChannelUser> page = xfChannelUserRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder),pageable);
        return PageUtil.toPage(page.map(xfChannelUserMapper::toDto));
    }

    @Override
    public List<XfChannelUserDto> queryAll(XfChannelUserQueryCriteria criteria){
        return xfChannelUserMapper.toDto(xfChannelUserRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder)));
    }

    @Override
    @Transactional
    public XfChannelUserDto findById(Long id) {
        XfChannelUser xfChannelUser = xfChannelUserRepository.findById(id).orElseGet(XfChannelUser::new);
        ValidationUtil.isNull(xfChannelUser.getId(),"XfChannelUser","id",id);
        return xfChannelUserMapper.toDto(xfChannelUser);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public XfChannelUserDto create(XfChannelUser resources) {
        //判断用户名是否唯一
        if (userRepository.findByUsername(resources.getUsername()) != null) {
            throw new EntityExistException(User.class, "username", resources.getUsername());
        }
        if (xfChannelUserRepository.findByUsername(resources.getUsername()) != null) {
            throw new EntityExistException(XfChannelUser.class, "username", resources.getUsername());
        }
        if (ObjectUtil.isAllEmpty(resources.getPassword())) {
            throw new BadRequestException("Password is null");
        }

        //创建sys_user信息
        User sysUser = new User();
        sysUser.setUsername(resources.getUsername());
        sysUser.setNickName(resources.getUsername());
        Optional<Dept> deptOptional = deptRepository.findById(Long.valueOf("2"));
        sysUser.setDept(deptOptional.get());//部门，2
        Optional<Role> roleOptional = roleRepository.findById(Long.valueOf("4"));
        Set<Role> roles = new HashSet<>();
        roles.add(roleOptional.get());
        sysUser.setRoles(roles);
        Optional<Job> jobOptional = jobRepository.findById(Long.valueOf("12"));
        Set<Job> jobs = new HashSet<>();
        jobs.add(jobOptional.get());
        sysUser.setJobs(jobs);//岗位，job_id，12

        sysUser.setPhone("19999999999");
        sysUser.setEmail("123456789@126.com");

        String password = passwordEncoder.encode(resources.getPassword());
        sysUser.setPassword(password);
        sysUser.setIsAdmin(false);
        sysUser.setEnabled(true);
        sysUser.setUserType(1);
        userRepository.save(sysUser);
        log.info("sysUser id {}", sysUser.getId());

        String username = SecurityUtils.getCurrentUsername();
        resources.setSysUserId(sysUser.getId());
        resources.setPassword(password);
        resources.setStatus(true);

        resources.setUpdateBy(username);
        //xfChannelUserRepository.save(resources);

        return xfChannelUserMapper.toDto(xfChannelUserRepository.save(resources));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(XfChannelUser resources) {
        XfChannelUser xfChannelUser = xfChannelUserRepository.findById(resources.getId()).orElseGet(XfChannelUser::new);
        ValidationUtil.isNull( xfChannelUser.getId(),"XfChannelUser","id",resources.getId());
        xfChannelUser.copy(resources);
        xfChannelUserRepository.save(xfChannelUser);
    }

    @Override
    public void deleteAll(Long[] ids) {
        for (Long id : ids) {
            xfChannelUserRepository.deleteById(id);
        }
    }

//    @Override
//    public void download(List<XfChannelUserDto> all, HttpServletResponse response) throws IOException {
//        List<Map<String, Object>> list = new ArrayList<>();
//        for (XfChannelUserDto xfChannelUser : all) {
//            Map<String,Object> map = new LinkedHashMap<>();
//            map.put("渠道Id", xfChannelUser.getChannelId());
//            map.put("sys_user_id", xfChannelUser.getSysUserId());
//            map.put("用户名", xfChannelUser.getUsername());
//            map.put("手机号码", xfChannelUser.getPhone());
//            map.put("登录状态", xfChannelUser.getStatus());
//            map.put("创建者", xfChannelUser.getCreateBy());
//            map.put("更新者", xfChannelUser.getUpdateBy());
//            map.put("创建日期", xfChannelUser.getCreateTime());
//            map.put("更新时间", xfChannelUser.getUpdateTime());
//            list.add(map);
//        }
//        FileUtil.downloadExcel(list, response);
//    }
}