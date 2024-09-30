package com.zq.service;

import org.springframework.stereotype.Component;

@Component
public class DriverServiceFallback implements TargetServiceClient {

    @Override
    public String showServer(String msg) {
        return "fallback for [showServer]";
    }
}
