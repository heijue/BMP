package com.k2.mobile.app.utils;    
 
import java.io.File;   
import java.io.FileInputStream;   
import java.io.FileNotFoundException;   
import java.io.IOException;
import java.io.InputStream;   
import java.net.HttpURLConnection;   
import java.net.MalformedURLException;
import java.net.URL;   
import java.util.Collections;   
import java.util.Map;   
import java.util.WeakHashMap;   
import java.util.concurrent.ExecutorService;   
import java.util.concurrent.Executors;    

import com.k2.mobile.app.R;
import com.k2.mobile.app.model.cache.FileCache;
import com.k2.mobile.app.model.cache.MemoryCache;

import android.app.Activity;   
import android.content.Context;   
import android.graphics.Bitmap;   
import android.graphics.BitmapFactory;   
import android.widget.ImageView;   

public class ImageLoaderUtil {

	MemoryCache memoryCache = new MemoryCache();   
    FileCache fileCache;   
    private Map<ImageView, String> imageViews = Collections.synchronizedMap(new WeakHashMap<ImageView, String>());   
    ExecutorService executorService;    
    
    public ImageLoaderUtil(Context context){   
        fileCache = new FileCache(context);   
        executorService = Executors.newFixedThreadPool(5);   
    }   
    // 默认加载没获取到的图片
    final int stub_id = R.drawable.default_img;			// 默认设置图片
    final int head_img_man = R.drawable.man_circle;		// 默认男性头像
    final int head_img_woman = R.drawable.woman_circle;	// 默认女生头像
    final int head_img = R.drawable.pre_head_pic;			// 无性别
    
    public void DisplayImage(String url, ImageView imageView, int imgType, String sex)   
    {   
        imageViews.put(imageView, url);   
        Bitmap bitmap = memoryCache.get(url);   
        if(bitmap != null)   
            imageView.setImageBitmap(bitmap);   
        else {   
            queuePhoto(url, imageView);   
            if(1 == imgType){
            	if(null != sex && ("M".equals(sex) || "m".equals(sex) || "男".equals(sex))){
            		imageView.setImageResource(head_img_man);
            	}else if(null != sex && ("W".equals(sex) || "w".equals(sex) || "女".equals(sex))){
            		imageView.setImageResource(head_img_woman);
            	}else{
            		imageView.setImageResource(head_img);
            	}
            }else{
            	imageView.setImageResource(stub_id);
            }
        }   
    }   
    
    private void queuePhoto(String url, ImageView imageView)   
    {   
        PhotoToLoad p = new PhotoToLoad(url, imageView);   
        executorService.submit(new PhotosLoader(p));   
    }   
    
    private Bitmap getBitmap(String url)   
    {   
        File f = fileCache.getFile(url);   
        // 从sd卡  
        Bitmap b = decodeFile(f);   
        if(b != null)   
            return b;   
       
//        try {   
//            Bitmap bitmap = null;   
//            URL imageUrl = new URL(url);   
//            HttpURLConnection conn = (HttpURLConnection)imageUrl.openConnection();   
//            conn.setConnectTimeout(30000);   
//            conn.setReadTimeout(30000);   
//            conn.setInstanceFollowRedirects(true);   
//            InputStream is=conn.getInputStream();   
//            OutputStream os = new FileOutputStream(f);   
//            IOUtil.CopyStream(is, os);   
//            os.close();   
//            bitmap = decodeFile(f);  
//            return bitmap;   
//        } catch (Exception ex){   
//           ex.printStackTrace();   
//           return null;   
//        }  
        // 从网络  
        URL myFileUrl = null;
		Bitmap bitmap = null;
		
		try {
			myFileUrl = new URL(url);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		
		try {
			HttpURLConnection conn = (HttpURLConnection) myFileUrl.openConnection();
			conn.setDoInput(true);
			conn.connect();
			InputStream is = conn.getInputStream();
			bitmap = BitmapFactory.decodeStream(is);
			is.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return bitmap;
    }   
    
    //解码图像用来减少内存消耗  
    private Bitmap decodeFile(File f){   
        try {   
            //解码图像大小  
            BitmapFactory.Options o = new BitmapFactory.Options();   
            o.inJustDecodeBounds = true;   
            BitmapFactory.decodeStream(new FileInputStream(f),null,o);   
    
            //找到正确的刻度值，它应该是2的幂。  
            final int REQUIRED_SIZE = 70;   
            int width_tmp = o.outWidth;
            int height_tmp = o.outHeight;   
            int scale = 1;   
            while(true){   
                if(width_tmp/2<REQUIRED_SIZE || height_tmp/2<REQUIRED_SIZE)   
                    break;   
                width_tmp/=2;   
                height_tmp/=2;   
                scale*=2;   
            }   
    
            BitmapFactory.Options o2 = new BitmapFactory.Options();   
            o2.inSampleSize = scale;   
            return BitmapFactory.decodeStream(new FileInputStream(f), null, o2);   
        } catch (FileNotFoundException e) {
        	e.printStackTrace();
        }   
        return null;   
    }   
    
    // 任务队列  
    private class PhotoToLoad   
    {   
        public String url;   
        public ImageView imageView;   
        public PhotoToLoad(String u, ImageView i){   
            url = u;   
            imageView = i;   
        }   
    }   
    
    class PhotosLoader implements Runnable {   
        PhotoToLoad photoToLoad;   
        PhotosLoader(PhotoToLoad photoToLoad){   
            this.photoToLoad = photoToLoad;   
        }   
    
        @Override  
        public void run() {   
            if(imageViewReused(photoToLoad)){
            	 return; 
            }  
            Bitmap bmp = getBitmap(photoToLoad.url);   
            memoryCache.put(photoToLoad.url, bmp);   
            if(imageViewReused(photoToLoad))   
                return;   
            BitmapDisplayer bd = new BitmapDisplayer(bmp, photoToLoad);   
            Activity a = (Activity)photoToLoad.imageView.getContext();   
            a.runOnUiThread(bd);   
        }   
    }   
    
    boolean imageViewReused(PhotoToLoad photoToLoad){   
        String tag=imageViews.get(photoToLoad.imageView);   
        if(tag == null || !tag.equals(photoToLoad.url))   
            return true;   
        return false;   
    }   
    
    //用于显示位图在UI线程  
    class BitmapDisplayer implements Runnable   
    {   
        Bitmap bitmap;   
        PhotoToLoad photoToLoad;   
        
        public BitmapDisplayer(Bitmap b, PhotoToLoad p){
        	bitmap = b;
        	photoToLoad = p;
        }
        
        @Override
		public void run()   
        {   
            if(imageViewReused(photoToLoad))   
                return;   
            if(bitmap != null)   
                photoToLoad.imageView.setImageBitmap(bitmap);   
            else  
                photoToLoad.imageView.setImageResource(stub_id);   
        }   
    }   
    
    public void clearCache() {   
        memoryCache.clear();   
        fileCache.clear();   
    }   
}
 