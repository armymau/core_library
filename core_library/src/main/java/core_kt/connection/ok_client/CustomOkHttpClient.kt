package core_kt.connection.ok_client

import android.util.Log
import com.squareup.okhttp.*
import core_kt.utils.TAG
import core_kt.utils.isDebug
import core_kt.utils.isSigned
import java.security.SecureRandom
import java.security.cert.CertificateException
import java.security.cert.X509Certificate
import java.util.*
import java.util.concurrent.TimeUnit
import javax.net.ssl.*

object CustomOkHttpClient {

    private var okHttpClient: OkHttpClient = OkHttpClient()
    private val HTTP_TIMEOUT = 15 * 1000 // milliseconds
    private val MEDIA_TYPE_PNG = MediaType.parse("image/png")

    fun getOkHttpClient(): OkHttpClient {
        okHttpClient.setConnectTimeout(HTTP_TIMEOUT.toLong(), TimeUnit.MILLISECONDS)
        okHttpClient.setReadTimeout(HTTP_TIMEOUT.toLong(), TimeUnit.MILLISECONDS)
        return okHttpClient
    }

    fun getOkHttpsClient(): OkHttpClient {

        okHttpClient.hostnameVerifier = HostnameVerifier { _, _ -> true }

        val trustAllCerts = arrayOf<TrustManager>(object : X509TrustManager {
            @Throws(CertificateException::class)
            override fun checkClientTrusted(x509Certificates: Array<X509Certificate>, s: String) {
            }

            @Throws(CertificateException::class)
            override fun checkServerTrusted(x509Certificates: Array<X509Certificate>, s: String) {
            }

            override fun getAcceptedIssuers(): Array<X509Certificate?> {
                return arrayOfNulls(0)
            }
        })

        try {
            val sslContext = SSLContext.getInstance("TLS")
            sslContext.init(null as Array<KeyManager>?, trustAllCerts, SecureRandom())
            okHttpClient.sslSocketFactory = sslContext.socketFactory
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
                        getOkHttpsClient().newCall(request).execute()
                    else
                        getOkHttpClient().newCall(request).execute()

            if (!isDebug && !isSigned) {
                Log.d(TAG, ">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>")
                Log.d(TAG, "GET")
                Log.e(TAG, methodName)
                Log.d(TAG, url)
                Log.d(TAG, "<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<")
                Log.d(TAG, "CODE         : \n" + response.code())
                Log.d(TAG, "RESPONSE     : \n" + response.toString())
            }
            return response.body().string()

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
                        getOkHttpsClient().newCall(request).execute()
                    else
                        getOkHttpClient().newCall(request).execute()

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
            return response.body().string()

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
                        getOkHttpsClient().newCall(request).execute()
                    else
                        getOkHttpClient().newCall(request).execute()

            if (!isDebug && !isSigned) {
                Log.d(TAG, ">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>")
                Log.d(TAG, "GET")
                Log.e(TAG, methodName)
                Log.d(TAG, url)
                Log.d(TAG, "<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<")
                Log.d(TAG, "CODE         : \n" + response.code())
                Log.d(TAG, "RESPONSE     : \n" + response.toString())
            }
            return response.body().string()

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
                        getOkHttpsClient().newCall(request.build()).execute()
                    else
                        getOkHttpClient().newCall(request.build()).execute()

            if (!isDebug && !isSigned) {
                Log.d(TAG, ">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>")
                Log.d(TAG, "GET MULTI HEADER")
                Log.e(TAG, methodName)
                Log.d(TAG, url)
                Log.d(TAG, "<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<")
                Log.d(TAG, "CODE         : \n" + response.code())
                Log.d(TAG, "RESPONSE     : \n" + response.toString())
            }
            return response.body().string()

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
                        getOkHttpsClient().newCall(request.build()).execute()
                    else
                        getOkHttpClient().newCall(request.build()).execute()

            if (!isDebug && !isSigned) {
                Log.d(TAG, ">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>")
                Log.d(TAG, "POST MULTI HEADER")
                Log.e(TAG, methodName)
                Log.d(TAG, url)
                Log.d(TAG, "<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<")
                Log.d(TAG, "CODE         : \n" + response.code())
                Log.d(TAG, "RESPONSE     : \n" + response.toString())
            }
            return response.body().string()

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
                        getOkHttpsClient().newCall(request).execute()
                    else
                        getOkHttpClient().newCall(request).execute()

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
            return response.body().string()

        } catch (e: Exception) {
            e.printStackTrace()
            return null
        }
    }
}
