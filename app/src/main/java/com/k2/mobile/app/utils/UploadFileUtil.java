package com.k2.mobile.app.utils;    

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
/**
 * @Title UploadFileUtil.java
 * @Package com.oppo.mo.utils
 * @Description 文件上传
 * @Company  K2
 * 
 * @ClassName: UploadFileUtil
 * @author linqijun
 * @date 2015-4-3 上午10:09:00
 * 
 */
public class UploadFileUtil {

	/**
	 * @param params
	 *            传递的普通参数
	 * @param uploadFile
	 *            需要上传的文件名
	 * @param fileFormName
	 *            需要上传文件表单中的名字
	 * @param newFileName
	 *            上传的文件名称，不填写将为uploadFile的名称
	 * @param urlStr
	 *            上传的服务器的路径
	 * @throws IOException
	 * 说明，使用上传文件上传须加如下代码
	 StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
		 .detectDiskReads()
		 .detectDiskWrites()
		 .detectNetwork() // 这里可以替换为detectAll() 就包括了磁盘读写和网络I/O
		 .penaltyLog()  
		 .build());
		 StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
		 .detectLeakedSqlLiteObjects() //探测SQLite数据库操作
		 .penaltyLog() //打印logcat
		 .penaltyDeath()
		 .build()); 
	 */
	public static void uploadForm(String params, String fileFormName, File uploadFile, String newFileName, String urlStr, Handler mHandler)
			throws IOException {
		if (newFileName == null || newFileName.trim().equals("")) {
			newFileName = uploadFile.getName();
		}

		StringBuilder sb = new StringBuilder();
		/*
		 * 上传文件的头
		 */
		sb.append("------WebKitFormBoundaryT1HoybnYeFOGFlBR\r\n");
		sb.append("Content-Disposition: form-data; name=\"" + fileFormName + "\"; filename=\"" + newFileName + "\"" + "\r\n");
		//		sb.append("Content-Type: image/jpeg" + "\r\n");// 如果服务器端有文件类型的校验，必须明确指定ContentType
		sb.append("Content-Type: text/html" + "\r\n");// 如果服务器端有文件类型的校验，必须明确指定ContentType
		sb.append("\r\n");

		params = toURLEncoded(params);

		String urls = urlStr + "?data="+params;

		byte[] headerInfo = sb.toString().getBytes("UTF-8");
		byte[] endInfo = ("\r\n------WebKitFormBoundaryT1HoybnYeFOGFlBR--\r\n").getBytes("UTF-8");

		URL url = new URL(urls);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setConnectTimeout(10000);
		conn.setReadTimeout(8000);		
		conn.setDoOutput(true);
		conn.setRequestMethod("POST");
		conn.setRequestProperty("Content-Type","multipart/form-data; boundary=" + "----WebKitFormBoundaryT1HoybnYeFOGFlBR");
		conn.setRequestProperty("Content-Length", String.valueOf(headerInfo.length + uploadFile.length() + endInfo.length));

		OutputStream out = conn.getOutputStream();
		InputStream in = new FileInputStream(uploadFile);
		out.write(headerInfo);

		byte[] buf = new byte[1024];
		int len;
		while ((len = in.read(buf)) != -1){
			out.write(buf, 0, len);
		}
		out.write(endInfo);
		in.close();
		out.close();

		Message msg = new Message();
		if (conn.getResponseCode() == 200) {
			//			System.out.println("上传成功");
			// 获取响应的输入流对象  
			InputStream is = conn.getInputStream();  
			// 创建字节输出流对象  
			ByteArrayOutputStream baos = new ByteArrayOutputStream();  
			// 定义读取的长度  
			int lens = 0;  
			// 定义缓冲区  
			byte buffer[] = new byte[1024];  
			// 按照缓冲区的大小，循环读取  
			while ((lens = is.read(buffer)) != -1) {  
				// 根据读取的长度写入到os对象中  
				baos.write(buffer, 0, lens);  
			}  
			// 释放资源  
			is.close();  
			baos.close();  
			// 返回字符串  
			final String result = new String(baos.toByteArray());  
			msg.obj = result;
		}

		mHandler.sendMessage(msg);
	}
	/**
	 * 加载远程图片
	 * @param url
	 *            远程图片URL
	 * @throws IOException
	 */
	public static Bitmap loadImageFromUrl(String url) throws Exception  {

		final DefaultHttpClient client = new DefaultHttpClient();
		final HttpGet getRequest = new HttpGet(url);

		HttpResponse response = client.execute(getRequest);
		int statusCode = response.getStatusLine().getStatusCode();
		if (statusCode != HttpStatus.SC_OK)  {
		}

		HttpEntity entity = response.getEntity();
		if (entity == null) {
		}
		InputStream is = null;
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		try {
			is = entity.getContent();
			byte[] buf = new byte[1024];
			int readBytes = -1;
			while ((readBytes = is.read(buf)) != -1) {
				baos.write(buf, 0, readBytes);
			}
		} finally {
			if (baos != null) {
				baos.close();
			}
			if (is != null) {
				is.close();
			}
		}

		byte[] imageArray = baos.toByteArray();
		// 解决加载图片 内存溢出的问题  
		// Options 只保存图片尺寸大小，不保存图片到内存
		BitmapFactory.Options opts = new BitmapFactory.Options();  
		// 缩放的比例，缩放是很难按准备的比例进行缩放的，其值表明缩放的倍数，SDK中建议其值是2的指数值,值越大会导致图片不清晰  
		opts.inSampleSize = 2;  

		return BitmapFactory.decodeByteArray(imageArray, 0, imageArray.length, opts);
	}


