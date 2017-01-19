package com.k2.mobile.app.model.adapter;

import java.util.List;

import com.k2.mobile.app.R;
import com.k2.mobile.app.model.bean.FileBean;
import com.k2.mobile.app.utils.ImageLoaderUtil;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

/**
 * @Title MyGridViewAdapter.java
 * @Package com.oppo.mo.model.adapter
 * @Description 适配首页图标
 * @Company  K2
 * 
 * @author linqijun
 * @date 2015-07-31 20:43:00
 * @version V1.0
 */
public class GridViewShowImgAdapter extends BaseAdapter {
	
	private Context mContext;
	private List<FileBean> fList;
	private ImageLoaderUtil imageLoader;   
	
	public GridViewShowImgAdapter(Context mContext, List<FileBean> fList) {
		this.mContext = mContext;
		this.fList = fList;
		imageLoader = new ImageLoaderUtil(mContext);
	}

	@Override
	public int getCount() {
		return null == fList ? 0 : fList.size();
	}

	@Override
	public Object getItem(int position) {
		return fList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}
	
	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
			
		convertView = LayoutInflater.from(mContext).inflate(R.layout.item_grid_show_img, null);
		ImageView iv_img = (ImageView) convertView.findViewById(R.id.iv_img);
		
		final FileBean fBean = fList.get(position);
		
		if(null != fBean && null != fBean.getFilePath()){
			imageLoader.DisplayImage(fBean.getFilePath(), iv_img, 0, null); // 加载远程图片
		}
		
		return convertView;
	}
}
