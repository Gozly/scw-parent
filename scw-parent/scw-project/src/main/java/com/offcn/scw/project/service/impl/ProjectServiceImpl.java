package com.offcn.scw.project.service.impl;

import com.alibaba.fastjson.JSON;
import com.offcn.scw.common.enums.ProjectStatusEnum;
import com.offcn.scw.project.constants.ProjectConstant;
import com.offcn.scw.project.dao.*;
import com.offcn.scw.project.enums.ProjectImageTypeEnume;
import com.offcn.scw.project.pojo.*;
import com.offcn.scw.project.service.ProjectService;
import com.offcn.scw.project.vo.ProjectRedisStorageVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.UUID;

//项目有关功能的业务层实现类
@Service
public class ProjectServiceImpl implements ProjectService {

    //注入
    @Autowired
    private StringRedisTemplate redisTemplate;

    @Autowired
    private TProjectMapper projectMapper;

    @Autowired
    private TProjectImagesMapper imagesMapper;

    @Autowired
    private TProjectTagMapper tagMapper;

    @Autowired
    private TProjectTypeMapper typeMapper;

    @Autowired
    private TReturnMapper returnMapper;

    //初始化项目 -- 添加项目发起人 -- 生成tocken -- 项目信息存入缓存
    @Override
    public String initProject(int memberid) {
        //生成tocken
        String tocken = UUID.randomUUID().toString().replaceAll("-", "");
        //实体类对象
        ProjectRedisStorageVo vo = new ProjectRedisStorageVo();
        vo.setMemberid(memberid);

        //转换对象为json串
        String json = JSON.toJSONString(vo);

        //json串存入Redis
        redisTemplate.boundValueOps(ProjectConstant.TEMP_PROJECT_PREFIX+tocken).set(json);

        return tocken;
    }

    //保存项目相关数据：包括，项目、图片、标签、类型、回报；删除缓存
    @Transactional
    @Override
    public void saveProject(ProjectStatusEnum projectStatusEnum, ProjectRedisStorageVo vo) {
        //实现项目数据的保存
        TProject project = new TProject();
        BeanUtils.copyProperties(vo,project);

        Date date = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String format = simpleDateFormat.format(date);
        project.setCreatedate(format);

        project.setStatus(projectStatusEnum.getCode() + "");

        projectMapper.insertSelective(project);

        Integer projectId = project.getId();
        //保存图片
        String headerImage = vo.getHeaderImage();
        TProjectImages headImage = new TProjectImages();//new TProjectImages(null,projectId,headerImage,0);
        headImage.setImgurl(headerImage);
        headImage.setImgtype(ProjectImageTypeEnume.HEADER.getCode());
        headImage.setProjectid(projectId);
        imagesMapper.insertSelective(headImage);

        List<String> detailsImage = vo.getDetailsImage();
        for(String image : detailsImage){
            TProjectImages dimage = new TProjectImages();//new TProjectImages(null,projectId,headerImage,0);
            dimage.setImgurl(image);
            dimage.setImgtype(ProjectImageTypeEnume.DETAILS.getCode());
            dimage.setProjectid(projectId);
            imagesMapper.insertSelective(dimage);
        }

        //保存标签
        List<Integer> tagids = vo.getTagids();
        for(Integer id : tagids){
            TProjectTag tag = new TProjectTag();
            tag.setProjectid(projectId);
            tag.setTagid(id);

            tagMapper.insertSelective(tag);
        }

        //保存类型
        List<Integer> typeids = vo.getTypeids();
        for(Integer id : typeids){
            TProjectType type = new TProjectType();
            type.setProjectid(projectId);
            type.setTypeid(id);

            typeMapper.insertSelective(type);
        }


        //保存回报
        List<TReturn> projectReturns = vo.getProjectReturns();
        for(TReturn tReturn : projectReturns){
            tReturn.setProjectid(projectId);

            returnMapper.insertSelective(tReturn);
        }

        //删除Redis中缓存
        String key = ProjectConstant.TEMP_PROJECT_PREFIX + vo.getProjectToken();
        redisTemplate.delete(key);
    }
}
