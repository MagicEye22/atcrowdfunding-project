package com.zpp.crowd.exception;

/**
 * @author : Zpp
 * @Date : 2022/9/5-23:10
 *
 * 管理员更新 账号重复异常处理
 */
public class LoginAcctAlreadyInForUpdateException extends RuntimeException {

    public LoginAcctAlreadyInForUpdateException() {
        super();
    }

    public LoginAcctAlreadyInForUpdateException(String message) {
        super(message);
    }

    public LoginAcctAlreadyInForUpdateException(String message, Throwable cause) {
        super(message, cause);
    }

    public LoginAcctAlreadyInForUpdateException(Throwable cause) {
        super(cause);
    }

    protected LoginAcctAlreadyInForUpdateException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
