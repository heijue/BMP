/*
 * Copyright (c) 2015. OPPO Co., Ltd.
 */
package com.k2.mobile.app.model.http.client.multipart.content;

import java.io.IOException;
import java.io.OutputStream;

import com.k2.mobile.app.model.http.client.multipart.MultipartEntity;

/**
 * @since 4.0
 */
public interface ContentBody extends ContentDescriptor {

    String getFilename();

    void writeTo(OutputStream out) throws IOException;

    void setCallBackInfo(MultipartEntity.CallBackInfo callBackInfo);

}
