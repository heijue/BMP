package com.k2.mobile.app.controller.activity.menu.task;

import com.alibaba.fastjson.JSON;
import com.k2.mobile.app.R;
import com.k2.mobile.app.common.config.BroadcastNotice;
import com.k2.mobile.app.common.config.LocalConstants;
import com.k2.mobile.app.common.exception.HttpException;
import com.k2.mobile.app.controller.core.BaseActivity;
import com.k2.mobile.app.controller.core.BaseApp;
import com.k2.mobile.app.model.bean.ReqMessage;
import com.k2.mobile.app.model.bean.ResTaskBean;
import com.k2.mobile.app.model.bean.TaskBean;
import com.k2.mobile.app.model.http.ResponseInfo;
import com.k2.mobile.app.model.http.callback.RequestCallBack;
import com.k2.mobile.app.model.http.other.SendRequest;
import com.k2.mobile.app.utils.DialogUtil;
import com.k2.mobile.app.utils.EncryptUtil;
import com.k2.mobile.app.utils.ErrorCodeContrast;
import com.k2.mobile.app.utils.FastJSONUtil;
import com.k2.mobile.app.utils.LogUtil;
import com.k2.mobile.app.utils.NetWorkUtil;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.TextView;


/**
 * @Title: TaskInfoActivity.java
 * @Package com.oppo.mo.model.bean
 * @Description: 任务详情
 * @Company:广东欧泊移动通讯有限公司
 * 
 * @author linqijun	
 * @date 2015-4-1 9:28:00
 * @version V1.0
 */
public class TaskInfoActivity extends BaseActivity implements OnClickListener {
	
	private TextView tv_title;
	private TextView tv_content_title, tv_sech;
	private WebView wv_description;
	private TextView tv_completion_time;
	private TextView tv_assigned_to;
	private Button btn_back;
	// 任务ID
	private String id;
	
	private Handler mHandler = new Handler(){
		@Override
		public void dispatchMessage(Message msg) {
			switch (msg.what) {
				case 1:
					String json = (String) msg.obj;
					if (null != json && !"".equals(json.trim())) {
						ResTaskBean bean = JSON.parseObject(json, ResTaskBean.class);
						if(bean != null){
							tv_content_title.setText(bean.getWorkListItemTitle());
							wv_description.loadDataWithBaseURL(null, bean.getField1(), "text/html", "utf-8", null);
							tv_completion_time.setText(bean.getLastDueTime());
							tv_assigned_to.setText(bean.getActionerChsName());
						}
					} else {
						DialogUtil.showLongToast(TaskInfoActivity.this, R.string.data_incomplete);
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
		setContentView(R.layout.activity_task_info);
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
		
		tv_title = (TextView) findViewById(R.id.tv_title);
		tv_content_title = (TextView) findViewById(R.id.tv_content_title);
		tv_sech = (TextView) findViewById(R.id.tv_sech);
		wv_description = (WebView) findViewById(R.id.wv_description);
		tv_completion_time = (TextView) findViewById(R.id.tv_completion_time);
		tv_assigned_to = (TextView) findViewById(R.id.tv_assigned_to);
		btn_back = (Button) findViewById(R.id.btn_back);
		
		tv_title.setText(getString(R.string.task_info));
		tv_sech.setVisibility(View.GONE);
		
		id = getIntent().getStringExtra("id");
		
		// 查询用户菜单
		if (!NetWorkUtil.isNetworkAvailable(TaskInfoActivity.this)) {            
			DialogUtil.showLongToast(TaskInfoActivity.this, R.string.global_network_disconnected);
		}else{
			String json = queryTaskInfo();
			SendRequest.sendSubmitRequest(TaskInfoActivity.this, json, BaseApp.token, BaseApp.reqLang, 
					LocalConstants.QUERY_TASK_INFO_SERVER, BaseApp.key, submitCallBack);
		}
	}
		
	/**
	 * 方法名: initListener() 
	 * 功 能 : 初始化 监听器 
	 * 参 数 : void 
	 * 返回值: void  
	 */
	private void initListener() {
		btn_back.setOnClickListener(this);
	}
	
	/**
	 * http请求回调
	 */
	RequestCallBack<String> submitCallBack = new RequestCallBack<String>() {

		@Override
		public void onStart() {
			DialogUtil.showWithCancelProgressDialog(TaskInfoActivity.this, null, getResources().getString(R.string.global_prompt_message), null);
		}

		@Override
		public void onLoading(long total, long current, boolean isUploading) {

		}
		
		@Override
		public void onSuccess(ResponseInfo<String> responseInfo) {                                     
			DialogUtil.closeDialog();
			String result = responseInfo.result.toString();
			if (null != result && !"".equals(result.trim())) {
				ReqMessage msgs = FastJSONUtil.getJSONToEntity(result, ReqMessage.class);
				// 判断返回标识状态是否为空
				if (null == msgs || null == msgs.getResCode() || "".equals(msgs.getResCode())) {				
					LogUtil.promptInfo(TaskInfoActivity.this, ErrorCodeContrast.getErrorCode("0", getResources()));
					return;
				}else if ("1103".equals(msgs.getResCode()) || "1104".equals(msgs.getResCode())) {
					LogUtil.promptInfo(TaskInfoActivity.this, ErrorCodeContrast.getErrorCode(msgs.getResCode(), getResources()));	
					Intent mIntent = new Intent(BroadcastNotice.USER_EXIT);
					sendBroadcast(mIntent);
					return;
				}else if("1210".equals(msgs.getResCode())){
					LogUtil.promptInfo(TaskInfoActivity.this, ErrorCodeContrast.getErrorCode(msgs.getResCode(), getResources()));	
					Intent mIntent = new Intent(BroadcastNotice.WIPE_USET);
					sendBroadcast(mIntent);
					return;
				}else if ("1".equals(msgs.getResCode())) {
					String decode = EncryptUtil.getDecodeData(msgs.getMessage(), BaseApp.key);
					Message msg = new Message();
					msg.what = 1;
					msg.obj = decode;
					mHandler.sendMessage(msg);
				}
			} else {
				LogUtil.promptInfo(TaskInfoActivity.this, ErrorCodeContrast.getErrorCode("0", getResources()));
			}
		}

		@Override
		public void onFailure(HttpException error, String msg) {
			DialogUtil.closeDialog();
			if(null != msg && msg.equals(LocalConstants.API_KEY)){
				LogUtil.promptInfo(TaskInfoActivity.this, ErrorCodeContrast.getErrorCode("1207", getResources()));
			}else{
				LogUtil.promptInfo(TaskInfoActivity.this, ErrorCodeContrast.getErrorCode("0", getResources()));
			}
		}
	};
	
	/**
	 * @Title: queryTaskInfo
	 * @Description: 查询任务详情
	 * @param void
	 * @return String 返回的数据 
	 * @throws
	 */
	private String queryTaskInfo(){
		
		TaskBean bean = new TaskBean();
		bean.setDeviceId(BaseApp.macAddr);
		bean.setTaskId(id);
		
		return JSON.toJSONString(bean);
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.btn_back:
				finish();
				break;
		}
	}
	
}