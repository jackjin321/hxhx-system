package me.zhengjie.domain;


import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import me.zhengjie.base.XFBaseEntity;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Getter
@Setter
@Accessors(chain = true)
@Table(name = "xf_channel_match_record")
public class ChannelMatchRecord extends XFBaseEntity implements Comparable<ChannelMatchRecord> {

    private Long userId;
    /**
     * 访问IP
     */
    private String accessIp;
    /**
     * 1 成功, 2 失败, 0 请求异常
     */
    private Integer status;

    private String phoneMd5;
    /**
     * 渠道id
     */
    private Long channelId;

    private String city;

    private String result;

    private String businessNo;



    @Override
    public int compareTo(ChannelMatchRecord o) {
        if (this.channelId.equals(o.channelId) && this.userId.equals(o.userId)) {
            return 0;
        }
        return 1;
    }
}
