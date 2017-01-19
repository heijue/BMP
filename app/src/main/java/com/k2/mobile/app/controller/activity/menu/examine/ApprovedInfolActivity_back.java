package com.k2.mobile.app.controller.activity.menu.examine;

import java.util.List;

import com.alibaba.fastjson.JSON;
import com.k2.mobile.app.R;
import com.k2.mobile.app.common.config.BroadcastNotice;
import com.k2.mobile.app.common.config.LocalConstants;
import com.k2.mobile.app.common.exception.HttpException;
import com.k2.mobile.app.controller.core.BaseActivity;
import com.k2.mobile.app.controller.core.BaseApp;
import com.k2.mobile.app.model.bean.FlowBean;
import com.k2.mobile.app.model.bean.PublicBean;
import com.k2.mobile.app.model.bean.ReqMessage;
import com.k2.mobile.app.model.bean.ResPublicBean;
import com.k2.mobile.app.model.http.ResponseInfo;
import com.k2.mobile.app.model.http.callback.RequestCallBack;
import com.k2.mobile.app.model.http.other.SendRequest;
import com.k2.mobile.app.utils.DialogUtil;
import com.k2.mobile.app.utils.EncryptUtil;
import com.k2.mobile.app.utils.ErrorCodeContrast;
import com.k2.mobile.app.utils.FastJSONUtil;
import com.k2.mobile.app.utils.LogUtil;
import com.k2.mobile.app.utils.NetWorkUtil;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Display;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

/**
 * 
 * @ClassName: ApprovedInfolActivity
 * @Description: 审批/申请详情
 * @author linqijun
 * @date 2015-4-27 20:12:46
 *
 */
@SuppressLint({ "NewApi", "ResourceAsColor" })
public class ApprovedInfolActivity_back extends BaseActivity implements OnClickListener {
	
	private Button btn_back;
	private TextView tv_title;
	private TextView tv_b_page;	
	private TextView tv_n_page;
	private TextView tv_tip_title;
	private TextView tv_tip_content;
	private LinearLayout ll_error;
	private WebView wv_show = null;
	private ProgressBar pb_progres;
	
	private String formInstanceCode = null;
	private int type = 1;
	// 审批
	private int operType = 0;
	private String status =null;
	private String procVerID = null;
	private String procSetID = null;
	private String workListItemCode = null;
	private LinearLayout ll_commit, ll_oper;
	private Button btn_agree;
	private Button btn_disagree;
	private int flag = 0;
	private int count = 1;
	private String examination;
	private int tmpCount = 0;
	
