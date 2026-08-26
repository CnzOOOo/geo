<template>
  <BasicModal v-bind="$attrs" @register="registerModal" title="本地发布" :showOkBtn="false" width="720px">
    <a-descriptions :column="1" bordered size="small">
      <a-descriptions-item label="文章">{{ article?.title || record?.articleId || '-' }}</a-descriptions-item>
      <a-descriptions-item label="渠道">{{ channel?.channelName || record?.channelId || '-' }}（{{ channel?.platform || '-' }}）</a-descriptions-item>
      <a-descriptions-item label="任务状态">{{ statusText }}</a-descriptions-item>
    </a-descriptions>

    <div class="mt-3">
      <a-alert
        :type="localConnected ? 'success' : 'warning'"
        show-icon
        :message="localStatusText"
      />
    </div>

    <div class="mt-3">
      <a-space wrap>
        <a-button :loading="checking" @click="handleCheck">检查本地服务</a-button>
        <a-button type="primary" :loading="publishing" :disabled="!localConnected" @click="handlePublish">本地发布</a-button>
        <a-button @click="handleDownload">下载文章文件</a-button>
        <a-button @click="handleCopyCommand">复制本地命令</a-button>
      </a-space>
    </div>

    <pre v-if="output" class="mt-3 mb-0">{{ output }}</pre>
  </BasicModal>
</template>

<script lang="ts" setup>
  import { ref } from 'vue';
  import { BasicModal, useModalInner } from '/@/components/Modal';
  import { useMessage } from '/@/hooks/web/useMessage';
  import { getArticleById } from '../article/article.api';
  import { getChannelById } from '../channel/channel.api';
  import { saveOrUpdatePublishTask } from './publishTask.api';
  import { checkLocalWechatsync, getChannelWsPort, publishViaLocalWechatsync } from '../wechatsync/localWechatsync';

  const emit = defineEmits(['register', 'success']);
  const { createMessage } = useMessage();
  const record = ref<any>(null);
  const article = ref<any>(null);
  const channel = ref<any>(null);
  const checking = ref(false);
  const publishing = ref(false);
  const localConnected = ref(false);
  const localStatusText = ref('尚未检查本地服务');
  const output = ref('');

  const [registerModal, { setModalProps }] = useModalInner(async (data) => {
    record.value = data?.record || null;
    article.value = null;
    channel.value = null;
    localConnected.value = false;
    localStatusText.value = '尚未检查本地服务';
    output.value = '';
    if (record.value) {
      await loadDetail();
    }
  });

  const statusText = ref('排队中');

  async function loadDetail() {
    try {
      article.value = await getArticleById({ id: record.value.articleId });
      channel.value = await getChannelById({ id: record.value.channelId });
      statusText.value = statusMeta[record.value.status]?.label || record.value.status;
    } catch (e) {
      output.value = '读取文章或渠道失败：' + (e?.message || e);
    }
  }

  async function handleCheck() {
    checking.value = true;
    try {
      const status = await checkLocalWechatsync(getChannelWsPort(channel.value));
      localConnected.value = !!status.connected;
      localStatusText.value = localConnected.value
        ? '本地服务和 Chrome 扩展已连接，可以发布'
        : '本地服务可达，但 Chrome 扩展未连接';
      output.value = JSON.stringify(status, null, 2);
    } catch (e: any) {
      localConnected.value = false;
      localStatusText.value = '本地 Wechatsync MCP 服务未启动';
      output.value = e?.message || '无法连接本地服务';
    } finally {
      checking.value = false;
    }
  }

  async function handlePublish() {
    if (!localConnected.value || !article.value || !channel.value || !record.value) return;
    publishing.value = true;
    setModalProps({ confirmLoading: true });
    try {
      const result: any = await publishViaLocalWechatsync(
        [channel.value.platform],
        article.value.title,
        article.value.contentMd || '',
        getChannelWsPort(channel.value)
      );
      const syncResult = Array.isArray(result?.results) ? result.results[0] : null;
      await saveOrUpdatePublishTask(
        {
          ...record.value,
          status: syncResult?.success ? 2 : 3,
          externalId: syncResult?.postId || record.value.externalId,
          externalUrl: syncResult?.postUrl || record.value.externalUrl,
          errorCode: syncResult?.success ? null : syncResult?.error ? 'LOCAL_SYNC_FAILED' : record.value.errorCode,
          errorMsg: syncResult?.success ? null : syncResult?.error || record.value.errorMsg,
        },
        true
      );
      output.value = JSON.stringify(result, null, 2);
      createMessage.success(syncResult?.success ? '本地发布成功' : '本地发布失败，请查看输出');
      emit('success');
    } catch (e: any) {
      output.value = e?.message || '本地发布失败';
      createMessage.error(output.value);
    } finally {
      publishing.value = false;
      setModalProps({ confirmLoading: false });
    }
  }

  function handleDownload() {
    if (!article.value) return;
    const fileName = `geo-${article.value.id}.md`;
    const content = `# ${article.value.title || ''}\n\n${article.value.contentMd || ''}`;
    const blob = new Blob([content], { type: 'text/markdown;charset=utf-8' });
    const url = URL.createObjectURL(blob);
    const link = document.createElement('a');
    link.href = url;
    link.download = fileName;
    link.click();
    URL.revokeObjectURL(url);
    createMessage.success(`已下载 ${fileName}`);
  }

  function handleCopyCommand() {
    if (!article.value || !channel.value) return;
    const command = `wechatsync sync "geo-${article.value.id}.md" -p ${channel.value.platform}`;
    navigator.clipboard
      .writeText(command)
      .then(() => createMessage.success('本地发布命令已复制'))
      .catch(() => createMessage.error('复制失败，请手动复制'));
  }

  const statusMeta = {
    0: { label: '排队中' },
    1: { label: '发布中' },
    2: { label: '成功' },
    3: { label: '失败' },
    4: { label: '需人工' },
  };
</script>
