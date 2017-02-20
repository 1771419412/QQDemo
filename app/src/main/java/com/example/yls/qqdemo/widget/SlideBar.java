package com.example.yls.qqdemo.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.example.yls.qqdemo.R;
import com.hyphenate.util.DensityUtil;

/**
 * Created by 雪无痕 on 2017/2/8.
 */

public class SlideBar extends View {

    private static final String[] SECTIONS = {"A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L"
            , "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z"};
    private Paint mPaint;
    private float mTextSize;
    private float mBaseLine;
    private OnSlideChangeListener mOnSlideChangeListener;
    private int mCurrentIndex=-1;
    private String mFirstLetter;


    public SlideBar(Context context) {
        this(context, null);
    }

    public SlideBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        mPaint = new Paint();
        mPaint.setTextSize(DensityUtil.sp2px(getContext(), 14));
        mPaint.setAntiAlias(true);
        mPaint.setTextAlign(Paint.Align.CENTER);//设置文本剧中
        mPaint.setColor(getResources().getColor(R.color.slide_bar_text_color));
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        mTextSize = h * 1.0f / SECTIONS.length;
        Paint.FontMetrics fontMetrics = mPaint.getFontMetrics();
        float textHeight = fontMetrics.descent - fontMetrics.ascent;
        mBaseLine = mTextSize / 2 + (textHeight / 2 - fontMetrics.descent);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        float x = getWidth() * 1.0f / 2;
        float y = mBaseLine;
        for (int i = 0; i < SECTIONS.length; i++) {
            canvas.drawText(SECTIONS[i], x, y, mPaint);
            y += mTextSize;
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                setBackgroundResource(R.drawable.bg_slidebar);
                break;
            case MotionEvent.ACTION_MOVE:
                //找出手指移动时所在的字符
                int  index = (int) (event.getY() / mTextSize);
                //只有位置发生变化才会通知外界
                if(index!=mCurrentIndex){
                    mFirstLetter = SECTIONS[index];
                    if(mOnSlideChangeListener!=null){
                        mOnSlideChangeListener.onSlideChange(mFirstLetter);
                    }
                }
                mCurrentIndex=index;
                break;
            case MotionEvent.ACTION_UP:
                setBackgroundColor(Color.TRANSPARENT);

                break;
        }
        return true;
    }
    public interface OnSlideChangeListener{
        void onSlideChange(String firstLetter);

    }
    public void setOnSlideChangeListener(OnSlideChangeListener l){
        mOnSlideChangeListener=l;

    }
}
