<template>
  <BasicModal v-bind="$attrs" @register="registerModal" title="发布到平台" @ok="handleSubmit" width="720px">
    <BasicForm @register="registerForm" />
  </BasicModal>
</template>

<script lang="ts" setup>
  import { ref } from 'vue';
  import { BasicModal, useModalInner } from '/@/components/Modal';
  import { BasicForm, useForm } from '/@/components/Form/index';
  import { useMessage } from '/@/hooks/web/useMessage';
  import { formSchema } from './publishWizard.data';
  import { getArticleById } from './article.api';
  import { getChannelList } from '../channel/channel.api';
  import { createPublishTask, updatePublishTaskResult } from '../publishTask/publishTask.api';
  import { formatDateTimeNow, getChannelWsPort, publishViaLocalWechatsync } from '../wechatsync/localWechatsync';

  const emit = defineEmits(['register', 'success']);
  const { createMessage } = useMessage();
  const channelMap = ref<Record<string, any>>({});
  const articleTitle = ref('');
  const articleContent = ref('');

  const [registerForm, { resetFields, setFieldsValue, validate, updateSchema }] = useForm({
    schemas: formSchema,
    showActionButtonGroup: false,
  });

  const [registerModal, { setModalProps, closeModal }] = useModalInner(async (data) => {
    await resetFields();
    await loadChannelOptions();
    setModalProps({ confirmLoading: false, showOkBtn: true });
    if (data?.record) {
      const article: any = await getArticleById({ id: data.record.id });
      articleTitle.value = article?.title || data.record.title;
      articleContent.value = article?.contentMd || '';
      await setFieldsValue({
        articleId: data.record.id,
        articleTitle: articleTitle.value,
        channelIds: [],
        immediateExecute: true,
      });
    }
  });

  async function loadChannelOptions() {
    try {
      const result: any = await getChannelList({ pageNo: 1, pageSize: 500, enabled: 1, status: 1 });
      const records = result?.records || [];
      channelMap.value = Object.fromEntries(records.map((item) => [item.id, item]));
      const options = (result?.records || []).map((item) => ({
        label: `${item.channelName}（${item.platform}）`,
        value: item.id,
      }));
      await updateSchema([{ field: 'channelIds', componentProps: { options } }]);
    } catch (e) {
      await updateSchema([{ field: 'channelIds', componentProps: { options: [] } }]);
    }
  }

  async function handleSubmit() {
    try {
      const values = await validate();
      const rawChannelIds = values.channelIds == null ? [] : Array.isArray(values.channelIds) ? values.channelIds : [values.channelIds];
      const channelIds = rawChannelIds
        .map((item) => (item && typeof item === 'object' ? item.value : item))
        .filter(Boolean);
      if (channelIds.length === 0) {
        createMessage.warning('请至少选择一个发布渠道');
        return;
      }
      setModalProps({ confirmLoading: true });

      const tasks = [];
      for (const channelId of channelIds) {
        const task: any = await createPublishTask({
          articleId: values.articleId,
          channelId,
          status: 4,
          errorCode: 'LOCAL_PENDING',
          errorMsg: '等待本地发布工作站执行',
        });
        tasks.push({ task, channelId, platform: channelMap.value[channelId]?.platform });
      }

      let successCount = 0;
      if (values.immediateExecute) {
        try {
          const platforms = tasks.map((item) => item.platform).filter(Boolean);
          const wsPorts = tasks.map((item) => getChannelWsPort(channelMap.value[item.channelId])).find(Boolean);
          const result: any = await publishViaLocalWechatsync(platforms, articleTitle.value, articleContent.value, wsPorts);
          const syncResults = Array.isArray(result?.results) ? result.results : [];
          for (const item of tasks) {
            const sync = syncResults.find((item2) => item2.platform === item.platform);
            if (sync?.success) {
              successCount += 1;
            }
            await updatePublishTaskResult({
              id: item.task.id,
              status: sync?.success ? 2 : 3,
              externalId: sync?.postId || item.task.externalId,
              externalUrl: sync?.postUrl || item.task.externalUrl,
              errorCode: sync?.success ? null : 'LOCAL_SYNC_FAILED',
              errorMsg: sync?.success ? null : sync?.error || '本地发布未返回成功结果',
              publishedAt: sync?.success ? formatDateTimeNow() : item.task.publishedAt,
            });
          }
          createMessage.success(`本地发布完成：${successCount}/${tasks.length} 个草稿`);
        } catch (e: any) {
          createMessage.warning(`已创建本地任务，但本地执行失败：${e?.message || '未知错误'}`);
        }
      } else {
        createMessage.success(`已创建 ${tasks.length} 个本地任务`);
      }
      closeModal();
      emit('success', { count: tasks.length });
    } catch (e: any) {
      createMessage.error(e?.message || e?.response?.data?.message || '发布失败，请检查渠道配置和后端日志');
    } finally {
      setModalProps({ confirmLoading: false });
    }
  }
</script>
