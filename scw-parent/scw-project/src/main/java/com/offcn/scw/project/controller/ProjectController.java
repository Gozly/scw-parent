package com.offcn.scw.project.controller;

import com.alibaba.fastjson.JSON;
import com.offcn.scw.common.enums.ProjectStatusEnum;
import com.offcn.scw.common.utils.OSSTemplate;
import com.offcn.scw.common.vo.BaseVo;
import com.offcn.scw.project.constants.ProjectConstant;
import com.offcn.scw.project.pojo.TReturn;
import com.offcn.scw.project.service.ProjectService;
import com.offcn.scw.project.vo.ProjectBaseInfoVo;
import com.offcn.scw.project.vo.ProjectRedisStorageVo;
import com.offcn.scw.project.vo.ProjectReturnVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundValueOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("projects")
@Api(tags = "项目控制器 -- 用于控制层")
@Slf4j
public class ProjectController {

    @Autowired
    private OSSTemplate ossTemplate;

    //Redis模板
    @Autowired
    private StringRedisTemplate redisTemplate;

    //业务层对象
    @Autowired
    private ProjectService projectService;

    //上传文件功能
    @ApiOperation("项目模块对外提供的多文件上传功能")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "files",value = "文件数组")
    })
    @PostMapping("upload")
    public ResponseEntity<Map<String,Object>>upload(MultipartFile[]files) throws IOException {
        Map<String,Object>map = new HashMap<>();
        List<String> list = new ArrayList<>();
        if(files != null && files.length > 0){
            for(MultipartFile file : files){
                String filename = file.getOriginalFilename();
                InputStream is = file.getInputStream();
                String url = ossTemplate.upload(is, filename);
                list.add(url);
            }
        }
        log.info("上传文件成功");
        map.put("list",list);
        return ResponseEntity.ok(map);
    }


    //项目发起操作 --- 第一步
    //项目初始化操作 -- 对应同意协议位置 -- 实际实现的就是项目初始信息保存到Redis功能
    @ApiOperation("项目模块对外提供的项目初始化功能 -- 项目操作第一步：同意协议")
    @PostMapping("init")
    public ResponseEntity<String>init(BaseVo vo){
        //BaseVo中存着登录令牌 -- 通过这个令牌验证当前用户是否登录
        String token = vo.getAccessToken();
        //从Redis中通过key获取值  通过令牌获取用户id
        String memberid = redisTemplate.boundValueOps(token).get();
        //登录判断 从Redis中获取登录用户id，如果获取不到则表示未登录，需要登录；如果获取到则表示登录过
        if(StringUtils.isEmpty(memberid)){
            return ResponseEntity.status(HttpStatus.I_AM_A_TEAPOT).body("未登录，请先登录");
        }

        String tockenP = projectService.initProject(Integer.parseInt(memberid));
        return ResponseEntity.ok(tockenP);
    }

    //项目发起操作 --- 第二步
    @ApiOperation("项目模块对外提供的项目初始化功能 -- 项目操作第二步：提交项目基本信息")
    @PostMapping("savebaseInfo")
    public ResponseEntity<String>savebaseInfo(ProjectBaseInfoVo vo){
        //获取登录令牌 -- 实现登录验证
        String accessToken = vo.getAccessToken();
        String memberid = redisTemplate.boundValueOps(accessToken).get();
        if(StringUtils.isEmpty(memberid)){
            return ResponseEntity.status(HttpStatus.I_AM_A_TEAPOT).body("未登录，请先登录");
        }
        //从Redis中获取已有的项目对象
        String projectToken = vo.getProjectToken();
        String key = ProjectConstant.TEMP_PROJECT_PREFIX +projectToken;
        String projectRedisStorageVoStr = redisTemplate.boundValueOps(key).get();
        ProjectRedisStorageVo redisStorageVo = JSON.parseObject(projectRedisStorageVoStr, ProjectRedisStorageVo.class);

        //把从页面来的数据转存到已有项目对象中
        BeanUtils.copyProperties(vo,redisStorageVo);

        String json = JSON.toJSONString(redisStorageVo);

        //再把项目对象存回Redis
        redisTemplate.boundValueOps(key).set(json);

        return ResponseEntity.ok("项目信息收集成功");
    }

    //项目发起的操作第三步 -- 实现项目回报信息的录入
    @ApiOperation("项目模块对外提供的项目初始化功能 -- 项目操作第三步：提交项目回报信息 -- 要求页面送json串格式的列表数据")
    @PostMapping("savereturn")
    public ResponseEntity<String>savereturn(@RequestBody List<ProjectReturnVo> vos){
        //收集回报信息
        if(vos != null && vos.size() > 0) {

            //登录验证
            ProjectReturnVo projectReturnVo = vos.get(0);
            String accessToken = projectReturnVo.getAccessToken();
            String memberid = redisTemplate.boundValueOps(accessToken).get();
            if(StringUtils.isEmpty(memberid)){
                return ResponseEntity.status(HttpStatus.I_AM_A_TEAPOT).body("未登录，请先登录");
            }

            String projectToken = projectReturnVo.getProjectToken();
            String key = ProjectConstant.TEMP_PROJECT_PREFIX + projectToken;
            //从Redis中获取出来项目对象
            String json = redisTemplate.boundValueOps(key).get();
            ProjectRedisStorageVo storageVo = JSON.parseObject(json, ProjectRedisStorageVo.class);

            //把回报信息添加到项目对象中
            List<TReturn>list = new ArrayList<>();
            for(ProjectReturnVo vo : vos){
                TReturn tReturn = new TReturn();
                BeanUtils.copyProperties(vo,tReturn);
                list.add(tReturn);
            }
            storageVo.setProjectReturns(list);

            //再把项目对象存回Redis
            String jsonString = JSON.toJSONString(storageVo);

            redisTemplate.boundValueOps(key).set(jsonString);

            return ResponseEntity.ok("回报信息收集完成");
        }
        return ResponseEntity.status(HttpStatus.I_AM_A_TEAPOT).body("没有收集到回报信息");
    }

    //项目发起操作的第四步 --- 实现项目数据从Redis保存到数据库功能
    @ApiOperation("项目模块对外提供的项目初始化功能 -- 项目操作第四步：保存项目信息")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "accessTocken",value = "登录令牌",required = true),
            @ApiImplicitParam(name = "projectTocken",value = "项目令牌",required = true),
            @ApiImplicitParam(name = "ops",value = "项目状态 0-草稿，1-提交待审",required = true)
    })
    @PostMapping("submit")
    public ResponseEntity<String>submit(String accessTocken,String projectTocken,String ops){
        //判断是否登录
        String memberid = redisTemplate.boundValueOps(accessTocken).get();
        if(StringUtils.isEmpty(memberid)){
            return ResponseEntity.status(HttpStatus.I_AM_A_TEAPOT).body("未登录，请先登录");
        }

        //从Redis中获取项目信息
        String key = ProjectConstant.TEMP_PROJECT_PREFIX + projectTocken;
        String json = redisTemplate.boundValueOps(key).get();
        ProjectRedisStorageVo vo = JSON.parseObject(json, ProjectRedisStorageVo.class);

        //判断项目状态是否存在  假如存在再判断哪种状态  不存在则提示
        if(!StringUtils.isEmpty(ops)){

            if("0".equals(ops)){
                //假如状态存在 -- 状态为0
                projectService.saveProject(ProjectStatusEnum.DRAFT,vo);
                return ResponseEntity.ok("项目保存草稿成功");
            }else if("1".equals(ops)){
                //假如状态存在 -- 状态为1
                projectService.saveProject(ProjectStatusEnum.SUBMIT_AUTH,vo);
                return ResponseEntity.ok("项目提交待审核");
            }

        }
        return ResponseEntity.status(HttpStatus.I_AM_A_TEAPOT).body("不支持此操作");
    }
}
