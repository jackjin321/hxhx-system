package me.zhengjie.domain;


import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.*;
import me.zhengjie.base.XFBaseEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.math.BigDecimal;

/**
 * - 激活数（启动APP）
 * - 总登录数（包含新老户）
 * - 激活-登录率
 * - 新户注册
 * - 老户登录
 * - 新户申请数（新用户注册到任意点击一款产品的人数）
 * - 老户申请数（老用户登录到任意点击一款产品的人数）
 * - 新户注册-申请率（新户申请数/新户注册数*100%）
 * - 老户登录-申请率（老户申请数/老户登录数*100%）
 * - 总产品UV（所有用户产生的产品UV数）
 * - 总产品PV
 * - arup值（人均收益）计算方式：总收益/当日新户老户登录数
 */
@Entity
@Getter
@Setter
@Table(name = "xf_channel_chart")
public class ChannelChart extends XFBaseEntity {


    /**
     * 渠道代码
     */
    @JsonSerialize(using= ToStringSerializer.class)
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
    private Long todayPv;

    /**
     * 渠道UV
     */
    private Long todayUv;

    private Long ipPv;

    private Long ipUv;

    /**
     * 总登录数（包含新老户）
     */
    private Long loginNum;

    /**
     * 新户注册
     */
    private Long registerNum;
    /**
     * 老户登录
     */
    private Long oldLoginNum;

    @Column(name = "for_c_register", length = 20)
    private Long forCRegister;


    /**
     * 新户申请数（新用户注册到任意点击一款产品的人数）
     */
    private Long newProductUv;

    /**
     * 老户申请数（老用户登录到任意点击一款产品的人数）
     */
    private Long oldProductUv;
    /**
     * 新户注册-申请率（新户申请数/新户注册数*100%）
     */
    private BigDecimal newProductRate;

    /**
     * 老户登录-申请率（老户申请数/老户登录数*100%）
     */
    private BigDecimal oldProductRate;

    /**
     * 总产品UV（所有用户产生的产品UV数）
     */
    private Long productUv;

    /**
     * 总产品PV
     */
    private Long productPv;

    /**
     * 产出值=产出uv/uv数
     */
    private BigDecimal outputValue;
}
