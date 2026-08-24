import { BasicColumn } from '/@/components/Table';
import { FormSchema } from '/@/components/Table';

export const columns: BasicColumn[] = [
  { title: '引擎', dataIndex: 'engine', width: 120 },
  { title: '问题', dataIndex: 'query', width: 260 },
  { title: '是否提到', dataIndex: 'mentioned', width: 90 },
  { title: '位置', dataIndex: 'position', width: 80 },
  { title: '准确率', dataIndex: 'accuracyScore', width: 100 },
  { title: '情绪', dataIndex: 'sentiment', width: 100 },
  { title: '时间', dataIndex: 'occurredAt', width: 160 },
];

export const searchFormSchema: FormSchema[] = [
  { label: '引擎', field: 'engine', component: 'Input', colProps: { span: 8 } },
  { label: '问题', field: 'query', component: 'Input', colProps: { span: 8 } },
  { label: '是否提到', field: 'mentioned', component: 'InputNumber', colProps: { span: 8 } },
];

export const formSchema: FormSchema[] = [
  { field: 'id', label: 'id', component: 'Input', show: false },
  { field: 'monitorTaskId', label: '监测任务ID', component: 'Input' },
  { field: 'merchantId', label: '商家ID', component: 'Input' },
  { field: 'engine', label: '引擎', component: 'Input' },
  { field: 'query', label: '问题', component: 'InputTextArea' },
  { field: 'occurredAt', label: '时间', component: 'DatePicker', componentProps: { showTime: true, valueFormat: 'YYYY-MM-DD HH:mm:ss' } },
  { field: 'answerText', label: '回答内容', component: 'InputTextArea', componentProps: { rows: 8 } },
  { field: 'mentioned', label: '是否提到', component: 'InputNumber', defaultValue: 0, componentProps: { min: 0, max: 1 } },
  { field: 'position', label: '位置', component: 'InputNumber' },
  { field: 'sourceUrlsJson', label: '来源链接 JSON', component: 'InputTextArea' },
  { field: 'accuracyScore', label: '准确率', component: 'InputNumber' },
  { field: 'sentiment', label: '情绪', component: 'Input' },
  { field: 'rawJson', label: '原始 JSON', component: 'InputTextArea' },
];
