package com.videourl.config;

import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

@Configuration
@EnableCaching
public class CacheConfig {

    @Bean
    public CacheManager cacheManager() {
        CaffeineCacheManager cacheManager = new CaffeineCacheManager();
        // 配置 Caffeine 缓存的属性
        cacheManager.setCaffeine(Caffeine.newBuilder().initialCapacity(100) // 初始缓存容量，可根据预估的初始键值对数量调整
                .maximumSize(100000)    // 最大缓存项数量，可根据实际情况调整
                .expireAfterWrite(30, TimeUnit.MINUTES) // 写入后 30 分钟过期，可根据业务需求调整
                .recordStats()); // 开启统计功能，方便监控缓存性能
        return cacheManager;
    }
}