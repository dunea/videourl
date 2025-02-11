package com.videourl.controller;

import com.videourl.model.Video;
import com.videourl.service.VideoService;
import com.videourl.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;


// 这里放视频接口相关的控制器，用于处理请求。
// 使用 @RestController 标注该类为 Spring 管理的控制器
@RestController
public class VideoController {

    private final VideoService videoService;
    private final VideoInfoCache videoInfoCache;
    private final VideoResourceCache videoResourceCache;

    @Autowired
    public VideoController(VideoService videoService) {
        this.videoService = videoService;
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
            CompletableFuture<byte[]> future = videoResourceCache.getVideoCover(videoUrl);
            byte[] coverBytes = future.get();

            if (coverBytes == null || coverBytes.length == 0) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.IMAGE_JPEG); // 根据实际图片类型修改，这里假设是 JPEG
            headers.setContentLength(coverBytes.length);

            // 设置缓存控制头，让浏览器缓存响应 7 天
            CacheControl cacheControl = CacheControl.maxAge(7, TimeUnit.DAYS);
            headers.setCacheControl(cacheControl.getHeaderValue());

            return new ResponseEntity<>(coverBytes, headers, HttpStatus.OK);
        } catch (InterruptedException | ExecutionException e) {
            // 处理异常，可根据具体情况进行日志记录和错误响应
            Thread.currentThread().interrupt();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    // 通过视频 URL 查找视频
    @GetMapping("/video/{videoUrl}")
    public Optional<Video> getVideoByUrl(@PathVariable String videoUrl) {
        return videoService.getVideoByUrl(videoUrl);
    }

    // 通过状态查找视频
    @GetMapping("/videos/status/{status}")
    public List<Video> getVideosByStatus(@PathVariable int status) {
        return videoService.getVideosByStatus(status);
    }

    // 通过创建时间查找视频
    @GetMapping("/videos/after/{dateTime}")
    public List<Video> getVideosAfterDate(@PathVariable String dateTime) {
        // 这里需要将字符串转换为 LocalDateTime 类型
        LocalDateTime dateTimeParsed = LocalDateTime.parse(dateTime);
        return videoService.getVideosAfterDate(dateTimeParsed);
    }
}