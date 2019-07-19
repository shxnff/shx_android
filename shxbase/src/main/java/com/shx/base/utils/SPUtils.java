package com.shx.base.utils;

import android.content.Context;
import android.content.SharedPreferences;
import com.shx.base.utils.provider.ImpSpProvider;
import java.util.HashSet;
import java.util.Set;

/**
 * 功能介绍：Sp工具类
 */

public class SPUtils {

    public static final String DEFAULE = "SHX_SP";

    public static boolean putString(Context context, String key, String value) {
        return putString(context, DEFAULE, key, value);
    }

    public static boolean putString(Context context, String name, String key, String value) {
        return setKeyAndValue(context, name, key, value);
    }

    public static String getString(Context context, String key) {
        return getString(context, key, null);
    }

    public static String getString(Context context, String key,
                                   String defaultValue) {
        return getString(context, DEFAULE, key, defaultValue);
    }

    public static String getString(Context context, String name, String key, String defaultValue) {
        return (String) getValue(context, name, key, String.class, defaultValue);
    }

    public static boolean putInt(Context context, String key, int value) {
        return putInt(context, DEFAULE, key, value);
    }

    public static boolean putInt(Context context, String name, String key, int value) {
        return setKeyAndValue(context, name, key, value);
    }

    public static int getInt(Context context, String key) {
        return getInt(context, key, -1);
    }

    public static int getInt(Context context, String key, int defaultValue) {
        return getInt(context, DEFAULE, key, defaultValue);
    }

    public static int getInt(Context context, String name, String key, int defaultValue) {
        return (int) getValue(context, name, key, Integer.class, defaultValue);
    }

    public static int getInt(Context context, String name, String key) {
        return (int) getValue(context, name, key, Integer.class, -1);
    }

    public static boolean putLong(Context context, String key, long value) {
        return putLong(context, DEFAULE, key, value);
    }

    public static boolean putLong(Context context, String name, String key, long value) {
        return setKeyAndValue(context, name, key, value);
    }

    public static long getLong(Context context, String key) {
        return getLong(context, key, -1);
    }

    public static long getLong(Context context, String key, long defaultValue) {
        return getLong(context, DEFAULE, key, defaultValue);
    }

    public static long getLong(Context context, String name, String key, long defaultValue) {
        return (long) getValue(context, name, key, Long.class, defaultValue);
    }

    public static long getLong(Context context, String name, String key) {
        return (long) getValue(context, name, key, Long.class, -1);
    }


    public static boolean putFloat(Context context, String key, float value) {
        return putFloat(context, DEFAULE, key, value);
    }

    public static boolean putFloat(Context context, String name, String key, float value) {
        return setKeyAndValue(context, name, key, value);
    }


    public static float getFloat(Context context, String key) {
        return getFloat(context, key, -1);
    }


    public static float getFloat(Context context, String key, float defaultValue) {
        return getFloat(context, DEFAULE, key, defaultValue);
    }

    public static float getFloat(Context context, String name, String key) {
        return (float) getValue(context, name, key, Float.class, -1);
    }

    public static float getFloat(Context context, String name, String key, float defaultValue) {
        return (float) getValue(context, name, key, Float.class, defaultValue);
    }


    public static boolean putBoolean(Context context, String key, boolean value) {
        return putBoolean(context, DEFAULE, key, value);
    }

    public static boolean putBoolean(Context context, String name, String key, boolean value) {
        return setKeyAndValue(context, name, key, value);
    }


    public static boolean getBoolean(Context context, String key) {
        return getBoolean(context, key, false);
    }

    public static boolean getBoolean(Context context, String key,
                                     boolean defaultValue) {
        return (boolean) getValue(context, DEFAULE, key, Boolean.class, defaultValue);
    }

    public static boolean getBoolean(Context context, String name, String key,
                                     boolean defaultValue) {
        return (boolean) getValue(context, name, key, Boolean.class, defaultValue);
    }

    public static boolean getBoolean(Context context, String name, String key) {
        return (boolean) getValue(context, name, key, Boolean.class, false);
    }


    public static boolean putSet(Context context, String key, Set value) {
        return putSet(context, DEFAULE, key, value);
    }

