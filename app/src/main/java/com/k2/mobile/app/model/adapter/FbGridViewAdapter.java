package com.k2.mobile.app.model.adapter;

import java.util.ArrayList;
import java.util.List;

import com.k2.mobile.app.R;
import com.k2.mobile.app.model.album.BitmapCache;
import com.k2.mobile.app.model.album.ImageItem;
import com.k2.mobile.app.model.album.BitmapCache.ImageCallback;
import com.k2.mobile.app.utils.LogUtil;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

public class FbGridViewAdapter extends BaseAdapter {

	private Context context;
	private List<ImageItem> imageLists;
	private BitmapCache cache;

	public FbGridViewAdapter(Context context) {
		this.context = context;
		cache = BitmapCache.getInstance();
	}

	public void setData(List<ImageItem> itemLists) {
		this.imageLists = new ArrayList<ImageItem>();
		this.imageLists = itemLists;
		notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		return imageLists.size() + 1;
	}

	@Override
	public Object getItem(int position) {
		return imageLists.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@SuppressLint("ViewHolder")
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		convertView = View.inflate(context, R.layout.activity_preview_gridview, null);
		ImageView img = (ImageView)convertView.findViewById(R.id.img);
		//最后一位就设置位添加按钮
		if(position == imageLists.size()) {
			img.setImageDrawable(context.getResources().getDrawable(R.drawable.button_selector_add));
		} else {
			ImageItem imageItem = imageLists.get(position);
			String thumbPath = imageItem.getThumbnailPath();
			String imagePath = imageItem.getImagePath();
			Bitmap bitmap = cache.getCacheBitmap(imageItem.getThumbnailPath(),imagePath);
			if (null != bitmap) {
				img.setImageBitmap(bitmap);
			} else {
				cache.displayBmp(img, thumbPath, imagePath, callback);
			}
		}
		return convertView;
	}

	ImageCallback callback = new ImageCallback() {
		@Override
		public void imageLoad(ImageView imageView, Bitmap bitmap,
				Object... params) {
			try {
				if (null != imageView && null != bitmap) {
					String url = (String) params[0];
					imageView.setImageBitmap(bitmap);
				} else {
					LogUtil.e("callback, bmp null");
				}
			} catch (Exception e) {
				LogUtil.e(e.getMessage());
			}
		}
	};

	/**现实图片参数配置*/
	private DisplayImageOptions getOptions() {
		DisplayImageOptions options = new DisplayImageOptions.Builder()
				.cacheInMemory(true)
				.cacheOnDisk(true)
				.showImageOnLoading(R.drawable.tt_message_image_default)
				.imageScaleType(ImageScaleType.IN_SAMPLE_INT)
				.bitmapConfig(Bitmap.Config.RGB_565)
				.build();
		return options;
	}

}