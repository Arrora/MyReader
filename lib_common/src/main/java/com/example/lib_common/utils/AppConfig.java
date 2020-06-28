package com.example.lib_common.utils;

import android.annotation.SuppressLint;
import android.app.Application;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * @Date: 2020/6/18
 * @Author: Aurora
 * @Description: 通过反射获取Application的工具类
 */
public class AppConfig {

    private static Application mApplication;
    private static final Object mLock = new Object();

    @SuppressLint("PrivateApi")
    public static Application getApplication() {
        if (mApplication == null) {
            synchronized (mLock) {
                if (mApplication == null) {
                    try {
//                        mApplication = (Application) Class.forName("android.app.ActivityThread")
//                                .getMethod("currentApplication")
//                                .invoke(null, (Object[]) null);
                        Class aClass = Class.forName("android.app.ActivityThread");
                        Method currentApplication = aClass.getMethod("currentApplication");
                        mApplication = (Application)currentApplication.invoke(null, null);
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    } catch (NoSuchMethodException e) {
                        e.printStackTrace();
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    } catch (InvocationTargetException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return mApplication;
    }
}
