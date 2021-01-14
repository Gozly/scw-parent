package com.offcn.scw.project.vo;

import com.offcn.scw.project.pojo.TReturn;
import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

//这是和页面交互的，同时是保存到Redis缓存的与项目有关的实体类
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@ApiModel
public class ProjectRedisStorageVo {
    private String projectToken;//项目的临时token
    private Integer memberid;//会员id
    private List<Integer> typeids; //项目的分类id
    private List<Integer> tagids; //项目的标签id
    private String name;//项目名称
    private String remark;//项目简介
    private Long money;//筹资金额
    private Integer day;//筹资天数
    private String headerImage;//项目头部图片
    private List<String> detailsImage;//项目详情图片
    private List<TReturn> projectReturns;//项目回报
}
