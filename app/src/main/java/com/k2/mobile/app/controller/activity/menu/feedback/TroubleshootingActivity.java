package com.k2.mobile.app.controller.activity.menu.feedback;

import java.util.ArrayList;
import java.util.List;

import com.alibaba.fastjson.JSON;
import com.k2.mobile.app.R;
import com.k2.mobile.app.common.config.BroadcastNotice;
import com.k2.mobile.app.common.config.LocalConstants;
import com.k2.mobile.app.common.exception.HttpException;
import com.k2.mobile.app.controller.core.BaseApp;
import com.k2.mobile.app.model.adapter.FeedbackListAdapter;
import com.k2.mobile.app.model.bean.FeedbackBean;
import com.k2.mobile.app.model.bean.PublicBean;
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
import com.k2.mobile.app.view.widget.XListView.IXListViewListener;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

/**
* @Title TroubleshootingActivity.java
* @Package com.oppo.mo.controller.activity.menu;
* @Description 问题处理
* @Company  K2
* 
* @author linqijun
* @date 2015-7-01 下午14:35
* @version V1.0
*/
@SuppressLint("NewApi")
public class TroubleshootingActivity extends Activity implements IXListViewListener, OnClickListener {

	public final static int REQUESTCODE_DIALOG = 1109;
	// 返回
	private Button btn_back;
	// 头部标题
	private TextView tv_title;
	private TextView tv_search;

	private XListView lv_show;
	// 页码
	private int pageIndex = 1;
	// 每页行数
	private int pageSize = 10;
	// 识别是否下拉刷新
	private int flag = 1;
	// 标记加载
	public int loadType = 1;

	// 适配器
	private FeedbackListAdapter mAdapter;
	private List<FeedbackBean> tList;

	// 广播接收
	private IncomingReceiver iReceiver = null;
	
	private Handler mHandler = new Handler() {
		@Override
		public void dispatchMessage(Message msg) {
			switch (msg.what) {
			case 1:
				String json = (String) msg.obj;
				if(3 != flag){
					if(null != tList){
						tList.clear();
					}else{
						tList = new ArrayList<FeedbackBean>();
					}
				}
				
				if(null != json){
					List<FeedbackBean> rlist = JSON.parseArray(json, FeedbackBean.class);
					if(null != rlist){
						tList.addAll(rlist);
						mAdapter.notifyDataSetChanged();
					}
					
					String tmpTips = null;
					
					if((null == rlist || 1 > rlist.size()) && 1 > tList.size()){ // 暂无数据
						tmpTips = getString(R.string.no_data);
					}else if(null != rlist && rlist.size() < 10){			// 所有数据加载完
						tmpTips = getString(R.string.all_data_loaded);
					}else{													// 查看更多
						tmpTips = getString(R.string.xlistview_footer_hint_normal);
					}
					
					lv_show.setTips(tmpTips);
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
		setContentView(R.layout.activity_feedback);
		initView();
		initListener();
		createFilter();
		initAdapter();
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
		lv_show = (XListView) findViewById(R.id.lv_show);
		lv_show.setPullRefreshEnable(true);		// 设置下拉更新
		lv_show.setPullLoadEnable(true);		// 设置让它上拉更新
		
		tv_title.setText(getString(R.string.troubleshooting));
		tv_search.setVisibility(View.GONE);
		
		tList = new ArrayList<FeedbackBean>();
		
		requestServer();
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
		lv_show.setOnItemClickListener(itemListener);
		lv_show.setXListViewListener(this);
	}
	
	/**
	 * 方法名: initAdapter() 
	 * 功 能 : 初始化适配器 
	 * 参 数 : void 
	 * 返回值: void
	 */
	private void initAdapter() {
		mAdapter = new FeedbackListAdapter(TroubleshootingActivity.this, tList, 1);
		lv_show.setAdapter(mAdapter);
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
		if (!NetWorkUtil.isNetworkAvailable(TroubleshootingActivity.this)) {
			DialogUtil.showLongToast(TroubleshootingActivity.this, R.string.global_network_disconnected);
		}else{
			SendRequest.sendSubmitRequest(TroubleshootingActivity.this, info, BaseApp.token, BaseApp.reqLang, server, BaseApp.key, submitCallBack);
		}
	}
	/**
	* @Title: requestServer
	* @Description: 提供外部发送请求报文
	* @param void
	* @return void
	* @throws
	*/
	private void requestServer(){
		String info = receiveMailQuest();
		requestServer(LocalConstants.FEEDBACK_LIST_SERVER, info);
	}
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
		bean.setFlag("1");
		bean.setPageNo(pageIndex + "");
		bean.setPageSize(pageSize + "");
		
		return JSON.toJSONString(bean);
	}
	
	private OnItemClickListener itemListener = new OnItemClickListener() {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			final FeedbackBean fBean = tList.get(position - 1);
			String flag = null;
			if(null != fBean.getStatus() && "3".equals(fBean.getStatus().trim())){
				flag = "0";
			}else if(null != fBean.getStatus() && "4".equals(fBean.getStatus().trim())){
				flag = "1";
			}else if(null != fBean.getStatus() && "7".equals(fBean.getStatus().trim())){
				flag = "2";
			}
			Intent mIntent = new Intent(TroubleshootingActivity.this, FeedbackTreatActivity.class);
			mIntent.putExtra("flag", flag);
			mIntent.putExtra("code", fBean.getQuestionFeedbackCode());
			startActivity(mIntent);
		}
	};

	/**
	 * @Title: createFilter
	 * @Description: 创建IntentFilter
	 * @param void
	 * @return void 
	 * @throws
	 */
	private void createFilter() {
		IntentFilter filter = new IntentFilter();
		filter.addAction(BroadcastNotice.FEEDBACK_FINISH_UPDATE_TREATED);
		iReceiver = new IncomingReceiver();
		// 注册广播
		TroubleshootingActivity.this.registerReceiver(iReceiver, filter);
	}
	
	/**
	 * 接收广播
	 */ 
	private class IncomingReceiver extends BroadcastReceiver{
		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();  
	        if (action.equals(BroadcastNotice.FEEDBACK_FINISH_UPDATE_TREATED)) {
	        	flag = 1;
	    		pageIndex = 1;
	    		String info = receiveMailQuest();
	    		requestServer(LocalConstants.FEEDBACK_LIST_SERVER, info);
	        }
		}
	}
	
