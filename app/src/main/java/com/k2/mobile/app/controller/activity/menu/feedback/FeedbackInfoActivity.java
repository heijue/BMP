package com.k2.mobile.app.controller.activity.menu.feedback;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.alibaba.fastjson.JSON;
import com.k2.mobile.app.R;
import com.k2.mobile.app.common.config.BroadcastNotice;
import com.k2.mobile.app.common.config.LocalConstants;
import com.k2.mobile.app.common.exception.HttpException;
import com.k2.mobile.app.controller.core.BaseApp;
import com.k2.mobile.app.model.adapter.GridViewShowImgAdapter;
import com.k2.mobile.app.model.bean.FeedbackListBean;
import com.k2.mobile.app.model.bean.FileBean;
import com.k2.mobile.app.model.bean.PublicBean;
import com.k2.mobile.app.model.bean.ReqMessage;
import com.k2.mobile.app.model.bean.ResPublicBean;
import com.k2.mobile.app.model.http.ResponseInfo;
import com.k2.mobile.app.model.http.callback.RequestCallBack;
import com.k2.mobile.app.model.http.other.SendRequest;
import com.k2.mobile.app.utils.DateUtil;
import com.k2.mobile.app.utils.DialogUtil;
import com.k2.mobile.app.utils.EncryptUtil;
import com.k2.mobile.app.utils.ErrorCodeContrast;
import com.k2.mobile.app.utils.FastJSONUtil;
import com.k2.mobile.app.utils.LogUtil;
import com.k2.mobile.app.utils.NetWorkUtil;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

/**
 * 
 * @ClassName: FeedbackInfoActivity
 * @Description: 问题反馈详情
 * @author linqijun
 * @date 2015-6-08 14:24:00
 * 
 */
public class FeedbackInfoActivity extends FragmentActivity implements OnClickListener {
	
	// 返回
	private Button btn_back;
	// 头部标题
	private TextView tv_title;
	private TextView tv_search;
	
	private TextView tv_number; 				// 编号
	private TextView tv_state;  				// 状态
	private TextView tv_job_number; 			// 工号
	private TextView tv_user_name;  			// 姓名
	private TextView tv_department;	 			// 部门 
	private TextView tv_contact_phone;			// 联系电话
	
	private TextView tv_question_class; 		// 问题类别
	private TextView tv_feedback_time;			// 反馈时间
	private TextView tv_question_summary; 		// 问题摘要
	private TextView tv_question_description; 	// 问题描述
	private GridView gv_img_show; 				// 图片展示
	private TextView tv_somebody;				// 指派人 
	private TextView tv_somebody_time;			// 指派时间
	private TextView tv_question_type;			// 问题类型
	private TextView tv_commitment_time;		// 承诺时间
	private TextView tv_deal_peoplee;			// 处理人
	private TextView tv_actual_time;			// 处理时间
	private TextView tv_remark;					// 备注
	
	private TextView tv_whether_solve;			// 是否解决
	private TextView tv_satisfaction_rate;		// 满意打分
	private TextView tv_evaluation_time;		// 评价时间
	private TextView tv_evaluation_title;		// 评价内容
	
	private TextView tv_actual_time_tip;		// 处理时间提示
	private TextView tv_commitment_time_tip;	// 承诺时间提示
	
	private RadioButton rb_yes, rb_no;			// 问题是否解决
	private EditText ed_description;			// 输入评价的内容
	
	private TextView tv_commit;					// 左边按钮
	private TextView tv_close;					// 右边按钮
	
	private TextView tv_s1, tv_s2, tv_s3, tv_s4, tv_s5;
	private LinearLayout staff_info, content_info, troub_info, evaluation_info, evaluation, ll_button;
	
	private String flag = null;
	private String code = null;
	private int status = 0;
	private int type = 1;		// 区别是否已解决
	private int score = 5;		// 评分
	// 文件集合
	private List<FileBean> fileList = null;
	private GridViewShowImgAdapter gvAdapter = null;
	// 图片集合
	private List<View> views = new ArrayList<View>();
	private FeedbackListBean bean = null;
	private Intent mIntent = null;
	private int operType = 1;
	
