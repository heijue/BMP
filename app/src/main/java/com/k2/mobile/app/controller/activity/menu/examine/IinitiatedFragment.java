package com.k2.mobile.app.controller.activity.menu.examine;
 
import java.util.ArrayList;
import java.util.List;

import com.alibaba.fastjson.JSON;
import com.k2.mobile.app.R;
import com.k2.mobile.app.common.config.BroadcastNotice;
import com.k2.mobile.app.common.exception.HttpException;
import com.k2.mobile.app.controller.activity.menu.hr.HRStaticLinkListActivity;
import com.k2.mobile.app.controller.core.BaseApp;
import com.k2.mobile.app.model.adapter.FlowAdapter;
import com.k2.mobile.app.model.bean.FlowBean;
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
import com.k2.mobile.app.view.widget.XListView;
import com.k2.mobile.app.view.widget.XListView.IXListViewListener;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView;

/**
* @Title IinitiatedFragment.java
* @Package com.oppo.mo.controller.activity.menu.workbench;
* @Description 我的待阅
* @Company  K2
* 
* @author linqijun
* @date 2015-3-13 下午3:22:19
* @version V1.0
*/
@SuppressLint("NewApi")
public class IinitiatedFragment extends Fragment implements OnClickListener{
	
	private View view = null;
	private TextView tv_fiction;
	private TextView tv_review;
	
	private XListView lv_work_show;
	// 页码
	private int pageIndex = 1;
	// 每页行数
	private int pageSize = 10;
	// 操作类别 1,拟制中 2,审核中 3,已通过 4,未审批 5,已审批
	private int operClass = 1;
	// 识别是否下拉刷新
	private int flag = 1;
	// 拟制中
	private String tipsQuasi = null;
	// 审核中
	private String tipsAudit = null;
	// 已通过
	private String tipspassed = null;
	// 适配器
	private FlowAdapter fAdapter = null;
	private List<FlowBean> fList = null;
	// 拟制临时集合
	private List<FlowBean> ficList = null;
	// 审核中临时集合
	private List<FlowBean> apiList = null;
	// 已通过临时集合
	private List<FlowBean> passList = null;
	// 广播
	private IncomingReceiver iReceiver = null;
	private int apiCount = 0;
	private int passCount = 0;
	private int positions = -1;
	private int x = 687;
	private int y = 600;
	
	protected Resources res;
	
