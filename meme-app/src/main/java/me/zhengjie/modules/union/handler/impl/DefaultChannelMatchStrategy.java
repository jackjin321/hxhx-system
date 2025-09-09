package me.zhengjie.modules.union.handler.impl;

import cn.hutool.core.text.CharSequenceUtil;
import cn.hutool.json.JSONObject;
import lombok.extern.slf4j.Slf4j;
import me.zhengjie.domain.Channel;
import me.zhengjie.domain.HxUser;
import me.zhengjie.modules.union.vo.H5ChannelRegisterBaseRequest;
import me.zhengjie.repository.HxUserRepository;
import me.zhengjie.modules.union.annotations.LoanChannelMatchHandler;
import me.zhengjie.modules.union.dto.ChannelMatchStrategyR;
import me.zhengjie.modules.union.dto.H5ChannelMatchR;
import me.zhengjie.modules.union.handler.AbstractChannelDbMatchHandler;
import me.zhengjie.modules.union.vo.ApiResult;
import me.zhengjie.modules.union.vo.H5ChannelMatchAccessRequest;

import javax.annotation.Resource;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

/**
 * 默认渠道撞库匹配器
 *
 * @author Eathon
 */
@Slf4j
@LoanChannelMatchHandler(name = "default-matcher")
public class DefaultChannelMatchStrategy extends AbstractChannelDbMatchHandler<ApiResult> {

    @Resource
    private HxUserRepository userRepository;


    @Override
    public ChannelMatchStrategyR<?> requestParamCheck(H5ChannelMatchAccessRequest request, Channel channel) {
        if (CharSequenceUtil.isBlank(request.getPhoneMd5())) {
            log.error("渠道执行撞库,手机号md5为空,渠道Id:{}", request.getPhoneMd5());
            return ChannelMatchStrategyR.fail("手机号md5为空");
        }
        return ChannelMatchStrategyR.success();
    }

    @Override
    public ChannelMatchStrategyR<?> accessible(H5ChannelMatchAccessRequest request, Channel channel) {
        HxUser userInfo = userRepository.findFirstByPhoneMd5(request.getPhoneMd5());
        return Objects.isNull(userInfo) ? ChannelMatchStrategyR.success() : ChannelMatchStrategyR.fail("撞库失败");
    }

    @Override
    protected String decryptRequestParamJson(String requestParamJson, Map<String, Object> metaParamMap) {
        return requestParamJson;
    }

    @Override
    protected H5ChannelMatchAccessRequest convertRequest(JSONObject jsonObj, Map<String, Object> metaParamMap) {
        return jsonObj.toBean(H5ChannelMatchAccessRequest.class);
    }

    @Override
    protected H5ChannelMatchR preProgress(H5ChannelMatchAccessRequest request, Channel channel, Map<String, Object> metaParamMap) {
        return null;
    }

    @Override
    protected ApiResult onSuccess(H5ChannelMatchAccessRequest request, H5ChannelMatchR resp, Channel channelConf, Map<String, Object> metaParamMap) {
        // 生成Ticket
        return ApiResult.ok(resp);
    }


    @Override
    protected ApiResult onFail(String msg) {
        return ApiResult.fail(msg, H5ChannelMatchR.fail());
    }



    @Override
    protected ApiResult onSuccess(String registerUrl, H5ChannelRegisterBaseRequest requestObj) {
        return ApiResult.ok("注册成功", registerUrl);
    }


    @Override
    protected ApiResult onFail(String msg, Optional<H5ChannelRegisterBaseRequest> requestOptional) {
        return ApiResult.fail(msg);
    }

    @Override
    protected String resolveRequestParamJson(String requestParamJson) {
        return requestParamJson;
    }

    @Override
    protected H5ChannelRegisterBaseRequest convertRequest(JSONObject requestJson, Channel channelConf) {
        try {
//            H5ChaLoginRegisterRequest bean = requestJson.toBean(H5ChaLoginRegisterRequest.class);
//            String decrypt = AesUtil.decrypt(bean.getEncryptData(), channelConf.getAesKey());
            H5ChannelRegisterBaseRequest request = new H5ChannelRegisterBaseRequest();
//            request.setCity(bean.getCity());
            //request.setPhone(decrypt);
            return request;
        } catch (Exception e){
            log.error("渠道使用默认注册器注册时,转换对象过程发生异常,渠道id:{}", channelConf.getId());
        }
        return null;
    }
}
