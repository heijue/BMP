package com.k2.mobile.app.controller.activity.menu.email;    

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.io.Serializable;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

import com.alibaba.fastjson.JSON;
import com.k2.mobile.app.R;
import com.k2.mobile.app.common.config.BroadcastNotice;
import com.k2.mobile.app.common.config.LocalConstants;
import com.k2.mobile.app.common.exception.HttpException;
import com.k2.mobile.app.controller.core.BaseActivity;
import com.k2.mobile.app.controller.core.BaseApp;
import com.k2.mobile.app.model.adapter.EmailListInfoAdapter;
import com.k2.mobile.app.model.bean.EmailBean;
import com.k2.mobile.app.model.bean.PublicBean;
import com.k2.mobile.app.model.bean.ReqMessage;
import com.k2.mobile.app.model.bean.ResPublicBean;
import com.k2.mobile.app.model.db.DBHelper;
import com.k2.mobile.app.model.db.table.Email;
import com.k2.mobile.app.model.http.ResponseInfo;
import com.k2.mobile.app.model.http.callback.RequestCallBack;
import com.k2.mobile.app.model.http.other.SendRequest;
import com.k2.mobile.app.utils.DialogUtil;
import com.k2.mobile.app.utils.EncryptUtil;
import com.k2.mobile.app.utils.ErrorCodeContrast;
import com.k2.mobile.app.utils.FastJSONUtil;
import com.k2.mobile.app.utils.LogUtil;
import com.k2.mobile.app.utils.NetWorkUtil;
import com.k2.mobile.app.view.widget.ClearEditText;
import com.k2.mobile.app.view.widget.XListView;
import com.k2.mobile.app.view.widget.XListView.IXListViewListener;
  
/**
* @Title MailSearchActivity.java
* @Package com.oppo.mo.controller.activity.menu.workbench;
* @Description 邮件－列表
* @Company  K2
* 
* @author linqijun
* @date 2015-4-9 下午16:42:43
* @version V1.0
*/
@SuppressLint("NewApi")
public class MailSearchActivity extends BaseActivity implements OnClickListener {
	
	public static final int RESULT_CODE = 1000;
	// 编辑列表
	private final int EDIT_CODE = 1001;
	// 详情
	private final int INFO_CODE = 1002;
	// 草稿
	private final int DRAFTS_CODE = 1003;
	// 返回
	private Button btn_back;
	// 标题
	private TextView tv_title;
	// 跳转到写Email
	private TextView tv_w_email;
	private TextView tc_write_email;
	private TextView tv_search;
	private ClearEditText cet_filter;
	private TextView tv_read, tv_no_read, tv_del;
	// 显示列表
	private XListView lv_show;
	// 分页索引
	private int pageIndex = 1;
	// 分页大小
	private int pageSize = 10;
	// 加载服务器类型 0上拉，1下拉，2显示
	private int loadType = 2;
	// 是否处于搜索状态0为正常 1为搜索
	private int isSearch = 0;
	// 文件夹类型
	private int folderType = 1;
	// 标识为已读（1）或未读（0）
	private int isRead = 0;
	// 操作类型
	private int operClass = 0;
	// 文件的code
	private String folderCode = null;
	// 选中的序列
	private int positions = 0;
	// 数据集
	private List<Email> esList = null;
	// 搜索列表
	private List<Email> filterDateList = new ArrayList<Email>();
	// 适配器
	private EmailListInfoAdapter eliAdapter = null;
	private Intent mIntent = null;
	private static int flag = 0;
	private List<Integer> indexList = null;
	// 广播接收
	private IncomingReceiver iReceiver = null;
	// 最后一条数据ID
	private String lastId = null;
	// 临时统计次数
	private boolean tempVal = true;
		
