<template>
  <BasicModal v-bind="$attrs" @register="registerModal" title="本地发布" :showOkBtn="false" width="720px">
    <a-descriptions :column="1" bordered size="small">
      <a-descriptions-item label="文章">{{ article?.title || record?.articleId || '-' }}</a-descriptions-item>
      <a-descriptions-item label="渠道">{{ channel?.channelName || record?.channelId || '-' }}（{{ channel?.platform || '-' }}）</a-descriptions-item>
      <a-descriptions-item label="任务状态">{{ statusText }}</a-descriptions-item>
    </a-descriptions>

    <div class="mt-3">
      <a-alert
        type="info"
        show-icon
        message="点击“本地发布”后，系统会自动检测本地 Wechatsync 服务并创建平台草稿。"
      />
    </div>

    <div class="mt-3">
      <a-space wrap>
        <a-button type="primary" :loading="publishing" @click="handlePublish">本地发布</a-button>
        <a-button @click="handleDownload">下载文章文件</a-button>
        <a-popconfirm title="确认已在本地成功发布？" @confirm="handleMarkManualSuccess">
          <a-button>标记为已手动发布</a-button>
        </a-popconfirm>
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
  import { updatePublishTaskResult } from './publishTask.api';
  import { formatDateTimeNow, getChannelWsPort, publishViaLocalWechatsync } from '../wechatsync/localWechatsync';

  const emit = defineEmits(['register', 'success']);
  const { createMessage } = useMessage();
  const record = ref<any>(null);
  const article = ref<any>(null);
  const channel = ref<any>(null);
  const publishing = ref(false);
  const output = ref('');
  const statusText = ref('排队中');
  const statusMeta = {
    0: { label: '排队中' },
    1: { label: '发布中' },
    2: { label: '成功' },
    3: { label: '失败' },
    4: { label: '需人工' },
  };

  const [registerModal, { setModalProps }] = useModalInner(async (data) => {
    record.value = data?.record || null;
    article.value = null;
    channel.value = null;
    output.value = '';
    statusText.value = '排队中';
    if (record.value) {
      await loadDetail();
    }
  });

  async function loadDetail() {
    try {
      article.value = await getArticleById({ id: record.value.articleId });
      channel.value = await getChannelById({ id: record.value.channelId });
      statusText.value = statusMeta[record.value.status]?.label || record.value.status;
    } catch (e) {
      output.value = '读取文章或渠道失败：' + (e?.message || e);
    }
  }

  async function handlePublish() {
    if (!article.value || !channel.value || !record.value) return;
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
      const nextStatus = syncResult?.success ? 2 : 3;
      const nextExternalId = syncResult?.postId || record.value.externalId;
      const nextExternalUrl = syncResult?.postUrl || record.value.externalUrl;
      const nextErrorCode = syncResult?.success ? null : 'LOCAL_SYNC_FAILED';
      const nextErrorMsg = syncResult?.success ? null : syncResult?.error || '本地发布未返回成功结果';
      const nextPublishedAt = syncResult?.success ? formatDateTimeNow() : record.value.publishedAt;
      await updatePublishTaskResult({
        id: record.value.id,
        status: nextStatus,
        externalId: nextExternalId,
        externalUrl: nextExternalUrl,
        errorCode: nextErrorCode,
        errorMsg: nextErrorMsg,
        publishedAt: nextPublishedAt,
      });
      record.value = {
        ...record.value,
        status: nextStatus,
        externalId: nextExternalId,
        externalUrl: nextExternalUrl,
        errorCode: nextErrorCode,
        errorMsg: nextErrorMsg,
        publishedAt: nextPublishedAt,
      };
      statusText.value = statusMeta[nextStatus]?.label || String(nextStatus);
      output.value = JSON.stringify(result, null, 2);
      if (syncResult?.success) {
        createMessage.success('本地发布成功');
      } else {
        createMessage.warning('本地发布未成功，请查看输出');
      }
      emit('success');
    } catch (e: any) {
      output.value = e?.message || '本地发布失败';
      createMessage.error(`本地发布失败：${output.value}`);
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

  async function handleMarkManualSuccess() {
    if (!record.value) return;
    try {
      await updatePublishTaskResult({
        id: record.value.id,
        status: 2,
        errorCode: null,
        errorMsg: null,
        publishedAt: record.value.publishedAt || formatDateTimeNow(),
      });
      record.value = {
        ...record.value,
        status: 2,
        errorCode: null,
        errorMsg: null,
        publishedAt: record.value.publishedAt || formatDateTimeNow(),
      };
      statusText.value = '成功';
      createMessage.success('已标记为手动发布成功');
      emit('success');
    } catch (e: any) {
      createMessage.error(e?.message || '标记失败');
    }
  }
</script>
