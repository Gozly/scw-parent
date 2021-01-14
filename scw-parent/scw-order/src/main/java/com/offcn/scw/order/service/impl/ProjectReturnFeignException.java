package com.offcn.scw.order.service.impl;

import com.offcn.scw.order.service.ProjectReturnFeign;
import com.offcn.scw.order.vo.TReturn;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ProjectReturnFeignException implements ProjectReturnFeign {
    @Override
    public ResponseEntity<List<TReturn>> getProjectReturnList(int projectId) {
        return ResponseEntity.ok(null);
    }
}