	private Handler mHandler = new Handler(){
		@Override
		public void dispatchMessage(Message msg) {
			switch (msg.what) {
				case 1:
					String json = (String)msg.obj;
					if(null != json){
						List<EmailBean> lbList = JSON.parseArray(json, EmailBean.class);
						if(null != lbList){
							if(0 != loadType){
								esList.clear();
								BaseApp.db.delByColumnName(Email.class, "userAccount", BaseApp.user.getUserId());
							}
							
							if(lbList.size() > 0){
								lastId = lbList.get(lbList.size() -1).getId();	
							}
							
							for(int i=0; i<lbList.size(); i++){
								Email email = new Email();
								email.setUserAccount(BaseApp.user.getUserId());
								email.setMailFolderType(folderType+"");
								email.setFolderCode(folderCode);
								switch (folderType) {
									case 1:
										email.setSenderName(lbList.get(i).getSenderName());
										email.setReceiptTime(lbList.get(i).getReceiptTime());
										break;
									case 2:
									case 3:
										email.setSenderName(lbList.get(i).getRecipient());
										email.setReceiptTime(lbList.get(i).getMailSendTime());
										break;
									case 4:
										email.setSenderName(lbList.get(i).getSenderName());
										email.setReceiptTime(lbList.get(i).getCreateTime());
										break;
									case 5:
										email.setSenderName(lbList.get(i).getSenderName());
										email.setReceiptTime(lbList.get(i).getReceiptTime());
										break;
								}
								email.setId(lbList.get(i).getId());
								email.setConvertToTask(lbList.get(i).getConvertToTask());
								email.setMailCode(lbList.get(i).getMailCode());
								email.setIsImportant(lbList.get(i).getIsImportant());
								email.setMailSize(lbList.get(i).getMailSize());
								email.setMailSubject(lbList.get(i).getMailSubject());
								email.setIsRead(lbList.get(i).getIsRead());
								email.setMailFrom(lbList.get(i).getMailFrom());
								esList.add(email);
								if(0 == loadType){
									BaseApp.db.delByColumnName(Email.class, "mailCode", email.getMailCode());
								}
								BaseApp.db.save(email);
							}
//							initAdapter();
							selectNone();
							eliAdapter.notifyDataSetChanged();
							indexList.clear();
							mHandler.sendEmptyMessage(3);							
						}
						
						String tmpTips = null;
						
						if((null == lbList || 1 > lbList.size()) && 1 > esList.size()){ // 暂无数据
							tmpTips = getString(R.string.no_data);
						}else if(null != lbList && lbList.size() < 10){			// 所有数据加载完
							tmpTips = getString(R.string.all_data_loaded);
						}else{													// 查看更多
							tmpTips = getString(R.string.xlistview_footer_hint_normal);
						}
						
						lv_show.setTips(tmpTips);
					}
					break;
				case 2:
					String jsons = (String)msg.obj;
					if(null != jsons && !"".equals(jsons)){
						EmailBean lBean = JSON.parseObject(jsons, EmailBean.class);
						if(null != lBean){
							mIntent = new Intent(MailSearchActivity.this, WriteEmailActivity.class);
							mIntent.putExtra("bean", lBean); 
							if(operClass == 4 || 1 == isSearch){
								mIntent.putExtra("list", (Serializable)filterDateList);
							}else{
								mIntent.putExtra("list", (Serializable)esList);
							}
							mIntent.putExtra("position", positions);
							startActivityForResult(mIntent, DRAFTS_CODE);
						}
					}
					break;
				case 3:
					if(indexList.size() > 0){
						if(1 == folderType || 5 == folderType){
							tv_read.setVisibility(View.VISIBLE);
							tv_no_read.setVisibility(View.VISIBLE);
						}
						tv_del.setVisibility(View.VISIBLE);
		        		tc_write_email.setVisibility(View.GONE);
		        		lv_show.setPullRefreshEnable(false);
		        		lv_show.setPullLoadEnable(false);
		        	}else{
		        		tc_write_email.setVisibility(View.VISIBLE);
		        		tv_read.setVisibility(View.GONE);
		        		tv_no_read.setVisibility(View.GONE);
						tv_del.setVisibility(View.GONE);
		        		lv_show.setPullRefreshEnable(true);
		        		lv_show.setPullLoadEnable(true);
		        	}
					break;
				case 4:				// 处理删除结果
					String resJson = (String)msg.obj;
					if(null != resJson ){
						ResPublicBean bean = JSON.parseObject(resJson, ResPublicBean.class);
						if (null != bean && "1".equals(bean.getSuccess())) {
							 LogUtil.promptInfo(MailSearchActivity.this, getString(R.string.delete_success));
							 List<Email> tmpList = new ArrayList<Email>();
							 int count = 0;
							 if(1 == isSearch){
								 for(int i=0; i<indexList.size(); i++){
									 if("0".equals(filterDateList.get(indexList.get(i)).getIsRead())){
											count++;
									  }
									 tmpList.add(filterDateList.get(indexList.get(i)));
								 }
								 
								 filterDateList.removeAll(tmpList);
							 }else{
								 for(int i=0; i<indexList.size(); i++){
									 if("0".equals(esList.get(indexList.get(i)).getIsRead())){
											count++;
									  }
									 tmpList.add(esList.get(indexList.get(i)));
								 }
							 }
							 
							 esList.removeAll(tmpList);
							 eliAdapter.notifyDataSetChanged();
							 
							 indexList.clear();
							 setDrawable(0);
							 selectNone();
							 mHandler.sendEmptyMessage(3);
							 //发送广播减少未读邮件
							 mIntent = new Intent(BroadcastNotice.EMAIL_COUNT_SUB);
							 mIntent.putExtra("subCount", count);
							 sendBroadcast(mIntent);
						}
					}
					break;
				case 5:				// 处理标识为已读结果
					String jsonRead = (String)msg.obj;
					if(null != jsonRead ){
						ResPublicBean bean = JSON.parseObject(jsonRead, ResPublicBean.class);
						int count = 0;
						if (null != bean && "1".equals(bean.getSuccess())) {
							List<Email> tmpList = new ArrayList<Email>();
							if(1 == isSearch){
								for(int i=0; i<indexList.size(); i++){
									if("0".equals(esList.get(indexList.get(i)).getIsRead())){
										count++;
									}
									esList.remove(filterDateList.get(indexList.get(i)));
									if(0 == isRead){
										filterDateList.get(indexList.get(i)).setIsRead("0");
									}else if(1 == isRead){
										filterDateList.get(indexList.get(i)).setIsRead("1");
									}
									
									tmpList.add(filterDateList.get(indexList.get(i)));
								 }
								esList.addAll(tmpList);
							}else {
								for(int i=0; i<indexList.size(); i++){
									if("0".equals(esList.get(indexList.get(i)).getIsRead())){
										count++;
									}
									
									if(0 == isRead){
										esList.get(indexList.get(i)).setIsRead("0");
									}else if(1 == isRead){
										esList.get(indexList.get(i)).setIsRead("1");
									}
								}
							}
							
							eliAdapter.notifyDataSetChanged();
							indexList.clear();
							setDrawable(0);
							selectNone();
							mHandler.sendEmptyMessage(3);
							
							// 发送广播减少未读邮件
							mIntent = new Intent(BroadcastNotice.EMAIL_COUNT_SUB);
							mIntent.putExtra("subCount", count);
							sendBroadcast(mIntent);
						}
					}
					break;
					
				case 6:
					String resStr = (String)msg.obj;
					if(null != resStr && !"".equals(resStr.trim())){
						List<EmailBean> lbList = JSON.parseArray(resStr, EmailBean.class);
						if(null != lbList){
							if(null == filterDateList){
								filterDateList = new ArrayList<Email>();
							}else{
								filterDateList.clear();
							}
							for(int i=0; i<lbList.size(); i++){
								Email email = new Email();
								email.setUserAccount(BaseApp.user.getUserId());
								email.setId(lbList.get(i).getId());
								email.setMailFolderType(folderType+"");
								email.setFolderCode(folderCode);
								email.setSenderName(lbList.get(i).getCreated_By());
								email.setReceiptTime(lbList.get(i).getCreation_Date());
								email.setMailCode(lbList.get(i).getModuleid());
								email.setIsImportant("0");
								email.setMailSubject(lbList.get(i).getTitle());
								email.setIsRead("1");
								filterDateList.add(email);
							}
							
							eliAdapter.updateListView(filterDateList);
						}
					}
					break;
			}
		};
	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);	 // 去掉头部
		setContentView(R.layout.activity_mail_search);
		initView();
		initListener();
		createFilter();
		BaseApp.addActivity(this);
	}
	
	/**
	 * 方法名: initView() 
	 * 功 能 : 初始化 
	 * 参 数 : void 
	 * 返回值: void
	 */
	private void initView() {
		btn_back = (Button) findViewById(R.id.btn_back);
		tv_w_email = (TextView) findViewById(R.id.tv_w_email);
		tc_write_email = (TextView) findViewById(R.id.tc_write_email);
		cet_filter = (ClearEditText) findViewById(R.id.cet_filter);
		tv_search = (TextView) findViewById(R.id.tv_search);
		tv_title = (TextView) findViewById(R.id.tv_title);
		tv_read = (TextView) findViewById(R.id.tv_read); 
		tv_no_read = (TextView) findViewById(R.id.tv_no_read); 
		tv_del = (TextView) findViewById(R.id.tv_del);
		lv_show = (XListView) findViewById(R.id.lv_show);
		
		// 设置不让它下拉更新
		lv_show.setPullRefreshEnable(true);
		// 设置让它上拉更新
		lv_show.setPullLoadEnable(true);
	}
	
	/**
	 * 方法名: initListener() 
	 * 功 能 : 初始化事件
	 * 参 数 : void 
	 * 返回值: void
	 */
	private void initListener() {
		btn_back.setOnClickListener(this);
		tv_w_email.setOnClickListener(this);
		tc_write_email.setOnClickListener(this);
		tv_del.setOnClickListener(this);
		tv_read.setOnClickListener(this);
		tv_no_read.setOnClickListener(this);
		cet_filter.addTextChangedListener(editTextWatcher);
		tv_search.setOnClickListener(this);
		lv_show.setOnItemClickListener(itemListener);
		lv_show.setXListViewListener(ixListener);
	}
	/**
	 * 方法名: initAdapter() 
	 * 功 能 : 初始化适配器
	 * 参 数 : void 
	 * 返回值: void
	 */
	private void initAdapter(){
		if(null == esList){
			esList = new ArrayList<Email>();
		}
		eliAdapter = new EmailListInfoAdapter(this, esList);
		lv_show.setAdapter(eliAdapter);
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
		filter.addAction(BroadcastNotice.EMAIL_UPDATE_LIST);
		iReceiver = new IncomingReceiver();
		// 注册广播
		registerReceiver(iReceiver, filter);
	}
	
	/**
	 * 方法名: logic() 
	 * 功 能 : 业务处理
	 * 参 数 : void 
	 * 返回值: void
	 */
	@SuppressLint("NewApi")
	private void logic(){
		indexList = new ArrayList<Integer>();
		setDrawable(0);
		
		folderType = getIntent().getIntExtra("folderType", 1);
		folderCode = getIntent().getStringExtra("folderCode");
		String title = getIntent().getStringExtra("title");
		
		if(null != title && !"".equals(title.trim())){
			tv_title.setText(title);
		}else{
			tv_title.setText(getString(R.string.receive_box));
		}
		
		
		if (BaseApp.db == null) {
			BaseApp.db = new DBHelper(this);
			BaseApp.db.createTable(Email.class);
		}
		
		if(null == folderCode){
			esList = BaseApp.db.customQuery(Email.class, "userAccount", BaseApp.user.getUserId(), "mailFolderType", folderType+"");
		}else{
			esList = BaseApp.db.customQuery(Email.class, "userAccount", BaseApp.user.getUserId(), "mailFolderType", folderType+"", "folderCode", folderCode);
		}
		
		// 只有收邮件状态下才出现已读标签
		if(1 != folderType){
			tv_read.setVisibility(View.GONE);
			tv_no_read.setVisibility(View.GONE);
			if(null != esList){
				esList.clear();
			}
		}
		
		if(null == esList || esList.size() < 1 || BaseApp.updateEmail){
			String info = customOrReceiveEmail();
			remoteDataRequest(LocalConstants.EMAIL_RECEIVE_SERVER, info);
			BaseApp.updateEmail = false;
		}else{
			int dividend = esList.size() / pageSize;
			int remainder = esList.size() % pageSize;
			if(remainder > 0){
				pageIndex = ++dividend;
			}else{
				pageIndex = dividend;
			}
		}
		
		initAdapter();
	}
	
	/**
	 * 接收广播
	 */ 
	private class IncomingReceiver extends BroadcastReceiver{
		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();  
	        if (action.equals(BroadcastNotice.EMAIL_UPDATE_LIST)) {
	        	boolean check = intent.getBooleanExtra("isChecked", false);
	        	int index = intent.getIntExtra("index", 0);
	        	if(check){
	        		indexList.add(index);
	        	}else{
	        		for(int i=0;i<indexList.size();i++){
	        			if(index == indexList.get(i)){
	        				indexList.remove(i);
	        			}
	        		}
	        	}
	        	mHandler.sendEmptyMessage(3);
	        }  
		}
	}
	
	// TextView 内容改变事件
	TextWatcher editTextWatcher = new TextWatcher(){

		@Override
		public void onTextChanged(CharSequence s, int start, int before, int count) {
			filterData(s.toString());
		}
		
		@Override
		public void beforeTextChanged(CharSequence s, int start, int count, int after) {
			
		}
		
		@Override
		public void afterTextChanged(Editable s) {
		}
	};
	
	/**
	 * 根据输入框中的值来过滤数据并更新ListView
	 * 
	 * @param filterStr
	 */
	private void filterData(String filterStr) {
		if (TextUtils.isEmpty(filterStr)) {
			// 当输入框里面的值为空，更新为原来的列表，否则为过滤数据列表
			operClass = 0;
			isSearch = 0;
			indexList.clear();
			filterDateList.clear();
			eliAdapter.updateListView(esList);
//			initAdapter();
			lv_show.setPullRefreshEnable(true);
			lv_show.setPullLoadEnable(true);
			mHandler.sendEmptyMessage(3);
		} else {
			isSearch = 1;
			indexList.clear();
			filterDateList.clear();
			setDrawable(0);
			selectNone();
			for (Email sortModel : esList) {
				String name = sortModel.getMailSubject();
				String senderName = sortModel.getSenderName();
				if (name.indexOf(filterStr.toString()) != -1 || senderName.indexOf(filterStr.toString()) != -1) {
					filterDateList.add(sortModel);
				}
			}
			// 设置不让它下拉更新
			lv_show.setPullRefreshEnable(false);
			// 设置让它上拉更新
			lv_show.setPullLoadEnable(false);
			eliAdapter.updateListView(filterDateList);
		}
	}
	
	/**
	 * 方法名: customOrReceiveEmail() 
	 * 功 能 : 自定义或收邮件请求报文
	 * 参 数 : void 
	 * 返回值: void
	 */
	private String customOrReceiveEmail(){
		
		PublicBean bean = new PublicBean();
		bean.setDeviceId(BaseApp.macAddr);
		bean.setUserAccount(BaseApp.user.getUserId());
		bean.setMailFolderType(folderType + "");
		bean.setFolderCode(folderCode);
		bean.setPageIndex(pageIndex + "");
		bean.setPageSize(pageSize + "");
		
		return JSON.toJSONString(bean);
	}
	
	/**
	* @Title: getQuestData
	* @Description: 请求报文
	* @param void
	* @return String 返回的数据 
	* @throws
	*/
	private String getQuestData(String mailCode, String mailType){
		PublicBean bean = new PublicBean();
		bean.setDeviceId(BaseApp.macAddr);
		bean.setUserAccount(BaseApp.user.getUserId());
		bean.setMailCode(mailCode);
		bean.setMailType(mailType);
		return JSON.toJSONString(bean);
	}
	/**
	 * 方法名: delEmail() 
	 * 功 能 : 删除邮件
	 * 参 数 : void 
	 * 返回值: void
	 */
	private String delEmail(){
		
		PublicBean bean = new PublicBean();
		bean.setDeviceId(BaseApp.macAddr);
		bean.setUserAccount(BaseApp.user.getUserId());
		bean.setMailType(folderType + "");
		bean.setFolderCode(folderCode);
		List<String> mList = new ArrayList<String>();
		if(0 == isSearch){
			if(4 == folderType){
				for (int i = 0; i < indexList.size(); i++) {
					mList.add(esList.get(indexList.get(i)).getId());
				}
			}else{
				for (int i = 0; i < indexList.size(); i++) {
					mList.add(esList.get(indexList.get(i)).getMailCode());
				}
			}
		}else if(1 == isSearch){
			if(4 == folderType){
				for (int i = 0; i < indexList.size(); i++) {
					mList.add(filterDateList.get(indexList.get(i)).getId());
				}
			}else{
				for (int i = 0; i < indexList.size(); i++) {
					mList.add(filterDateList.get(indexList.get(i)).getMailCode());
				}
			}
		}
		
		bean.setMailCodes(repeat(mList));
		
		return JSON.toJSONString(bean);
	}
	/**
	 * 方法名: isReadEmail() 
	 * 功 能 : 已读邮件
	 * 参 数 : void 
	 * 返回值: void
	 */
	private String isReadEmail(){
		
		PublicBean bean = new PublicBean();
		bean.setDeviceId(BaseApp.macAddr);
		bean.setUserAccount(BaseApp.user.getUserId());
		bean.setMailType(folderType + "");
		bean.setFolderCode(folderCode);
		List<String> mList = new ArrayList<String>();
		if(0 == isSearch){
			for (int i = 0; i < indexList.size(); i++) {
				mList.add(esList.get(indexList.get(i)).getMailCode());
			}
		}else if(1 == isSearch){
			for (int i = 0; i < indexList.size(); i++) {
				mList.add(filterDateList.get(indexList.get(i)).getMailCode());
			}
		}
		bean.setMailCodes(repeat(mList));
		bean.setIsRead(isRead + "");
		 
		return JSON.toJSONString(bean);
	}
	
	/**
	 * 方法名: emailSearch() 
	 * 功 能 : 邮件搜索
	 * 参 数 : void 
	 * 返回值: void
	 */
	private String emailSearch(String text){
		
		PublicBean bean = new PublicBean();
		bean.setDeviceId(BaseApp.macAddr);
		bean.setReader(BaseApp.user.getUserId());
		bean.setSearchType(1+"");
		if(null != lastId){
			bean.setLastId(lastId);
		}
		bean.setPageNo(pageIndex+"");
		bean.setPageSize(pageSize+"");
		bean.setModule("2");
		bean.setTerm(text);
		
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
	
	// listView item事件
	OnItemClickListener itemListener = new OnItemClickListener() {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			positions = position - 1;
			if(3 == folderType){
				operClass = 1;
				String info = getQuestData(esList.get(positions).getMailCode(), esList.get(positions).getMailFolderType());
				remoteDataRequest(LocalConstants.EMAIL_EMAIL_INFO_SERVER, info);
			}else{
				mIntent = new Intent(MailSearchActivity.this, EmailMessageInfoActivity.class);
				if(operClass == 4 || 1 == isSearch){
					mIntent.putExtra("list", (Serializable)filterDateList);
				}else{
					mIntent.putExtra("list", (Serializable)esList);
				}
				mIntent.putExtra("position", positions);
				mIntent.putExtra("folderType", folderType);
				mIntent.putExtra("folderCode", folderCode);
				startActivityForResult(mIntent, INFO_CODE);
			}
		}
	};
	
	// XListView 加载更新事件
	IXListViewListener ixListener = new IXListViewListener(){
		@Override
		public void onRefresh() {
			loadType = 1;
			pageIndex = 1;
			operClass = 0;
			String info = customOrReceiveEmail();
			remoteDataRequest(LocalConstants.EMAIL_RECEIVE_SERVER, info);
		}
		
		@Override
		public void onLoadMore() {
			loadType = 0;
			operClass = 0;
			pageIndex++;
			String info = customOrReceiveEmail();
			remoteDataRequest(LocalConstants.EMAIL_RECEIVE_SERVER, info);
		}
	};
	/**
	 * @Title: stopLoad
	 * @Description: 停止刷新时加载的进度条
	 * @param void
	 * @return void 
	 * @throws
	 */
	private void stopLoad(){
		lv_show.stopRefresh();
		lv_show.stopLoadMore();
		lv_show.setRefreshTime(res.getString(R.string.just));
	}
	/**
	 * http请求回调
	 */
	RequestCallBack<String> submitCallBack = new RequestCallBack<String>() {

		@Override
		public void onStart() {
			if(2 == loadType){
				String tips = res.getString(R.string.global_prompt_message);
				if(2 == operClass){
					tips = res.getString(R.string.deleting);
				}
				DialogUtil.showWithCancelProgressDialog(MailSearchActivity.this, null, tips, null);
			}
		}

		@Override
		public void onLoading(long total, long current, boolean isUploading) {

		}
		
		@Override
		public void onSuccess(ResponseInfo<String> responseInfo) { 
			if(2 == loadType){
				DialogUtil.closeDialog();
			}else{
				stopLoad();
			}
			
			String result = responseInfo.result.toString(); 
			if (null != result && !"".equals(result.trim())) {
				ReqMessage msg = FastJSONUtil.getJSONToEntity(result, ReqMessage.class);
				// 判断返回标识状态是否为空
				if (null == msg || null == msg.getResCode() || "".equals(msg.getResCode())) {				
					LogUtil.promptInfo(MailSearchActivity.this, ErrorCodeContrast.getErrorCode("0", res));
					return;
				// 验证不合法
				}else if ("1103".equals(msg.getResCode()) || "1104".equals(msg.getResCode())) {
					LogUtil.promptInfo(MailSearchActivity.this, ErrorCodeContrast.getErrorCode(msg.getResCode(), res));
					Intent mIntent = new Intent(BroadcastNotice.USER_EXIT);
					sendBroadcast(mIntent);
					return;
				}else if("1210".equals(msg.getResCode())){
					LogUtil.promptInfo(MailSearchActivity.this, ErrorCodeContrast.getErrorCode(msg.getResCode(), res));	
					Intent mIntent = new Intent(BroadcastNotice.WIPE_USET);
					sendBroadcast(mIntent);
					return;
				}else if (!"1".equals(msg.getResCode())) {
					LogUtil.promptInfo(MailSearchActivity.this, ErrorCodeContrast.getErrorCode(msg.getResCode(), res));	
					return;
				}
				// 判断消息体是否为空
				if (null != msg.getMessage() && !"".equals(msg.getMessage().trim())) {		 
					// 获取解密后并校验后的值
					String decode = EncryptUtil.getDecodeData(msg.getMessage(), BaseApp.key);
					int tmp = 1;
					Message msgs = new Message();
					if(1 == operClass){
						tmp = 2;
					}else if(2 == operClass){
						tmp = 4;
					}else if(3 == operClass){
						tmp = 5;
					}else if(4 == operClass){
						tmp = 6;
					}
					
					msgs.what = tmp;
					msgs.obj = decode;
					mHandler.sendMessage(msgs);
				}
			} else {
				LogUtil.promptInfo(MailSearchActivity.this, ErrorCodeContrast.getErrorCode("0", res));
			}
		}

		@Override
		public void onFailure(HttpException error, String msg) {
			if(loadType == 2){
				DialogUtil.closeDialog();
			}else{
				stopLoad();
			}
			if(null != msg && msg.equals(LocalConstants.API_KEY)){
				LogUtil.promptInfo(MailSearchActivity.this, ErrorCodeContrast.getErrorCode("1207", res));
			}else{
				LogUtil.promptInfo(MailSearchActivity.this, ErrorCodeContrast.getErrorCode("0", res));
			}
		}
	};

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.btn_back:
				this.finish();
				overridePendingTransition(R.anim.push_right_in, R.anim.abc_fade_out);
				break;
			case R.id.tc_write_email:		// 写邮件
				startActivity(WriteEmailActivity.class);
				break;
			case R.id.tv_search:			// 搜索
				String text = cet_filter.getText().toString();
				if(null == text || "".equals(text.trim())){
					break;
				}
				operClass = 4;
				String info = emailSearch(text);
				remoteDataRequest(LocalConstants.EMAIL_SEARCH_SERVER, info);
				break;
			case R.id.tv_w_email:
				indexList.clear();
				if (flag == 0) {
					selectAll();
					for(int i=0; i<esList.size(); i++){
						indexList.add(i);
					}
					flag = 1;
				} else {
					selectNone();
					flag = 0;
				}
				setDrawable(flag);
				mHandler.sendEmptyMessage(3);
				break;
			case R.id.tv_del:
				
				if(null == indexList || indexList.size() < 1){
					break;
				}
				
				AlertDialog mAlertDialog = new AlertDialog.Builder(mContext)
				.setTitle(res.getString(R.string.delete_mails))
				.setMessage(res.getString(R.string.is_delete_mails))
				.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						operClass = 2;
						String delInfo = delEmail();
						remoteDataRequest(LocalConstants.EMAIL_DEL_SERVER, delInfo);
					}
				}).setNegativeButton(android.R.string.cancel, null).show();
				
				break;
			case R.id.tv_read:
				if(null == indexList || indexList.size() < 1){
					break;
				}
				isRead = 1;
				operClass = 3;
				String isReads = isReadEmail();
				remoteDataRequest(LocalConstants.EMAIL_ISREAD_SERVER, isReads);
				
				break;
			case R.id.tv_no_read:
				if(null == indexList || indexList.size() < 1){
					break;
				}
				isRead = 0;
				operClass = 3;
				String isReadInfo = isReadEmail();
				remoteDataRequest(LocalConstants.EMAIL_ISREAD_SERVER, isReadInfo);
				break;
		}
	}
	
	/** 去重 **/
	private List<String> repeat(List<String> strList){
		List<String> listTemp = new ArrayList<String>();  
		Iterator<String> it = strList.iterator();  
		while(it.hasNext()){  
			  String str = it.next();  
			  if(listTemp.contains(str)){  
			   it.remove();  
			  }  
			  else{  
			   listTemp.add(str);  
			  } 
		}
		
		return strList;
	}
	/** 全选 */
	private void selectAll() {
		for (int i = 0; i < esList.size(); i++) {
			EmailListInfoAdapter.getIsSelected().put(i, true);
		}
		eliAdapter.notifyDataSetChanged();
	}
	
	/** 反选 */
	private void selectNone() {
		for (int i = 0; i < esList.size(); i++) {
			EmailListInfoAdapter.getIsSelected().put(i, false);
		}
		eliAdapter.notifyDataSetChanged();
	}
	
	private void setDrawable(int val){
		// 设置tv_w_email背景图片
		Drawable drawable = getResources().getDrawable(R.drawable.all_choose_p);
		if(val == 0){
			drawable = getResources().getDrawable(R.drawable.all_choose_n);
		}
		// setBackground()方法，在16版本以上才有
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
			tv_w_email.setBackground(drawable);
		} else {
			tv_w_email.setBackgroundDrawable(drawable);
		}
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(RESULT_CODE != resultCode){
			return;
		}
		
		/* 编辑 */
		if(EDIT_CODE == requestCode){
			loadType = 2;
			pageIndex = 1;
			String info = customOrReceiveEmail();
			remoteDataRequest(LocalConstants.EMAIL_RECEIVE_SERVER, info);
		}
		/* 详情 */
		if(INFO_CODE == requestCode){
			if(null == data){
				return;
			}
			
			List<Email> list = (List<Email>) data.getSerializableExtra("list");
			int back_position = data.getIntExtra("position",0);
			int deleteFlag = data.getIntExtra("deleteFlag",0);
			if(null != list){
				for(int i=0; i<list.size(); i++){
					BaseApp.db.delByColumnName(Email.class, "mailCode", list.get(i).getMailCode());
					Email email = list.get(i);
					BaseApp.db.save(email); 
				}
				if(operClass == 4 || 1 == isSearch){
					esList.removeAll(filterDateList);
					filterDateList.get(back_position).setIsRead("1");
					if(deleteFlag==1){
						esList.clear();
						filterDateList.clear();
					}
				}else{
					esList.clear();
				}
				esList.addAll(list); 
			}else{
				BaseApp.db.delByColumnName(Email.class, "userAccount", BaseApp.user.getUserId());
				esList.clear();
			}
			eliAdapter.notifyDataSetChanged();
			setDrawable(0);
			selectNone();
		}
		
		/* 草稿 */
		if(DRAFTS_CODE == requestCode){
			if(null == data){
				return;
			}
			List<Email> list = (List<Email>) data.getSerializableExtra("list");
			int back_position = data.getIntExtra("position",0);
			if(null != list){
				for(int i=0; i<list.size(); i++){
//					BaseApp.db.delByColumnName(Email.class, "mailCode", list.get(i).getMailCode());
					Email email = list.get(i);
//					BaseApp.db.save(email); 
				}
				if(operClass == 4 || 1 == isSearch){
					esList.removeAll(filterDateList);
				}else{
					esList.clear();
				}
				list.remove(back_position);
				esList.addAll(list); 
			}else{
//				BaseApp.db.delByColumnName(Email.class, "userAccount", BaseApp.user.getUserId());
				esList.clear();
			}
			eliAdapter.notifyDataSetChanged();
			setDrawable(0);
			selectNone();
			folderType = 3;
			
			
//			int position = data.getIntExtra("position", -1);
//			if(-1 != position){
//				esList.remove(position);
//				eliAdapter.notifyDataSetChanged();
//			}
		}
		
		super.onActivityResult(requestCode, resultCode, data);		
	}
	
	@Override
	protected void onResume() {
		if(tempVal){
			logic();
			tempVal = false;
		}else{
			if(3 == folderType){
				loadType = 1;
				pageIndex = 1;
				operClass = 0;
				logic();
			}
		}
		super.onResume();
	}
	
	@Override
	protected void onPause() {
		super.onPause();
	}
   
	@Override
	protected void onDestroy() {
		if(null != iReceiver){
			unregisterReceiver(iReceiver);
		}
		super.onDestroy();
	}
}
 