package core_kt.application

import android.app.Application
import android.content.Context
import android.support.multidex.MultiDex
import android.text.TextUtils

import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.Volley
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator
import com.nostra13.universalimageloader.core.ImageLoader
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration
import com.nostra13.universalimageloader.core.assist.QueueProcessingType
import core_kt.utils.TAG

open class CoreApp : Application() {

    var dimensions = IntArray(2)
    lateinit var displayDensity: String
    var diagonalScreenDevice: Double = 0.toDouble()
    var isTabletTipology: Boolean = false

    lateinit var mRequestQueue: RequestQueue


    class CoreApp private constructor(context: Context) {
        init {
            println("init complete")
        }
        companion object : SingletonHolder<CoreApp, Context>(::CoreApp)
    }

    /*
    companion object CoreAppSingleton {
        init {
            println("init complete")
        }
    }
    */

    /*
    @Synchronized
    fun getInstance(): CoreAppSingleton {
        //instance = CoreAppSingleton
        return instance
    }
    */

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

    open class SingletonHolder<out T, in A>(creator: (A) -> T) {
        private var creator: ((A) -> T)? = creator
        @Volatile private var instance: T? = null

        fun getInstance(arg: A): T {
            val i = instance
            if (i != null) {
                return i
            }

            return synchronized(this) {
                val i2 = instance
                if (i2 != null) {
                    i2
                } else {
                    val created = creator!!(arg)
                    instance = created
                    creator = null
                    created
                }
            }
        }
    }
}
