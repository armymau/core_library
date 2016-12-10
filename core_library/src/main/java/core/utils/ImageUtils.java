package core.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;


public class ImageUtils {

    public static File convertBitmapToFile(Context context, Bitmap bitmap) {
        //create a file to write bitmap data
        File f = null;
        try {
            f = new File(context.getCacheDir(), "tempFile");
            f.createNewFile();

            //Convert bitmap to byte array
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 0, bos);
            byte[] bitmapdata = bos.toByteArray();

            //write the bytes in file
            FileOutputStream fos = new FileOutputStream(f);
            fos.write(bitmapdata);
            fos.flush();
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return f;
    }

    public static Bitmap drawableToBitmap(Drawable drawable) {
        if (drawable instanceof BitmapDrawable) {
            return ((BitmapDrawable) drawable).getBitmap();
        }
        Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);

        return bitmap;
    }

    public static Bitmap getCircularBitmap(Bitmap bitmap) {
        Bitmap output = null;
        try {
            if (bitmap != null) {
                int measure;
                int x = bitmap.getWidth();
                int y = bitmap.getHeight();

                if (x < y) {
                    measure = x;
                } else {
                    measure = y;
                }

                output = Bitmap.createBitmap(measure, measure, Bitmap.Config.ARGB_8888);
                Canvas canvas = new Canvas(output);

                final int color = 0xff424242;
                final Paint paint = new Paint();
                final Rect rect = new Rect(0, 0, measure, measure);

                paint.setAntiAlias(true);
                canvas.drawARGB(0, 0, 0, 0);
                paint.setColor(color);
                canvas.drawCircle(measure / 2, measure / 2, measure / 2, paint);
                paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
                canvas.drawBitmap(bitmap, rect, rect, paint);
            }
        } catch (Exception e) {
            e.printStackTrace();
            output = null;
        }
        return output;
    }
}
