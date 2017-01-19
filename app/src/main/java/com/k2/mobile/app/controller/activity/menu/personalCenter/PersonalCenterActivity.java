package com.k2.mobile.app.controller.activity.menu.personalCenter;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.alibaba.fastjson.JSON;
import com.k2.mobile.app.R;
import com.k2.mobile.app.common.config.BroadcastNotice;
import com.k2.mobile.app.common.config.LocalConstants;
import com.k2.mobile.app.common.exception.HttpException;
import com.k2.mobile.app.controller.activity.menu.feedback.FeedbackActivity;
import com.k2.mobile.app.controller.core.BaseActivity;
import com.k2.mobile.app.controller.core.BaseApp;
import com.k2.mobile.app.model.bean.PublicBean;
import com.k2.mobile.app.model.bean.ReqMessage;
import com.k2.mobile.app.model.bean.ResPublicBean;
import com.k2.mobile.app.model.bean.User;
import com.k2.mobile.app.model.http.ResponseInfo;
import com.k2.mobile.app.model.http.callback.RequestCallBack;
import com.k2.mobile.app.model.http.other.SendRequest;
import com.k2.mobile.app.utils.DialogUtil;
import com.k2.mobile.app.utils.EncryptUtil;
import com.k2.mobile.app.utils.ErrorCodeContrast;
import com.k2.mobile.app.utils.FastJSONUtil;
import com.k2.mobile.app.utils.ImageLoaderUtil;
import com.k2.mobile.app.utils.ImageUtil;
import com.k2.mobile.app.utils.LogUtil;
import com.k2.mobile.app.utils.NetWorkUtil;
import com.k2.mobile.app.utils.StringUtil;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
* @Title PersonalCenterActivity.java
* @Package com.oppo.mo.controller.activity.menu;
* @Description 个人中心
* @Company  K2
* 
* @author linqijun
* @date 2015-3-31 下午9:00:57
* @version V1.0
*/
@SuppressLint("NewApi")
public class PersonalCenterActivity extends BaseActivity implements OnClickListener {
	
	// 头部标题与搜索
	private TextView tv_title, tv_sech;
	// 返回
	private Button btn_back;
	// 用户名, 部门, 职位, 工号, 办公电话, 手机号码, E-mail, 联系地址, tv_feedback
	private TextView tv_name, tv_dep, tv_office, tv_land_num, tv_mob_num, tv_email, tv_address, tv_feedback;
	// 办公电话，手机号码，E-mail，联系地址
	private RelativeLayout rl_land_num, rl_address;
	// 注销
	private TextView tv_logout;
	// 头像
	private ImageView iv_head_img;
	// 自定义的弹出框类
	//private PopwinPerCenter mPopwinPerCenter;
	// 获取照片路径
	public static final File PHOTO_DIR = new File(LocalConstants.LOCAL_FILE_PATH + "/oppo/mo/poto");
	// 文件
	private File tempFile;
	// 临时路径
	private Uri tempuri;
	// 修改的内容
	private String content;
	// 服务器返回的数据集
	private User userBean;
	// 用来标识请求照相功能 
	private static final int CAMERA_WITH_DATA = 100;
	// 用来标识请求gallery 
	private static final int PHOTO_PICKED_WITH_DATA = 110;	// 4.4以下版本
	private static final int SELECT_PIC_KITKAT = 111;		// 4.4版本
	// 取到剪切的图片
	private static final int CROP_PHOTO = 120;
	// 更新头像
	private static final int UPDATE_IMG = 130;
	
	// 修改类型：1办公电话，2手机号码，3E-mail，4联系地址
	private int typeClass = 1;
	// 操作类型
	private int operType = 1;
	private Bitmap bitmap = null;
	
	// 下载图片的类
	public ImageLoaderUtil imageLoader;   
	
