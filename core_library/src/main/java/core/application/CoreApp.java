package core.application;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;
import android.text.TextUtils;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;

import core.utils.CoreConstants;

public class CoreApp extends Application {

    private int[] dimensions = new int[2];
    private String displayDensity;
    private double diagonalScreenDevice;
    private boolean deviceTipology;
    private static CoreApp instance;


    private RequestQueue mRequestQueue;


    public static synchronized CoreApp getInstance() {
        if(instance == null) {
            instance = new CoreApp();
        }
        return instance;
    }

    @Override
    protected void attachBaseContext(Context context) {
        super.attachBaseContext(context);
        MultiDex.install(this);
    }

    public void onCreate() {
        super.onCreate();
        instance = this;

        initImageLoader(getApplicationContext());
    }

    //IMAGELOADER
    private static void initImageLoader(Context context) {
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context)
                .threadPriority(Thread.NORM_PRIORITY - 2)
                .denyCacheImageMultipleSizesInMemory()
                .diskCacheFileNameGenerator(new Md5FileNameGenerator())
                .diskCacheSize(100 * 1024 * 1024) // 50 Mb
                .memoryCacheSize(10 * 1024 * 1024)
                .tasksProcessingOrder(QueueProcessingType.LIFO)
                .build();
        ImageLoader.getInstance().init(config);
    }

    public void setDiagonal(double diagonalScreenDevice) {
        this.diagonalScreenDevice = diagonalScreenDevice;
    }

    public double getDiagonal() {
        return diagonalScreenDevice;
    }

    public void setDimensions(int[] dimensions) {
        this.dimensions = dimensions;
    }

    public void setDisplayDensity(String displayDensity) {
        this.displayDensity = displayDensity;
    }

    public void setTabletTipology(boolean deviceTipology) {
        this.deviceTipology = deviceTipology;
    }

    public boolean isTabletTipology() {
        return deviceTipology;
    }



    /** volley **/
    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(getApplicationContext());
        }
        return mRequestQueue;
    }

    public <T> void addToRequestQueue(Request<T> req, String tag) {
        req.setTag(TextUtils.isEmpty(tag) ? CoreConstants.TAG : tag);
        getRequestQueue().add(req);
    }

    public <T> void addToRequestQueue(Request<T> req) {
        req.setTag(CoreConstants.TAG);
        getRequestQueue().add(req);
    }

    public void cancelPendingRequests(Object tag) {
        if (mRequestQueue != null) {
            mRequestQueue.cancelAll(tag);
        }
    }
}
