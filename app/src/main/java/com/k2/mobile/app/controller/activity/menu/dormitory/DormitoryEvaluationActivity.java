package com.k2.mobile.app.controller.activity.menu.dormitory;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.alibaba.fastjson.JSON;
import com.k2.mobile.app.R;
import com.k2.mobile.app.common.config.BroadcastNotice;
import com.k2.mobile.app.common.config.LocalConstants;
import com.k2.mobile.app.common.exception.HttpException;
import com.k2.mobile.app.controller.core.BaseApp;
import com.k2.mobile.app.model.bean.DormRepairDetailBean;
import com.k2.mobile.app.model.bean.PublicBean;
import com.k2.mobile.app.model.bean.ReqMessage;
import com.k2.mobile.app.model.bean.ResPublicBean;
import com.k2.mobile.app.model.http.ResponseInfo;
import com.k2.mobile.app.model.http.callback.RequestCallBack;
import com.k2.mobile.app.model.http.other.SendRequest;
import com.k2.mobile.app.utils.DateUtil;
import com.k2.mobile.app.utils.DialogUtil;
import com.k2.mobile.app.utils.EncryptUtil;
import com.k2.mobile.app.utils.ErrorCodeContrast;
import com.k2.mobile.app.utils.FastJSONUtil;
import com.k2.mobile.app.utils.ImageUtil;
import com.k2.mobile.app.utils.LogUtil;
import com.k2.mobile.app.utils.NetWorkUtil;
import com.k2.mobile.app.utils.UploadFileUtil;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

/**
 * 
 * @ClassName: DormitoryEvaluationActivity
 * @Description: 宿舍报修评价
 * @author linqijun
 * @date 2015-6-08 14:24:00
 * 
 */
public class DormitoryEvaluationActivity extends FragmentActivity implements OnClickListener {
	
	// 返回
	private Button btn_back;
	// 头部标题
	private TextView tv_title;
	private TextView tv_search;
	
	private TextView tv_repair_code, tv_state, tv_number, tv_user_name,tv_department;
	private TextView tv_mm_name,tv_m_telephone,tv_s_expense, tv_c_time, tv_m_state;
	private TextView tv_y_n;
	private EditText ed_phone,ed_repair_items, ed_repair_addr, ed_description, ed_reason;
	private EditText ed_pa_time, ed_commitment_time, ed_remark;
	private LinearLayout ll_container, ll_m_container;
	private ViewPager vp_view, vp_m_view;
	private TextView tv_s1, tv_s2, tv_s3, tv_s4, tv_s5;
	private TextView tv_score;
	private TextView tv_commit;
	private RadioButton rb_yes, rb_no;
	private RadioGroup	rg_group;
	
	// 图片滑动
	private MyAdapter myAdapter = null;
	private String code = null;
	private String userCode = null;
	private int checkVal = 0;
	private int evaluation = 5;
	private int type = 1;
	private int operType = 1;
	private String mPhotoPath = null;
	private List<View> views = new ArrayList<View>();
	private ArrayList<File> lList = new ArrayList<File>();
	
