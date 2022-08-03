package com.example.server2.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
public class MyController {


    @GetMapping("/version")
    public String version(){
        return "当前访问灰度版本-------version:2.0";
    }

}
