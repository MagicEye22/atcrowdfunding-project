package com.zpp.crowd.constant;

/**
 * @author : Zpp
 * @Date : 2022/9/2-23:13
 */
public class CrowdConstant {

    public static final String ATTR_NAME_EXCEPTION = "exception";
    public static final String ATTR_NAME_LOGIN_ADMIN = "loginAdmin";
    public static final String ATTR_NAME_LOGIN_MEMBER = "loginMember";
    public static final String ATTR_NAME_PAGE_INFO = "pageInfo";
    public static final String ATTR_NAME_MESSAGE = "message";
    public static final String ATTR_NAME_TEMPLE_PROJECT = "tempProject";
    public static final String ATTR_NAME_PORTAL_DATA = "portal_data";
    public static final String ATTR_NAME_ORDER_PROJECT = "orderProjectVO";
    public static final String ATTR_NAME_ADDRESS_VO_LIST = "addressVOList";
    public static final String ATTR_NAME_ORDER_VO = "orderVO";

    public static final String MESSAGE_LOGIN_FAILED="账号或密码错误！请重新输入!";
    public static final String MESSAGE_LOGIN_ACCT_ALREADY_IN_USE="该账号已被使用,请重试!";
    public static final String MESSAGE_ACCESS_FORBIDDEN="请登录后再试!";
    public static final String MESSAGE_STRING_INVALIDATE="字符串不合法!请不要传入空字符串!";
    public static final String MESSAGE_SYSTEM_ERROR_LOGIN_NOT_UNIQUE = "系统错误：登录账号不唯一!";
    public static final String MESSAGE_STRING_DENIED = "无权限访问!";
    public static final String MESSAGE_AUTH_CODE_NOT_EXISTS = "验证码已过期,请重试！";
    public static final String MESSAGE_AUTH_CODE_INVALID = "验证码错误！" ;
    public static final String MESSAGE_PHONE_NUMBER_IS_NULL = "phoneNumber is null" ;
    public static final String MESSAGE_HEADER_PIC_UPLOAD_FAILED = "头图上传失败！";
    public static final String MESSAGE_HEADER_PIC_EMPTY = "头图为空！";
    public static final String MESSAGE_DETAIL_PIC_EMPTY = "详情图片为空！";
    public static final String MESSAGE_DETAIL_PIC_UPLOAD_FAILED = "详情图片上传失败！";
    public static final String MESSAGE_TEMPLE_PROJECT_MISSING = "ProjectVO 不存在！";


    public static final String REDIS_AUTH_CODE_PREFIX = "REDIS_AUTH_CODE_PREFIX_";



}
