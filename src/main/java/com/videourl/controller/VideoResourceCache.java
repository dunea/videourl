package com.videourl.controller;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.videourl.service.VideoService;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

public class VideoResourceCache {
    private final Cache<String, CompletableFuture<byte[]>> coverCache = CacheBuilder.newBuilder().expireAfterWrite(1, TimeUnit.MINUTES) // 写入后 1 分钟过期
            .build();

    private final VideoService videoService;

    public VideoResourceCache(VideoService videoService) {
        this.videoService = videoService;
    }

    public CompletableFuture<byte[]> getVideoCover(String url) {
        try {
            return coverCache.get(url, () -> CompletableFuture.supplyAsync(() -> {
                try {
                    return videoService.getVideoCover(url);
                } catch (Exception e) {
                    // 出现异常时，从缓存中移除该 URL 对应的 CompletableFuture
                    coverCache.invalidate(url);
                    throw e;
                }
            }));
        } catch (ExecutionException e) {
            // 处理 ExecutionException
            Throwable cause = e.getCause();
            if (cause instanceof RuntimeException) {
                throw (RuntimeException) cause;
            } else {
                throw new RuntimeException("Failed to load video cover", cause);
            }
        }
    }
}