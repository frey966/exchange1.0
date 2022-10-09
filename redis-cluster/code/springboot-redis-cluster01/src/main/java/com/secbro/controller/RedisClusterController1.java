package com.secbro.controller;

import com.secbro.sevice.IRedisCluster;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RedisClusterController1 {
    @Autowired
    @Qualifier("redisClusterTemplateService")
    private IRedisCluster redisTemplateCluster;

    @PostMapping("/cluster/{key}")
    public void setData(@PathVariable("key") String key){
        System.out.println("set " + key);
        redisTemplateCluster.set(key,key + " nice");
    }

    @GetMapping("/cluster/{key}")
    public void getData(@PathVariable("key") String key){

        System.out.println(redisTemplateCluster.get(key));
    }


}