    public static boolean putSet(Context context, String name, String key, Set value) {
        return setKeyAndValue(context, name, key, value);
    }


    public static Set getSet(Context context, String key) {
        return getSet(context, key, new HashSet<String>());
    }

    public static Set getSet(Context context, String key,
                             Set<String> defaultValue) {
        return (Set) getValue(context, DEFAULE, key, Set.class, defaultValue);
    }

    public static Set getSet(Context context, String name, String key) {
        return (Set) getValue(context, name, key, Set.class, null);
    }

    public static Set getSet(Context context, String name, String key,
                             Set defaultValue) {
        return (Set) getValue(context, name, key, Set.class, defaultValue);
    }

    public static boolean remove(Context context, String key) {
        return remove(context, DEFAULE, key);
    }

    public static boolean remove(Context context, String name, String key) {
        if (AppBaseUtils.isMainProcess()) {
            SharedPreferences.Editor editor = getSP(context, name).edit();
            editor.remove(key);
            return editor.commit();
        } else {
            //其他进程，使用ContentProvider
            return ImpSpProvider.removeToProvider(context, name, key);
        }
    }

    public static boolean clear(Context context) {
        return clear(context, DEFAULE);
    }


    public static boolean clear(Context context, String name) {

        if (AppBaseUtils.isMainProcess()) {
            SharedPreferences sp = getSP(context, name);
            SharedPreferences.Editor editor = sp.edit();
            editor.clear();
            return editor.commit();
        } else {
            //其他进程，使用ContentProvider
            try {
                return ImpSpProvider.clearToProvider(context, name);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    /**
     * @return 对应值
     */
    public static Object getValue(Context context,
                                  String name,
                                  String key, Class type, Object defaule) {
        if (context == null) {
            return false;
        }
        if (AppBaseUtils.isMainProcess()) {
            SharedPreferences sp = getSP(context, name);
            if (sp == null) {
                return defaule;
            }
            if (type.isAssignableFrom(String.class)) {
                return sp.getString(key, (String) defaule);
            } else if (type.isAssignableFrom(Integer.class)) {
                return sp.getInt(key, (Integer) defaule);
            } else if (type.isAssignableFrom(Boolean.class)) {
                return sp.getBoolean(key, false);
            } else if (type.isAssignableFrom(Float.class)) {
                return sp.getFloat(key, (Float) defaule);
            } else if (type.isAssignableFrom(Long.class)) {
                return sp.getLong(key, (Long) defaule);
            } else if (type.isAssignableFrom(Set.class)) {
                return sp.getStringSet(key, (Set<String>) defaule);
            }
        } else {
            //其他进程，使用ContentProvider
            try {
                return ImpSpProvider.getValueToProvider(context, name, key, type, defaule);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return defaule;
    }

    public static boolean setKeyAndValue(Context context, String key, Object value) {
        return setKeyAndValue(context, DEFAULE, key, value);
    }

    public static boolean setKeyAndValue(Context context, String name, String key, Object value) {
        if (context == null) {
            return false;
        }
        if (AppBaseUtils.isMainProcess()) {
            SharedPreferences sp = getSP(context, name);
            if (sp == null) {
                return false;
            }
            SharedPreferences.Editor editor = sp.edit();
            if (value instanceof String) {
                editor.putString(key, (String) value);
            } else if (value instanceof Integer) {
                editor.putInt(key, (Integer) value);
            } else if (value instanceof Boolean) {
                editor.putBoolean(key, (Boolean) value);
            } else if (value instanceof Float) {
                editor.putFloat(key, (Float) value);
            } else if (value instanceof Long) {
                editor.putLong(key, (Long) value);
            } else if (value instanceof Set) {
                editor.putStringSet(key, (Set) value);
            }
            return editor.commit();
        } else {
            //其他进程，使用ContentProvider
            try {
                return ImpSpProvider.setValueToProvider(context, name, key, value);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
        return false;
    }

    public static SharedPreferences getSP(Context context) {
        return context.getSharedPreferences(DEFAULE, Context.MODE_PRIVATE);
    }

    public static SharedPreferences getSP(Context context, String name) {
        return context.getSharedPreferences(name, Context.MODE_PRIVATE);
    }

}
