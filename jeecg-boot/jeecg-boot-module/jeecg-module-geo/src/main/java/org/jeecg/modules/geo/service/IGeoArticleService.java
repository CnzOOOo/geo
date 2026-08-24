package org.jeecg.modules.geo.service;

import org.jeecg.common.system.base.service.JeecgService;
import org.jeecg.modules.geo.entity.GeoArticle;
import org.jeecg.modules.geo.vo.GeoArticleGenerateRequest;

/**
 * GEO article service.
 */
public interface IGeoArticleService extends JeecgService<GeoArticle> {

    void submitForReview(String id);

    void publish(String id);

    void offline(String id);

    GeoArticle generateDraft(GeoArticleGenerateRequest request);
}
