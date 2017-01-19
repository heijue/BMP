package com.k2.mobile.app.controller.activity.menu.reimbursement;

import java.util.ArrayList;
import java.util.List;

import com.alibaba.fastjson.JSON;
import com.k2.mobile.app.R;
import com.k2.mobile.app.common.config.BroadcastNotice;
import com.k2.mobile.app.common.config.LocalConstants;
import com.k2.mobile.app.common.exception.HttpException;
import com.k2.mobile.app.controller.core.BaseActivity;
import com.k2.mobile.app.controller.core.BaseApp;
import com.k2.mobile.app.model.adapter.ExpenseClaimsAdapter;
import com.k2.mobile.app.model.bean.ExpenseClaimsBean;
import com.k2.mobile.app.model.bean.ReqMessage;
import com.k2.mobile.app.model.http.ResponseInfo;
import com.k2.mobile.app.model.http.callback.RequestCallBack;
import com.k2.mobile.app.model.http.other.SendRequest;
import com.k2.mobile.app.utils.DialogUtil;
import com.k2.mobile.app.utils.EncryptUtil;
import com.k2.mobile.app.utils.ErrorCodeContrast;
import com.k2.mobile.app.utils.FastJSONUtil;
import com.k2.mobile.app.utils.LogUtil;
import com.k2.mobile.app.utils.NetWorkUtil;
import com.k2.mobile.app.view.widget.XListView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

/**
 * @Title: ExpenseClaimsActivity.java
 * @Package com.oppo.mo.controller.activity.menu.workbench
 * @Description: 我的费用报销
 * @Company:广东欧泊移动通讯有限公司
 * 
 * @author linqijun	
 * @date 2015-03-31 8:28:00
 * @version V1.0
 */
public class ExpenseClaimsActivity extends BaseActivity implements OnClickListener {
	
	// 返回
	private Button btn_back;
	private TextView tv_username, tv_userid, tv_search;
	// 头部标题
	private TextView tv_title;
	// 记录
	private XListView lv_consumption = null;
	// 适配器
	private ExpenseClaimsAdapter pcAdapter = null;
	// 服务器返回数据集
	private List<ExpenseClaimsBean> beanList = null;
	
