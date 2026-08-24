package org.jeecg.modules.geo.publish;

import lombok.Data;

/**
 * Result of a publish action.
 */
@Data
public class GeoPublishResult {

    private boolean success;

    private String externalId;

    private String externalUrl;

    private String errorCode;

    private String errorMsg;

    public static GeoPublishResult ok(String externalId, String externalUrl) {
        GeoPublishResult result = new GeoPublishResult();
        result.setSuccess(true);
        result.setExternalId(externalId);
        result.setExternalUrl(externalUrl);
        return result;
    }

    public static GeoPublishResult manual(String message) {
        GeoPublishResult result = new GeoPublishResult();
        result.setSuccess(false);
        result.setErrorCode("MANUAL_REQUIRED");
        result.setErrorMsg(message);
        return result;
    }

    public static GeoPublishResult fail(String errorCode, String errorMsg) {
        GeoPublishResult result = new GeoPublishResult();
        result.setSuccess(false);
        result.setErrorCode(errorCode);
        result.setErrorMsg(errorMsg);
        return result;
    }
}
