package com.k2.mobile.app.controller.activity.menu.email;

import java.text.Collator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.k2.mobile.app.R;
import com.k2.mobile.app.common.config.BroadcastNotice;
import com.k2.mobile.app.common.config.LocalConstants;
import com.k2.mobile.app.common.exception.HttpException;
import com.k2.mobile.app.controller.core.BaseActivity;
import com.k2.mobile.app.controller.core.BaseApp;
import com.k2.mobile.app.model.adapter.SearchEmailAdapter;
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
import com.k2.mobile.app.utils.StringUtil;

/**
 * @Title: SearchActivity.java
 * @Package com.oppo.mo.model.bean
 * @Description: 搜索页
 * @Company:广东欧泊移动通讯有限公司
 * 
 * @author linqijun	
 * @date 2015-03-31 8:28:00
 * @version V1.0
 */
public class SearchEmailContactsActivity extends BaseActivity implements OnClickListener {
	
	private TextView tv_search;
	private TextView tv_tips;
	private ListView lv_keyword;
	private List<EmailBean> eList = null;
	private EditText et_keyword;
	private SearchEmailAdapter adapter;
	private Button btn_delete;
	private boolean flag = false;
	// 页码
	private int pageIndex = 1;
	// 每页行数
	private int pageSize = 20;
 
