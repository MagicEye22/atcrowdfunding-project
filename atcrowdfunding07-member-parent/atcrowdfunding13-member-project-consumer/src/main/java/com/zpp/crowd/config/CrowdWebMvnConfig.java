package com.zpp.crowd.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author : Zpp
 * @Date : 2022/11/1-22:12
 */
@Configuration
public class CrowdWebMvnConfig implements WebMvcConfigurer {
    /**
     * 配置 view-controller
     * @param registry
     */
    @Override
    public void addViewControllers(ViewControllerRegistry registry) {

        // 添加 view-controller

        // view-controller 是在 project-consumer 内部定义的，所以这里是一个不经过 Zuul
        // 访问的地址，所以这个路径前面不加路由规则中定义的前缀：“/project”
        registry.addViewController("/agree/protocol/to/page.html").setViewName("project-agree"); // 阅读协议
        registry.addViewController("/launch/project/page.html").setViewName("project-launch"); // 项目表单
        registry.addViewController("/return/info/page.html").setViewName("project-return"); // 回报表单
        registry.addViewController("/create/confirm/page.html").setViewName("project-confirm"); // 确定提交项目
        registry.addViewController("/create/success/page.html").setViewName("project-success"); // 完成页面
    }
}
