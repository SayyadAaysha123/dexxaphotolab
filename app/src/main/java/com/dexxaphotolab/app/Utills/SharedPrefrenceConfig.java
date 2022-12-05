package com.dexxaphotolab.app.Utills;

import android.content.Context;
import android.content.SharedPreferences;

import com.dexxaphotolab.app.ModelRetrofit.RecordRegister;
import com.dexxaphotolab.app.ModelRetrofit.ResponseLogin;


public class SharedPrefrenceConfig {

    private static final String SHARED_PREF_NAME = "My_shared_pref";
    private Context context;

    public SharedPrefrenceConfig(Context context) {
        this.context = context;
    }

    public static void savebooleanPreferance(Context context, String key, boolean value) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("MP", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(key, value);
        editor.apply();
        editor.commit();
    }

    public static boolean getPrefBoolean(Context context, String key, boolean defvalue) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("MP", Context.MODE_PRIVATE);
        boolean value = sharedPreferences.getBoolean(key, defvalue);
        return value;
    }


    public static void saveStringPreferance(Context context, String key, String value) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("MP", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, value);
        editor.apply();
        editor.commit();
    }

    public static String getPrefString(Context context, String key, String defvalue) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("MP", Context.MODE_PRIVATE);
        String value = sharedPreferences.getString(key, defvalue);
        return value;
    }



    public boolean isLoggedIn() {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString("id", null) != null;

    }


    public RecordRegister getUser() {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return new RecordRegister(
                sharedPreferences.getString("id", null),
                sharedPreferences.getString("name", null),
                sharedPreferences.getString("email", null),
                sharedPreferences.getString("city", null),
                sharedPreferences.getString("imageurl", null),
                sharedPreferences.getString("imagepath", null),
                sharedPreferences.getString("contact", null));
    }

    public void saveUser(RecordRegister user) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("id", user.getId());
        editor.putString("email", user.getEmail());
        editor.putString("name", user.getName());
        editor.putString("city", user.getCity());
        editor.putString("imageurl", user.getImage_url());
        editor.putString("imagepath", user.getImage());
        editor.putString("contact", user.getContact());
        editor.apply();
    }
    public void saveapitoken(ResponseLogin responseuser) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("api_token", responseuser.getApi_token());
        editor.apply();
    }

    //for logout
    public void clear() {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
    }

}
