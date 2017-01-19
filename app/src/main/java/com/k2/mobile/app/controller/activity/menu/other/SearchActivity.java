package com.k2.mobile.app.controller.activity.menu.other;

import java.util.ArrayList;
import java.util.List;

import com.alibaba.fastjson.JSON;
import com.k2.mobile.app.R;
import com.k2.mobile.app.common.config.LocalConstants;
import com.k2.mobile.app.common.exception.HttpException;
import com.k2.mobile.app.controller.activity.menu.addressList.MyAddressBookDetailActivity;
import com.k2.mobile.app.controller.core.BaseActivity;
import com.k2.mobile.app.controller.core.BaseApp;
import com.k2.mobile.app.model.adapter.SearchAdapter;
import com.k2.mobile.app.model.bean.Contacts;
import com.k2.mobile.app.model.bean.PublicRequestBean;
import com.k2.mobile.app.model.bean.PublicResultBean;
import com.k2.mobile.app.model.bean.ReqBodyBean;
import com.k2.mobile.app.model.bean.SearchBean;
import com.k2.mobile.app.model.http.ResponseInfo;
import com.k2.mobile.app.model.http.callback.RequestCallBack;
import com.k2.mobile.app.model.http.other.SendRequest;
import com.k2.mobile.app.utils.DialogUtil;
import com.k2.mobile.app.utils.EncryptUtil;
import com.k2.mobile.app.utils.ErrorCodeContrast;
import com.k2.mobile.app.utils.LogUtil;
import com.k2.mobile.app.utils.NetWorkUtil;
import com.k2.mobile.app.utils.PublicResHeaderUtils;
import com.k2.mobile.app.utils.StringUtil;
import com.k2.mobile.app.view.widget.ClearEditText;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.content.Intent;

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
public class SearchActivity extends BaseActivity implements OnClickListener {
	
	private TextView tv_search;
	private TextView tv_tips;
	private ListView lv_keyword;
	private List<SearchBean> keylist = null;
	private ClearEditText et_keyword;
	private SearchAdapter adapter;
	private Button btn_delete;
	private int operClass = 1;
	private boolean flag = false;
	
