package me.zhengjie.modules.union.handler.impl;


import cn.hutool.core.util.ObjectUtil;
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
import me.zhengjie.utils.DianDianUtil;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * 蓝鲸花
 */
@Slf4j
@LoanProductMatchHandler(name = "ljhua-matcher", confKey = "ljhua")
public class LjhuaMatchHandler extends AbstractProductDbMatchHandler<JSONObject> {

    private static final Integer SUCCESS_CODE = 200;

    /**
     * {
     * "msg": "SUCCESS",
     * "code": 200,
     * "data": {
     * "status": 0
     * }
     * }
     *
     * @param product
     * @param user
     * @param configProperties
     * @return
     */
    @Override
    protected Map<String, Object> buildRequestParamMatch(Product product, HxUser user, BaseLoanProductConfigProperties configProperties) {
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("phoneMd5", user.getPhoneMd5());
//        paramMap.put("ip", product.getIp());
        paramMap.put("channelCode", configProperties.getAppKey());
        log.info("营销产品:{}-撞库请求参数paramMap:{}", product.getProductName(), JSONUtil.toJsonStr(paramMap));
        return paramMap;
    }

    @Override
    protected Map<String, Object> buildRequestParamRegister(Product product, HxUser user, BaseLoanProductConfigProperties configProperties) {
        Map<String, Object> paramMap = new HashMap<>();
        JSONObject entries = new JSONObject();

        entries.set("phone", user.getPhone());
//        entries.set("ip", product.getIp());
        entries.set("channelCode", configProperties.getAppKey());

        paramMap.put("sign", DianDianUtil.java_openssl_encrypt(JSONUtil.toJsonStr(entries)));

        log.info("营销产品:{}-注册请求参数paramMap:{}", product.getProductName(), JSONUtil.toJsonStr(paramMap));
        return paramMap;
    }


    @Override
    protected ForestRequest<?> getRequestCli(String url) {
        return Forest.post(url).contentType(ContentType.APPLICATION_JSON);
    }

//    @Override
//    protected Class<JSONObject> getResponseType() {
//        return JSONObject.class;
//    }

    @Override
    protected Class<JSONObject> getResponseType() {
        return JSONObject.class;
    }

    /**
     * 是否撞库成功(0 ⽤户不存在，1  ⽤户存在)
     *
     * @param response  响应结果
     * @param productId
     * @param phoneMd5
     * @return
     */
    @Override
    protected MatchResp responseProcessMatch(JSONObject response, Long productId, String phoneMd5) {
        Integer code = response.getInt("code");
        String msg = response.getStr("msg");
        // 撞库成功
        if (Objects.equals(SUCCESS_CODE, code)) {
            JSONObject data = response.getJSONObject("data");
            if (ObjectUtil.isNotEmpty(data)) {
                Integer status = data.getInt("status");
                if (status == 0) {
                    return MatchResp.success(productId, msg);
                }
            }
            return MatchResp.fail(productId, msg);
        } else {
            return MatchResp.fail(productId, msg);
        }
    }

    /***
     * {
     *  "msg": "SUCCESS",
     *  "code": 200,
     *  "data": {
     *      "status": 0,
     *     "url":"www.baidu.com"
     *  }
     * }
     * @param response
     * @param productId
     * @param phoneMd5
     * @return
     */
    @Override
    protected RegisterResp responseProcessRegister(JSONObject response, Long productId, String phoneMd5) {
        Integer code = response.getInt("code");
        String msg = response.getStr("msg");
        //String url = response.getStr("data");
        // 撞库成功
        //是否成功(1 注册成功 0 ⽤户存在)
        if (Objects.equals(SUCCESS_CODE, code)) {
            JSONObject data = response.getJSONObject("data");
            if (ObjectUtil.isNotEmpty(data)) {
                return RegisterResp.success(productId, data.getStr("url"));
            }
            return RegisterResp.fail(productId, msg);
        } else {
            return RegisterResp.fail(productId, msg);
        }
    }
}
