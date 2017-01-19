package com.k2.mobile.app.view.widget;

import android.app.Activity;
import android.content.Context;
import android.graphics.Rect;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;

/**
* @Title FloatingView.java
* @Package com.oppo.mo.view.widget;
* @Description 浮动透明工号
* @Company  K2
* 
* @author liangzy
* @date 2015-04-13 下午2:22:03
* @version V1.0
*/

public class FloatView extends Activity {

	private float x = 0;
	private float y = 0;
	private WindowManager wm = null;
	
	public WindowManager.LayoutParams params = new WindowManager.LayoutParams();
	
	public int TOOL_BAR_HIGH = 0;
	// 要显示在窗口最前面的对象
	private View view_obj = null;

	public FloatView(float x, float y){
		this.x  = x;
		this.y = y;
	}
	
	/**
	 * 要显示在窗口最前面的方法
	 * 
	 * @param context  调用对象Context getApplicationContext()
	 * @param window  调用对象 Window getWindow()
	 * @param floatingViewObj  要显示的浮动对象 View
	 */
	public void show(Context context, Window window, View floatingViewObj) {
		// 关闭浮动显示对象然后再显示
		close(context);

		Rect frame = new Rect();
		// 这一句是关键，让其在top 层显示
		// getWindow()
		window.getDecorView().getWindowVisibleDisplayFrame(frame);
		TOOL_BAR_HIGH = frame.top;
//		wm = (WindowManager) context.getSystemService(Activity.WINDOW_SERVICE);
		wm = (WindowManager) context.getApplicationContext().getSystemService(Context.WINDOW_SERVICE);
		params.type = WindowManager.LayoutParams.TYPE_SYSTEM_ALERT | WindowManager.LayoutParams.TYPE_SYSTEM_OVERLAY;
		params.flags = LayoutParams.FLAG_NOT_TOUCH_MODAL | LayoutParams.FLAG_NOT_FOCUSABLE;

		params.width = android.view.ViewGroup.LayoutParams.WRAP_CONTENT;
		params.height = android.view.ViewGroup.LayoutParams.WRAP_CONTENT;
		// 设定透明度
		params.alpha = 255;
		// 设定内部文字对齐方式
		params.gravity = Gravity.LEFT | Gravity.TOP;
		// 以屏幕左上角为原点，设置x、y初始值
		params.x = (int) x;
		params.y = (int) y;
		wm.addView(floatingViewObj, params);
	}

	/**
	 * 关闭浮动显示对象
	 */
	public void close(Context context) {
		if (view_obj != null && view_obj.isShown()) {
			WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
			wm.removeView(view_obj);
		}
	}

}
