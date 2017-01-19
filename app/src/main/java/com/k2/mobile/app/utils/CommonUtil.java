/*
 * Copyright (c) 2015. OPPO Co., Ltd.
 */
package com.k2.mobile.app.utils;

import java.io.File;
import java.util.Locale;

import com.k2.mobile.app.common.config.HttpConstants;
import com.k2.mobile.app.common.config.LocalConstants;
import com.k2.mobile.app.controller.core.BaseApp;
import com.k2.mobile.app.model.bean.CurEmployee;
import com.k2.mobile.app.model.bean.CurUser;
import com.k2.mobile.app.model.bean.Employee;
import com.k2.mobile.app.model.bean.User;

import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Environment;
import android.os.StatFs;
import android.text.TextUtils;
import android.util.DisplayMetrics;

/**
 * @Title CommonUtil.java
 * @Package com.oppo.mo.utils
 * @Description 常用工具类
 * @Company  K2
 * 
 * @author Jason.Wu
 * @date 2015-01-27 15:13:00
 * @version V1.0
 */
public class CommonUtil {

	private final static String TAG = "CommonUtil";

	/**
	 * 判断URL特殊字符
	 * @param input
	 * @return
	 */
	public static boolean hasURLSpecialChars(String input)
	{
		boolean flag = false;
		if ((input != null) && (input.length() > 0)) {
			char c;
			for (int i = 0; i <= input.length() - 1; i++) {
				c = input.charAt(i);
				switch (c) {
				case '>':
					flag = true;
					break;
				case '<':
					flag = true;
					break;
				case '"':
					flag = true;
					break;
				case '&':
					flag = true;
					break;
				case '[':
					flag = true;
					break;
				case '~':
					flag = true;
					break;
				case '!':
					flag = true;
					break;
				case '@':
					flag = true;
					break;
				case '#':
					flag = true;
					break;
				case '$':
					flag = true;
					break;
				case '%':
					flag = true;
					break;
				case '`':
					flag = true;
					break;
				case '^':
					flag = true;
					break;
				case '*':
					flag = true;
					break;
				case '(':
					flag = true;
					break;
				case ')':
					flag = true;
					break;
				case '+':
					flag = true;
					break;
				case '=':
					flag = true;
					break;
				case '|':
					flag = true;
					break;
				case '{':
					flag = true;
					break;
				case '}':
					flag = true;
					break;
				case ']':
					flag = true;
					break;
				case ';':
					flag = true;
					break;
				case '\'':
					flag = true;
					break;

				}
			}
		}
		return flag;
	}
	
	/**
	 * 检查sdcard是否可用
	 * 
	 * @return true为可用，否则为不可用
	 */
	public static boolean sdCardIsAvailable() {
		String status = Environment.getExternalStorageState();
		if (!status.equals(Environment.MEDIA_MOUNTED))
			return false;
		return true;
	}

	/**
	 * Checks if there is enough Space on SDCard
	 * 
	 * @param updateSize
	 *            Size to Check
	 * @return True if the Update will fit on SDCard, false if not enough space
	 *         on SDCard Will also return false, if the SDCard is not mounted as
	 *         read/write
	 */
	public static boolean enoughSpaceOnSdCard(long updateSize) {
		String status = Environment.getExternalStorageState();
		if (!status.equals(Environment.MEDIA_MOUNTED))
			return false;
		return (updateSize < getRealSizeOnSdcard());
	}

	/**
	 * get the space is left over on sdcard
	 */
	public static long getRealSizeOnSdcard() {
		File path = new File(Environment.getExternalStorageDirectory()
				.getAbsolutePath());
		StatFs stat = new StatFs(path.getPath());
		long blockSize = stat.getBlockSize();
		long availableBlocks = stat.getAvailableBlocks();
		return availableBlocks * blockSize;
	}

	/**
	 * Checks if there is enough Space on phone self
	 * 
	 */
	public static boolean enoughSpaceOnPhone(long updateSize) {
		return getRealSizeOnPhone() > updateSize;
	}

	/**
	 * get the space is left over on phone self
	 */
	public static long getRealSizeOnPhone() {
		File path = Environment.getDataDirectory();
		StatFs stat = new StatFs(path.getPath());
		long blockSize = stat.getBlockSize();
		long availableBlocks = stat.getAvailableBlocks();
		long realSize = blockSize * availableBlocks;
		return realSize;
	}

