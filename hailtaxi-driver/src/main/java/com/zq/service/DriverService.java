package com.zq.service;

import com.zq.dto.Driver;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
@Service
public class DriverService {
    public static Map<Integer, Driver> driverMap = new HashMap<Integer, Driver>() {{
        put(1, new Driver(1, "tom", "bmw"));
        put(2, new Driver(1, "jack", "benz"));
    }};
}
