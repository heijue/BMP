/*
 * Copyright (c) 2015. OPPO Co., Ltd.
 */
package com.k2.mobile.app.model.http.client.multipart.content;

import java.io.IOException;
import java.io.InputStream;
import java.io.InterruptedIOException;
import java.io.OutputStream;

import com.k2.mobile.app.model.http.client.multipart.MIME;
import com.k2.mobile.app.utils.IOUtil;

/**
 * @since 4.0
 */
public class InputStreamBody extends AbstractContentBody {

    private final InputStream in;
    private final String filename;
    private long length;

    public InputStreamBody(final InputStream in, long length, final String filename, final String mimeType) {
        super(mimeType);
        if (in == null) {
            throw new IllegalArgumentException("Input stream may not be null");
        }
        this.in = in;
        this.filename = filename;
        this.length = length;
    }

    public InputStreamBody(final InputStream in, long length, final String filename) {
        this(in, length, filename, "application/octet-stream");
    }

    public InputStreamBody(final InputStream in, long length) {
        this(in, length, "no_name", "application/octet-stream");
    }

    public InputStream getInputStream() {
        return this.in;
    }

    @Override
	public void writeTo(final OutputStream out) throws IOException {
        if (out == null) {
            throw new IllegalArgumentException("Output stream may not be null");
        }
        try {
            byte[] tmp = new byte[4096];
            int l;
            while ((l = this.in.read(tmp)) != -1) {
                out.write(tmp, 0, l);
                callBackInfo.pos += l;
                if (!callBackInfo.doCallBack(false)) {
                    throw new InterruptedIOException("cancel");
                }
            }
            out.flush();
        } finally {
            IOUtil.closeQuietly(this.in);
        }
    }

    @Override
	public String getTransferEncoding() {
        return MIME.ENC_BINARY;
    }

    @Override
	public String getCharset() {
        return null;
    }

    @Override
	public long getContentLength() {
        return this.length;
    }

    @Override
	public String getFilename() {
        return this.filename;
    }

}
