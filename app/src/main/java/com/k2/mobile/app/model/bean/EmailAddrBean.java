package com.k2.mobile.app.model.bean;    

import java.io.Serializable;

import android.view.View;
import android.widget.TextView;
  
public class EmailAddrBean implements Serializable{
	
    private View view;
    private TextView tv_tmp;
    
	public View getView() {
		return view;
	}
	public void setView(View view) {
		this.view = view;
	}
	public TextView getTv_tmp() {
		return tv_tmp;
	}
	public void setTv_tmp(TextView tv_tmp) {
		this.tv_tmp = tv_tmp;
	}
 
}
 