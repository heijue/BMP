package com.k2.mobile.app.model.adapter;    

import java.util.List;

import com.k2.mobile.app.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
  
/**
 * @Title FlowAdapter.java
 * @Package com.oppo.mo.model.adapter
 * @Description 适配我的流程类-待审批
 * @Company  K2
 * 
 * @author liangzy
 * @date 2015-03-18 20:43:00
 * @version V1.0
 */
public class HomeCompanyNewsAdapter extends BaseAdapter {
	
	private Context context;
	private List<String> list;
	
	public HomeCompanyNewsAdapter(Context context, List<String> list) {
		super();
		this.context = context;
		this.list = list;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list == null ? 0 : list.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = LayoutInflater.from(context).inflate(R.layout.item_home_news, null);
			holder.tv_news_title = (TextView) convertView.findViewById(R.id.tv_news_title);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.tv_news_title.setText(list.get(position));
		return convertView;
	}

	protected class ViewHolder {
		TextView tv_news_title;
	}
	
}
 