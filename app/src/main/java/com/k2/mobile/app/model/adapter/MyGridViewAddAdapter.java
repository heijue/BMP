package com.k2.mobile.app.model.adapter;

import java.util.HashMap;
import java.util.List;

import com.k2.mobile.app.R;
import com.k2.mobile.app.model.bean.HomeMenuBean;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.CompoundButton.OnCheckedChangeListener;

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
public class MyGridViewAddAdapter extends BaseAdapter {
	
	private Context mContext;
	private List<HomeMenuBean> hList;
	// 用来控制CheckBox的选中状况  
    private static HashMap<Integer, Boolean> isSelected;
    // CheckBox显示与隐藏操作：1显示，0隐藏
    private int type;
    // 常用菜单是否存在的菜单
    private List<HomeMenuBean> tmpList = null;
    
	public MyGridViewAddAdapter(Context mContext, List<HomeMenuBean> hList, int type, List<HomeMenuBean> tmpList) {
		this.mContext = mContext;
		this.hList = hList;
		this.type = type;
		this.tmpList = tmpList;
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
			ViewHolder holder;
			if(null == convertView){
				holder = new ViewHolder();
				convertView = LayoutInflater.from(mContext).inflate(R.layout.item_home_grid_add, null);
				holder.item_ico = (ImageView) convertView.findViewById(R.id.iv_item);
				holder.item_name = (TextView) convertView.findViewById(R.id.tv_item);
				holder.cb_select = (CheckBox) convertView.findViewById(R.id.cb_select);
				convertView.setTag(holder);
			}else{
				holder = (ViewHolder)convertView.getTag();
			}
			
			final HomeMenuBean hBean = hList.get(position);
			
			if(null != hBean.getIsBlank() && "false".equals(hBean.getIsBlank())){
				holder.item_ico.setBackgroundResource(hBean.getMenuIcons());
			}else{
				if(null != hBean.getIcoURL() && !"".equals(hBean.getIcoURL())){
					holder.item_ico.setBackgroundResource(R.drawable.ico_submit_repair_n);
				}else{
					holder.item_ico.setBackgroundResource(R.drawable.ico_submit_repair_n);
				}
			}
			
			holder.item_name.setText(hBean.getMenuNmae());
			if(1 == type){
				// 判断常用菜单是否已添加,已添加过的则不允许再添加
				boolean val = true;
				if(null != tmpList){
					for(int i=0;i<tmpList.size();i++){
						if(null != hBean.getMenuCode() && null != tmpList.get(i).getMenuCode() && hBean.getMenuCode().equals(tmpList.get(i).getMenuCode())){
							val = false;
						}
					}
				}
				
				if(val){
					holder.cb_select.setVisibility(View.VISIBLE);	
					holder.item_ico.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View v) {
							boolean bl = getIsSelected().get(position);
							getIsSelected().put(position, !bl);
							notifyDataSetChanged();
						}
					});
				}
			}
			
			holder.cb_select.setChecked(getIsSelected().get(position));
			holder.cb_select.setOnCheckedChangeListener(new OnCheckedChangeListener() {
				@Override
				public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
					getIsSelected().put(position, isChecked);
				}
			});
			
		return convertView;
	}
	
	public static HashMap<Integer, Boolean> getIsSelected() {  
        return isSelected;  
    }  

    public static void setIsSelected(HashMap<Integer, Boolean> isSelected) {  
    	MyGridViewAddAdapter.isSelected = isSelected;  
    }
	
    protected class ViewHolder {
    	ImageView item_ico;
		TextView item_name;
		CheckBox cb_select;
	}
}
