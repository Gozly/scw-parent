package com.offcn.scw.project.service;

import com.offcn.scw.common.enums.ProjectStatusEnum;
import com.offcn.scw.project.vo.ProjectRedisStorageVo;

//项目有关功能的业务层
public interface ProjectService {

    //初始化项目 -- 指定项目的发起人  -- 对应同意协议操作步骤  -- 项目保存到缓存
    public String initProject(int memberid);

    //实现项目的保存功能
    public void saveProject(ProjectStatusEnum projectStatusEnum, ProjectRedisStorageVo vo);
}