	private Handler mHandler = new Handler(){
		@Override
		@SuppressWarnings("unchecked")
		public void dispatchMessage(Message msg) {
			switch (msg.what) {
				case 1:
					String json = (String)msg.obj;
					if(null == json || "".equals(json.trim())){
						break;
					}
					eList.clear();
					
					List<EmailBean> lBean = JSON.parseArray(json, EmailBean.class);
					if(null != lBean){
						eList.addAll(lBean);
					}
					
					if(eList!=null){
//						Comparator CHINA_COMPARE = Collator.getInstance(java.util.Locale.CHINA);
						ComparatorEmailBean emailBean = new ComparatorEmailBean();
						Collections.sort(eList,emailBean);
					}
					
					if(null != eList && eList.size() > 0){
						tv_tips.setVisibility(View.GONE);
					}else{
						tv_tips.setVisibility(View.VISIBLE);
					}
//					adapter.notifyDataSetChanged();
					initAdatper();
			}
		};
	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_search);
		initView();
		initListener();
		initAdatper();
		BaseApp.addActivity(this);
	}
	
	/*
	 * 方法名: initView() 
	 * 功 能 : 初始化 
	 * 参 数 : void 
	 * 返回值: void
	 */
	private void initView() {
		et_keyword = (EditText) findViewById(R.id.et_keyword);
		tv_search = (TextView) findViewById(R.id.tv_search);
		lv_keyword = (ListView) findViewById(R.id.lv_keyword);
		
		tv_tips = (TextView) findViewById(R.id.tv_tips);
		
		View view = LayoutInflater.from(this).inflate(R.layout.activity_search_button, null);
		btn_delete = (Button) view.findViewById(R.id.btn_delete);
		lv_keyword.addFooterView(view);
				
		btn_delete.setVisibility(View.GONE);
		eList = new ArrayList<EmailBean>();
		
		String info = customOrReceiveEmail(null);
		remoteDataRequest(LocalConstants.EMAIL_CONTACTS_SERVER, info);
	}

	/*
	 * 方法名: initListener() 
	 * 功 能 : 初始化监听事件 
	 * 参 数 : void 
	 * 返回值: void
	 */
	private void initListener() {
		tv_search.setOnClickListener(this);
		btn_delete.setOnClickListener(this);
		et_keyword.addTextChangedListener(textWatcher);
		lv_keyword.setOnItemClickListener(myListener);
	}

	/*
	 * 方法名: initAdatper() 
	 * 功 能 : 初始化适配器 
	 * 参 数 : void 
	 * 返回值: void
	 */
	private void initAdatper() {
		adapter = new SearchEmailAdapter(this, eList);
		lv_keyword.setAdapter(adapter);		
	}

	private TextWatcher textWatcher = new TextWatcher() {
		
		@Override
		public void onTextChanged(CharSequence s, int start, int before, int count) {
			String strVal = s.toString();
			if(null != strVal && !"".equals(strVal.trim())){
				flag = true;
				tv_search.setText(getString(R.string.search));
				if(s.length() > 1){
					if(StringUtil.checkForm(strVal)){
						if(s.length() > 6){
							String info = customOrReceiveEmail(s.toString());
							remoteDataRequest(LocalConstants.EMAIL_CONTACTS_SERVER, info);
						}
					}else{
						String info = customOrReceiveEmail(s.toString());
						remoteDataRequest(LocalConstants.EMAIL_CONTACTS_SERVER, info);
					}
				}
			}else{
				flag = false;
				tv_search.setText(getString(R.string.global_cancel));
			}
		}
		
		@Override
		public void beforeTextChanged(CharSequence s, int start, int count, int after) {
		}
		
		@Override
		public void afterTextChanged(Editable s) {
		}
		
	};
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_back:
			finish();
			break;
		case R.id.tv_search:
			if(!flag){
				finish();
				break;
			}
			String keyword = et_keyword.getText().toString().trim();
			if (null == keyword || "".equals(keyword.trim())) {
				DialogUtil.showLongToast(this, R.string.keyword_is_null);
				break;
			}
			
			String info = customOrReceiveEmail(keyword);
			remoteDataRequest(LocalConstants.EMAIL_CONTACTS_SERVER, info);
			break;
		case R.id.btn_delete:
			if(eList.size()>0){
				eList.clear();
				initAdatper();
			}
			break;
		}
		
	}
	
	private OnItemClickListener myListener = new OnItemClickListener() {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
 
			EmailBean bean = eList.get(position);
            Intent data = new Intent();  
            Bundle bundle = new Bundle();
            bundle.putSerializable("bean", bean);
            data.putExtras(bundle);
            setResult(20, data);  
			
			finish();
		}
	};
	
	/**
	 * 方法名: customOrReceiveEmail() 
	 * 功 能 : 自定义或收邮件请求报文
	 * 参 数 : void 
	 * 返回值: void
	 */
	private String customOrReceiveEmail(String searchText){
		
		PublicBean bean = new PublicBean();
		bean.setDeviceId(BaseApp.macAddr);
		bean.setUserAccount(BaseApp.user.getUserId());
		bean.setSearchText(searchText);
		bean.setPageIndex(pageIndex + "");
		bean.setPageSize(pageSize + "");
		
		return JSON.toJSONString(bean);
	}
	/**
	 * @Title: remoteDataRequest
	 * @Description: 远程数据请求
	 * @param void
	 * @return void 
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
			DialogUtil.showWithCancelProgressDialog(SearchEmailContactsActivity.this, null, res.getString(R.string.global_prompt_message), null);
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
					LogUtil.promptInfo(SearchEmailContactsActivity.this, ErrorCodeContrast.getErrorCode("0", res));
					return;
				// 验证不合法
				}else if ("1103".equals(msg.getResCode()) || "1104".equals(msg.getResCode())) {
					LogUtil.promptInfo(SearchEmailContactsActivity.this, ErrorCodeContrast.getErrorCode(msg.getResCode(), res));	
					Intent mIntent = new Intent(BroadcastNotice.USER_EXIT);
					sendBroadcast(mIntent);
					return;
				}else if("1210".equals(msg.getResCode())){
					LogUtil.promptInfo(SearchEmailContactsActivity.this, ErrorCodeContrast.getErrorCode(msg.getResCode(), res));	
					Intent mIntent = new Intent(BroadcastNotice.WIPE_USET);
					sendBroadcast(mIntent);
					return;
				}else if (!"1".equals(msg.getResCode())) {
					LogUtil.promptInfo(SearchEmailContactsActivity.this, ErrorCodeContrast.getErrorCode(msg.getResCode(), res));	
					return;
				}
				// 判断消息体是否为空
				if (null == msg.getMessage() || "".equals(msg.getMessage().trim())) {		 
					DialogUtil.showLongToast(SearchEmailContactsActivity.this, R.string.global_no_data);
				} else {
					// 获取解密后并校验后的值
					String decode = EncryptUtil.getDecodeData(msg.getMessage(), BaseApp.key);
					Message msgs = new Message();
					msgs.what = 1;
					msgs.obj = decode;
					mHandler.sendMessage(msgs);
				}
			} else {
				LogUtil.promptInfo(SearchEmailContactsActivity.this, ErrorCodeContrast.getErrorCode("0", res));
			}
		}

		@Override
		public void onFailure(HttpException error, String msg) {
			DialogUtil.closeDialog();
			if(null != msg && msg.equals(LocalConstants.API_KEY)){
				LogUtil.promptInfo(SearchEmailContactsActivity.this, ErrorCodeContrast.getErrorCode("1207", res));
			}else{
				LogUtil.promptInfo(SearchEmailContactsActivity.this, ErrorCodeContrast.getErrorCode("0", res));
			}
		}
	};
}

class ComparatorEmailBean implements Comparator {

	@Override
	public int compare(Object arg0, Object arg1) {
		EmailBean user0 = (EmailBean) arg0;
		EmailBean user1 = (EmailBean) arg1;
		int flag = Collator.getInstance(Locale.CHINESE).compare(user0.getDisplayName(), user1.getDisplayName());
		return flag;
	}
}