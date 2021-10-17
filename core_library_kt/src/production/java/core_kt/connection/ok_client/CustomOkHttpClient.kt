package core_kt.connection.ok_client

import android.util.Log
import core_kt.utils.TAG
import core_kt.utils.isDebug
import core_kt.utils.isSigned
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import okhttp3.Response
import java.security.SecureRandom
import java.security.cert.CertificateException
import java.security.cert.X509Certificate
import java.util.*
import java.util.concurrent.TimeUnit
import javax.net.ssl.KeyManager
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManager
import javax.net.ssl.X509TrustManager

private val HTTP_TIMEOUT = 120 * 1000 // milliseconds

fun getOkHttpClient(): OkHttpClient.Builder {

    val okHttpClient = OkHttpClient.Builder()
    okHttpClient.connectTimeout(HTTP_TIMEOUT.toLong(), TimeUnit.MILLISECONDS)
    okHttpClient.readTimeout(HTTP_TIMEOUT.toLong(), TimeUnit.MILLISECONDS)
    okHttpClient.writeTimeout(HTTP_TIMEOUT.toLong(), TimeUnit.MILLISECONDS)

    return okHttpClient
}

fun getOkHttpsClient(): OkHttpClient.Builder {

    val okHttpClient = getOkHttpClient()
    okHttpClient.hostnameVerifier { _, _ -> true }

    val x509TrustManager = object : X509TrustManager {
        @Throws(CertificateException::class)
        override fun checkClientTrusted(chain: Array<X509Certificate>, authType: String) {
        }

        @Throws(CertificateException::class)
        override fun checkServerTrusted(chain: Array<X509Certificate>, authType: String) {
            //chain[0].checkValidity()
        }

        override fun getAcceptedIssuers(): Array<X509Certificate?> {
            return arrayOfNulls(0)
        }
    }
    val trustAllCerts = arrayOf<TrustManager>(x509TrustManager)

    try {
        val sslContext = SSLContext.getInstance("TLS")
        sslContext.init(null as Array<KeyManager>?, trustAllCerts, SecureRandom())
        val sslSocketFactory = sslContext.socketFactory
        okHttpClient.sslSocketFactory(sslSocketFactory, x509TrustManager)
    } catch (var2: Exception) {
        var2.printStackTrace()
    }

    return okHttpClient
}

fun doGetRequest(methodName: String, url: String): String? {
    val response: Response
    try {
        val request = Request.Builder()
                .url(url)
                .build()

        response =
                if (url.contains("https://"))
                    getOkHttpsClient().build().newCall(request).execute()
                else
                    getOkHttpClient().build().newCall(request).execute()

        if (!isDebug && !isSigned) {
            Log.d(TAG, ">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>")
            Log.d(TAG, "GET")
            Log.e(TAG, methodName)
            Log.d(TAG, url)
            Log.d(TAG, "<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<")
            Log.d(TAG, "CODE         : \n" + response.code())
            Log.d(TAG, "RESPONSE     : \n" + response.toString())
        }
        return response.body()?.string()

    } catch (e: Exception) {
        e.printStackTrace()
        return null
    }
}

fun doPostRequest(methodName: String, url: String, formBody: RequestBody): String? {
    val response: Response
    try {
        val request = Request.Builder()
                .url(url)
                .post(formBody)
                .build()

        response =
                if (url.contains("https://"))
                    getOkHttpsClient().build().newCall(request).execute()
                else
                    getOkHttpClient().build().newCall(request).execute()

        if (!isDebug && !isSigned) {
            Log.d(TAG, ">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>")
            Log.d(TAG, "POST")
            Log.e(TAG, methodName)
            Log.d(TAG, url)
            Log.d(TAG, "PARAMETERS   : \n$formBody")
            Log.d(TAG, "<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<")
            Log.d(TAG, "CODE         : \n" + response.code())
            Log.d(TAG, "RESPONSE     : \n" + response.toString())
        }
        return response.body()?.string()

    } catch (e: Exception) {
        e.printStackTrace()
        return null
    }
}

