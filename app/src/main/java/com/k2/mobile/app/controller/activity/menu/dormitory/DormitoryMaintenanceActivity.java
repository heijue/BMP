package com.k2.mobile.app.controller.activity.menu.dormitory;

import com.k2.mobile.app.R;
import com.k2.mobile.app.controller.core.BaseActivity;
import com.k2.mobile.app.controller.core.BaseApp;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

/**
 * 
 * @ClassName: DormitoryMaintenanceActivity
 * @Description: 宿舍维修
 * @author linqijun
 * @date 2015-7-16 19:40:46
 *
 */
public class DormitoryMaintenanceActivity extends BaseActivity implements OnClickListener {
	
	private Button btn_back, btn_commit;
	private TextView tv_title;
	private TextView tv_sech;
	private WebView wv_show = null;
	
	private EditText ed_url;
	
	private int type = 1;
	
	private String url = "http://172.16.105.199:8080/mo/huiLife.do?module=";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 去除头部
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_dormitory_manager);
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
		btn_back = (Button) findViewById(R.id.btn_back);
		tv_title = (TextView) findViewById(R.id.tv_title);
		tv_sech = (TextView) findViewById(R.id.tv_sech);
		wv_show = (WebView) findViewById(R.id.wv_show);
		btn_commit = (Button) findViewById(R.id.btn_commit);
		ed_url = (EditText) findViewById(R.id.ed_url);
		
		tv_sech.setVisibility(View.GONE);
		tv_title.setText(getString(R.string.dormitory_maintenance));
		
		ed_url.setText(url);
		
		wv_show.setInitialScale(150);
		// 不支持缓存
		wv_show.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
//		wv_show.setWebChromeClient(new WebChromeClient());
		wv_show.setWebViewClient(new WebViewClient(){
			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
			        wv_show.loadUrl(url);
			        return true;
			}
			@Override
			public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
			        super.onReceivedError(view, errorCode, description, failingUrl);
			         //这里进行无网络或错误处理，具体可以根据errorCode的值进行判断，做跟详细的处理。
			} 
						      
			@Override
			public void onPageFinished(WebView view, String url) {
			       super.onPageFinished(view, url);
			}
		});	
		
		wv_show.setOnKeyListener(new View.OnKeyListener() {    
            @Override    
            public boolean onKey(View v, int keyCode, KeyEvent event) {    
                if (event.getAction() == KeyEvent.ACTION_DOWN) {    
                    if (keyCode == KeyEvent.KEYCODE_BACK && wv_show.canGoBack()) {  //表示按返回键时的操作  
                    	wv_show.goBack();   //后退    
                        //webview.goForward();//前进  
                        return true;    //已处理    
                    }    
                }    
                return false;    
            }    
        }); 
		
		// 启用支持javascript
		WebSettings settings = wv_show.getSettings();
		settings.setJavaScriptEnabled(true);
		settings.setJavaScriptCanOpenWindowsAutomatically(true);
		settings.setBuiltInZoomControls(true); // 支持页面放大缩小
		settings.setDisplayZoomControls(false);	// 隐藏放大缩小的按钮
		settings.setSupportZoom(true);
		settings.setUseWideViewPort(true);
	}
	
	/**
	 * 方法名: initListener() 
	 * 功 能 : 初始化 监听器 
	 * 参 数 : void 
	 * 返回值: void
	 */
	private void initListener() {
		btn_back.setOnClickListener(this);
		btn_commit.setOnClickListener(this);
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.btn_back:
	            finish();
				break;
			case R.id.btn_commit:
				String urls = ed_url.getText().toString();
				wv_show.loadUrl(urls);
				break;
		}		
	}
}