	private Handler mHandler = new Handler(){
		@Override
		public void dispatchMessage(Message msg) {
			switch (msg.what) {
				case 1:
					String resJson = (String) msg.obj;
					System.out.println("resJson = "+resJson);
					if (null != resJson && !"".equals(resJson.trim())) {
						bean = JSON.parseObject(resJson, FeedbackListBean.class);
						if (null != bean) {
							if (null != bean.getAttachmentList()) {
								if(null != fileList){
									fileList.clear();
								}
								fileList.addAll(JSON.parseArray(bean.getAttachmentList(), FileBean.class));
								gvAdapter.notifyDataSetChanged();
							}
							
							tv_number.setText(bean.getQuestionFeedbackNumber());
							tv_job_number.setText(bean.getCreatorCode());
							tv_user_name.setText(bean.getCreatorName());
							tv_department.setText(bean.getCreatorDepartment());
							tv_contact_phone.setText(bean.getCreatorPhoneNum());
							
							tv_question_class.setText(bean.getQuestionCategoryName());
							if(null != bean.getCreateDatetime() && !"".equals(bean.getCreateDatetime().trim())){
								String tmpTiem = bean.getCreateDatetime().replace("T", " ");
								tv_feedback_time.setText(DateUtil.getDate_ymdhm(tmpTiem));
							}
							tv_question_summary.setText(bean.getQuestionAbstract());
							tv_question_description.setText(bean.getQuestionDescription());
							
							tv_somebody.setText(bean.getReviewerName());
							if(null != bean.getReviewDatetime() && !"".equals(bean.getReviewDatetime().trim())){
								String tmpTiem = bean.getReviewDatetime().replace("T", " ");
								tv_somebody_time.setText(DateUtil.getDate_ymdhm(tmpTiem));
							}
							tv_question_type.setText(bean.getQuestionClassName());
							
							if(null != bean.getFinishEstimateTime() && !"".equals(bean.getFinishEstimateTime().trim())){
								String tmpTiem = bean.getFinishEstimateTime().replace("T", " ");
								tv_commitment_time.setText(DateUtil.getDate_ymdhm(tmpTiem));
							}
							
							tv_deal_peoplee.setText(bean.getHandlerName());
							if(null != bean.getHandleFactTime() && !"".equals(bean.getHandleFactTime().trim())){
								String tmpTiem = bean.getHandleFactTime().replace("T", " ");
								tv_actual_time.setText(DateUtil.getDate_ymdhm(tmpTiem));
							}
							tv_remark.setText(bean.getRemark());
							
							if(null != bean.getHandleResult() && !"".equals(bean.getHandleResult())){
								if("1".equals(bean.getHandleResult())){
									tv_whether_solve.setText(getString(R.string.txt_yes));
								}else{
									tv_whether_solve.setText(getString(R.string.txt_no));
								}
							}
							
							if(null == bean.getAppraiseScore() || "".equals(bean.getAppraiseScore().trim())){
								tv_satisfaction_rate.setText("0" + getString(R.string.branch));
							}else{
								tv_satisfaction_rate.setText(bean.getAppraiseScore() + getString(R.string.branch));
							}
							
							if(null != bean.getAppraiseDatetime() && !"".equals(bean.getAppraiseDatetime().trim())){
								String tmpTiem = bean.getAppraiseDatetime().replace("T", " ");
								tv_evaluation_time.setText(DateUtil.getDate_ymdhm(tmpTiem));
							}
							tv_evaluation_title.setText(bean.getAppraiseContent());
						}
					}
					break;
				case 2:
					String resSave = (String) msg.obj;
					if (null != resSave) {
						ResPublicBean bean = JSON.parseObject(resSave, ResPublicBean.class);
						if (null != bean && "1".equals(bean.getSuccess())) {
							DialogUtil.showLongToast(FeedbackInfoActivity.this, R.string.evaluation_success);
							Intent mIntent = new Intent(BroadcastNotice.FEEDBACK_FINISH_UPDATE);
							sendBroadcast(mIntent);
							finish();
						} else {
							DialogUtil.showLongToast(FeedbackInfoActivity.this, R.string.evaluation_failed);
						}
					}
					break;
				case 3:
					String resClose = (String) msg.obj;
					if (null != resClose) {
						ResPublicBean bean = JSON.parseObject(resClose, ResPublicBean.class);
						if (null != bean && "1".equals(bean.getSuccess())) {
							DialogUtil.showLongToast(FeedbackInfoActivity.this, R.string.close_success);
							Intent mIntent = new Intent(BroadcastNotice.FEEDBACK_FINISH_UPDATE);
							sendBroadcast(mIntent);
							finish();
						} else {
							DialogUtil.showLongToast(FeedbackInfoActivity.this, R.string.close_failure);
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
		setContentView(R.layout.activity_feedbcak_info);
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
		tv_number = (TextView) findViewById(R.id.tv_number);
		tv_state = (TextView) findViewById(R.id.tv_state);
		
		tv_job_number = (TextView) findViewById(R.id.tv_job_number);
		tv_user_name = (TextView) findViewById(R.id.tv_user_name);
		tv_department = (TextView) findViewById(R.id.tv_department);	 			
		tv_contact_phone = (TextView) findViewById(R.id.tv_contact_phone);	 			
		tv_question_class = (TextView) findViewById(R.id.tv_question_class);
		tv_feedback_time = (TextView) findViewById(R.id.tv_feedback_time);
		tv_question_summary = (TextView) findViewById(R.id.tv_question_summary);
		tv_question_description = (TextView) findViewById(R.id.tv_question_description);
		tv_somebody = (TextView) findViewById(R.id.tv_somebody);
		tv_somebody_time = (TextView) findViewById(R.id.tv_somebody_time);
		tv_question_type = (TextView) findViewById(R.id.tv_question_type);
		tv_commitment_time = (TextView) findViewById(R.id.tv_commitment_time);
		tv_deal_peoplee = (TextView) findViewById(R.id.tv_deal_peoplee);
		tv_actual_time = (TextView) findViewById(R.id.tv_actual_time);
		tv_remark = (TextView) findViewById(R.id.tv_remark);
		
		tv_actual_time_tip = (TextView) findViewById(R.id.tv_actual_time_tip);
		tv_commitment_time_tip = (TextView) findViewById(R.id.tv_commitment_time_tip);
		
		ed_description = (EditText) findViewById(R.id.ed_description);
		
		rb_yes = (RadioButton) findViewById(R.id.rb_yes); 
		rb_no = (RadioButton) findViewById(R.id.rb_no); 
		
		tv_whether_solve = (TextView) findViewById(R.id.tv_whether_solve);
		tv_satisfaction_rate = (TextView) findViewById(R.id.tv_satisfaction_rate);
		tv_evaluation_time = (TextView) findViewById(R.id.tv_evaluation_time);
		tv_evaluation_title = (TextView) findViewById(R.id.tv_evaluation_title);
		
		gv_img_show = (GridView) findViewById(R.id.gv_img_show);
		
		tv_s1 = (TextView) findViewById(R.id.tv_s1);
		tv_s2 = (TextView) findViewById(R.id.tv_s2); 
		tv_s3 = (TextView) findViewById(R.id.tv_s3); 
		tv_s4 = (TextView) findViewById(R.id.tv_s4);
		tv_s5 = (TextView) findViewById(R.id.tv_s5);
		
		tv_commit = (TextView) findViewById(R.id.tv_commit);
		tv_close = (TextView) findViewById(R.id.tv_close);
		
		staff_info = (LinearLayout) findViewById(R.id.staff_info);
		content_info = (LinearLayout) findViewById(R.id.content_info); 
		troub_info = (LinearLayout) findViewById(R.id.troub_info);
		evaluation_info = (LinearLayout) findViewById(R.id.evaluation_info);
		evaluation = (LinearLayout) findViewById(R.id.evaluation);
		ll_button = (LinearLayout) findViewById(R.id.ll_button);
	}
	
	/**
	 * 方法名: initListener() 
	 * 功 能 : 初始化 监听器 
	 * 参 数 : void 
	 * 返回值: void  
	 */
	private void initListener() {
		btn_back.setOnClickListener(this);
		tv_s1.setOnClickListener(this);
		tv_s2.setOnClickListener(this);
		tv_s3.setOnClickListener(this); 
		tv_s4.setOnClickListener(this);
		tv_s5.setOnClickListener(this);
		rb_yes.setOnClickListener(this); 
		rb_no.setOnClickListener(this);
		tv_commit.setOnClickListener(this);
		tv_close.setOnClickListener(this);
		gv_img_show.setOnItemClickListener(itemListener);
	}
	/**
	 * 方法名: logic() 
	 * 功 能 : 业务逻辑 
	 * 参 数 : void 
	 * 返回值: void  
	 */
	private void logic(){
		
		tv_title.setText(getString(R.string.feedback_info));
		tv_search.setVisibility(View.GONE);
		initAdapter();
		
		flag = getIntent().getStringExtra("flag");
		code = getIntent().getStringExtra("code");
		status = getIntent().getIntExtra("status", 0);
		
		switch (status) {
			case 1:
				tv_state.setText(getString(R.string.create));
				break;
			case 2:
				tv_state.setText(getString(R.string.reviews));
				troub_info.setVisibility(View.GONE);
				evaluation_info.setVisibility(View.GONE);
				evaluation.setVisibility(View.GONE);
				ll_button.setVisibility(View.GONE);
				break;
			case 3:
				tv_state.setText(getString(R.string.prehandle));
				evaluation_info.setVisibility(View.GONE);
				evaluation.setVisibility(View.GONE);
				ll_button.setVisibility(View.GONE);
				tv_actual_time_tip.setVisibility(View.GONE);
				tv_commitment_time_tip.setVisibility(View.GONE);
				tv_commitment_time.setVisibility(View.GONE);
				tv_actual_time.setVisibility(View.GONE);
				break;
			case 4:
				tv_state.setText(getString(R.string.handling));
				tv_state.setText(getString(R.string.finished));
				evaluation_info.setVisibility(View.GONE);
				evaluation.setVisibility(View.GONE);
				ll_button.setVisibility(View.GONE);
				tv_actual_time_tip.setVisibility(View.GONE);
				tv_actual_time.setVisibility(View.GONE);
				break;
			case 5:
				tv_state.setText(getString(R.string.appraise));
				ll_button.setVisibility(View.VISIBLE);
				tv_commit.setVisibility(View.VISIBLE);
				evaluation_info.setVisibility(View.GONE);
				tv_commit.setText(getString(R.string.submit_evaluation));
				tv_close.setText(getString(R.string.global_cancel));
				break;
			case 6:
				tv_state.setText(getString(R.string.finished));
				evaluation.setVisibility(View.GONE);
				ll_button.setVisibility(View.GONE);
				break;
			case 7:
				tv_state.setText(getString(R.string.unabledo));
				ll_button.setVisibility(View.VISIBLE);
				tv_commit.setVisibility(View.VISIBLE);
				tv_close.setVisibility(View.VISIBLE);
				tv_commit.setText(getString(R.string.close));
				tv_close.setText(getString(R.string.global_cancel));
				evaluation_info.setVisibility(View.GONE);
				evaluation.setVisibility(View.GONE);
				break;
		}
		
		String info = receiveMailQuest();
		
		if(null != info && !"".equals(info)){
			requestServer(LocalConstants.FEEDBACK_INFO_SERVER, info);
		}
	}
	
	private void initAdapter(){
		if(null == fileList){
			fileList = new ArrayList<FileBean>();
		}
		gvAdapter = new GridViewShowImgAdapter(this, fileList);
		gv_img_show.setAdapter(gvAdapter);
	}
	
	OnItemClickListener itemListener = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			mIntent = new Intent(FeedbackInfoActivity.this, ShowImgActivity.class);
			mIntent.putExtra("fileList", (Serializable)fileList);
			mIntent.putExtra("position", position);
			startActivity(mIntent);		
		}
	};
	
	/**
	 * 方法名: receiveMailQuest() 
	 * 功 能 : 请求报文
	 * 参 数 : void 
	 * 返回值: void
	 */
	private String receiveMailQuest(){
		PublicBean bean = new PublicBean();
		bean.setDeviceId(BaseApp.macAddr);
		bean.setUserAccount(BaseApp.user.getUserId());
		bean.setFlag(flag);
		bean.setCode(code);
		
		return JSON.toJSONString(bean);
	}
	
	/**
	 * 方法名: receiveMailQuest() 
	 * 功 能 : 请求评价报文
	 * 参 数 : void 
	 * 返回值: void
	 */
	private String receiveEvaluation(){
		String appraise = ed_description.getText().toString();
		
		PublicBean bean = new PublicBean();
		bean.setDeviceId(BaseApp.macAddr);
		bean.setUserAccount(BaseApp.user.getUserId());
		bean.setIsHandled(type+"");
		bean.setCode(code);
		bean.setAppraise(appraise);
		bean.setScore(score+"");
		
		return JSON.toJSONString(bean);
	}
	/**
	 * 方法名: receiveMailQuest() 
	 * 功 能 : 请求报文
	 * 参 数 : void 
	 * 返回值: void
	 */
	private String receiveClose(String code, String cd){
		PublicBean bean = new PublicBean();
		bean.setDeviceId(BaseApp.macAddr);
		bean.setUserAccount(BaseApp.user.getUserId());
		bean.setOpFlag(cd);
		bean.setCode(code);
		
		return JSON.toJSONString(bean);
	}
	/**
	 * 设置评分
	 */
	@SuppressLint("NewApi")
	private void setEvaluation(int evaluation){
		score = evaluation;
		switch (evaluation) {
			case 0:
				tv_s1.setBackgroundResource(R.drawable.favourites_b);
				tv_s2.setBackgroundResource(R.drawable.favourites_b);	
				tv_s3.setBackgroundResource(R.drawable.favourites_b);
				tv_s4.setBackgroundResource(R.drawable.favourites_b);
				tv_s5.setBackgroundResource(R.drawable.favourites_b);
				break;
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
		if (!NetWorkUtil.isNetworkAvailable(FeedbackInfoActivity.this)) {
			DialogUtil.showLongToast(FeedbackInfoActivity.this, R.string.global_network_disconnected);
		}else{
			SendRequest.sendSubmitRequest(FeedbackInfoActivity.this, info, BaseApp.token, BaseApp.reqLang, server, BaseApp.key, submitCallBack);
		}
	}
	
	/**
	 * http请求回调
	 */
	RequestCallBack<String> submitCallBack = new RequestCallBack<String>() {

		@Override
		public void onStart() {
			DialogUtil.showWithCancelProgressDialog(FeedbackInfoActivity.this, null, getResources().getString(R.string.global_prompt_message), null);
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
					LogUtil.promptInfo(FeedbackInfoActivity.this, ErrorCodeContrast.getErrorCode("0", getResources()));
					return;
				// 验证不合法
				}else if ("1103".equals(msg.getResCode()) || "1104".equals(msg.getResCode())) {
					LogUtil.promptInfo(FeedbackInfoActivity.this, ErrorCodeContrast.getErrorCode(msg.getResCode(), getResources()));
					Intent mIntent = new Intent(BroadcastNotice.USER_EXIT);
					FeedbackInfoActivity.this.sendBroadcast(mIntent);
					return;
				}else if("1210".equals(msg.getResCode())){
					LogUtil.promptInfo(FeedbackInfoActivity.this, ErrorCodeContrast.getErrorCode(msg.getResCode(), getResources()));	
					Intent mIntent = new Intent(BroadcastNotice.WIPE_USET);
					sendBroadcast(mIntent);
					return;
				}else if (!"1".equals(msg.getResCode())) {
					LogUtil.promptInfo(FeedbackInfoActivity.this, ErrorCodeContrast.getErrorCode(msg.getResCode(), getResources()));	
					return;
				}
				
				// 判断消息体是否为空
				if (null != msg.getMessage() && !"".equals(msg.getMessage().trim())) {		 
					// 获取解密后并校验后的值
					String decode = EncryptUtil.getDecodeData(msg.getMessage(), BaseApp.key);
					Message msgs = new Message();
					msgs.what = operType;
					msgs.obj = decode;
					mHandler.sendMessage(msgs);
				} 
			} else {
				LogUtil.promptInfo(FeedbackInfoActivity.this, ErrorCodeContrast.getErrorCode("0", getResources()));
			}
		}

		@Override
		public void onFailure(HttpException error, String msg) {
			DialogUtil.closeDialog();
			if(null != msg && msg.equals(LocalConstants.API_KEY)){
				LogUtil.promptInfo(FeedbackInfoActivity.this, ErrorCodeContrast.getErrorCode("1207", getResources()));
			}else{
				LogUtil.promptInfo(FeedbackInfoActivity.this, ErrorCodeContrast.getErrorCode("0", getResources()));
			}
		}
	};
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.btn_back:
				this.finish();
				break;
			case R.id.tv_commit:
				if(5 == status){
					operType = 2;
					String info = receiveEvaluation();
					if(null != info && !"".equals(info)){
						requestServer(LocalConstants.FEEDBACK_EVALUATION_SERVER, info);
					}
				}else if(7 == status){
					AlertDialog mAlertDialog = new AlertDialog.Builder(FeedbackInfoActivity.this)
					.setTitle(getResources().getString(R.string.close))
					.setMessage(getResources().getString(R.string.whether_or_not))
					.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							operType = 3;
							String info = receiveClose(code, "1");
							if(null != info && !"".equals(info)){
								requestServer(LocalConstants.FEEDBACK_DEL_SERVER, info);
							}
						}
					}).setNegativeButton(android.R.string.cancel, null).show();
				}
				break;
			case R.id.tv_close:
				finish();
				break;
			case R.id.tv_s1:
				setEvaluation(1);
				break;
			case R.id.tv_s2:
				setEvaluation(2);
				break;
			case R.id.tv_s3:
				setEvaluation(3);
				break;
			case R.id.tv_s4:
				setEvaluation(4);
				break;
			case R.id.tv_s5:
				setEvaluation(5);
				break;
			case R.id.rb_yes:
				type = 1;
				break;
			case R.id.rb_no:
				type = 0;
				break;
		}
	}
	
	@Override
	protected void onResume() {
		super.onResume();
	}
		
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(RESULT_OK != resultCode){
			return;
		}
		
		if(1220 == requestCode){
			finish();
		}
	};
}