package com.k2.mobile.app.controller.activity.menu.dormitory;

import java.util.ArrayList;
import java.util.List;

import com.alibaba.fastjson.JSON;
import com.k2.mobile.app.R;
import com.k2.mobile.app.common.config.BroadcastNotice;
import com.k2.mobile.app.common.config.LocalConstants;
import com.k2.mobile.app.common.exception.HttpException;
import com.k2.mobile.app.controller.core.BaseActivity;
import com.k2.mobile.app.controller.core.BaseApp;
import com.k2.mobile.app.model.adapter.DoemRepairListAdapter;
import com.k2.mobile.app.model.bean.DormRepairBean;
import com.k2.mobile.app.model.bean.PublicBean;
import com.k2.mobile.app.model.bean.ReqMessage;
import com.k2.mobile.app.model.http.ResponseInfo;
import com.k2.mobile.app.model.http.callback.RequestCallBack;
import com.k2.mobile.app.model.http.other.SendRequest;
import com.k2.mobile.app.utils.DialogUtil;
import com.k2.mobile.app.utils.EncryptUtil;
import com.k2.mobile.app.utils.ErrorCodeContrast;
import com.k2.mobile.app.utils.FastJSONUtil;
import com.k2.mobile.app.utils.LogUtil;
import com.k2.mobile.app.utils.NetWorkUtil;
import com.k2.mobile.app.view.widget.XListView;
import com.k2.mobile.app.view.widget.XListView.IXListViewListener;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

/**
 * 
 * @ClassName: DormitoryRepairActivity
 * @Description: 宿舍报修
 * @author linqijun
 * @date 2015-7-16 19:40:46
 *
 */
@SuppressLint("NewApi")
public class DormitoryRepairActivity extends BaseActivity implements OnClickListener, IXListViewListener {
	
	private Button btn_back;
	private TextView tv_title;
	private TextView tv_sech;
	private XListView lv_show;
	
	// 页码
	private int pageIndex = 1;
	// 每页行数
	private int pageSize = 10;
	// 识别是否下拉刷新
	private int flag = 1;
	// 标记加载
	public int loadType = 1;
	private Intent mIntent = null;
	// 报修列表
	private List<DormRepairBean> dbList = null;
	private DoemRepairListAdapter dormRepairAdapter = null;
	// 广播接收
	private IncomingReceiver iReceiver = null;
		
