/*
 * Copyright (c) 2015. OPPO Co., Ltd.
 */
package com.k2.mobile.app.utils;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.provider.MediaStore.MediaColumns;
import android.util.Log;

/**
 * @Title CameraUtil.java
 * @Package com.oppo.mo.utils
 * @Description 摄像头工具类
 * @Company  K2
 * 
 * @author Jason.Wu
 * @date 2015-01-27 15:13:00
 * @version V1.0
 */
public final class CameraUtil {

	private static final String TAG = "CameraUtil";

	private static String filePath = null;

	private CameraUtil() {
		// can't be instantiated
	}

	public static boolean takePhoto(final Activity activity, final String dir,
			final String filename, final int cmd) {
		filePath = dir + filename;

		final Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		final File cameraDir = new File(dir);
		if (!cameraDir.exists()) {
			return false;
		}

		final File file = new File(filePath);
		final Uri outputFileUri = Uri.fromFile(file);
		intent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);
		try {
			activity.startActivityForResult(intent, cmd);

		} catch (final ActivityNotFoundException e) {
			return false;
		}
		return true;
	}

	
	
	public static String getResultPhotoPath(Context context,
			final Intent intent) {
		//Toast.makeText(context, "filepath:" + filePath + ",temp:",1).show();
		if (filePath != null && new File(filePath).exists()) {
			return filePath;
		}

		return resolvePhotoFromIntent(context, intent);
	}

	public static String resolvePhotoFromIntent(final Context ctx,
			final Intent data) {
		if (ctx == null || data == null) {
			Log.e(TAG, "resolvePhotoFromIntent fail, invalid argument");
			return null;
		}
		String filePath = null;

		final Uri uri = Uri.parse(data.toURI());

		Cursor cu = ctx.getContentResolver().query(uri, null, null, null, null);
		if (cu != null && cu.getCount() > 0) {
			try {
				cu.moveToFirst();
				final int pathIndex = cu.getColumnIndex(MediaColumns.DATA);
				Log.e(TAG, "orition: " + cu.getString(cu.getColumnIndex(MediaStore.Images.ImageColumns.ORIENTATION)));
				filePath = cu.getString(pathIndex);
				Log.d(TAG, "photo from resolver, path:" + filePath);

			} catch (Exception e) {
				e.printStackTrace();
			}

		} else if (data.getData() != null) {
			filePath = data.getData().getPath();
			if (!(new File(filePath)).exists()) {
				filePath = null;
			}
			Log.d(TAG, "photo file from data, path:" + filePath);

		} else if (data.getAction() != null
				&& data.getAction().equals("inline-data")) {

			try {

				final Bitmap bitmap = (Bitmap) data.getExtras().get("data");
				final File file = new File(filePath);
				if (!file.exists()) {
					file.createNewFile();
				}

				BufferedOutputStream out;
				out = new BufferedOutputStream(new FileOutputStream(file));
				final int cQuality = 100;
				bitmap.compress(Bitmap.CompressFormat.PNG, cQuality, out);
				out.close();
				Log.d(TAG, "photo image from data, path:" + filePath);

			} catch (final Exception e) {
				e.printStackTrace();
			}

		} else {
			if (cu != null) {
				cu.close();
				cu = null;
			}
			Log.e(TAG, "resolve photo from intent failed");
			return null;
		}
		if (cu != null) {
			cu.close();
			cu = null;
		}
		
		
		return filePath;
	}

	


	public static String getResultPhotoPath(Context context, final Intent intent, final String dir) {
		if (filePath != null && new File(filePath).exists()) {
			return filePath;
		}

		return resolvePhotoFromIntent(context, intent, dir);
	}

	public static String resolvePhotoFromIntent(final Context ctx, final Intent data, final String dir) {
		if (ctx == null || data == null || dir == null) {
			Log.e(TAG, "resolvePhotoFromIntent fail, invalid argument");
			return null;
		}

		String filePath = null;

		final Uri uri = Uri.parse(data.toURI());
		
		Cursor cu = ctx.getContentResolver().query(uri, null, null, null, null);
		if (cu != null && cu.getCount() > 0) {
			try {
				cu.moveToFirst();
				final int pathIndex = cu.getColumnIndex(MediaColumns.DATA);
				Log.e(TAG, "orition: " + cu.getString(cu.getColumnIndex(MediaStore.Images.ImageColumns.ORIENTATION)));
				filePath = cu.getString(pathIndex);
				Log.d(TAG, "photo from resolver, path:" + filePath);

			} catch (Exception e) {
				e.printStackTrace();
			}

		} else if (data.getData() != null) {
			filePath = data.getData().getPath();
			if (!(new File(filePath)).exists()) {
				filePath = null;
			}
			Log.d(TAG, "photo file from data, path:" + filePath);

		} else if (data.getAction() != null && data.getAction().equals("inline-data")) {

			try {
//				final String fileName = MD5.getMessageDigest(DateFormat.format("yyyy-MM-dd-HH-mm-ss", System.currentTimeMillis()).toString().getBytes()) + PHOTO_DEFAULT_EXT;
//				Date date = new Date(System.currentTimeMillis());
//				SimpleDateFormat dateFormat = new SimpleDateFormat(
//						"'IMG'_yyyy-MM-dd HH:mm:ss");
//				final String fileName = dateFormat.format(date) + ".jpg";
				final String fileName = CommonUtil.getImgNameInCurrentTimeMillis();
				filePath = dir + fileName;

				final Bitmap bitmap = (Bitmap) data.getExtras().get("data");
				final File file = new File(filePath);
				if (!file.exists()) {
					file.createNewFile();
				}

				BufferedOutputStream out;
				out = new BufferedOutputStream(new FileOutputStream(file));
				final int cQuality = 100;
				bitmap.compress(Bitmap.CompressFormat.PNG, cQuality, out);
				out.close();
				Log.d(TAG, "photo image from data, path:" + filePath);

			} catch (final Exception e) {
				e.printStackTrace();
			}

		} else {
			if (cu != null) {
				cu.close();
				cu = null;
			}
			Log.e(TAG, "resolve photo from intent failed");
			return null;
		}
		if (cu != null) {
			cu.close();
			cu = null;
		}
		return filePath;
	}
	
	
	
	 
	    private static int calculateInSampleSize(BitmapFactory.Options options,
	            int reqWidth, int reqHeight) {
	        final int height = options.outHeight;
	        final int width = options.outWidth;
	        int inSampleSize = 1;
	        if (height > reqHeight || width > reqWidth) {
	            final int halfHeight = height / 2;
	            final int halfWidth = width / 2;
	            while ((halfHeight / inSampleSize) > reqHeight
	                    && (halfWidth / inSampleSize) > reqWidth) {
	                inSampleSize *= 2;
	            }
	        }
	        return inSampleSize;
	    }
	    
	 // 如果是放大图片，filter决定是否平滑，如果是缩小图片，filter无影�?
	    private static Bitmap createScaleBitmap(Bitmap src, int dstWidth,
	            int dstHeight) {
	        Bitmap dst = Bitmap.createScaledBitmap(src, dstWidth, dstHeight, false);
	        if (src != dst) { // 如果没有缩放，那么不回收
	            src.recycle(); // 释放Bitmap的native像素数组
	        }
	        return dst;
	    }
	    
	    // 从Resources中加载图�?
	    public static Bitmap decodeSampledBitmapFromResource(Resources res,
	            int resId, int reqWidth, int reqHeight) {
	        final BitmapFactory.Options options = new BitmapFactory.Options();
	        options.inJustDecodeBounds = true;
	        BitmapFactory.decodeResource(res, resId, options); // 读取图片长款
	        options.inSampleSize = calculateInSampleSize(options, reqWidth,
	                reqHeight); // 计算inSampleSize
	        options.inJustDecodeBounds = false;
	        Bitmap src = BitmapFactory.decodeResource(res, resId, options); // 载入�?��稍大的缩略图
	        return createScaleBitmap(src, reqWidth, reqHeight); // 进一步得到目标大小的缩略�?
	    }
	 
	
	
	
	
	
	
	
	
	
	/****************************下面�?.3以上的系统�?择图片返回的路径**********************************************/
	
	/**
	 * 
	 * 
	 * @param context
	 * @param intent
	 * @return
	 */
	public static String getPath(final Context context, Intent intent) {
		final Uri uri = intent.getData();

		final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;
	
		// DocumentProvider
		// if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
		if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {//
			// ExternalStorageProvider
			if (isExternalStorageDocument(uri)) {
				final String docId = DocumentsContract.getDocumentId(uri);
				final String[] split = docId.split(":");
				final String type = split[0];

				if ("primary".equalsIgnoreCase(type)) {
					return Environment.getExternalStorageDirectory() + "/"
							+ split[1];
				}

				// TODO handle non-primary volumes
			}
			// DownloadsProvider
			else if (isDownloadsDocument(uri)) {

				final String id = DocumentsContract.getDocumentId(uri);
				final Uri contentUri = ContentUris.withAppendedId(
						Uri.parse("content://downloads/public_downloads"),
						Long.valueOf(id));

				return getDataColumn(context, contentUri, null, null);
			}
			// MediaProvider
			else if (isMediaDocument(uri)) {
				final String docId = DocumentsContract.getDocumentId(uri);
				final String[] split = docId.split(":");
				final String type = split[0];

				Uri contentUri = null;
				if ("image".equals(type)) {
					contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
				} else if ("video".equals(type)) {
					contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
				} else if ("audio".equals(type)) {
					contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
				}

				final String selection = "_id=?";
				final String[] selectionArgs = new String[] { split[1] };

				return getDataColumn(context, contentUri, selection,
						selectionArgs);
			}
		}
		// MediaStore (and general)
		else if ("content".equalsIgnoreCase(uri.getScheme())) {

			// Return the remote address
			if (isGooglePhotosUri(uri))
				return uri.getLastPathSegment();

			return getDataColumn(context, uri, null, null);
		}
		// File
		else if ("file".equalsIgnoreCase(uri.getScheme())) {
			return uri.getPath();
		} 
		/*
		 * 如果�?.4上面不用"图片"来�?,�?图库"来�?,就会无法读取到图片路�?�?��只需要加个判�?如果是用旧方式来�?
		 * 就用旧方式来�?就是如果 DocumentsContract.isDocumentUri(context, uri)
		 * 返回false的话,就用旧的方式
		 */
		else if(!DocumentsContract.isDocumentUri(context, uri)){
			
			return selectImage(context,intent);
		}

		return null;
	}

	/**
	 * Get the value of the data column for this Uri. This is useful for
	 * MediaStore Uris, and other file-based ContentProviders.
	 * 
	 * @param context
	 *            The context.
	 * @param uri
	 *            The Uri to query.
	 * @param selection
	 *            (Optional) Filter used in the query.
	 * @param selectionArgs
	 *            (Optional) Selection arguments used in the query.
	 * @return The value of the _data column, which is typically a file path.
	 */
	public static String getDataColumn(Context context, Uri uri,
			String selection, String[] selectionArgs) {

		Cursor cursor = null;
		final String column = "_data";
		final String[] projection = { column };

		try {
			cursor = context.getContentResolver().query(uri, projection,
					selection, selectionArgs, null);
			if (cursor != null && cursor.moveToFirst()) {
				final int index = cursor.getColumnIndexOrThrow(column);
				return cursor.getString(index);
			}
		} finally {
			if (cursor != null)
				cursor.close();
		}
		return null;
	}

	/**
	 * @param uri
	 *            The Uri to check.
	 * @return Whether the Uri authority is ExternalStorageProvider.
	 */
	public static boolean isExternalStorageDocument(Uri uri) {
		return "com.android.externalstorage.documents".equals(uri
				.getAuthority());
	}

	/**
	 * @param uri
	 *            The Uri to check.
	 * @return Whether the Uri authority is DownloadsProvider.
	 */
	public static boolean isDownloadsDocument(Uri uri) {
		return "com.android.providers.downloads.documents".equals(uri
				.getAuthority());
	}

	/**
	 * @param uri
	 *            The Uri to check.
	 * @return Whether the Uri authority is MediaProvider.
	 */
	public static boolean isMediaDocument(Uri uri) {
		return "com.android.providers.media.documents".equals(uri
				.getAuthority());
	}

	/**
	 * @param uri
	 *            The Uri to check.
	 * @return Whether the Uri authority is Google Photos.
	 */
	public static boolean isGooglePhotosUri(Uri uri) {
		return "com.google.android.apps.photos.content".equals(uri
				.getAuthority());
	}

	public static String selectImage(Context context, Intent data) {
		Uri selectedImage = data.getData();
		// Log.e(TAG, selectedImage.toString());
		if (selectedImage != null) {
			String uriStr = selectedImage.toString();
			String path = uriStr.substring(10, uriStr.length());
			if (path.startsWith("com.sec.android.gallery3d")) {
				Log.e(TAG,
						"It's auto backup pic path:" + selectedImage.toString());
				return null;
			}
		}
		String[] filePathColumn = { MediaColumns.DATA };
		Cursor cursor = context.getContentResolver().query(selectedImage,
				filePathColumn, null, null, null);
		cursor.moveToFirst();
		int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
		String picturePath = cursor.getString(columnIndex);
		cursor.close();
		return picturePath;
	}

}
