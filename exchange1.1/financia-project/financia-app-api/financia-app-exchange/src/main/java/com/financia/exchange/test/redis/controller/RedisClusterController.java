package com.financia.exchange.test.redis.controller;

import com.financia.exchange.test.redis.service.IRedisCluster;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RedisClusterController {
    @Autowired
    @Qualifier("redisClusterTemplateService")
    private IRedisCluster iRedisCluster;

/*
    @PostMapping("/cluster/{key}")
    public void setData(@PathVariable("key") String key){
        System.out.println("set " + key);
        //redisTemplateCluster.set(key,key + " nice");
        //redisTemplateCluster.get("key1");
        System.out.println("key1="+redisTemplateCluster.get("key1"));
    }

    @GetMapping("/cluster/{key}")
    public void getData(@PathVariable("key") String key){

        System.out.println(redisTemplateCluster.get(key));

    }
*/

    @GetMapping("/test1")
    public void setData1(){
        System.out.println("key1="+iRedisCluster.get("key1"));
    }

   @GetMapping("/test2")
    public void getData1(){
        System.out.println("key2="+iRedisCluster.get("key2"));

    }


}
