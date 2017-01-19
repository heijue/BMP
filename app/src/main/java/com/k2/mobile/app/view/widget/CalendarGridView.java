package com.k2.mobile.app.view.widget;


import android.app.Activity;
import android.content.Context;
import android.view.Display;
import android.view.Gravity;
import android.view.WindowManager;
import android.widget.GridView;
import android.widget.LinearLayout;

/**
* @Title CalendarGridView.java
* @Package com.oppo.mo.view.widget;
* @Description 用于生成日历展示的GridView布局
* @Company  K2
* 
* @author linqijun
* @date 2015-03-19 11:19:00
* @version V1.0
*/
public class CalendarGridView extends GridView {

    /**
     * 当前操作的上下文对象
     */
    private Context mContext;

    /**
     * CalendarGridView 构造器
     *
     * @param context 当前操作的上下文对象
     */
    public CalendarGridView(Context context) {
        super(context);
        mContext = context;

        setGirdView();
    }

    /**
     * 初始化gridView 控件的布局
     */
    private void setGirdView() {
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(android.view.ViewGroup.LayoutParams.FILL_PARENT, android.view.ViewGroup.LayoutParams.WRAP_CONTENT);

        setLayoutParams(params);
        setNumColumns(7);						// 设置每行列数
        setGravity(Gravity.CENTER_VERTICAL);	// 位置居中
        setVerticalSpacing(1);					// 垂直间隔
        setHorizontalSpacing(1);				// 水平间隔
        // 设置分割线
//        setBackgroundColor(getResources().getColor(R.color.calendar_background));

        WindowManager windowManager = ((Activity) mContext).getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        int i = display.getWidth() / 6;
        int j = display.getWidth() - (i * 6);
        int x = j / 2;
        setPadding(x, 0, 0, 0);// 居中
    }
}
