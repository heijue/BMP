package com.k2.mobile.app.controller.activity.menu.task;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import com.alibaba.fastjson.JSON;
import com.k2.mobile.app.R;
import com.k2.mobile.app.common.config.BroadcastNotice;
import com.k2.mobile.app.common.config.LocalConstants;
import com.k2.mobile.app.common.exception.HttpException;
import com.k2.mobile.app.controller.activity.menu.other.SearchActivity;
import com.k2.mobile.app.controller.core.BaseActivity;
import com.k2.mobile.app.controller.core.BaseApp;
import com.k2.mobile.app.model.bean.ReqMessage;
import com.k2.mobile.app.model.bean.ResPublicBean;
import com.k2.mobile.app.model.bean.TaskBean;
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
import com.k2.mobile.app.view.wheel.StrericWheelAdapter;
import com.k2.mobile.app.view.wheel.WheelView;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.animation.AnticipateOvershootInterpolator;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;

/**
 * @Title: TaskBean.java
 * @Package com.oppo.mo.model.bean
 * @Description: 添加我的任务
 * @Company:广东欧泊移动通讯有限公司
 * 
 * @author linqijun
 * @date 2015-03-30 18:18:00
 * @version V1.0
 */
public class AddTaskActivity extends BaseActivity implements OnClickListener {

	private TextView tv_title;
	private Button btn_back;
	private Button btn_commit;
//	private Button btn_time_choose;
	private TextView tv_confirm;
	private TextView tv_cancel;
	
	private Button btn_execute;
	private RadioButton rb_yourself;
	private RadioButton rb_others;
	private EditText ed_execute;
	private TextView tv_date_time;
	private TextView tv_now_time;
	private EditText ed_task_titlen;
	private EditText ed_task_description;
	private TextView tv_sech;
	private String execute;
	private WheelView yearWheel, monthWheel, dayWheel, hourWheel, minuteWheel, secondWheel;
	public static String[] yearContent = null;
	public static String[] monthContent = null;
	public static String[] dayContent = null;
	public static String[] hourContent = null;
	public static String[] minuteContent = null;
	public static String[] secondContent = null;
	private AlertDialog dialog = null;

