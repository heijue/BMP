package com.k2.mobile.app.controller.activity.menu.addressList;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.k2.mobile.app.R;
import com.k2.mobile.app.common.config.LocalConstants;
import com.k2.mobile.app.common.exception.HttpException;
import com.k2.mobile.app.controller.activity.menu.other.SearchActivity;
import com.k2.mobile.app.controller.core.BaseActivity;
import com.k2.mobile.app.controller.core.BaseApp;
import com.k2.mobile.app.model.adapter.SortAdapter;
import com.k2.mobile.app.model.bean.Contacts;
import com.k2.mobile.app.model.bean.PublicRequestBean;
import com.k2.mobile.app.model.bean.PublicResultBean;
import com.k2.mobile.app.model.bean.ReqBodyBean;
import com.k2.mobile.app.model.http.ResponseInfo;
import com.k2.mobile.app.model.http.callback.RequestCallBack;
import com.k2.mobile.app.model.http.other.SendRequest;
import com.k2.mobile.app.utils.DateUtil;
import com.k2.mobile.app.utils.DialogUtil;
import com.k2.mobile.app.utils.EncryptUtil;
import com.k2.mobile.app.utils.ErrorCodeContrast;
import com.k2.mobile.app.utils.LogUtil;
import com.k2.mobile.app.utils.NetWorkUtil;
import com.k2.mobile.app.utils.PinyinComparator;
import com.k2.mobile.app.utils.PublicResHeaderUtils;
import com.k2.mobile.app.view.widget.SideBar;
import com.k2.mobile.app.view.widget.XListView;
import com.k2.mobile.app.view.widget.SideBar.OnTouchingLetterChangedListener;
import com.k2.mobile.app.view.widget.XListView.IXListViewListener;

/**
 * @Package com.oppo.mo.controller.activity.menu.workbench;
 * @Description 通讯录列表
 * @Company  K2
 * 
 * @ClassName: ContactslActivity
 * @Description: 通讯录列表
 * @author linqijun
 * @date 2015-4-8 下午4:04:00
 * 
 */
public class ContactslActivity extends BaseActivity implements OnClickListener,IXListViewListener {
	
	private TextView tv_title;
	private ImageView iv_per_center;
	private TextView tv_sech;
	private Button btn_back;
	private XListView sortListView;
	private SideBar sideBar;
	private TextView dialog;
	private SortAdapter adapter;
	
	private List<Contacts> cList = null;
	private PinyinComparator pinyinComparator;
	private SharedPreferences sharedPreferences;
	
	private int flag = 1;
	private Intent mIntent = null;

	public Handler mHandler = new Handler() {
		@Override
		public void dispatchMessage(Message msg) {
			switch (msg.what) {
				case 1:
					String str = (String) msg.obj;
					// 如果返回数据为空退出
					if(null == str || "".equals(str.trim())){
						break;
					}
					if(null != cList){
						cList.clear(); // 清除cList
					}else{
						cList = new ArrayList<Contacts>();
					}
					// 解析JSON数据到list
					cList.addAll(JSON.parseArray(str, Contacts.class));
					initAdapter();
					break;
				case 2:
					DialogUtil.closeDialog();
					String json = (String) msg.obj;
					if(null == json || "".equals(json.trim())){
						break;
					}
					
					Contacts contacts = JSON.parseObject(json, Contacts.class);
					if(contacts != null){
						mIntent = new Intent(ContactslActivity.this, MyAddressBookDetailActivity.class);
						Bundle mBundle = new Bundle();  
				        mBundle.putSerializable("contacts",contacts); 
				        mIntent.putExtras(mBundle);
				        startActivity(mIntent);
					}
					break;
					//初次加载
				case 3:
					DialogUtil.closeDialog();
					refreshData();
					initAdapter();
					break;
					//下拉刷新返回加载
				case 4:
					refreshData();
					onLoad(); //停止刷新
					adapter.updateListView(cList);
					break;
			}
		}
	};
	
	
	
	@Override
	protected void onCreate(Bundle paramBundle) {
		super.onCreate(paramBundle);
		// 去除头部
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.fragment_contacts);
		initView();
		initListener();
		
