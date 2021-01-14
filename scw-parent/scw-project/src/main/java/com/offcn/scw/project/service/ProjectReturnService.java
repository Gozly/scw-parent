package com.offcn.scw.project.service;

import com.offcn.scw.project.pojo.TReturn;

import java.util.List;

public interface ProjectReturnService {

    //根据项目id查看回报列表
    public List<TReturn>getReturnList(int projectId);
}
