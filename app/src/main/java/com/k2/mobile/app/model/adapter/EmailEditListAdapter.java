package com.k2.mobile.app.model.adapter;    

import java.text.ParseException;
import java.util.HashMap;
import java.util.List;

import com.k2.mobile.app.R;
import com.k2.mobile.app.model.db.table.Email;
import com.k2.mobile.app.utils.DateUtil;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TextView;
  
/**
 * @Title EmailListInfoAdapter.java
 * @Package com.oppo.mo.model.adapter
 * @Description 邮件列表
 * @Company  K2
 * 
 * @author linqijun
 * @date 2015-04-16 10:31:00
 * @version V1.0
 */
public class EmailEditListAdapter extends BaseAdapter {

	private Context context;
	private List<Email> list;
	// 用来控制CheckBox的选中状况  
    private static HashMap<Integer, Boolean> isSelected; 
	
	public EmailEditListAdapter(Context context, List<Email> list) {
		super();
		this.context = context;
		this.list = list;
		isSelected = new HashMap<Integer, Boolean>();  
	    // 初始化数据  
	    initData();  
	}
	// 初始化isSelected的数据  
	private void initData(){
		for (int i = 0; i < this.list.size(); i++) {  
            getIsSelected().put(i, false);  
        }  
	}
	@Override
	public int getCount() {
		return list == null ? 0 : list.size();
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
	public View getView(final int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder = null;
		if (convertView == null){
			viewHolder = new ViewHolder();
			
			convertView = LayoutInflater.from(context).inflate(R.layout.item_email_list_edit, null);
			viewHolder.ck_choose = (CheckBox) convertView.findViewById(R.id.ck_choose);
			viewHolder.tv_send_name = (TextView) convertView.findViewById(R.id.tv_send_name);
			viewHolder.receipt_time = (TextView) convertView.findViewById(R.id.receipt_time);
			viewHolder.tv_mail_subject = (TextView) convertView.findViewById(R.id.tv_mail_subject);
			
			convertView.setTag(viewHolder);
		}else{
			viewHolder = (ViewHolder) convertView.getTag();
		}
		
		final Email bean = list.get(position);
		
		viewHolder.tv_send_name.setText(bean.getSenderName());
		if (null != bean.getReceiptTime() && !"".equals(bean.getReceiptTime().trim())) {
			String time = null;
			try {
				time = DateUtil.datetimeFormat(bean.getReceiptTime().replaceAll("T", " "));
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			viewHolder.receipt_time.setText(time);
		}else{
			viewHolder.receipt_time.setText("");
		}
				
		viewHolder.tv_mail_subject.setText(bean.getMailSubject());
		viewHolder.ck_choose.setChecked(getIsSelected().get(position));
		viewHolder.ck_choose.setOnCheckedChangeListener(new OnCheckedChangeListener(){
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
        EmailEditListAdapter.isSelected = isSelected;  
    }  
	
    public static class ViewHolder {
		public CheckBox ck_choose;
		public TextView tv_send_name;
		public TextView receipt_time;
		public TextView tv_mail_subject;
	}
}
 