package com.dexxaphotolab.app.payment.Utils;

import android.content.Context;
import android.content.SharedPreferences;

public class PreferenceManager {

    private static final String PREF_NAME = "status_app";
    SharedPreferences.Editor mEditor;
    SharedPreferences mSharedPreferences;
    Context mContext;

    public PreferenceManager(Context context) {
        this.mContext = context;
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_NAME, 0);
        this.mSharedPreferences = sharedPreferences;
        this.mEditor = sharedPreferences.edit();
    }

    public void setString(String str, String str2) {
        this.mEditor.putString(str, str2);
        this.mEditor.commit();
    }
    public String getPath() {
        return this.mSharedPreferences.getString("path", "0");
    }


    public String getString(String str) {
        return this.mSharedPreferences.contains(str) ? this.mSharedPreferences.getString(str, null) : "";
    }

    public void setInt(String str, int i) {
        this.mEditor.putInt(str, i);
        this.mEditor.commit();
    }

}
