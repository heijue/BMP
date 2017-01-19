package com.k2.mobile.app.model.adapter;    

import java.util.List;

import com.k2.mobile.app.R;
import com.k2.mobile.app.model.bean.CommonNewsBean;
import com.k2.mobile.app.utils.DateUtil;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
  
/**
 * @Title FlowAdapter.java
 * @Package com.oppo.mo.model.adapter
 * @Description 适配新闻列表
 * @Company  K2
 * 
 * @author liangzy
 * @date 2015-04-03 15:41:03
 * @version V1.0
 */
public class NewsListAdapter extends BaseAdapter {

	private Context context;
	private List<CommonNewsBean> newsList;
	
	public NewsListAdapter(Context context, List<CommonNewsBean> newsList) {
		super();
		this.context = context;
		this.newsList = newsList;
	}

	@Override
	public int getCount() {
		return newsList == null ? 0 : newsList.size();
	}

	@Override
	public Object getItem(int position) {
		return newsList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder = null;
		if (convertView == null){
			viewHolder = new ViewHolder();
			convertView = LayoutInflater.from(context).inflate(R.layout.item_news_list, null);
			viewHolder.tv_news_tit = (TextView) convertView.findViewById(R.id.tv_news_tit);
			viewHolder.tv_author = (TextView) convertView.findViewById(R.id.tv_author);
			viewHolder.tv_author_dep = (TextView) convertView.findViewById(R.id.tv_author_dep);
			viewHolder.tv_time = (TextView) convertView.findViewById(R.id.tv_time);
			convertView.setTag(viewHolder);
		}else{
			viewHolder = (ViewHolder) convertView.getTag();
		}
		
		final CommonNewsBean bean = newsList.get(position);
		
		viewHolder.tv_news_tit.setText(bean.getNewsTitle());
		viewHolder.tv_author.setText(bean.getCreateUserName());
		viewHolder.tv_author_dep.setText("（"+bean.getCreateUserOrgName()+"）");
		if(null != bean.getPublicDatetime()){
			String time = bean.getPublicDatetime().replaceAll("T", " ");
			viewHolder.tv_time.setText(DateUtil.getDate_ymdhm(time));
		}
		
		return convertView;
	}
	
	protected class ViewHolder {
		TextView tv_news_tit, tv_author, tv_author_dep, tv_time;
	}
	
}
 