package com.xhj.learningaccumulation.Custom;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.PopupWindow;

/**
 * created by xuhaijia 2017/9/20
 * <p>
 * 背景自动变暗的PopupWindow
 */

public class MyPopupWindow extends PopupWindow {
    Activity mContext;
    private LayoutInflater mInflater;
    private View mContentView;
    private float darkF = 0.5f;


    public MyPopupWindow(Activity context, int layout, int style) {
        super(context);
        this.mContext = context;
        //打气筒
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        //打气
        mContentView = mInflater.inflate(layout, null);
        //设置View
        setContentView(mContentView);
        //设置宽与高
        setWidth(WindowManager.LayoutParams.MATCH_PARENT);

        setHeight(WindowManager.LayoutParams.WRAP_CONTENT);

        /**
         * 设置进出动画
         */
        setAnimationStyle(style);


        /**
         * 设置背景只有设置了这个才可以点击外边和BACK消失
         */
        setBackgroundDrawable(new ColorDrawable());


        /**
         * 设置可以获取集点
         */
        setFocusable(true);

        /**
         * 设置点击外边可以消失
         */
//        setOutsideTouchable(true);

        /**
         *设置可以触摸
         */
        setTouchable(true);


        /**
         * 初始化View与监听器
         */
        initView();

        initListener();
    }


    private void initView() {

    }

    private void initListener() {

    }

    // 设置背景亮度
    public void setDarkF(float darkF) {
        this.darkF = darkF;
    }

    @Override
    public void showAsDropDown(View anchor, int xoff, int yoff) {
        super.showAsDropDown(anchor, xoff, yoff);
    }

    @Override
    public void showAsDropDown(View anchor) {
        super.showAsDropDown(anchor);
    }

    @Override
    public void showAsDropDown(View anchor, int xoff, int yoff, int gravity) {
        super.showAsDropDown(anchor, xoff, yoff, gravity);
        WindowManager.LayoutParams layoutParams = mContext.getWindow().getAttributes();
        layoutParams.alpha = darkF;
        mContext.getWindow().setAttributes(layoutParams);

    }

    @Override
    public void dismiss() {
        super.dismiss();
        WindowManager.LayoutParams layoutParams = mContext.getWindow().getAttributes();
        layoutParams.alpha = 1.0f;
        mContext.getWindow().setAttributes(layoutParams);
    }

}
