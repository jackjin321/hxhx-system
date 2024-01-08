package me.zhengjie.modules.util;


import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.logging.Logger;

/**
 * @Auther: sai
 * @Date: 2022/6/14 0014 22:24
 * @ClassName: HttpClientUtils
 * @Version: 1.0
 * @Description:
 */
public class HttpClientUtils {

    Logger log = Logger.getLogger("com.test.httpclient.HttpClientUtils");

    public static void doGet(String url) {
        try {
            CloseableHttpClient client = HttpClients.createDefault();
            // 发送get请求
            HttpGet request = new HttpGet(url);
            // 发送请求
            HttpResponse httpResponse = client.execute(request);


            // 验证请求是否成功
            if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                // 得到请求响应信息
                String str = EntityUtils.toString(httpResponse.getEntity(), "utf-8");
                // 返回json
                System.out.println(str);
            }
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
