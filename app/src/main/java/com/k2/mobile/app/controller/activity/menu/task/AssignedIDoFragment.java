package com.k2.mobile.app.controller.activity.menu.task;
 
import java.util.ArrayList;
import java.util.List;

import com.alibaba.fastjson.JSON;
import com.k2.mobile.app.R;
import com.k2.mobile.app.common.config.BroadcastNotice;
import com.k2.mobile.app.common.config.LocalConstants;
import com.k2.mobile.app.common.exception.HttpException;
import com.k2.mobile.app.controller.core.BaseApp;
import com.k2.mobile.app.model.adapter.MyTaskAdapter;
import com.k2.mobile.app.model.bean.PublicBean;
import com.k2.mobile.app.model.bean.ReqMessage;
import com.k2.mobile.app.model.bean.ResTaskBean;
import com.k2.mobile.app.model.http.ResponseInfo;
import com.k2.mobile.app.model.http.callback.RequestCallBack;
import com.k2.mobile.app.model.http.other.SendRequest;
import com.k2.mobile.app.utils.DialogUtil;
import com.k2.mobile.app.utils.EncryptUtil;
import com.k2.mobile.app.utils.ErrorCodeContrast;
import com.k2.mobile.app.utils.FastJSONUtil;
import com.k2.mobile.app.utils.LogUtil;
import com.k2.mobile.app.utils.NetWorkUtil;
import com.k2.mobile.app.view.widget.ViewExpandAnimation;
import com.k2.mobile.app.view.widget.XListView;
import com.k2.mobile.app.view.widget.XListView.IXListViewListener;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView;

/**
* @Title AssignedIDoFragment.java
* @Package com.oppo.mo.controller.activity.menu.workbench;
* @Description 我指派
* @Company  K2
* 
* @author liangzy
* @date 2015-3-13 下午3:22:19
* @version V1.0
*/
@SuppressLint("NewApi")
public class AssignedIDoFragment extends Fragment implements IXListViewListener, OnClickListener {
	
	private View view = null;
	private TextView tv_be_accept, tv_unfinished, tv_completed;
	private XListView lv_show;
	// 页码
	private int pageIndex = 1;
	// 每页行数
	private int pageSize = 10;
	// 操作类别 1,待接受 2,未完成 3,已完成
	private int operClass = 1;
	private int taskType = 0;
	// 识别是否下拉刷新
	private int flag = 1;
	private boolean checkFlag = true;
	// 标记加载
	public int loadType = 1;

	// 适配器
	private MyTaskAdapter mAdapter;
	private List<ResTaskBean> tList;

	// 广播接收
	private IncomingReceiver iReceiver = null;
	
