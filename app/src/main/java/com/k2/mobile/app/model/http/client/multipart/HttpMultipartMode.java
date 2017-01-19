/*
 * Copyright (c) 2015. OPPO Co., Ltd.
 */
package com.k2.mobile.app.model.http.client.multipart;

/**
 * @since 4.0
 */
public enum HttpMultipartMode {

    /**
     * RFC 822, RFC 2045, RFC 2046 compliant
     */
    STRICT,
    /**
     * browser-compatible mode, i.e. only write Content-Disposition; use content charset
     */
    BROWSER_COMPATIBLE

}
