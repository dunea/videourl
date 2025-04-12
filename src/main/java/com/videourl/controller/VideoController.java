package com.videourl.controller;

import com.videourl.cache.VideoInfoCache;
import com.videourl.cache.VideoResourceCache;
import com.videourl.services.VideoService;
import com.videourl.utils.ffmpeg.VideoInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;


// 这里放视频接口相关的控制器，用于处理请求。
// 使用 @RestController 标注该类为 Spring 管理的控制器
@RestController
public class VideoController {

    private final VideoInfoCache videoInfoCache;
    private final VideoResourceCache videoResourceCache;

    @Autowired
    public VideoController(VideoService videoService) {
        this.videoInfoCache = new VideoInfoCache(videoService);
        this.videoResourceCache = new VideoResourceCache(videoService);
    }

    // 根据视频url获取视频封面
    @CrossOrigin(origins = "*")
    @GetMapping("/info")
    public VideoInfo getVideoInfo(@RequestParam("url") String videoUrl) {
        VideoInfo videoInfo = videoInfoCache.getVideoInfo(videoUrl);

        // 设置缓存控制头，让浏览器缓存响应 7 天
        CacheControl cacheControl = CacheControl.maxAge(7, TimeUnit.DAYS);

        return ResponseEntity.ok().cacheControl(cacheControl).body(videoInfo).getBody();
    }

    // 根据视频url获取视频封面
    @CrossOrigin(origins = "*")
    @GetMapping("/cover")
    public ResponseEntity<byte[]> getVideoCover(@RequestParam("url") String videoUrl) {
        try {
            // 视频封面资源缓存管理器
            CompletableFuture<byte[]> future = videoResourceCache.getVideoCover(videoUrl);
            byte[] coverBytes = future.get();

            // 获取封面为空则返回404
            if (coverBytes == null || coverBytes.length == 0) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
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
        } catch (InterruptedException | ExecutionException e) {
            // 处理异常，可根据具体情况进行日志记录和错误响应
            Thread.currentThread().interrupt();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}