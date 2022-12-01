package com.zpp.crowd.constant;

import java.util.HashSet;
import java.util.Set;

/**
 * @author : Zpp
 * @Date : 2022/10/31-21:57
 */
public class AccessPassResources {

    /**
     * 放行的 请求路径 集合
     */
    public static final Set<String> PASS_RES_SET = new HashSet<>();

    // 初始化资源
    static {
        PASS_RES_SET.add("/");
        PASS_RES_SET.add("/auth/member/to/reg/page.html");
        PASS_RES_SET.add("/auth/member/to/login/page.html");
        PASS_RES_SET.add("/auth/member/loguot");
        PASS_RES_SET.add("/auth/member/do/login");
        PASS_RES_SET.add("/auth/do/member/register");
        PASS_RES_SET.add("/auth/member/send/short/message");
    }

    /**
     * 放行的 静态资源 集合
     */
    public static final Set<String> STATIC_RES_SET = new HashSet<>();

    static {
        STATIC_RES_SET.add("bootstrap");
        STATIC_RES_SET.add("css");
        STATIC_RES_SET.add("fonts");
        STATIC_RES_SET.add("img");
        STATIC_RES_SET.add("jquery");
        STATIC_RES_SET.add("layer");
        STATIC_RES_SET.add("script");
        STATIC_RES_SET.add("ztree");
    }


    /**
     * 判断 是否是静态资源
     *
     * @param servletPath
     * @return true ： 是静态资源
     * false： 不是
     */
    public static boolean judgeCurrentServletPathWeatherStaticResource(String servletPath) {

        // 1.排除字符串无效的情况
        if (servletPath == null || servletPath.length() == 0) {
            throw new RuntimeException(CrowdConstant.MESSAGE_STRING_INVALIDATE);
        }

        // 2.根据“/”拆分 ServletPath 字符
        String[] split = servletPath.split("/");

        // 3.考虑到第一个斜杠左边经过拆分后得到一个空字符串是数组的第一个元素，所以 要使用下标 1 取第二个元素
        String firstLevelPath = split[1];


        return STATIC_RES_SET.contains(firstLevelPath);

    }
}
