package com.zq.controller;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.zq.dto.Driver;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.*;

import static com.zq.service.DriverService.driverMap;

@RestController
@RequestMapping("/driver")
@RefreshScope
@SentinelResource(defaultFallback = "defaultExHandler")
public class DriverController {
    @Value("${server.port:0}")
    String serverPort;
    @Value("${version:default-version}")
    String version;
    @Value("${spring.redis.host:0.0.0.0}")
    String springRedisHost;
    @Value("${spring.redis.port:0}")
    String springRedisPort;

    @Value("${spring.application.name}")
    private String appName;
    @GetMapping(value = "/echo")
    public String echo(@RequestParam("msg") String string) {
        return new StringBuilder()
                .append("version").append("：").append(version).append("</br>")
                .append("port").append("：").append(serverPort).append("</br>")
                .append("springRedisHost").append("：").append(springRedisHost).append("</br>")
                .append("springRedisPort").append("：").append(springRedisPort).append("</br>")
                .append("message").append("：").append(string).append("</br>")
                .toString();
    }

    @GetMapping(value = "/showserver")
    public String driber(@RequestParam("msg") String string) {
        if (string.equals("ex")) {
            throw new RuntimeException("ex test");
        }
        return new StringBuilder()
                .append("<h1>")
                .append(appName).append("</br>")
                .append("version：").append(version).append("</br>")
                .append("port：").append(serverPort).append("</br>")
                .append("</h1>")
                .append("message").append("：").append(string).append("</br>")
                .toString();
    }


    /****
     * 司机信息
     */
    @SentinelResource(value = "info", blockHandler = "blockExHandler", fallback = "exHandler")
    @GetMapping(value = "/info")
    public Driver info(@RequestParam(value = "id") int id) throws BlockException {
        Driver driver = driverMap.get(id);
        if (driver == null) {
//                throw new SystemBlockException("司机id=" + id + "不存在", null);
            throw new RuntimeException("司机id=" + id + "不存在", null);
        }
        return driver;
    }

    /**
     * info资源出现BlockException后的降级处理
     */
    public Driver blockExHandler(int id, BlockException e) {
        Driver driver = new Driver(id, "unknow", "unknow");
        driver.setMsg(e.toString());
        return driver;
    }

    /**
     * info资源出现任何Exception后的降级处理
     */
    public Driver exHandler(int id, Throwable e) {
        Driver driver = new Driver(id, "unknow", "unknow");
        driver.setMsg(e.toString());
        return driver;
    }

    /**
     * info资源出现任何Exception后的降级处理
     */
    public Driver defaultExHandler(int id, Throwable e) {
        Driver driver = new Driver(id, "unknow", "unknow");
        driver.setMsg("全局默认ex handler");
        return driver;
    }

    /***
     * 搜索指定城市的司机
     */
    @SentinelResource(value = "search")
    @GetMapping(value = "/search")
    public Driver search(@RequestParam(value = "city") String city) {
        //假设查询到了一个司机信息
        Driver driver = driverMap.get(1);
        driver.setMsg(city);
        return driver;
    }

    /****
     * 更新司机信息
     */
    @PutMapping(value = "/status")
    public Driver status(@RequestParam(value = "id") String id, @RequestParam(value = "status") Integer status) throws Exception {
        //修改状态
        Driver driver = driverMap.get(id);
        if (driver == null) {
            throw new RuntimeException("id=" + id + ",不存在");
        }
        driver.setStatus(status);
        return driver;
    }

}
