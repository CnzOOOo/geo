<template>
  <BasicModal v-bind="$attrs" @register="registerModal" title="发布到平台" @ok="handleSubmit" width="720px">
    <BasicForm @register="registerForm" />
  </BasicModal>
</template>

<script lang="ts" setup>
  import { BasicModal, useModalInner } from '/@/components/Modal';
  import { BasicForm, useForm } from '/@/components/Form/index';
  import { formSchema } from './publishWizard.data';
  import { createPublishTask, createAndExecutePublishTask } from '../publishTask/publishTask.api';

  const emit = defineEmits(['register', 'success']);

  const [registerForm, { resetFields, setFieldsValue, validate }] = useForm({
    schemas: formSchema,
    showActionButtonGroup: false,
  });

  const [registerModal, { setModalProps, closeModal }] = useModalInner(async (data) => {
    await resetFields();
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

  async function handleSubmit() {
    try {
      const values = await validate();
      setModalProps({ confirmLoading: true });
      const results = [];
      for (const channelId of values.channelIds) {
        if (values.immediateExecute) {
          results.push(await createAndExecutePublishTask({ articleId: values.articleId, channelId, status: 0 }));
        } else {
          results.push(await createPublishTask({ articleId: values.articleId, channelId, status: 0 }));
        }
      }
      closeModal();
      emit('success', { count: results.length });
    } finally {
      setModalProps({ confirmLoading: false });
    }
  }
</script>
