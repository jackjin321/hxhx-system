package me.zhengjie.domain;


import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import me.zhengjie.base.XFBaseEntity;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Getter
@Setter
@Accessors(chain = true)
@Table(name = "xf_product_register_record")
public class ProductRegisterRecord extends XFBaseEntity implements Comparable<ProductRegisterRecord> {

    /**
     * `channel_id` bigint                                                       NOT NULL COMMENT '来源渠道',
     *     `product_id` bigint                                                       NOT NULL COMMENT '撞库产品',
     *     `phone_md5`       varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT 'phoneMd5',
     *     `user_id`         bigint                                                       NOT NULL COMMENT '用户id',
     *     `city`            varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '撞库城市',
     *     `status`          int                                                          NOT NULL COMMENT '1 成功, 2 失败, 0 请求异常',
     */
    private Long userId;

    /**
     * 1 成功, 2 失败, 0 请求异常
     */
    private Integer status;

    private String phoneMd5;
    /**
     * 渠道id
     */
    private Long channelId;

    /**
     * 产品ID
     */
    private Long productId;

    private String city;

    private String result;



    @Override
    public int compareTo(ProductRegisterRecord o) {
        if (this.productId.equals(o.productId) && this.userId.equals(o.userId)) {
            return 0;
        }
        return 1;
    }
}
