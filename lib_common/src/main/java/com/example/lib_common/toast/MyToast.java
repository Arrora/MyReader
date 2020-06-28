package com.example.lib_common.toast;

import android.app.Application;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.arch.core.executor.ArchTaskExecutor;

import com.example.lib_common.R;
import com.example.lib_common.utils.AppConfig;


/**
 * @Date: 2020/6/19
 * @Author: Aurora
 * @Description: 可定制View样式的Toast
 */
public class MyToast {

    //加上volatile，防止指令重排序，造成多线程获取单例空指针
    private static volatile MyToast myToast;

    private static final Object mLock = new Object();

    private Toast mToast;

    private String msg;

    private int mRootViewId;
    private View mRootView;
    private IBindListener mListener;

    private MyToast() {
    }

    /**
     * 双重同步锁，保证单一实例
     *
     * @return
     */
    public static MyToast getInstance() {
        if (myToast == null) {
            synchronized (mLock) {
                if (myToast == null) {
                    myToast = new MyToast();
                }
            }
        }
        return myToast;
    }

    /**
     * 初始化Toast,以及对toast的一些必要配置
     */
    private void initToast(boolean needRootViewId) {
        initView(needRootViewId);
        if (mToast == null) {
            mToast = new Toast(AppConfig.getApplication());
        }
        mToast.setDuration(Toast.LENGTH_SHORT);//默认较短时间显示
        mToast.setView(mRootView);
    }

    /**
     * 初始化Toast里的View
     */
    private void initView(boolean needRootViewId) {
        Application mApplication = AppConfig.getApplication();
        LayoutInflater inflater = LayoutInflater.from(mApplication);
        if (!needRootViewId) {
            // 初始化默认RootView，并设置初始化默认RootView里的View
            mRootView = inflater.inflate(getDefaultRootViewId(), null);
            defaultBindView(mRootView);
        } else {
            // 根据外部传入的RootViewId初始化RootView，
            // 并把RootView通过接口回调出去
            mRootView = inflater.inflate(mRootViewId, null);
            mListener.bindView(mRootView);
        }
    }

    /**
     * 默认布局里的View设置
     *
     * @param mRootView
     */
    private void defaultBindView(View mRootView) {
        TextView tvMsg = mRootView.findViewById(R.id.tv_msg);
        tvMsg.setText(msg);
    }

    /**
     * 默认布局
     *
     * @return
     */
    private int getDefaultRootViewId() {
        return R.layout.toast_default_layout;
    }

    /**
     * 展示自定义Toast时调用
     * 需要外部传入自定义的RootViewId、自定义RootView初始化的接口
     *
     * @param rootViewId
     * @return
     */
    public void show(@LayoutRes int rootViewId, @NonNull IBindListener listener) {
        mRootViewId = rootViewId;
        mListener = listener;
        show(true);
    }

    /**
     * 展示默认Toast时调用
     * 需要给里面的TextView设置一个msg
     *
     * @param message
     */
    public void show(String message) {
        msg = message;
        show(false);
    }

    /**
     * 判断Toast调用是在UI线程，还是非UI线程
     * 如果在UI线程，直接创建Toast，并展示出来
     * 如果不在UI线程，需要切换到UI线程，再创建Toast并展示
     * @param needRootViewId
     */
    private void show(boolean needRootViewId) {
        if (Looper.myLooper() == Looper.getMainLooper()) {
            initToast(needRootViewId);
            mToast.show();
        } else {
            ArchTaskExecutor.getMainThreadExecutor().execute(() -> {
                initToast(needRootViewId);
                mToast.show();
            });
        }
    }
}
