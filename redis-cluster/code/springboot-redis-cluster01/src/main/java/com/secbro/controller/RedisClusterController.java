package com.secbro.controller;


import com.secbro.sevice.IRedisCluster;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RedisClusterController {

    @Autowired
    @Qualifier("redisClusterService")
    private IRedisCluster redisCluster;

    @PostMapping("/jedis1")
    public void setDataByJedis(){
        System.out.println(redisCluster.get("key1"));
    }

    @GetMapping("/jedis2")
    public void getDataByJedis(){
        System.out.println(redisCluster.get("key2"));
    }

}
