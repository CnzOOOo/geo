import { BasicColumn } from '/@/components/Table';
import { FormSchema } from '/@/components/Table';

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
  { label: '分类', field: 'category', component: 'Input', colProps: { span: 8 } },
  { label: '事实', field: 'fact', component: 'Input', colProps: { span: 8 } },
  { label: '状态', field: 'status', component: 'InputNumber', colProps: { span: 8 } },
];

export const formSchema: FormSchema[] = [
  { field: 'id', label: 'id', component: 'Input', show: false },
  { field: 'merchantId', label: '商家ID', component: 'Input' },
  { field: 'category', label: '分类', component: 'Input', required: true },
  { field: 'fact', label: '事实', component: 'Input', required: true },
  { field: 'value', label: '值', component: 'InputTextArea', required: true },
  { field: 'sourceType', label: '来源类型', component: 'Input' },
  { field: 'sourceUrl', label: '来源链接', component: 'Input' },
  { field: 'ownerId', label: '负责人', component: 'Input' },
  { field: 'verifiedAt', label: '核验时间', component: 'DatePicker', componentProps: { showTime: true, valueFormat: 'YYYY-MM-DD HH:mm:ss' } },
  { field: 'validFrom', label: '生效时间', component: 'DatePicker', componentProps: { showTime: true, valueFormat: 'YYYY-MM-DD HH:mm:ss' } },
  { field: 'validTo', label: '失效时间', component: 'DatePicker', componentProps: { showTime: true, valueFormat: 'YYYY-MM-DD HH:mm:ss' } },
  { field: 'status', label: '状态', component: 'InputNumber', defaultValue: 0, componentProps: { min: 0, max: 3 } },
];
