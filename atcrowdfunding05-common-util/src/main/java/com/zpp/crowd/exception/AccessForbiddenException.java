package com.zpp.crowd.exception;

/**
 * @author : Zpp
 * @Date : 2022/9/3-20:03
 *
 * 表示用户没有登录就访问受保护资源的异常
 */
public class AccessForbiddenException extends  RuntimeException{

    public AccessForbiddenException() {
        super();
    }

    public AccessForbiddenException(String message) {
        super(message);
    }

    public AccessForbiddenException(String message, Throwable cause) {
        super(message, cause);
    }

    public AccessForbiddenException(Throwable cause) {
        super(cause);
    }

    protected AccessForbiddenException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
