package com.k2.mobile.app.controller.activity.login;

import com.alibaba.fastjson.JSON;
import com.k2.mobile.app.R;
import com.k2.mobile.app.common.config.BroadcastNotice;
import com.k2.mobile.app.common.config.LocalConstants;
import com.k2.mobile.app.common.exception.HttpException;
import com.k2.mobile.app.controller.core.BaseActivity;
import com.k2.mobile.app.controller.core.BaseApp;
import com.k2.mobile.app.model.bean.PublicRequestBean;
import com.k2.mobile.app.model.bean.PublicResultBean;
import com.k2.mobile.app.model.bean.ReqBodyBean;
import com.k2.mobile.app.model.http.ResponseInfo;
import com.k2.mobile.app.model.http.callback.RequestCallBack;
import com.k2.mobile.app.model.http.other.SendRequest;
import com.k2.mobile.app.utils.DialogUtil;
import com.k2.mobile.app.utils.EncryptUtil;
import com.k2.mobile.app.utils.ErrorCodeContrast;
import com.k2.mobile.app.utils.LogUtil;
import com.k2.mobile.app.utils.NetWorkUtil;
import com.k2.mobile.app.utils.PublicResHeaderUtils;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

/**
* @Title ReceiveCheckNoTypeActivity.java
* @Package com.oppo.mo.controller.activity.login;
* @Description 选择验证方式
* @Company  K2
* 
* @author linqijun
* @date 2015-03-12 11:19:00
* @version V1.0
*/
public class CheckNoActivity extends BaseActivity implements OnClickListener {
	
	// 输入验证码
	private EditText et_vf_code;
	private Button btn_commit;
	private TextView tv_time_down;
	private String employeesNo = null;
	private String checkWay = null;
	private String timeOut = null;
	private int time = 0;
	private boolean flag = true;
	private int operClass = 1;
	private String userName = null;
	private String userPwd = null;
	
