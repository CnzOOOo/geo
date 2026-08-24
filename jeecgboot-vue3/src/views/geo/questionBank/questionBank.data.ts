import { BasicColumn } from '/@/components/Table';
import { FormSchema } from '/@/components/Table';

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
  { label: '类型', field: 'questionType', component: 'Input', colProps: { span: 8 } },
  { label: '区域', field: 'region', component: 'Input', colProps: { span: 8 } },
];

export const formSchema: FormSchema[] = [
  { field: 'id', label: 'id', component: 'Input', show: false },
  { field: 'merchantId', label: '商家ID', component: 'Input' },
  { field: 'questionType', label: '问题类型', component: 'Input', required: true },
  { field: 'question', label: '完整问题', component: 'InputTextArea', required: true },
  { field: 'intent', label: '意图', component: 'Input' },
  { field: 'region', label: '区域', component: 'Input' },
  { field: 'priority', label: '优先级', component: 'InputNumber', defaultValue: 3, componentProps: { min: 1, max: 5 } },
  { field: 'source', label: '来源', component: 'Input' },
  { field: 'status', label: '状态', component: 'InputNumber', defaultValue: 0, componentProps: { min: 0, max: 2 } },
];
