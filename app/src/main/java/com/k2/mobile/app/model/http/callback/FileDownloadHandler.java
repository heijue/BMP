/*
 * Copyright (c) 2015. OPPO Co., Ltd.
 */
package com.k2.mobile.app.model.http.callback;

import android.text.TextUtils;

import com.k2.mobile.app.utils.IOUtil;

import org.apache.http.HttpEntity;

import java.io.*;

public class FileDownloadHandler {

    public File handleEntity(HttpEntity entity,
                             RequestCallBackHandler callBackHandler,
                             String target,
                             boolean isResume,
                             String responseFileName) throws IOException {
        if (entity == null || TextUtils.isEmpty(target)) {
            return null;
        }

        File targetFile = new File(target);

        if (!targetFile.exists()) {
            File dir = targetFile.getParentFile();
            if (dir.exists() || dir.mkdirs()) {
                targetFile.createNewFile();
            }
        }

        long current = 0;
        BufferedInputStream bis = null;
        BufferedOutputStream bos = null;
        try {
            FileOutputStream fileOutputStream = null;
            if (isResume) {
                current = targetFile.length();
                fileOutputStream = new FileOutputStream(target, true);
            } else {
                fileOutputStream = new FileOutputStream(target);
            }
            long total = entity.getContentLength() + current;
            bis = new BufferedInputStream(entity.getContent());
            bos = new BufferedOutputStream(fileOutputStream);

            if (callBackHandler != null && !callBackHandler.updateProgress(total, current, true)) {
                return targetFile;
            }

            byte[] tmp = new byte[4096];
            int len;
            while ((len = bis.read(tmp)) != -1) {
                bos.write(tmp, 0, len);
                current += len;
                if (callBackHandler != null) {
                    if (!callBackHandler.updateProgress(total, current, false)) {
                        return targetFile;
                    }
                }
            }
            bos.flush();
            if (callBackHandler != null) {
                callBackHandler.updateProgress(total, current, true);
            }
        } finally {
            IOUtil.closeQuietly(bis);
            IOUtil.closeQuietly(bos);
        }

        if (targetFile.exists() && !TextUtils.isEmpty(responseFileName)) {
            File newFile = new File(targetFile.getParent(), responseFileName);
            while (newFile.exists()) {
                newFile = new File(targetFile.getParent(), System.currentTimeMillis() + responseFileName);
            }
            return targetFile.renameTo(newFile) ? newFile : targetFile;
        } else {
            return targetFile;
        }
    }

}
