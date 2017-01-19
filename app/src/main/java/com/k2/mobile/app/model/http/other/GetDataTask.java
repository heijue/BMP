package com.k2.mobile.app.model.http.other;

import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.DefaultHttpClient;

import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;

import com.k2.mobile.app.common.config.LocalConstants;
import com.k2.mobile.app.model.bean.FileBean;
import com.k2.mobile.app.utils.LogUtil;

/**
 * @Title GetDataTask.java
 * @Package com.oppo.mo.model.http
 * @Description 异步线程工具类
 * @Company  K2
 * 
 * @author Jason.Wu
 * @date 2015-02-03 15:34:00
 * @version V1.0
 */
public class GetDataTask extends AsyncTask<String, Integer, String> {

	private Handler handler;
	private String httpUrl;
	private List<NameValuePair> params;
	private int what;
	private FileBean fileBean;
	
	public GetDataTask(Handler h, String strUrl, String param,
			 int what) {
		this.handler = h;
		this.httpUrl = strUrl;
		this.what = what;
	}
	
	public GetDataTask(Handler h, String strUrl, List<NameValuePair> params,
			byte[] dataOutput, String fileName, int what, int recId) {
		this.handler = h;
		this.httpUrl = strUrl;
		this.params = params;
		this.what = what;
	}

	public GetDataTask(Handler h, String strUrl, List<NameValuePair> params,
			byte[] dataOutput, String fileName, int what) {
		this.handler = h;
		this.httpUrl = strUrl;
		this.params = params;
		this.what = what;
	}

	public GetDataTask(Handler h, String strUrl, List<NameValuePair> params, FileBean fb,
			int what) {
		this.handler = h;
		this.httpUrl = strUrl;
		this.params = params;
		this.what = what;
		this.fileBean = fb;
	}

	@Override
	protected String doInBackground(String... params) {
		String result = null;
		try {
			/*
			 * Need Add <uses-permission
			 * android:name="android.permission.INTERNET" />
			 */
			try {
				LogUtil.i("++++++ " + this.httpUrl + "   " + this.params);
				HttpClient httpClient = new DefaultHttpClient();
				result = ConnectionUtil.doPost(httpClient, this.httpUrl, this.params, LocalConstants.IS_USE_GZIP).trim();
				//result = ConnectionUtil.doPost(httpClient, this.httpUrl, this.params, Constants.IS_USE_GZIP).trim();//doHttpURLConnectionPost(this.httpUrl, this.params).trim();
				//result = ConnectionUtil.doGet(httpClient, this.httpUrl, this.param, Constants.IS_USE_GZIP).trim();//doHttpURLConnectionPost(this.httpUrl, this.params).trim();
			} catch (Exception e) {
				result = "";
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	// @Override
	@Override
	protected void onProgressUpdate(Integer... progress) {

	}

	@Override
	protected void onPostExecute(String result) {
		LogUtil.i("# Result ++++++ " + result);

		Message msg = Message.obtain();
		msg.what = what;
		if (what == LocalConstants.COMMON_UPLOAD_SERVICE) {
			msg.getData().putString("result", result);
			msg.obj = fileBean;
		} else {
			msg.obj = result;
		}
		handler.sendMessage(msg);
	}
	
	/*
	private String doHttpURLConnectionPost(String strUrl, String strParams) {
		try {
			HttpURLConnection uRLConnection;
			URL url = new URL(strUrl);

			if (Constants.CT_WAP) {
				LogUtils.i("TAG: ####  Using---CTWAP 200 proxy");
				Proxy proxy = new Proxy(java.net.Proxy.Type.HTTP,
						new InetSocketAddress(
								android.net.Proxy.getDefaultHost(),
								android.net.Proxy.getDefaultPort()));
				uRLConnection = (HttpURLConnection) url.openConnection(proxy);
			} else {
				LogUtils.i("TAG: ####  Not Using--- proxy");
				// Util.saveLog("####  Not Using--- proxy" + "\n");
				uRLConnection = (HttpURLConnection) url.openConnection();
			}
			
			uRLConnection = (HttpURLConnection) url.openConnection();

			uRLConnection.setDoInput(true);
			uRLConnection.setDoOutput(true);
			uRLConnection.setRequestMethod("POST");
			uRLConnection.setUseCaches(false);
			uRLConnection.setInstanceFollowRedirects(false);
			uRLConnection
					.setRequestProperty("Content-Type", "application/json");
			uRLConnection.setConnectTimeout(12000);
			try {
				uRLConnection.connect();
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
				if (Constants.CT_WAP) {
					LogUtils.i("TAG: #### try use proxy connect again");
					Proxy proxy = new Proxy(java.net.Proxy.Type.HTTP,
							new InetSocketAddress(
									android.net.Proxy.getDefaultHost(),
									android.net.Proxy.getDefaultPort()));
					uRLConnection = (HttpURLConnection) url
							.openConnection(proxy);
				} else {
					LogUtils.i("TAG: #### try use 172 proxy connect again");
					Proxy proxy = new Proxy(java.net.Proxy.Type.HTTP,
							new InetSocketAddress(
									android.net.Proxy.getDefaultHost(),
									android.net.Proxy.getDefaultPort()));
					uRLConnection = (HttpURLConnection) url
							.openConnection(proxy);
				}
				uRLConnection.setDoInput(true);
				uRLConnection.setDoOutput(true);
				uRLConnection.setRequestMethod("POST");
				uRLConnection.setUseCaches(false);
				uRLConnection.setInstanceFollowRedirects(false);
				uRLConnection.setRequestProperty("Content-Type",
						"application/json");
				uRLConnection.setConnectTimeout(12000);
				uRLConnection.connect();
			}

			DataOutputStream out = new DataOutputStream(
					uRLConnection.getOutputStream());
			if (strParams != null)
				out.write(strParams.toString().getBytes("UTF-8"));
			if (dataOutput != null) {
				out.write(dataOutput, 0, dataOutput.length);
			}
			out.flush();
			out.close();

			InputStream stream = uRLConnection.getInputStream();

			BufferedReader reader = new BufferedReader(new InputStreamReader(
					stream, "UTF-8"), 8192);

			StringBuilder builder = new StringBuilder();
			String readLine = null;
			while ((readLine = reader.readLine()) != null) {
				builder.append(readLine);
			}
			stream.close();
			reader.close();
			uRLConnection.disconnect();

			return builder.toString();
		} catch (IOException exception) {

			exception.printStackTrace();
			return null;
		}
	}*/
}