package me.zhengjie.vo;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChannelLogVO implements Serializable {
    private static final long serialVersionUID = 1L;
    private String uuid;
    private String accessIp;
}
