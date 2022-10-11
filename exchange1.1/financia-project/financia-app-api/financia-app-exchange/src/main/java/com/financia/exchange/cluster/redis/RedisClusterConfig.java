package com.financia.exchange.cluster.redis;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.jsontype.impl.LaissezFaireSubTypeValidator;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;


/**
 * @author sec
 * @version 1.0
 * @date 2021/10/17
 **/
@Component
@Configuration
@EnableCaching
@AutoConfigureAfter(RedisAutoConfiguration.class)
public class RedisClusterConfig {

	@Bean
	public KeyGenerator keyGenerator() {
		return new KeyGenerator() {
			@Override
			public Object generate(Object target, Method method, Object... params) {
				StringBuffer sb = new StringBuffer();
				sb.append(target.getClass().getName());
				sb.append(method.getName());
				for (Object obj : params) {
					sb.append(obj.toString());
				}
				return sb.toString();
			}
		};
	}


	/**
	 * 配置自定义redisTemplate
	 *
	 * @return
	 */

	@Bean
	RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory redisConnectionFactory) {



		RedisTemplate<String, Object> template = new RedisTemplate<>();

		template.setConnectionFactory(redisConnectionFactory);



		//使用Jackson2JsonRedisSerializer来序列化和反序列化redis的value值

		Jackson2JsonRedisSerializer serializer = new Jackson2JsonRedisSerializer(Object.class);

		ObjectMapper mapper = new ObjectMapper();

		mapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);

		mapper.activateDefaultTyping(

				LaissezFaireSubTypeValidator.instance ,
				ObjectMapper.DefaultTyping.NON_FINAL,

				JsonTypeInfo.As.WRAPPER_ARRAY);

		serializer.setObjectMapper(mapper);



		template.setValueSerializer(serializer);

		//使用StringRedisSerializer来序列化和反序列化redis的key值

		template.setKeySerializer(new StringRedisSerializer());

		template.setHashKeySerializer(new StringRedisSerializer());

		template.setHashValueSerializer(serializer);

		template.afterPropertiesSet();

		return template;

	}



}
