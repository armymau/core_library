package core.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;
import android.os.Build;

public class ConnectionUtils {

    @SuppressWarnings("deprecation")
    public static boolean isNetworkAvailable(Context context) {
        try {
            ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                Network[] networks = connectivity.getAllNetworks();
                NetworkInfo networkInfo;
                for (Network mNetwork : networks) {
                    networkInfo = connectivity.getNetworkInfo(mNetwork);
                    if (networkInfo.getState().equals(NetworkInfo.State.CONNECTED)  &&
                            connectivity.getActiveNetworkInfo().isAvailable() &&
                            connectivity.getActiveNetworkInfo().isConnected()) {
                        return true;
                    }
                }
            } else {
                if (connectivity != null) {
                    NetworkInfo[] info = connectivity.getAllNetworkInfo();
                    if (info != null) {
                        for (NetworkInfo anInfo : info) {
                            if (anInfo.getState() == NetworkInfo.State.CONNECTED &&
                                    connectivity.getActiveNetworkInfo().isAvailable() &&
                                    connectivity.getActiveNetworkInfo().isConnected()) {
                                return true;
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            return false;
        }
        return false;
    }
}
