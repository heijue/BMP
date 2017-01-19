package com.k2.mobile.app.controller.activity.menu.email;

import java.util.ArrayList;
import java.util.List;

import com.alibaba.fastjson.JSON;
import com.k2.mobile.app.R;
import com.k2.mobile.app.common.config.BroadcastNotice;
import com.k2.mobile.app.common.config.LocalConstants;
import com.k2.mobile.app.common.exception.HttpException;
import com.k2.mobile.app.controller.core.BaseActivity;
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
import com.k2.mobile.app.utils.ScreenUtils;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.annotation.SuppressLint;
import android.content.Intent;

/**
 * 
 * @ClassName: MyEmailActivity
 * @Description: 我的邮件
 * @author linqijun
 * @date 2015-3-12 下午8:56:46
 *
 */
public class MyEmailActivity extends BaseActivity implements OnClickListener {
	
	// 返回
	private Button btn_go_back;
	// 头部标题
	private TextView tv_title;
	// 搜索
	private TextView tv_w_email;
	private TextView tv_box_sum;
//	private TextView tv_s_search;
	private LinearLayout ll_write_email, ll_sendmail, ll_wastebasket, ll_drafts;
	private RelativeLayout rl_inbox;
	// 展示邮件
	private ListView lv_show;
	// 展示数据集
	private List<EmailBean> eList = null;  
	private EmailBean rBean = null;
//	private ClearEditText cet_filter = null;
	// 适配器
	private EmailHomeAdapter ehAdapter = null;
	// 操作类型
	private int operType = 1;
	// 跳转
	private Intent mIntent = null;
	// 标识是否是查询邮件操作
	private boolean flag = false;
	
