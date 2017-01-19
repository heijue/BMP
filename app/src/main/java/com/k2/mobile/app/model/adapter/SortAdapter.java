package com.k2.mobile.app.model.adapter;

import java.util.List;
import java.util.Locale;

import android.content.Context;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.SectionIndexer;
import android.widget.TextView;

import com.k2.mobile.app.R;
import com.k2.mobile.app.model.bean.Contacts;

public class SortAdapter extends BaseAdapter implements SectionIndexer{
	
	private List<Contacts> list = null;
	private Context mContext;
	
	public SortAdapter(Context mContext, List<Contacts> list) {
		this.mContext = mContext;
		this.list = list;
	}
	
	/**
	 * 当ListView数据发生变化时,调用此方法来更新ListView
	 * @param list
	 */
	public void updateListView(List<Contacts> list){
		this.list = list;
		notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		return this.list.size();
	}

	@Override
	public Object getItem(int position) {
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View view, ViewGroup arg2) {
		ViewHolder viewHolder = null;
		final Contacts mContent = list.get(position);
		if (view == null) {
			viewHolder = new ViewHolder();
			view = LayoutInflater.from(mContext).inflate(R.layout.item_addr_book, null);
			
			viewHolder.tvLetter = (TextView) view.findViewById(R.id.tv_catalog);
			viewHolder.tv_user_name = (TextView) view.findViewById(R.id.tv_user_name);
			viewHolder.tv_user_enname = (TextView) view.findViewById(R.id.tv_user_enname);
			viewHolder.tv_org_name = (TextView) view.findViewById(R.id.tv_org_name);
			
			view.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) view.getTag();
		}
		
		//根据position获取分类的首字母的Char ascii值
		int section = getSectionForPosition(position);
		
		//如果当前位置等于该分类首字母的Char的位置 ，则认为是第一次出现
		if(position == getPositionForSection(section)){
			viewHolder.tvLetter.setVisibility(View.VISIBLE);
			viewHolder.tvLetter.setText(mContent.getAlphabetical().substring(0, 1).toUpperCase(Locale.getDefault()));
		}else{
			viewHolder.tvLetter.setVisibility(View.GONE);
		}
		viewHolder.tv_user_name.setText(this.list.get(position).getUserChsName());
		viewHolder.tv_user_enname.setText(this.list.get(position).getUserEnName());
		viewHolder.tv_org_name.setText(this.list.get(position).getRealityOrgName());
		return view;
	}
	
	public static int getScreenWidth(Context context) { 
	    WindowManager manager = (WindowManager) context 
	            .getSystemService(Context.WINDOW_SERVICE); 
	    Display display = manager.getDefaultDisplay(); 
	    return display.getWidth(); 
	}

	final static class ViewHolder {
		TextView tvLetter;
		TextView tv_user_name;
		TextView tv_user_enname;
		TextView tv_org_name;
	}

	/**
	 * 根据ListView的当前位置获取分类的首字母的Char ascii值
	 */
	@Override
	public int getSectionForPosition(int position) {
		return list.get(position).getAlphabetical().charAt(0);
	}

	/**
	 * 根据分类的首字母的Char ascii值获取其第一次出现该首字母的位置
	 */
	@Override
	public int getPositionForSection(int section) {
		for (int i = 0; i < getCount(); i++) {
			String sortStr = list.get(i).getAlphabetical();
			char firstChar = sortStr.toUpperCase().charAt(0);
			if (firstChar == section) {
				return i;
			}
		}
		
		return -1;
	}
	
	/**
	 * 提取英文的首字母，非英文字母用#代替。
	 * 
	 * @param str
	 * @return
	 */
	private String getAlpha(String str) {
		String  sortStr = str.trim().substring(0, 1).toUpperCase();
		// 正则表达式，判断首字母是否是英文字母
		if (sortStr.matches("[A-Z]")) {
			return sortStr;
		} else {
			return "#";
		}
	}

	@Override
	public Object[] getSections() {
		return null;
	}
}