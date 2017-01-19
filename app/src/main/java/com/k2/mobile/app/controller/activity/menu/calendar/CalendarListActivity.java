package com.k2.mobile.app.controller.activity.menu.calendar;

import com.alibaba.fastjson.JSON;
import com.k2.mobile.app.R;
import com.k2.mobile.app.common.config.BroadcastNotice;
import com.k2.mobile.app.common.config.LocalConstants;
import com.k2.mobile.app.common.exception.HttpException;
import com.k2.mobile.app.controller.core.BaseActivity;
import com.k2.mobile.app.controller.core.BaseApp;
import com.k2.mobile.app.model.adapter.ClaendarListAdapter;
import com.k2.mobile.app.model.bean.PublicBean;
import com.k2.mobile.app.model.bean.ReqMessage;
import com.k2.mobile.app.model.bean.ResCalendarBean;
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

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;
import java.util.ArrayList;

/**
 * @Title: CalendarListActivity.java
 * @Package com.oppo.mo.model.bean
 * @Description: 我的日历列表
 * @Company:广东欧泊移动通讯有限公司
 * 
 * @author linqijun	
 * @date 2015-04-10 15:15:00
 * @version V1.0
 */
public class CalendarListActivity extends BaseActivity implements OnClickListener, IXListViewListener{
	
	private TextView tv_title;
	private Button btn_back;
	private TextView tv_sech;
	private XListView lv_calendar_list;
	
	private List<ResCalendarBean> resList = null;
	private ClaendarListAdapter clAdapter = null;
	
	// 加载服务器类型 0,不显示进度条， 1显示
	private int loadType = 1;
	// 分页当前页
	private int pageNo = 1;
	// 分页每页数
	private final int pageSize = 10;
	// 操作类型 0是上拉刷新 1，下拉刷新
	private int operType = 0;
	
