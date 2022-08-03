package com.example.gateway.config;


import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@RefreshScope
public class GrayConfig {

    @Value("${version.stable}")
    public String stable;

    @Value("${version.gray}")
    public String gray;
}
