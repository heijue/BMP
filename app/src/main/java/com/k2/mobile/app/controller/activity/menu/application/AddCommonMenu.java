package com.k2.mobile.app.controller.activity.menu.application;    

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

import com.alibaba.fastjson.JSON;
import com.k2.mobile.app.R;
import com.k2.mobile.app.common.config.BroadcastNotice;
import com.k2.mobile.app.common.config.LocalConstants;
import com.k2.mobile.app.common.config.MenuConstants;
import com.k2.mobile.app.common.exception.HttpException;
import com.k2.mobile.app.controller.core.BaseActivity;
import com.k2.mobile.app.controller.core.BaseApp;
import com.k2.mobile.app.model.adapter.AddCommonMenuAdapter;
import com.k2.mobile.app.model.bean.HomeMenuBean;
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
import com.k2.mobile.app.utils.NetWorkUtil;
import com.k2.mobile.app.utils.PublicResHeaderUtils;
import com.k2.mobile.app.view.widget.XListView;
import com.k2.mobile.app.view.widget.XListView.IXListViewListener;
  
public class AddCommonMenu extends BaseActivity implements IXListViewListener, OnClickListener{

	// 返回
	private Button btn_back;
	// 头部标题
	private TextView tv_title;
	private TextView tv_search;
	private XListView lv_show;
	// 主要中保存当前页菜单
	private List<HomeMenuBean> hList = null;
	private AddCommonMenuAdapter adapter = null; 
	private int flag = 1;
	
