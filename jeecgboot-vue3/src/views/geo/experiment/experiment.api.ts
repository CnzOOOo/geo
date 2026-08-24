import { defHttp } from '/@/utils/http/axios';
import { Modal } from 'ant-design-vue';

enum Api {
  list = '/geo/experiment/list',
  save = '/geo/experiment/add',
  edit = '/geo/experiment/edit',
  queryById = '/geo/experiment/queryById',
  deleteExperiment = '/geo/experiment/delete',
  deleteBatch = '/geo/experiment/deleteBatch',
}

export const getExperimentList = (params) => defHttp.get({ url: Api.list, params });
export const saveOrUpdateExperiment = (params, isUpdate) => {
  const url = isUpdate ? Api.edit : Api.save;
  return defHttp.post({ url, params });
};
export const getExperimentById = (params) => defHttp.get({ url: Api.queryById, params });
export const deleteExperiment = (params, handleSuccess) => {
  return defHttp.delete({ url: Api.deleteExperiment, params }, { joinParamsToUrl: true }).then(() => handleSuccess());
};
export const batchDeleteExperiment = (params, handleSuccess) => {
  Modal.confirm({
    title: '确认删除',
    content: '是否删除选中的数据？',
    okText: '确认',
    cancelText: '取消',
    onOk: () => {
      return defHttp.delete({ url: Api.deleteBatch, data: params }, { joinParamsToUrl: true }).then(() => handleSuccess());
    },
  });
};
