package core_kt.utils

import android.annotation.SuppressLint
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.os.Build

@SuppressLint("MissingPermission")
fun isNetworkAvailable(context: Context): Boolean {
    try {
        val connectivity = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val networks = connectivity.allNetworks
            var networkInfo: NetworkInfo
            for (mNetwork in networks) {
                networkInfo = connectivity.getNetworkInfo(mNetwork)
                if (!(networkInfo.state == NetworkInfo.State.CONNECTED &&
                                connectivity.activeNetworkInfo.isAvailable &&
                                connectivity.activeNetworkInfo.isConnected)) {
                    return true
                }
            }
        } else {
            val info = connectivity.allNetworkInfo
            if (info != null) {
                for (anInfo in info) {
                    if (anInfo.state == NetworkInfo.State.CONNECTED &&
                            connectivity.activeNetworkInfo.isAvailable &&
                            connectivity.activeNetworkInfo.isConnected) {
                        return true
                    }
                }
            }
        }
    } catch (e: Exception) {
        return false
    }
    return false
}