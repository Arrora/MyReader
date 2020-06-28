package com.example.lib_common.utils;

/**
 * @Date: 2020/6/18
 * @Author: Aurora
 * @Description: dip与px之间转换的工具类
 */
public class PxUtil {

    /**
     * px转dip
     *
     * @param px
     * @return
     */
    public static int px2dip(float px) {
        final float scale = AppConfig.getApplication()
                .getResources()
                .getDisplayMetrics()
                .density;
        return (int) (px / scale + 0.5f);
    }

    /**
     * dip转px
     *
     * @param dip
     * @return
     */
    public static int dip2px(float dip) {
        final float scale = AppConfig.getApplication()
                .getResources()
                .getDisplayMetrics()
                .density;
        return (int) (dip * scale + 0.5f);
    }

}
