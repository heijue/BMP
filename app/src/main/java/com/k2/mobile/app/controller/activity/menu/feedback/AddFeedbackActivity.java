package com.k2.mobile.app.controller.activity.menu.feedback;

import java.io.File;
import java.io.Serializable;
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
import com.k2.mobile.app.common.config.IntentConstant;
import com.k2.mobile.app.common.config.LocalConstants;
import com.k2.mobile.app.common.exception.HttpException;
import com.k2.mobile.app.controller.activity.menu.personalCenter.PersonalCenterActivity;
import com.k2.mobile.app.controller.core.BaseApp;
import com.k2.mobile.app.model.adapter.FbGridViewAdapter;
import com.k2.mobile.app.model.album.ImageItem;
import com.k2.mobile.app.model.bean.CategoryBean;
import com.k2.mobile.app.model.bean.FeedbackBean;
import com.k2.mobile.app.model.bean.FeedbackListBean;
import com.k2.mobile.app.model.bean.FileBean;
import com.k2.mobile.app.model.bean.PublicBean;
import com.k2.mobile.app.model.bean.ReqMessage;
import com.k2.mobile.app.model.bean.ResPublicBean;
import com.k2.mobile.app.model.bean.SpinnerBean;
import com.k2.mobile.app.model.event.SelectEvent;
import com.k2.mobile.app.model.http.ResponseInfo;
import com.k2.mobile.app.model.http.callback.RequestCallBack;
import com.k2.mobile.app.model.http.other.SendRequest;
import com.k2.mobile.app.utils.DialogUtil;
import com.k2.mobile.app.utils.EncryptUtil;
import com.k2.mobile.app.utils.ErrorCodeContrast;
import com.k2.mobile.app.utils.FastJSONUtil;
import com.k2.mobile.app.utils.LogUtil;
import com.k2.mobile.app.utils.NetWorkUtil;
import com.k2.mobile.app.utils.UploadBatchFileUtil;
import com.k2.mobile.app.utils.UploadFileUtil;
import com.k2.mobile.app.view.widget.PopwinPerCenter;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.v4.app.FragmentActivity;
import android.view.ContextThemeWrapper;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import de.greenrobot.event.EventBus;

/**
 * 
 * @ClassName: AddFeedbackActivity
 * @Description: 添加问题反馈
 * @author linqijun
 * @date 2015-6-08 14:24:00
 * 
 */
