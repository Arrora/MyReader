package com.example.lib_common.dialog;

import android.app.Dialog;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.lib_common.R;
import com.example.lib_common.utils.PxUtil;


/**
 * @Date: 2020/6/18
 * @Author: Aurora
 * @Description: 抽象Dialog类，继承自DialogFragment
 * 这里面做了一些基础通用配置，还有一部分留给子类重写
 */
public abstract class BaseIDialog extends DialogFragment {

    private FragmentActivity activity;
    protected DisplayMetrics displayMetrics;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        activity = getActivity();
        if (activity == null) {
            throw new IllegalArgumentException("FragmentActivity为空");
        }
        displayMetrics = activity.getResources().getDisplayMetrics();
        Dialog dialog = createDialog();
        setWindowSize(dialog);
        return dialog;
    }

    /**
     * 交给子类创建dialog
     *
     * @return
     */
    @NonNull
    protected abstract Dialog createDialog();

    /**
     * 设置Window大小
     *
     * @param dialog
     */
    protected void setWindowSize(Dialog dialog) {
        Window window = dialog.getWindow();
        if (window == null) {
            throw new RuntimeException("dialog.getWindow()为空");
        }
        WindowManager.LayoutParams params = window.getAttributes();
        params.width = getWindowWidth();
        params.height = getWindowHeight();
        params.gravity = Gravity.CENTER;
        window.setAttributes(params);
    }

    /**
     * 设置Window宽，并返回
     *
     * @return
     */
    public int getWindowWidth() {
        return displayMetrics.widthPixels - PxUtil.dip2px(50);
    }

    /**
     * 设置Window高，并返回
     *
     * @return
     */
    public int getWindowHeight() {
        return WindowManager.LayoutParams.WRAP_CONTENT;
    }

    /**
     * 根据传入的填充View，创建dialog
     * 做一些基础配置，并返回
     *
     * @param customLayout
     * @return
     */
    protected Dialog getBaseDialog(@NonNull View customLayout) {
        Dialog dialog = new Dialog(activity, R.style.Theme_Dialog_Task);
        //设置Dialog传入的填充View
        dialog.setContentView(customLayout);
        //设置对话框弹出后，点击或按返回键不消失
        dialog.setCancelable(false);
        //设置对话框弹出后，点击不消失，按返回键消失
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
        return dialog;
    }

    /**
     * commit需要在onSaveInstanceState之前使用，否则会报错
     * commitAllowingStateLoss可以在onSaveInstanceState之后使用
     * 但是不太安全，在onSaveInstanceState之后使用，Fragment 的状态就丢失了
     *
     * @param manager
     * @param tag
     */
    @Override
    public void show(@NonNull FragmentManager manager, @Nullable String tag) {
        FragmentTransaction ft = manager.beginTransaction();
        ft.add(this, tag);
        ft.commitAllowingStateLoss();
    }

    /**
     * 对外提供show Dialog的接口
     */
    public void show(@NonNull FragmentManager manager) {
        String tag = getDialogTag();
        this.show(manager, tag);
    }

    /**
     * 交给子类重写，获取Tag
     */
    @NonNull
    public abstract String getDialogTag();
}