	private Handler mHandler = new Handler(){
		@Override
		public void dispatchMessage(android.os.Message msg) {
			switch (msg.what) {
				case 1:
//					LogUtil.promptInfo(CheckNoActivity.this, ErrorCodeContrast.getErrorCode("9999", BaseApp.res));
					Intent mIntent = new Intent(BroadcastNotice.AUTON_LOGIN);
					sendBroadcast(mIntent);
					BaseApp.closeActivity();
					break;
				case 2:
					String json = (String) msg.obj;
					if(null != json && !"".equals(json.trim())){
						timeOut = json;
						if(null != timeOut){
							flag = true;
							tv_time_down.setEnabled(false);	
							tv_time_down.setText(timeOut+getString(R.string.seconds));
							time = Integer.parseInt(timeOut);
							new Thread(new ThreadShow()).start();  
						}
					}
					break;
				case 3:
					time--;
					if(time < 1){
						flag = false;
						tv_time_down.setEnabled(true);	
						tv_time_down.setText(R.string.resend);
					}else{
						tv_time_down.setText(time+getString(R.string.seconds));
					}
					break;
			}
		};
	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE); // 去除头部
		setContentView(R.layout.activity_check_no);
		initView();
		initListener();
		BaseApp.addActivity(this);
	}
	
	/**
	 * 方法名: initView() 
	 * 功 能 : 初始化 
	 * 参 数 : void 
	 * 返回值: void
	 */
	private void initView() {
		et_vf_code = (EditText) findViewById(R.id.et_vf_code);
		btn_commit = (Button) findViewById(R.id.btn_commit);
		tv_time_down = (TextView) findViewById(R.id.tv_time_down);
		employeesNo = getIntent().getStringExtra("employeesNo");
		checkWay = getIntent().getStringExtra("checkWay");
		timeOut = getIntent().getStringExtra("timeOut");
		userName = getIntent().getStringExtra("userName");
		userPwd = getIntent().getStringExtra("userPwd");
		
		if(null != timeOut){
			tv_time_down.setEnabled(false);	
			tv_time_down.setText(timeOut+getString(R.string.seconds));
			time = Integer.parseInt(timeOut);
			new Thread(new ThreadShow()).start();  
		}
	}
	
	/**
	 * 方法名: initListener() 
	 * 功 能 : 初始化监听器 
	 * 参 数 : void 
	 * 返回值: void
	 */
	private void initListener() {
		btn_commit.setOnClickListener(this);
		tv_time_down.setOnClickListener(this);
	}
		
	/**
	 * @Title: getSendCheckNoInfo
	 * @Description: 取得验证码信息
	 * @param void
	 * @return String 返回的数据 
	 * @throws
	 */
	private String getSendCheckNoInfo(){
		// 输入验证码
		String vf_code = et_vf_code.getText().toString();          		
		if (null == vf_code || "".equals(vf_code.trim())) {				
			LogUtil.promptInfo(CheckNoActivity.this, res.getString(R.string.check_code_no_is_null));
			return null;
		}
				
		ReqBodyBean bBean = new ReqBodyBean();
		bBean.setInvokeFunctionCode("F10000003");
		bBean.setInvokeParameter("[\""+userName+"\", \""+vf_code+"\"]");   
				
		PublicRequestBean bean = new PublicRequestBean();
		bean.setReqHeader(PublicResHeaderUtils.getReqHeader());
		bean.setReqBody(bBean);
		
		return JSON.toJSONString(bean);
	}
	/**
	 * @Title: getCheckInfo
	 * @Description: 取得验证码信息
	 * @param void
	 * @return String 返回的数据 
	 * @throws
	 */
	private String getCheckInfo(){
		
		ReqBodyBean bBean = new ReqBodyBean();
		bBean.setInvokeFunctionCode("F10000002");
		bBean.setInvokeParameter("[\""+employeesNo+"\", \""+checkWay+"\"]");
				
		PublicRequestBean bean = new PublicRequestBean();
		bean.setReqHeader(PublicResHeaderUtils.getReqHeader());
		bean.setReqBody(bBean);
		
		return JSON.toJSONString(bean);
	}
	
	/**
	 * http请求回调
	 */
	RequestCallBack<String> submitCallBack = new RequestCallBack<String>() {

		@Override
		public void onStart() {
			DialogUtil.showWithCancelProgressDialog(CheckNoActivity.this, null,res.getString(R.string.global_prompt_message), null);
		}

		@Override
		public void onLoading(long total, long current, boolean isUploading) {

		}
		
		
		@Override
		public void onSuccess(ResponseInfo<String> responseInfo) {                                     
			DialogUtil.closeDialog();
			String result = responseInfo.result.toString();
			byte[] ebase64 = EncryptUtil.decodeBase64(result);
			if (null != result && !"".equals(result.trim())) {
				PublicResultBean resMsg = JSON.parseObject(new String(ebase64), PublicResultBean.class);
				// 判断返回标识状态是否为空
				if (null == resMsg || null == resMsg.getResHeader().getStateCode() || "".equals(resMsg.getResHeader().getStateCode())) {				
					LogUtil.promptInfo(CheckNoActivity.this, ErrorCodeContrast.getErrorCode("0", res));
					return;
				// 验证不合法
				}else if (!"1".equals(resMsg.getResHeader().getStateCode())) {
					LogUtil.promptInfo(CheckNoActivity.this, resMsg.getResHeader().getReturnMsg());	
					return;
				}
				// 判断消息体是否为空
				if (null == resMsg.getResBody().getResultString() || "".equals(resMsg.getResBody().getResultString().trim())) {		 
					LogUtil.promptInfo(CheckNoActivity.this, "无数据返回");
				} else {
					Message msgs = new Message();
					msgs.what = operClass;
					msgs.obj = resMsg.getResBody().getResultString();
					mHandler.sendMessage(msgs);
				}
			} else {
				LogUtil.promptInfo(CheckNoActivity.this, ErrorCodeContrast.getErrorCode("0", res));
			}		
		}
		
		@Override
		public void onFailure(HttpException error, String msg) {
			DialogUtil.closeDialog();
			if(null != msg && msg.equals(LocalConstants.API_KEY)){
				LogUtil.promptInfo(CheckNoActivity.this, ErrorCodeContrast.getErrorCode("1207", res));
			}else{
				LogUtil.promptInfo(CheckNoActivity.this, ErrorCodeContrast.getErrorCode("0", res));
			}
		}
	};
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.btn_commit:
				operClass = 1;
				 // 判断网络是否连接
				if (!NetWorkUtil.isNetworkAvailable(CheckNoActivity.this)) {
					DialogUtil.showLongToast(CheckNoActivity.this, R.string.global_network_disconnected);
					break;
				}
				// 获取用户信息
				String info = getSendCheckNoInfo();
				if (null != info) {
					SendRequest.submitRequest(CheckNoActivity.this, info, submitCallBack);
				}
				break;
			case R.id.tv_time_down:
				operClass = 2;
				String infos = getCheckInfo();
				if (null != infos) {
					SendRequest.submitRequest(CheckNoActivity.this, infos, submitCallBack);
				}
				break;
		}
	}
	
	// 线程类  
    class ThreadShow implements Runnable {  
  
        @Override  
        public void run() {  
            while (flag) {  
                try {  
                    Thread.sleep(1000);  
                    Message msg = new Message();  
                    msg.what = 3;  
                    mHandler.sendMessage(msg);  
                } catch (Exception e) {  
                    e.printStackTrace();  
                }  
            }  
        }  
    } 
}
