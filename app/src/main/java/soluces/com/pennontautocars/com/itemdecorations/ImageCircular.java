package soluces.com.pennontautocars.com.itemdecorations;

import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.Drawable;

/**
 * Created by RAYA on 25/10/2016.
 */

public class ImageCircular extends Drawable
{
    private final Bitmap mBitmap;
    private final int mBitmapHeight;
    private final int mBitmapWidth;
    private final Paint mPaint;
    private final RectF mRectF;

    public ImageCircular(Bitmap paramBitmap)
    {
        this.mBitmap = paramBitmap;
        this.mRectF = new RectF();
        this.mPaint = new Paint();
        this.mPaint.setAntiAlias(true);
        this.mPaint.setDither(true);
        BitmapShader localBitmapShader = new BitmapShader(paramBitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
        this.mPaint.setShader(localBitmapShader);
        this.mBitmapWidth = this.mBitmap.getWidth();
        this.mBitmapHeight = this.mBitmap.getHeight();
    }

    public void draw(Canvas paramCanvas)
    {
        paramCanvas.drawOval(this.mRectF, this.mPaint);
    }

    public Bitmap getBitmap()
    {
        return this.mBitmap;
    }

    public int getIntrinsicHeight()
    {
        return this.mBitmapHeight;
    }

    public int getIntrinsicWidth()
    {
        return this.mBitmapWidth;
    }

    public int getOpacity()
    {
        return -3;
    }

    protected void onBoundsChange(Rect paramRect)
    {
        super.onBoundsChange(paramRect);
        this.mRectF.set(paramRect);
    }

    public void setAlpha(int paramInt)
    {
        if (this.mPaint.getAlpha() != paramInt)
        {
            this.mPaint.setAlpha(paramInt);
            invalidateSelf();
        }
    }

    public void setAntiAlias(boolean paramBoolean)
    {
        this.mPaint.setAntiAlias(paramBoolean);
        invalidateSelf();
    }

    public void setColorFilter(ColorFilter paramColorFilter)
    {
        this.mPaint.setColorFilter(paramColorFilter);
    }

    public void setDither(boolean paramBoolean)
    {
        this.mPaint.setDither(paramBoolean);
        invalidateSelf();
    }

    public void setFilterBitmap(boolean paramBoolean)
    {
        this.mPaint.setFilterBitmap(paramBoolean);
        invalidateSelf();
    }
}
