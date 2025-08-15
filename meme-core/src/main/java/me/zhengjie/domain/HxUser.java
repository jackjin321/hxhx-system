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

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import me.zhengjie.base.BaseEntity;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @author me zhengjie
 * @website
 * @description /
 * @date 2023-06-12
 **/
@Entity
@Data
@Table(name = "hx_user")
public class HxUser extends BaseEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "`user_id`")
    @ApiModelProperty(value = "ID")
    private Long userId;

    private Long channelId;

    private String channelName;

    private String platform;

    @Column(name = "`username`", unique = true)
    @ApiModelProperty(value = "用户名")
    private String username;

    @Column(name = "`nick_name`")
    @ApiModelProperty(value = "昵称")
    private String nickName;

    @Column(name = "`phone`", unique = true)
    @ApiModelProperty(value = "手机号码")
    private String phone;

    private String phoneMd5;


    @Column(name = "`password`")
    @ApiModelProperty(value = "密码")
    private String password;

    @Column(name = "`enabled`")
    @ApiModelProperty(value = "状态：1启用、0禁用")
    private Boolean enabled;

    @Column(name = "`pwd_reset_time`")
    @ApiModelProperty(value = "修改密码的时间")
    private Date pwdResetTime;


    @Column(name = "`register_time`")
    @ApiModelProperty(value = "注册时间")
    private Date registerTime;

    @Column(name = "`last_login_time`")
    @ApiModelProperty(value = "最近登录时间")
    private Date lastLoginTime;

    @Column(name = "`last_op_time`")
    @ApiModelProperty(value = "最近操作时间")
    private Date lastOpTime;

    public void copy(HxUser source) {
        BeanUtil.copyProperties(source, this, CopyOptions.create().setIgnoreNullValue(true));
    }
}
