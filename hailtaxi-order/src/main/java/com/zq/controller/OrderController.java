package com.zq.controller;

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
    @Value("${server.port:0}")
    String serverPort;
    @Value("${version:default-version}")
    String version;
    @Autowired
    private LoadBalancerClient loadBalancerClient;
    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private TargetServiceClient targetServiceClient;

    @Value("${spring.application.name}")
    private String appName;

    @GetMapping("/todriver/app-name")
    public String echoAppName() {
        //使用 LoadBalanceClient 和 RestTemolate 结合的方式来访问
        ServiceInstance serviceInstance = loadBalancerClient.choose("hailtaxi-driver");
        String url = String.format("http://%s:%s/driver/echo/%s", serviceInstance.getHost(), serviceInstance.getPort(), appName);
        System.out.println("request url:" + url);
        return restTemplate.getForObject(url, String.class);
    }
    @GetMapping(value = "/showserver")
    public String showServer(@RequestParam("msg") String string) {

        return new StringBuilder()
                .append("<h1>")
                .append(appName).append("</br>")
                .append("version：").append(version).append("</br>")
                .append("port：").append(serverPort).append("</br>")
                .append("</h1>")
                .append("message").append("：").append(string).append("</br>")
                .toString();
    }


    @GetMapping(value = "/testfeign")//127.0.0.1:8001/order/testfeign?msg=hello
    public String testFeign(@RequestParam("msg") String string) {
        String string1 = targetServiceClient.showServer(string);
        return "target return:\n" + string1;
    }
}
