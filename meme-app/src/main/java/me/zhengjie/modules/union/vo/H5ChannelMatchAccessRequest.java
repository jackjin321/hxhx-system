package me.zhengjie.modules.union.vo;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author evans
 * @description
 * @date 2024/5/9
 */
@Data
@Accessors(chain = true)
@Schema(name = "渠道MD5准入撞库请求参数")
public class H5ChannelMatchAccessRequest {

    @Parameter(description = "手机号MD5", required = true)
    private String phoneMd5;

    @Parameter(description = "城市", required = true)
    private String city;

}
