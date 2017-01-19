package com.k2.mobile.app.utils;  

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.k2.mobile.app.R;
import com.k2.mobile.app.common.config.BroadcastNotice;
import com.k2.mobile.app.controller.core.BaseApp;

/**
 * @Title: AutoUpdateProcedureUtil.java
 * @Package com.oppo.mo.utils
 * @Description: android:versionName方式程序自动更新
 * @Company:广东欧泊移动通讯有限公司
 * 
 * @author linqijun	
 * @date 2015-05-26 16:11:00
 * @version V1.0
 */ 
public class UpdateProcedureUtil {
	private Activity activity;
	/* 更新进度条 */
    private static final int DOWN_UPDATE = 1;
    /* 安装 */
    private static final int DOWN_OVER = 2;
	/* 提示语 */
	private String updateMsg = null;
	private String updateTitle = null;
	/* 文件url */
	private String apkUrl = null;
	private Dialog noticeDialog;
	private Dialog downloadDialog;
	/* 下载包安装路径 */
    private static final String savePath = Environment.getExternalStorageDirectory() + "/oppo-mo/";
    private static final String saveFileName = savePath + "oppo_oa.apk";
    /* 进度条与通知ui刷新的handler和msg常量 */
    private ProgressBar mProgress;
    private TextView tv_download_rate , tv_cancle;
    private int progress;
    private Thread downLoadThread;
    private boolean interceptFlag = false;
    
    public UpdateProcedureUtil(Activity activity) {
		this.activity = activity;
	}
    
    private Handler mHandler = new Handler(){
    	@Override
		public void handleMessage(Message msg) {
    		switch (msg.what) {
				case DOWN_UPDATE:
					if(99 == progress){
						if(null != downloadDialog){
							downloadDialog.dismiss();
						}
					}
					mProgress.setProgress(progress);
					tv_download_rate.setText(activity.getString(R.string.updating_program)+"("+progress+"%)");
					break;
				case DOWN_OVER:
					if(null != noticeDialog){
						noticeDialog.dismiss();
					}
					installApk();
					break;
			}
    	};
    };
	
