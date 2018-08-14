package core_kt.connection.volley

import android.content.Context
import android.util.Log
import com.android.volley.AuthFailureError
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.google.gson.Gson
import core_kt.common_interface.CoreVolleyNetworkCallback
import core_kt.connection.model.GenericResponse
import core_kt.utils.TAG
import core_kt.utils.isDebug
import org.json.JSONObject
import java.lang.ref.WeakReference

class CoreVolleyNetworkRequest(
        methodName: String,
        url: String,
        context: Context,
        callback: CoreVolleyNetworkCallback) : JsonObjectRequest(Request.Method.GET, url, null, Response.Listener<JSONObject> { response ->
            val mContext = WeakReference(context).get()
            val volleyCallback = WeakReference(callback).get()
            if (mContext != null && volleyCallback != null) {

                if (isDebug) {
                    Log.d(TAG, ">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>")
                    Log.e(TAG, methodName)
                    Log.d(TAG, url)
                    Log.d(TAG, "RESPONSE     : " + response!!.toString())
                }
                if (response != null) {
                    val result: GenericResponse?
                    val gson = Gson()
                    result = gson.fromJson(response.toString(), GenericResponse::class.java)
                    if (result != null) {
                        volleyCallback.onSuccess(result)
                    } else {
                        volleyCallback.onFailed(result)
                    }
                }
            }
        }, Response.ErrorListener { error ->
            if (isDebug) {
                Log.d(TAG, ">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>")
                Log.e(TAG, methodName)
                Log.d(TAG, url)
                Log.d(TAG, "ERROR     : " + if (error.networkResponse != null) error.networkResponse.statusCode else "networkResponse == null")
            }
        }) {


    @Throws(AuthFailureError::class)
    override fun getHeaders(): Map<String, String> {
        val headers = HashMap<String, String>()
        headers["Content-Type"] = "application/json; charset=utf-8"
        return headers
    }
}