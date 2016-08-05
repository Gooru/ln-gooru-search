package org.ednovo.gooru.search.es.service;



import java.util.Properties;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.annotation.Resource;

import org.ednovo.gooru.search.es.constant.Constants;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

public final class RedisClient  {

  private JedisPool pool = null;
  
  @Autowired
  @Resource(name = "configSettings")
  private Properties searchSettings;
  
  protected Properties getSearchSettings() {
    return this.searchSettings;
  }
  
  @PostConstruct
  public void initializeComponent() {
    JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
    jedisPoolConfig.setMaxTotal(1000);
    jedisPoolConfig.setMaxIdle(10);
    jedisPoolConfig.setMinIdle(1);
    jedisPoolConfig.setMaxWaitMillis(30000);
    jedisPoolConfig.setTestOnBorrow(true);
    String host = getSearchSettings().getProperty(Constants.REDIS_SERVER_HOST);
    Integer port = Integer.valueOf(getSearchSettings().getProperty(Constants.REDIS_SERVER_PORT));
    pool = new JedisPool(jedisPoolConfig, host, port);
  }

  public static RedisClient instance() {
    return Holder.INSTANCE;
  }

  public JSONObject getJsonObject(final String key) throws JSONException{
	  JSONObject result = null;
    Jedis jedis = null;
    try {
      jedis = getJedis();
      String json = jedis.get(key);
      if (json != null) {
        result = new JSONObject(json);
      }
    } finally {
      if (jedis != null) {
        jedis.close();
      }
    }
    return result;
  }

  public String get(final String key) {
    String value = null;
    Jedis jedis = null;
    try {
      jedis = getJedis();
      value = jedis.get(key);
    } finally {
      if (jedis != null) {
        jedis.close();
      }
    }
    return value;
  }

  public void del(String key) {
    Jedis jedis = null;
    try {
      jedis = getJedis();
      jedis.del(key);
    } finally {
      if (jedis != null) {
        jedis.close();
      }
    }
  }

  public void expire(String key, int seconds) {
    Jedis jedis = null;
    try {
      jedis = getJedis();
      jedis.expire(key, seconds);
    } finally {
      if (jedis != null) {
        jedis.close();
      }
    }
  }

  public void set(String key, String value, int expireInSeconds) {
    Jedis jedis = null;
    try {
      jedis = getJedis();
      jedis.set(key, value);
      jedis.expire(key, expireInSeconds);
    } finally {
      if (jedis != null) {
        jedis.close();
      }
    }
  }

  public void set(String key, String value) {
    Jedis jedis = null;
    try {
      jedis = getJedis();
      jedis.set(key, value);
    } finally {
      if (jedis != null) {
        jedis.close();
      }
    }
  }

  public Jedis getJedis() {

    return pool.getResource();
  }

  @PreDestroy
  public void finalizeComponent() {
    if (pool != null) {
      pool.destroy();
    }
  }

  private static class Holder {
    private static final RedisClient INSTANCE = new RedisClient();
  }
}

