package core_kt.utils

import android.app.Activity
import android.content.Context
import android.util.DisplayMetrics
import android.util.Log
import android.util.TypedValue

fun getDiagonalScreenDevice(context: Activity): Double {
    val dm = DisplayMetrics()
    context.windowManager.defaultDisplay.getMetrics(dm)
    val width = dm.widthPixels
    val height = dm.heightPixels
    val dens = dm.densityDpi
    val wi = width.toDouble() / dens.toDouble()
    val hi = height.toDouble() / dens.toDouble()
    val x = Math.pow(wi, 2.0)
    val y = Math.pow(hi, 2.0)
    val screenInches = Math.sqrt(x + y)

    Log.e(TAG, "Screen diagonal size : $screenInches")
    return screenInches
}

fun getScreenDimension(context: Activity): IntArray {
    val dim = IntArray(2)
    val displaymetrics = DisplayMetrics()
    context.windowManager.defaultDisplay.getMetrics(displaymetrics)
    val height = displaymetrics.heightPixels
    val width = displaymetrics.widthPixels
    dim[0] = width
    dim[1] = height

    Log.e(TAG, "Screen dimension : $width x $height")
    return dim
}

fun isTabletDevice(activityContext: Context): Boolean {
    try {
        val dm = activityContext.resources.displayMetrics
        val screenWidth = dm.widthPixels / dm.xdpi
        val screenHeight = dm.heightPixels / dm.ydpi
        val size = Math.sqrt(Math.pow(screenWidth.toDouble(), 2.0) + Math.pow(screenHeight.toDouble(), 2.0))

        Log.e(TAG, "Is tablet device : " + (size >= 6.9))
        return size >= 6.9
    } catch (t: Exception) {
        Log.e(TAG, "Failed to compute screen size", t)
        return false
    }

}

fun getDisplayDensity(context: Activity): String {
    val dm = DisplayMetrics()
    context.windowManager.defaultDisplay.getMetrics(dm)
    val dens = dm.densityDpi

    /*
    ldpi (low) ~120dpi
    mdpi (medium) ~160dpi
    hdpi (high) ~240dpi
    xhdpi (extra-high) ~320dpi
    xxhdpi (extra-extra-high) ~480dpi
    xxxhdpi (extra-extra-extra-high) ~640dpi
     */
    var dispDens: String

    if (dens < 160)
        dispDens = "ldpi"
    else if (dens < 240)
        dispDens = "mdpi"
    else if (dens < 320)
        dispDens = "hdpi"
    else if (dens < 480)
        dispDens = "xhdpi"
    else if (dens < 640)
        dispDens = "xxhdpi"
    else
        dispDens = "xxxhdpi"

    Log.e(TAG, "Display density : $dispDens")

    return dispDens
}

fun convertDpToPixels(context: Context?, dp: Float): Int {
    if (context != null) {
        val resources = context.resources
        return TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                dp,
                resources.displayMetrics
        ).toInt()
    } else {
        return 0
    }
}

fun convertPixelsToDp(context: Context, pixels: Float): Int {
    val scale = context.resources.displayMetrics.density
    return (0.5 + pixels / scale).toInt()
}