	private Handler mHandler = new Handler() {
		@Override
		public void dispatchMessage(Message msg) {
			switch (msg.what) {
			case 1:
				String json = (String) msg.obj;
				if(null != json && !"".equals(json)){
					if(3 != flag){
						dbList.clear();
					}
					
					List<DormRepairBean> tmpList = JSON.parseArray(json, DormRepairBean.class);
					if(null != tmpList){
						dbList.addAll(tmpList);
					}
					
					dormRepairAdapter.notifyDataSetChanged();
					
					String tmpTips = null;
					
					if((null == tmpList || 1 > tmpList.size()) && 1 > dbList.size()){ // 暂无数据
						tmpTips = getString(R.string.no_data);
					}else if(null != tmpList && tmpList.size() < 10){			// 所有数据加载完
						tmpTips = getString(R.string.all_data_loaded);
					}else{													// 查看更多
						tmpTips = getString(R.string.xlistview_footer_hint_normal);
					}
					
					lv_show.setTips(tmpTips);
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
		setContentView(R.layout.activity_dormitory_repair);
		initView();
		initListener();
		createFilter();
		initAdapter();
		BaseApp.addActivity(this);
	}

	/**
	 * 方法名: initView() 
	 * 功 能 : 初始化 
	 * 参 数 : void 
	 * 返回值: void
	 */
	private void initView() {
		btn_back = (Button) findViewById(R.id.btn_back);
		tv_title = (TextView) findViewById(R.id.tv_title);
		tv_sech = (TextView) findViewById(R.id.tv_sech);
		lv_show = (XListView) findViewById(R.id.lv_show);
		
		lv_show.setPullRefreshEnable(true);		// 设置下拉更新
		lv_show.setPullLoadEnable(true);		// 设置让它上拉更新
		tv_title.setText(getString(R.string.dormitory_repair));
		tv_sech.setText("");
		// 设置tv_search背景图片和样式
		Drawable drawable = getResources().getDrawable(R.drawable.new_selector);
		// setBackground()方法，在16版本以上才有
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
			tv_sech.setBackground(drawable);
		} else {
			tv_sech.setBackgroundDrawable(drawable);
		}
		// 重新计算tv_search的大小
		int w = View.MeasureSpec.makeMeasureSpec(0,View.MeasureSpec.UNSPECIFIED);  
		int h = View.MeasureSpec.makeMeasureSpec(0,View.MeasureSpec.UNSPECIFIED);  

		tv_sech.measure(w, h);  
		int height = tv_sech.getMeasuredHeight();  
		int width = tv_sech.getMeasuredWidth();  

		LayoutParams lp = tv_sech.getLayoutParams();    
		lp.width = (int)(width*0.6);
		lp.height = (int)(height*0.6);        
		tv_sech.setLayoutParams(lp);
		
		String info = receiveMailQuest();
		requestServer(LocalConstants.DORMITORY_REPAIR_SERVER, info);
	}
	
	/**
	 * 方法名: initListener() 
	 * 功 能 : 初始化 监听器 
	 * 参 数 : void 
	 * 返回值: void
	 */
	private void initListener() {
		btn_back.setOnClickListener(this);
		tv_sech.setOnClickListener(this);
		lv_show.setOnItemClickListener(itemListener);
		lv_show.setXListViewListener(this);
	}
	/**
	 * 方法名: initAdapter() 
	 * 功 能 : 初始化 适配器
	 * 参 数 : void 
	 * 返回值: void
	 */
	private void initAdapter(){
		if(null == dbList){
			dbList = new ArrayList<DormRepairBean>();
		}
		
		dormRepairAdapter = new DoemRepairListAdapter(this, dbList);
		lv_show.setAdapter(dormRepairAdapter);
	}
	
	/**
	 * @Title: createFilter
	 * @Description: 创建IntentFilter
	 * @param void
	 * @return void 
	 * @throws
	 */
	private void createFilter() {
		IntentFilter filter = new IntentFilter();
		filter.addAction(BroadcastNotice.DORMITORY_FINISH_UPDATE_TREATED);
		iReceiver = new IncomingReceiver();
		// 注册广播
		this.registerReceiver(iReceiver, filter);
	}
	
	/**
	 * 接收广播
	 */ 
	private class IncomingReceiver extends BroadcastReceiver{
		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();  
	        if (action.equals(BroadcastNotice.DORMITORY_FINISH_UPDATE_TREATED)) {
	        	flag = 2;
				pageIndex = 1;
				String info = receiveMailQuest();
				requestServer(LocalConstants.DORMITORY_REPAIR_SERVER, info);
	        }
		}
	}
	// 实现弹出动画效果
	private OnItemClickListener itemListener = new OnItemClickListener() {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			int positions = position - 1;
			int status = 0;
			
			String rStatus = dbList.get(positions).getRepairStatus();	// 获取状态
			String code = dbList.get(positions).getRepairCode();		// 获取每条信息的CODE
			String sid = dbList.get(positions).getId();
			String userCode = dbList.get(positions).getRepairUserCode();
			
			if(null != rStatus && !"".equals(rStatus.trim())){
				try {
					status = Integer.parseInt(rStatus);
				} catch (NumberFormatException e) {
					e.printStackTrace();
				}
			}
			
			switch (status) {
				case 1:
					mIntent = new Intent(DormitoryRepairActivity.this, AddDormitoryActivity.class);
					mIntent.putExtra("code", code);
					startActivity(mIntent);
					break;
				case 2:					// 待指派
				case 3:					// 待预审
				case 4:					// 待维修
					mIntent = new Intent(DormitoryRepairActivity.this, DormitoryWaitHandleActivity.class);
					mIntent.putExtra("code", code);
					mIntent.putExtra("id", sid);
					mIntent.putExtra("userCode", userCode);
					startActivity(mIntent);
					break;
				case 5:					// 评价
					mIntent = new Intent(DormitoryRepairActivity.this, DormitoryEvaluationActivity.class);
					mIntent.putExtra("code", code);
					startActivity(mIntent);
					break;
				case 6:					// 已评价
					mIntent = new Intent(DormitoryRepairActivity.this, DormitoryEvaluationActivity.class);
					mIntent.putExtra("code", code);
					mIntent.putExtra("checkVal", 1);
					startActivity(mIntent);
					break;
				case 7:					// 已关闭
					mIntent = new Intent(DormitoryRepairActivity.this, DormitoryColsedInfoActivity.class);
					mIntent.putExtra("code", code);
					startActivity(mIntent);
					break;
			}
		}
	};
	
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
		bean.setListType("1");
		bean.setPageIndex(pageIndex + "");
		bean.setPageSize(pageSize + "");
		
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
		if (!NetWorkUtil.isNetworkAvailable(DormitoryRepairActivity.this)) {
			DialogUtil.showLongToast(DormitoryRepairActivity.this, R.string.global_network_disconnected);
		}else{
			SendRequest.sendSubmitRequest(DormitoryRepairActivity.this, info, BaseApp.token, BaseApp.reqLang, server, BaseApp.key, submitCallBack);
		}
	}
	
	/**
	 * http请求回调
	 */
	RequestCallBack<String> submitCallBack = new RequestCallBack<String>() {

		@Override
		public void onStart() {
			if (1 == flag) {
				DialogUtil.showWithCancelProgressDialog(DormitoryRepairActivity.this, null, res.getString(R.string.global_prompt_message), null);
			}else{
				stopLoad();
			}
		}

		@Override
		public void onLoading(long total, long current, boolean isUploading) {

		}
		
		@Override
		public void onSuccess(ResponseInfo<String> responseInfo) {     
			if (1 == flag) {
				DialogUtil.closeDialog();
			}else{
				stopLoad();
			}
			String result = responseInfo.result.toString();
			if (null != result && !"".equals(result.trim())) {
				ReqMessage msg = FastJSONUtil.getJSONToEntity(result, ReqMessage.class);
				// 判断返回标识状态是否为空
				if (null == msg || null == msg.getResCode() || "".equals(msg.getResCode())) {				
					LogUtil.promptInfo(DormitoryRepairActivity.this, ErrorCodeContrast.getErrorCode("0", res));
					return;
				// 验证不合法
				}else if ("1103".equals(msg.getResCode()) || "1104".equals(msg.getResCode())) {
					LogUtil.promptInfo(DormitoryRepairActivity.this, ErrorCodeContrast.getErrorCode(msg.getResCode(), res));
					Intent mIntent = new Intent(BroadcastNotice.USER_EXIT);
					DormitoryRepairActivity.this.sendBroadcast(mIntent);
					return;
				}else if("1210".equals(msg.getResCode())){
					LogUtil.promptInfo(DormitoryRepairActivity.this, ErrorCodeContrast.getErrorCode(msg.getResCode(), res));	
					Intent mIntent = new Intent(BroadcastNotice.WIPE_USET);
					sendBroadcast(mIntent);
					return;
				}else if (!"1".equals(msg.getResCode())) {
					LogUtil.promptInfo(DormitoryRepairActivity.this, ErrorCodeContrast.getErrorCode(msg.getResCode(), res));	
					return;
				}
				
				// 判断消息体是否为空
				if (null != msg.getMessage() && !"".equals(msg.getMessage().trim())) {		 
					// 获取解密后并校验后的值
					String decode = EncryptUtil.getDecodeData(msg.getMessage(), BaseApp.key);
					Message msgs = new Message();
					msgs.what = 1;
					msgs.obj = decode;
					mHandler.sendMessage(msgs);
				} 
			} else {
				LogUtil.promptInfo(DormitoryRepairActivity.this, ErrorCodeContrast.getErrorCode("0", res));
			}
		}

		@Override
		public void onFailure(HttpException error, String msg) {
			if (1 == flag) {
				DialogUtil.closeDialog();
			}else{
				stopLoad();
			}
			if(null != msg && msg.equals(LocalConstants.API_KEY)){
				LogUtil.promptInfo(DormitoryRepairActivity.this, ErrorCodeContrast.getErrorCode("1207", res));
			}else{
				LogUtil.promptInfo(DormitoryRepairActivity.this, ErrorCodeContrast.getErrorCode("0", res));
			}
		}
	};
	
		// 刷新
		@Override
		public void onRefresh() {
			flag = 2;
			pageIndex = 1;
			String info = receiveMailQuest();
			requestServer(LocalConstants.DORMITORY_REPAIR_SERVER, info);
		}

		// 加载更多
		@Override
		public void onLoadMore() {
			flag = 3;
			pageIndex++;
			String info = receiveMailQuest();
			requestServer(LocalConstants.DORMITORY_REPAIR_SERVER, info);;
		}
		
		// 停止刷新
		private void stopLoad() {
			lv_show.stopRefresh();
			lv_show.stopLoadMore();
			lv_show.setRefreshTime(res.getString(R.string.just));
		}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.btn_back:
	            finish();
				break;
			case R.id.tv_sech:
				Intent eIntent = new Intent(DormitoryRepairActivity.this, AddDormitoryActivity.class);
				startActivity(eIntent);
				break;
		}		
	}
}