package core.utils;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;

public class DeviceUtils {

    public static double getDiagonalScreenDevice(Activity context) {
        DisplayMetrics dm = new DisplayMetrics();
        context.getWindowManager().getDefaultDisplay().getMetrics(dm);
        int width=dm.widthPixels;
        int height=dm.heightPixels;
        int dens=dm.densityDpi;
        double wi=(double)width/(double)dens;
        double hi=(double)height/(double)dens;
        double x = Math.pow(wi,2);
        double y = Math.pow(hi,2);
        double screenInches =  Math.sqrt(x+y);

        Log.e(CoreConstants.TAG, "Screen diagonal size : " + screenInches);
        return screenInches;
    }

    public static int[] getScreenDimension(Activity context) {
        int dim[] = new int[2];
        DisplayMetrics displaymetrics = new DisplayMetrics();
        context.getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        int height = displaymetrics.heightPixels;
        int width = displaymetrics.widthPixels;
        dim[0] = width; dim[1] = height;

        Log.e(CoreConstants.TAG, "Screen dimension : " + width + " x " + height);
        return dim;
    }

    public static boolean isTabletDevice(Context activityContext) {
        try {
            DisplayMetrics dm = activityContext.getResources().getDisplayMetrics();
            float screenWidth = dm.widthPixels / dm.xdpi;
            float screenHeight = dm.heightPixels / dm.ydpi;
            double size = Math.sqrt(Math.pow(screenWidth, 2) + Math.pow(screenHeight, 2));

            Log.e(CoreConstants.TAG, "Is tablet device : " + (size >= 6));
            return size >= 6;
        } catch (Exception t) {
            Log.e(CoreConstants.TAG, "Failed to compute screen size", t);
            return false;
        }
    }

    public static String getDisplayDensity(Activity context) {
        DisplayMetrics dm = new DisplayMetrics();
        context.getWindowManager().getDefaultDisplay().getMetrics(dm);
        int dens=dm.densityDpi;

		/*
		ldpi (low) ~120dpi
		mdpi (medium) ~160dpi
		hdpi (high) ~240dpi
		xhdpi (extra-high) ~320dpi
		xxhdpi (extra-extra-high) ~480dpi
		xxxhdpi (extra-extra-extra-high) ~640dpi
		 */
        String dispDens = "ldpi";

        if(dens < 160)
            dispDens = "ldpi";
        else if(dens < 240)
            dispDens = "mdpi";
        else if(dens < 320)
            dispDens = "hdpi";
        else if(dens < 480)
            dispDens = "xhdpi";
        else if(dens < 640)
            dispDens ="xxhdpi";
        else dispDens = "xxxhdpi";

        Log.e(CoreConstants.TAG, "Display density : " + dispDens);

        return dispDens;
    }

    public static int convertDpToPixels(Context context, float dp) {
        if (context != null) {
            Resources resources = context.getResources();
            return (int) TypedValue.applyDimension(
                    TypedValue.COMPLEX_UNIT_DIP,
                    dp,
                    resources.getDisplayMetrics()
            );
        } else {
            return 0;
        }
    }

    public static int convertPixelsToDp(Context context, float pixels) {
        float scale = context.getResources().getDisplayMetrics().density;
        return (int) (0.5 + (pixels / scale));
    }
}
