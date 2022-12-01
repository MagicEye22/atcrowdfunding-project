package com.zpp.crowd.util;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.common.comm.ResponseMessage;
import com.aliyun.oss.model.PutObjectResult;
import com.zpp.crowd.constant.CrowdConstant;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.util.EntityUtils;

import javax.servlet.http.HttpServletRequest;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author : Zpp
 * @Date : 2022/9/2-22:16
 */

/**
 * 项目通用的工具类
 */
public class CrowdUtil {

    /**
     *  专门负责上传文件到 OSS 服务器的工具方
     * @param endpoint OSS 参数
     * @param accessKeyId  OSS 参数
     * @param accessKeySecret  OSS 参数
     * @param inputStream  要上传的文件的输入流
     * @param bucketName  OSS 参数
     * @param bucketDomain  OSS 参数
     * @param originalName  要上传的文件的原始文件名
     * @return  包含上传结果以及上传的文件在 OSS 上的访问路
     */
    public static ResultEntity<String> uploadFileToOss(
            String endpoint,
            String accessKeyId,
            String accessKeySecret,
            InputStream inputStream,
            String bucketName,
            String bucketDomain,
            String originalName) {
        // 创建 OSSClient 实例。
        OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);
        // 生成上传文件的目录
        String folderName = new SimpleDateFormat("yyyyMMdd").format(new Date());
        // 生成上传文件在 OSS 服务器上保存时的文件名
        // 原始文件名：beautfulgirl.jpg
        // 生成文件名：wer234234efwer235346457dfswet346235.jpg
        // 使用 UUID 生成文件主体名称
        String fileMainName = UUID.randomUUID().toString().replace("-", "");
        // 从原始文件名中获取文件扩展名
        String extensionName = originalName.substring(originalName.lastIndexOf("."));
        // 使用目录、文件主体名称、文件扩展名称拼接得到对象名称
        String objectName = folderName + "/" + fileMainName + extensionName;

        try {
            // 调用 OSS 客户端对象的方法上传文件并获取响应结果数据
            PutObjectResult putObjectResult = ossClient.putObject(bucketName, objectName,
                    inputStream);
            // 从响应结果中获取具体响应消息
            ResponseMessage responseMessage = putObjectResult.getResponse();
            // 根据响应状态码判断请求是否成功
            if (responseMessage == null) {
                // 拼接访问刚刚上传的文件的路径
                String ossFileAccessPath = bucketDomain + "/" + objectName;
                // 当前方法返回成功
                return ResultEntity.successWithoutData(ossFileAccessPath);
            } else {
                // 获取响应状态码
                int statusCode = responseMessage.getStatusCode();
                // 如果请求没有成功，获取错误消息
                String errorMessage = responseMessage.getErrorResponseAsString();
                // 当前方法返回失败
                return ResultEntity.failed(" 当 前 响 应 状 态 码 =" + statusCode + " 错 误 消 息 =" + errorMessage);
            }
        } catch (Exception e) {
            e.printStackTrace();
            // 当前方法返回失败
            return ResultEntity.failed(e.getMessage());
        } finally {
            if (ossClient != null) {
                // 关闭 OSSClient。
                ossClient.shutdown();
            }
        }


    }



    /**
     * 发送验证码工具方法
     *
     * @param phoneNumber 手机号
     * @param appCode     购买的短信接口服务的appcode
     * @param templateId  短信模板id
     * @param host        短信接口调用的URL地址
     * @param path        具体发送短信功能的地址
     * @param method      请求方式
     * @return 返回调用结果  成功：返回验证码  失败；返回错误消息
     * 状态码：
     * 200： ok  正常
     * 400: INVALID_ARGUMENT 请求参数错误
     * 403: RATE_LIMIT_EXCEEDED / Quota Exhausted  触发限发机制（10分钟发3条短信）/ 套餐余额用完
     * 500: INTERNAL_ERROR  服务器内部错误
     */
    public static ResultEntity<String> sendAuthCodeByShortMessage(
            String phoneNumber,
            String appCode,
            String templateId,
            String host,
            String path,
            String method) {

        Map<String, String> headers = new HashMap<String, String>();

        //最后在header中的格式(中间是英文空格)为Authorization:APPCODE 83359fd73fe94948385f570e3c139105
        headers.put("Authorization", "APPCODE " + appCode);

        //根据API的要求，定义相对应的Content-Type
        headers.put("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");

        Map<String, String> querys = new HashMap<String, String>();

        Map<String, String> bodys = new HashMap<String, String>();

        StringBuilder authCode = new StringBuilder();
        // 生成验证码
        for (int i = 0; i < 4; i++) {
            int random = (int) (Math.random() * 10);
            authCode.append(random);
        }

        bodys.put("content", "code:" + authCode + ",expire_at:5");
        bodys.put("phone_number", phoneNumber);
        bodys.put("template_id", templateId);

        try {

            HttpResponse response = HttpUtils.doPost(host, path, method, headers, querys, bodys);

            StatusLine statusLine = response.getStatusLine();

            // 状态码
            int statusCode = statusLine.getStatusCode();

            // 消息
            String reasonPhrase = statusLine.getReasonPhrase();

            // 短信接口实际返回的 状态json消息
//            String returnStatus = EntityUtils.toString(response.getEntity());
//            HttpEntity entity = response.getEntity();
//            InputStream content = entity.getContent();
            if (statusCode == 200) {

                return ResultEntity.successWithoutData(authCode.toString());

            }

            // 不为200时的返回
            return ResultEntity.failed(reasonPhrase);

        } catch (Exception e) {

            e.printStackTrace();

            // 出现异常时的返回
            return ResultEntity.failed(e.getMessage());
        }
    }


    /**
     * 判断当前请求是否为ajax请求
     *
     * @param request 请求对象
     * @return true ：ajax请求
     * false：不是ajax请求
     */
    public static boolean judgeRequestType(HttpServletRequest request) {
        // 获取请求消息头
        String acceptHeader = request.getHeader("Accept");
        String xRequestedHeader = request.getHeader("X-Requested-With");

        // 判断
        return (acceptHeader != null && acceptHeader.contains("application/json")) ||
                (xRequestedHeader != null && xRequestedHeader.equals("XMLHttpRequest"));
    }


    /**
     * 对明文字符串进行MD5加密
     *
     * @param source 传入的明文字符串
     * @return 加密后的结果
     */
    public static String MD5(String source) {
        // 判断source
        if (source == null || source.length() == 0) {
            //如果不是有效的字符串
            throw new RuntimeException(CrowdConstant.MESSAGE_STRING_INVALIDATE);
        }
        try {
            // 获取MessageDigest对象
            String algorithm = "md5";
            MessageDigest messageDigest = MessageDigest.getInstance(algorithm);

            // 获取明文字符串对应的字节数组
            byte[] input = source.getBytes();

            // 执行加密

            byte[] output = messageDigest.digest(input);

            // 创建BigInteger对象
            int signum = 1;
            BigInteger bigInteger = new BigInteger(signum, output);

            // 按照16进制将bigInteger的值转换为字符串
            int radix = 16;
            String encoded = bigInteger.toString(radix).toUpperCase();

            return encoded;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        return null;
    }
}
