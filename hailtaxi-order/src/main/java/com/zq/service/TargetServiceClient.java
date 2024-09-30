package com.zq.service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = "hailtaxi-driver", path = "/driver", fallback = DriverServiceFallback.class)
public interface TargetServiceClient {
    @GetMapping(value = "/showserver")
    String showServer(@RequestParam("msg") String msg);
}
