package core.connection.ok_client;

import android.util.Log;

import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.io.InputStream;
import java.security.GeneralSecurityException;
import java.security.KeyStore;
import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

import core.utils.CoreConstants;
import okio.Buffer;

public class CustomOkHttpClient {

    private static OkHttpClient okHttpClient;
    private static final int HTTP_TIMEOUT = 15 * 1000; // milliseconds
    private static final MediaType MEDIA_TYPE_PNG = MediaType.parse("image/png");

    private static OkHttpClient getOkHttpClient() {
        if (okHttpClient == null) {
            okHttpClient = new OkHttpClient();
            okHttpClient.setConnectTimeout(HTTP_TIMEOUT, TimeUnit.MILLISECONDS);
            okHttpClient.setReadTimeout(HTTP_TIMEOUT, TimeUnit.MILLISECONDS);
        }
        return okHttpClient;
    }


    private static OkHttpClient getOkHttpsClient() throws GeneralSecurityException {
        if (okHttpClient == null) {
            X509TrustManager trustManager;
            SSLSocketFactory sslSocketFactory;
            try {
                trustManager = trustManagerForCertificates(trustedCertificatesInputStream());
                SSLContext sslContext = SSLContext.getInstance("TLS");
                sslContext.init(null, new TrustManager[] { trustManager }, null);
                sslSocketFactory = sslContext.getSocketFactory();
            } catch (GeneralSecurityException e) {
                throw new RuntimeException(e);
            }
            
            okHttpClient = new OkHttpClient().setSslSocketFactory(sslSocketFactory);
            okHttpClient.setConnectTimeout(HTTP_TIMEOUT, TimeUnit.MILLISECONDS);
            okHttpClient.setReadTimeout(HTTP_TIMEOUT, TimeUnit.MILLISECONDS);
        }
        return okHttpClient;
    }

