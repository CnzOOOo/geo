<template>
  <BasicModal v-bind="$attrs" @register="registerModal" :title="title" @ok="handleSubmit" width="720px">
    <BasicForm @register="registerForm" />
  </BasicModal>
</template>

<script lang="ts" setup>
  import { ref, computed, unref } from 'vue';
  import { BasicModal, useModalInner } from '/@/components/Modal';
  import { BasicForm, useForm } from '/@/components/Form/index';
  import { regionData } from '@/components/Form/src/utils/areaDataUtil';
  import { formSchema } from './merchant.data';
  import { saveOrUpdateMerchant, getMerchantById, getMerchantList } from './merchant.api';

  const emit = defineEmits(['register', 'success']);
  const isUpdate = ref(false);

  const [registerForm, { resetFields, setFieldsValue, validate, updateSchema }] = useForm({
    schemas: formSchema,
    showActionButtonGroup: false,
  });

  const [registerModal, { setModalProps, closeModal }] = useModalInner(async (data) => {
    await resetFields();
    await loadFormOptions();
    setModalProps({ confirmLoading: false, showOkBtn: true });
    isUpdate.value = !!data?.isUpdate;
    if (unref(isUpdate)) {
      data.record = await getMerchantById({ id: data.record.id });
      await setFieldsValue({
        ...data.record,
      });
    }
  });

  const title = computed(() => (!unref(isUpdate) ? '新增商家' : '编辑商家'));

  function collectAreaLabels(nodes: any[], level: number): string[] {
    const labels: string[] = [];
    const walk = (list: any[], currentLevel: number) => {
      if (!Array.isArray(list)) return;
      list.forEach((item) => {
        if (currentLevel === 0) labels.push(item.label);
        if (currentLevel > 0 && Array.isArray(item.children)) walk(item.children, currentLevel - 1);
      });
    };
    walk(nodes, level);
    return Array.from(new Set(labels));
  }

  async function loadFormOptions() {
    const categoryFallback = ['餐饮', '咖啡店', '餐厅', '美容美发', '健身', '教育培训', '医疗服务', '金融保险', '家政服务', '宠物服务', '酒店住宿', '汽车服务', '旅游景点', '其他'];
    const provinceFallback = collectAreaLabels(regionData, 0);
    const cityFallback = collectAreaLabels(regionData, 1);
    const districtFallback = collectAreaLabels(regionData, 2);

    let existingMerchants: any[] = [];
    try {
      const result: any = await getMerchantList({ pageNo: 1, pageSize: 500 });
      existingMerchants = result?.records || [];
    } catch (e) {
      existingMerchants = [];
    }

    const mergeOptions = (field: string, fallback: string[]) => {
      const values = Array.from(
        new Set([...fallback, ...existingMerchants.map((item) => item?.[field]).filter(Boolean)])
      );
      return values.map((value) => ({ value }));
    };

    await updateSchema([
      { field: 'category', componentProps: { options: mergeOptions('category', categoryFallback) } },
      { field: 'province', componentProps: { options: mergeOptions('province', provinceFallback) } },
      { field: 'city', componentProps: { options: mergeOptions('city', cityFallback) } },
      { field: 'district', componentProps: { options: mergeOptions('district', districtFallback) } },
    ]);
  }

  async function handleSubmit() {
    try {
      const values = await validate();
      setModalProps({ confirmLoading: true });
      await saveOrUpdateMerchant(values, isUpdate.value);
      closeModal();
      emit('success', values);
    } finally {
      setModalProps({ confirmLoading: false });
    }
  }
</script>
