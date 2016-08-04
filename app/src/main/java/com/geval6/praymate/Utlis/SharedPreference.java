package com.geval6.praymate.Utlis;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;
import java.util.ArrayList;
import java.util.HashMap;

public class SharedPreference {
    public static final String FAVORITES = "Temple_Favourites";
    public static final String PREFS_NAME = "Praymate";

    public void addFavourite(Context context, HashMap item) {
        ArrayList favourites = getFavorites(context);
        if (favourites == null) {
            favourites = new ArrayList();
        }
        favourites.add(item);
        saveFavorites(context, favourites);
    }

    public void removeFavourite(Context context, HashMap item) {
        ArrayList favorites = getFavorites(context);
        if (favorites != null) {
            favorites.remove(item);
            saveFavorites(context, favorites);
        }
    }

    public void saveFavorites(Context context, ArrayList favourites) {
        Editor editor = context.getSharedPreferences(PREFS_NAME, 0).edit();
        editor.putString(FAVORITES, new Gson().toJson((Object) favourites));
        editor.commit();
    }

    public ArrayList<HashMap> getFavorites(Context context) {
        SharedPreferences settings = context.getSharedPreferences(PREFS_NAME, 0);
        if (!settings.contains(FAVORITES)) {
            return null;
        }
        ArrayList favourites = (ArrayList) new Gson().fromJson(settings.getString(FAVORITES, null), ArrayList.class);
        ArrayList _favourties = new ArrayList();
        for (int i = 0; i < favourites.size(); i++) {
            LinkedTreeMap item = (LinkedTreeMap) favourites.get(i);
            HashMap _item = new HashMap();
            for (Object key : item.keySet()) {
                _item.put(key, item.get(key));
            }
            _favourties.add(_item);
        }
        return _favourties;
    }
}
