package com.videourl.cache;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.videourl.services.VideoService;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

/**
 * 视频封面资源缓存管理器
 * 功能：通过 Guava Cache 缓存视频封面字节流，减少对 VideoService 的重复调用
 * 特性：
 * - 缓存条目1分钟自动过期
 * - 异步加载数据避免阻塞
 * - 异常时自动清理无效缓存
 */
public class VideoResourceCache {
    // Guava Cache 实例，键为视频URL，值为封面数据的异步计算结果
    private final Cache<String, CompletableFuture<byte[]>> coverCache = CacheBuilder.newBuilder().expireAfterWrite(1, TimeUnit.MINUTES).build();

    private final VideoService videoService; // 视频服务接口，用于获取封面数据

    /**
     * 构造函数注入 VideoService
     *
     * @param videoService 视频服务实例
     */
    public VideoResourceCache(VideoService videoService) {
        this.videoService = videoService;
    }

    /**
     * 获取视频封面数据（优先从缓存读取）
     *
     * @param url 视频资源URL
     * @return 封面数据字节流的异步计算结果
     */
    public CompletableFuture<byte[]> getVideoCover(String url) {
        try {
            // 尝试从缓存获取数据，若不存在则异步加载
            return coverCache.get(url, () -> CompletableFuture.supplyAsync(() -> {
                try {
                    // 调用服务层获取封面数据
                    return videoService.getVideoCover(url);
                } catch (Exception e) {
                    // 异常时移除缓存条目，避免后续请求继续使用错误数据[3,6](@ref)
                    coverCache.invalidate(url);
                    throw e;
                }
            }));
        } catch (ExecutionException e) {
            // 处理异步任务执行异常
            Throwable cause = e.getCause();
            if (cause instanceof RuntimeException) {
                throw (RuntimeException) cause; // 直接抛出已知运行时异常
            } else {
                throw new RuntimeException("加载视频封面失败", cause); // 包装非预期异常
            }
        }
    }
}