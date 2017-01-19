package com.k2.mobile.app.model.adapter;    

import java.util.List;

import android.content.Context;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.k2.mobile.app.R;
import com.k2.mobile.app.model.bean.EmailBean;
  
/**
 * @Title EmailHomeAdapter.java
 * @Package com.oppo.mo.model.adapter
 * @Description 邮件首页
 * @Company  K2
 * 
 * @author linqijun
 * @date 2015-04-15 10:31:00
 * @version V1.0
 */
public class EmailHomeAdapter extends BaseAdapter {

	private Context context;
	private List<EmailBean> list;
	
	public EmailHomeAdapter(Context context, List<EmailBean> list) {
		super();
		this.context = context;
		this.list = list;
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
		
		convertView = LayoutInflater.from(context).inflate(R.layout.item_email_home, null);
		LinearLayout ll_title = (LinearLayout) convertView.findViewById(R.id.ll_title);
		LinearLayout ll_body = (LinearLayout) convertView.findViewById(R.id.ll_body);
		
		TextView tv_tips = (TextView) convertView.findViewById(R.id.tv_tips);
		TextView tv_folder = (TextView) convertView.findViewById(R.id.tv_folder);
		TextView tv_name = (TextView) convertView.findViewById(R.id.tv_name);
		TextView tv_sum = (TextView) convertView.findViewById(R.id.tv_sum);
		
		final EmailBean bean =  list.get(position);
		if("7".equals(bean.getFolderType()) || "8".equals(bean.getFolderType())){
			ll_body.setVisibility(View.GONE);
			tv_tips.setText(bean.getFolderName());
		}else {
			ll_title.setVisibility(View.GONE);
			if("1".equals(bean.getFolderType())){
				tv_folder.setBackgroundResource(R.drawable.inbox);
			}else if("2".equals(bean.getFolderType())){
				tv_folder.setBackgroundResource(R.drawable.sendmail);
			}else if("3".equals(bean.getFolderType())){
				tv_folder.setBackgroundResource(R.drawable.drafts);
			}else if("4".equals(bean.getFolderType())){
				tv_folder.setBackgroundResource(R.drawable.wastebasket);
			}else if("6".equals(bean.getFolderType())){
				tv_folder.setBackgroundResource(R.drawable.write_email_bg);
			}
			
			if (null != bean.getMailCount() && !"".equals(bean.getMailCount().trim())) {
				tv_name.setText(bean.getFolderName());
				if(Integer.parseInt(bean.getMailCount())<1){
					tv_sum.setVisibility(View.GONE);
				}else{
					tv_sum.setText(bean.getMailCount());
				}
			}else{
				tv_name.setText(bean.getFolderName());
				tv_sum.setVisibility(View.GONE);
			}
		}
			
		return convertView;
	}
	
	public static int getScreenWidth(Context context) { 
	    WindowManager manager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE); 
	    Display display = manager.getDefaultDisplay(); 
	    return display.getWidth(); 
	} 
	
	protected class ViewHolder {
		LinearLayout ll_title;
		LinearLayout ll_body;
		TextView tv_tips;
		TextView tv_folder;
		TextView tv_name;
		TextView tv_sum;
	}
}
 