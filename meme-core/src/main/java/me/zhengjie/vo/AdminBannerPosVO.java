package me.zhengjie.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 广告位位置VO
 * @Description
 * @Date 2021/7/6 17:27
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AdminBannerPosVO implements Serializable {

    /**
     * Banner页面英文名称
     */
    private String posEnName;

    /**
     * Banner页面显示名称
     */
    private String posShowName;

}
