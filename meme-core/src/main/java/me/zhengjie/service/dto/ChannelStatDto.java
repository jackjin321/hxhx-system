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
package me.zhengjie.service.dto;

import lombok.Data;

import javax.persistence.Column;
import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;

/**
* @website https://eladmin.vip
* @description /
* @author zhengjie
* @date 2025-06-15
**/
@Data
public class ChannelStatDto implements Serializable {

    private Long channelId;

    /**
     * 渠道名称
     */
    private String channelName;

    /**
     * 单价
     */
    private BigDecimal price;

    /**
     * 结算类型 1、uv 2、注册register 3 进件 into
     */
    private String priceType;

    /**
     * 渠道PV
     */

    /**
     * 渠道UV
     */
    private Long todayUv;


    private Long ipUv;

    /**
     * 总登录数（包含新老户）
     */
//    private Long loginNum;

    /**
     * 新户注册
     */
//    private Long registerNum;
    /**
     * 老户登录
     */
//    private Long oldLoginNum;

    @Column(name = "for_c_register", length = 20)
    private Long forCRegister;

    private Timestamp createTime;

}