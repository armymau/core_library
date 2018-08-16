package core_kt.service;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;

import com.google.gson.Gson;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.MultipartBuilder;
import com.squareup.okhttp.RequestBody;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import core_kt.connection.model.GenericResponse;

import static com.android.volley.VolleyLog.TAG;
import static core_kt.connection.model.CacheManagerKt.getCachedObjectForRequest;
import static core_kt.connection.model.CacheManagerKt.saveCachedObjectForRequest;
import static core_kt.connection.ok_client.CustomOkHttpClientKt.doGetRequest;
import static core_kt.connection.ok_client.CustomOkHttpClientKt.doGetRequestWithHeader;
import static core_kt.connection.ok_client.CustomOkHttpClientKt.doGetRequestWithMultiHeader;
import static core_kt.connection.ok_client.CustomOkHttpClientKt.doPostRequest;
import static core_kt.connection.ok_client.CustomOkHttpClientKt.doPostRequestWithMultiHeader;
import static core_kt.connection.ok_client.CustomOkHttpClientKt.doPostRequestWithMultipart;
import static core_kt.utils.ConnectionUtilsKt.isNetworkAvailable;
import static core_kt.utils.CoreConstantsKt.HTTP_METHOD_CACHED;
import static core_kt.utils.CoreConstantsKt.HTTP_METHOD_GET;
import static core_kt.utils.CoreConstantsKt.HTTP_METHOD_GET_WITH_AUTHORIZATION;
import static core_kt.utils.CoreConstantsKt.HTTP_METHOD_GET_WITH_MULTI_AUTHORIZATION;
import static core_kt.utils.CoreConstantsKt.HTTP_METHOD_POST;
import static core_kt.utils.CoreConstantsKt.HTTP_METHOD_POST_WITH_MULTIPART;
import static core_kt.utils.CoreConstantsKt.HTTP_METHOD_POST_WITH_MULTI_AUTHORIZATION;
import static core_kt.utils.CoreConstantsKt.HTTP_METHOD_REPONSE_CACHING_FAILED;
import static core_kt.utils.CoreConstantsKt.isDebug;
import static core_kt.utils.CoreConstantsKt.isSigned;

@SuppressWarnings("ALL")
public class ServiceManager <T> extends ProgressAsyncTask {

    private String response;
    private T clazz;
    private RequestBody formBody;
    private boolean shouldLoadCache;
    private Activity activity;
    private String endPointUrl = "";
    private int http_method;
    private String methodName;
    private HashMap<String, String> authorizations;
    private MultipartBuilder formEncodingBuilder;

    public ServiceManager(String methodName, int http_method, Activity activity, String endPointUrl, Map<String, Object> parameters, HashMap<String, String> authorizations, MultipartBuilder formEncodingBuilder, boolean shouldLoadCache, boolean isVisibleProgress, T clazz) {
        super(activity, isVisibleProgress);

        this.methodName = methodName;
        this.http_method = http_method;
        this.activity = activity;
        this.shouldLoadCache = shouldLoadCache;
        this.formEncodingBuilder = formEncodingBuilder;
        this.clazz = clazz;
        response = null;

        if (http_method == HTTP_METHOD_GET) {
            this.endPointUrl = endPointUrl + prepareParametersForGetMethod(parameters);
        } else if (http_method == HTTP_METHOD_POST) {
            this.endPointUrl = endPointUrl;
            this.formBody = prepareParametersForPostMethod(parameters);
        } else if(http_method == HTTP_METHOD_GET_WITH_AUTHORIZATION) {
            this.endPointUrl = endPointUrl;
            this.authorizations = authorizations;
        } else if (http_method == HTTP_METHOD_POST_WITH_MULTIPART) {
            this.endPointUrl = endPointUrl;
            this.formBody = prepareParametersForPostMethodWithMultipart(parameters, formEncodingBuilder);
        } else if(http_method == HTTP_METHOD_GET_WITH_MULTI_AUTHORIZATION) {
            this.endPointUrl = endPointUrl;
            this.authorizations = authorizations;
        } else if(http_method == HTTP_METHOD_POST_WITH_MULTI_AUTHORIZATION) {
            this.endPointUrl = endPointUrl;
            this.authorizations = authorizations;
            this.formBody = prepareParametersForPostMethodWithJsonType(parameters);
        }
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        if (shouldLoadCache) {
            try {
                response = (String) getCachedObjectForRequest(activity, new URL(endPointUrl));
            } catch (Exception e) {
                Log.e(TAG, HTTP_METHOD_REPONSE_CACHING_FAILED + " for " + methodName);
                e.printStackTrace();
            }
        }
    }

