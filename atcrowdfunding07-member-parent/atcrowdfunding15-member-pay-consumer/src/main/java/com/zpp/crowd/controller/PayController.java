package com.zpp.crowd.controller;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.api.request.AlipayTradePagePayRequest;
import com.zpp.crowd.api.MySQLRemoteService;
import com.zpp.crowd.config.PayProperties;
import com.zpp.crowd.constant.CrowdConstant;
import com.zpp.crowd.entity.vo.OrderProjectVO;
import com.zpp.crowd.entity.vo.OrderVO;
import com.zpp.crowd.util.ResultEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author : Zpp
 * @Date : 2022/11/11-13:15
 */
@Slf4j
@Controller
public class PayController {

    @Autowired
    private PayProperties payProperties;

    @Autowired
    private MySQLRemoteService mySQLRemoteService;

    @ResponseBody
    @RequestMapping("/generate/order")
    public String generateOrder(HttpSession session, OrderVO orderVo) throws AlipayApiException {

        // 从session域中取出 orderProjectVO
        OrderProjectVO orderProjectVO = (OrderProjectVO) session.getAttribute(CrowdConstant.ATTR_NAME_ORDER_PROJECT);

        // 组装对象
        orderVo.setOrderProjectVO(orderProjectVO);

        // 生成订单号
        // 生成当前日期 生成字符串
        String time = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());

        // 生成用户id
        String user = UUID.randomUUID().toString().replace("-", "").toUpperCase();

        String orderNum = time + user;

        orderVo.setOrderNum(orderNum);

        // 计算总金额
        Integer freight = orderProjectVO.getFreight();// 运费
        Integer supportPrice = orderProjectVO.getSupportPrice(); // 价格
        Integer returnCount = orderProjectVO.getReturnCount(); // 数量

        orderVo.setOrderAmount((double) (supportPrice * returnCount + freight));

        // 将 orderVO放到session域中

        session.setAttribute(CrowdConstant.ATTR_NAME_ORDER_VO,orderVo);


