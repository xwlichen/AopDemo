package com.smart.aoplibrary.utils;


import static com.smart.aoplibrary.Constant.SINGLE_CLICK_SPACE_TIME;

/**
 * Created by PC on 2018/6/8.
 */

public class CommonUtils {


    public  static long lastClickTime = 0;//最新点击事件
    /**
     *
     * @return tr
     */
    public synchronized static boolean isSingleClick() {
        long currentTime = System.currentTimeMillis();
        boolean flag;
        if (currentTime - lastClickTime >
                SINGLE_CLICK_SPACE_TIME) {
            flag = true;
        } else {
            flag = false;
        }
        lastClickTime = currentTime;
        return flag;
    }



}
