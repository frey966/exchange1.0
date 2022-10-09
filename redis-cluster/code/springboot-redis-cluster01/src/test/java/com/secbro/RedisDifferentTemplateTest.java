package com.secbro;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;

import javax.annotation.Resource;

/**
 * @author zzs
 */
@Slf4j
@SpringBootTest
class RedisDifferentTemplateTest {


	@Resource
	private RedisTemplate<String, Object> redisTemplate;

	@Resource
	private StringRedisTemplate stringRedisTemplate;


	@Test
	void testSimple() {
		redisTemplate.opsForValue().set("myWeb", "www.choupangxia.com");
		Assertions.assertEquals("www.choupangxia.com", redisTemplate.opsForValue().get("myWeb"));

		Assertions.assertEquals("www.choupangxia.com", stringRedisTemplate.opsForValue().get("myWeb"));
	}
}