package com.k2.mobile.app.model.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import com.alibaba.fastjson.JSON;
import com.k2.mobile.app.R;
import com.k2.mobile.app.common.config.BroadcastNotice;
import com.k2.mobile.app.common.config.HttpConstants;
import com.k2.mobile.app.common.exception.HttpException;
import com.k2.mobile.app.controller.core.BaseApp;
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
import com.k2.mobile.app.view.dynamicgrid.BaseDynamicGridAdapter;

public class CheeseDynamicAdapter extends BaseDynamicGridAdapter {
	
	// 根据这个变量来判断是否显示删除图标，true是显示，false是不显示
	private boolean isShowDelete;
	private int positions = 0;
	private HomeMenuBean beans = null;
		
    public CheeseDynamicAdapter(Context context, List<?> items, int columnCount) {
        super(context, items, columnCount);
    }

    public void setIsShowDelete(boolean isShowDelete) {
		this.isShowDelete = isShowDelete;
		notifyDataSetChanged();
	}
    
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_home_grid, null);
        ImageView item_ico = (ImageView) convertView.findViewById(R.id.iv_item);
        TextView item_name = (TextView) convertView.findViewById(R.id.tv_item);
        ImageView iv_delete = (ImageView) convertView.findViewById(R.id.iv_delete);
        
        final HomeMenuBean bean = (HomeMenuBean)getItem(position);
        item_name.setText(bean.getMenuNmae());
        
        if(null != bean.getIsBlank() && "false".equals(bean.getIsBlank())){
			item_ico.setBackgroundResource(bean.getMenuIcons());
		}else{
			if(null != bean.getIcoURL() && !"".equals(bean.getIcoURL())){
				item_ico.setBackgroundResource(R.drawable.ico_submit_repair_n);
			}else{
				item_ico.setBackgroundResource(R.drawable.ico_submit_repair_n);
			}
		}
        
    	if (null != bean && HttpConstants.ADD.equals(bean.getMenuCode())) {
    		 iv_delete.setVisibility(View.GONE);								// 设置删除按钮是否显示
		}else{
			 iv_delete.setVisibility(isShowDelete ? View.VISIBLE : View.GONE);	// 设置删除按钮是否显示
		}
    	
    	iv_delete.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				positions = position;
				beans = bean;
				if (!NetWorkUtil.isNetworkAvailable(mContext)) {            
					DialogUtil.showLongToast(mContext, R.string.global_network_disconnected);
				} else {
					List<String> cList = new ArrayList<String>();
					cList.add(bean.getMenuCode());
					String json = remoteDataRequest(cList);
					SendRequest.submitRequest(mContext, json, submitCallBack);
				}
			}
		});
    	
        return convertView;
    }

    /**
	 * 方法名: deleteMenu()  
	 * 功 能 : 删除菜单
	 * 参 数 : void 
	 * 返回值: String JSON请求数据
	 */
	protected String remoteDataRequest(List<String> code) {
		
		StringBuffer bf = new StringBuffer();
		for (int i = 0; i < code.size(); i++) {
			bf.append(code.get(i));
			bf.append(",");
		}
		
		String resData = bf.substring(0, bf.length() - 1);
		
		ReqBodyBean bBean = new ReqBodyBean();
		bBean.setInvokeFunctionCode("F20000004");
		bBean.setInvokeParameter("[\""+resData+"\"]");
				
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
			DialogUtil.showWithCancelProgressDialog(mContext, null, mContext.getResources().getString(R.string.global_prompt_message), null);
		}

		@Override
		public void onLoading(long total, long current, boolean isUploading) {

		}
		
		@Override
		public void onSuccess(ResponseInfo<String> responseInfo) {                                     
			DialogUtil.closeDialog();
			String result = responseInfo.result.toString();
			byte[] ebase64 = EncryptUtil.decodeBase64(result);
			System.out.println("ebase64 = "+ new String(ebase64));
			if (null != result && !"".equals(result.trim())) {
				PublicResultBean resMsg = JSON.parseObject(new String(ebase64), PublicResultBean.class);
				// 判断返回标识状态是否为空
				if (null == resMsg || null == resMsg.getResHeader().getStateCode() || "".equals(resMsg.getResHeader().getStateCode())) {				
					LogUtil.promptInfo(mContext, ErrorCodeContrast.getErrorCode("0", mContext.getResources()));
					return;
				}else if (!"1".equals(resMsg.getResHeader().getStateCode())) {
					LogUtil.promptInfo(mContext, resMsg.getResHeader().getReturnMsg());	
					return;
				}
				// 判断消息体是否为空
				if (null == resMsg.getResBody().getResultString() || "".equals(resMsg.getResBody().getResultString().trim())) {		 
					LogUtil.promptInfo(mContext, ErrorCodeContrast.getErrorCode("0", mContext.getResources()));
				} else {
					if(null != resMsg.getResHeader().getSectetKey() && !"".equals(resMsg.getResHeader().getSectetKey().trim())){
						BaseApp.key = resMsg.getResHeader().getSectetKey();
					}
					
					if("1".equals(resMsg.getResBody().getResultString().trim())){
						remove(beans);
						Intent intent = new Intent(BroadcastNotice.HOME_DEL_UPDATE);    
						intent.putExtra("position", positions);
						mContext.sendBroadcast(intent);
					}
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
    
    private class CheeseViewHolder {
        public TextView item_name;
        public ImageView item_ico;
        public ImageView iv_delete;
    }
}