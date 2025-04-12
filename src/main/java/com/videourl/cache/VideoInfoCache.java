package com.videourl.cache;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.videourl.services.VideoService;
import com.videourl.utils.ffmpeg.VideoInfo;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

@Component
public class VideoInfoCache {
    private final Cache<String, CompletableFuture<VideoInfo>> videoInfoCache;
    private final VideoService videoService;

    public VideoInfoCache(VideoService videoService) {
        this.videoService = videoService;
        // 构建 Guava Cache 实例，设置缓存过期时间为写入后 10 分钟
        this.videoInfoCache = CacheBuilder.newBuilder().expireAfterWrite(10, TimeUnit.MINUTES).build();
    }


    public CompletableFuture<VideoInfo> getVideoInfo(String url) {
        try {
            // 从缓存中获取视频信息，如果不存在则通过 load 方法加载
            return videoInfoCache.get(url, () -> CompletableFuture.supplyAsync(() -> {
                try {
                    // 调用服务层获取视频信息
                    return videoService.getVideoInfo(url);
                } catch (Exception e) {
                    // 异常时移除缓存条目，避免后续请求继续使用错误数据[3,6](@ref)
                    videoInfoCache.invalidate(url);
                    throw e;
                }
            }));
        } catch (ExecutionException e) {
            // 处理异步任务执行异常
            Throwable cause = e.getCause();
            if (cause instanceof RuntimeException) {
                throw (RuntimeException) cause; // 直接抛出已知运行时异常
            } else {
                throw new RuntimeException("获取视频信息失败", cause); // 包装非预期异常
            }
        }
    }
}