package com.hyl.atcrowdfunding.exception;

/**
 * @author: hyl
 * @date: 2019/07/12
 **/
public class LoginFailException extends RuntimeException {

    public LoginFailException(String message){
        super(message);
    }
}
