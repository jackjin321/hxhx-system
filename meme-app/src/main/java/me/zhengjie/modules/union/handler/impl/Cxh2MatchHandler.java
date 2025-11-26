package me.zhengjie.modules.union.handler.impl;


import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.dtflys.forest.Forest;
import com.dtflys.forest.backend.ContentType;
import com.dtflys.forest.http.ForestRequest;
import lombok.extern.slf4j.Slf4j;
import me.zhengjie.domain.HxUser;
import me.zhengjie.domain.Product;
import me.zhengjie.modules.union.annotations.LoanProductMatchHandler;
import me.zhengjie.modules.union.config.properties.BaseLoanProductConfigProperties;
import me.zhengjie.modules.union.handler.AbstractProductDbMatchHandler;
import me.zhengjie.modules.union.vo.MatchResp;
import me.zhengjie.modules.union.vo.RegisterResp;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * 诚小花
 */
@Slf4j
@LoanProductMatchHandler(name = "cxh2-matcher", confKey = "cxh2")
public class Cxh2MatchHandler extends AbstractProductDbMatchHandler<JSONObject> {

    private static final String SUCCESS_CODE = "0000";

    @Override
    protected Map<String, Object> buildRequestParamMatch(Product product, HxUser user, BaseLoanProductConfigProperties configProperties) {
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("phoneMd5", user.getPhoneMd5());
        paramMap.put("channelCode", configProperties.getAppKey());
//        paramMap.put("channelSign", product.getChaSign());
        log.info("营销产品:{}-撞库请求参数paramMap:{}", product.getProductName(), JSONUtil.toJsonStr(paramMap));
        return paramMap;
    }

    @Override
    protected Map<String, Object> buildRequestParamRegister(Product product, HxUser user, BaseLoanProductConfigProperties configProperties) {
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("phone", user.getPhone());
        paramMap.put("channelCode", configProperties.getAppKey());
//        paramMap.put("channelSign", product.getChaSign());
        log.info("营销产品:{}-注册请求参数paramMap:{}", product.getProductName(), JSONUtil.toJsonStr(paramMap));
        return paramMap;
    }


    @Override
    protected ForestRequest<?> getRequestCli(String url) {
        return Forest.post(url).contentType(ContentType.APPLICATION_JSON);
    }

    @Override
    protected Class<JSONObject> getResponseType() {
        return JSONObject.class;
    }

    @Override
    protected MatchResp responseProcessMatch(JSONObject response, Long productId, String phoneMd5) {
        String code = response.getStr("code");
        String msg = response.getStr("desc");
        // 撞库成功
        if (Objects.equals(SUCCESS_CODE, code)) {
            return MatchResp.success(productId, msg);
        } else {
            return MatchResp.fail(productId, msg);
        }
    }

    @Override
    protected RegisterResp responseProcessRegister(JSONObject response, Long productId, String phoneMd5) {
        String code = response.getStr("code");
        String msg = response.getStr("desc");
        String data = response.getStr("data");
        // 撞库成功
        if (Objects.equals(SUCCESS_CODE, code)) {
            return RegisterResp.success(productId, data);
        } else {
            return RegisterResp.fail(productId, msg);
        }
    }
}
