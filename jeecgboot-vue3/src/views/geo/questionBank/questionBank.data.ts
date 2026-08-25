import { BasicColumn } from '/@/components/Table';
import { FormSchema } from '/@/components/Table';
import { merchantSelectProps } from '../merchant/merchantSelect';

export const columns: BasicColumn[] = [
  { title: '问题', dataIndex: 'question', width: 320, align: 'left' },
  { title: '类型', dataIndex: 'questionType', width: 100 },
  { title: '意图', dataIndex: 'intent', width: 160 },
  { title: '区域', dataIndex: 'region', width: 120 },
  { title: '优先级', dataIndex: 'priority', width: 80 },
  { title: '来源', dataIndex: 'source', width: 160 },
  { title: '状态', dataIndex: 'status', width: 80 },
];

export const searchFormSchema: FormSchema[] = [
  { label: '问题', field: 'question', component: 'Input', colProps: { span: 8 } },
  {
    label: '类型',
    field: 'questionType',
    component: 'Select',
    colProps: { span: 8 },
    componentProps: {
      allowClear: true,
      options: [
        { label: 'BRAND 品牌词', value: 'BRAND' },
        { label: 'HOW 怎么做/是否', value: 'HOW' },
        { label: 'WHICH 推荐', value: 'WHICH' },
        { label: 'COMPARE 对比', value: 'COMPARE' },
        { label: 'AVOID 避坑', value: 'AVOID' },
        { label: 'FAQ 常见问题', value: 'FAQ' },
      ],
    },
  },
  { label: '区域', field: 'region', component: 'AutoComplete', colProps: { span: 8 } },
];

export const formSchema: FormSchema[] = [
  { field: 'id', label: 'id', component: 'Input', show: false },
  {
    field: 'merchantId',
    label: '商家',
    component: 'ApiSelect',
    required: true,
    componentProps: merchantSelectProps,
  },
  {
    field: 'questionType',
    label: '问题类型',
    component: 'Select',
    required: true,
    componentProps: {
      options: [
        { label: 'BRAND 品牌词', value: 'BRAND' },
        { label: 'HOW 怎么做/是否', value: 'HOW' },
        { label: 'WHICH 推荐', value: 'WHICH' },
        { label: 'COMPARE 对比', value: 'COMPARE' },
        { label: 'AVOID 避坑', value: 'AVOID' },
        { label: 'FAQ 常见问题', value: 'FAQ' },
      ],
    },
  },
  { field: 'question', label: '完整问题', component: 'InputTextArea', required: true },
  {
    field: 'intent',
    label: '意图',
    component: 'AutoComplete',
    componentProps: {
      allowClear: true,
      options: [
        { value: '找店' },
        { value: '比价' },
        { value: '避坑' },
        { value: '营业信息' },
        { value: '地址电话' },
        { value: '预约' },
        { value: '场景推荐' },
        { value: '其他' },
      ],
    },
  },
  {
    field: 'region',
    label: '区域',
    component: 'AutoComplete',
    componentProps: {
      allowClear: true,
      placeholder: '例如：上海/徐汇区',
    },
  },
  { field: 'priority', label: '优先级', component: 'InputNumber', defaultValue: 3, componentProps: { min: 1, max: 5 } },
  {
    field: 'source',
    label: '来源',
    component: 'AutoComplete',
    componentProps: {
      allowClear: true,
      options: [
        { value: '人工整理' },
        { value: '用户反馈' },
        { value: 'AI 搜索' },
        { value: '行业报告' },
        { value: '客服记录' },
        { value: '其他' },
      ],
    },
  },
  {
    field: 'status',
    label: '状态',
    component: 'Select',
    defaultValue: 0,
    componentProps: {
      options: [
        { label: '草稿', value: 0 },
        { label: '启用', value: 1 },
        { label: '停用', value: 2 },
      ],
    },
  },
];
