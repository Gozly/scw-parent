package com.offcn.scw.common.enums;

//保存订单状态
public enum OrderStatusEnum {
    UNPAY(0, "未支付"),
    CANCEL(1, "已取消"),
    PAYED(2, "支付成功"),
    WAITING(3, "等待发货"),
    SEND(4, "已发货"),
    SENDED(5, "已送达"),
    SUCCESS(6, "交易完成"),
    FAIL(7, "交易未完成");

    private int code;
    private String status;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    OrderStatusEnum(int code, String status) {
        this.code = code;
        this.status = status;
    }

    OrderStatusEnum() {
    }
}
