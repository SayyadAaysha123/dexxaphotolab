package com.dexxaphotolab.app.payment.Utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.dexxaphotolab.app.payment.Network.RetrofitClient;
import com.dexxaphotolab.app.payment.Network.apis.SubscriptionApi;
import com.dexxaphotolab.app.payment.Network.models.ActiveStatus;
import com.dexxaphotolab.app.payment.database.DatabaseHelper;
import com.dexxaphotolab.app.payment.Config;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import static android.content.Context.MODE_PRIVATE;


public class PreferenceUtils {
    public static final String TAG = "PreferenceUtils";


    public static boolean isActivePlan(Context context) {
        String status = getSubscriptionStatus(context);
        if (status != null) {
            SharedPreferences.Editor editor = context.getSharedPreferences("subscibe11", MODE_PRIVATE).edit();
            editor.putString("subscribe", "1");
            editor.apply();
            return status.equals("active");
        } else {
            return false;
        }
    }

    public static boolean isLoggedIn(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(Constants.USER_LOGIN_STATUS, MODE_PRIVATE);
        return preferences.getBoolean(Constants.USER_LOGIN_STATUS, false);
    }

    public static boolean isMandatoryLogin(Context context) {
        DatabaseHelper db = new DatabaseHelper(context);
        return db.getConfigurationData().getAppConfig().getMandatoryLogin();
    }

    public static String getSubscriptionStatus(Context context) {
        DatabaseHelper db = new DatabaseHelper(context);
        return db.getActiveStatusData().getStatus();
    }

    public static long getCurrentTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy:MM:dd:HH:mm");
        String currentDateAndTime = sdf.format(new Date());

        Date date = null;
        try {
            date = sdf.parse(currentDateAndTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);

        return calendar.getTimeInMillis();
    }

    public static long getExpireTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy:MM:dd:HH:mm");
        String currentDateAndTime = sdf.format(new Date());

        Date date = null;
        try {
            date = sdf.parse(currentDateAndTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.HOUR, 2);

        return calendar.getTimeInMillis();
    }

    public static boolean isValid(Context context) {
        String savedTime = getUpdateTime(context);
        long currentTime = getCurrentTime();
        return Long.parseLong(savedTime) > currentTime;
    }

    private static String getUpdateTime(Context context) {
        DatabaseHelper db = new DatabaseHelper(context);
        return String.valueOf(db.getActiveStatusData().getExpireTime());
    }

    public static void updateSubscriptionStatus(final Context context) {
        //get saved user id
        String userId = getUserId(context);
        Retrofit retrofit =  RetrofitClient.getRetrofitInstance();
        SubscriptionApi subscriptionApi = retrofit.create(SubscriptionApi.class);

        Call<ActiveStatus> call = subscriptionApi.getActiveStatus(Config.API_KEY, userId);
        call.enqueue(new Callback<ActiveStatus>() {
            @Override
            public void onResponse(Call<ActiveStatus> call, Response<ActiveStatus> response) {
                if (response.code() == 200) {
                    Log.e("12121", "onResponse: " + response.body().getStatus() );
                    Log.e("12121", "onResponse: " + response.body().getExpireDate() );
                    ActiveStatus activeStatus = response.body();
                    DatabaseHelper db = new DatabaseHelper(context);
                    db.deleteAllActiveStatusData();
                    db.insertActiveStatusData(activeStatus);
                }
            }

            @Override
            public void onFailure(Call<ActiveStatus> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    public static void clearSubscriptionSavedData(Context context) {
        DatabaseHelper db = new DatabaseHelper(context);
        db.deleteAllActiveStatusData();


    }

    public static String getUserId(Context context) {
        DatabaseHelper db = new DatabaseHelper(context);
        return db.getUserData().getUserId();
    }

    public static boolean isProgramGuideEnabled(Context context) {
        DatabaseHelper databaseHelper = new DatabaseHelper(context);
        return databaseHelper.getConfigurationData().getAppConfig().getProgramGuideEnable();
    }


}
