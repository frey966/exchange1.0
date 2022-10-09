package com.secbro.redis;
import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisPoolConfig;
import java.util.LinkedHashSet;
import java.util.Set;
public class RedisMain {
    public static void main(String[] args) {
        JedisCluster cluster =null;
        try {
            System.out.println("====begin-----------");
            Set<HostAndPort> nodes = new LinkedHashSet<HostAndPort>();
            //一般选用slaveof从IP+端口进行增删改查，不用master
            nodes.add(new HostAndPort("54.179.184.125", 7001));
            nodes.add(new HostAndPort("54.179.184.125", 7002));
            nodes.add(new HostAndPort("54.169.76.123", 7003));
            nodes.add(new HostAndPort("54.169.76.123", 7004));
            nodes.add(new HostAndPort("54.255.85.152", 7005));
            nodes.add(new HostAndPort("54.255.85.152", 7006));
            // Jedis连接池配置
            JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
            // 最大空闲连接数, 默认8个
            jedisPoolConfig.setMaxIdle(100);
            // 最大连接数, 默认8个
            jedisPoolConfig.setMaxTotal(500);
            //最小空闲连接数, 默认0
            jedisPoolConfig.setMinIdle(10);
            // 获取连接时的最大等待毫秒数(如果设置为阻塞时BlockWhenExhausted),如果超时就抛异常, 小于零:阻塞不确定的时间, 默认-1
            jedisPoolConfig.setMaxWaitMillis(2000); // 设置2秒
            //对拿到的connection进行validateObject校验
            jedisPoolConfig.setTestOnBorrow(true);
            //未设置auth Password
            //JedisCluster jedis = new JedisCluster(nodes, jedisPoolConfig);
            //设置auth Password
            JedisCluster jedis = new JedisCluster(nodes,5000,3000,10,"chuanzhangredis", jedisPoolConfig);
            //jedis.set("person-name", "123");
            System.out.println("===="+jedis.get("person-name"));
            System.out.println("===="+jedis.get("key1"));
            System.out.println("===="+jedis.get("key2"));
            System.out.println("===="+jedis.get("key3"));
            System.out.println("===="+jedis.get("key4"));
        }catch(Exception e) {
            e.printStackTrace();
        }finally {
            if(null !=cluster)
                cluster.close();
        }
    }
}