package core_kt.utils

import android.app.Activity
import android.content.Context
import android.util.DisplayMetrics
import android.util.Log
import android.util.TypedValue
import kotlin.math.pow
import kotlin.math.sqrt

const val TABLET_MIN_DIAGONAL_DEVICE = 6.79

fun getDiagonalScreenDevice(context: Activity): Double {
    val dm = context.resources.displayMetrics
    val width = dm.widthPixels
    val height = dm.heightPixels
    val dens = dm.densityDpi
    val wi = width.toDouble() / dens.toDouble()
    val hi = height.toDouble() / dens.toDouble()
    val x = wi.pow(2.0)
    val y = hi.pow(2.0)
    val screenInches = sqrt(x + y)

    Log.e(TAG, "Screen diagonal size : $screenInches")
    return screenInches
}

fun getScreenDimension(context: Activity): IntArray {
    val dim = IntArray(2)
    val displaymetrics = context.resources.displayMetrics
    val height = displaymetrics.heightPixels
    val width = displaymetrics.widthPixels
    dim[0] = width
    dim[1] = height

    Log.e(TAG, "Screen dimension : $width x $height")
    return dim
}

fun isTabletDevice(activityContext: Context): Boolean {
    return try {
        val dm = activityContext.resources.displayMetrics
        val screenWidth = dm.widthPixels / dm.xdpi
        val screenHeight = dm.heightPixels / dm.ydpi
        val size = sqrt(screenWidth.toDouble().pow(2.0) + screenHeight.toDouble().pow(2.0))

        Log.e(TAG, "Is tablet device : " + (size >= TABLET_MIN_DIAGONAL_DEVICE))
        size >= TABLET_MIN_DIAGONAL_DEVICE
    } catch (t: Exception) {
        Log.e(TAG, "Failed to compute screen size", t)
        false
    }
}

fun getDisplayDensity(context: Activity): String {
    val dm = context.resources.displayMetrics
    val dens = dm.densityDpi

    /*
    ldpi (low) ~120dpi
    mdpi (medium) ~160dpi
    hdpi (high) ~240dpi
    xhdpi (extra-high) ~320dpi
    xxhdpi (extra-extra-high) ~480dpi
    xxxhdpi (extra-extra-extra-high) ~640dpi
     */

    val dispDens: String = when {
        dens < 160 -> "ldpi"
        dens < 240 -> "mdpi"
        dens < 320 -> "hdpi"
        dens < 480 -> "xhdpi"
        dens < 640 -> "xxhdpi"
        else -> "xxxhdpi"
    }

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