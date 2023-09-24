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
import lombok.Getter;
import lombok.Setter;
import me.zhengjie.base.BaseEntity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author zhanghouying
 * @date 2019-08-24
 */
@Entity
@Getter
@Setter
@Table(name = "xf_channel")
public class Channel extends BaseEntity implements Serializable {

    @Id
    @Column(name = "channel_id")
    @ApiModelProperty(value = "ID", hidden = true)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String channelName;
    private String channelCode;
    private BigDecimal price;
    private String priceType;//价格类型，uv，cpa
    private String process;//流程控制，A面，助贷流程，B面，贷超流程
    private String authStatus;//二要素开关，on开启，off关闭
    private String portStatus;//AB面，A，B
    private Integer sort;
    private Boolean recycle;
    private String channelRemarks;
    /**
     * 注册扣量比率
     */
    @NotNull
    private String registerBuckleRate;



    /**
     * 进件扣量比例
     */
//    @NotNull
//    private String intoBuckleRate;

    public void copy(Channel source) {
        BeanUtil.copyProperties(source, this, CopyOptions.create().setIgnoreNullValue(true));
    }
}
