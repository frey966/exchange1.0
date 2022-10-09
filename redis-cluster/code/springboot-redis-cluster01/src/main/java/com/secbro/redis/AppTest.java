package com.secbro.redis;

import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisPoolConfig;

import java.util.LinkedHashSet;
import java.util.Set;

public class AppTest {

    public static void main(String[] args) {
        JedisPoolConfig poolConfig = new JedisPoolConfig();
// 最大连接数
        poolConfig.setMaxTotal(10);
// 最大空闲数
        poolConfig.setMaxIdle(10);
// 最大允许等待时间，如果超过这个时间还未获取到连接，则会报JedisException异常：
// Could not get a resource from the pool
        poolConfig.setMaxWaitMillis(1000);
        Set<HostAndPort> nodes = new LinkedHashSet<HostAndPort>();
        nodes.add(new HostAndPort("54.179.184.125", 7001));
        nodes.add(new HostAndPort("54.179.184.125", 7002));
        nodes.add(new HostAndPort("54.169.76.123", 7003));
        nodes.add(new HostAndPort("54.169.76.123", 7004));
        nodes.add(new HostAndPort("54.255.85.152", 7005));
        nodes.add(new HostAndPort("54.255.85.152", 7006));
        //JedisCluster cluster = new JedisCluster(nodes,"chuanzhangredis", poolConfig);
        JedisCluster jedis = new JedisCluster(nodes,5000,3000,10,"chuanzhangredis", poolConfig);
        System.out.println(jedis.get("key1"));
        try {
            jedis.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}