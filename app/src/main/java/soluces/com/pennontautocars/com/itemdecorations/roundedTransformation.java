package soluces.com.pennontautocars.com.itemdecorations;

import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;

import com.squareup.picasso.Transformation;

/**
 * Created by Root on 23/05/2016.
 */
public class roundedTransformation implements Transformation {
    @Override
    public Bitmap transform(Bitmap bitmap) {


        int size = Math.min(bitmap.getWidth(),bitmap.getHeight());

        int x =(bitmap.getWidth()- size)/2;
        int y =(bitmap.getHeight()-size)/2;
        Bitmap squaredBitmap = Bitmap.createBitmap(bitmap,x,y,size,size);
        if(squaredBitmap!=bitmap){
            bitmap.recycle();
        }

        Bitmap bitmapfinal = Bitmap.createBitmap(size,size, Bitmap.Config.ARGB_8888);
        Canvas canvas= new Canvas(bitmapfinal);
        Paint paint = new Paint();
        BitmapShader shader = new BitmapShader(squaredBitmap,BitmapShader.TileMode.CLAMP,BitmapShader.TileMode.CLAMP);
        paint.setShader(shader);
        paint.setAntiAlias(true);



        float r = size/2f;
        canvas.drawCircle(r,r,r,paint);
        squaredBitmap.recycle();
        return bitmapfinal;
    }

    @Override
    public String key() {
        return "circle";
    }
}
