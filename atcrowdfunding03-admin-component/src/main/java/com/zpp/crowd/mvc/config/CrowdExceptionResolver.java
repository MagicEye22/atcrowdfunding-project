package com.zpp.crowd.mvc.config;

import com.google.gson.Gson;
import com.zpp.crowd.constant.CrowdConstant;
import com.zpp.crowd.exception.AccessForbiddenException;
import com.zpp.crowd.exception.LoginAcctAlreadyInForUpdateException;
import com.zpp.crowd.exception.LoginAcctAlreadyInUseException;
import com.zpp.crowd.exception.LoginFailedException;
import com.zpp.crowd.util.CrowdUtil;
import com.zpp.crowd.util.ResultEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author : Zpp
 * @Date : 2022/9/2-22:37
 */

/**
 * @ControllerAdvice 表示当前这个类是基于注解的一个异常处理类
 */
@ControllerAdvice
public class CrowdExceptionResolver  {

    /**
     * 处理管理员 更新 时 账号重复的异常处理
     * @param loginAcctAlreadyInUseException
     * @param request
     * @param response
     * @return 去的视图是 system-error
     * @throws IOException
     */
    @ExceptionHandler(value = LoginAcctAlreadyInForUpdateException.class)
    public ModelAndView resolveLoginAcctAlreadyInUseException(LoginAcctAlreadyInForUpdateException loginAcctAlreadyInUseException, HttpServletRequest request, HttpServletResponse response) throws IOException {

        String viewName="system-error";
        return commonResolve(viewName,loginAcctAlreadyInUseException,request,response);
    }


    /**
     *  处理管理员新增 时 账号重复的异常处理
     * @param loginAcctAlreadyInUseException
     * @param request
     * @param response
     * @return
     * @throws IOException
     */
    @ExceptionHandler(value = LoginAcctAlreadyInUseException.class)
    public ModelAndView resolveLoginAcctAlreadyInUseException(LoginAcctAlreadyInUseException loginAcctAlreadyInUseException, HttpServletRequest request, HttpServletResponse response) throws IOException {

        String viewName="admin/admin-add";
        return commonResolve(viewName,loginAcctAlreadyInUseException,request,response);
    }


    /**
     * 用户未登录访问受保护资源的异常处理方法-->使用了SpringSecurity ，该异常已无用
     *
     * 修改为 访问 角色维护页面无权限 返回的 json信息
     * @param accessDeniedException 异常类型  SpringSecurity没有权限访问的异常
     * @param request 请求对象
     * @param response 响应对象
     * @return 视图页面和错误消息
     * @throws IOException
     */
    @ExceptionHandler(value = AccessDeniedException.class)
    public ModelAndView resolveAccessForbiddenException(
            AccessDeniedException accessDeniedException,
            HttpServletRequest request,
            HttpServletResponse response) throws IOException {

        String viewName="system-error";

        return commonResolve(viewName,accessDeniedException,request,response);
    }


    /**
     * 登录失败的异常处理方法
     * @param exception 异常类型
     * @param request 请求对象
     * @param response 响应对象
     * @return 视图页面和错误消息
     * @throws IOException
     */
    @ExceptionHandler(value = LoginFailedException.class)
    public ModelAndView resolverLoginFailedException(
            //实际捕获到的异常类型
            LoginFailedException exception,
            //当前请求的对象
            HttpServletRequest request,
            //当前响应对象
            HttpServletResponse response) throws IOException {


        String viewName= "admin/admin-login";
        return commonResolve(viewName,exception,request,response);
    }



    /**
     * 空指针异常映射
     * @param exception
     * @param request
     * @param response
     * @return
     * @throws IOException
     */
    // @ExceptionHandler将一个具体的异常类型和一个方法关联起来
    @ExceptionHandler(value = NullPointerException.class)
    public ModelAndView resolverNullPointException(
            //实际捕获到的异常类型
            NullPointerException exception,
            //当前请求的对象
            HttpServletRequest request,
            //当前响应对象
            HttpServletResponse response) throws IOException {


        String viewName= "system-error";
        return commonResolve(viewName,exception,request,response);

    }

    /**
     * 数学类异常
     * @param arithmeticException
     * @param request
     * @param response
     * @return
     * @throws IOException
     */
    @ExceptionHandler(value = ArithmeticException.class)
    public ModelAndView MathException(ArithmeticException arithmeticException ,HttpServletRequest request,HttpServletResponse response) throws IOException {
        return commonResolve("system-error",arithmeticException,request,response);
    }
    /**
     * 核心异常处理方法
     * @param viewName  相应的视图名称  指定要前往的视图名
     * @param e   异常类型  SpringMVC 捕获到的异常对象
     * @param request 请求对象  为了判断当前请求是 “普通请求” 还是 “Ajax 请求” 需要传入原生 request 对象
     * @param response 相应对象  为了能够将 JSON 字符串作为当前请求的响应数据返回给浏览器
     * @return  ModelAndView
     * @throws IOException
     */
    private ModelAndView commonResolve(String viewName, Exception e, HttpServletRequest request, HttpServletResponse response) throws IOException {
        //1，判断当前请求类型
        boolean judgeRequestType = CrowdUtil.judgeRequestType(request);

        //2,如果是Ajax请求
        if (judgeRequestType) {

            //3.创建ResultEntity对象
            ResultEntity<Object> resultEntity = ResultEntity.failed(e.getMessage());

            //4，创建Gson对象
            Gson gson = new Gson();

            //5，将ResultEntity转为Json字符串
            String json = gson.toJson(resultEntity);

            //6.将json字符串作为响应体返回给浏览器
            response.getWriter().write(json);

            //7，由于上面已经通过原生的response所以不提供ModelAndView对象
            return null;
        }

        //8,如果不是Ajax请求则创建ModelAndView对象
        ModelAndView modelAndView = new ModelAndView();
        //9.就Exception对象存入模型
        modelAndView.addObject(CrowdConstant.ATTR_NAME_EXCEPTION, e);

        //10，设置对应视图名称
        modelAndView.setViewName(viewName);

        return modelAndView;

    }
}
