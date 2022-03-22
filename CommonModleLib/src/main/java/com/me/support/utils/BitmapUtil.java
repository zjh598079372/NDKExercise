package com.me.support.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.media.ExifInterface;
import android.util.Log;
import android.widget.ImageView;
import android.widget.ScrollView;

import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.load.model.LazyHeaders;
import com.bumptech.glide.request.RequestOptions;
import com.qw.soul.permission.SoulPermission;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.SoftReference;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/// <summary>
/// Bitmap工具类
/// </summary>
@SuppressWarnings("deprecation")
public class BitmapUtil {

    private static String TAG = BitmapUtil.class.getSimpleName();

    /**
     * 以RGB_565的设定读取外部图片
     *
     * @param imageUrl 外部资源路径
     * @param quality  需求质量（最大需求边长）
     * @return 按照设定解码后的Bitmap
     **/
    public static Bitmap loadOuterImage(String imageUrl, int quality) {
        Options options = new Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(imageUrl, options);
        options.inPreferredConfig = Config.RGB_565;
        if (quality != 0) {
            options.inSampleSize = calculateInSampleSize(options, quality,
                    quality);
        }
        options.inDither = false;
        options.inPurgeable = true;
        options.inJustDecodeBounds = false;
        return new SoftReference<Bitmap>(BitmapFactory.decodeFile(imageUrl,
                options)).get();
    }

    public static Bitmap loadOuterImage_ARGB_8888(String imageUrl, int quality) {
        Options options = new Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(imageUrl, options);
        options.inPreferredConfig = Config.ARGB_8888;
        if (quality != 0) {
            options.inSampleSize = calculateInSampleSize(options, quality,
                    quality);
        }
        options.inDither = false;
        options.inPurgeable = true;
        options.inJustDecodeBounds = false;
        return new SoftReference<Bitmap>(BitmapFactory.decodeFile(imageUrl,
                options)).get();
    }

