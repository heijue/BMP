package com.k2.mobile.app.model.http.other;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;
import com.k2.mobile.app.R;
import com.k2.mobile.app.common.config.HttpConstants;
import com.k2.mobile.app.common.config.LocalConstants;
import com.k2.mobile.app.model.bean.HTTPOutput;
import com.k2.mobile.app.model.http.HttpHandler;
import com.k2.mobile.app.model.http.HttpUtils;
import com.k2.mobile.app.model.http.RequestParams;
import com.k2.mobile.app.model.http.callback.RequestCallBack;
import com.k2.mobile.app.model.http.client.HttpRequest;
import com.k2.mobile.app.utils.CommonUtil;
import com.k2.mobile.app.utils.DeviceUtil;
import com.k2.mobile.app.utils.EncryptUtil;
import com.k2.mobile.app.utils.FastJSONUtil;


/***************************************
 * @FileName: SendRequest.java
 * @Package:com.zyb.bestway.task
 * @Description: TODO
 * @author: Jason Wu
 * @date:2014-12-14
 * @Update:*
 * @date:2014-12-14
 * @version V1.0
 *************************************/

/**
 * @Title SendRequest.java
 * @Package com.oppo.mo.model.httpk
 * @Description 接口請求工具类
 * @Company  K2
 * 
 * @author linqijun
 * @date 2015-02-03 15:34:00
 * @version V1.0
 * @param <T>
 */
public class SendRequest {

	private static long start_time = 0;
	private static int server_id = 0;
	private static int count = 1;
	
	/**
	 * GetSubmit Request
	 * 
	 * @param c
	 * @param h
	 * @param userName
	 * @param userPassword
	 * @param what
	 */
	@SuppressLint("NewApi")
	public static <T> void getSubmitRequest(Context c, Handler h, int what, T entity) {

		int sdk = DeviceUtil.init(c).sdk_int;
		
		String strJSON = FastJSONUtil.getEntityToJson(entity);

		List<NameValuePair> list = new ArrayList<NameValuePair>();
		list = ConnectionUtil.formatParamsStrToList(LocalConstants.GET_SUBMIT_SERVICE, strJSON);//URLEncoder.encode(strJSON,"UTF-8"))

		String url = UrlStrController.formatUrlStr(c.getString(R.string.global_http_header) + CommonUtil.getServerURL(c), LocalConstants.GET_SUBMIT_SERVICE);
		
		GetDataTask dt = new GetDataTask(h, url, list, null, null, what);
		if(sdk >= 11) {
			dt.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
		} else {
			dt.execute();
		}
	}
	/**
	 * @Title: getSubmitRequest1
	 * @Description: http请求（支持GZip压缩）
	 * @param @param c
	 * @param @param entity
	 * @param @param requestCallBack    设定文件
	 * @return void    返回类型
	 * @throws
	 */
	@SuppressWarnings("unchecked")
	public static <T> void getSubmitRequest1(Context c, T entity, RequestCallBack<String> requestCallBack, int month) {
		int sdk = DeviceUtil.init(c).sdk_int;
		// 验证200毫秒内同一个方法提交两次
		if(0 == start_time){
			start_time = System.currentTimeMillis();
			server_id = month;
		}else{
			if(server_id == month){
				long nowTime = System.currentTimeMillis();
				if((nowTime - start_time) < 200){
					requestCallBack.onFailure(null, LocalConstants.API_KEY);
					return;
//					count++;
//					if(count > 4){
//						requestCallBack.onFailure(null, LocalConstants.API_KEY);
//						return;
//					}
				}else{
					start_time = nowTime;
//					count = 1;
				}
			}else{
				start_time = System.currentTimeMillis();
				server_id = month;
			}
		}
		// 将对象转为json字符串
		String strJSON = FastJSONUtil.getEntityToJson(entity);
		// 设置参数
		List<NameValuePair> list = new ArrayList<NameValuePair>();
		list.add(new BasicNameValuePair("data", strJSON));
		// 设置发送消息体
		RequestParams params = new RequestParams();
		params.addBodyParameter(list);
		params.addHeader("Connection", "Keep-Alive");
		params.addHeader("Accept-Encoding", "gzip");
		params.addHeader("Accept", "application/octet-stream");

		String url = UrlStrController.formatUrlStr(c.getString(R.string.global_http_header) + CommonUtil.getServerURL(c), month);
		HttpUtils http = new HttpUtils();
		http.send(HttpRequest.HttpMethod.POST, url, params, requestCallBack);
	}
	
	
	/**
	 * @Title: getSubmitRequest1
	 * @Description: http请求（支持GZip压缩）
	 * @param @param c
	 * @param @param entity
	 * @param @param requestCallBack    设定文件
	 * @return void    返回类型
	 * @throws
	 */
	@SuppressWarnings("unchecked")
	public static <T> void submitRequest(Context c, String strJSON, RequestCallBack<String> requestCallBack) {
		int sdk = DeviceUtil.init(c).sdk_int;
		System.out.println("resquest Data = "+strJSON);
		String base64 = "";
		if(null != strJSON && !"".equals(strJSON)){
			base64 = EncryptUtil.encodeBase64(strJSON.getBytes());
		}
		// 将数据加密
//		String encodeData = EncryptUtil.getEncodeData(strJSON, "k2_mo_ap");
//		System.out.println("encodeData = "+encodeData);
		// 设置参数
		List<NameValuePair> list = new ArrayList<NameValuePair>();
		list.add(new BasicNameValuePair("RequestString", base64));
		// 设置发送消息体
		RequestParams params = new RequestParams();
		params.addBodyParameter(list);
		params.addHeader("Connection", "Keep-Alive");
		params.addHeader("Accept-Encoding", "gzip");
		params.addHeader("Accept", "application/octet-stream");
		
		String url = "http://"+ HttpConstants.DOMAIN_NAME + ":" + HttpConstants.PROT + HttpConstants.METHOD;
		System.out.println("服务器地址"+url);
		HttpUtils http = new HttpUtils();
		http.send(HttpRequest.HttpMethod.POST, url, params, requestCallBack);
	}
	
