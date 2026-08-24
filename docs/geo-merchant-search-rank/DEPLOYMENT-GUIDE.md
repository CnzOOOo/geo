# GEO 部署指南

## 1. 目标架构

```text
GitHub Pages
  管理后台前端
  https://user.github.io/JeecgBoot/
  https://user.github.io/JeecgBoot/docs/
        |
        | HTTPS API
        v
Caddy / Nginx
  https://geo-api.example.com/jeecg-boot
        |
        v
Docker
  backend -> MySQL -> Redis
```

前端静态资源由 GitHub Pages 托管，服务器只运行后端、数据库、Redis 和 HTTPS 反向代理。

## 2. 仓库设置

### 2.1 GitHub Pages

仓库 `Settings -> Pages -> Build and deployment -> Source` 选择 `GitHub Actions`。

### 2.2 Repository variables

在 `Settings -> Secrets and variables -> Actions -> Variables` 中配置：

| 变量 | 示例 | 说明 |
|---|---|---|
| `GEO_API_URL` | `https://geo-api.example.com/jeecg-boot` | 后端完整 API 地址，必须 HTTPS |
| `GEO_PUBLIC_PATH` | `/` 或 `/JeecgBoot/` | 自定义域名用 `/`，项目 Pages 用仓库路径 |

如果不配置 `GEO_PUBLIC_PATH`，工作流会默认使用 `/仓库名/`。

### 2.3 Repository secrets

后端 Docker 镜像默认推送到 GHCR，不需要额外 Secret。

如果使用私有镜像仓库，可自行增加：

| Secret | 说明 |
|---|---|
| `REGISTRY_USERNAME` | 镜像仓库用户名 |
| `REGISTRY_PASSWORD` | 镜像仓库 Token |

## 3. 前端和文档发布

工作流文件：

```text
.github/workflows/deploy-github-pages.yml
```

触发方式：

- 推送 `main` 或 `master` 分支
- 手动执行 `workflow_dispatch`

工作流会执行：

```text
pnpm install
pnpm build
node scripts/build-geo-docs.mjs
upload Pages artifact
deploy Pages
```

发布后页面地址：

```text
后台前端：https://user.github.io/JeecgBoot/
使用文档：https://user.github.io/JeecgBoot/docs/
```

如果使用自定义域名：

```text
后台前端：https://admin.example.com/
使用文档：https://admin.example.com/docs/
```

## 4. 后端 Docker 镜像

工作流文件：

```text
.github/workflows/docker-backend.yml
```

触发方式：

- 推送 `v*` tag，例如 `v1.0.0`
- 手动执行 `workflow_dispatch`

工作流会：

```text
Maven 打包 jar
使用 jeecg-system-start/Dockerfile 构建镜像
推送到 ghcr.io/<owner>/jeecg-geo-backend
```

镜像地址：

```text
ghcr.io/<owner>/jeecg-geo-backend:latest
ghcr.io/<owner>/jeecg-geo-backend:v1.0.0
```

## 5. 服务器部署

### 5.1 准备文件

把仓库中 `deploy/geo` 目录放到服务器，例如：

```text
/opt/geo/
  .env
  docker-compose.prod.yml
  Caddyfile
  init-sql/
  config/
```

### 5.2 配置 .env

```bash
cd /opt/geo
cp .env.example .env
vi .env
```

至少修改：

```dotenv
GEO_API_URL=https://geo-api.example.com/jeecg-boot
GEO_API_DOMAIN=geo-api.example.com
BACKEND_PORT=28080
BACKEND_IMAGE=jeecg-geo-backend:local
MYSQL_ROOT_PASSWORD=...
MYSQL_PASSWORD=...
REDIS_PASSWORD=...
GEO_AI_APIKEY=...
GEO_SITE_BASEURL=https://merchant.example.com
```

### 5.3 启动

```bash
docker compose -f docker-compose.prod.yml up -d
docker compose -f docker-compose.prod.yml ps
docker compose -f docker-compose.prod.yml logs -f backend
```

首次启动会由 Flyway 自动执行 GEO 模块建表脚本。

### 5.4 首次初始化数据库（必做）

生产环境 Flyway 默认关闭，新服务器第一次部署前必须手动导入完整基础库和 GEO 表。

基础库文件：

```text
jeecg-boot/db/jeecgboot-mysql-5.7.sql
```

GEO 表脚本：

```text
jeecg-boot/jeecg-module-system/jeecg-system-start/src/main/resources/flyway/sql/mysql/V3.9.3_0__geo_module.sql
jeecg-boot/jeecg-module-system/jeecg-system-start/src/main/resources/flyway/sql/mysql/V3.9.3_1__geo_wechatsync_tool_menu.sql
```

服务器初始化命令示例：

```bash
# 把基础库中的 jeecg-boot 数据库名替换为 geo
sed 's/`jeecg-boot`/`geo`/g' jeecgboot-mysql-5.7.sql > /tmp/geo-base.sql

docker exec -i geo-mysql-1 mysql -uroot -p"$MYSQL_ROOT_PASSWORD" < /tmp/geo-base.sql
docker exec -i geo-mysql-1 mysql -uroot -p"$MYSQL_ROOT_PASSWORD" geo < V3.9.3_0__geo_module.sql
docker exec -i geo-mysql-1 mysql -uroot -p"$MYSQL_ROOT_PASSWORD" geo < V3.9.3_1__geo_wechatsync_tool_menu.sql
```

