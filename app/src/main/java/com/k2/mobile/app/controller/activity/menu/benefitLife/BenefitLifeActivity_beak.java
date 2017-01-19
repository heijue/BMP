package com.k2.mobile.app.controller.activity.menu.benefitLife;

import java.util.ArrayList;
import java.util.List;

import com.alibaba.fastjson.JSON;
import com.k2.mobile.app.R;
import com.k2.mobile.app.common.config.BroadcastNotice;
import com.k2.mobile.app.common.config.HttpConstants;
import com.k2.mobile.app.common.config.LocalConstants;
import com.k2.mobile.app.common.config.MenuConstants;
import com.k2.mobile.app.common.exception.HttpException;
import com.k2.mobile.app.controller.core.BaseActivity;
import com.k2.mobile.app.controller.core.BaseApp;
import com.k2.mobile.app.model.adapter.BenefitLifeAdapter;
import com.k2.mobile.app.model.bean.HomeMenuBean;
import com.k2.mobile.app.model.bean.PublicBean;
import com.k2.mobile.app.model.bean.ReqMessage;
import com.k2.mobile.app.model.http.ResponseInfo;
import com.k2.mobile.app.model.http.callback.RequestCallBack;
import com.k2.mobile.app.utils.DialogUtil;
import com.k2.mobile.app.utils.EncryptUtil;
import com.k2.mobile.app.utils.ErrorCodeContrast;
import com.k2.mobile.app.utils.FastJSONUtil;
import com.k2.mobile.app.utils.LogUtil;
import com.k2.mobile.app.view.widget.XListView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.TextView;

/**
* @Title BenefitLifeActivity.java
* @Package com.oppo.mo.controller.activity.benefitLife;
* @Description 惠生活－静态页
* @Company  K2
* 
* @author linqijun
* @date 2015-5-5 下午20:05:57
* @version V1.0
*/
@SuppressLint("NewApi")
public class BenefitLifeActivity_beak extends BaseActivity implements OnClickListener, OnItemClickListener {
	
	// 头部标题与搜索
	private TextView tv_title, tv_sech;
	// 返回
	private Button btn_back;
	// 显示菜单
	private XListView lv_show ;
	// 菜单适配器
	private BenefitLifeAdapter mAdapter;
	// 存放数据集合 
	private List<HomeMenuBean> hList = null;
	private Intent mIntent = null;
	
	private Handler mHandler = new Handler(){
		@Override
		public void dispatchMessage(Message msg) {
			switch (msg.what) {
			case 1:
				String json = (String) msg.obj;
				if (null != json) {
					hList.clear();
					List<HomeMenuBean> hbList = JSON.parseArray(json, HomeMenuBean.class);
					if(null != hbList) {
						for(int i = 0; i<hbList.size(); i++) {
							HomeMenuBean hmBean = hbList.get(i);
							for(int j=0; j<MenuConstants.menuCode.length; j++) {
								if (MenuConstants.menuCode[j].contains(hmBean.getMenuCode().trim())) {
									if (hmBean.getMenuCode().trim().equals(MenuConstants.menuCode[j])) {
										hmBean.setMenuType("2");
										hmBean.setMenuIcons(MenuConstants.icons[j]);
										hmBean.setMenuNmae(getString(MenuConstants.names[j]));
									}
								}
							}
							hList.add(hmBean);
						}
					}

					mAdapter.notifyDataSetChanged();
				}
				break;
			}
		}
	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);	// 去除头部
		setContentView(R.layout.activity_benefit_life);
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
		btn_back = (Button) findViewById(R.id.btn_back);
		tv_title = (TextView) findViewById(R.id.tv_title);
		tv_sech = (TextView) findViewById(R.id.tv_sech);
		lv_show = (XListView) findViewById(R.id.lv_show);
		
