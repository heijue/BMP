package com.k2.mobile.app.controller.activity.menu.email;    

import java.util.ArrayList;
import java.util.List;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
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
import com.k2.mobile.app.model.adapter.EmailEditListAdapter;
import com.k2.mobile.app.model.adapter.EmailEditListAdapter.ViewHolder;
import com.k2.mobile.app.model.bean.PublicBean;
import com.k2.mobile.app.model.bean.ReqMessage;
import com.k2.mobile.app.model.bean.ResPublicBean;
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
import com.k2.mobile.app.view.widget.XListView;
  
/**
* @Title MailSearchActivity.java
* @Package com.oppo.mo.controller.activity.menu.workbench;
* @Description 邮件列表——>编辑
* @Company  K2
* 
* @author linqijun
* @date 2015-5-11 下午11:53:43
* @version V1.0
*/
public class MailListEditActivity extends BaseActivity implements OnClickListener {
	
	private TextView  tv_del, tv_is_read;
	private TextView tv_all_choose;
	private Button btn_back;
	private TextView tv_finish;
	// 标题
	private TextView tv_title;
	// 跳转到写Email
	private TextView tv_w_email;
	// 显示列表
	private XListView lv_show;
	// 分页索引
	private int pageIndex = 1;
	// 分页大小
	private int pageSize = 10;
	// 操作类型 1是默认查询, 2删除, 3标为已读
	private int operType = 1;
	// 文件类型 
	private int folderType = 1;
	// code
	private String folderCode = null;
	private static int flag = 0;
	private boolean checkFlag = false;
	// 数据集
	private List<Email> esList = null;
	// 适配器
	private EmailEditListAdapter eliAdapter = null;
	
	private Intent mIntent = null;
	
	private Handler mHandler = new Handler(){
		@Override
		public void dispatchMessage(Message msg) {
			switch (msg.what) {
				case 2:
					String jsons = (String)msg.obj;
					if(null != jsons ){
						ResPublicBean bean = JSON.parseObject(jsons, ResPublicBean.class);
						if (null != bean && "1".equals(bean.getSuccess())) {
							 LogUtil.promptInfo(MailListEditActivity.this, res.getString(R.string.delete_success));
							 List<Email> tmpList = new ArrayList<Email>();
							for (int i = 0; i < esList.size(); i++) {
								if (!EmailEditListAdapter.getIsSelected().get(i)) {
									tmpList.add(esList.get(i));
								}
							}
							esList.clear();
							esList.addAll(tmpList);
							selectNone();
							checkFlag = true;
						}
					}					
					break;
				case 3:
					String jsonRead = (String)msg.obj;
					if(null != jsonRead ){
						ResPublicBean bean = JSON.parseObject(jsonRead, ResPublicBean.class);
						if (null != bean && "1".equals(bean.getSuccess())) {
							checkFlag = true;
							selectNone();
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
		setContentView(R.layout.activity_mail_list_edit);
		initView();
		initListener();
		logic();
		BaseApp.addActivity(this);
	}
	
	/**
	 * 方法名: initView() 
	 * 功 能 : 初始化 
	 * 参 数 : void 
	 * 返回值: void
	 */
	private void initView() {
		tv_all_choose = (TextView) findViewById(R.id.tv_all_choose);
		tv_del = (TextView) findViewById(R.id.tv_del);
		tv_finish = (TextView) findViewById(R.id.tv_finish);
		btn_back = (Button) findViewById(R.id.btn_back);
		tv_w_email = (TextView) findViewById(R.id.tv_w_email);
		tv_title = (TextView) findViewById(R.id.tv_title);
		tv_is_read = (TextView) findViewById(R.id.tv_read);
		lv_show = (XListView) findViewById(R.id.lv_show);
		
		// 设置不让它下拉更新
		lv_show.setPullRefreshEnable(false);
		// 设置让它上拉更新
		lv_show.setPullLoadEnable(false);
	}
	
	/**
	 * 方法名: initListener() 
	 * 功 能 : 初始化事件
	 * 参 数 : void 
	 * 返回值: void
	 */
	private void initListener() {
		lv_show.setOnItemClickListener(itemListener);
		tv_all_choose.setOnClickListener(this);
		tv_del.setOnClickListener(this);
		tv_is_read.setOnClickListener(this);
		tv_finish.setOnClickListener(this);
	}
	/**
	 * 方法名: initAdapter() 
	 * 功 能 : 初始化适配器
	 * 参 数 : void 
	 * 返回值: void
	 */
	private void initAdapter(){
		eliAdapter = new EmailEditListAdapter(this, esList);
		lv_show.setAdapter(eliAdapter);
	}
	/**
	 * 方法名: logic() 
	 * 功 能 : 业务处理
	 * 参 数 : void 
	 * 返回值: void
	 */
	private void logic(){
		btn_back.setVisibility(View.GONE);
		tv_w_email.setVisibility(View.GONE);
		tv_title.setText(getString(R.string.receive_box));
		
		esList = (List<Email>) getIntent().getSerializableExtra("list") ;
		folderType = getIntent().getIntExtra("folderType", 0);
		folderCode = getIntent().getStringExtra("folderCode");
				
		initAdapter();
	}
	// listView item事件
	OnItemClickListener itemListener = new OnItemClickListener() {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			ViewHolder vHolder = (ViewHolder)view.getTag();
            //在每次获取点击的item时将对应的checkBox状态改变，同时修改map的值
            vHolder.ck_choose.toggle();
			EmailEditListAdapter.getIsSelected().put(position, vHolder.ck_choose.isChecked());
		}
	};
	
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
		for (int i = 0; i < esList.size(); i++) {
			if (EmailEditListAdapter.getIsSelected().get(i)) {
				mList.add(esList.get(i).getMailCode());
			}
		}
		bean.setIsRead("1");
		bean.setMailCodes(mList);		
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
		for (int i = 0; i < esList.size(); i++) {
			if (EmailEditListAdapter.getIsSelected().get(i)) {
				mList.add(esList.get(i).getMailCode());
			}
		}
		
		bean.setMailCodes(mList);
		
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
			DialogUtil.showWithCancelProgressDialog(MailListEditActivity.this, null, res.getString(R.string.global_prompt_message), null);
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
					LogUtil.promptInfo(MailListEditActivity.this, ErrorCodeContrast.getErrorCode("0", res));
					return;
				// 验证不合法
				}else if ("1103".equals(msg.getResCode()) || "1104".equals(msg.getResCode())) {
					LogUtil.promptInfo(MailListEditActivity.this, ErrorCodeContrast.getErrorCode(msg.getResCode(), res));
					Intent mIntent = new Intent(BroadcastNotice.USER_EXIT);
					sendBroadcast(mIntent);
					return;
				}else if("1210".equals(msg.getResCode())){
					LogUtil.promptInfo(MailListEditActivity.this, ErrorCodeContrast.getErrorCode(msg.getResCode(), res));	
					Intent mIntent = new Intent(BroadcastNotice.WIPE_USET);
					sendBroadcast(mIntent);
					return;
				}else if (!"1".equals(msg.getResCode())) {
					LogUtil.promptInfo(MailListEditActivity.this, ErrorCodeContrast.getErrorCode(msg.getResCode(), res));	
					return;
				}
				// 判断消息体是否为空
				if (null != msg.getMessage() && !"".equals(msg.getMessage().trim())) {		 
					// 获取解密后并校验后的值
					String decode = EncryptUtil.getDecodeData(msg.getMessage(), BaseApp.key);
					Message msgs = new Message();
					msgs.what = operType;
					msgs.obj = decode;
					mHandler.sendMessage(msgs);
				}
			} else {
				LogUtil.promptInfo(MailListEditActivity.this, ErrorCodeContrast.getErrorCode("0", res));
			}
		}

