package com.k2.mobile.app.controller.activity.menu.personalCenter;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.alibaba.fastjson.JSON;
import com.k2.mobile.app.R;
import com.k2.mobile.app.common.config.BroadcastNotice;
import com.k2.mobile.app.common.config.HttpConstants;
import com.k2.mobile.app.common.config.LocalConstants;
import com.k2.mobile.app.controller.core.BaseActivity;
import com.k2.mobile.app.controller.core.BaseApp;
import com.k2.mobile.app.model.bean.HTTPOutput;
import com.k2.mobile.app.model.bean.PublicBean;
import com.k2.mobile.app.model.bean.ReqMessage;
import com.k2.mobile.app.model.bean.ResPublicBean;
import com.k2.mobile.app.model.http.other.UrlStrController;
import com.k2.mobile.app.utils.DialogUtil;
import com.k2.mobile.app.utils.EncryptUtil;
import com.k2.mobile.app.utils.ErrorCodeContrast;
import com.k2.mobile.app.utils.FastJSONUtil;
import com.k2.mobile.app.utils.LogUtil;
import com.k2.mobile.app.utils.UploadFileUtil;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

/**
* @Title EditHeadImgActivity.java
* @Package com.oppo.mo.controller.activity.menu;
* @Description 图片预览
* @Company  K2
* 
* @author linqijun
* @date 2015-5-5 下午20:05:57
* @version V1.0
*/
@SuppressLint("NewApi")
public class EditHeadImgActivity extends BaseActivity implements OnClickListener {
	
	// 头部标题与搜索
	private TextView tv_title, tv_sech;
	// 返回
	private Button btn_back;
	private ImageView iv_show;
	private Bitmap bitmap = null;
	
