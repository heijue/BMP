package com.k2.mobile.app.controller.activity.login;

import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.k2.mobile.app.R;
import com.k2.mobile.app.common.config.HttpConstants;
import com.k2.mobile.app.controller.core.BaseActivity;
import com.k2.mobile.app.controller.core.BaseApp;
import com.k2.mobile.app.view.widget.SwitchButton;
import com.k2.mobile.app.view.widget.SwitchButton.OnChangeListener;

public class SettingActivity extends BaseActivity implements OnClickListener {
	
	private Button btn_back;
	private Button btn_out_login;
	private TextView tv_title;
	private TextView tv_sech;
	private EditText ed_real_name;
	private EditText ed_port;
	private EditText ed_site;
	private ImageView iv_choose_ssl;
	private SwitchButton sw_language;

	private boolean flag_img = false;
	private boolean sslFlag = false;
	
	private String real_name;
	private String port;
	private String site;
	private int selected;
	
	private Editor editor;
	private EditText ed_ip;
    private EditText ed_user;
    private EditText ed_passwd;

	private String vpn_ip = "";
	private String vpn_user = "";
	private String vpn_passwd = "";

    @Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE); // 去除头部
		setContentView(R.layout.activity_setting);
		vpn_ip = settingSharedPreference.getString("vpn_ip","");
		vpn_user = settingSharedPreference.getString("vpn_user","");
		vpn_passwd = settingSharedPreference.getString("vpn_passwd","");


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
		btn_back = (Button) findViewById(R.id.btn_back);
		tv_title = (TextView) findViewById(R.id.tv_title);
		tv_sech = (TextView) findViewById(R.id.tv_sech);
        ed_ip = (EditText) findViewById(R.id.edt_ip);
        ed_user = (EditText) findViewById(R.id.edt_user);
        ed_passwd = (EditText) findViewById(R.id.edt_passwd);
        btn_out_login = (Button) findViewById(R.id.btn_out_login);
		ed_real_name = (EditText) findViewById(R.id.ed_real_name);
		ed_port = (EditText) findViewById(R.id.ed_port);
		ed_site = (EditText) findViewById(R.id.ed_site);
		iv_choose_ssl = (ImageView) findViewById(R.id.iv_choose_ssl);
		sw_language = (SwitchButton) findViewById(R.id.sw_language);
		sw_language.setOnChangeListener(new OnChangeListener() {

			@Override
			public void onChange(int position) {
				selected = position;
			}
		});

		tv_title.setText("设置");
		tv_sech.setText("");
		ed_ip.setText(vpn_ip);
		ed_user.setText(vpn_user);
		ed_passwd.setText(vpn_passwd);
	}
	
	/**
	 * 方法名: initListener() 
	 * 功 能 : 初始化监听器 
	 * 参 数 : void 
	 * 返回值: void
	 */
	private void initListener() {
		btn_back.setOnClickListener(this);
		tv_sech.setOnClickListener(this);
		iv_choose_ssl.setOnClickListener(this);
		btn_out_login.setOnClickListener(this);
	}

	private void logic(){
		editor = settingSharedPreference.edit();
		
		boolean saveSettingStatu = settingSharedPreference.getBoolean("saveSettingStatu", true);
		boolean sslChooseStatu = settingSharedPreference.getBoolean("sslChooseStatu", true);
		
		if (false == saveSettingStatu) {
			tv_sech.setBackgroundResource(R.drawable.select);
			flag_img = false;
		} else {
			tv_sech.setBackgroundResource(R.drawable.select_avtive);
			flag_img = true;
		}
		
		if (false == sslChooseStatu) {
			iv_choose_ssl.setImageResource(R.drawable.select);
			sslFlag = false;
		} else {
			iv_choose_ssl.setImageResource(R.drawable.select_avtive);
			sslFlag = true;
		}
		
		String realmName = settingSharedPreference.getString("realmName", "");
		String parent = settingSharedPreference.getString("parent", "");
		String site = settingSharedPreference.getString("site", "");
		
		ed_real_name.setText(realmName);
		ed_port.setText(parent);
		ed_site.setText(site);

		sw_language.setCurrentPosition(settingSharedPreference.getInt("locale",0));
	}
	
	/**
	 * 保存设置信息
	 */
	private void saveUser() {
		
		real_name = ed_real_name.getText().toString().trim();
		port = ed_port.getText().toString().trim();
		site = ed_site.getText().toString().trim();
        String ip = ed_ip.getText().toString().trim();
        String user = ed_user.getText().toString().trim();
        String passwd = ed_passwd.getText().toString().trim();

        editor.putString("vpn_ip",ip);
        editor.putString("vpn_user",user);
        editor.putString("vpn_passwd",passwd);
        editor.putString("realmName", real_name);
		editor.putString("parent", port);
		editor.putString("site", site);
		editor.putBoolean("saveSettingStatu", flag_img);
		editor.putBoolean("sslChooseStatu", sslFlag);
		editor.putInt("locale",selected);
		editor.commit();
		
		HttpConstants.DOMAIN_NAME = real_name;
		HttpConstants.PROT = port;
		HttpConstants.SITE = site;
	}
	
	private void setServer(){
		
		real_name = ed_real_name.getText().toString().trim();
		port = ed_port.getText().toString().trim();
		site = ed_site.getText().toString().trim();
		
		HttpConstants.DOMAIN_NAME = real_name;
		HttpConstants.PROT = port;
		HttpConstants.SITE = site;
	}
	
	private void localExit(){
		if (flag_img == false) {
			saveUser();
		} else {
			setServer();
		}
		checkLang();					// 判断语言
		animFinish();
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.btn_back:
				localExit();
				break;
			case R.id.tv_sech:
				if(flag_img){
					tv_sech.setBackgroundResource(R.drawable.select_avtive);
				}else{
					tv_sech.setBackgroundResource(R.drawable.select);
					saveUser();
				}
				
				flag_img = !flag_img;
				break;
			case R.id.btn_out_login:
//				goExitApp();
				BaseApp.closeActivity();
				break;
		}	
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			localExit();
		}
		return super.onKeyDown(keyCode, event);
	}
}
