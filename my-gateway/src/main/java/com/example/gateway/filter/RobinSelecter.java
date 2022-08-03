package com.example.gateway.filter;

import com.alibaba.cloud.nacos.ribbon.NacosServer;
import com.example.gateway.common.MyThreadLocal;
import com.google.common.collect.Lists;
import com.netflix.loadbalancer.ILoadBalancer;
import com.netflix.loadbalancer.RoundRobinRule;
import com.netflix.loadbalancer.Server;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Component
@Slf4j
public class RobinSelecter extends RoundRobinRule {

    private AtomicInteger nextServerCyclicCounter;

    public RobinSelecter(){
        this.nextServerCyclicCounter = new AtomicInteger(0);
    }

    @Override
    public Server choose(ILoadBalancer lb, Object key) {
        if(null == lb){
            return null;
        }
        Server selectedServer = null;
        int count = 0;
        while (Objects.isNull(selectedServer) && count++ < 10){
            List<Server> reachableServers = lb.getReachableServers();
            List<Server> allServers = lb.getAllServers();
            int upCount = reachableServers.size();
            int serverCount = allServers.size();
            if(upCount == 0 && serverCount ==0){
                return null;
            }
            //根据元数据筛选nacos服务
            List<Server> filterServers = reachableServers.stream()
                    .filter(item-> MyThreadLocal.get().equals(((NacosServer)item).getMetadata().get("version")))
                    .collect(Collectors.toList());
            int nextServerIndex = this.incrementAndGetModulo(filterServers.size());
            selectedServer = filterServers.get(nextServerIndex);
            if(null == selectedServer){
                Thread.yield();
                continue;
            }
            if(selectedServer.isAlive() && selectedServer.isReadyToServe()){
                return selectedServer;
            }
        }

        if (count >= 10) {
            log.warn("No available alive servers after 10 tries from load balancer: " + lb);
        }
        return selectedServer;

    }

    private int incrementAndGetModulo(int modulo) {
        int current;
        int next;
        do {
            current = this.nextServerCyclicCounter.get();
            next = (current + 1) % modulo;
        } while(!this.nextServerCyclicCounter.compareAndSet(current, next));

        return next;
    }
}
