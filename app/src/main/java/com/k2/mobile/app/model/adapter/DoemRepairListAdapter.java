package com.k2.mobile.app.model.adapter;

import java.util.List;

import com.alibaba.fastjson.JSON;
import com.k2.mobile.app.R;
import com.k2.mobile.app.common.config.BroadcastNotice;
import com.k2.mobile.app.common.config.LocalConstants;
import com.k2.mobile.app.common.exception.HttpException;
import com.k2.mobile.app.controller.activity.menu.dormitory.AddDormitoryActivity;
import com.k2.mobile.app.controller.activity.menu.dormitory.DormitoryEvaluationActivity;
import com.k2.mobile.app.controller.core.BaseApp;
import com.k2.mobile.app.model.bean.DormRepairBean;
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
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
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
public class DoemRepairListAdapter extends BaseAdapter {

	private Activity activity;
	private List<DormRepairBean> list;
	private int positions = 0;
	private int operType = 1;

	public DoemRepairListAdapter(Activity activity, List<DormRepairBean> list) {
		super();
		this.activity = activity;
		this.list = list;
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

		convertView = LayoutInflater.from(activity).inflate(R.layout.item_doem_repair_list, null);

		TextView tv_number = (TextView) convertView.findViewById(R.id.tv_number);
		TextView tv_state = (TextView) convertView.findViewById(R.id.tv_state);
		TextView tv_items = (TextView) convertView.findViewById(R.id.tv_items);
		TextView tv_repair_addr = (TextView) convertView.findViewById(R.id.tv_repair_addr);
		TextView tv_repair_time = (TextView) convertView.findViewById(R.id.tv_repair_time);
		TextView tv_maintenance_man = (TextView) convertView.findViewById(R.id.tv_maintenance_man);
		TextView tv_finish_time = (TextView) convertView.findViewById(R.id.tv_finish_time);
		TextView tv_oper_del = (TextView) convertView.findViewById(R.id.tv_oper_del);
		TextView tv_oper_state = (TextView) convertView.findViewById(R.id.tv_oper_state);

		final int index = position;
		final DormRepairBean bean = list.get(position);

		tv_number.setText(bean.getRepairNumber());
		tv_items.setText(bean.getRepairItems());
		tv_repair_addr.setText(bean.getRepairAddress());
		if (null != bean.getRepairerUserName() && !"".equals(bean.getRepairerUserName().trim())) {
			tv_maintenance_man.setText(bean.getRepairerUserName());
		} else {
			tv_maintenance_man.setText("--");
		}

		if (null == bean.getFinishTime() || "".equals(bean.getFinishTime().trim()) || "1753-01-01T12:00:00".equals(bean.getFinishTime().trim())) {
			tv_finish_time.setText("--");
		} else {
			String time = DateUtil.getDate_ymdhm(bean.getFinishTime().replaceAll("T", " "));
			tv_finish_time.setText(time);
		}
		int status = 0;
		if (null != bean.getRepairStatus() && !"".equals(bean.getRepairStatus())) {
			String tmpVal = "";
			try {
				status = Integer.parseInt(bean.getRepairStatus());
			} catch (NumberFormatException e) {
				e.printStackTrace();
			}

			switch (status) {
			case 1:
				tmpVal = activity.getString(R.string.create);
				tv_oper_del.setVisibility(View.VISIBLE);
				tv_oper_state.setText(activity.getString(R.string.edit));
				break;
			case 2:
				tmpVal = activity.getString(R.string.reviews);
				tv_oper_del.setVisibility(View.GONE);
				tv_oper_state.setText(activity.getString(R.string.close));
				break;
			case 3:
				tmpVal = activity.getString(R.string.pending_trial);
				tv_oper_del.setVisibility(View.GONE);
				tv_oper_state.setText(activity.getString(R.string.close));
				break;
			case 4:
				tmpVal = activity.getString(R.string.maintenance);
				tv_oper_del.setVisibility(View.GONE);
				tv_oper_state.setText(activity.getString(R.string.close));
				break;
			case 5:
				tmpVal = activity.getString(R.string.repair);
				tv_oper_del.setVisibility(View.GONE);
				tv_oper_state.setText(activity.getString(R.string.evaluation));
				break;
			case 6:
				tmpVal = activity.getString(R.string.been_evaluated);
				tv_oper_del.setVisibility(View.GONE);
				tv_oper_state.setVisibility(View.GONE);
				break;
			case 7:
				tmpVal = activity.getString(R.string.closed);
				tv_oper_del.setVisibility(View.GONE);
				tv_oper_state.setVisibility(View.GONE);
				break;
			}

			tv_state.setText(tmpVal);
		}

		if (null != bean.getRepairSubmitTime() && !"".equals(bean.getRepairSubmitTime().trim())) {
			String time = DateUtil.getDate_ymdhm(bean.getRepairSubmitTime().replaceAll("T", " "));
			tv_repair_time.setText(time);
		}
		// 删除
		tv_oper_del.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				positions = index;
				operType = 1;
				String info = receiveMailQuest(bean.getRepairUserCode(), bean.getId());
				requestServer(LocalConstants.DORMITORY_REPAIR_DEL_SERVER, info);
			}
		});
		
		final int statusVal = status;
		// 其他操作
		tv_oper_state.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				switch (statusVal) {
					case 1:					// 编辑
						Intent eIntent = new Intent(activity, AddDormitoryActivity.class);
						eIntent.putExtra("code", bean.getRepairCode());
						activity.startActivity(eIntent);
						break;
					case 2:					// 关闭
					case 3:
					case 4:
						operType = 2;
						String info = receiveMailQuest(bean.getRepairUserCode(), bean.getId());
						requestServer(LocalConstants.DORMITORY_REPAIR_CLOSE_SERVER, info);
						break;
					case 5:					// 评价
						Intent mIntent = new Intent(activity, DormitoryEvaluationActivity.class);
						mIntent.putExtra("code", bean.getRepairCode());
						activity.startActivity(mIntent);
						break;
				}
			}
		});
		
		return convertView;
	}

	/**
	 * 方法名: receiveMailQuest() 
	 * 功 能 : 请求报文 
	 * 参 数 : String code 唯一标识 
	 * 返回值: void
	 */
	private String receiveMailQuest(String code, String id) {
		PublicBean bean = new PublicBean();
		bean.setDeviceId(BaseApp.macAddr);
		bean.setUserAccount(BaseApp.user.getUserId());
		bean.setId(id);;
		bean.setUserCode(code);

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
		} else {
			SendRequest.sendSubmitRequest(activity, info, BaseApp.token, BaseApp.reqLang, server, BaseApp.key, submitCallBack);
		}
	}

	/**
	 * http请求回调
	 */
	RequestCallBack<String> submitCallBack = new RequestCallBack<String>() {

		@Override
		public void onStart() {
			DialogUtil.showWithCancelProgressDialog(activity, null, activity.getString(R.string.global_prompt_message), null);
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
				} else if ("1103".equals(msg.getResCode()) || "1104".equals(msg.getResCode())) {
					Intent mIntent = new Intent(BroadcastNotice.USER_EXIT);
					activity.sendBroadcast(mIntent);
					return;
				} else if ("1210".equals(msg.getResCode())) {
					LogUtil.promptInfo(activity, ErrorCodeContrast.getErrorCode(msg.getResCode(), activity.getResources()));
					Intent mIntent = new Intent(BroadcastNotice.WIPE_USET);
					activity.sendBroadcast(mIntent);
					return;
				} else if (!"1".equals(msg.getResCode())) {
					LogUtil.promptInfo(activity, ErrorCodeContrast.getErrorCode(msg.getResCode(), activity.getResources()));
					return;
				}
				// 判断消息体是否为空
				if (null != msg.getMessage() && !"".equals(msg.getMessage().trim())) {
					// 获取解密后并校验后的值
					String decode = EncryptUtil.getDecodeData(msg.getMessage(), BaseApp.key);
					if (null != decode && !"".equals(decode)) {
						ResPublicBean bean = JSON.parseObject(decode, ResPublicBean.class);
						if (null != bean && null != bean.getSuccess() && "1".equals(bean.getSuccess().trim())) {
							if(1 == operType){
								list.remove(positions);
								notifyDataSetChanged();
							}else if(2 == operType){
								Intent mIntent = new Intent(BroadcastNotice.DORMITORY_FINISH_UPDATE_TREATED);
								activity.sendBroadcast(mIntent);
							}
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
			if (null != msg && msg.equals(LocalConstants.API_KEY)) {
				LogUtil.promptInfo(activity, ErrorCodeContrast.getErrorCode("1207", activity.getResources()));
			} else {
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
