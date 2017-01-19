/*
 * Copyright (c) 2015. OPPO Co., Ltd.
 */
package com.k2.mobile.app.controller.core;

import java.util.Locale;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import com.k2.mobile.app.R;
import com.k2.mobile.app.common.config.LocalConstants;
import com.k2.mobile.app.utils.AlertUtil;
import com.k2.mobile.app.utils.CommonUtil;
import com.k2.mobile.app.utils.GPSUtil;
import com.k2.mobile.app.utils.LogUtil;
import com.k2.mobile.app.utils.AlertUtil.OnAlertSelectId;

/**
 * @Title BaseActivity.java
 * @Package com.oppo.mo.controller.core
 * @Description activity基类
 * @Company  K2
 * 
 * @author Jason.Wu
 * @date 2015-01-27 15:13:00
 * @version V1.0
 */
public abstract class BaseActivity extends Activity {

	protected Resources res;
	protected Context mContext;
	public BaseApp application;
	protected SharedPreferences loginSharedPreference;//
	protected SharedPreferences settingSharedPreference;//
	
	@Override
	public void setContentView(int layoutResID) {		
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		super.setContentView(layoutResID);

		res = this.getResources();
		mContext = this;
		
		loginSharedPreference = this.getSharedPreferences(
				LocalConstants.SHARED_PREFERENCE_LOGIN, Context.MODE_PRIVATE);

		settingSharedPreference = this.getSharedPreferences(
				LocalConstants.SHARED_PREFERENCE_SETTING, Context.MODE_PRIVATE);
	}


	@Override
	public void setContentView(View view, LayoutParams params) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.setContentView(view, params);

		res = this.getResources();
		mContext = this;
	}


	@Override
	public void setContentView(View view) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.setContentView(view);

		res = this.getResources();
		mContext = this;
	}
	
	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		super.onRestoreInstanceState(savedInstanceState);
	}


	@Override
	protected void onResume() {
		super.onResume();
	}


	@Override
	protected void onSaveInstanceState(Bundle outState) {
		// TODO Auto-generated method stub
		super.onSaveInstanceState(outState);
	}


	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
	}


	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
	}


	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}


	@Override
	public void onLowMemory() {
		super.onLowMemory();
	}

