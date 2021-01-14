package com.offcn.scw.user.bean;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.validation.constraints.NegativeOrZero;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@ApiModel //此实体类在文档中也生成介绍
public class User {

    @ApiModelProperty("主键")
    private Integer id;
    @ApiModelProperty("用户名")
    private String username;
    @ApiModelProperty("邮箱")
    private String email;
}
