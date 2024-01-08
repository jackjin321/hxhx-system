package me.zhengjie.modules.util;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class TestClient_V1 {

    public static void main(String[] args) throws Exception {

        Map<String, String> map = new HashMap<>();
	//商户信息请联系官方提供
        map.put("appid", "6KJAG1ZJF63K161E66S3D7ZHN52NHFB5");
        map.put("secret", "pFeNMTyFhNRzHrCtNPzc25S7HxtSaR3j");
        map.put("timestamp", System.currentTimeMillis() + "");
        //商户号
        map.put("member_id", "0x0003838");//1658755286467
        //姓名
        map.put("name", "李四");
        //身份证
        map.put("id_no", "110102200101017072");
        //手机号
        map.put("phone_no", "13822286355");
        //订单号（保持唯一）
        map.put("trans_id", new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()) + RandomStringUtils.randomNumeric(16));
//        map.put("trans_id", "QZXY-166634dasdadre8UUJGMVPMhoOhW");


        // 按map的key字母排序
        Map<String, String> sortMap = MapSort.sortMapByKey(map);
        StringBuilder sb = new StringBuilder();
        for (String key : sortMap.keySet()) {
            sb.append(key).append("=").append(sortMap.get(key)).append("&");
        }
        // 计算签名
        String sign = MD5Util.string2MD5(sb.toString());
        map.put("sign", sign);
        // 移除密钥
        map.remove("secret");

        //call  black totaldebt overdue unify
        // 生成URL
        StringBuilder URL = new StringBuilder("http://test.gzkmwl.net/v1/sscore?");
        //StringBuilder URL = new StringBuilder("http://localhost:8081/v1/unify?");
        for (String key : map.keySet()) {
            URL.append("&");
            URL.append(key).append("=").append(map.get(key));
        }
        String url = URL.toString();
        System.out.println(url);
        //Thread.sleep(15000);
        HttpClientUtils.doGet(url);
    }

    /**
     * * 处理调用接口回参中的特殊字符
     * * @param str
     * * @return
     */
    public String ReplaceAll(String str) {
        String replaceAll3 = null;
        if (!StringUtils.isBlank(str)) {
            String replaceAll = str.replaceAll("\\\\", "");
            System.out.println(replaceAll);
            String replaceAll2 = replaceAll.replaceAll("\"[{]", "{");
            replaceAll3 = replaceAll2.replaceAll("[}]\"", "}");
        }

        return replaceAll3;
    }
}



