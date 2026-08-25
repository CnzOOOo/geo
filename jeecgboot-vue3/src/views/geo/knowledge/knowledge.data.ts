import { BasicColumn } from '/@/components/Table';
import { FormSchema } from '/@/components/Table';
import { merchantSelectProps } from '../merchant/merchantSelect';

export const columns: BasicColumn[] = [
  { title: '分类', dataIndex: 'category', width: 120 },
  { title: '事实', dataIndex: 'fact', width: 200 },
  { title: '值', dataIndex: 'value', width: 300, align: 'left' },
  { title: '来源', dataIndex: 'sourceType', width: 120 },
  { title: '来源链接', dataIndex: 'sourceUrl', width: 260 },
  { title: '核验时间', dataIndex: 'verifiedAt', width: 160 },
  { title: '状态', dataIndex: 'status', width: 80 },
];

export const searchFormSchema: FormSchema[] = [
  {
    label: '分类',
    field: 'category',
    component: 'Select',
    colProps: { span: 8 },
    componentProps: {
      allowClear: true,
      options: [
        { label: '基础事实', value: '基础事实' },
        { label: '可信证据', value: '可信证据' },
        { label: '差异事实', value: '差异事实' },
        { label: '其他', value: '其他' },
      ],
    },
  },
  { label: '事实', field: 'fact', component: 'Input', colProps: { span: 8 } },
  {
    label: '状态',
    field: 'status',
    component: 'Select',
    colProps: { span: 8 },
    componentProps: {
      allowClear: true,
      options: [
        { label: '草稿', value: 0 },
        { label: '已验证', value: 1 },
        { label: '已过期', value: 2 },
        { label: '已停用', value: 3 },
      ],
    },
  },
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
    field: 'category',
    label: '分类',
    component: 'Select',
    required: true,
    componentProps: {
      allowClear: true,
      options: [
        { label: '基础事实', value: '基础事实' },
        { label: '可信证据', value: '可信证据' },
        { label: '差异事实', value: '差异事实' },
        { label: '其他', value: '其他' },
      ],
    },
  },
  { field: 'fact', label: '事实', component: 'Input', required: true },
  { field: 'value', label: '值', component: 'InputTextArea', required: true },
  {
    field: 'sourceType',
    label: '来源类型',
    component: 'AutoComplete',
    componentProps: {
      allowClear: true,
      options: [
        { value: '官网' },
        { value: '地图' },
        { value: '点评' },
        { value: '媒体报道' },
        { value: '用户评价' },
        { value: '问答平台' },
        { value: '论坛' },
        { value: '其他' },
      ],
    },
  },
  { field: 'sourceUrl', label: '来源链接', component: 'Input' },
  { field: 'ownerId', label: '负责人', component: 'Input' },
  { field: 'verifiedAt', label: '核验时间', component: 'DatePicker', componentProps: { showTime: true, valueFormat: 'YYYY-MM-DD HH:mm:ss' } },
  { field: 'validFrom', label: '生效时间', component: 'DatePicker', componentProps: { showTime: true, valueFormat: 'YYYY-MM-DD HH:mm:ss' } },
  { field: 'validTo', label: '失效时间', component: 'DatePicker', componentProps: { showTime: true, valueFormat: 'YYYY-MM-DD HH:mm:ss' } },
  {
    field: 'status',
    label: '状态',
    component: 'Select',
    defaultValue: 0,
    componentProps: {
      options: [
        { label: '草稿', value: 0 },
        { label: '已验证', value: 1 },
        { label: '已过期', value: 2 },
        { label: '已停用', value: 3 },
      ],
    },
  },
];
