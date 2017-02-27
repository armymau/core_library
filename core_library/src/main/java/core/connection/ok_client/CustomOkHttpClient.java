package core.connection.ok_client;

import android.util.Log;

import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import core.utils.CoreConstants;

public class CustomOkHttpClient {

    private static OkHttpClient okHttpClient;
    private static final int HTTP_TIMEOUT = 7 * 1000; // milliseconds
    private static final MediaType MEDIA_TYPE_PNG = MediaType.parse("image/png");

    private static OkHttpClient getOkHttpClient() {
        if (okHttpClient == null) {
            okHttpClient = new OkHttpClient();
            okHttpClient.setConnectTimeout(HTTP_TIMEOUT, TimeUnit.MILLISECONDS);
            okHttpClient.setReadTimeout(HTTP_TIMEOUT, TimeUnit.MILLISECONDS);
        }
        return okHttpClient;
    }

    public static String doGetRequest(String methodName, String url) {
        Response response;
        try {
            Request request = new Request.Builder()
                    .url(url)
                    .build();

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


    public static String doGetRequestWithHeader(String methodName, String url, String authorization) {
        Response response;
        try {
            Request request = new Request.Builder()
                    .url(url)
                    .get()
                    .addHeader("Authorization", authorization)
                    .build();

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

    public static String doPostRequestWithMultipart(String methodName, String url, RequestBody formBody) {
        Response response;
        try {
            Request request = new Request.Builder()
                    .url(url)
                    .post(formBody)
                    .build();

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