    private static InputStream trustedCertificatesInputStream() {
        String comodoRsaCertificationAuthority = ""
                + "-----BEGIN CERTIFICATE-----\n" +
                "MIIDyzCCArOgAwIBAgIEJamjXTANBgkqhkiG9w0BAQsFADCBlTELMAkGA1UEBhMC\n" +
                "SVQxDjAMBgNVBAgTBUl0YWx5MQ4wDAYDVQQHEwVNaWxhbjEUMBIGA1UEChMLWWVz\n" +
                "cy5FbmVyZ3kxGjAYBgNVBAsTEUVuZXJneSBNYW5hZ2VtZW50MTQwMgYDVQQDEytz\n" +
                "dmlsYXBwc2VydmVyLndlc3RldXJvcGUuY2xvdWRhcHAuYXp1cmUuY29tMB4XDTE3\n" +
                "MDUyNDA2MDgxMloXDTE4MDUxOTA2MDgxMlowgZUxCzAJBgNVBAYTAklUMQ4wDAYD\n" +
                "VQQIEwVJdGFseTEOMAwGA1UEBxMFTWlsYW4xFDASBgNVBAoTC1llc3MuRW5lcmd5\n" +
                "MRowGAYDVQQLExFFbmVyZ3kgTWFuYWdlbWVudDE0MDIGA1UEAxMrc3ZpbGFwcHNl\n" +
                "cnZlci53ZXN0ZXVyb3BlLmNsb3VkYXBwLmF6dXJlLmNvbTCCASIwDQYJKoZIhvcN\n" +
                "AQEBBQADggEPADCCAQoCggEBAMOdDJILUg19ZFd2GfJUI3n0E2egzqtbK14ItVvI\n" +
                "NStyYgTmMusmxEm9K3jj5vVgmAT6qvNhn355uamkQ0lmViD9jN8jCCZwnJB/u89O\n" +
                "yxSyREV6i3epYk/Trz2LdkOxfVtvVvb2LTD3B0+cHdts8pnuHj/TyHoGV1Bm998C\n" +
                "QnxkjHC0qSN+jMKNIOpxIJw45jjpiFh0eeu4NpiYc9Z0qlO+X8HRwSZPrm9jtxLX\n" +
                "PLZPN0U8Q6ek/TWZca+dFhbXvckjAV2nr3cBQLB5BnDLa7YuKRoxyZAHPDXTytKU\n" +
                "1U8T1CQqZwUZCNqUv7SiApQ6LMNWgzi/hfWNWRvnbPZACGsCAwEAAaMhMB8wHQYD\n" +
                "VR0OBBYEFNNIjngMqQmNUZw9RN2yhekU7/rGMA0GCSqGSIb3DQEBCwUAA4IBAQBH\n" +
                "2aYVtQYFZaY/0Ftt5V4QU95z0AlncXJZvbNIxrv2e9RqcFHTEGkNvhbbbIxRF9Q3\n" +
                "XjP7kDepVJG0zhJWDFEVye2+IV3Vs0lEgsx8nP4tXMsOcXp6tnHgCSVV7C1W2FwN\n" +
                "KzNiiQ6pPisKRi9u/gy5kh7WgiMkVZL9gFSWz8Ri1La7V6gJf2jRlMBhhiYGzygy\n" +
                "bfY2XUpBr38zMlueNBO9zgEombUoJVw7ch+2Js4LelozoVcMHaayx/f5lEEROztV\n" +
                "EsxI6l0vYPBaK3VgmsLwv9kA5bf9aUtSDb7HtX7PWRwSM3e2Dn8YGadbQiv2iEjQ\n" +
                "CjD7Du6iZt+ZNs/hG3Kw\n" +
                "-----END CERTIFICATE-----\n";
        String entrustRootCertificateAuthority = ""
                + "-----BEGIN CERTIFICATE-----\n" +
                "MIIDyzCCArOgAwIBAgIEJamjXTANBgkqhkiG9w0BAQsFADCBlTELMAkGA1UEBhMC\n" +
                "SVQxDjAMBgNVBAgTBUl0YWx5MQ4wDAYDVQQHEwVNaWxhbjEUMBIGA1UEChMLWWVz\n" +
                "cy5FbmVyZ3kxGjAYBgNVBAsTEUVuZXJneSBNYW5hZ2VtZW50MTQwMgYDVQQDEytz\n" +
                "dmlsYXBwc2VydmVyLndlc3RldXJvcGUuY2xvdWRhcHAuYXp1cmUuY29tMB4XDTE3\n" +
                "MDUyNDA2MDgxMloXDTE4MDUxOTA2MDgxMlowgZUxCzAJBgNVBAYTAklUMQ4wDAYD\n" +
                "VQQIEwVJdGFseTEOMAwGA1UEBxMFTWlsYW4xFDASBgNVBAoTC1llc3MuRW5lcmd5\n" +
                "MRowGAYDVQQLExFFbmVyZ3kgTWFuYWdlbWVudDE0MDIGA1UEAxMrc3ZpbGFwcHNl\n" +
                "cnZlci53ZXN0ZXVyb3BlLmNsb3VkYXBwLmF6dXJlLmNvbTCCASIwDQYJKoZIhvcN\n" +
                "AQEBBQADggEPADCCAQoCggEBAMOdDJILUg19ZFd2GfJUI3n0E2egzqtbK14ItVvI\n" +
                "NStyYgTmMusmxEm9K3jj5vVgmAT6qvNhn355uamkQ0lmViD9jN8jCCZwnJB/u89O\n" +
                "yxSyREV6i3epYk/Trz2LdkOxfVtvVvb2LTD3B0+cHdts8pnuHj/TyHoGV1Bm998C\n" +
                "QnxkjHC0qSN+jMKNIOpxIJw45jjpiFh0eeu4NpiYc9Z0qlO+X8HRwSZPrm9jtxLX\n" +
                "PLZPN0U8Q6ek/TWZca+dFhbXvckjAV2nr3cBQLB5BnDLa7YuKRoxyZAHPDXTytKU\n" +
                "1U8T1CQqZwUZCNqUv7SiApQ6LMNWgzi/hfWNWRvnbPZACGsCAwEAAaMhMB8wHQYD\n" +
                "VR0OBBYEFNNIjngMqQmNUZw9RN2yhekU7/rGMA0GCSqGSIb3DQEBCwUAA4IBAQBH\n" +
                "2aYVtQYFZaY/0Ftt5V4QU95z0AlncXJZvbNIxrv2e9RqcFHTEGkNvhbbbIxRF9Q3\n" +
                "XjP7kDepVJG0zhJWDFEVye2+IV3Vs0lEgsx8nP4tXMsOcXp6tnHgCSVV7C1W2FwN\n" +
                "KzNiiQ6pPisKRi9u/gy5kh7WgiMkVZL9gFSWz8Ri1La7V6gJf2jRlMBhhiYGzygy\n" +
                "bfY2XUpBr38zMlueNBO9zgEombUoJVw7ch+2Js4LelozoVcMHaayx/f5lEEROztV\n" +
                "EsxI6l0vYPBaK3VgmsLwv9kA5bf9aUtSDb7HtX7PWRwSM3e2Dn8YGadbQiv2iEjQ\n" +
                "CjD7Du6iZt+ZNs/hG3Kw\n" +
                "-----END CERTIFICATE-----\n";
        return new Buffer()
                .writeUtf8(comodoRsaCertificationAuthority)
                .writeUtf8(entrustRootCertificateAuthority)
                .inputStream();
    }