    @Override
    protected Object doInBackground(Object[] objects) {
        return doInBackground();
    }

    protected T doInBackground(Void... objects) {
        String result = null;
        if (shouldLoadCache) {
            if (response != null) {
                publishProgress(response);
            }
        }
        try {
            String responseNow = callEndPoint(shouldLoadCache);
            if (responseNow != null) {
                if (!isDebug && !isSigned) {
                    Log.d(TAG, "REMOTE RESPONSE : \n" + responseNow);
                    Log.d(TAG, ">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
                }
                result = responseNow;
            } else {
                if (shouldLoadCache) {
                    result = response;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        Gson gson = new Gson();
        T objectResponse;
        try {
            objectResponse = (T) gson.fromJson(result, (Class<Object>) clazz);
        } catch (Exception e) {
            return null;
        }
        return objectResponse;

        //return result;
    }

    private String callEndPoint(boolean shouldLoadCache) throws MalformedURLException {
        String response = null;
        if (isNetworkAvailable(activity)) {
            try {
                if (http_method == HTTP_METHOD_GET) {
                    response = doGetRequest(methodName, endPointUrl);

                } else if (http_method == HTTP_METHOD_POST) {
                    response = doPostRequest(methodName, endPointUrl, formBody);

                } else if (http_method == HTTP_METHOD_GET_WITH_AUTHORIZATION) {
                    response = doGetRequestWithHeader(methodName, endPointUrl, authorizations.get("Authorization"));

                } else if (http_method == HTTP_METHOD_POST_WITH_MULTIPART) {
                    response = doPostRequestWithMultipart(methodName, endPointUrl, formBody);

                } else if (http_method == HTTP_METHOD_GET_WITH_MULTI_AUTHORIZATION) {
                    response = doGetRequestWithMultiHeader(methodName, endPointUrl, authorizations);

                } else if (http_method == HTTP_METHOD_POST_WITH_MULTI_AUTHORIZATION) {
                    response = doPostRequestWithMultiHeader(methodName, endPointUrl, authorizations, formBody);

                }
                if (response != null) {
                    Gson gson = new Gson();
                    GenericResponse result = gson.fromJson(response, GenericResponse.class);
                    if (shouldLoadCache) {
                        if (result != null && result.getStatus() == 200) {
                            saveCachedObjectForRequest(activity, new URL(endPointUrl), response);
                        } else {
                            response = (String) getCachedObjectForRequest(activity, new URL(endPointUrl));
                        }
                    }
                } else {
                    if (shouldLoadCache) {
                        Log.e(TAG, HTTP_METHOD_CACHED + " for " + methodName);
                        response = (String) getCachedObjectForRequest(activity, new URL(endPointUrl));
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        Intent intent = new Intent();
        intent.setAction("core.library.NO_CONNECTION_ACTION");
        intent.putExtra("isNetworkAvailable", isNetworkAvailable(activity));
        activity.sendBroadcast(intent);
        return response;
    }

    public static String prepareParametersForGetMethod(Map<String, Object> parameters) {
        String param = "";
        List<String> keys = new ArrayList<>(parameters.keySet());
        List<Object> values = new ArrayList<>(parameters.values());
        for (int i = 0; i < keys.size(); i++) {
            param += "&" + keys.get(i) + "=" + values.get(i);
        }
        return param;
    }

    public static RequestBody prepareParametersForPostMethod(Map<String, Object> parameters) {
        FormEncodingBuilder formEncodingBuilder = new FormEncodingBuilder();
        List<String> keys = new ArrayList<>(parameters.keySet());
        List<Object> values = new ArrayList<>(parameters.values());

        for (int i = 0; i < keys.size(); i++) {
            formEncodingBuilder.add(keys.get(i), (String) values.get(i));
        }
        RequestBody formBody = null;
        try {
            formBody = formEncodingBuilder.build();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return formBody;
    }

    public static RequestBody prepareParametersForPostMethodWithMultipart(Map<String, Object> parameters, MultipartBuilder formEncodingBuilder) {
        RequestBody formBody = null;
        try {
            formBody = formEncodingBuilder.type(MultipartBuilder.FORM).build();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return formBody;
    }

    public static RequestBody prepareParametersForPostMethodWithJsonType(Map<String, Object> parameters) {
        RequestBody formBody = null;
        try {
            Gson gson = new Gson();

            MediaType mediaType = MediaType.parse("application/json");

            if(parameters.size() == 0) {
                formBody = RequestBody.create(null, new byte[]{});
            }

            Iterator it = parameters.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry pair = (Map.Entry) it.next();
                formBody = RequestBody.create(mediaType, (String) pair.getValue());
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return formBody;
    }
}
