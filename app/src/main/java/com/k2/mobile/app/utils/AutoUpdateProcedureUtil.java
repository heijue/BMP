package com.k2.mobile.app.utils;  

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.k2.mobile.app.R;
import com.k2.mobile.app.common.config.BroadcastNotice;
import com.k2.mobile.app.common.config.LocalConstants;
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
@SuppressLint("NewApi")
public class AutoUpdateProcedureUtil {

	private Activity activity;
	/* 无法连接 */
    private static final int DOWN_ERROR = -1;
	/* 更新进度条 */
    private static final int DOWN_UPDATE = 1;
    /* 安装 */
    private static final int DOWN_OVER = 2;
	/* 文件url */
	private String apkUrl = null;
	private Dialog noticeDialog;
	private AlertDialog downloadDialog;
	/* 下载包安装路径 */
//    private static final String savePath = Environment.getExternalStorageDirectory() + "/oppo-mo/";
    private static final String saveFileName = LocalConstants.LOCAL_FILE_PATH + "oppo_oa.apk";
    /* 进度条与通知ui刷新的handler和msg常量 */
    private ProgressBar mProgress;
    private TextView tv_download_rate, tv_cancle;
    private Button bn_download,bn_refuse_download;
    private int progress;
    private Thread downLoadThread;
    private boolean interceptFlag = false;
    private String size = "1";
    
    public AutoUpdateProcedureUtil(Activity activity) {
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
				case DOWN_ERROR:
					if(null != downloadDialog){
						downloadDialog.dismiss();
					}
					Intent mIntent = new Intent(BroadcastNotice.AUTO_UPDATE_PROCEDURE);
					mIntent.putExtra("falg", -1);
					activity.sendBroadcast(mIntent);
					break;
			}
    	};
    };
	
    /**
     * 外部接口让主Activity调用 
     * 
     * @param url APP文件路径
     * @param serverVersion 服务器APP版本
     * @param val 是否强制更新 
     */
  	public void checkUpdateInfo(String url, String serverVersion, boolean val, String size){
  		this.size = size;
  		int version = 0;
  		if(null == url){
  			return;
  		}
  		this.apkUrl = url;
  		try {
			version = SystemUtil.getVersionCode(activity); // 获取本地版本
		} catch (Exception e) {
			e.printStackTrace();
		}
  		
  		if(null == serverVersion || "".equals(serverVersion.trim()) || 0 == version){
			return;
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
  				showDownloadDialog(val, size);
  			}else{
  				showNoticeDialog(val, size);
  			}
  		}else{
  			BaseApp.upgrade = false;
  			activity.sendBroadcast(new Intent(BroadcastNotice.AUTO_UPDATE_PROCEDURE));
  		}
  	}
  	/**
     * 弹出Dialog提示是否下载
     */
  	private void showNoticeDialog(final boolean val, final String size){
  		  		
  		AlertDialog.Builder builder = new Builder(activity, R.style.DownloadDialog);
  		final LayoutInflater inflater = LayoutInflater.from(activity);
  		View body = inflater.inflate(R.layout.download_body, null);
  		bn_download =(Button) body.findViewById(R.id.bn_download);
  		bn_refuse_download =(Button) body.findViewById(R.id.bn_refuse_download);
  		
  		noticeDialog = builder.create();
  		noticeDialog.getWindow().getAttributes().gravity = Gravity.CENTER;
  		noticeDialog.setCancelable(false);
  		noticeDialog.show();
  		
  		DisplayMetrics dm = new DisplayMetrics();
  		activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
  		
		int width = (int)(dm.widthPixels * 0.9); 		// 宽度
		int height = (int)(dm.heightPixels * 0.26); 	// 高度
		
  		WindowManager.LayoutParams params = noticeDialog.getWindow().getAttributes();
  		params.width = width;
  		params.height = height;
  		noticeDialog.setContentView(body,params);
  		// 确定升级
  		bn_download.setOnClickListener(new android.view.View.OnClickListener(){
  			@Override
  	  		public void onClick(View v) {
  			noticeDialog.dismiss();
			showDownloadDialog(val, size);
  	  	}});
  		// 取消升级
  		bn_refuse_download.setOnClickListener(new android.view.View.OnClickListener(){
  			@Override
  			public void onClick(View v) {
  			noticeDialog.dismiss();
  			activity.sendBroadcast(new Intent(BroadcastNotice.AUTO_UPDATE_PROCEDURE));
  			interceptFlag = true;
  			
  		}});
  	}
  	
  	private void showDownloadDialog(boolean val, final String size){
  		
  		AlertDialog.Builder builder = new Builder(activity);
  		
  		final LayoutInflater inflater = LayoutInflater.from(activity);
  		View v = inflater.inflate(R.layout.progress, null);
  		mProgress = (ProgressBar)v.findViewById(R.id.progress);
  		tv_download_rate = (TextView)v.findViewById(R.id.tv_download_rate);
  		tv_download_rate.setText(activity.getString(R.string.updating_program)+"(0%)");
  		tv_cancle = (TextView)v.findViewById(R.id.tv_cancle);
  		
  		downloadDialog = builder.create();
  		// 强制更新
  		if(val){
  			tv_cancle.setVisibility(View.GONE);
  		}
  		
  		tv_cancle.setOnClickListener(new android.view.View.OnClickListener(){
  			@Override
  	  		public void onClick(View v) {
  				downloadDialog.dismiss();
  				activity.sendBroadcast(new Intent(BroadcastNotice.AUTO_UPDATE_PROCEDURE));
  				interceptFlag = true;
  	  			
  	  		}});
  		
  		downloadDialog.setCancelable(false);
  		downloadDialog.show();
  		
  		DisplayMetrics dm = new DisplayMetrics();
  		activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
		int width = (int)(dm.widthPixels * 0.9); 	// 宽度
		int height = 0;
		// 强制更新
  		if(val){
  			tv_cancle.setVisibility(View.GONE);
  			height = (int)(dm.heightPixels*0.17); 	// 高度
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
  	/* 开始下载 */
  	private Runnable mdownApkRunnable = new Runnable() {	
  		@Override
  		public void run() {
  			try {
  				URL url = new URL(apkUrl);
  			
  				HttpURLConnection conn = (HttpURLConnection)url.openConnection();
  				conn.setConnectTimeout(5*1000);
  				conn.setReadTimeout(5*1000);
  				conn.connect();
  				
  				int responseCode = conn.getResponseCode(); 
  				if(200 != responseCode){					// 连接超时或等待超时
  					mHandler.sendEmptyMessage(DOWN_ERROR);
  					return;
  				}
  			  
  				int length = 1; // conn.getContentLength();
  				if(null != size && !"".equals(size.trim())){
  					try {
  						length = Integer.parseInt(size);
					} catch (Exception e) {
						length = 1;
						e.printStackTrace();
					}
  				}
  				
  				InputStream is = conn.getInputStream();
  				
  				File file = new File(LocalConstants.LOCAL_FILE_PATH);
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
 