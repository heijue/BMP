/*
 * Copyright (c) 2015. OPPO Co., Ltd.
 */
package com.k2.mobile.app.model.http.client.multipart.content;

import java.io.IOException;
import java.io.OutputStream;

import com.k2.mobile.app.model.http.client.multipart.MIME;

/**
 * Body part that is built using a byte array containing a file.
 *
 * @since 4.1
 */
public class ByteArrayBody extends AbstractContentBody {

    /**
     * The contents of the file contained in this part.
     */
    private final byte[] data;

    /**
     * The name of the file contained in this part.
     */
    private final String filename;

    /**
     * Creates a new ByteArrayBody.
     *
     * @param data     The contents of the file contained in this part.
     * @param mimeType The mime type of the file contained in this part.
     * @param filename The name of the file contained in this part.
     */
    public ByteArrayBody(final byte[] data, final String mimeType, final String filename) {
        super(mimeType);
        if (data == null) {
            throw new IllegalArgumentException("byte[] may not be null");
        }
        this.data = data;
        this.filename = filename;
    }

    /**
     * Creates a new ByteArrayBody.
     *
     * @param data     The contents of the file contained in this part.
     * @param filename The name of the file contained in this part.
     */
    public ByteArrayBody(final byte[] data, final String filename) {
        this(data, "application/octet-stream", filename);
    }

    @Override
	public String getFilename() {
        return filename;
    }

    @Override
	public void writeTo(final OutputStream out) throws IOException {
        out.write(data);
        callBackInfo.pos += data.length;
        callBackInfo.doCallBack(false);
    }

    @Override
	public String getCharset() {
        return null;
    }

    @Override
	public String getTransferEncoding() {
        return MIME.ENC_BINARY;
    }

    @Override
	public long getContentLength() {
        return data.length;
    }

}
