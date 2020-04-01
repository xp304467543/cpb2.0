

package com.fenghuang.caipiaobao.widget.chat.bean;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.text.style.DynamicDrawableSpan;

import com.fenghuang.baselib.utils.ViewUtils;

import java.lang.ref.WeakReference;

/**
 * @author Hieu Rocker (rockerhieu@gmail.com)
 */
public class EmojiconSpan extends DynamicDrawableSpan {

    private final Context mContext;

    private final int mResourceId;

    private final int mSize;//图片大小

    private final int mTextSize;//文字大小

    private int mHeight;

    private int mWidth;

    private int mTop;

    private Drawable mDrawable;//图片对象

    private WeakReference<Drawable> mDrawableRef;

    public EmojiconSpan(Context context, int resourceId, int size, int alignment, int textSize) {
        super(alignment);
        mContext = context;
        mResourceId = resourceId;
        mWidth = mHeight = mSize = size;
        mTextSize = textSize;
    }

    public EmojiconSpan(Context context, int resourceId, int textSize) {
        this(context, resourceId, textSize, DynamicDrawableSpan.ALIGN_BOTTOM, textSize);

    }

    public Drawable getDrawable() {
        if (mDrawable == null) {
            try {
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//                    mDrawable = mContext.getResources().getDrawable(mResourceId, null);
//                } else {
//                    mDrawable = mContext.getResources().getDrawable(mResourceId);
//                }
                mDrawable = resizeImage(mResourceId, ViewUtils.INSTANCE.dp2px(60), ViewUtils.INSTANCE.dp2px(60));
                mHeight = mSize;
                mWidth = mHeight * mDrawable.getIntrinsicWidth() / mDrawable.getIntrinsicHeight();
                mTop = (mTextSize - mHeight) / 2;
                mDrawable.setBounds(10, mTop, mWidth+10, mTop + mHeight);
            } catch (Exception e) {
                // swallow
            }
        }
        return mDrawable;
    }

    @Override
    public void draw(Canvas canvas, CharSequence text, int start, int end, float x, int top, int y, int bottom, Paint paint) {
        //super.draw(canvas, text, start, end, x, top, y, bottom, paint);
        Drawable b = getCachedDrawable();
        canvas.save();

        int transY = bottom - b.getBounds().bottom;
        if (mVerticalAlignment == ALIGN_BASELINE) {
            transY = top + ((bottom - top) / 2) - ((b.getBounds().bottom - b.getBounds().top) / 2) - mTop;
        }
        canvas.translate(x, transY);
        b.draw(canvas);
        canvas.restore();
    }

    private Drawable getCachedDrawable() {
        if (mDrawableRef == null || mDrawableRef.get() == null) {
            mDrawableRef = new WeakReference<>(getDrawable());
        }
        return mDrawableRef.get();
    }


    //使用Bitmap加Matrix来缩放
    public Drawable resizeImage(Integer resId, int w, int h) {
        Resources res = mContext.getResources();
        Bitmap bmp = BitmapFactory.decodeResource(res, resId);
        Bitmap BitmapOrg = bmp;
        int width = BitmapOrg.getWidth();
        int height = BitmapOrg.getHeight();
        int newWidth = w;
        int newHeight = h;
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);
        // if you want to rotate the Bitmap
        // matrix.postRotate(45);
        Bitmap resizedBitmap = Bitmap.createBitmap(BitmapOrg, 0, 0, width,
                height, matrix, true);

        return new BitmapDrawable(resizedBitmap);
    }

}