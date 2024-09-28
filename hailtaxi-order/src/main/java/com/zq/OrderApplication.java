package com.zq;

import com.alibaba.cloud.nacos.ribbon.NacosRule;
import com.netflix.loadbalancer.IRule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
@EnableDiscoveryClient
public class OrderApplication {
    public static void main(String[] args) {
        SpringApplication.run(OrderApplication.class, args);
    }

    @RestController()
    @RequestMapping("/order")
    public class NacosController {

        @Autowired
        private LoadBalancerClient loadBalancerClient;
        @Autowired
        private RestTemplate restTemplate;

        @Value("${spring.application.name}")
        private String appName;

        @GetMapping("/echo/app-name")
        public String echoAppName() {
            //使用 LoadBalanceClient 和 RestTemolate 结合的方式来访问
            ServiceInstance serviceInstance = loadBalancerClient.choose("hailtaxi-driver");
            String url = String.format("http://%s:%s/driver/echo/%s", serviceInstance.getHost(), serviceInstance.getPort(), appName);
            System.out.println("request url:" + url);
            return restTemplate.getForObject(url, String.class);
        }

        @GetMapping(value = "/{string}")
        public String driber(@PathVariable String string) {
            return new StringBuilder()
                    .append("<h1>order server</h1>").append("</br>")
                    .append("message").append("：").append(string).append("</br>")
                    .toString();
        }
    }

    //实例化 RestTemplate 实例
    @Bean
    public RestTemplate restTemplate() {

        return new RestTemplate();
    }

    /***
     * Nacos负载均衡算法
     * @return
     */
    @Bean
    @Scope(value="prototype")
    public IRule loadBalanceRule(){
        return new NacosRule();
    }
}