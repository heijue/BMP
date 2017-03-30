package com.k2.mobile.app.model.adapter;    

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.k2.mobile.app.R;
import com.k2.mobile.app.model.bean.EmailBean;

import java.util.List;
  
/**
 * @Title ReceiveEmailAdapter.java
 * @Package com.oppo.mo.model.adapter
 * @Description 适配收邮件
 * @Company  K2
 * 
 * @author liangzy
 * @date 2015-03-18 20:43:00
 * @version V1.0
 */
public class ReceiveEmailAdapter extends BaseAdapter {

	private Context context;
	private List<EmailBean> mList;
	
	public ReceiveEmailAdapter(Context context, List<EmailBean> mList) {
		super();
		this.context = context;
		this.mList = mList;
	}

	@Override
	public int getCount() {
		return mList == null ? 0 : mList.size();
	}

	@Override
	public Object getItem(int position) {
		return mList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView == null){
			holder = new ViewHolder();
			convertView = LayoutInflater.from(context).inflate(R.layout.item_receive_email, null);
			holder.tv_sender = (TextView) convertView.findViewById(R.id.tv_sender);
			holder.tv_time = (TextView) convertView.findViewById(R.id.tv_time);
			holder.tv_title = (TextView) convertView.findViewById(R.id.tv_title);
			convertView.setTag(holder);
		}else{
			holder = (ViewHolder) convertView.getTag();
		}
		
		final EmailBean bean = mList.get(position);
//		holder.tv_sender.setText(bean.getSender());
//		holder.tv_time.setText(bean.gettTime());
//		holder.tv_title.setText(bean.gettTitle());
		return convertView;
	}
	
	protected class ViewHolder {
		TextView tv_sender;
		TextView tv_time;
		TextView tv_title;
	}
	
}
 