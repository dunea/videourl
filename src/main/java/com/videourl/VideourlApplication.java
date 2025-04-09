package com.videourl;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

// 使用 @SpringBootApplication 注解，开启组件扫描，自动扫描当前包及子包中的所有组件
@SpringBootApplication
@EnableCaching
public class VideourlApplication {

    public static void main(String[] args) {
        SpringApplication.run(VideourlApplication.class, args);  // 启动 Spring Boot 应用
    }
}
