package com.zq.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ServerConfig {
    @Value("${server.port:0}")
    public String serverPort;
    @Value("${version:default-version}")
    public String version;
    @Value("${spring.redis.host:0.0.0.0}")
    public String springRedisHost;
    @Value("${spring.redis.port:0}")
    public String springRedisPort;

    @Value("${spring.application.name}")
    public String appName;
}
