package com.secbro.redis.service;

public interface IRedisCluster {

    String set(String key, String value);

    String get(String key);
}
