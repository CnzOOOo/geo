import { BasicColumn } from '/@/components/Table';
import { FormSchema } from '/@/components/Table';
import { merchantSelectProps } from '../merchant/merchantSelect';

export const columns: BasicColumn[] = [
  { title: '实验名称', dataIndex: 'name', width: 220 },
  { title: '商家ID', dataIndex: 'merchantId', width: 220 },
  { title: '状态', dataIndex: 'status', width: 80 },
  { title: '开始时间', dataIndex: 'startedAt', width: 160 },
  { title: '结束时间', dataIndex: 'endedAt', width: 160 },
  { title: '结论', dataIndex: 'conclusion', width: 300, align: 'left' },
];

export const searchFormSchema: FormSchema[] = [
  { label: '实验名称', field: 'name', component: 'Input', colProps: { span: 8 } },
  {
    label: '状态',
    field: 'status',
    component: 'Select',
    colProps: { span: 8 },
    componentProps: {
      allowClear: true,
      options: [
        { label: '草稿', value: 0 },
        { label: '运行中', value: 1 },
        { label: '已完成', value: 2 },
        { label: '已停止', value: 3 },
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
  { field: 'name', label: '实验名称', component: 'Input', required: true },
  { field: 'controlGroupJson', label: '对照组 JSON', component: 'InputTextArea', componentProps: { rows: 6 } },
  { field: 'variantGroupJson', label: '实验组 JSON', component: 'InputTextArea', componentProps: { rows: 6 } },
  {
    field: 'status',
    label: '状态',
    component: 'Select',
    defaultValue: 0,
    componentProps: {
      options: [
        { label: '草稿', value: 0 },
        { label: '运行中', value: 1 },
        { label: '已完成', value: 2 },
        { label: '已停止', value: 3 },
      ],
    },
  },
  { field: 'startedAt', label: '开始时间', component: 'DatePicker', componentProps: { showTime: true, valueFormat: 'YYYY-MM-DD HH:mm:ss' } },
  { field: 'endedAt', label: '结束时间', component: 'DatePicker', componentProps: { showTime: true, valueFormat: 'YYYY-MM-DD HH:mm:ss' } },
  { field: 'conclusion', label: '结论', component: 'InputTextArea' },
];