@SuppressLint("NewApi")
public class AddFeedbackActivity extends FragmentActivity 
implements OnClickListener,OnItemClickListener,OnItemLongClickListener{

	// 用来标识请求照相功能
	private static final int CAMERA_WITH_DATA = 100;
	// 用来标识请求gallery
	private static final int PHOTO_PICKED_WITH_DATA = 110; // 4.4以下版本
	private static final int SELECT_PIC_KITKAT = 111; // 4.4版本
	// 返回
	private Button btn_back;
	//	private Button btn_photo;
	// 头部标题
	private TextView tv_title;
	private TextView tv_search;
	private Spinner sp_class;
	private EditText ed_abstract;
	private EditText ed_description;
	private EditText ed_phone;
	//	private LinearLayout ll_container;
	//	private ViewPager vp_view;
	private Button btn_submit;
	private Button btn_cancel;
	// 自定义的弹出框类
	private PopwinPerCenter mPopwinPerCenter;
	// 操作类别 1，查询类别 2，保存数据, 3,编辑
	private int operType = 1;
	// 存放类别
	private List<FeedbackBean> fbList = null;
	private List<View> views = new ArrayList<View>();
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
	private String sourceCode = null;

	private String flag = "0";
	private Intent mIntent = null;

	private List<CategoryBean> sbList = null;
	private List<FileBean> fileList = null;
	//选择图片的gridview
	private GridView gridview;
	//装在图片数据
	private List<ImageItem> imageLists = new ArrayList<ImageItem>();
	//gridview适配器
	private FbGridViewAdapter mAdapter;

	private Handler mHandler = new Handler() {
		@Override
		public void dispatchMessage(Message msg) {
			switch (msg.what) {
			case 1:
				String resJson = (String) msg.obj;
				if (null != resJson && !"".equals(resJson.trim())) {
					if (null == fbList) {
						fbList = new ArrayList<FeedbackBean>();
					}
					fbList = JSON.parseArray(resJson, FeedbackBean.class);
					if (null != fbList) {
						List<SpinnerBean> strList = new ArrayList<SpinnerBean>();
						for (int i = 0; i < fbList.size(); i++) {
							SpinnerBean sBean = new SpinnerBean(fbList.get(i).getId(), fbList.get(i).getQuestionCategoryName());
							strList.add(sBean);
						}
						setAdapter(strList,null);
					}
				}
				break;
			case 2:
				String resSave = (String) msg.obj;
				if (null != resSave) {
					ResPublicBean bean = JSON.parseObject(resSave, ResPublicBean.class);
					if (null != bean && "1".equals(bean.getSuccess())) {
						if(0 == isSubmit && 0 == isCheck){
							DialogUtil.showLongToast(AddFeedbackActivity.this, R.string.global_add_new_status_success);
						}else if(1 == isSubmit){
							DialogUtil.showLongToast(AddFeedbackActivity.this, R.string.submittal_succeed);
						}else if(0 == isSubmit && 1 == isCheck){
							DialogUtil.showLongToast(AddFeedbackActivity.this, R.string.edit_success);
						}

						mIntent = new Intent(BroadcastNotice.FEEDBACK_FINISH_UPDATE);
						AddFeedbackActivity.this.sendBroadcast(mIntent);
						finish();
					} else {
						if(0 == isSubmit && 0 == isCheck){
							DialogUtil.showLongToast(AddFeedbackActivity.this, R.string.global_add_new_status_failed);	
						}else if(1 == isSubmit){
							DialogUtil.showLongToast(AddFeedbackActivity.this, R.string.submittal_failure);
						}else if(0 == isSubmit && 1 == isCheck){
							DialogUtil.showLongToast(AddFeedbackActivity.this, R.string.edit_failed);
						}
					}
				}
				break;
			case 3:
				String resed = (String) msg.obj;
				if (null != resed && !"".equals(resed.trim())) {
					FeedbackListBean bean = JSON.parseObject(resed, FeedbackListBean.class);
					if (null != bean) {
						if (null != bean.getQuestionCategorylist()) {
							sbList = JSON.parseArray(bean.getQuestionCategorylist(),CategoryBean.class);
							List<SpinnerBean> strList = new ArrayList<SpinnerBean>();
							for (int i = 0; i < sbList.size(); i++) {
								SpinnerBean sBean = new SpinnerBean(sbList.get(i).getId(), sbList.get(i).getQuestionCategoryName());
								strList.add(sBean);
							}
							setAdapter(strList, bean.getQuestionCategoryID());
						}
						if (null != bean.getAttachmentList()) {
							fileList = JSON.parseArray(bean.getAttachmentList(), FileBean.class);
							
							if (null != fileList) {
								new Thread(new Runnable() {
									@Override
									public void run() {
										for (int i = 0; i < fileList.size(); i++) {
											try {
												byte[] imageArray = UploadFileUtil.getByteFromUrl(fileList.get(i).getFilePath());
												String fileName = i + getPhotoFileName();
												String imagePath = UploadFileUtil.svaeFile(imageArray,LocalConstants.LOCAL_FILE_PATH,fileName);
												if(imagePath != null) {
													ImageItem imageItem = new ImageItem();
													imageItem.setImagePath(imagePath);
													imageLists.add(imageItem);
													mAdapter.setData(imageLists);
												}
											} catch (Exception e) {
												e.printStackTrace();
											}
										}
									}
								}).start();
							}
						}

						ed_abstract.setText(bean.getQuestionAbstract());
						ed_description.setText(bean.getQuestionDescription());
						if(null != bean.getCreatorPhoneNum()){
							ed_phone.setText(bean.getCreatorPhoneNum());
						}else{
							ed_phone.setText("");
						}
					}
				}
				break;
			case 4:
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
					requestServer(LocalConstants.FEEDBACK_OPER_SERVER, info);
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
		setContentView(R.layout.activity_add_feedbcak);
		initView();
		initListener();
		logic();
		BaseApp.addActivity(this);
		//注册
		EventBus.getDefault().register(this);
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
		//		btn_photo = (Button) findViewById(R.id.btn_photo);
		sp_class = (Spinner) findViewById(R.id.sp_class);

		ed_abstract = (EditText) findViewById(R.id.ed_abstract);
		ed_description = (EditText) findViewById(R.id.ed_description);
		ed_phone = (EditText) findViewById(R.id.ed_phone);
		//		ll_container = (LinearLayout) findViewById(R.id.ll_container);
		//		vp_view = (ViewPager) findViewById(R.id.vp_view);
		btn_submit = (Button) findViewById(R.id.btn_submit);
		btn_cancel = (Button) findViewById(R.id.btn_cancel);

		gridview = (GridView)findViewById(R.id.gridview);
		mAdapter = new FbGridViewAdapter(this);
		mAdapter.setData(imageLists);
		gridview.setOnItemClickListener(this);
		gridview.setOnItemLongClickListener(this);
		gridview.setAdapter(mAdapter);
	}

	/**
	 * 方法名: initListener() 
	 * 功 能 : 初始化 监听器 
	 * 参 数 : void 
	 * 返回值: void
	 */
	private void initListener() {
		btn_back.setOnClickListener(this);
		//		btn_photo.setOnClickListener(this);
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
		tv_title.setText(getString(R.string.add_feedback));
		tv_search.setVisibility(View.GONE);
		code = getIntent().getStringExtra("code");
		if (null != code && !"".equals(code.trim())) {
			operType = 3;
			isCheck = 1;
			String info = receiveMailQuest_ed();
			if (null != info && !"".equals(info)) {
				requestServer(LocalConstants.FEEDBACK_INFO_SERVER, info);
			}
		} else {
			ed_phone.setText(BaseApp.user.getMobilePhone());
			String info = receiveMailQuest();
			requestServer(LocalConstants.FEEDBACK_CLASS_SERVER, info);
		}
	}

	/**
	 * 方法名: setAdapter() 
	 * 功 能 : 设置下拉列框 
	 * 参 数 : list 数据集 
	 * 返回值: void
	 */
	private void setAdapter(List<SpinnerBean> list,String questionCategoryID) {
		ArrayAdapter<SpinnerBean> adapter = new ArrayAdapter<SpinnerBean>(this, R.layout.spinner_user_check, list);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		sp_class.setAdapter(adapter);
		if(questionCategoryID != null) {
			for(int i=0; i<list.size();i++) {
				if(list.get(i).getValue().equals(questionCategoryID)) {
					sp_class.setSelection(i, true);
				}
			}
		}
	}

	/** 为弹出窗口实现监听类 */
	private OnClickListener itemsOnClick = new OnClickListener() {
		@Override
		public void onClick(View v) {
			mPopwinPerCenter.dismiss();
			switch (v.getId()) {
			case R.id.btn_take_photo: // 拍照
				if(imageLists.size() > 4)return;
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
	private String categoryId;
	private String categoryName;
	private String summary;
	private String desc;
	private String phone;

	/**
	 * 方法名: chooseImage() 
	 * 功 能 : 从图库选择图片 
	 * 参 数 : void 
	 * 返回值: void
	 */
	public void chooseImage() {
		if(imageLists.size() > 4) {
			Toast.makeText(this, getString(R.string.max_check_five_photo), Toast.LENGTH_SHORT).show();
		} else {
			Intent intent = new Intent(this,PickPhotoActivity.class);
			intent.putExtra(IntentConstant.EXTRA_IMAGE_LIST_COUNT,5-imageLists.size());
			startActivityForResult(intent, PHOTO_PICKED_WITH_DATA);
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
				DialogUtil.showLongToast(AddFeedbackActivity.this, R.string.upload_up_five);
				break;
			}
			// 实例化SelectPicPopupWindow
			mPopwinPerCenter = new PopwinPerCenter(this, itemsOnClick);
			// 显示窗口并设置layout在PopupWindow中显示的位置
			mPopwinPerCenter.showAtLocation(this.findViewById(R.id.rl_show), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
			break;
			//暂存
		case R.id.btn_submit:
			isSubmit = 0;
			operType = 2;
			imageHandle();
			break;
			//送审
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
	 * @param fileName
	 *            ：文件名
	 * @return 文件类型
	 */
	private String getFileType(String fileName) {
		return fileName.substring(fileName.lastIndexOf("."), fileName.length());
	}

	/**
	 * 处理图片
	 * @param
	 * @return
	 */
	private void imageHandle() {
		
		categoryId = ((SpinnerBean) sp_class.getSelectedItem()).getValue();
		categoryName = ((SpinnerBean) sp_class.getSelectedItem()).getText();
		summary = ed_abstract.getText().toString();
		desc = ed_description.getText().toString();
		phone = ed_phone.getText().toString();
		
		if (null == summary || "".equals(summary.trim())) {
			DialogUtil.showLongToast(AddFeedbackActivity.this, R.string.question_summary_null);
			return;
		} else if (null == desc || "".equals(desc.trim())) {
			DialogUtil.showLongToast(AddFeedbackActivity.this, R.string.question_description_null);
			return;
		} else if (null == phone || "".equals(phone.trim())) {
			DialogUtil.showLongToast(AddFeedbackActivity.this, R.string.phone_null);
			return;
		}
		
		//取出ImagePath转为文件集合;
		lList = new ArrayList<File>();
		for(ImageItem imageItem : imageLists) {
			File file = new File(imageItem.getImagePath());
			lList.add(file);
		}
		//图片类型
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

		DialogUtil.showWithCancelProgressDialog(AddFeedbackActivity.this, null, getResources().getString(R.string.global_prompt_message), null);

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
		return JSON.toJSONString(bean);
	}

	/**
	 * 方法名: receiveMailQuest() 
	 * 功 能 : 请求报文 
	 * 参 数 : void 
	 * 返回值: void
	 */
	private String receiveMailQuest_ed() {
		PublicBean bean = new PublicBean();
		bean.setDeviceId(BaseApp.macAddr);
		bean.setUserAccount(BaseApp.user.getUserId());
		bean.setFlag(flag);
		bean.setCode(code);
		return JSON.toJSONString(bean);
	}

	/**
	 * 方法名: receiveMailQuestAE() 
	 * 功 能 : 添加或修改请求报文 
	 * 参 数 : void 
	 * 返回值: void
	 */
	private String receiveMailQuestAE(List<FileBean> flBeaen) {
		PublicBean bean = new PublicBean();

		bean.setDeviceId(BaseApp.macAddr);
		bean.setUserAccount(BaseApp.user.getUserId());
		bean.setUserName(BaseApp.user.getUserName());
		bean.setCode(code);
		bean.setUserDepart(BaseApp.user.getRealityOrgName());
		bean.setUserPhone(phone);
		bean.setCategoryId(categoryId);
		bean.setCategoryName(categoryName);
		bean.setSummary(summary);
		bean.setDesc(desc);
		bean.setIsSubmit(isSubmit + "");
		bean.setSourceCode(sourceCode);
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
		if (!NetWorkUtil.isNetworkAvailable(AddFeedbackActivity.this)) {
			DialogUtil.showLongToast(AddFeedbackActivity.this,
					R.string.global_network_disconnected);
		} else {
			SendRequest.sendSubmitRequest(AddFeedbackActivity.this, info, BaseApp.token, BaseApp.reqLang, server, BaseApp.key, submitCallBack);
		}
	}

	/**
	 * http请求回调
	 */
	RequestCallBack<String> submitCallBack = new RequestCallBack<String>() {

		@Override
		public void onStart() {
			if (1 == operType) {
				DialogUtil.showWithCancelProgressDialog(AddFeedbackActivity.this, null, getResources().getString(R.string.global_prompt_message), null);
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
					LogUtil.promptInfo(AddFeedbackActivity.this, ErrorCodeContrast.getErrorCode("0", getResources()));
					return;
					// 验证不合法
				} else if ("1103".equals(msg.getResCode()) || "1104".equals(msg.getResCode())) {
					LogUtil.promptInfo(AddFeedbackActivity.this, ErrorCodeContrast.getErrorCode(msg.getResCode(), getResources()));
					Intent mIntent = new Intent(BroadcastNotice.USER_EXIT);
					AddFeedbackActivity.this.sendBroadcast(mIntent);
					return;
				} else if("1210".equals(msg.getResCode())){
					LogUtil.promptInfo(AddFeedbackActivity.this, ErrorCodeContrast.getErrorCode(msg.getResCode(), getResources()));	
					Intent mIntent = new Intent(BroadcastNotice.WIPE_USET);
					sendBroadcast(mIntent);
					return;
				} else if (!"1".equals(msg.getResCode())) {
					LogUtil.promptInfo(AddFeedbackActivity.this, ErrorCodeContrast.getErrorCode(msg.getResCode(), getResources()));
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
				LogUtil.promptInfo(AddFeedbackActivity.this, ErrorCodeContrast.getErrorCode("0", getResources()));
			}
		}

		@Override
		public void onFailure(HttpException error, String msg) {
			DialogUtil.closeDialog();

			if (null != msg && msg.equals(LocalConstants.API_KEY)) {
				LogUtil.promptInfo(AddFeedbackActivity.this, ErrorCodeContrast.getErrorCode("1207", getResources()));
			} else {
				LogUtil.promptInfo(AddFeedbackActivity.this, ErrorCodeContrast.getErrorCode("0", getResources()));
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
			ImageItem imageItem = new ImageItem();
			imageItem.setImagePath(mPhotoPath);
			List<ImageItem> itemLists = new ArrayList<ImageItem>();
			itemLists.add(imageItem);
			handleImagePickData(itemLists);
			break;
		case PHOTO_PICKED_WITH_DATA: // 从相册获取成功
			setIntent(data);
			break;
		}
	};

	@Override
	protected void onDestroy() {
		EventBus.getDefault().unregister(this);
		super.onDestroy();
	}

	/**
	 * 选择图片返回的订阅
	 * @param event
	 */
	public void onEventMainThread(SelectEvent event) {
		SelectEvent.Event type = event.getEvent();
		List<ImageItem> itemList = event.getList();
		if (itemList == null || itemList.size() <= 0) {
			return;
		}
		switch (type) {
		case SELECT_PHOTO_LIST:
			handleImagePickData(itemList);
			break;
		}
	}

	/**
	 * 处理选择返回的图片
	 */
	private void handleImagePickData(List<ImageItem> itemList) {
		List<ImageItem> mLists = new ArrayList<ImageItem>();
		mLists.addAll(imageLists);
		mLists.addAll(itemList);
		imageLists = mLists;
		handlerGridViewHeight();
		mAdapter.setData(imageLists);
	}
	
	//处理gridview的高度
	private void handlerGridViewHeight() {
		LayoutParams params = gridview.getLayoutParams();
//		if (imageLists.size() > 4) {
//			params.height = maxHeightGV;
//			gridview.setLayoutParams(params);
//		} else if(imageLists.size() < 4) {
//			params.height = minHeightGV;
//			gridview.setLayoutParams(params);
//		}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		if(position == imageLists.size()) {
			if (imageLists.size() > 4) {
				DialogUtil.showLongToast(this, R.string.upload_up_five);
				return;
			}
			// 实例化SelectPicPopupWindow
			mPopwinPerCenter = new PopwinPerCenter(this, itemsOnClick);
			// 显示窗口并设置layout在PopupWindow中显示的位置
			mPopwinPerCenter.showAtLocation(this.findViewById(R.id.rl_show), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
		} else {
			//预览图片
			previewPhoto(position);
		}
	}


	@Override
	public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
		if(position != imageLists.size()) {
			showDeleDialog(position);
		}
		return true;
	}
	
	/**
	 * 删除提示框
	 */
	private void showDeleDialog(final int position) {
		AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(this,
                android.R.style.Theme_Holo_Light_Dialog));

        LayoutInflater inflater = (LayoutInflater)this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View dialog_view = inflater.inflate(R.layout.tt_custom_dialog, null);
        TextView textText = (TextView)dialog_view.findViewById(R.id.dialog_title);
        textText.setText(R.string.del_dialog_tip);
        builder.setView(dialog_view);
        builder.setPositiveButton(getString(R.string.confim), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            	imageLists.remove(position);
            	handlerGridViewHeight();
            	mAdapter.setData(imageLists);
            }
        });
        builder.setNegativeButton(getString(R.string.cancle), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        });
        final AlertDialog alertDialog = builder.create();
        alertDialog.show();
	}

	/**
	 * 图片预览
	 */
	private void previewPhoto(int position) {
		List<FileBean> lists = new ArrayList<FileBean>();
		for(ImageItem imageItem : imageLists) {
			FileBean fileBean = new FileBean();
			fileBean.setFilePath(imageItem.getImagePath());
			lists.add(fileBean);
		}
		Intent intent = new Intent(this,ShowImgActivity.class);
		intent.putExtra(IntentConstant.EXTRA_IMAGE_LIST_SHOWIMG, (Serializable)lists);
		intent.putExtra(IntentConstant.EXTRA_IMAGE_LIST_SHOWIMG_POSITION, position);
		intent.putExtra(IntentConstant.EXTRA_IMAGE_LIST_LOCAL_FLAG, true);
		startActivity(intent);
	}

}