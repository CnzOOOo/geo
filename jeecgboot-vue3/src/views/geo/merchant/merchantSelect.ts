import { getMerchantList } from './merchant.api';

export const merchantSelectProps = {
  api: getMerchantList,
  labelField: 'merchantName',
  valueField: 'id',
  params: { pageNo: 1, pageSize: 500 },
  pageConfig: {
    isPage: true,
    pageField: 'pageNo',
    pageSizeField: 'pageSize',
    totalField: 'total',
    listField: 'records',
  },
  showSearch: true,
  allowClear: true,
  placeholder: '请选择商家',
  optionFilterProp: 'label',
};
