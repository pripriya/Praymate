package com.geval6.praymate.RequestManager;

import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import org.json.JSONArray;
import org.json.JSONObject;

public class HKFunctions {
    public static Object objectFromJson(String jsonString) {
        Log.i("++++",jsonString);
        try {
            Object object;
            if (getJsonType(jsonString) == JSONObject.class) {
                object = new JSONObject(jsonString);
            } else {
                 object = new JSONArray(jsonString);
            }

            if (object.getClass() == JSONObject.class) {
                return hashMapFromJsonObject((JSONObject) object);
            }

            else if (object.getClass() != JSONArray.class) {
                return object;
            } else {
                return arrayListFromJsonArray((JSONArray) object);
            }
        } catch (Exception e) {
            return null;
        }
    }

    private static Class getJsonType(String jsonString) {
        if (jsonString.trim().substring(0, 1).equalsIgnoreCase("{")) {
            return JSONObject.class;
        }
        return JSONArray.class;
    }

    private static HashMap hashMapFromJsonObject(JSONObject jsonObject) {
        HashMap<String, Object> hashMap = new HashMap();
        Iterator<String> keys = jsonObject.keys();
        while (keys.hasNext()) {
            String key = (String) keys.next();
            try {
                Object object = jsonObject.get(key);
                if (object.getClass() == JSONObject.class) {
                    hashMap.put(key, hashMapFromJsonObject((JSONObject) object));
                } else if (object.getClass() == JSONArray.class) {
                    hashMap.put(key, arrayListFromJsonArray((JSONArray) object));
                } else {
                    hashMap.put(key, object);
                }
            } catch (Exception e) {
            }
        }
        return hashMap;
    }

    private static ArrayList arrayListFromJsonArray(JSONArray jsonArray) {
        ArrayList<Object> arrayList = new ArrayList();
        for (int i = 0; i < jsonArray.length(); i++) {
            try {
                Object object = jsonArray.get(i);
                if (object.getClass() == JSONObject.class) {
                    arrayList.add(hashMapFromJsonObject((JSONObject) object));
                } else if (object.getClass() == JSONArray.class) {
                    arrayList.add(arrayListFromJsonArray((JSONArray) object));
                } else {
                    arrayList.add(object);
                }
            } catch (Exception e) {
            }
        }
        return arrayList;
    }
}
