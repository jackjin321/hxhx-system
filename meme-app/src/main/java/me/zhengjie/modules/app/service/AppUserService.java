
package me.zhengjie.modules.app.service;

import me.zhengjie.domain.Channel;
import me.zhengjie.modules.union.dto.H5ChannelUnionLoginResultDTO;
import me.zhengjie.modules.union.vo.H5ChannelRegisterBaseRequest;
import me.zhengjie.vo.MemberAuth;

public interface AppUserService {


    boolean checkSmsCode(String phone, String code);
    boolean sendSmsCode(String phone);
    boolean sendFanQin(String phone, String code);
    boolean sendFanQinV2(String phone, String code);

    String checkIdentity(MemberAuth memberAuth);

    /**
     * 渠道联登
     *
     * @param request 请求参数
     * @return 渠道联登
     */
    H5ChannelUnionLoginResultDTO chaUnionLogin(H5ChannelRegisterBaseRequest request, Channel channel);


}
