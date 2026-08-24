import { defHttp } from '/@/utils/http/axios';
import { Modal } from 'ant-design-vue';

enum Api {
  list = '/geo/monitorTask/list',
  save = '/geo/monitorTask/add',
  edit = '/geo/monitorTask/edit',
  queryById = '/geo/monitorTask/queryById',
  runNow = '/geo/monitorTask/runNow',
  deleteTask = '/geo/monitorTask/delete',
  deleteBatch = '/geo/monitorTask/deleteBatch',
}

export const getMonitorTaskList = (params) => defHttp.get({ url: Api.list, params });
export const saveOrUpdateMonitorTask = (params, isUpdate) => {
  const url = isUpdate ? Api.edit : Api.save;
  return defHttp.post({ url, params });
};
export const getMonitorTaskById = (params) => defHttp.get({ url: Api.queryById, params });
export const runMonitorTask = (params, handleSuccess) => {
  return defHttp.post({ url: Api.runNow, params }).then(() => handleSuccess());
};
export const deleteMonitorTask = (params, handleSuccess) => {
  return defHttp.delete({ url: Api.deleteTask, params }, { joinParamsToUrl: true }).then(() => handleSuccess());
};
export const batchDeleteMonitorTask = (params, handleSuccess) => {
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
