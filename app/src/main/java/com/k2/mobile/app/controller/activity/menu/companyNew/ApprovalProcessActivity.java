package com.k2.mobile.app.controller.activity.menu.companyNew;

import java.util.ArrayList;
import java.util.List;

import com.alibaba.fastjson.JSON;
import com.k2.mobile.app.R;
import com.k2.mobile.app.common.config.BroadcastNotice;
import com.k2.mobile.app.common.config.LocalConstants;
import com.k2.mobile.app.common.exception.HttpException;
import com.k2.mobile.app.controller.core.BaseActivity;
import com.k2.mobile.app.controller.core.BaseApp;
import com.k2.mobile.app.model.adapter.NewsProcessListAdapter;
import com.k2.mobile.app.model.bean.CommonNewsBean;
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

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

/**
 * 
 * @ClassName: ApprovalProcessActivity
 * @Description: 审批文件
 * @author linqijun
 * @date 2015-7-31 下午17:32:57
 *
 */
public class ApprovalProcessActivity extends BaseActivity implements IXListViewListener, OnClickListener {
	
	// 头部标题
	private TextView tv_title;
	// 返回
	private Button btn_back;
	// 搜索
	private TextView tv_search;
	// 第三方下拉刷新
	private XListView mXListView;
	private List<CommonNewsBean> newsList = null;
	private NewsProcessListAdapter mListAdapter = null;
	// 页码
	private int pageIndex = 1;
	// 每页行数
	private int pageSize = 10;
	// 操作类型
	private int operClass = 1;

