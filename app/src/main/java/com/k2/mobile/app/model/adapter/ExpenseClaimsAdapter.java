package com.k2.mobile.app.model.adapter;

import java.util.List;

import com.k2.mobile.app.R;
import com.k2.mobile.app.model.bean.ExpenseClaimsBean;
import com.k2.mobile.app.utils.StateCodeContrast;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class ExpenseClaimsAdapter extends BaseAdapter {

    private List<ExpenseClaimsBean> lt_pc = null;
    private Activity activit = null;
 
    public ExpenseClaimsAdapter(Activity _activit, List<ExpenseClaimsBean> _lt_pc ) {
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
			v = LayoutInflater.from(activit).inflate(R.layout.item_expense_claims, null);
			
			holder.tv_time = (TextView) v.findViewById(R.id.tv_time);
			holder.tv_money = (TextView) v.findViewById(R.id.tv_money);
			holder.tv_state = (TextView) v.findViewById(R.id.tv_state);
			
			v.setTag(holder);
		}else{
			holder = (ViewHolder) v.getTag();
		}
		
		final ExpenseClaimsBean bean = lt_pc.get(position);
		
		holder.tv_time.setText(bean.getReport_submitted_date());
		holder.tv_money.setText("￥"+bean.getTotal());
		String tips = StateCodeContrast.getStartCode(bean.getStatus(), activit.getResources());
		holder.tv_state.setText(tips);
		
		return v;
	}
	
	protected class ViewHolder{
		TextView tv_time;
		TextView tv_money;
		TextView tv_state;
	}

}
