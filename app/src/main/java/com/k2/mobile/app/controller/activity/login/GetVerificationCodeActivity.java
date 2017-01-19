package com.k2.mobile.app.controller.activity.login;

import com.alibaba.fastjson.JSON;
import com.k2.mobile.app.R;
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
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

/**
* @Title GetVerificationCodeActivity.java
* @Package com.oppo.mo.controller.activity.login;
* @Description 获取验证码
* @Company  K2
* 
* @author linqijun
* @date 2015-03-12 11:19:00
* @version V1.0
*/
public class GetVerificationCodeActivity extends BaseActivity implements OnClickListener {
	
	private RadioGroup rg_group;
	private RadioButton rb_phone;
	private RadioButton rb_email;
	// 确认按钮
	private Button btn_confirm;
	
	private String email = null;
	private String phone = null;
	private String userName = null;
	private String userPwd = null;
	
	private String employeesNo = null;
	private String checkWay = "1";
	
	private boolean flag = false;
	
	private Handler mHandler = new Handler(){
		@Override
		public void dispatchMessage(Message msg) {
			switch (msg.what) {
				case 1:
					String json = (String) msg.obj;
					if(null != json && !"-1".equals(json.trim())){
						Intent mIntent = new Intent(GetVerificationCodeActivity.this, CheckNoActivity.class);
						mIntent.putExtra("employeesNo", employeesNo);
						mIntent.putExtra("timeOut", json);
						mIntent.putExtra("checkWay", checkWay);
						mIntent.putExtra("userName", userName);
						mIntent.putExtra("userPwd", userPwd);
						startActivity(mIntent);
						finish();
					}
					break;
			}
		};
	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE); // 去除头部
		setContentView(R.layout.activity_get_verification_code);
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
		btn_confirm = (Button) findViewById(R.id.btn_confirm);
		
		rg_group = (RadioGroup) findViewById(R.id.rg_group);
		rb_phone = (RadioButton) findViewById(R.id.phone_number);
		rb_email = (RadioButton) findViewById(R.id.email);
		
		email = getIntent().getStringExtra("email");
		phone = getIntent().getStringExtra("phone");
		userName = getIntent().getStringExtra("userName");
		userPwd = getIntent().getStringExtra("userPwd");
		
		employeesNo = getIntent().getStringExtra("employeesNo");
		
		if((null == phone || "".equals(phone.trim())) && (null == email || "".equals(email.trim()))){
			DialogUtil.showLongToast(GetVerificationCodeActivity.this, R.string.please_contact);
			finish();
		}else if(null == phone || "".equals(phone.trim())){
			rb_phone.setVisibility(View.GONE);
		}else if(null == email || "".equals(email.trim())){
			rb_email.setVisibility(View.GONE);
		}else{
			rb_phone.setText(phone);
			rb_email.setText(email);
		}		
	}
	
	/**
	 * 方法名: initListener() 
	 * 功 能 : 初始化监听器 
	 * 参 数 : void 
	 * 返回值: void
	 */
	private void initListener() {
		btn_confirm.setOnClickListener(this);
		rg_group.setOnCheckedChangeListener(ckeckChangeListener);
	}
	// RadioGroup 中的RadioButton被选中
	OnCheckedChangeListener ckeckChangeListener = new OnCheckedChangeListener() {
		@Override
		public void onCheckedChanged(RadioGroup group, int checkedId) {
			 if (checkedId == rb_phone.getId()) {  
				 flag = true;
             } else {   
            	 flag = false;
             }  
		}
	};
	
	private String getCheckInfo(){
		
		if(flag){
			checkWay = "2";
		}else{
			checkWay = "1";
		}
				
		ReqBodyBean bBean = new ReqBodyBean();
		bBean.setInvokeFunctionCode("F10000002");
		bBean.setInvokeParameter("[\""+employeesNo+"\", \""+checkWay+"\"]");
				
		PublicRequestBean bean = new PublicRequestBean();
		bean.setReqHeader(PublicResHeaderUtils.getReqHeader());
		bean.setReqBody(bBean);
		
		return JSON.toJSONString(bean);
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.btn_confirm:
				 // 判断网络是否连接
				if (!NetWorkUtil.isNetworkAvailable(GetVerificationCodeActivity.this)) {
					DialogUtil.showLongToast(GetVerificationCodeActivity.this, R.string.global_network_disconnected);
					break;
				}
				// 获取用户信息
				String info = getCheckInfo();
				if (null != info) {
					SendRequest.submitRequest(GetVerificationCodeActivity.this, info, submitCallBack);
				}
				break;
		}
	}
	
	/**
	 * http请求回调
	 */
	RequestCallBack<String> submitCallBack = new RequestCallBack<String>() {

		@Override
		public void onStart() {
			DialogUtil.showWithCancelProgressDialog(GetVerificationCodeActivity.this, null, res.getString(R.string.global_prompt_message), null);
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
					LogUtil.promptInfo(GetVerificationCodeActivity.this, ErrorCodeContrast.getErrorCode("0", res));
					return;
				// 验证不合法
				}else if (!"1".equals(resMsg.getResHeader().getStateCode())) {
					LogUtil.promptInfo(GetVerificationCodeActivity.this, resMsg.getResHeader().getReturnMsg());	
					return;
				}
				// 判断消息体是否为空
				if (null == resMsg.getResBody().getResultString() || "".equals(resMsg.getResBody().getResultString().trim())) {		 
					LogUtil.promptInfo(GetVerificationCodeActivity.this, "无返回数据");
				} else {
					Message msgs = new Message();
					msgs.what = 1;
					msgs.obj = resMsg.getResBody().getResultString();
					mHandler.sendMessage(msgs);
				}
			} else {
				LogUtil.promptInfo(GetVerificationCodeActivity.this, ErrorCodeContrast.getErrorCode("0", res));
			}		
		}

		@Override
		public void onFailure(HttpException error, String msg) {
			DialogUtil.closeDialog();
			if(null != msg && msg.equals(LocalConstants.API_KEY)){
				LogUtil.promptInfo(GetVerificationCodeActivity.this, ErrorCodeContrast.getErrorCode("1207", res));
			}else{
				LogUtil.promptInfo(GetVerificationCodeActivity.this, ErrorCodeContrast.getErrorCode("0", res));
			}
		}
	};	
}
