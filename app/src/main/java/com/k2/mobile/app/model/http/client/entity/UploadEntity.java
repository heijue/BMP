/*
 * Copyright (c) 2015. OPPO Co., Ltd.
 */
package com.k2.mobile.app.model.http.client.entity;

import com.k2.mobile.app.model.http.callback.RequestCallBackHandler;

/**
 * Created with IntelliJ IDEA.
 * User: wyouflf
 * Date: 13-7-3
 * Time: 下午1:40
 */
public interface UploadEntity {
    void setCallBackHandler(RequestCallBackHandler callBackHandler);
}
