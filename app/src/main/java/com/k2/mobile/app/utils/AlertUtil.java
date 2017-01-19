/*
 * Copyright (c) 2015. OPPO Co., Ltd.
 */
package com.k2.mobile.app.utils;

import java.lang.reflect.Field;

import com.k2.mobile.app.R;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

/**
 * @Title AlertUtil.java
 * @Package com.oppo.mo.utils
 * @Description 模态对话框工具类
 * @Company  K2
 * 
 * @author Jason.Wu
 * @date 2015-01-27 15:13:00
 * @version V1.0
 */
public class AlertUtil {

	public interface OnAlertSelectId {
		void onClick(int whichButton);
	}

	private AlertUtil() {
	
	}

//	public static Dialog showDialog(Context c, final OnAlertSelectId alertDo) {
//		View view = LayoutInflater.from(c).inflate(
//				R.layout.photo_choose_dialog, null);
//		final Dialog dialog = new Dialog(c, R.style.transparentFrameWindowStyle);
//		dialog.setContentView(view, new LayoutParams(LayoutParams.FILL_PARENT,
//				LayoutParams.WRAP_CONTENT));
//		Window window = dialog.getWindow();
//		// 设置显示动画
//		window.setWindowAnimations(R.style.main_menu_animstyle);
//		WindowManager.LayoutParams wl = window.getAttributes();
//		wl.x = 0;
//		wl.y = ((Activity) c).getWindowManager().getDefaultDisplay()
//				.getHeight();
//		// 以下这两句是为了保证按钮可以水平满屏
//		wl.width = ViewGroup.LayoutParams.MATCH_PARENT;
//		wl.height = ViewGroup.LayoutParams.WRAP_CONTENT;
//
//		// 设置显示位置
//		dialog.onWindowAttributesChanged(wl);
//		// 设置点击外围解散
//		dialog.setCanceledOnTouchOutside(true);
//
//		Button btnPic = (Button) view.findViewById(R.id.btn_pics);
//		btnPic.setOnClickListener(new View.OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//				// TODO Auto-generated method stub
//				alertDo.onClick(0);
//				dialog.dismiss();
//			}
//		});
//		Button btnTakePhoto = (Button) view.findViewById(R.id.btn_takePhoto);
//		btnTakePhoto.setOnClickListener(new View.OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//				// TODO Auto-generated method stub
//				alertDo.onClick(1);
//				dialog.dismiss();
//			}
//		});
//		Button btnCancel = (Button) view.findViewById(R.id.btn_cancel);
//		btnCancel.setOnClickListener(new View.OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//				// TODO Auto-generated method stub
//				dialog.dismiss();
//			}
//		});
//
//		dialog.show();
//		return dialog;
//	}
//
	public static Dialog showCustomDialog(Context c, int content,
			int leftBtnTxt, int rightBtnTxt, int leftTxtColor,
			final OnAlertSelectId alertDo) {
		View view = LayoutInflater.from(c)
				.inflate(R.layout.dialog_custom, null);
		final Dialog dialog = new Dialog(c, R.style.mDialogStyle);
		dialog.setContentView(view, new LayoutParams(LayoutParams.FILL_PARENT,
				LayoutParams.WRAP_CONTENT));
		Window window = dialog.getWindow();
		// 设置显示动画
		// window.setWindowAnimations(R.style.main_menu_animstyle);
		WindowManager.LayoutParams wl = window.getAttributes();
		// 以下这两句是为了保证按钮可以水平满屏
		wl.width = ((Activity) c).getWindowManager().getDefaultDisplay()
				.getWidth() / 5 * 4;
		wl.height = ((Activity) c).getWindowManager().getDefaultDisplay()
				.getHeight() / 4;

		// 设置显示位置
		// dialog.onWindowAttributesChanged(wl);
		// 设置点击外围解散
		dialog.setCanceledOnTouchOutside(true);

		TextView tvContent = (TextView) view.findViewById(R.id.tv_content);
		tvContent.setText(c.getResources().getString(content));

		Button btnLeft = (Button) view.findViewById(R.id.btn_left);
		btnLeft.setText(c.getResources().getString(leftBtnTxt));
		btnLeft.setTextColor(c.getResources().getColor(leftTxtColor));
		btnLeft.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				alertDo.onClick(0);
				dialog.dismiss();
			}
		});

		Button btnRight = (Button) view.findViewById(R.id.btn_right);
		btnRight.setText(c.getResources().getString(rightBtnTxt));
		btnRight.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				alertDo.onClick(1);
				dialog.dismiss();
			}
		});

		dialog.show();
		return dialog;
	}
	


