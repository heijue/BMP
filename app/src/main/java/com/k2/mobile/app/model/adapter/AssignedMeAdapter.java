package com.k2.mobile.app.model.adapter;

import java.util.List;

import com.alibaba.fastjson.JSON;
import com.k2.mobile.app.R;
import com.k2.mobile.app.common.config.BroadcastNotice;
import com.k2.mobile.app.common.config.LocalConstants;
import com.k2.mobile.app.common.exception.HttpException;
import com.k2.mobile.app.controller.activity.menu.task.TaskInfoActivity;
import com.k2.mobile.app.controller.core.BaseApp;
import com.k2.mobile.app.model.bean.ReqMessage;
import com.k2.mobile.app.model.bean.ResPublicBean;
import com.k2.mobile.app.model.bean.ResTaskBean;
import com.k2.mobile.app.model.bean.TaskBean;
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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.MeasureSpec;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * @Title MyTaskAdapter.java
 * @Package com.oppo.mo.model.adapter
 * @Description 适配我的任务类
 * @Company  K2
 * 
 * @author liangzy
 * @date 2015-03-18 20:43:00
 * @version V1.0
 */
public class AssignedMeAdapter extends BaseAdapter {
	
	private Context mContext;
	private List<ResTaskBean> beanList;
	// 任务类型 0,指派 1未完成 2已完成
	private int taskType = 0;
	
	private int mLcdWidth = 0;
	private float mDensity = 0;

	public AssignedMeAdapter(Context _mContext, List<ResTaskBean> _beanList, int _taskType) {
		super();
		this.mContext = _mContext;
		this.beanList = _beanList;
		this.taskType = _taskType;
	}

	@Override
	public int getCount() {
		return beanList != null ? beanList.size() : 0;
	}

