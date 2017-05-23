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
            SSLContext sslContext = sslContextForTrustedCertificates(trustedCertificatesInputStream());
            okHttpClient = new OkHttpClient().setSocketFactory(sslContext.getSocketFactory());
            okHttpClient.setConnectTimeout(HTTP_TIMEOUT, TimeUnit.MILLISECONDS);
            okHttpClient.setReadTimeout(HTTP_TIMEOUT, TimeUnit.MILLISECONDS);
        }
        return okHttpClient;
    }

    private static InputStream trustedCertificatesInputStream() {
        String comodoRsaCertificationAuthority = ""
                + "-----BEGIN CERTIFICATE-----\n"
                + "MIIF2DCCA8CgAwIBAgIQTKr5yttjb+Af907YWwOGnTANBgkqhkiG9w0BAQwFADCB\n"
                + "hTELMAkGA1UEBhMCR0IxGzAZBgNVBAgTEkdyZWF0ZXIgTWFuY2hlc3RlcjEQMA4G\n"
                + "A1UEBxMHU2FsZm9yZDEaMBgGA1UEChMRQ09NT0RPIENBIExpbWl0ZWQxKzApBgNV\n"
                + "BAMTIkNPTU9ETyBSU0EgQ2VydGlmaWNhdGlvbiBBdXRob3JpdHkwHhcNMTAwMTE5\n"
                + "MDAwMDAwWhcNMzgwMTE4MjM1OTU5WjCBhTELMAkGA1UEBhMCR0IxGzAZBgNVBAgT\n"
                + "EkdyZWF0ZXIgTWFuY2hlc3RlcjEQMA4GA1UEBxMHU2FsZm9yZDEaMBgGA1UEChMR\n"
                + "Q09NT0RPIENBIExpbWl0ZWQxKzApBgNVBAMTIkNPTU9ETyBSU0EgQ2VydGlmaWNh\n"
                + "dGlvbiBBdXRob3JpdHkwggIiMA0GCSqGSIb3DQEBAQUAA4ICDwAwggIKAoICAQCR\n"
                + "6FSS0gpWsawNJN3Fz0RndJkrN6N9I3AAcbxT38T6KhKPS38QVr2fcHK3YX/JSw8X\n"
                + "pz3jsARh7v8Rl8f0hj4K+j5c+ZPmNHrZFGvnnLOFoIJ6dq9xkNfs/Q36nGz637CC\n"
                + "9BR++b7Epi9Pf5l/tfxnQ3K9DADWietrLNPtj5gcFKt+5eNu/Nio5JIk2kNrYrhV\n"
                + "/erBvGy2i/MOjZrkm2xpmfh4SDBF1a3hDTxFYPwyllEnvGfDyi62a+pGx8cgoLEf\n"
                + "Zd5ICLqkTqnyg0Y3hOvozIFIQ2dOciqbXL1MGyiKXCJ7tKuY2e7gUYPDCUZObT6Z\n"
                + "+pUX2nwzV0E8jVHtC7ZcryxjGt9XyD+86V3Em69FmeKjWiS0uqlWPc9vqv9JWL7w\n"
                + "qP/0uK3pN/u6uPQLOvnoQ0IeidiEyxPx2bvhiWC4jChWrBQdnArncevPDt09qZah\n"
                + "SL0896+1DSJMwBGB7FY79tOi4lu3sgQiUpWAk2nojkxl8ZEDLXB0AuqLZxUpaVIC\n"
                + "u9ffUGpVRr+goyhhf3DQw6KqLCGqR84onAZFdr+CGCe01a60y1Dma/RMhnEw6abf\n"
                + "Fobg2P9A3fvQQoh/ozM6LlweQRGBY84YcWsr7KaKtzFcOmpH4MN5WdYgGq/yapiq\n"
                + "crxXStJLnbsQ/LBMQeXtHT1eKJ2czL+zUdqnR+WEUwIDAQABo0IwQDAdBgNVHQ4E\n"
                + "FgQUu69+Aj36pvE8hI6t7jiY7NkyMtQwDgYDVR0PAQH/BAQDAgEGMA8GA1UdEwEB\n"
                + "/wQFMAMBAf8wDQYJKoZIhvcNAQEMBQADggIBAArx1UaEt65Ru2yyTUEUAJNMnMvl\n"
                + "wFTPoCWOAvn9sKIN9SCYPBMtrFaisNZ+EZLpLrqeLppysb0ZRGxhNaKatBYSaVqM\n"
                + "4dc+pBroLwP0rmEdEBsqpIt6xf4FpuHA1sj+nq6PK7o9mfjYcwlYRm6mnPTXJ9OV\n"
                + "2jeDchzTc+CiR5kDOF3VSXkAKRzH7JsgHAckaVd4sjn8OoSgtZx8jb8uk2Intzna\n"
                + "FxiuvTwJaP+EmzzV1gsD41eeFPfR60/IvYcjt7ZJQ3mFXLrrkguhxuhoqEwWsRqZ\n"
                + "CuhTLJK7oQkYdQxlqHvLI7cawiiFwxv/0Cti76R7CZGYZ4wUAc1oBmpjIXUDgIiK\n"
                + "boHGhfKppC3n9KUkEEeDys30jXlYsQab5xoq2Z0B15R97QNKyvDb6KkBPvVWmcke\n"
                + "jkk9u+UJueBPSZI9FoJAzMxZxuY67RIuaTxslbH9qh17f4a+Hg4yRvv7E491f0yL\n"
                + "S0Zj/gA0QHDBw7mh3aZw4gSzQbzpgJHqZJx64SIDqZxubw5lT2yHh17zbqD5daWb\n"
                + "QOhTsiedSrnAdyGN/4fy3ryM7xfft0kL0fJuMAsaDk527RH89elWsn2/x20Kk4yl\n"
                + "0MC2Hb46TpSi125sC8KKfPog88Tk5c0NqMuRkrF8hey1FGlmDoLnzc7ILaZRfyHB\n"
                + "NVOFBkpdn627G190\n"
                + "-----END CERTIFICATE-----\n";
        String entrustRootCertificateAuthority = ""
                + "-----BEGIN CERTIFICATE-----\n"
                + "MIIEkTCCA3mgAwIBAgIERWtQVDANBgkqhkiG9w0BAQUFADCBsDELMAkGA1UEBhMC\n"
                + "VVMxFjAUBgNVBAoTDUVudHJ1c3QsIEluYy4xOTA3BgNVBAsTMHd3dy5lbnRydXN0\n"
                + "Lm5ldC9DUFMgaXMgaW5jb3Jwb3JhdGVkIGJ5IHJlZmVyZW5jZTEfMB0GA1UECxMW\n"
                + "KGMpIDIwMDYgRW50cnVzdCwgSW5jLjEtMCsGA1UEAxMkRW50cnVzdCBSb290IENl\n"
                + "cnRpZmljYXRpb24gQXV0aG9yaXR5MB4XDTA2MTEyNzIwMjM0MloXDTI2MTEyNzIw\n"
                + "NTM0MlowgbAxCzAJBgNVBAYTAlVTMRYwFAYDVQQKEw1FbnRydXN0LCBJbmMuMTkw\n"
                + "NwYDVQQLEzB3d3cuZW50cnVzdC5uZXQvQ1BTIGlzIGluY29ycG9yYXRlZCBieSBy\n"
                + "ZWZlcmVuY2UxHzAdBgNVBAsTFihjKSAyMDA2IEVudHJ1c3QsIEluYy4xLTArBgNV\n"
                + "BAMTJEVudHJ1c3QgUm9vdCBDZXJ0aWZpY2F0aW9uIEF1dGhvcml0eTCCASIwDQYJ\n"
                + "KoZIhvcNAQEBBQADggEPADCCAQoCggEBALaVtkNC+sZtKm9I35RMOVcF7sN5EUFo\n"
                + "Nu3s/poBj6E4KPz3EEZmLk0eGrEaTsbRwJWIsMn/MYszA9u3g3s+IIRe7bJWKKf4\n"
                + "4LlAcTfFy0cOlypowCKVYhXbR9n10Cv/gkvJrT7eTNuQgFA/CYqEAOwwCj0Yzfv9\n"
                + "KlmaI5UXLEWeH25DeW0MXJj+SKfFI0dcXv1u5x609mhF0YaDW6KKjbHjKYD+JXGI\n"
                + "rb68j6xSlkuqUY3kEzEZ6E5Nn9uss2rVvDlUccp6en+Q3X0dgNmBu1kmwhH+5pPi\n"
                + "94DkZfs0Nw4pgHBNrziGLp5/V6+eF67rHMsoIV+2HNjnogQi+dPa2MsCAwEAAaOB\n"
                + "sDCBrTAOBgNVHQ8BAf8EBAMCAQYwDwYDVR0TAQH/BAUwAwEB/zArBgNVHRAEJDAi\n"
                + "gA8yMDA2MTEyNzIwMjM0MlqBDzIwMjYxMTI3MjA1MzQyWjAfBgNVHSMEGDAWgBRo\n"
                + "kORnpKZTgMeGZqTx90tD+4S9bTAdBgNVHQ4EFgQUaJDkZ6SmU4DHhmak8fdLQ/uE\n"
                + "vW0wHQYJKoZIhvZ9B0EABBAwDhsIVjcuMTo0LjADAgSQMA0GCSqGSIb3DQEBBQUA\n"
                + "A4IBAQCT1DCw1wMgKtD5Y+iRDAUgqV8ZyntyTtSx29CW+1RaGSwMCPeyvIWonX9t\n"
                + "O1KzKtvn1ISMY/YPyyYBkVBs9F8U4pN0wBOeMDpQ47RgxRzwIkSNcUesyBrJ6Zua\n"
                + "AGAT/3B+XxFNSRuzFVJ7yVTav52Vr2ua2J7p8eRDjeIRRDq/r72DQnNSi6q7pynP\n"
                + "9WQcCk3RvKqsnyrQ/39/2n3qse0wJcGE2jTSW3iDVuycNsMm4hH2Z0kdkquM++v/\n"
                + "eu6FSqdQgPCnXEqULl8FmTxSQeDNtGPPAUO6nIPcj2A781q0tHuu2guQOHXvgR1m\n"
                + "0vdXcDazv/wor3ElhVsT/h5/WrQ8\n"
                + "-----END CERTIFICATE-----\n";
        return new Buffer()
                .writeUtf8(comodoRsaCertificationAuthority)
                .writeUtf8(entrustRootCertificateAuthority)
                .inputStream();
    }

    private static SSLContext sslContextForTrustedCertificates(InputStream in) throws GeneralSecurityException {
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
            return (SSLContext) trustManagers[0];
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
