package com.secbro.redis;

import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisPoolConfig;

import java.util.HashSet;
import java.util.Set;
public class RedisClusterTest2 {

    public static void main(String[] args) {
        Set<HostAndPort> nodes = new HashSet<>();
        nodes.add(new HostAndPort("54.179.184.125", 7001));
        nodes.add(new HostAndPort("54.179.184.125", 7002));
        nodes.add(new HostAndPort("54.169.76.123", 7003));
        nodes.add(new HostAndPort("54.169.76.123", 7004));
        nodes.add(new HostAndPort("54.255.85.152", 7005));
        nodes.add(new HostAndPort("54.255.85.152", 7006));
        //池的基本配置
        JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
        //是否启用后进先出
        jedisPoolConfig.setLifo(true);
        //最大空闲连接数
        jedisPoolConfig.setMaxIdle(100);
        //最大连接数
        jedisPoolConfig.setMaxTotal(500);
        //获取连接时的最大等待毫秒数(如果设置为阻塞时BlockWhenExhausted),如果超时就抛异常, 小于零:阻塞不确定的时间,  默认-1
        jedisPoolConfig.setMaxWaitMillis(2000);
        //每次逐出检查时 逐出的最大数目 如果为负数就是 : 1/abs(n), 默认3
        jedisPoolConfig.setNumTestsPerEvictionRun(3);
        //对象空闲多久后逐出, 当空闲时间>该值 且 空闲连接>最大空闲数 时直接逐出,不再根据MinEvictableIdleTimeMillis判断 ?(默认逐出策略)
        jedisPoolConfig.setSoftMinEvictableIdleTimeMillis(1800000);
        //在获取连接的时候检查有效性, 默认false
        jedisPoolConfig.setTestOnBorrow(true);
        //在空闲时检查有效性, 默认false
        jedisPoolConfig.setTestWhileIdle(false);
        //JedisCluster jedisCluster = new JedisCluster(hostAndPortSet, jedisPoolConfig);
        //JedisCluster jedis = new JedisCluster(nodes, 1000,30,3,"chuanzhangredis",jedisPoolConfig);
        JedisCluster jedis = new JedisCluster(nodes,5000,3000,10,"chuanzhangredis", jedisPoolConfig);
        System.out.println(jedis.get("key1"));
        System.out.println(jedis.get("key2"));
        System.out.println(jedis.get("key3"));
    }

}
