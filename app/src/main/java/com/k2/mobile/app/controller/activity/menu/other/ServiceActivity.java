package com.k2.mobile.app.controller.activity.menu.other;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.k2.mobile.app.R;
import com.k2.mobile.app.controller.core.BaseActivity;
import com.k2.mobile.app.controller.core.BaseApp;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

/**
* @Title ServiceActivity.java
* @Package com.oppo.mo.controller.activity.menu;
* @Description 服务
* @Company  K2
* 
* @author lingzy
* @date 2015-3-31 下午9:00:57
* @version V1.0
*/
public class ServiceActivity extends BaseActivity implements OnItemClickListener, OnClickListener {
	
	// 头部标题与搜索
	private TextView tv_title, tv_sech;
	// 返回
	private Button btn_back;
	
	// 
	private GridView mGridView;
	private List<Map<String, Object>> list;
	private Map<String, Object> map;
	private SimpleAdapter mAdapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 去除头部
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_service);
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
		mGridView = (GridView) findViewById(R.id.gv_list);
		
		tv_title.setText(getResources().getString(R.string.service));
		tv_sech.setVisibility(View.GONE);
		
		
		list = new ArrayList<Map<String, Object>>();
		for(int i=0;i<10;i++) {
			map = new HashMap<String, Object>();
			map.put("itImage", R.drawable.mi_system);
			map.put("itText", "服务"+String.valueOf(i));
			list.add(map);
		}
		
//		mAdapter = new SimpleAdapter(this, list, R.layout.item_gridview, 
//				new String[] {"itImage", "itText"}, new int[] {R.id.btn_start_time, R.id.accept});
//		
//		mGridView.setAdapter(mAdapter);
		
	  }  
	    
	/**
	 * 方法名: initListener() 
	 * 功 能 : 初始化监听器
	 * 参 数 : void 
	 * 返回值: void
	 */
	private void initListener() {
		btn_back.setOnClickListener(this);
		mGridView.setOnItemClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.top_go_back:
			finish();
			overridePendingTransition(R.anim.push_left_in, R.anim.abc_fade_out);
			break;
		}
	}
	
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
				HashMap<String, Object> item = (HashMap<String, Object>) parent.getItemAtPosition(position);  
				//显示所选Item的ItemText  
				setTitle((String)item.get("ItemText"));  
	}


}
