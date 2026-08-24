# JeecgBoot GEO 模块开发任务清单

## 目标

在 JeecgBoot 中建设一套 GEO 运营后台，覆盖：

- 商家主档和知识库。
- 问题库和文章工坊。
- 发布渠道和发布任务。
- AI 引用、舆情和效果监测。
- 实验、报表和合规审计。
- 为未来 AI Agent 知识库接入预留接口。

## 前置依赖

- JeecgBoot 后端：Spring Boot + MySQL + Redis。
- JeecgBoot 前端：Vue3 + Ant Design。
- AI Provider：OpenAI、DeepSeek、豆包等至少一个可用 API。
- 定时任务：Quartz 或 xxl-job。
- 可选检索：Elasticsearch/OpenSearch 存储监测结果和日志。
- 可选对象存储：保存文章素材、截图、证据文件。

## 总体架构

```text
Vue3 管理后台
  -> JeecgBoot 接口
  -> GEO 业务模块
      -> 知识库/文章/发布/监测/实验/审计
  -> 外部适配层
      -> AI Provider
      -> 平台发布 API/人工确认队列
      -> AI 搜索测试任务
      -> 地图/点评/内容平台监测任务
```

## Sprint 计划

### Sprint 0：基础框架

- 创建 `geo_*` 数据表。
- 创建 GEO 菜单和权限。
- 创建多租户/商家隔离。
- 创建通用状态字典：问题类型、内容类型、发布平台、任务状态、舆情状态。

### Sprint 1：知识库和文章工坊

- 商家主档 CRUD。
- 知识条目 CRUD、来源、有效期、审核状态。
- 问题库导入和查询。
- 文章草稿、版本、审核、发布状态。
- 文章 E-E-A-T 评分表和审核清单。
- AI 初稿生成接口。

### Sprint 2：发布中心

- 渠道配置和凭证加密。
- 发布任务队列、重试、状态。
- 人工确认发布流程。
- 外部 URL 回填。
- 发布日志和失败原因。

### Sprint 3：舆情和 AI 监测

- 监测任务配置。
- 查询集管理。
- AI 回答记录。
- 品牌/店铺 mention 标记。
- 引用来源解析。
- 事实准确率人工/半自动标注。
- 舆情事件和通知。

### Sprint 4：实验和报表

- 实验组/对照组配置。
- 指标快照。
- GEO 周报。
- 旧文章翻新任务。
- A/B 测试标题、结构化数据、发布时间。

### Sprint 5：合规和 AI Agent 扩展

- 审计日志。
- 数据来源台账。
- 自动化边界控制。
- llms.txt/sitemap 生成和审计。
- 公开知识库导出接口。
- AI Agent 接入预留 API。

## 数据模型

### 核心表

| 表名 | 用途 | 关键字段 |
|---|---|---|
| `geo_merchant` | 店铺/商家 | `tenant_id`, `name`, `aliases`, `category`, `province`, `city`, `district`, `address`, `lng`, `lat`, `phone`, `opening_hours`, `status` |
| `geo_question_bank` | 用户问题库 | `merchant_id`, `question_type`, `question`, `intent`, `region`, `priority`, `status` |
| `geo_knowledge_item` | 知识条目 | `merchant_id`, `category`, `fact`, `value`, `source_type`, `source_url`, `owner_id`, `verified_at`, `valid_from`, `valid_to`, `status` |
| `geo_article` | 文章 | `merchant_id`, `title`, `title_type`, `question_id`, `content_md`, `status`, `review_status`, `reviewer_id`, `published_at`, `canonical_url` |
| `geo_article_version` | 文章版本 | `article_id`, `version`, `content_md`, `changed_by`, `created_at` |
| `geo_channel` | 发布渠道 | `merchant_id`, `platform`, `config_encrypted`, `enabled`, `rate_limit`, `status` |
| `geo_publish_task` | 发布任务 | `article_id`, `channel_id`, `status`, `external_id`, `external_url`, `error_code`, `retry_count` |
| `geo_monitor_task` | 监测任务 | `merchant_id`, `query_set_json`, `engine_config`, `cadence`, `enabled` |
| `geo_mention` | AI 回答 | `task_id`, `engine`, `query`, `occurred_at`, `answer_text`, `mentioned`, `position`, `source_urls_json`, `accuracy_score` |
| `geo_sentiment_event` | 舆情事件 | `merchant_id`, `platform`, `event_type`, `title`, `content`, `sentiment`, `severity`, `status`, `owner_id` |
| `geo_experiment` | 实验 | `merchant_id`, `name`, `control_group`, `variant_group`, `status`, `started_at`, `ended_at` |
| `geo_metric_snapshot` | 指标快照 | `experiment_id`, `metric_name`, `value`, `period` |
| `geo_audit_log` | 审计日志 | `user_id`, `action`, `target_type`, `target_id`, `before_json`, `after_json`, `created_at` |