	@Override
	public Object getItem(int position) {
		return beanList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = LayoutInflater.from(mContext).inflate(R.layout.mytask_list_item, null);
			holder.title = (TextView) convertView.findViewById(R.id.title);
			holder.time_start = (TextView) convertView.findViewById(R.id.time_start);
			holder.time_end = (TextView) convertView.findViewById(R.id.time_end);
			holder.be_accepted = (TextView) convertView.findViewById(R.id.be_accepted);
			holder.termination = (TextView) convertView.findViewById(R.id.termination);
			holder.accept = (TextView) convertView.findViewById(R.id.accept);
			holder.complete = (TextView) convertView.findViewById(R.id.complete);
			holder.terminated = (TextView) convertView.findViewById(R.id.terminated);
			holder.btn_task_info = (Button) convertView.findViewById(R.id.btn_task_info);
			
			holder.footer = (LinearLayout) convertView.findViewById(R.id.show_edit);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		
		final ResTaskBean bean = beanList.get(position);
		
		if(null != bean.getWorkCreateType() && "1".equals(bean.getWorkCreateType().trim())){
			holder.title.setText("【"+mContext.getResources().getString(R.string.system)+"】"+bean.getWorkListItemTitle());
		}else if (null != bean.getWorkCreateType() && ("2".equals(bean.getWorkCreateType().trim()) || "3".equals(bean.getWorkCreateType().trim()))) {
			holder.title.setText("【"+mContext.getResources().getString(R.string.custom)+"】"+bean.getWorkListItemTitle());
		}else if (null != bean.getWorkCreateType() && "4".equals(bean.getWorkCreateType().trim())) {
			holder.title.setText("【"+mContext.getResources().getString(R.string.conference)+"】"+bean.getWorkListItemTitle());
		}else if (null != bean.getWorkCreateType() && "5".equals(bean.getWorkCreateType().trim())) {
			holder.title.setText("【"+mContext.getResources().getString(R.string.email)+"】"+bean.getWorkListItemTitle());
		}
		
		holder.time_start.setText(bean.getCreateDatetime().replace("T", " "));
		holder.time_end.setText(bean.getFinishDatetime().replace("T", " "));
		// 查看详情
		holder.btn_task_info.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent mIntent = new Intent(mContext, TaskInfoActivity.class);
				mIntent.putExtra("id", bean.getId());
				mContext.startActivity(mIntent);
			}
		});
		
		final String createType = bean.getWorkCreateType();			// 任务创建类型
		final String process = bean.getProcessingType();			// 执行类型
		final String itemStatus = bean.getWorkListItemStatus();		// 任务完成状态
		
		final int checkVal = checkVal(createType);
		
		switch (taskType) {
		case 0:
			if (null != process && "0".equals(process.trim())) {
				holder.accept.setVisibility(View.VISIBLE);
				holder.be_accepted.setVisibility(View.GONE);
				if(1 == checkVal){
					holder.accept.setText(mContext.getResources().getString(R.string.accept));
				}else if(2 == checkVal){
					holder.accept.setText(mContext.getResources().getString(R.string.begin));
				}
				
				holder.accept.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						String info = updateTaskInfo(bean.getId(), 0);	// 终止
						resquestAction(info, LocalConstants.UPDATE_TASK_STATE_SERVER);
					}
				});
			} else if (null != process && "1".equals(process.trim())) {
				if(1 == checkVal){
					//text已接受
					holder.be_accepted.setText(mContext.getResources().getString(R.string.have_been_accepted));
				}else if(2 == checkVal){
					//text已开始
					holder.be_accepted.setText(mContext.getResources().getString(R.string.has_begun));
				}
			}

			if (null != itemStatus && "0".equals(itemStatus.trim())) {
				//text 待接受
				holder.termination.setVisibility(View.GONE);
				holder.terminated.setVisibility(View.VISIBLE);
				holder.terminated.setText(mContext.getResources().getString(R.string.to_be_accepted));
			} else if (null != itemStatus && "1".equals(itemStatus.trim())) {
				//text已完成
				holder.termination.setVisibility(View.GONE);
				holder.terminated.setVisibility(View.VISIBLE);
				holder.terminated.setText(mContext.getResources().getString(R.string.has_been_completed));
			}
