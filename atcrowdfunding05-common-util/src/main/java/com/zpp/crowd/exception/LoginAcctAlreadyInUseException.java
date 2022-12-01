package com.zpp.crowd.exception;

/**
 * @author : Zpp
 * @Date : 2022/9/5-23:10
 *
 * 管理员新增 或更新 账号重复异常处理
 */
public class LoginAcctAlreadyInUseException extends RuntimeException {

    public LoginAcctAlreadyInUseException() {
        super();
    }

    public LoginAcctAlreadyInUseException(String message) {
        super(message);
    }

    public LoginAcctAlreadyInUseException(String message, Throwable cause) {
        super(message, cause);
    }

    public LoginAcctAlreadyInUseException(Throwable cause) {
        super(cause);
    }

    protected LoginAcctAlreadyInUseException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