    private static X509TrustManager trustManagerForCertificates(InputStream in) throws GeneralSecurityException {
            CertificateFactory certificateFactory = CertificateFactory.getInstance("X.509");
            Collection<? extends Certificate> certificates = certificateFactory.generateCertificates(in);
            if (certificates.isEmpty()) {
                throw new IllegalArgumentException("expected non-empty set of trusted certificates");
            }

            // Put the certificates a key store.
            char[] password = "password".toCharArray(); // Any password will work.
            KeyStore keyStore = newEmptyKeyStore(password);
            int index = 0;
            for (Certificate certificate : certificates) {
                String certificateAlias = Integer.toString(index++);
                keyStore.setCertificateEntry(certificateAlias, certificate);
            }

            // Use it to build an X509 trust manager.
            KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
            keyManagerFactory.init(keyStore, password);
            TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
            trustManagerFactory.init(keyStore);
            TrustManager[] trustManagers = trustManagerFactory.getTrustManagers();
            if (trustManagers.length != 1 || !(trustManagers[0] instanceof X509TrustManager)) {
                throw new IllegalStateException("Unexpected default trust managers:"
                        + Arrays.toString(trustManagers));
            }
            return (X509TrustManager) trustManagers[0];
    }

    private static KeyStore newEmptyKeyStore(char[] password) throws GeneralSecurityException {
        try {
            KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
            InputStream in = null; // By convention, 'null' creates an empty key store.
            keyStore.load(in, password);
            return keyStore;
        } catch (IOException e) {
            throw new AssertionError(e);
        }
    }

