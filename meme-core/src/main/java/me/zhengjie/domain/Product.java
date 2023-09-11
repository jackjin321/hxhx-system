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
import java.math.BigDecimal;

/**
 * @author zhanghouying
 * @date 2019-08-24
 */
@Entity
@Getter
@Setter
@Table(name = "xf_product")
public class Product extends BaseEntity implements Serializable {

    @Id
    @Column(name = "product_id")
    @ApiModelProperty(value = "ID", hidden = true)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String portStatus;//AB面，A，B

    private String productName;

    private BigDecimal price;

    private String priceType;

    private String applyLink;
    /**
     * 状态 offShelves|下架，waitForOnShelves|等待上架，onShelves|上架
     */
    private String status;

    private Integer sort;

    private String imageUrl;

    /**
     * 跳转类型，direct，加白访问，normal，特殊访问
     */
    private String jumpType;
    private String cornerMark;
    private Integer minAmount;
    private Integer maxAmount;
    private Integer minMonth;
    private Integer maxMonth;
    private String rate;
    private String applyCondition;


    public void copy(Product source) {
        BeanUtil.copyProperties(source, this, CopyOptions.create().setIgnoreNullValue(true));
    }
}
