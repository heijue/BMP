package com.k2.mobile.app.model.adapter;

import java.util.List;

import com.k2.mobile.app.R;
import com.k2.mobile.app.model.bean.ResCalendarBean;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class ClaendarListAdapter extends BaseAdapter{
	
	private Context context;
	private List<ResCalendarBean> list;

	
	public ClaendarListAdapter(Context _context, List<ResCalendarBean> _list) {
		super();
		this.list = _list;
		context = _context;
	}

	@Override
	public int getCount() {
		return list != null ? list.size() : 0;
	}

	@Override
	public Object getItem(int arg0) {
		return list.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		return arg0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup arg2) {
		View v = convertView;
		ViewHolder holder = null;
		if(v == null){
			v = LayoutInflater.from(context).inflate(R.layout.item_calendar_list, null);
			holder = new ViewHolder();
			holder.tv_event_type = (TextView) v.findViewById(R.id.tv_event_type);
			holder.tv_title = (TextView) v.findViewById(R.id.tv_title);
			holder.wv_content = (WebView) v.findViewById(R.id.wv_content);
			holder.wv_content.setBackgroundColor(0);  
			holder.tv_start_time = (TextView) v.findViewById(R.id.tv_start_time);
			holder.tv_end_time = (TextView) v.findViewById(R.id.tv_end_time);
			holder.tv_remark = (TextView) v.findViewById(R.id.tv_remark);
			v.setTag(holder);
		}else{
			holder = (ViewHolder) v.getTag();
		}
		
		final ResCalendarBean bean = list.get(position);
		if(null != bean.getEventType() && "1".equals(bean.getEventType())){
			holder.tv_event_type.setText(context.getResources().getString(R.string.my_calendar));
		} else if (null != bean.getEventType() && "2".equals(bean.getEventType())) {
			holder.tv_event_type.setText(context.getResources().getString(R.string.my_tasks));
		}
		holder.tv_title.setText(bean.getTitle());
		holder.wv_content.loadDataWithBaseURL(null, bean.getDetail(), "text/html", "utf-8", null);
		holder.tv_start_time.setText(bean.getStartTime().replaceAll("T", " "));
		holder.tv_end_time.setText(bean.getEndTime().replaceAll("T", " "));
		holder.tv_remark.setText(bean.getRemark());
		
		return v;
	}

	class ViewHolder{
		TextView tv_event_type;
		TextView tv_title;
		WebView  wv_content;
		TextView tv_start_time;
		TextView tv_end_time;
		TextView tv_remark;
	}


}