        // 向支付宝接口发送请求
        return sendRequestTOAliPay(orderNum, orderVo.getOrderAmount(), orderProjectVO.getProjectName(), orderProjectVO.getReturnContent());

    }


    /**
     * 调用支付宝接口的方法
     *
     * @param outTradeNo  商户订单号
     * @param totalAmount 商品金额
     * @param subject     订单的标题 -- 项目名
     * @param body        商品的描述 --回报描述
     * @return 返回的支付宝登录界面
     * @throws AlipayApiException
     */
    private String sendRequestTOAliPay(String outTradeNo, Double totalAmount, String subject, String body) throws AlipayApiException {
        //获得初始化的AlipayClient
        AlipayClient alipayClient = new DefaultAlipayClient(payProperties.getGatewayUrl(),
                payProperties.getAppId(), payProperties.getMerchantPrivateKey(),
                "json", payProperties.getCharset(),
                payProperties.getAlipayPublicKey(), payProperties.getSignType());

        //设置请求参数
        AlipayTradePagePayRequest alipayRequest = new AlipayTradePagePayRequest();
        alipayRequest.setReturnUrl(payProperties.getReturnUrl());
        alipayRequest.setNotifyUrl(payProperties.getNotifyUrl());

        alipayRequest.setBizContent("{\"out_trade_no\":\"" + outTradeNo + "\","
                + "\"total_amount\":\"" + totalAmount + "\","
                + "\"subject\":\"" + subject + "\","
                + "\"body\":\"" + body + "\","
                + "\"product_code\":\"FAST_INSTANT_TRADE_PAY\"}");

        //若想给BizContent增加其他可选请求参数，以增加自定义超时时间参数timeout_express来举例说明
        //alipayRequest.setBizContent("{\"out_trade_no\":\""+ out_trade_no +"\","
        //		+ "\"total_amount\":\""+ total_amount +"\","
        //		+ "\"subject\":\""+ subject +"\","
        //		+ "\"body\":\""+ body +"\","
        //		+ "\"timeout_express\":\"10m\","
        //		+ "\"product_code\":\"FAST_INSTANT_TRADE_PAY\"}");
        //请求参数可查阅【电脑网站支付的API文档-alipay.trade.page.pay-请求参数】章节

        //请求
        String result = alipayClient.pageExecute(alipayRequest).getBody();

        return result;
    }

    @RequestMapping("/notify")
    public void notifyUrlMethod(HttpServletRequest request) throws UnsupportedEncodingException, AlipayApiException {
        //获取支付宝POST过来反馈信息
        Map<String, String> params = new HashMap<String, String>();
        Map<String, String[]> requestParams = request.getParameterMap();
        for (Iterator<String> iter = requestParams.keySet().iterator(); iter.hasNext(); ) {
            String name = (String) iter.next();
            String[] values = (String[]) requestParams.get(name);
            String valueStr = "";
            for (int i = 0; i < values.length; i++) {
                valueStr = (i == values.length - 1) ? valueStr + values[i]
                        : valueStr + values[i] + ",";
            }
            //乱码解决，这段代码在出现乱码时使用
            valueStr = new String(valueStr.getBytes("ISO-8859-1"), "utf-8");
            params.put(name, valueStr);
        }
        boolean signVerified = AlipaySignature.rsaCheckV1(params,
                payProperties.getAlipayPublicKey(),
                payProperties.getCharset(),
                payProperties.getSignType()); //调用SDK验证签名


	/* 实际验证过程建议商户务必添加以下校验：
	1、需要验证该通知数据中的out_trade_no是否为商户系统中创建的订单号，
	2、判断total_amount是否确实为该订单的实际金额（即商户订单创建时的金额），
	3、校验通知中的seller_id（或者seller_email) 是否为out_trade_no这笔单据的对应的操作方（有的时候，一个商户可能有多个seller_id/seller_email）
	4、验证app_id是否为该商户本身。
	*/
        if (signVerified) {//验证成功
            //商户订单号
            String out_trade_no = new String(request.getParameter("out_trade_no").getBytes("ISO-8859-1"), "UTF-8");

            //支付宝交易号
            String trade_no = new String(request.getParameter("trade_no").getBytes("ISO-8859-1"), "UTF-8");

            //交易状态
            String trade_status = new String(request.getParameter("trade_status").getBytes("ISO-8859-1"), "UTF-8");

            log.info("trade_status:{}",trade_status);

        } else {//验证失败

            log.info("trade_status:{}","验证失败！");

        }

    }


    //@ResponseBody
    @RequestMapping("/return")
    public String returnUrlMethod(HttpServletRequest request,HttpSession session) throws UnsupportedEncodingException, AlipayApiException {
        //获取支付宝GET过来反馈信息
        Map<String, String> params = new HashMap<String, String>();
        Map<String, String[]> requestParams = request.getParameterMap();
        for (Iterator<String> iter = requestParams.keySet().iterator(); iter.hasNext(); ) {
            String name = (String) iter.next();
            String[] values = (String[]) requestParams.get(name);
            String valueStr = "";
            for (int i = 0; i < values.length; i++) {
                valueStr = (i == values.length - 1) ? valueStr + values[i]
                        : valueStr + values[i] + ",";
            }
            //乱码解决，这段代码在出现乱码时使用
            valueStr = new String(valueStr.getBytes("ISO-8859-1"), "utf-8");
            params.put(name, valueStr);
        }

        boolean signVerified = AlipaySignature.rsaCheckV1(params,
                payProperties.getAlipayPublicKey(),
                payProperties.getCharset(),
                payProperties.getSignType()); //调用SDK验证签名


        if (signVerified) {
            //商户订单号
            String orderNum = new String(request.getParameter("out_trade_no").getBytes("ISO-8859-1"), "UTF-8");

            //支付宝交易号
            String payOrderNum = new String(request.getParameter("trade_no").getBytes("ISO-8859-1"), "UTF-8");

            //付款金额
            String orderAmount = new String(request.getParameter("total_amount").getBytes("ISO-8859-1"), "UTF-8");

            // 保存到数据库

            // 从session域中获取 orderv对象
            OrderVO orderVo = (OrderVO) session.getAttribute(CrowdConstant.ATTR_NAME_ORDER_VO);

            orderVo.setPayOrderNum(payOrderNum);// 设置支付宝交易的订单号
            // 执行保存
            ResultEntity<String> resultEntity = mySQLRemoteService.saveOrderRemote(orderVo);

             return "redirect:http://localhost/project/get/project/detail/"+orderVo.getOrderProjectVO().getProjectId();

            // 测试
           // return "trade_no:" + orderNum + "<br/>out_trade_no:" + payOrderNum + "<br/>total_amount:" + orderAmount;

        } else {

            return "验签失败！";
        }

    }

}
