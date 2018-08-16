package core_kt.connection.url_connection

import android.os.Build
import android.util.Log
import core_kt.utils.HTTP_REQUEST_METHOD_GET
import core_kt.utils.HTTP_REQUEST_METHOD_POST
import core_kt.utils.HTTP_TIMEOUT
import core_kt.utils.TAG
import java.io.*
import java.net.HttpURLConnection
import java.net.URL
import java.security.SecureRandom
import java.security.cert.CertificateException
import java.security.cert.X509Certificate
import javax.net.ssl.HttpsURLConnection
import javax.net.ssl.SSLContext
import javax.net.ssl.X509TrustManager

@Throws(Exception::class)
fun doGET(endPointUrl: String): String? {
    val conn: HttpURLConnection
    var inputStream: InputStream? = null

    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.FROYO) {
        System.setProperty("http.keepAlive", "false")
    }

    if (endPointUrl.startsWith("file://")) {
        return readIt(FileInputStream(endPointUrl.replace("file://", "")))
    }

    if (endPointUrl.startsWith("https://")) {
        HttpsURLConnection.setDefaultHostnameVerifier { _, _ -> true }
        val context = SSLContext.getInstance("TLS")
        context.init(null, arrayOf<X509TrustManager>(object : X509TrustManager {
            @Throws(CertificateException::class)
            override fun checkClientTrusted(chain: Array<X509Certificate>, authType: String) {
            }

            @Throws(CertificateException::class)
            override fun checkServerTrusted(chain: Array<X509Certificate>, authType: String) {
            }

            override fun getAcceptedIssuers(): Array<X509Certificate?> {
                return arrayOfNulls(0)
            }
        }), SecureRandom())
        HttpsURLConnection.setDefaultSSLSocketFactory(context.socketFactory)
    }

    try {
        val url = URL(endPointUrl)
        conn = url.openConnection() as HttpURLConnection
        conn.readTimeout = HTTP_TIMEOUT
        conn.connectTimeout = HTTP_TIMEOUT
        conn.requestMethod = HTTP_REQUEST_METHOD_GET
        conn.doOutput = true
        conn.doInput = true
        conn.useCaches = true
        conn.allowUserInteraction = false
        conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded")

        // Starts the query
        conn.connect()

        val responseCode = conn.responseCode
        Log.d(TAG, "The response code is: $responseCode")
        inputStream = conn.inputStream

        // Convert the InputStream into a string
        return readIt(inputStream)
    } catch (e: Exception) {
        e.printStackTrace()
        return null
    } finally {
        inputStream?.close()
    }
}

@Throws(Exception::class)
fun doPOST(endPointUrl: String): String? {
    val conn: HttpURLConnection
    var inputStream: InputStream? = null
    val dataout: DataOutputStream


    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.FROYO) {
        System.setProperty("http.keepAlive", "false")
    }

    if (endPointUrl.startsWith("file://")) {
        return readIt(FileInputStream(endPointUrl.replace("file://", "")))
    }

    if (endPointUrl.startsWith("https://")) {
        HttpsURLConnection.setDefaultHostnameVerifier { _, _ -> true }
        val context = SSLContext.getInstance("TLS")
        context.init(null, arrayOf<X509TrustManager>(object : X509TrustManager {
            @Throws(CertificateException::class)
            override fun checkClientTrusted(chain: Array<X509Certificate>, authType: String) {
            }

            @Throws(CertificateException::class)
            override fun checkServerTrusted(chain: Array<X509Certificate>, authType: String) {
            }

            override fun getAcceptedIssuers(): Array<X509Certificate?> {
                return arrayOfNulls(0)
            }
        }), SecureRandom())
        HttpsURLConnection.setDefaultSSLSocketFactory(context.socketFactory)
    }

    try {
        val url = URL(endPointUrl)
        conn = url.openConnection() as HttpURLConnection
        conn.readTimeout = HTTP_TIMEOUT
        conn.connectTimeout = HTTP_TIMEOUT
        conn.requestMethod = HTTP_REQUEST_METHOD_POST
        conn.doOutput = true
        conn.doInput = true
        conn.useCaches = true
        conn.allowUserInteraction = false
        conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded")

        dataout = DataOutputStream(conn.outputStream)
        // perform POST operation
        dataout.writeBytes(endPointUrl)

        val responseCode = conn.responseCode
        Log.d(TAG, "The response code is: $responseCode")
        inputStream = conn.inputStream

        // Convert the InputStream into a string
        return readIt(inputStream)
    } catch (e: Exception) {
        e.printStackTrace()
        return null
    } finally {
        inputStream?.close()
    }
}

@Throws(IOException::class)
private fun readIt(stream: InputStream?): String {
    val inForm: BufferedReader
    inForm = BufferedReader(InputStreamReader(stream!!, "UTF-8"))

    val sb = StringBuffer("")
    while ((inForm.readLine()) != null) {
        sb.append(inForm.readLine())
    }
    return sb.toString()
}