	private Handler mHandler = new Handler(){
		@Override
		public void dispatchMessage(Message msg) {
			switch (msg.what) {
				case 1:
					String json = (String) msg.obj;
					if (null != json &&  !"".equals(json.trim())) {
						beanList.clear();
						beanList.addAll(JSON.parseArray(json, ExpenseClaimsBean.class));
						pcAdapter.notifyDataSetChanged();
					}else{
						DialogUtil.showLongToast(ExpenseClaimsActivity.this, R.string.global_no_data);
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
		setContentView(R.layout.activity_menu_expense_claims);
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
	@SuppressLint("NewApi")
	private void initView() {		
		tv_title = (TextView) findViewById(R.id.tv_title);		
		tv_search = (TextView) findViewById(R.id.tv_sech); 
		btn_back = (Button) findViewById(R.id.btn_back);
		lv_consumption = (XListView) findViewById(R.id.lv_show_consumption);
		
		tv_username = (TextView) findViewById(R.id.tv_username);
		tv_userid = (TextView) findViewById(R.id.tv_userid);
		
		tv_username.setText(BaseApp.user.getUserName());
		tv_userid.setText(BaseApp.user.getUserId());
		// 上拉加载数据
		lv_consumption.setPullLoadEnable(false);
		// 下拉加载数据
		lv_consumption.setPullRefreshEnable(false);
		
		tv_search.setVisibility(View.GONE);
		
		// 设置头部提示
		tv_title.setText(getString(R.string.my_reimbursement));
		beanList = new ArrayList<ExpenseClaimsBean>();
		pcAdapter = new ExpenseClaimsAdapter(this, beanList);
		lv_consumption.setAdapter(pcAdapter);
		queryConsume();
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
	 * @Title: queryConsume
	 * @Description: 请求查询个人消费
	 * @param void
	 * @return void 
	 * @throws
	 */
	private void queryConsume(){
		if (!NetWorkUtil.isNetworkAvailable(ExpenseClaimsActivity.this)) {            // 判断网络是否连接
			DialogUtil.showLongToast(ExpenseClaimsActivity.this, R.string.global_network_disconnected);
		}else{
			String info = getUserECInfo();
			SendRequest.sendSubmitRequest(ExpenseClaimsActivity.this, info, BaseApp.token, BaseApp.reqLang, 
					LocalConstants.EXPENSE_SCHEDULE_SERVER, BaseApp.key, submitCallBack);	
		}
	}
	
	/**
	 * @Title: getUserPCInfo
	 * @Description: 获取用户消费记录
	 * @param void
	 * @return String 返回的数据 
	 * @throws
	 */
	private String getUserECInfo(){
				
		ExpenseClaimsBean bean = new ExpenseClaimsBean();
		bean.setDeviceId(BaseApp.macAddr);
		bean.setUserAccount(BaseApp.user.getUserId());
		bean.setDateFrom("");
		bean.setDateTo("");
		
		String json = JSON.toJSONString(bean);
		
		return json;
	}

	/**
	 * http请求回调
	 */
	RequestCallBack<String> submitCallBack = new RequestCallBack<String>() {

		@Override
		public void onStart() {
			DialogUtil.showWithCancelProgressDialog(ExpenseClaimsActivity.this, null, getResources().getString(R.string.global_prompt_message), null);
		}

		@Override
		public void onLoading(long total, long current, boolean isUploading) {

		}
		
		@Override
		public void onSuccess(ResponseInfo<String> responseInfo) { 
			DialogUtil.closeDialog();
			
			Message msgs = new Message();
			String result = responseInfo.result.toString();			
			if (null != result && !"".equals(result.trim())) {
				ReqMessage msg = FastJSONUtil.getJSONToEntity(result, ReqMessage.class);
				// 判断返回标识状态是否为空
				if (null == msg || null == msg.getResCode() || "".equals(msg.getResCode())) {				
					LogUtil.promptInfo(ExpenseClaimsActivity.this, ErrorCodeContrast.getErrorCode("0", getResources()));
				}else if ("1103".equals(msg.getResCode()) || "1104".equals(msg.getResCode())) {
					LogUtil.promptInfo(ExpenseClaimsActivity.this, ErrorCodeContrast.getErrorCode(msg.getResCode(), getResources()));
					Intent mIntent = new Intent(BroadcastNotice.USER_EXIT);
					sendBroadcast(mIntent);
					return;
				}else if("1210".equals(msg.getResCode())){
					LogUtil.promptInfo(ExpenseClaimsActivity.this, ErrorCodeContrast.getErrorCode(msg.getResCode(), getResources()));	
					Intent mIntent = new Intent(BroadcastNotice.WIPE_USET);
					sendBroadcast(mIntent);
					return;
				}else if("1".equals(msg.getResCode()) && null != msg.getMessage()){
					// 获取解密后并校验后的值
					String decode = EncryptUtil.getDecodeData(msg.getMessage(), BaseApp.key);
					msgs.what = 1;
					msgs.obj = decode;
					mHandler.sendMessage(msgs);
				}else {
					LogUtil.promptInfo(ExpenseClaimsActivity.this, ErrorCodeContrast.getErrorCode(msg.getResCode(), getResources()));
				}
			} else {
				LogUtil.promptInfo(ExpenseClaimsActivity.this, ErrorCodeContrast.getErrorCode("0", getResources()));
			}
		}

		@Override
		public void onFailure(HttpException error, String msg) {
			DialogUtil.closeDialog();
			if(null != msg && msg.equals(LocalConstants.API_KEY)){
				LogUtil.promptInfo(ExpenseClaimsActivity.this, ErrorCodeContrast.getErrorCode("1207", getResources()));
			}else{
				LogUtil.promptInfo(ExpenseClaimsActivity.this, ErrorCodeContrast.getErrorCode("0", getResources()));
			}
		}
	};
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_back:
			this.finish();
			break;
		}
	}
}