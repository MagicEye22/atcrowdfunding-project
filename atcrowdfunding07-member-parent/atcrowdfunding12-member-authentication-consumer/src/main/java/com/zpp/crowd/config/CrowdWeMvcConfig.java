package com.zpp.crowd.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author : Zpp
 * @Date : 2022/10/26-22:24
 */
@Configuration
public class CrowdWeMvcConfig implements WebMvcConfigurer {

    /**
     * 配置 view-controller
     * @param registry
     */
    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        // 请求的地址
        String urlPath="/auth/member/to/reg/page.html";

        // 视图模板名
        String viewName="member-reg";

        // 添加 view-controller
        registry.addViewController(urlPath).setViewName(viewName);
        registry.addViewController("/auth/member/to/login/page.html").setViewName("member-login");
        registry.addViewController("/auth/member/to/center/page.html").setViewName("member-center");
        registry.addViewController("/member/my/crowd.html").setViewName("member-crowd");
    }
}
