package com.k2.mobile.app.controller.activity.menu.benefitLife;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.TextView;

import com.k2.mobile.app.R;
import com.k2.mobile.app.common.config.HttpConstants;
import com.k2.mobile.app.controller.core.BaseActivity;
import com.k2.mobile.app.controller.core.BaseApp;

/**
 * 
 * @ClassName: StaticLinkListActivity
 * @Description: 静态页链接集合
 * @author linqijun
 * @date 2015-7-16 19:40:46
 *
 */
public class StaticLinkListActivity extends BaseActivity implements OnClickListener {
	
	private Button btn_back;
	private TextView tv_title;
	private TextView tv_sech;
	private WebView wv_show = null;
	
	private int type = 1;  // 静态网页类型
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 去除头部
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_static_link);
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
	@SuppressLint("NewApi")
	private void initView() {
		btn_back = (Button) findViewById(R.id.btn_back);
		tv_title = (TextView) findViewById(R.id.tv_title);
		tv_sech = (TextView) findViewById(R.id.tv_sech);
		wv_show = (WebView) findViewById(R.id.wv_show);
		
		tv_sech.setVisibility(View.GONE);
		type = getIntent().getIntExtra("type", 0);
		String urls = HttpConstants.STATIC_LINK_URL + type;
		switch (type) {
			case 1:
				tv_title.setText(getString(R.string.bus_timetable));
				break;
			case 2:
				tv_title.setText(getString(R.string.granducato_hotel));
				break;
			case 3:
				tv_title.setText(getString(R.string.sports));
				break;
			case 4:
				tv_title.setText(getString(R.string.beer_and_skittles));
				break;
			case 5:
				tv_title.setText(getString(R.string.network_correlation));
				break;
			case 6:
				tv_title.setText(getString(R.string.safety_management));
				break;
			case 7:
				tv_title.setText(getString(R.string.certificate_manage));
				break;
			case 8:
				tv_title.setText(getString(R.string.forget_password));
				urls = "http://"+ HttpConstants.DOMAIN_NAME + ":" + HttpConstants.PROT + "/MbileService/ForgetPasswordAction";
				break;
		}
		
		// 启用支持javascript
		WebSettings settings = wv_show.getSettings();
		settings.setJavaScriptEnabled(true);
		settings.setJavaScriptCanOpenWindowsAutomatically(true);
		settings.setBuiltInZoomControls(true);	// 支持页面放大缩小
		settings.setDisplayZoomControls(false);	// 隐藏放大缩小的按钮
		settings.setSupportZoom(true);			// 可以缩放
		settings.setUseWideViewPort(true);		// 为图片添加放大缩小功能
		
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
                    	wv_show.goBack();   	// 后退    
                        // webview.goForward();	// 前进  
                        return true;    		// 已处理    
                    }    
                }    
                return false;    
            }    
        }); 
		
		wv_show.loadUrl(urls);

	}
	
	/**
	 * 方法名: initListener() 
	 * 功 能 : 初始化 监听器 
	 * 参 数 : void 
	 * 返回值: void
	 */
	private void initListener() {
		btn_back.setOnClickListener(this);
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.btn_back:
	            finish();
				break;
		}		
	}
}