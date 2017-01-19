package com.k2.mobile.app.controller.activity.menu.email;

import java.io.Serializable;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document; 

import com.alibaba.fastjson.JSON;
import com.k2.mobile.app.R;
import com.k2.mobile.app.common.config.BroadcastNotice;
import com.k2.mobile.app.common.config.LocalConstants;
import com.k2.mobile.app.common.exception.HttpException;
import com.k2.mobile.app.controller.core.BaseApp;
import com.k2.mobile.app.model.bean.EmailBean;
import com.k2.mobile.app.model.bean.PublicBean;
import com.k2.mobile.app.model.bean.ReqMessage;
import com.k2.mobile.app.model.bean.ResPublicBean;
import com.k2.mobile.app.model.db.table.Email;
import com.k2.mobile.app.model.http.ResponseInfo;
import com.k2.mobile.app.model.http.callback.RequestCallBack;
import com.k2.mobile.app.model.http.other.SendRequest;
import com.k2.mobile.app.utils.DateUtil;
import com.k2.mobile.app.utils.DialogUtil;
import com.k2.mobile.app.utils.EncryptUtil;
import com.k2.mobile.app.utils.ErrorCodeContrast;
import com.k2.mobile.app.utils.FastJSONUtil;
import com.k2.mobile.app.utils.LogUtil;
import com.k2.mobile.app.utils.NetWorkUtil;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.webkit.WebSettings;
import android.webkit.WebSettings.LayoutAlgorithm;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.TextView;

/**
* @Title AlreadySendEmailActivity.java
* @Package com.oppo.mo.controller.activity.menu.workbench;
* @Description 邮件——>邮件详情
* @Company  K2
* 
* @author linqijun
* @date 2015-4-22 上午10:40:03
* @version V1.0
*/
public class EmailMessageInfoActivity extends FragmentActivity implements OnClickListener {
	
	// 返回
	private Button btn_back;
	// 头部标题
	private TextView tv_title;
	private TextView tv_b_page, tv_n_page;
	private TextView tv_content_title, tv_send_name, tv_receive_name, tv_time, 
					  tv_send, tv_reply, tv_delete, tv_add_task, tv_cc, tv_cc_title;
	private WebView wv_content;
	// 结果集
	private List<Email> eList = null; 
	// 邮件详情类型 1草稿 2发件箱 2收件箱
	private int mailType = 1;
	// 当前显示详情下标
	private int position = 0;
	// 文件类型
	private int folderType = 1;
	// 文件编码
	private String folderCode = null;
	// 操作类型 1：查询 2：删除
	private int operClass = 1;
	
	// 跳转
	private Intent mIntent = null;
	private EmailBean bean = null;
	private boolean isRead=true;
	private int deleteFlag = 0;
	
