package com.kongqw.serialport.utils;

import android.content.Context;
import android.os.Environment;
import android.util.DisplayMetrics;
import android.view.WindowManager;

import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiskCache;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.UsingFreqLimitedMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.utils.StorageUtils;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by zhujian on 15/1/14.
 */
public class ImageLoaderUtil {
    private static ImageLoaderConfiguration IMImageLoaderConfig;
    private static ImageLoader IMImageLoadInstance;
    private static Map<Integer, Map<Integer, DisplayImageOptions>> avatarOptionsMaps = new HashMap<>();
    public static final int FILE_SAVE_TYPE_IMAGE = 0X00013;

    public static void initImageLoaderConfig(Context context) {
        try {
            File cacheDir = StorageUtils.getOwnCacheDirectory(context,
                    getSavePath(FILE_SAVE_TYPE_IMAGE));
            File reserveCacheDir = StorageUtils.getCacheDirectory(context);

            int maxMemory = (int) (Runtime.getRuntime().maxMemory());
            // 使用最大可用内存值的1/8作为缓存的大小。
            int cacheSize = maxMemory / 8;
            DisplayMetrics metrics = new DisplayMetrics();
            WindowManager mWm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
            mWm.getDefaultDisplay().getMetrics(metrics);

            IMImageLoaderConfig = new ImageLoaderConfiguration.Builder(context)
                    .memoryCacheExtraOptions(metrics.widthPixels, metrics.heightPixels)
                    .threadPriority(Thread.NORM_PRIORITY - 2)
                    // .denyCacheImageMultipleSizesInMemory()
                    .memoryCache(new UsingFreqLimitedMemoryCache(cacheSize))
                    .diskCacheFileNameGenerator(new Md5FileNameGenerator())
                    .tasksProcessingOrder(QueueProcessingType.LIFO)
                    .diskCacheExtraOptions(metrics.widthPixels, metrics.heightPixels, null)
                    .diskCache(new UnlimitedDiskCache(cacheDir, reserveCacheDir, new Md5FileNameGenerator()))
                    .diskCacheSize(1024 * 1024 * 1024).diskCacheFileCount(1000).build();

            IMImageLoadInstance = ImageLoader.getInstance();
            IMImageLoadInstance.init(IMImageLoaderConfig);
        } catch (Exception e) {
        }
    }

    /**
     * 图片缓存地址
     *
     * @param type
     * @return
     */
    public static String getSavePath(int type) {
        String path;
        String folder = (type == FILE_SAVE_TYPE_IMAGE) ? "images" : "audio";
        if (CommonUtils.checkSDCard()) {
            path = Environment.getExternalStorageDirectory().toString() + File.separator + "BabyTalk" + File.separator
                    + folder + File.separator;

        } else {
            path = Environment.getDataDirectory().toString() + File.separator + "BabyTalk" + File.separator + folder
                    + File.separator;
        }
        return path;
    }

    /**
     * 清除缓存 这个暂时不要删
     */
    public static void clearCache() {
        try {
            if (IMImageLoadInstance != null) {
                IMImageLoadInstance.clearMemoryCache();
                IMImageLoadInstance.clearDiskCache();
            }
            if (null != avatarOptionsMaps) {
                avatarOptionsMaps.clear();
            }
        } catch (Exception e) {
        }
    }
}
