package com.k2.mobile.app.controller.server;    

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.IBinder;

import com.alibaba.fastjson.JSON;
import com.k2.mobile.app.common.config.BroadcastNotice;
import com.k2.mobile.app.common.config.LocalConstants;
import com.k2.mobile.app.common.exception.HttpException;
import com.k2.mobile.app.controller.core.BaseApp;
import com.k2.mobile.app.model.bean.PublicBean;
import com.k2.mobile.app.model.bean.ReqMessage;
import com.k2.mobile.app.model.bean.WorkCountBean;
import com.k2.mobile.app.model.db.DBHelper;
import com.k2.mobile.app.model.http.ResponseInfo;
import com.k2.mobile.app.model.http.callback.RequestCallBack;
import com.k2.mobile.app.model.http.other.SendRequest;
import com.k2.mobile.app.utils.EncryptUtil;
import com.k2.mobile.app.utils.FastJSONUtil;

/**
 * @Title BackManagementServer.java
 * @Package com.oppo.mo.controller.server
 * @Description 后台服务类
 * @Company  K2
 * 
 * @author linqijun
 * @date 2015-03-7 10:44:00
 * @version V1.0
 */
public class BackManagementServer extends Service{

	private CommandReceiver cmdReceiver;
	protected boolean flag = true;
	private DBHelper db;
	//邮件请求模块
	private final String EMAIL_COUNT_MOUDLE = "2";
	//审批请求模块
	private final String APPROVAL_COUNT_MOUDLE = "3";
	
	private Handler handler = new Handler( );
	private Runnable runnable = new Runnable( ) {
		@Override
		public void run ( ) {
			SendRequest.sendSubmitRequest(getApplicationContext(), getJson(BaseApp.user.getUserId(),EMAIL_COUNT_MOUDLE), BaseApp.token, BaseApp.reqLang, 
    				LocalConstants.WORK_COUNT_SERVER, BaseApp.key, submitEmailCallBack); 
		}
	};

	
	@Override 
    public void onCreate() {  
		super.onCreate(); 
		// 过滤广播信息
    	IntentFilter intentFilter = new IntentFilter();
    	intentFilter.addAction(BroadcastNotice.STOP_BACK_SERVER);
    	intentFilter.addAction(BroadcastNotice.FLOAT_JOB_NUMBER);
    	cmdReceiver = new CommandReceiver();
    	registerReceiver(cmdReceiver, intentFilter);
    	
    	db = new DBHelper(getApplicationContext());
    	BaseApp.db = db;
    	
    	SendRequest.sendSubmitRequest(getApplicationContext(), getJson(BaseApp.user.getUserId(),APPROVAL_COUNT_MOUDLE), BaseApp.token, BaseApp.reqLang, 
				LocalConstants.WORK_COUNT_SERVER, BaseApp.key, submitApprovalCallBack);
    	handler.postDelayed(runnable, 1000 * 60 * 3);         // 开始从后台获取数据
    } 
	
	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override 
    public void onDestroy() {  
		// 取消BroadcastReceiver
		this.unregisterReceiver(cmdReceiver);
		if(null != handler){
			handler.removeCallbacks(runnable);           // 停止handler
		}
		
        super.onDestroy(); 
    } 
 
    @Override 
    public int onStartCommand(Intent intent, int flags, int startId) {  
        return super.onStartCommand(intent, flags, startId); 
    } 
    //接收广播
    private class CommandReceiver extends BroadcastReceiver{
		@Override
		public void onReceive(Context context, Intent intent) {
			handler.removeCallbacks(runnable);           // 停止handler
			stopSelf();
		}
    }
 
    /**
	 * @Title: getJson
	 * @Description: 工作待办数量参数
	 * @param void
	 * @return String 返回的数据 
	 * @throws
	 */
	private String getJson(String userCode,String module){
		PublicBean bean = new PublicBean();
		bean.setUserCode(userCode);
		bean.setModule(module);
		return JSON.toJSONString(bean);
	}
	
	/**
	 * http请求回调
	 */
	RequestCallBack<String> submitEmailCallBack = new RequestCallBack<String>() {

		@Override
		public void onStart() {
			
		}

		@Override
		public void onLoading(long total, long current, boolean isUploading) {
			
		}
		
		@Override
		public void onSuccess(ResponseInfo<String> responseInfo) {                                     
			String result = responseInfo.result.toString();
			if (null != result && !"".equals(result.trim())) {
				ReqMessage msg = FastJSONUtil.getJSONToEntity(result, ReqMessage.class);
				// 判断返回标识状态是否为空
				if (null == msg || null == msg.getResCode() || "".equals(msg.getResCode())) {				
					return;
				}else if ("1".equals(msg.getResCode())) {
					// 获取解密后并校验后的值
					String decode = EncryptUtil.getDecodeData(msg.getMessage(), BaseApp.key);
					if(null != decode){
						WorkCountBean workCountBean = JSON.parseObject(decode, WorkCountBean.class);
						int workCount=0;
						if(workCountBean.getCount()!=null){
							try {
								workCount=Integer.parseInt(workCountBean.getCount());
							} catch (NumberFormatException e) {
							}
						}
						Intent mIntent = new Intent(BroadcastNotice.EMAIL_COUNT);
						mIntent.putExtra("emailCount", workCount);
						sendBroadcast(mIntent);						
					}
				}
			}
			
			SendRequest.sendSubmitRequest(getApplicationContext(), getJson(BaseApp.user.getUserId(),APPROVAL_COUNT_MOUDLE), BaseApp.token, BaseApp.reqLang, 
					LocalConstants.WORK_COUNT_SERVER, BaseApp.key, submitApprovalCallBack);
		}

		@Override
		public void onFailure(HttpException error, String msg) {
			
		}
	};		
	/**
	 * http请求回调
	 */
	RequestCallBack<String> submitApprovalCallBack = new RequestCallBack<String>() {
		
		@Override
		public void onStart() {
			
		}
		
		@Override
		public void onLoading(long total, long current, boolean isUploading) {
			
		}
		
		@Override
		public void onSuccess(ResponseInfo<String> responseInfo) { 
			String result = responseInfo.result.toString();
			if (null != result && !"".equals(result.trim())) {
				ReqMessage msg = FastJSONUtil.getJSONToEntity(result, ReqMessage.class);
				// 判断返回标识状态是否为空
				if (null == msg || null == msg.getResCode() || "".equals(msg.getResCode())) {				
					return;
				}else if ("1".equals(msg.getResCode())) {
					// 获取解密后并校验后的值
					String decode = EncryptUtil.getDecodeData(msg.getMessage(), BaseApp.key);
					if(null != decode){
						WorkCountBean workCountBean = JSON.parseObject(decode, WorkCountBean.class);
						int workCount=0;
						if(workCountBean.getCount()!=null){
							try {
								workCount=Integer.parseInt(workCountBean.getCount());
							} catch (NumberFormatException e) {
							}
						}
							Intent mIntent = new Intent(BroadcastNotice.APPROVAL_COUNT);
							mIntent.putExtra("approvalCount", workCount);
							sendBroadcast(mIntent);
					}
				}
			}  
		}
		
		@Override
		public void onFailure(HttpException error, String msg) {
			
		}
	};		
}
 