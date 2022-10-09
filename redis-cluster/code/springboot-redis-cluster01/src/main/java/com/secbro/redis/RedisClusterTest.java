package com.secbro.redis;

import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.JedisCluster;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
public class RedisClusterTest {

    public static void main(String[] args) throws IOException {
        GenericObjectPoolConfig config = new GenericObjectPoolConfig();
        // 最大连接数
        config.setMaxTotal(30);
        // 最大空闲数
        config.setMaxIdle(10);
        // 最大允许等待时间，如果超过这个时间还未获取到连接，则会报JedisException异常
        // Could not get a resource from the pool
        config.setMaxWaitMillis(2000);
        Set<HostAndPort> nodes = new HashSet<HostAndPort>();
        nodes.add(new HostAndPort("54.179.184.125", 7001));
        nodes.add(new HostAndPort("54.179.184.125", 7002));
        nodes.add(new HostAndPort("54.169.76.123", 7003));
        nodes.add(new HostAndPort("54.169.76.123", 7004));
        nodes.add(new HostAndPort("54.255.85.152", 7005));
        nodes.add(new HostAndPort("54.255.85.152", 7006));
        //1.如果集群没有密码
        //JedisCluster jc = new JedisCluster(jedisClusterNode,config);
        //2.如果使用到密码，请使用如下构造函数
        JedisCluster jedis = new JedisCluster(nodes,5000,3000,10,"chuanzhangredis", config);
        System.out.println("==================");
        System.out.println(jedis.get("key1"));
        jedis.close();
    }

    }
