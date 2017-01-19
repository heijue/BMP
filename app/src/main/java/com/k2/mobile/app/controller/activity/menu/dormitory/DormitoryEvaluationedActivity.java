package com.k2.mobile.app.controller.activity.menu.dormitory;

import com.alibaba.fastjson.JSON;
import com.k2.mobile.app.R;
import com.k2.mobile.app.common.config.BroadcastNotice;
import com.k2.mobile.app.common.config.LocalConstants;
import com.k2.mobile.app.common.exception.HttpException;
import com.k2.mobile.app.controller.core.BaseApp;
import com.k2.mobile.app.model.bean.PublicBean;
import com.k2.mobile.app.model.bean.ReqMessage;
import com.k2.mobile.app.model.bean.ResPublicBean;
import com.k2.mobile.app.model.http.ResponseInfo;
import com.k2.mobile.app.model.http.callback.RequestCallBack;
import com.k2.mobile.app.model.http.other.SendRequest;
import com.k2.mobile.app.utils.DialogUtil;
import com.k2.mobile.app.utils.EncryptUtil;
import com.k2.mobile.app.utils.ErrorCodeContrast;
import com.k2.mobile.app.utils.FastJSONUtil;
import com.k2.mobile.app.utils.LogUtil;
import com.k2.mobile.app.utils.NetWorkUtil;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;

/**
 * 
 * @ClassName: DormitoryEvaluationActivity
 * @Description: 宿舍报修评价
 * @author linqijun
 * @date 2015-6-08 14:24:00
 * 
 */
public class DormitoryEvaluationedActivity extends FragmentActivity implements OnClickListener {
	
	// 返回
	private Button btn_back;
	// 头部标题
	private TextView tv_title;
	private TextView tv_search;
	
	private TextView tv_list_title;
	private TextView tv_s1, tv_s2, tv_s3, tv_s4, tv_s5;
	private TextView tv_score;
	private TextView tv_commit;
	private EditText ed_description;
	private RadioButton rb_yes, rb_no;
	
	private String title = null;
	private String code = null;
	private int evaluation = 0;
	private int type = 1;
	