	private Handler mHandler = new Handler(){
		@Override
		public void dispatchMessage(Message msg) {
			switch (msg.what) {
				case 1:
					String json = (String) msg.obj;
					if (null != json) {
						userBean = JSON.parseObject(json, User.class);
						if(null != userBean) {
							tv_dep.setText(userBean.getRealityOrgName());
							tv_office.setText("("+userBean.getJobDesc()+")");
							tv_land_num.setText(userBean.getOfficeTel());
							tv_mob_num.setText(userBean.getMobilePhone());
							tv_email.setText(userBean.getEmail());
							tv_address.setText(userBean.getOfficeAddress());
							imageLoader.DisplayImage(userBean.getFileUrl(), iv_head_img,1, userBean.getSex()); // 加载远程图片
						}
					}
					break;
					
				case 2:
					String jsons = (String) msg.obj;
					if(null != jsons){
						ResPublicBean reBean = JSON.parseObject(jsons, ResPublicBean.class);
						if(null != reBean && "1".equals(reBean.getSuccess())){
							DialogUtil.showLongToast(PersonalCenterActivity.this, R.string.global_edit_status_success);
							switch (typeClass) {
								case 1:
									tv_land_num.setText(content);
									break;
								case 2:
									tv_mob_num.setText(content);
									break;
								case 3:
									tv_email.setText(content);
									break;
								case 4:
									tv_address.setText(content);
									break;
							}
						}else{
							DialogUtil.showLongToast(PersonalCenterActivity.this, R.string.edit_failed);
						}
					}
					break;
			}
		};
	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 去除头部
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_personal_center);
		initView();
		initListener();
		BaseApp.addActivity(this);
		
		imageLoader = new ImageLoaderUtil(this); 
		if (savedInstanceState == null) {
	        getFragmentManager().beginTransaction().add(R.id.container, new PlaceholderFragment()).commit();
	    }
		
