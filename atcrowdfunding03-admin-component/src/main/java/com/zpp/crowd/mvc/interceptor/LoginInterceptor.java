package com.zpp.crowd.mvc.interceptor;

import ch.qos.logback.core.CoreConstants;
import com.zpp.crowd.constant.CrowdConstant;
import com.zpp.crowd.entity.Admin;
import com.zpp.crowd.exception.AccessForbiddenException;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * @author : Zpp
 * @Date : 2022/9/3-19:55
 *
 * 登录验证拦截器
 */
@Deprecated
public class LoginInterceptor extends HandlerInterceptorAdapter {


    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 1.通过request对象获取Session对象
        HttpSession session = request.getSession();

        // 2.尝试从session域中获取Admin对象
        Admin admin =(Admin) session.getAttribute(CrowdConstant.ATTR_NAME_LOGIN_ADMIN);

        // 3.判断admin是否为null
        if (admin==null){

            // 4.为空就抛出自定义异常
            throw  new AccessForbiddenException(CrowdConstant.MESSAGE_ACCESS_FORBIDDEN);
        }

        // 5.如果admin不为空，则返回true放行
        return true;
    }
}