// Activity Method ----------------------------------------------------------------------------------------------------------------------------
	
	/**
	 * @Title: checkUserPermission
	 * @Description: 检查用户权限
	 * @return void    返回类型
	 * @throws
	 */
	public void checkUserPermission() {
		// 调用接口检查用户是否拥有权限
		
		// 如果具备权限，则调用接口验证TOKEN是否过期
		checkTokenIfExpired();
	}
	
	/**
	 * @Title: checkTokenIfExpired
	 * @Description: 检查TOKEN是否过期
	 * @return void    返回类型
	 * @throws
	 */
	public void checkTokenIfExpired() {
		
	}
	
	/**
	 * 
	 * @Title: startActivity
	 * @Description: 跳转Activity
	 * @return void    返回类型
	 * @throws
	 */
	public void startActivity(Class<?> activity) {
		Intent intent = new Intent(this, activity);
		startActivity(intent);
	}
	
	/**
	 * @Title: initUserInfo
	 * @Description: 初始化员工数据（从全局变量中读取）
	 * @param     设定文件
	 * @return void    返回类型
	 * @throws
	 */
	 public void initUserInfo() {
		 /*if(application == null) {
			application = (BaseApp) this.getApplicationContext(); 
		 }*/
	 }
	
	public void changeLang(Locale l) {
		Configuration config = res.getConfiguration();
		DisplayMetrics dm = res.getDisplayMetrics();
		config.locale = l;
		res.updateConfiguration(config, dm);
	}

	public void checkLang() {
		Configuration config = res.getConfiguration();  
		//String languageType = CommonUtil.getLanguageType(this).toString().toLowerCase();
		config.locale = CommonUtil.getLanguageType(this);
//		if (languageType.equals(Locale.getDefault().toString().toLowerCase())) {
//			config.locale = Locale.getDefault();
//		} else if (languageType.equals(Locale.ENGLISH.toString().toLowerCase())) {
//			config.locale = Locale.ENGLISH;
//		} else if (languageType.equals(Locale.SIMPLIFIED_CHINESE.toString().toLowerCase())) {
//			config.locale = Locale.SIMPLIFIED_CHINESE;
//		} else if (languageType.equals(Locale.US.toString().toLowerCase())) {
//			config.locale = Locale.US;
//		}
//		LogUtil.i("Class: "+this.getClass().getName() + "config.locale: "+config.locale);
		
		BaseApp.reqLang = config.locale.toString();
		
		changeLang(config.locale);
	}
	
	public void checkLang(Locale locale) {
		Configuration config = res.getConfiguration();  
		String languageType = locale.toString().toLowerCase();
		if (languageType.equals(Locale.getDefault().toString().toLowerCase())) {
			config.locale = Locale.getDefault();
		} else if (languageType.equals(Locale.ENGLISH.toString().toLowerCase())) {
			config.locale = Locale.ENGLISH;
		} else if (languageType.equals(Locale.SIMPLIFIED_CHINESE.toString().toLowerCase())) {
			config.locale = Locale.SIMPLIFIED_CHINESE;
		} else if (languageType.equals(Locale.US.toString().toLowerCase())) {
			config.locale = Locale.US;
		}
		LogUtil.i("Class: "+this.getClass().getName() + "config.locale: "+config.locale);
		changeLang(config.locale);
	}

	protected void hideKeyboard(View view) {
		InputMethodManager imm = (InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
	}

	public void animFinish() {
		super.finish();
		overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
	}

	public void defaultFinish() {
		super.finish();
	}

	protected boolean hasExtra(String pExtraKey) {
		if (getIntent() != null) {
			return getIntent().hasExtra(pExtraKey);
		}
		return false;
	}
	
	protected void openActivity(Class<?> pClass) {
		openActivity(pClass, null);
	}

	protected void openActivityWithAnimFinish(Class<?> pClass, Bundle pBundle) {
		Intent intent = new Intent(this, pClass);
		intent.addFlags(Intent.FLAG_ACTIVITY_NO_USER_ACTION);
		if (pBundle != null) {
			intent.putExtras(pBundle);
		}
		startActivity(intent);
		this.animFinish();
	}

	protected void openActivity(Class<?> pClass, Bundle pBundle) {
		Intent intent = new Intent(this, pClass);
		intent.addFlags(Intent.FLAG_ACTIVITY_NO_USER_ACTION);
		if (pBundle != null) {
			intent.putExtras(pBundle);
		}
		startActivity(intent);
		overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
	}

	protected void openActivityIsTop(Class<?> pClass) {
		openActivityIsTop(pClass, null);
	}

	protected void openActivityIsTop(Class<?> pClass, Bundle pBundle) {
		Intent intent = new Intent(this, pClass);
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		if (pBundle != null) {
			intent.putExtras(pBundle);
		}
		startActivity(intent);
		overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
	}

	protected void openActivityClearTop(Class<?> pClass, Bundle pBundle) {
		Intent intent = new Intent(this, pClass);
		if (pBundle != null) {
			intent.putExtras(pBundle);
		}
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		intent.addFlags(Intent.FLAG_ACTIVITY_NO_USER_ACTION);
		startActivity(intent);
		overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
	}

	protected void openActivityClearTopAnim2(Class<?> pClass, Bundle pBundle) {
		Intent intent = new Intent(this, pClass);
		if (pBundle != null) {
			intent.putExtras(pBundle);
		}
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		intent.addFlags(Intent.FLAG_ACTIVITY_NO_USER_ACTION);
		startActivity(intent);
		overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
	}

	protected void openActivity(String pAction) {
		openActivity(pAction, null);
	}

	protected void openActivity(String pAction, Bundle pBundle) {
		Intent intent = new Intent(pAction);
		intent.addFlags(Intent.FLAG_ACTIVITY_NO_USER_ACTION);
		if (pBundle != null) {
			intent.putExtras(pBundle);
		}
		startActivity(intent);
	}

	/**
	 * 退出APP
	 * */
	public void goExitApp() {
		AlertUtil.showCustomDialog(mContext,
				R.string.global_exit_app,
				R.string.global_cancel, R.string.global_ok,
				R.color.black, new OnAlertSelectId() {

					@Override
					public void onClick(int whichButton) {
						switch (whichButton) {
						case 0: {// cancel
							break;
						}
						case 1: {// confirm
							exitApp();
							break;
						}

						default:
							break;
						}
					}
				});
	}

	/**
	 * 注销
	 * */
	public void goLogout() {
		AlertUtil.showCustomDialog(mContext,
				R.string.global_logout_app,
				R.string.global_cancel, R.string.global_ok,
				R.color.black, new OnAlertSelectId() {

					@Override
					public void onClick(int whichButton) {
						// TODO Auto-generated method stub
						switch (whichButton) {
						case 0: {// cancel
							break;
						}
						case 1: {// confirm
							logout();
							break;
						}

						default:
							break;
						}
					}
				});
	}
	
	/**
	 * 完全退出app
	 */
	public void exitApp() {
		try {
			defaultFinish();
			BaseApp.exit();
			int nPid = android.os.Process.myPid();  
	        android.os.Process.killProcess(nPid);  
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 注销
	 */
	public void logout() {
		BaseApp.removeActivity(this);
		//openActivityWithAnimFinish(LoginActivity.class, null);
	}


	 /**
	  * 返回上一个页面
	  */
	 public void goBack(Activity activity) {
		 BaseApp.removeActivity(activity);
		animFinish();
	 }
	 
	 /**
	  * 返回上一个页面
	  */
	 public void goBack(Activity activity, Class<?> pClass, Bundle bundle) {
		 BaseApp.removeActivity(activity);
		 openActivityWithAnimFinish(pClass, bundle);
	 }
	
	/**
	 * Check GPS
	 * */
	protected void checkGPS() {
		if (!GPSUtil.isOPen(this)) {
			gpsOpenDialog();
		}
	}
	
	
	/**
	 * Open GPS dialog
	 */
	private void gpsOpenDialog() {
		Dialog dialog = AlertUtil.showCustomDialog(this, R.string.global_gps_not_open,
				R.string.global_exit, R.string.global_go_system_settings,
				R.color.black, new OnAlertSelectId() {

					@Override
					public void onClick(int whichButton) {
						// TODO Auto-generated method stub
						switch (whichButton) {
						case 0: {// cancel
							BaseActivity.this.finish();
							System.exit(0);
							break;
						}
						case 1: {// setting
							GPSUtil.openGPS(BaseActivity.this);

							break;
						}

						default:
							break;
						}
					}
				});
		dialog.setCancelable(false);
	}
}
