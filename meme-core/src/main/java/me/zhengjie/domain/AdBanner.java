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
import java.io.Serializable;

/**
 * @author zhanghouying
 * @date 2019-08-24
 */
@Entity
@Getter
@Setter
@Table(name = "xf_banner")
public class AdBanner extends BaseEntity implements Serializable {

    @Id
    @ApiModelProperty(value = "ID", hidden = true)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 内部统计名称，不对外展示
     */
//    private String bannerName;

    /**
     * 标题
     */
    private String title;

    /**
     * 子标题
     */
    private String subTitle;

    /**
     * 页面
     */
    private String page;

    /**
     * 位置
     */
    private String pos;

    /**
     * h5|h5页面,native|原生页面,otherApp|其它App,noJump|不跳转
     */
    private String jumpType;

    /**
     * 跳转Url,或者跳转页面
     */
    private String jumpUrl;

    /**
     * 图片URL
     */
    private String imageUrl;

    /**
     * on|开启 off|关闭
     */
    private String status;

    /**
     * 排序数字，数字越大的排在前面
     */
    private Integer sortNum;

    private Long channelId;

//    private String channelName;

    private Long productId;

//    private String productName;

    public void copy(AdBanner source) {
        BeanUtil.copyProperties(source, this, CopyOptions.create().setIgnoreNullValue(true));
    }
}
