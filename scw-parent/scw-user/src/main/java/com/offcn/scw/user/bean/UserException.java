package com.offcn.scw.user.bean;

import com.offcn.scw.user.enums.UserExceptionEnum;

//用户异常的类型 -- 实现自定义异常
public class UserException extends RuntimeException{
    //在发生使用者自定义的异常时，抛出相应的异常，自定义
    public UserException(UserExceptionEnum exceptionEnum){
        //添加自定义的异常提示 -- 封装到了枚举中
        super(exceptionEnum.getMsg());
    }
}
