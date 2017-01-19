package com.k2.mobile.app.controller.activity.menu.other;

import com.k2.mobile.app.R;
import com.k2.mobile.app.controller.activity.menu.feedback.FeedbackActivity;
import com.k2.mobile.app.controller.core.BaseActivity;
import com.k2.mobile.app.controller.core.BaseApp;

import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

/**
* @Title HelpActivity.java
* @Package com.oppo.mo.controller.activity.menu;
* @Description 帮助
* @Company  K2
* 
* @author lingzy
* @date 015-3-12 下午9:00:57
* @version V1.0
*/
public class HelpActivity extends BaseActivity implements OnClickListener {

	// 头部标题与搜索
	private TextView tv_title, tv_sech;
	// 返回
	private Button btn_back;
	// 意见反馈
	private ImageView iv_feedback;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 去除头部
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_menu_help);
		initView();
		initListener();
		BaseApp.addActivity(this);
	}

	/**
	 * 方法名: initView() 
	 * 功 能 : 初始化 
	 * 参 数 : void 
	 * 返回值: void
	 */
	private void initView() {
		tv_title = (TextView) findViewById(R.id.tv_top_title);
		tv_sech = (TextView) findViewById(R.id.tv_sech);
		btn_back = (Button) findViewById(R.id.top_go_back);
		iv_feedback = (ImageView) findViewById(R.id.iv_feedback);
		
		tv_title.setText(getString(R.string.help));
		tv_sech.setVisibility(View.GONE);
	}

	/**
	 * 方法名: initListener() 
	 * 功 能 : 初始化监听器
	 * 参 数 : void 
	 * 返回值: void
	 */
	private void initListener() {
		btn_back.setOnClickListener(this);
		iv_feedback.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.top_go_back:
			finish();
			overridePendingTransition(R.anim.push_left_in, R.anim.abc_fade_out);
			break;
		case R.id.iv_feedback:
			startActivity(FeedbackActivity.class);
			overridePendingTransition(R.anim.push_left_in, R.anim.abc_fade_out);
			break;
		}
	}

}
