package com.k2.mobile.app.controller.activity.menu.dormitory;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.k2.mobile.app.R;
import com.k2.mobile.app.common.config.BroadcastNotice;
import com.k2.mobile.app.common.config.HttpConstants;
import com.k2.mobile.app.common.config.LocalConstants;
import com.k2.mobile.app.common.exception.HttpException;
import com.k2.mobile.app.controller.activity.menu.personalCenter.PersonalCenterActivity;
import com.k2.mobile.app.controller.core.BaseApp;
import com.k2.mobile.app.model.bean.DormRepairDetailBean;
import com.k2.mobile.app.model.bean.FeedbackBean;
import com.k2.mobile.app.model.bean.FileBean;
import com.k2.mobile.app.model.bean.PublicBean;
import com.k2.mobile.app.model.bean.ReqMessage;
import com.k2.mobile.app.model.bean.ResPublicBean;
import com.k2.mobile.app.model.http.ResponseInfo;
import com.k2.mobile.app.model.http.callback.RequestCallBack;
import com.k2.mobile.app.model.http.other.SendRequest;
import com.k2.mobile.app.utils.DialogUtil;
import com.k2.mobile.app.utils.EncryptUtil;
import com.k2.mobile.app.utils.ErrorCodeContrast;
import com.k2.mobile.app.utils.FastJSONUtil;
import com.k2.mobile.app.utils.ImageUtil;
import com.k2.mobile.app.utils.LogUtil;
import com.k2.mobile.app.utils.NetWorkUtil;
import com.k2.mobile.app.utils.UploadBatchFileUtil;
import com.k2.mobile.app.utils.UploadFileUtil;
import com.k2.mobile.app.view.widget.PopwinPerCenter;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * 
 * @ClassName: AddDormitoryActivity
 * @Description: 添加宿舍报修
 * @author linqijun
 * @date 2015-6-08 14:24:00
 * 
 */
@SuppressLint("NewApi")
public class AddDormitoryActivity extends FragmentActivity implements OnClickListener {

	// 用来标识请求照相功能
	private static final int CAMERA_WITH_DATA = 100;
	// 用来标识请求gallery
	private static final int PHOTO_PICKED_WITH_DATA = 110; // 4.4以下版本
	private static final int SELECT_PIC_KITKAT = 111; // 4.4版本
	/*
	 * 开发文件服务器：172.16.101.49 测试文件服务器：172.16.101.43 生产环境文件服务器： 流程,邮件
	 * 172.16.100.19 图片：172.16.100.10
	 */
	// 返回
	private Button btn_back;
	private Button btn_photo;
	// 头部标题
	private TextView tv_title, tv_search, tv_number, tv_user_name, tv_department;
	private EditText ed_repair_items;
	private EditText ed_description;
	private EditText ed_phone, ed_repair_addr;
	private LinearLayout ll_container;
	private ViewPager vp_view;
	private Button btn_submit;
	private Button btn_cancel;
	// 自定义的弹出框类
	private PopwinPerCenter mPopwinPerCenter;
	// 操作类别 1，查询类别 2，保存数据, 3,编辑
	private int operType = 1;
	private List<View> views = new ArrayList<View>();
	// 图片滑动
	private MyAdapter myAdapter = null;
	// 文件
	private File tempFile;
	// 临时路径
	private Uri tempuri;
	private Bitmap bitmap;
	private String mPhotoPath = null;
	private ArrayList<File> lList = new ArrayList<File>();
	// 0：保存，1送审
	private int isSubmit = 0;
	// 0:正常添加，1为编辑
	private int isCheck = 0;
	private String code = null;

	private Intent mIntent = null;

