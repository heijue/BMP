/*
 * Copyright (c) 2015. OPPO Co., Ltd.
 */
package com.k2.mobile.app.model.http.other;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;







import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.protocol.HTTP;
import android.os.Handler;
import android.os.Message;
import android.os.StrictMode;

import com.k2.mobile.app.common.config.HttpConstants;
import com.k2.mobile.app.common.config.LocalConstants;
import com.k2.mobile.app.common.exception.DataException;
import com.k2.mobile.app.common.exception.HttpException;
import com.k2.mobile.app.utils.EncryptUtil;
import com.k2.mobile.app.utils.LogUtil;

/**
 * @Title ConnectionUtil.java
 * @Package com.oppo.mo.model.task
 * @Description 网络HTTP交互工具类
 * @Company  K2
 * 
 * @author Jason.Wu
 * @date 2015-01-27 15:13:00
 * @version V1.0
 */
public class ConnectionUtil {

	public static String TAG = "ConnectionUtil";
	
	/**
	 * Android在4.0之前的版本支持在线程中访问网络， 但是在4.0版本以后对这部份程序进行了优化， 也就是说访问网络的代码不能写在主线程中了。
	 */
	public static void connection() {

		try {
			StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
			.detectDiskReads()
			.detectDiskWrites()
			.detectNetwork()
			// 这里可以替换为detectAll()就包括了磁盘读写和网络I/O
			.penaltyLog()
			// 打印logCat，当然也可以定位到dropBox，通过文件保存相应的log
			.build());
		StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder().detectLeakedSqlLiteObjects()// 探测SQLite数据库操作
			.penaltyLog()
			// 打印logCat
			.penaltyDeath()
			.build());
		} catch (Exception e) {
			// TODO: handle exception
			throw new HttpException(e);
		}
		
	}
	
	/**
	 * 以Get方式获取数据
	 * @param httpClient
	 * @param url
	 * @param params
	 * @return String
	 */
	public static String doGet(HttpClient httpClient,String url, List<NameValuePair> params, boolean isGzip) {
		
		connection();
		
		// 组裝url
		url += "?data" + URLEncodedUtils.format(params, HTTP.UTF_8);
//		LogUtils.i("Class: "+this.getClass().getName() + "url: "+url);
		// 建立HttpGet对象
		HttpGet httpGet = new HttpGet(url);
		if(isGzip) {
			httpGet.setHeader("Connection", "Keep-Alive");  
			httpGet.addHeader("Accept-Encoding", "gzip");
			httpGet.addHeader("Accept", "application/octet-stream");
		} 
		
		String strResult = "";
		try {
			
			// 发送请求并等待响应
			HttpResponse httpResponse = httpClient.execute(httpGet);
			
			// 若状态码为200 ok
			if (httpResponse.getStatusLine().getStatusCode() == 200) {
				
				// 读取返回数据
				strResult = EncryptUtil.getJsonStringFromGZIP(httpResponse);//EntityUtils.toString(httpResponse.getEntity());//
			}
			else {
				strResult = "Error Response:" + httpResponse.getStatusLine().toString();
			}
		} catch (ClientProtocolException e) {
			throw new HttpException(e);
			//e.printStackTrace();
		}
		catch (IOException e) {
			throw new HttpException(e);
			//e.printStackTrace();
		}
		catch (Exception e) {
			throw new HttpException(e);
			//e.printStackTrace();  
		}
		
		return strResult;
	}
	

	
	/**
	 * 以Get方式获取数据
	 * @param httpClient
	 * @param url
	 * @param params
	 * @return String
	 */
	public static String doGet(HttpClient httpClient,String url, String param, boolean isGzip) {
		
		connection();
		
		// 组裝url
		url += "?" + param;// + URLEncodedUtils.format(param, HTTP.UTF_8);
		
		// 建立HttpGet对象
		HttpGet httpGet = new HttpGet(url);
//		// 设置 请求超时时间   
//		httpGet.getParams().setParameter(HttpConnectionParams.SO_TIMEOUT,  
//				Constants.HTTP_TIMEOUT_SOCKET); 
		if(isGzip) {
			httpGet.setHeader("Connection", "Keep-Alive");  
			httpGet.setHeader("Accept-Encoding", "gzip");
			httpGet.setHeader("Accept", "application/octet-stream");
		} 
		
		String strResult = "";
		try {
			
			// 发送请求并等待响应
			HttpResponse httpResponse = httpClient.execute(httpGet);
			
			// 若状态码为200 ok
			if (httpResponse.getStatusLine().getStatusCode() == 200) {
				
				// 读取返回数据
				strResult = EncryptUtil.getJsonStringFromGZIP(httpResponse);//EntityUtils.toString(httpResponse.getEntity());//
			}
			else {
				strResult = "Error Response:" + httpResponse.getStatusLine().toString();
			}
		} catch (ClientProtocolException e) {
			throw new HttpException(e);
			//e.printStackTrace();
		}
		catch (IOException e) {
			throw new HttpException(e);
			//e.printStackTrace();
		}
		catch (Exception e) {
			throw new HttpException(e);
			//e.printStackTrace();  
		}
		
		return strResult;
	}
	
	/**
	 * 以Post方式获取数据
	 * @param httpClient
	 * @param url
	 * @param param
	 * @return String
	 */
	public static String doPost(HttpClient httpClient,String url, String param, boolean isGzip) {
		
		connection();
		
		// 建立HttpPost对象
		HttpPost httpRequest = new HttpPost(url);
		// 设置 请求超时时间   
		httpRequest.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT,  
				LocalConstants.HTTP_TIMEOUT_SOCKET);  

		if(isGzip) {
			httpRequest.setHeader("Connection", "Keep-Alive");  
			httpRequest.setHeader("Accept-Encoding", "gzip");
			httpRequest.setHeader("Accept", "application/octet-stream");
		} 
		
		String strResult = "doPostError";
		
		try {
			
			// 添加请求参数到请求对象
			StringEntity stringEntity = new StringEntity(param); 
			stringEntity.setContentType("application/x-www-form-urlencoded");  
			httpRequest.setEntity(stringEntity);  
			
			// 发送请求并等待响应
			 
			HttpResponse httpResponse = httpClient.execute(httpRequest);
			
			// 若状态码为200 ok
			if (httpResponse.getStatusLine().getStatusCode() == 200) {
				
				// 使用GZIP解压，读取返回数据
				strResult = EncryptUtil.getJsonStringFromGZIP(httpResponse);//EntityUtils.toString(httpResponse.getEntity());//
			}
			else {
				strResult = "Error Response:" + httpResponse.getStatusLine().toString();
			}
		}
		catch (ClientProtocolException e) {
			
			strResult = e.getMessage().toString();
			throw new HttpException(e);
			//e.printStackTrace();
		}
		catch (IOException e) {
			
			strResult = e.getMessage().toString();
			throw new HttpException(e);
			//e.printStackTrace();
		}
		catch (Exception e) {
			
			strResult = e.getMessage().toString();
			throw new HttpException(e);
			//e.printStackTrace();
		}
		
		return strResult;
	}
	
	/**
	 * 以Post方式获取数据
	 * @param httpClient
	 * @param url
	 * @param params
	 * @return String
	 */
	public static String doPost(HttpClient httpClient,String url, List<NameValuePair> params, boolean isGzip) {
		 
		connection();
		// 建立HttpPost对象
		HttpPost httpRequest = new HttpPost(url);
		// 设置 请求超时时间   
		httpRequest.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT,  
				LocalConstants.HTTP_TIMEOUT_SOCKET);  

		if(isGzip) {
			httpRequest.setHeader("Connection", "Keep-Alive");  
			httpRequest.setHeader("Accept-Encoding", "gzip");
			httpRequest.setHeader("Accept", "application/octet-stream");
		} 
		
		String strResult = "doPostError";
		
		try {
			
			// 添加请求参数到请求对象
			UrlEncodedFormEntity entity = new UrlEncodedFormEntity(params, HTTP.UTF_8);
			httpRequest.setEntity(entity);

			// 发送请求并等待响应
			
			HttpResponse httpResponse = httpClient.execute(httpRequest);
			
			// 若状态码为200 ok
			if (httpResponse.getStatusLine().getStatusCode() == 200) {
				
				// 使用GZIP解压，读取返回数据
				strResult = EncryptUtil.getJsonStringFromGZIP(httpResponse);//EntityUtils.toString(httpResponse.getEntity());//
			}
			else {
				strResult = "Error Response:" + httpResponse.getStatusLine().toString();
			}
		}
		catch (ClientProtocolException e) {
			
			strResult = e.getMessage().toString();
			throw new HttpException(e);
			//e.printStackTrace();
		}
		catch (IOException e) {
			
			strResult = e.getMessage().toString();
			throw new HttpException(e);
			//e.printStackTrace();
		}
		catch (Exception e) {
			
			strResult = e.getMessage().toString();
			throw new HttpException(e);
			//e.printStackTrace();
		}
		
		return strResult;
	}
	
	/**
	 * @Title: getHttpClient
	 * @Description: TODO
	 * @param @return    设定文件
	 * @return DefaultHttpClient    返回类型
	 * @throws
	 */
	private static DefaultHttpClient getHttpClient() {
		DefaultHttpClient httpClient = new DefaultHttpClient();
 
		// 设置 连接超时时间
		httpClient.getParams().setParameter(
				CoreConnectionPNames.CONNECTION_TIMEOUT, LocalConstants.HTTP_TIMEOUT_CONNECTION);
		// 设置 读数据超时时间
		httpClient.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT,
				LocalConstants.HTTP_TIMEOUT_SOCKET);
		// 设置 字符集
		httpClient.getParams().setParameter("http.protocol.content-charset",
				"UTF-8");
		return httpClient;
	}
	
	/**
	 * 下载网络图片到本地
	 * @param strImageUrl
	 * @param filePath
	 */
	public static void downLoadImage(String strImageUrl, String filePath) {
		
		connection();
		
		try {
			
			strImageUrl = URLEncoder.encode(strImageUrl, "utf-8").replaceAll("\\+", "%20").replaceAll("%3A", ":").replaceAll("%2F", "/");
			URL url = new URL(strImageUrl);
			
			InputStream is = url.openStream();
			
			FileOutputStream fos = new FileOutputStream(filePath);
			
			byte buf[] = new byte[1024*4];
			
			// 循环读取
			int numread;
			while ((numread = is.read(buf)) != -1) {

				// 写入本地存储路径
				fos.write(buf, 0, numread);
			}

			// 关闭输入输出流
			is.close();
			fos.close();
		}
		catch (Exception e) {
			throw new HttpException(e);
			//e.printStackTrace();
		}
	}
	
	/**
	 * 获取网络连接参数集合
	 * @param className
	 * @param methodName
	 * @param parameter
	 * @return List<NameValuePair>
	 */
	public static List<NameValuePair> getPostParams(String dllName, String nameSpace, String className, String methodName, String parameter) {
		
		// 组装参数
		StringBuffer paramBuffer = new StringBuffer();
		paramBuffer.append(dllName); 					// UM
		paramBuffer.append(HttpConstants.PARAMETER_TOKEN);	// ◎
		paramBuffer.append(nameSpace);					// aZaaS.aBzB.UM
		paramBuffer.append(HttpConstants.PARAMETER_TOKEN);
		paramBuffer.append(className);					// UserService
		paramBuffer.append(HttpConstants.PARAMETER_TOKEN);
		paramBuffer.append(methodName);					// Login
		paramBuffer.append(HttpConstants.PARAMETER_TOKEN);
		paramBuffer.append(parameter);					// {UserName="ariesly",Password="123456"}
		
		LogUtil.i("paramBuffer: "+paramBuffer);
		
		// 声明参数集合
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("", paramBuffer.toString()));
		
		return params;
	}
	
	/**
	 * @Title: getPostParam
	 * @Description: 获取网络连接参数集合
	 * @param parameter
	 * @param 设定文件
	 * @return List<NameValuePair>    返回类型
	 * @throws
	 */
	public static String getPostParam(String parameter) {
		
		// 组装参数
		StringBuffer paramBuffer = new StringBuffer();
		try {
			paramBuffer.append("data");				
			paramBuffer.append("=");				
			paramBuffer.append(""+URLEncoder.encode(parameter,"UTF-8"));// {UserName="ariesly",Password="123456"}
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
			throw new DataException(e);
		}					
		
//		LogUtil.i(TAG, "paramBuffer: "+paramBuffer);
		
		return paramBuffer.toString();
	}
	
	/**
	 * @Title: getPostParams
	 * @Description: 获取网络连接参数集合
	 * @param parameter
	 * @param 设定文件
	 * @return List<NameValuePair>    返回类型
	 * @throws
	 */
	public static List<NameValuePair> getPostParams(String parameter) {
		
//		// 组装参数
//		StringBuffer paramBuffer = new StringBuffer();
//		paramBuffer.append(parameter);					// {UserName="ariesly",Password="123456"}
//		
//		LogUtil.i("paramBuffer: "+paramBuffer);
		
		// 声明参数集合
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("data", parameter));//paramBuffer.toString()));
		
		return params;
	}
	
	/**
	 * @Title: formatParamsString
	 * @Description: TODO
	 * @param @param mode
	 * @param @param jsonParmas
	 * @param @return    设定文件
	 * @return String    返回类型
	 * @throws
	 */
	public static String formatParamsString(int mode, String jsonParmas) {
		
		String strResult = null;
		try {
			switch (mode) {
			case LocalConstants.GET_SUBMIT_SERVICE: {
				strResult = getPostParam(jsonParmas);
				break;
			}
		}
	} catch (Exception e) {
		// TODO: handle exception
		throw new HttpException(e);
	}
	
	return strResult;
	}
	
	/**
	 * 获取封装请求参数
	 * @param mode
	 * @return
	 */
	public static List<NameValuePair> formatParamsStrToList(int mode, String jsonParmas) {
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		try {
			switch (mode) {
			case LocalConstants.GET_SUBMIT_SERVICE: {
				params = getPostParams(jsonParmas);
				break;
			}
			}
		} catch (Exception e) {
			throw new HttpException(e);
		}
		
		return params;
	}
	
	/**
	 * 发送HttpPost请求
	 * @param mode
	 * @return
	 */
	public static void getHttpPostResult(Handler h, int what, int mode, String jsonParmas, boolean isGzip) {
		try {
			HttpClient httpClient = new DefaultHttpClient();
			
			List<NameValuePair> list = new ArrayList<NameValuePair>();
			list = ConnectionUtil.formatParamsStrToList(mode, jsonParmas);
			
			String result = ConnectionUtil.doPost(httpClient, HttpConstants.WEBSERVICE_URL, list, isGzip);
			
			LogUtil.i("HTTP Post result: "+result);
			
			Message msg = new Message();
			msg.what = what;
			msg.obj = result;
			h.sendMessage(msg);
		} catch (Exception e) {
			// TODO: handle exception
			throw new HttpException(e);
		}
	}
}
