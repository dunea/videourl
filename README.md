# Videourl

* 一款在线视频通过 url 快速获取封面的工具。
* 接口全部已解决跨域，可以在任何网站、APP里使用。

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