	private Handler mHandler = new Handler() {
		@Override
		public void dispatchMessage(Message msg) {
			switch (msg.what) {
			case 1:
				String resJson = (String) msg.obj;
				if(null != resJson && !"".equals(resJson)){
					final DormRepairDetailBean dBean = JSON.parseObject(resJson, DormRepairDetailBean.class);
					if(null != dBean){
						tv_number.setText(dBean.getRepairUserCode());
						tv_user_name.setText(dBean.getRepairUserName());
						tv_department.setText(dBean.getRepairUserOrgName());
						ed_phone.setText(dBean.getRepairUserMobilePhone());
						ed_repair_items.setText(dBean.getRepairItems());
						ed_repair_addr.setText(dBean.getRepairAddress());
						ed_description.setText(dBean.getRepairreason());
						if(null != dBean.getAttachmentList()){
							new Thread(new Runnable() {
								@Override
								public void run() {
									for (int i = 0; i < dBean.getAttachmentList().size(); i++) {
										try {
											Bitmap bitemp = UploadFileUtil.loadImageFromUrl(dBean.getAttachmentList().get(i).getFilePath());
											Message msg = new Message();
											msg.what = 3;
											msg.obj = bitemp;
											mHandler.sendMessage(msg);
										} catch (Exception e) {
											e.printStackTrace();
										}
									}
								}
							}).start();
						}
					}
				}
				break;
			case 2:
				String resSave = (String) msg.obj;
				if (null != resSave) {
					ResPublicBean bean = JSON.parseObject(resSave, ResPublicBean.class);
					if (null != bean && "1".equals(bean.getSuccess())) {
						if(0 == isSubmit && 0 == isCheck){
							DialogUtil.showLongToast(AddDormitoryActivity.this, R.string.global_add_new_status_success);
						}else if(1 == isSubmit){
							DialogUtil.showLongToast(AddDormitoryActivity.this, R.string.sumit_ok);
						}else if(0 == isSubmit && 1 == isCheck){
							DialogUtil.showLongToast(AddDormitoryActivity.this, R.string.edit_success);
						}
						
						mIntent = new Intent(BroadcastNotice.DORMITORY_FINISH_UPDATE_TREATED);
						AddDormitoryActivity.this.sendBroadcast(mIntent);
						finish();
					} else {
						if(0 == isSubmit && 0 == isCheck){
							DialogUtil.showLongToast(AddDormitoryActivity.this, R.string.global_add_new_status_failed);	
						}else if(1 == isSubmit){
							DialogUtil.showLongToast(AddDormitoryActivity.this, R.string.submit_failure);
						}else if(0 == isSubmit && 1 == isCheck){
							DialogUtil.showLongToast(AddDormitoryActivity.this, R.string.edit_failed);
						}
					}
				}
				break;
			case 3:
				Bitmap bitemp = (Bitmap) msg.obj;
				setImag(bitemp);
				myAdapter.notifyDataSetChanged();
				break;
			case 999:
				String resFileCode = (String) msg.obj;
				FeedbackBean fileBean = null;
				if (null != resFileCode && !"".equals(resFileCode.trim())) {
					fileBean = JSON.parseObject(resFileCode, FeedbackBean.class);
				}

				String info = null;
				if (null != fileBean && null != fileBean.getData()) {
					info = receiveMailQuestAE(fileBean.getData());
				} else {
					info = receiveMailQuestAE(null);
				}
				 
				if (null != info && !"".equals(info.trim())) {
					requestServer(LocalConstants.DORMITORY_REPAIR_ADD_SERVER, info);
				} else {
					DialogUtil.closeDialog();
				}

				break;
			}
		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().detectDiskReads().detectDiskWrites().detectNetwork() // 这里可以替换为detectAll()
				.penaltyLog().build());
		StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder().detectLeakedSqlLiteObjects() // 探测SQLite数据库操作
				.penaltyLog() // 打印logcat
				.penaltyDeath().build());
		// 去除头部
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_add_dorm_repair);
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
		tv_title = (TextView) findViewById(R.id.tv_title);
		tv_search = (TextView) findViewById(R.id.tv_sech);
		btn_back = (Button) findViewById(R.id.btn_back);
		btn_photo = (Button) findViewById(R.id.btn_photo);
		tv_number = (TextView) findViewById(R.id.tv_number);
		tv_user_name = (TextView) findViewById(R.id.tv_user_name);
		tv_department = (TextView) findViewById(R.id.tv_department);
		ed_repair_items = (EditText) findViewById(R.id.ed_repair_items);
		ed_description = (EditText) findViewById(R.id.ed_description);
		ed_phone = (EditText) findViewById(R.id.ed_phone);
		ed_repair_addr = (EditText) findViewById(R.id.ed_repair_addr);
		ll_container = (LinearLayout) findViewById(R.id.ll_container);
		vp_view = (ViewPager) findViewById(R.id.vp_view);
		btn_submit = (Button) findViewById(R.id.btn_submit);
		btn_cancel = (Button) findViewById(R.id.btn_cancel);
	}

	/**
	 * 方法名: initListener() 
	 * 功 能 : 初始化 监听器 
	 * 参 数 : void 
	 * 返回值: void
	 */
	private void initListener() {
		btn_back.setOnClickListener(this);
		btn_photo.setOnClickListener(this);
		tv_search.setOnClickListener(this);
		btn_submit.setOnClickListener(this);
		btn_cancel.setOnClickListener(this);
	}

	/**
	 * 方法名: logic() 
	 * 功 能 : 业务逻辑 
	 * 参 数 : void 
	 * 返回值: void
	 */
	private void logic() {
		code = getIntent().getStringExtra("code");	
		
		tv_search.setVisibility(View.GONE);
		tv_number.setText(BaseApp.user.getUserAccount());
		tv_user_name.setText(BaseApp.user.getUserName());
		tv_department.setText(BaseApp.user.getRealityOrgName());
		ed_phone.setText(BaseApp.user.getMobilePhone());
		// 设置幕后item的缓存数目
		vp_view.setOffscreenPageLimit(5);
		// 设置页与页之间的间距
		vp_view.setPageMargin(30);
		// 将父类的touch事件分发至viewPgaer，否则只能滑动中间的一个view对象
		ll_container.setOnTouchListener(new View.OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				return vp_view.dispatchTouchEvent(event);
			}
		});
		 
		if (null != code && !"".equals(code.trim())) {
			tv_title.setText(getString(R.string.edit_repair));
			String info = receiveMailQuest();
			if (null != info && !"".equals(info)) {
				requestServer(LocalConstants.DORMITORY_REPAIR_DETAIL_SERVER, info);
			}
		} else {
			tv_title.setText(getString(R.string.add_repair));
		}

		myAdapter = new MyAdapter();
		vp_view.setAdapter(myAdapter);
	}

	/**
	 * 方法名: setImag() 
	 * 功 能 : 加载图片 
	 * 参 数 : bitmap 位图 
	 * 返回值: void
	 */
	private void setImag(Bitmap bitmap) {
		if (null == bitmap) {
			return;
		}

		if (4 == ll_container.getVisibility() || 8 == ll_container.getVisibility()) {
			ll_container.setVisibility(View.VISIBLE);
		}

		mPhotoPath = LocalConstants.LOCAL_FILE_PATH + "/" + getPhotoFileName();
		File ffile = new File(mPhotoPath);
		try {
			ImageUtil.saveBitmap(ffile, bitmap, 2048);
		} catch (IOException e) {
			e.printStackTrace();
		}

		lList.add(ffile);

		ImageView iv = new ImageView(this);
		iv.setImageBitmap(bitmap);
		iv.setScaleType(ImageView.ScaleType.FIT_XY);
		views.add(iv);
	}

	/** 为弹出窗口实现监听类 */
	private OnClickListener itemsOnClick = new OnClickListener() {
		@Override
		public void onClick(View v) {
			mPopwinPerCenter.dismiss();
			switch (v.getId()) {
			case R.id.btn_take_photo: // 拍照
				Intent intent_tp = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
				// 相机拍照后的照片存储的路径
				if (!PersonalCenterActivity.PHOTO_DIR.exists()) {
					PersonalCenterActivity.PHOTO_DIR.mkdirs(); // 创建照片的存储目录
				}
				mPhotoPath = PersonalCenterActivity.PHOTO_DIR + "/" + getPhotoFileName();
				tempFile = new File(mPhotoPath);
				tempuri = Uri.fromFile(tempFile);
				intent_tp.putExtra(MediaStore.EXTRA_OUTPUT, tempuri);
				startActivityForResult(intent_tp, CAMERA_WITH_DATA);
				break;
			case R.id.btn_pick_photo: // 相册
				chooseImage();
				break;
			}
		}
	};

	/**
	 * 方法名: chooseImage() 
	 * 功 能 : 从图库选择图片 
	 * 参 数 : void 
	 * 返回值: void
	 */
	public void chooseImage() {
		if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
			Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
			intent.addCategory(Intent.CATEGORY_OPENABLE);
			intent.setType("image/*");
			startActivityForResult(intent, SELECT_PIC_KITKAT); // 4.4版本
		} else {
			Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
			intent.addCategory(Intent.CATEGORY_OPENABLE);
			intent.setType("image/*");
			startActivityForResult(intent, PHOTO_PICKED_WITH_DATA); // 4.4以下版本
		}
	}

	/** 使用系统当前日期加以调整作为照片的名称 */
	private String getPhotoFileName() {
		Date date = new Date(System.currentTimeMillis());
		SimpleDateFormat dateFormat = new SimpleDateFormat("'IMG'_yyyyMMdd_HHmmss");
		return dateFormat.format(date) + ".jpg";
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_back:
			this.finish();
			break;
		case R.id.btn_photo:
			if (views.size() > 4) {
				DialogUtil.showLongToast(AddDormitoryActivity.this, R.string.upload_up_five);
				break;
			}
			// 实例化SelectPicPopupWindow
			mPopwinPerCenter = new PopwinPerCenter(this, itemsOnClick);
			// 显示窗口并设置layout在PopupWindow中显示的位置
			mPopwinPerCenter.showAtLocation(this.findViewById(R.id.rl_show), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
			break;
		case R.id.btn_submit:
			isSubmit = 0;
			operType = 2;
			imageHandle();
			break;
		case R.id.btn_cancel:
			isSubmit = 1;
			operType = 2;
			imageHandle();
			break;
		}
	}

	/**
	 * 获取文件的类型
	 * 
	 * @param fileName：文件名
	 * @return 文件类型
	 */
	private String getFileType(String fileName) {
		return fileName.substring(fileName.lastIndexOf("."), fileName.length());
	}

	/**
	 * 处理图片
	 * 
	 * @param
	 * @return
	 */
	private void imageHandle() {
		StringBuffer sbFileTypes = new StringBuffer();
		for (File tempFile : lList) {
			String fileName = tempFile.getName();
			sbFileTypes.append(getFileType(fileName));
		}

		Map<String, String> params = new HashMap<String, String>();
		params.put("fileTypes", sbFileTypes.toString());
		params.put("method", "upload");
		UploadBatchFileUtil uploadService = new UploadBatchFileUtil(mHandler);
		uploadService.uploadFileToServer(HttpConstants.FILE_URL, params, lList);

		DialogUtil.showWithCancelProgressDialog(AddDormitoryActivity.this, null, getResources().getString(R.string.global_prompt_message), null);
	}

	/**
	 * 方法名: receiveMailQuest() 
	 * 功 能 : 请求报文 
	 * 参 数 : void 
	 * 返回值: void
	 */
	private String receiveMailQuest() {
		PublicBean bean = new PublicBean();
		bean.setDeviceId(BaseApp.macAddr);
		bean.setUserAccount(BaseApp.user.getUserId());
		bean.setRepairCode(code);
		return JSON.toJSONString(bean);
	}

	/**
	 * 方法名: receiveMailQuestAE() 
	 * 功 能 : 添加或修改请求报文 
	 * 参 数 : void 
	 * 返回值: void
	 */
	private String receiveMailQuestAE(List<FileBean> flBeaen) {

		String number = tv_number.getText().toString();
		String userName = tv_user_name.getText().toString();
		String department = tv_department.getText().toString();
		String addr = ed_repair_addr.getText().toString();
		String items = ed_repair_items.getText().toString();
		String desc = ed_description.getText().toString();
		String phone = ed_phone.getText().toString();
		
		if (null == phone || "".equals(phone.trim())) {
			DialogUtil.showLongToast(AddDormitoryActivity.this, R.string.phone_null);
			return null;
		} else if (null == items || "".equals(items.trim())) {
			DialogUtil.showLongToast(AddDormitoryActivity.this, R.string.repair_items_null);
			return null;
		} else if (null == addr || "".equals(addr.trim())) {
			DialogUtil.showLongToast(AddDormitoryActivity.this, R.string.repair_addr_null);
			return null;
		} else if (null == desc || "".equals(desc.trim())) {
			DialogUtil.showLongToast(AddDormitoryActivity.this, R.string.question_description_null);
			return null;
		}
		
		PublicBean bean = new PublicBean();
		bean.setDeviceId(BaseApp.macAddr);
		bean.setRepairCode(code);
		bean.setIsSubmit(isSubmit+"");
		bean.setRepairUserCode(number);
		bean.setRepairUserName(userName);
		bean.setRepairUserOrgName(department);
		bean.setRepairUserMobilePhone(phone);
		bean.setRepairItems(items);
		bean.setRepairAddress(addr);
		bean.setRepairReason(desc);
		List<String> arr = new ArrayList<String>();
		if (null != flBeaen) {
			for (int i = 0; i < flBeaen.size(); i++) {
				arr.add(flBeaen.get(i).getFileid());
			}
			bean.setAttachIds(arr);
		}

		return JSON.toJSONString(bean);
	}

	/**
	 * @Title: requestServer
	 * @Description: 发送请求报文
	 * @param void
	 * @return void
	 * @throws
	 */
	private void requestServer(int server, String info) {
		// 判断网络是否连接
		if (!NetWorkUtil.isNetworkAvailable(AddDormitoryActivity.this)) {
			DialogUtil.showLongToast(AddDormitoryActivity.this, R.string.global_network_disconnected);
		} else {
			SendRequest.sendSubmitRequest(AddDormitoryActivity.this, info, BaseApp.token, BaseApp.reqLang, server, BaseApp.key, submitCallBack);
		}
	}

	/**
	 * http请求回调
	 */
	RequestCallBack<String> submitCallBack = new RequestCallBack<String>() {

		@Override
		public void onStart() {
			if (1 == operType) {
				DialogUtil.showWithCancelProgressDialog(AddDormitoryActivity.this, null, getResources().getString(R.string.global_prompt_message), null);
			}
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
					LogUtil.promptInfo(AddDormitoryActivity.this, ErrorCodeContrast.getErrorCode("0", getResources()));
					return;
					// 验证不合法
				} else if ("1103".equals(msg.getResCode()) || "1104".equals(msg.getResCode())) {
					LogUtil.promptInfo(AddDormitoryActivity.this, ErrorCodeContrast.getErrorCode(msg.getResCode(), getResources()));
					Intent mIntent = new Intent(BroadcastNotice.USER_EXIT);
					AddDormitoryActivity.this.sendBroadcast(mIntent);
					return;
				} else if("1210".equals(msg.getResCode())){
					LogUtil.promptInfo(AddDormitoryActivity.this, ErrorCodeContrast.getErrorCode(msg.getResCode(), getResources()));	
					Intent mIntent = new Intent(BroadcastNotice.WIPE_USET);
					sendBroadcast(mIntent);
					return;
				} else if (!"1".equals(msg.getResCode())) {
					LogUtil.promptInfo(AddDormitoryActivity.this, ErrorCodeContrast.getErrorCode(msg.getResCode(), getResources()));
					return;
				}

				// 判断消息体是否为空
				if (null != msg.getMessage() && !"".equals(msg.getMessage().trim())) {
					// 获取解密后并校验后的值
					String decode = EncryptUtil.getDecodeData(msg.getMessage(), BaseApp.key);
					Message msgs = new Message();
					msgs.what = operType;
					msgs.obj = decode;
					mHandler.sendMessage(msgs);
				}
			} else {
				LogUtil.promptInfo(AddDormitoryActivity.this, ErrorCodeContrast.getErrorCode("0", getResources()));
			}
		}

		@Override
		public void onFailure(HttpException error, String msg) {
			DialogUtil.closeDialog();

			if (null != msg && msg.equals(LocalConstants.API_KEY)) {
				LogUtil.promptInfo(AddDormitoryActivity.this, ErrorCodeContrast.getErrorCode("1207", getResources()));
			} else {
				LogUtil.promptInfo(AddDormitoryActivity.this, ErrorCodeContrast.getErrorCode("0", getResources()));
			}
		}
	};

	@Override
	protected void onResume() {
		super.onResume();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode != RESULT_OK) {
			return;
		}

		BitmapFactory.Options opts = new BitmapFactory.Options();
		opts.inSampleSize = 4;

		switch (requestCode) {
		case CAMERA_WITH_DATA: // 拍照成功
			// 广播刷新相册
			Intent intentBc = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
			intentBc.setData(tempuri);
			this.sendBroadcast(intentBc);
			bitmap = BitmapFactory.decodeFile(mPhotoPath, opts);
			setImag(bitmap);
			myAdapter.notifyDataSetChanged();
			break;
		case PHOTO_PICKED_WITH_DATA: // 从相册获取成功
			String imageFilePath = "";
			try {
				if (null == data) {
					break;
				}
				Uri uri = data.getData();
				Cursor cursor = getContentResolver().query(uri, null, null, null, null);
				cursor.moveToFirst();
				imageFilePath = cursor.getString(1);

				bitmap = BitmapFactory.decodeFile(imageFilePath, opts);
				setImag(bitmap);
				myAdapter.notifyDataSetChanged();

				cursor.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
			break;

		case SELECT_PIC_KITKAT: // 4.4版本的处理
			if (null == data) {
				break;
			}

			Uri selectedImage = data.getData();
			String imagePath = ImageUtil.getPath(this, selectedImage); // 获取图片的绝对路径
			try {
				bitmap = BitmapFactory.decodeFile(imagePath, opts);
				setImag(bitmap);
				myAdapter.notifyDataSetChanged();
			} catch (Exception e) {
				e.printStackTrace();
			}

			break;
		}
	};

	// ViewPager适配器
	public class MyAdapter extends PagerAdapter {

		@Override
		public int getCount() {
			return views.size();
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == arg1;
		}

		@Override
		public void destroyItem(View container, int position, Object object) {
			((ViewPager) container).removeView(views.get(position % views.size()));
		}

		/**
		 * 载入图片进去，用当前的position 除以 图片数组长度取余数是关键
		 */
		@Override
		public Object instantiateItem(View container, int position) {
			((ViewPager) container).addView(views.get(position % views.size()), 0);
			return views.get(position % views.size());
		}
	}
}