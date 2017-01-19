package com.k2.mobile.app.utils;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import com.k2.mobile.app.common.config.LocalConstants;
import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiscCache;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.UsingFreqLimitedMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.utils.StorageUtils;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.WindowManager;

public class ImageLoderUtil {
	private static ImageLoaderConfiguration IMImageLoaderConfig;
    private static ImageLoader IMImageLoadInstance;
    private static Map<Integer,Map<Integer,DisplayImageOptions>> avatarOptionsMaps=new HashMap<Integer,Map<Integer,DisplayImageOptions>>();
    public final static int CIRCLE_CORNER = -10;

    public static void initImageLoaderConfig(Context context) {
        try {
        	String cachePath = "";
        	if(CommonUtil.sdCardIsAvailable()) {
        		cachePath = LocalConstants.LOCAL_FILE_PATH;
        	} else {
        		cachePath = LocalConstants.LOCAL_FILE_PATH_T;
        	}
            File cacheDir = StorageUtils.getOwnCacheDirectory(context,cachePath);
            File reserveCacheDir = StorageUtils.getCacheDirectory(context);

            int maxMemory = (int) (Runtime.getRuntime().maxMemory() );
            // 使用最大可用内存值的1/8作为缓存的大小。
            int cacheSize = maxMemory/8;
            DisplayMetrics metrics=new DisplayMetrics();
            WindowManager mWm = (WindowManager)context.getSystemService(Context.WINDOW_SERVICE);
            mWm.getDefaultDisplay().getMetrics(metrics);

            IMImageLoaderConfig = new ImageLoaderConfiguration.Builder(context)
                    .memoryCacheExtraOptions(metrics.widthPixels, metrics.heightPixels)
                    .threadPriority(Thread.NORM_PRIORITY-2)
//                    .denyCacheImageMultipleSizesInMemory()
                    .memoryCache(new UsingFreqLimitedMemoryCache(cacheSize))
                    .diskCacheFileNameGenerator(new Md5FileNameGenerator())
                    .tasksProcessingOrder(QueueProcessingType.LIFO)
                    .diskCacheExtraOptions(metrics.widthPixels, metrics.heightPixels, null)
                    .diskCache(new UnlimitedDiscCache(cacheDir,reserveCacheDir,new Md5FileNameGenerator()))
                    .diskCacheSize(1024 * 1024 * 1024)
                    .diskCacheFileCount(1000)
                    .build();

            IMImageLoadInstance = ImageLoader.getInstance();
            IMImageLoadInstance.init(IMImageLoaderConfig);
        }catch (Exception e){
            LogUtil.e(e.toString());
        }
    }
}
 