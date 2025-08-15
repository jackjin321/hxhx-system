package me.zhengjie.domain;


import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import me.zhengjie.base.XFBaseEntity;

import javax.persistence.*;

@Entity
@Getter
@Setter
@Accessors(chain = true)
@Table(name = "xf_product_log")
public class ProductLog extends XFBaseEntity implements Comparable<ProductLog> {

    private Long userId;

    private String uuid;
    /**
     * 用户状态，新用户，老用户
     */
    private String userStatus;
    /**
     * 用户手机号码
     */
    private String phone;

    private String phoneMd5;

    /**
     * 渠道id
     */
    private Long channelId;
    /**
     * 渠道名称
     */
    private String channelName;

    private String channelPort;
    /**
     * 产品ID
     */
    private Long productId;

    /**
     * 产品名称
     */
    private String productName;

//    private String productPort;

    /**
     * 访问IP
     */
    private String accessIp;
    /**
     * 访问设备
     */
    private String accessOs;


    /**
     * 广告位相关
     */
    private Long bannerId;
    /**
     * 页面
     */
    private String page;

    /**
     * 位置
     */
    private String pos;

    /**
     * 页面
     */
    private String pageName;

    /**
     * 位置
     */
    private String posName;


    /**
     * 是否首次访问
     */
    private Boolean isFirstTo;


    @Override
    public int compareTo(ProductLog o) {
        if (this.productId.equals(o.productId) && this.userId.equals(o.userId)) {
            return 0;
        }
        return 1;
    }
}