	private Handler mHandler = new Handler() {
		@Override
		public void dispatchMessage(Message msg) {
			switch (msg.what) {
			case 1:
				String json = (String)msg.obj;
				if(null != json){
					List<CommonNewsBean> list = JSON.parseArray(json, CommonNewsBean.class);
					if(null != list){
						if(operClass != 2){
							newsList.clear();
						}
						newsList.addAll(list);
						mListAdapter.notifyDataSetChanged();
						
						String tmpTips = null;
						
						if((null == list || 1 > list.size()) && 1 > newsList.size()){ // 暂无数据
							tmpTips = getString(R.string.no_data);
						}else if(null != list && list.size() < 10){			// 所有数据加载完
							tmpTips = getString(R.string.all_data_loaded);
						}else{													// 查看更多
							tmpTips = getString(R.string.xlistview_footer_hint_normal);
						}
						
						mXListView.setTips(tmpTips);
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
		setContentView(R.layout.activity_news_process);
		initView();
		initListener();
	}
	
	/**
	 * 方法名: initView() 
	 * 功 能 : 初始化布局控件 
	 * 参 数 : void 
	 * 返回值: void
	 */
	private void initView() {
		
		tv_title = (TextView) findViewById(R.id.tv_title);
		btn_back = (Button) findViewById(R.id.btn_back);
		tv_search = (TextView) findViewById(R.id.tv_sech);
		
		tv_title.setText(getString(R.string.process_file));
		tv_search.setVisibility(View.GONE);
		
		mXListView = (XListView) findViewById(R.id.lv_news_show);
		newsList = new ArrayList<CommonNewsBean>();
		// 设置不让它下拉更新
		mXListView.setPullRefreshEnable(true);
		// 设置让它上拉更新
		mXListView.setPullLoadEnable(true);
		mListAdapter = new NewsProcessListAdapter(ApprovalProcessActivity.this, newsList);
		mXListView.setAdapter(mListAdapter);
		mXListView.setXListViewListener(this);
		remoteRequest();
	}

	/**
	 * 方法名: initListener() 
	 * 功 能 : 初始化监听事件 
	 * 参 数 : void 
	 * 返回值: void
	 */
	private void initListener() {
		btn_back.setOnClickListener(this);
		mXListView.setXListViewListener(this);
		mXListView.setOnItemClickListener(itemListener);
	}

	OnItemClickListener itemListener = new OnItemClickListener() {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			CommonNewsBean cnBean = newsList.get(position - 1);
			Intent mIntent = new Intent(ApprovalProcessActivity.this, NewsInfolActivity.class);
			mIntent.putExtra("formInstanceCode", cnBean.getFormInstanceCode());
			mIntent.putExtra("type", 1);
			mIntent.putExtra("procVerID", cnBean.getProcVerID());
			mIntent.putExtra("procSetID", cnBean.getProcSetID());
			startActivity(mIntent);
		}
	};

	/**
	 * 方法名: remoteRequestData() 
	 * 功 能 : 组合请求数据 
	 * 参 数 : void 
	 * 返回值: String 请求数据
	 */
	private String remoteRequestData() {

		PublicBean bean = new PublicBean();
		bean.setDeviceId(BaseApp.macAddr);
		bean.setUserAccount(BaseApp.user.getUserId());
		bean.setPageIndex(pageIndex + "");
		bean.setPageSize(pageSize + "");

		return JSON.toJSONString(bean);
	}

	/**
	 * 方法名: remoteRequest() 
	 * 功 能 : 发送请求数据 
	 * 参 数 : void 
	 * 返回值: void
	 */
	public void remoteRequest() {
		// 查询用户菜单
		if (!NetWorkUtil.isNetworkAvailable(ApprovalProcessActivity.this)) {
			DialogUtil.showLongToast(ApprovalProcessActivity.this, R.string.global_network_disconnected);
		} else {
			String info = remoteRequestData();
			SendRequest.sendSubmitRequest(ApprovalProcessActivity.this, info, BaseApp.token, BaseApp.reqLang, LocalConstants.NEW_PROCESS_SERVER, BaseApp.key, submitCallBack);
		}
	}

	/**
	 * http请求回调
	 */
	RequestCallBack<String> submitCallBack = new RequestCallBack<String>() {

		@Override
		public void onStart() {
			if (1 == operClass) {
				DialogUtil.showWithCancelProgressDialog(ApprovalProcessActivity.this, null, res.getString(R.string.global_prompt_message), null);
			}
		}

		@Override
		public void onLoading(long total, long current, boolean isUploading) {

		}

		@Override
		public void onSuccess(ResponseInfo<String> responseInfo) {
			if (1 == operClass) {
				DialogUtil.closeDialog();
			} else {
				onLoad();
			}
			String result = responseInfo.result.toString();
			if (null != result && !"".equals(result.trim())) {
				ReqMessage msgs = FastJSONUtil.getJSONToEntity(result, ReqMessage.class);
				// 判断返回标识状态是否为空
				if (null == msgs || null == msgs.getResCode() || "".equals(msgs.getResCode())) {
					LogUtil.promptInfo(ApprovalProcessActivity.this, ErrorCodeContrast.getErrorCode("0", res));
					return;
				} else if ("1103".equals(msgs.getResCode()) || "1104".equals(msgs.getResCode())) {
					LogUtil.promptInfo(ApprovalProcessActivity.this, ErrorCodeContrast.getErrorCode(msgs.getResCode(), res));	
					Intent mIntent = new Intent(BroadcastNotice.USER_EXIT);
					ApprovalProcessActivity.this.sendBroadcast(mIntent);
					return;
				} else if("1210".equals(msgs.getResCode())){
					LogUtil.promptInfo(ApprovalProcessActivity.this, ErrorCodeContrast.getErrorCode(msgs.getResCode(), res));	
					Intent mIntent = new Intent(BroadcastNotice.WIPE_USET);
					ApprovalProcessActivity.this.sendBroadcast(mIntent);
					return;
				} else if ("1".equals(msgs.getResCode())) {
					Message msg = new Message();
					String decode = EncryptUtil.getDecodeData(msgs.getMessage(), BaseApp.key);
					msg.what = 1;
					msg.obj = decode;
					mHandler.sendMessage(msg);
				}
			} else {
				LogUtil.promptInfo(ApprovalProcessActivity.this, ErrorCodeContrast.getErrorCode("0", res));
			}
		}

		@Override
		public void onFailure(HttpException error, String msg) {
			if (1 == operClass) {
				DialogUtil.closeDialog();
			} else {
				onLoad();
			}
			if(null != msg && msg.equals(LocalConstants.API_KEY)){
				LogUtil.promptInfo(ApprovalProcessActivity.this, ErrorCodeContrast.getErrorCode("1207", res));
			}else{
				LogUtil.promptInfo(ApprovalProcessActivity.this, ErrorCodeContrast.getErrorCode("0", res));
			}
		}
	};

	// 刷新
	@Override
	public void onRefresh() {
		operClass = 3;
		pageIndex = 1;
		remoteRequest();
	}

	// 加载更多
	@Override
	public void onLoadMore() {
		operClass = 2;
		pageIndex++;
		remoteRequest();
	}

	// 停止刷新
	private void onLoad() {
		mXListView.stopRefresh();
		mXListView.stopLoadMore();
		mXListView.setRefreshTime(res.getString(R.string.just));
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
