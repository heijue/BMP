package com.k2.mobile.app.controller.activity.fragment;

import java.util.ArrayList;
import java.util.List;

import com.alibaba.fastjson.JSON;
import com.k2.mobile.app.R;
import com.k2.mobile.app.common.config.BroadcastNotice;
import com.k2.mobile.app.common.config.LocalConstants;
import com.k2.mobile.app.common.exception.HttpException;
import com.k2.mobile.app.controller.activity.menu.email.MailSearchActivity;
import com.k2.mobile.app.controller.activity.menu.email.WriteEmailActivity;
import com.k2.mobile.app.controller.core.BaseApp;
import com.k2.mobile.app.model.adapter.EmailHomeAdapter;
import com.k2.mobile.app.model.bean.EmailBean;
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
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

/**
 * @Title EmailFragment.java
 * @Package com.oppo.mo.controller.activity.fragment
 * @Description 首页－Email
 * @Company  K2
 * 
 * @author linqijun
 * @date 2015-04-22 20:45:03
 * @version V1.0
 */
public class EmailFragment extends Fragment implements OnClickListener {
	
	private View views, view;
	// 返回
	private Button btn_go_back;
	// 头部标题
	private TextView tv_title;
	// 搜索
	private TextView tv_w_email;
	private TextView tv_box_sum;
//		private TextView tv_s_search;
	private LinearLayout ll_write_email, ll_sendmail, ll_wastebasket, ll_drafts;
	private RelativeLayout rl_inbox;
	// 展示数据集
	private List<EmailBean> eList = null;  
	private EmailBean rBean = null;
//		private ClearEditText cet_filter = null;
	// 适配器
	private EmailHomeAdapter ehAdapter = null;
	// 操作类型
	private int operType = 1;
	// 是否下拉刷新
	private int checkVal = 0;
	// 跳转
	private Intent mIntent = null;
	// 标识是否是查询邮件操作
	private boolean flag = false;
	private XListView lv_em_menu;
	
	private int x = 270;
	private int y = 200;
	
	private Handler mHandler = new Handler(){
		@Override
		public void dispatchMessage(Message msg) {
			switch (msg.what) {
				case 1:
					String json = (String) msg.obj;
					if (null != json) {
						initData();
						
						List<EmailBean> mList = JSON.parseArray(json, EmailBean.class);
						if(null != mList){
							setData(mList);
							ehAdapter.notifyDataSetChanged();
						}
					}
					
					Intent mIntent = new Intent(BroadcastNotice.EMAIL_COUNT);
					getActivity().sendBroadcast(mIntent);
					break;
					
				case 7:
					lv_em_menu.dispatchTouchEvent(MotionEvent.obtain(SystemClock.uptimeMillis(), 
		        			SystemClock.uptimeMillis(), MotionEvent.ACTION_UP, x, y, 0));
					
					lv_em_menu.dispatchTouchEvent(MotionEvent.obtain(SystemClock.uptimeMillis()+20, 
		        			SystemClock.uptimeMillis()+20, MotionEvent.ACTION_UP, 0, 0, 0));
					break;
				case 8:
					lv_em_menu.dispatchTouchEvent(MotionEvent.obtain(SystemClock.uptimeMillis(), 
		        			SystemClock.uptimeMillis(), MotionEvent.ACTION_MOVE, x, y, 0));
		        	y+=80;
					break;
			}
		};
	};
		
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		views = inflater.inflate(R.layout.activity_menu_workbench_my_email, null);
		initView();
		initListener();
		
		getEmailInfo();
		