	private Handler mHandler = new Handler(){
		@Override
		public void dispatchMessage(Message msg) {
			 switch (msg.what) { 
				case 1:
					String resSave = (String) msg.obj;
					if (null != resSave) {
						ResPublicBean bean = JSON.parseObject(resSave, ResPublicBean.class);
						if (null != bean && "1".equals(bean.getSuccess())) {
							DialogUtil.showLongToast(DormitoryEvaluationedActivity.this, R.string.evaluation_success);
							Intent mIntent = new Intent(BroadcastNotice.FEEDBACK_FINISH_UPDATE);
							sendBroadcast(mIntent);
							setResult(RESULT_OK);  
							finish();
						} else {
							DialogUtil.showLongToast(DormitoryEvaluationedActivity.this, R.string.evaluation_failed);
						}
					}
					break;
			}
		};
	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 去除头部
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_feedbcak_evaluation);
		initView();
		initListener();
		logic();
		BaseApp.addActivity(this);
	}

	/**
	 * 方法名: initView() 
	 * 功 能 : 初始化 
	 * 参 数 : void 
	 * 返回值: void
	 */
	private void initView() {	
		tv_title = (TextView) findViewById(R.id.tv_title);
		tv_search = (TextView) findViewById(R.id.tv_sech);
		btn_back = (Button) findViewById(R.id.btn_back);
		tv_list_title = (TextView) findViewById(R.id.tv_list_title);
		ed_description = (EditText) findViewById(R.id.ed_description);
		tv_s1 = (TextView) findViewById(R.id.tv_s1);
		tv_s2 = (TextView) findViewById(R.id.tv_s2); 
		tv_s3 = (TextView) findViewById(R.id.tv_s3); 
		tv_s4 = (TextView) findViewById(R.id.tv_s4);
		tv_s5 = (TextView) findViewById(R.id.tv_s5);
		tv_commit = (TextView) findViewById(R.id.tv_commit);
		tv_score = (TextView) findViewById(R.id.tv_score);
		rb_yes = (RadioButton) findViewById(R.id.rb_yes); 
		rb_no = (RadioButton) findViewById(R.id.rb_no); 
	}
	
	/**
	 * 方法名: initListener() 
	 * 功 能 : 初始化 监听器 
	 * 参 数 : void 
	 * 返回值: void  
	 */
	private void initListener() {
		btn_back.setOnClickListener(this);
		tv_search.setOnClickListener(this);
		tv_s1.setOnClickListener(this);
		tv_s2.setOnClickListener(this);
		tv_s3.setOnClickListener(this); 
		tv_s4.setOnClickListener(this);
		tv_s5.setOnClickListener(this);
		rb_yes.setOnClickListener(this); 
		rb_no.setOnClickListener(this);
		tv_commit.setOnClickListener(this);
	}
	/**
	 * 方法名: logic() 
	 * 功 能 : 业务逻辑 
	 * 参 数 : void 
	 * 返回值: void  
	 */
	private void logic(){
		tv_title.setText(getString(R.string.question_feedback_evaluation));
		tv_search.setVisibility(View.GONE);
		
		title = getIntent().getStringExtra("title");
		code = getIntent().getStringExtra("code");
		
		tv_list_title.setText(title);
		tv_score.setText("5" + getString(R.string.branch));
	}
	
	/**
	 * 方法名: receiveMailQuest() 
	 * 功 能 : 请求报文
	 * 参 数 : void 
	 * 返回值: void
	 */
	private String receiveMailQuest(){
		String appraise = ed_description.getText().toString();
		
		PublicBean bean = new PublicBean();
		bean.setDeviceId(BaseApp.macAddr);
		bean.setUserAccount(BaseApp.user.getUserId());
		bean.setIsHandled(type+"");
		bean.setCode(code);
		bean.setAppraise(appraise);
		bean.setScore(evaluation+"");
		
		return JSON.toJSONString(bean);
	}
	
	/**
	* @Title: requestServer
	* @Description: 发送请求报文
	* @param void
	* @return void
	* @throws
	*/
	private void requestServer(int server, String info) {
		// 判断网络是否连接
		if (!NetWorkUtil.isNetworkAvailable(DormitoryEvaluationedActivity.this)) {
			DialogUtil.showLongToast(DormitoryEvaluationedActivity.this, R.string.global_network_disconnected);
		}else{
			SendRequest.sendSubmitRequest(DormitoryEvaluationedActivity.this, info, BaseApp.token, BaseApp.reqLang, server, BaseApp.key, submitCallBack);
		}
	}
	
	/**
	 * http请求回调
	 */
	RequestCallBack<String> submitCallBack = new RequestCallBack<String>() {

		@Override
		public void onStart() {
			DialogUtil.showWithCancelProgressDialog(DormitoryEvaluationedActivity.this, null, getResources().getString(R.string.global_prompt_message), null);
		}

		@Override
		public void onLoading(long total, long current, boolean isUploading) {

		}
		
		@Override
		public void onSuccess(ResponseInfo<String> responseInfo) {     
			DialogUtil.closeDialog();
			String result = responseInfo.result.toString();
			if (null != result && !"".equals(result.trim())) {
				ReqMessage msg = FastJSONUtil.getJSONToEntity(result, ReqMessage.class);
				// 判断返回标识状态是否为空
				if (null == msg || null == msg.getResCode() || "".equals(msg.getResCode())) {				
					LogUtil.promptInfo(DormitoryEvaluationedActivity.this, ErrorCodeContrast.getErrorCode("0", getResources()));
					return;
				// 验证不合法
				}else if ("1103".equals(msg.getResCode()) || "1104".equals(msg.getResCode())) {
					LogUtil.promptInfo(DormitoryEvaluationedActivity.this, ErrorCodeContrast.getErrorCode(msg.getResCode(), getResources()));	
					Intent mIntent = new Intent(BroadcastNotice.USER_EXIT);
					DormitoryEvaluationedActivity.this.sendBroadcast(mIntent);
					return;
				}else if("1210".equals(msg.getResCode())){
					LogUtil.promptInfo(DormitoryEvaluationedActivity.this, ErrorCodeContrast.getErrorCode(msg.getResCode(), getResources()));	
					Intent mIntent = new Intent(BroadcastNotice.WIPE_USET);
					sendBroadcast(mIntent);
					return;
				}else if (!"1".equals(msg.getResCode())) {
					LogUtil.promptInfo(DormitoryEvaluationedActivity.this, ErrorCodeContrast.getErrorCode(msg.getResCode(), getResources()));	
					return;
				}
				
				// 判断消息体是否为空
				if (null != msg.getMessage() && !"".equals(msg.getMessage().trim())) {		 
					// 获取解密后并校验后的值
					String decode = EncryptUtil.getDecodeData(msg.getMessage(), BaseApp.key);
					Message msgs = new Message();
					msgs.what = 1;
					msgs.obj = decode;
					mHandler.sendMessage(msgs);
				} 
			} else {
				LogUtil.promptInfo(DormitoryEvaluationedActivity.this, ErrorCodeContrast.getErrorCode("0", getResources()));
			}
		}

		@Override
		public void onFailure(HttpException error, String msg) {
			DialogUtil.closeDialog();
			if(null != msg && msg.equals(LocalConstants.API_KEY)){
				LogUtil.promptInfo(DormitoryEvaluationedActivity.this, ErrorCodeContrast.getErrorCode("1207", getResources()));
			}else{
				LogUtil.promptInfo(DormitoryEvaluationedActivity.this, ErrorCodeContrast.getErrorCode("0", getResources()));
			}
		}
	};
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.btn_back:
				this.finish();
				break;
			case R.id.tv_s1:
				evaluation = 1;
				setEvaluation();
				break;
			case R.id.tv_s2:
				evaluation = 2;
				setEvaluation();
				break;
			case R.id.tv_s3:
				evaluation = 3;
				setEvaluation();
				break;
			case R.id.tv_s4:
				evaluation = 4;
				setEvaluation();
				break;
			case R.id.tv_s5:
				evaluation = 5;
				setEvaluation();
				break;
			case R.id.rb_yes:
				type = 1;
				break;
			case R.id.rb_no:
				type = 0;
				break;
			case R.id.tv_commit:
				String info = receiveMailQuest();
				if(null != info && !"".equals(info)){
					requestServer(LocalConstants.FEEDBACK_EVALUATION_SERVER, info);
				}
				break;
		}
	}
	/**
	 * 设置评分
	 * 
	 * 
	 */
	@SuppressLint("NewApi")
	private void setEvaluation(){
		switch (evaluation) {
			case 1:
				tv_s1.setBackgroundResource(R.drawable.favourites);
				tv_s2.setBackgroundResource(R.drawable.favourites_b);	
				tv_s3.setBackgroundResource(R.drawable.favourites_b);
				tv_s4.setBackgroundResource(R.drawable.favourites_b);
				tv_s5.setBackgroundResource(R.drawable.favourites_b);
				break;
			case 2:
				tv_s1.setBackgroundResource(R.drawable.favourites);
				tv_s2.setBackgroundResource(R.drawable.favourites);		
				tv_s3.setBackgroundResource(R.drawable.favourites_b);
				tv_s4.setBackgroundResource(R.drawable.favourites_b);
				tv_s5.setBackgroundResource(R.drawable.favourites_b);
				break;
			case 3:
				tv_s1.setBackgroundResource(R.drawable.favourites);
				tv_s2.setBackgroundResource(R.drawable.favourites);	
				tv_s3.setBackgroundResource(R.drawable.favourites);
				tv_s4.setBackgroundResource(R.drawable.favourites_b);
				tv_s5.setBackgroundResource(R.drawable.favourites_b);
				break;
			case 4:
				tv_s1.setBackgroundResource(R.drawable.favourites);
				tv_s2.setBackgroundResource(R.drawable.favourites);	
				tv_s3.setBackgroundResource(R.drawable.favourites);
				tv_s4.setBackgroundResource(R.drawable.favourites);
				tv_s5.setBackgroundResource(R.drawable.favourites_b);
				break;
			case 5:
				tv_s1.setBackgroundResource(R.drawable.favourites);
				tv_s2.setBackgroundResource(R.drawable.favourites);	
				tv_s3.setBackgroundResource(R.drawable.favourites);
				tv_s4.setBackgroundResource(R.drawable.favourites);
				tv_s5.setBackgroundResource(R.drawable.favourites);
				break;
		}
		tv_score.setText(evaluation + getString(R.string.branch));
	}
	
	@Override
	protected void onResume() {
		super.onResume();
	}
		
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		
	};	
}