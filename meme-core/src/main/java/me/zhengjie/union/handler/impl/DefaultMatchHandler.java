package me.zhengjie.union.handler.impl;


import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.dtflys.forest.Forest;
import com.dtflys.forest.backend.ContentType;
import com.dtflys.forest.http.ForestRequest;
import lombok.extern.slf4j.Slf4j;
import me.zhengjie.domain.HxUser;
import me.zhengjie.domain.Product;
import me.zhengjie.union.handler.AbstractProductDbMatchHandler;
import me.zhengjie.union.vo.RegisterResp;
import me.zhengjie.union.vo.MatchResp;
import me.zhengjie.union.annotations.LoanProductMatchHandler;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * @Author: PuYunJian
 * @CreateTime: 2024-08-29  17:05
 * @Description: 营销产品对接我方文档-撞库
 */
@Slf4j
@LoanProductMatchHandler(name = "default-matcher")
public class DefaultMatchHandler extends AbstractProductDbMatchHandler<JSONObject> {

    private static final Integer SUCCESS_CODE = 0;


    @Override
    protected Map<String, Object> buildRequestParamMatch(Product product, HxUser user, me.zhengjie.union.config.properties.BaseLoanProductConfigProperties configProperties) {
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("phoneMd5", user.getPhoneMd5());
//        paramMap.put("city", capital.getCity());
//        paramMap.put("channelSign", product.getChaSign());
        log.info("营销产品:{}-对接我方文档撞库请求参数paramMap:{}", product.getProductName(), JSONUtil.toJsonStr(paramMap));
        return paramMap;
    }

    @Override
    protected Map<String, Object> buildRequestParamRegister(Product product, HxUser user, me.zhengjie.union.config.properties.BaseLoanProductConfigProperties configProperties) {
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("phoneMd5", user.getPhoneMd5());
//        paramMap.put("city", capital.getCity());
//        paramMap.put("channelSign", product.getChaSign());
        log.info("营销产品:{}-对接我方文档撞库请求参数paramMap:{}", product.getProductName(), JSONUtil.toJsonStr(paramMap));
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
        Integer code = response.getInt("code");
        String msg = response.getStr("message");
        // 撞库成功
        if (Objects.equals(SUCCESS_CODE, code)) {
            return MatchResp.success(productId, msg);
        } else {
            return MatchResp.fail(productId, msg);
        }
    }
    @Override
    protected RegisterResp responseProcessRegister(JSONObject response, Long productId, String phoneMd5) {
        Integer code = response.getInt("code");
        String msg = response.getStr("message");
        // 撞库成功
        if (Objects.equals(SUCCESS_CODE, code)) {
            return RegisterResp.success(productId,null);
        } else {
            return RegisterResp.fail(productId, msg);
        }
    }
}
