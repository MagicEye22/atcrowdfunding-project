package com.zpp.crowd;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;


/**
 * @author : Zpp
 * @Date : 2022/10/24-23:10
 */
@EnableZuulProxy
@SpringBootApplication
public class CrowdMainClass {
    public static void main(String[] args) {

        SpringApplication.run(CrowdMainClass.class,args);
    }
}
