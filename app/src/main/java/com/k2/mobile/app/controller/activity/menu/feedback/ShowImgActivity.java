package com.k2.mobile.app.controller.activity.menu.feedback;

import java.util.ArrayList;
import java.util.List;

import com.k2.mobile.app.R;
import com.k2.mobile.app.common.config.IntentConstant;
import com.k2.mobile.app.controller.core.BaseApp;
import com.k2.mobile.app.model.adapter.GuideAdapter;
import com.k2.mobile.app.model.album.BitmapCache;
import com.k2.mobile.app.model.bean.FileBean;
import com.k2.mobile.app.utils.ImageLoaderUtil;
import com.k2.mobile.app.utils.ImageLoderUtil;
import com.k2.mobile.app.utils.LogUtil;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.imageaware.ImageAware;
import com.nostra13.universalimageloader.core.imageaware.ImageViewAware;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;

/**
 * @Title ShowImgActivity.java
 * @Package com.oppo.mo.controller.activity.menu;
 * @Description 图片预览
 * @Company  K2
 * 
 * @author linqijun
 * @date 2015-7-31 10:50:57
 * @version V1.0
 */
@SuppressLint("NewApi")
public class ShowImgActivity extends Activity implements OnClickListener, OnPageChangeListener{

	// 文件集合
	private List<FileBean> fileList = null;
	private ViewPager vp_showImg = null;
	private LinearLayout ll = null;
	private GuideAdapter vpAdapter;
	private List<View> views;
	// 记录当前选中位置
	private int currentIndex;
	private ImageLoaderUtil imageLoader;
	private int size = 0;
	private int position = -1; // 当前位置
	private boolean localFlag;
	private BitmapCache cache;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 去除头部
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_guide);
		BaseApp.addActivity(this);
		initView();
		initListener();
	}

	/**
	 * 方法名: initView() 
	 * 功 能 : 初始化 
	 * 参 数 : void 
	 * 返回值: void
	 */
	@SuppressWarnings("unchecked")
	@SuppressLint("NewApi")
	private void initView() {	
		//缓存管理类
		ImageLoderUtil.initImageLoaderConfig(this);
		imageLoader = new ImageLoaderUtil(this);
		fileList = (List<FileBean>) getIntent().getSerializableExtra(IntentConstant.EXTRA_IMAGE_LIST_SHOWIMG);
		position = getIntent().getIntExtra(IntentConstant.EXTRA_IMAGE_LIST_SHOWIMG_POSITION, -1);
		localFlag = getIntent().getBooleanExtra(IntentConstant.EXTRA_IMAGE_LIST_LOCAL_FLAG, false);

		vp_showImg = (ViewPager) findViewById(R.id.viewpager); 
		ll = (LinearLayout) findViewById(R.id.ll);
		ll.setVisibility(View.GONE);
		views = new ArrayList<View>();	 

		LinearLayout.LayoutParams mParams = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		try {
			if(null != fileList){
				size = fileList.size();
				// 初始化引导图片列表
				for (int i = 0; i < size; i++) {
					ImageView iv = new ImageView(this);
					iv.setLayoutParams(mParams);
					String imagePath = fileList.get(i).getFilePath();
					if(null != imagePath) {
						if(localFlag) {
							ImageAware imageAware = new ImageViewAware(iv, true);
							ImageLoader.getInstance().displayImage("file://"+imagePath, imageAware, getOptions());
						} else {
							imageLoader.DisplayImage(imagePath, iv, 0, null); // 加载远程图片
						}
					} 
					views.add(iv);
				}
				vpAdapter = new GuideAdapter(views);
				vp_showImg.setAdapter(vpAdapter);
				vp_showImg.setCurrentItem(position);
			}		
		}catch(Exception e) {
			LogUtil.e("ShowImgActivity error");
		}

	}

	/**
	 * 方法名: initListener() 
	 * 功 能 : 初始化 监听器 
	 * 参 数 : void 
	 * 返回值: void  
	 */
	private void initListener() {
		vp_showImg.setOnPageChangeListener(this);
	}

	/**
	 * @Title: setCurView
	 * @Description: 设置当前的引导页
	 * @param @param position
	 * @return void 返回类型
	 * @throws
	 */
	private void setCurView(int position) {
		if (position < 0 || position >= size) {
			return;
		}
		vp_showImg.setCurrentItem(position);
	}

	/**
	 * @Title: setCurDot
	 * @Description: 当前引导小点
	 * @param @param positon
	 * @return void 返回类型
	 * @throws
	 */
	private void setCurDot(int positon) {
		if (positon < 0 || positon > size - 1 || currentIndex == positon) {
			return;
		}

		currentIndex = positon;
	}

	// 当滑动状态改变时调用
	@Override
	public void onPageScrollStateChanged(int arg0) {
		// TODO Auto-generated method stub
	}

	// 当前页面被滑动时调用
	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {
		// TODO Auto-generated method stub
	}

	// 当新的页面被选中时调用
	@Override
	public void onPageSelected(int arg0) {
		// 设置底部小点选中状态
		//		setCurDot(arg0);
	}

	@Override
	public void onClick(View v) {
		int position = (Integer) v.getTag();
		setCurView(position);
		setCurDot(position);
	}

	
	private DisplayImageOptions getOptions() {
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                    .cacheInMemory(true)
                    .cacheOnDisk(true)
                    .showImageOnLoading(R.drawable.default_img)
                    .imageScaleType(ImageScaleType.IN_SAMPLE_INT)
                    .bitmapConfig(Bitmap.Config.RGB_565)
                    .build();
        return options;
    }
}