    /**
     * 外部接口让主Activity调用 
     * 
     * @param url APP文件路径
     * @param serverVersion 服务器APP版本
     */
  	public boolean checkUpdateInfo(String url, String serverVersion, boolean val){
  		int version = 0;
  		if(null == url){
  			return false;
  		}
  		this.apkUrl = url;
  		try {
			version = SystemUtil.getVersionCode(activity); // 获取本地版本
		} catch (Exception e) {
			e.printStackTrace();
		}
  		
  		if(null == serverVersion || "".equals(serverVersion.trim()) || 0 == version){
			return false;
		}
  		int sVerion = 0;
  		
  		try {
  			sVerion = Integer.parseInt(serverVersion);
		} catch (NumberFormatException e) {
			e.printStackTrace();
		}
  		if (sVerion > version){		// 本地版本与服务端版本不一致则提示更新
  			BaseApp.upgrade = true; // 是否需要更新
  			if(val){
  				showDownloadDialog(val);
  			}else{
  				showNoticeDialog(val);
  			}
  		}else{
  			BaseApp.upgrade = false; // 是否需要更新
  			activity.sendBroadcast(new Intent(BroadcastNotice.UPDATE_PROCEDURE));
  		}
  		
  		return BaseApp.upgrade;
  	}
  	/**
     * 弹出Dialog提示是否下载
     */
  	private void showNoticeDialog(final boolean val){
  		
  		updateMsg = activity.getString(R.string.auto_update);
  		updateTitle = activity.getString(R.string.app_update);
  		
  		AlertDialog.Builder builder = new Builder(activity);
  		builder.setTitle(updateTitle);
  		builder.setMessage(updateMsg);
  		builder.setPositiveButton(activity.getString(R.string.download), new OnClickListener() {			
  			@Override
  			public void onClick(DialogInterface dialog, int which) {
  				dialog.dismiss();
  				showDownloadDialog(val);			
  			}
  		});
  		
  		builder.setNegativeButton(activity.getString(R.string.then_again), new OnClickListener() {			
  			@Override
  			public void onClick(DialogInterface dialog, int which) {
  				activity.sendBroadcast(new Intent(BroadcastNotice.CANCLE_UPDATE_PROCEDURE));
  				dialog.dismiss();				
  			}
  		});
  		
  		noticeDialog = builder.create();
  		WindowManager.LayoutParams params = noticeDialog.getWindow().getAttributes();
  		params.type = WindowManager.LayoutParams.TYPE_SYSTEM_ALERT;
  		noticeDialog.getWindow().setAttributes(params);
  		noticeDialog.getWindow().getAttributes().gravity = Gravity.CENTER;
  		noticeDialog.setCancelable(false);
  		noticeDialog.show();
  	}
  	
private void showDownloadDialog(boolean val){
  		
  		AlertDialog.Builder builder = new Builder(activity);
  		
  		final LayoutInflater inflater = LayoutInflater.from(activity);
  		View v = inflater.inflate(R.layout.progress, null);
  		mProgress = (ProgressBar)v.findViewById(R.id.progress);
  		tv_download_rate = (TextView)v.findViewById(R.id.tv_download_rate);
  		tv_download_rate.setText(activity.getString(R.string.updating_program)+"(0%)");
  		tv_cancle = (TextView)v.findViewById(R.id.tv_cancle);
  		
  		downloadDialog = builder.create();
  		
  		if(val){
  			tv_cancle.setVisibility(View.GONE);
  		}
  		tv_cancle.setOnClickListener(new android.view.View.OnClickListener(){@Override
  	  		public void onClick(View v) {
  				downloadDialog.dismiss();
  				activity.sendBroadcast(new Intent(BroadcastNotice.CANCLE_UPDATE_PROCEDURE));
  				interceptFlag = true;
  	  			
  	  		}});
  		
  		downloadDialog.setCancelable(false);
  		downloadDialog.show();
  		
  		DisplayMetrics dm = new DisplayMetrics();
  		activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
		int width = (int)(dm.widthPixels * 0.9); 	// 宽度
		int height = 0;
  		if(val){
  			tv_cancle.setVisibility(View.GONE);
  			height = (int)(dm.heightPixels*0.18); 	// 高度
  		}else{
  			height = (int)(dm.heightPixels*0.215); 	// 高度
  		}
  		
		WindowManager.LayoutParams params = downloadDialog.getWindow().getAttributes();
		params.gravity = Gravity.CENTER;
		params.width = width;
		params.height = height;
		
		downloadDialog.getWindow().setAttributes(params);
		downloadDialog.setContentView(v, params);
  		downloadApk();
  	}
  	
//  	private void showDownloadDialog(){
//  		
//  		AlertDialog.Builder builder = new Builder(activity);
////  		builder.setTitle(activity.getString(R.string.updating_program));
//  		
//  		final LayoutInflater inflater = LayoutInflater.from(activity);
//  		View v = inflater.inflate(R.layout.progress, null);
//  		mProgress = (ProgressBar)v.findViewById(R.id.progress);
//  		tv_download_rate = (TextView)v.findViewById(R.id.tv_download_rate);
//  		
//  		builder.setView(v);
//  		builder.setNegativeButton(activity.getString(R.string.global_cancel), new OnClickListener() {	
//  			@Override
//  			public void onClick(DialogInterface dialog, int which) {
//  				dialog.dismiss();
//  				activity.sendBroadcast(new Intent(BroadcastNotice.CANCLE_UPDATE_PROCEDURE));
//  				interceptFlag = true;
//  			}
//  		});
//  		downloadDialog = builder.create();
//  		downloadDialog.setCancelable(false);
//  		downloadDialog.show();
//  		downloadApk();
//  	}
  	/* 开始下载 */
  	private Runnable mdownApkRunnable = new Runnable() {	
  		@Override
  		public void run() {
  			try {
  				URL url = new URL(apkUrl);
  			
  				HttpURLConnection conn = (HttpURLConnection)url.openConnection();
  				conn.connect();
  				int length = conn.getContentLength();
  				InputStream is = conn.getInputStream();
  				
  				File file = new File(savePath);
  				if(!file.exists()){
  					file.mkdir();
  				}
  				
  				String apkFile = saveFileName;
  				File ApkFile = new File(apkFile);
  				FileOutputStream fos = new FileOutputStream(ApkFile);
  				
  				int count = 0;
  				byte buf[] = new byte[1024];
  				
  				do{   		   		
  		    		int numread = is.read(buf);
  		    		count += numread;
  		    	    progress =(int)(((float)count / length) * 100);
  		    	    //更新进度
  		    	    mHandler.sendEmptyMessage(DOWN_UPDATE);
  		    		if(numread <= 0){	
  		    			//下载完成通知安装
  		    			mHandler.sendEmptyMessage(DOWN_OVER);
  		    			break;
  		    		}
  		    		fos.write(buf,0,numread);
  		    	}while(!interceptFlag);	// 点击取消就停止下载.
  				
  				fos.close();
  				is.close();
  			} catch (MalformedURLException e) {
  				e.printStackTrace();
  			} catch(IOException e){
  				e.printStackTrace();
  			}
  		}
  	};
  	
  /**
   * 下载apk
   * @param url
   */
   private void downloadApk(){
  		downLoadThread = new Thread(mdownApkRunnable);
  		downLoadThread.start();
  	}
   /**
    * 安装apk
    * @param url
    */
  	private void installApk(){
  		File apkfile = new File(saveFileName);
          if (!apkfile.exists()) {
              return;
          }    
          Intent i = new Intent(Intent.ACTION_VIEW);
          i.setDataAndType(Uri.parse("file://" + apkfile.toString()), "application/vnd.android.package-archive"); 
          activity.startActivity(i);
  	}
}
 