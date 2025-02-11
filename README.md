# Videourl

* 一款在线视频通过 url 快速获取封面的工具。
* 接口全部已解决跨域，可以在任何网站、APP里使用。
* 接口运行成本极低，支持无限制免费使用，同时可以使用Docker自行部署享受更快的速度。

#### 支持功能

- 视频url获取封面
- 视频url获取视频信息
    - 宽度/高度
    - 编码信息
    - 视频时长
    - 视频大小

#### 性能优化

- 信息使用内存进行缓存了键值
- 图片使用Cloudflare CDN进行缓存
- 同一个url使用了并发锁

### 视频链接获取封面

```base
https://videourl.meapis.com/cover?url=你的url

url参数尽量进行URI编码避免出现问题

响应: 封面.jpeg
```

### 视频链接获取视频信息

```base
https://videourl.meapis.com/info?url=你的url

url参数尽量进行URI编码避免出现问题

响应: 

{
  "format": "mov,mp4,m4a,3gp,3g2,mj2",
  "width": "720",
  "height": "1280",
  "duration": "115.4",
  "size": "26337720"
}
```

### 自行部署

```aiignore
使用docker进行部署：

docker pull ghcr.io/dunea/videourl:main

环境变量：

DATABASE_URL = jdbc:mysql://localhost:3306/videourl?useSSL=false&serverTimezone=UTC&useLegacyDatetimeCode=false
DATABASE_USERNAME = root
DATABASE_PASSWORD = password
```