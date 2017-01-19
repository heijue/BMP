package com.k2.mobile.app.controller.activity.fragment;

import java.util.ArrayList;

import com.k2.mobile.app.R;
import com.k2.mobile.app.common.config.BroadcastNotice;
import com.k2.mobile.app.controller.activity.menu.examine.ApprovedFragment;
import com.k2.mobile.app.controller.activity.menu.examine.IinitiatedFragment;
import com.k2.mobile.app.controller.activity.menu.personalCenter.PersonalCenterActivity;
import com.k2.mobile.app.model.adapter.MyFragmentPagerAdapter;

import android.annotation.SuppressLint;
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
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * @Title ApprovalFragment.java
 * @Package com.oppo.mo.controller.activity.fragment
 * @Description 审批
 * @Company  K2
 * 
 * @author linqijun
 * @date 2015-04-22 20:45:03
 * @version V1.0
 */
public class ApprovalFragment extends Fragment implements OnClickListener {
	
	private View view = null;
	private TextView tv_title;
	private ImageView iv_per_center;
	private ViewPager viewPager;			// 页卡内容
	private ImageView iv_cursor;			// 动画图片
	private TextView tv_launch, tv_pending;	// 选项名称（我发起的、我审批的）
	private ArrayList<Fragment> fragments;	// Tab页面列表
	private int currIndex = 0;				// 当前页卡编号
	private static final int pageSize = 2;// 页卡总数
	private int width = 0;					// 滑动图片宽度
	private int beforePosition = 0; 		// 上一次位置
	private MyFragmentPagerAdapter mAdapter;
	private int selectedColor, unSelectedColor;
		
	private IinitiatedFragment mPAFragment = null;	// 我的待阅
	private ApprovedFragment mPFragment = null;		// 我审批的
	private IncomingReceiver iReceiver = null;
	
	private boolean flag = true;
		
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.fragment_approval, null);
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
		tv_launch = (TextView) view.findViewById(R.id.tv_launch);
		tv_pending = (TextView) view.findViewById(R.id.tv_pending);
		tv_title.setText(R.string.my_approval_process);
		selectedColor = getResources().getColor(R.color.main_title_background_color);
		unSelectedColor = getResources().getColor(R.color.main_tv_font);
		
		iv_per_center.setVisibility(View.GONE);
		
		initImageView(view);
		initTextView(view);
		initViewPager(view);
	}
	
	/**
	 * 初始化Viewpager页
	 */
	private void initViewPager(View parentView) {
		mPAFragment = new IinitiatedFragment();
		mPFragment = new ApprovedFragment();
		
		viewPager = (ViewPager) parentView.findViewById(R.id.view_pager);
		fragments = new ArrayList<Fragment>();
		fragments.add(mPFragment);
		fragments.add(mPAFragment);
		
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
		tv_launch = (TextView) parentView.findViewById(R.id.tv_launch);
		tv_pending = (TextView) parentView.findViewById(R.id.tv_pending);

		tv_pending.setTextColor(selectedColor);
		tv_launch.setTextColor(unSelectedColor);

		tv_pending.setOnClickListener(new MyOnClickListener(0));
		tv_launch.setOnClickListener(new MyOnClickListener(1));
	}
	
	/**
	 * 初始化动画，这个就是页卡滑动时，下面的横线也滑动的效果，在这里需要计算一些数据
	 */
	private void initImageView(View parentView) {
		iv_cursor = (ImageView) parentView.findViewById(R.id.iv_cursor);
		WindowManager wm = (WindowManager) getActivity().getSystemService(Context.WINDOW_SERVICE);
		width = wm.getDefaultDisplay().getWidth() / pageSize;
		LayoutParams layoutParams = iv_cursor.getLayoutParams();
		layoutParams.width = width;
		iv_cursor.setLayoutParams(layoutParams);
	}
	
	/**
	 * @Title: createFilter
	 * @Description: 创建IntentFilter
	 * @param void
	 * @return void 
	 * @throws
	 */
	private void createFilter() {
		IntentFilter filter = new IntentFilter();
		filter.addAction(BroadcastNotice.NOT_APPROVAL);
		filter.addAction(BroadcastNotice.APPROVAL_DELETE_TIPS);
		filter.addAction(BroadcastNotice.APPROVAL_COUNT_SUB);
		filter.addAction(BroadcastNotice.APPROVAL_DOUBLE_CLICK);
		iReceiver = new IncomingReceiver();
		// 注册广播
		getActivity().registerReceiver(iReceiver, filter);
	}
	
	/**
	 * 接收广播
	 */ 
	private class IncomingReceiver extends BroadcastReceiver{
		@SuppressLint("Recycle")
		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();  
	        if(BroadcastNotice.APPROVAL_DOUBLE_CLICK.equals(action)){
	        	 if(0 == currIndex){
	        		 mPFragment.doubleClickRefresh(); 
	        	 }else if(1 == currIndex){
	        		 mPAFragment.doubleClickRefresh(); 
	        	 }
	        }
		}
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
					tv_pending.setTextColor(selectedColor);
					tv_launch.setTextColor(unSelectedColor);
					break;
				case 1:
					tv_pending.setTextColor(unSelectedColor);
					tv_launch.setTextColor(selectedColor);
					break;
			}
			viewPager.setCurrentItem(index);
		}
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
				iv_cursor.startAnimation(anima);
			}else if (arg0 + 1 == currIndex) {	// 往左滑的时候arg0的值为前一项的值
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
					tv_pending.setTextColor(selectedColor);
					tv_launch.setTextColor(unSelectedColor);
//					if(flag){
//						flag = false;
//						mPFragment.checkFlag = false;
//						String info = mPFragment.getQuestData();
//						mPFragment.requestServer(info);
//					}
					break;
				case 1:
					tv_pending.setTextColor(unSelectedColor);
					tv_launch.setTextColor(selectedColor);
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

	public void getApproval(){
		if(flag){
			flag = false;
			mPFragment.requestServer();
		}
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
	
	@Override
	public void onDestroy() {
		// 停止接收广播
		if(null != iReceiver){
			getActivity().unregisterReceiver(iReceiver);
		}
		super.onDestroy();
	}
}
