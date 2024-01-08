package me.zhengjie.domain;


import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import me.zhengjie.base.BaseEntity;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Data
@Table(name = "hx_user_report")
public class HxUserReport extends BaseEntity implements Serializable {

    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "`user_id`")
    @ApiModelProperty(value = "ID")
    private Long userId;

    /**
     * 逾期
     */
    private String overdue;

    private String transId;
    /**
     * 行为雷达
     */
    private String behaviorReport;
    /**
     * 申请雷达
     */
    private String applyReport;

    /**
     * 履约
     */
    private String performance;

    private String phone;

    /**
     * 真实姓名
     */
    private String realName;
    private String province;
    private String city;

    /**
     * 身份证号码
     */
    private String idCard;

    private String report;
}
