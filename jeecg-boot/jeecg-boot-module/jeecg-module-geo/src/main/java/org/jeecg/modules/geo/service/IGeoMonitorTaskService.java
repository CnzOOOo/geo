package org.jeecg.modules.geo.service;

import org.jeecg.common.system.base.service.JeecgService;
import org.jeecg.modules.geo.entity.GeoMonitorTask;

/**
 * GEO monitor task service.
 */
public interface IGeoMonitorTaskService extends JeecgService<GeoMonitorTask> {

    GeoMonitorTask runNow(String id);
}