		String info = getAddressBookUser();
		queryUserAddrBook(info);
		
		BaseApp.addActivity(this);
	}
	
	/**
	 * @Title: refreshData
	 * @Description: 加载/刷新联系人数据
	 * @param     
	 * @return void  
	 * @throws
	 */
	private void refreshData() {
		Iterator<Contacts> iter = cList.iterator();  
		while(iter.hasNext()){  
			Contacts c = iter.next();  
		    if(c.getUserCode().trim().length() == 8){  
		    }else{
		    	iter.remove();  
		    }
		} 
		// 记录当前时间
		SharedPreferences.Editor editor = sharedPreferences.edit();
		editor.putString("time", DateUtil.getDownloadDateStr());
		editor.commit();
	}

	/**
	 * 方法名: initView() 
	 * 功 能 : 初始化 
	 * 参 数 : void 
	 * 返回值: void
	 */
	private void initView() {
		tv_title = (TextView) findViewById(R.id.tv_title);
		iv_per_center = (ImageView) findViewById(R.id.iv_per_center);
		btn_back = (Button) findViewById(R.id.btn_back);
		tv_sech = (TextView) findViewById(R.id.tv_sech);
		tv_title.setText(R.string.my_address_book);
		pinyinComparator = new PinyinComparator();
		
		sideBar = (SideBar) findViewById(R.id.sidrbar);
		dialog = (TextView) findViewById(R.id.dialog);
		dialog.getBackground().setAlpha(179);
//		mClearEditText = (ClearEditText) view.findViewById(R.id.filter_edit);
		sortListView = (XListView) findViewById(R.id.country_lvcountry);
		//设置让它下拉更新
		sortListView.setPullRefreshEnable(false);
		sortListView.setPullLoadEnable(false);
		iv_per_center.setVisibility(View.GONE);
		btn_back.setVisibility(View.VISIBLE);
		
		tv_sech.setText("");
		sideBar.setTextView(dialog);
	}
	
	/**
	 * 方法名: initListener() 
	 * 功 能 : 监听事件 
	 * 参 数 : void 
	 * 返回值: void
	 */
	private void initListener() {
		btn_back.setOnClickListener(this);
		tv_sech.setOnClickListener(this);
		sideBar.setOnTouchingLetterChangedListener(this.tlchListener);
		sortListView.setOnItemClickListener(itemClickListener);
		sortListView.setXListViewListener(this);
//		mClearEditText.addTextChangedListener(editTextWatcher);
	}
	
	/**
	 * 方法名: initAdapter() 
	 * 功 能 : 初始化适配器
	 * 参 数 : void 
	 * 返回值: void
	 */
	private void initAdapter() {
		Collections.sort(this.cList, this.pinyinComparator);
		adapter = new SortAdapter(ContactslActivity.this, this.cList);
		sortListView.setAdapter(this.adapter);
	}
			 
	/**
	 * 方法名: getAddressBookUser() 
	 * 功 能 : 请求报文
	 * 参 数 : void 
	 * 返回值: JSON报文
	 */
	private String getAddressBookUser() {
		ReqBodyBean bBean = new ReqBodyBean();
		bBean.setInvokeFunctionCode("F30000001");
				
		PublicRequestBean bean = new PublicRequestBean();
		bean.setReqHeader(PublicResHeaderUtils.getReqHeader());
		bean.setReqBody(bBean);
		
		return JSON.toJSONString(bean);
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
		
		if (!NetWorkUtil.isNetworkAvailable(ContactslActivity.this)) {
			DialogUtil.showLongToast(ContactslActivity.this, R.string.global_network_disconnected);
		}else{
			if (null != info) {
				SendRequest.submitRequest(ContactslActivity.this, info, submitCallBack);
			}
		}
	}
	
	// 右则字母点击事件
	OnTouchingLetterChangedListener tlchListener = new OnTouchingLetterChangedListener() {
		@Override
		public void onTouchingLetterChanged(String s) {
			if(null != cList && cList.size() > 0){
				//该字母首次出现的位置
				int position = adapter.getPositionForSection(s.charAt(0));
				if(position != -1){
					sortListView.setSelection(position);
				}
			}
		}
	};
		
		// listView item点击事件
	OnItemClickListener itemClickListener = new OnItemClickListener() {
	    @Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
	    	if(position == 0)return;
	    	flag = 2;
			// 利用adapter.getItem(position)来获取当前position所对应的对象
	    	String userAccount = ((Contacts)adapter.getItem(position - 1)).getUserCode();
	    	getUserInfo(userAccount);
		}
	};
		
		/**
		 * http请求回调
		 */
		RequestCallBack<String> submitCallBack = new RequestCallBack<String>() {

			@Override
			public void onStart() {
				if(flag == 4)return;
				DialogUtil.showWithCancelProgressDialog(ContactslActivity.this, null, res.getString(R.string.global_prompt_message), null);
			}

			@Override
			public void onLoading(long total, long current, boolean isUploading) {

			}
			
			@Override
			public void onSuccess(ResponseInfo<String> responseInfo) {                                     
				DialogUtil.closeDialog();
				String result = responseInfo.result.toString();
				byte[] ebase64 = EncryptUtil.decodeBase64(result);
				if (null != result && !"".equals(result.trim())) {
					PublicResultBean resMsg = JSON.parseObject(new String(ebase64), PublicResultBean.class);
					// 判断返回标识状态是否为空
					if (null == resMsg || null == resMsg.getResHeader().getStateCode() || "".equals(resMsg.getResHeader().getStateCode())) {				
						LogUtil.promptInfo(ContactslActivity.this, ErrorCodeContrast.getErrorCode("0", res));
						return;
					// 验证不合法
					}else if (!"1".equals(resMsg.getResHeader().getStateCode())) {
						LogUtil.promptInfo(ContactslActivity.this, resMsg.getResHeader().getReturnMsg());	
						return;
					}
					// 判断消息体是否为空
					if (null == resMsg.getResBody().getResultString() || "".equals(resMsg.getResBody().getResultString().trim())) {		 
						LogUtil.promptInfo(ContactslActivity.this, ErrorCodeContrast.getErrorCode("0", res));
					} else {
						if(null != resMsg.getResHeader().getSectetKey() && !"".equals(resMsg.getResHeader().getSectetKey().trim())){
							BaseApp.key = resMsg.getResHeader().getSectetKey();
						}
						// 获取解密后并校验后的值
						Message msgs = new Message();
						msgs.what = flag;
						msgs.obj = resMsg.getResBody().getResultString();
						mHandler.sendMessage(msgs);
					}
				} else {
					LogUtil.promptInfo(ContactslActivity.this, ErrorCodeContrast.getErrorCode("0", res));
				}		
			}

			@Override
			public void onFailure(HttpException error, String msg) {
				DialogUtil.closeDialog();
				if(msg.equals(LocalConstants.API_KEY)){
					LogUtil.promptInfo(ContactslActivity.this, ErrorCodeContrast.getErrorCode("1207", res));
				}else{
					LogUtil.promptInfo(ContactslActivity.this, ErrorCodeContrast.getErrorCode("0", res));
				}
			}
		};
		
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.iv_per_center:	// 个人中心
				finish();
				break;
			case R.id.tv_sech:
				mIntent = new Intent(ContactslActivity.this, SearchActivity.class);
				mIntent.putExtra("operType", 11);
				ContactslActivity.this.startActivity(mIntent);
				break;
			case R.id.btn_back:
				finish();
				break;
			}
		}
		
		/**
		 * @Title: startActivity
		 * @Description: 跳转Activity
		 * @return void    返回类型
		 * @throws
		 */
		@Override
		public void startActivity(Class<?> activity) {
			Intent intent = new Intent(ContactslActivity.this, activity);
			startActivity(intent);
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
		
		//下拉刷新
		@Override
		public void onRefresh() {
			flag = 4;
			// 向服务器发送请求
			String info = getAddressBookUser();
			queryUserAddrBook(info);
		}
		
		//上拉加载
		@Override
		public void onLoadMore() {
		}
		
		//停止刷新
		private void onLoad() {
			sortListView.stopRefresh();
			sortListView.stopLoadMore();
			sortListView.setRefreshTime(res.getString(R.string.just));
		}
}
