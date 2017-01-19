package com.k2.mobile.app.model.adapter;    

import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.k2.mobile.app.R;
import com.k2.mobile.app.common.config.BroadcastNotice;
import com.k2.mobile.app.common.config.LocalConstants;
import com.k2.mobile.app.common.exception.HttpException;
import com.k2.mobile.app.controller.core.BaseApp;
import com.k2.mobile.app.model.bean.FlowBean;
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
  
/**
 * @Title FlowAdapter.java
 * @Package com.oppo.mo.model.adapter
 * @Description 适配我的流程类
 * @Company  K2
 * 
 * @author liangzy
 * @date 2015-03-18 20:43:00
 * @version V1.0
 */
public class FlowAdapter extends BaseAdapter {

	private Context context;
	private List<FlowBean> list;
	private int type = 4;
	private int positions = 0;
	private int flag = 1;
	private int OpenTyp=-1;
	 
	// 跳转
	private Intent mIntent = null;
	
	public FlowAdapter(Context context, List<FlowBean> list,int OpenType) {
		super();
		this.context = context;
		this.list = list;
		this.OpenTyp=OpenType;
	}

	@Override
	public int getCount() {
		return list == null ? 0 : list.size();
	}

	@Override
	public Object getItem(int position) {
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		convertView = LayoutInflater.from(context).inflate(R.layout.item_mp_pending_approval, null);
		TextView tv_title = (TextView) convertView.findViewById(R.id.tv_title);
		
		TextView tv_n_prepared =null;
		TextView tv_t_time  =null;
		TextView tv_approval_start =null;
		
		TextView tv_t_prepared =null;
		TextView tv_tip_time  =null;
		TextView tv_lab_approval_start =null;
		
		switch(this.OpenTyp)
		{
		case 1:  
			
			  //tv_t_prepared = (TextView) convertView.findViewById(R.id.tv_t_prepared);
			  //tv_t_prepared.setText("申请人:");
			  //tv_t_prepared.setText(context.getResources().getString(R.string.prepared));
			  tv_n_prepared = (TextView) convertView.findViewById(R.id.tv_n_prepared);
			  
			  tv_tip_time = (TextView) convertView.findViewById(R.id.tv_tip_time);
			  //tv_tip_time.setText("到达:");
            tv_tip_time.setText("");
			  tv_t_time = (TextView) convertView.findViewById(R.id.tv_t_time); 

			  //tv_lab_approval_start = (TextView) convertView.findViewById(R.id.tv_lab_approval_start);
			  //tv_lab_approval_start.setText("当前环节:");
			  tv_approval_start = (TextView) convertView.findViewById(R.id.tv_approval_start);
			break;
		case 2: 
			 //tv_t_prepared = (TextView) convertView.findViewById(R.id.tv_t_prepared);
			  //tv_t_prepared.setText("申请人:");
			  tv_n_prepared = (TextView) convertView.findViewById(R.id.tv_n_prepared);
			  
			  tv_tip_time = (TextView) convertView.findViewById(R.id.tv_tip_time);
			  //tv_tip_time.setText("审阅:");
            tv_tip_time.setText("");
			  tv_t_time = (TextView) convertView.findViewById(R.id.tv_t_time); 

			  //tv_lab_approval_start = (TextView) convertView.findViewById(R.id.tv_lab_approval_start);
			  //tv_lab_approval_start.setText("审阅环节:");
			  tv_approval_start = (TextView) convertView.findViewById(R.id.tv_approval_start);
			break;
		case 4:
			  //tv_t_prepared = (TextView) convertView.findViewById(R.id.tv_t_prepared);
			  //tv_t_prepared.setText("申请人:");
			  tv_n_prepared = (TextView) convertView.findViewById(R.id.tv_n_prepared);
			  
			  tv_tip_time = (TextView) convertView.findViewById(R.id.tv_tip_time);
			  //tv_tip_time.setText("到达:");
            tv_tip_time.setText("");
			  tv_t_time = (TextView) convertView.findViewById(R.id.tv_t_time); 

			  //tv_lab_approval_start = (TextView) convertView.findViewById(R.id.tv_lab_approval_start);
			  //tv_lab_approval_start.setText("当前环节:");
			  tv_approval_start = (TextView) convertView.findViewById(R.id.tv_approval_start);
			break;
		case 5:
			 //tv_t_prepared = (TextView) convertView.findViewById(R.id.tv_t_prepared);
			  //tv_t_prepared.setText("申请人:");
			  tv_n_prepared = (TextView) convertView.findViewById(R.id.tv_n_prepared);
			  
			  tv_tip_time = (TextView) convertView.findViewById(R.id.tv_tip_time);
			  //tv_tip_time.setText("处理:");
            tv_tip_time.setText("");
			  tv_t_time = (TextView) convertView.findViewById(R.id.tv_t_time); 

			  //tv_lab_approval_start = (TextView) convertView.findViewById(R.id.tv_lab_approval_start);
			  //tv_lab_approval_start.setText("处理环节:");
			  tv_approval_start = (TextView) convertView.findViewById(R.id.tv_approval_start);
			break;
		default:break; 
		}
		
		final FlowBean bean = list.get(position);
		tv_title.setText(bean.getFolio());
		String dName = "";
		if(null != bean.getDisplayName() && !"".equals(bean.getDisplayName().trim()) && bean.getDisplayName().contains("\\")){
			String[] tmp = bean.getDisplayName().split("\\\\");
			if(null != tmp && (tmp.length > 0)){
				dName = tmp[tmp.length - 1];
			}else{
				dName = bean.getDisplayName();
			}
		}else{
			dName = bean.getDisplayName();
		}
		
		tv_n_prepared.setText(dName);
		tv_t_time.setText(bean.getAssignedDate());
		tv_approval_start.setText(bean.getActivityName());
		
		return convertView;
	}
	
