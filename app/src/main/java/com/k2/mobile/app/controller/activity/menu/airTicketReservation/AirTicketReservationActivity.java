package com.k2.mobile.app.controller.activity.menu.airTicketReservation;


import com.k2.mobile.app.R;
import com.k2.mobile.app.controller.core.BaseActivity;
import com.k2.mobile.app.controller.core.BaseApp;
import com.k2.mobile.app.view.widget.CircleImageView;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
* @Title AirTicketReservationActivity.java
* @Package com.oppo.mo.controller.activity.benefitLife;
* @Description 机票预定
* @Company  K2
* 
* @author AirTicketReservationActivity
* @date 2015-8-13 下午14:05:57
* @version V1.0
*/
@SuppressLint("NewApi")
public class AirTicketReservationActivity extends BaseActivity implements OnClickListener {
	
	// 头部标题与搜索
	private TextView tv_title, tv_sech;
	// 返回
	private Button btn_back;
	private RelativeLayout  rl_my, rl_zh, rl_tb;
	private CircleImageView civ_zh, civ_my, civ_tb;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);	// 去除头部
		setContentView(R.layout.activity_air_ticket_reservation);
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
		
		rl_my = (RelativeLayout) findViewById(R.id.rl_my);
		rl_zh = (RelativeLayout) findViewById(R.id.rl_zh); 
		rl_tb = (RelativeLayout) findViewById(R.id.rl_tb); 
		
		civ_zh = (CircleImageView) findViewById(R.id.iv_zh);
		civ_my = (CircleImageView) findViewById(R.id.iv_my);
		civ_tb = (CircleImageView) findViewById(R.id.iv_tb);
		
		tv_title.setText(getString(R.string.air_ticket_reservation));	
		tv_sech.setVisibility(View.GONE);	 
	}
	
	/**
	 * 方法名: initListener() 
	 * 功 能 : 初始化监听器
	 * 参 数 : void 
	 * 返回值: void
	 */
	private void initListener() {
		btn_back.setOnClickListener(this);
		rl_my.setOnClickListener(this);
		rl_zh.setOnClickListener(this);
		rl_tb.setOnClickListener(this);
		civ_zh.setOnClickListener(this);
		civ_my.setOnClickListener(this);
		civ_tb.setOnClickListener(this);
	}
	
	private void logic(){
		
	}
		
	@Override
	public void onClick(View v) {
		String number = null;
		String pack = null;
		String url = null;
		switch (v.getId()) {
			case R.id.btn_back:	// 返回
				finish();
				break;
			case R.id.rl_my:
				number = "4006139139";
				phoneCall(number);
				break;
			case R.id.rl_zh:
				number = "4006782001";
				phoneCall(number);
				break;
			case R.id.rl_tb:
				number = "4006940069";
				phoneCall(number);
				break;
			case R.id.iv_zh:
				pack = "com.travelsky.bluesky";
				url = "http://bluesky.travelsky.com/BlueSkyMobile/download/MobileOBT.Client.Android.apk";
				if(checkApp(pack, url)){
					Intent intent = new Intent();
					ComponentName com = new ComponentName("com.travelsky.bluesky", "com.travelsky.bluesky.UIView");
					intent.setComponent(com);
					Bundle bundle = new Bundle();
					bundle.putString("serviceCode", "oppo");
					bundle.putString("account", "oppo" + BaseApp.user.getUserId());
					intent.putExtras(bundle);
					startActivity(intent);
				}
				break;
			case R.id.iv_my:
				pack = "com.yf.shinetour";
				url = "http://112.124.127.37:6003/APP/Android/ShineTour.apk";
				if(checkApp(pack, url)){
					Intent intent = new Intent();
					intent = getPackageManager().getLaunchIntentForPackage(pack);
					startActivity(intent);
				}
				break;
			case R.id.iv_tb:
				pack = "com.tempus.frtravel.app";
				url = "http://d.feiren.com/gj/Cococ.apk";
				if(checkApp(pack, url)){
					Intent intent = new Intent();
					intent = getPackageManager().getLaunchIntentForPackage(pack);
					startActivity(intent);
				}
				break;
		}
	}
	/**
	 * 方法名: phoneCall() 
	 * 功 能 : 拨打电话
	 * 参 数 : String number: 电话号码 
	 * 返回值: void
	 */
	private void phoneCall(String number){
		Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + number));
        startActivity(intent);
	}
	/**
	 * 方法名: checkCozyGo() 
	 * 功 能 : 检测中航APP是否安装和版本号
	 * 参 数 : void
	 * 返回值: void
	 */
	private boolean checkApp(String pack, final String url) {
		boolean result = false;
		PackageInfo Info;
//		int versionCode = 0;
		try {
			final PackageManager pm = this.getPackageManager(); 
			Info = pm.getPackageInfo(pack, 0);	// 当前版本的版本号
//			versionCode = Info.versionCode;							// 当前版本的包名
		} catch (NameNotFoundException e) {
			e.printStackTrace();
			Info = null;
		}
		
		if (Info == null ) {   // || versionCode < 49
			new AlertDialog.Builder(this)
					.setTitle(getString(R.string.tip_information))
					.setMessage(getString(R.string.download_app))
					.setPositiveButton(getString(R.string.global_ok), new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog, int i) {
									// 按钮事件
									Uri uri = Uri.parse(url);
									Intent downloadIntent = new Intent(Intent.ACTION_VIEW, uri);
									startActivity(downloadIntent);
								}
					}).setNegativeButton(getString(R.string.global_cancel), null).show();
			result = false;
		} else {
			result = true;
		}
		
		return result;
	}
}
