package com.videourl.services;

import com.videourl.utils.UtilFfmpeg;

import com.videourl.utils.VideoInfo;
import org.springframework.stereotype.Service;
import org.springframework.cache.annotation.Cacheable;

import java.io.IOException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

// 视频相关的业务逻辑，如通过FFmpeg获取视频封面和信息等。
@Service
public class VideoService {


    /**
     * 根据视频url获取视频封面，并保存视频信息到db
     *
     * @param videoUrl 视频链接
     * @return 视频信息
     */
    @Cacheable(value = "videoInfoCache", key = "#videoUrl")
    public VideoInfo getVideoInfo(String videoUrl) {
        try {
            videoUrl = decodeUrlIfEncoded(videoUrl);
            return UtilFfmpeg.getVideoInfo(videoUrl);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    // 根据视频url获取视频封面，并保存视频信息到db、视频封面到r2
    public byte[] getVideoCover(String videoUrl) {
        try {
            videoUrl = decodeUrlIfEncoded(videoUrl);
            return UtilFfmpeg.getVideoCover(videoUrl);
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 判断URL是不是使用了URI编码，如果是则解码后返回，如果不是则直接返回
     *
     * @param url 待判断和解码的URL
     * @return 处理后的URL
     */
    private static String decodeUrlIfEncoded(String url) {
        // 先尝试对 URL 进行解码
        String decodedUrl = URLDecoder.decode(url, StandardCharsets.UTF_8);
        // 再对解码后的 URL 进行编码
        String reEncodedUrl = URLEncoder.encode(decodedUrl, StandardCharsets.UTF_8);
        // 如果重新编码后的 URL 与原始 URL 相同，说明原始 URL 已经是编码过的
        if (reEncodedUrl.replaceAll("\\+", "%20").equals(url.replaceAll("\\+", "%20"))) {
            return decodedUrl;
        }
        // 如果不是编码过的 URL 或者解码过程出现异常，直接返回原始 URL
        return url;
    }
}