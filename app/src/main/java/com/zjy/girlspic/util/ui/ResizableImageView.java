package com.zjy.girlspic.util.ui;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * Created by jiyoung.tsang on 16/7/4.
 */
public class ResizableImageView extends ImageView {

    public ResizableImageView(Context context){
        super(context);
    }

    public ResizableImageView(Context context, AttributeSet attrs){
        super(context,attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        Drawable drawable = getDrawable();
        if(drawable!=null){
            int width = MeasureSpec.getSize(widthMeasureSpec);
            //根据宽度和原比例来计算高度
            int heigth = (int)Math.ceil((float)width * (float)drawable.getIntrinsicHeight()/(float)drawable.getIntrinsicWidth());
            setMeasuredDimension(width,heigth);
        }else {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        }
    }
}
