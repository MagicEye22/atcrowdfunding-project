package com.zpp.crowd.test;

import com.zpp.crowd.util.CrowdUtil;
import org.junit.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * @author : Zpp
 * @Date : 2022/9/3-15:18
 */
public class StringTest {


    @Test
    public  void  testMd5(){
//        System.out.println(CrowdUtil.MD5("123123"));

        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        System.out.println(bCryptPasswordEncoder.encode("123123"));
    }
}
