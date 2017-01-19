package com.k2.mobile.app.model.adapter;

import java.util.List;

import com.alibaba.fastjson.JSON;
import com.k2.mobile.app.R;
import com.k2.mobile.app.common.config.BroadcastNotice;
import com.k2.mobile.app.common.config.LocalConstants;
import com.k2.mobile.app.common.exception.HttpException;
import com.k2.mobile.app.controller.activity.menu.feedback.AddFeedbackActivity;
import com.k2.mobile.app.controller.activity.menu.feedback.FeedbackInfoActivity;
import com.k2.mobile.app.controller.activity.menu.feedback.FeedbackTreatActivity;
import com.k2.mobile.app.controller.core.BaseApp;
import com.k2.mobile.app.model.bean.FeedbackBean;
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
import com.k2.mobile.app.utils.LogUtil;
import com.k2.mobile.app.utils.NetWorkUtil;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

/**
 * @Title FeedbackListAdapter.java
 * @Package com.oppo.mo.model.adapter
 * @Description 问题反馈列表
 * @Company  K2
 * 
 * @author linqijun
 * @date 2015-04-16 10:31:00
 * @version V1.0
 */
@SuppressLint("ResourceAsColor")
public class FeedbackListAdapter extends BaseAdapter {

	private Activity activity;
	private List<FeedbackBean> list;
	private int type = 0;
	private int positions = 0;
	int[] val = { 
			R.string.create, 	R.string.reviews, 	R.string.prehandle,
			R.string.handling,	R.string.appraise,	R.string.finished, 
			R.string.unabledo 
		};
	
