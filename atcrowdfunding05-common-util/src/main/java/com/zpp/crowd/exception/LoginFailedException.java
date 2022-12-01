package com.zpp.crowd.exception;

/**
 * @author : Zpp
 * @Date : 2022/9/3-15:25
 *
 *
 * 登录失败抛出的异常
 */
public class LoginFailedException extends RuntimeException{
    public LoginFailedException() {

    }

    public LoginFailedException(String message) {
        super(message);
    }

    public LoginFailedException(String message, Throwable cause) {
        super(message, cause);
    }

    public LoginFailedException(Throwable cause) {
        super(cause);
    }

    public LoginFailedException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
