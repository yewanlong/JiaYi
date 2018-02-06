
package com.kongqw.serialport.utils;

import android.os.Environment;

public class CommonUtils {
    /**
     * 检测Sdcard是否存在
     *
     * @return
     */
    public static boolean checkSDCard() {
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            return true;
        }
        return false;
    }


}
