/*
 * Copyright (c) 2015. OPPO Co., Ltd.
 */
package com.k2.mobile.app.utils;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore.MediaColumns;

import java.io.Closeable;
import java.io.InputStream;
import java.io.OutputStream;

public class IOUtil {

	private IOUtil() {
	}

	public static void closeQuietly(Closeable closeable) {
		if (closeable != null) {
			try {
				closeable.close();
			} catch (Throwable e) {
			}
		}
	}

	public static void closeQuietly(Cursor cursor) {
		if (cursor != null) {
			try {
				cursor.close();
			} catch (Throwable e) {
			}
		}
	}

	public static void CopyStream(InputStream is, OutputStream os) {
		final int buffer_size = 1024;
		try {
			byte[] bytes = new byte[buffer_size];
			for (;;) {
				int count = is.read(bytes, 0, buffer_size);
				if (count == -1)
					break;
				os.write(bytes, 0, count);
				is.close();
				os.close();
			}
		} catch (Exception ex) {
		}
	}
	
	// 将UIR转换成file的真实路径
	public static String getRealPathFromURI(Uri contentUri, Context context) {
	    String res = null;
	    String[] proj = { MediaColumns.DATA };
	    Cursor cursor = context.getContentResolver().query(contentUri, proj, null, null, null);
	    if(cursor.moveToFirst()){;
	       int column_index = cursor.getColumnIndexOrThrow(MediaColumns.DATA);
	       res = cursor.getString(column_index);
	    }
	    cursor.close();
	    return res;
	}
}
