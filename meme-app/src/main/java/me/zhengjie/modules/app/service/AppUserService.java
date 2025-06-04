
package me.zhengjie.modules.app.service;

import me.zhengjie.vo.MemberAuth;

public interface AppUserService {


    boolean checkSmsCode(String phone, String code);
    boolean sendSmsCode(String phone);
    boolean sendFanQin(String phone, String code);

    String checkIdentity(MemberAuth memberAuth);
}
