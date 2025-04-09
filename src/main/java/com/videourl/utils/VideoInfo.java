package com.videourl.utils;

import java.util.Objects;

public class VideoInfo {
    private String format;
    private String width;
    private String height;
    private String duration;
    private String size;

    // 构造函数
    public VideoInfo(String format, String width, String height, String duration, String size) {
        this.format = format;
        this.width = width;
        this.height = height;
        this.duration = duration;
        this.size = size;
    }

    // Getter 和 Setter 方法
    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public String getWidth() {
        return width;
    }

    public void setWidth(String width) {
        this.width = width;
    }

    public String getHeight() {
        return height;
    }

    public void setHeight(String height) {
        this.height = height;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }


    @Override
    public String toString() {
        StringBuilder info = new StringBuilder("VideoInfo{");
        info.append("\n    format='").append(Objects.toString(format, "null")).append("',");
        info.append("\n    width='").append(Objects.toString(width, "null")).append("',");
        info.append("\n    height='").append(Objects.toString(height, "null")).append("',");
        info.append("\n    duration='").append(Objects.toString(duration, "null")).append("',");
        info.append("\n    size='").append(Objects.toString(size, "null")).append("'");
        info.append("\n}");
        return info.toString();
    }
}
