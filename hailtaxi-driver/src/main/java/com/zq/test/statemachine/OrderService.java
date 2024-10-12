package com.zq.test.statemachine;

import com.google.common.collect.Maps;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.statemachine.StateMachine;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Map;
import java.util.Objects;

/**
 * @description: 订单服务
 */
@Service
public class OrderService {

    @Resource
    private StateMachine<OrderStatusEnum, OrderStatusChangeEventEnum> orderStateMachine;

    private long id = 1L;

    private Map<Long, Order> orders = Maps.newConcurrentMap();

    public Order create() {
        Order order = new Order();
        order.setOrderStatus(OrderStatusEnum.WAIT_PAYMENT);
        order.setOrderId(id++);
        orders.put(order.getOrderId(), order);
        System.out.println("订单创建成功:" + order.toString());
        return order;
    }

    public Order pay(long id) {
        Order order = orders.get(id);
        System.out.println("尝试支付，订单号：" + id);
        Message message = MessageBuilder.withPayload(OrderStatusChangeEventEnum.PAYED).
                setHeader("order", order).build();
        if (!sendEvent(message)) {
            System.out.println(" 支付失败, 状态异常，订单号：" + id);
        }
        return orders.get(id);
    }

    public Order deliver(long id) {
        Order order = orders.get(id);
        System.out.println(" 尝试发货，订单号：" + id);
        if (!sendEvent(MessageBuilder.withPayload(OrderStatusChangeEventEnum.DELIVERY)
                .setHeader("order", order).build())) {
            System.out.println(" 发货失败，状态异常，订单号：" + id);
        }
        return orders.get(id);
    }

    public Order receive(long id) {
        Order order = orders.get(id);
        System.out.println(" 尝试收货，订单号：" + id);
        if (!sendEvent(MessageBuilder.withPayload(OrderStatusChangeEventEnum.RECEIVED)
                .setHeader("order", order).build())) {
            System.out.println(" 收货失败，状态异常，订单号：" + id);
        }
        return orders.get(id);
    }


    public Map<Long, Order> getOrders() {
        return orders;
    }

    /**
     * 发送状态转换事件
     * @param message
     * @return
     */
    private synchronized boolean sendEvent(Message<OrderStatusChangeEventEnum> message) {
        boolean result = false;
        try {
            orderStateMachine.start();
            result = orderStateMachine.sendEvent(message);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (Objects .nonNull(message)) {
                Order order = (Order) message.getHeaders().get("order");
                if (Objects.nonNull(order) && Objects.equals(order.getOrderStatus(), OrderStatusEnum.FINISH)) {
                    orderStateMachine.stop();
                }
            }
        }
        return result;
    }
}
