package com.k2.mobile.app.controller.activity.menu.calendar;

import java.net.URL;
import java.util.Calendar;

import com.alibaba.fastjson.JSON;
import com.k2.mobile.app.R;
import com.k2.mobile.app.common.config.BroadcastNotice;
import com.k2.mobile.app.common.config.LocalConstants;
import com.k2.mobile.app.common.exception.HttpException;
import com.k2.mobile.app.controller.core.BaseActivity;
import com.k2.mobile.app.controller.core.BaseApp;
import com.k2.mobile.app.model.bean.CalendarBean;
import com.k2.mobile.app.model.bean.PublicBean;
import com.k2.mobile.app.model.bean.ReqMessage;
import com.k2.mobile.app.model.bean.ResCalendarBean;
import com.k2.mobile.app.model.http.ResponseInfo;
import com.k2.mobile.app.model.http.callback.RequestCallBack;
import com.k2.mobile.app.model.http.other.SendRequest;
import com.k2.mobile.app.utils.DateUtil;
import com.k2.mobile.app.utils.DialogUtil;
import com.k2.mobile.app.utils.ErrorCodeContrast;
import com.k2.mobile.app.utils.FastJSONUtil;
import com.k2.mobile.app.utils.LogUtil;
import com.k2.mobile.app.utils.NetWorkUtil;
import com.k2.mobile.app.view.wheel.StrericWheelAdapter;
import com.k2.mobile.app.view.wheel.WheelView;

import android.os.Bundle;
import android.text.Html;
import android.text.Html.ImageGetter;
import android.text.method.LinkMovementMethod;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.animation.AnticipateOvershootInterpolator;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.drawable.Drawable;

/**
 * @Title: AddCalendarActivity.java
 * @Package com.oppo.mo.model.bean
 * @Description: 添加我的日历
 * @Company:广东欧泊移动通讯有限公司
 * 
 * @author linqijun	
 * @date 2015-04-09 17:06:00
 * @version V1.0
 */
public class AddCalendarActivity extends BaseActivity implements OnClickListener {
	
	private TextView tv_title;
	private Button btn_back;
	private TextView tv_sech;
	
	private EditText ed_title;
	private EditText ed_content;
//	private TextView ed_start_time;
//	private TextView ed_end_time;
	private TextView tv_start_now_time;
	private TextView tv_start_date_time;
	private TextView tv_end_now_time;
	private TextView tv_end_date_time;
	private TextView tv_confirm;
	private TextView tv_cancel;
	private EditText ed_remark;
		
	private Button btn_commit;
	private Button btn_del;
	
	private WheelView yearWheel,monthWheel,dayWheel,hourWheel,minuteWheel;
	public static String[] yearContent = null;
	public static String[] monthContent = null;
	public static String[] dayContent = null;
	public static String[] hourContent = null;
	public static String[] minuteContent = null;
	public static String[] secondContent = null;
	
