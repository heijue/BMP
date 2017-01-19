package com.k2.mobile.app.model.adapter;    

import java.text.ParseException;
import java.util.HashMap;
import java.util.List;

import com.k2.mobile.app.R;
import com.k2.mobile.app.common.config.BroadcastNotice;
import com.k2.mobile.app.model.db.table.Email;
import com.k2.mobile.app.utils.DateUtil;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
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
@SuppressLint("ResourceAsColor")
public class EmailListInfoAdapter extends BaseAdapter {

	private Context context;
	private List<Email> list;
    int type = 0;
 // 用来控制CheckBox的选中状况  
    private static HashMap<Integer, Boolean> isSelected;
    
	public EmailListInfoAdapter(Context context, List<Email> list) {
		super();
		this.context = context;
		this.list = list;
		isSelected = new HashMap<Integer, Boolean>();   
	    // 初始化数据  
	    initData();  
	}
	
	// 初始化isSelected的数据  
	private void initData(){
		if(null != list){
			for (int i = 0; i < this.list.size(); i++) {  
	            getIsSelected().put(i, false);  
	        } 
		}
	}
	
	/**
	 * 当ListView数据发生变化时,调用此方法来更新ListView
	 * @param list
	 */
	public void updateListView(List<Email> list){
		this.list = list;
		notifyDataSetChanged();
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
	public View getView(int position, View convertView, ViewGroup parent) {
		
		convertView = LayoutInflater.from(context).inflate(R.layout.item_email_list, null);
		
		ImageView iv_not_read = (ImageView) convertView.findViewById(R.id.iv_not_read);
		ImageView iv_important = (ImageView) convertView.findViewById(R.id.iv_important);
		TextView tv_send_name = (TextView) convertView.findViewById(R.id.tv_send_name);
		TextView receipt_time = (TextView) convertView.findViewById(R.id.receipt_time);
		TextView tv_mail_subject = (TextView) convertView.findViewById(R.id.tv_mail_subject);
		CheckBox ck_choose = (CheckBox) convertView.findViewById(R.id.ck_choose);
		
		final int index = position;
		final Email bean = list.get(index);
		tv_send_name.setText(bean.getSenderName());
		if (null != bean.getReceiptTime() && !"".equals(bean.getReceiptTime().trim())) {
			String time = null;
			try {
				time = DateUtil.datetimeFormat(bean.getReceiptTime().replaceAll("T", " "));
			} catch (ParseException e) {
				e.printStackTrace();
			}
			receipt_time.setText(time);
		}else{
			receipt_time.setText("");
		}
		
		if(null != bean.getIsImportant() && "1".equals(bean.getIsImportant().trim()) && null != bean.getIsRead() && "0".equals(bean.getIsRead().trim())){
			iv_not_read.setVisibility(View.GONE);
			iv_important.setVisibility(View.VISIBLE);
			tv_send_name.setTextColor(0xff00925f);
			tv_mail_subject.setTextColor(0xff00925f);
		}else if(null != bean.getIsImportant() && "1".equals(bean.getIsImportant().trim())){
			iv_not_read.setVisibility(View.GONE);
			iv_important.setVisibility(View.VISIBLE);
		}
		else if(null != bean.getIsRead() && "0".equals(bean.getIsRead().trim())){
			iv_not_read.setVisibility(View.GONE);
			iv_important.setVisibility(View.GONE);
			tv_send_name.setTextColor(0xff00925f);
			tv_mail_subject.setTextColor(0xff00925f);
		}else{
			iv_not_read.setVisibility(View.GONE);
			iv_important.setVisibility(View.GONE);
		}
		
		ck_choose.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				getIsSelected().put(index, isChecked);
				Intent mIntent = new Intent(BroadcastNotice.EMAIL_UPDATE_LIST);
				mIntent.putExtra("index", index);
				mIntent.putExtra("isChecked", isChecked);
				mIntent.putExtra("mailCode", bean.getMailCode());
				context.sendBroadcast(mIntent);
			}
		});
		
		try {
			 ck_choose.setChecked(getIsSelected().get(index));
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		tv_mail_subject.setText(bean.getMailSubject());
		
		return convertView;
	}
	
	public static HashMap<Integer, Boolean> getIsSelected() {  
        return isSelected;  
    }  

    public static void setIsSelected(HashMap<Integer, Boolean> isSelected) {  
    	EmailListInfoAdapter.isSelected = isSelected;  
    } 
    
    public static class ViewHolder {
		public ImageView iv_tips;
		public TextView tv_important;
		public TextView tv_send_name;
		public TextView receipt_time;
		public TextView tv_mail_subject;
		public CheckBox ck_choose;
	}
}
 