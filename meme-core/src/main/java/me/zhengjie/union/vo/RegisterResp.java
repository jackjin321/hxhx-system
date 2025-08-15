package me.zhengjie.union.vo;

import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.util.Objects;

/**
 * 匹配结果
 *
 * @author Eathon
 */
@Data
@Accessors(chain = true)
public class RegisterResp {


    /**
     * 1 成功, 2 失败, 0 请求异常
     */
    private MatchStatusEnum status;
    /**
     * 产品id
     */
    private Long productId;

    /**
     * 跳转地址
     */
    private String redirectUrl;

    /**
     * 撞库返回信息
     */
    private String msg;
    /**
     * 产品价格
     */
    private BigDecimal price;


    public boolean isSucceed() {
        return Objects.equals(MatchStatusEnum.SUCCESS, status);
    }


    public static RegisterResp success(Long productId, String redirectUrl) {
        return new RegisterResp().setProductId(productId).setRedirectUrl(redirectUrl).setStatus(MatchStatusEnum.SUCCESS);
    }

    public static RegisterResp fail(Long productId, String msg) {
        return new RegisterResp().setProductId(productId).setStatus(MatchStatusEnum.FAIL).setMsg(msg);
    }


    public static RegisterResp error(Long productId, String msg) {
        return new RegisterResp().setProductId(productId).setStatus(MatchStatusEnum.ERROR).setMsg(msg);
    }


}
