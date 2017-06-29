package com.zhiliao.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.core.RedisTemplate;

/**
 * Description:redis 缓存配置
 *
 * @author Jin
 * @create 2017-04-12
 **/
public class RedisCacheConfiguration extends CachingConfigurerSupport {

    @Bean
    public CacheManager cacheManager(@Autowired RedisTemplate<Object, Object> redisTemplate) {
        RedisCacheManager redisCacheManager = new RedisCacheManager(redisTemplate);
        redisCacheManager.setDefaultExpiration(1800);
        return redisCacheManager;
    }
}