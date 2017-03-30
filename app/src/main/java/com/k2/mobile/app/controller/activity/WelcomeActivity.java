package com.k2.mobile.app.controller.activity;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import com.alibaba.fastjson.JSON;
import com.igexin.sdk.PushManager;
import com.k2.mobile.app.R;
import com.k2.mobile.app.common.config.BroadcastNotice;
import com.k2.mobile.app.common.config.HttpConstants;
import com.k2.mobile.app.common.exception.HttpException;
import com.k2.mobile.app.controller.activity.login.LoginActivity;
import com.k2.mobile.app.controller.core.BaseActivity;
import com.k2.mobile.app.controller.core.BaseApp;
import com.k2.mobile.app.model.bean.AutoUpdateProcedureBean;
import com.k2.mobile.app.model.bean.PublicRequestBean;
import com.k2.mobile.app.model.bean.PublicResultBean;
import com.k2.mobile.app.model.bean.ReqBodyBean;
import com.k2.mobile.app.model.http.ResponseInfo;
import com.k2.mobile.app.model.http.callback.RequestCallBack;
import com.k2.mobile.app.model.http.other.SendRequest;
import com.k2.mobile.app.utils.AutoUpdateProcedureUtil;
import com.k2.mobile.app.utils.EncryptUtil;
import com.k2.mobile.app.utils.ErrorCodeContrast;
import com.k2.mobile.app.utils.LogUtil;
import com.k2.mobile.app.utils.PublicResHeaderUtils;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

/**
 * 
 * @ClassName: WelcomeActivity
 * @Description: 欢迎页
 * @author linqijun
 * @date 2015-3-6 上午10:16:31
 *
 */
public class WelcomeActivity extends BaseActivity {

	private IncomingReceiver iReceiver;
	// 标识是否跳转到登陆
	private boolean flag = true;
	// 判断是否已关闭
	private boolean isColse = false;


	private Handler mHandler = new Handler() {
		@Override
		public void dispatchMessage(android.os.Message msg) {
			switch (msg.what) {
			case 0:
			case 1:
				if(!isColse){
					isColse = true;
					WelcomeActivity.this.startActivity(LoginActivity.class);
					WelcomeActivity.this.finish();
				}
				break;
			case 2:
				AlertDialog mAlertDialog = new AlertDialog.Builder(WelcomeActivity.this)
				.setTitle(getResources().getString(R.string.no_update_tip))
				.setMessage(getResources().getString(R.string.no_update))
				.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						mHandler.sendEmptyMessage(1);
					}
				}).show();
				break;
			default:
				if(flag){
					mHandler.sendEmptyMessage(1);	
				}
				break;
			}
		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_welcome);
		checkLang();					// 判断语言
		setWifi();
		
		//暂时不支持推送  ，处理Mac地址为空的问题 ljw2016-01-28
		//BaseApp.clientid="-1";
		 
		try{
	    PushManager.getInstance().initialize(getApplicationContext());
		BaseApp.clientid = PushManager.getInstance().getClientid(getApplicationContext());
			if(BaseApp.clientid==""||BaseApp.clientid==null)
			{
				BaseApp.clientid="-1";
			}
		}
		catch(Exception ex){
			BaseApp.clientid="-1";
		}
		  
		String realmName = settingSharedPreference.getString("realmName", "");
		String parent = settingSharedPreference.getString("parent", "");
		String site = settingSharedPreference.getString("site", "");

