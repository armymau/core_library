package core_kt.service

import android.app.Activity
import android.content.Intent
import android.util.Log
import com.google.gson.Gson
import com.squareup.okhttp.FormEncodingBuilder
import com.squareup.okhttp.MediaType
import com.squareup.okhttp.MultipartBuilder
import com.squareup.okhttp.RequestBody
import core_kt.connection.model.GenericResponse
import core_kt.connection.model.getCachedObjectForRequest
import core_kt.connection.model.saveCachedObjectForRequest
import core_kt.connection.ok_client.CustomOkHttpClient.doGetRequest
import core_kt.connection.ok_client.CustomOkHttpClient.doGetRequestWithHeader
import core_kt.connection.ok_client.CustomOkHttpClient.doGetRequestWithMultiHeader
import core_kt.connection.ok_client.CustomOkHttpClient.doPostRequest
import core_kt.connection.ok_client.CustomOkHttpClient.doPostRequestWithMultiHeader
import core_kt.connection.ok_client.CustomOkHttpClient.doPostRequestWithMultipart
import core_kt.utils.*
import java.net.MalformedURLException
import java.net.URL
import java.util.*

open class ServiceManager<T>(private val methodName: String, private val http_method: Int, private val activity: Activity, endPointUrl: String, parameters: Map<String, Any>, authorizations: HashMap<String, String>, private val formEncodingBuilder: MultipartBuilder?, private val shouldLoadCache: Boolean, isVisibleProgress: Boolean, private val clazz: T) : ProgressAsyncTask<T>(activity, isVisibleProgress) {

    private var response: String? = null
    private var formBody: RequestBody? = null
    private var endPointUrl = ""
    lateinit var authorizations: HashMap<String, String>

    init {
        response = null

        when (http_method) {
            HTTP_METHOD_GET -> this.endPointUrl = endPointUrl + prepareParametersForGetMethod(parameters)

            HTTP_METHOD_POST -> {
                this.endPointUrl = endPointUrl
                this.formBody = prepareParametersForPostMethod(parameters)
            }
            HTTP_METHOD_GET_WITH_AUTHORIZATION -> {
                this.endPointUrl = endPointUrl
                this.authorizations = authorizations
            }
            HTTP_METHOD_POST_WITH_MULTIPART -> {
                this.endPointUrl = endPointUrl
                this.formBody = formEncodingBuilder?.let { prepareParametersForPostMethodWithMultipart(it) }
            }
            HTTP_METHOD_GET_WITH_MULTI_AUTHORIZATION -> {
                this.endPointUrl = endPointUrl
                this.authorizations = authorizations
            }
            HTTP_METHOD_POST_WITH_MULTI_AUTHORIZATION -> {
                this.endPointUrl = endPointUrl
                this.authorizations = authorizations
                this.formBody = prepareParametersForPostMethodWithJsonType(parameters)
            }
        }
    }

    override fun onPreExecute() {
        super.onPreExecute()
        if (shouldLoadCache) {
            try {
                response = getCachedObjectForRequest(activity, URL(endPointUrl)) as String
            } catch (e: Exception) {
                Log.e(TAG, "$HTTP_METHOD_REPONSE_CACHING_FAILED for $methodName")
                e.printStackTrace()
            }
        }
    }

    @Override
    override fun doInBackground(vararg p0: String) : T? {
        return doInBackground()
    }
    