	/**
	 * 获取图片的byte[]
	 */
	public static byte[] getByteFromUrl(String url) throws Exception  {

		final DefaultHttpClient client = new DefaultHttpClient();
		final HttpGet getRequest = new HttpGet(url);

		HttpResponse response = client.execute(getRequest);
		int statusCode = response.getStatusLine().getStatusCode();
		if (statusCode != HttpStatus.SC_OK)  {
		}

		HttpEntity entity = response.getEntity();
		if (entity == null) {
		}
		InputStream is = null;
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		try {
			is = entity.getContent();
			byte[] buf = new byte[1024];
			int readBytes = -1;
			while ((readBytes = is.read(buf)) != -1) {
				baos.write(buf, 0, readBytes);
			}
		} finally {
			if (baos != null) {
				baos.close();
			}
			if (is != null) {
				is.close();
			}
		}

		byte[] imageArray = baos.toByteArray();
		return imageArray;
	}

	/**byte2file*/
	public static void saveFile(byte[] content,String path) {
		File file = new File(path);
		if (!file.exists()) {
			try {
				boolean status = file.createNewFile();
				System.out.println("is create new file :" + status);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		FileOutputStream fos = null;
		try {
			fos = new FileOutputStream(file);
			fos.write(content);
			fos.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static String svaeFile(byte[] imageArray, String localFilePath, String fileName) {
		File mDirs = new File(localFilePath);
		String imagePath = localFilePath + fileName;
		if(mDirs.mkdir()){
			System.out.println("is create new dir");
		}
		File file = new File(imagePath);
		if(!file.exists()) {
			try {
				boolean status = file.createNewFile();
				System.out.println("is create new file :" + status);
			} catch (IOException e) {
				e.printStackTrace();
				return null;
			}
		}
		
		FileOutputStream fos = null;
		try {
			fos = new FileOutputStream(file);
			fos.write(imageArray);
			fos.flush();
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
		return imagePath;
	}  



	// 编码转换方式
	public static  String toURLEncoded(String paramString) {  
		if (paramString == null || paramString.equals("")) {  
			return "";  
		}  

		try{  
			String str = new String(paramString.getBytes(), "UTF-8");  
			str = URLEncoder.encode(str, "UTF-8");  
			return str;  
		}catch (Exception localException){  
			localException.printStackTrace();
		}  

		return "";  
	}

}
