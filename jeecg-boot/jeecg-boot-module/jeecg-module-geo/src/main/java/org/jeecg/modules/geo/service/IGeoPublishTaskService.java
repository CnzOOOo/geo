package org.jeecg.modules.geo.service;

import org.jeecg.common.system.base.service.JeecgService;
import org.jeecg.modules.geo.entity.GeoPublishTask;

/**
 * GEO publish task service.
 */
public interface IGeoPublishTaskService extends JeecgService<GeoPublishTask> {

    GeoPublishTask execute(String id);

    boolean updatePublishResult(GeoPublishTask task);
}