	private Handler mHandler = new Handler(){
		@Override
		public void dispatchMessage(Message msg) {
			 switch (msg.what) { 
				case 1:
					String resJson = (String) msg.obj;
					if(null != resJson && !"".equals(resJson)){
						final DormRepairDetailBean dBean = JSON.parseObject(resJson, DormRepairDetailBean.class);
						if(null != dBean){
							userCode = dBean.getRepairUserCode();
							tv_repair_code.setText(dBean.getRepairNumber()); 
							int status = 0;
							if (null != dBean.getRepairStatus() && !"".equals(dBean.getRepairStatus())) {
								String tmpVal = "";
								try {
									status = Integer.parseInt(dBean.getRepairStatus());
								} catch (NumberFormatException e) {
									e.printStackTrace();
								}

								switch (status) {
								case 1:
									tmpVal = getString(R.string.create);
									break;
								case 2:
									tmpVal = getString(R.string.reviews);
									break;
								case 3:
									tmpVal = getString(R.string.pending_trial);
									break;
								case 4:
									tmpVal = getString(R.string.maintenance);
									break;
								case 5:
									tmpVal = getString(R.string.repair);
									break;
								case 6:
									tmpVal = getString(R.string.been_evaluated);
									break;
								case 7:
									tmpVal = getString(R.string.closed);
									break;
								}

								tv_state.setText(tmpVal);
							}
							tv_number.setText(dBean.getRepairUserCode());
							tv_user_name.setText(dBean.getRepairUserName());
							tv_department.setText(dBean.getRepairUserOrgName());
							ed_phone.setText(dBean.getRepairUserMobilePhone());
							ed_repair_items.setText(dBean.getRepairItems());
							ed_repair_addr.setText(dBean.getRepairAddress());
							ed_description.setText(dBean.getRepairreason());
							
							tv_mm_name.setText(dBean.getRepairerUserName());
							tv_m_telephone.setText(dBean.getRepairerMobilePhone());
							tv_s_expense.setText(dBean.getActualAmount());
							
							if(null != dBean && null != dBean.getAcceptUpdateTime() &&!"".equals(dBean.getAcceptUpdateTime())){
								String time = DateUtil.getDate_ymdhm(dBean.getAcceptUpdateTime().replaceAll("T", " "));
								ed_pa_time.setText(time);
							}
							
							if(null != dBean && null != dBean.getPlanFinishTime() &&!"".equals(dBean.getPlanFinishTime())){
								String time = DateUtil.getDate_ymdhm(dBean.getPlanFinishTime().replaceAll("T", " "));
								ed_commitment_time.setText(time);
							}
							
							if(null != dBean.getIsRepair() && "1".equals(dBean.getIsRepair().trim())){
								tv_m_state.setText(R.string.complete);
							}else if(null != dBean.getIsRepair() && "0".equals(dBean.getIsRepair().trim())){
								tv_m_state.setText(R.string.not_complete);
							}
							
							if(null != dBean && null != dBean.getFinishTime() &&!"".equals(dBean.getFinishTime())){
								String time = DateUtil.getDate_ymdhm(dBean.getFinishTime().replaceAll("T", " "));
								tv_c_time.setText(time);
							}
							ed_remark.setText(dBean.getRepairRemark());
							
							if(1 == checkVal){
								tv_score.setText(dBean.getServiceScore() + getString(R.string.branch));
								ed_reason.setText(dBean.getAssessmentContent());
								if(null != dBean.getProblemIsSolved() && "1".equals(dBean.getProblemIsSolved().trim())){
									tv_y_n.setText(getString(R.string.txt_yes));
								}else{
									tv_y_n.setText(getString(R.string.txt_no));
								}
							}
							
							if(null != dBean.getAttachmentList()){
								new Thread(new Runnable() {
									@Override
									public void run() {
										for (int i = 0; i < dBean.getAttachmentList().size(); i++) {
											try {
												Bitmap bitemp = UploadFileUtil.loadImageFromUrl(dBean.getAttachmentList().get(i).getFilePath());
												Message msg = new Message();
												msg.what = 2;
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
					Bitmap bitemp = (Bitmap) msg.obj;
					setImag(bitemp);
					myAdapter.notifyDataSetChanged();
					break;
				case 3:
					String resSave = (String) msg.obj;
					if (null != resSave) {
						ResPublicBean bean = JSON.parseObject(resSave, ResPublicBean.class);
						if (null != bean && "1".equals(bean.getSuccess())) {
							DialogUtil.showLongToast(DormitoryEvaluationActivity.this, R.string.evaluation_success);
							Intent mIntent = new Intent(BroadcastNotice.DORMITORY_FINISH_UPDATE_TREATED);
							DormitoryEvaluationActivity.this.sendBroadcast(mIntent);
							finish();
						} else {
							DialogUtil.showLongToast(DormitoryEvaluationActivity.this, R.string.evaluation_failed);
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
		setContentView(R.layout.activity_dormitory_evaluation);
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
		
		tv_repair_code = (TextView) findViewById(R.id.tv_repair_code);
		tv_state = (TextView) findViewById(R.id.tv_state); 
		tv_number = (TextView) findViewById(R.id.tv_number);  
		tv_user_name = (TextView) findViewById(R.id.tv_user_name);
		tv_department = (TextView) findViewById(R.id.tv_department);
		tv_mm_name = (TextView) findViewById(R.id.tv_mm_name);
		tv_m_telephone = (TextView) findViewById(R.id.tv_m_telephone);
		tv_s_expense = (TextView) findViewById(R.id.tv_s_expense);
		tv_c_time = (TextView) findViewById(R.id.tv_c_time);
		tv_m_state = (TextView) findViewById(R.id.tv_m_state);
		tv_y_n = (TextView) findViewById(R.id.tv_y_n);
		
		ed_phone = (EditText) findViewById(R.id.ed_phone);
		ed_repair_items = (EditText) findViewById(R.id.ed_repair_items);
		ed_repair_addr = (EditText) findViewById(R.id.ed_repair_addr); 
		ed_description = (EditText) findViewById(R.id.ed_description);
		ed_pa_time = (EditText) findViewById(R.id.ed_pa_time);
		ed_commitment_time = (EditText) findViewById(R.id.ed_commitment_time);
		ed_remark = (EditText) findViewById(R.id.ed_remark);
		
		ll_container = (LinearLayout) findViewById(R.id.ll_container);
		vp_view = (ViewPager) findViewById(R.id.vp_view);
		
		ed_reason = (EditText) findViewById(R.id.ed_reason);
		tv_s1 = (TextView) findViewById(R.id.tv_s1);
		tv_s2 = (TextView) findViewById(R.id.tv_s2); 
		tv_s3 = (TextView) findViewById(R.id.tv_s3); 
		tv_s4 = (TextView) findViewById(R.id.tv_s4);
		tv_s5 = (TextView) findViewById(R.id.tv_s5);
		tv_commit = (TextView) findViewById(R.id.tv_commit);
		tv_score = (TextView) findViewById(R.id.tv_score);
		rb_yes = (RadioButton) findViewById(R.id.rb_yes); 
		rb_no = (RadioButton) findViewById(R.id.rb_no);
		rg_group = (RadioGroup) findViewById(R.id.rg_group);
	}
	
	/**
	 * 方法名: initListener() 
	 * 功 能 : 初始化 监听器 
	 * 参 数 : void 
	 * 返回值: void  
	 */
	private void initListener() {
		btn_back.setOnClickListener(this);
		tv_search.setOnClickListener(this);
		tv_s1.setOnClickListener(this);
		tv_s2.setOnClickListener(this);
		tv_s3.setOnClickListener(this); 
		tv_s4.setOnClickListener(this);
		tv_s5.setOnClickListener(this);
		rb_yes.setOnClickListener(this); 
		rb_no.setOnClickListener(this);
		tv_commit.setOnClickListener(this);
	}
	/**
	 * 方法名: logic() 
	 * 功 能 : 业务逻辑 
	 * 参 数 : void 
	 * 返回值: void  
	 */
	private void logic(){
		tv_title.setText(getString(R.string.evaluation));
		tv_search.setVisibility(View.GONE);
		
		code = getIntent().getStringExtra("code");
		checkVal = getIntent().getIntExtra("checkVal", 0);
		tv_score.setText("5" + getString(R.string.branch));
		
		if(1 == checkVal){
			tv_s1.setVisibility(View.GONE);
			tv_s2.setVisibility(View.GONE);
			tv_s3.setVisibility(View.GONE); 
			tv_s4.setVisibility(View.GONE);
			tv_s5.setVisibility(View.GONE);
			rg_group.setVisibility(View.GONE);
			tv_y_n.setVisibility(View.VISIBLE);
			ed_reason.setEnabled(false);
			tv_commit.setVisibility(View.GONE);
		}
		
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
		
		myAdapter = new MyAdapter();
		vp_view.setAdapter(myAdapter);
		
		String info = receiveMailQuest();
		if (null != info && !"".equals(info)) {
			requestServer(LocalConstants.DORMITORY_REPAIR_DETAIL_SERVER, info);
		}
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
	
	/** 使用系统当前日期加以调整作为照片的名称 */
	private String getPhotoFileName() {
		Date date = new Date(System.currentTimeMillis());
		SimpleDateFormat dateFormat = new SimpleDateFormat("'IMG'_yyyyMMdd_HHmmss");
		return dateFormat.format(date) + ".jpg";
	}
	
	/**
	 * 方法名: receiveMailQuest() 
	 * 功 能 : 请求报文
	 * 参 数 : void 
	 * 返回值: void
	 */
	private String receiveMailQuest(){
		
		PublicBean bean = new PublicBean();
		bean.setDeviceId(BaseApp.macAddr);
		bean.setUserAccount(BaseApp.user.getUserId());
		bean.setRepairCode(code);
		
		return JSON.toJSONString(bean);
	}
	
	/**
	 * 方法名: receiveMailQuest() 
	 * 功 能 : 请求报文
	 * 参 数 : void 
	 * 返回值: void
	 */
	private String receiveEvaluation(){
		
		String description = ed_reason.getText().toString();
		if(null == description || "".equals(description.trim())){
			return null;
		}
		
		PublicBean bean = new PublicBean();
		bean.setDeviceId(BaseApp.macAddr);
		bean.setUserAccount(BaseApp.user.getUserId());
		bean.setRepairCode(code);
		bean.setAssessmentContent(description); // 评价意见			
		bean.setProblemIsSolved(type + "");;	 // 是否解决
		bean.setServiceScore(evaluation + "");		 // 服务评分
		bean.setUserCode(userCode);			 // 用户 
		
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
		if (!NetWorkUtil.isNetworkAvailable(DormitoryEvaluationActivity.this)) {
			DialogUtil.showLongToast(DormitoryEvaluationActivity.this, R.string.global_network_disconnected);
		}else{
			SendRequest.sendSubmitRequest(DormitoryEvaluationActivity.this, info, BaseApp.token, BaseApp.reqLang, server, BaseApp.key, submitCallBack);
		}
	}
	
	/**
	 * http请求回调
	 */
	RequestCallBack<String> submitCallBack = new RequestCallBack<String>() {

		@Override
		public void onStart() {
			DialogUtil.showWithCancelProgressDialog(DormitoryEvaluationActivity.this, null, getResources().getString(R.string.global_prompt_message), null);
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
					LogUtil.promptInfo(DormitoryEvaluationActivity.this, ErrorCodeContrast.getErrorCode("0", getResources()));
					return;
				// 验证不合法
				}else if ("1103".equals(msg.getResCode())) {
					LogUtil.promptInfo(DormitoryEvaluationActivity.this, ErrorCodeContrast.getErrorCode(msg.getResCode(), getResources()));
					Intent mIntent = new Intent(BroadcastNotice.USER_EXIT);
					DormitoryEvaluationActivity.this.sendBroadcast(mIntent);
					return;
				}else if("1210".equals(msg.getResCode())){
					LogUtil.promptInfo(DormitoryEvaluationActivity.this, ErrorCodeContrast.getErrorCode(msg.getResCode(), getResources()));	
					Intent mIntent = new Intent(BroadcastNotice.WIPE_USET);
					sendBroadcast(mIntent);
					return;
				}else if (!"1".equals(msg.getResCode())) {
					LogUtil.promptInfo(DormitoryEvaluationActivity.this, ErrorCodeContrast.getErrorCode(msg.getResCode(), getResources()));	
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
				LogUtil.promptInfo(DormitoryEvaluationActivity.this, ErrorCodeContrast.getErrorCode("0", getResources()));
			}
		}

		@Override
		public void onFailure(HttpException error, String msg) {
			DialogUtil.closeDialog();
			if(null != msg && msg.equals(LocalConstants.API_KEY)){
				LogUtil.promptInfo(DormitoryEvaluationActivity.this, ErrorCodeContrast.getErrorCode("1207", getResources()));
			}else{
				LogUtil.promptInfo(DormitoryEvaluationActivity.this, ErrorCodeContrast.getErrorCode("0", getResources()));
			}
		}
	};
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.btn_back:
				this.finish();
				break;
			case R.id.tv_s1:
				evaluation = 1;
				setEvaluation();
				break;
			case R.id.tv_s2:
				evaluation = 2;
				setEvaluation();
				break;
			case R.id.tv_s3:
				evaluation = 3;
				setEvaluation();
				break;
			case R.id.tv_s4:
				evaluation = 4;
				setEvaluation();
				break;
			case R.id.tv_s5:
				evaluation = 5;
				setEvaluation();
				break;
			case R.id.rb_yes:
				type = 1;
				break;
			case R.id.rb_no:
				type = 0;
				break;
			case R.id.tv_commit:
				operType = 3;
				String info = receiveEvaluation();
				if(null != info && !"".equals(info)){
					requestServer(LocalConstants.DORMITORY_REPAIR_EVALUATION_SERVER, info);
				}
				break;
		}
	}
	/**
	 * 设置评分
	 * 
	 * 
	 */
	@SuppressLint("NewApi")
	private void setEvaluation(){
		switch (evaluation) {
			case 1:
				tv_s1.setBackgroundResource(R.drawable.favourites);
				tv_s2.setBackgroundResource(R.drawable.favourites_b);	
				tv_s3.setBackgroundResource(R.drawable.favourites_b);
				tv_s4.setBackgroundResource(R.drawable.favourites_b);
				tv_s5.setBackgroundResource(R.drawable.favourites_b);
				break;
			case 2:
				tv_s1.setBackgroundResource(R.drawable.favourites);
				tv_s2.setBackgroundResource(R.drawable.favourites);		
				tv_s3.setBackgroundResource(R.drawable.favourites_b);
				tv_s4.setBackgroundResource(R.drawable.favourites_b);
				tv_s5.setBackgroundResource(R.drawable.favourites_b);
				break;
			case 3:
				tv_s1.setBackgroundResource(R.drawable.favourites);
				tv_s2.setBackgroundResource(R.drawable.favourites);	
				tv_s3.setBackgroundResource(R.drawable.favourites);
				tv_s4.setBackgroundResource(R.drawable.favourites_b);
				tv_s5.setBackgroundResource(R.drawable.favourites_b);
				break;
			case 4:
				tv_s1.setBackgroundResource(R.drawable.favourites);
				tv_s2.setBackgroundResource(R.drawable.favourites);	
				tv_s3.setBackgroundResource(R.drawable.favourites);
				tv_s4.setBackgroundResource(R.drawable.favourites);
				tv_s5.setBackgroundResource(R.drawable.favourites_b);
				break;
			case 5:
				tv_s1.setBackgroundResource(R.drawable.favourites);
				tv_s2.setBackgroundResource(R.drawable.favourites);	
				tv_s3.setBackgroundResource(R.drawable.favourites);
				tv_s4.setBackgroundResource(R.drawable.favourites);
				tv_s5.setBackgroundResource(R.drawable.favourites);
				break;
		}
		tv_score.setText(evaluation + getString(R.string.branch));
	}
	
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