	private Intent mIntent = null;
	// 用户名
	private String userName;
	// 工号
	private String userAccount;
	private int type = 1;
	private int taskType = 2;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 去除头部
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_add_my_taskl);
		initView();
		initListener();
		BaseApp.addActivity(this);
	}

	/**
	 * 方法名: initView() 
	 * 功 能 : 
	 * 初始化 参 数 : void 
	 * 返回值: void
	 */
	private void initView() {
		tv_title = (TextView) findViewById(R.id.tv_title);
		btn_back = (Button) findViewById(R.id.btn_back);
		tv_sech = (TextView) findViewById(R.id.tv_sech);
		rb_yourself = (RadioButton) findViewById(R.id.rb_yourself);
	    rb_others  = (RadioButton) findViewById(R.id.rb_others);
		
		btn_commit = (Button) findViewById(R.id.btn_commit);
//		btn_time_choose = (Button) findViewById(R.id.btn_time_choose);
		btn_execute = (Button) findViewById(R.id.btn_execute);
		ed_execute = (EditText) findViewById(R.id.ed_execute);
		tv_date_time = (TextView) findViewById(R.id.tv_date_time);
		tv_now_time = (TextView) findViewById(R.id.tv_now_time);
		ed_task_titlen = (EditText) findViewById(R.id.ed_task_titlen);
		ed_task_description = (EditText) findViewById(R.id.ed_task_description);

		tv_title.setText(getResources().getString(R.string.add_task));
		tv_sech.setVisibility(View.GONE);
		ed_execute.setEnabled(false);
		
		tv_date_time.setText(DateUtil.getDate_ymd());
		tv_now_time.setText(DateUtil.getDate_hms());
		rb_others.setChecked(true);
		
		initContent();
	}

	/**
	 * 方法名: initListener() 
	 * 功 能 : 
	 * 初始化 参 数 : void 
	 * 返回值: void
	 */
	private void initListener() {
		btn_execute.setOnClickListener(this);
//		btn_time_choose.setOnClickListener(this);
		tv_date_time.setOnClickListener(this);
		tv_now_time.setOnClickListener(this);
		btn_commit.setOnClickListener(this);
		btn_back.setOnClickListener(this);
		rb_yourself.setOnClickListener(this);
	    rb_others.setOnClickListener(this);
	}

	/**
	 * 方法名: initContent() 
	 * 功 能 : 
	 * 初始化 参 数 : void 
	 * 返回值: void
	 */
	public void initContent() {
		// 添加年
		yearContent = new String[10];
		for (int i = 0; i < 10; i++)
			yearContent[i] = String.valueOf(i + 2013);
		// 添加月
		monthContent = new String[12];
		for (int i = 0; i < 12; i++) {
			monthContent[i] = String.valueOf(i + 1);
			if (monthContent[i].length() < 2) {
				monthContent[i] = "0" + monthContent[i];
			}
		}
		// 添加天
		dayContent = new String[31];
		for (int i = 0; i < 31; i++) {
			dayContent[i] = String.valueOf(i + 1);
			if (dayContent[i].length() < 2) {
				dayContent[i] = "0" + dayContent[i];
			}
		}
		// 添加小时
		hourContent = new String[24];
		for (int i = 0; i < 24; i++) {
			hourContent[i] = String.valueOf(i);
			if (hourContent[i].length() < 2) {
				hourContent[i] = "0" + hourContent[i];
			}
		}
		// 添加分
		minuteContent = new String[60];
		for (int i = 0; i < 60; i++) {
			minuteContent[i] = String.valueOf(i);
			if (minuteContent[i].length() < 2) {
				minuteContent[i] = "0" + minuteContent[i];
			}
		}
		// 秒
		secondContent = new String[60];
		for (int i = 0; i < 60; i++) {
			secondContent[i] = String.valueOf(i);
			if (secondContent[i].length() < 2) {
				secondContent[i] = "0" + secondContent[i];
			}
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_back:
			startActivity(MyTaskActivity.class);
			finish();
			break;
		case R.id.rb_yourself:
			rb_yourself.setChecked(true);
			rb_others.setChecked(false);
			btn_execute.setEnabled(false);
			break;
		case R.id.rb_others:
			rb_yourself.setChecked(false);
			rb_others.setChecked(true);
			btn_execute.setEnabled(true);
			break;
		case R.id.tv_confirm:			// 选择时间
			StringBuffer sb = new StringBuffer();
			if(1 == type){
				sb.append(yearWheel.getCurrentItemValue())
				.append("-")
				.append(monthWheel.getCurrentItemValue())
				.append("-")
				.append(dayWheel.getCurrentItemValue());
				tv_date_time.setText(sb);
			}else if(2 == type){
				sb.append(hourWheel.getCurrentItemValue())
				.append(":")
				.append(minuteWheel.getCurrentItemValue());
				tv_now_time.setText(sb);
			}
		case R.id.tv_cancel:
			if(null != dialog){
				dialog.dismiss();
			}
			break;
		case R.id.btn_commit:
			String info = checkValue();
			if (info != null) {
				// 判断网络是否连接
				if (!NetWorkUtil.isNetworkAvailable(AddTaskActivity.this)) {
					DialogUtil.showLongToast(AddTaskActivity.this, R.string.global_network_disconnected);
				} else {
					// 查询我的所有工作任务
					if (null != info) {
						SendRequest.sendSubmitRequest(AddTaskActivity.this, info, BaseApp.token, BaseApp.reqLang,
								LocalConstants.ADD_TASK_SERVER, BaseApp.key, submitCallBack);
					}
				}
			}
			break;
		case R.id.tv_now_time:
			type = 2;
			setTime(R.layout.time_hm);
			break;
		case R.id.tv_date_time:
			type = 1;
			setTime(R.layout.time_ymd);
			break;
		case R.id.btn_execute:
			mIntent = new Intent(AddTaskActivity.this, SearchActivity.class);
			mIntent.putExtra("operType", 1);
			startActivityForResult(mIntent, 1);
			break;
		}
	}
	// 
	private void setTime(int layouts){
		LayoutInflater inflater = LayoutInflater.from(AddTaskActivity.this);
		View view = inflater.inflate(layouts, null);
		Calendar calendar = Calendar.getInstance();
		if(1 == type){
			int curYear = calendar.get(Calendar.YEAR);
			int curMonth = calendar.get(Calendar.MONTH) + 1;
			int curDay = calendar.get(Calendar.DAY_OF_MONTH);
			
			yearWheel = (WheelView) view.findViewById(R.id.yearwheel);
			monthWheel = (WheelView) view.findViewById(R.id.monthwheel);
			dayWheel = (WheelView) view.findViewById(R.id.daywheel);
			
			yearWheel.setAdapter(new StrericWheelAdapter(yearContent));
			yearWheel.setCurrentItem(curYear - 2013);
			yearWheel.setCyclic(true);
			yearWheel.setInterpolator(new AnticipateOvershootInterpolator());

			monthWheel.setAdapter(new StrericWheelAdapter(monthContent));

			monthWheel.setCurrentItem(curMonth - 1);

			monthWheel.setCyclic(true);
			monthWheel.setInterpolator(new AnticipateOvershootInterpolator());

			dayWheel.setAdapter(new StrericWheelAdapter(dayContent));
			dayWheel.setCurrentItem(curDay - 1);
			dayWheel.setCyclic(true);
			dayWheel.setInterpolator(new AnticipateOvershootInterpolator());
			
		}else if(2 == type){
			int curHour = calendar.get(Calendar.HOUR_OF_DAY);
			int curMinute = calendar.get(Calendar.MINUTE);
			
			hourWheel = (WheelView) view.findViewById(R.id.hourwheel);
			minuteWheel = (WheelView) view.findViewById(R.id.minutewheel);
			
			hourWheel.setAdapter(new StrericWheelAdapter(hourContent));
			hourWheel.setCurrentItem(curHour);
			hourWheel.setCyclic(true);
			hourWheel.setInterpolator(new AnticipateOvershootInterpolator());
	
			minuteWheel.setAdapter(new StrericWheelAdapter(minuteContent));
			minuteWheel.setCurrentItem(curMinute);
			minuteWheel.setCyclic(true);
			minuteWheel.setInterpolator(new AnticipateOvershootInterpolator());
		}
		
		tv_confirm = (TextView) view.findViewById(R.id.tv_confirm);
		tv_cancel = (TextView) view.findViewById(R.id.tv_cancel);
		
		tv_confirm.setOnClickListener(this);
		tv_cancel.setOnClickListener(this);
		
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setView(view);
		
		dialog = builder.show();
	}

	/**
	 * @Title: checkValue
	 * @Description: 检测约束值
	 * @param void
	 * @return String 返回的数据
	 * @throws
	 */
	@SuppressLint("SimpleDateFormat")
	private String checkValue() {
		String title = ed_task_titlen.getText().toString();
		String description = ed_task_description.getText().toString();
		String startTime = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(new Date());
		StringBuilder sb = new StringBuilder();
		sb.append(tv_date_time.getText().toString())
		  .append(" ")
		  .append(tv_now_time.getText().toString())
		  .append(":01");
		
		if(rb_yourself.isChecked()){
			execute = BaseApp.user.getUserId();
			taskType = 2;
		}else{
			taskType = 1;
		}

		if (null == title || "".equals(title)) {
			DialogUtil.showLongToast(AddTaskActivity.this, R.string.title_not_null);
			return null;
		} else if (null == description || "".equals(description)) {
			DialogUtil.showLongToast(AddTaskActivity.this, R.string.description_not_null);
			return null;
		} else if (null == execute || "".equals(execute)) {
			DialogUtil.showLongToast(AddTaskActivity.this, R.string.execute_not_null);
			return null;
		}

		int timeCheck = DateUtil.timeComparingSize(startTime, sb.toString());
		if (-1 == timeCheck) {
			DialogUtil.showLongToast(AddTaskActivity.this, R.string.time_format_error);
			return null;
		} else if (0 == timeCheck) {
			DialogUtil.showLongToast(AddTaskActivity.this, R.string.time_can_not);
			return null;
		} else if (1 == timeCheck) {
			DialogUtil.showLongToast(AddTaskActivity.this, R.string.start_now_time_err);
			return null;
		}

		TaskBean bean = new TaskBean();
		bean.setUserId(BaseApp.user.getUserId());
		bean.setTaskTitle(title);
		bean.setExecuteName(execute);
		bean.setLastDueTime(sb.toString());
		;
		bean.setContent(description);
		bean.setTaskType(""+taskType);

		return JSON.toJSONString(bean);
	}

	/**
	 * http请求回调
	 */
	RequestCallBack<String> submitCallBack = new RequestCallBack<String>() {

		@Override
		public void onStart() {
			DialogUtil.showWithCancelProgressDialog(AddTaskActivity.this, null, getResources().getString(R.string.global_prompt_message),null);
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
					LogUtil.promptInfo(AddTaskActivity.this, ErrorCodeContrast.getErrorCode("0", getResources()));
					return;
					// 验证不合法
				}else if ("1103".equals(msg.getResCode()) || "1104".equals(msg.getResCode())) {
					LogUtil.promptInfo(AddTaskActivity.this, ErrorCodeContrast.getErrorCode(msg.getResCode(), getResources()));
					Intent mIntent = new Intent(BroadcastNotice.USER_EXIT);
					sendBroadcast(mIntent);
					return;
				}else if("1210".equals(msg.getResCode())){
					LogUtil.promptInfo(AddTaskActivity.this, ErrorCodeContrast.getErrorCode(msg.getResCode(), getResources()));	
					Intent mIntent = new Intent(BroadcastNotice.WIPE_USET);
					sendBroadcast(mIntent);
					return;
				}else if (!"1".equals(msg.getResCode())) {
					LogUtil.promptInfo(AddTaskActivity.this, ErrorCodeContrast.getErrorCode(msg.getResCode(), getResources()));
					return;
				}
				// 判断消息体是否为空
				if (null == msg.getMessage() || "".equals(msg.getMessage().trim())) {
					LogUtil.promptInfo(AddTaskActivity.this, ErrorCodeContrast.getErrorCode("0", getResources()));
				} else {
					// 获取解密后并校验后的值
					String decode = EncryptUtil.getDecodeData(msg.getMessage(), BaseApp.key);
					if (null != decode && !"".equals(decode.trim())) {
						ResPublicBean bean = JSON.parseObject(decode, ResPublicBean.class);
						if (null != bean && "1".equals(bean.getSuccess())) {
							DialogUtil.showLongToast(AddTaskActivity.this, R.string.global_add_new_status_success);
							startActivity(MyTaskActivity.class);
							finish();
						} else {
							DialogUtil.showLongToast(AddTaskActivity.this, R.string.global_add_new_status_failed);
						}
					} else {
						DialogUtil.showLongToast(AddTaskActivity.this, R.string.global_add_new_status_failed);
					}
				}
			} else {
				LogUtil.promptInfo(AddTaskActivity.this, ErrorCodeContrast.getErrorCode("0", getResources()));
			}
		}

		@Override
		public void onFailure(HttpException error, String msg) {
			DialogUtil.closeDialog();
			if(null != msg && msg.equals(LocalConstants.API_KEY)){
				LogUtil.promptInfo(AddTaskActivity.this, ErrorCodeContrast.getErrorCode("1207", getResources()));
			}else{
				LogUtil.promptInfo(AddTaskActivity.this, ErrorCodeContrast.getErrorCode("0", getResources()));
			}
		}
	};

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		if (requestCode == 1) {
			if (data != null) {
				userName = data.getExtras().getString("userName");
				userAccount = data.getExtras().getString("userAccount");
				ed_execute.setText(userName + "(" + userAccount + ")");
				execute = userAccount;
			}
		}

		super.onActivityResult(requestCode, resultCode, data);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			startActivity(MyTaskActivity.class);
			overridePendingTransition(R.anim.push_right_in, R.anim.push_left_out);
			finish();
		}

		return false;
	}
}