import { BasicColumn } from '/@/components/Table';
import { FormSchema } from '/@/components/Table';

export const columns: BasicColumn[] = [
  { title: '平台', dataIndex: 'platform', width: 120 },
  { title: '事件类型', dataIndex: 'eventType', width: 120 },
  { title: '标题', dataIndex: 'title', width: 240 },
  { title: '情绪', dataIndex: 'sentiment', width: 100 },
  { title: '严重度', dataIndex: 'severity', width: 80 },
  { title: '状态', dataIndex: 'status', width: 80 },
  { title: '负责人', dataIndex: 'ownerId', width: 120 },
];

export const searchFormSchema: FormSchema[] = [
  { label: '平台', field: 'platform', component: 'Input', colProps: { span: 8 } },
  { label: '情绪', field: 'sentiment', component: 'Input', colProps: { span: 8 } },
  { label: '状态', field: 'status', component: 'InputNumber', colProps: { span: 8 } },
];

export const formSchema: FormSchema[] = [
  { field: 'id', label: 'id', component: 'Input', show: false },
  { field: 'merchantId', label: '商家ID', component: 'Input', required: true },
  { field: 'platform', label: '平台', component: 'Input' },
  { field: 'eventType', label: '事件类型', component: 'Input' },
  { field: 'title', label: '标题', component: 'Input' },
  { field: 'content', label: '内容', component: 'InputTextArea' },
  { field: 'sentiment', label: '情绪', component: 'Input' },
  { field: 'severity', label: '严重度', component: 'InputNumber', defaultValue: 0 },
  { field: 'status', label: '状态', component: 'InputNumber', defaultValue: 0 },
  { field: 'ownerId', label: '负责人', component: 'Input' },
  { field: 'sourceUrl', label: '来源链接', component: 'Input' },
];
