package core_kt.connection.volley

import android.content.Context
import com.android.volley.RequestQueue
import com.android.volley.toolbox.BasicNetwork
import com.android.volley.toolbox.DiskBasedCache
import com.android.volley.toolbox.HurlStack


class CoreVolleyNetwork private constructor(context: Context) {

    var mRequestQueue: RequestQueue
    lateinit var mInstance: CoreVolleyNetwork
    val mCtx: Context = context

    init {
        mRequestQueue = getRequestQueue()
    }

    @Synchronized
    fun getInstance() : CoreVolleyNetwork {
        mInstance = CoreVolleyNetwork(mCtx)
        return mInstance
    }

    fun getRequestQueue(): RequestQueue {
        val maxMemory = (Runtime.getRuntime().maxMemory() / 1024).toInt()
        val cacheSize = maxMemory / 4

        val cache = DiskBasedCache(mCtx.cacheDir, cacheSize)
        val network = BasicNetwork(HurlStack())
        mRequestQueue = RequestQueue(cache, network)

        mRequestQueue.start()

        return mRequestQueue
    }
}
