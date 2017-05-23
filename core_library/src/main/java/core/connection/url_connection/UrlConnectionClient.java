package core.connection.url_connection;

import android.os.Build;
import android.util.Log;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.X509TrustManager;

import core.utils.CoreConstants;

public class UrlConnectionClient {

    public static String doGET(String endPointUrl) throws Exception {
        HttpURLConnection conn;
        InputStream is = null;

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.FROYO) {
            System.setProperty("http.keepAlive", "false");
        }

        if (endPointUrl.startsWith("file://")) {
            return readIt(new FileInputStream(endPointUrl.replace("file://", "")));
        }

        if (endPointUrl.startsWith("https://")) {
            HttpsURLConnection.setDefaultHostnameVerifier(new HostnameVerifier() {
                public boolean verify(String hostname, SSLSession session) {
                    return true;
                }
            });
            SSLContext context = SSLContext.getInstance("TLS");
            context.init(null, new X509TrustManager[]{
                    new X509TrustManager() {
                        public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
                        }

                        public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
                        }

                        public X509Certificate[] getAcceptedIssuers() {
                            return new X509Certificate[0];
                        }
                    }
            }, new SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(context.getSocketFactory());
        }

        try {
            URL url = new URL(endPointUrl);
            conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(CoreConstants.HTTP_TIMEOUT);
            conn.setConnectTimeout(CoreConstants.HTTP_TIMEOUT);
            conn.setRequestMethod(CoreConstants.HTTP_REQUEST_METHOD_GET);
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setUseCaches(true);
            conn.setAllowUserInteraction(false);
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

            // Starts the query
            conn.connect();

            int responseCode = conn.getResponseCode();
            Log.d(CoreConstants.TAG, "The response code is: " + responseCode);
            is = conn.getInputStream();

            // Convert the InputStream into a string
            return readIt(is);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            if (is != null) {
                is.close();
            }
        }
    }

    public static String doPOST(String endPointUrl) throws Exception {
        HttpURLConnection conn;
        InputStream is = null;
        DataOutputStream dataout;


        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.FROYO) {
            System.setProperty("http.keepAlive", "false");
        }

        if (endPointUrl.startsWith("file://")) {
            return readIt(new FileInputStream(endPointUrl.replace("file://", "")));
        }

        if (endPointUrl.startsWith("https://")) {
            HttpsURLConnection.setDefaultHostnameVerifier(new HostnameVerifier() {
                public boolean verify(String hostname, SSLSession session) {
                    return true;
                }
            });
            SSLContext context = SSLContext.getInstance("TLS");
            context.init(null, new X509TrustManager[]{
                    new X509TrustManager() {
                        public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
                        }

                        public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
                        }

                        public X509Certificate[] getAcceptedIssuers() {
                            return new X509Certificate[0];
                        }
                    }
            }, new SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(context.getSocketFactory());
        }

        try {
            URL url = new URL(endPointUrl);
            conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(CoreConstants.HTTP_TIMEOUT);
            conn.setConnectTimeout(CoreConstants.HTTP_TIMEOUT);
            conn.setRequestMethod(CoreConstants.HTTP_REQUEST_METHOD_POST);
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setUseCaches(true);
            conn.setAllowUserInteraction(false);
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

            dataout = new DataOutputStream(conn.getOutputStream());
            // perform POST operation
            dataout.writeBytes(endPointUrl);

            int responseCode = conn.getResponseCode();
            Log.d(CoreConstants.TAG, "The response code is: " + responseCode);
            is = conn.getInputStream();

            // Convert the InputStream into a string
            return readIt(is);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            if (is != null) {
                is.close();
            }
        }
    }

    private static String readIt(InputStream stream) throws IOException {
        BufferedReader inForm;
        inForm = new BufferedReader(new InputStreamReader(stream, "UTF-8"));

        StringBuffer sb = new StringBuffer("");
        String line;
        while((line = inForm.readLine()) != null) {
            sb.append(line);
        }
        return sb.toString();
    }
}
