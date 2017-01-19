package com.k2.mobile.app.controller.activity.menu.companyNew;

import java.util.ArrayList;
import java.util.List;

import com.alibaba.fastjson.JSON;
import com.k2.mobile.app.R;
import com.k2.mobile.app.common.config.BroadcastNotice;
import com.k2.mobile.app.common.config.LocalConstants;
import com.k2.mobile.app.common.exception.HttpException;
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
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

public class NewsProcessFragment extends Fragment implements IXListViewListener, OnClickListener {
	
	// 第三方下拉刷新
	private XListView mXListView;
	private View view = null;
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
				System.out.println("json = "+json);
				if(null != json){
					List<CommonNewsBean> list = JSON.parseArray(json, CommonNewsBean.class);
					if(null != list){
						if(operClass != 2){
							newsList.clear();
						}
						newsList.addAll(list);
						mListAdapter.notifyDataSetChanged();
					}
				}
				break;
			}
		};
	};

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.fragment_news_process, null);
		initView();
		initListener();

		return view;
	}

	/**
	 * 方法名: initView() 
	 * 功 能 : 初始化布局控件 
	 * 参 数 : void 
	 * 返回值: void
	 */
	private void initView() {

		newsList = new ArrayList<CommonNewsBean>();
		mXListView = (XListView) view.findViewById(R.id.lv_news_show);
		// 设置不让它下拉更新
		mXListView.setPullRefreshEnable(true);
		// 设置让它上拉更新
		mXListView.setPullLoadEnable(true);
		mListAdapter = new NewsProcessListAdapter(getActivity(), newsList);
		mXListView.setAdapter(mListAdapter);
		mXListView.setXListViewListener(this);
	}

	/**
	 * 方法名: initListener() 
	 * 功 能 : 初始化监听事件 
	 * 参 数 : void 
	 * 返回值: void
	 */
	private void initListener() {
		mXListView.setXListViewListener(this);
		mXListView.setOnItemClickListener(itemListener);
	}

	OnItemClickListener itemListener = new OnItemClickListener() {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			CommonNewsBean cnBean = newsList.get(position - 1);
			Intent mIntent = new Intent(getActivity(), NewsInfolActivity.class);
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
		if (!NetWorkUtil.isNetworkAvailable(getActivity())) {
			DialogUtil.showLongToast(getActivity(), R.string.global_network_disconnected);
		} else {
			String info = remoteRequestData();
			SendRequest.sendSubmitRequest(getActivity(), info, BaseApp.token, BaseApp.reqLang, LocalConstants.NEW_PROCESS_SERVER, BaseApp.key, submitCallBack);
		}
	}

	/**
	 * http请求回调
	 */
	RequestCallBack<String> submitCallBack = new RequestCallBack<String>() {

		@Override
		public void onStart() {
			if (1 == operClass) {
				DialogUtil.showWithCancelProgressDialog(getActivity(), null, getResources().getString(R.string.global_prompt_message), null);
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
					LogUtil.promptInfo(getActivity(), ErrorCodeContrast.getErrorCode("0", getResources()));
					return;
				} else if ("1103".equals(msgs.getResCode()) || "1104".equals(msgs.getResCode())) {
					LogUtil.promptInfo(getActivity(), ErrorCodeContrast.getErrorCode(msgs.getResCode(), getResources()));	
					Intent mIntent = new Intent(BroadcastNotice.USER_EXIT);
					getActivity().sendBroadcast(mIntent);
					return;
				} else if("1210".equals(msgs.getResCode())){
					LogUtil.promptInfo(getActivity(), ErrorCodeContrast.getErrorCode(msgs.getResCode(), getResources()));	
					Intent mIntent = new Intent(BroadcastNotice.WIPE_USET);
					getActivity().sendBroadcast(mIntent);
					return;
				} else if ("1".equals(msgs.getResCode())) {
					Message msg = new Message();
					String decode = EncryptUtil.getDecodeData(msgs.getMessage(), BaseApp.key);
					msg.what = 1;
					msg.obj = decode;
					mHandler.sendMessage(msg);
				}
			} else {
				LogUtil.promptInfo(getActivity(), ErrorCodeContrast.getErrorCode("0", getResources()));
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
				LogUtil.promptInfo(getActivity(), ErrorCodeContrast.getErrorCode("1207", getResources()));
			}else{
				LogUtil.promptInfo(getActivity(), ErrorCodeContrast.getErrorCode("0", getResources()));
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
		mXListView.setRefreshTime(getResources().getString(R.string.just));
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.btn_back:
				break;
		}
	}
}