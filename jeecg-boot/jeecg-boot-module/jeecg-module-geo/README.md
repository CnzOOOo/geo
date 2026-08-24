# jeecg-module-geo

GEO 商家搜索排名运营模块，基于 JeecgBoot 3.9.3。

## 已实现

- 商家主档：`geo_merchant`
- 问题库：`geo_question_bank`
- 知识库：`geo_knowledge_item`
- 文章工坊：`geo_article`
- 发布渠道：`geo_channel`
- 发布任务：`geo_publish_task`
- 监测任务：`geo_monitor_task`
- AI 回答记录：`geo_mention`
- 舆情事件：`geo_sentiment_event`
- 实验：`geo_experiment`

每张表提供标准 CRUD 接口：

```text
GET    /geo/{module}/list
POST   /geo/{module}/add
POST   /geo/{module}/edit
DELETE /geo/{module}/delete
DELETE /geo/{module}/deleteBatch
GET    /geo/{module}/queryById
```

文章额外提供：

```text
POST /geo/article/submitReview
POST /geo/article/publish
POST /geo/article/offline
POST /geo/article/generate
```

发布任务额外提供：

```text
POST /geo/publishTask/execute
```

已实现发布适配器：

- `wechat_mp`：微信公众号官方草稿/发布接口，优先级 100。
- `wechatsync` 外部 CLI 桥接：支持知乎、CSDN、简书、头条号、百家号、搜狐、网易、B站等平台，优先级 50。
- `webhook`：自有站或官网 Webhook，优先级 20。
- `douyin`、`xiaohongshu`、`zhihu`、`wangyi`、`sohu`、`baijiahao`、`toutiao`、`qiehao`、`bilibili`、`csdn`、`jianshu`、`meituan`、`dianping`、`baidu_map`、`amap`：当前为人工发布队列。
- 其他平台：通用人工兜底。

Wechatsync 渠道配置示例：

```json
{
  "wechatsyncEnabled": true,
  "cliPath": "wechatsync",
  "token": "与Chrome扩展一致的token",
  "wsPort": "9527"
}
```

License 说明：Wechatsync 根仓库为 GPL-3.0。本项目只通过外部 CLI 调用，不将其源码打入 JeecgBoot，避免 GPL 传染。

监测任务额外提供：

```text
POST /geo/monitorTask/runNow
```

SEO 产物接口：

```text
GET /geo/seo/llms-txt
GET /geo/seo/sitemap
GET /geo/seo/local-business-schema
```

## 建表

Flyway 脚本：

```text
jeecg-boot/jeecg-module-system/jeecg-system-start/src/main/resources/flyway/sql/mysql/V3.9.3_0__geo_module.sql
```

脚本会创建 `geo_*` 表，并给 admin 角色插入 GEO 运营菜单和商家主档菜单。

## 前端

商家主档页面：

```text
jeecgboot-vue3/src/views/geo/merchant/index.vue
jeecgboot-vue3/src/views/geo/questionBank/index.vue
jeecgboot-vue3/src/views/geo/knowledge/index.vue
jeecgboot-vue3/src/views/geo/article/index.vue
jeecgboot-vue3/src/views/geo/channel/index.vue
jeecgboot-vue3/src/views/geo/publishTask/index.vue
jeecgboot-vue3/src/views/geo/monitorTask/index.vue
jeecgboot-vue3/src/views/geo/mention/index.vue
jeecgboot-vue3/src/views/geo/sentimentEvent/index.vue
jeecgboot-vue3/src/views/geo/experiment/index.vue
```

发布、监测、舆情、实验页面已按同一模式补齐。

## 编译

```bash
cd jeecg-boot
mvn -pl jeecg-boot-module/jeecg-module-geo -am -DskipTests compile
```

## 当前边界

- 当前为数据层和 CRUD 骨架。
- AI 初稿生成、平台真实发布、AI 查询执行尚未接入。
- 所有发布和纠错动作仍必须人工确认。
