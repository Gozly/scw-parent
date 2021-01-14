package com.offcn.scw.user.vo;

import com.offcn.scw.common.vo.BaseVo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

//在用户登录成功之后，保存用户信息的实体类 -- 和页面有关
@ApiModel("在用户登录成功之后，保存用户信息的实体类 -- 和页面有关")
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class UserRespVo extends BaseVo {
    //令牌
    //@ApiModelProperty("令牌")
    //private String accessToken;
    @ApiModelProperty("用户账号")
    private String loginacct;
    @ApiModelProperty("用户名")
    private String username;
    @ApiModelProperty("邮箱")
    private String email;
    @ApiModelProperty("实名认证与否")
    private String authstatus;
    @ApiModelProperty("用户类型")
    private String usertype;
    @ApiModelProperty("真实名称")
    private String realname;
    @ApiModelProperty("身份号码")
    private String cardnum;
    @ApiModelProperty("账号类型")
    private String accttype;
}