fun doGetRequestWithHeader(methodName: String, url: String, authorization: String): String? {
    val response: Response
    try {
        val request = Request.Builder()
                .url(url)
                .get()
                .addHeader("Authorization", authorization)
                .build()

        response =
                if (url.contains("https://"))
                    getOkHttpsClient().build().newCall(request).execute()
                else
                    getOkHttpClient().build().newCall(request).execute()

        if (!isDebug && !isSigned) {
            Log.d(TAG, ">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>")
            Log.d(TAG, "GET")
            Log.e(TAG, methodName)
            Log.d(TAG, url)
            Log.d(TAG, "<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<")
            Log.d(TAG, "CODE         : \n" + response.code())
            Log.d(TAG, "RESPONSE     : \n" + response.toString())
        }
        return response.body()?.string()

    } catch (e: Exception) {
        e.printStackTrace()
        return null
    }
}

fun doGetRequestWithMultiHeader(methodName: String, url: String, authorizations: HashMap<String, String>): String? {
    val response: Response
    try {
        val request = Request.Builder().url(url).get()

        val it = authorizations.entries.iterator()
        while (it.hasNext()) {
            val pair = it.next() as Map.Entry<*, *>
            request.addHeader(pair.key as String, pair.value as String)
        }
        request.build()

        response =
                if (url.contains("https://"))
                    getOkHttpsClient().build().newCall(request.build()).execute()
                else
                    getOkHttpClient().build().newCall(request.build()).execute()

        if (!isDebug && !isSigned) {
            Log.d(TAG, ">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>")
            Log.d(TAG, "GET MULTI HEADER")
            Log.e(TAG, methodName)
            Log.d(TAG, url)
            Log.d(TAG, "<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<")
            Log.d(TAG, "CODE         : \n" + response.code())
            Log.d(TAG, "RESPONSE     : \n" + response.toString())
        }
        return response.body()?.string()

    } catch (e: Exception) {
        e.printStackTrace()
        return null
    }
}

fun doPostRequestWithMultiHeader(methodName: String, url: String, authorizations: HashMap<String, String>, formBody: RequestBody): String? {
    val response: Response
    try {
        val request = Request.Builder().url(url).post(formBody)

        val it = authorizations.entries.iterator()
        while (it.hasNext()) {
            val pair = it.next() as Map.Entry<*, *>
            request.addHeader(pair.key as String, pair.value as String)
        }
        request.build()

        response =
                if (url.contains("https://"))
                    getOkHttpsClient().build().newCall(request.build()).execute()
                else
                    getOkHttpClient().build().newCall(request.build()).execute()

        if (!isDebug && !isSigned) {
            Log.d(TAG, ">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>")
            Log.d(TAG, "POST MULTI HEADER")
            Log.e(TAG, methodName)
            Log.d(TAG, url)
            Log.d(TAG, "<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<")
            Log.d(TAG, "CODE         : \n" + response.code())
            Log.d(TAG, "RESPONSE     : \n" + response.toString())
        }
        return response.body()?.string()

    } catch (e: Exception) {
        e.printStackTrace()
        return null
    }
}

fun doPostRequestWithMultipart(methodName: String, url: String, formBody: RequestBody): String? {
    val response: Response
    try {
        val request = Request.Builder()
                .url(url)
                .post(formBody)
                .build()

        response =
                if (url.contains("https://"))
                    getOkHttpsClient().build().newCall(request).execute()
                else
                    getOkHttpClient().build().newCall(request).execute()

        if (!isDebug && !isSigned) {
            Log.d(TAG, ">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>")
            Log.d(TAG, "POST WITH MULTIPART")
            Log.e(TAG, methodName)
            Log.d(TAG, url)
            Log.d(TAG, "PARAMETERS   : \n$formBody")
            Log.d(TAG, "<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<")
            Log.d(TAG, "CODE         : \n" + response.code())
            Log.d(TAG, "RESPONSE     : \n" + response.toString())
        }
        return response.body()?.string()

    } catch (e: Exception) {
        e.printStackTrace()
        return null
    }
}