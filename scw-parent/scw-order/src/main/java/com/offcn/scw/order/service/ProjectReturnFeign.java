package com.offcn.scw.order.service;

import com.offcn.scw.order.service.impl.ProjectReturnFeignException;
import com.offcn.scw.order.vo.TReturn;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

//远程调用功能 -- 采用feign方式  -- 调用项目回报列表
@FeignClient(value = "SCW-PROJECT",fallback = ProjectReturnFeignException.class)
public interface ProjectReturnFeign {

    //实现远程调用的方法
    @GetMapping("/projectreturn/{projectId}")
    public ResponseEntity<List<TReturn>>getProjectReturnList(@PathVariable("projectId")int projectId);
}