	private Handler mHandler = new Handler(){
		@Override
		public void dispatchMessage(android.os.Message msg) {
			DialogUtil.closeDialog();
			String res = (String)msg.obj;
			if (null != res && !"".equals(res.trim())) {
				ReqMessage resMsg = FastJSONUtil.getJSONToEntity(res, ReqMessage.class);
				// 判断返回标识状态是否为空
				if (null == resMsg || null == resMsg.getResCode() || "".equals(resMsg.getResCode())) {				
					LogUtil.promptInfo(EditHeadImgActivity.this, ErrorCodeContrast.getErrorCode("0", getResources()));
				}else if ("1103".equals(resMsg.getResCode()) || "1104".equals(resMsg.getResCode())) {
					LogUtil.promptInfo(EditHeadImgActivity.this, ErrorCodeContrast.getErrorCode(resMsg.getResCode(), getResources()));	
					Intent mIntent = new Intent(BroadcastNotice.USER_EXIT);
					sendBroadcast(mIntent);
					return;
				}else if("1".equals(resMsg.getResCode()) && null != resMsg.getMessage()){
					// 获取解密后并校验后的值
					String decode = EncryptUtil.getDecodeData(resMsg.getMessage(), BaseApp.key);
					if(null != decode){
						ResPublicBean rBean = JSON.parseObject(decode, ResPublicBean.class);
						if(null != rBean && "1".equals(rBean.getSuccess())){
							DialogUtil.showLongToast(EditHeadImgActivity.this, R.string.update_head_sculpture_success);
							Intent mIntent = new Intent();  
							EditHeadImgActivity.this.setResult(RESULT_OK, mIntent); 
						    finish();
						}else{
							DialogUtil.showLongToast(EditHeadImgActivity.this, R.string.update_head_sculpture_failed);
							finish();
						}
					}else{
						DialogUtil.showLongToast(EditHeadImgActivity.this, R.string.update_head_sculpture_failed);
					}
				}else {
					LogUtil.promptInfo(EditHeadImgActivity.this, ErrorCodeContrast.getErrorCode(resMsg.getResCode(), getResources()));
				}
			} else {
				LogUtil.promptInfo(EditHeadImgActivity.this, ErrorCodeContrast.getErrorCode("0", getResources()));
			}
		};
	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
		 .detectDiskReads()
		 .detectDiskWrites()
		 .detectNetwork() // 这里可以替换为detectAll() 就包括了磁盘读写和网络I/O
		 .penaltyLog()  
		 .build());
		 StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
		 .detectLeakedSqlLiteObjects() //探测SQLite数据库操作
		 .penaltyLog() //打印logcat
		 .penaltyDeath()
		 .build()); 
		
		// 去除头部
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_edit_img);
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
	private void initView() {
		tv_title = (TextView) findViewById(R.id.tv_top_title);
		tv_sech = (TextView) findViewById(R.id.tv_sech);
		btn_back = (Button) findViewById(R.id.top_go_back);
		iv_show = (ImageView) findViewById(R.id.iv_show);
		
		tv_title.setText(getResources().getString(R.string.cancel_preview));	// 取消预览
		tv_sech.setText(getResources().getString(R.string.confirm_upload));	// 确认
		
		byte[] b = getIntent().getByteArrayExtra("bitmap");
		bitmap = BitmapFactory.decodeByteArray(b, 0, b.length);
		if (bitmap != null)
		{
			iv_show.setImageBitmap(bitmap);
		}
		
	}
	
	/**
	 * 方法名: initListener() 
	 * 功 能 : 初始化监听器
	 * 参 数 : void 
	 * 返回值: void
	 */
	private void initListener() {
		tv_title.setOnClickListener(this);
		tv_sech.setOnClickListener(this);
		btn_back.setOnClickListener(this);
	}

	/**
	 * @Title: modifyAvatar
	 * @Description: 修改头像
	 * @param void
	 * @return String 返回的数据 
	 * @throws
	 */
	private String modifyAvatar() {
		PublicBean bean = new PublicBean();
		bean.setDeviceId(BaseApp.macAddr);
		bean.setUserAccount(BaseApp.user.getUserId());
		return JSON.toJSONString(bean);
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.top_go_back:	// 返回
			case R.id.tv_top_title:	
				finish();
				overridePendingTransition(R.anim.push_left_in, R.anim.abc_fade_out);
				break;
			case R.id.tv_sech:
				try {
					DialogUtil.showWithCancelProgressDialog(EditHeadImgActivity.this, null, getString(R.string.global_prompt_message), null);
					new Thread(new Runnable() {
						@Override
						public void run() {
							sendQuest();
						}
					}).start();
				} catch (Exception e) {
					e.printStackTrace();
				}
				break;
		}
	}	
	
	/** 使用系统当前日期加以调整作为照片的名称*/
	private String getPhotoFileName() {
		Date date = new Date(System.currentTimeMillis());
		SimpleDateFormat dateFormat = new SimpleDateFormat("'IMG'_yyyyMMdd_HHmmss");
		return dateFormat.format(date) + ".jpg";
	}
	/**
	 * @Title: sendQuest
	 * @Description: 请求
	 * @param void
	 * @return void
	 * @throws
	 */
	private void sendQuest(){
		if (bitmap != null)
		{
			File file = saveBitmap(bitmap);
			if(null != file && file.exists()){
				String params = modifyAvatar();
				// 请求远程服务器的方法
				String urls = "http://"+HttpConstants.WEBSERVICE_URL + UrlStrController.formatUrlStr("", LocalConstants.PERSONAL_CENTER_MODIFY_AVATAR_SERVER);
				// 将数据加密
				String encodeData = EncryptUtil.getEncodeData(params, BaseApp.key);
				// http请求参数
				HTTPOutput entity = new HTTPOutput();
				entity.setMessage(encodeData);
				entity.setToken(BaseApp.token);
				entity.setDeviceType("1");
				entity.setReqLang(BaseApp.reqLang);
				entity.setUrl(urls);
				
				String json = JSON.toJSONString(entity);
				try {
					UploadFileUtil.uploadForm(json.replaceAll(" ", "::"), "uploadFile", file, file.getName(), urls, mHandler);
				} catch (IOException e) {
					e.printStackTrace();
					mHandler.sendEmptyMessage(1);
				}
			}
		}
	}
	
	/** 保存方法 */
	public File saveBitmap(Bitmap bm) {
		
		File file = new File(LocalConstants.LOCAL_FILE_PATH);
		if (!file.exists()) {
			file.mkdir();
		}
		
		File f = new File(LocalConstants.LOCAL_FILE_PATH, getPhotoFileName());
	    if (f.exists()) {
	    	f.delete();
	    }
	    
	   try {
		   FileOutputStream out = new FileOutputStream(f);
		   bm.compress(Bitmap.CompressFormat.PNG, 100, out);
		   out.flush();
		   out.close();
	   } catch (FileNotFoundException e) {
		   e.printStackTrace();
	   } catch (IOException e) {
		   e.printStackTrace();
	   }
	   
	   return f;
	}
}