	private Handler mHandler = new Handler(){
		@Override
		public void dispatchMessage(Message msg) {
			switch (msg.what) {
				case 1:
					String json = (String)msg.obj;
					if (json != null) {
						resList.clear();
						resList.addAll(JSON.parseArray(json, ResCalendarBean.class));
						lv_calendar_list.setPullLoadEnable(true);
						clAdapter.notifyDataSetChanged();
					}
					break;
				case 2:
					String jsons = (String) msg.obj;
					if (null != jsons &&  !"".equals(jsons.trim())) {
						resList.addAll(JSON.parseArray(jsons, ResCalendarBean.class));
						clAdapter.notifyDataSetChanged();
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
		setContentView(R.layout.activity_calendar_list);
		initView();
		initListener();
		initAdapter();
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
		btn_back = (Button) findViewById(R.id.btn_back);
		tv_sech = (TextView) findViewById(R.id.tv_sech);
		lv_calendar_list = (XListView) findViewById(R.id.lv_calendar_list);
		// 设置可下拉更新
		lv_calendar_list.setPullRefreshEnable(true);
		// 设置可上拉更新
		lv_calendar_list.setPullLoadEnable(true);
		tv_title.setText(res.getString(R.string.calecndar_list));
		tv_sech.setVisibility(View.GONE);
		
		resList = new ArrayList<ResCalendarBean>();
		
		operCalendar();
	}
	
	/**
	 * 方法名: initListener() 
	 * 功 能 : 初始化 监听器 
	 * 参 数 : void 
	 * 返回值: void
	 */
	private void initListener() {
		btn_back.setOnClickListener(this);
		lv_calendar_list.setXListViewListener(this);
	}
	/**
	 * 方法名: initAdapter() 
	 * 功 能 : 初始化 适配器
	 * 参 数 : void 
	 * 返回值: void
	 */
	private void initAdapter(){
		resList = new ArrayList<ResCalendarBean>();
		clAdapter = new ClaendarListAdapter(this, resList);
		lv_calendar_list.setAdapter(clAdapter);
	}
	
	/**
	 * @Title: operCalendar
	 * @Description: 向服务器发送请求
	 * @return void  
	 * @throws
	 */
    private void operCalendar(){
    	
    	if (!NetWorkUtil.isNetworkAvailable(this)) {
 			DialogUtil.showLongToast(CalendarListActivity.this, R.string.global_network_disconnected);
 			return;
 		} 
    	String info = requestInfo();
    	if(null != info && !"".equals(info.trim())){
    		SendRequest.sendSubmitRequest(CalendarListActivity.this, info, BaseApp.token, BaseApp.reqLang, 
    				LocalConstants.MY_CALENDAR_LIST_SERVER, BaseApp.key, submitCallBack);    		
    	}
    }
    /**
	 * @Title: stopLoad
	 * @Description: 停止刷新加载
	 * @return void  
	 * @throws
	 */
    private void stopLoad(){
    	lv_calendar_list.stopRefresh();
    	lv_calendar_list.stopLoadMore();
    	lv_calendar_list.setRefreshTime("刚刚");
	}
    
    /**
	 * @Title: requestInfo()
	 * @Description: 请求报文
	 * @return void 返回的数据 
	 * @throws
	 */
    private String requestInfo(){
    	
    	PublicBean bean = new PublicBean();
    	bean.setDeviceId(BaseApp.macAddr);
    	bean.setUserAccount(BaseApp.user.getUserId());
    	bean.setPageIndex(pageNo + "");
    	bean.setPageSize(pageSize + "");
    	
    	return JSON.toJSONString(bean);
    }
    /**
	 * http请求回调
	 */
	RequestCallBack<String> submitCallBack = new RequestCallBack<String>() {

		@Override
		public void onStart() {
			if(loadType == 1){
				DialogUtil.showWithCancelProgressDialog(CalendarListActivity.this, null, res.getString(R.string.global_prompt_message), null);
			}
		}

		@Override
		public void onLoading(long total, long current, boolean isUploading) {

		}
		
		@Override
		public void onSuccess(ResponseInfo<String> responseInfo) {                                     
			if(loadType == 1){
				DialogUtil.closeDialog();
			}else{
				stopLoad();
			}
			String result = responseInfo.result.toString();
			if (null != result && !"".equals(result.trim())) {
				ReqMessage resMsg = FastJSONUtil.getJSONToEntity(result, ReqMessage.class);
				// 判断返回标识状态是否为空
				if (null == resMsg || null == resMsg.getResCode() || "".equals(resMsg.getResCode())) {				
					LogUtil.promptInfo(CalendarListActivity.this, ErrorCodeContrast.getErrorCode("0", res));
					return;
				// 验证不合法
				}else if ("1103".equals(resMsg.getResCode()) || "1104".equals(resMsg.getResCode())) {
					LogUtil.promptInfo(CalendarListActivity.this, ErrorCodeContrast.getErrorCode(resMsg.getResCode(), res));
					Intent mIntent = new Intent(BroadcastNotice.USER_EXIT);
					sendBroadcast(mIntent);
					return;
				}else if("1210".equals(resMsg.getResCode())){
					LogUtil.promptInfo(CalendarListActivity.this, ErrorCodeContrast.getErrorCode(resMsg.getResCode(), res));	
					Intent mIntent = new Intent(BroadcastNotice.WIPE_USET);
					sendBroadcast(mIntent);
					return;
				}else if (!"1".equals(resMsg.getResCode())) {
					LogUtil.promptInfo(CalendarListActivity.this, ErrorCodeContrast.getErrorCode(resMsg.getResCode(), res));	
					return;
				}
				
				if (null == resMsg.getMessage() || "".equals(resMsg.getMessage().trim())) {
					DialogUtil.showLongToast(CalendarListActivity.this, R.string.no_data);
					return;
				}
				
				Message msg = new Message();
				if(operType == 0){
					msg.what = 1;
				}else{
					msg.what = 2;
				}
				msg.obj = EncryptUtil.getDecodeData(resMsg.getMessage(), BaseApp.key);
				mHandler.sendMessage(msg);
			} else {
				LogUtil.promptInfo(CalendarListActivity.this, ErrorCodeContrast.getErrorCode("0", res));
			}
		}

		@Override
		public void onFailure(HttpException error, String resMsg) {
			if(loadType == 1){
				DialogUtil.closeDialog();
			}else{
				stopLoad();
			}
			if(null != resMsg && resMsg.equals(LocalConstants.API_KEY)){
				LogUtil.promptInfo(CalendarListActivity.this, ErrorCodeContrast.getErrorCode("1207", res));
			}else{
				LogUtil.promptInfo(CalendarListActivity.this, ErrorCodeContrast.getErrorCode("0", res));
			}
		}
	};	
	 
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.btn_back:
				finish();
				break;
		}
	}

	@Override
	public void onRefresh() {
		loadType = 0;
		pageNo = 1;
		operType = 0;
		operCalendar();
	}

	@Override
	public void onLoadMore() {
		loadType = 0;
		operType = 1;
		pageNo++;
		operCalendar();
	}
}