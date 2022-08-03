package com.example.server1.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(value = "my-server")
public interface MyInterface {

    @GetMapping("/test/version")
    String testVersion();
}
