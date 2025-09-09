package me.zhengjie.modules.union.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import me.zhengjie.domain.HxUser;
import me.zhengjie.service.dto.HxJwtUserDto;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class H5ChannelUnionLoginResultDTO {

    /**
     * token字符串
     */
    private String tokenStr;

    /**
     * 用户id
     */
    private HxUser userInfo;

    /**
     * H5 登录用户信息
     */
    private HxJwtUserDto h5LoginUser;

    /**
     * 跳转URL
     */
    private String link;

}
