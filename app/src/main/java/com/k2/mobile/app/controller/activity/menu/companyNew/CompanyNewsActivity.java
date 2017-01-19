package com.k2.mobile.app.controller.activity.menu.companyNew;

import java.util.ArrayList;

import com.k2.mobile.app.R;
import com.k2.mobile.app.controller.core.BaseApp;
import com.k2.mobile.app.model.adapter.CompanyNewsAdapter;

import android.annotation.SuppressLint;
import android.content.Context;
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
import android.view.Window;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * 
 * @ClassName: CompanyNewsActivity
 * @Description: 公司新闻
 * @author liangzy
 * @date 2015-3-23 下午15:00:57
 *
 */
public class CompanyNewsActivity extends FragmentActivity implements OnClickListener {
	
	// 头部标题
	private TextView tv_title;
	// 返回
	private Button btn_back;
	// 搜索
	private TextView tv_search;
	// 页卡内容
	private ViewPager mViewPager;
	// 页卡总数
	private static final int pageSize = 2;
	// 选项名称
	private TextView tv_com_news, tv_process_file;
	// 当前页卡编号
	private int currIndex = 0;
	// 上一次位置
	private int beforePosition = 0;
	// 滑动图片宽度
	private int width = 0;
	// Fragment页面列表
	private ArrayList<Fragment> fragments;
	// 适配器
	private CompanyNewsAdapter mAdapter;
	// 选中与未选中选项颜色
	private int selectedColor, unSelectedColor;
	// 滑动图片
	private ImageView iv_cursor;
	
	private NewsComFragment marketFragment = null;
	private NewsProcessFragment devFragment = null;
	// 计数器
	private int count = 0;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 去除头部
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_menu_company_news);
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
		btn_back = (Button) findViewById(R.id.btn_back);
		tv_title = (TextView) findViewById(R.id.tv_title);
		tv_search = (TextView) findViewById(R.id.tv_sech);
		
		// 设置滑动图片占屏幕的一半
		iv_cursor = (ImageView) findViewById(R.id.iv_cursor);
		WindowManager wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
		width = wm.getDefaultDisplay().getWidth() / pageSize;
		LayoutParams layoutParams = iv_cursor.getLayoutParams();
		layoutParams.width = width;
		iv_cursor.setLayoutParams(layoutParams);
		
		// 因使用公共头部，在此故tv_search变成写邮件
		tv_search.setText("");
		tv_search.setVisibility(View.GONE);
		// 设置tv_search背景图片和样式
		Drawable drawable = getResources().getDrawable(R.drawable.search);
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
		lp.width = (int)(width*0.7);
		lp.height = (int)(height*0.7); 
		tv_search.setLayoutParams(lp);

		tv_title.setText(getString(R.string.company_news));
		
		selectedColor = getResources().getColor(R.color.main_title_background_color);
		unSelectedColor = getResources().getColor(R.color.main_tv_font);
		
		mViewPager = (ViewPager) findViewById(R.id.view_pager);
		
		// 化选项导航
		tv_com_news = (TextView) findViewById(R.id.tv_com_news);
		tv_process_file = (TextView) findViewById(R.id.tv_process_file);
		// 设置第一个选项背景
		tv_com_news.setTextColor(selectedColor);
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
		marketFragment = new NewsComFragment();
		devFragment = new NewsProcessFragment();
		fragments.add(marketFragment);
		fragments.add(devFragment);
		mAdapter = new CompanyNewsAdapter(getSupportFragmentManager(), fragments);
		mViewPager.setAdapter(mAdapter);
		mViewPager.setOnPageChangeListener(new MyOnPageChangeListener());
		mViewPager.setCurrentItem(0);
	}
	
	/**
	 * 方法名: initListener() 
	 * 功 能 : 初始化监听器
	 * 参 数 : void 
	 * 返回值: void
	 */
	private void initListener() {
		tv_com_news.setOnClickListener(new MyOnClickListener(0));
		tv_process_file.setOnClickListener(new MyOnClickListener(1));
		btn_back.setOnClickListener(this);
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
				tv_com_news.setTextColor(selectedColor);
				tv_process_file.setTextColor(unSelectedColor);
				break;
			case 1:
				tv_com_news.setTextColor(unSelectedColor);
				tv_process_file.setTextColor(selectedColor);
				if (count == 0) {
					devFragment.remoteRequest();
				}
				count = 1;
				break;
			}
			mViewPager.setCurrentItem(index);
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
				if (count == 0) {
					devFragment.remoteRequest();
				}
				count = 1;
			}
		}
		@Override
		public void onPageSelected(int index) {
			currIndex = index;
			switch (index) {
				case 0:
					tv_com_news.setTextColor(selectedColor);
					tv_process_file.setTextColor(unSelectedColor);
					break;
				case 1:
					tv_com_news.setTextColor(unSelectedColor);
					tv_process_file.setTextColor(selectedColor);
					break;
			}
		}
		
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_back:
			finish();
			break;
		}
	}

}
