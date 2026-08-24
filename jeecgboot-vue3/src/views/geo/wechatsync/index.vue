<template>
  <div class="p-4">
    <a-card title="Wechatsync 环境检查">
      <a-space wrap>
        <a-button type="primary" :loading="statusLoading" @click="loadStatus">重新检测</a-button>
        <a-button :loading="installLoading" @click="installCli">一键安装</a-button>
        <a-button :loading="updateCheckLoading" @click="checkUpdate">检查更新</a-button>
        <a-button :loading="updateLoading" @click="updateCli">一键更新</a-button>
        <a-button type="primary" @click="openChromeWebStore">安装浏览器插件</a-button>
        <a-button @click="downloadPlugin">下载插件包</a-button>
        <a-button :loading="platformLoading" @click="checkPlatformStatus">{{ isServerMode ? '本地工作站检查指引' : '检查平台登录' }}</a-button>
      </a-space>

      <a-descriptions bordered :column="1" class="mt-4">
        <a-descriptions-item label="部署模式">
          <a-tag :color="isServerMode ? 'blue' : 'green'">{{ isServerMode ? '生产服务器模式' : '本地工作站模式' }}</a-tag>
        </a-descriptions-item>
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
      </a-descriptions>

      <a-alert
        v-if="issues.length"
        type="warning"
        show-icon
        class="mt-4"
        :message="`发现 ${issues.length} 个问题`"
        :description="issues.join('；')"
      />

      <a-card size="small" title="同步桥接设置" class="mt-4">
        <a-alert
          :type="bridgeConnected ? 'success' : 'warning'"
          show-icon
          class="mb-3"
          :message="bridgeConnected ? 'Chrome 扩展已连接' : '需要先在扩展中开启同步桥接'"
          :description="bridgeConnected
            ? 'CLI 与扩展之间的 WebSocket 已接通，可以继续检查平台登录并执行草稿发布测试。'
            : '该功能在扩展弹窗的“设置”里，不在 GEO 页面中；点击下方按钮打开扩展页面后按步骤操作。'"
        />
        <ol class="mb-3">
          <li>点击浏览器工具栏中的“文章同步助手”扩展图标。</li>
          <li>在弹窗右上角点击“设置”（齿轮图标）。</li>
          <li>打开“同步桥接”下的“CLI / MCP 连接”开关。</li>
          <li>复制页面显示的 Token，并确认服务器地址为 <code>ws://localhost:9527</code>。</li>
          <li>回到 GEO 的“发布渠道”配置，填入该 Token；端口默认 9527 可不填。</li>
          <li>回到本页点击“检查平台登录”，看到“Chrome Extension 已连接”后再发布。</li>
        </ol>
        <a-space wrap>
          <a-button type="primary" @click="openExtensionPage">打开扩展设置</a-button>
          <a-button :loading="platformLoading" @click="checkPlatformStatus">{{ isServerMode ? '查看本地工作站检查指引' : '检查平台登录' }}</a-button>
        </a-space>
      </a-card>

      <a-card v-if="isServerMode" size="small" title="本地发布工作站" class="mt-4">
        <p class="mb-2">生产服务器不运行 Wechatsync CLI，真实平台草稿由本地发布工作站执行。</p>
        <ol class="mb-3">
          <li>打开 Chrome，确认“文章同步助手”扩展已启用。</li>
          <li>在扩展设置中开启“同步桥接 / CLI / MCP 连接”。</li>
          <li>复制扩展 Token，配置到本地 CLI 环境变量 <code>WECHATSYNC_TOKEN</code>。</li>
          <li>在本机执行：<code>wechatsync platforms --auth</code>，确认目标平台已登录。</li>
          <li>再通过 GEO 发布任务把文章发布到各平台草稿箱。</li>
        </ol>
        <a-button :loading="platformLoading" @click="checkPlatformStatus">查看本地工作站检查指引</a-button>
      </a-card>

      <a-card v-if="platformOutput" size="small" title="平台登录状态" class="mt-4">
        <pre class="mb-0">{{ platformOutput }}</pre>
      </a-card>

      <a-card v-if="pluginInfo.chromeWebStoreUrl" size="small" title="浏览器插件安装说明" class="mt-4">
        <ol class="mb-2">
          <li v-for="(step, index) in pluginInfo.steps" :key="index">{{ step }}</li>
        </ol>
        <a-space wrap>
          <a-button type="primary" @click="openChromeWebStore">Chrome 官方安装入口</a-button>
          <a-button @click="downloadPlugin">下载插件包</a-button>
          <a-button @click="openInstallPage">Wechatsync 官方安装页</a-button>
        </a-space>
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

  const { createMessage } = useMessage();
  const status = ref<any>({});
  const updateInfo = ref<any>({});
  const statusLoading = ref(false);
  const installLoading = ref(false);
  const updateCheckLoading = ref(false);
  const updateLoading = ref(false);
  const platformLoading = ref(false);
  const pluginInfo = ref<any>({});
  const extensionPageUrl = 'chrome-extension://hchobocdmclopcbnibdnoafilagadion/src/popup/index.html';

  const issues = computed(() => status.value.issues || []);
  const platformOutput = computed(() => status.value.platformStatus?.output || '');
  const isServerMode = computed(() => status.value.deploymentMode === 'server');
  const bridgeConnected = computed(() => {
    return /Chrome Extension 已连接|已连接/.test(platformOutput.value);
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

  loadStatus();
  loadPluginInfo();
</script>
