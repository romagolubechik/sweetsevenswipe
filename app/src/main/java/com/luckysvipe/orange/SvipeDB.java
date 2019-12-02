package com.luckysvipe.orange;

import android.content.Context;
import android.content.SharedPreferences;

public class SvipeDB {
    private static String svipe = "svipe";
    private SharedPreferences preferences;

    public SvipeDB(Context context){
        String NAME = "svipe";
        preferences = context.getSharedPreferences(NAME, Context.MODE_PRIVATE);
    }

    public void setSvipe(String data){
        preferences.edit().putString(SvipeDB.svipe, data).apply();
    }

    public String getSvipe(){
        return preferences.getString(svipe, "");
    }
}
