package me.zhengjie.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * @Description
 * @Author sanjin
 * @Date 2021/6/30 17:59
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ParamBannerQuery implements Serializable {

    /**
     * 页面
     */
    @NotBlank(message = "页面不能为空")
    private String page;

    private Long userId;
    /**
     * 位置
     */
    @NotBlank(message = "位置不能为空")
    private String pos;

    private Long id;

    private Long productId;

    /**
     * 渠道ID
     */
    private Long channelId;

    private String uuid;
    private String channelCode;
    private String realIP;
    private String phone;
}
