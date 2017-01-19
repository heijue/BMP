package com.k2.mobile.app.model.adapter;

import java.util.List;

import com.k2.mobile.app.R;
import com.k2.mobile.app.model.bean.PopularTypeBean;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * @Title LivelyConsultationAdapter.java
 * @Package com.oppo.mo.model.adapter
 * @Description 
 * @Company  K2
 * 
 * @author linqijun
 * @date 2015-03-18 20:43:00
 * @version V1.0
 */
public class LivelyConsultationAdapter extends BaseAdapter {
	
	private Context mContext;
	private List<PopularTypeBean> hList;
    
	public LivelyConsultationAdapter(Context mContext, List<PopularTypeBean> hList) {
		this.mContext = mContext;
		this.hList = hList;
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
			convertView = LayoutInflater.from(mContext).inflate(R.layout.item_common_menu_add, null);
			LinearLayout ll_grid_item = (LinearLayout) convertView.findViewById(R.id.ll_grid_item);
			ImageView item_ico = (ImageView) convertView.findViewById(R.id.iv_item);
			TextView item_name = (TextView) convertView.findViewById(R.id.tv_item);
			CheckBox cb_select = (CheckBox) convertView.findViewById(R.id.cb_select);
			
			cb_select.setVisibility(View.GONE);
			item_ico.setBackgroundResource(R.drawable.salary_benefits);
			
			PopularTypeBean bean = hList.get(position);
			item_name.setText(bean.getTypeName());
			
		return convertView;
	}
		
}
