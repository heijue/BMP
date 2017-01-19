/*
 * Copyright (c) 2015. OPPO Co., Ltd.
 */
package com.k2.mobile.app.model.http.client.multipart;

import org.apache.http.protocol.HTTP;

import java.nio.charset.Charset;

/**
 * @ClassName: MIME
 * @Description: MIME实体类
 * @author Jason.Wu
 * @date 2015-02-05 10:01:03
 * @since 4.0
 */
public class MIME {

    public static final String CONTENT_TYPE = "Content-Type";
    public static final String CONTENT_TRANSFER_ENC = "Content-Transfer-Encoding";
    public static final String CONTENT_DISPOSITION = "Content-Disposition";

    public static final String ENC_8BIT = "8bit";
    public static final String ENC_BINARY = "binary";

    /**
     * The default character set to be used, i.e. "UTF-8"
     */
    public static final Charset DEFAULT_CHARSET = Charset.forName(HTTP.UTF_8);

}