		tv_title.setText(getString(R.string.benefit_life));	
		tv_sech.setVisibility(View.GONE);	 
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
		lv_show.setOnItemClickListener(this);
	}
	
	private void logic(){
		hList = new ArrayList<HomeMenuBean>();
		initData();
		
		mAdapter = new BenefitLifeAdapter(this, hList);
		lv_show.setAdapter(mAdapter);
		
		lv_show.setPullRefreshEnable(false);		// 设置下拉更新
		lv_show.setPullLoadEnable(false);			// 设置让它上拉更新
		
//		// 查询用户菜单
//		if (!NetWorkUtil.isNetworkAvailable(this)) {            
//			DialogUtil.showLongToast(this, R.string.global_network_disconnected);
//		}else{
//			String json = remoteDataRequest();// 
//			SendRequest.sendSubmitRequest(this, json, BaseApp.token, BaseApp.reqLang, 
//					LocalConstants.MUNE_SUB_LIST_SERVER, BaseApp.key, submitCallBack);
//		}
	}
	
	private void initData(){
		
		HomeMenuBean  hmBean = new HomeMenuBean();
		hmBean.setMenuCode(HttpConstants.BUS_TIMETABLE);
		hmBean.setMenuIcons(R.drawable.ico_bus_timetable);
		hmBean.setMenuNmae(getString(R.string.bus_timetable));
		hList.add(hmBean);
		
		HomeMenuBean  hotelBean = new HomeMenuBean();
		hotelBean.setMenuCode(HttpConstants.GHOTEL);
		hotelBean.setMenuIcons(R.drawable.ico_hotel);
		hotelBean.setMenuNmae(getString(R.string.granducato_hotel));
		hList.add(hotelBean);
		
		HomeMenuBean  sBean = new HomeMenuBean();
		sBean.setMenuCode(HttpConstants.SPORTS);
		sBean.setMenuIcons(R.drawable.ico_sports);
		sBean.setMenuNmae(getString(R.string.sports));
		hList.add(sBean);
		
		HomeMenuBean  skBean = new HomeMenuBean();
		skBean.setMenuCode(HttpConstants.BANDS);
		skBean.setMenuIcons(R.drawable.ico_beer_and_skittles);
		skBean.setMenuNmae(getString(R.string.beer_and_skittles));
		hList.add(skBean);
		
		HomeMenuBean  ntBean = new HomeMenuBean();
		ntBean.setMenuCode(HttpConstants.NETWORK);
		ntBean.setMenuIcons(R.drawable.ico_network);
		ntBean.setMenuNmae(getString(R.string.network_correlation));
		hList.add(ntBean);
		
		HomeMenuBean  smBean = new HomeMenuBean();
		smBean.setMenuCode(HttpConstants.SAFETY);
		smBean.setMenuIcons(R.drawable.ico_safety);
		smBean.setMenuNmae(getString(R.string.safety_management));
		hList.add(smBean);
		
		HomeMenuBean  cmBean = new HomeMenuBean();
		cmBean.setMenuCode(HttpConstants.CERTIFICATE);
		cmBean.setMenuIcons(R.drawable.ico_certificate);
		cmBean.setMenuNmae(getString(R.string.certificate_manage));
		hList.add(cmBean);
	} 
	/**
	 * 方法名: remoteDataRequest()  
	 * 功 能 : 请求数据
	 * 参 数 : void 
	 * 返回值: String JSON请求数据
	 * @return 
	 */
	private String remoteDataRequest(){
		PublicBean bean = new PublicBean();
		bean.setUserAccount(BaseApp.user.getUserId());
		bean.setParentCode(HttpConstants.BENEFIT_LIFE);
		
		return JSON.toJSONString(bean);
	}
	
	/**
	 * http请求回调
	 */
	RequestCallBack<String> submitCallBack = new RequestCallBack<String>() {

		@Override
		public void onStart() {
			DialogUtil.showWithCancelProgressDialog(BenefitLifeActivity_beak.this, null, getResources().getString(R.string.global_prompt_message), null);
		}

		@Override
		public void onLoading(long total, long current, boolean isUploading) {

		}
		
		@Override
		public void onSuccess(ResponseInfo<String> responseInfo) {                                     
			DialogUtil.closeDialog();
			String result = responseInfo.result.toString();
			if (null != result && !"".equals(result.trim())) {
				ReqMessage msgs = FastJSONUtil.getJSONToEntity(result, ReqMessage.class);
				// 判断返回标识状态是否为空
				if (null == msgs || null == msgs.getResCode() || "".equals(msgs.getResCode())) {				
					LogUtil.promptInfo(BenefitLifeActivity_beak.this, ErrorCodeContrast.getErrorCode("0", getResources()));
					return;
				}else if ("1103".equals(msgs.getResCode()) || "1104".equals(msgs.getResCode())) {
					LogUtil.promptInfo(BenefitLifeActivity_beak.this, ErrorCodeContrast.getErrorCode(msgs.getResCode(), getResources()));
					Intent mIntent = new Intent(BroadcastNotice.USER_EXIT);
					sendBroadcast(mIntent);
					return;
				}else if("1210".equals(msgs.getResCode())){
					LogUtil.promptInfo(BenefitLifeActivity_beak.this, ErrorCodeContrast.getErrorCode(msgs.getResCode(), getResources()));	
					Intent mIntent = new Intent(BroadcastNotice.WIPE_USET);
					sendBroadcast(mIntent);
					return;
				}else if ("1".equals(msgs.getResCode())) {
					if(null != msgs.getMessage() && !"".equals(msgs.getMessage().trim())){
						// 获取解密后并校验后的值
						String decode = EncryptUtil.getDecodeData(msgs.getMessage(), BaseApp.key);
						Message msg = new Message();
						msg.what = 1;
						msg.obj = decode; 
						mHandler.sendMessage(msg);
					}
				}
			} else {
				LogUtil.promptInfo(BenefitLifeActivity_beak.this, ErrorCodeContrast.getErrorCode("0", getResources()));
			}
		}

		@Override
		public void onFailure(HttpException error, String msg) {
			DialogUtil.closeDialog();
			if(null != msg && msg.equals(LocalConstants.API_KEY)){
				LogUtil.promptInfo(BenefitLifeActivity_beak.this, ErrorCodeContrast.getErrorCode("1207", getResources()));
			}else{
				LogUtil.promptInfo(BenefitLifeActivity_beak.this, ErrorCodeContrast.getErrorCode("0", getResources()));
			}
		}
	};
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.btn_back:	// 返回
				finish();
				break;
		}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		HomeMenuBean bean = hList.get(position - 1);
		int type = 0;
		if (HttpConstants.GHOTEL.equals(bean.getMenuCode())) {						// 住宿旅馆
			// 住宿旅馆
			type = 2;
		} else if (HttpConstants.BANDS.equals(bean.getMenuCode())) {				// 吃喝玩乐
			// 吃喝玩乐
			type = 4;
		} else if (HttpConstants.SAFETY.equals(bean.getMenuCode())) {				// 安全管理
			// 安全管理
			type = 6;
		} else if (HttpConstants.CERTIFICATE.equals(bean.getMenuCode())) {			// 证件办理
			// 证件办理
			type = 7;
		} else if (HttpConstants.BUS_TIMETABLE.equals(bean.getMenuCode())) {		// 班车时刻表
			// 班车时刻表
			type = 1;
		} else if (HttpConstants.SPORTS.equals(bean.getMenuCode())) {				// 体育运动
			// 体育运动
			type = 3;
		} else if (HttpConstants.NETWORK.equals(bean.getMenuCode())) {				// 网络相关
			// 网络相关
			type = 5;
		} 
		
		if(0 < type){
			mIntent = new Intent(BenefitLifeActivity_beak.this, StaticLinkListActivity.class);
			mIntent.putExtra("type", type);
			startActivity(mIntent);
		}
	}	
}
