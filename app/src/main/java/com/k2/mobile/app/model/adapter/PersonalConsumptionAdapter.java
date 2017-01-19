package com.k2.mobile.app.model.adapter;

import java.util.List;

import com.k2.mobile.app.R;
import com.k2.mobile.app.model.bean.PConsumptionBean;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class PersonalConsumptionAdapter extends BaseAdapter {

    private List<PConsumptionBean> lt_pc = null;
    private Activity activit = null;
 
    public PersonalConsumptionAdapter(Activity _activit, List<PConsumptionBean> _lt_pc ) {
    	activit = _activit;
    	lt_pc = _lt_pc;
    }

    @Override
    public int getCount() {
        return lt_pc == null ? 0 : lt_pc.size() ;
    }

    @Override
    public Object getItem(int position) {
        return lt_pc.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
    	View v = convertView;
		ViewHolder holder = null;
		if(v == null){
			holder = new ViewHolder();
			v = LayoutInflater.from(activit).inflate(R.layout.item_personal_consumption, null);
			
			holder.tv_consume_money = (TextView) v.findViewById(R.id.tv_consume_money);
			holder.tv_consume_time = (TextView) v.findViewById(R.id.tv_consume_time);
			holder.tv_current_balance = (TextView) v.findViewById(R.id.tv_current_balance);
			holder.tv_deposit_name = (TextView) v.findViewById(R.id.tv_deposit_name);
			holder.tv_node = (TextView) v.findViewById(R.id.tv_node);
			
			v.setTag(holder);
		}else{
			holder = (ViewHolder) v.getTag();
		}
		
		final PConsumptionBean bean = lt_pc.get(position);
		
		holder.tv_consume_money.setText("￥"+bean.getConsume_money());
		holder.tv_current_balance.setText("￥"+bean.getBalance());
		holder.tv_deposit_name.setText(bean.getDeposit_name());
		holder.tv_consume_time.setText(bean.getConsume_time());
		holder.tv_node.setText(bean.getNode_name());
		
		return v;
	}
	
	protected class ViewHolder{
		TextView tv_consume_money;
		TextView tv_consume_time;
		TextView tv_deposit_name;
		TextView tv_current_balance;
		TextView tv_node;
	}

}
