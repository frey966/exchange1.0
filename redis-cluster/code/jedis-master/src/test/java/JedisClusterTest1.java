import org.junit.Test;
import redis.clients.jedis.ConnectionPoolConfig;
import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.JedisCluster;

import java.util.HashSet;
import java.util.Set;

public class JedisClusterTest1  {

  private static final int DEFAULT_TIMEOUT = 2000;
  private static final int DEFAULT_REDIRECTIONS = 5;
  private static final ConnectionPoolConfig DEFAULT_POOL_CONFIG = new ConnectionPoolConfig();
  //private static final DefaultJedisClientConfig DEFAULT_CLIENT_CONFIG = DefaultJedisClientConfig.builder().password("cluster").build();

  @Test
  public void testCalculateConnectionPerSlot() {
    Set<HostAndPort> jedisClusterNode = new HashSet<>();
    jedisClusterNode.add(new HostAndPort("54.179.184.125", 7001));
    jedisClusterNode.add(new HostAndPort("54.179.184.125", 7002));
    jedisClusterNode.add(new HostAndPort("54.169.76.123", 7003));
    jedisClusterNode.add(new HostAndPort("54.169.76.123", 7004));
    jedisClusterNode.add(new HostAndPort("54.255.85.152", 7005));
    jedisClusterNode.add(new HostAndPort("54.255.85.152", 7006));
    System.out.println("---begin-----------");
    try (
            JedisCluster jc = new JedisCluster(jedisClusterNode, DEFAULT_TIMEOUT, DEFAULT_TIMEOUT, DEFAULT_REDIRECTIONS, "chuanzhangredis", DEFAULT_POOL_CONFIG))
    {
      System.out.println("jc=="+jc);
      jc.set("foo", "bar1");
      jc.set("test", "test1");
      System.out.println("=foo=="+jc.get("foo"));
      System.out.println("=test=="+jc.get("test"));
      System.out.println("---end-----------");
      //assertEquals("bar", node3.get("foo"));
      //assertEquals("test", node2.get("test"));
    }

/*    try (JedisCluster jc2 = new JedisCluster(new HostAndPort("54.179.184.125", 7001), DEFAULT_TIMEOUT,
        DEFAULT_TIMEOUT, DEFAULT_REDIRECTIONS, "chuanzhangredis", DEFAULT_POOL_CONFIG)) {
      jc2.set("foo", "bar");
      jc2.set("test", "test");

    }*/
  }


}
