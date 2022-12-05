package com.dexxaphotolab.app.Activities;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.dexxaphotolab.app.AnalyticsApplication;
import com.dexxaphotolab.app.BuildConfig;
import com.dexxaphotolab.app.NewData.NewActivity.WelcomeActivity;
import com.dexxaphotolab.app.R;
import com.dexxaphotolab.app.Retrofit.Base_Url1;
import com.dexxaphotolab.app.Utills.SharedPrefrenceConfig;
import com.dexxaphotolab.app.help.ConnectionDetector;
import com.dexxaphotolab.app.payment.Config;
import com.dexxaphotolab.app.payment.Network.RetrofitClient;
import com.dexxaphotolab.app.payment.Network.apis.ConfigurationApi;
import com.dexxaphotolab.app.payment.Network.config.ApkUpdateInfo;
import com.dexxaphotolab.app.payment.Network.config.Configuration;
import com.dexxaphotolab.app.payment.Utils.ApiResources;
import com.dexxaphotolab.app.payment.Utils.PreferenceUtils;
import com.dexxaphotolab.app.payment.database.DatabaseHelper;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.sanojpunchihewa.updatemanager.UpdateManager;

import org.json.JSONException;
import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import static com.dexxaphotolab.app.Config.API_KEY;


public class ActivitySplashScreen extends AppCompatActivity {
    Handler handler;
    Context context;
    Tracker mTracker;
    SharedPrefrenceConfig sharedprefconfig;
    private boolean status = false;
    private DatabaseHelper db;
    boolean firsttime = false;
    boolean isInternetPresent;
    ConnectionDetector cd;
    public static String Google_banner, Google_interstitial, ads_click_counrter, google_native, google_open_ads, adsEnable;
    private InterstitialAd interstitialAd;
    final int SPLASH_DISPLAY_LENGTH = 3000;
    UpdateManager mUpdateManager;
    private Thread timer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);


        cd = new ConnectionDetector(getApplicationContext());
        isInternetPresent = cd.isConnectingToInternet();
        SharedPreferences prefs = getSharedPreferences("firsttime", MODE_PRIVATE);
        firsttime = prefs.getBoolean("firsttime", false);

        context = ActivitySplashScreen.this;


        sharedprefconfig = new SharedPrefrenceConfig(context);
        db = new DatabaseHelper(ActivitySplashScreen.this);


        getConfigurationData();

        AnalyticsApplication application = (AnalyticsApplication) getApplication();
        mTracker = application.getDefaultTracker();

        status = PreferenceUtils.isLoggedIn(getApplicationContext());
        Log.e("hhhhh", "onCreateView: " + status);


        ApiCat5();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mTracker.setScreenName("Image~" + "Google Analytics Testing");
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());
    }


    private boolean isNeedUpdate(String versionCode) {

        Log.e("vvvvvv", "isNeedUpdate: " + Integer.parseInt(versionCode));
        return Integer.parseInt(versionCode) > BuildConfig.VERSION_CODE;
    }

    private void showAppUpdateDialog(final ApkUpdateInfo info) {
        new AlertDialog.Builder(this)
                .setTitle("New version: " + info.getVersionName())
                .setMessage(info.getWhatsNew())
                .setPositiveButton("Update Now", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //update clicked
                        dialog.dismiss();
                        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(info.getApkUrl()));
                        startActivity(browserIntent);
                        finish();
                    }
                })
                .setNegativeButton("Later", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (info.isSkipable()) {
                            if (db.getConfigurationData() != null) {
                                startact();
                            } else {
                                finish();
                            }
                        } else {
                            System.exit(0);
                        }
                        dialog.dismiss();
                    }
                })
                .setCancelable(false)
                .show();
    }

    private void ApiCat5() {

        StringRequest request = new StringRequest(Request.Method.GET, Base_Url1.BASE_URL + "config?API-KEY=" + API_KEY,
                new com.android.volley.Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        JSONObject obj = null;

                        Log.e("hhhh", "onResponse: " + response);
                        try {

                            obj = new JSONObject(response);
                            JSONObject Object = obj.getJSONObject("ads_config");

                            adsEnable = Object.optString("ads_enable");
                            Google_banner = Object.optString("admob_banner_ads_id");
                            Google_interstitial = Object.optString("admob_interstitial_ads_id");
                            google_native = Object.optString("admob_native_ads_id");
                            google_open_ads = Object.optString("admob_open_ads_id");
                            ads_click_counrter = Object.optString("interstital_click_manage");

                            SharedPreferences.Editor editor = getSharedPreferences("Ads_bundle", MODE_PRIVATE).edit();
                            editor.putString("google_banner", Google_banner);
                            editor.putString("google_native", google_native);
                            editor.putString("google_interstitial", Google_interstitial);
                            editor.putString("google_open_ads", google_open_ads);
                            editor.putString("ads_click", ads_click_counrter);
                            editor.putString("ads_enable", adsEnable);
                            editor.apply();

//                            startact();


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Log.e("error", error.toString());
            }
        });

        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(request);


    }

    public void getConfigurationData() {
        Retrofit retrofit = RetrofitClient.getRetrofitInstance();
        ConfigurationApi api = retrofit.create(ConfigurationApi.class);
        Call<Configuration> call = api.getConfigurationData(Config.API_KEY);
        call.enqueue(new Callback<Configuration>() {
            @Override

            public void onResponse(Call<Configuration> call, Response<Configuration> response) {
                if (response.code() == 200) {
                    Configuration configuration = response.body();
                    if (configuration != null) {

                        ApiResources.CURRENCY = configuration.getPaymentConfig().getCurrency();
                        ApiResources.PAYPAL_CLIENT_ID = configuration.getPaymentConfig().getPaypalClientId();

                        Log.e("paypalpaypal", "onResponse: " + configuration.getPaymentConfig().getPaypalClientId());
                        ApiResources.EXCHSNGE_RATE = configuration.getPaymentConfig().getExchangeRate();
                        ApiResources.RAZORPAY_EXCHANGE_RATE = configuration.getPaymentConfig().getRazorpayExchangeRate();

                        db.deleteAllDownloadData();
                        db.deleteAllAppConfig();
                        db.insertConfigurationData(configuration);

                        if (isNeedUpdate(configuration.getApkUpdateInfo().getVersionCode())) {
                            showAppUpdateDialog(configuration.getApkUpdateInfo());
                            return;
                        }


                        if (db.getConfigurationData() != null) {
//                            timer.start();
                            startact();
                        } else {
                            showErrorDialog(getString(R.string.error_toast), getString(R.string.no_configuration_data_found));
                        }
                    } else {
                        showErrorDialog(getString(R.string.error_toast), getString(R.string.failed_to_communicate));
                    }
                } else {
                    showErrorDialog(getString(R.string.error_toast), getString(R.string.failed_to_communicate));
                }
            }

            @Override
            public void onFailure(Call<Configuration> call, Throwable t) {
                Log.e("ConfigError", t.getLocalizedMessage());
                showErrorDialog(getString(R.string.error_toast), getString(R.string.failed_to_communicate));
            }
        });
    }

    private void showErrorDialog(String title, String message) {
        new AlertDialog.Builder(this)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        System.exit(0);
                        finish();
                    }
                })
                .setCancelable(false)
                .show();
    }


    private void startact() {


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                gotonext();
            }
        }, SPLASH_DISPLAY_LENGTH);

    }

    private void gotonext() {

        SharedPreferences prefs = getSharedPreferences("myPrefs", MODE_PRIVATE);
        boolean status = prefs.getBoolean("name", false);//"No name defined" is the default value.


        if (firsttime) {
            Intent intent = new Intent(context, ActivityHome.class);
            startActivity(intent);
            finish();
        } else {
            Intent intent = new Intent(context, WelcomeActivity.class);
            startActivity(intent);
            finish();
        }


    }

}
