package core_kt.application

import android.app.Application
import android.content.Context
import android.text.TextUtils
import androidx.multidex.MultiDex
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.Volley
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator
import com.nostra13.universalimageloader.core.ImageLoader
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration
import com.nostra13.universalimageloader.core.assist.QueueProcessingType
import core_kt.singleton.SingletonHolder
import core_kt.utils.TAG

open class CoreApp : Application {

    var dimensions = IntArray(2)
    lateinit var displayDensity: String
    var diagonalScreenDevice: Double = 0.toDouble()
    var isTabletTipology: Boolean = false

    lateinit var mRequestQueue: RequestQueue

    constructor()

    private constructor(context: Context) : this()

    fun getRequestQueue(): RequestQueue {
        mRequestQueue = Volley.newRequestQueue(applicationContext)
        return mRequestQueue
    }

    override fun attachBaseContext(context: Context) {
        super.attachBaseContext(context)
        MultiDex.install(this)
    }

    override fun onCreate() {
        super.onCreate()

        CoreApp(this)
        initImageLoader(applicationContext)
    }

    fun <T> addToRequestQueue(req: Request<T>, tag: String) {
        req.tag = if (TextUtils.isEmpty(tag)) TAG else tag
        mRequestQueue.add(req)
    }

    fun <T> addToRequestQueue(req: Request<T>) {
        req.tag = TAG
        mRequestQueue.add(req)
    }

    fun cancelPendingRequests(tag: Any) {
        mRequestQueue.cancelAll(tag)
    }

    //IMAGELOADER
    private fun initImageLoader(context: Context) {
        val config = ImageLoaderConfiguration.Builder(context)
                .threadPriority(Thread.NORM_PRIORITY - 2)
                .denyCacheImageMultipleSizesInMemory()
                .diskCacheFileNameGenerator(Md5FileNameGenerator())
                .diskCacheSize(100 * 1024 * 1024) // 50 Mb
                .memoryCacheSize(10 * 1024 * 1024)
                .tasksProcessingOrder(QueueProcessingType.LIFO)
                .build()
        ImageLoader.getInstance().init(config)
    }

    companion object : SingletonHolder<CoreApp, Context>(::CoreApp)
}
