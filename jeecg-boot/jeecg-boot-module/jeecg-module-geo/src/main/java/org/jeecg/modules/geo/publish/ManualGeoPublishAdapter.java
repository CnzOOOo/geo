package org.jeecg.modules.geo.publish;

import org.jeecg.modules.geo.entity.GeoArticle;
import org.jeecg.modules.geo.entity.GeoChannel;
import org.springframework.stereotype.Component;

/**
 * Fallback adapter that requires manual publishing.
 */
@Component
public class ManualGeoPublishAdapter implements GeoPublishAdapter {

    @Override
    public boolean supports(String platform, GeoChannel channel) {
        return true;
    }

    @Override
    public GeoPublishResult publish(GeoArticle article, GeoChannel channel) {
        return GeoPublishResult.manual("请到平台后台人工发布：" + channel.getPlatform());
    }
}
