package com.zpp.crowd.mvc.config;

import com.zpp.crowd.constant.CrowdConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.access.AccessDeniedHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;


/**
 * @author : Zpp
 * @Date : 2022/10/13-23:10
 */
@Configuration
@EnableWebSecurity
//开启 @PreAuthorize ，@PostAuthorize ，@PostFilter，@PreFilter 的注解功能
/*注解表示启用全局方法权限管理功能*/
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class CrowdfundingSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private  BCryptPasswordEncoder bCryptPasswordEncoder;


    @Override
    protected void configure(HttpSecurity http) throws Exception {


        // 放行登录页 和所有静态资源
        http.authorizeRequests()
                .antMatchers("/admin", "/bootstrap/**",
                        "/crowd-js/**", "/css/**", "/fonts/**", "/img/**", "/jquery/**",
                        " /layer/**", "/script/**","/ztree/**")
                .permitAll()

                // 权限控制测试 访问admin分页模板的访问控制 --具备  ‘经理’的角色
                //.antMatchers("/admin/get/page").hasRole("经理")

                .anyRequest()
                .authenticated();



        // 开启表单登录的功能
        http.formLogin()
                .loginPage("/admin")//指定登录的页面
                .loginProcessingUrl("/security/admin/do/login")//指定处理登录的请求
                .defaultSuccessUrl("/admin/to/main")//登录成功后去的页面
                // 修改默认登录表单接收的请求参数名
                .usernameParameter("loginAcct")
                .passwordParameter("userPass");


        // 退出登录 ，  指定退出登录的地址                      退出成功后前往的地址
        http.logout().logoutUrl("/security/admin/do/logout").logoutSuccessUrl("/admin");

        // 配置没有权限异常消息显示页面
        http.exceptionHandling().accessDeniedHandler(new AccessDeniedHandler() {
            @Override
            public void handle(HttpServletRequest Request, HttpServletResponse Response, AccessDeniedException e) throws IOException, ServletException {
                HttpSession session = Request.getSession();
                session.setAttribute("exception2",new Exception(CrowdConstant.MESSAGE_STRING_DENIED));
                Request.getRequestDispatcher("/WEB-INF/system-error403.jsp").forward(Request,Response);
            }
        });
        //http.exceptionHandling().accessDeniedPage("/error");

        // csrf功能 -关闭
        http.csrf().disable();
    }


    /*@Bean
    public PasswordEncoder passwordEncoder(){
        return  new BCryptPasswordEncoder();
    }*/

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        // 测试
       // auth.inMemoryAuthentication().withUser("test").password("123123").roles("ADMIN");


        auth.userDetailsService(userDetailsService).passwordEncoder(bCryptPasswordEncoder);
    }
}
