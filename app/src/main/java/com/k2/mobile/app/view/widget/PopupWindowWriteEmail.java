package com.k2.mobile.app.view.widget;

import com.k2.mobile.app.R;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.PopupWindow;

/**
 * @Title PopwinWriteEmail.java
 * @Package com.oppo.mo.view.widget;
 * @Description 用于显示写邮件底部弹出的窗口
 * @Company  K2
 * 
 * @author liangzy
 * @date 2015-03-30 11:19:00
 * @version V1.0
 */
public class PopupWindowWriteEmail extends PopupWindow {

	private View mMenuView;
	// 不保存草稿， 保存草稿， 取消
	private Button btn_not_save_drafts, btn_save_drafts, btn_cancel;

	public PopupWindowWriteEmail(Activity context, OnClickListener itemsOnClick) {
		super(context);
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mMenuView = inflater.inflate(R.layout.popup_write_email, null);

		btn_not_save_drafts = (Button) mMenuView.findViewById(R.id.btn_not_save_drafts);
		btn_save_drafts = (Button) mMenuView.findViewById(R.id.btn_save_drafts);
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
		btn_not_save_drafts.setOnClickListener(itemsOnClick);
		btn_save_drafts.setOnClickListener(itemsOnClick);
		// 设置显示的View
		this.setContentView(mMenuView);
		// 设置弹出窗体的宽
		this.setWidth(android.view.ViewGroup.LayoutParams.FILL_PARENT);
		// 设置弹出窗体的高
		this.setHeight(android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
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