	private Handler mHandler = new Handler(){
		@Override
		public void dispatchMessage(android.os.Message msg) {
			switch (msg.what) {
				case 1:
					String json = (String) msg.obj;
					if(null == json || "".equals(json.trim())){
						break;
					}
					bean = JSON.parseObject(json, EmailBean.class);
					if(null != bean){
						tv_content_title.setText(bean.getMailSubject());
						tv_send_name.setText(bean.getSenderName());
						tv_receive_name.setText(bean.getRecipient());
						String time = "";
//						if(2 == folderType || 4 == folderType){
//							if (null != bean.getMailSendTime() && !"".equals(bean.getMailSendTime())) {
//								try {
//									time = DateUtil.datetimeFormat(bean.getMailSendTime().replaceAll("T", " "));
//								} catch (ParseException e) {
//									e.printStackTrace();
//								}
//							}
//						}else{
//							if (null != bean.getReceiptTime() && !"".equals(bean.getReceiptTime())) {
//								try {
//									time = DateUtil.datetimeFormat(bean.getReceiptTime().replaceAll("T", " "));
//								} catch (ParseException e) {
//									e.printStackTrace();
//								}
//							}
//						}
						String tmp = bean.getMailSendTime() == null ? bean.getReceiptTime() : bean.getMailSendTime();
						if (null != tmp && !"".equals(tmp)) {
							try {
								time = DateUtil.datetimeFormat(tmp.replaceAll("T", " "));
							} catch (ParseException e) {
								e.printStackTrace();
							}
						}
						
						tv_time.setText(time);
						if(null != bean.getCc() && !"".equals(bean.getCc().trim())){
							tv_cc_title.setVisibility(View.VISIBLE);
							tv_cc.setVisibility(View.VISIBLE);
							tv_cc.setText(bean.getCc());
						}
						
						if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
							// android4.4自动适应图片
							StringBuilder sb = new StringBuilder();
							sb.append("<meta charset=\"utf-8\">");
							sb.append("<meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">");
							sb.append("<meta name=\"viewport\" content=\" initial-scale=1.0, maximum-scale=1.0, user-scalable=no\">");
							sb.append("<meta name=\"apple-mobile-web-app-capable\" content=\"yes\">");
							sb.append("<meta name=\"apple-mobile-web-app-status-bar-style\" content=\"black\">");
							sb.append("<meta name=\"format-detection\" content=\"telephone=no\">");
							sb.append("<style type=\"text/css\">");
							sb.append("p {");
							sb.append("word-wrap:break-word;");
							sb.append("}");
							sb.append("</style>");
							sb.append("<script type=\"text/javascript\" src=\"http://mo.myoas.com:10086/static/js/zepto.min.js?v=\"></script>");
							sb.append("<script type=\"text/javascript\" src=\"http://mo.myoas.com:10086/static/js/common.js?v=\"></script>");
							sb.append("<script type=\"text/javascript\" src=\"http://mo.myoas.com:10086/static/assets/js/jquery-2.0.3.min.js?v=\"></script>");
							sb.append("<script type=\"text/javascript\" src=\"http://mo.myoas.com:10086/static/assets/js/bootstrap.js?v=\"></script>");
							sb.append("<script type=\"text/javascript\">");
							sb.append("$(function () {");
							sb.append("$(\"img\").each(function(){");
							sb.append("var img = $(this);");
							sb.append("var imgWidth, imgHeight;");
							sb.append("var theImage = new Image();");
							sb.append("$(theImage).attr(\"src\",$(this).attr(\"src\")).load(function() {");
							sb.append("imgWidth = this.width;");
							sb.append("imgHeight = this.height;");
							sb.append("var width = imgWidth;");
							sb.append("var screenWidth = $(window).width();");
							sb.append("if(width > screenWidth){");
							sb.append("var size = imgHeight/imgWidth;");
							sb.append("var newWidth = screenWidth;");
							sb.append("var newHeight = size*newWidth;");
							sb.append("$(img).css(\"width\",newWidth);");
							sb.append("$(img).css(\"height\",newHeight);");
							sb.append("}");
							sb.append("jQuery.event.remove(this);");
							sb.append("jQuery.removeData(this);");
							sb.append("});");
							sb.append("})");
							sb.append("});");
							sb.append("</script>"
							);
							
							Document doc_Dis = Jsoup.parse(sb.toString() + bean.getMailContent());
							String newHtmlContent = doc_Dis.toString();
							wv_content.loadDataWithBaseURL(null, newHtmlContent, "text/html", "utf-8", null);
						}else{
							wv_content.loadDataWithBaseURL(null, bean.getMailContent(), "text/html", "utf-8", null);
						}
						// 发送广播通知消息-1
						if(!isRead){
							mIntent = new Intent(BroadcastNotice.EMAIL_COUNT_SUB);
							mIntent.putExtra("subCount", 1);
							sendBroadcast(mIntent);
							isRead = true;
						}
					}
					break;
				case 2:
					String delRes = (String) msg.obj;
					if(null != delRes && !"".equals(delRes)){
						ResPublicBean rBean = JSON.parseObject(delRes, ResPublicBean.class);
						if (null != rBean && "1".equals(rBean.getSuccess())) {
							LogUtil.promptInfo(EmailMessageInfoActivity.this, getResources().getString(R.string.delete_success));
							eList.remove(position);
							if(eList.size() < 1){
								deleteFlag = 1;
								exitActivity();
								break;
							}
							
							if(position == 0){
								queryPage(2);
							}else{
								queryPage(1);
							}
							setPageButtonBackground();
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
		setContentView(R.layout.activity_email_info);
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
		btn_back = (Button) findViewById(R.id.btn_back);		
		tv_b_page = (TextView) findViewById(R.id.tv_b_page);	
		tv_n_page = (TextView) findViewById(R.id.tv_n_page);	
		tv_content_title = (TextView) findViewById(R.id.tv_content_title);
		wv_content = (WebView) findViewById(R.id.wv_content);
		tv_send_name = (TextView) findViewById(R.id.tv_send_name);
		tv_receive_name = (TextView) findViewById(R.id.tv_receive_name);
		tv_cc = (TextView) findViewById(R.id.tv_cc);
		tv_cc_title = (TextView) findViewById(R.id.tv_cc_title);
		tv_time = (TextView) findViewById(R.id.tv_time);
		tv_send = (TextView) findViewById(R.id.tv_send);
		tv_reply = (TextView) findViewById(R.id.tv_reply);
		tv_delete = (TextView) findViewById(R.id.tv_delete);
		tv_add_task = (TextView) findViewById(R.id.tv_add_task);
		
		// 设置标提语		
		tv_title.setText(getString(R.string.email_info));
		wv_content.setBackgroundColor(0);
		tv_add_task.setVisibility(View.GONE);
		tv_cc_title.setText(getString(R.string.the_addressee_cc) + ":");
		
		WebSettings settings = wv_content.getSettings(); 			// webView: 类WebView的实例 
		settings.setJavaScriptEnabled(true);
		settings.setNeedInitialFocus(false);  
		settings.setSupportZoom(true);
		settings.setLoadWithOverviewMode(true);						// 适应屏幕
		settings.setLayoutAlgorithm(LayoutAlgorithm.SINGLE_COLUMN); 
		settings.setLoadsImagesAutomatically(true);					// 自动加载图片
		settings.setCacheMode(WebSettings.LOAD_DEFAULT | WebSettings.LOAD_CACHE_ELSE_NETWORK);
		
		wv_content.setWebViewClient(new WebViewClient(){
			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
//				view.loadUrl(url);
				return true;
			}
		});
		
		eList = (List<Email>) getIntent().getSerializableExtra("list");
		position = getIntent().getIntExtra("position", 0);
		setPageButtonBackground();
		folderType = getIntent().getIntExtra("folderType", 1);
		folderCode = getIntent().getStringExtra("folderCode");
		
		if(2 == folderType){
			tv_reply.setVisibility(View.GONE);
		}else if(4 == folderType){
			tv_reply.setVisibility(View.GONE);
			tv_send.setVisibility(View.GONE);
			tv_delete.setText(getString(R.string.completely_delete));
		}
			
		if(null != eList){
			mailType = Integer.parseInt(eList.get(position).getMailFolderType());
			if("0".equals(eList.get(position).getIsRead())){
				isRead=false;
			}
			eList.get(position).setIsRead("1");
			String info = getQuestData();
			sendQuest(LocalConstants.EMAIL_EMAIL_INFO_SERVER, info);
		}
	}
	
	/**
	 * 方法名: sendQuest() 
	 * 功 能 : 远程数据请求
	 * 参 数 : void 
	 * 返回值: void
	 */
	private void sendQuest(int server, String info) {
		// 检查网络
        if (!NetWorkUtil.isNetworkAvailable(this)) {
			DialogUtil.showLongToast(EmailMessageInfoActivity.this, R.string.global_network_disconnected);
		} else {
			SendRequest.sendSubmitRequest(EmailMessageInfoActivity.this, info, BaseApp.token, BaseApp.reqLang, 
					server, BaseApp.key, submitCallBack);
		}
	}
	
	/**
	 * 方法名: initListener() 
	 * 功 能 : 初始化 监听器 
	 * 参 数 : void 
	 * 返回值: void  
	 */
	private void initListener() {
		btn_back.setOnClickListener(this);
		tv_send.setOnClickListener(this);
		tv_b_page.setOnClickListener(this); 
		tv_n_page.setOnClickListener(this);
		tv_delete.setOnClickListener(this);
		tv_reply.setOnClickListener(this);
		tv_add_task.setOnClickListener(this);
	}
	
   /**
	* @Title: getQuestData
	* @Description: 请求报文
	* @param void
	* @return String 返回的数据 
	* @throws
	*/
	private String getQuestData(){
		PublicBean bean = new PublicBean();
		bean.setDeviceId(BaseApp.macAddr);
		bean.setUserAccount(BaseApp.user.getUserId());
		if(null != eList && null != eList.get(position).getMailCode()){
			bean.setMailCode(eList.get(position).getMailCode());
		}
		
		bean.setMailType(mailType+"");
		if(null != eList && null != eList.get(position).getMailFrom()){
			bean.setMailFrom(eList.get(position).getMailFrom());
		}
		
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
		if(4 == folderType){
			mList.add(eList.get(position).getId());
		}else{
			mList.add(eList.get(position).getMailCode());
		}
		bean.setMailCodes(mList);
		
		return JSON.toJSONString(bean);
	}
	
	void setPageButtonBackground(){
		
		if(position==0){
			tv_b_page.setBackgroundResource(R.drawable.upage_d_disable);
		}else{
			tv_b_page.setBackgroundResource(R.drawable.upage_n);
		}
		
		if(position==eList.size()-1){
			tv_n_page.setBackgroundResource(R.drawable.npage_d_disable);
		}else{
			tv_n_page.setBackgroundResource(R.drawable.npage_n);
		}
	}
	
	/**
	 * 方法名: queryPage() 
	 * 功 能 : 查询分页
	 * 参 数 : val, 1:上一页 2:下一页
	 * 返回值: void
	 */
	private void queryPage(int val){
		operClass = 1;
		if(1 == val){
			position--;
			if(position < 0){
				position = 0;
				return;
			}
			
			setPageButtonBackground();
		}else if(2 == val){
			int size = eList.size() - 1;
			position++;
			setPageButtonBackground();
			if(size < position){
				position = size;
				setPageButtonBackground();
				return;
			}
		}
		
		String infos = getQuestData();
		sendQuest(LocalConstants.EMAIL_EMAIL_INFO_SERVER, infos);
	}
	/**
	 * 方法名: exitActivity() 
	 * 功 能 : 退出当前Activity
	 * 参 数 : void
	 * 返回值: void
	 */
	private void exitActivity(){
		mIntent = new Intent(EmailMessageInfoActivity.this, MailSearchActivity.class);
		mIntent.putExtra("list", (Serializable)eList);
		mIntent.putExtra("position", position);
		mIntent.putExtra("deleteFlag", deleteFlag);
		setResult(MailSearchActivity.RESULT_CODE, mIntent);
		finish();
//		overridePendingTransition(R.anim.push_left_in, R.anim.abc_fade_out);
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.btn_back:
				exitActivity();
				break;
			case R.id.tv_send:	  // 转发
				if(null == bean){
					break;
				}
				mIntent = new Intent(EmailMessageInfoActivity.this, WriteEmailActivity.class);
				mIntent.putExtra("bean", bean);
				mIntent.putExtra("val", 2);
				startActivity(mIntent);
				break;
			case R.id.tv_reply:  // 回复
				if(null == bean){
					break;
				}
				mIntent = new Intent(EmailMessageInfoActivity.this, WriteEmailActivity.class);
				mIntent.putExtra("bean", bean);
				mIntent.putExtra("val", 1);
				startActivity(mIntent);
				break;
			case R.id.tv_b_page: // 上一页
				queryPage(1);
				break;
			case R.id.tv_n_page: // 下一页
				queryPage(2);
				break;
			case R.id.tv_delete:
				operClass = 2;
				String delInfo = delEmail();
				sendQuest(LocalConstants.EMAIL_DEL_SERVER, delInfo);
				break;
			case R.id.tv_add_task:	// 将邮件转变为任务
				if(null == bean){
					break;
				}
				mIntent = new Intent(EmailMessageInfoActivity.this, EmailConvertTaskActivity.class);
				mIntent.putExtra("bean", bean);
				startActivity(mIntent);
				break;
		}
	}
	
	/**
	 * http请求回调
	 */
	RequestCallBack<String> submitCallBack = new RequestCallBack<String>() {

		@Override
		public void onStart() {
			String tips = getString(R.string.global_prompt_message);
			if(2 == operClass){
				tips = getString(R.string.deleting);
			}
			DialogUtil.showWithCancelProgressDialog(EmailMessageInfoActivity.this, null, tips, null);
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
					LogUtil.promptInfo(EmailMessageInfoActivity.this, ErrorCodeContrast.getErrorCode("0", getResources()));
					return;
				// 验证不合法
				}else if ("1103".equals(msg.getResCode()) || "1104".equals(msg.getResCode())) {
					LogUtil.promptInfo(EmailMessageInfoActivity.this, ErrorCodeContrast.getErrorCode(msg.getResCode(), getResources()));
					Intent mIntent = new Intent(BroadcastNotice.USER_EXIT);
					sendBroadcast(mIntent);
					return;
				}else if("1210".equals(msg.getResCode())){
					LogUtil.promptInfo(EmailMessageInfoActivity.this, ErrorCodeContrast.getErrorCode(msg.getResCode(), getResources()));	
					Intent mIntent = new Intent(BroadcastNotice.WIPE_USET);
					sendBroadcast(mIntent);
					return;
				}else if (!"1".equals(msg.getResCode())) {
					LogUtil.promptInfo(EmailMessageInfoActivity.this, ErrorCodeContrast.getErrorCode(msg.getResCode(), getResources()));	
					return;
				}
				// 判断消息体是否为空
				if (null == msg.getMessage() || "".equals(msg.getMessage().trim())) {		 
					LogUtil.promptInfo(EmailMessageInfoActivity.this, ErrorCodeContrast.getErrorCode("0", getResources()));
				} else {
					eList.get(position).setIsRead("1");
					// 获取解密后并校验后的值
					String decode = EncryptUtil.getDecodeData(msg.getMessage(), BaseApp.key);
					if(null != decode && !"".equals(decode)){
						Message msgs = new Message();
						msgs.what = operClass;
						msgs.obj = decode;
						mHandler.sendMessage(msgs);
					}
				}
			} else {
				LogUtil.promptInfo(EmailMessageInfoActivity.this, ErrorCodeContrast.getErrorCode("0", getResources()));
			}
		}

		@Override
		public void onFailure(HttpException error, String msg) {
			DialogUtil.closeDialog();
			if(null != msg && msg.equals(LocalConstants.API_KEY)){
				LogUtil.promptInfo(EmailMessageInfoActivity.this, ErrorCodeContrast.getErrorCode("1207", getResources()));
			}else{
				LogUtil.promptInfo(EmailMessageInfoActivity.this, ErrorCodeContrast.getErrorCode("0", getResources()));
			}
		}
	};	
	
	
	@Override 
    public boolean onKeyDown(int keyCode, KeyEvent event) { 
        if ((keyCode == KeyEvent.KEYCODE_BACK)) { 
        	exitActivity();
        }
        
        return super.onKeyDown(keyCode, event);  
    } 
}