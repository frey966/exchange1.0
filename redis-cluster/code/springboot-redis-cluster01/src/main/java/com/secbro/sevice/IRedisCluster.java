package com.secbro.sevice;

public interface IRedisCluster {

    String set(String key, String value);

    String get(String key);
}
