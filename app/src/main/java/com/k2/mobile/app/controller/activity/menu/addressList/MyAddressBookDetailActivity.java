package com.k2.mobile.app.controller.activity.menu.addressList;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.k2.mobile.app.R;
import com.k2.mobile.app.controller.core.BaseActivity;
import com.k2.mobile.app.controller.core.BaseApp;
import com.k2.mobile.app.model.bean.Contacts;
import com.k2.mobile.app.utils.ImageLoaderUtil;
import com.k2.mobile.app.utils.StringUtil;
import com.k2.mobile.app.view.widget.CircleImageView;

/**
 * @Package com.oppo.mo.controller.activity.menu.workbench;
 * @Description 通讯录详情
 * @Company  K2
 * 
 * @ClassName: MyAddressBookDetailActivity
 * @Description: 通讯录详情
 * @author linqijun
 * @date 2015-4-8 下午4:04:00
 * 
 */
public class MyAddressBookDetailActivity extends BaseActivity implements OnClickListener {
	 
	private Button btn_back;
	private TextView tv_title;
	private TextView tv_sech;
	private Contacts contacts;
	
	private TextView tv_user_name;
	private TextView tv_user_account;
	private TextView tv_user_eng_Name;
	private TextView tv_user_position;
	private TextView tv_tell;
	private TextView tv_phoneNumber;
	private TextView tv_compy_email;
	
	private LinearLayout ll_phoneNumber;
	private LinearLayout ll_compy_email;
	private LinearLayout ll_cornet;
	
	private CircleImageView iv_head_por;
	// 更新头像
	public ImageLoaderUtil imageLoader;   
	
	@Override
	protected void onCreate(Bundle paramBundle) {
		super.onCreate(paramBundle);
		// 去除头部
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_contact_details);
		initView();
		initListener();
		loadData();
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
		tv_sech = (TextView) findViewById(R.id.tv_sech);
		iv_head_por = (CircleImageView) findViewById(R.id.iv_head_por);
		tv_user_name = (TextView) findViewById(R.id.tv_user_name);
		tv_user_account = (TextView) findViewById(R.id.tv_user_account);
		tv_user_eng_Name = (TextView) findViewById(R.id.tv_user_eng_Name);
		tv_user_position = (TextView) findViewById(R.id.tv_user_position);
		tv_tell = (TextView) findViewById(R.id.tv_tell);
		tv_phoneNumber = (TextView) findViewById(R.id.tv_phoneNumber);
		tv_compy_email = (TextView) findViewById(R.id.tv_compy_email);
		
		ll_phoneNumber = (LinearLayout) findViewById(R.id.ll_phoneNumber);
		ll_compy_email = (LinearLayout) findViewById(R.id.ll_compy_email);
		ll_cornet = (LinearLayout) findViewById(R.id.ll_cornet);
		
		tv_title.setVisibility(View.GONE);
		tv_sech.setVisibility(View.GONE);
		imageLoader = new ImageLoaderUtil(this);
	}
	
	/**
	 * 方法名: initListener() 
	 * 功 能 : 监听事件 
	 * 参 数 : void 
	 * 返回值: void
	 */
	private void initListener() {
		btn_back.setOnClickListener(this);
		tv_tell.setOnClickListener(this);
		
		ll_phoneNumber.setOnClickListener(this);
		ll_compy_email.setOnClickListener(this);
		ll_cornet.setOnClickListener(this);
	}
	
	/**
	 * 方法名: loadData() 
	 * 功 能 : 监听事件 
	 * 参 数 : void 
	 * 返回值: void
	 */
	private void loadData() {
		
		contacts = (Contacts)getIntent().getSerializableExtra("contacts"); 
		
		if(contacts != null){
//			imageLoader.DisplayImage(contacts.getFileUrl(), iv_head_por, 1, contacts.getSex()); // 加载远程图片
			tv_title.setText(getString(R.string.personal_details));
			tv_user_name.setText(contacts.getUserChsName());
			tv_user_account.setText(contacts.getUserAccount());
			if (null != contacts.getUserEnName() && "" != contacts.getUserEnName().trim()) {
				tv_user_eng_Name.setText(contacts.getUserEnName());
			}
			
			tv_user_position.setText(contacts.getRealityOrgName());//JobDesc();
			tv_tell.setText(contacts.getOfficeTel());
			if(null != contacts.getMobilePhone() && !"".equals(contacts.getMobilePhone().trim())){
				String strTmp = contacts.getMobilePhone();
				if(StringUtil.checkMobilePhone(strTmp)){
					tv_phoneNumber.setText(strTmp.substring(0, 3) + "****"+strTmp.substring(7, strTmp.length()));
				}else{
					tv_phoneNumber.setText(strTmp);
				}
			}
			if(null != contacts.getEmail() && !"".equals(contacts.getEmail())){
				if(StringUtil.checkEmail(contacts.getEmail())){
					String[] staVal = contacts.getEmail().split("@");
					if(3 < staVal[0].length()){
						tv_compy_email.setText(staVal[0].substring(0, 3)+"****@"+staVal[1]);
					}else{
						tv_compy_email.setText(staVal[0].substring(0, staVal[0].length())+"****"+staVal[1]);
					}
				}else{
					tv_compy_email.setText(contacts.getEmail());
				}
			}
		}
	}
	/**
	 * 方法名: call() 
	 * 功 能 : 拨打电话
	 * 参 数 : number 电话号码 
	 * 返回值: void
	 */
	private void call(String number){
		
		if(null == number || "".equals(number.trim())){
			return;
		}
		
        Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:"+number));  
        startActivity(intent);  
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.btn_back:
				finish();
				overridePendingTransition(R.anim.push_left_in, R.anim.abc_fade_out);
				break;
			case R.id.ll_cornet:
				String tellNumber = tv_tell.getText().toString();
				call(tellNumber);
				break;
			case R.id.ll_phoneNumber:
				if(null != contacts.getMobilePhone() && !"".equals(contacts.getMobilePhone().trim())){
					call(contacts.getMobilePhone());
				}
				break;
			case R.id.ll_compy_email:
				if(null != contacts.getEmail() && !"".equals(contacts.getEmail().trim())){
					String[] reciver = new String[] {contacts.getEmail()};  
			        Intent myIntent = new Intent(android.content.Intent.ACTION_SEND);  
			        myIntent.setType("plain/text");  
			        myIntent.putExtra(android.content.Intent.EXTRA_EMAIL, reciver);  
			        startActivity(Intent.createChooser(myIntent, "send email"));  
				}
				break;
		}
	}
}
