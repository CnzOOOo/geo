import { FormSchema } from '/@/components/Table';
import { getChannelList } from '../channel/channel.api';

export const formSchema: FormSchema[] = [
  {
    field: 'articleTitle',
    label: '文章标题',
    component: 'Input',
    componentProps: { disabled: true },
  },
  {
    field: 'articleId',
    label: '文章ID',
    component: 'Input',
    componentProps: { disabled: true },
  },
  {
    field: 'channelIds',
    label: '发布渠道',
    component: 'ApiSelect',
    required: true,
    componentProps: {
      api: getChannelList,
      mode: 'multiple',
      labelField: 'channelName',
      valueField: 'id',
      params: { pageNo: 1, pageSize: 100, enabled: 1, status: 1 },
      pageConfig: { isPage: true, pageField: 'pageNo', pageSizeField: 'pageSize', totalField: 'total', listField: 'records' },
    },
  },
  {
    field: 'immediateExecute',
    label: '创建后立即执行',
    component: 'Switch',
    defaultValue: true,
  },
];
