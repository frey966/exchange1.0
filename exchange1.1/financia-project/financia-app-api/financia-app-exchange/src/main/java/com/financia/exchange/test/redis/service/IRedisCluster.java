package com.financia.exchange.test.redis.service;

public interface IRedisCluster {

    String set(String key, String value);

    String get(String key);
}
