/*
 * Copyright (c) 2015. OPPO Co., Ltd.
 */
package com.k2.mobile.app.model.http.client.entity;

import org.apache.http.Header;
import org.apache.http.HttpEntity;

import java.io.IOException;
import java.io.InputStream;
import java.util.zip.GZIPInputStream;


public class GZipDecompressingEntity extends DecompressingEntity {

    /**
     * Creates a new {@link GZipDecompressingEntity} which will wrap the specified
     * {@link org.apache.http.HttpEntity}.
     *
     * @param entity the non-null {@link org.apache.http.HttpEntity} to be wrapped
     */
    public GZipDecompressingEntity(final HttpEntity entity) {
        super(entity);
    }

    @Override
    InputStream decorate(final InputStream wrapped) throws IOException {
        return new GZIPInputStream(wrapped);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Header getContentEncoding() {

        /* This HttpEntityWrapper has dealt with the Content-Encoding. */
        return null;
    }
}