	@Override
	public void onDestroy() {
		if(null != iReceiver)
			TroubleshootingActivity.this.unregisterReceiver(iReceiver);
		super.onDestroy();
	};

	/**
	 * http请求回调
	 */
	RequestCallBack<String> submitCallBack = new RequestCallBack<String>() {

		@Override
		public void onStart() {
			if (1 == flag) {
				DialogUtil.showWithCancelProgressDialog(TroubleshootingActivity.this, null, getResources().getString(R.string.global_prompt_message), null);
			}else{
				stopLoad();
			}
		}

		@Override
		public void onLoading(long total, long current, boolean isUploading) {

		}
		
		@Override
		public void onSuccess(ResponseInfo<String> responseInfo) {     
			if (1 == flag) {
				DialogUtil.closeDialog();
			}else{
				stopLoad();
			}
			String result = responseInfo.result.toString();
			if (null != result && !"".equals(result.trim())) {
				ReqMessage msg = FastJSONUtil.getJSONToEntity(result, ReqMessage.class);
				// 判断返回标识状态是否为空
				if (null == msg || null == msg.getResCode() || "".equals(msg.getResCode())) {				
					LogUtil.promptInfo(TroubleshootingActivity.this, ErrorCodeContrast.getErrorCode("0", getResources()));
					return;
				// 验证不合法
				}else if ("1103".equals(msg.getResCode()) || "1104".equals(msg.getResCode())) {
					LogUtil.promptInfo(TroubleshootingActivity.this, ErrorCodeContrast.getErrorCode(msg.getResCode(), getResources()));
					Intent mIntent = new Intent(BroadcastNotice.USER_EXIT);
					TroubleshootingActivity.this.sendBroadcast(mIntent);
					return;
				}else if("1210".equals(msg.getResCode())){
					LogUtil.promptInfo(TroubleshootingActivity.this, ErrorCodeContrast.getErrorCode(msg.getResCode(), getResources()));	
					Intent mIntent = new Intent(BroadcastNotice.WIPE_USET);
					sendBroadcast(mIntent);
					return;
				}else if (!"1".equals(msg.getResCode())) {
					LogUtil.promptInfo(TroubleshootingActivity.this, ErrorCodeContrast.getErrorCode(msg.getResCode(), getResources()));	
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
				LogUtil.promptInfo(TroubleshootingActivity.this, ErrorCodeContrast.getErrorCode("0", getResources()));
			}
		}

		@Override
		public void onFailure(HttpException error, String msg) {
			if (1 == flag) {
				DialogUtil.closeDialog();
			}else{
				stopLoad();
			}
			if(null != msg && msg.equals(LocalConstants.API_KEY)){
				LogUtil.promptInfo(TroubleshootingActivity.this, ErrorCodeContrast.getErrorCode("1207", getResources()));
			}else{
				LogUtil.promptInfo(TroubleshootingActivity.this, ErrorCodeContrast.getErrorCode("0", getResources()));
			}
		}
	};
	
	// 刷新
	@Override
	public void onRefresh() {
		flag = 2;
		pageIndex = 1;
		String info = receiveMailQuest();
		requestServer(LocalConstants.FEEDBACK_LIST_SERVER, info);
	}

	// 加载更多
	@Override
	public void onLoadMore() {
		flag = 3;
		pageIndex++;
		String info = receiveMailQuest();
		requestServer(LocalConstants.FEEDBACK_LIST_SERVER, info);
	}
	
	// 停止刷新
	private void stopLoad() {
		lv_show.stopRefresh();
		lv_show.stopLoadMore();
		lv_show.setRefreshTime(getResources().getString(R.string.just));
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
