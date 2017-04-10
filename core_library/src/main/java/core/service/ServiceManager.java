package core.service;

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

import core.connection.model.CacheManager;
import core.connection.model.GenericResponse;
import core.connection.ok_client.CustomOkHttpClient;
import core.utils.ConnectionUtils;
import core.utils.CoreConstants;

@SuppressWarnings("ALL")
public class ServiceManager extends ProgressAsyncTask {

    private String response;
    private RequestBody formBody;
    private boolean shouldLoadCache;
    private Activity activity;
    private String endPointUrl = "";
    private int http_method;
    private String methodName;
    private HashMap<String, String> authorizations;
    private MultipartBuilder formEncodingBuilder;

    public ServiceManager(String methodName, int http_method, Activity activity, String endPointUrl, Map<String, Object> parameters, HashMap<String, String> authorizations, MultipartBuilder formEncodingBuilder, boolean shouldLoadCache, boolean isVisibleProgress) {
        super(activity, isVisibleProgress);

        this.methodName = methodName;
        this.http_method = http_method;
        this.activity = activity;
        this.shouldLoadCache = shouldLoadCache;
        this.formEncodingBuilder = formEncodingBuilder;
        response = null;

        if (http_method == CoreConstants.HTTP_METHOD_GET) {
            this.endPointUrl = endPointUrl + prepareParametersForGetMethod(parameters);
        } else if (http_method == CoreConstants.HTTP_METHOD_POST) {
            this.endPointUrl = endPointUrl;
            this.formBody = prepareParametersForPostMethod(parameters);
        } else if(http_method == CoreConstants.HTTP_METHOD_GET_WITH_AUTHORIZATION) {
            this.endPointUrl = endPointUrl;
            this.authorizations = authorizations;
        } else if (http_method == CoreConstants.HTTP_METHOD_POST_WITH_MULTIPART) {
            this.endPointUrl = endPointUrl;
            this.formBody = prepareParametersForPostMethodWithMultipart(parameters, formEncodingBuilder);
        } else if(http_method == CoreConstants.HTTP_METHOD_GET_WITH_MULTI_AUTHORIZATION) {
            this.endPointUrl = endPointUrl;
            this.authorizations = authorizations;
        } else if(http_method == CoreConstants.HTTP_METHOD_POST_WITH_MULTI_AUTHORIZATION) {
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
                response = (String) CacheManager.getCachedObjectForRequest(activity, new URL(endPointUrl));
            } catch (Exception e) {
                Log.e(CoreConstants.TAG, CoreConstants.HTTP_METHOD_REPONSE_CACHING_FAILED + " for " + methodName);
                e.printStackTrace();
            }
        }
    }

    @Override
    protected String doInBackground(String... params) {
        String result = null;
        if (shouldLoadCache) {
            if (response != null) {
                publishProgress(response);
            }
        }
        try {
            String responseNow = callEndPoint(shouldLoadCache);
            if (responseNow != null) {
                if (!CoreConstants.isDebug && !CoreConstants.isSigned) {
                    Log.d(CoreConstants.TAG, "REMOTE RESPONSE : \n" + responseNow);
                    Log.d(CoreConstants.TAG, ">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
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
        return result;
    }

    private String callEndPoint(boolean shouldLoadCache) throws MalformedURLException {
        String response = null;
        if (ConnectionUtils.isNetworkAvailable(activity)) {
            try {
                if (http_method == CoreConstants.HTTP_METHOD_GET) {
                    response = CustomOkHttpClient.doGetRequest(methodName, endPointUrl);

                } else if (http_method == CoreConstants.HTTP_METHOD_POST) {
                    response = CustomOkHttpClient.doPostRequest(methodName, endPointUrl, formBody);

                } else if (http_method == CoreConstants.HTTP_METHOD_GET_WITH_AUTHORIZATION) {
                    response = CustomOkHttpClient.doGetRequestWithHeader(methodName, endPointUrl, authorizations.get("Authorization"));

                } else if (http_method == CoreConstants.HTTP_METHOD_POST_WITH_MULTIPART) {
                    response = CustomOkHttpClient.doPostRequestWithMultipart(methodName, endPointUrl, formBody);

                } else if (http_method == CoreConstants.HTTP_METHOD_GET_WITH_MULTI_AUTHORIZATION) {
                    response = CustomOkHttpClient.doGetRequestWithMultiHeader(methodName, endPointUrl, authorizations);

                } else if (http_method == CoreConstants.HTTP_METHOD_POST_WITH_MULTI_AUTHORIZATION) {
                    response = CustomOkHttpClient.doPostRequestWithMultiHeader(methodName, endPointUrl, authorizations, formBody);

                }
                if (response != null) {
                    Gson gson = new Gson();
                    GenericResponse result = gson.fromJson(response, GenericResponse.class);
                    if (shouldLoadCache) {
                        if (result != null && result.getStatus() == 200) {
                            CacheManager.saveCachedObjectForRequest(activity, new URL(endPointUrl), response);
                        } else {
                            response = (String) CacheManager.getCachedObjectForRequest(activity, new URL(endPointUrl));
                        }
                    }
                } else {
                    if (shouldLoadCache) {
                        Log.e(CoreConstants.TAG, CoreConstants.HTTP_METHOD_CACHED + " for " + methodName);
                        response = (String) CacheManager.getCachedObjectForRequest(activity, new URL(endPointUrl));
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        Intent intent = new Intent();
        intent.setAction("core.library.NO_CONNECTION_ACTION");
        intent.putExtra("isNetworkAvailable", ConnectionUtils.isNetworkAvailable(activity));
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
