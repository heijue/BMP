package com.k2.mobile.app.view.widget;

import com.k2.mobile.app.R;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.PopupWindow;

/**
 * @Title PopwinPerCenter.java
 * @Package com.oppo.mo.view.widget;
 * @Description 用于显示更改头像弹出的窗口
 * @Company  K2
 * 
 * @author liangzy
 * @date 2015-04-02 11:19:00
 * @version V1.0
 */
public class PopwinPerCenter extends PopupWindow {

	private Button btn_take_photo, btn_pick_photo, btn_cancel;
	private View mMenuView;

	public PopwinPerCenter(Activity context, OnClickListener itemsOnClick) {
		super(context);
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mMenuView = inflater.inflate(R.layout.popup_per_center, null);
		btn_take_photo = (Button) mMenuView.findViewById(R.id.btn_take_photo);
		btn_pick_photo = (Button) mMenuView.findViewById(R.id.btn_pick_photo);
		btn_cancel = (Button) mMenuView.findViewById(R.id.btn_cancel);
		// 取消按钮
		btn_cancel.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// 销毁弹出框
				dismiss();
			}
		});
		
		// 设置按钮监听
		btn_pick_photo.setOnClickListener(itemsOnClick);
		btn_take_photo.setOnClickListener(itemsOnClick);
		// 设置显示的View
		this.setContentView(mMenuView);
		// 设置弹出窗体的宽
		this.setWidth(LayoutParams.FILL_PARENT);
		// 设置弹出窗体的高
		this.setHeight(LayoutParams.WRAP_CONTENT);
		// 设置弹出窗体可点击
		this.setFocusable(true);
		// 设置弹出动画效果
		this.setAnimationStyle(R.style.popup_animation);

		// 实例化一个ColorDrawable颜色为半透明
		ColorDrawable dw = new ColorDrawable(0xb0000000);
		// 设置弹出窗体的背景
		this.setBackgroundDrawable(dw);

	}

}