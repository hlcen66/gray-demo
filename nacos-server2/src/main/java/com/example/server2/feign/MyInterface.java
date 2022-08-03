package com.example.server2.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(value = "my-server")
public interface MyInterface {


    /**
     * 灰度版本2.0
     * @return
     */
    @GetMapping("/test/version")
    String testVersion();
}