### 状态机

- 知识条目：`草稿 -> 待核验 -> 已核验 -> 已过期 -> 已停用`
- 文章：`草稿 -> 待审核 -> 已通过 -> 待发布 -> 已发布 -> 已下线`
- 发布任务：`排队中 -> 发布中 -> 成功 -> 失败 -> 需人工处理`
- 舆情事件：`待处理 -> 处理中 -> 已闭环 -> 已归档`

## API 清单

| 模块 | 接口 | 说明 |
|---|---|---|
| 商家 | `GET/POST/PUT /api/geo/merchant` | 商家主档 |
| 问题库 | `GET/POST/PUT /api/geo/question-bank` | 问题库 CRUD/导入 |
| 知识库 | `GET/POST/PUT /api/geo/knowledge` | 知识条目 CRUD/审核 |
| 文章 | `GET/POST/PUT /api/geo/article` | 文章 CRUD/审核/发布 |
| 生成 | `POST /api/geo/article/generate` | AI 初稿生成 |
| 渠道 | `GET/POST/PUT /api/geo/channel` | 渠道配置 |
| 发布 | `GET/POST /api/geo/publish-task` | 发布任务和重试 |
| 监测 | `GET/POST /api/geo/monitor-task` | 监测任务配置 |
| 回答 | `GET/POST /api/geo/mention` | AI 回答记录 |
| 舆情 | `GET/POST /api/geo/sentiment-event` | 舆情事件处理 |
| 实验 | `GET/POST /api/geo/experiment` | 实验配置 |
| 指标 | `GET /api/geo/metrics/weekly` | GEO 周报数据 |
| 导出 | `GET /api/geo/knowledge/export` | 公开知识库导出 |
| 审计 | `GET /api/geo/audit-log` | 审计查询 |

## 前端菜单

```text
GEO 运营
  ├── 商家主档
  ├── 问题库
  ├── 知识库
  ├── 文章工坊
  ├── 发布中心
  ├── AI 监测
  ├── 舆情事件
  ├── 实验中心
  ├── GEO 周报
  └── 合规审计
```

## 定时任务

| 任务 | 频率 | 动作 |
|---|---|---|
| AI 查询监测 | 每日 | 按查询集调用 AI 工具/API，记录 mention 和引用 |
| 舆情扫描 | 每小时/每日 | 扫描评论、搜索、平台通知 |
| 事实过期提醒 | 每日 | 检查知识条目有效期 |
| GEO 周报 | 每周一 | 汇总指标并通知负责人 |
| 旧文章翻新 | 每月 | 生成待翻新清单 |
| llms.txt/sitemap 审计 | 每月 | 检查配置和内容变更 |

## P0/P1/P2 开发任务

### P0：先跑通单店闭环

- 商家主档。
- 问题库。
- 知识库。
- 文章草稿/审核/发布。
- 官网、公众号、地图、点评渠道配置。
- 人工发布流程。
- 基础 AI 监测和周报。

### P1：规模化运营

- AI 初稿生成和人工审核工作流。
- 发布任务队列和失败重试。
- 一鱼多吃内容模板。
- 舆情事件和通知。
- 实验组/对照组。
- A/B 测试。
- 数据报表和看板。

### P2：AI Agent 和自动化

- 公开知识库导出。
- llms.txt/sitemap 自动生成。
- AI Agent 知识检索 API。
- 多模型引用对比。
- 自动纠错建议。
- 按商家批量复制。

## 自动化边界

| 可自动化 | 必须人工确认 |
|---|---|
| AI 初稿生成 | 文章发布 |
| 查询和 AI 回答记录 | 差评回应 |
| 知识过期提醒 | 事实变更 |
| 周报生成 | 平台账号绑定 |
| 发布失败告警 | 资质、价格、优惠修改 |
| 实验数据汇总 | 付费推广和活动 |

## 安全与合规

- 渠道凭证加密存储，不写入日志。
- 按角色分配权限，商家数据租户隔离。
- 所有事实、文章、发布、监测结果记录审计日志。
- 文章发布前必须经过人工审核。
- AI 生成内容不能直接发布，必须有来源核验和人工确认。
- 不采集用户敏感信息，不伪造评论、评价、销量和媒体报道。
- 结构化数据必须与页面正文一致。
