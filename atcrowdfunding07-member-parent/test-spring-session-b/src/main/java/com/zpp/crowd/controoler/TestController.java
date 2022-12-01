package com.zpp.crowd.controoler;


import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

/**
 * @author : Zpp
 * @Date : 2022/10/31-20:55
 */
@RestController
public class TestController {

    @RequestMapping("/test/session/get")
    public String test(HttpSession session) {

        Object sessionKey = session.getAttribute("sessionKey");

        return sessionKey.toString();
    }
}
