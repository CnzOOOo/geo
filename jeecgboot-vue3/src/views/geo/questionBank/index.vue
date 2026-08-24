<template>
  <div>
    <BasicTable @register="registerTable" :rowSelection="rowSelection" :class="{ 'p-4': true }">
      <template #tableTitle>
        <a-button type="primary" preIcon="ant-design:plus-outlined" @click="handleAdd">新增</a-button>
        <a-button v-if="selectedRowKeys.length > 0" type="primary" danger preIcon="ant-design:delete-outlined" @click="batchHandleDelete">批量删除</a-button>
      </template>
      <template #action="{ record }">
        <TableAction :actions="getActions(record)" />
      </template>
    </BasicTable>
    <QuestionBankModal @register="registerModal" @success="reload" />
  </div>
</template>

<script lang="ts" setup>
  import { ref } from 'vue';
  import { BasicTable, useTable, TableAction } from '/@/components/Table';
  import { useModal } from '/@/components/Modal';
  import QuestionBankModal from './QuestionBankModal.vue';
  import { columns, searchFormSchema } from './questionBank.data';
  import { getQuestionList, deleteQuestion, batchDeleteQuestion } from './questionBank.api';

  const selectedRowKeys = ref<Array<string | number>>([]);
  const [registerModal, { openModal }] = useModal();
  const [registerTable, { reload }] = useTable({
    title: 'GEO 问题库',
    api: getQuestionList,
    columns,
    formConfig: { schemas: searchFormSchema },
    rowKey: 'id',
    useSearchForm: true,
    showTableSetting: true,
    bordered: true,
    actionColumn: { width: 160, title: '操作', dataIndex: 'action', slots: { customRender: 'action' } },
  });

  const rowSelection = {
    type: 'checkbox',
    columnWidth: 40,
    selectedRowKeys,
    onChange: (keys) => { selectedRowKeys.value = keys; },
  };

  function getActions(record) {
    return [
      { label: '编辑', onClick: handleEdit.bind(null, record) },
      { label: '删除', popConfirm: { title: '是否确认删除？', confirm: handleDelete.bind(null, record) } },
    ];
  }

  function handleAdd() { openModal(true, { isUpdate: false }); }
  function handleEdit(record) { openModal(true, { record, isUpdate: true }); }
  function handleDelete(record) { deleteQuestion({ id: record.id }, reload); }
  function batchHandleDelete() {
    batchDeleteQuestion({ ids: selectedRowKeys.value.join(',') }, () => {
      selectedRowKeys.value = [];
      reload();
    });
  }
</script>
