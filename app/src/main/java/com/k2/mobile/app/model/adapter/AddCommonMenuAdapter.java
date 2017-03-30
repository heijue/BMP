package com.k2.mobile.app.model.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.k2.mobile.app.R;
import com.k2.mobile.app.model.bean.HomeMenuBean;

import java.util.HashMap;
import java.util.List;

/**
 * @Title MyGridViewAddAdapter.java
 * @Package com.oppo.mo.model.adapter
 * @Description 适配首页图标 添加
 * @Company  K2
 * 
 * @author linqijun
 * @date 2015-03-18 20:43:00
 * @version V1.0
 */
public class AddCommonMenuAdapter extends BaseAdapter {
	
	private Context mContext;
	private List<HomeMenuBean> hList;
	// 用来控制CheckBox的选中状况  
    private static HashMap<Integer, Boolean> isSelected;
    
	public AddCommonMenuAdapter(Context mContext, List<HomeMenuBean> hList) {
		this.mContext = mContext;
		this.hList = hList;
		isSelected = new HashMap<Integer, Boolean>();
		// 初始化数据  
	    initData();
	}
	
	// 初始化isSelected的数据  
	private void initData(){
		for (int i = 0; i < this.hList.size(); i++) {  
			getIsSelected().put(i, false);  
		}  
	}

	@Override
	public int getCount() {
		return null == hList ? 0 : hList.size();
	}

	@Override
	public Object getItem(int position) {
		return hList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
			convertView = LayoutInflater.from(mContext).inflate(R.layout.item_common_menu_add, null);
			LinearLayout ll_grid_item = (LinearLayout) convertView.findViewById(R.id.ll_grid_item);
			ImageView item_ico = (ImageView) convertView.findViewById(R.id.iv_item);
			TextView item_name = (TextView) convertView.findViewById(R.id.tv_item);
			CheckBox cb_select = (CheckBox) convertView.findViewById(R.id.cb_select);
			
			final HomeMenuBean hBean = hList.get(position);
			if(null != hBean.getIsBlank() && "false".equals(hBean.getIsBlank())){
				item_ico.setBackgroundResource(hBean.getMenuIcons());
			}else{
				if(null != hBean.getIcoURL() && !"".equals(hBean.getIcoURL())){
					item_ico.setBackgroundResource(R.drawable.ico_submit_repair_n);
				}else{
					item_ico.setBackgroundResource(R.drawable.ico_submit_repair_n);
				}
			}
			
			item_name.setText(hBean.getMenuNmae());
						
			cb_select.setChecked(getIsSelected().get(position));
			cb_select.setOnCheckedChangeListener(new OnCheckedChangeListener() {
				@Override
				public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
					getIsSelected().put(position, isChecked);
				}
			});
			
			ll_grid_item.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					boolean val = getIsSelected().get(position);
					getIsSelected().put(position, !val);
					notifyDataSetChanged();
				}
			});
			
		return convertView;
	}
	
	public static HashMap<Integer, Boolean> getIsSelected() {  
        return isSelected;  
    }  

    public static void setIsSelected(HashMap<Integer, Boolean> isSelected) {  
    	AddCommonMenuAdapter.isSelected = isSelected;  
    }
	
}