	private Handler mHandler = new Handler(){
		@Override
		public void dispatchMessage(Message msg) {
			String json = "";
			switch (msg.what) {
				case 1:
					json = (String)msg.obj;
					if (null == json || "".equals(json)) {
						DialogUtil.showLongToast(SearchActivity.this, R.string.no_data);
						break;
					}
					List<SearchBean> slist = JSON.parseArray(json, SearchBean.class); 
					if(slist != null){
						tv_tips.setVisibility(View.GONE);
						keylist.clear();
						keylist.addAll(slist);
						adapter.notifyDataSetChanged();
					}
					
					break;
				case 2:
					DialogUtil.closeDialog();
					String jsons = (String) msg.obj;
					System.out.println("jsons = "+jsons);
					if(null == jsons || "".equals(jsons.trim())){
						break;
					}
					
					Contacts contacts = JSON.parseObject(jsons, Contacts.class);
					if(contacts != null){
						Intent mIntent = new Intent(SearchActivity.this, MyAddressBookDetailActivity.class);
						Bundle mBundle = new Bundle();  
				        mBundle.putSerializable("contacts",contacts); 
				        mIntent.putExtras(mBundle);
				        startActivity(mIntent);
				        finish();
					}
					break;
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
		et_keyword = (ClearEditText) findViewById(R.id.et_keyword);
		tv_search = (TextView) findViewById(R.id.tv_search);
		tv_tips = (TextView) findViewById(R.id.tv_tips);
		lv_keyword = (ListView) findViewById(R.id.lv_keyword);
				
		View view = LayoutInflater.from(this).inflate(R.layout.activity_search_button, null);
		btn_delete = (Button) view.findViewById(R.id.btn_delete);
		lv_keyword.addFooterView(view);
				
		btn_delete.setVisibility(View.GONE);
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
		keylist = new ArrayList<SearchBean>();
		adapter = new SearchAdapter(this, keylist);
		lv_keyword.setAdapter(adapter);		
	}

	private TextWatcher textWatcher = new TextWatcher() {
		
		@Override
		public void onTextChanged(CharSequence s, int start, int before, int count) {
			
			String strVal = s.toString();
			if(null != strVal && !"".equals(strVal.trim())){
				tv_search.setText(getString(R.string.search));
				flag = true;
				String info = "";
				if(s.length() > 1){
					if(StringUtil.checkForm(strVal)){
						if(s.length() > 6){
							info = getAddressBookUser(s.toString());
							queryUserAddrBook(info);
						}
					}else{
						info = getAddressBookUser(s.toString());
						queryUserAddrBook(info);
					}
				}
			}else{
				tv_search.setText(getString(R.string.global_cancel));
				flag = false;
			}
		}
		
		@Override
		public void beforeTextChanged(CharSequence s, int start, int count, int after) {
			//s:变化前的所有字符； start:字符开始的位置； count:变化前的总字节数；after:变化后的字节数
//			 Toast.makeText(getApplicationContext(), "变化前:"+s+";"+start+";"+count+";"+after, Toast.LENGTH_SHORT).show();
		}
		
		@Override
		public void afterTextChanged(Editable s) {
			 //s:变化后的所有字符
//		    Toast.makeText(getApplicationContext(), "变化:"+s, Toast.LENGTH_SHORT).show();
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
			
			String info = getAddressBookUser(keyword);
			queryUserAddrBook(info);
			break;
		case R.id.btn_delete:
			if(keylist.size()>0){
				keylist.clear();
				initAdatper();
			}
			break;
		default:
			break;
		}
	}
	
	private OnItemClickListener myListener = new OnItemClickListener() {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			operClass = 2;
			String userAccount = keylist.get(position).getUserAccount();
			getUserInfo(userAccount);
		}
	};
		
	/**
	 * 方法名: getAddressBookUser() 
	 * 功 能 : 请求报文
	 * 参 数 : void 
	 * 返回值: JSON报文
	 */
	private String getAddressBookUser(String keyVal) {
		ReqBodyBean bBean = new ReqBodyBean();
		bBean.setInvokeFunctionCode("F30000003");
		bBean.setInvokeParameter("[\""+keyVal+"\"]");
				
		PublicRequestBean bean = new PublicRequestBean();
		bean.setReqHeader(PublicResHeaderUtils.getReqHeader());
		bean.setReqBody(bBean);
		
		return JSON.toJSONString(bean);
	}
	
	/**
	 * @Title: startActivity
	 * @Description: 跳转Activity
	 * @return void    返回类型
	 * @throws
	 */
	public void getUserInfo(String userCode){
    	
    	ReqBodyBean bBean = new ReqBodyBean();
		bBean.setInvokeFunctionCode("F30000002");
		bBean.setInvokeParameter("[\""+userCode+"\"]");
				
		PublicRequestBean bean = new PublicRequestBean();
		bean.setReqHeader(PublicResHeaderUtils.getReqHeader());
		bean.setReqBody(bBean);
    	
    	String info = JSON.toJSONString(bean);
    	queryUserAddrBook(info);
	}
	
	/**
	 * 方法名: queryUserAddrBook() 
	 * 功 能 : 数据请求  
	 * 参 数 : info 报文数据
	 * 		   server 请求方法
	 * 返回值: void
	 */
	private void queryUserAddrBook(String info) {
		
		if(null == info || "".equals(info.trim())){
			return;
		}
		
		if (!NetWorkUtil.isNetworkAvailable(SearchActivity.this)) {
			DialogUtil.showLongToast(SearchActivity.this, R.string.global_network_disconnected);
		}else{
			if (null != info) {
				SendRequest.submitRequest(SearchActivity.this, info, submitCallBack);
			}
		}
	}
	
	/**
	 * http请求回调
	 */
	RequestCallBack<String> submitCallBack = new RequestCallBack<String>() {

		@Override
		public void onStart() {
			DialogUtil.showWithCancelProgressDialog(SearchActivity.this, null, getResources().getString(R.string.global_prompt_message), null);
		}

		@Override
		public void onLoading(long total, long current, boolean isUploading) {

		}
		
		@Override
		public void onSuccess(ResponseInfo<String> responseInfo) {                                     
			DialogUtil.closeDialog();
			String result = responseInfo.result.toString();
			byte[] ebase64 = EncryptUtil.decodeBase64(result);
			System.out.println("ebase64 = "+new String(ebase64));
			if (null != result && !"".equals(result.trim())) {
				PublicResultBean resMsg = JSON.parseObject(new String(ebase64), PublicResultBean.class);
				// 判断返回标识状态是否为空
				if (null == resMsg || null == resMsg.getResHeader().getStateCode() || "".equals(resMsg.getResHeader().getStateCode())) {				
					LogUtil.promptInfo(SearchActivity.this, ErrorCodeContrast.getErrorCode("0", res));
					return;
				}else if (!"1".equals(resMsg.getResHeader().getStateCode())) {
					LogUtil.promptInfo(SearchActivity.this, resMsg.getResHeader().getReturnMsg());	
					return;
				}
				// 判断消息体是否为空
				if (null == resMsg.getResBody().getResultString() || "".equals(resMsg.getResBody().getResultString().trim())) {		 
					LogUtil.promptInfo(SearchActivity.this, ErrorCodeContrast.getErrorCode("0", res));
					return;
				} else if("-1".equals(resMsg.getResBody().getResultString())){
					DialogUtil.showLongToast(SearchActivity.this, R.string.no_data);
					return;
				}else {
					if(null != resMsg.getResHeader().getSectetKey() && !"".equals(resMsg.getResHeader().getSectetKey().trim())){
						BaseApp.key = resMsg.getResHeader().getSectetKey();
					}
					// 获取解密后并校验后的值
					Message msgs = new Message();
					msgs.what = operClass;
					msgs.obj = resMsg.getResBody().getResultString();
					mHandler.sendMessage(msgs);
				}
			} else {
				LogUtil.promptInfo(SearchActivity.this, ErrorCodeContrast.getErrorCode("0", res));
			}	
		}

		@Override
		public void onFailure(HttpException error, String msg) {
			DialogUtil.closeDialog();
			if(null != msg && msg.equals(LocalConstants.API_KEY)){
				LogUtil.promptInfo(SearchActivity.this, ErrorCodeContrast.getErrorCode("1207", getResources()));
			}else{
				LogUtil.promptInfo(SearchActivity.this, ErrorCodeContrast.getErrorCode("0", getResources()));
			}
		}
	};
}