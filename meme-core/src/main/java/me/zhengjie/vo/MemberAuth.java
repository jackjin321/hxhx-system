package me.zhengjie.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MemberAuth implements Serializable {

    private static final long serialVersionUID = 1L;


    /**
     * 真实姓名
     */
    private String realName;
    /**
     * 身份证号码
     */
    private String idCard;

    private String province;

    private String city;
}
