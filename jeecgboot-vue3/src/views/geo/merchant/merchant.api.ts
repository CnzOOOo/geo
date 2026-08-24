import { defHttp } from '/@/utils/http/axios';
import { Modal } from 'ant-design-vue';

enum Api {
  list = '/geo/merchant/list',
  save = '/geo/merchant/add',
  edit = '/geo/merchant/edit',
  queryById = '/geo/merchant/queryById',
  deleteMerchant = '/geo/merchant/delete',
  deleteBatch = '/geo/merchant/deleteBatch',
}

export const getMerchantList = (params) => defHttp.get({ url: Api.list, params });

export const saveOrUpdateMerchant = (params, isUpdate) => {
  const url = isUpdate ? Api.edit : Api.save;
  return defHttp.post({ url, params });
};

export const getMerchantById = (params) => defHttp.get({ url: Api.queryById, params });

export const deleteMerchant = (params, handleSuccess) => {
  return defHttp.delete({ url: Api.deleteMerchant, params }, { joinParamsToUrl: true }).then(() => {
    handleSuccess();
  });
};

export const batchDeleteMerchant = (params, handleSuccess) => {
  Modal.confirm({
    title: '确认删除',
    content: '是否删除选中的数据？',
    okText: '确认',
    cancelText: '取消',
    onOk: () => {
      return defHttp.delete({ url: Api.deleteBatch, data: params }, { joinParamsToUrl: true }).then(() => {
        handleSuccess();
      });
    },
  });
};
