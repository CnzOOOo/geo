# Wechatsync 存草稿链路测试报告

## 测试时间

2026-08-19

## 测试目标

验证文章发布到多个平台的草稿箱，不正式发布，整个链路是否正常。

## 测试环境

| 项目 | 状态 |
|---|---|
| Wechatsync CLI | 已安装，版本 `1.0.0` |
| CLI 路径 | `C:\Users\CnzOO\AppData\Roaming\npm\wechatsync.cmd` |
| Chrome/Edge 扩展 | 未连接 |
| 同步桥接 | 未启用或未连接 |
| 平台登录 | 未能检查 |
| JeecgBoot 后端 | 已启动 |
| GEO 前端 | 已启动 |

## 测试结果

### 1. CLI 可执行性

命令：

```text
wechatsync --version
```

结果：通过。

输出：

```text
1.0.0
```

### 2. 文章文件解析链路

命令：

```text
wechatsync sync test-draft-article.md -p zhihu,csdn,jianshu --dry-run
```

结果：通过。

说明：

- 文件能被识别。
- 标题能正确提取。
- 支持 `zhihu`、`csdn`、`jianshu` 三个平台参数。
- 内容长度和 Markdown 格式解析正常。

### 3. 真实存草稿链路

命令：

```text
wechatsync sync test-draft-article.md -p zhihu
```

结果：失败/被阻断。

失败原因：

```text
需要安装 Chrome 扩展
WechatSync CLI 需要配合 Chrome 扩展使用
```

详细阻断点：

- CLI 启动正常。
- CLI 能读取文章和平台参数。
- CLI 无法连接 Chrome/Edge 扩展。
- 扩展未启用“同步桥接”，或 Token 未配置。
- 因此无法继续执行知乎、CSDN、简书等平台的草稿创建。

### 4. GEO 系统到 Wechatsync CLI 的桥接

结果：部分通过。

说明：

- GEO 后端已能调用外部 Wechatsync CLI。
- 后端环境检查能识别 CLI 安装状态。
- 发布任务执行时会调用 CLI 同步命令。
- 由于扩展未连接，真实草稿创建仍会被 CLI 阻断。

## 结论

当前链路状态：

```text
文章文件 -> Wechatsync CLI：通过
Wechatsync CLI -> Chrome 扩展：未连接
Chrome 扩展 -> 各平台草稿箱：未执行
```

因此，暂时不能确认各平台草稿箱是否创建成功。

## 下一步操作

1. 安装并启用 Wechatsync Chrome/Edge 扩展。
2. 在扩展设置中开启“同步桥接”。
3. 配置 Token 和端口。
4. 在浏览器中登录需要测试的平台账号。
5. 在 GEO“环境检查”页面点击“检查平台登录”。
6. 重新执行文章“发布到平台”，选择“创建后立即执行”，并确认各平台草稿箱。

## 测试文件

```text
E:\projects\codex-wk\geo_docs_research\test-draft-article.md
```
