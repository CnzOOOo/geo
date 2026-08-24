import { defHttp } from '/@/utils/http/axios';

enum Api {
  status = '/geo/wechatsync/status',
  install = '/geo/wechatsync/install',
  checkUpdate = '/geo/wechatsync/check-update',
  update = '/geo/wechatsync/update',
  pluginInfo = '/geo/wechatsync/plugin-info',
  platformStatus = '/geo/wechatsync/platform-status',
}

export const getWechatsyncStatus = () => defHttp.get({ url: Api.status });

export const installWechatsyncCli = () => defHttp.post({ url: Api.install });

export const checkWechatsyncUpdate = () => defHttp.get({ url: Api.checkUpdate });

export const updateWechatsyncCli = () => defHttp.post({ url: Api.update });

export const getWechatsyncPluginInfo = () => defHttp.get({ url: Api.pluginInfo });

export const getWechatsyncPlatformStatus = () => defHttp.get({ url: Api.platformStatus });
