import { BasicColumn } from '/@/components/Table';
import { FormSchema } from '/@/components/Table';

export const columns: BasicColumn[] = [
  { title: '任务名称', dataIndex: 'name', width: 200 },
  { title: '商家ID', dataIndex: 'merchantId', width: 220 },
  { title: '频率', dataIndex: 'cadence', width: 100 },
  { title: '启用', dataIndex: 'enabled', width: 80 },
  { title: '状态', dataIndex: 'status', width: 80 },
  { title: '上次运行', dataIndex: 'lastRunAt', width: 160 },
];

export const searchFormSchema: FormSchema[] = [
  { label: '任务名称', field: 'name', component: 'Input', colProps: { span: 8 } },
  { label: '启用', field: 'enabled', component: 'InputNumber', colProps: { span: 8 } },
];

export const formSchema: FormSchema[] = [
  { field: 'id', label: 'id', component: 'Input', show: false },
  { field: 'merchantId', label: '商家ID', component: 'Input', required: true },
  { field: 'name', label: '任务名称', component: 'Input', required: true },
  { field: 'querySetJson', label: '查询集 JSON', component: 'InputTextArea', componentProps: { rows: 6 } },
  { field: 'engineConfigJson', label: '引擎配置 JSON', component: 'InputTextArea', componentProps: { rows: 6 } },
  { field: 'cadence', label: '频率', component: 'Input' },
  { field: 'enabled', label: '启用', component: 'InputNumber', defaultValue: 0, componentProps: { min: 0, max: 1 } },
  { field: 'status', label: '状态', component: 'InputNumber', defaultValue: 0 },
];
