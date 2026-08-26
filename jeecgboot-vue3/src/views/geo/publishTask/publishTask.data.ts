import { BasicColumn } from '/@/components/Table';
import { FormSchema } from '/@/components/Table';

export const columns: BasicColumn[] = [
  { title: '文章', dataIndex: 'articleId', width: 240, slots: { customRender: 'articleId' } },
  { title: '渠道', dataIndex: 'channelId', width: 180, slots: { customRender: 'channelId' } },
  { title: '状态', dataIndex: 'status', width: 100, slots: { customRender: 'status' } },
  { title: '外部ID', dataIndex: 'externalId', width: 180 },
  { title: '外部链接', dataIndex: 'externalUrl', width: 240, slots: { customRender: 'externalUrl' } },
  { title: '错误码', dataIndex: 'errorCode', width: 130 },
  { title: '错误信息', dataIndex: 'errorMsg', width: 220 },
  { title: '重试次数', dataIndex: 'retryCount', width: 90 },
  { title: '发布时间', dataIndex: 'publishedAt', width: 160 },
];

export const searchFormSchema: FormSchema[] = [
  { label: '文章ID', field: 'articleId', component: 'Input', colProps: { span: 8 } },
  {
    label: '状态',
    field: 'status',
    component: 'Select',
    colProps: { span: 8 },
    componentProps: {
      allowClear: true,
      options: [
        { label: '排队中', value: 0 },
        { label: '发布中', value: 1 },
        { label: '成功', value: 2 },
        { label: '失败', value: 3 },
        { label: '需人工', value: 4 },
      ],
    },
  },
];

export const formSchema: FormSchema[] = [
  { field: 'id', label: 'id', component: 'Input', show: false },
  {
    field: 'articleId',
    label: '文章',
    component: 'Select',
    required: true,
    componentProps: {
      options: [],
      showSearch: true,
      optionFilterProp: 'label',
      placeholder: '请选择文章',
    },
  },
  {
    field: 'channelId',
    label: '渠道',
    component: 'Select',
    required: true,
    componentProps: {
      options: [],
      showSearch: true,
      optionFilterProp: 'label',
      placeholder: '请选择渠道',
    },
  },
  {
    field: 'status',
    label: '状态',
    component: 'Select',
    defaultValue: 0,
    componentProps: {
      options: [
        { label: '排队中', value: 0 },
        { label: '发布中', value: 1 },
        { label: '成功', value: 2 },
        { label: '失败', value: 3 },
        { label: '需人工', value: 4 },
      ],
    },
  },
  { field: 'externalId', label: '外部ID', component: 'Input' },
  { field: 'externalUrl', label: '外部链接', component: 'Input' },
  { field: 'errorCode', label: '错误码', component: 'Input' },
  { field: 'errorMsg', label: '错误信息', component: 'InputTextArea' },
  { field: 'retryCount', label: '重试次数', component: 'InputNumber', defaultValue: 0 },
  { field: 'scheduledAt', label: '计划时间', component: 'DatePicker', componentProps: { showTime: true, valueFormat: 'YYYY-MM-DD HH:mm:ss' } },
  { field: 'publishedAt', label: '发布时间', component: 'DatePicker', componentProps: { showTime: true, valueFormat: 'YYYY-MM-DD HH:mm:ss' } },
];
