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
package me.zhengjie.domain;

import lombok.Data;
import cn.hutool.core.bean.BeanUtil;
import io.swagger.annotations.ApiModelProperty;
import cn.hutool.core.bean.copier.CopyOptions;
import me.zhengjie.base.BaseEntity;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.sql.Timestamp;
import java.io.Serializable;

/**
* @website https://eladmin.vip
* @description /
* @author zhengjie
* @date 2025-06-15
**/
@Entity
@Data
@Table(name="xf_channel_user")
public class XfChannelUser extends BaseEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "`id`")
    @ApiModelProperty(value = "ID")
    private Long id;

    @Column(name = "`channel_id`",nullable = false)
    @NotNull
    @ApiModelProperty(value = "渠道Id")
    private Long channelId;

    @Column(name = "`sys_user_id`",nullable = false)
    @NotNull
    @ApiModelProperty(value = "sys_user_id")
    private Long sysUserId;

    @Column(name = "`username`")
    @ApiModelProperty(value = "用户名")
    private String username;

    @Column(name = "`phone`")
    @ApiModelProperty(value = "手机号码")
    private String phone;

    @Column(name = "`password`")
    @ApiModelProperty(value = "密码")
    private String password;

    @Column(name = "`status`")
    @ApiModelProperty(value = "登录状态")
    private Boolean status;


    public void copy(XfChannelUser source){
        BeanUtil.copyProperties(source,this, CopyOptions.create().setIgnoreNullValue(true));
    }
}
