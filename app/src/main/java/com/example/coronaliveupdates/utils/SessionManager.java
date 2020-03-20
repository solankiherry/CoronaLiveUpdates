package com.example.coronaliveupdates.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class SessionManager {
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    Context _context;

    int PRIVATE_MODE = 0;

    private static final String PREF_NAME = "AppPref";
    public static final String APP_COUNTRIES = "countries";

    public SessionManager(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    public void setLogoutUser() {
        editor.clear();
        editor.commit();
    }

    public void setStoredData(String dataKey, String data) {
        editor.putString(dataKey, data);
        editor.commit();
    }

    public String getStoredData(String dataKey) {
        return pref.getString(dataKey, "");
    }
}