import { BasicColumn } from '/@/components/Table';
import { FormSchema } from '/@/components/Table';

export const columns: BasicColumn[] = [
  {
    title: '店铺名称',
    dataIndex: 'merchantName',
    width: 200,
    align: 'left',
  },
  {
    title: '分类',
    dataIndex: 'category',
    width: 120,
  },
  {
    title: '城市',
    dataIndex: 'city',
    width: 120,
  },
  {
    title: '地址',
    dataIndex: 'address',
    width: 260,
  },
  {
    title: '电话',
    dataIndex: 'phone',
    width: 140,
  },
  {
    title: '营业时间',
    dataIndex: 'openingHours',
    width: 180,
  },
  {
    title: '状态',
    dataIndex: 'status',
    width: 80,
  },
];

export const searchFormSchema: FormSchema[] = [
  {
    label: '店铺名称',
    field: 'merchantName',
    component: 'Input',
    colProps: { span: 8 },
  },
  {
    label: '城市',
    field: 'city',
    component: 'AutoComplete',
    colProps: { span: 8 },
  },
  {
    label: '分类',
    field: 'category',
    component: 'AutoComplete',
    colProps: { span: 8 },
  },
];

export const formSchema: FormSchema[] = [
  {
    field: 'id',
    label: 'id',
    component: 'Input',
    show: false,
  },
  {
    field: 'merchantName',
    label: '店铺名称',
    component: 'Input',
    required: true,
  },
  {
    field: 'aliases',
    label: '别名',
    component: 'Input',
  },
  {
    field: 'category',
    label: '分类',
    component: 'AutoComplete',
    componentProps: {
      allowClear: true,
      placeholder: '请选择或输入行业类目',
      options: [
        { value: '餐饮' },
        { value: '咖啡店' },
        { value: '餐厅' },
        { value: '美容美发' },
        { value: '健身' },
        { value: '教育培训' },
        { value: '医疗服务' },
        { value: '金融保险' },
        { value: '家政服务' },
        { value: '宠物服务' },
        { value: '酒店住宿' },
        { value: '汽车服务' },
        { value: '旅游景点' },
        { value: '其他' },
      ],
    },
  },
  {
    field: 'province',
    label: '省',
    component: 'Input',
    show: false,
  },
  {
    field: 'city',
    label: '市',
    component: 'Input',
    show: false,
  },
  {
    field: 'district',
    label: '区',
    component: 'Input',
    show: false,
  },
  {
    field: 'areaLinkage',
    label: '省市区',
    component: 'Input',
    slot: 'areaLinkage',
    required: true,
    rules: [{ required: true, message: '请选择省市区' }],
    componentProps: {
      placeholder: '请选择省市区',
    },
  },
  {
    field: 'address',
    label: '地址',
    component: 'InputTextArea',
  },
  {
    field: 'phone',
    label: '电话',
    component: 'Input',
  },
  {
    field: 'openingHours',
    label: '营业时间',
    component: 'Input',
  },
  {
    field: 'serviceArea',
    label: '服务范围',
    component: 'Input',
  },
  {
    field: 'website',
    label: '官网',
    component: 'Input',
  },
  {
    field: 'description',
    label: '简介',
    component: 'InputTextArea',
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