    /**
     * 以ARGB_8888的设定读取资源文件中的图片
     *
     * @param res     资源ID
     * @param quality 需求质量（最大需求边长）
     * @param context 当前上下文
     * @return 按照设定解码后的Bitmap
     **/
    public static Bitmap loadResourceImage(int res, int quality, Context context) {
        Options options = new Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(context.getResources(), res, options);
        options.inPreferredConfig = Config.ARGB_8888;
        options.inDither = false;
        options.inPurgeable = true;
        if (quality != 0) {
            options.inSampleSize = calculateInSampleSize(options, quality,
                    quality);
        }
        options.inJustDecodeBounds = false;
        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(),
                res, options);
        return bitmap;
    }

    /**
     * 缩放Bitmap到指定大小
     *
     * @param source Bitmap
     * @param size   目标大小
     * @return 缩放后的Bitmap
     **/
    public static Bitmap scaleBitmap(Bitmap source, int size) {
        Bitmap target = Bitmap.createBitmap(size, size, source.getConfig());
        Canvas canvas = new Canvas(target);
        canvas.drawBitmap(source, null, new Rect(0, 0, target.getWidth(),
                target.getHeight()), null);
        return target;
    }

    /**
     * 等比缩放图片
     *
     * @param resId
     * @param newWidth
     * @param newHeight
     * @return
     */
    public static Bitmap zoomImg(Context context, int resId, int newWidth, int newHeight) {
        // 获得图片的宽高
        Bitmap bm = BitmapFactory.decodeResource(context.getResources(), resId);
        int width = bm.getWidth();
        int height = bm.getHeight();
        // 计算缩放比例
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        // 取得想要缩放的matrix参数
        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);
        // 得到新的图片
        Bitmap newbm = Bitmap.createBitmap(bm, 0, 0, width, height, matrix, true);
        Log.i(TAG, "zoomImg: " + scaleWidth + "\n" + scaleHeight + "\n" + width + "\n" + height + "\n" + newWidth + "\n" + newHeight);
        return newbm;
    }


    /**
     * 以RGB_565的设定读取外部图片
     *
     * @param imageUrl 外部资源路径
     * @param scale    需求缩小倍数（2的次方数）
     * @return 按照设定解码后的Bitmap
     **/
    public static Bitmap loadOuterImageByScale(String imageUrl, int scale) {
        Options options = new Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(imageUrl, options);
        options.inPreferredConfig = Config.RGB_565;
        if (scale != 0) {
            options.inSampleSize = scale;
        }
        options.inDither = false;
        options.inPurgeable = true;
        options.inJustDecodeBounds = false;
        return new SoftReference<Bitmap>(BitmapFactory.decodeFile(imageUrl, options)).get();
    }

    /**
     * 计算需求缩放倍率
     *
     * @param op        BitmapFactory设定
     * @param reqWidth  需求宽度
     * @param reqheight 需求高度
     * @return 缩放倍率（2的次方数）
     **/
    private static int calculateInSampleSize(Options op,
                                             int reqWidth, int reqheight) {
        int originalWidth = op.outWidth;
        int originalHeight = op.outHeight;
        int inSampleSize = 1;
        if (originalWidth > reqWidth || originalHeight > reqheight) {
            int halfWidth = originalWidth / 2;
            int halfHeight = originalHeight / 2;
            while ((halfWidth / inSampleSize > reqWidth)
                    && (halfHeight / inSampleSize > reqheight)) {
                inSampleSize *= 2;
            }
        }
        return inSampleSize;
    }

    /**
     * 获取资源图片宽或高
     *
     * @param imageUrl 图片路径
     * @param HorW     宽或高（H、W）
     * @return 宽或高
     **/
    public static int getResWH(String imageUrl, String HorW) {
        Options options = new Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(imageUrl, options);
        return (HorW == "H" || HorW == "h") ? options.outHeight
                : (HorW == "W" || HorW == "w") ? options.outWidth : 0;
    }

    /**
     * 获取资源图片宽或高
     *
     * @param resId   资源ID
     * @param context 上下文
     * @param HorW    宽或高（H、W）
     * @return 宽或高
     **/
    public static int getResWH(int resId, Context context, String HorW) {
        Options options = new Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(context.getResources(), resId, options);
        return (HorW == "H" || HorW == "h") ? options.outHeight
                : (HorW == "W" || HorW == "w") ? options.outWidth : 0;
    }

    /**
     * 获取资源图片宽或高
     *
     * @param imageUrl 图片路径
     * @return 宽或高
     **/
    public static int getMinOfWH(String imageUrl) {
        Options options = new Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(imageUrl, options);
        return (options.outHeight < options.outWidth) ? options.outHeight
                : (options.outHeight > options.outWidth) ? options.outWidth : 0;
    }

    /**
     * 保存Bitmap到本地
     *
     * @param bitmap  bitmap对象
     * @param dir     保存目录
     * @param imgName 图片名称
     **/
    public static void saveBitmap(Bitmap bitmap, String dir, String imgName) {
        File f = new File(dir, imgName);
        if (f.exists()) {
            f.delete();
        }
        try {
            FileOutputStream out = new FileOutputStream(f);
            bitmap.compress(Bitmap.CompressFormat.PNG, 90, out);
            out.flush();
            out.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 保存Bitmap到本地
     *
     * @param bitmap bitmap对象
     * @param path   保存路径
     **/
    public static void saveBitmap(final Bitmap bitmap, String path, final Bitmap.CompressFormat format) {
        final File f = new File(path);
        if (f.exists()) {
            f.delete();
        }
        try {
            FileOutputStream out = new FileOutputStream(f);
            bitmap.compress(format, 90, out);
            out.flush();
            out.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * 获取图片的旋转角度。
     * 只能通过原始文件获取，如果已经进行过bitmap操作无法获取。
     */
    public static int getRotateDegree(String path) {
        int result = 0;
        try {
            ExifInterface exif = new ExifInterface(path);
            int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
            Log.e("orientation=", ""+ orientation);
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    result = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    result = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    result = 270;
                    break;
            }
        } catch (IOException ignore) {
            ignore.printStackTrace();
            return 0;
        }
        return result;
    }

    /**
     * 旋转图片
     *
     * @param bm
     * @param orientationDegree
     * @param needMirror
     * @return
     */
    public static Bitmap rotateBitmap(Bitmap bm, int orientationDegree, boolean needMirror) {
        try {
            Matrix m = new Matrix();
            m.setRotate(orientationDegree, (float) bm.getWidth() / 2, (float) bm.getHeight() / 2);
            Bitmap bm1 = Bitmap.createBitmap(bm, 0, 0, bm.getWidth(), bm.getHeight(), m, true);
            if (needMirror) {
                m = new Matrix();
                m.setScale(-1, 1);
                m.postTranslate(bm1.getWidth(), 0);
                bm1 = Bitmap.createBitmap(bm1, 0, 0, bm1.getWidth(), bm1.getHeight(), m, true);
            }
            return bm1;
        } catch (OutOfMemoryError ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public static int getNeedScaleX1440(String imageUrl) {
        int scale = 1;
        int l = Math.max(getResWH(imageUrl, "H"), getResWH(imageUrl, "W"));

        while (l > 1440) {
            scale = scale * 2;
            l = l / 2;
        }
        return scale;
    }

    public static int getNeedScaleX(String imageUrl, int needScaleX) {
        if (needScaleX < 0) {
            return 1;
        }
        int scale = 1;
        int l = Math.max(getResWH(imageUrl, "H"), getResWH(imageUrl, "W"));

        while (l > needScaleX) {
            scale = scale * 2;
            l = l / 2;
        }
        return scale;
    }


    /**
     * 获取资源图片宽或高
     *
     * @param resId   资源ID
     * @param context 上下文
     * @return 宽或高
     **/
    public static int getResWH(int resId, Context context) {
        Options options = new Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(context.getResources(), resId, options);
        return (options.outHeight < options.outWidth) ? options.outHeight
                : (options.outHeight > options.outWidth) ? options.outWidth : 0;
    }

    /**
     * 以RGB_565的设定读取资源文件中的图片
     *
     * @param context 当前上下文
     * @param resId   资源ID
     * @return 按照设定解码后的Bitmap
     **/
    public static Bitmap readBitMap(Context context, int resId) {
        Options opt = new Options();
        opt.inPreferredConfig = Config.RGB_565;
        opt.inPurgeable = true;
        opt.inInputShareable = true;
        InputStream is = context.getResources().openRawResource(resId);
        return new SoftReference<Bitmap>(BitmapFactory.decodeStream(is, null,
                opt)).get();
    }

    /**
     * 通过遮罩将正方形图片转为圆形图片
     *
     * @param source   源Bitmap
     * @param diameter 目标直径
     * @return 圆形图片Bitmap
     **/
    public static Bitmap createCircleImage(Bitmap source, int diameter) {
        final Paint paint = new Paint();
        paint.setAntiAlias(true);
        Bitmap target = Bitmap.createBitmap(diameter, diameter,
                Config.ARGB_8888);
        Canvas canvas = new Canvas(target);
        canvas.drawCircle(diameter / 2, diameter / 2, diameter / 2, paint);
        paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
        canvas.drawBitmap(source, 0, 0, paint);
        return target;
    }

    /**
     * 图片切圆角
     *
     * @param bitmap       源Bitmap
     * @param cornerRadius 圆角半径
     * @param type         0:切上边,1:切左边,2:切右边,3:切下边,其他:切四边
     * @return 圆角图片Bitmap
     **/
    public static Bitmap CutRoundCorner(int type, Bitmap bitmap,
                                        int cornerRadius) {
        try {
            final int width = bitmap.getWidth();
            final int height = bitmap.getHeight();

            Bitmap paintingBoard = Bitmap.createBitmap(width, height,
                    Config.ARGB_8888);
            Canvas canvas = new Canvas(paintingBoard);
            canvas.drawARGB(Color.TRANSPARENT, Color.TRANSPARENT,
                    Color.TRANSPARENT, Color.TRANSPARENT);

            final Paint paint = new Paint();
            paint.setAntiAlias(true);
            paint.setColor(Color.BLACK);

            if (0 == type) {
                clipTop(canvas, paint, cornerRadius, width, height);
            } else if (1 == type) {
                clipLeft(canvas, paint, cornerRadius, width, height);
            } else if (2 == type) {
                clipRight(canvas, paint, cornerRadius, width, height);
            } else if (3 == type) {
                clipBottom(canvas, paint, cornerRadius, width, height);
            } else {
                clipAll(canvas, paint, cornerRadius, width, height);
            }

            paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
            final Rect src = new Rect(0, 0, width, height);
            final Rect dst = src;
            canvas.drawBitmap(bitmap, src, dst, paint);
            return paintingBoard;
        } catch (Exception exp) {
            return bitmap;
        }
    }

    /**
     * 左边切圆角
     *
     * @param canvas 画布
     * @param paint  画笔
     * @param offset 圆角半径
     * @param width  画布宽
     * @param height 画布高
     */
    private static void clipLeft(final Canvas canvas, final Paint paint,
                                 int offset, int width, int height) {
        final Rect block = new Rect(offset, 0, width, height);
        canvas.drawRect(block, paint);
        final RectF rectF = new RectF(0, 0, offset * 2, height);
        canvas.drawRoundRect(rectF, offset, offset, paint);
    }

    /**
     * 右边切圆角
     *
     * @param canvas 画布
     * @param paint  画笔
     * @param offset 圆角半径
     * @param width  画布宽
     * @param height 画布高
     */
    private static void clipRight(final Canvas canvas, final Paint paint,
                                  int offset, int width, int height) {
        final Rect block = new Rect(0, 0, width - offset, height);
        canvas.drawRect(block, paint);
        final RectF rectF = new RectF(width - offset * 2, 0, width, height);
        canvas.drawRoundRect(rectF, offset, offset, paint);
    }

    /**
     * 顶边切圆角
     *
     * @param canvas 画布
     * @param paint  画笔
     * @param offset 圆角半径
     * @param width  画布宽
     * @param height 画布高
     */
    private static void clipTop(final Canvas canvas, final Paint paint,
                                int offset, int width, int height) {
        final Rect block = new Rect(0, offset, width, height);
        canvas.drawRect(block, paint);
        final RectF rectF = new RectF(0, 0, width, offset * 2);
        canvas.drawRoundRect(rectF, offset, offset, paint);
    }

    /**
     * 底边切圆角
     *
     * @param canvas 画布
     * @param paint  画笔
     * @param offset 圆角半径
     * @param width  画布宽
     * @param height 画布高
     */
    private static void clipBottom(final Canvas canvas, final Paint paint,
                                   int offset, int width, int height) {
        final Rect block = new Rect(0, 0, width, height - offset);
        canvas.drawRect(block, paint);
        final RectF rectF = new RectF(0, height - offset * 2, width, height);
        canvas.drawRoundRect(rectF, offset, offset, paint);
    }

    /**
     * 四边切圆角
     *
     * @param canvas 画布
     * @param paint  画笔
     * @param offset 圆角半径
     * @param width  画布宽
     * @param height 画布高
     */
    private static void clipAll(final Canvas canvas, final Paint paint,
                                int offset, int width, int height) {
        final RectF rectF = new RectF(0, 0, width, height);
        canvas.drawRoundRect(rectF, offset, offset, paint);
    }

    /**
     * 设置水印图片在左上角
     *
     * @param src
     * @param watermark
     * @param paddingLeft
     * @param paddingTop
     * @return
     */
    public static Bitmap createWaterMaskLeftTop(Bitmap src, Bitmap watermark, int paddingLeft, int paddingTop) {
        return createWaterMaskBitmap(src, watermark, paddingLeft, paddingTop);
    }

    /**
     * 设置水印图片在右下角
     *
     * @param src
     * @param watermark
     * @param paddingRight
     * @param paddingBottom
     * @return
     */
    public static Bitmap createWaterMaskRightBottom(Bitmap src, Bitmap watermark, int paddingRight, int paddingBottom) {
        return createWaterMaskBitmap(src, watermark, src.getWidth() - watermark.getWidth() - paddingRight, src.getHeight() - watermark.getHeight() - paddingBottom);
    }

    /**
     * 设置水印图片到右上角
     *
     * @param src
     * @param watermark
     * @param paddingRight
     * @param paddingTop
     * @return
     */
    public static Bitmap createWaterMaskRightTop(Bitmap src, Bitmap watermark, int paddingRight, int paddingTop) {
        return createWaterMaskBitmap(src, watermark, src.getWidth() - watermark.getWidth() - paddingRight, paddingTop);
    }

    /**
     * 设置水印图片到左下角
     *
     * @param src
     * @param watermark
     * @param paddingLeft
     * @param paddingBottom
     * @return
     */
    public static Bitmap createWaterMaskLeftBottom(Bitmap src, Bitmap watermark, int paddingLeft, int paddingBottom) {
        return createWaterMaskBitmap(src, watermark, paddingLeft, src.getHeight() - watermark.getHeight() - paddingBottom);
    }

    /**
     * 设置水印图片到中间
     *
     * @param src
     * @param watermark
     * @return
     */
    public static Bitmap createWaterMaskCenter(Bitmap src, Bitmap watermark) {
        return createWaterMaskBitmap(src, watermark, (src.getWidth() - watermark.getWidth()) / 2, (src.getHeight() - watermark.getHeight()) / 2);
    }

    private static Bitmap createWaterMaskBitmap(Bitmap src, Bitmap watermark, int paddingLeft, int paddingTop) {
        if (src == null) {
            return null;
        }
        int width = src.getWidth();
        int height = src.getHeight();
        //创建一个bitmap
        Bitmap newb = Bitmap.createBitmap(width, height, Config.ARGB_8888);// 创建一个新的和SRC长度宽度一样的位图
        //将该图片作为画布
        Canvas canvas = new Canvas(newb);
        //在画布 0，0坐标上开始绘制原始图片
        canvas.drawBitmap(src, 0, 0, null);
        //在画布上绘制水印图片
        canvas.drawBitmap(watermark, paddingLeft, paddingTop, null);
        // 保存
        canvas.save();
        // 存储
        canvas.restore();
        return newb;
    }

    /**
     * @param bitmap     原图
     * @param edgeLength 希望得到的正方形部分的边长
     * @return 缩放截取正中部分后的位图。
     */
    public static Bitmap getCenterSquareScaleBitmap(Bitmap bitmap,
                                                    int edgeLength) {
        if (null == bitmap || edgeLength <= 0) {
            return null;
        }

        Bitmap result = bitmap;
        int widthOrg = bitmap.getWidth();
        int heightOrg = bitmap.getHeight();

        if (widthOrg >= edgeLength && heightOrg >= edgeLength) {
            // 压缩到一个最小长度是edgeLength的bitmap
            int longerEdge = (int) (edgeLength * Math.max(widthOrg, heightOrg) / Math
                    .min(widthOrg, heightOrg));
            int scaledWidth = widthOrg > heightOrg ? longerEdge : edgeLength;
            int scaledHeight = widthOrg > heightOrg ? edgeLength : longerEdge;
            Bitmap scaledBitmap;

            try {
                scaledBitmap = Bitmap.createScaledBitmap(bitmap, scaledWidth,
                        scaledHeight, true);
            } catch (Exception e) {
                return null;
            }

            // 从图中截取正中间的正方形部分。
            int xTopLeft = (scaledWidth - edgeLength) / 2;
            int yTopLeft = (scaledHeight - edgeLength) / 2;

            try {
                result = Bitmap.createBitmap(scaledBitmap, xTopLeft, yTopLeft,
                        edgeLength, edgeLength);
                scaledBitmap.recycle();
            } catch (Exception e) {
                return null;
            }
        }

        return result;
    }

    /**
     * 获取整个scrollview的截屏
     *
     * @param scrollView 需要截屏的ScrollView
     * @return 绘制出来的Bitmap
     */
    public static Bitmap getBitmapFromScrollView(ScrollView scrollView) {
        int h = 0;
        Bitmap bitmap = null;
        // 获取scrollview实际高度
        for (int i = 0; i < scrollView.getChildCount(); i++) {
            h += scrollView.getChildAt(i).getHeight();
            scrollView.getChildAt(i).setBackgroundColor(
                    Color.parseColor("#ffffff"));
        }
        // 创建对应大小的bitmap
        bitmap = Bitmap.createBitmap(scrollView.getWidth(), h,
                Config.RGB_565);
        final Canvas canvas = new Canvas(bitmap);
        scrollView.draw(canvas);
        return bitmap;
    }

    /**
     * 压缩图片
     *
     * @param sourceBitmap 源Bitmap
     * @return 压缩后Bitmap
     */
    public static Bitmap compressImage(Bitmap sourceBitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        sourceBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        int options = 100;
        while (baos.toByteArray().length / 1024 > 100) {
            baos.reset();
            sourceBitmap.compress(Bitmap.CompressFormat.JPEG, options, baos);
            options -= 10;
        }
        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());
        Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);
        return bitmap;
    }



    /**
     * 添加时间水印
     *
     * @param src
     */
    public static Bitmap addTimeWatermark(@Nullable Bitmap src) {
        // 获取原始图片与水印图片的宽与高
        int w = src.getWidth();
        int h = src.getHeight();
        Bitmap newBitmap = Bitmap.createBitmap(w, h, Config.ARGB_8888);
        Canvas mCanvas = new Canvas(newBitmap);
        // 往位图中开始画入src原始图片
        mCanvas.drawBitmap(src, 0, 0, null);
        //添加文字
        Paint textPaint = new Paint();
        String time = getSysTime();
        textPaint.setColor(Color.WHITE);
        textPaint.setTextSize(20);

        mCanvas.drawText(time, 20, h - 24, textPaint);
        mCanvas.save();
        mCanvas.restore();
        return newBitmap;
    }

    /**
     * 获得系统时间
     *
     * @return String：格式为"yyyy-MM-dd HH:mm:ss"的日期和时间
     **/
    public static String getSysTime() {
        Date date = new Date();
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String time = format.format(date);
        return time;
    }

    public void loadVideoImageByUrl(String url, ImageView img){
        GlideUrl glideUrl = new GlideUrl(url, new LazyHeaders.Builder()
                .addHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 " +
                        "(KHTML, like Gecko) Chrome/86.0.4240.198 Safari/537.36").build());
        Glide.with(SoulPermission.getInstance().getTopActivity())
                .setDefaultRequestOptions(
                        new RequestOptions()
                                .frame(0)
                )
                .load(glideUrl)
                .into(img);
    }

}
