package com.k2.mobile.app.controller.activity.menu.hr;

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

import com.alibaba.fastjson.JSON;
import com.k2.mobile.app.R;
import com.k2.mobile.app.common.config.LocalConstants;
import com.k2.mobile.app.common.exception.HttpException;
import com.k2.mobile.app.controller.core.BaseActivity;
import com.k2.mobile.app.controller.core.BaseApp;
import com.k2.mobile.app.model.adapter.LConsultationAdapter;
import com.k2.mobile.app.model.bean.PopularContentBean;
import com.k2.mobile.app.model.bean.PublicRequestBean;
import com.k2.mobile.app.model.bean.PublicResultBean;
import com.k2.mobile.app.model.bean.ReqBodyBean;
import com.k2.mobile.app.model.http.ResponseInfo;
import com.k2.mobile.app.model.http.callback.RequestCallBack;
import com.k2.mobile.app.model.http.other.SendRequest;
import com.k2.mobile.app.utils.DialogUtil;
import com.k2.mobile.app.utils.EncryptUtil;
import com.k2.mobile.app.utils.ErrorCodeContrast;
import com.k2.mobile.app.utils.LogUtil;
import com.k2.mobile.app.utils.PublicResHeaderUtils;
import com.k2.mobile.app.view.widget.XListView;
import com.k2.mobile.app.view.widget.XListView.IXListViewListener;

import java.util.ArrayList;
import java.util.List;

/**
* @Title BenefitLifeActivity.java
* @Package com.oppo.mo.controller.activity.benefitLife;
* @Description HR助手－静态页
* @Company  K2
* 
* @author HRHelperActivity
* @date 2015-5-5 下午20:05:57
* @version V1.0
*/
@SuppressLint("NewApi")
public class LConsultationActivity extends BaseActivity implements IXListViewListener, OnClickListener, OnItemClickListener {
	
	// 头部标题与搜索
	private TextView tv_title, tv_sech;
	// 返回
	private Button btn_back;
	private XListView lv_show;
	
	private LConsultationAdapter lcAdapter = null;
	private List<PopularContentBean> hList;
	private int page = 1;
    private int pageSize = 10;
	
    private Handler mHandler = new Handler(){
		@Override
		public void dispatchMessage(Message msg) {
			switch(msg.what){
				case 1:
					String json = (String) msg.obj;
					if(null != json){
						hList.clear();
						hList.addAll(JSON.parseArray(json, PopularContentBean.class));
						initAdapter();
					}
					
					break;
			}
		}
    };
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);	// 去除头部
		setContentView(R.layout.activity_add_lively_consultation);
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
		
		lv_show.setPullRefreshEnable(false);		// 设置下拉更新
		lv_show.setPullLoadEnable(false);		// 设置让它上拉不更新
		
	}
	
	/**
	 * 方法名: initListener() 
	 * 功 能 : 初始化监听器
	 * 参 数 : void 
	 * 返回值: void
	 */
	private void initListener() {
		btn_back.setOnClickListener(this);
		lv_show.setXListViewListener(this);
		lv_show.setOnItemClickListener(this);
	}
	
	private void logic(){
		
		String typeCode = getIntent().getStringExtra("typeCode");
		String title = getIntent().getStringExtra("title");
		
		tv_title.setText(title);	
		tv_sech.setVisibility(View.GONE);	
		
		hList = new ArrayList<PopularContentBean>();
		// 获取用户信息
		String info = getMenu(typeCode);
		if (null != info) {
			SendRequest.submitRequest(LConsultationActivity.this, info, submitCallBack);
		}
	}

	private void initAdapter(){
		lcAdapter = new LConsultationAdapter(this, hList);
		lv_show.setAdapter(lcAdapter);
	}
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.btn_back:	// 返回
				finish();
				break;
		}
	}
	
	private String getMenu(String typeCode){
		ReqBodyBean bBean = new ReqBodyBean();
		bBean.setInvokeFunctionCode("F50000002");
		bBean.setInvokeParameter("[\""+ typeCode +"\", \""+ pageSize +"\", \""+ page +"\"]");
				
		PublicRequestBean bean = new PublicRequestBean();
		bean.setReqHeader(PublicResHeaderUtils.getReqHeader());
		bean.setReqBody(bBean);
		
		return JSON.toJSONString(bean);
	}
	
	/**
	 * http请求回调
	 */
	RequestCallBack<String> submitCallBack = new RequestCallBack<String>() {

		@Override
		public void onStart() {
			DialogUtil.showWithCancelProgressDialog(LConsultationActivity.this, null, res.getString(R.string.global_prompt_message), null);
		}

		@Override
		public void onLoading(long total, long current, boolean isUploading) {

		}
		
		@Override
		public void onSuccess(ResponseInfo<String> responseInfo) {                                     
			DialogUtil.closeDialog();
			String result = responseInfo.result.toString();
			byte[] ebase64 = EncryptUtil.decodeBase64(result);
			System.out.println("ebase64 = " + new String(ebase64));
			if (null != result && !"".equals(result.trim())) {
				PublicResultBean resMsg = JSON.parseObject(new String(ebase64), PublicResultBean.class);
				// 判断返回标识状态是否为空
				if (null == resMsg || null == resMsg.getResHeader().getStateCode() || "".equals(resMsg.getResHeader().getStateCode())) {				
					LogUtil.promptInfo(LConsultationActivity.this, ErrorCodeContrast.getErrorCode("0", res));
					return;
				// 验证不合法
				}else if (!"1".equals(resMsg.getResHeader().getStateCode())) {
					LogUtil.promptInfo(LConsultationActivity.this, resMsg.getResHeader().getReturnMsg());	
					return;
				}
				// 判断消息体是否为空
				if (null == resMsg.getResBody().getResultString() || "".equals(resMsg.getResBody().getResultString().trim())) {		 
					LogUtil.promptInfo(LConsultationActivity.this, ErrorCodeContrast.getErrorCode("0", res));
				} else {
					if(null != resMsg.getResHeader().getSectetKey() && !"".equals(resMsg.getResHeader().getSectetKey().trim())){
						BaseApp.key = resMsg.getResHeader().getSectetKey();
					}
					// 获取解密后并校验后的值
					Message msgs = new Message();
					msgs.what = 1;
					msgs.obj = resMsg.getResBody().getResultString();
					mHandler.sendMessage(msgs);
				}
			} else {
				LogUtil.promptInfo(LConsultationActivity.this, ErrorCodeContrast.getErrorCode("0", res));
			}		
		}

		@Override
		public void onFailure(HttpException error, String msg) {
			DialogUtil.closeDialog();
			if(null != msg && msg.equals(LocalConstants.API_KEY)){
				LogUtil.promptInfo(LConsultationActivity.this, ErrorCodeContrast.getErrorCode("1207", res));
			}else{
				LogUtil.promptInfo(LConsultationActivity.this, ErrorCodeContrast.getErrorCode("0", res));
			}
		}
	};	
	

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		PopularContentBean bean = hList.get(position - 1);
		Intent mIntent = new Intent(LConsultationActivity.this, HRStaticLinkListActivity.class);
		mIntent.putExtra("title", bean.getPopularTitle());
		mIntent.putExtra("popularCode", bean.getPopularCode());
		startActivity(mIntent);
	}

	@Override
	public void onRefresh() {
	}

	@Override
	public void onLoadMore() {
		// TODO Auto-generated method stub
		
	}
}