	private List<FlowBean> fList = null;
	private String url;
	private int server = LocalConstants.NEW_INFO_SERVER ;
	// 当前显示详情下标
	private int positions = -1;
	private ProgersssDialog dialog = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 去除头部
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_approved_info);
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
	@SuppressLint("NewApi")
	private void initView() {
		btn_back = (Button) findViewById(R.id.btn_back);
		tv_title = (TextView) findViewById(R.id.tv_title);
		tv_b_page = (TextView) findViewById(R.id.tv_b_page);	
		tv_n_page = (TextView) findViewById(R.id.tv_n_page);
		tv_tip_title = (TextView) findViewById(R.id.tv_tip_title);
		tv_tip_content = (TextView) findViewById(R.id.tv_tip_content);
		ll_error = (LinearLayout) findViewById(R.id.ll_error);
		pb_progres = (ProgressBar) findViewById(R.id.pb_progres);
		
		wv_show = (WebView) findViewById(R.id.wv_show);
		ll_commit = (LinearLayout) findViewById(R.id.ll_commit);
		ll_oper = (LinearLayout) findViewById(R.id.ll_oper);
		
        btn_agree = (Button) findViewById(R.id.btn_agree);
		btn_disagree = (Button) findViewById(R.id.btn_disagree);
		
		// 获取传递过来的参数
		fList = (List<FlowBean>)getIntent().getSerializableExtra("list");
		type = getIntent().getIntExtra("type", 1);
		operType = getIntent().getIntExtra("operType", 0);
		status = getIntent().getStringExtra("status");
		positions = getIntent().getIntExtra("positions", -1);
				
		if(null != status && null != status && "4".equals(status) && 1 == operType){
			btn_disagree.setEnabled(false);
		}
		
		// 如果网络没连接
		if (!NetWorkUtil.isNetworkAvailable(this)) {    
			 wv_show.setVisibility(View.GONE);   
	         ll_commit.setVisibility(View.GONE);
	         ll_error.setVisibility(View.VISIBLE);  
	         return;
		}
		
		switch (operType) {
			case 1:
				ll_commit.setVisibility(View.VISIBLE);
				btn_agree.setText(getString(R.string.submittal));
				btn_disagree.setText(getString(R.string.global_delete));
				break;
			case 4:
				ll_commit.setVisibility(View.VISIBLE);
				btn_agree.setText(getString(R.string.agree));
				btn_disagree.setText(getString(R.string.disagree));
				break;
		}
		
		// 启用支持javascript
		WebSettings settings = wv_show.getSettings();
		settings.setJavaScriptEnabled(true);
		settings.setJavaScriptCanOpenWindowsAutomatically(true);
		settings.setBuiltInZoomControls(true);	// 支持页面放大缩小
		settings.setDisplayZoomControls(false);	// 隐藏放大缩小的按钮
		settings.setSupportZoom(true);			// 可以缩放
		settings.setUseWideViewPort(true);		// 为图片添加放大缩小功能
		
		wv_show.setInitialScale(150);
		// 不支持缓存
		wv_show.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
		wv_show.setWebViewClient(new WebViewClient(){
			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
			        wv_show.loadUrl(url);
			        return true;
			}
			@Override
			public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
			        super.onReceivedError(view, errorCode, description, failingUrl);
			        wv_show.setVisibility(View.GONE);   
			        ll_commit.setVisibility(View.GONE);
			        ll_error.setVisibility(View.VISIBLE);   
			} 
						      
			@Override
			public void onPageFinished(WebView view, String url) {
			       super.onPageFinished(view, url);
			}
		});
		
		if(null != fList){
			if(1 == fList.size()){
				tv_n_page.setEnabled(false);
				tv_b_page.setEnabled(false);
			}
		}
		
		setPageButtonBackground();
		loadHtml();
	}
	
	/**
	 * 方法名: initListener() 
	 * 功 能 : 初始化 监听器 
	 * 参 数 : void 
	 * 返回值: void
	 */
	private void initListener() {
		btn_back.setOnClickListener(this);
		btn_agree.setOnClickListener(this);
		btn_disagree.setOnClickListener(this);
		tv_b_page.setOnClickListener(this); 
		tv_n_page.setOnClickListener(this);
		wv_show.setWebChromeClient(new WebChromeClient() {
          @Override
          public void onProgressChanged(WebView view, int newProgress) {
              if (newProgress == 100) {
            	  pb_progres.setVisibility(View.INVISIBLE);
              } else {
                  if (View.INVISIBLE == pb_progres.getVisibility()) {
                	  pb_progres.setVisibility(View.VISIBLE);
                  }
                  pb_progres.setProgress(newProgress);
              }
              super.onProgressChanged(view, newProgress);
          }
	    });
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.btn_back:
	            finish();
				break;
			case R.id.btn_agree:
				switch (operType) {
	    			case 1:
	    				count = 1;
	    				String info = sendApprovedData(procSetID, formInstanceCode, 1);
	    				requestServer(LocalConstants.TRIAL_START_FLOW_SERVER, info);
	    				break;
	    			case 4:
	    				count = 2;
	    				String infos = getQuestData(workListItemCode);
						requestServer(LocalConstants.MY_PROCESS_AGREE_SERVER, infos);
	    				break;
				}
        	
				break;
			case R.id.btn_disagree:
				switch (operType) {
    			case 1:
    				count = 3;
					AlertDialog mAlertDialog = new AlertDialog.Builder(ApprovedInfolActivity_back.this)
					.setTitle(res.getString(R.string.delete_flows))
					.setMessage(res.getString(R.string.is_delete_flow))
					.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							String info = sendApprovedData(procSetID, formInstanceCode, 0);
							requestServer(LocalConstants.DELETE_START_FLOW_SERVER, info);
							dialog.dismiss();
						}
					}).setNegativeButton(android.R.string.cancel, null).show();
    				break;
    			case 4:
    				count = 4;
    				flag = 1;
    				dialog = new ProgersssDialog(ApprovedInfolActivity_back.this);
					WindowManager windowManager = ApprovedInfolActivity_back.this.getWindowManager();
		        	Display display = windowManager.getDefaultDisplay();
		        	WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
		        	lp.width = (int)(display.getWidth()*0.9); //设置宽度
		        	lp.height = (int)(display.getHeight()*0.45); //设置宽度
		        	dialog.getWindow().setAttributes(lp);
		        	
					dialog.show();
    				break;
				}
				break;
			case R.id.tv_b_page: // 上一页
				queryPage(1);
				break;
			case R.id.tv_n_page: // 下一页
				queryPage(2);
				break;
		}		
	}
	
	private void setPageButtonBackground(){
		
		if(null == fList){
			return;
		}
		
		if(positions == 0){
			tv_b_page.setBackgroundResource(R.drawable.upage_d_disable);
		}else{
			tv_b_page.setBackgroundResource(R.drawable.upage_n);
		}
		
		if(positions == (fList.size()-1)){
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
		
		wv_show.setVisibility(View.VISIBLE);
		ll_oper.setVisibility(View.GONE);
		
		if(null != fList && 1 == fList.size() && 1 > tmpCount){
			tmpCount++;
			positions = 0;
			setPageButtonBackground();
		}else{
			if(1 == val){
				positions--;
				if(positions < 0){
					positions = 0;
					return;
				}
				setPageButtonBackground();
			}else if(2 == val){
				int size = fList.size() - 1;
				positions++;
				setPageButtonBackground();
				if(size < positions){
					positions = size;
					setPageButtonBackground();
					return;
				}
			}
		}
		
		loadHtml();
		switch (operType) {
			case 1:
				btn_agree.setTextColor(0xff00925f);
				btn_disagree.setTextColor(0xff353636);
				btn_agree.setEnabled(true);
				btn_disagree.setEnabled(true);
				break;
			case 4:
				btn_agree.setTextColor(0xff00925f);
				btn_disagree.setTextColor(0xff353636);
				btn_agree.setEnabled(true);
				btn_disagree.setEnabled(true);
				break;
		}
	}
	
	private void loadHtml(){
		if(null != fList && positions<fList.size() && -1 != positions){
			FlowBean fBean = fList.get(positions);
//			formInstanceCode = fBean.getFormInstanceCode();
//			procVerID = fBean.getProcVerID();
//			procSetID = fBean.getProcSetID();
//			workListItemCode = fBean.getWorkListItemCode();
//			examination = fBean.getFolio();
//			tv_title.setText(fBean.getFolio());
//			url = "http://"+UrlStrController.formatUrlStr(HttpConstants.WEBSERVICE_URL, server)+
//					"?formInstanceCode="+formInstanceCode+"&type="+type+"&procVerID="+procVerID+"&procSetID="+procSetID;
//			
//			wv_show.loadUrl(url);
		}
	}
	
   /**
	* @Title: getQuestData
	* @Description: 请求报文
	* @param void
	* @return String 返回的数据 
	* @throws
	*/
	private String getQuestData(String workListItemCode){

		PublicBean bean = new PublicBean();
		bean.setDeviceId(BaseApp.macAddr);
		bean.setUserAccount(BaseApp.user.getUserId());
		bean.setWorkListItemCode(workListItemCode);
		
		return JSON.toJSONString(bean);
	}
	/**
	* @Title: delQuestData
	* @Description: 请求报文
	* @param void
	* @return String 返回的数据 
	* @throws
	*/
	private String delQuestData(String opinion){

		PublicBean bean = new PublicBean();
		bean.setDeviceId(BaseApp.macAddr);
		bean.setUserAccount(BaseApp.user.getUserId());
		bean.setWorkListItemCode(workListItemCode);
		bean.setApproveComment(opinion);
		
		return JSON.toJSONString(bean);
	}
   /**
	* @Title: getQuestData
	* @Description: 请求报文
	* @param void
	* @return String 返回的数据 
	* @throws
	*/
	private String sendApprovedData(String procSetID, String formInstanceCode, int type){

		PublicBean bean = new PublicBean();
		bean.setDeviceId(BaseApp.macAddr);
		bean.setUserAccount(BaseApp.user.getUserId());
		bean.setFormInstanceCode(formInstanceCode);
		if(1 == type){
			bean.setProcSetID(procSetID);
		}
		
		return JSON.toJSONString(bean);
	}
	
	/**
	* @Title: requestServer
	* @Description: 发送请求报文
	* @param void
	* @return void
	* @throws
	*/
	private void requestServer(int server, String info){
		// 判断网络是否连接
		if (!NetWorkUtil.isNetworkAvailable(ApprovedInfolActivity_back.this)) {
			DialogUtil.showLongToast(ApprovedInfolActivity_back.this, R.string.global_network_disconnected);
		}else{			
			SendRequest.sendSubmitRequest(ApprovedInfolActivity_back.this, info, BaseApp.token, BaseApp.reqLang, 
					server, BaseApp.key, submitCallBack);
	}
  }
	
	/**
	 * http请求回调
	 */
	RequestCallBack<String> submitCallBack = new RequestCallBack<String>() {

		@Override
		public void onStart() {
			DialogUtil.showWithCancelProgressDialog(ApprovedInfolActivity_back.this, null, res.getString(R.string.global_prompt_message), null);
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
					LogUtil.promptInfo(ApprovedInfolActivity_back.this, ErrorCodeContrast.getErrorCode("0", res));
					return;
				// 验证不合法
				} else if ("1103".equals(msg.getResCode()) || "1104".equals(msg.getResCode())) {
					LogUtil.promptInfo(ApprovedInfolActivity_back.this, ErrorCodeContrast.getErrorCode(msg.getResCode(), res));	
					Intent mIntent = new Intent(BroadcastNotice.USER_EXIT);
					sendBroadcast(mIntent);
					return;
				}else if("1210".equals(msg.getResCode())){
					LogUtil.promptInfo(ApprovedInfolActivity_back.this, ErrorCodeContrast.getErrorCode(msg.getResCode(), res));	
					Intent mIntent = new Intent(BroadcastNotice.WIPE_USET);
					ApprovedInfolActivity_back.this.sendBroadcast(mIntent);
					return;
				}else if (!"1".equals(msg.getResCode())) {
					LogUtil.promptInfo(ApprovedInfolActivity_back.this, ErrorCodeContrast.getErrorCode(msg.getResCode(), res));	
					return;
				}
				// 判断消息体是否为空
				if (null == msg.getMessage() || "".equals(msg.getMessage().trim())) {		 
					LogUtil.promptInfo(ApprovedInfolActivity_back.this, ErrorCodeContrast.getErrorCode("0", res));
				} else {
					// 获取解密后并校验后的值
					String decode = EncryptUtil.getDecodeData(msg.getMessage(), BaseApp.key);
					if (null != decode) {
						ResPublicBean bean = JSON.parseObject(decode, ResPublicBean.class);
						if(null != bean && null != bean.getSuccess() && "1".equals(bean.getSuccess().trim())) {
							String action = "";
							String tips = null;
							if(1 == count){
								action = BroadcastNotice.I_STARTED_REFRESH;
//								LogUtil.promptInfo(ApprovedInfolActivity.this, res.getString(R.string.submittal_succeed));
								Intent mIntent = new Intent(action);
								ApprovedInfolActivity_back.this.sendBroadcast(mIntent);
								tips = res.getString(R.string.submittal_succeed);
							}else if (2 == count) {
//								LogUtil.promptInfo(ApprovedInfolActivity.this, res.getString(R.string.approval_succeed));
								action = BroadcastNotice.NOT_APPROVAL;
								Intent mIntent = new Intent(action);
								ApprovedInfolActivity_back.this.sendBroadcast(mIntent);
								tips = res.getString(R.string.approval_succeed);
							}else if(3 == count){
								action = BroadcastNotice.I_STARTED_REFRESH;
//								LogUtil.promptInfo(ApprovedInfolActivity.this, res.getString(R.string.delete_success));
								Intent mIntent = new Intent(action);
								ApprovedInfolActivity_back.this.sendBroadcast(mIntent);
								tips = res.getString(R.string.delete_success);
							}else if(4 == count){
								action = BroadcastNotice.NOT_APPROVAL;
//								LogUtil.promptInfo(ApprovedInfolActivity.this, res.getString(R.string.approval_succeed));
								Intent mIntent = new Intent(action);
								ApprovedInfolActivity_back.this.sendBroadcast(mIntent);
								tips = res.getString(R.string.approval_succeed);
							}
							
							wv_show.setVisibility(View.GONE);
							ll_oper.setVisibility(View.VISIBLE);
							
							final FlowBean fBean = fList.get(positions);
							tv_tip_title.setText(getString(R.string.file_tips)+fBean.getFolio());
							tv_tip_content.setText(tips);
							
							fList.remove(positions);
							btn_agree.setTextColor(0xff9a9a9a);
							btn_disagree.setTextColor(0xff9a9a9a);
							btn_agree.setEnabled(false);
							btn_disagree.setEnabled(false);
//							if(0 == fList.size()){
//								finish();
//							}
						}else{
							if(1 == count){
								LogUtil.promptInfo(ApprovedInfolActivity_back.this, res.getString(R.string.submittal_failure));
							}else if (3 == count) {
								LogUtil.promptInfo(ApprovedInfolActivity_back.this, res.getString(R.string.delete_failed));
							}else {
								LogUtil.promptInfo(ApprovedInfolActivity_back.this, res.getString(R.string.approval_failure));
							}
						}
					}
				}
			} else {
				LogUtil.promptInfo(ApprovedInfolActivity_back.this, ErrorCodeContrast.getErrorCode("0", res));
			}
		}

		@Override
		public void onFailure(HttpException error, String msg) {
			DialogUtil.closeDialog();
			if(null != msg && msg.equals(LocalConstants.API_KEY)){
				LogUtil.promptInfo(ApprovedInfolActivity_back.this, ErrorCodeContrast.getErrorCode("1207", res));
			}else{
				LogUtil.promptInfo(ApprovedInfolActivity_back.this, ErrorCodeContrast.getErrorCode("0", res));
			}
		}
	};
	
	public class ProgersssDialog extends Dialog implements android.view.View.OnClickListener{

		private TextView tv_examination;
		private EditText ed_opinion;
		private TextView tv_commit;
		private TextView tv_close;
		private View dView = null;
		private Context context = null;
		      
		public ProgersssDialog(Context context) {
			 super(context, R.style.actibity_dialog);
			 
			 this.context = context;
			 initDialog();
			 		     
		     tv_examination.setText(examination);
		     
		     tv_commit.setOnClickListener(this);
		     tv_close.setOnClickListener(this);
			 //dialog添加视图
			 setContentView(dView);
       }

		private void initDialog(){
			 // 加载布局文件
			 LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			 dView = inflater.inflate(R.layout.dialog_not_approval, null); 
			 tv_examination = (TextView) dView.findViewById(R.id.tv_examination);
			 ed_opinion = (EditText) dView.findViewById(R.id.ed_opinion);
			 tv_commit = (TextView) dView.findViewById(R.id.tv_commit);
			 tv_close = (TextView) dView.findViewById(R.id.tv_close);
		}
		
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
				case R.id.tv_close:
					dismiss();
					break;
				case R.id.tv_commit:
					String opinion = ed_opinion.getText().toString();
					if(null == opinion || "".equals(opinion.trim())){
						DialogUtil.showLongToast(mContext, R.string.disagree_reason_tips);
						break;
					}
					String info = delQuestData(opinion);
					requestServer(LocalConstants.MY_PROCESS_DELETE_APPROVAL_SERVER, info);
					dismiss();
					break;
			}
		}
	}
	
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if(wv_show.canGoBack()) {
            	// 返回上一页面
            	wv_show.goBack();
                return true;
            }
        }
        
        return super.onKeyDown(keyCode, event);
    }
}