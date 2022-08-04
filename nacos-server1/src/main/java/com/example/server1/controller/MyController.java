package com.example.server1.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
public class MyController {

    /**
     * 稳定版本1.0
     * @return
     */
    @GetMapping("/version")
    public String version(){
        return "当前访问稳定版本-----version:1.0";
    }

}
