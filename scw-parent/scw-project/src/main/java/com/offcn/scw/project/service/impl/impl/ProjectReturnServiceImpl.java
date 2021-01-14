package com.offcn.scw.project.service.impl.impl;

import com.offcn.scw.project.dao.TReturnMapper;
import com.offcn.scw.project.pojo.TReturn;
import com.offcn.scw.project.pojo.TReturnExample;
import com.offcn.scw.project.service.ProjectReturnService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProjectReturnServiceImpl implements ProjectReturnService {
    @Autowired
    private TReturnMapper returnMapper;

    //根据项目id查看当前项目的回报列表
    @Override
    public List<TReturn> getReturnList(int projectId) {
        TReturnExample example = new TReturnExample();
        TReturnExample.Criteria criteria = example.createCriteria();
        criteria.andProjectidEqualTo(projectId);

        List<TReturn> list = returnMapper.selectByExample(example);

        return list;
    }
}
