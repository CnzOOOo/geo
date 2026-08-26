<template>
  <div>
    <BasicTable @register="registerTable" :rowSelection="rowSelection" :class="{ 'p-4': true }">
      <template #tableTitle>
        <a-button type="primary" preIcon="ant-design:plus-outlined" @click="handleAdd">新增</a-button>
        <a-button v-if="selectedRowKeys.length > 0" type="primary" danger preIcon="ant-design:delete-outlined" @click="batchHandleDelete">批量删除</a-button>
      </template>
      <template #titleType="{ record }">
        <span>{{ titleTypeMeta[record.titleType] || record.titleType || '-' }}</span>
      </template>
      <template #status="{ record }">
        <a-tag :color="statusMeta[record.status]?.color || 'default'">{{ statusMeta[record.status]?.label || record.status }}</a-tag>
      </template>
      <template #reviewStatus="{ record }">
        <a-tag :color="reviewStatusMeta[record.reviewStatus]?.color || 'default'">{{ reviewStatusMeta[record.reviewStatus]?.label || record.reviewStatus }}</a-tag>
      </template>
      <template #action="{ record }">
        <TableAction :actions="getActions(record)" />
      </template>
    </BasicTable>
    <ArticleModal @register="registerModal" @success="reload" />
    <PublishWizardModal @register="registerPublishModal" @success="reload" />
  </div>
</template>

<script lang="ts" setup>
  import { ref } from 'vue';
  import { BasicTable, useTable, TableAction } from '/@/components/Table';
  import { useModal } from '/@/components/Modal';
  import ArticleModal from './ArticleModal.vue';
  import PublishWizardModal from './PublishWizardModal.vue';
  import { columns, searchFormSchema } from './article.data';
  import {
    getArticleList,
    deleteArticle,
    batchDeleteArticle,
    submitReviewArticle,
    publishArticle,
    offlineArticle,
  } from './article.api';

  const selectedRowKeys = ref<Array<string | number>>([]);
  const [registerModal, { openModal }] = useModal();
  const [registerPublishModal, { openModal: openPublishModal }] = useModal();
  const titleTypeMeta = {
    QUESTION: '问答式',
    COMPARE: '对比式',
    AVOID: '避坑式',
  };
  const statusMeta = {
    0: { label: '草稿', color: 'default' },
    1: { label: '待审核', color: 'processing' },
    2: { label: '已发布', color: 'success' },
    3: { label: '已下线', color: 'warning' },
  };
  const reviewStatusMeta = {
    0: { label: '未审核', color: 'default' },
    1: { label: '审核中', color: 'processing' },
    2: { label: '已通过', color: 'success' },
    3: { label: '已驳回', color: 'error' },
  };
  const [registerTable, { reload }] = useTable({
    title: 'GEO 文章工坊',
    api: getArticleList,
    columns,
    formConfig: { schemas: searchFormSchema },
    rowKey: 'id',
    useSearchForm: true,
    showTableSetting: true,
    bordered: true,
    actionColumn: { width: 320, title: '操作', dataIndex: 'action', slots: { customRender: 'action' } },
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
      { label: '发布到平台', onClick: handlePublish.bind(null, record) },
      { label: '提交审核', onClick: () => submitReviewArticle({ id: record.id }, reload) },
      { label: '发布', onClick: () => publishArticle({ id: record.id }, reload) },
      { label: '下线', onClick: () => offlineArticle({ id: record.id }, reload) },
      { label: '删除', popConfirm: { title: '是否确认删除？', confirm: handleDelete.bind(null, record) } },
    ];
  }

  function handleAdd() { openModal(true, { isUpdate: false }); }
  function handleEdit(record) { openModal(true, { record, isUpdate: true }); }
  function handlePublish(record) { openPublishModal(true, { record, isUpdate: false }); }
  function handleDelete(record) { deleteArticle({ id: record.id }, reload); }
  function batchHandleDelete() {
    batchDeleteArticle({ ids: selectedRowKeys.value.join(',') }, () => {
      selectedRowKeys.value = [];
      reload();
    });
  }
</script>
