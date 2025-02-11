# 基础镜像，使用包含 FFmpeg 和 Gradle 构建环境的镜像，这里选择基于 Ubuntu 的镜像
FROM ubuntu:22.04

# 设置工作目录
WORKDIR /app

# 更新系统并安装 FFmpeg、OpenJDK 和其他必要的依赖
RUN apt-get update && \
    apt-get install -y ffmpeg openjdk-17-jdk-headless git && \
    apt-get clean && \
    rm -rf /var/lib/apt/lists/*

# 复制项目文件到容器中
COPY . /app

# 赋予 gradlew 执行权限
RUN chmod +x gradlew

# 使用 Gradle 构建项目
RUN ./gradlew clean build

# 将生成的 JAR 文件复制到工作目录
RUN cp build/libs/videourl-1.0-SNAPSHOT.jar /app/videourl.jar

# 暴露 Spring 应用的端口
EXPOSE 8083

# 启动 Spring 应用
CMD ["java", "-jar", "videourl.jar"]