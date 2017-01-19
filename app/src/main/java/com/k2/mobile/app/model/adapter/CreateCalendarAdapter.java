package com.k2.mobile.app.model.adapter;

import java.util.List;

import com.k2.mobile.app.R;
import com.k2.mobile.app.controller.activity.menu.calendar.AddCalendarActivity;
import com.k2.mobile.app.model.bean.CalendarBean;
import com.k2.mobile.app.model.bean.ResCalendarBean;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class CreateCalendarAdapter extends BaseAdapter {
	
    private List<CalendarBean> ltCal = null;
    private Activity activity = null;
 
    public CreateCalendarAdapter(Activity _activit, List<CalendarBean> _ltCal ) {
    	activity = _activit;
    	ltCal = _ltCal;
    }

    @Override
    public int getCount() {
        return ltCal == null ? 0 : ltCal.size() ;
    }

    @Override
    public Object getItem(int position) {
        return ltCal.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
		
    	convertView = LayoutInflater.from(activity).inflate(R.layout.item_work_calendar, null);
		
    	TextView tv_time = (TextView) convertView.findViewById(R.id.tv_time);
    	TextView tv_back_day = (TextView) convertView.findViewById(R.id.tv_back_day);
    	TextView tv_week = (TextView) convertView.findViewById(R.id.tv_week);
    	Button btn_new_calendar = (Button) convertView.findViewById(R.id.btn_new_calendar);
    	LinearLayout ll_add_control = (LinearLayout) convertView.findViewById(R.id.ll_add_control);
		
		final CalendarBean bean = ltCal.get(position);
		if(null != bean.getResBean()){
			for(int i=0; i<bean.getResBean().size(); i++){
				final ResCalendarBean resBean = bean.getResBean().get(i);
				View v = LayoutInflater.from(activity).inflate(R.layout.item_show_event, null);
				LinearLayout ll_show = (LinearLayout) v.findViewById(R.id.ll_show);
				TextView tv_content = (TextView) v.findViewById(R.id.tv_content);
				ll_show.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						Intent mIntent = new Intent(activity, AddCalendarActivity.class);
						Bundle mBundle = new Bundle();  
				        mBundle.putSerializable("resBean",resBean); 
				        mIntent.putExtras(mBundle);
				        activity.startActivityForResult(mIntent, 1020);
					}
				});
				
				tv_content.setText(resBean.getTitle());
				ll_add_control.addView(v);
			}
		}
		String month = "";
		String day = "";
		if(null != bean.getMonth()){
			if(bean.getMonth().length() == 1){
				month = "0" + bean.getMonth();
			}else{
				month = bean.getMonth();
			}
		}
		
		if(null != bean.getDay()){
			if(bean.getDay().length() == 1){
				day = "0" + bean.getDay();
			}else{
				day = bean.getDay();
			}
		}
		
		tv_time.setText(month + "." + day);
		tv_week.setText(bean.getWeek());
		
		btn_new_calendar.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent mIntent = new Intent(activity, AddCalendarActivity.class);
				Bundle mBundle = new Bundle();  
		        mBundle.putSerializable("cBean",bean); 
		        mIntent.putExtras(mBundle);
		        activity.startActivityForResult(mIntent, 1020);
			}
		});
		return convertView;
	}
}