		return views;
	}


	/**
	 * 方法名: initView() 
	 * 功 能 : 初始化 
	 * 参 数 : void 
	 * 返回值: void
	 */
	@SuppressLint("NewApi")
	private void initView() {		
		tv_title = (TextView) views.findViewById(R.id.tv_title);
		tv_w_email = (TextView) views.findViewById(R.id.tv_w_email);
		btn_go_back = (Button) views.findViewById(R.id.btn_back);
		lv_em_menu = (XListView) views.findViewById(R.id.lv_em_menu);
		
		btn_go_back.setVisibility(View.GONE);
		tv_w_email.setVisibility(View.GONE);
		tv_title.setText(getString(R.string.my_mails));
		
		lv_em_menu.setPullRefreshEnable(true);	// 设置下拉更新
		lv_em_menu.setPullLoadEnable(false);	// 设置让它上拉更新
		
		eList = new ArrayList<EmailBean>();
		initData();		
		initAdapter();
	}
	
	private void initData(){
		
		eList.clear();
		
		EmailBean tBean = new EmailBean();
		tBean.setFolderType("7");
		tBean.setFolderName(getString(R.string.email));
		eList.add(tBean);
		
		EmailBean iBean = new EmailBean();
		iBean.setFolderType("1");
		iBean.setFolderName(getString(R.string.receive_box));
		eList.add(iBean);
		
		EmailBean wBean = new EmailBean();
		wBean.setFolderType("6");
		wBean.setFolderName(getString(R.string.write_email));
		eList.add(wBean);
		
		EmailBean sBean = new EmailBean();
		sBean.setFolderType("2");
		sBean.setFolderName(getString(R.string.sent_mail));
		eList.add(sBean);
		
		EmailBean bBean = new EmailBean();
		bBean.setFolderType("4");
		bBean.setFolderName(getString(R.string.wastepaper_basket));
		eList.add(bBean);
		
		EmailBean dbBean = new EmailBean();
		dbBean.setFolderType("3");
		dbBean.setFolderName(getString(R.string.draft_box));
		eList.add(dbBean);
		
		EmailBean dBean = new EmailBean();
		dBean.setFolderType("8");
		dBean.setFolderName(getString(R.string.folders));
		eList.add(dBean);
	}
	
	/**
	 * 方法名: initListener() 
	 * 功 能 : 初始化 监听器 
	 * 参 数 : void 
	 * 返回值: void
	 */
	private void initListener() {
		lv_em_menu.setOnItemClickListener(itemListener);	
		lv_em_menu.setXListViewListener(xListener);
	}
	/**
	 * 方法名: initAdapter() 
	 * 功 能 : 初始化 适配器 
	 * 参 数 : void 
	 * 返回值: void
	 */
	private void initAdapter(){
		ehAdapter = new EmailHomeAdapter(getActivity(), eList);
		lv_em_menu.setAdapter(ehAdapter);
	}
	/**
	 * 方法名: getEmailInfo() 
	 * 功 能 : 更新邮件详情
	 * 参 数 : void 
	 * 返回值: void
	 */
	public void getEmailInfo(){
		String info = receiveMailQuest();
		remoteDataRequest(LocalConstants.EMAIL_HOME_SERVER, info);
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
    	lv_em_menu.dispatchTouchEvent(MotionEvent.obtain(SystemClock.uptimeMillis(), SystemClock.uptimeMillis(), MotionEvent.ACTION_DOWN, x, y, 0));
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
	
	/**
	 * 方法名: setData() 
	 * 功 能 : 设置填充数据 
	 * 参 数 : void 
	 * 返回值: void
	 */
	private void setData(List<EmailBean> eBean){
		// 显示收邮件的数量
		if(null != eBean){
			for (EmailBean bean : eBean){
				if( "1".equals(bean.getFolderType().trim())){
					for(int i=0; i<eList.size(); i++){
						if(bean.getFolderType().equals(eList.get(i).getFolderType())){
							eList.get(i).setFolderCode(bean.getFolderCode());
							eList.get(i).setMailCount(bean.getMailCount());
						}
					}
				}
			}
			
			// 添加自定义事项
			if (null != eBean) {
				for (int i = 0; i < eBean.size(); i++){
					if(null != eBean.get(i).getFolderType() && "5".equals(eBean.get(i).getFolderType().trim())){
						eList.add(eBean.get(i));
					}
				}
			}
		} 
	}
	
	// 上拉分页加载，下拉刷新
	IXListViewListener xListener = new IXListViewListener() {
		
		@Override
		public void onRefresh() {
			checkVal = 1;
			getEmailInfo();
		}
		
		@Override
		public void onLoadMore() {
			 
		}
	};
	
	/**
	* @Title: onLoad
	* @Description: 停止加载进度条
	* @param void
	* @return String 返回的数据 
	* @throws
	*/
	private void onLoad() {
		lv_em_menu.stopRefresh();
		lv_em_menu.stopLoadMore();
		lv_em_menu.setRefreshTime(getResources().getString(R.string.just));
	}
	
	// 事件
	OnItemClickListener itemListener = new OnItemClickListener() {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			EmailBean bean = (EmailBean)parent.getItemAtPosition(position);
			if(null == bean){
				return;
			}
			if("-1".equals(bean.getFolderType()) || "-2".equals(bean.getFolderType())){
				return;
			}else if("6".equals(bean.getFolderType())){
				Intent mIntent = new Intent(getActivity(), WriteEmailActivity.class);
				getActivity().startActivity(mIntent);
			}else{
				queryList(bean.getFolderType(), bean.getFolderCode(), bean.getFolderName());
				flag = true;
			}
		}
	};
	/**
	 * 方法名: queryList() 
	 * 功 能 : 请求查询列表
	 * 参 数 : void 
	 * 返回值: void
	 */
	private void queryList(String folderType, String folderCode, String title){
		if(null == folderType){
			return;
		}
		int tmp = 0;
		try {
			 tmp = Integer.parseInt(folderType);
		} catch (NumberFormatException e) {
			return;
		}
				
		mIntent = new Intent(getActivity(), MailSearchActivity.class);
		mIntent.putExtra("title", title);
		mIntent.putExtra("folderType", tmp);
		mIntent.putExtra("folderCode", folderCode);
		
		startActivity(mIntent);
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
		
		return JSON.toJSONString(bean);
	}
	 
	/**
	 * @Title: remoteDataRequest
	 * @Description: 远程数据请求
	 * @param server: 方法
	 * @return info: 请求参数
	 * @throws
	 */
	private void remoteDataRequest(int server, String info){
		// 判断网络是否连接
		if (!NetWorkUtil.isNetworkAvailable(getActivity())) {
			DialogUtil.showLongToast(getActivity(), R.string.global_network_disconnected);
		}else{
			if (null != info) {
				SendRequest.sendSubmitRequest(getActivity(), info, BaseApp.token, BaseApp.reqLang, server, BaseApp.key, submitCallBack);
			}
		}
	}
			
	/**
	 * http请求回调
	 */
	RequestCallBack<String> submitCallBack = new RequestCallBack<String>() {

		@Override
		public void onStart() {
			if(1 != checkVal){
				DialogUtil.showWithCancelProgressDialog(getActivity(), null, getActivity().getResources().getString(R.string.global_prompt_message), null);
			}else{
				onLoad();
			}
		}

		@Override
		public void onLoading(long total, long current, boolean isUploading) {

		}
		
		@Override
		public void onSuccess(ResponseInfo<String> responseInfo) {   
			if(1 != checkVal){
				DialogUtil.closeDialog();
			}
			String result = responseInfo.result.toString();
			if (null != result && !"".equals(result.trim())) {
				ReqMessage msg = FastJSONUtil.getJSONToEntity(result, ReqMessage.class);
				// 判断返回标识状态是否为空
				if (null == msg || null == msg.getResCode() || "".equals(msg.getResCode())) {				
					LogUtil.promptInfo(getActivity(), ErrorCodeContrast.getErrorCode("0", getActivity().getResources()));
					return;
				// 验证不合法
				} else if ("1103".equals(msg.getResCode()) || "1104".equals(msg.getResCode())) {
					LogUtil.promptInfo(getActivity(), ErrorCodeContrast.getErrorCode(msg.getResCode(), getActivity().getResources()));	
					Intent mIntent = new Intent(BroadcastNotice.USER_EXIT);
					getActivity().sendBroadcast(mIntent);
					return;
				}else if("1210".equals(msg.getResCode())){
					LogUtil.promptInfo(getActivity(), ErrorCodeContrast.getErrorCode(msg.getResCode(), getActivity().getResources()));	
					Intent mIntent = new Intent(BroadcastNotice.WIPE_USET);
					getActivity().sendBroadcast(mIntent);
					return;
				} else if (!"1".equals(msg.getResCode())) {
					LogUtil.promptInfo(getActivity(), ErrorCodeContrast.getErrorCode(msg.getResCode(), getActivity().getResources()));	
					return;
				}
				// 判断消息体是否为空
				if (null == msg.getMessage() || "".equals(msg.getMessage().trim())) {		 
					DialogUtil.showLongToast(getActivity(), R.string.global_no_data);
				} else {
					// 获取解密后并校验后的值
					String decode = EncryptUtil.getDecodeData(msg.getMessage(), BaseApp.key);
					Message msgs = new Message();
					msgs.what = operType;
					msgs.obj = decode;
					mHandler.sendMessage(msgs); 
				}
			} else {
				LogUtil.promptInfo(getActivity(), ErrorCodeContrast.getErrorCode("0", getActivity().getResources()));
			}
		}

		@Override
		public void onFailure(HttpException error, String msg) {
			if(1 != checkVal){
				DialogUtil.closeDialog();
			}else{
				onLoad();
			}
			if(null != msg && msg.equals(LocalConstants.API_KEY)){
			}else{
				LogUtil.promptInfo(getActivity(), ErrorCodeContrast.getErrorCode("0", getActivity().getResources()));
			}
		}
	};
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
//			case R.id.tv_s_search:			// 邮件搜索
//				String Keyword = cet_filter.getText().toString();
//				if(null == Keyword || "".equals(Keyword.trim())){
//					DialogUtil.showLongToast(getActivity(), R.string.keyword_is_null);
//					break;
//				}
//				operType = 2;
//				String json = searchEmail(Keyword, 1);
//				remoteDataRequest(LocalConstants.EMAIL_SEARCH_SERVER, json);
//				break;
			case R.id.rl_inbox:
				flag = true;
				queryList(rBean.getFolderType(), rBean.getFolderCode(), getString(R.string.receive_box));
				break;  
			case R.id.ll_write_email:			// 写邮件
				flag = true;
				Intent mIntent = new Intent(getActivity(), WriteEmailActivity.class);
				getActivity().startActivity(mIntent);
				break;
			case R.id.ll_sendmail:
				queryList("2", null, getString(R.string.sent_mail));
				break; 
			case R.id.ll_drafts:
				queryList("3", null, getString(R.string.draft_box));
				break;
			case R.id.ll_wastebasket:
				queryList("4", null, getString(R.string.wastepaper_basket));
				break;
		}
	}
	
	@Override
	public void onResume() {
		if(flag){
			getEmailInfo();
			flag = false;
		}
		super.onResume();
	}
//  Fragment虽然有onResume和onPause的，但是这两个方法是Activity的方法，
//	调用时机也是与Activity相同，和ViewPager搭配使用这个方法就很鸡肋了，
//	根本不是想要的效果,可使用以下方法代替
//	@Override
//	public void setUserVisibleHint(boolean isVisibleToUser) {
//		super.setUserVisibleHint(isVisibleToUser);
//		if (isVisibleToUser) {
//            //相当于Fragment的onResume
//        } else {
//            //相当于Fragment的onPause
//        }
//	}
}
