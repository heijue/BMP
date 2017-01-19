/*
 * Copyright (c) 2015. OPPO Co., Ltd.
 */
package com.k2.mobile.app.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.SoftReference;
import java.text.DecimalFormat;
import java.util.LinkedHashMap;

import com.k2.mobile.app.R;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentUris;
import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;

/**
 * @Title ImageUtil.java
 * @Package com.oppo.mo.utils
 * @Description 图片工具类
 * @Company  K2
 * 
 * @author Jason.Wu
 * @date 2015-01-27 15:13:00
 * @version V1.0
 */
public class ImageUtil {
	private static final String TAG = "ImageUtil";
	private static int DayCount = 15;// 天数
	private static final long CLEARTIME = DayCount * 24 * 60 * 60 * 1000;

	public static int imageHeight = 200;
	public static int imageWidth = 200;

	/**
	 * 默认图片
	 */
	private final static int Default_Img = R.drawable.ic_launcher;

	private static Object lock = new Object();

	/**
	 * 内存图片软引用缓�?
	 */
	private static LinkedHashMap<String, SoftReference> imageCache = new LinkedHashMap<String, SoftReference>(
			20);

	/**
	 * 获得程序在sd�?��的cahce目录
	 * 
	 * @param context
	 *            The context to use
	 * @return The external cache dir
	 */
	public static String getExternalCacheDir(Context context) {
		// android 2.2 以后才支持的特�?
		if (hasExternalCacheDir()) {
			return context.getExternalCacheDir().getPath() + File.separator
					+ "img";
		}

		// Before Froyo we need to construct the external cache dir ourselves
		// 2.2以前我们�?��自己构�?
		final String cacheDir = "/Android/data/" + context.getPackageName()
		+ "/cache/img/";
		return Environment.getExternalStorageDirectory().getPath() + cacheDir;
	}

	public static boolean hasExternalCacheDir() {
		return Build.VERSION.SDK_INT >= Build.VERSION_CODES.FROYO;
	}

	/**
	 * 保存图片到SD�?
	 * 
	 * @param imagePath
	 * @param buffer
	 * @throws IOException
	 */
	public static void saveImage(String imagePath, byte[] buffer)
			throws IOException {
		File f = new File(imagePath);
		if (f.exists()) {
			return;
		} else {
			File parentFile = f.getParentFile();
			if (!parentFile.exists()) {
				parentFile.mkdirs();
			}
			f.createNewFile();
			FileOutputStream fos = new FileOutputStream(imagePath);
			fos.write(buffer);
			fos.flush();
			fos.close();
		}
	}

	/**
	 * 保存图片到缓�?
	 * 
	 * @param imagePath
	 * @param bm
	 */
	public static void saveImage(String imagePath, Bitmap bm) {

		if (bm == null || imagePath == null || "".equals(imagePath)) {
			return;
		}

		File f = new File(imagePath);
		if (f.exists()) {
			return;
		} else {
			try {
				File parentFile = f.getParentFile();
				if (!parentFile.exists()) {
					parentFile.mkdirs();
				}
				f.createNewFile();
				FileOutputStream fos;
				fos = new FileOutputStream(f);
				bm.compress(Bitmap.CompressFormat.PNG, 100, fos);
				fos.close();
			} catch (FileNotFoundException e) {
				f.delete();
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
				f.delete();
			}
		}
	}

	/**
	 * 从SD卡加载图�?
	 * 
	 * @param imagePath
	 * @return
	 */
	public static Bitmap getImageFromLocal(String imagePath) {
		File file = new File(imagePath);
		if (file.exists()) {
			Bitmap bitmap = BitmapFactory.decodeFile(imagePath);
			file.setLastModified(System.currentTimeMillis());
			return bitmap;
		}
		return null;
	}

	/**
	 * 从本地文件中删除文件
	 * 
	 * @param imagePath
	 */
	private static void deleteImageFromLocal(String imagePath) {
		File file = new File(imagePath);
		if (file.exists()) {
			file.delete();
		}
	}


	// ///////////////////////////////////////////////////////////////////////
	// 公共方法

	public interface ImageCallback {
		public void loadImage(Bitmap bitmap, String imagePath);
	}