//	public static Dialog showURLSettingDialog(Context c, int content,
//			int leftBtnTxt, int rightBtnTxt, int leftTxtColor,
//			final OnAlertSelectId alertDo) {
//		View view = LayoutInflater.from(c)
//				.inflate(R.layout.dialog_custom_single_edittext, null);
//		final Dialog dialog = new Dialog(c, R.style.myDialogStyle);
//		dialog.setContentView(view, new LayoutParams(LayoutParams.FILL_PARENT,
//				LayoutParams.WRAP_CONTENT));
//		Window window = dialog.getWindow();
//		// 设置显示动画
//		// window.setWindowAnimations(R.style.main_menu_animstyle);
//		WindowManager.LayoutParams wl = window.getAttributes();
//		// 以下这两句是为了保证按钮可以水平满屏
//		wl.width = ((Activity) c).getWindowManager().getDefaultDisplay()
//				.getWidth() / 5 * 4;
//		wl.height = ((Activity) c).getWindowManager().getDefaultDisplay()
//				.getHeight() / 4;
//
//		// 设置显示位置
//		// dialog.onWindowAttributesChanged(wl);
//		// 设置点击外围解散
//		dialog.setCanceledOnTouchOutside(false);
//
//		TextView tvContent = (TextView) view.findViewById(R.id.textView1);
//		tvContent.setText(c.getResources().getString(content));
//		
//		final EditText editTextName = (EditText) view
//				.findViewById(R.id.editTextName);
//		editTextName.setHint(R.string.login_url_placeholder);
//		
//		Button btnLeft = (Button) view.findViewById(R.id.btn_left);
//		btnLeft.setText(c.getResources().getString(leftBtnTxt));
//		btnLeft.setTextColor(c.getResources().getColor(leftTxtColor));
//		btnLeft.setOnClickListener(new View.OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//				// TODO Auto-generated method stub
//				alertDo.onClick(0);
//				dialog.dismiss();
//			}
//		});
//
//		dialog.show();
//		return dialog;
//	}

//	/**
//	 * URL设置窗体
//	 * @param h
//	 * @param c
//	 * @param what
//	 */
//	public static void showURLSettingDialog(final Handler h, final Context c,
//			final int what) {
//
//		LayoutInflater factory = LayoutInflater.from(c);
//		final View textEntryView = factory.inflate(
//				R.layout.dialog_single_edittext, null);
//
//		final TextView textView = (TextView) textEntryView
//				.findViewById(R.id.textView1);
//
//		final EditText editTextName = (EditText) textEntryView
//				.findViewById(R.id.editTextName);
//		editTextName.setHint(R.string.login_url_placeholder);
//		AlertDialog.Builder ad1 = new AlertDialog.Builder(c);
//		ad1.setTitle(c.getResources().getString(R.string.login_url_title)+c.getResources().getString(R.string.login_not_redo_title));
//		ad1.setIcon(android.R.drawable.ic_dialog_info);
//		ad1.setView(textEntryView);
//		ad1.setCancelable(false);
//		ad1.setPositiveButton(c.getResources().getString(R.string.global_ok),
//				new DialogInterface.OnClickListener() {
//					public void onClick(DialogInterface dialog, int i) {
//						String folderName = editTextName.getText().toString()
//								.trim();
//						if (folderName != "" && folderName.length() > 0) {
//							if (CommonUtil.hasURLSpecialChars(folderName)) {
//								alterField(dialog, false);
//								textView.setVisibility(View.VISIBLE);
//								textView.setText(c
//										.getResources()
//										.getString(
//												R.string.global_input_format_illgal));
//							} else {
//								Message m = new Message();
//								m.what = what;
//								m.obj = editTextName.getText().toString();
//								h.sendMessage(m);
//								alterField(dialog, true);
//							}
//
//						} else {
//							alterField(dialog, false);
//							textView.setVisibility(View.VISIBLE);
//							textView.setText(c.getResources().getString(
//									R.string.global_url_empty));
//						}
//					}
//				});
//		/*ad1.setNegativeButton(c.getResources().getString(R.string.global_cancel),
//				new DialogInterface.OnClickListener() {
//					public void onClick(DialogInterface dialog, int i) {
//						alterField(dialog, true);
//						dialog.dismiss();
//					}
//				});*/
//		ad1.show();
//
//	}

	/**
	 * 屏蔽键盘输入
	 * @param dialog
	 * @param isField
	 */
	public static void alterField(DialogInterface dialog, boolean isField) {
		Field field;
		try {
			field = dialog.getClass().getSuperclass()
					.getDeclaredField("mShowing");
			field.setAccessible(true);
			field.set(dialog, isField);
		} catch (NoSuchFieldException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	/*
	public static Dialog showPickVideoDialog(Context c, final OnAlertSelectId alertDo) {
		View view = LayoutInflater.from(c).inflate(
				R.layout.video_choose_dialog, null);
		final Dialog dialog = new Dialog(c, R.style.transparentFrameWindowStyle);
		dialog.setContentView(view, new LayoutParams(LayoutParams.FILL_PARENT,
				LayoutParams.WRAP_CONTENT));
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

		Button btnVideo = (Button) view.findViewById(R.id.btn_video);
		btnVideo.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				alertDo.onClick(0);
				dialog.dismiss();
			}
		});
//		Button btnTakePhoto = (Button) view.findViewById(R.id.btn_takePhoto);
//		btnTakePhoto.setOnClickListener(new View.OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//				// TODO Auto-generated method stub
//				alertDo.onClick(1);
//				dialog.dismiss();
//			}
//		});
		Button btnCancel = (Button) view.findViewById(R.id.btn_cancel);
		btnCancel.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				dialog.dismiss();
			}
		});

		dialog.show();
		return dialog;
	}
	
	public static Dialog showPickAudioDialog(Context c, final OnAlertSelectId alertDo) {
		View view = LayoutInflater.from(c).inflate(
				R.layout.audio_choose_dialog, null);
		final Dialog dialog = new Dialog(c, R.style.transparentFrameWindowStyle);
		dialog.setContentView(view, new LayoutParams(LayoutParams.FILL_PARENT,
				LayoutParams.WRAP_CONTENT));
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

		Button btnAudio = (Button) view.findViewById(R.id.btn_audio);
		btnAudio.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				alertDo.onClick(0);
				dialog.dismiss();
			}
		});
		Button btnCancel = (Button) view.findViewById(R.id.btn_cancel);
		btnCancel.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				dialog.dismiss();
			}
		});

		dialog.show();
		return dialog;
	}
	*/
}
