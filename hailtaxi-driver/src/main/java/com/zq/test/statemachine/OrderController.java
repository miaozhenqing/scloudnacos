package com.zq.test.statemachine;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
public class OrderController {

    @Resource
    private OrderService orderService;

    @RequestMapping("/testOrderStatusChange")//127.0.0.1:8002/testOrderStatusChange
    public String testOrderStatusChange(){
        orderService.create();
        orderService.create();
        orderService.pay(1L);
        orderService.deliver(1L);
        orderService.receive(1L);
        orderService.pay(2L);
        orderService.deliver(2L);
        orderService.receive(2L);
        System.out.println("全部订单状态：" + orderService.getOrders());
        return "success";
    }

}