package com.k2.mobile.app.model.adapter;

import java.util.ArrayList;
import java.util.List;

import com.alibaba.fastjson.JSON;
import com.k2.mobile.app.R;
import com.k2.mobile.app.common.config.BroadcastNotice;
import com.k2.mobile.app.common.config.LocalConstants;
import com.k2.mobile.app.common.exception.HttpException;
import com.k2.mobile.app.controller.core.BaseApp;
import com.k2.mobile.app.model.bean.HomeMenuBean;
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
import com.k2.mobile.app.utils.LogUtil;
import com.k2.mobile.app.utils.NetWorkUtil;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * @Title MyGridViewAdapter.java
 * @Package com.oppo.mo.model.adapter
 * @Description 适配首页图标
 * @Company  K2
 * 
 * @author liangzy
 * @date 2015-03-18 20:43:00
 * @version V1.0
 */
public class MyGridViewAdapter extends BaseAdapter {
	
	private Context mContext;
	private ImageView item_ico;
	private TextView item_name;
	private ImageView iv_delete;
	// 根据这个变量来判断是否显示删除图标，true是显示，false是不显示
	private boolean isShowDelete;
	private List<HomeMenuBean> hList;
	private int positions = 0;
	
	private Handler mHandler = new Handler() {
		@Override
		public void dispatchMessage(Message msg) {
			switch (msg.what) {
			case 1:
				String json = (String)msg.obj;
				if (null != json) {
					ResPublicBean bean = JSON.parseObject(json, ResPublicBean.class);
					if(null != bean.getSuccess() && "1".equals(bean.getSuccess())){
						Intent intent = new Intent(BroadcastNotice.HOME_DEL_UPDATE);    
						intent.putExtra("position", positions);
						mContext.sendBroadcast(intent);
					}
				} else {
					DialogUtil.showLongToast(mContext, R.string.delete_failed);
				}
				break;
			}
		};
	};

	public MyGridViewAdapter(Context mContext, List<HomeMenuBean> hList) {
		this.mContext = mContext;
		this.hList = hList;
	}

	public void setIsShowDelete(boolean isShowDelete) {
		this.isShowDelete = isShowDelete;
		notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		return null == hList ? 0 : hList.size();
	}

	@Override
	public Object getItem(int position) {
		return hList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}
	
	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
			convertView = LayoutInflater.from(mContext).inflate(R.layout.item_home_grid, null);
			item_ico = (ImageView) convertView.findViewById(R.id.iv_item);
			item_name = (TextView) convertView.findViewById(R.id.tv_item);
			iv_delete = (ImageView) convertView.findViewById(R.id.iv_delete);
			
			final HomeMenuBean hBean = hList.get(position);
			
			if (null != hBean && "1".equals(hBean.getMenuType())) {
				// 设置删除按钮是否显示
				iv_delete.setVisibility(View.GONE);
			}else{
				// 设置删除按钮是否显示
				iv_delete.setVisibility(isShowDelete ? View.VISIBLE : View.GONE);
			}
			
			iv_delete.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					positions = position;
					if (!NetWorkUtil.isNetworkAvailable(mContext)) {            
						DialogUtil.showLongToast(mContext, R.string.global_network_disconnected);
					} else {
						List<String> cList = new ArrayList<String>();
						cList.add(hBean.getMenuCode());
						String json = remoteDataRequest(cList);
						SendRequest.sendSubmitRequest(mContext, json, BaseApp.token, BaseApp.reqLang, LocalConstants.MUNE_COMMONLY_DEL_SERVER, BaseApp.key, submitCallBack);
					}
				}
			});
			
			item_ico.setBackgroundResource(hBean.getMenuIcons());
			item_name.setText(hBean.getMenuNmae());
			
		return convertView;
	}

	/**
	 * 方法名: deleteMenu()  
	 * 功 能 : 删除菜单
	 * 参 数 : void 
	 * 返回值: String JSON请求数据
	 */
	protected String remoteDataRequest(List<String> code) {
		PublicBean bean = new PublicBean();
		bean.setDeviceId(BaseApp.macAddr);
		bean.setUserAccount(BaseApp.user.getUserId());
		bean.setMenuCode(code);
		String json = JSON.toJSONString(bean);
		return json;
	}
	
	/**
	 * http请求回调
	 */
	RequestCallBack<String> submitCallBack = new RequestCallBack<String>() {

		@Override
		public void onStart() {
			DialogUtil.showWithCancelProgressDialog(mContext, null, mContext.getResources().getString(R.string.global_prompt_message), null);
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
					LogUtil.promptInfo(mContext, ErrorCodeContrast.getErrorCode("0", mContext.getResources()));
					return;
				}else if ("1103".equals(msgs.getResCode()) || "1104".equals(msgs.getResCode())) {
					Intent mIntent = new Intent(BroadcastNotice.USER_EXIT);
					mContext.sendBroadcast(mIntent);
					return;
				}else if("1210".equals(msgs.getResCode())){
					LogUtil.promptInfo(mContext, ErrorCodeContrast.getErrorCode(msgs.getResCode(), mContext.getResources()));	
					Intent mIntent = new Intent(BroadcastNotice.WIPE_USET);
					mContext.sendBroadcast(mIntent);
					return;
				}else if ("1".equals(msgs.getResCode())) {
					// 获取解密后并校验后的值
					String decode = EncryptUtil.getDecodeData(msgs.getMessage(), BaseApp.key);
					Message msg = new Message();
					msg.what = 1;
					msg.obj = decode; 
					mHandler.sendMessage(msg);
				}
			} else {
				LogUtil.promptInfo(mContext, ErrorCodeContrast.getErrorCode("0", mContext.getResources()));
			}
		}

		@Override
		public void onFailure(HttpException error, String msg) {
			DialogUtil.closeDialog();
			LogUtil.promptInfo(mContext, ErrorCodeContrast.getErrorCode("0", mContext.getResources()));
		}
	};
	
}