	private ResCalendarBean resBean = null;
	private CalendarBean cBean = null;
	private int operType = 1;
	private int eventType = 0;
	private int flag = 1;
	private AlertDialog dialog = null;
			
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 去除头部
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_add_my_calendar);
		initView();
		initListener();
		initContent();
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
		btn_back = (Button) findViewById(R.id.btn_back);
		tv_sech = (TextView) findViewById(R.id.tv_sech);
		
		ed_title = (EditText) findViewById(R.id.ed_title);
		ed_content = (EditText) findViewById(R.id.ed_content);
		tv_start_now_time = (TextView) findViewById(R.id.tv_start_now_time);
		tv_start_date_time = (TextView) findViewById(R.id.tv_start_date_time);
		tv_end_now_time = (TextView) findViewById(R.id.tv_end_now_time);
		tv_end_date_time = (TextView) findViewById(R.id.tv_end_date_time);
		ed_remark = (EditText) findViewById(R.id.ed_remark);
				
		btn_commit = (Button) findViewById(R.id.btn_commit);
		btn_del = (Button) findViewById(R.id.btn_del);
		
		tv_start_now_time.setText(DateUtil.getDate_hms());
		tv_end_now_time.setText(DateUtil.getDate_hms());
		tv_start_date_time.setText(DateUtil.getDate_ymd());
		tv_end_date_time.setText(DateUtil.getDate_ymd());
		
		ed_content.setMovementMethod(ScrollingMovementMethod.getInstance());// 设置可滚动  
		ed_content.setMovementMethod(LinkMovementMethod.getInstance());//设置超链接可以打开网页  
        
		resBean = (ResCalendarBean) getIntent().getSerializableExtra("resBean");
		cBean = (CalendarBean) getIntent().getSerializableExtra("cBean");
		
		if (null != resBean) {
			tv_title.setText(res.getString(R.string.event));
			operType = 2;
			ed_title.setText(resBean.getTitle());
			if(null != resBean.getDetail()){
				ed_content.setText(Html.fromHtml(resBean.getDetail(), imgGetter, null));
			}
			if (null != resBean.getStartTime()) {
				String startTime = resBean.getStartTime().replaceAll("T", " ");
				tv_start_now_time.setText(DateUtil.getDate_hms(startTime));
				tv_start_date_time.setText(DateUtil.getDate_ymd(startTime));
			}
			if (null != resBean.getEndTime()) {
				String endTime = resBean.getEndTime().replaceAll("T", " ");
				tv_end_now_time.setText(DateUtil.getDate_hms(endTime));
				tv_end_date_time.setText(DateUtil.getDate_ymd(endTime));
			}
			ed_remark.setText(resBean.getRemark());
			
			if(null != resBean.getEventType() && "2".equals(resBean.getEventType().trim())){
				eventType = 1;
				ed_title.setEnabled(false);
				tv_start_now_time.setEnabled(false);
				tv_start_date_time.setEnabled(false);
				tv_end_now_time.setEnabled(false);
				tv_end_date_time.setEnabled(false);
				ed_remark.setEnabled(false);
				ed_content.setEnabled(false);
				btn_commit.setVisibility(View.GONE);
				btn_del.setVisibility(View.GONE);
			}else {
				if(null != resBean.getDetail()){
					ed_content.setText(Html.fromHtml(resBean.getDetail(), imgGetter, null));
				}
			}
		}else if (null != cBean) {
			tv_title.setText(res.getString(R.string.add_event));
			btn_del.setVisibility(View.GONE);
		}
		
		tv_sech.setVisibility(View.GONE);
	}
	
	/**
	 * 方法名: initListener() 
	 * 功 能 : 初始化 监听器 
	 * 参 数 : void 
	 * 返回值: void
	 */
	private void initListener() {
		btn_back.setOnClickListener(this);
		tv_start_now_time.setOnClickListener(this); 
		tv_start_date_time.setOnClickListener(this); 
		tv_end_now_time.setOnClickListener(this); 
		tv_end_date_time.setOnClickListener(this); 
		
		btn_commit.setOnClickListener(this);
		btn_del.setOnClickListener(this);
	}
	
	// fromhtml函数的第一个参数里面的含有的url  
    ImageGetter imgGetter = new ImageGetter() {  
        @Override
		public Drawable getDrawable(String source) {  
            Drawable drawable = null;  
            URL url;  
            try {  
                url = new URL(source);  
                drawable = Drawable.createFromStream(url.openStream(), ""); // 获取网路图片  
            } catch (Exception e) {  
                e.printStackTrace();  
                return null;  
            }  
            drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());  
            
            return drawable;  
        }  
    };  
	/**
	 * 方法名: initContent() 
	 * 功 能 : 初始化时间控件
	 * 参 数 : void 
	 * 返回值: void
	 */
	public void initContent()
	{
		// 添加年
		yearContent = new String[10];
		for(int i=0;i<10;i++)
			yearContent[i] = String.valueOf(i+2013);
		// 添加月
		monthContent = new String[12];
		for(int i=0;i<12;i++)
		{
			monthContent[i]= String.valueOf(i+1);
			if(monthContent[i].length()<2)
	        {
				monthContent[i] = "0"+monthContent[i];
	        }
		}
		// 添加天
		dayContent = new String[31];
		for(int i=0;i<31;i++)
		{
			dayContent[i]=String.valueOf(i+1);
			if(dayContent[i].length()<2)
	        {
				dayContent[i] = "0"+dayContent[i];
	        }
		}	
		// 添加小时
		hourContent = new String[24];
		for(int i=0;i<24;i++)
		{
			hourContent[i]= String.valueOf(i);
			if(hourContent[i].length()<2)
	        {
				hourContent[i] = "0"+hourContent[i];
	        }
		}
		// 添加分
		minuteContent = new String[60];
		for(int i=0;i<60;i++)
		{
			minuteContent[i]=String.valueOf(i);
			if(minuteContent[i].length()<2)
	        {
				minuteContent[i] = "0"+minuteContent[i];
	        }
		}
		// 秒
		secondContent = new String[60];
		for(int i=0;i<60;i++)
		{
			secondContent[i]=String.valueOf(i);
			if(secondContent[i].length()<2)
	        {
				secondContent[i] = "0"+secondContent[i];
	        }
		}
	}
	/**
	 * 方法名: chooseTime() 
	 * 功 能 : 时间选择器
	 * 参 数 : void 
	 * 返回值: void
	 */
	private void chooseTime(int layout){
		LayoutInflater inflater = LayoutInflater.from(AddCalendarActivity.this);  
		View view = inflater.inflate(layout, null); 
		Calendar calendar = Calendar.getInstance();
		
		if(1 == flag || 3 == flag){
			int curYear = calendar.get(Calendar.YEAR);
		    int curMonth= calendar.get(Calendar.MONTH)+1;
		    int curDay = calendar.get(Calendar.DAY_OF_MONTH);
		     
		    yearWheel = (WheelView)view.findViewById(R.id.yearwheel);
			monthWheel = (WheelView)view.findViewById(R.id.monthwheel);
			dayWheel = (WheelView)view.findViewById(R.id.daywheel);
			 
			yearWheel.setAdapter(new StrericWheelAdapter(yearContent));
		 	yearWheel.setCurrentItem(curYear-2013);
		    yearWheel.setCyclic(true);
		    yearWheel.setInterpolator(new AnticipateOvershootInterpolator());
	        
	        monthWheel.setAdapter(new StrericWheelAdapter(monthContent));
	       
	        monthWheel.setCurrentItem(curMonth - 1);
	     
	        monthWheel.setCyclic(true);
	        monthWheel.setInterpolator(new AnticipateOvershootInterpolator());
	        
	        dayWheel.setAdapter(new StrericWheelAdapter(dayContent));
	        dayWheel.setCurrentItem(curDay - 1);
	        dayWheel.setCyclic(true);
	        dayWheel.setInterpolator(new AnticipateOvershootInterpolator());
			 
		}else if(2 == flag || 4 == flag){
		    int curHour = calendar.get(Calendar.HOUR_OF_DAY);
	        int curMinute = calendar.get(Calendar.MINUTE);
	        
	        hourWheel = (WheelView)view.findViewById(R.id.hourwheel);
		    minuteWheel = (WheelView)view.findViewById(R.id.minutewheel);
		    
		    hourWheel.setAdapter(new StrericWheelAdapter(hourContent));
	        hourWheel.setCurrentItem(curHour);
	        hourWheel.setCyclic(true);
	        hourWheel.setInterpolator(new AnticipateOvershootInterpolator());
	        
	        minuteWheel.setAdapter(new StrericWheelAdapter(minuteContent));
	        minuteWheel.setCurrentItem(curMinute);
	        minuteWheel.setCyclic(true);
	        minuteWheel.setInterpolator(new AnticipateOvershootInterpolator());
		}
		tv_confirm = (TextView) view.findViewById(R.id.tv_confirm);
		tv_cancel = (TextView) view.findViewById(R.id.tv_cancel);
		
		tv_confirm.setOnClickListener(this);
		tv_cancel.setOnClickListener(this);
		
        AlertDialog.Builder builder = new AlertDialog.Builder(this);  
        builder.setView(view); 
            
        dialog = builder.show();
	}
	/**
	* @Title: checkValue
	* @Description: 检测约束值
	* @param void
	* @return String 返回的数据 
	* @throws
	*/
	private String checkValue(){
		String title = ed_title.getText().toString();
		String content = ed_content.getText().toString();
		String startTime = tv_start_date_time.getText().toString() +" "+tv_start_now_time.getText().toString()+":00";
		String endTime = tv_end_date_time.getText().toString() +" "+tv_end_now_time.getText().toString()+":00";
		String remark =  ed_remark.getText().toString(); 
	 
		if (null == title || "".equals(title)) {
			DialogUtil.showLongToast(AddCalendarActivity.this, R.string.title_is_null);
			return null;
		}else if (null == content || "".equals(content)) {
			DialogUtil.showLongToast(AddCalendarActivity.this, R.string.content_is_null);
			return null;
		}
		
		ResCalendarBean bean = new ResCalendarBean();
		bean.setDeviceId(BaseApp.macAddr);
		bean.setUserId(BaseApp.user.getUserId());
		if (2 == operType) {
			bean.setId(resBean.getId());
			bean.setUserAccount(resBean.getUserAccount());
			bean.setUserCode(resBean.getUserCode());
		}else {
			bean.setUserAccount(BaseApp.user.getUserId());
			bean.setUserCode(BaseApp.user.getUserId());
		}
		bean.setEventType((eventType+1)+"");
		bean.setTitle(title);
		bean.setDetail(content);
		bean.setStartTime(startTime);
		bean.setEndTime(endTime);
		bean.setRemark(remark);
		
		return JSON.toJSONString(bean);
	}
   /**
	* @Title: delCalendar
	* @Description: 删除日历
	* @param void
	* @return String 返回的数据 
	* @throws
	*/
	private String delCalendar(String id){
		
		PublicBean bean = new PublicBean();
		bean.setCid(id);
		bean.setDeviceId(BaseApp.macAddr);
		bean.setUserAccount(BaseApp.user.getUserId());
		
		return JSON.toJSONString(bean);
	}
		
	/**
	 * @Title: operCalendar
	 * @Description: 日历增删除查改操作
	 * @param operType 操作类型，1删除，2修改, 3添加
	 * @return void 返回的数据 
	 * @throws
	 */
    private void operCalendar(int _operType, String info){
    	
    	if (!NetWorkUtil.isNetworkAvailable(this)) {
 			DialogUtil.showLongToast(AddCalendarActivity.this, R.string.global_network_disconnected);
 			return;
 		} 
    	operType = _operType;
    	int type = 0;
		switch (_operType) {
			case 1:		// 删除我的日历
				type = LocalConstants.MY_CALENDAR_DELETE_SERVER;
				break;	
			case 2:		// 修改我的日历
				type = LocalConstants.MY_CALENDAR_UPDATE_SERVER;
				break;
			case 3:		// 增加我的日历
				type = LocalConstants.MY_CALENDAR_ADD_SERVER;
				break;
		}
		
		SendRequest.sendSubmitRequest(AddCalendarActivity.this, info, BaseApp.token, BaseApp.reqLang, 
				type, BaseApp.key, submitCallBack);
    }

    /**
	 * http请求回调
	 */
	RequestCallBack<String> submitCallBack = new RequestCallBack<String>() {

		@Override
		public void onStart() {
			DialogUtil.showWithCancelProgressDialog(AddCalendarActivity.this, null, res.getString(R.string.global_prompt_message), null);
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
					LogUtil.promptInfo(AddCalendarActivity.this, ErrorCodeContrast.getErrorCode("0", res));
					return;
				// 验证不合法
				}else if ("1103".equals(msg.getResCode()) || "1104".equals(msg.getResCode())) {
					LogUtil.promptInfo(AddCalendarActivity.this, ErrorCodeContrast.getErrorCode(msg.getResCode(), res));
					Intent mIntent = new Intent(BroadcastNotice.USER_EXIT);
					sendBroadcast(mIntent);
					return;
				}else if("1210".equals(msg.getResCode())){
					LogUtil.promptInfo(AddCalendarActivity.this, ErrorCodeContrast.getErrorCode(msg.getResCode(), res));	
					Intent mIntent = new Intent(BroadcastNotice.WIPE_USET);
					sendBroadcast(mIntent);
					return;
				}else if (!"1".equals(msg.getResCode())) {
					LogUtil.promptInfo(AddCalendarActivity.this, ErrorCodeContrast.getErrorCode(msg.getResCode(), res));	
					return;
				}
				
				if(operType == 1){
					DialogUtil.showLongToast(AddCalendarActivity.this, R.string.delete_success);
				}else if (operType == 2) {
					DialogUtil.showLongToast(AddCalendarActivity.this, R.string.global_edit_status_success);
				}else if (operType == 3) {
					DialogUtil.showLongToast(AddCalendarActivity.this, R.string.global_add_new_status_success);
				}
				
				Intent data = new Intent();  
		        setResult(1020, data);  
		        finish();
		        
			} else {
				LogUtil.promptInfo(AddCalendarActivity.this, ErrorCodeContrast.getErrorCode("0", res));
			}
		}

		@Override
		public void onFailure(HttpException error, String msg) {
			DialogUtil.closeDialog();
			if(null != msg && msg.equals(LocalConstants.API_KEY)){
				LogUtil.promptInfo(AddCalendarActivity.this, ErrorCodeContrast.getErrorCode("1207", res));
			}else{
				LogUtil.promptInfo(AddCalendarActivity.this, ErrorCodeContrast.getErrorCode("0", res));
			}
		}
	};	
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.btn_back:
				finish();
				break;
			case R.id.tv_start_now_time:
				flag = 2;
				chooseTime(R.layout.time_hm);
				break;
			case R.id.tv_start_date_time:
				flag = 1;
				chooseTime(R.layout.time_ymd);
				break; 
			case R.id.tv_end_now_time:
				flag = 4;
				chooseTime(R.layout.time_hm);
				break;
			case R.id.tv_end_date_time:
				flag = 3;
				chooseTime(R.layout.time_ymd);
				break;
			case R.id.tv_confirm:
				StringBuffer sb = new StringBuffer();  
				if(1 == flag || 3 == flag){
					sb.append(yearWheel.getCurrentItemValue()).append("-")
	        		.append(monthWheel.getCurrentItemValue()).append("-")
	        		.append(dayWheel.getCurrentItemValue());
				}else if(2 == flag || 4 == flag){
	        		sb.append(hourWheel.getCurrentItemValue())  
	        		.append(":").append(minuteWheel.getCurrentItemValue());
				}
				
				String start_time = tv_start_date_time.getText().toString() +" "+tv_start_now_time.getText().toString()+":00";
        		String end_time = tv_end_date_time.getText().toString() +" "+tv_end_now_time.getText().toString()+":00";
				
        		switch (flag) {
					case 2:
						start_time = tv_start_date_time.getText().toString() +" "+sb.toString()+":00";
						break;
					case 1:
						start_time = sb.toString() +" "+tv_start_now_time.getText().toString()+":00";
						break;
					case 4:
						end_time = tv_end_date_time.getText().toString() +" "+sb.toString()+":00";
						break;
					case 3:
						end_time = sb.toString() +" "+tv_end_now_time.getText().toString()+":00";
						break;
				}
        		
				int timeCheck = DateUtil.timeComparingSize(start_time, end_time);
				if ( -1 == timeCheck) {
					DialogUtil.showLongToast(AddCalendarActivity.this, R.string.time_format_error);
					break;
				} else if (0 == timeCheck) { 
					DialogUtil.showLongToast(AddCalendarActivity.this, R.string.time_can_not);
					break;
				} else if (1 == timeCheck) { 
					DialogUtil.showLongToast(AddCalendarActivity.this, R.string.start_greater_end_time);
					break;
				}
				
				switch (flag) {
					case 2:
						tv_start_now_time.setText(sb.toString());
						break;
					case 1:
						tv_start_date_time.setText(sb.toString());
						break;
					case 4:
						tv_end_now_time.setText(sb.toString());
						break;
					case 3:
						tv_end_date_time.setText(sb.toString());
						break;
			  }
			case R.id.tv_cancel:
				if(null != dialog){
					dialog.dismiss();
				}
				break;
			case R.id.btn_commit:
				String mInfo = checkValue();
				if(2 == operType){
					operCalendar(2, mInfo);
				}else{
					operCalendar(3, mInfo);
				}
				
				break;
			case R.id.btn_del:
				String info = delCalendar(resBean.getId());
				operCalendar(1, info);
				break;
		}
	}
}