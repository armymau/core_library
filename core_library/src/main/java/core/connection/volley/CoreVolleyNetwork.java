package core.connection.volley;

import android.content.Context;

import com.android.volley.Cache;
import com.android.volley.Network;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.DiskBasedCache;
import com.android.volley.toolbox.HurlStack;


public class CoreVolleyNetwork {

    private static CoreVolleyNetwork mInstance;
    private static Context mCtx;
    private RequestQueue mRequestQueue;

    private CoreVolleyNetwork(Context context) {
        mCtx = context;
        mRequestQueue = getRequestQueue();
    }

    public static synchronized CoreVolleyNetwork getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new CoreVolleyNetwork(context);
        }
        return mInstance;
    }

    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);
            final int cacheSize = maxMemory / 4;

            Cache cache = new DiskBasedCache(mCtx.getCacheDir(), cacheSize);
            Network network = new BasicNetwork(new HurlStack());
            mRequestQueue = new RequestQueue(cache, network);

            mRequestQueue.start();
        }
        return mRequestQueue;
    }
}
