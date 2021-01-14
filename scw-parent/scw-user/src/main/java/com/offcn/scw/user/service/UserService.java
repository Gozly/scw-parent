package com.offcn.scw.user.service;

import com.offcn.scw.user.pojo.TMember;

public interface UserService {
    //注册功能
    public void registerUser(TMember member);

    //登录功能
    public TMember login(String username,String password);

    //按照主键查询
    public TMember findOne(Integer id);
}
