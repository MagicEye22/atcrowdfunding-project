package com.zpp.crowd.message;

import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;

import java.util.HashMap;
import java.util.Map;

public class Tools {
    public static void main(String[] args) {

        // 短信接口调用的URL地址
        String host = "https://zwp.market.alicloudapi.com";
        // 具体发送短信功能的地址
        String path = "/sms/sendv2";
        // 请求方式
        String method = "GET";
        // 阿里云控制台中购买的短信服务接口的商品信息中
        String appcode = "bb0965dafdcb407fa3695a38992acebf";

        Map<String, String> headers = new HashMap<String, String>();
        //最后在header中的格式(中间是英文空格)为Authorization:APPCODE 83359fd73fe94948385f570e3c139105
        headers.put("Authorization", "APPCODE " + appcode);
        // 封装其他的参数
        Map<String, String> querys = new HashMap<String, String>();

        // 短信的内容 包括验证码

        querys.put("content", "【儿童教务】您正在登录验证,验证码为3256 ,60s内有效,请尽快验证。");
        // 接收短信的手机号
        querys.put("mobile", "17344245322");


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
            HttpResponse response = HttpUtils.doGet(host, path, method, headers, querys);
            System.out.println(response.toString());
            //获取response的body
            System.out.println(EntityUtils.toString(response.getEntity()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}