	private Handler mHandler = new Handler(){
		@Override
		public void dispatchMessage(Message msg) {
			switch (msg.what) {
				case 1:
					String jsons = (String) msg.obj;
					System.out.println("add jsons = "+jsons);
					if(null != jsons){
						List<HomeMenuBean> allList = JSON.parseArray(jsons, HomeMenuBean.class);
						if(null != allList && 0 < allList.size()){
							checkMenu(allList);
						}
					}
					break;
				case 2:
					String res = (String) msg.obj;
					if (null != res) {
						if(null != res && "1".equals(res)){
							DialogUtil.showLongToast(AddCommonMenu.this, R.string.add_menu_successd);
							ArrayList<HomeMenuBean> addList = new ArrayList<HomeMenuBean>();
							for (int i = 0; i < hList.size(); i++) {
								if (AddCommonMenuAdapter.getIsSelected().get(i)) {
									addList.add(hList.get(i));
								}
							}
							Intent mIntent = new Intent(BroadcastNotice.ADD_UPDATE_COMMOM);
							mIntent.putExtra("updateCommon", addList);
							sendBroadcast(mIntent);
							finish();
						} else {
							DialogUtil.showLongToast(AddCommonMenu.this, R.string.add_failed);
						}
					}
					break;
			}
		};
	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_common_menu);
		initView();						// 初始化UI
		initListener();					// 初始化监听器
	}
	
	@SuppressLint("NewApi")
	private void initView(){
		tv_title = (TextView) findViewById(R.id.tv_title);
		tv_search = (TextView) findViewById(R.id.tv_sech);
		btn_back = (Button) findViewById(R.id.btn_setting);
		lv_show = (XListView) findViewById(R.id.lv_show);
		
		lv_show.setPullRefreshEnable(true);		// 设置下拉更新
		lv_show.setPullLoadEnable(false);		// 设置让它上拉不更新
		
		tv_title.setText(getString(R.string.add_common_menu));
		tv_search.setText(getString(R.string.save));
		
		// 设置tv_search背景图片和样式
		Drawable drawable = getResources().getDrawable(R.drawable.background_button_frame);
		// setBackground()方法，在16版本以上才有
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
			tv_search.setBackground(drawable);
		} else {
			tv_search.setBackgroundDrawable(drawable);
		}
		// 重新计算tv_search的大小
		int w = View.MeasureSpec.makeMeasureSpec(0,View.MeasureSpec.UNSPECIFIED);  
		int h = View.MeasureSpec.makeMeasureSpec(0,View.MeasureSpec.UNSPECIFIED);  

		tv_search.measure(w, h);  
		int height = tv_search.getMeasuredHeight();  
		int width = tv_search.getMeasuredWidth();  

		LayoutParams lp = tv_search.getLayoutParams();    
		lp.width = (int)(width*1.3);
		lp.height = (int)(height*1.2);        
		tv_search.setLayoutParams(lp);
		
		hList = new ArrayList<HomeMenuBean>();
		
		String info = getMenu();
		getAllMenu(info);
	}
	
	private void initListener(){
		btn_back.setOnClickListener(this);
		tv_search.setOnClickListener(this);
//		lv_show.setOnItemClickListener(itemListener);
		lv_show.setXListViewListener(this);
	}

	// 实现弹出动画效果
	private OnItemClickListener itemListener = new OnItemClickListener() {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//			int positions = position - 1;
			boolean val = AddCommonMenuAdapter.getIsSelected().get(position);
			AddCommonMenuAdapter.getIsSelected().put(position, !val);
			adapter.notifyDataSetChanged();
		}
	};
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.btn_setting:		// 退出
				finish();
				break;
			case R.id.tv_sech:			// 完成
				flag = 3;
				String info = addMenuJson();
				getAllMenu(info);
				break;
			default:
				break;
		}
	}
	
	/**
	 * @Title: checkMenu
	 * @Description: 获取菜单
	 * @return void 
	 * @throws
	 */
	private void checkMenu(List<HomeMenuBean> allList) {
		hList.clear();
		for (int i = 0; i<allList.size(); i++) {
			HomeMenuBean hmBean = allList.get(i);
			if(null != hmBean.getIsBlank() && "false".equals(hmBean.getIsBlank())){
				for(int j=0; j<MenuConstants.menuCode.length; j++) {
					if (MenuConstants.menuCode[j].contains(hmBean.getMenuCode().trim())) {
						if (hmBean.getMenuCode().trim().equals(MenuConstants.menuCode[j])) {
							hmBean.setMenuIcons(MenuConstants.icons[j]);
							hmBean.setMenuNmae(AddCommonMenu.this.getString(MenuConstants.names[j]));
						}
						hList.add(hmBean);
					}
				}
			}else if(null != hmBean.getIsBlank() && "true".equals(hmBean.getIsBlank())){
				hmBean.setMenuNmae(hmBean.getMenuChsName());
				hList.add(hmBean);
			}
//			for(int j=0; j<MenuConstants.menuCode.length; j++) {
//				if (MenuConstants.menuCode[j].contains(hmBean.getMenuCode().trim())) {
//					if (hmBean.getMenuCode().trim().equals(MenuConstants.menuCode[j])) {
//						hmBean.setMenuIcons(MenuConstants.icons[j]);
//						hmBean.setMenuNmae(AddCommonMenu.this.getString(MenuConstants.names[j]));
//					}
//					hList.add(hmBean);
//				}
//			}
		}
		
		// 判断常用菜单是否已添加,已添加过的则不允许再添加
		if(null != BaseApp.hList){
			for(int i=0;i<BaseApp.hList.size();i++){
				for(int j=0;j<hList.size();j++){
					if(null != hList.get(j).getMenuCode() && null != BaseApp.hList.get(i).getMenuCode() && hList.get(j).getMenuCode().equals(BaseApp.hList.get(i).getMenuCode())){
						hList.remove(j);
					}
				}
			}
		}
				
		adapter = new AddCommonMenuAdapter(AddCommonMenu.this, hList);
		lv_show.setAdapter(adapter);
		
	}
	
	/**
	 * 方法名: getMenu()  
	 * 功 能 : 请求数据
	 * 参 数 : void 
	 * 返回值: String JSON请求数据
	 */
	public String getMenu() {
		ReqBodyBean bBean = new ReqBodyBean();
		bBean.setInvokeFunctionCode("F20000001");
		bBean.setInvokeParameter("[\"24198966f5104d4dba76b8e3d64a9468\"]");
				
		PublicRequestBean bean = new PublicRequestBean();
		bean.setReqHeader(PublicResHeaderUtils.getReqHeader());
		bean.setReqBody(bBean);
		
		return JSON.toJSONString(bean);
	}
	
	/**
	 * 方法名: addMenu()  
	 * 功 能 : 请求数据
	 * 参 数 : void 
	 * 返回值: String JSON请求数据
	 */
	private String addMenuJson() {
		StringBuffer bf = new StringBuffer();
		for (int i = 0; i < hList.size(); i++) {
			if (AddCommonMenuAdapter.getIsSelected().get(i)) {
				bf.append(hList.get(i).getMenuCode());
				bf.append(",");
			}
		}
		
		if(1 > bf.length()){
			DialogUtil.showLongToast(AddCommonMenu.this, R.string.select_menu);
			return null;
		}
		
		String resData = bf.substring(0, bf.length() - 1);
		
		ReqBodyBean bBean = new ReqBodyBean();
		bBean.setInvokeFunctionCode("F20000003");
		bBean.setInvokeParameter("[\""+resData+"\"]");
				
		PublicRequestBean bean = new PublicRequestBean();
		bean.setReqHeader(PublicResHeaderUtils.getReqHeader());
		bean.setReqBody(bBean);
		
		return JSON.toJSONString(bean);
	}
	
	/**
	 * @Title: getAllMenu
	 * @Description: 获取所有菜单
	 * @param operType 操作类型，1查询，2修改
	 * @return void 返回的数据 
	 * @throws
	 */
	public void getAllMenu(String info) {
		if (!NetWorkUtil.isNetworkAvailable(AddCommonMenu.this)) {            // 判断网络是否连接
			DialogUtil.showLongToast(AddCommonMenu.this, R.string.global_network_disconnected);
			return;
		}
		
		if (null != info) {
			SendRequest.submitRequest(AddCommonMenu.this, info, submitCallBack);
		}
	}
	
	/**
	 * http请求回调
	 */
	RequestCallBack<String> submitCallBack = new RequestCallBack<String>() {

		@Override
		public void onStart() {
			if(1 == flag){
				DialogUtil.showWithCancelProgressDialog(AddCommonMenu.this, null, getResources().getString(R.string.global_prompt_message), null);
			}
		}

		@Override
		public void onLoading(long total, long current, boolean isUploading) {

		}
		
		@Override
		public void onSuccess(ResponseInfo<String> responseInfo) {                                     
			if(2 == flag){
				stopLoad();
			}else{
				DialogUtil.closeDialog();
			}
			String result = responseInfo.result.toString();
			byte[] ebase64 = EncryptUtil.decodeBase64(result);
			if (null != result && !"".equals(result.trim())) {
				PublicResultBean resMsg = JSON.parseObject(new String(ebase64), PublicResultBean.class);
				// 判断返回标识状态是否为空
				if (null == resMsg || null == resMsg.getResHeader().getStateCode() || "".equals(resMsg.getResHeader().getStateCode())) {				
					LogUtil.promptInfo(AddCommonMenu.this, ErrorCodeContrast.getErrorCode("0", res));
					return;
				// 验证不合法
				}else if (!"1".equals(resMsg.getResHeader().getStateCode())) {
					LogUtil.promptInfo(AddCommonMenu.this, resMsg.getResHeader().getReturnMsg());	
					return;
				}
				// 判断消息体是否为空
				if (null == resMsg.getResBody().getResultString() || "".equals(resMsg.getResBody().getResultString().trim())) {		 
					LogUtil.promptInfo(AddCommonMenu.this, ErrorCodeContrast.getErrorCode("0", res));
				} else {
					if(null != resMsg.getResHeader().getSectetKey() && !"".equals(resMsg.getResHeader().getSectetKey().trim())){
						BaseApp.key = resMsg.getResHeader().getSectetKey();
					}
					// 获取解密后并校验后的值
					Message msgs = new Message();
					if(3 == flag){
						msgs.what = 2;
					}else{
						msgs.what = 1;
					}
					msgs.obj = resMsg.getResBody().getResultString();
					mHandler.sendMessage(msgs);
				}
			} else {
				LogUtil.promptInfo(AddCommonMenu.this, ErrorCodeContrast.getErrorCode("0", res));
			}		
		}
		
		@Override
		public void onFailure(HttpException error, String msg) {
			if(2 == flag){
				stopLoad();
			}else{
				DialogUtil.closeDialog();
			}
			
			if(null != msg && msg.equals(LocalConstants.API_KEY)){
				LogUtil.promptInfo(AddCommonMenu.this, ErrorCodeContrast.getErrorCode("1207", getResources()));
			}else{
				LogUtil.promptInfo(AddCommonMenu.this, ErrorCodeContrast.getErrorCode("0", getResources()));
			}
		}
	};

	// 刷新
	@Override
	public void onRefresh() {
		flag = 2;
		String info = getMenu();
		getAllMenu(info);
	}

	// 加载更多
	@Override
	public void onLoadMore() {
		
	}
	
	// 停止刷新
	private void stopLoad() {
		lv_show.stopRefresh();
		lv_show.stopLoadMore();
		lv_show.setRefreshTime(getResources().getString(R.string.just));
	}
}
 