    public static String doGetRequest(String methodName, String url) {
        Response response;
        try {
            Request request = new Request.Builder()
                    .url(url)
                    .build();

            if(url.contains("https://"))
                response = getOkHttpsClient().newCall(request).execute();
            else
                response = getOkHttpClient().newCall(request).execute();

            if (!CoreConstants.isDebug && !CoreConstants.isSigned) {
                Log.d(CoreConstants.TAG, ">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
                Log.d(CoreConstants.TAG, "GET");
                Log.e(CoreConstants.TAG, methodName);
                Log.d(CoreConstants.TAG, url);
                Log.d(CoreConstants.TAG, "<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<");
                Log.d(CoreConstants.TAG, "CODE         : \n" + response.code());
                Log.d(CoreConstants.TAG, "RESPONSE     : \n" + response.toString());
            }
            return response.body().string();

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String doPostRequest(String methodName, String url, RequestBody formBody) {
        Response response;
        try {
            Request request = new Request.Builder()
                    .url(url)
                    .post(formBody)
                    .build();

            if(url.contains("https://"))
                response = getOkHttpsClient().newCall(request).execute();
            else
                response = getOkHttpClient().newCall(request).execute();

            if (!CoreConstants.isDebug && !CoreConstants.isSigned) {
                Log.d(CoreConstants.TAG, ">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
                Log.d(CoreConstants.TAG, "POST");
                Log.e(CoreConstants.TAG, methodName);
                Log.d(CoreConstants.TAG, url);
                Log.d(CoreConstants.TAG, "PARAMETERS   : \n" + formBody);
                Log.d(CoreConstants.TAG, "<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<");
                Log.d(CoreConstants.TAG, "CODE         : \n" + response.code());
                Log.d(CoreConstants.TAG, "RESPONSE     : \n" + response.toString());
            }
            return response.body().string();

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String doGetRequestWithHeader(String methodName, String url, String authorization) {
        Response response;
        try {
            Request request = new Request.Builder()
                    .url(url)
                    .get()
                    .addHeader("Authorization", authorization)
                    .build();

            if(url.contains("https://"))
                response = getOkHttpsClient().newCall(request).execute();
            else
                response = getOkHttpClient().newCall(request).execute();

            if (!CoreConstants.isDebug && !CoreConstants.isSigned) {
                Log.d(CoreConstants.TAG, ">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
                Log.d(CoreConstants.TAG, "GET");
                Log.e(CoreConstants.TAG, methodName);
                Log.d(CoreConstants.TAG, url);
                Log.d(CoreConstants.TAG, "<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<");
                Log.d(CoreConstants.TAG, "CODE         : \n" + response.code());
                Log.d(CoreConstants.TAG, "RESPONSE     : \n" + response.toString());
            }
            return response.body().string();

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String doGetRequestWithMultiHeader(String methodName, String url, HashMap<String, String> authorizations) {
        Response response;
        try {
            Request.Builder request = new Request.Builder().url(url).get();

            Iterator it = authorizations.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry pair = (Map.Entry)it.next();
                request.addHeader((String) pair.getKey(), (String) pair.getValue());
            }
            request.build();

            if(url.contains("https://"))
                response = getOkHttpsClient().newCall(request.build()).execute();
            else
                response = getOkHttpClient().newCall(request.build()).execute();

            if (!CoreConstants.isDebug && !CoreConstants.isSigned) {
                Log.d(CoreConstants.TAG, ">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
                Log.d(CoreConstants.TAG, "GET MULTI HEADER");
                Log.e(CoreConstants.TAG, methodName);
                Log.d(CoreConstants.TAG, url);
                Log.d(CoreConstants.TAG, "<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<");
                Log.d(CoreConstants.TAG, "CODE         : \n" + response.code());
                Log.d(CoreConstants.TAG, "RESPONSE     : \n" + response.toString());
            }
            return response.body().string();

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String doPostRequestWithMultiHeader(String methodName, String url, HashMap<String, String> authorizations, RequestBody formBody) {
        Response response;
        try {
            Request.Builder request = new Request.Builder().url(url).post(formBody);

            Iterator it = authorizations.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry pair = (Map.Entry)it.next();
                request.addHeader((String) pair.getKey(), (String) pair.getValue());
            }
            request.build();

            if(url.contains("https://"))
                response = getOkHttpsClient().newCall(request.build()).execute();
            else
                response = getOkHttpClient().newCall(request.build()).execute();

            if (!CoreConstants.isDebug && !CoreConstants.isSigned) {
                Log.d(CoreConstants.TAG, ">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
                Log.d(CoreConstants.TAG, "POST MULTI HEADER");
                Log.e(CoreConstants.TAG, methodName);
                Log.d(CoreConstants.TAG, url);
                Log.d(CoreConstants.TAG, "<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<");
                Log.d(CoreConstants.TAG, "CODE         : \n" + response.code());
                Log.d(CoreConstants.TAG, "RESPONSE     : \n" + response.toString());
            }
            return response.body().string();

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String doPostRequestWithMultipart(String methodName, String url, RequestBody formBody) {
        Response response;
        try {
            Request request = new Request.Builder()
                    .url(url)
                    .post(formBody)
                    .build();

            if(url.contains("https://"))
                response = getOkHttpsClient().newCall(request).execute();
            else
                response = getOkHttpClient().newCall(request).execute();

            if (!CoreConstants.isDebug && !CoreConstants.isSigned) {
                Log.d(CoreConstants.TAG, ">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
                Log.d(CoreConstants.TAG, "POST WITH MULTIPART");
                Log.e(CoreConstants.TAG, methodName);
                Log.d(CoreConstants.TAG, url);
                Log.d(CoreConstants.TAG, "PARAMETERS   : \n" + formBody);
                Log.d(CoreConstants.TAG, "<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<");
                Log.d(CoreConstants.TAG, "CODE         : \n" + response.code());
                Log.d(CoreConstants.TAG, "RESPONSE     : \n" + response.toString());
            }
            return response.body().string();

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
