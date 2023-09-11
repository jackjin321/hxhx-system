
package me.zhengjie.modules.app.service.impl;


import cn.hutool.core.util.ObjectUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.zhengjie.modules.app.service.AppUserService;
import me.zhengjie.vo.MemberAuth;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class AppUserServiceImpl implements AppUserService {
    private static final String appCode = "8e9a2f3851e642bf904c3f72ba3a29f5";

    private static final String idCheckHost = "https://eid.shumaidata.com";
    private static final String path = "/eid/check";

    @Override
    public String checkIdentity(MemberAuth memberAuth) {
        //String reqId = StringUtil.genReqId();

        //Map<String, String> headers = new HashMap<String, String>();
        //最后在header中的格式(中间是英文空格)为Authorization:APPCODE 83359fd73fe94948385f570e3c139105
        //headers.put("Authorization", "APPCODE " + appCode);
        Map<String, Object> querys = new HashMap<String, Object>();
        querys.put("idcard", memberAuth.getIdCard());
        querys.put("name", memberAuth.getRealName());

        /**
         * {
         *     "code": "0", //返回码，0：成功，非0：失败（详见错误码定义）
         *           //当code=0时，再判断下面result中的res；当code!=0时，表示调用已失败，无需再继续
         *     "message": "成功", //返回码说明
         *     "result": {
         *         "name": "冯天", //姓名
         *         "idcard": "350301198011129422", //身份证号
         *         "res": "1", //核验结果状态码，1 一致；2 不一致；3 无记录
         *         "description": "一致",  //核验结果状态描述
         *        "sex": "男",
         *         "birthday": "19940320",
         *         "address": "江西省南昌市东湖区"
         *     }
         * }
         *
         * 失败返回示例
         * {
         *     "code": "20310", //指返回结果码，并非http状态码
         *     "message": "请求姓名不标准：姓名为空或者包含特殊字符",
         *     "result": {}
         * }
         */
        //校验结果（1：验证一致,0：验证不一致,2:此身份证号不存在）
        String checkResult = null;

        String message = "";
        try {
            //HttpResponse response = HttpUtils.doGet(idCheckHost, path, method, headers, querys);
            HttpResponse getResponse = HttpRequest.post(idCheckHost + path)
                    .header("Authorization", "APPCODE " + appCode).form(querys).execute();
            //获取response的body
            int status1 = getResponse.getStatus();
            System.out.println("请求响应状态码:" + status1);
            String result = getResponse.body();
            //System.out.println(result);
            log.info("二要素接口结果 {}", result);
            //memberAuthCheck.setApiResult(result);
            JSONObject entity = JSON.parseObject(result);
            String code = entity.getString("code");//不同状态的码含义
            message = entity.getString("message");//不同状态的码含义
            if ("0".equals(code)) {
                JSONObject dataMap = entity.getJSONObject("result");
                String retCode = dataMap.getString("res"); //核验结果状态码，1 一致；2 不一致；3 无记录
                //memberAuthCheck.setStatus(Integer.valueOf(retCode));
                if ("1".equals(retCode)) {
                    //checkResult = true;
                    checkResult = null;
                } else {
                    //checkResult = false;
                    checkResult = "姓名与身份证不一致";
                }
            } else {
                ///memberAuthCheck.setStatus(-1);
                checkResult = message;
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.info("二要素接口 网络异常");
            //memberAuthCheck.setStatus(-1);
            checkResult = "系统内部错误";
        } finally {
            //memberAuthCheck.setResult(message);
            //memberAuthService.saveOrUpdate(memberAuthCheck);
        }
        return checkResult;
    }

}
