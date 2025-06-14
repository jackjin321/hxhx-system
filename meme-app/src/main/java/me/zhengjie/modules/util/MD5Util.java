package me.zhengjie.modules.util;

import cn.hutool.core.util.XmlUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONUtil;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import java.security.MessageDigest;
import java.util.HashMap;
import java.util.Map;

/**
 * @Auther: sai
 * @Date: 2022/6/15 0015 22:35
 * @ClassName: MD5Util
 * @Version: 1.0
 * @Description: 用MD5加密解密
 */
public class MD5Util {

    /***
     * MD5加码 生成32位md5码
     */
    public static String string2MD5(String inStr){
        MessageDigest md5 = null;
        try{
            md5 = MessageDigest.getInstance("MD5");
        }catch (Exception e){
            System.out.println(e.toString());
            e.printStackTrace();
            return "";
        }
        char[] charArray = inStr.toCharArray();
        byte[] byteArray = new byte[charArray.length];

        for (int i = 0; i < charArray.length; i++)
            byteArray[i] = (byte) charArray[i];
        byte[] md5Bytes = md5.digest(byteArray);
        StringBuffer hexValue = new StringBuffer();
        for (int i = 0; i < md5Bytes.length; i++){
            int val = ((int) md5Bytes[i]) & 0xff;
            if (val < 16)
                hexValue.append("0");
            hexValue.append(Integer.toHexString(val));
        }
        return hexValue.toString();

    }

    /**
     * 加密解密算法 执行一次加密，两次解密
     */
    public static String convertMD5(String inStr){

        char[] a = inStr.toCharArray();
        for (int i = 0; i < a.length; i++){
            a[i] = (char) (a[i] ^ 't');
        }
        String s = new String(a);
        return s;

    }
//     //测试主函数
    public static void main(String args[]) {
//        String s = new String("tangfuqiang");
//        System.out.println("原始：" + s);
//        System.out.println("MD5后：" + string2MD5(s));  // 20b75697d8bf931a6730662ae117c3bf
//        System.out.println("加密的：" + convertMD5(s));
//        System.out.println("解密的f：" + convertMD5(convertMD5(s)));

        Map<String, Object> param = new HashMap<>();
        param.put("userid", 257);
        param.put("account", "chxxyzm");
        param.put("password", "123456Ch");
        param.put("mobile", "18966496840");
        param.put("content", String.format("【臣禾信息】您的验证码为：%s，请在5分钟内输入，请勿转发或告知他人，谨防诈骗。", "123456"));
        param.put("action", "send");
        param.put("sendTime", "");
        param.put("extno", "");

        HttpResponse response = HttpUtil.createPost("http://121.40.72.241:8868/sms.aspx").form(param).execute();

        int status = response.getStatus();
        String body = response.body();
        System.out.println(body);
        Document document = XmlUtil.parseXml(body);
        // 获取根元素
        Element root = document.getDocumentElement();
        // 获取<returnstatus>节点的内容
        Node toNode = root.getElementsByTagName("returnstatus").item(0); // 获取第一个<to>节点
        if (toNode != null && toNode.getNodeType() == Node.ELEMENT_NODE) {
            Element toElement = (Element) toNode;
            String returnStatus = toElement.getTextContent(); // 获取<to>节点的文本内容
            System.out.println("To: " + returnStatus); // 输出结果
        }
//        cn.hutool.json.JSONObject param = JSONUtil.createObj();
//        param.put("userid", 257);
//        param.put("account", "chxxyzm");
//        param.put("password", "123456Ch");
//        param.put("mobile", "18966496840");
//        param.put("content", String.format("【臣禾信息】您的验证码为：%s，请在5分钟内输入，请勿转发或告知他人，谨防诈骗。", "123456"));
//        param.put("action", "send");
//        param.put("sendTime", "");
//        param.put("extno", "");
////        String url = gateway + wayUrl;
//        System.out.println(JSONUtil.toJsonStr(param));
//        HttpResponse response = HttpRequest.post("http://121.40.72.241:8868/sms.aspx")
//                .body(JSONUtil.toJsonStr(param), "application/json")
//                .execute();
//        int status = response.getStatus();
//        String body = response.body();
//        System.out.println(body);
//        cn.hutool.json.JSONObject result = JSONUtil.parseObj(body);
//        System.out.println(result);
        //log.info("{} request {}; response {}", phone, JSONUtil.toJsonStr(param), body);
//        if ("0000".equals(result.getStr("respCode"))) {
//            //return true;
//        }
    }
}

