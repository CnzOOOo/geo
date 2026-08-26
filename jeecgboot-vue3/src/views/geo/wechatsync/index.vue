<template>
  <div class="p-4">
    <a-card title="Wechatsync 环境检查">
      <a-space wrap>
        <a-button type="primary" :loading="statusLoading" @click="loadStatus">重新检测</a-button>
        <template v-if="!isServerMode">
          <a-button :loading="installLoading" @click="installCli">一键安装</a-button>
          <a-button :loading="updateCheckLoading" @click="checkUpdate">检查更新</a-button>
          <a-button :loading="updateLoading" @click="updateCli">一键更新</a-button>
          <a-button type="primary" @click="openChromeWebStore">安装浏览器插件</a-button>
          <a-button @click="downloadPlugin">下载插件包</a-button>
          <a-button :loading="platformLoading" @click="checkPlatformStatus">检查平台登录</a-button>
        </template>
        <template v-else>
          <a-button type="primary" @click="openExtensionPage">打开扩展设置</a-button>
          <a-button @click="copyLocalCommand">复制本地检查命令</a-button>
          <a-button :loading="platformLoading" @click="checkPlatformStatus">查看本地工作站检查指引</a-button>
        </template>
      </a-space>

      <a-row :gutter="[12, 12]" class="mt-4">
        <a-col :span="isServerMode ? 12 : 24">
          <a-card size="small" title="服务器环境">
            <a-descriptions bordered :column="1" size="small">
              <a-descriptions-item label="部署模式">
                <a-tag :color="isServerMode ? 'blue' : 'green'">{{ isServerMode ? '生产服务器模式' : '本地工作站模式' }}</a-tag>
              </a-descriptions-item>
              <a-descriptions-item label="后端服务">
                <a-tag :color="status.serverReady ? 'green' : 'red'">{{ status.serverReady ? '正常' : '异常' }}</a-tag>
              </a-descriptions-item>
              <template v-if="!isServerMode">
                <a-descriptions-item label="CLI 已安装">
                  <a-tag :color="status.cliInstalled ? 'green' : 'red'">{{ status.cliInstalled ? '是' : '否' }}</a-tag>
                </a-descriptions-item>
                <a-descriptions-item label="CLI 路径">{{ status.cliPath || '-' }}</a-descriptions-item>
                <a-descriptions-item label="CLI 版本">{{ status.cliVersion || '-' }}</a-descriptions-item>
                <a-descriptions-item label="Token 已配置">
                  <a-tag :color="status.tokenConfigured ? 'green' : 'orange'">{{ status.tokenConfigured ? '是' : '否' }}</a-tag>
                </a-descriptions-item>
                <a-descriptions-item label="WebSocket 端口">{{ status.wsPort || '9527' }}</a-descriptions-item>
                <a-descriptions-item label="更新状态">
                  <template v-if="updateInfo.latestVersion">
                    当前 {{ updateInfo.currentVersion || '-' }} / 最新 {{ updateInfo.latestVersion }}
                    <a-tag v-if="updateInfo.updateAvailable" color="orange">可更新</a-tag>
                  </template>
                  <span v-else>未检查</span>
                </a-descriptions-item>
              </template>
              <a-descriptions-item v-else label="CLI 安装位置">
                <span>本地工作站，不在服务器安装</span>
              </a-descriptions-item>
            </a-descriptions>
            <a-alert
              v-if="isServerMode"
              type="success"
              show-icon
              class="mt-2"
              message="服务器环境正常，CLI 不需要安装"
            />
            <a-alert
              v-if="!isServerMode && issues.length"
              type="warning"
              show-icon
              class="mt-2"
              :message="`发现 ${issues.length} 个问题`"
              :description="issues.join('；')"
            />
          </a-card>
        </a-col>

        <a-col :span="isServerMode ? 12 : 24">
          <a-card size="small" title="本地发布工作站">
            <template v-if="isServerMode">
              <p>真实平台草稿由本地电脑执行，服务器只管理任务。</p>
              <ol class="mb-2">
                <li>打开 Chrome，确认“文章同步助手”扩展已启用。</li>
                <li>在扩展设置中开启“同步桥接 / CLI / MCP 连接”。</li>
                <li>复制扩展 Token，并配置到本地环境变量 <code>WECHATSYNC_TOKEN</code>。</li>
                <li>在本机执行本地检查命令，确认目标平台已登录。</li>
                <li>回到 GEO 发布任务，创建草稿任务。</li>
              </ol>
              <pre class="mb-2">{{ localCommand }}</pre>
              <a-space wrap>
                <a-button type="primary" @click="openExtensionPage">打开扩展设置</a-button>
                <a-button @click="copyLocalCommand">复制命令</a-button>
                <a-button @click="openChromeWebStore">Chrome 官方安装入口</a-button>
                <a-button @click="openInstallPage">Wechatsync 官方安装页</a-button>
              </a-space>
            </template>
            <template v-else>
              <a-alert
                :type="bridgeConnected ? 'success' : 'warning'"
                show-icon
                class="mb-3"
                :message="bridgeConnected ? 'Chrome 扩展已连接' : '需要先在扩展中开启同步桥接'"
                :description="bridgeConnected
                  ? 'CLI 与扩展之间的 WebSocket 已接通，可以继续检查平台登录并执行草稿发布测试。'
                  : '该功能在扩展弹窗的“设置”里，不在 GEO 页面中。'"
              />
              <ol class="mb-3">
                <li>点击浏览器工具栏中的“文章同步助手”扩展图标。</li>
                <li>在弹窗右上角点击“设置”（齿轮图标）。</li>
                <li>打开“同步桥接”下的“CLI / MCP 连接”开关。</li>
                <li>复制页面显示的 Token，并确认服务器地址为 <code>ws://localhost:9527</code>。</li>
                <li>回到 GEO 的“发布渠道”配置，填入该 Token；端口默认 9527 可不填。</li>
              </ol>
              <a-space wrap>
                <a-button type="primary" @click="openExtensionPage">打开扩展设置</a-button>
                <a-button :loading="platformLoading" @click="checkPlatformStatus">检查平台登录</a-button>
              </a-space>
            </template>
          </a-card>
        </a-col>
      </a-row>

      <a-card size="small" title="本地 MCP 服务" class="mt-4">
        <a-input
          v-model:value="localMcpToken"
          placeholder="扩展 Token，从扩展设置复制"
          style="max-width: 420px"
          class="mb-2"
          allowClear
        />
        <a-space wrap class="mb-2">
          <a-button :loading="localMcpChecking" @click="checkLocalMcp">检测本地服务</a-button>
          <a-button @click="copyInstallCommand">复制安装命令</a-button>
          <a-button @click="copyStartCommand">复制启动命令</a-button>
          <a-button @click="downloadMcpScript">下载一键启动脚本</a-button>
        </a-space>
        <a-alert
          :type="localMcpConnected ? 'success' : 'warning'"
          show-icon
          :message="localMcpMessage"
        />
        <pre v-if="localMcpOutput" class="mt-2 mb-0">{{ localMcpOutput }}</pre>
      </a-card>

      <a-card v-if="platformOutput" size="small" title="检查输出" class="mt-4">
        <pre class="mb-0">{{ platformOutput }}</pre>
      </a-card>
    </a-card>
  </div>