		@Override
		public void onFailure(HttpException error, String msg) {
			DialogUtil.closeDialog();
			if(null != msg && msg.equals(LocalConstants.API_KEY)){
				LogUtil.promptInfo(MailListEditActivity.this, ErrorCodeContrast.getErrorCode("1207", res));
			}else{
				LogUtil.promptInfo(MailListEditActivity.this, ErrorCodeContrast.getErrorCode("0", res));
			}
		}
	};

	@Override
	public void onClick(View v) {
		boolean bl = false;
		switch (v.getId()) {
			case R.id.tv_w_email:		// 写邮件
				startActivity(WriteEmailActivity.class);
				break;
			case R.id.tv_finish:
				Intent mIntent = new Intent(MailListEditActivity.this, MailSearchActivity.class);
				if(checkFlag){
					setResult(MailSearchActivity.RESULT_CODE, mIntent); 
				}else{
					setResult(RESULT_CANCELED, mIntent); 
				}
				finish();
				break;
			case R.id.tv_all_choose:
				if (flag == 0) {
					selectAll();
					tv_all_choose.setText(res.getString(R.string.global_cancel));
					flag = 1;
				} else {
					selectNone();
					tv_all_choose.setText(res.getString(R.string.select_all));
					flag = 0;
				}
				break;
			case R.id.tv_del:
				for (int i = 0; i < esList.size(); i++) {
					if (EmailEditListAdapter.getIsSelected().get(i)) {
						bl = true;
					}
				}
				
				if(!bl){
					break;
				}
				
				AlertDialog mAlertDialog = new AlertDialog.Builder(mContext)
				.setTitle(res.getString(R.string.delete_mails))
				.setMessage(res.getString(R.string.is_delete_mails))
				.setPositiveButton(android.R.string.ok,  new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						operType = 2;
						String delInfo = delEmail();
						remoteDataRequest(LocalConstants.EMAIL_DEL_SERVER, delInfo);
					}
				}).setNegativeButton(android.R.string.cancel, null).show();
				
				break;
			case R.id.tv_read:
				for (int i = 0; i < esList.size(); i++) {
					if (EmailEditListAdapter.getIsSelected().get(i)) {
						bl = true;
					}
				}
				
				if(bl){
					operType = 3;
					String isReadInfo = isReadEmail();
					remoteDataRequest(LocalConstants.EMAIL_ISREAD_SERVER, isReadInfo);
				}
				
				break;
		}
	}
	
	/** 全选 */
	private void selectAll() {
		for (int i = 0; i < esList.size(); i++) {
			EmailEditListAdapter.getIsSelected().put(i, true);
		}
		eliAdapter.notifyDataSetChanged();
	}
	
	/** 反选 */
	private void selectNone() {
		for (int i = 0; i < esList.size(); i++) {
			EmailEditListAdapter.getIsSelected().put(i, false);
		}
		eliAdapter.notifyDataSetChanged();
	}

	@Override 
    public boolean onKeyDown(int keyCode, KeyEvent event) { 
        if ((keyCode == KeyEvent.KEYCODE_BACK)) { 
        	Intent mIntent = new Intent(MailListEditActivity.this, MailSearchActivity.class);
			if(checkFlag){
				setResult(MailSearchActivity.RESULT_CODE, mIntent); 
			}else{
				setResult(RESULT_CANCELED, mIntent); 
			}
        }
        
        return super.onKeyDown(keyCode, event);  
    } 
}
 