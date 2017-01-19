package com.k2.mobile.app.controller.activity.menu.email;

import java.io.Serializable;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.StrictMode;
import android.text.Editable;
import android.text.Html;
import android.text.Html.ImageGetter;
import android.text.method.LinkMovementMethod;
import android.text.method.ScrollingMovementMethod;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.View.OnKeyListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.k2.mobile.app.R;
import com.k2.mobile.app.common.config.BroadcastNotice;
import com.k2.mobile.app.common.config.LocalConstants;
import com.k2.mobile.app.common.exception.HttpException;
import com.k2.mobile.app.controller.core.BaseActivity;
import com.k2.mobile.app.controller.core.BaseApp;
import com.k2.mobile.app.model.adapter.ShowContactAdapter;
import com.k2.mobile.app.model.bean.Contacts;
import com.k2.mobile.app.model.bean.EmailAddrBean;
import com.k2.mobile.app.model.bean.EmailBean;
import com.k2.mobile.app.model.bean.PublicBean;
import com.k2.mobile.app.model.bean.ReqMessage;
import com.k2.mobile.app.model.bean.ResPublicBean;
import com.k2.mobile.app.model.bean.ViewEmailBean;
import com.k2.mobile.app.model.db.DBHelper;
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
import com.k2.mobile.app.view.widget.AutolinefeedView;
import com.k2.mobile.app.view.widget.PopupWindowWriteEmail;
/**
 * 
 * @ClassName: WriteEmailActivity
 * @Description: 写邮件
 * @author linqijun
 * @date 2015-3-12 下午8:56:46
 *
 */
public class WriteEmailActivity extends BaseActivity implements OnClickListener {
	
	// 收件人， 主题， 正文内容
	private EditText et_theme, et_content;
	private AutoCompleteTextView actv_show;
	private AutoCompleteTextView actv_show_cc;
	private AutoCompleteTextView actv_show_bcc;
	private Button btn_back;
	private TextView tv_title;
	private TextView tv_cc_and_bcc;
	private TextView tv_check;
	private RelativeLayout rl_cc_and_bcc,rl_cc,rl_bcc;
	// 发送
	private TextView tv_sech;
	private Button btn_execute;
	private Button btn_execute_cc;
	private Button btn_execute_bcc;
	private TextView tv_content;
	
	private LinearLayout ll_addr;
	private LinearLayout ll_parent_addr;
	private LinearLayout ll_addr_cc;
	private LinearLayout ll_parent_addr_cc;
	private LinearLayout ll_addr_bcc;
	private LinearLayout ll_parent_addr_bcc;
	// 邮件地址
	private AutolinefeedView afv_addr;
	// 抄送地址
	private AutolinefeedView afv_addr_cc;
	// 密送地址
	private AutolinefeedView afv_addr_bcc;
	
	private CheckBox cb_important;
	// 底部弹出窗口
	private PopupWindow mPopupWindow;
	// 页面跳转
	private Intent mIntent = null;
	// 收件人列表
	private List<EmailBean> rList = null;
	private List<EmailBean> rListCC = null;
	private List<EmailBean> rListBCC = null;
	// 操作类型 1草稿箱，2是普通写邮件, 3 是获取通信录
	private int operType = 2;
	// 选中序列号
	private int position = -1;
	// 邮件编号
	private String mailCode = null;
	// 联系人列表
	private List<Contacts> contactsList = null;
	// 联系人适配器
	private ShowContactAdapter sAdapter = null;
	private ShowContactAdapter sAdapterCC = null;
	private ShowContactAdapter sAdapterBCC = null;
	private View view = null;
	private LayoutInflater inflater = null;
	private List<ViewEmailBean> vLsit = null;
	private List<ViewEmailBean> vLsitCC = null;
	private List<ViewEmailBean> vLsitBCC = null;
	private ViewEmailBean veBean = null;
	private ViewEmailBean veBeanCC = null;
	private ViewEmailBean veBeanBCC = null;
	private int viewId = 18400;
	private View tmpView = null;
	
	private int afv_addr_height;
	private int ll_addr_height;
	private int ll_parent_addr_height;
	
	public final String REPLY_MAIL_TYPE = "4";
	
	private ArrayList<EmailAddrBean> aEmailAddr = new ArrayList<EmailAddrBean>();
	private ArrayList<EmailAddrBean> aEmailCCAddr = new ArrayList<EmailAddrBean>();
	private ArrayList<EmailAddrBean> aEmailBCCAddr = new ArrayList<EmailAddrBean>();
	// 发送HTML
	private Editable.Factory mEditableFactory = Editable.Factory.getInstance();
	
	private EmailBean replyBean = null;
	// 结果集
	private List<Email> eList = null; 
	
