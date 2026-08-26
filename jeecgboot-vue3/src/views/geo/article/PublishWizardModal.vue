<template>
  <BasicModal v-bind="$attrs" @register="registerModal" title="发布到平台" @ok="handleSubmit" width="720px">
    <BasicForm @register="registerForm" />
  </BasicModal>
</template>

<script lang="ts" setup>
  import { BasicModal, useModalInner } from '/@/components/Modal';
  import { BasicForm, useForm } from '/@/components/Form/index';
  import { useMessage } from '/@/hooks/web/useMessage';
  import { formSchema } from './publishWizard.data';
  import { getChannelList } from '../channel/channel.api';
  import { createPublishTask, createAndExecutePublishTask } from '../publishTask/publishTask.api';

  const emit = defineEmits(['register', 'success']);
  const { createMessage } = useMessage();

  const [registerForm, { resetFields, setFieldsValue, validate, updateSchema }] = useForm({
    schemas: formSchema,
    showActionButtonGroup: false,
  });

  const [registerModal, { setModalProps, closeModal }] = useModalInner(async (data) => {
    await resetFields();
    await loadChannelOptions();
    setModalProps({ confirmLoading: false, showOkBtn: true });
    if (data?.record) {
      await setFieldsValue({
        articleId: data.record.id,
        articleTitle: data.record.title,
        channelIds: [],
        immediateExecute: true,
      });
    }
  });

  async function loadChannelOptions() {
    try {
      const result: any = await getChannelList({ pageNo: 1, pageSize: 500, enabled: 1, status: 1 });
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
      const results = [];
      for (const channelId of channelIds) {
        if (values.immediateExecute) {
          results.push(await createAndExecutePublishTask({ articleId: values.articleId, channelId, status: 0 }));
        } else {
          results.push(await createPublishTask({ articleId: values.articleId, channelId, status: 0 }));
        }
      }
      closeModal();
      emit('success', { count: results.length });
    } catch (e: any) {
      createMessage.error(e?.message || e?.response?.data?.message || '发布失败，请检查渠道配置和后端日志');
    } finally {
      setModalProps({ confirmLoading: false });
    }
  }
</script>
