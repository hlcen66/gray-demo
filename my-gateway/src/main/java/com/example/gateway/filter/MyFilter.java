package com.example.gateway.filter;

import com.example.gateway.common.MyThreadLocal;
import com.example.gateway.config.GrayConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Objects;

@Component
@RefreshScope
public class MyFilter implements GlobalFilter, Ordered {

    @Autowired
    private GrayConfig grayConfig;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        ServerHttpResponse response = exchange.getResponse();
        response.getHeaders().add("Content-Type", "application/json;charset=UTF-8");
        //自定义网关流量规则
        Integer userId = Integer.parseInt(Objects.requireNonNull(request.getHeaders().get("userId")).get(0));
        if(StringUtils.isEmpty(userId)){
            MyThreadLocal.set(grayConfig.getStable());
            return chain.filter(exchange.mutate().request(request).build());
        }
        //染色规则
        if(userId % 10 == 1 || userId % 10 == 9){
            MyThreadLocal.set(grayConfig.getGray());
        }else{
            MyThreadLocal.set(grayConfig.getStable());
        }
        return chain.filter(exchange.mutate().request(request).build());
    }

    @Override
    public int getOrder() {
        return -1;
    }
}
