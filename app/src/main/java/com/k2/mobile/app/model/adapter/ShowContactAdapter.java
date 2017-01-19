package com.k2.mobile.app.model.adapter;

import java.util.ArrayList;
import java.util.List;

import com.k2.mobile.app.R;
import com.k2.mobile.app.model.bean.Contacts;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

public class ShowContactAdapter extends BaseAdapter implements Filterable{
	
	private ArrayFilter mFilter;  
	private ArrayList<Contacts> mUnfilteredData;
	
	private List<Contacts> list = null;
	private Context mContext;
	
	public ShowContactAdapter(Context mContext, List<Contacts> list) {
		this.mContext = mContext;
		this.list = list;
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
			view = LayoutInflater.from(mContext).inflate(R.layout.item_write_contact, null);
			
			viewHolder.tv_user_name = (TextView) view.findViewById(R.id.tv_user_name);
//			viewHolder.tv_user_account = (TextView) view.findViewById(R.id.tv_user_account);
			viewHolder.tv_department = (TextView) view.findViewById(R.id.tv_department);
			
			view.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) view.getTag();
		}
		int flag = 1;
		String userEnName = this.list.get(position).getUserEnName();
		if(null == userEnName || "".equals(userEnName.trim())){
			flag = 0;
		}
		
		viewHolder.tv_user_name.setText(this.list.get(position).getUserChsName()+(flag==1?"("+userEnName+")":""));
//		viewHolder.tv_user_account.setText(this.list.get(position).getUserCode());
		viewHolder.tv_department.setText(this.list.get(position).getRealityOrgName());
		if(null == this.list.get(position).getRealityOrgName() || "".equals(this.list.get(position).getRealityOrgName().trim())){
			viewHolder.tv_department.setVisibility(View.GONE);
		}
		return view;
	}

	final static class ViewHolder {
		TextView tv_user_name;
//		TextView tv_user_account;
		TextView tv_department;
	}

	  @Override  
	    public Filter getFilter() {  
	        if (mFilter == null) {  
	            mFilter = new ArrayFilter();  
	        }  
	        return mFilter;  
	    }  
	  
	    private class ArrayFilter extends Filter {  
	  
	        @Override  
	        protected FilterResults performFiltering(CharSequence prefix) {  
	            FilterResults results = new FilterResults();  
	  
	            if (mUnfilteredData == null) {  
	                mUnfilteredData = new ArrayList<Contacts>(list);  
	            }  
	  
	            if (prefix == null || prefix.length() == 0) {  
	                ArrayList<Contacts> list = mUnfilteredData;  
	                results.values = list;  
	                results.count = list.size();  
	            } else {  
//	                String prefixString = prefix.toString().toLowerCase();  
	                String prefixString = prefix.toString();  
	  
	                ArrayList<Contacts> unfilteredValues = mUnfilteredData;  
	                int count = unfilteredValues.size();  
	  
	                ArrayList<Contacts> newValues = new ArrayList<Contacts>(count);  
	  
	                for (int i = 0; i < count; i++) {  
	                    Contacts pc = unfilteredValues.get(i);  
	                    if (pc != null) {  
	                        if(pc.getUserChsName()!= null && pc.getUserChsName().startsWith(prefixString)){  
	                            newValues.add(pc);  
	                        }else if(pc.getUserCode() != null && pc.getUserCode().startsWith(prefixString)){  
	                            newValues.add(pc);  
	                        }else if(pc.getOrgName() != null && pc.getOrgName().startsWith(prefixString)){  
	                            newValues.add(pc);  
	                        }else if(pc.getUserEnName() != null && pc.getUserEnName().startsWith(prefixString)){  
	                            newValues.add(pc);  
	                        } 
	                    }  
	                }  
	  
	                results.values = newValues;  
	                results.count = newValues.size();  
	            }  
	  
	            return results;  
	        }  
	  
	        @Override  
	        protected void publishResults(CharSequence constraint, FilterResults results) {  
	             //noinspection unchecked  
	            list = (List<Contacts>) results.values;  
	            if (results.count > 0) {  
	                notifyDataSetChanged();  
	            } else {  
	                notifyDataSetInvalidated();  
	            }  
	        }  
	    }  
}