package com.k2.mobile.app.controller.activity.menu.task;

import java.util.ArrayList;

import com.k2.mobile.app.R;
import com.k2.mobile.app.controller.core.BaseApp;
import com.k2.mobile.app.model.adapter.MyFragmentPagerAdapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.TranslateAnimation;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * 
 * @ClassName: MyTaskActivity
 * @Description: 我的任务
 * @author linqijun
 * @date 2015-3-12 下午9:00:00
 * 
 */
public class MyTaskActivity extends FragmentActivity implements OnClickListener {
	
	private final int REQUESTCODE_DIALOG = 1109;
	// 返回
	private Button btn_back;
	// 头部标题
	private TextView tv_title;
	private TextView tv_search;
	private ViewPager mViewPager;			// 页卡内容
	private ImageView iv_cursor;			// 动画图片
	private TextView tv_i_assigned, tv_assigned_me;	// 选项名称（我指派、指派给我）
	private ArrayList<Fragment> fragments;	// Tab页面列表
	private int currIndex = 0;				// 当前页卡编号
	private static final int pageSize = 2;	// 页卡总数
	private int width = 0;					// 滑动图片宽度
	private int beforePosition = 0; 		// 上一次位置
	private MyFragmentPagerAdapter mAdapter;
	private int selectedColor, unSelectedColor;
	private AssignedIDoFragment mAssignedIDoFragment = null;		// 我指派
	private AssignedToMeFragment mAssignedToIFragment = null;		// 指派给我
	private boolean flag = true;
	private Intent mIntent;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 去除头部
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_my_task);
		initView();
		initListener();
		BaseApp.addActivity(this);
	}

	/**
	 * 方法名: initView() 
	 * 功 能 : 初始化 
	 * 参 数 : void 
	 * 返回值: void
	 */
	@SuppressLint("NewApi")
	private void initView() {	
		
		tv_title = (TextView) findViewById(R.id.tv_title);
		tv_search = (TextView) findViewById(R.id.tv_sech);
		btn_back = (Button) findViewById(R.id.btn_back);
		
		// 设置滑动图片占屏幕的一半
		iv_cursor = (ImageView) findViewById(R.id.iv_cursor);
		WindowManager wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
		width = wm.getDefaultDisplay().getWidth() / pageSize;
		LayoutParams layoutParams = iv_cursor.getLayoutParams();
		layoutParams.width = width;
		iv_cursor.setLayoutParams(layoutParams);
		
		// 因使用公共头部
		tv_search.setText("");
		// 设置tv_search背景图片和样式
		Drawable drawable = getResources().getDrawable(R.drawable.new_selector);
		// setBackground()方法，在16版本以上才有
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
			tv_search.setBackground(drawable);
		} else {
			tv_search.setBackgroundDrawable(drawable);
		}
		// 重新计算tv_search的大小
		int w = View.MeasureSpec.makeMeasureSpec(0,View.MeasureSpec.UNSPECIFIED);  
		int h = View.MeasureSpec.makeMeasureSpec(0,View.MeasureSpec.UNSPECIFIED);  

		tv_search.measure(w, h);  
		int height = tv_search.getMeasuredHeight();  
		int width = tv_search.getMeasuredWidth();  

		LayoutParams lp = tv_search.getLayoutParams();    
		lp.width = (int)(width*0.6);
		lp.height = (int)(height*0.6);        
		tv_search.setLayoutParams(lp);
			
		tv_title.setText(getString(R.string.my_tasks));
		
		selectedColor = getResources().getColor(R.color.main_title_background_color);
		unSelectedColor = getResources().getColor(R.color.main_tv_font);
		
		mViewPager = (ViewPager) findViewById(R.id.view_pager);

		// 化选项导航
		tv_i_assigned = (TextView) findViewById(R.id.tv_i_assigned);
		tv_assigned_me = (TextView) findViewById(R.id.tv_assigned_me);
		// 设置第一个选项背景
		tv_i_assigned.setTextColor(selectedColor);
		// 初始化viewPager
		initViewPager();

	}
	
	/**
	 * 方法名: initViewPager() 
	 * 功 能 : 初始化viewPager
	 * 参 数 : void 
	 * 返回值: void
	 */
	private void initViewPager() {
		fragments = new ArrayList<Fragment>();
		mAssignedIDoFragment = new AssignedIDoFragment();
		mAssignedToIFragment = new AssignedToMeFragment();
		
		fragments.add(mAssignedIDoFragment);
		fragments.add(mAssignedToIFragment);
		
		mAdapter = new MyFragmentPagerAdapter(getSupportFragmentManager(), fragments);
		mViewPager.setAdapter(mAdapter);
		mViewPager.setOnPageChangeListener(new MyOnPageChangeListener());
		mViewPager.setCurrentItem(0);
	}

	/**
	 * 方法名: initListener() 
	 * 功 能 : 初始化 监听器 
	 * 参 数 : void 
	 * 返回值: void  
	 */
	private void initListener() {
		btn_back.setOnClickListener(this);
		tv_search.setOnClickListener(this);
		tv_i_assigned.setOnClickListener(new MyOnClickListener(0));
		tv_assigned_me.setOnClickListener(new MyOnClickListener(1));
	}
	
	/**
	 * 
	 * @ClassName: MyOnClickListener
	 * @Description: 头标点击监听
	 *
	 */
	private class MyOnClickListener implements OnClickListener {		
		private int index = 0;
		public MyOnClickListener(int i) {
			index = i;
		}
		@Override
		public void onClick(View v) {
			switch (index) {
			case 0:
				tv_i_assigned.setTextColor(selectedColor);
				tv_assigned_me.setTextColor(unSelectedColor);
				break;
			case 1:
				tv_i_assigned.setTextColor(unSelectedColor);
				tv_assigned_me.setTextColor(selectedColor);
				break;
			}
			mViewPager.setCurrentItem(index);
			if(flag){
				mAssignedToIFragment.requestServer();
				flag = false;
			}
		}
		
	}
	
	
	/**
	 * 方法名: MyOnPageChangeListener
	 * 功 能 : 为选项卡绑定监听器
	 * 参 数 : void 
	 * 返回值: void
	 */
	public class MyOnPageChangeListener implements OnPageChangeListener {
		@Override
		public void onPageScrollStateChanged(int index) {
		}
		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
			TranslateAnimation anima = null;
			if (arg0 == currIndex) { //	往右滑的时候arg0的值为当前的项的值
				anima = new TranslateAnimation(beforePosition, currIndex * width + width * arg1, 0, 0);
				beforePosition = (int) (currIndex * width + width * arg1);
				anima.setFillAfter(true);
				anima.setDuration(500);
				iv_cursor.startAnimation(anima);
			}
			else if (arg0 + 1 == currIndex) {	// 往左滑的时候arg0的值为前一项的值
				anima = new TranslateAnimation(beforePosition, arg0 * width + width * arg1, 0, 0);
				beforePosition = (int) (arg0 * width + width * arg1);
				anima.setFillAfter(true);
				anima.setDuration(500);
				iv_cursor.startAnimation(anima);
			}
		}
		@Override
		public void onPageSelected(int index) {
			currIndex = index;
			switch (index) {
				case 0:
					tv_i_assigned.setTextColor(selectedColor);
					tv_assigned_me.setTextColor(unSelectedColor);
					break;
				case 1:
					tv_i_assigned.setTextColor(unSelectedColor);
					tv_assigned_me.setTextColor(selectedColor);
					if(flag){
						mAssignedToIFragment.requestServer();
						flag = false;
					}
					break;
			}
		}
		
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.btn_back:
				this.finish();
				break;
			case R.id.tv_sech:
				mIntent = new Intent(MyTaskActivity.this, AddTaskActivity.class);
				startActivityForResult(mIntent, REQUESTCODE_DIALOG);
				finish();
		}
	}
	
	@Override
	protected void onResume() {
		super.onResume();
	}
		
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(REQUESTCODE_DIALOG == requestCode){
		}
	};	
}