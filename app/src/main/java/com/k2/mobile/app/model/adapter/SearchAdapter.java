package com.k2.mobile.app.model.adapter;

import java.util.List;

import com.k2.mobile.app.R;
import com.k2.mobile.app.model.bean.SearchBean;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class SearchAdapter extends BaseAdapter{
	
	private Context context;
	private List<SearchBean> list;

	
	public SearchAdapter(Context _context, List<SearchBean> _list) {
		super();
		this.list = _list;
		context = _context;
	}

	@Override
	public int getCount() {
		return list != null ? list.size() : 0;
	}

	@Override
	public Object getItem(int arg0) {
		return list.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		return arg0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup arg2) {
		View v = convertView;
		ViewHolder holder = null;
		if(v == null){
			v = LayoutInflater.from(context).inflate(R.layout.item_search, null);
			holder = new ViewHolder();
			holder.tv_user_name = (TextView) v.findViewById(R.id.tv_user_name);
//			holder.tv_user_account = (TextView) v.findViewById(R.id.tv_user_account);
			holder.tv_department = (TextView) v.findViewById(R.id.tv_department);
			v.setTag(holder);
		}else{
			holder = (ViewHolder) v.getTag();
		}
		
		final SearchBean bean = list.get(position);
		int flag = 1;
		String userEnName = "";
		if(null==bean.getUserEnName()||"".equals(bean.getUserEnName().trim())){
			flag =  0;
		}else {
			userEnName = bean.getUserEnName();
		}
		holder.tv_user_name.setText(bean.getUserChsName()+(flag == 1?"("+userEnName+")":""));
//		holder.tv_user_account.setText(bean.getUserAccount());
		holder.tv_department.setText(bean.getRealityOrgName());
		
		return v;
	}
	
	class ViewHolder{
		TextView tv_user_name;
//		TextView tv_user_account;
		TextView tv_department;
	}


}