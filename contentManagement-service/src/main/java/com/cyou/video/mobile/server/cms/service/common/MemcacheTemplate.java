package com.cyou.video.mobile.server.cms.service.common;

import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.TimeoutException;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.Assert;

import com.google.code.ssm.Cache;
import com.google.code.ssm.CacheFactory;
import com.google.code.ssm.providers.CacheException;

public class MemcacheTemplate implements InitializingBean {

    private final Cache cache;

    public MemcacheTemplate(Cache cache) {
        this.cache = cache;
    }

    public MemcacheTemplate(CacheFactory factory) {
        this.cache = factory.getCache();
    }

    public <T> T get(final String key) {
        try {
            return cache.get(key, null);
        } catch (TimeoutException e) {
            e.printStackTrace();
        } catch (CacheException e) {
            e.printStackTrace();
        }
        return null;
    }
    /**
     * 
     * @param keys
     * @return
     */
    public Map<String, Object> getBulk(final String... keys) {
        try {
            return cache.getBulk(Arrays.asList(keys), null);
        } catch (TimeoutException e) {
            e.printStackTrace();
        } catch (CacheException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 添加，如果key存在不会替换，不存在会新建。
     * 
     * @param key
     * @param value
     * @param expiration
     */
    public void add(final String key, final Object value, final int expiration) {
        try {
            cache.add(key, expiration, value, null);
        } catch (TimeoutException e) {
            e.printStackTrace();
        } catch (CacheException e) {
            e.printStackTrace();
        }
    }

    /**
     * 添加，如果key存在直接覆盖，不存在会新建。
     * 
     * @param key
     * @param value
     * @param expiration
     */
    public void set(final String key, final Object value, final int expiration) {
        try {
            cache.set(key, expiration, value, null);
        } catch (TimeoutException e) {
            e.printStackTrace();
        } catch (CacheException e) {
            e.printStackTrace();
        }
    }

    public void delete(final String key) {
        try {
            cache.delete(key);
        } catch (TimeoutException e) {
            e.printStackTrace();
        } catch (CacheException e) {
            e.printStackTrace();
        }
    }

    /**
     * 批量删除
     * 
     * @param keys
     */
    public void delete(String... keys) {
        try {
            cache.delete(Arrays.asList(keys));
        } catch (TimeoutException e) {
            e.printStackTrace();
        } catch (CacheException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        Assert.notNull(cache);
    }

}
