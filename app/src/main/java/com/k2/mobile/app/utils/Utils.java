package com.k2.mobile.app.utils;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

import com.k2.mobile.app.R;
import com.k2.mobile.app.model.bean.ItemsBean;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;

/**
 * Base Helper
 * 
 * @author ZYB2
 * 
 */
public class Utils {

	public interface OnAlertSelectId {
		void onClick(String whichName);
	}

	/**
	 * 拷贝
	 * 
	 * @param is
	 * @param os
	 */
	public static void CopyStream(InputStream is, OutputStream os) {
		final int buffer_size = 1024;
		try {
			byte[] bytes = new byte[buffer_size];
			for (;;) {
				int count = is.read(bytes, 0, buffer_size);
				if (count == -1)
					break;
				os.write(bytes, 0, count);
			}
		} catch (Exception ex) {
		}
	}

	public static Dialog showDialog(Context c, final OnAlertSelectId alertDo,
			List<ItemsBean> itemBeanL) {
		View view = LayoutInflater.from(c).inflate(R.layout.task_choose_dialog, null);
		LinearLayout taskOperatePanl = (LinearLayout) view
				.findViewById(R.id.task_operate_panl);
		final Dialog dialog = new Dialog(c, R.style.transparentFrameWindowStyle);
		dialog.setContentView(view);
		Window window = dialog.getWindow();
		// 设置显示动画
		window.setWindowAnimations(R.style.main_menu_animstyle);
		WindowManager.LayoutParams wl = window.getAttributes();
		wl.x = 0;
		wl.y = ((Activity) c).getWindowManager().getDefaultDisplay()
				.getHeight();
		// 以下这两句是为了保证按钮可以水平满屏
		wl.width = ViewGroup.LayoutParams.MATCH_PARENT;
		wl.height = ViewGroup.LayoutParams.WRAP_CONTENT;

		// 设置显示位置
		dialog.onWindowAttributesChanged(wl);
		// 设置点击外围解散
		dialog.setCanceledOnTouchOutside(true);
		LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
				LayoutParams.MATCH_PARENT,
				LayoutParams.MATCH_PARENT, 1); // , 1是可选写的
		for (int i = 0; i < itemBeanL.size(); i++) {
			ItemsBean item = itemBeanL.get(i);
			Button btn = new Button(c);
			if (itemBeanL.size() > 1) {
				if (i == 0) {
					btn.setBackgroundResource(R.drawable.task_up_selector);
				} else if (i == itemBeanL.size() - 1) {
					btn.setBackgroundResource(R.drawable.task_down_selector);
				} else {
					btn.setBackgroundResource(R.drawable.task_center_selector);
				}
			} else {
				btn.setBackgroundResource(R.drawable.task_cancel_selector);
			}
			btn.setId(i);
			btn.setText(item.getName());
			LinearLayout.LayoutParams lp2 = new LinearLayout.LayoutParams(
					LayoutParams.MATCH_PARENT,
					LayoutParams.MATCH_PARENT, 1); // , 1是可选写的
			lp2.setMargins(0, 1, 0, 0);
			btn.setLayoutParams(lp2);
			final String name = item.getName();
			btn.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					alertDo.onClick(name);
					dialog.dismiss();
				}
			});
			taskOperatePanl.addView(btn);
		}
		Button btnCancel = new Button(c);
		lp.setMargins(0, 5, 0, 0);
		btnCancel.setText("取消");
		btnCancel.setLayoutParams(lp);
		btnCancel.setBackgroundResource(R.drawable.task_cancel_selector);
		btnCancel.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}
		});
		taskOperatePanl.addView(btnCancel);
		dialog.show();
		return dialog;
	}

}
