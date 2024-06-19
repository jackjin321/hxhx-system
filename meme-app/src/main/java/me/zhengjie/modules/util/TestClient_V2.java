package me.zhengjie.modules.util;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicHeader;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class TestClient_V2 {

    public final static String memberId = "";
    public final static String appid = "";
    public final static String scerect = "";

    public static void main(String[] args) throws Exception {
        JSONObject json = new JSONObject();
        json.put("member_id", "0x0003838");
        json.put("accessoryUrl", "https://www.baidu.com/s?ie=UTF-8&wd=baidu");
        json.put("timestamp", System.currentTimeMillis() + "");

        json.put("trans_id", new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()) + RandomStringUtils.randomNumeric(16));

        JSONObject data = new JSONObject();
        data.put("cert_name", "李四"); //李四
        data.put("cert_no", "110102200101017072");//110102200101017072
        data.put("mobile", "13866666666");//13866666666
        System.out.println("===============AES模式 begin===============");
        System.out.println("加密前：" + data);
        String aes = AesUtils.encrypt("pFeNMTyFhNRzHrCtNPzc25S7HxtSaR3j", "6KJAG1ZJF63K161E66S3D7ZHN52NHFB5".substring(0, 16), data.toString());
        System.out.println("加密后：" + aes);
        String d1 = AesUtils.decrypt("pFeNMTyFhNRzHrCtNPzc25S7HxtSaR3j", "6KJAG1ZJF63K161E66S3D7ZHN52NHFB5".substring(0, 16), aes);
        json.put("data", aes);
        System.out.println("请求参数:" + json);
        System.out.println("解密后：" + d1);
        System.out.println("===============AES模式 end===============");

        json.put("data", aes);
        System.out.println("请求参数:" + json);

        //------------------------------------
        String send = send("http://8.134.11.49:8183/v2/api/pd/prodService/unify", json);
        System.out.println("返回结果：" + send);

        JSONObject object = JSONObject.parseObject(send);
        String aesData = AesUtils.decrypt("pFeNMTyFhNRzHrCtNPzc25S7HxtSaR3j", "6KJAG1ZJF63K161E66S3D7ZHN52NHFB5".substring(0, 16), object.get("data").toString());
        System.out.println("解密数据：" + aesData);
    }


    public static String checkResult(String transId, String realName, String idCard, String phone) throws Exception {
        JSONObject json = new JSONObject();
        json.put("member_id", memberId);
        json.put("accessoryUrl", "https://www.baidu.com/s?ie=UTF-8&wd=baidu");
        json.put("timestamp", System.currentTimeMillis() + "");

        json.put("trans_id", transId);

        JSONObject data = new JSONObject();
        data.put("cert_name", realName); //李四
        data.put("cert_no", idCard);//110102200101017072
        data.put("mobile", phone);//13866666666
        System.out.println("===============AES模式 begin===============");
        System.out.println("加密前：" + data);
        String aes = AesUtils.encrypt(scerect, appid.substring(0, 16), data.toString());
        System.out.println("加密后：" + aes);
        String d1 = AesUtils.decrypt(scerect, appid.substring(0, 16), aes);
        json.put("data", aes);
        System.out.println("请求参数:" + json);
        System.out.println("解密后：" + d1);
        System.out.println("===============AES模式 end===============");

        json.put("data", aes);
        System.out.println("请求参数:" + json);

        //------------------------------------
//        String send = send("http://8.134.11.49:8183/v2/api/pd/prodService/unify", json);
        String send = send("http://api.gzkmwl.net/v2/api/pd/prodService/unify", json);
        System.out.println("返回结果：" + send);

        JSONObject object = JSONObject.parseObject(send);
        String aesData = AesUtils.decrypt(scerect, appid.substring(0, 16), object.get("data").toString());
        System.out.println("解密数据：" + aesData);
        return aesData;
    }


    /**
     * 发送post请求
     *
     * @param url        路径
     * @param jsonObject 参数(json类型)
     * @return
     */
    public static String send(String url, JSONObject jsonObject) throws Exception {
        String body = "";

        //创建httpclient对象
        CloseableHttpClient client = HttpClients.createDefault();
        //创建post方式请求对象
        HttpPost httpPost = new HttpPost(url);

        //装填参数
        StringEntity s = new StringEntity(jsonObject.toString(), "utf-8");
        System.out.println(jsonObject.toString());
        s.setContentEncoding(new BasicHeader(HTTP.CONTENT_TYPE,
                "application/json"));
        //设置参数到请求对象中
        httpPost.setEntity(s);

        //设置header信息
        httpPost.setHeader("Content-Type", "application/json");
        //执行请求操作，并拿到结果（同步阻塞）
        CloseableHttpResponse response = client.execute(httpPost);
        //获取结果实体
        HttpEntity entity = response.getEntity();
        if (entity != null) {
            //按指定编码转换结果实体为String类型
            body = EntityUtils.toString(entity, "UTF-8");
        }
        EntityUtils.consume(entity);
        //释放链接
        response.close();
        return body;
    }
}