    fun doInBackground(): T? {
        var result: String? = null

        if (shouldLoadCache) {
            if (response != null) publishProgress(response as String)
        }
        try {
            val responseNow = callEndPoint(shouldLoadCache)
            if (responseNow != null) {
                if (!isDebug && !isSigned) {
                    Log.d(TAG, "REMOTE RESPONSE : \n$responseNow")
                    Log.d(TAG, ">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>")
                }
                result = responseNow
            } else {
                if (shouldLoadCache) {
                    result = response
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        val gson = Gson()
        val objectResponse: T
        try {
            objectResponse = gson.fromJson(result, clazz as Class<*>) as T
        } catch (e: Exception) {
            return null
        }
        return objectResponse
    }

    @Throws(MalformedURLException::class)
    private fun callEndPoint(shouldLoadCache: Boolean): String? {
        var response: String? = null
        if (isNetworkAvailable(activity)) {
            try {
                if (http_method == HTTP_METHOD_GET) {
                    response = doGetRequest(methodName, endPointUrl)

                } else if (http_method == HTTP_METHOD_POST) {
                    response = formBody?.let { doPostRequest(methodName, endPointUrl, it) }

                } else if (http_method == HTTP_METHOD_GET_WITH_AUTHORIZATION) {
                    response = authorizations["Authorization"]?.let { doGetRequestWithHeader(methodName, endPointUrl, it) }

                } else if (http_method == HTTP_METHOD_POST_WITH_MULTIPART) {
                    response = formBody?.let { doPostRequestWithMultipart(methodName, endPointUrl, it) }

                } else if (http_method == HTTP_METHOD_GET_WITH_MULTI_AUTHORIZATION) {
                    response = doGetRequestWithMultiHeader(methodName, endPointUrl, authorizations)

                } else if (http_method == HTTP_METHOD_POST_WITH_MULTI_AUTHORIZATION) {
                    response = formBody?.let { doPostRequestWithMultiHeader(methodName, endPointUrl, authorizations, it) }

                }
                if (response != null) {
                    val gson = Gson()
                    val result = gson.fromJson(response, GenericResponse::class.java)
                    if (shouldLoadCache) {
                        if (result != null && result.status == 200) {
                            saveCachedObjectForRequest(activity, URL(endPointUrl), response)
                        } else {
                            response = getCachedObjectForRequest(activity, URL(endPointUrl)) as String
                        }
                    }
                } else {
                    if (shouldLoadCache) {
                        Log.e(TAG, "$HTTP_METHOD_CACHED for $methodName")
                        response = getCachedObjectForRequest(activity, URL(endPointUrl)) as String
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        val intent = Intent()
        intent.action = "core.library.NO_CONNECTION_ACTION"
        intent.putExtra("isNetworkAvailable", isNetworkAvailable(activity))
        activity.sendBroadcast(intent)
        return response
    }

    fun prepareParametersForGetMethod(parameters: Map<String, Any>): String {
        var param = ""
        val keys = ArrayList(parameters.keys)
        val values = ArrayList(parameters.values)
        for (i in keys.indices) {
            param += "&" + keys[i] + "=" + values[i]
        }
        return param
    }

    fun prepareParametersForPostMethod(parameters: Map<String, Any>): RequestBody? {
        val formEncodingBuilder = FormEncodingBuilder()
        val keys = ArrayList(parameters.keys)
        val values = ArrayList(parameters.values)

        for (i in keys.indices) {
            formEncodingBuilder.add(keys[i], values[i] as String)
        }
        var formBody: RequestBody? = null
        try {
            formBody = formEncodingBuilder.build()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return formBody
    }

    fun prepareParametersForPostMethodWithMultipart(formEncodingBuilder: MultipartBuilder): RequestBody? {
        var formBody: RequestBody? = null
        try {
            formBody = formEncodingBuilder.type(MultipartBuilder.FORM).build()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return formBody
    }

    fun prepareParametersForPostMethodWithJsonType(parameters: Map<String, Any>): RequestBody? {
        var formBody: RequestBody? = null
        try {
            val mediaType = MediaType.parse("application/json")

            if (parameters.isEmpty()) {
                formBody = RequestBody.create(null, byteArrayOf())
            }

            val it = parameters.entries.iterator()
            while (it.hasNext()) {
                val pair = it.next() as Map.Entry<*, *>
                formBody = RequestBody.create(mediaType, pair.value as String)
            }

        } catch (e: Exception) {
            e.printStackTrace()
        }
        return formBody
    }
}
