package com.k2.mobile.app.controller.activity.menu.hr;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.webkit.ValueCallback;
import android.widget.Button;
import android.widget.TextView;

import com.k2.mobile.app.R;
import com.k2.mobile.app.common.config.HttpConstants;
import com.k2.mobile.app.controller.core.BaseActivity;
import com.k2.mobile.app.controller.core.BaseApp;

import org.xwalk.core.XWalkPreferences;
import org.xwalk.core.XWalkUIClient;
import org.xwalk.core.XWalkView;

/**
 * @author linqijun
 * @ClassName: StaticLinkListActivity
 * @Description: 静态页链接集合
 * @date 2015-7-16 19:40:46
 */

public class HRStaticLinkListActivity extends BaseActivity implements OnClickListener {

    private Button btn_back;
    private TextView tv_title;
    private TextView tv_sech;

    private String title;
    private String urls = null;
    private String popularCode = null;

    private ValueCallback<Uri> mUploadMessage;
    ValueCallback<Uri[]> mUploadCallbackAboveL;
    ProgressDialog mProgressDialog;
    private final static int FILECHOOSER_RESULTCODE = 1;
    private XWalkView mXWalkView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 去除头部
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_static_link);
        initDate();
        initView();
        initListener();
        BaseApp.addActivity(this);

    }

    private void initDate() {
        if (null != popularCode && !"".equals(popularCode.trim())) {
            urls = "http://" + HttpConstants.DOMAIN_NAME + ":" + HttpConstants.PROT + HttpConstants.POPULAR + "?" +
                    "popularCode=" + popularCode;
        } else {
            urls = getIntent().getStringExtra("url");
        }

        if (urls.contains("?")) {
            urls += "&o=1&u=" + BaseApp.token;
        } else {
            urls += "?o=1&u=" + BaseApp.token;
        }
    }

    //onActivityResult回调
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (mXWalkView != null) {
            mXWalkView.onActivityResult(requestCode, resultCode, data);
        }
        if (requestCode == FILECHOOSER_RESULTCODE) {
            if (null == mUploadMessage && null == mUploadCallbackAboveL)
                return;
            Uri result = data == null || resultCode != RESULT_OK ? null : data.getData();
            if (mUploadMessage != null) {
                mUploadMessage.onReceiveValue(result);
                mUploadMessage = null;
            }
        }
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
        tv_sech.setVisibility(View.GONE);
        mXWalkView = (XWalkView) findViewById(R.id.xWalkWebView);
        title = getIntent().getStringExtra("title");
        popularCode = getIntent().getStringExtra("popularCode");
        tv_title.setText(title);
        System.out.println(urls);
        mXWalkView.load(urls, null);
        // 开启调试
        XWalkPreferences.setValue(XWalkPreferences.REMOTE_DEBUGGING, true);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mXWalkView != null) {
            mXWalkView.pauseTimers();
            mXWalkView.onHide();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mXWalkView != null) {
            mXWalkView.resumeTimers();
            mXWalkView.onShow();
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        if (mXWalkView != null) {
            mXWalkView.onNewIntent(intent);
        }
    }

    /**
     * 方法名: initListener()
     * 功 能 : 初始化 监听器
     * 参 数 : void
     * 返回值: void
     */
    private void initListener() {
        btn_back.setOnClickListener(this);
        mXWalkView.setUIClient(new XWalkUIClient(mXWalkView) {
            public void onPageLoadStarted(XWalkView view, String url) {
                if (mProgressDialog == null) {
                    mProgressDialog =  mProgressDialog.show(HRStaticLinkListActivity.this,null,
                            HRStaticLinkListActivity.this.getString(R.string.progressdialog_loading) ,
                            false, true);
                }
            }

            public void onPageLoadStopped(XWalkView view, String url, XWalkUIClient.LoadStatus status) {
                mProgressDialog.dismiss();
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_back:
                finish();
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mProgressDialog != null) {
            mProgressDialog = null;
        }
        if (mXWalkView != null) {
            mXWalkView.onDestroy();
        }
    }
}