package com.k2.mobile.app.controller.activity;

import java.util.ArrayList;
import java.util.List;

import com.k2.mobile.app.R;
import com.k2.mobile.app.controller.activity.login.LoginActivity;
import com.k2.mobile.app.controller.core.BaseActivity;
import com.k2.mobile.app.model.adapter.GuideAdapter;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

/**
 * 
 * @ClassName: GuideActivity
 * @Description: 新手引导页
 * @author liangzy
 * @date 2015-3-6 上午10:19:52
 * 
 */
public class GuideActivity extends BaseActivity implements OnClickListener,
		OnPageChangeListener {

	private ViewPager vp;
	private GuideAdapter vpAdapter;
	private List<View> views;
	private Button button;

	// 引导图片资源
	private static final int[] pics = { R.drawable.icon, R.drawable.icon,
			R.drawable.icon, R.drawable.icon };
	// 底部小店图片
	private ImageView[] dots;
	// 记录当前选中位置
	private int currentIndex;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_guide);
		button = (Button) findViewById(R.id.button);
		views = new ArrayList<View>();
		LinearLayout.LayoutParams mParams = new LinearLayout.LayoutParams(
				LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT);

		// 初始化引导图片列表
		for (int i = 0; i < pics.length; i++) {
			ImageView iv = new ImageView(this);
			iv.setLayoutParams(mParams);
			iv.setImageResource(pics[i]);
			views.add(iv);
		}
		vp = (ViewPager) findViewById(R.id.viewpager);
		// 初始化Adapter
		vpAdapter = new GuideAdapter(views);
		vp.setAdapter(vpAdapter);
		// 绑定回调
		vp.setOnPageChangeListener(this);
		// 初始化底部小圆点
		initDots();
		button.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				startActivity(LoginActivity.class);
				GuideActivity.this.finish();
			}
		});
	}

	private void initDots() {
		LinearLayout ll = (LinearLayout) findViewById(R.id.ll);

		dots = new ImageView[pics.length];

		// 循环取得小点图片
		for (int i = 0; i < pics.length; i++) {
			dots[i] = (ImageView) ll.getChildAt(i);
			// 都设为灰色
			dots[i].setEnabled(true);
			dots[i].setOnClickListener(this);
			// 设置位置tag，方便取出与当前位置对应
			dots[i].setTag(i);
		}

		currentIndex = 0;
		// 设置为白色 - 状态
		dots[currentIndex].setEnabled(false);
	}

	/**
	 * @Title: setCurView
	 * @Description: 设置当前的引导页
	 * @param @param position
	 * @return void 返回类型
	 * @throws
	 */
	private void setCurView(int position) {
		if (position < 0 || position >= pics.length) {
			return;
		}
		vp.setCurrentItem(position);
	}

	/**
	 * @Title: setCurDot
	 * @Description: 当前引导小点
	 * @param @param positon
	 * @return void 返回类型
	 * @throws
	 */
	private void setCurDot(int positon) {
		if (positon < 0 || positon > pics.length - 1 || currentIndex == positon) {
			return;
		}

		dots[positon].setEnabled(false);
		dots[currentIndex].setEnabled(true);

		currentIndex = positon;
	}

	// 当滑动状态改变时调用
	@Override
	public void onPageScrollStateChanged(int arg0) {
		// TODO Auto-generated method stub
	}

	// 当当前页面被滑动时调用
	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {
		// TODO Auto-generated method stub
	}

	// 当新的页面被选中时调用
	@Override
	public void onPageSelected(int arg0) {
		// 设置底部小点选中状态
		setCurDot(arg0);
		if (arg0 == 3) {
			button.setVisibility(View.VISIBLE);

		} else {
			button.setVisibility(View.GONE);
		}
	}

	@Override
	public void onClick(View v) {
		int position = (Integer) v.getTag();
		setCurView(position);
		setCurDot(position);
	}

}