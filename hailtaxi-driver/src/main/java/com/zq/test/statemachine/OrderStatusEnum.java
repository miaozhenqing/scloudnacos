package com.zq.test.statemachine;

public enum OrderStatusEnum {
    // 待支付
    WAIT_PAYMENT,
    // 待发货
    WAIT_DELIVER,
    // 待收货
    WAIT_RECEIVE,
    // 完成
    FINISH;
}
