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
  import { saveOrUpdatePublishTask, getPublishTaskById } from './publishTask.api';

  const emit = defineEmits(['register', 'success']);
  const isUpdate = ref(false);
  const [registerForm, { resetFields, setFieldsValue, validate }] = useForm({ schemas: formSchema, showActionButtonGroup: false });
  const [registerModal, { setModalProps, closeModal }] = useModalInner(async (data) => {
    await resetFields();
    setModalProps({ confirmLoading: false, showOkBtn: true });
    isUpdate.value = !!data?.isUpdate;
    if (unref(isUpdate)) {
      data.record = await getPublishTaskById({ id: data.record.id });
      await setFieldsValue({ ...data.record });
    }
  });
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