	/**
	 * @Title: getSubmitRequest1
	 * @Description: http请求（支持GZip压缩）
	 * @param @param c
	 * @param @param appName 项目名称
	 * @param @param appPlatform 平台 1：android 2：IOS
	 * @param @param requestCallBack    设定文件
	 * @return void    返回类型
	 * @throws
	 */
	@SuppressWarnings("unchecked")
	public static <T> void getSubmitRequest1(Context c, String appName, String appPlatform, RequestCallBack<String> requestCallBack, int month) {
		int sdk = DeviceUtil.init(c).sdk_int;
		// 设置参数
		List<NameValuePair> list = new ArrayList<NameValuePair>();
		list.add(new BasicNameValuePair("appName", appName));
		list.add(new BasicNameValuePair("appPlatform", appPlatform));
		// 设置发送消息体
		RequestParams params = new RequestParams();
		params.addBodyParameter(list);
		params.addHeader("Connection", "Keep-Alive");
		params.addHeader("Accept-Encoding", "gzip");
		params.addHeader("Accept", "application/octet-stream");

		String url = UrlStrController.formatUrlStr(c.getString(R.string.global_http_header) + CommonUtil.getServerURL(c), month);
		HttpUtils http = new HttpUtils();
		http.send(HttpRequest.HttpMethod.POST, url, params, requestCallBack);
	}
	
	/**
	 * @Title: fileDownloader
	 * @Description: 文件下载（支持断点下载）
	 * @param c
	 * @param entity
	 * @param requestCallBack    设定文件
	 * @return void    返回类型
	 * @throws
	 */
	public static HttpHandler fileDownloader(Context c, String url, String target, RequestCallBack<File> requestCallBack) {
		HttpUtils http = new HttpUtils();
		HttpHandler handler = http.download(url, target, true, true, requestCallBack);
		return handler;
	}
	
	/**
	 * @Title: sendSubmitRequest
	 * @Description: 请求数据加密
	 * @param mContext 上下文
	 * @param getJson 请求消息体
	 * @param reqClientID 客户端ID
	 * @param reqLang 客户端语言
	 * @param monthId 配置远程方法序号
	 * @param key 加密密钥
	 * @param submitCallBack 回调方法
	 * @return void 
	 * @throws
	 */
	public static void sendSubmitRequest(Context mContext,String getJson, String reqClientID,
			String reqLang, int monthId, String key, RequestCallBack<String> submitCallBack) {
		System.out.println("getJson = "+getJson);
		// 判断请求数据是否完整
		if (null == getJson || "".equals(getJson) || null == key) {   
			return;
		}  
		// 请求远程服务器的方法
		String urls = UrlStrController.formatUrlStr("", monthId);
		// 将数据加密
//		String encodeData = EncryptUtil.getEncodeData(getJson, key);
		// http请求参数
		HTTPOutput entity = new HTTPOutput();
//		entity.setMessage(encodeData);
		entity.setMessage(getJson);
		entity.setToken(reqClientID);
		entity.setDeviceType("1");
		entity.setReqLang(reqLang);
		entity.setUrl(urls);
		// 发送请求
		SendRequest.getSubmitRequest1(mContext, entity, submitCallBack, monthId);
	}
	
	/**
	 * @Title: sendSubmitRequest
	 * @Description: 请求数据不加密
	 * @param mContext 上下文
	 * @param getJson 请求消息体
	 * @param monthId 配置远程方法序号
	 * @param submitCallBack 回调方法
	 * @return void 
	 * @throws
	 */
	public static void sendSubmitRequest(Context mContext, String getJson, int monthId, RequestCallBack<String> submitCallBack) {
		System.out.println("getJson = "+getJson);
		// 判断请求数据是否完整
		if (null == getJson || "".equals(getJson)) {   
			return;
		}  
		// 请求远程服务器的方法
		HTTPOutput entity = new HTTPOutput();
		entity.setMessage(getJson);
		// 发送请求
		SendRequest.getSubmitRequest1(mContext, entity, submitCallBack, monthId);
	}
}
