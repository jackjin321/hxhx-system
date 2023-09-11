package me.zhengjie.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @Description
 * @Author sanjin
 * @Date 2021/7/6 17:27
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class BannerAppPageVO implements Serializable {

    /**
     * Banner页面英文名称
     */
    private String pageEnName;
    private String pageUrl;

    /**
     * Banner页面显示名称
     */
    private String pageShowName;

}
