package com.zpp.crowd.filter;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import com.zpp.crowd.constant.AccessPassResources;
import com.zpp.crowd.constant.CrowdConstant;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * @author : Zpp
 * @Date : 2022/10/31-22:18
 */
@Component
public class CrowdAccessFilter extends ZuulFilter {
    @Override
    public String filterType() {
        // 这里返回“pre”意思是在目标微服务前执行过滤
        return "pre";
    }

    @Override
    public int filterOrder() {
        return 0;
    }

    @Override
    public boolean shouldFilter() {

        // 1.获取RequestContext对象
        RequestContext requestContext = RequestContext.getCurrentContext();

        // 2.获取Request对象
        HttpServletRequest request = requestContext.getRequest();

        // 3.获取servletPath
        String servletPath = request.getServletPath();

        // 判断当前请求是否能放行
        boolean containsResult = AccessPassResources.PASS_RES_SET.contains(servletPath);

        if (containsResult) {
            // 是可放行请求
            return false;
        }

        // 判断是否是静态资源
        boolean judgeStaticResult = AccessPassResources.judgeCurrentServletPathWeatherStaticResource(servletPath);


        if (judgeStaticResult) {
            // 放行
            return false;
        }

        return true;
    }

    @Override
    public Object run() throws ZuulException {
        // 1.获取RequestContext对象
        RequestContext requestContext = RequestContext.getCurrentContext();

        // 2.获取Request对象
        HttpServletRequest request = requestContext.getRequest();

        // 获取当前的session对象
        HttpSession session = request.getSession();

        // 检查是否登录
        Object loginMember = session.getAttribute(CrowdConstant.ATTR_NAME_LOGIN_MEMBER);

        // 未登录
        if (loginMember==null){

            // 获取Response对象
            HttpServletResponse response = requestContext.getResponse();

            // 提示用户登录
            session.setAttribute(CrowdConstant.ATTR_NAME_MESSAGE,CrowdConstant.MESSAGE_ACCESS_FORBIDDEN);

            // 重定向到登录页面
            try {
                response.sendRedirect("/auth/member/to/login/page.html");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return null;
    }
}
