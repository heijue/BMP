package com.k2.mobile.app.controller.activity.menu.consumption;

import java.util.ArrayList;
import java.util.List;

import com.alibaba.fastjson.JSON;
import com.k2.mobile.app.R;
import com.k2.mobile.app.common.config.BroadcastNotice;
import com.k2.mobile.app.common.config.LocalConstants;
import com.k2.mobile.app.common.exception.HttpException;
import com.k2.mobile.app.controller.core.BaseActivity;
import com.k2.mobile.app.controller.core.BaseApp;
import com.k2.mobile.app.model.adapter.PersonalConsumptionAdapter;
import com.k2.mobile.app.model.bean.PConsumptionBean;
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
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

/**
 * 
 * @ClassName: PersonalConsumptionQueryActivity
 * @Description: 个人消费查询
 * @author linqijun
 * @date 2015-3-12 下午9:00:26
 *
 */
public class PersonalConsumptionQueryActivity extends BaseActivity implements OnClickListener {
	
	// 返回
	private Button btn_back;
	// 头部标题
	private TextView tv_title;
	private TextView tv_sech;
	// 用户名, 工号
	private TextView tv_username, tv_userid;
	// 记录
	private XListView lv_consumption = null;
	// 适配器
	private PersonalConsumptionAdapter pcAdapter = null;
	// 服务器返回数据集
	private List<PConsumptionBean> pcBeanList = null;
	// 加载服务器类型 0,不显示进度条， 1显示
	private int loadType = 1;
	// 分页当前页
	private int pageNo = 0;
	// 分页每页数
	private final int pageSize = 10;
	// 操作类型 0是上拉刷新 1，下拉刷新
	private int operType = 0;
	