	/**
	 * 每次打开含有大量图片的activity�?�?��个新线程,�?��并清理缓�?
	 * 
	 * @param context
	 */
	public static void checkCache(final Context context) {
		new Thread() {
			@Override
			public void run() {
				int state = 0;// 记录清除结果 0为都没清�? 1为只清除了sd�? 2为只清除了rom Cache ,3
				// 为都清除�?
				String cacheS = "0M";
				String cacheD = "0M";
				File sdCache = new File(getExternalCacheDir(context)); 
				File cacheDir = context.getCacheDir(); 
				try {
					if (sdCache != null && sdCache.exists()) {
						long sdFileSize = getFileSize(sdCache);
						if (sdFileSize > 1024 * 1024 * 50) {
							// SD�?��清理
							long clearFileSize = clear(sdCache);
							state += 1;
							cacheS = clearFileSize + "";
						}
					}
					if (cacheDir != null && cacheDir.exists()) {
						long cacheFileSize = getFileSize(cacheDir);
						if (cacheFileSize > 1024 * 1024 * 50) {
							// ROM�?��清理
							long clearFileSize = clear(cacheDir);
							state += 2;
							cacheD = clearFileSize + "";
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			};
		}.start();
	}

	/**
	 * 清除路径
	 * 
	 * @param cacheDir
	 * @return
	 */
	public static long clear(File cacheDir) {
		long clearFileSize = 0;
		File[] files = cacheDir.listFiles();
		if (files == null)
			return 0;
		for (File f : files) {
			if (f.isFile()) {
				if (System.currentTimeMillis() - f.lastModified() > CLEARTIME) {
					long fileSize = f.length();
					if (f.delete()) {
						clearFileSize += fileSize;
					}
				}
			} else {
				clear(f);
			}
		}
		return clearFileSize;
	}

	/**
	 * 取得文件大小
	 * 
	 * @param f
	 * @return
	 * @throws Exception
	 */
	public static long getFileSize(File f) throws Exception {
		long size = 0;
		File flist[] = f.listFiles();
		for (int i = 0; i < flist.length; i++) {
			if (flist[i].isDirectory()) {
				size = size + getFileSize(flist[i]);
			} else {
				size = size + flist[i].length();
			}
		}
		return size;
	}

	/**
	 * 转换文件大小
	 * 
	 * @param fileS
	 * @return
	 */
	public static String FormetFileSize(long fileS) {
		DecimalFormat df = new DecimalFormat("#.00");
		String fileSizeString = "";
		if (fileS < 1024) {
			fileSizeString = df.format((double) fileS) + "B";
		} else if (fileS < 1048576) {
			fileSizeString = df.format((double) fileS / 1024) + "K";
		} else if (fileS < 1073741824) {
			fileSizeString = df.format((double) fileS / 1048576) + "M";
		} else {
			fileSizeString = df.format((double) fileS / 1073741824) + "G";
		}
		return fileSizeString;
	}

	/**
	 * 转换图片尺寸
	 * 
	 * @param Resources res
	 * @param int drawable
	 * @param double scale
	 * @return
	 */
	public static Bitmap rescaleImage(Resources res, int drawable, double scale) {
		/* 取得屏幕分辨率大小 
		DisplayMetrics dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		 */

		/* 初始化相关变量 */
		Bitmap bmp=BitmapFactory.decodeResource(res, drawable);
		int bmpWidth=bmp.getWidth(); 
		int bmpHeight=bmp.getHeight(); 

		float scaleWidth=1; 
		float scaleHeight=1; 

		/* 计算出这次要缩小的比例 */
		scaleWidth = (float) (scaleWidth*scale); 
		scaleHeight = (float) (scaleHeight*scale);

		/* 产生reSize后的Bitmap对象 */
		Matrix matrix = new Matrix();
		matrix.postScale(scaleWidth, scaleHeight); 

		Bitmap resizeBmp = Bitmap.createBitmap(bmp, 0, 0, bmpWidth, bmpHeight,matrix,true);

		return resizeBmp;
	}

	/**
	 * 以最省内存的方式读取本地资源的图片
	 * @param context
	 *@param resId
	 * @return
	 */
	public static Bitmap readBitMap(Context context, int resId) {
		Bitmap bitmap = null;
		InputStream is = null;
		try {
			BitmapFactory.Options opt = new BitmapFactory.Options();
			opt.inPreferredConfig = Bitmap.Config.RGB_565;
			opt.inPurgeable = true;
			opt.inInputShareable = true;
			opt.inSampleSize = 2;
			// 获取资源图片 
			is = context.getResources().openRawResource(resId);
			bitmap = BitmapFactory.decodeStream(is, null, opt);
		} catch(Exception e) {
			//			LogUtil.i(TAG, e.getMessage());
		} finally {
			try {
				if(is != null) {
					is.close();
					is = null;
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				//				LogUtil.i(TAG, e.getMessage());
			}
		}
		return bitmap;
	}

	public static Bitmap decodeSampledBitmapFromResource(Resources res, int resId,
			int reqWidth, int reqHeight) {
		// 第一次解析将inJustDecodeBounds设置为true，来获取图片大小
		final BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeResource(res, resId, options);
		// 调用上面定义的方法计算inSampleSize值
		options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
		// 使用获取到的inSampleSize值再次解析图片
		options.inJustDecodeBounds = false;
		return BitmapFactory.decodeResource(res, resId, options);
	}

	public static int calculateInSampleSize(BitmapFactory.Options options,
			int reqWidth, int reqHeight) {
		// 源图片的高度和宽度
		final int height = options.outHeight;
		final int width = options.outWidth;
		int inSampleSize = 1;
		if (height > reqHeight || width > reqWidth) {
			// 计算出实际宽高和目标宽高的比率
			final int heightRatio = Math.round((float) height / (float) reqHeight);
			final int widthRatio = Math.round((float) width / (float) reqWidth);
			// 选择宽和高中最小的比率作为inSampleSize的值，这样可以保证最终图片的宽和高
			// 一定都会大于等于目标的宽和高。
			inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
		}
		return inSampleSize;
	}

	//	public static Bitmap readBitMap(Context context, int resId) {
	////		ParcelFileDescriptor pfd;
	////        try
	////        {
	////            pfd = context.getContentResolver().openFileDescriptor(uri, "r");
	////        } catch (IOException ex)
	////        {
	////            return null;
	////        }
	////        java.io.FileDescriptor fd = pfd.getFileDescriptor();
	//		InputStream is = null;// 获取资源图片
	//		is = context.getResources().openRawResource(resId);
	//		
	//        BitmapFactory.Options options = new BitmapFactory.Options();
	//        //先指定原始大小   
	//        options.inSampleSize = 1;
	//        //只进行大小判断   
	//        options.inJustDecodeBounds = true;
	//        //调用此方法得到options得到图片的大小   
	//        BitmapFactory.decodeFileDescriptor(fd, null, options);
	//        //BitmapFactory.decodeStream(is, null, options);
	//        //我们的目标是在800pixel的画面上显示。   
	//        //所以需要调用computeSampleSize得到图片缩放的比例   
	//        options.inSampleSize = computeSampleSize(options, 800);
	//        //OK,我们得到了缩放的比例，现在开始正式读入BitMap数据   
	//        options.inJustDecodeBounds = false;
	//        options.inDither = false;
	//        options.inPreferredConfig = Bitmap.Config.ARGB_8888;
	//
	//        //根据options参数，减少所需要的内存   
	//        //        Bitmap sourceBitmap = BitmapFactory.decodeFileDescriptor(fd, null, options);
	//        Bitmap sourceBitmap = BitmapFactory.decodeStream(is, null, options);
	//        return sourceBitmap;
	//	}

	/**
	 * 这个函数会对图片的大小进行判断，并得到合适的缩放比例，比如2即1/2,3即1/3   
	 * */
	public static int computeSampleSize(BitmapFactory.Options options, int target)
	{
		int w = options.outWidth;
		int h = options.outHeight;
		int candidateW = w / target;
		int candidateH = h / target;
		int candidate = Math.max(candidateW, candidateH);
		if (candidate == 0)
			return 1;
		if (candidate > 1)
		{
			if ((w > target) && (w / candidate) < target)
				candidate -= 1;
		}
		if (candidate > 1)
		{
			if ((h > target) && (h / candidate) < target)
				candidate -= 1;
		}
		//if (VERBOSE)
		Log.v(TAG, "for w/h " + w + "/" + h + " returning " + candidate + "(" + (w / candidate) + " / " + (h / candidate));
		return candidate;
	}

	/**
	 * 获取默认图片保存路径
	 * @param context
	 * @return
	 */
	public static String getImgPath(Context context) {
		//		LogUtil.i("TAG", "getImgPath(): "+context.getFilesDir() + File.separator + "bestway_logo.png");
		return "bestway_logo.png";//getFilesDir() + File.separator + "bestway_logo.png";
		//		return getFilesDir() + File.separator + "bestway_logo_" + getAccount() + ".png";
	}

	public static String getMyImgPath(Context context) {
		//		LogUtil.i("TAG", "getImgPath(): "+context.getFilesDir() + File.separator + "bestway_logo.png");
		return "bestway_clock.png";//getFilesDir() + File.separator + "bestway_logo.png";
		//		return getFilesDir() + File.separator + "bestway_logo_" + getAccount() + ".png";
	}

	/**
	 * 从/data/读取图片
	 * @param url
	 * @return
	 */
	public static Bitmap getLoacalBitmap(Context context, String url)  {       
		FileInputStream fis  = null;
		try {  
			//把图片文件打开为文件流，然后解码为bitmap    
			File file = new File(url);
			if(!file.exists()) {
				return null;
			}
			fis = new FileInputStream(url);  
			Bitmap bitmap = BitmapFactory.decodeStream(fis);
			return bitmap;         
		}         
		catch (FileNotFoundException e)         
		{             
			e.printStackTrace();       
			return null;              
		} finally {
			try {
				if(fis!= null) {
					fis.close();
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	/**
	 * 获取bitmap大小
	 * @param bitmap
	 * @return
	 */
	public static long getBitmapsize(Bitmap bitmap){

		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR1) {
			return bitmap.getByteCount();
		} 
		// Pre HC-MR1
		return bitmap.getRowBytes() * bitmap.getHeight();
	}

	/**
	 * 将字符串转换成Bitmap类型
	 * 
	 * @param string
	 * @return
	 */
	public static Bitmap stringToBitmap(String string) {
		Bitmap bitmap = null;
		try {
			byte[] bitmapArray;
			bitmapArray = Base64.decode(string, Base64.DEFAULT);
			bitmap = BitmapFactory.decodeByteArray(bitmapArray, 0,
					bitmapArray.length);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return bitmap;
	}

	/**
	 * 将Bitmap转换成字符串
	 * 
	 * @param bitmap
	 * @return
	 */
	public static String bitmapToString(Bitmap bitmap) {

		String string = null;
		ByteArrayOutputStream bStream = new ByteArrayOutputStream();
		bitmap.compress(CompressFormat.PNG, 100, bStream);
		byte[] bytes = bStream.toByteArray();
		string = Base64.encodeToString(bytes, Base64.DEFAULT);
		return string;
	}

	/**
	 * 将图片压缩保存
	 * 
	 * @param srcPath file 文件 
	 * @param bitmap 位图
	 * @param size 大小
	 * @return Bitmap 压缩后的位图
	 */
	public static void saveBitmap(File file , Bitmap bitmap, int size) throws IOException{
		FileOutputStream out;
		try{
			Bitmap bmp = compressImage(bitmap, size);
			out = new FileOutputStream(file);
			if(bmp.compress(Bitmap.CompressFormat.JPEG, 100, out)){
				out.flush();
				out.close();
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace(); 
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/**
	 * 将图片压缩
	 * 
	 * @param srcPath 文件路径
	 * @return Bitmap 压缩后的位图
	 */
	public static Bitmap compressImageFromFile(String srcPath) {
		BitmapFactory.Options newOpts = new BitmapFactory.Options();
		newOpts.inJustDecodeBounds = true;//只读边,不读内容
		Bitmap bitmap = BitmapFactory.decodeFile(srcPath, newOpts);

		newOpts.inJustDecodeBounds = false;
		int w = newOpts.outWidth;
		int h = newOpts.outHeight;
		float hh = 800f;//
		float ww = 480f;//
		int be = 1;
		if (w > h && w > ww) {
			be = (int) (newOpts.outWidth / ww);
		} else if (w < h && h > hh) {
			be = (int) (newOpts.outHeight / hh);
		}
		if (be <= 0)
			be = 1;

		newOpts.inSampleSize = be;						// 设置采样率
		newOpts.inPreferredConfig = Config.ARGB_8888;	// 该模式是默认的,可不设
		newOpts.inPurgeable = true;						// 同时设置才会有效
		newOpts.inInputShareable = true;				// 当系统内存不够时候图片自动被回收

		bitmap = BitmapFactory.decodeFile(srcPath, newOpts);

		return bitmap;
	}
	/**
	 * 将位图压缩到指定大小以下
	 * 
	 * @param bitmap
	 * @return
	 */
	public static Bitmap compressImage(Bitmap image, int size) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		image.compress(Bitmap.CompressFormat.JPEG, 100, baos);						// 质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
		int options = 100;
		while ( baos.toByteArray().length / 1024 > size) {    						// 循环判断如果压缩后图片是否大于指定大小,大于继续压缩        
			baos.reset();															// 重置baos即清空baos
			options -= 10;															// 每次都减少10
			image.compress(Bitmap.CompressFormat.JPEG, options, baos);				// 这里压缩options%，把压缩后的数据存放到baos中
		}

		ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());	// 把压缩后的数据baos存放到ByteArrayInputStream中
		Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);			 	// 把ByteArrayInputStream数据生成图片

		return bitmap;
	}

	/** 
	 * 功能简述:4.4及以上获取图片的方法
	 * @param context 上下文
	 * @param uri 路径
	 * @return 图片路径
	 */
	@SuppressLint("NewApi")
	public static String getPath(final Context context, final Uri uri) {
		final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;
		// DocumentProvider
		if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
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
		return null;
	}

	public static String getDataColumn(Context context, Uri uri, String selection, String[] selectionArgs) {
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

	public static boolean isExternalStorageDocument(Uri uri) {
		return "com.android.externalstorage.documents".equals(uri.getAuthority());
	}

	public static boolean isDownloadsDocument(Uri uri) {
		return "com.android.providers.downloads.documents".equals(uri.getAuthority());
	}

	public static boolean isMediaDocument(Uri uri) {
		return "com.android.providers.media.documents".equals(uri.getAuthority());
	}

	private static boolean isGooglePhotosUri(Uri uri) {
		return "com.google.android.apps.photos.content".equals(uri.getAuthority());
	}

	public static Bitmap getBigBitmapForDisplay(String imagePath,
			Context context) {

		if (null == imagePath || !new File(imagePath).exists())
			return null;
		try {
			int degeree = PhotoHelper.readPictureDegree(imagePath);
			Bitmap bitmap = BitmapFactory.decodeFile(imagePath);
			if (bitmap == null)
				return null;
			DisplayMetrics dm = new DisplayMetrics();
			((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(dm);
			float scale = bitmap.getWidth() / (float) dm.widthPixels;
			Bitmap newBitMap = null;
			if (scale > 1) {
				newBitMap = zoomBitmap(bitmap, (int) (bitmap.getWidth() / scale), (int) (bitmap.getHeight() / scale));
				bitmap.recycle();
				Bitmap resultBitmap = PhotoHelper.rotaingImageView(degeree, newBitMap);
				return resultBitmap;
			}
			Bitmap resultBitmap = PhotoHelper.rotaingImageView(degeree, bitmap);
			return resultBitmap;
		} catch (Exception e) {
			return null;
		}
	}

	private static Bitmap zoomBitmap(Bitmap bitmap, int width, int height) {
		if (null == bitmap) {
			return null;
		}
		try {
			int w = bitmap.getWidth();
			int h = bitmap.getHeight();
			Matrix matrix = new Matrix();
			float scaleWidth = ((float) width / w);
			float scaleHeight = ((float) height / h);
			matrix.postScale(scaleWidth, scaleHeight);
			Bitmap newbmp = Bitmap.createBitmap(bitmap, 0, 0, w, h, matrix, true);
			return newbmp;
		} catch (Exception e) {
			return null;
		}
	}

}