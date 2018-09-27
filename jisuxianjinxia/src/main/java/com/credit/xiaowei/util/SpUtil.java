package com.credit.xiaowei.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import com.credit.xiaowei.app.App;

import net.grandcentrix.tray.AppPreferences;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;


/**
 * ClassName：SpUtil<p>
 * Author：xiejingwen
 * Fuction：SharedPreferences工具<p>
 */
public class SpUtil {
    private static AppPreferences appPreferences;


    public static String getString(String key) {
        return getSharedPreferences().getString(key, "");
    }

    public static void putString(String key, String value) {
        getSharedPreferences().put(key, value);
    }

    public static boolean getBoolean(String key) {
        return getSharedPreferences().getBoolean(key, false);
    }

    public static boolean getBoolean(String key, boolean defValue) {
        return getSharedPreferences().getBoolean(key, false);
    }

    public static void putBoolean(String key, boolean value) {
        getSharedPreferences().put(key, value);
    }

    public static int getInt(String key) {
        return getInt(key, 0);
    }

    public static int getInt(String key, int value) {
        return getSharedPreferences().getInt(key, value);
    }

    public static void putInt(String key, int value) {
        getSharedPreferences().put(key, value);
    }

    public static long getLong(String key) {
        return getSharedPreferences().getLong(key, 0);
    }

    public static void putLong(String key, long value) {
        getSharedPreferences().put(key, value);
    }

    public static void remove(String key) {
        getSharedPreferences().remove(key);
    }

    public static AppPreferences getSharedPreferences() {
        if (appPreferences == null) {
            appPreferences = new AppPreferences(App.getContext());
        }
        return appPreferences;
    }


    /**
     * 存储List<String>
     *
     * @param key     List<String>对应的key
     * @param strList 对应需要存储的List<String>
     */
    public static void putStrListValue(String key,
                                       List<String> strList) {
        if (null == strList) {
            return;
        }
        // 保存之前先清理已经存在的数据，保证数据的唯一性
        removeStrList(key);
        int size = strList.size();
        putInt(key + "size", size);
        for (int i = 0; i < size; i++) {
            putString(key + i, strList.get(i));
        }
    }

    /**
     * 取出List<String>
     *
     * @param key List<String> 对应的key
     * @return List<String>
     */
    public static List<String> getStrListValue(String key) {
        List<String> strList = new ArrayList<String>();
        int size = getInt(key + "size", 0);
        //Log.d("sp", "" + size);
        for (int i = 0; i < size; i++) {
            strList.add(getString(key + i));
        }
        return strList;
    }

    /**
     * 清空List<String>所有数据
     *
     * @param key List<String>对应的key
     */
    public static void removeStrList(String key) {
        int size = getInt(key + "size", 0);
        if (0 == size) {
            return;
        }
        remove(key + "size");
        for (int i = 0; i < size; i++) {
            remove(key + i);
        }
    }

    //存储list
    public static <T> void putList(String key, List<T> list) {
        if (null != list) {
            Gson gson = new Gson();
            String data = gson.toJson(list);
            putString(key, data);
        }
    }

    /**
     * 获取List
     *
     * @param tag
     * @return
     */
    public static <T> List<T> getDataList(String tag) {
        List<T> datalist = new ArrayList<T>();
        String strJson = getString(tag);
        if (null == strJson) {
            return datalist;
        }
        Gson gson = new Gson();
        datalist = gson.fromJson(strJson, new TypeToken<List<T>>() {
        }.getType());
        return datalist;
    }


    /**
     * 用于保存集合
     *
     * @param key key
     * @param map map数据
     * @return 保存结果
     */
    public static <K, V> boolean putHashMapData(String key, Map<K, V> map) {
        boolean result;
        try {
            Gson gson = new Gson();
            String json = gson.toJson(map);
            putString(key, json);
            result = true;
        } catch (Exception e) {
            result = false;
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 用于获取hashMap
     *
     * @param key key
     * @return HashMap
     */
    public static <V> HashMap<String, V> getHashMapData(String key, Class<V> clsV) {
        String json = getString(key);
        if(TextUtils.isEmpty(json)) return null;
        HashMap<String, V> map = new HashMap<>();
        Gson gson = new Gson();
        JsonObject obj = new JsonParser().parse(json).getAsJsonObject();
        Set<Map.Entry<String, JsonElement>> entrySet = obj.entrySet();
        for (Map.Entry<String, JsonElement> entry : entrySet) {
            String entryKey = entry.getKey();
            JsonElement value = entry.getValue();
            map.put(entryKey, gson.fromJson(value, clsV));
        }
        return map;
    }

//    /**
//     * 用于获取hashMap
//     *
//     * @param key key
//     * @return HashMap
//     */
//    public static <V> HashMap<String, V> getHashMapData(String key, Type type) {
//        String json = getString(key);
//        if(TextUtils.isEmpty(json)) return null;
//        HashMap<String, V> map = new HashMap<>();
//        Gson gson = new Gson();
//        JsonObject obj = new JsonParser().parse(json).getAsJsonObject();
//        Set<Map.Entry<String, JsonElement>> entrySet = obj.entrySet();
//        for (Map.Entry<String, JsonElement> entry : entrySet) {
//            String entryKey = entry.getKey();
//            JsonElement value = entry.getValue();
//            map.put(entryKey, (V) gson.fromJson(value, type));
//        }
//
////        HashMap<String, V> map = new HashMap<>();
////        Gson gson = new Gson();
////        map = gson.fromJson(json, new TypeToken<Map<String, V>>() {
////        }.getType());
////        for (String mapKey : map.keySet()) {
////            JsonElement jsonElement = (JsonElement) map.keySet();
////            map.put(mapKey, (V) gson.fromJson(jsonElement.toString(), type));
////        }
//        return map;
//    }

    /**
     * 用于获取hashMap
     *
     * @param key key
     * @return HashMap
     */
    public static HashMap<String, List<String>> getHashMapData(String key, Type type) {
        String json = getString(key);
        if(TextUtils.isEmpty(json)) return null;
        HashMap<String, List<String>> map = new HashMap<>();
        Gson gson = new Gson();
        JsonObject obj = new JsonParser().parse(json).getAsJsonObject();
        Set<Map.Entry<String, JsonElement>> entrySet = obj.entrySet();
        for (Map.Entry<String, JsonElement> entry : entrySet) {
            String entryKey = entry.getKey();
            JsonElement value = entry.getValue();
            map.put(entryKey, (List<String>) gson.fromJson(value, type));
        }
        return map;
    }
}
