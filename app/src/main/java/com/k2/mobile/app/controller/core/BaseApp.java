/*
 * Copyright (c) 2015. OPPO Co., Ltd.
 */
package com.k2.mobile.app.controller.core;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;

import com.k2.mobile.app.model.bean.Employee;
import com.k2.mobile.app.model.bean.HomeMenuBean;
import com.k2.mobile.app.model.bean.User;
import com.k2.mobile.app.model.db.DBHelper;
import com.k2.mobile.app.utils.CommonUtil;
import com.k2.mobile.app.utils.NetWorkUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * @Title BaseApp.java
 * @Package com.oppo.mo.controller.core
 * @Description 应用级类
 * @Company  K2
 * 
 * @author Jason.Wu
 * @date 2015-01-27 15:13:00
 * @version V1.0
 */
@SuppressLint("NewApi")
public class BaseApp extends Application {
	private  static  BaseApp instance;
	/* 用于退出Activity */
	private static List<Activity> activities;
	/* 是否登陆 */
	public static boolean isLogin = false;
	/* 线程池参数 */
	public WorkThread workThread;
	/* 全局上下文 */
	public Context context;
	/* 本地数据库 */
	public static DBHelper db = null;
	/* MAC地址 */
	public static String macAddr = null;
	/* DES加密密钥 */
	public static String key = null;
	/* TOKEN */
	public static String token = null;
	/* 验证  */
	public static String mask = null;
	public static String clientid = null;
	// 本地语言
	public static String reqLang = "zh"; // zh中文，en英文
	/* 登陆用户信息 */
	public static User user = null;
	/* 保存常用菜单信息 */
	public static List<HomeMenuBean> hList = null;
	/* 是否需要更新邮件 */
	public static boolean updateEmail = false;
	/* 程序是否更新 */
	public static boolean upgrade = false;

	public static String[] localeValues;

	public static Locale[] localeSymbols;

	static {
		localeValues = new String[]{"en-US", "zh-CN", "ja-JP"};
		localeSymbols = new Locale[] { new Locale("en","US"),new Locale("zh","CN"), new Locale("ja","JP") };
	}

	public SharedPreferences getSP(String name,int mode){
		return this.getSharedPreferences(name,mode);
	}

	public static BaseApp getInstance() {
		return BaseApp.instance;
	}

	public BaseApp() {
		
	}

	@Override
	public void onCreate() {
		super.onCreate();
//		捕抓异常类
//		ExceptionCatchUtil exception = ExceptionCatchUtil.getInstance();  
//		exception.init(getApplicationContext());  
	        
		this.context = this.getApplicationContext();
		activities = new ArrayList<Activity>();
		user = new User();

		instance = this;
		// 全局异常
		// CrashHandler crashHandler = CrashHandler.getInstance();
		// crashHandler.init(getApplicationContext());

	}
	 /**
	  * 保存登录用户数据
	  * */
	 public void setLoginUserInfos(Context context, User user) {
		 CommonUtil.setUser(context, user);
	 }
	 
	 /**
	  * 保存用户员工数据
	  * */
	 public void setEmployeeInfos(Context context, Employee employee) {
		 String languageType = employee.getLanguage();
		 if(languageType == null || languageType.length() == 0) {
			 languageType = CommonUtil.getLanguageType(context).toString();
		 }
		 
		 CommonUtil.setEmployee(context, employee);
	 }

	/**
	 * add
	 */
	public static void addActivity(Activity activity) {
		activities.add(activity);
	}

	/**
	 * remove
	 */
	public static void removeActivity(Activity activity) {
		activities.remove(activity);
	}

	/**
	 * 全部关闭Activity
	 */
	public static void closeActivity() {
		if (null != activities) {
			for (int i=0; i<activities.size(); i++) {
				activities.get(i).finish();
			}
		}
	}
	
	/**
	 * for each
	 */
	public static void exit() {
		try {
			for (Activity activity : activities) {
//				LogUtils.i("Class: "+this.getClass().getName() + "activities.size(): "+activities.size());
				if (activity != null)
					activity.finish();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * for each
	 */
	public static void exit(Activity currentActivity) {
		try {
			for (Activity activity : activities) {
				if (currentActivity == activity
						|| currentActivity.equals(activity)) {
					continue;
				} else {
					activity.finish();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onTerminate() {
		// TODO Auto-generated method stub
//		threadPoolManager.stop();
		workThread.stopWorker();
		super.onTerminate();
	}

	@Override
	public void onLowMemory() {
		// TODO Auto-generated method stub
		super.onLowMemory();
	}

	@Override
	public void onTrimMemory(int level) {
		// TODO Auto-generated method stub
		final boolean isIceCreamSandwich = Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH;
		if(isIceCreamSandwich) {
			super.onTrimMemory(level);
		}
	}
	
	
// ----------------------------------------------------------------------------------------------------------------------------
	
	/**
     * 内部类，工作线程
     */
    private class WorkThread extends Thread {
         // 该工作线程是否有效，用于结束该工作线程
         private boolean isRunning = true;

         /*
          * 关键所在啊，如果任务队列不空，则取出任务执行，若任务队列空，则等待
          */
         @Override
         public void run() {
              while (isRunning) {// 注意，若线程无效则自然结束run方法，该线程就没用了
            	  
            	  if (NetWorkUtil.isNetworkAvailable(getApplicationContext())) {
            		  sendFileUploadRequest();
            	  }
            		  // 加入线程队列，上传文件
            		  //threadPoolManager.addAsyncTask(new UploadFileTask(context, handler, TASK_CALLBACK, file));
            	  try {
					Thread.sleep(5000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
              }
         }

         // 停止工作，让该线程自然执行完run方法，自然结束
         public void stopWorker() {
              isRunning = false;
         }
    }
    
    /**
	 * 上传文件(异步) 
	 * */
	public synchronized void sendFileUploadRequest() {
    	/*//LogUtils.i("Class: "+this.getClass().getName() + "------------------------------------------ sendFileUploadRequest()");
		FileBean file = getUnuploadRecord();
  	  	if(file == null) {
  	  		return;
  	  	}
  	  	
		// 根据路径读取文件
		String filePath = file.getFullPath();
		LogUtils.i("Class: "+this.getClass().getName() + "ID: "+file.getId()+" ---- status: " + file.getStatus());
		LogUtils.i("Class: "+this.getClass().getName() + "filePath: " + filePath);
		Bitmap bitmap = null;
		try {
			// 将文件转换成二进制字符串
			int index = 0;
			bitmap = ImageUtil.getLoacalBitmap(this.context, filePath);
			if(bitmap == null) {
				return;
			}
			
			String binaryData = ImageUtil.bitmapToString(bitmap);
			
//			if(binaryData != null && binaryData.length() > 0)
//				LogUtils.i("Class: "+this.getClass().getName() + "binaryData: " + binaryData.substring(0, 100));
        	
			SendRequest.getFileUploadRequest(context, handler,
					TASK_CALLBACK, file.getId(), binaryData, index);
		} catch (Exception e) {
			e.printStackTrace();
		}*/
	}
}