//		int lang = settingSharedPreference.getInt("locale",0);
//		Resources resources = getResources();
//		Configuration config = resources.getConfiguration();
//		DisplayMetrics dm = resources.getDisplayMetrics();
//		config.locale = BaseApp.localeSymbols[lang];
//		resources.updateConfiguration(config,dm);

		HttpConstants.DOMAIN_NAME = realmName; 
		HttpConstants.PROT = parent;
		HttpConstants.SITE = site;
						
		createFilter();
		// 控制当前页，不能停留超过5秒
		mHandler.postDelayed(runnable, 3000);
		// 此线程去获取版本信息
		SendRequest.submitRequest(mContext, getVersion(), submitCallBack);
	}
	
	/**
	 * @Title: getLoginInfo
	 * @Description: 获取登陆信息
	 * @return String 返回的数据 
	 * @throws
	 */
	private String getVersion(){
		// 取得用户名
		
		ReqBodyBean bBean = new ReqBodyBean();
		bBean.setInvokeFunctionCode("F10000009");
		bBean.setInvokeParameter("[]");
				
		PublicRequestBean bean = new PublicRequestBean();
		bean.setReqHeader(PublicResHeaderUtils.getReqHeader());
		bean.setReqBody(bBean);
		
		return JSON.toJSONString(bean);
	}
	
	Runnable runnable = new Runnable(){
	   @Override
	   public void run() {
		   mHandler.sendEmptyMessage(10);
	   }
	};
	 
	/**
	 * 设置wifi
	 */
	private void setWifi() {
		final WifiManager wifi = (WifiManager) getSystemService(Context.WIFI_SERVICE);
		if (wifi == null){
			return;
		}
		WifiInfo info = wifi.getConnectionInfo();
		BaseApp.macAddr = info.getMacAddress();
		if (null == BaseApp.macAddr) {
			wifi.setWifiEnabled(true);
			for (int i = 0; i < 10; i++) {
				WifiInfo _info = wifi.getConnectionInfo();
				BaseApp.macAddr = _info.getMacAddress();
				if (BaseApp.macAddr != null){
					break;
				}
				try {
					Thread.sleep(500);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			wifi.setWifiEnabled(false);
		}
	}

	/**
	 * http请求回调
	 */
	RequestCallBack<String> submitCallBack = new RequestCallBack<String>() {

		@Override
		public void onStart() {
		}

		@Override
		public void onLoading(long total, long current, boolean isUploading) {

		}

		@Override
		public void onSuccess(ResponseInfo<String> responseInfo) {
			flag = false;
			String result = responseInfo.result.toString();
			byte[] ebase64 = EncryptUtil.decodeBase64(result);
			if (null != result && !"".equals(result.trim())) {
				PublicResultBean resMsg = JSON.parseObject(new String(ebase64), PublicResultBean.class);
				System.out.println("resMsg.getResBody().getResultString() = "+resMsg.getResBody().getResultString());
				if (null == resMsg || null == resMsg.getResHeader().getStateCode() || "".equals(resMsg.getResHeader().getStateCode())) {				
					LogUtil.promptInfo(WelcomeActivity.this, ErrorCodeContrast.getErrorCode("0", res));
					return;
				// 如果用新手机登陆
				}else if (!"1".equals(resMsg.getResHeader().getStateCode())) {
					LogUtil.promptInfo(WelcomeActivity.this, resMsg.getResHeader().getReturnMsg());	
					return;
				}
				// 判断消息体是否为空
				if (null == resMsg.getResBody().getResultString() || "".equals(resMsg.getResBody().getResultString().trim())) {		 
					LogUtil.promptInfo(WelcomeActivity.this, ErrorCodeContrast.getErrorCode("0", res));
				} else {
					if(null != resMsg.getResHeader().getSectetKey() && !"".equals(resMsg.getResHeader().getSectetKey().trim())){
						BaseApp.key = resMsg.getResHeader().getSectetKey();
					}
					
					AutoUpdateProcedureBean bean = JSON.parseObject( resMsg.getResBody().getResultString(), AutoUpdateProcedureBean.class);
					AutoUpdateProcedureUtil mUpdate = new AutoUpdateProcedureUtil(WelcomeActivity.this);
					boolean val = false;
					if(null != bean.getIsNeedUpdate() && !"".equals(bean.getIsNeedUpdate().trim()) && "true".equals(bean.getIsNeedUpdate().trim())){
						val = true;
					}
					
					mUpdate.checkUpdateInfo(bean.getDownloadUrl(), bean.getVersionCode(), val, bean.getFileSize());
					return;
				}
			} 
			
			Message msgs = new Message();
			msgs.what = 1;
			mHandler.sendMessage(msgs);
		}

		@Override
		public void onFailure(HttpException error, String msg) {
			flag = false;
			Message msgs = new Message();
			msgs.what = 1;
			mHandler.sendMessage(msgs);
		}
	};

	/**
	 * @Title: createFilter
	 * @Description: 创建IntentFilter
	 * @return void 
	 * @throws
	 */
	private void createFilter() {
		IntentFilter filter = new IntentFilter();
		filter.addAction(BroadcastNotice.AUTO_UPDATE_PROCEDURE);
		iReceiver = new IncomingReceiver();
		// 注册广播
		WelcomeActivity.this.registerReceiver(iReceiver, filter);
	}
	
	/**
	 * 接收广播
	 */ 
	private class IncomingReceiver extends BroadcastReceiver{
		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();  
	        if (action.equals(BroadcastNotice.AUTO_UPDATE_PROCEDURE)) {
	        	int falg = intent.getIntExtra("falg", 0);
	        	if(-1 == falg){
	        		mHandler.sendEmptyMessage(2);
	        	}else{
	        		mHandler.sendEmptyMessage(1);
	        	}
	        } 
		}
	}
	
	// 签名
	public static String getSign(Map<String, String> newMap, String url, String secret) {
		StringBuffer newBuffer = new StringBuffer();
		newBuffer.append(url);
		newBuffer.append("\n");
		Iterator<Entry<String, String>> it = sortMapByKey(newMap).entrySet().iterator();
		while (it.hasNext()) {
			Entry<String, String> pairs = it.next();
			newBuffer.append(pairs.getKey());
			newBuffer.append("=");
			newBuffer.append(pairs.getValue());
			newBuffer.append("\n");
		}
		newBuffer.append(secret);
		String ssssString = EncryptUtil.stringToMD5(newBuffer.toString());
		return ssssString;
	}

	public static Map<String, String> sortMapByKey(Map<String, String> map) {
		if (map == null || map.isEmpty()) {
			return null;
		}
		Map<String, String> sortMap = new TreeMap<String, String>(new MapKeyComparator());
		sortMap.putAll(map);
		return sortMap;
	}
	
	@Override
	protected void onDestroy() {
		if(null != iReceiver){
			unregisterReceiver(iReceiver);
		}
		super.onDestroy();
	}
}

	class MapKeyComparator implements Comparator<String> {
		@Override
		public int compare(String str1, String str2) {
			return str1.compareTo(str2);
		}
	}