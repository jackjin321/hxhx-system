package me.zhengjie.modules.union.dto;

import io.swagger.v3.oas.annotations.Parameter;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @ClassName RequestTxIdVO
 * @Description
 * @Author 14322
 * @Date 2024/7/4 20:20
 **/
@Data
public class RequestTxId {


    @Parameter(name = "requestTxId", description = "请求事务id")
    @NotBlank(message = "请求事务id不能为空")
    private String requestTxId;


    /**
     * 生成并赋值requestTxId
     */
    public void setRequestTxId() {
        this.requestTxId = RequestIdUtil.buildRequestId();
    }
}