	/**
	 * 根据手机分辨率从dp转成px
	 * 
	 * @param context
	 * @param dpValue
	 * @return
	 */
	public static int dip2px(Context context, float dpValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dpValue * scale + 0.5f);
	}

	/**
	 * 根据手机的分辨率px(像素)转成dp
	 */
	public static int px2dip(Context context, float pxValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (pxValue / scale + 0.5f) - 15;
	}

	public static String getSDCardPath(Context context) {
		if (!sdCardIsAvailable()) {
			File directory = context.getFilesDir();
			return directory.getPath();
		}
		return Environment.getExternalStorageDirectory().getPath();
	}

	public static boolean createRootPath(Context context) {
		String rootPath = CommonUtil.getSDCardPath(context) + File.separator
				+ LocalConstants.MAIN_FOLDER_NAME;
		File file = new File(rootPath);
		if (!file.exists()) {
			return file.mkdirs();
		}
		return true;
	}

	public static String createSubFolderPath(Context context, String folderName) {
		String folderPath = CommonUtil.getSDCardPath(context) + File.separator
				+ LocalConstants.MAIN_FOLDER_NAME + File.separator + folderName;
		File folderDir = new File(folderPath);
		if (!folderDir.exists()) {
			folderDir.mkdirs();
		}
		return folderDir.getPath();
	}

	public static String getImgNameInCurrentTimeMillis() {
		return "IMG" + System.currentTimeMillis() + ".jpg";
	}

	/**
	 * Get Server URL
	 * @param context
	 * @return
	 */
	public static String getServerURL(Context context) {
		SharedPreferences projectPre = context.getSharedPreferences(LocalConstants.SP_SERVER_URL, Context.MODE_PRIVATE);
		String url = projectPre.getString(LocalConstants.SP_SERVER_URL_VALUE, HttpConstants.WEBSERVICE_URL);
		return url;
	}
	
	/**
	 * Set Server URL
	 * @param context
	 * @return
	 */
	public static void setServerURL(Context context, String url) {
		SharedPreferences projectPre = context.getSharedPreferences(LocalConstants.SP_SERVER_URL, Context.MODE_PRIVATE);
		SharedPreferences.Editor prefsWriter = projectPre.edit();
        prefsWriter.putString(LocalConstants.SP_SERVER_URL_VALUE, url);
        prefsWriter.commit();
	}

	/**
	 * Get First Tag
	 * @param context
	 * @return
	 */
	public static boolean getFirstTag(Context context) {
		SharedPreferences projectPre = context.getSharedPreferences(LocalConstants.SP_FIRST_TAG, Context.MODE_PRIVATE);
		boolean isFirstVisit = projectPre.getBoolean(LocalConstants.SP_IS_FIRST_VISIT, false);
		return isFirstVisit;
	}
	
	/**
	 * Set First Tag
	 * @param context
	 * @return
	 */
	public static void setFirstTag(Context context, boolean isFirstVisit) {
		SharedPreferences projectPre = context.getSharedPreferences(LocalConstants.SP_FIRST_TAG, Context.MODE_PRIVATE);
		SharedPreferences.Editor prefsWriter = projectPre.edit();
        prefsWriter.putBoolean(LocalConstants.SP_IS_FIRST_VISIT, isFirstVisit);
        prefsWriter.commit();
	}
	/**
	 * 判断App是否在前台运行
	 * @param context
	 * @param packageName 包名
	 * @return boolean true:是在前台运行 false:在后台运行
	 */
	public static boolean isRunningForeground (Context context, String packageName){
	    ActivityManager am = (ActivityManager)context.getSystemService(Context.ACTIVITY_SERVICE);
	    ComponentName cn = am.getRunningTasks(1).get(0).topActivity;
	    String currentPackageName = cn.getPackageName();
	    if(!TextUtils.isEmpty(currentPackageName) && currentPackageName.equals(packageName))
	    {
	        return true ;
	    }
	 
	    return false ;
	}
	
	/**
	 * Get language type
	 * @param context
	 * @return
	 */
	public static Locale getLanguageType(Context context) {
//		SharedPreferences languagePre = context.getSharedPreferences(LocalConstants.SP_LANGUAGE_TYPE, Context.MODE_PRIVATE);
//		LogUtil.i(TAG, "Locale.getDefault(): "+Locale.getDefault().toString().toLowerCase());
//		String languageType = languagePre.getString(LocalConstants.SP_LANGUAGE_TYPE_VALUE, Locale.getDefault().toString().toLowerCase());
//		LogUtil.i(TAG, "languageType: "+languageType);
//		Locale locale = new Locale(languageType);
//		LogUtil.i(TAG, "locale.toString(): "+locale.toString());

		SharedPreferences settingSharedPreference = context.getSharedPreferences(LocalConstants.SHARED_PREFERENCE_SETTING, Context.MODE_PRIVATE);
		int lang = settingSharedPreference.getInt("locale",0);
		if ( lang < 3){
			return BaseApp.localeSymbols[lang];
		}
		return BaseApp.localeSymbols[0];
		//config.locale = BaseApp.localeSymbols[lang];
		//resources.updateConfiguration(config,dm);
		//return locale;
	}
	
	/**
	 * Set language type
	 * @param context
	 * @return
	 */
	public static void setLanguageType(Context context, Locale locale) {//(Context context, int languageType) {
		SharedPreferences languagePre = context.getSharedPreferences(LocalConstants.SP_LANGUAGE_TYPE, Context.MODE_PRIVATE);
		SharedPreferences.Editor prefsWriter = languagePre.edit();
//        prefsWriter.putInt(Constants.SP_LANGUAGE_TYPE_VALUE, languageType);
//		LogUtil.i(TAG, "locale: "+locale.toString().toLowerCase());
        prefsWriter.putString(LocalConstants.SP_LANGUAGE_TYPE_VALUE, locale.toString().toLowerCase());
        prefsWriter.commit();
	}
	
	/**
	 * Get CanManager Location
	 * @param context
	 * @return
	 */
	public static boolean getCanManager(Context context) {
		SharedPreferences projectPre = context.getSharedPreferences(LocalConstants.SP_CAN_MANAGER, Context.MODE_PRIVATE);
		return projectPre.getBoolean(LocalConstants.SP_CAN_MANAGER_VALUE, false);
	}
	
	/**
	 * Set CanManager Location
	 * @param context
	 * @return
	 */
	public static void setCanManager(Context context, boolean canManager) {
		SharedPreferences projectPre = context.getSharedPreferences(LocalConstants.SP_CAN_MANAGER, Context.MODE_PRIVATE);
		SharedPreferences.Editor prefsWriter = projectPre.edit();
        prefsWriter.putBoolean(LocalConstants.SP_CAN_MANAGER_VALUE, canManager);
        prefsWriter.commit();
	}
	
	/**
	 * Set Employee Info.
	 * @param context
	 * @return
	 */
	public static void setEmployee(Context context, Employee employee) {
		SharedPreferences projectPre = context.getSharedPreferences(LocalConstants.SP_EMPLOYEE, Context.MODE_PRIVATE);
		SharedPreferences.Editor prefsWriter = projectPre.edit();
        prefsWriter.putString(LocalConstants.SP_EMPLOYEE_ID_VALUE, employee.getEmployeeId());
        prefsWriter.putString(LocalConstants.SP_EMPLOYEE_LOGIN_ID_VALUE, employee.getLoginId());
        prefsWriter.putString(LocalConstants.SP_EMPLOYEE_FULL_NAME_VALUE, employee.getFullName());
        prefsWriter.putInt(LocalConstants.SP_EMPLOYEE_GENDER_VALUE, employee.getGender());
        prefsWriter.putBoolean(LocalConstants.SP_EMPLOYEE_IS_SUPERVISOR_VALUE, employee.isIsSupervisor());
        prefsWriter.putString(LocalConstants.SP_EMPLOYEE_LANGUAGE_VALUE, employee.getLanguage());
        prefsWriter.putInt(LocalConstants.SP_EMPLOYEE_LAST_STATUS_VALUE, employee.getLastStatus());
        prefsWriter.putString(LocalConstants.SP_EMPLOYEE_PROJECT_ID_VALUE, employee.getProjectId());
        prefsWriter.putString(LocalConstants.SP_EMPLOYEE_PROJECT_NAME_VALUE, employee.getProjectName());
        prefsWriter.putString(LocalConstants.SP_EMPLOYEE_PROJECT_ID_SELECTED_VALUE, employee.getProjectId());
        prefsWriter.putString(LocalConstants.SP_EMPLOYEE_PROJECT_NAME_SELECTED_VALUE, employee.getProjectName());
        prefsWriter.putString(LocalConstants.SP_EMPLOYEE_CURPROJECT_ID_VALUE, employee.getCurProjectId());
        prefsWriter.putString(LocalConstants.SP_EMPLOYEE_CURPROJECT_NAME_VALUE, employee.getCurProjectName());
        prefsWriter.putBoolean(LocalConstants.SP_EMPLOYEE_CAN_MANAGER_VALUE, employee.isCanManager());
        prefsWriter.commit();
	}
	
	public static void setCurEmployee(Context context, CurEmployee curEmployee) {
		SharedPreferences projectPre = context.getSharedPreferences(LocalConstants.SP_EMPLOYEE, Context.MODE_PRIVATE);
		SharedPreferences.Editor prefsWriter = projectPre.edit();
        prefsWriter.putString(LocalConstants.SP_EMPLOYEE_ID_VALUE, curEmployee.getEMPLOYEE_ID());
        prefsWriter.putString(LocalConstants.SP_EMPLOYEE_LOGIN_ID_VALUE, curEmployee.getEMPLOYEE_LOGIN_ID());
        prefsWriter.putString(LocalConstants.SP_EMPLOYEE_FULL_NAME_VALUE, curEmployee.getEMPLOYEE_FULL_NAME());
        prefsWriter.putInt(LocalConstants.SP_EMPLOYEE_GENDER_VALUE, curEmployee.getEMPLOYEE_GENDER());
        prefsWriter.putBoolean(LocalConstants.SP_EMPLOYEE_IS_SUPERVISOR_VALUE, curEmployee.EMPLOYEE_IS_SUPERVISOR);
        prefsWriter.putString(LocalConstants.SP_EMPLOYEE_LANGUAGE_VALUE, curEmployee.getEMPLOYEE_LANGUAGE());
        prefsWriter.putInt(LocalConstants.SP_EMPLOYEE_LAST_STATUS_VALUE, curEmployee.getEMPLOYEE_LAST_STATUS());
        prefsWriter.putString(LocalConstants.SP_EMPLOYEE_PROJECT_ID_VALUE, curEmployee.getEMPLOYEE_PROJECT_ID());
        prefsWriter.putString(LocalConstants.SP_EMPLOYEE_PROJECT_NAME_VALUE, curEmployee.getEMPLOYEE_PROJECT_NAME());
        prefsWriter.putString(LocalConstants.SP_EMPLOYEE_PROJECT_ID_SELECTED_VALUE, curEmployee.getEMPLOYEE_PROJECT_ID_SELECTED());
        prefsWriter.putString(LocalConstants.SP_EMPLOYEE_PROJECT_NAME_SELECTED_VALUE, curEmployee.getEMPLOYEE_PROJECT_NAME_SELECTED());
        prefsWriter.putBoolean(LocalConstants.SP_EMPLOYEE_CAN_MANAGER_VALUE, curEmployee.isEMPLOYEE_CAN_MANAGER());
        prefsWriter.commit();
	}
	
	/**
	 * Get Employee Info.
	 * @param context
	 * @return
	 */
	public static CurEmployee getEmployee(Context context) {
		SharedPreferences employeePre = context.getSharedPreferences(LocalConstants.SP_EMPLOYEE, Context.MODE_PRIVATE);
		
		CurEmployee employee = new CurEmployee();
		employee.setEMPLOYEE_ID(employeePre.getString(LocalConstants.SP_EMPLOYEE_ID_VALUE, ""));
		employee.setEMPLOYEE_LOGIN_ID(employeePre.getString(LocalConstants.SP_EMPLOYEE_LOGIN_ID_VALUE, ""));
		employee.setEMPLOYEE_FULL_NAME(employeePre.getString(LocalConstants.SP_EMPLOYEE_FULL_NAME_VALUE, ""));
		employee.setEMPLOYEE_GENDER(employeePre.getInt(LocalConstants.SP_EMPLOYEE_GENDER_VALUE, -1));
		employee.setEMPLOYEE_IS_SUPERVISOR(employeePre.getBoolean(LocalConstants.SP_EMPLOYEE_IS_SUPERVISOR_VALUE, false));
		employee.setEMPLOYEE_LANGUAGE(employeePre.getString(LocalConstants.SP_EMPLOYEE_LANGUAGE_VALUE, ""));
		employee.setEMPLOYEE_LAST_STATUS(employeePre.getInt(LocalConstants.SP_EMPLOYEE_LAST_STATUS_VALUE, -1));
		employee.setEMPLOYEE_PROJECT_ID(employeePre.getString(LocalConstants.SP_EMPLOYEE_PROJECT_ID_VALUE, ""));
		employee.setEMPLOYEE_PROJECT_NAME_SELECTED(employeePre.getString(LocalConstants.SP_EMPLOYEE_PROJECT_NAME_SELECTED_VALUE, ""));
		employee.setEMPLOYEE_PROJECT_ID_SELECTED(employeePre.getString(LocalConstants.SP_EMPLOYEE_PROJECT_ID_SELECTED_VALUE, ""));
		employee.setEMPLOYEE_PROJECT_NAME(employeePre.getString(LocalConstants.SP_EMPLOYEE_PROJECT_NAME_VALUE, ""));
		employee.setEMPLOYEE_CAN_MANAGER(employeePre.getBoolean(LocalConstants.SP_EMPLOYEE_CAN_MANAGER_VALUE, false));
		
		return employee;
	}
	
	/**
	 * Set Login User Info.
	 * @param context
	 * @return
	 */
	public static void setUser(Context context, User user) {
		SharedPreferences projectPre = context.getSharedPreferences(LocalConstants.SP_USER, Context.MODE_PRIVATE);
		SharedPreferences.Editor prefsWriter = projectPre.edit();

        prefsWriter.putString(LocalConstants.SP_USER_ID_VALUE, user.getUserId());
		
        prefsWriter.putString(LocalConstants.SP_USER_NAME_VALUE, user.getUserName());
        prefsWriter.putString(LocalConstants.SP_USER_ADDRESS_VALUE, user.getAddress());
        prefsWriter.putString(LocalConstants.SP_USER_EMAIL_VALUE, user.getEmail());
        prefsWriter.putString(LocalConstants.SP_USER_FIRST_NAME_VALUE, user.getFirstName());
        prefsWriter.putString(LocalConstants.SP_USER_LAST_NAME_VALUE, user.getLastName());
        prefsWriter.putString(LocalConstants.SP_USER_PHONE_VALUE, user.getPhone());
        prefsWriter.putString(LocalConstants.SP_USER_REMARK_VALUE, user.getRemark());
        prefsWriter.putString(LocalConstants.SP_USER_SEX_VALUE, user.getSex());
        prefsWriter.putInt(LocalConstants.SP_USER_STATUS_VALUE, user.getStatus());
        prefsWriter.commit();
	}
	
	/**
	 * Get Login User Info.
	 * @param context
	 * @return
	 */
	public static CurUser getUser(Context context) {
		SharedPreferences userPre = context.getSharedPreferences(LocalConstants.SP_USER, Context.MODE_PRIVATE);
		
		CurUser user = new CurUser();
		user.setUSER_ID(userPre.getString(LocalConstants.SP_USER_ID_VALUE, ""));
		user.setUSER_NAME(userPre.getString(LocalConstants.SP_USER_NAME_VALUE, ""));
		user.setUSER_ADDRESS(userPre.getString(LocalConstants.SP_USER_ADDRESS_VALUE, ""));
		user.setUSER_EMAIL(userPre.getString(LocalConstants.SP_USER_EMAIL_VALUE, ""));
		user.setUSER_FIRST_NAME(userPre.getString(LocalConstants.SP_USER_FIRST_NAME_VALUE, ""));
		user.setUSER_LAST_NAME(userPre.getString(LocalConstants.SP_USER_LAST_NAME_VALUE, ""));
		user.setUSER_PHONE(userPre.getString(LocalConstants.SP_USER_PHONE_VALUE, ""));
		user.setUSER_REMARK(userPre.getString(LocalConstants.SP_USER_REMARK_VALUE, ""));
		user.setUSER_SEX(userPre.getInt(LocalConstants.SP_USER_SEX_VALUE, -1));
		user.setUSER_STATUS(userPre.getInt(LocalConstants.SP_USER_STATUS_VALUE, -1));

		return user;
	}
	
}
