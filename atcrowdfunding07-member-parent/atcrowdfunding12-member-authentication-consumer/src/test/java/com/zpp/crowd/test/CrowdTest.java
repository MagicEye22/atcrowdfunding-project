package com.zpp.crowd.test;

import com.zpp.crowd.util.HttpUtils;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.util.EntityUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashMap;
import java.util.Map;

/**
 * @author : Zpp
 * @Date : 2022/10/26-21:05
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class CrowdTest {


    @Test
    public void test1() {
      String host = "https://wwsms.market.alicloudapi.com";
        String path = "/send_sms";
        String method = "POST";
        String appcode = "bb0965dafdcb407fa3695a38992acebf";
        Map<String, String> headers = new HashMap<String, String>();
        //最后在header中的格式(中间是英文空格)为Authorization:APPCODE 83359fd73fe94948385f570e3c139105
        headers.put("Authorization", "APPCODE " + appcode);
        //根据API的要求，定义相对应的Content-Type
        headers.put("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
        Map<String, String> querys = new HashMap<String, String>();
        Map<String, String> bodys = new HashMap<String, String>();
        bodys.put("content", "code:4399,expire_at:120");
        bodys.put("phone_number", "1734411245322");
        bodys.put("template_id", "TPL_0001");


        try {
            /**
             * 重要提示如下:
             * HttpUtils请从
             * https://github.com/aliyun/api-gateway-demo-sign-java/blob/master/src/main/java/com/aliyun/api/gateway/demo/util/HttpUtils.java
             * 下载
             *
             * 相应的依赖请参照
             * https://github.com/aliyun/api-gateway-demo-sign-java/blob/master/pom.xml
             */
            HttpResponse response = HttpUtils.doPost(host, path, method, headers, querys, bodys);

            StatusLine statusLine = response.getStatusLine();

            // 状态码：
            // 400: INVALID_ARGUMENT 请求参数错误
            // 403: RATE_LIMIT_EXCEEDED / Quota Exhausted  触发限发机制（10分钟发3条短信）/ 套餐余额用完
            // 500: INTERNAL_ERROR  服务器内部错误
            System.out.println(statusLine.getStatusCode());

            System.out.println(statusLine.getReasonPhrase());

           // System.out.println(response.toString());
            //获取response的body
            System.out.println(EntityUtils.toString(response.getEntity()));
            response.getEntity();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public  void test2(){
        String s="$2a$10$iz6yIS78uLpbpHtg3T6sQO/d1kb4hnuW47feeo5NhgvVE66yxcgS.";
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        String encode = bCryptPasswordEncoder.encode("123123");
        System.out.println(bCryptPasswordEncoder.matches(encode,s));


    }
}
