<template>
  <BasicModal v-bind="$attrs" @register="registerModal" :title="title" @ok="handleSubmit" width="720px">
    <BasicForm @register="registerForm">
      <template #areaLinkage="{ model }">
        <a-cascader
          :options="areaOptions"
          :value="getAreaValue(model)"
          :showSearch="true"
          allowClear
          placeholder="请选择省市区"
          @change="handleAreaChange(model, $event)"
        />
      </template>
    </BasicForm>
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
        areaLinkage: [data.record.province, data.record.city, data.record.district].filter(Boolean).join('/'),
      });
    }
  });

  const title = computed(() => (!unref(isUpdate) ? '新增商家' : '编辑商家'));

  function buildAreaOptions() {
    return regionData.map((province: any) => ({
      label: province.label,
      value: province.label,
      children: (province.children || []).map((city: any) => ({
        label: city.label,
        value: city.label,
        children: (city.children || []).map((district: any) => ({
          label: district.label,
          value: district.label,
        })),
      })),
    }));
  }

  const areaOptions = buildAreaOptions();

  function getAreaValue(model: any) {
    return [model.province, model.city, model.district].filter(Boolean);
  }

  function handleAreaChange(model: any, value: string[]) {
    const area = Array.isArray(value) ? value : [];
    model.province = area[0] || '';
    model.city = area[1] || '';
    model.district = area[2] || '';
    model.areaLinkage = area.join('/');
  }

  async function loadFormOptions() {
    const categoryFallback = ['餐饮', '咖啡店', '餐厅', '美容美发', '健身', '教育培训', '医疗服务', '金融保险', '家政服务', '宠物服务', '酒店住宿', '汽车服务', '旅游景点', '其他'];

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
    ]);
  }

  async function handleSubmit() {
    try {
      const values = await validate();
      const area = values.areaLinkage ? String(values.areaLinkage).split('/').filter(Boolean) : [];
      values.province = area[0] || '';
      values.city = area[1] || '';
      values.district = area[2] || '';
      delete values.areaLinkage;
      setModalProps({ confirmLoading: true });
      await saveOrUpdateMerchant(values, isUpdate.value);
      closeModal();
      emit('success', values);
    } finally {
      setModalProps({ confirmLoading: false });
    }
  }
</script>
