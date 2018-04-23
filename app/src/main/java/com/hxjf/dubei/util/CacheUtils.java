package com.hxjf.dubei.util;

import android.content.Context;
import android.os.Environment;
import android.text.format.Formatter;

import java.io.File;

/**
 * Created by Chen_Zhang on 2017/6/28.
 */

public class CacheUtils {
    //获取缓存大小
    public static String getTotalCacheSize(Context context) throws Exception {
        long cacheSize = getFolderSize(context.getCacheDir());
        //判断是否有存储卡
        if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
            cacheSize += getFolderSize(context.getExternalCacheDir());
        }
        return Formatter.formatFileSize(context,cacheSize);
        
    }

    //清理缓存
    public static void clearAllCache(Context context){
        delete(context.getCacheDir());
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
            delete(context.getExternalCacheDir());
        }
    }

    private static boolean delete(File cacheDir) {
        if (cacheDir != null && cacheDir.isDirectory() ){
            String[] children = cacheDir.list();
            for (int i = 0; i < children.length; i++) {
                boolean success = delete(new File(cacheDir,children[i]));
                if (!success){
                    return false;
                }
            }
        }
        return cacheDir.delete();
    }


    private static long getFolderSize(File file) throws Exception {
        long size = 0;
        try {
            File[] fileList = file.listFiles();
            for (int i = 0; i < fileList.length; i++) {
                // 如果下面还有文件
                if (fileList[i].isDirectory()) {
                    size = size + getFolderSize(fileList[i]);
                } else {
                    size = size + fileList[i].length();
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return size;
    }
}
