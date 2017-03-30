package com.k2.mobile.app.controller.activity.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import com.k2.mobile.app.R;
import com.k2.mobile.app.common.config.BroadcastNotice;
import com.k2.mobile.app.controller.activity.menu.application.CommonFragment;
import com.k2.mobile.app.controller.activity.menu.application.MoreFragment;
import com.k2.mobile.app.controller.activity.menu.personalCenter.PersonalCenterActivity;
import com.k2.mobile.app.model.adapter.MyFragmentPagerAdapter;

import java.util.ArrayList;

/**
 * @Title ApplicationFragment.java
 * @Package com.oppo.mo.controller.activity.fragment
 * @Description 首页－应用
 * @Company  K2
 * 
 * @author linqijun
 * @date 2015-04-22 20:45:03
 * @version V1.0
 */
public class ApplicationFragment extends Fragment implements OnClickListener {
	
	private View view;
	private TextView tv_title;
	private ImageView iv_per_center;
	private ViewPager viewPager;			// 页卡内容
	private ImageView imageView;			// 动画图片
	private TextView tv_common, tv_more;	// 选项名称
	private ArrayList<Fragment> fragments;	// Tab页面列表
	private int currIndex = 0;				// 当前页卡编号
	private static final int pageSize = 2;	// 页卡总数
	private int width = 0;					// 滑动图片宽度
	private int beforePosition = 0; 		// 上一次位置
	private MyFragmentPagerAdapter mAdapter;
	private int selectedColor, unSelectedColor;
	private CommonFragment cFragment = null;
	private MoreFragment mFragment = null;
	private AddMoreReceiver receiver;
	private boolean flag = true;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.fragment_application, null);
        initView();
		initListener();
		createFilter();
		return view;
	}
	
	/*
	 * 方法名: initView()  
	 * 功 能 : 初始化布局控件 
	 * 参 数 : void 
	 * 返回值: void
	 */
	private void initView() {
		tv_title = (TextView) view.findViewById(R.id.tv_title);
		iv_per_center = (ImageView) view.findViewById(R.id.iv_per_center);
		tv_title.setText(getString(R.string.mobile_oa));
		selectedColor = getResources().getColor(R.color.main_title_background_color);
		unSelectedColor = getResources().getColor(R.color.main_tv_font);
		
		initImageView(view);
		initTextView(view);
		initViewPager(view);
		
		iv_per_center.setVisibility(View.GONE);
	}
	
	/**
	 * @Title: createFilter
	 * @Description: 创建IntentFilter
	 * @param void
	 * @return void 
	 * @throws
	 */
	private void createFilter() {
		receiver = new AddMoreReceiver();
		IntentFilter filter = new IntentFilter();
		filter.addAction(BroadcastNotice.HOME_ADD_MORE_ICO);
		filter.addAction(BroadcastNotice.ADD_UPDATE_COMMOM);
		// 注册广播
		getActivity().registerReceiver(receiver, filter );
	}
	
	/**
	 * 接收广播
	 */
	private class AddMoreReceiver extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			if(BroadcastNotice.HOME_ADD_MORE_ICO.equals(action)){
				tv_common.setTextColor(unSelectedColor);
				tv_more.setTextColor(selectedColor);
				viewPager.setCurrentItem(1);
				mFragment.showCheckBox();
				mFragment.showSelect();
			}else if(BroadcastNotice.ADD_UPDATE_COMMOM.equals(action)){
				tv_common.setTextColor(selectedColor);
				tv_more.setTextColor(unSelectedColor);
				viewPager.setCurrentItem(0);
				mFragment.hidCheckBox();
			}
		}
	}
	
	/**
	 * 初始化Viewpager页
	 */
	private void initViewPager(View parentView) {
		cFragment = new CommonFragment();
		mFragment = new MoreFragment();
		
		viewPager = (ViewPager) parentView.findViewById(R.id.view_pager);
		fragments = new ArrayList<Fragment>();
		fragments.add(cFragment);
		fragments.add(mFragment);
		
		mAdapter = new MyFragmentPagerAdapter(getChildFragmentManager(), fragments);
		viewPager.setAdapter(mAdapter);
		viewPager.setCurrentItem(0);
		viewPager.setOnPageChangeListener(new MyOnPageChangeListener());
	}
	
	/**
	 * 初始化头标
	 * 
	 */
	private void initTextView(View parentView) {
		tv_common = (TextView) parentView.findViewById(R.id.tv_common);
		tv_more = (TextView) parentView.findViewById(R.id.tv_more);

		tv_common.setTextColor(selectedColor);
		tv_more.setTextColor(unSelectedColor);
		
		tv_common.setOnClickListener(new MyOnClickListener(0));
		tv_more.setOnClickListener(new MyOnClickListener(1));
	}
	
	/**
	 * 初始化动画，这个就是页卡滑动时，下面的横线也滑动的效果，在这里需要计算一些数据
	 */
	private void initImageView(View parentView) {
		imageView = (ImageView) parentView.findViewById(R.id.iv_cursor);
		WindowManager wm = (WindowManager) getActivity().getSystemService(Context.WINDOW_SERVICE);
		width = wm.getDefaultDisplay().getWidth() / pageSize;
		LayoutParams layoutParams = imageView.getLayoutParams();
		layoutParams.width = width;
		imageView.setLayoutParams(layoutParams);
	}
	
	/**
	 * 头标点击监听
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
				tv_common.setTextColor(selectedColor);
				tv_more.setTextColor(unSelectedColor);
				mFragment.hidCheckBox();
				mFragment.hideSelect();
				mFragment.checkSelectAll();
				break;
			case 1:
				tv_common.setTextColor(unSelectedColor);
				tv_more.setTextColor(selectedColor);
				cFragment.setStart();
				if(flag){
					flag = false;
					String info = mFragment.getMenu();
					mFragment.operMenu(info);
				}
				
				break;
			}
			viewPager.setCurrentItem(index);
		}
	}
	// 提供外部退出长按
	public boolean exitLongClick(){
		int tmp = mFragment.getType();
		if(1 == tmp){
			mFragment.hidCheckBox();
			mFragment.hideSelect();
			mFragment.checkSelectAll();
			viewPager.setCurrentItem(0);
			return false;
		}
		
		return cFragment.setStart();
	}
		
	/**
	 * 为选项卡绑定监听器
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
				imageView.startAnimation(anima);
			}
			else if (arg0 + 1 == currIndex) {	// 往左滑的时候arg0的值为前一项的值
				anima = new TranslateAnimation(beforePosition, arg0 * width + width * arg1, 0, 0);
				beforePosition = (int) (arg0 * width + width * arg1);
				anima.setFillAfter(true);
				anima.setDuration(500);
				imageView.startAnimation(anima);
				cFragment.setStart();
				if(flag){
					flag = false;
					String info = mFragment.getMenu();
					mFragment.operMenu(info);
				}
			}
		}
		
		@Override
		public void onPageSelected(int index) {
			currIndex = index;
			switch (index) {
				case 0:
					tv_common.setTextColor(selectedColor);
					tv_more.setTextColor(unSelectedColor);
					mFragment.hidCheckBox();
					mFragment.hideSelect();
//					mFragment.checkPer();
					mFragment.checkSelectAll();
					break;
				case 1:
					tv_common.setTextColor(unSelectedColor);
					tv_more.setTextColor(selectedColor);
					break;
			}
		}
	}
	
	/*
	 * 方法名: initListener()  
	 * 功 能 : 初始化事件
	 * 参 数 : void 
	 * 返回值: void
	 */
	private void initListener() {
		iv_per_center.setOnClickListener(this);
	}
	
	@Override
	public void onDestroy() {
		getActivity().unregisterReceiver(receiver);
		super.onDestroy();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.iv_per_center:	// 个人中心
			startActivity(PersonalCenterActivity.class);
			getActivity().overridePendingTransition(R.anim.push_right_in, R.anim.abc_fade_out);
			break;
		}
	}
	
	/**
	 * @Title: startActivity
	 * @Description: 跳转Activity
	 * @return void    返回类型
	 * @throws
	 */
	public void startActivity(Class<?> activity) {
		Intent intent = new Intent(getActivity(), activity);
		startActivity(intent);
	}
	
}
