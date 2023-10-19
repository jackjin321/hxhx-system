
package me.zhengjie.domain;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
* @website
* @description /
* @author me zhengjie
* @date 2023-06-12
**/
@Entity
@Data
@Table(name="hx_sys_config")
public class HxSysConfig implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "`id`")
    @ApiModelProperty(value = "ID")
    private Long id;

    @Column(name = "`name`",nullable = false)
    @NotBlank
    @ApiModelProperty(value = "配置名称")
    private String name;

    @Column(name = "`key`",unique = true,nullable = false)
    @NotBlank
    @ApiModelProperty(value = "key")
    private String key;

    @Column(name = "`value`")
    @ApiModelProperty(value = "值")
    private String value;

    @Column(name = "`remark`")
    @ApiModelProperty(value = "备注")
    private String remark;

    public void copy(HxSysConfig source){
        BeanUtil.copyProperties(source,this, CopyOptions.create().setIgnoreNullValue(true));
    }
}
