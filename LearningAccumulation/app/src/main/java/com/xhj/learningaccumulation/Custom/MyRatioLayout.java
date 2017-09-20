package com.xhj.learningaccumulation.Custom;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

import com.xhj.learningaccumulation.R;


/**
 * Created by Administrator on 2017/5/8.
 */
public class MyRatioLayout extends RelativeLayout {
    private float ratio;

    public MyRatioLayout(Context context) {
        super(context);
    }

    public MyRatioLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.MyRatioLayout);
        //根据属性名称获取对应的值，属性名称的格式为类名_属性名
        ratio = typedArray.getFloat(R.styleable.MyRatioLayout_ratio, 0.0f);

    }

    public MyRatioLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @SuppressWarnings("unused")

    @Override

    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        // For simple implementation, or internal size is always 0.

        // We depend on the container to specify the layout size of

        // our view. We can't really know what it is since we will be

        // adding and removing different arbitrary views and do not

        // want the layout to change as this happens.

//        setMeasuredDimension(getDefaultSize(0, widthMeasureSpec), getDefaultSize(0, heightMeasureSpec));
//
//
//
//        // Children are just made to fill our space.
//
//        int childWidthSize = getMeasuredWidth();
//
//        int childHeightSize = getMeasuredHeight();
//
//        //高度和宽度一样
//
//        heightMeasureSpec = widthMeasureSpec = MeasureSpec.makeMeasureSpec(childWidthSize, MeasureSpec.EXACTLY);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        //获取高度的模式和尺寸
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        //宽确定，高不确定
//        if(widthMode == MeasureSpec.EXACTLY&&heightMode!=MeasureSpec.EXACTLY&&ratio!=0){
        heightSize = (int) (widthSize* ratio +0.5f);//根据宽度和比例计算高度

        heightMeasureSpec = MeasureSpec.makeMeasureSpec(heightSize, MeasureSpec.EXACTLY);


        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

    }
}