</template>

<script lang="ts" setup>
  import { ref, computed } from 'vue';
  import { useMessage } from '/@/hooks/web/useMessage';
  import {
    getWechatsyncStatus,
    installWechatsyncCli,
    checkWechatsyncUpdate,
    updateWechatsyncCli,
    getWechatsyncPluginInfo,
    getWechatsyncPlatformStatus,
  } from './wechatsync.api';
  import { checkLocalWechatsync } from './localWechatsync';

  const { createMessage } = useMessage();
  const status = ref<any>({});
  const updateInfo = ref<any>({});
  const statusLoading = ref(false);
  const installLoading = ref(false);
  const updateCheckLoading = ref(false);
  const updateLoading = ref(false);
  const platformLoading = ref(false);
  const localMcpChecking = ref(false);
  const localMcpConnected = ref(false);
  const localMcpMessage = ref('尚未检测本地 MCP 服务');
  const localMcpOutput = ref('');
  const localMcpToken = ref('');
  const pluginInfo = ref<any>({});
  const extensionPageUrl = 'chrome-extension://hchobocdmclopcbnibdnoafilagadion/src/popup/index.html';

  const issues = computed(() => status.value.issues || []);
  const platformOutput = computed(() => status.value.platformStatus?.output || '');
  const isServerMode = computed(() => status.value.deploymentMode === 'server');
  const localCommand = computed(() => 'wechatsync --version\nwechatsync platforms --auth');
  const bridgeConnected = computed(() => {
    return /Chrome Extension 已连接|已连接/.test(platformOutput.value);
  });
  const mcpInstallCommand = `git clone https://github.com/wechatsync/Wechatsync.git "%USERPROFILE%\\Wechatsync"
cd /d "%USERPROFILE%\\Wechatsync"
corepack pnpm install
corepack pnpm --filter @wechatsync/mcp-server build`;
  const mcpStartCommand = computed(() => {
    const token = localMcpToken.value || '扩展Token';
    return `$env:WECHATSYNC_TOKEN="${token}"
$env:SYNC_WS_PORT="9527"
$env:SYNC_HTTP_PORT="9529"
node "$env:USERPROFILE\\Wechatsync\\packages\\mcp-server\\dist\\index.js" --sse`;
  });

  async function loadStatus() {
    statusLoading.value = true;
    try {
      status.value = await getWechatsyncStatus();
    } finally {
      statusLoading.value = false;
    }
  }

  async function installCli() {
    installLoading.value = true;
    try {
      const res: any = await installWechatsyncCli();
      createMessage.success(res?.output || '安装完成');
      await loadStatus();
    } finally {
      installLoading.value = false;
    }
  }

  async function checkUpdate() {
    updateCheckLoading.value = true;
    try {
      updateInfo.value = await checkWechatsyncUpdate();
      createMessage.success('更新检查完成');
    } finally {
      updateCheckLoading.value = false;
    }
  }

  async function updateCli() {
    updateLoading.value = true;
    try {
      const res: any = await updateWechatsyncCli();
      createMessage.success(res?.output || '更新完成');
      await loadStatus();
    } finally {
      updateLoading.value = false;
    }
  }

  async function loadPluginInfo() {
    pluginInfo.value = await getWechatsyncPluginInfo();
  }

  async function checkPlatformStatus() {
    platformLoading.value = true;
    try {
      const res: any = await getWechatsyncPlatformStatus();
      status.value.platformStatus = res;
    } finally {
      platformLoading.value = false;
    }
  }

  async function checkLocalMcp() {
    localMcpChecking.value = true;
    try {
      const wsPort = status.value.wsPort || 9527;
      const res = await checkLocalWechatsync(wsPort);
      localMcpConnected.value = !!res.connected;
      localMcpMessage.value = localMcpConnected.value
        ? '本地 MCP 服务已连接，Chrome 扩展可发布'
        : '本地 MCP 服务可达，但 Chrome 扩展未连接';
      localMcpOutput.value = JSON.stringify(res, null, 2);
    } catch (e: any) {
      localMcpConnected.value = false;
      localMcpMessage.value = '本地 MCP 服务未启动，请下载一键启动脚本或复制命令';
      localMcpOutput.value = e?.message || '无法连接本地服务';
    } finally {
      localMcpChecking.value = false;
    }
  }

  function copyInstallCommand() {
    navigator.clipboard
      .writeText(mcpInstallCommand)
      .then(() => createMessage.success('MCP 安装命令已复制'))
      .catch(() => createMessage.error('复制失败，请手动复制'));
  }

  function copyStartCommand() {
    navigator.clipboard
      .writeText(mcpStartCommand)
      .then(() => createMessage.success('MCP 启动命令已复制'))
      .catch(() => createMessage.error('复制失败，请手动复制'));
  }

  function downloadMcpScript() {
    const content = `@echo off
setlocal
where node >nul 2>nul
if not errorlevel 1 goto node_ready
echo Node.js not found. Installing Node.js LTS...
where winget >nul 2>nul
if errorlevel 1 goto node_direct
winget install -e --id OpenJS.NodeJS.LTS --accept-source-agreements --accept-package-agreements
goto node_check

:node_direct
echo winget not found. Downloading Node.js LTS directly...
powershell -NoProfile -ExecutionPolicy Bypass -Command "$ErrorActionPreference='Stop'; try { $d=Invoke-RestMethod 'https://nodejs.org/dist/index.json'; $v=($d | Where-Object lts | Select-Object -First 1).version; $u='https://nodejs.org/dist/'+$v+'/node-'+$v+'-x64.msi'; $o=$env:TEMP+'\node-setup.msi'; Invoke-WebRequest $u -OutFile $o; Start-Process msiexec -ArgumentList '/i', $o, '/qn' -Wait; Remove-Item $o -Force; exit 0 } catch { Write-Host $_; exit 1 }"

:node_check
set "PATH=%PATH%;%ProgramFiles%\nodejs"
where node >nul 2>nul
if errorlevel 1 (
  echo Node.js install failed. Please install Node.js from https://nodejs.org manually.
  pause
  exit /b 1
)
:node_ready
where corepack >nul 2>nul
if errorlevel 1 (
  echo corepack not found. Installing corepack via npm...
  call npm install -g corepack
)
if not exist "%USERPROFILE%\\Wechatsync" (
  git clone https://github.com/wechatsync/Wechatsync.git "%USERPROFILE%\\Wechatsync"
)
cd /d "%USERPROFILE%\\Wechatsync"
if not exist packages\\mcp-server\\src\\ws-bridge.ts.patched (
  curl -fsSL -o "%TEMP%\\wechatsync-mcp-heartbeat.patch" https://front.rucode.cn/patches/wechatsync-mcp-heartbeat.patch
  if errorlevel 1 (
    echo Patch download failed. Please check network.
    pause
    exit /b 1
  )
  git apply "%TEMP%\\wechatsync-mcp-heartbeat.patch"
  if errorlevel 1 (
    echo Patch apply failed. The source may already include the heartbeat fix.
    pause
    exit /b 1
  )
  echo patched> packages\\mcp-server\\src\\ws-bridge.ts.patched
)
if not exist node_modules (
  corepack pnpm install
)
if not exist packages\\mcp-server\\dist\\index.js (
  corepack pnpm --filter @wechatsync/mcp-server build
)
set WECHATSYNC_TOKEN=${localMcpToken.value || 'PASTE_TOKEN_HERE'}
if "%WECHATSYNC_TOKEN%"=="PASTE_TOKEN_HERE" (
  set /p WECHATSYNC_TOKEN=Please enter extension Token:
)
set SYNC_WS_PORT=9527
set SYNC_HTTP_PORT=9529
echo Starting Wechatsync MCP...
node packages\\mcp-server\\dist\\index.js --sse
pause`;
    const blob = new Blob([content], { type: 'text/plain;charset=utf-8' });
    const url = URL.createObjectURL(blob);
    const link = document.createElement('a');
    link.href = url;
    link.download = 'start-wechatsync-mcp.cmd';
    link.click();
    URL.revokeObjectURL(url);
    createMessage.success('一键启动脚本已下载');
  }

  function openChromeWebStore() {
    if (pluginInfo.value.chromeWebStoreUrl) {
      window.open(pluginInfo.value.chromeWebStoreUrl, '_blank');
    }
  }

  function downloadPlugin() {
    if (pluginInfo.value.downloadUrl) {
      window.open(pluginInfo.value.downloadUrl, '_blank');
    }
  }

  function openInstallPage() {
    if (pluginInfo.value.installPageUrl) {
      window.open(pluginInfo.value.installPageUrl, '_blank');
    }
  }

  function openExtensionPage() {
    window.open(extensionPageUrl, '_blank');
  }

  function copyLocalCommand() {
    navigator.clipboard
      .writeText(localCommand.value)
      .then(() => createMessage.success('本地检查命令已复制'))
      .catch(() => createMessage.error('复制失败，请手动复制'));
  }

  loadStatus();
  loadPluginInfo();
  setTimeout(() => {
    checkLocalMcp();
  }, 300);
</script>
