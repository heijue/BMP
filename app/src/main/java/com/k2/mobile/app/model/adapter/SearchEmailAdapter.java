package com.k2.mobile.app.model.adapter;

import java.util.List;

import com.k2.mobile.app.R;
import com.k2.mobile.app.model.bean.EmailBean;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class SearchEmailAdapter extends BaseAdapter{
	
	private Context context;
	private List<EmailBean> list;

	
	public SearchEmailAdapter(Context _context, List<EmailBean> _list) {
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
		
		final EmailBean bean = list.get(position);
		
		int flag = 1;
		String userEnName = bean.getEnName();
		if(null == userEnName || "".equals(userEnName.trim())){
			flag = 0;
		}
		
		holder.tv_user_name.setText(bean.getDisplayName()+(flag==1?"("+userEnName+")":""));
		if(null != bean.getType() && "4".equals(bean.getType().trim())){
			holder.tv_department.setVisibility(View.VISIBLE);
//			holder.tv_user_account.setText(bean.getCode());
			holder.tv_department.setText(bean.getDisplayOrgName());
		}
		
		return v;
	}
	
	class ViewHolder{
		TextView tv_user_name;
//		TextView tv_user_account;
		TextView tv_department;
	}


}