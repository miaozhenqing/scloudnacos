package com.zq;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@EnableDiscoveryClient
public class DriverApplication {

    public static void main(String[] args) {
        SpringApplication.run(DriverApplication.class, args);
    }


    @RestController
    @RefreshScope
    public class EchoController {
        @Value("${server.port:0}")
        String serverPort;
        @Value("${version:default-version}")
        String version;
        @Value("${spring.redis.host:0.0.0.0}")
        String springRedisHost;
        @Value("${spring.redis.port:0}")
        String springRedisPort;

        @GetMapping(value = "/echo/{string}")
        public String echo(@PathVariable String string) {
            return new StringBuilder()
                    .append("version").append("：").append(version).append("</br>")
                    .append("port").append("：").append(serverPort).append("</br>")
                    .append("springRedisHost").append("：").append(springRedisHost).append("</br>")
                    .append("springRedisPort").append("：").append(springRedisPort).append("</br>")
                    .append("message").append("：").append(string).append("</br>")
                    .toString();
        }
    }

}