package com.secbro.redis.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service("redisClusterTemplateService")
public class RedisClusterTemplateService implements IRedisCluster {

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Override
    public String set(String key, String value) {
        redisTemplate.opsForValue().set(key,value);
        return key;
    }

    @Override
    public String get(String key) {
        return redisTemplate.opsForValue().get(key);
    }
}
