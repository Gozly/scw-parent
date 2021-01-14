package com.offcn.scw.user.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

//和页面交互的实体类
@ApiModel("实体类 -- 和页面交互的实体类 -- UserReqVo")
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class UserReqVo {
    @ApiModelProperty("用户账号")
    private String loginacct;
    @ApiModelProperty("用户密码")
    private String userpswd;
    @ApiModelProperty("用户邮箱")
    private String email;
    @ApiModelProperty("用户验证码")
    private String code;
}
