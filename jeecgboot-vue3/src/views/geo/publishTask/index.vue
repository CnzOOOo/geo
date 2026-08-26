<template>
  <div>
    <BasicTable @register="registerTable" :rowSelection="rowSelection" :class="{ 'p-4': true }">
      <template #tableTitle>
        <a-button type="primary" preIcon="ant-design:plus-outlined" @click="handleAdd">新增</a-button>
        <a-button v-if="selectedRowKeys.length > 0" type="primary" danger preIcon="ant-design:delete-outlined" @click="batchHandleDelete">批量删除</a-button>
      </template>
      <template #articleId="{ record }">
        <span>{{ articleMap[record.articleId]?.title || `${record.articleId}（已删除）` }}</span>
      </template>
      <template #channelId="{ record }">
        <span>{{ channelMap[record.channelId]?.channelName || `${record.channelId}（已删除）` }}</span>
      </template>
      <template #status="{ record }">
        <a-tag :color="statusMeta[record.status]?.color || 'default'">{{ statusMeta[record.status]?.label || record.status }}</a-tag>
      </template>
      <template #externalUrl="{ record }">
        <a
          v-if="record.externalUrl"
          :href="record.externalUrl"
          target="_blank"
          rel="noopener"
        >{{ record.externalUrl }}</a>
        <span v-else>-</span>
      </template>
      <template #action="{ record }">
        <TableAction :actions="getActions(record)" />
      </template>
    </BasicTable>
    <PublishTaskModal @register="registerModal" @success="reload" />
  </div>
</template>

<script lang="ts" setup>
  import { onMounted, ref } from 'vue';
  import { BasicTable, useTable, TableAction } from '/@/components/Table';
  import { useModal } from '/@/components/Modal';
  import PublishTaskModal from './PublishTaskModal.vue';
  import { columns, searchFormSchema } from './publishTask.data';
  import { getPublishTaskList, deletePublishTask, batchDeletePublishTask, executePublishTask } from './publishTask.api';
  import { getArticleList } from '../article/article.api';
  import { getChannelList } from '../channel/channel.api';

  const selectedRowKeys = ref<Array<string | number>>([]);
  const [registerModal, { openModal }] = useModal();
  const articleMap = ref<Record<string, any>>({});
  const channelMap = ref<Record<string, any>>({});
  const statusMeta = {
    0: { label: '排队中', color: 'default' },
    1: { label: '发布中', color: 'processing' },
    2: { label: '成功', color: 'success' },
    3: { label: '失败', color: 'error' },
    4: { label: '需人工', color: 'warning' },
  };
  const [registerTable, { reload }] = useTable({
    title: 'GEO 发布任务',
    api: getPublishTaskList,
    columns,
    formConfig: { schemas: searchFormSchema },
    rowKey: 'id',
    useSearchForm: true,
    showTableSetting: true,
    bordered: true,
    actionColumn: { width: 160, title: '操作', dataIndex: 'action', slots: { customRender: 'action' } },
  });
  const rowSelection = { type: 'checkbox', columnWidth: 40, selectedRowKeys, onChange: (keys) => { selectedRowKeys.value = keys; } };
  function getActions(record) {
    return [
      { label: '编辑', onClick: handleEdit.bind(null, record) },
      { label: '执行', onClick: () => executePublishTask({ id: record.id }, reload) },
      { label: '删除', popConfirm: { title: '是否确认删除？', confirm: handleDelete.bind(null, record) } },
    ];
  }
  function handleAdd() { openModal(true, { isUpdate: false }); }
  function handleEdit(record) { openModal(true, { record, isUpdate: true }); }
  function handleDelete(record) { deletePublishTask({ id: record.id }, reload); }
  function batchHandleDelete() {
    batchDeletePublishTask({ ids: selectedRowKeys.value.join(',') }, () => {
      selectedRowKeys.value = [];
      reload();
    });
  }

  async function loadReferenceData() {
    try {
      const [articleResult, channelResult]: any[] = await Promise.all([
        getArticleList({ pageNo: 1, pageSize: 1000 }),
        getChannelList({ pageNo: 1, pageSize: 1000 }),
      ]);
      articleMap.value = Object.fromEntries((articleResult?.records || []).map((item) => [item.id, item]));
      channelMap.value = Object.fromEntries((channelResult?.records || []).map((item) => [item.id, item]));
    } catch (e) {
      articleMap.value = {};
      channelMap.value = {};
    }
  }

  onMounted(loadReferenceData);
</script>
