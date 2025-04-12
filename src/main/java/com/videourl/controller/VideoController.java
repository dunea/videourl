package com.videourl.controller;

import com.videourl.cache.VideoInfoCache;
import com.videourl.cache.VideoCoverCache;
import com.videourl.utils.ffmpeg.VideoInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;


// 这里放视频接口相关的控制器，用于处理请求。
// 使用 @RestController 标注该类为 Spring 管理的控制器
@RestController
public class VideoController {

    private final VideoInfoCache videoInfoCache;
    private final VideoCoverCache videoCoverCache;

    @Autowired
    public VideoController(VideoInfoCache videoInfoCache, VideoCoverCache videoCoverCache) {
        this.videoInfoCache = videoInfoCache;
        this.videoCoverCache = videoCoverCache;
    }

    // 根据视频url获取视频封面
    @CrossOrigin(origins = "*")
    @GetMapping("/info")
    public CompletableFuture<ResponseEntity<VideoInfo>> getVideoInfo(@RequestParam("url") String videoUrl) {
        return videoInfoCache.getVideoInfo(videoUrl).thenApply(videoInfo -> {
            // 新建headers响应头
            HttpHeaders headers = new HttpHeaders();
            // 响应封面的视频信息类型
            headers.setContentType(MediaType.APPLICATION_JSON);

            // 设置缓存控制头，让浏览器缓存响应 7 天
            CacheControl cacheControl = CacheControl.maxAge(7, TimeUnit.DAYS);
            headers.setCacheControl(cacheControl.getHeaderValue());

            // 返回响应的视频信息
            return new ResponseEntity<>(videoInfo, headers, HttpStatus.OK);
        }).exceptionally(e -> {
            // 处理异常，可根据具体情况进行日志记录和错误响应
            Thread.currentThread().interrupt();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        });
    }

    // 根据视频url获取视频封面
    @CrossOrigin(origins = "*")
    @GetMapping("/cover")
    public CompletableFuture<ResponseEntity<?>> getVideoCover(@RequestParam("url") String videoUrl) {
        return videoCoverCache.getVideoCover(videoUrl).thenApply(coverBytes -> {
            if (coverBytes == null || coverBytes.length == 0) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }

            // 新建headers响应头
            HttpHeaders headers = new HttpHeaders();
            // 响应封面的图片类型
            headers.setContentType(MediaType.IMAGE_JPEG);
            headers.setContentLength(coverBytes.length);

            // 设置缓存控制头，让浏览器缓存响应 7 天
            CacheControl cacheControl = CacheControl.maxAge(7, TimeUnit.DAYS);
            headers.setCacheControl(cacheControl.getHeaderValue());

            // 返回响应的图片资源
            return new ResponseEntity<>(coverBytes, headers, HttpStatus.OK);
        }).exceptionally(e -> {
            // 处理异常，可根据具体情况进行日志记录和错误响应
            Thread.currentThread().interrupt();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        });
    }
}