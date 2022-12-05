package com.dexxaphotolab.app.payment.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.dexxaphotolab.app.Activities.ActivityHome;
import com.dexxaphotolab.app.payment.database.DatabaseHelper;
import com.dexxaphotolab.app.R;
import com.dexxaphotolab.app.Utills.Admanager;
import com.dexxaphotolab.app.Utills.SharedPrefrenceConfig;
import com.dexxaphotolab.app.payment.Config;
import com.dexxaphotolab.app.payment.Network.RetrofitClient;
import com.dexxaphotolab.app.payment.Network.apis.LoginApi;
import com.dexxaphotolab.app.payment.Network.apis.SubscriptionApi;
import com.dexxaphotolab.app.payment.Network.models.ActiveStatus;
import com.dexxaphotolab.app.payment.Network.models.User;
import com.dexxaphotolab.app.payment.Utils.ApiResources;
import com.dexxaphotolab.app.payment.Utils.Constants;
import com.dexxaphotolab.app.payment.Utils.RtlUtils;
import com.dexxaphotolab.app.payment.Utils.ToastMsg;
import com.google.firebase.analytics.FirebaseAnalytics;


import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class LoginActivity extends AppCompatActivity {
    private EditText etEmail, etPass;
    private TextView tvSignUp, tvReset;
    private Button btnLogin;
    private ProgressDialog dialog;
    private View backgroundView;
    LinearLayout linearAds;
    LinearLayout ll_skip;
    SharedPrefrenceConfig sharedprefconfig;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        RtlUtils.setScreenDirection(this);
        SharedPreferences sharedPreferences = getSharedPreferences("push", MODE_PRIVATE);
        boolean isDark = sharedPreferences.getBoolean("dark", false);

        setTheme(R.style.AppThemeLight);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.pactivity_login);

        new Admanager(getApplicationContext()).loadAd();

        Toolbar toolbar = findViewById(R.id.toolbar);
        ll_skip = findViewById(R.id.ll_skip);

        SharedPreferences prefsss = getSharedPreferences("subscibe11", MODE_PRIVATE);
        String substatus = prefsss.getString("subscribe", "0");

        if (substatus.equals("0")) {

            linearAds = findViewById(R.id.banner_container);

            new Admanager(this).loadBanner(LoginActivity.this, findViewById(R.id.banner_container));
        }

        if (!isDark) {
            toolbar.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        }

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Back to login with phone");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        FirebaseAnalytics mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.ITEM_ID, "id");
        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, "login_activity");
        bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "activity");
        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);

        dialog = new ProgressDialog(this);
        dialog.setMessage("Please wait");
        dialog.setCancelable(false);

        etEmail = findViewById(R.id.email);
        etPass = findViewById(R.id.password);
        tvSignUp = findViewById(R.id.signup);
        btnLogin = findViewById(R.id.signin);
        tvReset = findViewById(R.id.reset_pass);
        backgroundView = findViewById(R.id.background_view);


        ll_skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new Admanager(getApplicationContext()).showInterstitial(LoginActivity.this, new Admanager.GetBackPointer() {
                    @Override
                    public void returnAction() {

                        Intent i = new Intent(LoginActivity.this, ActivityHome.class);
                        startActivity(i);
                    }
                });


            }
        });
        tvReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new Admanager(getApplicationContext()).showInterstitial(LoginActivity.this, new Admanager.GetBackPointer() {
                    @Override
                    public void returnAction() {

                        startActivity(new Intent(LoginActivity.this, PassResetActivity.class));
                    }
                });


            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!isValidEmailAddress(etEmail.getText().toString())) {
                    new ToastMsg(LoginActivity.this).toastIconError("Please enter valid email");
                } else if (etPass.getText().toString().equals("")) {
                    new ToastMsg(LoginActivity.this).toastIconError("Please enter password");
                } else {
                    String email = etEmail.getText().toString();
                    String pass = etPass.getText().toString();
                    login(email, pass);
                }
            }
        });

        tvSignUp.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                new Admanager(getApplicationContext()).showInterstitial(LoginActivity.this, new Admanager.GetBackPointer() {
                    @Override
                    public void returnAction() {

                        startActivity(new Intent(LoginActivity.this, SignUpActivity.class));
                    }
                });

            }

        });

    }


    private void login(String email, final String password) {
        dialog.show();
        Retrofit retrofit = RetrofitClient.getRetrofitInstance();
        LoginApi api = retrofit.create(LoginApi.class);
        Call<User> call = api.postLoginStatus(Config.API_KEY, email, password);
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.code() == 200) {
                    assert response.body() != null;
                    if (response.body().getStatus().equalsIgnoreCase("success")) {
                        User user = response.body();
                        DatabaseHelper db = new DatabaseHelper(LoginActivity.this);
                        db.deleteUserData();
                        db.insertUserData(user);
                        ApiResources.USER_PHONE = user.getPhone();

                        SharedPreferences.Editor preferences = getSharedPreferences(Constants.USER_LOGIN_STATUS, MODE_PRIVATE).edit();
                        preferences.putBoolean(Constants.USER_LOGIN_STATUS, true);
                        preferences.apply();
                        preferences.commit();

                        updateSubscriptionStatus(db.getUserData().getUserId());
                    } else {
                        new ToastMsg(LoginActivity.this).toastIconError(response.body().getData());
                        dialog.dismiss();
                    }
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                dialog.cancel();
                new ToastMsg(LoginActivity.this).toastIconError(getString(R.string.error_toast));
            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    public boolean isValidEmailAddress(String email) {
        String ePattern = "^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\])|(([a-zA-Z\\-0-9]+\\.)+[a-zA-Z]{2,}))$";
        java.util.regex.Pattern p = java.util.regex.Pattern.compile(ePattern);
        java.util.regex.Matcher m = p.matcher(email);
        return m.matches();
    }

    public void updateSubscriptionStatus(String userId) {
        Retrofit retrofit = RetrofitClient.getRetrofitInstance();
        SubscriptionApi subscriptionApi = retrofit.create(SubscriptionApi.class);

        Call<ActiveStatus> call = subscriptionApi.getActiveStatus(Config.API_KEY, userId);
        call.enqueue(new Callback<ActiveStatus>() {
            @Override
            public void onResponse(Call<ActiveStatus> call, Response<ActiveStatus> response) {
                if (response.code() == 200) {
                    if (response.body() != null) {
                        ActiveStatus activeStatus = response.body();

                        DatabaseHelper db = new DatabaseHelper(LoginActivity.this);
                        db.deleteAllActiveStatusData();
                        db.insertActiveStatusData(activeStatus);

                        new Admanager(getApplicationContext()).showInterstitial(LoginActivity.this, new Admanager.GetBackPointer() {
                            @Override
                            public void returnAction() {

                                Intent intent = new Intent(LoginActivity.this, ActivityHome.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);

                                startActivity(intent);
                                finish();
                            }
                        });


                        dialog.cancel();
                    }
                }
            }

            @Override
            public void onFailure(Call<ActiveStatus> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }
}
