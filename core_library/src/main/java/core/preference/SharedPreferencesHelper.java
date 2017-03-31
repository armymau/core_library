package core.preference;


import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.Log;

import core.utils.CoreConstants;

public class SharedPreferencesHelper {

    private static Editor getPreferencesEditor(Context context, int mode) {
        try {
            SharedPreferences sp = context.getSharedPreferences("core_preferences", mode);
            return sp.edit();
        } catch (Exception e) {
            Log.e(CoreConstants.TAG, e.toString());
        }
        return null;
    }

    private static SharedPreferences getPreferences(Context context) {
        try {
            return context.getSharedPreferences("core_preferences", Context.MODE_PRIVATE);
        } catch (Exception e) {
            Log.e(CoreConstants.TAG, e.toString());
        }
        return null;
    }

    /***/
    public static boolean getBooleanPreference(Context context, String valueKey) {
        try {
            SharedPreferences _preferences = getPreferences(context);
            return _preferences != null && _preferences.getBoolean(valueKey, false);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public static String getStringPreference(Context context, String valueKey, String defaultValue) {
        try {
            SharedPreferences _preferences = getPreferences(context);
            return _preferences != null ? _preferences.getString(valueKey, defaultValue) : null;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static int getIntPreference(Context context, String valueKey) {
        try {
            SharedPreferences _preferences = getPreferences(context);
            return _preferences != null ? _preferences.getInt(valueKey, 0) : 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static long getLongPreference(Context context, String valueKey) {
        try {
            SharedPreferences _preferences = getPreferences(context);
            return _preferences != null ? _preferences.getLong(valueKey, (long) 0) : 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static float getFloatPreference(Context context, String valueKey, Float defaultValue) {
        try {
            SharedPreferences _preferences = getPreferences(context);
            return _preferences != null ? _preferences.getFloat(valueKey, defaultValue) : 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }


    /***/
    public static void setBooleanPreference(Context context, String valueKey, boolean value) {
        Editor editor;
        try {
            editor = SharedPreferencesHelper.getPreferencesEditor(context, Context.MODE_PRIVATE);
            if (editor != null) {
                editor.putBoolean(valueKey, value);
                editor.apply();
            }
        } catch (Exception e) {
            Log.e(CoreConstants.TAG, "error in setBooleanPreference for key " + valueKey + " and value " + value, e);
        }
    }

    public static void setStringPreference(Context context, String valueKey, String value) {
        Editor editor;
        try {
            editor = SharedPreferencesHelper.getPreferencesEditor(context, Context.MODE_PRIVATE);
            if (editor != null) {
                editor.putString(valueKey, value);
                editor.apply();
            }
        } catch (Exception e) {
            Log.e(CoreConstants.TAG, "error in setStringPreference for key " + valueKey + " and value " + value, e);
        }
    }

    public static void setIntPreference(Context context, String valueKey, int value) {
        Editor editor;
        try {
            editor = getPreferencesEditor(context, Context.MODE_PRIVATE);
            if (editor != null) {
                editor.putInt(valueKey, value);
                editor.apply();
            }
        } catch (Exception e) {
            Log.e(CoreConstants.TAG, "error in setIntPreference for key " + valueKey + " and value " + value, e);
        }
    }

    public static void setLongPreference(Context context, String valueKey, long value) {
        Editor editor;
        try {
            editor = getPreferencesEditor(context, Context.MODE_PRIVATE);
            if (editor != null) {
                editor.putLong(valueKey, value);
                editor.apply();
            }
        } catch (Exception e) {
            Log.e(CoreConstants.TAG, "error in setLongPreference for key " + valueKey + " and value " + value, e);
        }
    }

    public static void setFloatPreference(Context context, String valueKey, float value) {
        Editor editor;
        try {
            editor = getPreferencesEditor(context, Context.MODE_PRIVATE);
            if (editor != null) {
                editor.putFloat(valueKey, value);
                editor.apply();
            }
        } catch (Exception e) {
            Log.e(CoreConstants.TAG, "error in setFloatPreference for key " + valueKey + " and value " + value, e);
        }
    }

    public static void resetSinglePreference(Context context, String valueKey) {
        Editor editor;
        try {
            editor = getPreferencesEditor(context, Context.MODE_PRIVATE);
            if (editor != null) {
                editor.remove(valueKey);
                editor.commit();
            }
        } catch (Exception e) {
            Log.e(CoreConstants.TAG, "error in clearPreference, " + e);
        }
    }

    /***/
    public static void clearAllPreference(Context context) {
        Editor editor;
        try {
            editor = getPreferencesEditor(context, Context.MODE_PRIVATE);
            if (editor != null) {
                editor.clear();
                editor.commit();
            }
        } catch (Exception e) {
            Log.e(CoreConstants.TAG, "error in clearPreference, " + e);
        }
    }
}