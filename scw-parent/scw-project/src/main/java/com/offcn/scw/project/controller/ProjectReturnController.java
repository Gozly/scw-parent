package com.offcn.scw.project.controller;

import com.offcn.scw.project.pojo.TReturn;
import com.offcn.scw.project.service.ProjectReturnService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("projectreturn")
@Api(tags = "回报控制器 -- 用于控制层")
@Slf4j
public class ProjectReturnController {

    @Autowired
    private ProjectReturnService returnService;

    //上传文件功能
    @ApiOperation("项目模块对外提供的项目回报信息列表查看")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "projectId",value = "项目id",required = true)
    })
    @GetMapping("{projectId}")
    public ResponseEntity<List<TReturn>>getReturnList(@PathVariable("projectId")int projectId){
        List<TReturn> returnList = returnService.getReturnList(projectId);
        return ResponseEntity.ok(returnList);
    }
}
