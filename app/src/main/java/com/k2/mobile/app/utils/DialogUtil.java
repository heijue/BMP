package com.k2.mobile.app.utils;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.widget.Toast;

/**
 * @Title DialogUtil.java
 * @Package com.oppo.mo.utils
 * @Description Dialog帮助类
 * @Company  K2
 * 
 * @author linqijun
 * @date 2015-03-13 17:56:00
 * @version V1.0
 */
public class DialogUtil {

	public static AlertDialog mAlertDialog;

	public static AlertDialog showAlertDialog(Context mContext, String TitleID, String Message) {
		mAlertDialog = new AlertDialog.Builder(mContext).setTitle(TitleID).setMessage(Message).show();
		return mAlertDialog;
	}

	public static AlertDialog showAlertDialog(Context mContext, Resources res, int pTitelResID, String pMessage,
			DialogInterface.OnClickListener pOkClickListener) {
		String title = res.getString(pTitelResID);
		return showAlertDialog(mContext, title, pMessage, pOkClickListener, null, null);
	}

	public static AlertDialog showAlertDialog(Context mContext, String pTitle, String pMessage, 
			DialogInterface.OnClickListener pOkClickListener,
			DialogInterface.OnClickListener pCancelClickListener,
			DialogInterface.OnDismissListener pDismissListener) {
		mAlertDialog = new AlertDialog.Builder(mContext)
				.setTitle(pTitle)
				.setMessage(pMessage)
				.setPositiveButton(android.R.string.ok, pOkClickListener)
				.setNegativeButton(android.R.string.cancel, pCancelClickListener).show();
		if (pDismissListener != null) {
			mAlertDialog.setOnDismissListener(pDismissListener);
		}
		return mAlertDialog;
	}

	public static AlertDialog showAlertDialog(Context mContext, String pTitle,
			String pMessage, String pPositiveButtonLabel,
			String pNegativeButtonLabel,
			DialogInterface.OnClickListener pOkClickListener,
			DialogInterface.OnClickListener pCancelClickListener,
			DialogInterface.OnDismissListener pDismissListener) {
		mAlertDialog = new AlertDialog.Builder(mContext).setTitle(pTitle)
				.setMessage(pMessage)
				.setPositiveButton(pPositiveButtonLabel, pOkClickListener)
				.setNegativeButton(pNegativeButtonLabel, pCancelClickListener)
				.show();
		if (pDismissListener != null) {
			mAlertDialog.setOnDismissListener(pDismissListener);
		}
		return mAlertDialog;
	}

	public static ProgressDialog showProgressDialog(Context mContext,
			Resources res, int pTitelResID, String pMessage,
			DialogInterface.OnCancelListener pCancelClickListener) {
		String title = res.getString(pTitelResID);
		return showProgressDialog(mContext, title, pMessage,
				pCancelClickListener);
	}

	public static ProgressDialog showProgressDialog(Context mContext,
			String pTitle, String pMessage,
			DialogInterface.OnCancelListener pCancelClickListener) {
		mAlertDialog = ProgressDialog.show(mContext, pTitle, pMessage, true, true);
		mAlertDialog.setOnCancelListener(pCancelClickListener);
		return (ProgressDialog) mAlertDialog;
	}

	public static ProgressDialog showProgressDialogNotCancel(Context mContext,
			Resources res, int pTitelResID, String pMessage,
			DialogInterface.OnCancelListener pCancelClickListener) {
		String title = res.getString(pTitelResID);
		return showNotCancelProgressDialog(mContext, title, pMessage,
				pCancelClickListener);
	}

	public static ProgressDialog showNotCancelProgressDialog(Context mContext,
			String pTitle, String pMessage,
			DialogInterface.OnCancelListener pCancelClickListener) {
		mAlertDialog = ProgressDialog.show(mContext, pTitle, pMessage, true, true);
		mAlertDialog.setOnCancelListener(pCancelClickListener);
		mAlertDialog.setCancelable(false);
		mAlertDialog.setCanceledOnTouchOutside(false);
		return (ProgressDialog) mAlertDialog;
	}

	public static ProgressDialog showWithCancelProgressDialog(Context mContext, String pTitle, String pMessage,
			DialogInterface.OnCancelListener pCancelClickListener) {
		if(null != mAlertDialog){
			closeDialog();
		}
		mAlertDialog = ProgressDialog.show(mContext, pTitle, pMessage, true, true);
		mAlertDialog.setOnCancelListener(pCancelClickListener);
		mAlertDialog.setCancelable(true);
		mAlertDialog.setCanceledOnTouchOutside(false);
		return (ProgressDialog) mAlertDialog;
	}

	public static void closeDialog() {
		if (mAlertDialog != null) {
			mAlertDialog.dismiss();
			mAlertDialog = null;
		}
	}
	
	public static void showLongToast(Context context, int pResId) {
		Toast.makeText(context, context.getString(pResId), Toast.LENGTH_SHORT).show();
	}

	public static void showLongToast(Context context, String str) {
		Toast.makeText(context, str, Toast.LENGTH_SHORT).show();
	}
}
