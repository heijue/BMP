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
import com.k2.mobile.app.model.bean.ResBindTypeBean;
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

/**
* @Title UserCheckActivity.java
* @Package com.oppo.mo.controller.activity.login;
* @Description 用户绑定
* @Company  K2
* 
* @author linqijun
* @date 2015-03-24 11:28:00
* @version V1.0
*/
public class UserCheckActivity extends BaseActivity implements OnClickListener {
 
//	private TextView tv_title;		// 头部标题
//	private Button btn_back;		// 返回 
	private Button btn_commit;		// 提交按钮
//	private ImageView iv_divider;
	private EditText et_employee_no; // 工号
	
	private String employeesNo = null;
	private String userName = null;
	private String userPwd = null;
	private Intent mIntent;
	
	private Handler mHandler = new Handler(){
		@Override
		public void dispatchMessage(Message msg) {
			switch (msg.what) {
				case 1:
					String decode = (String) msg.obj;
					if(null == decode){
						LogUtil.promptInfo(UserCheckActivity.this, ErrorCodeContrast.getErrorCode("1203", res));
						break;
					}
					
					ResBindTypeBean bean = JSON.parseObject(decode, ResBindTypeBean.class);
					
					mIntent = new Intent(UserCheckActivity.this, GetVerificationCodeActivity.class);
					mIntent.putExtra("email", bean.getEmail());
					mIntent.putExtra("phone", bean.getPhone());
					mIntent.putExtra("userName", userName);
					mIntent.putExtra("userPwd", userPwd);
					mIntent.putExtra("employeesNo", employeesNo);
					
					startActivity(mIntent);
					finish();
					break;
				default:
					break;
			}
		};
	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE); // 去除头部
		setContentView(R.layout.activity_user_check);
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
//		tv_title = (TextView) findViewById(R.id.tv_top_title);
//		iv_divider = (ImageView) findViewById(R.id.public_top_divider);
//		btn_back = (Button) findViewById(R.id.btn_top_go_back);
		btn_commit = (Button) findViewById(R.id.btn_commit);
		et_employee_no = (EditText) findViewById(R.id.et_employee_no);
		
//		btn_back.setVisibility(View.VISIBLE);
//		iv_divider.setVisibility(View.VISIBLE);
//		tv_title.setText(getString(R.string.binding_job_no));
		
		userName = getIntent().getStringExtra("userName");
		userPwd = getIntent().getStringExtra("userPwd");
		et_employee_no.setText(userName);
		et_employee_no.setEnabled(false);
	}
	
	/**
	 * 方法名: initListener() 
	 * 功 能 : 初始化监听器 
	 * 参 数 : void 
	 * 返回值: void
	 */
	private void initListener() {
//		btn_back.setOnClickListener(this);
		btn_commit.setOnClickListener(this);
	}

	/**
	 * @Title: getLoginInfo
	 * @Description: 获取接收验证码信息
	 * @param void
	 * @return String 返回的数据 
	 * @throws
	 */
	private String getCheckInfo(){
		// 员工编号
		employeesNo = et_employee_no.getText().toString();          		
		// 判别员工编号是否为空
		if (null == employeesNo || "".equals(employeesNo.trim())) {				
			LogUtil.promptInfo(UserCheckActivity.this, res.getString(R.string.employee_no_is_null));
			return null;
		}
				
		ReqBodyBean bBean = new ReqBodyBean();
		bBean.setInvokeFunctionCode("F10000007");
		bBean.setInvokeParameter("[\""+employeesNo+"\"]");
				
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
			DialogUtil.showWithCancelProgressDialog(UserCheckActivity.this, null,res.getString(R.string.global_prompt_message), null);
		}

		@Override
		public void onLoading(long total, long current, boolean isUploading) {

		}
		
		@Override
		public void onSuccess(ResponseInfo<String> responseInfo) {                                     
			DialogUtil.closeDialog();
			String result = responseInfo.result.toString();
			byte[] ebase64 = EncryptUtil.decodeBase64(result);
			if (null != ebase64) {
				PublicResultBean resMsg = JSON.parseObject(new String(ebase64), PublicResultBean.class);
				if (null == resMsg || null == resMsg.getResHeader().getStateCode() || "".equals(resMsg.getResHeader().getStateCode())) {				
					LogUtil.promptInfo(UserCheckActivity.this, "无法获取数据");
					return;
				// 验证不合法
				}else if (!"1".equals(resMsg.getResHeader().getStateCode())) {
					LogUtil.promptInfo(UserCheckActivity.this, resMsg.getResHeader().getReturnMsg());	
					return;
				}
				// 判断消息体是否为空
				if (null == resMsg.getResBody().getResultString() || "".equals(resMsg.getResBody().getResultString().trim())) {		 
					LogUtil.promptInfo(UserCheckActivity.this, "无数据返回");
				} else {
					// 获取解密后并校验后的值 
					Message msgs = new Message();
					msgs.what = 1;
					msgs.obj = resMsg.getResBody().getResultString();
					mHandler.sendMessage(msgs);
				}
			} else {
				LogUtil.promptInfo(UserCheckActivity.this, ErrorCodeContrast.getErrorCode("0", res));
			}		
		}

		@Override
		public void onFailure(HttpException error, String msg) {
			DialogUtil.closeDialog();
			if(null != msg && msg.equals(LocalConstants.API_KEY)){
				LogUtil.promptInfo(UserCheckActivity.this, ErrorCodeContrast.getErrorCode("1207", res));
			}else{
				LogUtil.promptInfo(UserCheckActivity.this, ErrorCodeContrast.getErrorCode("0", res));
			}
		}
	};
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.btn_top_go_back:
				finish();
				break;
			case   R.id.btn_commit:
				if (!NetWorkUtil.isNetworkAvailable(UserCheckActivity.this)) {
					DialogUtil.showLongToast(UserCheckActivity.this, R.string.global_network_disconnected);
					break;
				}
				// 获取用户信息
				String info = getCheckInfo();
				if (null != info) {
					SendRequest.submitRequest(UserCheckActivity.this, info, submitCallBack);
				}
				break;
		}
	}
}
