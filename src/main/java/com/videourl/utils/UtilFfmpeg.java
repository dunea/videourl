package com.videourl.utils;

import net.bramp.ffmpeg.FFprobe;
import net.bramp.ffmpeg.probe.FFmpegProbeResult;
import net.bramp.ffmpeg.probe.FFmpegStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class UtilFfmpeg {
    private static final Logger logger = LoggerFactory.getLogger(UtilFfmpeg.class);
    private static final int THUMBNAIL_TIME = 5; // 提取第5秒的画面作为封面

    // 使用 FFprobe 获取视频信息
    public static VideoInfo getVideoInfo(String videoUrl) throws IOException {
        // 初始化 FFprobe 实例
        FFprobe ffprobe = new FFprobe(); // 如果 ffprobe 已经在系统路径中可以直接使用 FFprobe ffprobe = new FFprobe();

        // 获取视频信息
        FFmpegProbeResult probeResult = ffprobe.probe(videoUrl);

        // 获取视频流信息（第一个视频流）
        FFmpegStream stream = probeResult.getStreams().get(0);

        // 提取视频信息
        String format_name = probeResult.format.format_name;
        String width = String.valueOf(stream.width);
        String height = String.valueOf(stream.height);
        String duration = String.valueOf(stream.duration);
        String size = String.valueOf(probeResult.format.size);

        // 创建 VideoInfo 对象并返回
        return new VideoInfo(format_name, width, height, duration, size);
    }


    // 提取远程视频封面并返回为字节数组
    public static byte[] getVideoCover(String videoUrl) throws IOException, InterruptedException {
        // 构建 FFmpeg 命令
        String[] cmd = {"ffmpeg",                                       // FFmpeg 可执行文件
                "-ss", "00:00:01",                              // 定位到第 1 秒
                "-i", videoUrl,                                 // 输入视频 URL
                "-vframes", "1",                                // 提取 1 帧
                "-f", "image2pipe",                            // 输出格式为管道流
                "-c:v", "mjpeg",                                // 编码为 JPEG
                "-"                                             // 输出到 stdout
        };

        // 启动进程
        Process process = new ProcessBuilder(cmd).start();

        // 捕获 stdout 流
        try (InputStream stdout = process.getInputStream(); ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {

            byte[] buffer = new byte[4096];
            int bytesRead;

            // 读取 FFmpeg 输出流到字节数组
            while ((bytesRead = stdout.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }

            // 等待进程结束
            process.waitFor();
            return outputStream.toByteArray();
        } finally {
            process.destroy();
        }
    }


    // 测试下
    public static void main(String[] args) {
        String videoUrl = "https://r2.mes-y.com/user_media/3c8d2c846235404b9cefb622c7d9fd49.mp4";

        try {
            VideoInfo videoInfo = getVideoInfo(videoUrl);
            System.out.println("视频信息：");
            System.out.println("格式: " + videoInfo.getFormat());
            System.out.println("宽度: " + videoInfo.getWidth());
            System.out.println("高度: " + videoInfo.getHeight());
            System.out.println("时长: " + videoInfo.getDuration());
            System.out.println("文件大小: " + videoInfo.getSize());
        } catch (IOException e) {
            System.out.println("获取视频信息发生错误");
        }

        try {
            byte[] videoCover = getVideoCover(videoUrl);
            System.out.println("视频封面大小：" + videoCover.length);
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