	private Handler mHandler = new Handler(){
		@Override
		public void dispatchMessage(Message msg) {
			switch (msg.what) {
				case 1:
					String json = (String) msg.obj;
					if (null != json &&  !"".equals(json.trim())) {
						pcBeanList.clear();
						List<PConsumptionBean> tmpList = JSON.parseArray(json, PConsumptionBean.class);
						if(null != tmpList){
							pcBeanList.addAll(tmpList);
							pcAdapter.notifyDataSetChanged();
						}
						
						String tmpTips = null;
						
						if((null == tmpList || 1 > tmpList.size()) && 1 > pcBeanList.size()){ // 暂无数据
							tmpTips = getString(R.string.no_data);
						}else if(null != tmpList && tmpList.size() < 10){			// 所有数据加载完
							tmpTips = getString(R.string.all_data_loaded);
						}else{													// 查看更多
							tmpTips = getString(R.string.xlistview_footer_hint_normal);
						}
						
						lv_consumption.setTips(tmpTips);
					}
					break;
				case 2:
					String jsons = (String) msg.obj;
					if (null != jsons &&  !"".equals(jsons.trim())) {
						List<PConsumptionBean> tmpList = JSON.parseArray(jsons, PConsumptionBean.class);
						if(null != tmpList){
							pcBeanList.addAll(tmpList);
							pcAdapter.notifyDataSetChanged();
						}
						

						String tmpTips = null;
						
						if((null == tmpList || 1 > tmpList.size()) && 1 > pcBeanList.size()){ // 暂无数据
							tmpTips = getString(R.string.no_data);
						}else if(null != tmpList && tmpList.size() < 10){			// 所有数据加载完
							tmpTips = getString(R.string.all_data_loaded);
						}else{													// 查看更多
							tmpTips = getString(R.string.xlistview_footer_hint_normal);
						}
						
						lv_consumption.setTips(tmpTips);
					}else{
						String tmpTips = null;
						if(1 > pcBeanList.size()){ // 暂无数据
							tmpTips = getString(R.string.no_data);
						}else {													// 查看更多
							tmpTips = getString(R.string.all_data_loaded);
						}
						
						lv_consumption.setTips(tmpTips);
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
		setContentView(R.layout.activity_menu_personal_con_qry);
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
		tv_sech = (TextView) findViewById(R.id.tv_sech);
		btn_back = (Button) findViewById(R.id.btn_back);
		lv_consumption = (XListView) findViewById(R.id.lv_show_consumption);
		
		// 上拉加载数据
		lv_consumption.setPullLoadEnable(true);
		// 下拉加载数据
		lv_consumption.setPullRefreshEnable(true);
		// 设置头部提示
		tv_title.setText(getString(R.string.person_consume_query));
		
		pcBeanList = new ArrayList<PConsumptionBean>();
		pcAdapter = new PersonalConsumptionAdapter(this, pcBeanList);
		lv_consumption.setAdapter(pcAdapter);
		
		tv_sech.setVisibility(View.GONE);
		
		tv_username = (TextView) findViewById(R.id.tv_username);
		tv_userid = (TextView) findViewById(R.id.tv_userid);
		tv_username.setText(BaseApp.user.getUserName());
		tv_userid.setText(BaseApp.user.getUserId());

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
		lv_consumption.setXListViewListener(viewListView);
	}
	
	private IXListViewListener viewListView = new IXListViewListener() {
		
		@Override
		public void onRefresh() {
			loadType = 0;
			pageNo = 0;
			operType = 0;
			queryConsume();
		}
		
		@Override
		public void onLoadMore() {
			loadType = 0;
			operType = 1;
			pageNo++;
			queryConsume();
		}
	};
	
	private void stopLoad(){
		lv_consumption.stopRefresh();
		lv_consumption.stopLoadMore();
		lv_consumption.setRefreshTime(res.getString(R.string.just));
	}
	/**
	 * @Title: queryConsume
	 * @Description: 请求查询个人消费
	 * @param void
	 * @return void 
	 * @throws
	 */
	private void queryConsume(){
		if (!NetWorkUtil.isNetworkAvailable(PersonalConsumptionQueryActivity.this)) {            // 判断网络是否连接
			DialogUtil.showLongToast(PersonalConsumptionQueryActivity.this, R.string.global_network_disconnected);
		}else{
			String info = getUserPCInfo();
			SendRequest.sendSubmitRequest(PersonalConsumptionQueryActivity.this, info, BaseApp.token, BaseApp.reqLang, 
					LocalConstants.MY_CONSUME_SERVER, BaseApp.key, submitCallBack);	
		}
	}
	
	/**
	 * @Title: getUserPCInfo
	 * @Description: 获取用户消费记录
	 * @param void
	 * @return String 返回的数据 
	 * @throws
	 */
	private String getUserPCInfo(){
				
		PConsumptionBean bean = new PConsumptionBean();
		bean.setDeviceId(BaseApp.macAddr);
		bean.setUserId(BaseApp.user.getUserId());
		bean.setPageNo(pageNo+"");
		bean.setPageSize(pageSize+"");
		
		String json = JSON.toJSONString(bean);
		
		return json;
	}

	/**
	 * http请求回调
	 */
	RequestCallBack<String> submitCallBack = new RequestCallBack<String>() {

		@Override
		public void onStart() {
			if(loadType == 1){
				DialogUtil.showWithCancelProgressDialog(PersonalConsumptionQueryActivity.this, null, res.getString(R.string.global_prompt_message), null);
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
			
			Message msgs = new Message();
			String result = responseInfo.result.toString();
			if (null != result && !"".equals(result.trim())) {
				ReqMessage msg = FastJSONUtil.getJSONToEntity(result, ReqMessage.class);
				// 判断返回标识状态是否为空
				if (null == msg || null == msg.getResCode() || "".equals(msg.getResCode())) {				
					LogUtil.promptInfo(PersonalConsumptionQueryActivity.this, ErrorCodeContrast.getErrorCode("0", res));
				}else if ("1103".equals(msg.getResCode()) || "1104".equals(msg.getResCode())) {
					LogUtil.promptInfo(PersonalConsumptionQueryActivity.this, ErrorCodeContrast.getErrorCode(msg.getResCode(), res));	
					Intent mIntent = new Intent(BroadcastNotice.USER_EXIT);
					sendBroadcast(mIntent);
					return;
				}else if("1210".equals(msg.getResCode())){
					LogUtil.promptInfo(PersonalConsumptionQueryActivity.this, ErrorCodeContrast.getErrorCode(msg.getResCode(), res));	
					Intent mIntent = new Intent(BroadcastNotice.WIPE_USET);
					PersonalConsumptionQueryActivity.this.sendBroadcast(mIntent);
					return;
				}else if("1".equals(msg.getResCode())){
//					if(null == msg.getMessage() || "".equals(msg.getMessage().trim())){
//						return;
//					}
					// 获取解密后并校验后的值
					String decode = EncryptUtil.getDecodeData(msg.getMessage(), BaseApp.key);
					if(operType == 0){
						msgs.what = 1;
					}else{
						msgs.what = 2;
					}
					msgs.obj = decode;
					mHandler.sendMessage(msgs);
				}else {
					LogUtil.promptInfo(PersonalConsumptionQueryActivity.this, ErrorCodeContrast.getErrorCode(msg.getResCode(), res));
				}
			} else {
				LogUtil.promptInfo(PersonalConsumptionQueryActivity.this, ErrorCodeContrast.getErrorCode("0", res));
			}
		}

		@Override
		public void onFailure(HttpException error, String msg) {
			if(loadType == 1){
				DialogUtil.closeDialog();
			}else{
				stopLoad();
			}
			if(null != msg && msg.equals(LocalConstants.API_KEY)){
				LogUtil.promptInfo(PersonalConsumptionQueryActivity.this, ErrorCodeContrast.getErrorCode("1207", res));
			}else{
				LogUtil.promptInfo(PersonalConsumptionQueryActivity.this, ErrorCodeContrast.getErrorCode("0", res));
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