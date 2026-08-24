import { BasicColumn } from '/@/components/Table';
import { FormSchema } from '/@/components/Table';

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
  { label: '状态', field: 'status', component: 'InputNumber', colProps: { span: 8 } },
];

export const formSchema: FormSchema[] = [
  { field: 'id', label: 'id', component: 'Input', show: false },
  { field: 'merchantId', label: '商家ID', component: 'Input', required: true },
  { field: 'name', label: '实验名称', component: 'Input', required: true },
  { field: 'controlGroupJson', label: '对照组 JSON', component: 'InputTextArea', componentProps: { rows: 6 } },
  { field: 'variantGroupJson', label: '实验组 JSON', component: 'InputTextArea', componentProps: { rows: 6 } },
  { field: 'status', label: '状态', component: 'InputNumber', defaultValue: 0 },
  { field: 'startedAt', label: '开始时间', component: 'DatePicker', componentProps: { showTime: true, valueFormat: 'YYYY-MM-DD HH:mm:ss' } },
  { field: 'endedAt', label: '结束时间', component: 'DatePicker', componentProps: { showTime: true, valueFormat: 'YYYY-MM-DD HH:mm:ss' } },
  { field: 'conclusion', label: '结论', component: 'InputTextArea' },
];
