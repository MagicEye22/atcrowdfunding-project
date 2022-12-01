package com.zpp.crowd.mvc.Controller;

import com.zpp.crowd.entity.Admin;
import com.zpp.crowd.service.api.AdminService;
import com.zpp.crowd.util.CrowdUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.util.List;

/**
 * @author : Zpp
 * @Date : 2022/8/30-23:17
 */
@Deprecated
//@Controller
public class TestController {


    @Autowired
    private AdminService adminService;

    private Logger logger = LoggerFactory.getLogger(TestController.class);
   /* @RequestMapping("/test/ssm.html")
    public  String test(Model model){
       List<Admin> admins = adminService.getAll();
        model.addAttribute("admins",admins);
        return "target";
    }*/



    @RequestMapping("/test/ssm")
    public String test2(Model model, HttpServletRequest request,@RequestParam("user")String s) throws UnsupportedEncodingException {
        request.setCharacterEncoding("utf-8");
        System.out.println(s);
        boolean b = CrowdUtil.judgeRequestType(request);
        logger.info("ajax?==="+b);

        List<Admin> admins = adminService.getAll();
        model.addAttribute("admins", admins);
        /*String a=null;
        System.out.println(a.length());*/
        System.out.println(10/0);

        return "test/success";
    }

    @ResponseBody
    @RequestMapping("/send/array")
    public String testReceiveArrayOne(@RequestParam("arr[]") List<Integer> arr) {
        arr.forEach(System.out::println);
        return "success";
    }

    @ResponseBody
    @RequestMapping("/send/arrayTwo")
    public String testReceiveArrayTwo(@RequestParam("arr") List<Integer> arr) {
        arr.forEach(System.out::println);
        return "success";
    }


    @ResponseBody
    @RequestMapping("/send/arrayThree")
    public String testReceiveArrayThree(@RequestBody List<Integer> arr) {


        for (Integer numb : arr) {
            logger.info("requestBody==" + numb);
        }
        return "success";
    }

   /* @ResponseBody
    @RequestMapping("send/compose/object")
    public ResultEntity<Student> testReceiveComposeObject(@RequestBody Student student,HttpServletRequest request) {
        logger.info("Student=="+student);
        boolean b = CrowdUtil.judgeRequestType(request);
        logger.info("ajax?==="+b);
        String a=null;
        System.out.println(a.length());
        //将 “查询” 到的student对象封装到ResultEntity中返回
        return ResultEntity.successWithoutData(student);
    }*/
}
