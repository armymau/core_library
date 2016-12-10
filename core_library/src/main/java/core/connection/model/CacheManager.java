package core.connection.model;

import android.content.Context;
import android.content.ContextWrapper;
import java.net.URL;
import core.utils.CoreConstants;


public class CacheManager {

    /* il metodo salverà un oggetto (deve essere un oggetto scrivibile su file) nella
    cache, il nome del file sarà dato dal metodo di comodo “getIdentifierForRequest” */
    public static void saveCachedObjectForRequest(Context context, URL request, Object object) {
        context.getSharedPreferences(CoreConstants.TAG, Context.MODE_PRIVATE).edit().putString(getIdentifierForRequest(request), object.toString()).apply();
    }

    /* 	il metodo recupererà un oggetto precedentemente salvato in cache, il nome
    del file da cui recuperare l’oggetto sarà dato da “getIdentifierForRequest” */
    public static Object getCachedObjectForRequest(Context context, URL request) {
        try {
            String jsonParams = new ContextWrapper(context).getSharedPreferences(CoreConstants.TAG, Context.MODE_PRIVATE).getString(getIdentifierForRequest(request), "");
            if (!jsonParams.isEmpty()) {
                return jsonParams;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /* il metodo ritornerà una stringa univoca a partire da una richiesta Http */
    private static String getIdentifierForRequest(URL request) {
        return request.toString();
    }
}
