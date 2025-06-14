
package me.zhengjie.modules.app.service.impl;


import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.XmlUtil;
import cn.hutool.crypto.digest.DigestUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.zhengjie.access.RedisCacheKey;
import me.zhengjie.modules.app.service.AppUserService;
import me.zhengjie.utils.RandomUtil;
import me.zhengjie.utils.RedisUtils;
import me.zhengjie.utils.SecurityUtils;
import me.zhengjie.utils.StringUtils;
import me.zhengjie.vo.MemberAuth;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
@RequiredArgsConstructor
public class AppUserServiceImpl implements AppUserService {
    private static final String appCode = "3d76aa18e38a4c419bcd02366435b840";
    private static final String idCheckHost = "https://eid.shumaidata.com";
    private static final String path = "/eid/check";
    private static final String gateway = "https://openapi.danmi.com/";
    private static final String wayUrl = "/textSMS/sendSMS/batch/v1";
    private static final String signName = "助帮";
    private static final String accountId = "100140147453";
    private static final String accountSid = "91a2f1057c6075cd1c1470331855de9e";
    private static final String authToken = "25dcfec252e8a9d043ce49e87c067fba";


    private final RedisUtils redisUtils;

    @Override
    public boolean checkSmsCode(String phone, String code) {
        String loginCodeKey = RedisCacheKey.loginCodeKey(phone);
        // 查询验证码
        String cacheCode = (String) redisUtils.get(loginCodeKey);
        // 清除验证码
        //redisUtils.del(loginCodeKey);
        if (StringUtils.isBlank(code)) {
//            throw new BadRequestException("验证码不存在或已过期");
            return false;
        }
        if (code.equals(cacheCode)) {
//            redisValueTemplate.delete(loginCodeKey);
            redisUtils.del(loginCodeKey);
            return true;
        }
        return false;
    }

    @Override
    public boolean sendSmsCode(String phone) {
        // 保存
        double count = redisUtils.hincr("FLASH_CODE", phone, 1L);
        log.info("count {}", count);
        if (count > 10) {
            return false;
        }

        String code = RandomUtil.randomNumber(6);
        log.info("code {}", code);
        //发送短信，需要重新找一个渠道
//        boolean flag = sendFanQin(phone, code);
        boolean flag = sendFanQinV2(phone, code);
        if (flag) {
            redisUtils.del(RedisCacheKey.loginCodeKey(phone));
            // 保存
            redisUtils.set(RedisCacheKey.loginCodeKey(phone), code, 5, TimeUnit.MINUTES);
        }
        return flag;
    }

    /**
     * $reqData=[
     * "accountSid"=>self::$danmi["accountSid"],
     * "accountId"=>self::$danmi["accountId"],
     * "sig"=>md5(self::$danmi["accountSid"].self::$danmi["authToken"].$milliseconds),
     * "timestamp"=>$milliseconds,
     * "dataList"=>[
     * [
     * "to"=>$number,
     * "templateid"=>234234,
     * "smsContent"=>"【".self::$danmi["signName"]."】您的验证码是：".$code."，两分钟内有效，请勿透露给他人"
     * ]
     * ]
     * ];
     *
     * @param phone
     * @param code
     * @return
     */
    @Override
    public boolean sendFanQin(String phone, String code) {
        //return sendSms(phone, code, SmsEnum.FanQin);
        long currentTimeMillis = DateUtil.current();
        String sig = DigestUtil.md5Hex(String.format("%s%s%d", accountSid, authToken, currentTimeMillis));
        cn.hutool.json.JSONObject param = JSONUtil.createObj();
        param.put("accountSid", accountSid);
        param.put("accountId", accountId);
        param.put("sig", sig);
        param.put("timestamp", currentTimeMillis);
        cn.hutool.json.JSONArray dataList = JSONUtil.createArray();
        cn.hutool.json.JSONObject data = JSONUtil.createObj();
        data.put("to", phone);
        data.put("templateid", 234234);
        data.put("smsContent", String.format("【%s】您的验证码是：%s，两分钟内有效，请勿透露给他人", signName, code));
        dataList.put(data);
        param.put("dataList", dataList);
        String url = gateway + wayUrl;
        HttpResponse response = HttpRequest.post(url)
                .body(JSONUtil.toJsonStr(param), "application/json")
                .execute();
        int status = response.getStatus();
        String body = response.body();
        cn.hutool.json.JSONObject result = JSONUtil.parseObj(body);
//        System.out.println(result);
        log.info("{} request {}; response {}", phone, JSONUtil.toJsonStr(param), body);
        if ("0000".equals(result.getStr("respCode"))) {
            return true;
        }
        //{"success":true,"respCode":"0000","respDesc":"请求成功。","smsId":"ia1868011096980733952","failList":[]}
        return false;
    }

    @Override
    public boolean sendFanQinV2(String phone, String code) {
        //return sendSms(phone, code, SmsEnum.FanQin);
        //long currentTimeMillis = DateUtil.current();
        //String sig = DigestUtil.md5Hex(String.format("%s%s%d", accountSid, authToken, currentTimeMillis));
        Map<String, Object> param = new HashMap<>();
        param.put("userid", 257);
        param.put("account", "chxxyzm");
        param.put("password", "123456Ch");
        param.put("mobile", phone);
        param.put("content", String.format("【臣禾信息】您的验证码为：%s，请在5分钟内输入，请勿转发或告知他人，谨防诈骗。", code));
        param.put("action", "send");
        param.put("sendTime", "");
        param.put("extno", "");

        HttpResponse response = HttpUtil.createPost("http://121.40.72.241:8868/sms.aspx").form(param).execute();

        int status = response.getStatus();
        String body = response.body();
        //System.out.println(body);
        log.info("{} request {}; response {}", phone, JSONUtil.toJsonStr(param), body);
        Document document = XmlUtil.parseXml(body);
        // 获取根元素
        Element root = document.getDocumentElement();
        // 获取<returnstatus>节点的内容
        Node toNode = root.getElementsByTagName("returnstatus").item(0); // 获取第一个<to>节点
        if (toNode != null && toNode.getNodeType() == Node.ELEMENT_NODE) {
            Element toElement = (Element) toNode;
            String returnStatus = toElement.getTextContent(); // 获取<to>节点的文本内容
            System.out.println("To: " + returnStatus); // 输出结果
            if ("Success".equals(returnStatus)) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

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
