package me.zhengjie.domain;

import lombok.Getter;
import lombok.Setter;
import me.zhengjie.base.XFBaseEntity;
import me.zhengjie.utils.StringUtil;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;


@Entity
@Getter
@Setter
@DynamicUpdate
@Table(name = "xf_channel_log")
public class ChannelLog extends XFBaseEntity  implements Comparable<ChannelLog> {

    private Long userId;

    private String uuid;//前端会话标识

    private String phone;

    private String phoneMd5;

    private Long channelId;
    private Long regChannelId;//注册渠道

    private String channelName;
    private String regChannelName;//注册渠道

    private String browser;

    private String platform;

    private String deviceId;

    private String deviceBrand;

    private Long lastChannelId;

    private String lastChannelName;

    private String osId;

    private String osName;

    private String accessIp;

    /**
     * 是否是新用户
     */
    private Boolean isRegister;

    /**
     * 是否已经登录
     */
    private Boolean isLogin;

    @Override
    public int compareTo(ChannelLog o) {

        if (StringUtil.isEmpty(this.accessIp) && StringUtil.isEmpty(o.accessIp)) {
            return 0;
        }
//        if (StringUtil.isEmpty(this.accessIp) || StringUtil.isEmpty(o.accessIp)) {
//            return 1;
//        }


//        if (o.getIp().equals(this.ip) && this.getOsName().equals(o.getOsName()) && this.getOsId().equals(this.osId)) {
//        if (o.getAccessIp().equals(this.accessIp) && this.getOsName().equals(o.getOsName()) && o.getOsId().equals(this.osId)) {
//            return 0;
//        }

        if ((StringUtil.isNotEmpty(this.uuid) || StringUtil.isNotEmpty(o.uuid)) && StringUtil.getDefVal(this.uuid, "").equals(StringUtil.getDefVal(o.uuid, ""))) {
            return 0;
        }
//        if (!this.osId.equals("") && !o.osId.equals("") && this.osId.equals(this.osId)) {
//        if (!this.osId.equals("") && !o.osId.equals("") && o.osId.equals(this.osId)) {
//            return 0;
//        }
        return 1;
    }
}
