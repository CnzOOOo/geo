<template>
  <div class="p-4 geo-wechatsync-page">
    <a-card>
      <template #title>
        <div class="page-title">Wechatsync 本地发布服务</div>
        <div class="page-subtitle">服务器管理任务，本地 MCP 启动器负责连接 Chrome 扩展并创建平台草稿。</div>
      </template>

      <a-row :gutter="[16, 16]">
        <a-col :span="isServerMode ? 12 : 24">
          <div class="section-title">服务器状态</div>
          <a-descriptions :column="1" size="small">
            <a-descriptions-item label="部署模式">
              <a-tag :color="isServerMode ? 'blue' : 'green'">{{ isServerMode ? '生产服务器模式' : '本地工作站模式' }}</a-tag>
            </a-descriptions-item>
            <a-descriptions-item label="后端服务">
              <a-tag :color="status.serverReady ? 'green' : 'red'">{{ status.serverReady ? '正常' : '异常' }}</a-tag>
            </a-descriptions-item>
          </a-descriptions>
        </a-col>

        <a-col :span="isServerMode ? 12 : 24">
          <div class="section-title">本地 MCP 服务</div>
          <a-alert
            :type="localMcpConnected ? 'success' : 'warning'"
            show-icon
            :message="localMcpMessage"
            class="mb-3"
          />
          <a-space wrap>
            <a-button type="primary" @click="downloadMcpExe">下载 MCP 启动器</a-button>
            <a-button :loading="localMcpChecking" @click="checkLocalMcp">检测服务</a-button>
          </a-space>
          <p class="hint">下载后双击 exe 运行。Token 读取 exe 同目录下的 token.txt，内容为扩展设置里的 Token。</p>
        </a-col>
      </a-row>
    </a-card>
  </div>
</template>

<script lang="ts" setup>
  import { ref, computed } from 'vue';
  import { getWechatsyncStatus } from './wechatsync.api';
  import { checkLocalWechatsync } from './localWechatsync';

  const status = ref<any>({});
  const localMcpChecking = ref(false);
  const localMcpConnected = ref(false);
  const localMcpMessage = ref('下载 MCP 启动器并双击运行，然后点击检测服务');
  const isServerMode = computed(() => status.value.deploymentMode === 'server');

  async function loadStatus() {
    status.value = await getWechatsyncStatus();
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
    } catch (e: any) {
      localMcpConnected.value = false;
      localMcpMessage.value = '本地 MCP 服务未启动，请下载启动器并运行';
    } finally {
      localMcpChecking.value = false;
    }
  }

  function downloadMcpExe() {
    window.open('https://front.rucode.cn/downloads/geo-wechatsync-mcp.exe', '_blank');
  }

  loadStatus();
  setTimeout(() => {
    checkLocalMcp();
  }, 300);
</script>

<style scoped>
  .geo-wechatsync-page :deep(.ant-card-body) {
    padding: 20px;
  }

  .page-title {
    font-size: 18px;
    font-weight: 700;
    color: #172033;
  }

  .page-subtitle {
    margin-top: 4px;
    color: #8a95a5;
    font-size: 13px;
  }

  .section-title {
    margin-bottom: 12px;
    font-size: 14px;
    font-weight: 650;
    color: #253147;
  }

  .hint {
    margin: 12px 0 0;
    color: #8a95a5;
    font-size: 13px;
    line-height: 1.7;
  }
</style>
