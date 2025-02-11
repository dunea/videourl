package com.videourl;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

// 使用 @SpringBootApplication 注解，开启组件扫描，自动扫描当前包及子包中的所有组件
@SpringBootApplication
@EnableJpaRepositories(basePackages = "com.videourl.repository")
@EntityScan(basePackages = "com.videourl.model") // 这个注解是告诉 Spring Boot 启动类
@EnableCaching
public class VideourlApplication {

    public static void main(String[] args) {
        SpringApplication.run(VideourlApplication.class, args);  // 启动 Spring Boot 应用
    }
}
