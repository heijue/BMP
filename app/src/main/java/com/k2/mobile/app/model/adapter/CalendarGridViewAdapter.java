package com.k2.mobile.app.model.adapter;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import com.k2.mobile.app.R;
import com.k2.mobile.app.utils.CalendarUtil;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.res.Resources;
import android.os.Build;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.LinearLayout.LayoutParams;
/**
 * @Title CalendarGridViewAdapter.java
 * @Package com.oppo.mo.model.adapter
 * @Description 适配日历类
 * @Company  K2
 * 
 * @author linqijun
 * @date 2015-03-18 20:43:00
 * @version V1.0
 */
public class CalendarGridViewAdapter extends BaseAdapter {

    private Calendar calStartDate = Calendar.getInstance();// 当前显示的日历
    private Calendar calSelected = Calendar.getInstance(); // 选择的日历

    private Activity activity;
    Resources resources;
    ArrayList<java.util.Date> titles;
    
    public void setSelectedDate(Calendar cal) {
        calSelected = cal;
    }

    private Calendar calToday = Calendar.getInstance(); // 今日
    private int iMonthViewCurrentMonth = 0; // 当前视图月

    // 根据改变的日期更新日历
    // 填充日历控件用
    private void UpdateStartDateForMonth() {
        calStartDate.set(Calendar.DATE, 1); // 设置成当月第一天
        iMonthViewCurrentMonth = calStartDate.get(Calendar.MONTH);// 得到当前日历显示的月

        // 星期一是2 星期天是1 填充剩余天数
        int iDay = 0;
        int iFirstDayOfWeek = Calendar.MONDAY;
        int iStartDay = iFirstDayOfWeek;
        if (iStartDay == Calendar.MONDAY) {
            iDay = calStartDate.get(Calendar.DAY_OF_WEEK) - Calendar.MONDAY;
            if (iDay < 0)
                iDay = 6;
        }
        if (iStartDay == Calendar.SUNDAY) {
            iDay = calStartDate.get(Calendar.DAY_OF_WEEK) - Calendar.SUNDAY;
            if (iDay < 0)
                iDay = 6;
        }
        
        calStartDate.add(Calendar.DAY_OF_WEEK, -iDay);
        calStartDate.add(Calendar.DAY_OF_MONTH, -1);// 周日第一位
    }

    private ArrayList<java.util.Date> getDates() {

        UpdateStartDateForMonth();

        ArrayList<java.util.Date> alArrayList = new ArrayList<java.util.Date>();

        for (int i = 1; i <= 42; i++) {
            alArrayList.add(calStartDate.getTime());
            calStartDate.add(Calendar.DAY_OF_MONTH, 1);
        }

        return alArrayList;
    }

    // construct
    public CalendarGridViewAdapter(Activity activit, Calendar cal) {
        calStartDate = cal;
        activity = activit;
        resources = activity.getResources();
        titles = getDates();
    }

    public CalendarGridViewAdapter(Activity a) {
        activity = a;
        resources = activity.getResources();
    }


    @Override
    public int getCount() {
        return titles.size();
    }

    @Override
    public Object getItem(int position) {
        return titles.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

	@SuppressLint("NewApi")
	@Override
    public View getView(int position, View convertView, ViewGroup parent) {
		LinearLayout imageLayout = new LinearLayout(activity);
        imageLayout.setId(position + 7999);
        imageLayout.setOrientation(0);
        imageLayout.setGravity(Gravity.CENTER);
        
        LinearLayout iv = new LinearLayout(activity);
        iv.setId(position + 5000);
        iv.setGravity(Gravity.CENTER);
        iv.setOrientation(1);
        iv.setBackgroundColor(resources.getColor(R.color.white));
        iv.setPadding(0, 5, 0, 5);

        Date myDate = (Date) getItem(position);
        Calendar calCalendar = Calendar.getInstance();
        calCalendar.setTime(myDate);

        final int iMonth = calCalendar.get(Calendar.MONTH);
        final int iDay = calCalendar.get(Calendar.DAY_OF_WEEK);
        
        // 公历
        TextView txtDay = new TextView(activity);
        txtDay.setGravity(Gravity.CENTER_HORIZONTAL);
        txtDay.setTextSize(14);
        
        // 老黄历
        TextView txtToDay = new TextView(activity);
        txtToDay.setGravity(Gravity.CENTER_HORIZONTAL);
        txtToDay.setTextSize(10);

        CalendarUtil calendarUtil = new CalendarUtil(calCalendar);
        if (equalsDate(calToday.getTime(), myDate)) {
        	// 当前日期
        	txtToDay.setText(calendarUtil.toString());
        } else {
        	txtToDay.setText(calendarUtil.toString());
        }

        // 黄历颜色
        txtToDay.setTextColor(resources.getColor(R.color.lightgray5));	
        // 判断是否是当前月
        if (iMonth == iMonthViewCurrentMonth) {
        	// 当前月公历颜色
        	txtDay.setTextColor(resources.getColor(R.color.main_tv_font));
        } else {
        	txtDay.setTextColor(resources.getColor(R.color.lightgray5));
        }

        // 判断周六周日
        if (iDay == 7 || iDay == 1) {
        	txtDay.setTextColor(resources.getColor(R.color.lightgray5));
        } 
        // 设置背景颜色
        if (equalsDate(calSelected.getTime(), myDate)) {
        	// 选择的
        	if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
        		iv.setBackground(resources.getDrawable(R.drawable.textview_circle_choose));
            } else {
            	iv.setBackgroundDrawable(resources.getDrawable(R.drawable.textview_circle_choose));
            }
        	txtDay.setTextColor(resources.getColor(R.color.white));		// 公历
        	txtToDay.setTextColor(resources.getColor(R.color.white));	// 黄历
        } else {
        	if (equalsDate(calToday.getTime(), myDate)) {
        		// 当前日期
        		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            		iv.setBackground(resources.getDrawable(R.drawable.textview_circle));
                } else {
                	iv.setBackgroundDrawable(resources.getDrawable(R.drawable.textview_circle));
                }
        		txtDay.setTextColor(resources.getColor(R.color.white));		// 公历
        		txtToDay.setTextColor(resources.getColor(R.color.white));	// 黄历
        	}
        }// 设置背景颜色结束
        
        int day = myDate.getDate(); // 日期
        txtDay.setText(String.valueOf(day));
        txtDay.setId(position + 500);
        iv.setTag(myDate);
                
        // Android获得屏幕的宽和高    
        WindowManager windowManager = activity.getWindowManager();    
        Display display = windowManager.getDefaultDisplay();    
        int screenWidth = display.getWidth();    
        
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(android.view.ViewGroup.LayoutParams.WRAP_CONTENT, android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
        iv.addView(txtDay, lp);

        LinearLayout.LayoutParams lp1 = new LinearLayout.LayoutParams(android.view.ViewGroup.LayoutParams.WRAP_CONTENT, android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
        iv.addView(txtToDay, lp1);
        
        if(720 >= screenWidth){
        	iv.setLayoutParams(new LayoutParams(80,80));
        }else if(1080 <= screenWidth){
        	iv.setLayoutParams(new LayoutParams(110,110));
        }
        
        imageLayout.addView(iv);
        
        return imageLayout;
    }

    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
    }

    private Boolean equalsDate(Date date1, Date date2) {

        if (date1.getYear() == date2.getYear() && date1.getMonth() == date2.getMonth() && date1.getDate() == date2.getDate()) {
            return true;
        } else {
            return false;
        }

    }

}
