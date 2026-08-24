# GEO Wechatsync 草稿链路测试报告

## 测试时间

2026-08-20

## 测试结论

整条链路已打通：文章文件 -> Wechatsync CLI -> Chrome 扩展 -> 平台草稿箱。

知乎、B站、掘金、CSDN、百家号、雪球、简书可以成功创建草稿；搜狐号返回“用户不通过”，公众号登录态超时；网易号、头条号等暂缓测试。

## 测试环境

| 项目 | 状态 |
|---|---|
| JeecgBoot 后端 | `http://localhost:8080/jeecg-boot`，已启动 |
| GEO 前端 | `http://localhost:3100`，已启动 |
| Wechatsync CLI | `1.0.0` |
| Chrome 扩展 | 文章同步助手 `2.0.9` |
| 同步桥接 | 已开启并连接 |
| WECHATSYNC_TOKEN | 已从扩展存储读取，并配置到后端环境变量 |
| WebSocket | `9527` |

## 平台登录状态

实时检查结果：

- 已登录：知乎 `hlohlo`、B站 `27C-L`、掘金、百家号、CSDN、搜狐号、雪球、微信公众号、简书、X `KatiChen121584`
- 未登录/暂缓：网易号、头条号、微博、豆瓣、语雀、小红书等

注意：微信公众号登录状态检查通过，但创建草稿时仍然返回“登录态超时”，需要在浏览器重新登录公众号后台后复测。

## 草稿同步结果

| 平台 | 结果 | 草稿链接或失败原因 |
|---|---|---|
| 知乎 | 成功 | `https://zhuanlan.zhihu.com/p/2073795170336765542/edit` |
| B站 | 成功 | `https://member.bilibili.com/platform/upload/text/edit?aid=383172` |
| 掘金 | 成功 | `https://juejin.cn/editor/drafts/7675652432984571919` |
| CSDN | 成功 | `https://editor.csdn.net/md?articleId=163922086` |
| 百家号 | 成功 | `https://baijiahao.baidu.com/builder/rc/edit?type=news&article_id=1874030997338503302` |
| 雪球 | 成功 | `https://mp.xueqiu.com/write/draft/29817013` |
| 简书 | 成功 | `https://www.jianshu.com/writer#/notebooks/44799044/notes/142466677` |
| 微信公众号 | 失败 | 登录态超时，请重新登录 |
| 搜狐号 | 失败 | 用户不通过 |
| 网易号 | 未测试 | 未登录，暂缓 |
| 头条号 | 未测试 | 未登录，暂缓 |

所有测试均只进入草稿箱，没有正式发布。

## GEO 端到端测试

通过 GEO API 临时创建渠道和文章，再调用 `/geo/publishTask/createAndExecute`：

- 渠道：`zhihu`，配置 `{"wechatsyncEnabled": true}`
- 文章：`GEO 商家搜索排名测试文章`
- 任务状态：`2`（成功）
- 外部链接：`https://zhuanlan.zhihu.com/p/2073805016771645520/edit`
- 临时测试记录已清理

本次同时修复了 `externalUrl` 字段：之前保存的是 CLI 整段输出，现在解析为草稿直链。

## 剩余待办

1. 在 Chrome 重新登录微信公众号后台，复测 `weixin` 草稿。
2. 检查搜狐号账号资质或后台状态，确认“用户不通过”的具体原因。
3. 后续登录网易号、头条号等平台后再补充测试。
4. 在 GEO“发布渠道”为每个平台创建渠道，platform 使用 Wechatsync 支持的 id，配置 `{"wechatsyncEnabled": true}`。
5. 渠道 `wsPort` 默认 `9527`；Token 已由后端环境变量兜底，渠道可不重复填写。

## License 说明

Wechatsync 根仓库为 GPL-3.0。本项目只通过外部 CLI 桥接调用，不把其源码打入 JeecgBoot，避免 GPL 传染。
