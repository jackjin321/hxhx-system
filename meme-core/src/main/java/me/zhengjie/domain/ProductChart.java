package me.zhengjie.domain;

import lombok.*;
import me.zhengjie.base.XFBaseEntity;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.math.BigDecimal;


@Entity
@Getter
@Setter
@Table(name = "xf_product_chart")
public class ProductChart extends XFBaseEntity {


    /**
     * 统计维度：产品汇总，product，广告位+产品，bannerProduct，广告位汇总，banner
     */
    private String category;
    /**
     * 今日PV
     */
    private Long todayPv;

    /**
     * 今日UV
     */
    private Long todayUv;

    private Long bannerId;

    private String bannerName;

    /**
     * 产品Id
     */
    private Long productId;

    /**
     * 产品名称
     */
    private String productName;

    /**
     * 首次访问
     */
    private Long firstTo;


    private Long ipPv;

    private Long ipUv;

    /**
     * 产品单价
     */
    private BigDecimal price;

    /**
     * 价格类型 1、uv 2、cpa
     */
    private String priceType;

}