//			
//			if (null != itemStatus && "2".equals(itemStatus.trim())) {
//				//text已终止
//				holder.termination.setVisibility(View.GONE);
//				holder.terminated.setVisibility(View.VISIBLE);
//				holder.terminated.setText(mContext.getResources().getString(R.string.have_been_termination));
//			}
			break;
		case 1:
			if ((null != process && "0".equals(process.trim())) && (2 == checkVal)) {
					holder.be_accepted.setVisibility(View.GONE);
					holder.accept.setVisibility(View.VISIBLE);
					holder.accept.setText(mContext.getResources().getString(R.string.begin));	// 开始
					holder.accept.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View v) {
							if ((null != process && "0".equals(process.trim())) && ((null != createType) && (2 == checkVal))) {
								String info = updateTaskInfo(bean.getId(), 0);		// 开始
								resquestAction(info, LocalConstants.UPDATE_TASK_STATE_SERVER);
							}
						}
					});
				} else if ((null != process && "0".equals(process.trim())) && (1 == checkVal)) {
					holder.be_accepted.setVisibility(View.GONE);
					holder.accept.setVisibility(View.VISIBLE);
					holder.accept.setText(mContext.getResources().getString(R.string.accept));	// 接受
					holder.accept.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View v) {
							if ((null != process && "0".equals(process.trim())) && (1 == checkVal)) {
								String info = updateTaskInfo(bean.getId(), 0);		// 接受
								resquestAction(info, LocalConstants.UPDATE_TASK_STATE_SERVER);
							}
						}
					});
				} else if ((null != process && "1".equals(process.trim())) && (2 == checkVal)) {
					holder.be_accepted.setText(mContext.getResources().getString(R.string.has_begun));		// 已开始
				} else if ((null != process && "1".equals(process.trim())) && (1 == checkVal)) {
					holder.be_accepted.setText(mContext.getResources().getString(R.string.have_been_accepted));// 已接受
				}
				
				if ((null != itemStatus && "0".equals(itemStatus.trim())) && (null != process && "0".equals(process.trim()))) {
					holder.termination.setVisibility(View.GONE);
					holder.terminated.setVisibility(View.VISIBLE);
					holder.terminated.setText(mContext.getResources().getString(R.string.to_be_accepted));
				} else if ((null != itemStatus && "0".equals(itemStatus.trim())) && (null != process && "1".equals(process.trim())) && (1 == checkVal)) {
					holder.termination.setText(mContext.getResources().getString(R.string.complete));
					holder.termination.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View v) {
							String info = updateTaskInfo(bean.getId(), 1);		// 完成
							resquestAction(info, LocalConstants.UPDATE_TASK_STATE_SERVER);
						}
					});				
				} else if ((null != itemStatus && "0".equals(itemStatus.trim())) && (null != process && "1".equals(process.trim())) && (2 == checkVal)) {
					holder.complete.setVisibility(View.VISIBLE);
					holder.complete.setText(mContext.getResources().getString(R.string.complete));
					holder.termination.setText(mContext.getResources().getString(R.string.termination));
					holder.complete.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View v) {
							if ((null != itemStatus && "0".equals(itemStatus.trim())) && (null != process && "1".equals(process.trim())) && (2 == checkVal)) {
								String info = updateTaskInfo(bean.getId(), 1);		// 完成
								resquestAction(info, LocalConstants.UPDATE_TASK_STATE_SERVER);
							}
						}
					});
					holder.termination.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View v) {
							if ((null != itemStatus && "0".equals(itemStatus.trim())) && (null != process && "1".equals(process.trim())) && (2 == checkVal)) {
								String info = updateTaskInfo(bean.getId(), 2);		// 终止
								resquestAction(info, LocalConstants.UPDATE_TASK_STATE_SERVER);
							}
						}
					});
				}
			
			break;
		case 2:		// 已完成
			if (null != process && "1".equals(process.trim()) && (2 == checkVal)) {
				holder.be_accepted.setText(mContext.getResources().getString(R.string.has_begun));
			} else if (null != process && "1".equals(process.trim()) && (1 == checkVal)) {
				holder.be_accepted.setText(mContext.getResources().getString(R.string.have_been_accepted));
			}

			holder.termination.setVisibility(View.GONE);
			holder.terminated.setVisibility(View.VISIBLE);
			
			if (null != itemStatus && "0".equals(itemStatus.trim())) {
				holder.terminated.setText(mContext.getResources().getString(R.string.to_be_accepted));
			} else if (null != itemStatus && "1".equals(itemStatus.trim())){
				holder.terminated.setText(mContext.getResources().getString(R.string.has_been_completed));
			} else if (null != itemStatus && "2".equals(itemStatus.trim())){
				holder.terminated.setText(mContext.getResources().getString(R.string.have_been_termination));
			}
			
			break;
		
		}
		// get footer height
		int widthSpec = MeasureSpec.makeMeasureSpec((int) (mLcdWidth - 10 * mDensity), MeasureSpec.EXACTLY);
		holder.footer.measure(widthSpec, 0);
		LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) holder.footer.getLayoutParams();
		params.bottomMargin = (-holder.footer.getMeasuredHeight());
		holder.footer.setVisibility(View.GONE);
		
		return convertView;
	}
	/**
	 * 
	 * 验证工作类型
	 * @param workCreateType 工作类型
	 * @return int 类型结果
	 */
	private int checkVal(String workCreateType){
		int res = 0;
		if(null == workCreateType || "".equals(workCreateType.trim())){
			return 0;
		}
		int tmp = Integer.parseInt(workCreateType);
		if(0 == tmp || 1 == tmp  || 4 == tmp ){
			res = 1;
		}else if(2 == tmp || 3 == tmp  || 5 == tmp ){
			res = 2;
		}
		return res;
	}
	
	protected class ViewHolder {
		TextView title;
		TextView time_start;
		TextView time_end;
		TextView be_accepted;		// 待接受
		TextView accept;			// 接受
		TextView termination;		// 终止		
		TextView complete;			// 完成
		TextView terminated;		// 已终止
		Button btn_task_info;		// 任务详情
		LinearLayout footer;
	}
	
	/*
	 * 方法名: updateTaskInfo() 
	 * 功 能 : 修改任务状态 
	 * 参 数 : void 
	 * 返回值: String 请求消息
	 */
	private String updateTaskInfo(String taskId, int taskType) {
		TaskBean bean = new TaskBean();
		bean.setDeviceId(BaseApp.macAddr);
		bean.setTaskId(taskId);
		bean.setTaskType(taskType + "");

		return JSON.toJSONString(bean);
	}

	private void resquestAction(String json, int server) {
		// 查询用户菜单
		if (!NetWorkUtil.isNetworkAvailable(mContext)) {
			DialogUtil.showLongToast(mContext, R.string.global_network_disconnected);
		} else {
			SendRequest.sendSubmitRequest(mContext, json, BaseApp.token, BaseApp.reqLang,
					server, BaseApp.key, submitCallBack);
		}
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
				ReqMessage msg = FastJSONUtil.getJSONToEntity(result, ReqMessage.class);
				// 判断返回标识状态是否为空
				if (null == msg || null == msg.getResCode() || "".equals(msg.getResCode())) {				
					LogUtil.promptInfo(mContext, ErrorCodeContrast.getErrorCode("0", mContext.getResources()));
					return;
				// 验证不合法
				}else if ("1103".equals(msg.getResCode()) || "1104".equals(msg.getResCode())) {
					LogUtil.promptInfo(mContext, ErrorCodeContrast.getErrorCode(msg.getResCode(), mContext.getResources()));	
					Intent mIntent = new Intent(BroadcastNotice.USER_EXIT);
					mContext.sendBroadcast(mIntent);
					return;
				}else if("1210".equals(msg.getResCode())){
					LogUtil.promptInfo(mContext, ErrorCodeContrast.getErrorCode(msg.getResCode(), mContext.getResources()));	
					Intent mIntent = new Intent(BroadcastNotice.WIPE_USET);
					mContext.sendBroadcast(mIntent);
					return;
				}else if (!"1".equals(msg.getResCode())) {
					LogUtil.promptInfo(mContext, ErrorCodeContrast.getErrorCode(msg.getResCode(), mContext.getResources()));	
					return;
				}
				// 判断消息体是否为空
				if (null == msg.getMessage() || "".equals(msg.getMessage().trim())) {		 
					LogUtil.promptInfo(mContext, ErrorCodeContrast.getErrorCode("0", mContext.getResources()));
				} else {
					// 获取解密后并校验后的值
					String decode = EncryptUtil.getDecodeData(msg.getMessage(), BaseApp.key);
					if (null != decode) {
						ResPublicBean bean = JSON.parseObject(decode, ResPublicBean.class);
						if(null != bean && null != bean.getSuccess() && "1".equals(bean.getSuccess())) {
							String action = BroadcastNotice.TASKS_UI_UPDATE;
							if (1 == taskType) {
								action = BroadcastNotice.TASKS_UNFINISH_UPDATE;
							}else if (2 == taskType) {
								action = BroadcastNotice.TASKS_FINISH_UPDATE;
							}
							Intent mIntent = new Intent(action);
							mContext.sendBroadcast(mIntent);
							return;
						}
					}
					
					LogUtil.promptInfo(mContext, ErrorCodeContrast.getErrorCode("0", mContext.getResources()));
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
