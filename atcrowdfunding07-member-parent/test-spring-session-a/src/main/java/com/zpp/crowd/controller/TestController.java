package com.zpp.crowd.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

/**
 * @author : Zpp
 * @Date : 2022/10/31-20:52
 */

@RestController
public class TestController {

    @RequestMapping("/test/session")
    public String test(HttpSession session){
        session.setAttribute("sessionKey","123");
        return "111";
    }
}
