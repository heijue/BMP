/*
 * Copyright (c) 2015. OPPO Co., Ltd.
 */
package com.k2.mobile.app.model.http.callback;

import com.k2.mobile.app.utils.IOUtil;
import com.k2.mobile.app.utils.OtherUtil;

import org.apache.http.HttpEntity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class StringDownloadHandler {

    public String handleEntity(HttpEntity entity, RequestCallBackHandler callBackHandler, String charset) throws IOException {
        if (entity == null) return null;

        long current = 0;
        long total = entity.getContentLength();

        if (callBackHandler != null && !callBackHandler.updateProgress(total, current, true)) {
            return null;
        }

        InputStream inputStream = null;
        StringBuilder sb = new StringBuilder();
        try {
            inputStream = entity.getContent();
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, charset));
            String line = "";
            while ((line = reader.readLine()) != null) {
                sb.append(line).append('\n');
                current += OtherUtil.sizeOfString(line, charset);
                if (callBackHandler != null) {
                    if (!callBackHandler.updateProgress(total, current, false)) {
                        break;
                    }
                }
            }
            if (callBackHandler != null) {
                callBackHandler.updateProgress(total, current, true);
            }
        } finally {
            IOUtil.closeQuietly(inputStream);
        }
        return sb.toString().trim();
    }

}
