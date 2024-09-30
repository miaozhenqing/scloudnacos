package com.zq.config;

//import com.alibaba.cloud.nacos.ribbon.NacosRule;
//import com.netflix.loadbalancer.IRule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.web.client.RestTemplate;

@Configuration
public class ServerConfig {

    //实例化 RestTemplate 实例
    @Bean
    public RestTemplate restTemplate() {

        return new RestTemplate();
    }

    /***
     * Nacos负载均衡算法
     * @return
     */
//    @Bean
//    @Scope(value="prototype")
//    public IRule loadBalanceRule(){
//        return new NacosRule();
//    }
}
