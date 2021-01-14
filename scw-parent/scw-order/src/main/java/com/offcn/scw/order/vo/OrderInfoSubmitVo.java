package com.offcn.scw.order.vo;

import com.offcn.scw.common.vo.BaseVo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

//订单信息提交实体类  -- 和页面交互
@ApiModel
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class OrderInfoSubmitVo extends BaseVo {

    @ApiModelProperty("项目id")
    private Integer projectid;//项目ID
    @ApiModelProperty("回报ID")
    private Integer returnid;//回报ID
    @ApiModelProperty("回报数量")
    private Integer rtncount;//回报数量
    @ApiModelProperty("收货地址")
    private String address;//收货地址
    @ApiModelProperty("是否开发票 0 - 不开发票， 1 - 开发票")
    private Byte invoice;//是否开发票 0 - 不开发票， 1 - 开发票
    @ApiModelProperty("发票名头")
    private String invoictitle;//发票名头
    @ApiModelProperty("备注")
    private String remark;//备注

}