		String info = getInfo();
		operProfile(1, info);
	}

	/**
	 * 方法名: initView() 
	 * 功 能 : 初始化 
	 * 参 数 : void 
	 * 返回值: void
	 */
	private void initView() {
		tv_title = (TextView) findViewById(R.id.tv_title);
		tv_sech = (TextView) findViewById(R.id.tv_sech);
		btn_back = (Button) findViewById(R.id.btn_back);
		rl_land_num = (RelativeLayout) findViewById(R.id.rl_land_num);
		rl_address = (RelativeLayout) findViewById(R.id.rl_address);
		tv_logout = (TextView) findViewById(R.id.tv_logout);
		
		tv_name = (TextView) findViewById(R.id.tv_name); 
		tv_dep = (TextView) findViewById(R.id.tv_dep);
		tv_office = (TextView) findViewById(R.id.tv_office);
		tv_land_num = (TextView) findViewById(R.id.tv_land_num);
		tv_mob_num = (TextView) findViewById(R.id.tv_mob_num);
		tv_email = (TextView) findViewById(R.id.tv_email);
		tv_address = (TextView) findViewById(R.id.tv_address);
		tv_feedback = (TextView) findViewById(R.id.tv_feedback);
		
		iv_head_img = (ImageView) findViewById(R.id.iv_head_img);
		
		tv_title.setText(getString(R.string.personal_center));
		tv_title.setVisibility(View.GONE);
		tv_sech.setVisibility(View.GONE);
		
		tv_name.setText(BaseApp.user.getUserName()+"/"+BaseApp.user.getUserId());
		tv_feedback.setText(getString(R.string.use_feedback));
	}
	
	/**
	 * @Title: operProfile
	 * @Description: 个人资料操作
	 * @param operType 操作类型，1查询，2修改
	 * @return void 返回的数据 
	 * @throws
	 */
	private void operProfile(int _operType, String info) {
		if (!NetWorkUtil.isNetworkAvailable(PersonalCenterActivity.this)) {            // 判断网络是否连接
			DialogUtil.showLongToast(PersonalCenterActivity.this, R.string.global_network_disconnected);
		}
		operType = _operType;
		int type = 0;
		switch (_operType) {
			case 1:		// 查询个人资料
				info = getInfo();
				type = LocalConstants.PERSONAL_CENTER_DETAILS_SERVER;
				break;
			case 2:		// 修改个人资料
				type = LocalConstants.PERSONAL_CENTER_MODIFY_DATA_SERVER;
				break;
		}
		if (null != info) {
			SendRequest.sendSubmitRequest(PersonalCenterActivity.this, info, BaseApp.token, BaseApp.reqLang, 
					type, BaseApp.key, submitCallBack);
		}
	}	

	/**
	 * @Title: getInfo
	 * @Description: 获取个人资料
	 * @param void
	 * @return String 返回的数据 
	 * @throws
	 */
	private String getInfo() {
		PublicBean bean = new PublicBean();
		bean.setDeviceId(BaseApp.macAddr);
		bean.setUserAccount(BaseApp.user.getUserId());
		String json = JSON.toJSONString(bean);
		return json;
	}
	
	/**
	 * @Title: updateInfo
	 * @Description: 修改个人资料
	 * @param void
	 * @return String 返回的数据 
	 * @throws
	 */
	private String updateInfo() {
		PublicBean bean = new PublicBean();
		bean.setDeviceId(BaseApp.macAddr);
		bean.setUserAccount(BaseApp.user.getUserId());
		bean.setUpdateType(typeClass+"");
		bean.setContent(content);
		String json = JSON.toJSONString(bean);
		return json;
	}
	
	/**
	 * 方法名: initListener() 
	 * 功 能 : 初始化监听器
	 * 参 数 : void 
	 * 返回值: void
	 */
	private void initListener() {
		btn_back.setOnClickListener(this);
		iv_head_img.setOnClickListener(this); 
		rl_land_num.setOnClickListener(this);
		rl_address.setOnClickListener(this);
		tv_feedback.setOnClickListener(this);
		tv_logout.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_back:	// 返回
			finish();
			break;
		case R.id.tv_logout:
			Intent mIntent = new Intent(BroadcastNotice.USER_LOGOUT);
			this.sendBroadcast(mIntent);
			break;
		case R.id.iv_head_img:		// 更换头像
			// 实例化SelectPicPopupWindow
			 //mPopwinPerCenter = new PopwinPerCenter(this, itemsOnClick);
			// 显示窗口并设置layout在PopupWindow中显示的位置
			 //mPopwinPerCenter.showAtLocation(this.findViewById(R.id.rl_show), Gravity.BOTTOM|Gravity.CENTER_HORIZONTAL, 0, 0); 
			break;
		case R.id.rl_land_num:	// 修改办公电话
			typeClass = 1;
			openDialog();
			break;
		case R.id.rl_address:	// 修改联系地址
			typeClass = 4;
			openDialog();
			break;
		case R.id.tv_feedback:	// 软件使用意见反馈
			startActivity(FeedbackActivity.class);
			break;
		}
	}
	
	/** 修改资料 */
	private void openDialog() {
		View view = LayoutInflater.from(this).inflate(R.layout.dialog_per_center, null);
		final EditText et_edit = (EditText) view.findViewById(R.id.et_edit);
		String titleName = "";
		switch (typeClass) {
		case 1:
			titleName = getResources().getString(R.string.modify_office_phone);
			if (null != userBean) {
				et_edit.setText(userBean.getOfficeTel());
			}
			break;
		case 2:
			titleName = getResources().getString(R.string.modify_mobile_number);
			if (null != userBean) {
				et_edit.setText(userBean.getMobilePhone());
			}
			break;
		case 3:
			titleName = getResources().getString(R.string.modify_email);
			if (null != userBean) {
				et_edit.setText(userBean.getEmail());
			}
			break;
		case 4:
			titleName = getResources().getString(R.string.modify_address);
			if (null != userBean) {
				et_edit.setText(userBean.getOfficeAddress());
			}
			break;
		}
		AlertDialog.Builder dialog = new AlertDialog.Builder(this);
		dialog.setTitle(titleName);
		dialog.setView(view);
		dialog.setPositiveButton(getString(R.string.save), new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				content = et_edit.getText().toString();
				boolean bl = false;
				if (typeClass == 1) {
					if (StringUtil.checkTelephone(content)) {
						bl = true;
					}
				} else if (typeClass == 2) {
					if (StringUtil.checkMobilePhone(content)) {
						bl = true;
					}
				} else if (typeClass == 3) {
					if (StringUtil.checkEmail(content)) {
						bl = true;
					}
				} else if (typeClass == 4) {
					if (null != content && !"".equals(content.trim())) {
						bl = true;
					}
				}
				if (!bl) {
					LogUtil.promptInfo(PersonalCenterActivity.this, getResources().getString(R.string.global_input_format_illgal));
				}else{
					String info = updateInfo();
					operProfile(2, info);
				}
			}
		}).setNegativeButton(getString(R.string.global_cancel), null);
		dialog.show(); 
	}

	/** 为弹出窗口实现监听类 */
	private OnClickListener itemsOnClick = new OnClickListener() {
		@Override
		
		public void onClick(View v) {
			//mPopwinPerCenter.dismiss();
			switch (v.getId()) {
				case R.id.btn_take_photo:		// 拍照
					Intent intent_tp = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
					// 相机拍照后的照片存储的路径
					if (!PHOTO_DIR.exists()) {
						PHOTO_DIR.mkdirs();	// 创建照片的存储目录
					}
					tempFile = new File(PHOTO_DIR, getPhotoFileName());
					tempuri = Uri.fromFile(tempFile);
					intent_tp.putExtra(MediaStore.EXTRA_OUTPUT, tempuri);
					startActivityForResult(intent_tp, CAMERA_WITH_DATA);
					break;
				case R.id.btn_pick_photo:		// 相册
					chooseImage();
					break;
			}         
		}
	};
	
	/** 使用系统当前日期加以调整作为照片的名称*/
	private String getPhotoFileName() {
		Date date = new Date(System.currentTimeMillis());
		SimpleDateFormat dateFormat = new SimpleDateFormat("'IMG'_yyyyMMdd_HHmmss");
		return dateFormat.format(date) + ".jpg";
	}
	
	/**
	 * http请求回调
	 */
	RequestCallBack<String> submitCallBack = new RequestCallBack<String>() {
		
		@Override
		public void onStart() {
			DialogUtil.showWithCancelProgressDialog(PersonalCenterActivity.this, null, getResources().getString(R.string.global_prompt_message), null);
		}

		@Override
		public void onLoading(long total, long current, boolean isUploading) {

		}
		
		@Override
		public void onSuccess(ResponseInfo<String> responseInfo) {
			DialogUtil.closeDialog();
			Message msgs = new Message();
			String result = responseInfo.result.toString();
			if (null != result && !"".equals(result.trim())) {
				ReqMessage msg = FastJSONUtil.getJSONToEntity(result, ReqMessage.class);
				// 判断返回标识状态是否为空
				if (null == msg || null == msg.getResCode() || "".equals(msg.getResCode())) {				
					LogUtil.promptInfo(PersonalCenterActivity.this, ErrorCodeContrast.getErrorCode("0", getResources()));
				}else if ("1103".equals(msg.getResCode()) || "1104".equals(msg.getResCode())) {
					LogUtil.promptInfo(PersonalCenterActivity.this, ErrorCodeContrast.getErrorCode(msg.getResCode(), getResources()));	
					Intent mIntent = new Intent(BroadcastNotice.USER_EXIT);
					sendBroadcast(mIntent);
					return;
				}else if("1210".equals(msg.getResCode())){
					LogUtil.promptInfo(PersonalCenterActivity.this, ErrorCodeContrast.getErrorCode(msg.getResCode(), getResources()));	
					Intent mIntent = new Intent(BroadcastNotice.WIPE_USET);
					sendBroadcast(mIntent);
					return;
				}else if("1".equals(msg.getResCode()) && null != msg.getMessage()){
					// 获取解密后并校验后的值
					String decode = EncryptUtil.getDecodeData(msg.getMessage(), BaseApp.key);
					msgs.what = operType;
					msgs.obj = decode;
					mHandler.sendMessage(msgs);
				}else {
					LogUtil.promptInfo(PersonalCenterActivity.this, ErrorCodeContrast.getErrorCode(msg.getResCode(), getResources()));
				}
			} else {
				LogUtil.promptInfo(PersonalCenterActivity.this, ErrorCodeContrast.getErrorCode("0", getResources()));
			}
		}

		@Override
		public void onFailure(HttpException error, String msg) {
			DialogUtil.closeDialog();
			if(null != msg && msg.equals(LocalConstants.API_KEY)){
				LogUtil.promptInfo(PersonalCenterActivity.this, ErrorCodeContrast.getErrorCode("1207", getResources()));
			}else{
				LogUtil.promptInfo(PersonalCenterActivity.this, ErrorCodeContrast.getErrorCode("0", getResources()));
			}
		}
	};
	/**
	 * 方法名: editPoto() 
	 * 功 能 : 编辑图片
	 * 参 数 : void 
	 * 返回值: void
	 */
	private void editPoto(Uri u){
		Intent intent = new Intent("com.android.camera.action.CROP"); // 剪裁
		intent.setDataAndType(u, "image/*");
		intent.putExtra("scale", true);
		// 设置宽高比例
		intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // 设置裁剪图片宽高
        intent.putExtra("outputX", 300);
	    intent.putExtra("outputY", 300);
	    intent.putExtra("return-data", true);
	    
		startActivityForResult(intent, CROP_PHOTO); //设置裁剪参数显示图片至预览
	}
	
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
            startActivityForResult(intent, SELECT_PIC_KITKAT);		// 4.4版本
        } else {
        	Intent intent = new Intent(Intent.ACTION_GET_CONTENT); 
    		intent.addCategory(Intent.CATEGORY_OPENABLE);  
    		intent.setType("image/*");
            startActivityForResult(intent, PHOTO_PICKED_WITH_DATA); 	// 4.4以下版本
        }
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		
		if(resultCode != RESULT_OK){
			return;
		}
		switch (requestCode) {
			case CAMERA_WITH_DATA:			// 拍照成功
				//广播刷新相册 
				Intent intentBc = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
				intentBc.setData(tempuri);     
				this.sendBroadcast(intentBc);  
				editPoto(tempuri);
				break;
			case PHOTO_PICKED_WITH_DATA:	// 从相册获取成功
				Uri u = data.getData();  
				 if(null != u){
					 editPoto(u);
				 }
				break;
			case SELECT_PIC_KITKAT:	// 4.4版本的处理
                    Uri selectedImage = data.getData();
                    String imagePath = ImageUtil.getPath(this, selectedImage);  // 获取图片的绝对路径
                    Uri newUri = Uri.parse("file:///" + imagePath); 			// 将绝对路径转换为URL
                    editPoto(newUri);
                    break;
			case CROP_PHOTO:				// 预览
				Bundle extras = data.getExtras();
				try {	 
					if (null != extras) {
						bitmap = extras.getParcelable("data");
						ByteArrayOutputStream baos = new ByteArrayOutputStream();
						bitmap.compress(Bitmap.CompressFormat.PNG, 70, baos);	// (0-100)压缩文件
						byte[] datas = baos.toByteArray();
						// 跳转预览
						Intent mIntent = new Intent(PersonalCenterActivity.this, EditHeadImgActivity.class);
						mIntent.putExtra("bitmap", datas);
						startActivityForResult(mIntent, UPDATE_IMG);
					}
					
				} catch(Exception e) {
					e.printStackTrace();
				}
				break;
			case UPDATE_IMG:				// 确认上传
				iv_head_img.setImageBitmap(bitmap);
				break;
		}
		
		super.onActivityResult(requestCode, resultCode, data);
	}
		
    public static class PlaceholderFragment extends Fragment {

        public PlaceholderFragment() {
        	
        }

		@Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_peronal, container, false);
            return rootView;
        }
    }
}
