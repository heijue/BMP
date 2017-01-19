package com.k2.mobile.app.model.adapter;

import java.util.List;

import com.k2.mobile.app.R;
import com.k2.mobile.app.model.bean.HomeMenuBean;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
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
public class BenefitLifeAdapter extends BaseAdapter {
	
	private Context mContext;
	private List<HomeMenuBean> beanList;

	public BenefitLifeAdapter(Context _mContext, List<HomeMenuBean> _beanList) {
		super();
		this.mContext = _mContext;
		this.beanList = _beanList;
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
			convertView = LayoutInflater.from(mContext).inflate(R.layout.item_benefit_life_list, null);
			holder.tv_icon = (TextView) convertView.findViewById(R.id.tv_icon);
			holder.id_label = (TextView) convertView.findViewById(R.id.id_label);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		
		final HomeMenuBean bean = beanList.get(position);
		
		holder.tv_icon.setBackgroundResource(bean.getMenuIcons());
		holder.id_label.setText(bean.getMenuNmae());
		
		return convertView;
	}
	
	protected class ViewHolder {
		TextView tv_icon;
		TextView id_label;
	}
	
	
}
