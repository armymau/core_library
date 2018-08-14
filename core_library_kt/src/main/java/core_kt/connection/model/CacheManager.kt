package core_kt.connection.model

import android.content.Context
import core_kt.preference.getStringPreference
import core_kt.utils.TAG
import java.net.URL


/* 	il metodo recupererà un oggetto precedentemente salvato in cache, il nome del file da cui recuperare l’oggetto sarà dato da “getIdentifierForRequest” */
fun getCachedObjectForRequest(context: Context, request: URL): Any? {
    try {
        val jsonParams = getStringPreference(context, getIdentifierForRequest(request), "")
        if (!jsonParams!!.isEmpty()) {
            return jsonParams
        }
    } catch (e: Exception) {
        e.printStackTrace()
    }
    return null
}

/* il metodo salverà un oggetto (deve essere un oggetto scrivibile su file) nella cache, il nome del file sarà dato dal metodo di comodo “getIdentifierForRequest” */
fun saveCachedObjectForRequest(context: Context, request: URL, obj: Any) {
    context.getSharedPreferences(TAG, Context.MODE_PRIVATE).edit().putString(getIdentifierForRequest(request), obj.toString()).apply()
}

/* il metodo ritornerà una stringa univoca a partire da una richiesta Http */
fun getIdentifierForRequest(request: URL): String {
    return request.toString()
}