	protected class ViewHolder {
		TextView tv_title;
		TextView tv_time;
		TextView tv_show_approver;
		TextView tv_approver;
		TextView tv_tip_time;
		LinearLayout ll_commit;
		Button btn_agree;
		Button btn_disagree;
		
		RelativeLayout rl_e_approval;
		RelativeLayout rl_prepared;
		RelativeLayout rl_approval;
		
		TextView tv_n_prepared;
		TextView tv_t_time;
		TextView tv_n_approval;
		TextView tv_state;
	}

   /**
	* @Title: getQuestData
	* @Description: 请求报文
	* @param void
	* @return String 返回的数据 
	* @throws
	*/
	private String getQuestData(String workListItemCode){

		PublicBean bean = new PublicBean();
		bean.setDeviceId(BaseApp.macAddr);
		bean.setUserAccount(BaseApp.user.getUserId());
		bean.setWorkListItemCode(workListItemCode);
		
		return JSON.toJSONString(bean);
	}
	/**
	* @Title: getQuestData
	* @Description: 请求报文
	* @param void
	* @return String 返回的数据 
	* @throws
	*/
	private String sendApprovedData(String procSetID, String formInstanceCode, int type){

		PublicBean bean = new PublicBean();
		bean.setDeviceId(BaseApp.macAddr);
		bean.setUserAccount(BaseApp.user.getUserId());
		bean.setFormInstanceCode(formInstanceCode);
		if(1 == type){
			bean.setProcSetID(procSetID);
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
	private void requestServer(int server, String info){
		// 判断网络是否连接
		if (!NetWorkUtil.isNetworkAvailable(context)) {
			DialogUtil.showLongToast(context, R.string.global_network_disconnected);
		}else{			
			SendRequest.sendSubmitRequest(context, info, BaseApp.token, BaseApp.reqLang, server, BaseApp.key, submitCallBack);
	}
  }
	
	/**
	 * http请求回调
	 */
	RequestCallBack<String> submitCallBack = new RequestCallBack<String>() {

		@Override
		public void onStart() {
			DialogUtil.showWithCancelProgressDialog(context, null, context.getResources().getString(R.string.global_prompt_message), null);
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
					LogUtil.promptInfo(context, ErrorCodeContrast.getErrorCode("0", context.getResources()));
					return;
				// 验证不合法
				}else if ("1103".equals(msg.getResCode()) || "1104".equals(msg.getResCode())) {
					LogUtil.promptInfo(context, ErrorCodeContrast.getErrorCode(msg.getResCode(), context.getResources()));	
					Intent mIntent = new Intent(BroadcastNotice.USER_EXIT);
					context.sendBroadcast(mIntent);
					return;
				}else if("1210".equals(msg.getResCode())){
					LogUtil.promptInfo(context, ErrorCodeContrast.getErrorCode(msg.getResCode(), context.getResources()));	
					Intent mIntent = new Intent(BroadcastNotice.WIPE_USET);
					context.sendBroadcast(mIntent);
					return;
				}else if (!"1".equals(msg.getResCode())) {
					LogUtil.promptInfo(context, ErrorCodeContrast.getErrorCode(msg.getResCode(), context.getResources()));	
					return;
				}
				// 判断消息体是否为空
				if (null == msg.getMessage() || "".equals(msg.getMessage().trim())) {		 
					LogUtil.promptInfo(context, ErrorCodeContrast.getErrorCode("0", context.getResources()));
				} else {
					// 获取解密后并校验后的值
					String decode = EncryptUtil.getDecodeData(msg.getMessage(), BaseApp.key);
					if (null != decode) {
						ResPublicBean bean = JSON.parseObject(decode, ResPublicBean.class);
						if(null != bean && null != bean.getSuccess() && "1".equals(bean.getSuccess())) {
							if (1 == type) {
								if(1 == flag){
									LogUtil.promptInfo(context, context.getResources().getString(R.string.submittal_succeed));
								}else if (2 == flag) {
									LogUtil.promptInfo(context, context.getResources().getString(R.string.delete_success));
								}
								list.remove(positions);
								notifyDataSetChanged();
							}else if (4 == type) {
								LogUtil.promptInfo(context, context.getResources().getString(R.string.approval_succeed));
								mIntent = new Intent(BroadcastNotice.NOT_APPROVAL);
								context.sendBroadcast(mIntent);
								
								mIntent = new Intent(BroadcastNotice.APPROVAL_COUNT);
								context.sendBroadcast(mIntent);
							}
							return;
						}else{
							if (4 == type) {
								LogUtil.promptInfo(context, context.getResources().getString(R.string.approval_failure));
							}else if (1 == type) {
								if(1 == flag){
									LogUtil.promptInfo(context, context.getResources().getString(R.string.submittal_failure));
								}else if (2 == flag) {
									LogUtil.promptInfo(context, context.getResources().getString(R.string.delete_failed));
								}
							}
						}
					}
				}
			} else {
				LogUtil.promptInfo(context, ErrorCodeContrast.getErrorCode("0", context.getResources()));
			}
		}

		@Override
		public void onFailure(HttpException error, String msg) {
			DialogUtil.closeDialog();
			if(msg.equals(LocalConstants.API_KEY)){
				LogUtil.promptInfo(context, ErrorCodeContrast.getErrorCode("1207", context.getResources()));
			}else{
				LogUtil.promptInfo(context, ErrorCodeContrast.getErrorCode("0", context.getResources()));
			}
		}
	};
}
 