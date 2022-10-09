package com.secbro;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

@SpringBootTest
public class SpringbootredisApplicationTest {

    @Autowired
    StringRedisTemplate redisTemplate;

    ValueOperations<String, String> stringRedis;

    @PostConstruct
    public void init() {
        stringRedis = redisTemplate.opsForValue();
    }

    @Test
    void contextLoads() {
        System.out.println("stringRedis=="+stringRedis);
        System.out.println("key1="+stringRedis.get("key1"));
        List<String> list = new ArrayList<>();
        list.add("test");
        list.add("test2");
        //System.out.println(redisTemplate.countExistingKeys(list));
    }

}
