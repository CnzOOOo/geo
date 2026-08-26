<template>
  <BasicModal v-bind="$attrs" @register="registerModal" :title="title" @ok="handleSubmit" width="760px">
    <BasicForm @register="registerForm" />
  </BasicModal>
</template>

<script lang="ts" setup>
  import { ref, computed, unref } from 'vue';
  import { BasicModal, useModalInner } from '/@/components/Modal';
  import { BasicForm, useForm } from '/@/components/Form/index';
  import { formSchema } from './publishTask.data';
  import { getArticleList } from '../article/article.api';
  import { getChannelList } from '../channel/channel.api';
  import { saveOrUpdatePublishTask, getPublishTaskById } from './publishTask.api';

  const emit = defineEmits(['register', 'success']);
  const isUpdate = ref(false);
  const [registerForm, { resetFields, setFieldsValue, validate, updateSchema }] = useForm({ schemas: formSchema, showActionButtonGroup: false });
  const [registerModal, { setModalProps, closeModal }] = useModalInner(async (data) => {
    await resetFields();
    await loadOptions();
    setModalProps({ confirmLoading: false, showOkBtn: true });
    isUpdate.value = !!data?.isUpdate;
    if (unref(isUpdate)) {
      data.record = await getPublishTaskById({ id: data.record.id });
      await setFieldsValue({ ...data.record });
    }
  });

  async function loadOptions() {
    try {
      const [articleResult, channelResult]: any[] = await Promise.all([
        getArticleList({ pageNo: 1, pageSize: 500 }),
        getChannelList({ pageNo: 1, pageSize: 500 }),
      ]);
      const articleOptions = (articleResult?.records || []).map((item) => ({
        label: item.title,
        value: item.id,
      }));
      const channelOptions = (channelResult?.records || []).map((item) => ({
        label: `${item.channelName}（${item.platform}）`,
        value: item.id,
      }));
      await updateSchema([
        { field: 'articleId', componentProps: { options: articleOptions } },
        { field: 'channelId', componentProps: { options: channelOptions } },
      ]);
    } catch (e) {
      await updateSchema([
        { field: 'articleId', componentProps: { options: [] } },
        { field: 'channelId', componentProps: { options: [] } },
      ]);
    }
  }
  const title = computed(() => (!unref(isUpdate) ? '新增发布任务' : '编辑发布任务'));
  async function handleSubmit() {
    try {
      const values = await validate();
      setModalProps({ confirmLoading: true });
      await saveOrUpdatePublishTask(values, isUpdate.value);
      closeModal();
      emit('success', values);
    } finally {
      setModalProps({ confirmLoading: false });
    }
  }
</script>