	public FeedbackListAdapter(Activity activity, List<FeedbackBean> list, int type) {
		super();
		this.activity = activity;
		this.list = list;
		this.type = type;
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
		
		convertView = LayoutInflater.from(activity).inflate(R.layout.item_feedback_list, null);
		
		TextView tv_list_title = (TextView) convertView.findViewById(R.id.tv_list_title);
		TextView tv_state = (TextView) convertView.findViewById(R.id.tv_state);
		TextView tv_time = (TextView) convertView.findViewById(R.id.tv_time);
		TextView tv_feedback_class = (TextView) convertView.findViewById(R.id.tv_feedback_class);
		TextView tv_oper_state = (TextView) convertView.findViewById(R.id.tv_oper_state);
		TextView tv_oper_pending = (TextView) convertView.findViewById(R.id.tv_oper_pending);
		
		final int index = position;
		final FeedbackBean bean = list.get(position);
		int status = 0;

		tv_list_title.setText(bean.getQuestionAbstract());
		if (null != bean.getStatus() && !"".equals(bean.getStatus().trim())) {
			status = Integer.parseInt(bean.getStatus());
			tv_state.setText(activity.getString(val[status - 1]));
		}
		if (null != bean.getCreateDatetime() && !"".equals(bean.getCreateDatetime().trim())) {
			String time = bean.getCreateDatetime().replace("T", " ");
			tv_time.setText(DateUtil.getDate_ymdhm(time));
		}
		
		tv_feedback_class.setText(bean.getQuestionCategoryName());
		tv_oper_state.setId(index);
		
		if(0 == type){
			switch (status) {
			case 1:
				tv_oper_pending.setVisibility(View.VISIBLE);
				tv_oper_pending.setText(activity.getString(R.string.global_delete));
				tv_oper_state.setText(activity.getString(R.string.edit));
				break;
			case 2:
			case 3:
			case 4:
			case 6:
				tv_oper_state.setVisibility(View.GONE);
				break;
			case 5:
				tv_oper_state.setText(activity.getString(R.string.evaluation));
				break;
			case 7:
				tv_oper_state.setText(activity.getString(R.string.close));
				break;
			}
		}else if(1 == type){
			switch (status) {
			case 1:
			case 2:
			case 5:
			case 6:
				tv_oper_state.setVisibility(View.GONE);
				break;
			case 3:
				tv_oper_pending.setText(activity.getString(R.string.pretreatment));
				tv_oper_state.setText(activity.getString(R.string.handle));
				tv_oper_pending.setVisibility(View.VISIBLE);
				break;
			case 4:
				tv_oper_state.setText(activity.getString(R.string.handle));
				break;
			case 7:
				tv_oper_state.setText(activity.getString(R.string.contact_user));
				break;
		}
		}
		
		final int mStatus = status;
		tv_oper_pending.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if(0 == type){
					if(1 == mStatus){
						AlertDialog mAlertDialog = new AlertDialog.Builder(activity)
						.setTitle(activity.getString(R.string.global_delete))
						.setMessage(activity.getString(R.string.is_delete_feedback))
						.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int which) {
								String info = receiveMailQuest(bean.getQuestionFeedbackCode(), "0");
								if(null != info && !"".equals(info)){
									requestServer(LocalConstants.FEEDBACK_DEL_SERVER, info);
								}
							}
						}).setNegativeButton(android.R.string.cancel, null).show();
						
					}
				}else if(1 == type){
					Intent mIntent = new Intent(activity, FeedbackTreatActivity.class);
					mIntent.putExtra("flag", "0"); 
					mIntent.putExtra("code", bean.getQuestionFeedbackCode());
					activity.startActivity(mIntent);
				}
			}
		});
		
		tv_oper_state.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				switch (mStatus) {
					case 1:
						Intent mIntent = new Intent(activity, AddFeedbackActivity.class);
						mIntent.putExtra("code", bean.getQuestionFeedbackCode()); 
						activity.startActivity(mIntent);
						break;
					case 3:
					case 4:
						Intent intents = new Intent(activity, FeedbackTreatActivity.class);
						intents.putExtra("flag", "1"); 
						intents.putExtra("code", bean.getQuestionFeedbackCode());
						activity.startActivity(intents);
						break;
					case 5:
						Intent intent = new Intent(activity, FeedbackInfoActivity.class);
						intent.putExtra("flag", "0");
						intent.putExtra("status", mStatus);
						intent.putExtra("code", bean.getQuestionFeedbackCode());
						activity.startActivity(intent);
						break;
					case 7:
						if(1 == type){
							if(null != bean.getCreatorPhoneNum() && !"".equals(bean.getCreatorPhoneNum().trim())){
								// 打电话
								Intent its = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + bean.getCreatorPhoneNum().trim()));
								activity.startActivity(its);
							}
						}else{
							AlertDialog mAlertDialog = new AlertDialog.Builder(activity)
							.setTitle(activity.getResources().getString(R.string.close))
							.setMessage(activity.getResources().getString(R.string.whether_or_not))
							.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog, int which) {
									positions = position;
									String info = receiveMailQuest(bean.getQuestionFeedbackCode(), "1");
									if(null != info && !"".equals(info)){
										requestServer(LocalConstants.FEEDBACK_DEL_SERVER, info);
									}
								}
							}).setNegativeButton(android.R.string.cancel, null).show();
						}
						break;
				}
			}
		});
		
		return convertView;
	}

	/**
	 * 方法名: receiveMailQuest() 
	 * 功 能 : 请求报文
	 * 参 数 : void 
	 * 返回值: void
	 */
	private String receiveMailQuest(String code, String cd){
		PublicBean bean = new PublicBean();
		bean.setDeviceId(BaseApp.macAddr);
		bean.setUserAccount(BaseApp.user.getUserId());
		bean.setOpFlag(cd);
		bean.setCode(code);
		
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
		if (!NetWorkUtil.isNetworkAvailable(activity)) {
			DialogUtil.showLongToast(activity, R.string.global_network_disconnected);
		}else{
			SendRequest.sendSubmitRequest(activity, info, BaseApp.token, BaseApp.reqLang, server, BaseApp.key, submitCallBack);
		}
	}
	
	/**
	 * http请求回调
	 */
	RequestCallBack<String> submitCallBack = new RequestCallBack<String>() {

		@Override
		public void onStart() {
			DialogUtil.showWithCancelProgressDialog(activity, null, activity.getResources().getString(R.string.global_prompt_message), null);
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
					LogUtil.promptInfo(activity, ErrorCodeContrast.getErrorCode("0", activity.getResources()));
					return;
				// 验证不合法
				}else if ("1103".equals(msg.getResCode()) || "1104".equals(msg.getResCode())) {
					Intent mIntent = new Intent(BroadcastNotice.USER_EXIT);
					activity.sendBroadcast(mIntent);
					return;
				}else if("1210".equals(msg.getResCode())){
					LogUtil.promptInfo(activity, ErrorCodeContrast.getErrorCode(msg.getResCode(), activity.getResources()));	
					Intent mIntent = new Intent(BroadcastNotice.WIPE_USET);
					activity.sendBroadcast(mIntent);
					return;
				}else if (!"1".equals(msg.getResCode())) {
					LogUtil.promptInfo(activity, ErrorCodeContrast.getErrorCode(msg.getResCode(), activity.getResources()));	
					return;
				}
				// 判断消息体是否为空
				if (null != msg.getMessage() && !"".equals(msg.getMessage().trim())) {		 
					// 获取解密后并校验后的值
					String decode = EncryptUtil.getDecodeData(msg.getMessage(), BaseApp.key);
					if(null != decode && !"".equals(decode)){
						ResPublicBean bean = JSON.parseObject(decode, ResPublicBean.class);
						if(null != bean && null != bean.getSuccess() && "1".equals(bean.getSuccess().trim())){
							list.remove(positions);
							notifyDataSetChanged();
						}
					}
				} 
			} else {
				LogUtil.promptInfo(activity, ErrorCodeContrast.getErrorCode("0", activity.getResources()));
			}
		}

		@Override
		public void onFailure(HttpException error, String msg) {
			DialogUtil.closeDialog();
			if(null != msg && msg.equals(LocalConstants.API_KEY)){
				LogUtil.promptInfo(activity, ErrorCodeContrast.getErrorCode("1207", activity.getResources()));
			}else{
				LogUtil.promptInfo(activity, ErrorCodeContrast.getErrorCode("0", activity.getResources()));
			}
		}
	};
	
	public static class ViewHolder {
		public TextView tv_list_title;
		public TextView tv_state;
		public TextView tv_time;
		public TextView tv_feedback_class;
		public TextView tv_oper_state;
	}
}
