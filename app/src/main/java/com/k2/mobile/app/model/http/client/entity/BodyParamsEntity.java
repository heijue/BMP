/*
 * Copyright (c) 2015. OPPO Co., Ltd.
 */
package com.k2.mobile.app.model.http.client.entity;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.entity.AbstractHttpEntity;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;

import com.k2.mobile.app.model.http.client.util.URLEncodedUtils;
import com.k2.mobile.app.utils.LogUtil;

/**
 * Author: wyouflf
 * Date: 13-7-26
 * Time: 下午4:21
 */
public class BodyParamsEntity extends AbstractHttpEntity implements Cloneable {

    protected byte[] content;

    private boolean dirty = true;

    private String charset = HTTP.UTF_8;

    private List<NameValuePair> params;

    public BodyParamsEntity() {
        this((String) null);
    }

    public BodyParamsEntity(String charset) {
        super();
        if (charset != null) {
            this.charset = charset;
        }
        setContentType(URLEncodedUtils.CONTENT_TYPE);
        params = new ArrayList<NameValuePair>();
    }

    public BodyParamsEntity(List<NameValuePair> params) {
        this(params, null);
    }

    public BodyParamsEntity(List<NameValuePair> params, String charset) {
        super();
        if (charset != null) {
            this.charset = charset;
        }
        setContentType(URLEncodedUtils.CONTENT_TYPE);
        this.params = params;
        refreshContent();
    }

    public BodyParamsEntity addParameter(String name, String value) {
        this.params.add(new BasicNameValuePair(name, value));
        this.dirty = true;
        return this;
    }

    public BodyParamsEntity addParams(List<NameValuePair> params) {
        this.params.addAll(params);
        this.dirty = true;
        return this;
    }

    private void refreshContent() {
        if (dirty) {
            try {
                this.content = URLEncodedUtils.format(params, charset).getBytes(charset);
            } catch (UnsupportedEncodingException e) {
                LogUtil.e(e.getMessage(), e);
            }
            dirty = false;
        }
    }

    @Override
	public boolean isRepeatable() {
        return true;
    }

    @Override
	public long getContentLength() {
        refreshContent();
        return this.content.length;
    }

    @Override
	public InputStream getContent() throws IOException {
        refreshContent();
        return new ByteArrayInputStream(this.content);
    }

    @Override
	public void writeTo(final OutputStream outStream) throws IOException {
        if (outStream == null) {
            throw new IllegalArgumentException("Output stream may not be null");
        }
        refreshContent();
        outStream.write(this.content);
        outStream.flush();
    }

    /**
     * Tells that this entity is not streaming.
     *
     * @return <code>false</code>
     */
    @Override
	public boolean isStreaming() {
        return false;
    }

    @Override
	public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

}
