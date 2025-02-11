package com.videourl.repository;

import com.videourl.model.StatusType;
import com.videourl.model.Video;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

// 标记为 @Repository 类，让 Spring 扫描到并生成实例
@Repository
public interface VideoRepository extends JpaRepository<Video, Long> {

    // 基本的 CRUD 操作已经由 JpaRepository 提供，不需要手动实现

    // 自定义查询方法（示例：通过视频 URL 查找视频）
    Optional<Video> findByVideoUrl(String videoUrl);

    // 自定义查询方法（示例：通过状态查找视频）
    List<Video> findByStatus(StatusType status);

    // 自定义查询方法（示例：根据创建时间查询）
    List<Video> findByCreatedAtAfter(LocalDateTime dateTime);

    // 你可以根据需求定义更多自定义查询方法
}