	private Handler mHandler = new Handler() {
		@Override
		public void dispatchMessage(Message msg) {
			switch (msg.what) {
			case 1:
				String json = (String) msg.obj;
				if(null != json){
					List<ResTaskBean> rlist = JSON.parseArray(json, ResTaskBean.class);
					if(null != rlist){
						if(3 != flag){
							tList.clear();
						}
						tList.addAll(rlist);
						if (checkFlag) {
							mAdapter.notifyDataSetChanged();
						}else {
							checkFlag = true;
							initAdapter();
						}
					}
				}
				
				break;
			}
		};
	};

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.fragment_assigned, null);	
		initView();
		initListener();
		createFilter();
		initAdapter();
		requestServer();
		return view;
	}	

	/**
	 * 方法名: initView() 
	 * 功 能 : 初始化布局控件 
	 * 参 数 : void 
	 * 返回值: void
	 */
	private void initView() {
		tv_be_accept = (TextView)  view.findViewById(R.id.tv_be_accept);
		tv_unfinished = (TextView)  view.findViewById(R.id.tv_unfinished); 
		tv_completed = (TextView)  view.findViewById(R.id.tv_completed);
		
		lv_show = (XListView) view.findViewById(R.id.lv_show);
		lv_show.setPullRefreshEnable(true);		// 设置下拉更新
		lv_show.setPullLoadEnable(true);		// 设置让它上拉更新

		tList = new ArrayList<ResTaskBean>();
		
		switchBg(1);
	}

	/**
	 * 方法名: initAdapter() 
	 * 功 能 : 初始化适配器 
	 * 参 数 : void 
	 * 返回值: void
	 */
	private void initAdapter() {
		mAdapter = new MyTaskAdapter(getActivity(), tList, taskType);
		lv_show.setAdapter(mAdapter);
	}
	
	/*
	 * 方法名: initListener()  
	 * 功 能 : 初始化事件
	 * 参 数 : void 
	 * 返回值: void
	 */
	private void initListener(){
		tv_be_accept.setOnClickListener(this);
		tv_unfinished.setOnClickListener(this);
		tv_completed.setOnClickListener(this);
		
		lv_show.setOnItemClickListener(itemListener);
		lv_show.setXListViewListener(this);
	}
		
	/**
	* @Title: requestServer
	* @Description: 发送请求报文
	* @param void
	* @return void
	* @throws
	*/
	private void requestServer() {
		// 判断网络是否连接
		if (!NetWorkUtil.isNetworkAvailable(getActivity())) {
			DialogUtil.showLongToast(getActivity(), R.string.global_network_disconnected);
		}else{
			String info = getAcceptInfo(1);
			switch (operClass) {
			case 1:
				info = getAcceptInfo(1);
				taskType = 0;
				break;
			case 2:
				info = getAcceptInfo(2);
				taskType = 1;
				break;
			case 3:
				info = getAcceptInfo(3);
				taskType = 2;
				break;
			}

			SendRequest.sendSubmitRequest(getActivity(), info, BaseApp.token, BaseApp.reqLang, LocalConstants.MY_TASK_QUERY_SERVER, BaseApp.key, submitCallBack);
		}
	}
	
   /**
	* @Title: getAcceptInfo
	* @Description: 请求报文
	* @param void
	* @return String 返回的数据 
	* @throws
	*/
	private String getAcceptInfo(int type) {
		
		PublicBean bean = new PublicBean();
		bean.setDeviceId(BaseApp.macAddr);
		bean.setUserAccount(BaseApp.user.getUserId());
		bean.setTaskType(type+"");
		bean.setPageIndex(pageIndex + "");
		bean.setPageSize(pageSize + "");
		
		return JSON.toJSONString(bean);
	}
	
	// 实现弹出动画效果
	private OnItemClickListener itemListener = new OnItemClickListener() {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			View show_edit = view.findViewById(R.id.show_edit);
			show_edit.startAnimation(new ViewExpandAnimation(show_edit));
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
		filter.addAction(BroadcastNotice.TASK_UI_UPDATE);
		filter.addAction(BroadcastNotice.TASK_UNFINISH_UPDATE);
		filter.addAction(BroadcastNotice.TASK_FINISH_UPDATE);
		iReceiver = new IncomingReceiver();
		// 注册广播
		getActivity().registerReceiver(iReceiver, filter);
	}
	
	/**
	 * 接收广播
	 */ 
	private class IncomingReceiver extends BroadcastReceiver{
		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();  
	        if (action.equals(BroadcastNotice.TASK_UI_UPDATE)) {
	        	operClass = 1;
	        	requestServer();
	        } else if (action.equals(BroadcastNotice.TASK_UNFINISH_UPDATE)) {
//	        	flag = 1;
	        	operClass = 2;
	        	requestServer();
	        } else if (action.equals(BroadcastNotice.TASK_FINISH_UPDATE)) {
//	        	flag = 1;
	        	operClass = 3;
	        	requestServer();
	        }
		}
	}
	
	@Override
	public void onDestroy() {
		if(null != iReceiver)
			getActivity().unregisterReceiver(iReceiver);
		super.onDestroy();
	};

	/**
	 * http请求回调
	 */
	RequestCallBack<String> submitCallBack = new RequestCallBack<String>() {

		@Override
		public void onStart() {
			if (1 == flag) {
				DialogUtil.showWithCancelProgressDialog(getActivity(), null, getResources().getString(R.string.global_prompt_message), null);
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
					LogUtil.promptInfo(getActivity(), ErrorCodeContrast.getErrorCode("0", getResources()));
					return;
				// 验证不合法
				}else if ("1103".equals(msg.getResCode()) || "1104".equals(msg.getResCode())) {
					LogUtil.promptInfo(getActivity(), ErrorCodeContrast.getErrorCode(msg.getResCode(), getResources()));
					Intent mIntent = new Intent(BroadcastNotice.USER_EXIT);
					getActivity().sendBroadcast(mIntent);
					return;
				}else if("1210".equals(msg.getResCode())){
					LogUtil.promptInfo(getActivity(), ErrorCodeContrast.getErrorCode(msg.getResCode(), getResources()));	
					Intent mIntent = new Intent(BroadcastNotice.WIPE_USET);
					getActivity().sendBroadcast(mIntent);
					return;
				}else if (!"1".equals(msg.getResCode())) {
					LogUtil.promptInfo(getActivity(), ErrorCodeContrast.getErrorCode(msg.getResCode(), getResources()));	
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
				}else{
					if(1 == flag){
						tList.clear();
						mAdapter.notifyDataSetChanged();
					}
				}
				
			} else {
				LogUtil.promptInfo(getActivity(), ErrorCodeContrast.getErrorCode("0", getResources()));
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
				LogUtil.promptInfo(getActivity(), ErrorCodeContrast.getErrorCode("1207", getResources()));
			}else{
				LogUtil.promptInfo(getActivity(), ErrorCodeContrast.getErrorCode("0", getResources()));
			}
		}
	};
	
	// 刷新
	@Override
	public void onRefresh() {
		flag = 2;
		pageIndex = 1;
		requestServer();
	}

	// 加载更多
	@Override
	public void onLoadMore() {
		flag = 3;
		pageIndex++;
		requestServer();
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
			case R.id.tv_be_accept:	// 待接受
				switchBg(1);
				operClass = 1;
				pageIndex = 1;
				flag  = 1;
				checkFlag = false;
				requestServer();
				break;
			case R.id.tv_unfinished:	// 未完成
				switchBg(2);
				operClass = 2;
				pageIndex = 1;
				flag  = 1;
				checkFlag = false;
				requestServer();
				break;
			case R.id.tv_completed:	// 已完成
				switchBg(3);
				operClass = 3;
				pageIndex = 1;
				flag  = 1;
				checkFlag = false;
				requestServer();
				break;
		}
	}
	
	/**
	 * 方法名: switchBg() 
	 * 功 能 : 设置背景
	 * 参 数 : val 被选中的控件编号 
	 * 返回值: void
	 */
	private void switchBg(int val){
		switch (val) {
			case 1:
				if (Build.VERSION.SDK_INT < 16) {
					tv_be_accept.setBackgroundDrawable(getResources().getDrawable(R.drawable.tab_bg));
					tv_unfinished.setBackgroundDrawable(getResources().getDrawable(R.color.white));
					tv_completed.setBackgroundDrawable(getResources().getDrawable(R.color.white));
	            } else {
	            	tv_be_accept.setBackground(getResources().getDrawable(R.drawable.tab_bg));
	            	tv_unfinished.setBackground(getResources().getDrawable(R.color.white));
	            	tv_completed.setBackground(getResources().getDrawable(R.color.white));
	            }
				tv_be_accept.setTextColor(getResources().getColor(R.color.white));
				tv_unfinished.setTextColor(getResources().getColor(R.color.black));
				tv_completed.setTextColor(getResources().getColor(R.color.black));
				break;
			case 2:
				if (Build.VERSION.SDK_INT < 16) {
					tv_be_accept.setBackgroundDrawable(getResources().getDrawable(R.color.white));
					tv_unfinished.setBackgroundDrawable(getResources().getDrawable(R.drawable.tab_bg));
					tv_completed.setBackgroundDrawable(getResources().getDrawable(R.color.white));
	            } else {
	            	tv_be_accept.setBackground(getResources().getDrawable(R.color.white));
	            	tv_unfinished.setBackground(getResources().getDrawable(R.drawable.tab_bg));
	            	tv_completed.setBackground(getResources().getDrawable(R.color.white));
	            }
				tv_unfinished.setTextColor(getResources().getColor(R.color.white));
				tv_be_accept.setTextColor(getResources().getColor(R.color.black));
				tv_completed.setTextColor(getResources().getColor(R.color.black));
				break;
			case 3:
				if (Build.VERSION.SDK_INT < 16) {
					tv_unfinished.setBackgroundDrawable(getResources().getDrawable(R.color.white));
					tv_be_accept.setBackgroundDrawable(getResources().getDrawable(R.color.white));
					tv_completed.setBackgroundDrawable(getResources().getDrawable(R.drawable.tab_bg));
	            } else {
	            	tv_unfinished.setBackground(getResources().getDrawable(R.color.white));
	            	tv_be_accept.setBackground(getResources().getDrawable(R.color.white));
	            	tv_completed.setBackground(getResources().getDrawable(R.drawable.tab_bg));
	            }
				tv_unfinished.setTextColor(getResources().getColor(R.color.black));
				tv_be_accept.setTextColor(getResources().getColor(R.color.black));
				tv_completed.setTextColor(getResources().getColor(R.color.white));
				break;
		}
	}
}