	private Handler mHandler = new Handler(){
		@Override
		public void dispatchMessage(android.os.Message msg) {
			switch (msg.what) {
				case 1:
					String str = (String) msg.obj;
					// 如果返回数据为空退出
					if(null == str || "".equals(str.trim())){
						break;
					}
					if(null != contactsList){
						contactsList.clear(); // 清除cList
					}
					// 解析JSON数据到list
					contactsList.addAll(JSON.parseArray(str, Contacts.class));
	
					if ((contactsList != null) && (contactsList.size() > 0)){
						// 插入数据前先清除之前的数据
						BaseApp.db.delByColumnName(Contacts.class, "userAccount", BaseApp.user.getUserId());
						for (int i = 0; i < contactsList.size(); i++) {
							Contacts bean = new Contacts();
							bean.setUserAccount(BaseApp.user.getUserId());
							bean.setUpdateTime(DateUtil.getDownloadDateStr());
							bean.setUserCode((contactsList.get(i)).getUserCode());
							bean.setAlphabetical(contactsList.get(i).getAlphabetical());
							bean.setRealityOrgName(contactsList.get(i).getRealityOrgName());
							bean.setUserEnName(contactsList.get(i).getUserEnName());
							bean.setUserChsName(contactsList.get(i).getUserChsName());
							bean.setType(contactsList.get(i).getType());
							// 保存数据
							BaseApp.db.save(bean);
						}
					}
						
					break;
			}
		};
	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		struct();
		// 去除头部
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_email_write);
		initView();
		initListener();
		initDB();
		logic();
		BaseApp.addActivity(this);
	}

	@SuppressLint("NewApi")
	public static void struct() {  
        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()  
                .detectDiskReads().detectDiskWrites().detectNetwork() 
                .penaltyLog().build());  
        StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()  
                .detectLeakedSqlLiteObjects() // 探测SQLite数据库操作  
                .penaltyLog() // 打印logcat  
                .penaltyDeath().build());  
    }  
	
	/**
	 * 方法名: initView() 
	 * 功 能 : 初始化 
	 * 参 数 : void 
	 * 返回值: void
	 */
	private void initView() {
		btn_back = (Button) findViewById(R.id.btn_back);
		tv_title = (TextView) findViewById(R.id.tv_title);
		tv_sech = (TextView) findViewById(R.id.tv_sech);
		ll_addr = (LinearLayout) findViewById(R.id.ll_addr);
		ll_parent_addr = (LinearLayout) findViewById(R.id.ll_parent_addr);
		ll_addr_cc = (LinearLayout) findViewById(R.id.ll_addr_cc);
		ll_parent_addr_cc = (LinearLayout) findViewById(R.id.ll_parent_addr_cc);
		ll_addr_bcc = (LinearLayout) findViewById(R.id.ll_addr_bcc);
		ll_parent_addr_bcc = (LinearLayout) findViewById(R.id.ll_parent_addr_bcc);
		btn_execute = (Button) findViewById(R.id.btn_execute);
		btn_execute_cc = (Button) findViewById(R.id.btn_execute_cc);
		btn_execute_bcc = (Button) findViewById(R.id.btn_execute_bcc);
		afv_addr = (AutolinefeedView) findViewById(R.id.afv_addr);
		afv_addr_cc = (AutolinefeedView) findViewById(R.id.afv_addr_cc);
		afv_addr_bcc = (AutolinefeedView) findViewById(R.id.afv_addr_bcc);
		cb_important = (CheckBox)findViewById(R.id.cb_important);
		et_theme = (EditText) findViewById(R.id.et_theme);
		et_content = (EditText) findViewById(R.id.et_content);
		rl_cc_and_bcc = (RelativeLayout)findViewById(R.id.rl_cc_and_bcc);
		rl_cc = (RelativeLayout)findViewById(R.id.rl_cc);
		rl_bcc = (RelativeLayout)findViewById(R.id.rl_bcc);
		tv_cc_and_bcc = (TextView)findViewById(R.id.tv_cc_and_bcc);
		tv_check = (TextView)findViewById(R.id.tv_check);
		
		et_content.setMovementMethod(ScrollingMovementMethod.getInstance());// 设置可滚动  
		et_content.setMovementMethod(LinkMovementMethod.getInstance());		// 设置超链接可以打开网页  
	}
	
	
	/**
	 * 方法名: initListener() 
	 * 功 能 : 初始化 监听器 
	 * 参 数 : void 
	 * 返回值: void
	 */
	private void initListener() {
		btn_back.setOnClickListener(this);
		tv_sech.setOnClickListener(this);
		ll_addr.setOnClickListener(this);
		ll_addr_cc.setOnClickListener(this);
		ll_addr_bcc.setOnClickListener(this);
		btn_execute.setOnClickListener(this);
		btn_execute_cc.setOnClickListener(this);
		btn_execute_bcc.setOnClickListener(this);
		et_theme.setOnFocusChangeListener(onFocusChangeListener);
		et_theme.setFocusable(true);
		et_theme.setFocusableInTouchMode(true);
		rl_cc_and_bcc.setOnClickListener(this);
		et_content.setOnClickListener(this);
		tv_check.setOnClickListener(this);
	}
	
	/**
	 * 方法名: logic() 
	 * 功 能 : 业务处理 
	 * 参 数 : void 
	 * 返回值: void
	 */
	@SuppressLint("NewApi")
	private void logic() {
		
		tv_title.setText(getString(R.string.write_email));
		tv_sech.setText(getString(R.string.send));
		tv_sech.setTextSize(14);
		
		Resources resources = WriteEmailActivity.this.getResources(); 
		Drawable btnDrawable = resources.getDrawable(R.drawable.background_button_frame); 
		// setBackground()方法，在16版本以上才有
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
			tv_sech.setBackground(btnDrawable);
		} else {
			tv_sech.setBackgroundDrawable(btnDrawable);
		}
		
		// 计算LOGO图标的大小
		WindowManager wm = (WindowManager)getBaseContext().getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        int width = (int)(display.getWidth() * 0.13);
        int height = (int)(display.getHeight() * 0.05);
        
        LayoutParams lp = tv_sech.getLayoutParams(); 
        lp.width = width;
        lp.height = height;
        tv_sech.setLayoutParams(lp);
		
        rList = new ArrayList<EmailBean>();
        rListCC = new ArrayList<EmailBean>();
        rListBCC = new ArrayList<EmailBean>();
        vLsit = new ArrayList<ViewEmailBean>();
		vLsitCC = new ArrayList<ViewEmailBean>();
		vLsitBCC = new ArrayList<ViewEmailBean>();
		
		inflater = getLayoutInflater();
		afv_addr.addView(initEditView());
		afv_addr_cc.addView(initEditCCView());
		afv_addr_bcc.addView(initEditBCCView());
		afv_addr.measure(0, 0);
		
		ll_addr.measure(0, 0);
		ll_parent_addr.measure(0, 0);
		
		afv_addr_height = afv_addr.getMeasuredHeight();
		ll_addr_height = ll_addr.getMeasuredHeight();
		ll_parent_addr_height = ll_parent_addr.getMeasuredHeight();
								
		int val = getIntent().getIntExtra("val", 0);
		EmailBean bean = (EmailBean) getIntent().getSerializableExtra("bean") ;
		position = getIntent().getIntExtra("position", -1);
		if(null != bean && -1 != position){
			mailCode = bean.getMailCode();
			eList = (List<Email>) getIntent().getSerializableExtra("list");
			repeatSendEmail(bean);
		}else if(null != bean && 0 != val){
			actionReFw(val, bean);
		}else{
			setDefaultContent();
		}
	}
	
	private void setDefaultContent(){
		
		DisplayMetrics dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		
		int widthPixels = dm.widthPixels; 	// 宽度
		int heightPixels = dm.heightPixels; // 高度

		int widths = View.MeasureSpec.makeMeasureSpec(0,View.MeasureSpec.UNSPECIFIED);
		int heights = View.MeasureSpec.makeMeasureSpec(0,View.MeasureSpec.UNSPECIFIED);

		et_content.measure(widths, heights);
		int height = et_content.getMeasuredHeight(); 
		int width = et_content.getMeasuredWidth();
		
		float hDividend = 0;
		float wDividend = 0;
		
		if(720 > widthPixels){
			hDividend = 26;
			wDividend = 1.2f;
		}else{
			hDividend = 60;
			wDividend = 3.2f;
		}
		
		int checkCount = (int)(height/hDividend);
		int checkWidthCount = (int)(width/wDividend);
		
		StringBuffer sb = new StringBuffer();
		for(int i=0;i<checkCount; i++){
			sb.append("<br/>");
		}
		
		for(int i=0; i<checkWidthCount; i++){
			sb.append("&nbsp;");
		}
		
		sb.append(getString(R.string.reply_from_android));
		et_content.setText(Html.fromHtml(sb.toString(), imgGetter, null)); 
	}
	
	/**
	 * 方法名: repeatSendEmail() 
	 * 功 能 : 草稿重发
	 * 参 数 : bean 邮件信息
	 * 返回值: void
	 */
	private void repeatSendEmail(EmailBean bean){
		if(null != bean.getRecipientJson()){
			List<EmailBean> list = JSON.parseArray(bean.getRecipientJson(), EmailBean.class);
			if(null != list){
				for(int i=0; i<list.size(); i++){
					EmailBean nBean = list.get(i);
					veBean = new ViewEmailBean();
					veBean.setBean(nBean);
					View v = initShowView(nBean);
					if(null != v){
						veBean.setV(v);
						addContent(veBean);
					}
				}
			}
		}
		
		et_theme.setText(bean.getMailSubject());
		if(null != bean.getMailContent()){
			et_content.setText(Html.fromHtml(bean.getMailContent(), imgGetter, null)); 
		}
		
		if(null != bean.getIsImportant() && "true".equals(bean.getIsImportant())){
			cb_important.setChecked(true);
		}
	}
	/**
	 * 方法名: actionReFw() 
	 * 功 能 : 邮件转发、回复
	 * 参 数 : val 1表示回复，2转发
	 * 参 数 : bean 邮件信息
	 * 返回值: void
	 */
	private void actionReFw(int val, EmailBean bean){
		System.out.println("val = "+val);
		if(1 == val){
			EmailBean tmpBean = new EmailBean();
			tmpBean.setCode(bean.getSenderCode());
			tmpBean.setDisplayName(bean.getSenderName());
			tmpBean.setType(bean.getType());
			veBean = new ViewEmailBean();
			veBean.setBean(tmpBean);
			View v = initShowView(tmpBean);
			if(null != v){
				veBean.setV(v);
				veBean.getBean().setType("4");
				addContent(veBean);
			}
			
			if(null != bean.getCc() && !"".equals(bean.getCc())){
				String[] ccName = bean.getCc().split(",");
				String[] ccCode = bean.getCcCode().split(";");
				
				rl_cc_and_bcc.setVisibility(View.GONE);
				rl_cc.setVisibility(View.VISIBLE);
				rl_bcc.setVisibility(View.VISIBLE);
				
				for(int i=0; i <ccName.length; i++){
					EmailBean tmpCCBean = new EmailBean();
					int len = ccCode.length;
					if(null != ccCode && i < len){
						tmpCCBean.setCode(ccCode[i]);
					}
					tmpCCBean.setDisplayName(ccName[i]);
					tmpCCBean.setType(bean.getType());
					
					veBean = new ViewEmailBean();
					veBean.setBean(tmpCCBean);
					View ccView = initShowCCView(tmpCCBean);
					if(null != ccView){
						veBean.setV(ccView);
						addCCContent(veBean);
					}
				}
			}
			
			et_theme.setText("Re:"+bean.getMailSubject());
			if(null != bean.getMailContent()){
				StringBuffer sb = new StringBuffer();
				sb.append("<br/>");
				sb.append("<br/>");
				sb.append("<br/>");
				sb.append("<br/>");
				sb.append("<div>"+getString(R.string.reply_from_android)+"</div>");
				sb.append("<br/>");
				sb.append("<br/>");
				sb.append("<br/>");
				sb.append("---------"+getString(R.string.reply_original_email)+"---------");
				String time ="";
				if (null != bean.getReceiptTime() && !"".equals(bean.getReceiptTime())) {
						time = bean.getReceiptTime().replaceAll("T", " ");
				}
				sb.append("<div><font style=\"font-weight:bold;\">"+getString(R.string.reply_sender_item)+"</font>"+bean.getSenderName()+"</div>");
//				sb.append("<div><font style=\"font-weight:bold;\">"+getString(R.string.reply_sendtime_item)+"</font>"+time+"</div>");
//				sb.append("<div><font style=\"font-weight:bold;\">"+getString(R.string.reply_receiver_item)+"</font>"+bean.getRecipient()+"</div>");
				sb.append("<div><font style=\"font-weight:bold;\">"+getString(R.string.reply_subject_item)+"</font>"+bean.getMailSubject()+"</div>");
				sb.append("<br/>");
				et_content.setText(Html.fromHtml(sb.toString()+bean.getMailContent(), imgGetter, null)); 
			}
			replyBean = bean;
		}else if(2 == val){
			et_theme.setText("Fw:"+bean.getMailSubject());
			if(null != bean.getMailContent()){
				StringBuffer sb = new StringBuffer();
				sb.append("<br/>");
				sb.append("<br/>");
				sb.append("<br/>");
				sb.append("<br/>");
				sb.append("<div>"+getString(R.string.reply_from_android)+"</div>");
				sb.append("<br/>");
				sb.append("<br/>");
				sb.append("<br/>");
				sb.append("---------"+getString(R.string.reply_original_email)+"---------");
				String time = "";
				if (null != bean.getReceiptTime() && !"".equals(bean.getReceiptTime())) {
					time = bean.getReceiptTime().replaceAll("T", " ");
				}
				sb.append("<div><font style=\"font-weight:bold;\">"+getString(R.string.reply_sender_item)+"</font>"+bean.getSenderName()+"</div>");
//				sb.append("<div><font style=\"font-weight:bold;\">"+getString(R.string.reply_sendtime_item)+"</font>"+time+"</div>");
//				sb.append("<div><font style=\"font-weight:bold;\">"+getString(R.string.reply_receiver_item)+"</font>"+bean.getRecipient()+"</div>");
				sb.append("<div><font style=\"font-weight:bold;\">"+getString(R.string.reply_subject_item)+"</font>"+bean.getMailSubject()+"</div>");
				sb.append("<br/>");
				et_content.setText(Html.fromHtml(sb.toString()+bean.getMailContent(), imgGetter, null)); 
			}
		}
	}
	// 设置文本框可以显示网络图片
	ImageGetter imgGetter = new Html.ImageGetter() {  
	     @Override
		public Drawable getDrawable(String source) {  
	          Drawable drawable = null;  
	          URL url;  
	          try {  
	              url = new URL(source);  
	              drawable = Drawable.createFromStream(url.openStream(), ""); // 获取网路图片  
	              drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
              } catch (Exception e) {  
	              e.printStackTrace();                  
	              return null;  
	          }  
	          return drawable;  
	     }  
	}; 
	
	private void cleanAllInvidView(){
		cleanInvidCCView();
		cleanInvidView();
		cleanInvidBCCView();
	}
	
	/**
	 * 方法名: initEditView() 
	 * 功 能 : 添加收件人输入 
	 * 参 数 : void 
	 * 返回值: void
	 */
	private View initEditView(){
		view = inflater.inflate(R.layout.item_editview, null);
		actv_show = (AutoCompleteTextView) view.findViewById(R.id.actv_show);
		sAdapter = new ShowContactAdapter(this, contactsList);
		actv_show.setAdapter(sAdapter);
		actv_show.setThreshold(1);  				// 设置输入一个字符 提示，默认为2 
		actv_show.setOnItemClickListener(acomplete);  
		actv_show.setOnKeyListener(onKeyListener);
		actv_show.setOnClickListener(new android.view.View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				tv_cc_and_bcc.setText(getString(R.string.cc_and_bcc) + "," + getString(R.string.sender)+":"+BaseApp.user.getUserChsName());
				if(vLsitCC.size() > 0 || vLsitBCC.size() > 0){
					rl_cc_and_bcc.setVisibility(View.GONE);
					rl_cc.setVisibility(View.VISIBLE);
					rl_bcc.setVisibility(View.VISIBLE);
				}else{
					rl_cc_and_bcc.setVisibility(View.VISIBLE);
					rl_cc.setVisibility(View.GONE);
					rl_bcc.setVisibility(View.GONE);
				}
				btn_execute.setVisibility(View.VISIBLE);
				btn_execute_cc.setVisibility(View.INVISIBLE);
				btn_execute_bcc.setVisibility(View.INVISIBLE);
				cleanAllInvidView();
				afv_addr.requestFocus();
			}
		});
		
		actv_show.setOnFocusChangeListener(new android.view.View.OnFocusChangeListener() {  
		    @Override  
		    public void onFocusChange(View v, boolean hasFocus) {  
		        if(!hasFocus) {	
		        	// 此处为失去焦点时的处理内容
					tmpView = null;
					if(null != aEmailAddr){
						for(int i=0; i<aEmailAddr.size(); i++){
							EmailAddrBean eBean = aEmailAddr.get(i);
							eBean.getTv_tmp().setBackgroundColor(Color.rgb(255, 255, 255));
							eBean.getTv_tmp().setTextColor(Color.rgb(0, 146, 95));
						}
					}
					
				}else{
					tv_cc_and_bcc.setText(getString(R.string.cc_and_bcc)+","+getString(R.string.sender)+":"+BaseApp.user.getUserChsName());
					if(vLsitCC.size()>0||vLsitBCC.size()>0){
						rl_cc_and_bcc.setVisibility(View.GONE);
						rl_cc.setVisibility(View.VISIBLE);
						rl_bcc.setVisibility(View.VISIBLE);
					}else{
						rl_cc_and_bcc.setVisibility(View.VISIBLE);
						rl_cc.setVisibility(View.GONE);
						rl_bcc.setVisibility(View.GONE);
					}
					
					btn_execute.setVisibility(View.VISIBLE);
					btn_execute_cc.setVisibility(View.INVISIBLE);
					btn_execute_bcc.setVisibility(View.INVISIBLE);
					cleanAllInvidView();
				}
			}
		});
		
		return view;
	}
	
	
	/**
	 * 方法名: initCCEditView() 
	 * 功 能 : 添加抄送收件人输入 
	 * 参 数 : void 
	 * 返回值: void
	 */
	private View initEditCCView(){
		
		view = inflater.inflate(R.layout.item_editview, null);
		actv_show_cc = (AutoCompleteTextView) view.findViewById(R.id.actv_show);
		sAdapterCC = new ShowContactAdapter(this, contactsList);
		actv_show_cc.setAdapter(sAdapterCC);
		actv_show_cc.setThreshold(1);  				// 设置输入一个字符 提示，默认为2 
		actv_show_cc.setOnItemClickListener(acompleteCC);  
		actv_show_cc.setOnKeyListener(onKeyCCListener);
		actv_show_cc.setOnFocusChangeListener(new android.view.View.OnFocusChangeListener() {  
			@Override  
			public void onFocusChange(View v, boolean hasFocus) {  
				if(!hasFocus) {		// 此处为失去焦点时的处理内容
					tmpView = null;
					if(null != aEmailCCAddr){
						for(int i=0; i<aEmailCCAddr.size(); i++){
							EmailAddrBean eBean = aEmailCCAddr.get(i);
							eBean.getTv_tmp().setBackgroundColor(Color.rgb(255, 255, 255));
							eBean.getTv_tmp().setTextColor(Color.rgb(0, 146, 95));
						}
					}
				}else{
					btn_execute.setVisibility(View.INVISIBLE);
					btn_execute_cc.setVisibility(View.VISIBLE);
					btn_execute_bcc.setVisibility(View.INVISIBLE);
					cleanAllInvidView();
				}
			}
		});
		return view;
	}
	/**
	 * 方法名: initBCCEditView() 
	 * 功 能 : 添加密送收件人输入 
	 * 参 数 : void 
	 * 返回值: void
	 */
	private View initEditBCCView(){
		
		view = inflater.inflate(R.layout.item_editview, null);
		actv_show_bcc = (AutoCompleteTextView) view.findViewById(R.id.actv_show);
		sAdapterBCC = new ShowContactAdapter(this, contactsList);
		actv_show_bcc.setAdapter(sAdapterBCC);
		actv_show_bcc.setThreshold(1);  				// 设置输入一个字符 提示，默认为2 
		actv_show_bcc.setOnItemClickListener(acompleteBCC);  
		actv_show_bcc.setOnKeyListener(onKeyBCCListener);
		actv_show_bcc.setOnFocusChangeListener(new android.view.View.OnFocusChangeListener() {  
			@Override  
			public void onFocusChange(View v, boolean hasFocus) {  
				if(!hasFocus) {		// 此处为失去焦点时的处理内容
					tmpView = null;
					if(null != aEmailBCCAddr){
						for(int i=0; i<aEmailBCCAddr.size(); i++){
							EmailAddrBean eBean = aEmailBCCAddr.get(i);
							eBean.getTv_tmp().setBackgroundColor(Color.rgb(255, 255, 255));
							eBean.getTv_tmp().setTextColor(Color.rgb(0, 146, 95));
						}
					}
				}else{
					btn_execute.setVisibility(View.INVISIBLE);
					btn_execute_cc.setVisibility(View.INVISIBLE);
					btn_execute_bcc.setVisibility(View.VISIBLE);
					cleanAllInvidView();
				}
			}
		});
		
		return view;
	}
	
	private void cleanInvidView(){
		String addr = actv_show.getText().toString();
		if(null != addr && !"".equals(addr.trim())){
			actv_show.setText("");
		}
	}
	
	private void cleanInvidCCView(){
		String addr = actv_show_cc.getText().toString();
		if(null != addr && !"".equals(addr.trim())){
			actv_show_cc.setText("");
		}
	}
	
	private void cleanInvidBCCView(){
		String addr = actv_show_bcc.getText().toString();
		if(null != addr && !"".equals(addr.trim())){
			actv_show_bcc.setText("");
		}
	}
	
	private OnFocusChangeListener onFocusChangeListener = new OnFocusChangeListener() {
		
		@Override
		public void onFocusChange(View v, boolean hasFocus) {
			if(hasFocus){
				tv_cc_and_bcc.setText(getString(R.string.cc_and_bcc)+","+getString(R.string.sender)+":"+BaseApp.user.getUserChsName());
				if(vLsitCC.size()>0||vLsitBCC.size()>0){
					rl_cc_and_bcc.setVisibility(View.GONE);
					rl_cc.setVisibility(View.VISIBLE);
					rl_bcc.setVisibility(View.VISIBLE);
				}else{
					rl_cc_and_bcc.setVisibility(View.VISIBLE);
					rl_cc.setVisibility(View.GONE);
					rl_bcc.setVisibility(View.GONE);
				}
				cleanAllInvidView();
				et_theme.requestFocus();
			}else{
				
			}
		}
	};
	
	private void clearAllFoucus(){
		afv_addr_cc.clearFocus();
		afv_addr_bcc.clearFocus();
		afv_addr.clearFocus();
	}
	
	// 监听软键盘
	private OnKeyListener onKeyListener = new OnKeyListener() {
		@Override
		public boolean onKey(View v, int keyCode, KeyEvent event) {
			if(keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN){
				String addr = actv_show.getText().toString();
				if(null != addr && !"".equals(addr.trim())){
					actv_show.setText("");
				}
				return true;
			}else if(keyCode == KeyEvent.KEYCODE_DEL && event.getAction() == KeyEvent.ACTION_DOWN){
				String addr = actv_show.getText().toString();
				if(null == addr || "".equals(addr.trim())){
					if(null != tmpView){
						deleteContent(tmpView,true);
						for(int i=0; i<aEmailAddr.size(); i++){
							EmailAddrBean eBean = aEmailAddr.get(i);
							if(tmpView == eBean.getView()){
								aEmailAddr.remove(i);
							}
						}
						tmpView = null;
					}else if(aEmailAddr.size() > 0){
						EmailAddrBean eBean = aEmailAddr.get(aEmailAddr.size() - 1);
						tmpView = eBean.getView();
						eBean.getTv_tmp().setBackgroundColor(Color.rgb(38, 31, 185));
						eBean.getTv_tmp().setTextColor(Color.rgb(255, 255, 255));
					}
				}
			}
			
			return false;
		}
	};
	// 监听软键盘
	private OnKeyListener onKeyBCCListener = new OnKeyListener() {
		@Override
		public boolean onKey(View v, int keyCode, KeyEvent event) {
			if(keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN){
				String addr = actv_show_bcc.getText().toString();
				if(null != addr && !"".equals(addr.trim())){
					actv_show_bcc.setText("");
				}
				return true;
			}else if(keyCode == KeyEvent.KEYCODE_DEL && event.getAction() == KeyEvent.ACTION_DOWN){
				String addr = actv_show_bcc.getText().toString();
				if(null == addr || "".equals(addr.trim())){
					if(null != tmpView){
						deleteBCCContent(tmpView,true);
						for(int i=0; i<aEmailBCCAddr.size(); i++){
							EmailAddrBean eBean = aEmailBCCAddr.get(i);
							if(tmpView == eBean.getView()){
								aEmailBCCAddr.remove(i);
							}
						}
						tmpView = null;
					}else if(aEmailBCCAddr.size() > 0){
						EmailAddrBean eBean = aEmailBCCAddr.get(aEmailBCCAddr.size() - 1);
						tmpView = eBean.getView();
						eBean.getTv_tmp().setBackgroundColor(Color.rgb(38, 31, 185));
						eBean.getTv_tmp().setTextColor(Color.rgb(255, 255, 255));
					}
				}
			}
			
			return false;
		}
	};
	// 监听软键盘
	private OnKeyListener onKeyCCListener = new OnKeyListener() {
		@Override
		public boolean onKey(View v, int keyCode, KeyEvent event) {
			if(keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN){
				String addr = actv_show_cc.getText().toString();
				if(null != addr && !"".equals(addr.trim())){
					actv_show_cc.setText("");
				}
				
				return true;
			}else if(keyCode == KeyEvent.KEYCODE_DEL && event.getAction() == KeyEvent.ACTION_DOWN){
				String addr = actv_show_cc.getText().toString();
				if(null == addr || "".equals(addr.trim())){
					if(null != tmpView){
						deleteCCContent(tmpView,true);
						for(int i=0; i<aEmailCCAddr.size(); i++){
							EmailAddrBean eBean = aEmailCCAddr.get(i);
							if(tmpView == eBean.getView()){
								aEmailCCAddr.remove(i);
							}
						}
						tmpView = null;
					}else if(aEmailCCAddr.size() > 0){
						EmailAddrBean eBean = aEmailCCAddr.get(aEmailCCAddr.size() - 1);
						tmpView = eBean.getView();
						eBean.getTv_tmp().setBackgroundColor(Color.rgb(38, 31, 185));
						eBean.getTv_tmp().setTextColor(Color.rgb(255, 255, 255));
					}
				}
			}
			
			return false;
		}
	};
	
	/**
	 * 方法名: initShowView() 
	 * 功 能 : 添加收件人 
	 * 参 数 : void 
	 * 返回值: void
	 */
	@SuppressLint("ResourceAsColor")
	private View initShowView(EmailBean bean){
		boolean check = false;
		if(null != vLsit && vLsit.size() > 0){
			for(int i=0; i<vLsit.size(); i++){
				if(null != bean.getCode() && bean.getCode().equals(vLsit.get(i).getBean().getCode())){
					check = true;
					break;
				}
			}
		}
		
		if(check){
			return null;
		}
		
		view = inflater.inflate(R.layout.item_email_address, null);
		view.setId(viewId);
		tv_content = (TextView) view.findViewById(R.id.tv_content);
		tv_content.setId(viewId + 300);
		view.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {	
				if(null != aEmailAddr){
					for(int i=0; i<aEmailAddr.size(); i++){
						EmailAddrBean eBean = aEmailAddr.get(i);
						if(v.getId() == eBean.getView().getId()){
							tmpView = v;
							eBean.getTv_tmp().setBackgroundColor(Color.rgb(38, 31, 185));
							eBean.getTv_tmp().setTextColor(Color.rgb(255, 255, 255));
						}else{
							eBean.getTv_tmp().setBackgroundColor(Color.rgb(255, 255, 255));
							eBean.getTv_tmp().setTextColor(Color.rgb(0, 146, 95));
						}
					}
				}
				 
		        et_theme.clearFocus();
				et_content.clearFocus();								
			}
		});
		
		tv_content.setText(bean.getDisplayName());
		viewId++;
		
		EmailAddrBean eBean = new EmailAddrBean();
		eBean.setView(view);
		eBean.setTv_tmp(tv_content);
		aEmailAddr.add(eBean);
		
		return view;
	}
	/**
	 * 方法名: initShowCCView() 
	 * 功 能 : 添加 抄送收件人 
	 * 参 数 : void 
	 * 返回值: void
	 */
	@SuppressLint("ResourceAsColor")
	private View initShowCCView(EmailBean bean){
		boolean check = false;
		if(null != vLsitCC && vLsitCC.size() > 0){
			for(int i=0; i<vLsitCC.size(); i++){
				if(null != bean.getCode() && bean.getCode().equals(vLsitCC.get(i).getBean().getCode())){
					check = true;
					break;
				}
			}
		}
		
		if(check){
			return null;
		}
		
		view = inflater.inflate(R.layout.item_email_address, null);
		view.setId(viewId);
		tv_content = (TextView) view.findViewById(R.id.tv_content);
		tv_content.setId(viewId + 300);
		view.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {	
				if(null != aEmailCCAddr){
					for(int i=0; i<aEmailCCAddr.size(); i++){
						EmailAddrBean eBean = aEmailCCAddr.get(i);
						if(v.getId() == eBean.getView().getId()){
							tmpView = v;
							eBean.getTv_tmp().setBackgroundColor(Color.rgb(38, 31, 185));
							eBean.getTv_tmp().setTextColor(Color.rgb(255, 255, 255));
						}else{
							eBean.getTv_tmp().setBackgroundColor(Color.rgb(255, 255, 255));
							eBean.getTv_tmp().setTextColor(Color.rgb(0, 146, 95));
						}
					}
				}
				
				et_theme.clearFocus();
				et_content.clearFocus();								
			}
		});
		
		tv_content.setText(bean.getDisplayName());
		viewId++;
		
		EmailAddrBean eBean = new EmailAddrBean();
		eBean.setView(view);
		eBean.setTv_tmp(tv_content);
		aEmailCCAddr.add(eBean);
		
		return view;
	}
	/**
	 * 方法名: initShowBCCView() 
	 * 功 能 : 添加密送收件人 
	 * 参 数 : void 
	 * 返回值: void
	 */
	@SuppressLint("ResourceAsColor")
	private View initShowBCCView(EmailBean bean){
		boolean check = false;
		if(null != vLsitBCC && vLsitBCC.size() > 0){
			for(int i=0; i<vLsitBCC.size(); i++){
				if(null != bean.getCode() && bean.getCode().equals(vLsitBCC.get(i).getBean().getCode())){
					check = true;
					break;
				}
			}
		}
		
		if(check){
			return null;
		}
		
		view = inflater.inflate(R.layout.item_email_address, null);
		view.setId(viewId);
		tv_content = (TextView) view.findViewById(R.id.tv_content);
		tv_content.setId(viewId + 300);
		view.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {	
				if(null != aEmailBCCAddr){
					for(int i=0; i<aEmailBCCAddr.size(); i++){
						EmailAddrBean eBean = aEmailBCCAddr.get(i);
						if(v.getId() == eBean.getView().getId()){
							tmpView = v;
							eBean.getTv_tmp().setBackgroundColor(Color.rgb(38, 31, 185));
							eBean.getTv_tmp().setTextColor(Color.rgb(255, 255, 255));
						}else{
							eBean.getTv_tmp().setBackgroundColor(Color.rgb(255, 255, 255));
							eBean.getTv_tmp().setTextColor(Color.rgb(0, 146, 95));
						}
					}
				}
				
				// 弹出键盘
				et_theme.clearFocus();
				et_content.clearFocus();								
			}
		});
		
		tv_content.setText(bean.getDisplayName());
		viewId++;
		
		EmailAddrBean eBean = new EmailAddrBean();
		eBean.setView(view);
		eBean.setTv_tmp(tv_content);
		aEmailBCCAddr.add(eBean);
		
		return view;
	}
	
	OnItemClickListener acomplete = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,long id) {
			Contacts pc = (Contacts)parent.getItemAtPosition(position);  
			if(null != pc){
				actv_show.setText("");  
				EmailBean bean = new EmailBean();
				bean.setCode(pc.getUserCode());
				bean.setDisplayName(pc.getUserChsName());
				bean.setDisplayOrgName(pc.getOrgName());
				bean.setType(pc.getType());
				
				veBean = new ViewEmailBean();
				veBean.setBean(bean);
				View v = initShowView(bean);
				if(null != v){
					veBean.setV(v);
					addContent(veBean);
				}
			}
		}
	}; 
	OnItemClickListener acompleteCC = new OnItemClickListener() {
		
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,long id) {
			Contacts pc = (Contacts)parent.getItemAtPosition(position);  
			if(null != pc){
				actv_show_cc.setText("");  
				EmailBean bean = new EmailBean();
				bean.setCode(pc.getUserCode());
				bean.setDisplayName(pc.getUserChsName());
				bean.setDisplayOrgName(pc.getOrgName());
				bean.setType(pc.getType());
				veBean = new ViewEmailBean();
				veBean.setBean(bean);
				View v = initShowCCView(bean);
				if(null != v){
					veBean.setV(v);
					addCCContent(veBean);
				}
			}
		}
	}; 
	OnItemClickListener acompleteBCC = new OnItemClickListener() {
		
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,long id) {
			Contacts pc = (Contacts)parent.getItemAtPosition(position);  
			if(null != pc){
				actv_show_bcc.setText("");  
				EmailBean bean = new EmailBean();
				bean.setCode(pc.getUserCode());
				bean.setDisplayName(pc.getUserChsName());
				bean.setDisplayOrgName(pc.getOrgName());
				bean.setType(pc.getType());
				veBean = new ViewEmailBean();
				veBean.setBean(bean);
				View v = initShowBCCView(bean);
				if(null != v){
					veBean.setV(v);
					addBCCContent(veBean);
				}
			}
		}
	}; 
	
	/**
	 * 方法名: initDB() 
	 * 功 能 : 初始化数据库
	 * 参 数 : void 
	 * 返回值: void
	 */
	private void initDB() {
		
		if (BaseApp.db == null) {
			BaseApp.db = new DBHelper(WriteEmailActivity.this);
			BaseApp.db.createTable(Contacts.class);
		}
		
		contactsList = BaseApp.db.customQuery(Contacts.class, "userAccount", BaseApp.user.getUserId());
		if(null == contactsList){
			operType = 3;
			contactsList = new ArrayList<Contacts>();
			// 向服务器发送请求
			String info = getAddressBookUser();
			remoteDataRequest(LocalConstants.USER_COMMUNICATION_LIST_SERVER, info);
		}else if(null != contactsList && 1 > contactsList.size()){
			operType = 3;
			contactsList = new ArrayList<Contacts>();
			// 向服务器发送请求
			String info = getAddressBookUser();
			remoteDataRequest(LocalConstants.USER_COMMUNICATION_LIST_SERVER, info);
		}
	}
	
	/**
	 * 添加一组控件
	 * @param veBean 集View和bean
	 */
	private void addContent(ViewEmailBean veBean) {
		vLsit.add(veBean);
		if(afv_addr.getChildCount() < vLsit.size()){
			vLsit.remove(veBean);
			return;
		}else{
			afv_addr.removeViewAt(vLsit.size() - 1);
		}
		afv_addr.addView(veBean.getV());
		afv_addr.addView(initEditView());
		afv_addr.measure(0, 0);
		int myrows = afv_addr.getTotalrows();
		if(myrows < 1){
			myrows = 1;
		}
		int sub_height = ll_parent_addr_height-ll_addr_height;
		int height = ll_addr_height*(myrows);
		int ll_height = ll_addr_height*(myrows);
		int p_height = ll_addr_height*(myrows) + sub_height;
		ViewGroup.LayoutParams afv_addrparams = afv_addr.getLayoutParams();
		ViewGroup.LayoutParams params = ll_addr.getLayoutParams();
		ViewGroup.LayoutParams parentparams = ll_parent_addr.getLayoutParams();
		parentparams.height = p_height ;
		params.height = ll_height ;
		afv_addrparams.height = height;
		ll_addr.setLayoutParams(params);
		afv_addr.setLayoutParams(afv_addrparams);
		ll_parent_addr.setLayoutParams(parentparams);
		
		afv_addr.requestFocus();
	}
	/**
	 * 添加一组控件
	 * @param veBean 集View和bean
	 */
	private void addCCContent(ViewEmailBean veBean) {
		vLsitCC.add(veBean);
		if(afv_addr_cc.getChildCount()<vLsitCC.size()){
			vLsitCC.remove(veBean);
			return;
		}else{
			afv_addr_cc.removeViewAt(vLsitCC.size() - 1);
		}
		
		afv_addr_cc.addView(veBean.getV());
		afv_addr_cc.addView(initEditCCView());
		afv_addr_cc.measure(0, 0);
		int myrows=afv_addr_cc.getTotalrows();
		if(myrows < 1){
			myrows = 1;
		}
		int sub_height = ll_parent_addr_height-ll_addr_height;
		int height = ll_addr_height*(myrows);
		int ll_height = ll_addr_height*(myrows);
		int p_height = ll_addr_height*(myrows)+sub_height;
		ViewGroup.LayoutParams afv_addrparams = afv_addr_cc.getLayoutParams();
		ViewGroup.LayoutParams params = ll_addr_cc.getLayoutParams();
		ViewGroup.LayoutParams parentparams = ll_parent_addr_cc.getLayoutParams();
		parentparams.height = p_height ;
		params.height = ll_height ;
		afv_addrparams.height = height;
		ll_addr_cc.setLayoutParams(params);
		afv_addr_cc.setLayoutParams(afv_addrparams);
		ll_parent_addr_cc.setLayoutParams(parentparams);
		
		afv_addr_cc.requestFocus();
	}
	/**
	 * 添加一组控件
	 * @param veBean 集View和bean
	 */
	private void addBCCContent(ViewEmailBean veBean) {
		vLsitBCC.add(veBean);
		if(afv_addr_bcc.getChildCount()<vLsitBCC.size()){
			vLsitBCC.remove(veBean);
			return;
		}else{
			afv_addr_bcc.removeViewAt(vLsitBCC.size() - 1);
		}
		afv_addr_bcc.addView(veBean.getV());
		afv_addr_bcc.addView(initEditBCCView());
		afv_addr_bcc.measure(0, 0);
		int myrows = afv_addr_bcc.getTotalrows();
		if(myrows < 1){
			myrows = 1;
		}
		int sub_height = ll_parent_addr_height-ll_addr_height;
		int height = ll_addr_height * (myrows);
		int ll_height = ll_addr_height * (myrows);
		int p_height = ll_addr_height * (myrows)+sub_height;
		ViewGroup.LayoutParams afv_addrparams = afv_addr_bcc.getLayoutParams();
		ViewGroup.LayoutParams params = ll_addr_bcc.getLayoutParams();
		ViewGroup.LayoutParams parentparams = ll_parent_addr_bcc.getLayoutParams();
		parentparams.height = p_height ;
		params.height = ll_height ;
		afv_addrparams.height = height;
		ll_addr_bcc.setLayoutParams(params);
		afv_addr_bcc.setLayoutParams(afv_addrparams);
		ll_parent_addr_bcc.setLayoutParams(parentparams);
		
		afv_addr_bcc.requestFocus();
	}
	/**
	 * 删除一组控件
	 * @param v	事件触发控件，触发删除事件对应的“删除”按钮
	 */
	private void deleteContent(View v,boolean onFocus) {
		if (v == null) {
			return;
		}
		// 判断第几个删除按钮触发了事件
		int iIndex = -1;
		for (int i = 0; i < vLsit.size(); i++) {
			if (vLsit.get(i).getV() == v) {
				iIndex = i;
				break;
			}
		}
		
		if (iIndex >= 0) {
			// 从外围llContentView容器里删除第iIndex控件
			afv_addr.removeViewAt(iIndex);
			vLsit.remove(iIndex);
		}
		if(onFocus){
			afv_addr.requestFocus();
		}	
		afv_addr.measure(0, 0);
		int myrows = afv_addr.getTotalrows();
		if(myrows < 2){
			myrows = 0;
		}
		
		myrows++;
		int sub_height = ll_parent_addr_height - ll_addr_height;
		int height = ll_addr_height * (myrows);
		int ll_height = ll_addr_height * (myrows);
		int p_height = ll_addr_height * (myrows)+sub_height;
			
			ViewGroup.LayoutParams afv_addrparams = afv_addr.getLayoutParams();
			ViewGroup.LayoutParams params = ll_addr.getLayoutParams();
			ViewGroup.LayoutParams parentparams = ll_parent_addr.getLayoutParams();
			parentparams.height = p_height ;
			params.height = ll_height ;
			afv_addrparams.height = height;
			ll_addr.setLayoutParams(params);
			afv_addr.setLayoutParams(afv_addrparams);
			ll_parent_addr.setLayoutParams(parentparams);		
		
	}
	/**
	 * 删除一组控件
	 * @param v	事件触发控件，触发删除事件对应的“删除”按钮
	 */
	private void deleteCCContent(View v,boolean onFocus) {
		if (v == null) {
			return;
		}
		// 判断第几个删除按钮触发了事件
		int iIndex = -1;
		for (int i = 0; i < vLsitCC.size(); i++) {
			if (vLsitCC.get(i).getV() == v) {
				iIndex = i;
				break;
			}
		}
		
		if (iIndex >= 0) {
			// 从外围llContentView容器里删除第iIndex控件
			afv_addr_cc.removeViewAt(iIndex);
			vLsitCC.remove(iIndex);
		}
		if(onFocus){
			afv_addr_cc.requestFocus();
		}
		afv_addr_cc.measure(0, 0);
		int myrows = afv_addr_cc.getTotalrows();
		if(myrows < 2){
			myrows = 0;
		}
		myrows++;
		int sub_height = ll_parent_addr_height-ll_addr_height;
		int height = ll_addr_height*(myrows);
		int ll_height = ll_addr_height*(myrows);
		int p_height = ll_addr_height*(myrows)+sub_height;
			
			ViewGroup.LayoutParams afv_addrparams = afv_addr_cc.getLayoutParams();
			ViewGroup.LayoutParams params = ll_addr_cc.getLayoutParams();
			ViewGroup.LayoutParams parentparams = ll_parent_addr_cc.getLayoutParams();
			parentparams.height = p_height ;
			params.height = ll_height ;
			afv_addrparams.height = height;
			ll_addr_cc.setLayoutParams(params);
			afv_addr_cc.setLayoutParams(afv_addrparams);
			ll_parent_addr_cc.setLayoutParams(parentparams);
	}
	/**
	 * 删除一组控件
	 * @param v	事件触发控件，触发删除事件对应的“删除”按钮
	 */
	private void deleteBCCContent(View v,boolean onFocus) {
		if (v == null) {
			return;
		}
		// 判断第几个删除按钮触发了事件
		int iIndex = -1;
		for (int i = 0; i < vLsitBCC.size(); i++) {
			if (vLsitBCC.get(i).getV() == v) {
				iIndex = i;
				break;
			}
		}
		
		if (iIndex >= 0) {
			// 从外围llContentView容器里删除第iIndex控件
			afv_addr_bcc.removeViewAt(iIndex);
			vLsitBCC.remove(iIndex);
		}
		if(onFocus){
			afv_addr_bcc.requestFocus();
		}
		afv_addr_bcc.measure(0, 0);
		int myrows = afv_addr_bcc.getTotalrows();
		if(myrows<2){
			myrows = 0;
		}
		myrows++;
		int sub_height = ll_parent_addr_height-ll_addr_height;
		int height=ll_addr_height * (myrows);
		int ll_height=ll_addr_height * (myrows);
		int p_height=ll_addr_height * (myrows)+sub_height;
			
			ViewGroup.LayoutParams afv_addrparams = afv_addr_bcc.getLayoutParams();
			ViewGroup.LayoutParams params = ll_addr_bcc.getLayoutParams();
			ViewGroup.LayoutParams parentparams = ll_parent_addr_bcc.getLayoutParams();
			parentparams.height = p_height ;
			params.height = ll_height ;
			afv_addrparams.height = height;
			ll_addr_bcc.setLayoutParams(params);
			afv_addr_bcc.setLayoutParams(afv_addrparams);
			ll_parent_addr_bcc.setLayoutParams(parentparams);
	}
	
	/**
	 * 方法名: getAddressBookUser() 
	 * 功 能 : 请求报文
	 * 参 数 : void 
	 * 返回值: JSON报文
	 */
	private String getAddressBookUser() {
		PublicBean bean = new PublicBean();
		bean.setDeviceId(BaseApp.macAddr);
		bean.setUserAccount(BaseApp.user.getUserId());
		bean.setApp("1");
		return JSON.toJSONString(bean);
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			// 取消
			case R.id.btn_back:
				String theme = et_theme.getText().toString();
				if (null == theme || "".equals(theme.trim())) {
					this.finish();
				} else {
					mPopupWindow = new PopupWindowWriteEmail(this, itemsOnClick);
					// 设置layout在PopupWindow中显示的位置
					mPopupWindow.showAtLocation(WriteEmailActivity.this.findViewById(R.id.show_popup), Gravity.BOTTOM|Gravity.CENTER_HORIZONTAL, 0, 0);
				}
				break;
			case R.id.tv_sech:	// 发送
				operType = 2;
				String info = null;;
				try {
//					cleanAllInvidView();
					info = sendEmail(operType);
				} catch (Exception e) {
					e.printStackTrace();
				}
				remoteDataRequest(LocalConstants.EMAIL_SEND_EMAIL_SERVER, info);
				break;
			case R.id.btn_execute:
				mIntent = new Intent(WriteEmailActivity.this, SearchEmailContactsActivity.class);
				startActivityForResult(mIntent, 20);
				break;
			case R.id.btn_execute_cc:
				mIntent = new Intent(WriteEmailActivity.this, SearchEmailContactsActivity.class);
				startActivityForResult(mIntent, 21);
				break;
			case R.id.btn_execute_bcc:
				mIntent = new Intent(WriteEmailActivity.this, SearchEmailContactsActivity.class);
				startActivityForResult(mIntent, 22);
				break;
			case R.id.ll_addr:
				tv_cc_and_bcc.setText(getString(R.string.cc_and_bcc) + "," + getString(R.string.sender)+":"+BaseApp.user.getUserChsName());
				if(vLsitCC.size()>0||vLsitBCC.size()>0){
					rl_cc_and_bcc.setVisibility(View.GONE);
					rl_cc.setVisibility(View.VISIBLE);
					rl_bcc.setVisibility(View.VISIBLE);
				}else{
					rl_cc_and_bcc.setVisibility(View.VISIBLE);
					rl_cc.setVisibility(View.GONE);
					rl_bcc.setVisibility(View.GONE);
				}
				btn_execute.setVisibility(View.VISIBLE);
				btn_execute_cc.setVisibility(View.INVISIBLE);
				btn_execute_bcc.setVisibility(View.INVISIBLE);
				cleanAllInvidView();
				afv_addr.requestFocus();
				operSoftKeyboard(afv_addr); 
				break;
			case R.id.ll_addr_cc:
				btn_execute.setVisibility(View.INVISIBLE);
				btn_execute_cc.setVisibility(View.VISIBLE);
				btn_execute_bcc.setVisibility(View.INVISIBLE);
				cleanAllInvidView();
				afv_addr_cc.requestFocus();
				operSoftKeyboard(afv_addr_cc); 
				break;
			case R.id.ll_addr_bcc:
				btn_execute.setVisibility(View.INVISIBLE);
				btn_execute_cc.setVisibility(View.INVISIBLE);
				btn_execute_bcc.setVisibility(View.VISIBLE);
				cleanAllInvidView();
				afv_addr_bcc.requestFocus();
				operSoftKeyboard(afv_addr_bcc); 
				break;
			case R.id.rl_cc_and_bcc:
				rl_cc_and_bcc.setVisibility(View.GONE);
				rl_cc.setVisibility(View.VISIBLE);
				rl_bcc.setVisibility(View.VISIBLE);
				cleanAllInvidView();
				btn_execute_cc.setVisibility(View.VISIBLE);
				btn_execute.setVisibility(View.INVISIBLE);
				btn_execute_bcc.setVisibility(View.INVISIBLE);
				afv_addr_cc.requestFocus();
				operSoftKeyboard(afv_addr_cc); 
				break;
			case R.id.et_content:
				cleanAllInvidView();
				break;
			case R.id.tv_check:			// 扩大选择区域
				boolean check = cb_important.isChecked();
				if(check){
					cb_important.setChecked(false);
				}else{
					cb_important.setChecked(true);
				}
				break;
		}		
	}
	
	/**
	 * popupwindow弹出窗口事件监听 
	 */
	private OnClickListener itemsOnClick = new OnClickListener() {
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			// 不保存草稿
			case R.id.btn_not_save_drafts:
				if (null != mPopupWindow) {
					mPopupWindow.dismiss();
				}
				finish();
				break;
			// 保存草稿
			case R.id.btn_save_drafts:
				operType = 1;
				String info = null;
				try {
					info = sendEmail(operType);
				} catch (Exception e) {
					e.printStackTrace();
				}
				remoteDataRequest(LocalConstants.EMAIL_SEND_EMAIL_SERVER, info);
				break;
			}
		}		
	};
	/**
	 * 如果软键盘打开刚隐藏，否则反之
	 */
	private void operSoftKeyboard(View view){
		InputMethodManager m = (InputMethodManager) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
		m.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
	}
	
	/**
	 * 方法名: customOrReceiveEmail() 
	 * 功 能 : 自定义或收邮件请求报文
	 * 参 数 : void 
	 * 返回值: void
	 */
	private String sendEmail(int openType) throws Exception{
//		String emailAddr = actv_show.getText().toString();
//		String emailCCAddr = actv_show_cc.getText().toString();
//		String emailBCCAddr = actv_show_bcc.getText().toString();
		String theme = et_theme.getText().toString();
//		String content = et_content.getText().toString(); 
		Editable t = mEditableFactory.newEditable(et_content.getText());
		 
		// 封装文本信息及图片，转换为Html格式，并去除<p></p>
		String content = (Html.toHtml(t)).replace("<p>", "").replace("</p>", "");
		
		rList.clear();
		rListCC.clear();
		rListBCC.clear();
		
//		if (null == emailAddr || "".equals(emailAddr.trim())) {
//			if(null == vLsit || vLsit.size() < 1){
//				DialogUtil.showLongToast(WriteEmailActivity.this, R.string.mail_addr_is_null);
//				return null;
//			}
//		}else{
//			EmailBean ebean = new EmailBean();
//			ebean.setCode(emailAddr);
//			rList.add(ebean);
//		}
//		
//		if (null == emailCCAddr || "".equals(emailCCAddr.trim())) {
//			
//		}else{
//			EmailBean ebean = new EmailBean();
//			ebean.setCode(emailAddr);
//			rListCC.add(ebean);
//		}
//		
//		if (null == emailBCCAddr || "".equals(emailBCCAddr.trim())) {
//			
//		}else{
//			EmailBean ebean = new EmailBean();
//			ebean.setCode(emailAddr);
//			rListBCC.add(ebean);
//		}
		if(openType==2){
			if(null == vLsit || vLsit.size() < 1){
				DialogUtil.showLongToast(WriteEmailActivity.this, getString(R.string.email_address_invalid));
				return null;
			}
		}
		
		
		boolean checkVal = false;
		boolean checkValCC = false;
		boolean checkValBCC = false;
		String addr = "";
		String addrCC = "";
		String addrBCC = "";
		for(int i=0; i<vLsit.size(); i++){
			if(null == vLsit.get(i).getBean().getCode() || "".equals(vLsit.get(i).getBean().getCode())){
				checkVal = true;
				addr = vLsit.get(i).getBean().getDisplayName();
				break;
			}
		}
		for(int i=0; i<vLsitCC.size(); i++){
			if(null == vLsitCC.get(i).getBean().getCode() || "".equals(vLsitCC.get(i).getBean().getCode())){
				checkValCC = true;
				addrCC = vLsitCC.get(i).getBean().getDisplayName();
				break;
			}
		}
		for(int i=0; i<vLsitBCC.size(); i++){
			if(null == vLsitBCC.get(i).getBean().getCode() || "".equals(vLsitBCC.get(i).getBean().getCode())){
				checkValBCC = true;
				addrBCC = vLsitBCC.get(i).getBean().getDisplayName();
				break;
			}
		}
		
		if(checkVal){
			DialogUtil.showLongToast(WriteEmailActivity.this, addr + " " + getString(R.string.email_address_invalid));
			return null;
		}
		if(checkValCC){
			DialogUtil.showLongToast(WriteEmailActivity.this, addrCC + " " + getString(R.string.email_address_invalid));
			return null;
		}
		if(checkValBCC){
			DialogUtil.showLongToast(WriteEmailActivity.this, addrBCC + " " + getString(R.string.email_address_invalid));
			return null;
		}
		
		if (null == theme || "".equals(theme)) {
			DialogUtil.showLongToast(WriteEmailActivity.this, R.string.title_is_null);
			return null;
		}
//		else if (null == content || "".equals(content)) {
//			DialogUtil.showLongToast(WriteEmailActivity.this, R.string.content_is_null);
//			return null;
//		}
		
		PublicBean bean = new PublicBean();
		bean.setDeviceId(BaseApp.macAddr);
		bean.setUserAccount(BaseApp.user.getUserId());
		bean.setSenderName(BaseApp.user.getUserName());
		if(null != replyBean){
			bean.setSourceMailCode(replyBean.getMailCode());
			bean.setMailType(REPLY_MAIL_TYPE);
		}else{
			bean.setMailCode(null);
		}
		
		if(null != vLsit){
			for(int i=0;i<vLsit.size();i++){
				rList.add(vLsit.get(i).getBean());
			}
		}
		if(null != vLsitCC){
			for(int i=0;i<vLsitCC.size();i++){
				rListCC.add(vLsitCC.get(i).getBean());
			}
		}
		if(null != vLsitBCC){
			for(int i=0;i<vLsitBCC.size();i++){
				rListBCC.add(vLsitBCC.get(i).getBean());
			}
		}
		bean.setRecipent(rList);
		bean.setCc(rListCC);
		bean.setBcc(rListBCC);
		bean.setMailCode(mailCode);
		bean.setSubject(theme);
		bean.setContent(content);
		if(cb_important.isChecked()){
			bean.setIsImportantRe("1");
		}else{
			bean.setIsImportantRe("0");
		}
		bean.setIsEncryptionRe("0");
		bean.setNeedReceiptRe("0");
		bean.setMailStatus(operType+"");
		
		return JSON.toJSONString(bean, SerializerFeature.BrowserCompatible);
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
	 * 方法名: clearData() 
	 * 功 能 : 清除数据
	 * 参 数 : void 
	 * 返回值: void
	 */
	private void clearData(){
		rList.clear();
		rListCC.clear();
		rListBCC.clear();
		afv_addr.removeAllViews();
		afv_addr_cc.removeAllViews();
		afv_addr_bcc.removeAllViews();
		vLsit.clear();
		vLsitCC.clear();
		vLsitBCC.clear();
		et_theme.setText("");
		et_content.setText("");
		actv_show.setText("");
		actv_show_cc.setText("");
		actv_show_bcc.setText("");
	}
	/**
	 * http请求回调
	 */
	RequestCallBack<String> submitCallBack = new RequestCallBack<String>() {

		@Override
		public void onStart() {
			DialogUtil.showWithCancelProgressDialog(WriteEmailActivity.this, null, res.getString(R.string.global_prompt_message), null);
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
					LogUtil.promptInfo(WriteEmailActivity.this, ErrorCodeContrast.getErrorCode("0", res));
					return;
				// 验证不合法
				}else if ("1103".equals(msg.getResCode()) || "1104".equals(msg.getResCode())) {
					LogUtil.promptInfo(WriteEmailActivity.this, ErrorCodeContrast.getErrorCode(msg.getResCode(), res));	
					Intent mIntent = new Intent(BroadcastNotice.USER_EXIT);
					sendBroadcast(mIntent);
					return;
				}else if("1210".equals(msg.getResCode())){
					LogUtil.promptInfo(WriteEmailActivity.this, ErrorCodeContrast.getErrorCode(msg.getResCode(), res));	
					Intent mIntent = new Intent(BroadcastNotice.WIPE_USET);
					sendBroadcast(mIntent);
					return;
				}else if (!"1".equals(msg.getResCode())) {
					LogUtil.promptInfo(WriteEmailActivity.this, ErrorCodeContrast.getErrorCode(msg.getResCode(), res));	
					return;
				}
				// 判断消息体是否为空
				if (null == msg.getMessage() || "".equals(msg.getMessage().trim())) {		 
					DialogUtil.showLongToast(WriteEmailActivity.this, R.string.global_no_data);
				} else {
					// 获取解密后并校验后的值
					String decode = EncryptUtil.getDecodeData(msg.getMessage(), BaseApp.key);
					if (decode != null) {
						if(3 == operType){
							 Message msgs = new Message();
							 msgs.obj = decode;
							 msgs.what = 1;
							 mHandler.sendMessage(msgs);
							 return;
						}
						
						ResPublicBean bean = JSON.parseObject(decode, ResPublicBean.class);
						if(null != bean.getSuccess() && "1".equals(bean.getSuccess().trim())){
							if (operType == 2 && -1 == position) {
								clearData();
								DialogUtil.showLongToast(WriteEmailActivity.this, R.string.email_send_success);
								finish();
							}else if(operType == 1){
								DialogUtil.showLongToast(WriteEmailActivity.this, R.string.save_draft_success);
								finish();
							}else if(-1 != position){
								DialogUtil.showLongToast(WriteEmailActivity.this, R.string.email_send_success);
								mIntent = new Intent(WriteEmailActivity.this, MailSearchActivity.class);
								mIntent.putExtra("list", (Serializable)eList);
								mIntent.putExtra("position", position);
								setResult(MailSearchActivity.RESULT_CODE, mIntent);
								finish();
							}
						}else{
							if (operType == 2 || -1 == position) {
								DialogUtil.showLongToast(WriteEmailActivity.this, R.string.email_send_failed);
							}else if(operType == 1){
								DialogUtil.showLongToast(WriteEmailActivity.this, R.string.save_draft_failed);
							}
						}
					}
				}
			} else {
				LogUtil.promptInfo(WriteEmailActivity.this, ErrorCodeContrast.getErrorCode("0", res));
			}
		}

		@Override
		public void onFailure(HttpException error, String msg) {
			DialogUtil.closeDialog();
			if(null != msg && msg.equals(LocalConstants.API_KEY)){
				LogUtil.promptInfo(WriteEmailActivity.this, ErrorCodeContrast.getErrorCode("1207", res));
			}else{
				LogUtil.promptInfo(WriteEmailActivity.this, ErrorCodeContrast.getErrorCode("0", res));
			}
		}
	};
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		
		if(20 != resultCode){
			return;
		}
		
		if (20 == requestCode) {
			EmailBean bean = (EmailBean)data.getSerializableExtra("bean");
			veBean = new ViewEmailBean();
			veBean.setBean(bean);
			View v = initShowView(bean);
			if(null != v){
				veBean.setV(v);
				addContent(veBean);
			}
		}else if(21 == requestCode){
			EmailBean bean = (EmailBean)data.getSerializableExtra("bean");
			veBean = new ViewEmailBean();
			veBean.setBean(bean);
			View v = initShowCCView(bean);
			if(null != v){
				veBean.setV(v);
				addCCContent(veBean);
			}
		}else if(22 == requestCode){
			EmailBean bean = (EmailBean)data.getSerializableExtra("bean");
			veBean = new ViewEmailBean();
			veBean.setBean(bean);
			View v = initShowBCCView(bean);
			if(null != v){
				veBean.setV(v);
				addBCCContent(veBean);
			}
		}		
	};
	
	@Override  
    public boolean onKeyDown(int keyCode, KeyEvent event)  {  
        if (keyCode == KeyEvent.KEYCODE_BACK ) {  
        	String emailAddr =  actv_show.getText().toString();
			String theme = et_theme.getText().toString();
			String content = et_content.getText().toString();
			
			if ((null == emailAddr || "".equals(emailAddr.trim())) && (null == theme || "".equals(theme.trim())) && (null == content || "".equals(content.trim()))) {
				this.finish();
			} else {
				mPopupWindow = new PopupWindowWriteEmail(this, itemsOnClick);
				// 设置layout在PopupWindow中显示的位置
				mPopupWindow.showAtLocation(WriteEmailActivity.this.findViewById(R.id.show_popup), Gravity.BOTTOM|Gravity.CENTER_HORIZONTAL, 0, 0);
			}
        }
		return false;
    };   
}