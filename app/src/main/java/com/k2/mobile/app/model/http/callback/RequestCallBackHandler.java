/*
 * Copyright (c) 2015. OPPO Co., Ltd.
 */
package com.k2.mobile.app.model.http.callback;

public interface RequestCallBackHandler {
    /**
     * @param total
     * @param current
     * @param forceUpdateUI
     * @return continue
     */
    boolean updateProgress(long total, long current, boolean forceUpdateUI);
}