导入后验证：

```sql
SELECT COUNT(*) FROM sys_permission;
SELECT COUNT(*) FROM airag_flow;
SHOW TABLES LIKE 'geo_%';
```

### 5.5 验证

```bash
curl https://geo-api.example.com/jeecg-boot/actuator/health
```

### 5.6 自动部署后端到服务器（CI/CD）

GitHub Actions 只负责构建和发布，后端容器仍运行在你的服务器上。

工作流文件：

```text
.github/workflows/deploy-backend.yml
```

触发方式：

- 推送 `v*` tag
- 手动执行 `workflow_dispatch`

工作流会：

```text
Maven 打包 jar
复制 deploy/geo 到服务器
在服务器生成 .env
docker build 后端镜像
docker compose up -d
```

服务器需要提前安装：

- Docker Engine
- Docker Compose 插件
- SSH 公钥已加入服务器 `authorized_keys`
- 当前用户有 `docker` 权限

需要配置的 Repository secrets：

| Secret | 说明 |
|---|---|
| `SERVER_HOST` | 服务器 IP 或域名 |
| `SERVER_USER` | SSH 用户 |
| `SERVER_SSH_KEY` | SSH 私钥 |
| `SERVER_PORT` | SSH 端口，默认 `22` |
| `MYSQL_ROOT_PASSWORD` | MySQL root 密码 |
| `MYSQL_PASSWORD` | 业务库密码 |
| `REDIS_PASSWORD` | Redis 密码 |
| `GEO_AI_APIKEY` | 可选，AI Key |
| `WECHATSYNC_TOKEN` | 可选，建议留空 |

需要配置的 Repository variables：

| Variable | 说明 |
|---|---|
| `SERVER_PATH` | 服务器部署目录，例如 `/opt/geo` |
| `GEO_API_URL` | 后端完整 API 地址 |
| `GEO_API_DOMAIN` | Caddy 域名 |
| `GEO_ENABLE_PROXY` | 设为 `true` 后自动启动 Caddy HTTPS |
| `BACKEND_PORT` | 后端宿主机端口，默认 `18080` |
| `MYSQL_DATABASE` | 默认 `geo` |
| `MYSQL_USER` | 默认 `geo` |
| `GEO_AI_BASEURL` | 默认 `https://api.deepseek.com` |
| `GEO_AI_MODEL` | 默认 `deepseek-chat` |
| `GEO_SITE_BASEURL` | 商家官网地址 |
| `GEO_SITE_SITENAME` | 商家名 |
| `SYNC_WS_PORT` | 默认 `9527` |

注意：后端不能直接“运行在 GitHub Actions 上”。GitHub Actions 只能完成构建和触发服务器部署，真正常驻服务必须跑在服务器、云主机或 PaaS。

## 6. Caddy HTTPS

`Caddyfile` 示例：

```caddy
{$GEO_API_DOMAIN} {
	encode gzip
	reverse_proxy backend:8080
}
```

Caddy 会自动申请和续期 Let's Encrypt 证书。

如果使用已有 Nginx，可以去掉 Compose 中的 `caddy` 服务，并在 Nginx 中反向代理：

```nginx
location /jeecg-boot/ {
    proxy_pass http://127.0.0.1:8080;
    proxy_set_header Host $host;
    proxy_set_header X-Real-IP $remote_addr;
    proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
}
```

## 7. 发布链路说明

Wechatsync 依赖 Chrome 扩展和浏览器登录态，不建议放入服务器容器。

推荐：

```text
服务器：GEO 后端、知识库、SEO、任务、数据库
本地运营机：Chrome + Wechatsync 扩展 + CLI，执行草稿发布
```

如果必须服务器自动发布，后续应改为各平台官方 API 或独立浏览器 Worker。

## 8. 常见问题

### 8.0 资源建议

GEO 三个容器建议预留约 2GB 内存：

- 后端：1GB 上限，JVM `-Xmx512m`
- MySQL：768MB 上限
- Redis：128MB 上限

3.5GB 的服务器可以运行，但会比较紧张。如果同一台服务器还运行 WordPress、其他 MySQL、Next.js 等容器，建议升级到 4GB 以上，或把 MySQL/Redis 改为独立实例。

### 8.1 前端能打开，但接口 404

检查：

- `GEO_API_URL` 是否包含 `/jeecg-boot`
- 后端容器是否正常
- Caddy/Nginx 域名是否正确
- 浏览器控制台是否报混合内容

### 8.2 GitHub Pages 刷新页面 404

构建脚本会自动生成 `404.html`，作为 history 路由回退。

如果仍 404，确认 `VITE_PUBLIC_PATH` 与 Pages 路径一致。

### 8.3 数据库初始化失败

查看：

```bash
docker compose -f docker-compose.prod.yml logs mysql
docker compose -f docker-compose.prod.yml logs backend
```

确认 `MYSQL_DATABASE`、用户名、密码与后端环境变量一致。

### 8.4 服务器无法访问平台草稿

这是预期行为。Wechatsync 草稿发布需要本地浏览器登录态，服务器只保存任务状态，不直接执行浏览器发布。

## 9. 安全建议

- `.env` 不要提交到 Git
- MySQL、Redis 密码使用强随机值
- 后端端口只绑定 `127.0.0.1`，不要直接暴露公网
- 建议定期备份 `mysql_data`、`redis_data`
- 生产环境限制 CORS 白名单，不要长期使用 `*`
