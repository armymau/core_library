package core_kt.utils

import android.content.Context
import android.graphics.*
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream

fun convertBitmapToFile(context: Context, bitmap: Bitmap): File? {
    //create a file to write bitmap data
    var f: File? = null
    try {
        f = File(context.cacheDir, "tempFile")
        f.createNewFile()

        //Convert bitmap to byte array
        val bos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 0, bos)
        val bitmapdata = bos.toByteArray()

        //write the bytes in file
        val fos = FileOutputStream(f)
        fos.write(bitmapdata)
        fos.flush()
        fos.close()
    } catch (e: Exception) {
        e.printStackTrace()
    }

    return f
}

fun drawableToBitmap(drawable: Drawable): Bitmap {
    if (drawable is BitmapDrawable) {
        return drawable.bitmap
    }
    val bitmap = Bitmap.createBitmap(drawable.intrinsicWidth, drawable.intrinsicHeight, Bitmap.Config.ARGB_8888)
    val canvas = Canvas(bitmap)
    drawable.setBounds(0, 0, canvas.width, canvas.height)
    drawable.draw(canvas)

    return bitmap
}

fun getCircularBitmap(bitmap: Bitmap?): Bitmap? {
    var output: Bitmap? = null
    try {
        if (bitmap != null) {
            val measure: Int
            val x = bitmap.width
            val y = bitmap.height

            if (x < y) {
                measure = x
            } else {
                measure = y
            }

            output = Bitmap.createBitmap(measure, measure, Bitmap.Config.ARGB_8888)
            val canvas = Canvas(output!!)

            val color = -0xbdbdbe
            val paint = Paint()
            val rect = Rect(0, 0, measure, measure)

            paint.isAntiAlias = true
            canvas.drawARGB(0, 0, 0, 0)
            paint.color = color
            canvas.drawCircle((measure / 2).toFloat(), (measure / 2).toFloat(), (measure / 2).toFloat(), paint)
            paint.xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_IN)
            canvas.drawBitmap(bitmap, rect, rect, paint)
        }
    } catch (e: Exception) {
        e.printStackTrace()
        output = null
    }

    return output
}