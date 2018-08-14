package core_kt.preference

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import core_kt.utils.TAG

private fun getPreferencesEditor(context: Context, mode: Int): SharedPreferences.Editor? {
    try {
        val sp = context.getSharedPreferences("core_preferences", mode)
        return sp.edit()
    } catch (e: Exception) {
        Log.e(TAG, e.toString())
    }
    return null
}

private fun getPreferences(context: Context): SharedPreferences? {
    try {
        return context.getSharedPreferences("core_preferences", Context.MODE_PRIVATE)
    } catch (e: Exception) {
        Log.e(TAG, e.toString())
    }
    return null
}

/** */
fun getBooleanPreference(context: Context, valueKey: String): Boolean {
    try {
        val _preferences = getPreferences(context)
        return _preferences != null && _preferences.getBoolean(valueKey, false)
    } catch (e: Exception) {
        e.printStackTrace()
    }

    return false
}

fun getStringPreference(context: Context, valueKey: String, defaultValue: String): String? {
    try {
        val _preferences = getPreferences(context)
        return _preferences?.getString(valueKey, defaultValue)
    } catch (e: Exception) {
        e.printStackTrace()
    }

    return null
}

fun getIntPreference(context: Context, valueKey: String): Int {
    try {
        val _preferences = getPreferences(context)
        return _preferences?.getInt(valueKey, 0) ?: 0
    } catch (e: Exception) {
        e.printStackTrace()
    }

    return 0
}

fun getLongPreference(context: Context, valueKey: String): Long {
    try {
        val _preferences = getPreferences(context)
        return _preferences?.getLong(valueKey, 0.toLong()) ?: 0
    } catch (e: Exception) {
        e.printStackTrace()
    }

    return 0
}

fun getFloatPreference(context: Context, valueKey: String, defaultValue: Float?): Float {
    try {
        val _preferences = getPreferences(context)
        return _preferences?.getFloat(valueKey, defaultValue!!) ?: 0f
    } catch (e: Exception) {
        e.printStackTrace()
    }
    return 0f
}


/** */
fun setBooleanPreference(context: Context, valueKey: String, value: Boolean) {
    val editor: SharedPreferences.Editor?
    try {
        editor = getPreferencesEditor(context, Context.MODE_PRIVATE)
        if (editor != null) {
            editor.putBoolean(valueKey, value)
            editor.apply()
        }
    } catch (e: Exception) {
        Log.e(TAG, "error in setBooleanPreference for key $valueKey and value $value", e)
    }

}

fun setStringPreference(context: Context, valueKey: String, value: String) {
    val editor: SharedPreferences.Editor?
    try {
        editor = getPreferencesEditor(context, Context.MODE_PRIVATE)
        if (editor != null) {
            editor.putString(valueKey, value)
            editor.apply()
        }
    } catch (e: Exception) {
        Log.e(TAG, "error in setStringPreference for key $valueKey and value $value", e)
    }

}

fun setIntPreference(context: Context, valueKey: String, value: Int) {
    val editor: SharedPreferences.Editor?
    try {
        editor = getPreferencesEditor(context, Context.MODE_PRIVATE)
        if (editor != null) {
            editor.putInt(valueKey, value)
            editor.apply()
        }
    } catch (e: Exception) {
        Log.e(TAG, "error in setIntPreference for key $valueKey and value $value", e)
    }

}

fun setLongPreference(context: Context, valueKey: String, value: Long) {
    val editor: SharedPreferences.Editor?
    try {
        editor = getPreferencesEditor(context, Context.MODE_PRIVATE)
        if (editor != null) {
            editor.putLong(valueKey, value)
            editor.apply()
        }
    } catch (e: Exception) {
        Log.e(TAG, "error in setLongPreference for key $valueKey and value $value", e)
    }

}

fun setFloatPreference(context: Context, valueKey: String, value: Float) {
    val editor: SharedPreferences.Editor?
    try {
        editor = getPreferencesEditor(context, Context.MODE_PRIVATE)
        if (editor != null) {
            editor.putFloat(valueKey, value)
            editor.apply()
        }
    } catch (e: Exception) {
        Log.e(TAG, "error in setFloatPreference for key $valueKey and value $value", e)
    }

}

fun resetSinglePreference(context: Context, valueKey: String) {
    val editor: SharedPreferences.Editor?
    try {
        editor = getPreferencesEditor(context, Context.MODE_PRIVATE)
        if (editor != null) {
            editor.remove(valueKey)
            editor.commit()
        }
    } catch (e: Exception) {
        Log.e(TAG, "error in clearPreference, $e")
    }

}

/** */
fun clearAllPreference(context: Context) {
    val editor: SharedPreferences.Editor?
    try {
        editor = getPreferencesEditor(context, Context.MODE_PRIVATE)
        if (editor != null) {
            editor.clear()
            editor.commit()
        }
    } catch (e: Exception) {
        Log.e(TAG, "error in clearPreference, $e")
    }

}