package org.jeecg.modules.geo.publish;

import org.jeecg.modules.geo.entity.GeoArticle;
import org.jeecg.modules.geo.entity.GeoChannel;

/**
 * Publish adapter for a GEO channel.
 */
public interface GeoPublishAdapter {

    boolean supports(String platform, GeoChannel channel);

    GeoPublishResult publish(GeoArticle article, GeoChannel channel) throws Exception;

    default int getPriority() {
        return 0;
    }
}
