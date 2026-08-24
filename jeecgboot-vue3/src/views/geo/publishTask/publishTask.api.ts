import { defHttp } from '/@/utils/http/axios';
import { Modal } from 'ant-design-vue';

enum Api {
  list = '/geo/publishTask/list',
  save = '/geo/publishTask/add',
  edit = '/geo/publishTask/edit',
  queryById = '/geo/publishTask/queryById',
  execute = '/geo/publishTask/execute',
  createAndExecute = '/geo/publishTask/createAndExecute',
  deleteTask = '/geo/publishTask/delete',
  deleteBatch = '/geo/publishTask/deleteBatch',
}

export const getPublishTaskList = (params) => defHttp.get({ url: Api.list, params });
export const saveOrUpdatePublishTask = (params, isUpdate) => {
  const url = isUpdate ? Api.edit : Api.save;
  return defHttp.post({ url, params });
};
export const getPublishTaskById = (params) => defHttp.get({ url: Api.queryById, params });
export const createPublishTask = (params) => defHttp.post({ url: Api.save, params });
export const createAndExecutePublishTask = (params) => defHttp.post({ url: Api.createAndExecute, params });
export const executePublishTask = (params, handleSuccess) => {
  return defHttp.post({ url: Api.execute, params }).then(() => handleSuccess());
};
export const deletePublishTask = (params, handleSuccess) => {
  return defHttp.delete({ url: Api.deleteTask, params }, { joinParamsToUrl: true }).then(() => handleSuccess());
};
export const batchDeletePublishTask = (params, handleSuccess) => {
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
