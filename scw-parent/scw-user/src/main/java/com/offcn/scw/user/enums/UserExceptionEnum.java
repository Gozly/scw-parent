package com.offcn.scw.user.enums;

public enum UserExceptionEnum {
    LOGINACCT_EXIST(1,"登录账户已经存在"),
    EMAIL_EXIST(2,"注册邮箱已经存在"),
    LOGINACCT_LOCKED(3,"账号已经锁定");

    //属性和get、set方法 --- 枚举不能使用lombok
    private int code;
    private String msg;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    UserExceptionEnum(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    UserExceptionEnum() {
    }
}
