package com.example.exercisendk;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapRegionDecoder;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;

import android.view.SurfaceView;
import android.view.View;
import android.widget.Scroller;

/**
 * 大图加载控件
 */
import androidx.annotation.Nullable;

import com.me.support.utils.LogUtil;

import java.io.IOException;
import java.io.InputStream;

public class BigImageView extends View implements GestureDetector.OnGestureListener {

    private static final String TAG = BigImageView.class.getSimpleName();

    private BitmapFactory.Options options;

    // 控件宽度
    private int measuredWidth = 0;

    // 控件高度
    private int measuredHeight = 0;

    // 区域解码器的矩形参数
    private Rect rect;

    // 手势类
    private GestureDetector detector;

    // 区域解码器
    private BitmapRegionDecoder decoder;

    private Scroller scroller;

    // 图片宽度
    private int imageWidth;

    // 图片高度
    private int imageHeight;

    // 图片宽度的缩放因子
    private float widthScale = 1;

    // 图片高度的缩放因子
    private float heightScale = 1;

    public BigImageView(Context context) {
        this(context, null, 0);
    }

    public BigImageView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BigImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        detector = new GestureDetector(context, this);
        options = new BitmapFactory.Options();
        scroller = new Scroller(context);
    }

    /**
     * 加载图片
     *
     * @param inputStream inputStream
     */
    public void loadImage(InputStream inputStream) {
        options.inJustDecodeBounds = true;
        // 解码后的bitmap，只有属性（宽、高），没有大小，不占用内存
        BitmapFactory.decodeStream(inputStream, null, options);
        // 图片宽度
        imageWidth = options.outWidth;
        if (imageWidth < measuredWidth) {
            widthScale = measuredWidth / (imageWidth * 1.0f);
            imageWidth = measuredWidth;
        }
        // 图片高度
        imageHeight = options.outHeight;
        if (imageHeight < measuredHeight) {
            heightScale = measuredHeight / (imageHeight * 1.0f);
            imageHeight = measuredHeight;
        }
        // 可复用开关打开
        options.inMutable = true;
        options.inJustDecodeBounds = false;
        // 根据图片大小，初始化矩形参数
        rect = initRect(imageWidth, imageHeight);
        try {
            // 初始化区域解码器
            decoder = BitmapRegionDecoder.newInstance(inputStream, false);
        } catch (IOException e) {
            e.printStackTrace();
        }
        // 重新执行测量、布局、绘制
        requestLayout();
        invalidate();
    }

    /**
     * 初始化区域解码器的矩形参数
     *
     * @param imageWidth imageWidth
     * @param imageHeight imageHeight
     * @return
     */
    private Rect initRect(int imageWidth, int imageHeight) {
        Rect rect = new Rect();
        if (imageWidth > measuredWidth) { // 图片的宽度大于控件宽度
            rect.left = (imageWidth - measuredWidth) / 2;
            rect.right = measuredWidth + rect.left;
        } else { // 如果相等
            rect.left = 0;
            rect.right = measuredWidth;
        }
        if (imageHeight > measuredHeight) { // 图片的高度大于控件高度
            rect.top = (imageHeight - measuredHeight) / 2;
            rect.bottom = measuredHeight + rect.top;
        } else { // 如果相等
            rect.top = 0;
            rect.bottom = measuredHeight;
        }

        return rect;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        measuredWidth = getMeasuredWidth();
        measuredHeight = getMeasuredHeight();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (decoder == null) { // 区域解码器的对象不能为空
            Log.i(TAG, "decoder is null");
            return;
        }
        Bitmap bitmap = decoder.decodeRegion(rect, options);
        Matrix matrix = new Matrix();
//        matrix.setScale(widthScale * heightScale, widthScale * heightScale);
        matrix.setScale(widthScale, heightScale);
        if (bitmap != null) {
            Log.i(TAG, "drawBitmap--163");
            canvas.drawBitmap(bitmap, matrix, null);
        }
    }

    @Override
    public boolean onDown(MotionEvent e) {
        if (!scroller.isFinished()) {
            scroller.forceFinished(true);
        }
        return true;
    }

    @Override
    public void onShowPress(MotionEvent e) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        if (rect == null) {
            Log.i(TAG, "rect is null");
            return true;
        }
        rect.offset((int) distanceX, (int) distanceY);
        if (rect.left < 0) {
            rect.left = 0;
            rect.right = measuredWidth;
        }
        if (rect.top < 0) {
            rect.top = 0;
            rect.bottom = measuredHeight;
        }
        if (rect.right > imageWidth) {
            rect.right = imageWidth;
            rect.left = imageWidth - measuredWidth;
        }
        if (rect.bottom > imageHeight) {
            rect.bottom = imageHeight;
            rect.top = imageHeight - measuredHeight;
        }
        Log.i(TAG, "onScroll, e1.getAction()-->"+e1.getAction()+"--e2.getAction()-->"+e2.getAction()+"--distanceX-->"+distanceX+"--distanceY-->"+distanceY);

        invalidate(); // 重新绘制
        return false;
    }

    @Override
    public void onLongPress(MotionEvent e) {

    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        if (rect == null) {
            Log.i(TAG, "onFling, rect is null");
            return true;
        }
        if (scroller == null) {
            Log.i(TAG, "onFling, scroller is null");
            return true;
        }

        Log.i(TAG, "onFling, e1.getAction()-->"+e1.getAction()+"--e2.getAction()-->"+e2.getAction()+"--velocityX-->"+velocityX+"--velocityY-->"+velocityY);
        scroller.fling(rect.left, rect.top, (int) -velocityX, (int) -velocityY, 0, imageWidth - measuredWidth, 0, imageHeight - measuredHeight);
        return false;
    }

    /**
     * 在滚动过程中的处理
     */
    @Override
    public void computeScroll() {
        Log.i(TAG,"computeScroll()-->");
        if (scroller.isFinished()) { // 如果已经停止滚动，那么就不处理
            return;
        }
        Log.i(TAG,"scroller.getCurrX()-->"+scroller.getCurrX()+"--scroller.getCurrY()"+scroller.getCurrY());
        if (scroller.computeScrollOffset()) {
            rect.left = scroller.getCurrX();
            rect.top = scroller.getCurrY();
            rect.right = rect.left + measuredWidth;
            rect.bottom = rect.top + measuredHeight;
            invalidate();
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // 手势和触摸事件绑定
        return detector.onTouchEvent(event);
    }

}