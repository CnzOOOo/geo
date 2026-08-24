# GEO 系统接口与凭据提供清单

## 总状态

| 模块 | 状态 | 说明 |
|---|---|---|
| 后端基础 CRUD | 已实现 | 10 张表 CRUD 和文章状态流转 |
| 前端页面 | 已实现 | 10 个模块页面已落地 |
| MySQL / Redis | 运行中 | 本地 Docker 容器 |
| 后端服务 | 运行中 | `8080/jeecg-boot` |
| 前端服务 | 运行中 | `3100` |
| AI 初稿生成 | 代码已实现，待接真实 API | `/geo/article/generate`，需用户提供 Key |
| 平台发布 | 适配器骨架已实现，待接真实账号 | `/geo/publishTask/execute`，已有人工兜底和 Webhook |
| AI 搜索监测 | 执行骨架已实现，待接真实引擎 | `/geo/monitorTask/runNow`，无 Key 时落人工复核记录 |
| SEO 产物生成 | 已实现 | llms.txt、Sitemap、JSON-LD 可由商家数据生成 |

## 需要你提供的接口和凭据

| 编号 | 项目 | 用途 | 状态 |
|---|---|---|---|
| 1 | DeepSeek 或 OpenAI 兼容 API：`base_url`、`api_key`、`model` | AI 初稿生成、内容润色 | 待提供 |
| 2 | 豆包/火山方舟 API：`endpoint`、`api_key`、`model` | 豆包场景内容生成与测试 | 待提供 |
| 3 | 微信公众号 AppID / AppSecret | 公众号文章发布 | 待提供 |
| 4 | 知乎账号/开放平台凭据 | 知乎问答发布 | 待提供 |
| 5 | 小红书专业号/开放平台凭据 | 小红书图文发布 | 待提供 |
| 6 | 抖音/巨量开放平台凭据 | 短视频脚本或内容发布 | 待提供 |
| 7 | 网易号后台账号 | 网易号文章发布 | 待提供 |
| 8 | 搜狐号后台账号 | 搜狐号文章发布 | 待提供 |
| 9 | 百家号后台账号 | 百家号文章发布 | 待提供 |
| 10 | 头条号后台账号 | 头条号文章发布 | 待提供 |
| 11 | 知乎后台账号 | 知乎问答/文章发布 | 待提供 |
| 12 | 企鹅号后台账号 | 企鹅号文章发布 | 待提供 |
| 13 | B站后台账号 | B站动态/专栏发布 | 待提供 |
| 14 | CSDN后台账号 | CSDN文章发布 | 待提供 |
| 15 | 简书后台账号 | 简书文章发布 | 待提供 |
| 16 | 美团/点评商家后台账号 | 门店信息、商品、评价维护 | 待提供 |
| 17 | 百度地图开放平台 Key | POI 信息、地点数据 | 待提供 |
| 18 | 高德开放平台 Key | POI 信息、地点数据 | 待提供 |
| 19 | 官网域名、服务器 SSH/FTP、建站后台 | llms.txt、Sitemap、JSON-LD 部署 | 待提供 |
| 20 | 店铺真实资料 | 商家主档、知识库、文章素材 | 待提供 |
| 21 | AI 搜索测试账号/浏览器环境 | DeepSeek、豆包、ChatGPT 等人工监测 | 待提供 |

## 状态说明

- `待提供`：代码和流程已预留，但需要你的账号、Key、域名或资料才能真实调用。
- `已实现`：不依赖外部凭据的代码已经完成。
- `运行中`：当前本地环境已经启动。
- `需配置`：代码已实现，但还需要在配置文件中填入真实值。

## 当前建议配置

后端 `application-dev.yml` 建议新增配置：

```yaml
geo:
  ai:
    provider: openai-compatible
    base-url: https://api.deepseek.com/v1
    api-key: ${GEO_AI_API_KEY}
    model: deepseek-chat
    timeout-seconds: 60
```

公众号渠道 `geo_channel.config_encrypted` 建议配置：

```json
{
  "appId": "你的AppID",
  "appSecret": "你的AppSecret",
  "thumbMediaId": "公众号素材库封面的media_id",
  "author": "店铺名",
  "digest": "可选摘要",
  "needOpenComment": 1,
  "onlyFansCanComment": 0
}
```

发布适配器优先级：

```text
wechat_mp 官方草稿/发布接口 -> 100
webhook 自有站/官网 -> 20
douyin/xiaohongshu/zhihu/wangyi/sohu/baijiahao/toutiao/qiehao/bilibili/csdn/jianshu/meituan/dianping/baidu_map/amap -> 人工队列
其他未知平台 -> 通用人工兜底
```

发布平台建议先只接“官网/自有站”和“人工确认队列”，公众号、知乎、小红书、抖音等平台确认官方 API 可用后再逐个接入。

## Wechatsync 外部 CLI 桥接

Wechatsync 根仓库 License 是 GPL-3.0，包内 CLI/MCP 标注 MIT，但根 LICENSE 仍覆盖仓库。为避免 GPL 传染到 JeecgBoot，本项目不复制其源码，只通过外部 CLI 桥接调用。

渠道配置示例：

```json
{
  "wechatsyncEnabled": true,
  "cliPath": "wechatsync",
  "token": "与Chrome扩展一致的token",
  "wsPort": "9527"
}
```

调用方式：

```bash
wechatsync sync /tmp/article.md -p zhihu,csdn,jianshu
```

后续 Wechatsync 更新时，只需单独升级 `@wechatsync/cli` 或 Chrome 扩展，不需要改动 GEO 后端。

## 下一步验证

1. 用 `admin/123456` 登录前端。
2. 创建一家测试商家。
3. 录入 10 条问题、20 条知识、3 篇文章。
4. 测试文章提交审核、发布、下线。
5. 测试 llms.txt、Sitemap、JSON-LD 输出。
6. 提供 AI Key 后测试文章初稿生成。
7. 提供平台凭据后测试发布任务。
