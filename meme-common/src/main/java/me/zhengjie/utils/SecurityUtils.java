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
package me.zhengjie.utils;

import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import lombok.extern.slf4j.Slf4j;
import me.zhengjie.exception.BadRequestException;
import me.zhengjie.utils.enums.DataScopeEnum;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import java.util.List;

/**
 * 获取当前登录的用户
 * @author Zheng Jie
 * @date 2019-01-17
 */
@Slf4j
public class SecurityUtils {

    /**
     * 获取当前登录的用户
     * @return UserDetails
     */
    public static UserDetails getCurrentUser() {
        UserDetailsService userDetailsService = SpringContextHolder.getBean(UserDetailsService.class);
        return userDetailsService.loadUserByUsername(getCurrentUsername());
    }

    /**
     * 获取系统用户名称
     *
     * @return 系统用户名称
     */
    public static String getCurrentUsername() {
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            throw new BadRequestException(HttpStatus.UNAUTHORIZED, "当前登录状态过期");
        }
        if (authentication.getPrincipal() instanceof UserDetails) {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            return userDetails.getUsername();
        }
        throw new BadRequestException(HttpStatus.UNAUTHORIZED, "找不到当前登录的信息");
    }

    /**
     * 获取系统用户ID
     *
     * @return 系统用户ID
     */
    public static Long getCurrentUserId() {
        UserDetails userDetails = getCurrentUser();
        return new JSONObject(new JSONObject(userDetails).get("user")).get("id", Long.class);
    }

    public static Integer getCurrentUserType() {
        UserDetails userDetails = getCurrentUser();
        return new JSONObject(new JSONObject(userDetails).get("user")).get("userType", Integer.class);
    }

    public static String getCurrentUserPhoneByApp() {
        UserDetails userDetails = getCurrentUser();
//        System.out.println(new JSONObject(userDetails));
        //{"authorities":[{"authority":"appFind"}],"user":{"lastOpTime":1694917867000,"registerTime":1694917867000,"updateTime":1694917867000,"userId":6,"enabled":true,"lastLoginTime":1694917867000,"password":"$2a$10$jmw5mnbA/9lfvxKFlGcwNOD1Yf9ax0HshbcoHOGlhmxkZgBdN9hAG","createBy":"System","phone":"13800010001","updateBy":"System","createTime":1694917867000,"channelName":"表单h5测试","channelId":1,"username":"13800010001"}}
        return new JSONObject(new JSONObject(userDetails).get("user")).get("phone", String.class);
    }
    public static Long getCurrentUserIdByApp() {
        UserDetails userDetails = getCurrentUser();
        //System.out.println(new JSONObject(userDetails));
        //{"authorities":[{"authority":"appFind"}],"user":{"lastOpTime":1694917867000,"registerTime":1694917867000,"updateTime":1694917867000,"userId":6,"enabled":true,"lastLoginTime":1694917867000,"password":"$2a$10$jmw5mnbA/9lfvxKFlGcwNOD1Yf9ax0HshbcoHOGlhmxkZgBdN9hAG","createBy":"System","phone":"13800010001","updateBy":"System","createTime":1694917867000,"channelName":"表单h5测试","channelId":1,"username":"13800010001"}}
        return new JSONObject(new JSONObject(userDetails).get("user")).get("userId", Long.class);
    }
    public static Long getRegChannelIdByApp() {
        UserDetails userDetails = getCurrentUser();
        //System.out.println(new JSONObject(userDetails));
        return new JSONObject(new JSONObject(userDetails).get("user")).get("channelId", Long.class);
    }
    public static String getRegChannelNameByApp() {
        UserDetails userDetails = getCurrentUser();
        //System.out.println(new JSONObject(userDetails));
        return new JSONObject(new JSONObject(userDetails).get("user")).get("channelName", String.class);
    }
    public static Long getCurrentUserIdByAdmin() {
        UserDetails userDetails = getCurrentUser();
//        System.out.println(new JSONObject(userDetails));
        return new JSONObject(new JSONObject(userDetails).get("user")).get("id", Long.class);
    }

    /**
     * 获取当前用户的数据权限
     * @return /
     */
    public static List<Long> getCurrentUserDataScope(){
        UserDetails userDetails = getCurrentUser();
        JSONArray array = JSONUtil.parseArray(new JSONObject(userDetails).get("dataScopes"));
        return JSONUtil.toList(array,Long.class);
    }

    /**
     * 获取数据权限级别
     * @return 级别
     */
    public static String getDataScopeType() {
        List<Long> dataScopes = getCurrentUserDataScope();
        if(dataScopes.size() != 0){
            return "";
        }
        return DataScopeEnum.ALL.getValue();
    }
}
