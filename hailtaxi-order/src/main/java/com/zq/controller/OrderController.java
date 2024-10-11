package com.zq.controller;

import com.zq.config.ServerConfig;
import com.zq.service.MsgProducerService;
import com.zq.service.TargetServiceClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("/order")
public class OrderController {
    @Autowired
    private ServerConfig serverConfig;
    @Autowired
    private LoadBalancerClient loadBalancerClient;
    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private TargetServiceClient targetServiceClient;
    @Autowired
    private MsgProducerService msgProducerService;


    @GetMapping("/todriver/app-name")
    public String echoAppName() {
        //使用 LoadBalanceClient 和 RestTemolate 结合的方式来访问
        ServiceInstance serviceInstance = loadBalancerClient.choose("hailtaxi-driver");
        String url = String.format("http://%s:%s/driver/echo/%s", serviceInstance.getHost(), serviceInstance.getPort(), serverConfig.appName);
        System.out.println("request url:" + url);
        return restTemplate.getForObject(url, String.class);
    }
    @GetMapping(value = "/showserver")
    public String showServer(@RequestParam("msg") String string) {

        return new StringBuilder()
                .append("<h1>")
                .append(serverConfig.appName).append("</br>")
                .append("version：").append(serverConfig.version).append("</br>")
                .append("port：").append(serverConfig.serverPort).append("</br>")
                .append("</h1>")
                .append("message").append("：").append(string).append("</br>")
                .toString();
    }


    @GetMapping(value = "/testfeign")//127.0.0.1:8001/order/testfeign?msg=hello
    public String testFeign(@RequestParam("msg") String string) {
        String string1 = targetServiceClient.showServer(string);
        return "target return:\n" + string1;
    }
    @GetMapping(value = "/teststreamkafka")//127.0.0.1:8001/order/teststreamkafka?msg=hello
    public String testStreamKafka(@RequestParam("msg") String string) {
        msgProducerService.send(string);
        return "ok";
    }
}
