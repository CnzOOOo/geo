package org.jeecg.modules.geo.publish;

import java.util.Set;

import org.jeecg.modules.geo.entity.GeoArticle;
import org.jeecg.modules.geo.entity.GeoChannel;
import org.springframework.stereotype.Component;

/**
 * Explicit manual adapters for platforms without a free general publish API.
 */
@Component
public class KnownPlatformManualPublishAdapter implements GeoPublishAdapter {

    private static final Set<String> PLATFORMS = Set.of(
            "douyin",
            "xiaohongshu",
            "zhihu",
            "wangyi",
            "163",
            "sohu",
            "baijiahao",
            "toutiao",
            "qiehao",
            "tencent_content",
            "sina",
            "meituan",
            "dianping",
            "baidu_map",
            "amap",
            "bilibili",
            "csdn",
            "jianshu"
    );

    @Override
    public boolean supports(String platform, GeoChannel channel) {
        return platform != null && PLATFORMS.contains(platform.toLowerCase());
    }

    @Override
    public int getPriority() {
        return 1;
    }

    @Override
    public GeoPublishResult publish(GeoArticle article, GeoChannel channel) {
        return GeoPublishResult.manual("平台暂未接入官方自动发布，请人工发布：" + channel.getPlatform());
    }
}
