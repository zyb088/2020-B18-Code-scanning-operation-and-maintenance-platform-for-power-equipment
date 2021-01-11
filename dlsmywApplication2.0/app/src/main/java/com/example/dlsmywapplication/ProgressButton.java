package com.example.dlsmywapplication;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.util.AttributeSet;

import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.AppCompatButton;


//工具类
public class ProgressButton extends AppCompatButton {
    /**
     * 圆角幅度
     */
    private float mCornerRadius = 0;
    private float mProgressMargin = 0;
    /**
     * 是否进度条已走完
     */
    private boolean mFinish;
    /**
     * 当前进度值
     */
    private int mProgress;
    /**
     * 最大进度值
     */
    private int mMaxProgress = 100;
    /**
     * 最小进度值
     */
    private int mMinProgress = 0;

    private GradientDrawable mDrawableButton;
    private GradientDrawable mDrawableProgressBackground;
    private GradientDrawable mDrawableProgress;

    @RequiresApi(api = Build.VERSION_CODES.M)
    public ProgressButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        initialize(context, attrs);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public ProgressButton(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initialize(context, attrs);
    }
    /**
     * 自定义简单组件的必须流程，在这里接收传入的自定义属性，完成各属性值和数据的初始化
     * @param context 上下文
     * @param attrs 自定义属性值
     */
    @RequiresApi(api = Build.VERSION_CODES.M)
    private void initialize(Context context, AttributeSet attrs) {
        //Progress background drawable
        mDrawableProgressBackground = new GradientDrawable();
        //Progress drawable
        mDrawableProgress = new GradientDrawable();
        //Normal drawable
        mDrawableButton = new GradientDrawable();

        //Get default normal color
        int defaultButtonColor = getResources().getColor(R.color.colorGray, null);
        //Get default progress color
        int defaultProgressColor = getResources().getColor(R.color.darkblue, null);
        //Get default progress background color
        int defaultBackColor = getResources().getColor(R.color.colorGray, null);

        TypedArray attr = context.obtainStyledAttributes(attrs, R.styleable.ProgressButton);

        try {
            mProgressMargin = attr.getDimension(R.styleable.ProgressButton_progressMargin, mProgressMargin);
            mCornerRadius = attr.getDimension(R.styleable.ProgressButton_cornerRadius, mCornerRadius);
            //Get custom normal color
            int buttonColor = attr.getColor(R.styleable.ProgressButton_buttonColor, defaultButtonColor);
            //Set normal color
            mDrawableButton.setColor(buttonColor);
            //Get custom progress background color
            int progressBackColor = attr.getColor(R.styleable.ProgressButton_progressBackColor, defaultBackColor);
            //Set progress background drawable color
            mDrawableProgressBackground.setColor(progressBackColor);
            //Get custom progress color
            int progressColor = attr.getColor(R.styleable.ProgressButton_progressColor, defaultProgressColor);
            //Set progress drawable color
            mDrawableProgress.setColor(progressColor);

            //Get default progress
            mProgress = attr.getInteger(R.styleable.ProgressButton_progress, mProgress);
            //Get minimum progress
            mMinProgress = attr.getInteger(R.styleable.ProgressButton_minProgress, mMinProgress);
            //Get maximize progress
            mMaxProgress = attr.getInteger(R.styleable.ProgressButton_maxProgress, mMaxProgress);

        } finally {
            attr.recycle();
        }

        //Set corner radius
        mDrawableButton.setCornerRadius(mCornerRadius);
        mDrawableProgressBackground.setCornerRadius(mCornerRadius);
        mDrawableProgress.setCornerRadius(mCornerRadius - mProgressMargin);
        setBackgroundDrawable(mDrawableButton);

        mFinish = false;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (mProgress > mMinProgress && mProgress <= mMaxProgress && !mFinish) {
            //Calculate the width of progress
            float progressWidth =
                    (float) getMeasuredWidth() * ((float) (mProgress - mMinProgress) / mMaxProgress - mMinProgress);

            //If progress width less than 2x corner radius, the radius of progress will be wrong
            if (progressWidth < mCornerRadius * 2) {
                progressWidth = mCornerRadius * 2;
            }

            //Set rect of progress
            mDrawableProgress.setBounds((int) mProgressMargin, (int) mProgressMargin,
                    (int) (progressWidth - mProgressMargin), getMeasuredHeight() - (int) mProgressMargin);

            //Draw progress
            mDrawableProgress.draw(canvas);

            if (mProgress == mMaxProgress) {
                setBackgroundDrawable(mDrawableButton);
                mFinish = true;
            }
        }
        super.onDraw(canvas);
    }

    /**
     * Set current progress
     */
    public void setProgress(int progress) {
        if (!mFinish) {
            mProgress = progress;
            setBackgroundDrawable(mDrawableProgressBackground);
            invalidate();
        }
    }

    public void setMaxProgress(int maxProgress) {
        mMaxProgress = maxProgress;
    }

    public void setMinProgress(int minProgress) {
        mMinProgress = minProgress;
    }

    public void reset() {
        mFinish = false;
        mProgress = mMinProgress;
    }
}
