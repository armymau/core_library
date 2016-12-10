package core.connection.volley;

import android.content.Context;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;

import core.common_interface.CoreVolleyNetworkCallback;
import core.connection.model.GenericResponse;
import core.utils.CoreConstants;

public class CoreVolleyNetworkRequest extends JsonObjectRequest {

    private static String methodName;
    private static String url;
    private static WeakReference<Context> activityWeakReference;
    private static WeakReference<CoreVolleyNetworkCallback> callbackWeakReference;

    public CoreVolleyNetworkRequest(String methodName, String url, Context context, CoreVolleyNetworkCallback callback) {
        super(Method.GET, url, null, responseListener, errorListener);

        this.methodName = methodName;
        this.url = url;
        activityWeakReference = new WeakReference<>(context);
        callbackWeakReference = new WeakReference<>(callback);
    }

    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        HashMap<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json; charset=utf-8");
        return headers;
    }

    // here you can write a custom retry policy
    @Override
    public RetryPolicy getRetryPolicy() {
        return super.getRetryPolicy();
    }

    private final static Response.Listener<JSONObject> responseListener = new Response.Listener<JSONObject>() {
        @Override
        public void onResponse(JSONObject response) {
            Context mContext = activityWeakReference.get();
            CoreVolleyNetworkCallback volleyCallback = callbackWeakReference.get();
            if (mContext != null && volleyCallback != null) {

                if (CoreConstants.isDebug) {
                    Log.d(CoreConstants.TAG, ">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
                    Log.e(CoreConstants.TAG, methodName);
                    Log.d(CoreConstants.TAG, url);
                    Log.d(CoreConstants.TAG, "RESPONSE     : " + response.toString());
                }
                if (response != null) {
                    GenericResponse result;
                    Gson gson = new Gson();
                    result = gson.fromJson(response.toString(), GenericResponse.class);
                    if (result != null) {
                        volleyCallback.onSuccess(result);
                    } else {
                        volleyCallback.onFailed(result);
                    }
                }
            }
        }
    };


    private final static Response.ErrorListener errorListener = new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
            if (CoreConstants.isDebug) {
                Log.d(CoreConstants.TAG, ">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
                Log.e(CoreConstants.TAG, methodName);
                Log.d(CoreConstants.TAG, url);
                Log.d(CoreConstants.TAG, "ERROR     : " + (error.networkResponse != null ? error.networkResponse.statusCode : "networkResponse == null"));
            }
        }
    };
}