	private Handler mHandler = new Handler(){
		@Override
		public void dispatchMessage(Message msg) {
			switch (msg.what) {
			case 1:
				String json = (String) msg.obj;
				System.out.println("app json = "+json);
				if(null != json && !"-1".equals(json)){
					List<FlowBean> fbList = JSON.parseArray(json, FlowBean.class);
					if(null != fbList){
						if(3 != flag){
							fList.clear();
						}
						// 设置操作类型
						for(int i=0; i<fbList.size(); i++){
							fbList.get(i).setClassType(operClass);
						}
						
						fList.addAll(fbList);
						fAdapter.notifyDataSetChanged();
						
						if(1 == operClass){
							ficList.clear();
							ficList.addAll(fList);
						}else if(2 == operClass){
							apiList.clear();
							apiList.addAll(fList);
						}else if(3 == operClass){
							passList.clear();
							passList.addAll(fList);
						}
					}
					
					String tmpTips = null;
					
					if((null == fbList || 1 > fbList.size()) && 1 > fList.size()){ 				// 暂无数据
						tmpTips = getString(R.string.no_data);
					}else if(null != fbList && fbList.size() < 10){			// 所有数据加载完
						tmpTips = getString(R.string.all_data_loaded);
					}else{													// 查看更多
						tmpTips = getString(R.string.xlistview_footer_hint_normal);
					}
					
					lv_work_show.setTips(tmpTips);
					if(1 == operClass){
						tipsQuasi = tmpTips;
					}else if(2 == operClass){
						tipsAudit = tmpTips;
					}else if(3 == operClass){
						tipspassed = tmpTips;
					}
				}					
				break;
			case 7:
				lv_work_show.dispatchTouchEvent(MotionEvent.obtain(SystemClock.uptimeMillis(), 
	        			SystemClock.uptimeMillis()+20, MotionEvent.ACTION_UP, x, y, 0));
				break;
			case 8:
				lv_work_show.dispatchTouchEvent(MotionEvent.obtain(SystemClock.uptimeMillis(), 
	        			SystemClock.uptimeMillis(), MotionEvent.ACTION_MOVE, x, y, 0));
				y+=110;
				break;
			}
		};
	};
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.fragment_launch_approval, null);
		initView();
		initListener();
		initAdapter(1);
		createFilter();
		
		requestServer();
		
		return view;
	}
	/*
	 * 方法名: initView() 
	 * 功 能 : 初始化布局控件 
	 * 参 数 : void 
	 * 返回值: void
	 */
	private void initView(){
		tv_fiction = (TextView) view.findViewById(R.id.tv_fiction);
		tv_review = (TextView) view.findViewById(R.id.tv_review);
		
	    lv_work_show = (XListView) view.findViewById(R.id.lv_work_show);
	    lv_work_show.setPullRefreshEnable(true);	// 设置下拉更新
	    lv_work_show.setPullLoadEnable(false);		// 设置让它上拉更新 ljw 2016-01-16
		
		fList = new ArrayList<FlowBean>();
		ficList = new ArrayList<FlowBean>();
		apiList = new ArrayList<FlowBean>();
		passList = new ArrayList<FlowBean>();
		
		res = getResources();
		
		switchBg(1);
	}

	/**
	 * 方法名: initAdapter() 
	 * 功 能 : 初始化适配器 
	 * 参 数 : void 
	 * 返回值: void
	 */
	//ljw 2016-06-17 修改适配器显示
	private void initAdapter(int openType) {
		fAdapter = new FlowAdapter(getActivity(), fList,openType);
		lv_work_show.setAdapter(fAdapter);
	}
		
	/*
	 * 方法名: initListener()  
	 * 功 能 : 初始化事件
	 * 参 数 : void 
	 * 返回值: void
	 */
	private void initListener(){
		tv_fiction.setOnClickListener(this);
		tv_review.setOnClickListener(this);
		lv_work_show.setXListViewListener(xListener);
		lv_work_show.setOnItemClickListener(itemListener);
	}
	
	/**
	 * @Title: createFilter
	 * @Description: 创建IntentFilter
	 * @param void
	 * @return void 
	 * @throws
	 */
	private void createFilter() {
		IntentFilter filter = new IntentFilter();
		filter.addAction(BroadcastNotice.I_STARTED_REFRESH);
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
	        if(action.equals(BroadcastNotice.I_STARTED_REFRESH))  
	        { 
	        	fList.remove(positions);
	        	fAdapter.notifyDataSetChanged();
	        } 
		}
	}
	
	/**
	 * @Title: getLoginInfo
	 * @Description: 获取登陆信息
	 * @param void
	 * @return String 返回的数据 
	 * @throws
	 */
	private String getLoginInfo(String code){		
		ReqBodyBean bBean = new ReqBodyBean();
		bBean.setInvokeFunctionCode(code);
		bBean.setInvokeParameter("[\""+pageSize+"\",\""+pageIndex+"\"]");
				
		PublicRequestBean bean = new PublicRequestBean();
		bean.setReqHeader(PublicResHeaderUtils.getReqHeader());
		bean.setReqBody(bBean);
		
		return JSON.toJSONString(bean);
	}
	
	/**
	 * @Title: doubleClickRefresh
	 * @Description: 双击刷新
	 * @param void
	 * @return void 
	 * @throws
	 */
	public void doubleClickRefresh(){
		x = 687;
    	y = 200;
    	lv_work_show.dispatchTouchEvent(MotionEvent.obtain(SystemClock.uptimeMillis(), SystemClock.uptimeMillis(), MotionEvent.ACTION_DOWN, x, y, 0));
    	new Thread(new Runnable() {
			@Override
			public void run() {
				for(int i=0; i < 6;i++){
					Message msg = new Message();
					msg.what = 8;
					mHandler.sendMessage(msg);
					try {
						Thread.sleep(20);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				
				try {
					Thread.sleep(20);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				
				Message msg = new Message();
				msg.what = 7;
				mHandler.sendMessage(msg);
				
			}
		}).start();
	}
	
	/*
	 * 方法名: refresh()  
	 * 功 能 : 刷新数据
	 * 参 数 : void 
	 * 返回值: void
	 */
	public void refresh(){
		flag = 2;
		pageIndex = 1;
		requestServer();
	}
	
	IXListViewListener xListener = new IXListViewListener() {
		@Override
		public void onRefresh() {
			refresh();
		}
		
		@Override
		public void onLoadMore() {
			flag = 3;
			pageIndex++;
			requestServer();
		}
	};
	
	OnItemClickListener itemListener = new OnItemClickListener() {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			positions = position - 1; 
			if(fList.get(positions).getOpentype()!=null&&fList.get(positions).getOpentype()!="")
			{
				int _type=Integer.parseInt(fList.get(positions).getOpentype());
				switch(_type)
				{
				  case 1: OpenNativeIntent(); break;
				  case 2:break;
				  case 3: OpenHtml5Intent(fList.get(positions));break;
				  default:OpenNativeIntent();break;
				}
			} 
			else
			{
				//默认打开原生Native
				OpenNativeIntent();
			} 
		}
	};
	
	//打开Native的详情界面
	private void OpenNativeIntent()
	{ 
		Intent mIntent = new Intent(getActivity(), ApprovedInfolActivity.class);
		mIntent.putExtra("SN", fList.get(positions).getSN());
		mIntent.putExtra("folio", fList.get(positions).getFolio());
		mIntent.putExtra("checkServer", 1);
		mIntent.putExtra("destination", fList.get(positions).getDestination()); 
		mIntent.putExtra("processFullName", fList.get(positions).getProcessFullName());
		mIntent.putExtra("bsCode", fList.get(positions).getBsCode());
		startActivity(mIntent);
	}

	//打开HTML5的详情界面
	private void OpenHtml5Intent(FlowBean Model)	
	{
		Intent mIntent = new Intent(getActivity(), HRStaticLinkListActivity.class);
		mIntent.putExtra("title", Model.getFolio());
		mIntent.putExtra("url", Model.getLinkurl());
		startActivity(mIntent);
		return;
	}
	
	/**
	* @Title: onLoad
	* @Description: 停止加载进度条
	* @param void
	* @return String 返回的数据 
	* @throws
	*/
	private void onLoad() {
		lv_work_show.stopRefresh();
		lv_work_show.stopLoadMore();
		lv_work_show.setRefreshTime(getResources().getString(R.string.just));
	}

	/**
	* @Title: requestServer
	* @Description: 发送请求报文
	* @param void
	* @return void
	* @throws
	*/
	private void requestServer(){
		// 判断网络是否连接
		if (!NetWorkUtil.isNetworkAvailable(getActivity())) {
			DialogUtil.showLongToast(getActivity(), R.string.global_network_disconnected);
		}else{
			// 验证登陆信息
			String code = null;
			if(1 == operClass){
				code = "F60000003";
			}else if(2 == operClass){
				code = "F60000004";
			}
			
			String info = getLoginInfo(code);
			SendRequest.submitRequest(getActivity(), info, submitCallBack);
		}
	}
	
	/**
	 * http请求回调
	 */
	RequestCallBack<String> submitCallBack = new RequestCallBack<String>() {

		@Override
		public void onStart() {
			if (1 == flag) {
				DialogUtil.showWithCancelProgressDialog(getActivity(), null, getResources().getString(R.string.global_prompt_message), null);
			}else{
				onLoad();
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
				onLoad();
			}
			String result = responseInfo.result.toString();
			byte[] ebase64 = EncryptUtil.decodeBase64(result);
			
			if (null != result && !"".equals(result.trim())) {
				PublicResultBean resMsg = JSON.parseObject(new String(ebase64), PublicResultBean.class);
				// 判断返回标识状态是否为空
				if (null == resMsg || null == resMsg.getResHeader().getStateCode() || "".equals(resMsg.getResHeader().getStateCode())) {				
					LogUtil.promptInfo(getActivity(), ErrorCodeContrast.getErrorCode("0", res));
					return;
				}else if (!"1".equals(resMsg.getResHeader().getStateCode())) {
					LogUtil.promptInfo(getActivity(), resMsg.getResHeader().getReturnMsg());	
					return;
				}
				// 判断消息体是否为空
				if (null == resMsg.getResBody().getResultString() || "".equals(resMsg.getResBody().getResultString().trim())) {		 
					LogUtil.promptInfo(getActivity(), ErrorCodeContrast.getErrorCode("0", res));
				} else {
					if(null != resMsg.getResHeader().getSectetKey() && !"".equals(resMsg.getResHeader().getSectetKey().trim())){
						BaseApp.key = resMsg.getResHeader().getSectetKey();
					}
					// 获取解密后并校验后的值
					Message msgs = new Message();
					msgs.what = 1;
					msgs.obj = resMsg.getResBody().getResultString();
					mHandler.sendMessage(msgs);
				}
			} else {
				LogUtil.promptInfo(getActivity(), ErrorCodeContrast.getErrorCode("0", res));
			}		
		}

		@Override
		public void onFailure(HttpException error, String msg) {
			if (1 == flag) {
				DialogUtil.closeDialog();
			}else{
				onLoad();
			}
		}
	};
	
	/**
	 * 方法名: MyOnPageChangeListener
	 * 功 能 : 为选项卡绑定监听器
	 * 参 数 : void 
	 * 返回值: void
	 */
	public class MyOnPageChangeListener implements OnPageChangeListener {
		@Override
		public void onPageSelected(final int position) {
			
		}
		@Override
		public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
			 
		}
		@Override
		public void onPageScrollStateChanged(int state) {
			 
		}
	}
	
	@Override
	public void onDestroy() {
		if(null != iReceiver)
			getActivity().unregisterReceiver(iReceiver);
		super.onDestroy();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.tv_fiction:
				switchBg(1);
				operClass = 1;
				pageIndex = 1;
				lv_work_show.setTips(tipsQuasi);
				flag  = 1;
				fList.clear();
				fList.addAll(ficList);
				initAdapter(1);
				break;
			case R.id.tv_review:
				switchBg(2);
				operClass = 2;
				pageIndex = 1;
				lv_work_show.setTips(tipsAudit);
				flag  = 1;
				if(1 > apiCount){
					apiCount++;
//					requestServer();
					doubleClickRefresh();
				}else{
					fList.clear();
					fList.addAll(apiList);
					initAdapter(2);
				}
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
					tv_fiction.setBackgroundDrawable(getResources().getDrawable(R.drawable.tab_bg));
					tv_review.setBackgroundDrawable(getResources().getDrawable(R.color.white));
	            } else {
	            	tv_fiction.setBackground(getResources().getDrawable(R.drawable.tab_bg));
	            	tv_review.setBackground(getResources().getDrawable(R.color.white));
	            }
				tv_fiction.setTextColor(getResources().getColor(R.color.white));
				tv_review.setTextColor(getResources().getColor(R.color.black));
				break;
			case 2:
				if (Build.VERSION.SDK_INT < 16) {
					tv_fiction.setBackgroundDrawable(getResources().getDrawable(R.color.white));
					tv_review.setBackgroundDrawable(getResources().getDrawable(R.drawable.tab_bg));
	            } else {
	            	tv_fiction.setBackground(getResources().getDrawable(R.color.white));
	            	tv_review.setBackground(getResources().getDrawable(R.drawable.tab_bg));
	            }
				tv_review.setTextColor(getResources().getColor(R.color.white));
				tv_fiction.setTextColor(getResources().getColor(R.color.black));
				break;
			case 3:
				if (Build.VERSION.SDK_INT < 16) {
					tv_fiction.setBackgroundDrawable(getResources().getDrawable(R.color.white));
					tv_review.setBackgroundDrawable(getResources().getDrawable(R.color.white));
	            } else {
	            	tv_fiction.setBackground(getResources().getDrawable(R.color.white));
	            	tv_review.setBackground(getResources().getDrawable(R.color.white));
	            }
				tv_fiction.setTextColor(getResources().getColor(R.color.black));
				tv_review.setTextColor(getResources().getColor(R.color.black));
				break;
		}
	}
}