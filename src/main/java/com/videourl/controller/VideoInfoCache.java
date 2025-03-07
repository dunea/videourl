package com.videourl.controller;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.videourl.service.VideoService;
import com.videourl.util.VideoInfo;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

public class VideoInfoCache {
    private final Cache<String, VideoInfo> cache;
    private final VideoService videoService;

    public VideoInfoCache(VideoService videoService) {
        this.videoService = videoService;
        // 构建 Guava Cache 实例，设置缓存过期时间为写入后 10 分钟
        this.cache = CacheBuilder.newBuilder().expireAfterWrite(10, TimeUnit.MINUTES).build();
    }

    public VideoInfo getVideoInfo(String url) {
        try {
            // 从缓存中获取视频信息，如果不存在则通过 load 方法加载
            return cache.get(url, () -> videoService.getVideoInfo(url));
        } catch (ExecutionException e) {
            // 处理 ExecutionException，这里只是简单地重新抛出异常
            throw new RuntimeException("Failed to get video info for url: " + url, e.getCause());
        }
    }
}