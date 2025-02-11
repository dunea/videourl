package com.videourl.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.Objects;

import com.videourl.model.ProviderType;
import com.videourl.model.StatusType;

// 视频实体类（Video.java），包括视频的信息结构，映射到 MySQL 数据库。
@Entity  // 表示这是一个 JPA 实体类
@Table(name = "video")  // 映射到数据库中的 'videos' 表
public class Video {

    @Id  // 主键
    @GeneratedValue(strategy = GenerationType.IDENTITY)  // 自增主键策略
    private Long id;

    @Column(name = "video_url", nullable = false, length = 255)
    private String videoUrl; // 原视频地址

    @Enumerated(EnumType.STRING)  // 使用枚举类型映射
    @Column(name = "status", nullable = false)
    private StatusType status; // 状态

    @Enumerated(EnumType.STRING)  // 使用枚举类型映射
    @Column(name = "provider", nullable = false)
    private ProviderType provider; // 储存桶位置

    @Column(name = "key_column", nullable = false, length = 255)
    private String key; // 储存通里的key

    @Column(name = "format", nullable = false, length = 20)
    private String format;  // 视频格式（例如：mp4, mkv）

    @Column(name = "width", nullable = false)
    private int width;      // 视频宽度

    @Column(name = "height", nullable = false)
    private int height;     // 视频高度

    @Column(name = "duration", nullable = false)
    private long duration;  // 视频时长（单位：秒）

    @Column(name = "size", nullable = false)
    private long size;      // 视频文件大小（单位：字节）

    @Column(name = "created_at")  // 映射到数据库的 'created_at' 字段
    private LocalDateTime createdAt;  // 创建时间

    @Column(name = "last_used_at")  // 最后使用时间字段
    private LocalDateTime lastUsedAt;

    // 默认构造函数
    public Video() {
    }

    // 带参构造函数
    public Video(String videoUrl, StatusType status, ProviderType provider, String key, String format, int width, int height, long duration, long size, LocalDateTime createdAt, LocalDateTime lastUsedAt) {
        this.videoUrl = videoUrl;
        this.status = status;
        this.provider = provider;
        this.key = key;
        this.format = format;
        this.width = width;
        this.height = height;
        this.duration = duration;
        this.size = size;
        this.createdAt = createdAt;
        this.lastUsedAt = lastUsedAt;
    }

    // Getter 和 Setter 方法
    public Long getId() {
        return id;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setStatus(StatusType status) {
        this.status = status;
    }

    public StatusType getStatus() {
        return status;
    }

    public void setProvider(ProviderType provider) {
        this.provider = provider;
    }

    public ProviderType getProvider() {
        return provider;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getKey() {
        return key;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getLastUsedAt() {
        return lastUsedAt;
    }

    public void setLastUsedAt(LocalDateTime lastUsedAt) {
        this.lastUsedAt = lastUsedAt;
    }

    // 重写 toString 方法用于打印
    @Override
    public String toString() {
        return String.format("Video{id=%d, videoUrl='%s', status=%d, provider=%d, key='%s', format='%s', width=%d, height=%d, duration=%d, size=%d, createdAt=%s, lastUsedAt=%s}", id, videoUrl, status.getValue(), provider.getValue(), key, format, width, height, duration, size, Objects.toString(createdAt, "null"),  // 处理可能为 null 的字段
                Objects.toString(lastUsedAt, "null")  // 处理可能为 null 的字段
        );
    }
}