	private Handler mHandler = new Handler(){
		@Override
		public void dispatchMessage(Message msg) {
			switch (msg.what) {
				case 1:
					String json = (String) msg.obj;
					if (null != json) {
						List<EmailBean> mList = JSON.parseArray(json, EmailBean.class);
						if(null != mList){
							setData(mList);
							initAdapter();
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
		setContentView(R.layout.activity_menu_workbench_my_email);
		initView();
		initListener();
		BaseApp.addActivity(this);
		
		String info = receiveMailQuest();
		remoteDataRequest(LocalConstants.EMAIL_HOME_SERVER, info);
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
		tv_w_email = (TextView) findViewById(R.id.tv_w_email);
		btn_go_back = (Button) findViewById(R.id.btn_back);
		lv_show = (ListView) findViewById(R.id.lv_show);
		tv_box_sum = (TextView) findViewById(R.id.tv_box_sum);
		rl_inbox = (RelativeLayout) findViewById(R.id.rl_inbox); 
		ll_write_email = (LinearLayout) findViewById(R.id.ll_write_email);
		ll_sendmail = (LinearLayout) findViewById(R.id.ll_sendmail); 
		ll_wastebasket = (LinearLayout) findViewById(R.id.ll_wastebasket); 
		ll_drafts = (LinearLayout) findViewById(R.id.ll_drafts);
		
		tv_w_email.setVisibility(View.GONE);
		tv_title.setText(getString(R.string.my_mails));		
	}
	
	/**
	 * 方法名: initListener() 
	 * 功 能 : 初始化 监听器 
	 * 参 数 : void 
	 * 返回值: void
	 */
	private void initListener() {
		btn_go_back.setOnClickListener(this);
		ll_write_email.setOnClickListener(this);
		lv_show.setOnItemClickListener(itemListener);
		rl_inbox.setOnClickListener(this);  
		ll_sendmail.setOnClickListener(this); 
		ll_wastebasket.setOnClickListener(this);
		ll_drafts.setOnClickListener(this);
	}
	/**
	 * 方法名: initAdapter() 
	 * 功 能 : 初始化 适配器 
	 * 参 数 : void 
	 * 返回值: void
	 */
	private void initAdapter(){
		ehAdapter = new EmailHomeAdapter(this, eList);
		lv_show.setAdapter(ehAdapter);
		ScreenUtils.setListViewHeightBasedOnChildren(lv_show);
	}
	/**
	 * 方法名: setData() 
	 * 功 能 : 设置填充数据 
	 * 参 数 : void 
	 * 返回值: void
	 */
	private void setData(List<EmailBean> eBean){
		if (null != eList) {
			eList.clear();
		}else{
			eList = new ArrayList<EmailBean>();
		}
		// 显示收邮件的数量
		if(null != eBean){
			for (int i = 0; i < eBean.size(); i++){
				if(null != eBean && "1".equals(eBean.get(i).getFolderType().trim())){
					rBean = eBean.get(i);
					tv_box_sum.setText(rBean.getMailCount());
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
	// 事件
	OnItemClickListener itemListener = new OnItemClickListener() {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			EmailBean bean = (EmailBean)parent.getItemAtPosition(position);
			queryList(bean.getFolderType(), bean.getFolderCode(), bean.getFolderName());
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
				
		mIntent = new Intent(MyEmailActivity.this, MailSearchActivity.class);
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
		if (!NetWorkUtil.isNetworkAvailable(this)) {
			DialogUtil.showLongToast(this, R.string.global_network_disconnected);
		}else{
			if (null != info) {
				SendRequest.sendSubmitRequest(this, info, BaseApp.token, BaseApp.reqLang, server, BaseApp.key, submitCallBack);
			}
		}
	}
	
	/**
	 * http请求回调
	 */
	RequestCallBack<String> submitCallBack = new RequestCallBack<String>() {

		@Override
		public void onStart() {
			DialogUtil.showWithCancelProgressDialog(MyEmailActivity.this, null, res.getString(R.string.global_prompt_message), null);
		}

		@Override
		public void onLoading(long total, long current, boolean isUploading) {

		}
		
		@Override
		public void onSuccess(ResponseInfo<String> responseInfo) {                                     
			DialogUtil.closeDialog();
			String result = responseInfo.result.toString();
			if (null != result && !"".equals(result.trim())) {
				ReqMessage msg = FastJSONUtil.getJSONToEntity(result, ReqMessage.class);
				// 判断返回标识状态是否为空
				if (null == msg || null == msg.getResCode() || "".equals(msg.getResCode())) {				
					LogUtil.promptInfo(MyEmailActivity.this, ErrorCodeContrast.getErrorCode("0", res));
					return;
				// 验证不合法
				} else if ("1103".equals(msg.getResCode()) || "1104".equals(msg.getResCode())) {
					LogUtil.promptInfo(MyEmailActivity.this, ErrorCodeContrast.getErrorCode(msg.getResCode(), res));	
					Intent mIntent = new Intent(BroadcastNotice.USER_EXIT);
					sendBroadcast(mIntent);
					return;
				} else if("1210".equals(msg.getResCode())){
					LogUtil.promptInfo(MyEmailActivity.this, ErrorCodeContrast.getErrorCode(msg.getResCode(), res));	
					Intent mIntent = new Intent(BroadcastNotice.WIPE_USET);
					sendBroadcast(mIntent);
					return;
				} else if (!"1".equals(msg.getResCode())) {
					LogUtil.promptInfo(MyEmailActivity.this, ErrorCodeContrast.getErrorCode(msg.getResCode(), res));	
					return;
				}
				// 判断消息体是否为空
				if (null == msg.getMessage() || "".equals(msg.getMessage().trim())) {		 
					DialogUtil.showLongToast(MyEmailActivity.this, R.string.global_no_data);
				} else {
					// 获取解密后并校验后的值
					String decode = EncryptUtil.getDecodeData(msg.getMessage(), BaseApp.key);
					Message msgs = new Message();
					msgs.what = operType;
					msgs.obj = decode;
					mHandler.sendMessage(msgs); 
				}
			} else {
				LogUtil.promptInfo(MyEmailActivity.this, ErrorCodeContrast.getErrorCode("0", res));
			}
		}

		@Override
		public void onFailure(HttpException error, String msg) {
			DialogUtil.closeDialog();
			if(null != msg && msg.equals(LocalConstants.API_KEY)){
				LogUtil.promptInfo(MyEmailActivity.this, ErrorCodeContrast.getErrorCode("1207", res));
			}else{
				LogUtil.promptInfo(MyEmailActivity.this, ErrorCodeContrast.getErrorCode("0", res));
			}
		}
	};
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			// 返回
			case R.id.btn_back:
				this.finish();
				break;
			case R.id.ll_write_email:			// 写邮件
				startActivity(WriteEmailActivity.class);
				break;
//			case R.id.tv_s_search:			// 邮件搜索
//				String Keyword = cet_filter.getText().toString();
//				if(null == Keyword || "".equals(Keyword.trim())){
//					DialogUtil.showLongToast(MyEmailActivity.this, R.string.keyword_is_null);
//					break;
//				}
//				operType = 2;
//				String json = searchEmail(Keyword, 1);
//				remoteDataRequest(LocalConstants.EMAIL_SEARCH_SERVER, json);
//				break;
			case R.id.rl_inbox:
				queryList(rBean.getFolderType(), rBean.getFolderCode(), getString(R.string.receive_box));
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
}