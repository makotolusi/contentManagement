package com.cyou.video.mobile.server.cms.service.common;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.Assert;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.Protocol;

public class RedisTemplate implements InitializingBean {

    private JedisPool pool;

    public RedisTemplate(JedisPoolConfig config, String hostname, int port, String password) {
        if ("".equalsIgnoreCase(password)) {
            password = null;
        }
        config.setBlockWhenExhausted(false);
        pool = new JedisPool(config, hostname, port, Protocol.DEFAULT_TIMEOUT, password);
    }

    public Jedis jedis() {
        return pool.getResource();
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        Assert.notNull(pool, "'pool' cannot be null");
    }

    public void returnResource(Jedis jedis) {
        pool.returnResource(jedis);
    }

    public void returnBrokenResource(Jedis jedis) {
        pool.returnBrokenResource(jedis);
    }
    
    public void closePool(){
        pool.destroy();
    }
    
}
