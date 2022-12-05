package com.dexxaphotolab.app.payment.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.dexxaphotolab.app.Activities.ActivityHome;
import com.dexxaphotolab.app.payment.database.DatabaseHelper;
import com.dexxaphotolab.app.R;
import com.dexxaphotolab.app.payment.Config;
import com.dexxaphotolab.app.payment.Network.RetrofitClient;
import com.dexxaphotolab.app.payment.Network.apis.FirebaseAuthApi;
import com.dexxaphotolab.app.payment.Network.apis.SubscriptionApi;
import com.dexxaphotolab.app.payment.Network.models.ActiveStatus;
import com.dexxaphotolab.app.payment.Network.models.User;
import com.dexxaphotolab.app.payment.Utils.ApiResources;
import com.dexxaphotolab.app.payment.Utils.Constants;
import com.dexxaphotolab.app.payment.Utils.RtlUtils;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.ErrorCodes;
import com.firebase.ui.auth.IdpResponse;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


import java.util.Arrays;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class FirebaseSignUpActivity extends AppCompatActivity {
    private static final String TAG = "SignInActivity";
    FirebaseAuth firebaseAuth;
    private static int RC_PHONE_SIGN_IN = 123;
    private static int RC_FACEBOOK_SIGN_IN = 124;
    private static int RC_GOOGLE_SIGN_IN = 125;
    private ProgressBar progressBar;
    private View backgroundView;
    private Button googleAuth, phoneAuth, emailAuth, facebookAuth;
    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        RtlUtils.setScreenDirection(this);
        SharedPreferences sharedPreferences = getSharedPreferences("push", MODE_PRIVATE);
        boolean isDark = sharedPreferences.getBoolean("dark", false);
        setTheme(R.style.AppThemeLight);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.pactivity_firebase_sign_up);

        databaseHelper = new DatabaseHelper(FirebaseSignUpActivity.this);

        FirebaseAnalytics mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.ITEM_ID, "id");
        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, "sign_up_activity");
        bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "activity");
        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);

        backgroundView = findViewById(R.id.background_view);
        googleAuth = findViewById(R.id.google_auth);
        phoneAuth = findViewById(R.id.phoneSignInBtn);
        emailAuth = findViewById(R.id.emailSignInBtn);
        facebookAuth = findViewById(R.id.facebookSignInBtn);
        if (Config.ENABLE_FACEBOOK_LOGIN) {
            facebookAuth.setVisibility(View.VISIBLE);
        }
        if (Config.ENABLE_GOOGLE_LOGIN) {
            googleAuth.setVisibility(View.VISIBLE);
        }
        if (Config.ENABLE_PHONE_LOGIN) {
            phoneAuth.setVisibility(View.VISIBLE);
        }


        progressBar = findViewById(R.id.phone_auth_progress_bar);

        firebaseAuth = FirebaseAuth.getInstance();

    }

    public void signInWithPhone(View view) {
        phoneSignIn();
    }

    private void phoneSignIn() {
        progressBar.setVisibility(View.VISIBLE);
        if (firebaseAuth.getCurrentUser() != null) {

            if (!FirebaseAuth.getInstance().getCurrentUser().getUid().isEmpty()) {
                final String phoneNumber = FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber();
                if (!phoneNumber.isEmpty())
                    sendDataToServer();
            }

        } else {
            progressBar.setVisibility(View.GONE);
            List<AuthUI.IdpConfig> providers = Arrays.asList(
                    new AuthUI.IdpConfig.PhoneBuilder().build());
            startActivityForResult(
                    AuthUI.getInstance()
                            .createSignInIntentBuilder()
                            .setAvailableProviders(providers)
                            .build(),
                    RC_PHONE_SIGN_IN);
        }
    }

    private void facebookSignIn() {
        progressBar.setVisibility(View.VISIBLE);
        if (firebaseAuth.getCurrentUser() != null) {
            if (!FirebaseAuth.getInstance().getCurrentUser().getUid().isEmpty()) {
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                sendFacebookDataToServer(user.getDisplayName(), String.valueOf(user.getPhotoUrl()), user.getEmail());

            }

        } else {
            progressBar.setVisibility(View.GONE);
            List<AuthUI.IdpConfig> providers = Arrays.asList(
                    new AuthUI.IdpConfig.FacebookBuilder()
                            .build());
            startActivityForResult(
                    AuthUI.getInstance()
                            .createSignInIntentBuilder()
                            .setAvailableProviders(providers)
                            .setIsSmartLockEnabled(false)
                            .build(),
                    RC_FACEBOOK_SIGN_IN);
        }
    }

    private void sendDataToServer() {
        progressBar.setVisibility(View.VISIBLE);
        String phoneNo = FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber();
        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        Retrofit retrofit = RetrofitClient.getRetrofitInstance();
        FirebaseAuthApi api = retrofit.create(FirebaseAuthApi.class);
        Call<User> call = api.getPhoneAuthStatus(Config.API_KEY, uid, phoneNo);
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.code() == 200) {
                    if (response.body().getStatus().equals("success")) {

                        User user = response.body();
                        DatabaseHelper db = new DatabaseHelper(FirebaseSignUpActivity.this);
                        db.deleteUserData();
                        db.insertUserData(user);
                        ApiResources.USER_PHONE = user.getPhone();

                        SharedPreferences.Editor preferences = getSharedPreferences(Constants.USER_LOGIN_STATUS, MODE_PRIVATE).edit();
                        preferences.putBoolean(Constants.USER_LOGIN_STATUS, true);
                        preferences.apply();
                        preferences.commit();
                        updateSubscriptionStatus(user.getUserId());
                    }

                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                phoneSignIn();
            }
        });


    }

    private void sendFacebookDataToServer(String username, String photoUrl, String email) {
        progressBar.setVisibility(View.VISIBLE);
        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        Retrofit retrofit = RetrofitClient.getRetrofitInstance();
        FirebaseAuthApi api = retrofit.create(FirebaseAuthApi.class);
        Call<User> call = api.getFacebookAuthStatus(Config.API_KEY, uid, username, email, Uri.parse(photoUrl));
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.code() == 200) {
                    if (response.body().getStatus().equals("success")) {

                        User user = response.body();
                        DatabaseHelper db = new DatabaseHelper(FirebaseSignUpActivity.this);
                        db.deleteUserData();
                        db.insertUserData(user);
                        ApiResources.USER_PHONE = user.getPhone();

                        SharedPreferences.Editor preferences = getSharedPreferences(Constants.USER_LOGIN_STATUS, MODE_PRIVATE).edit();
                        preferences.putBoolean(Constants.USER_LOGIN_STATUS, true);
                        preferences.apply();
                        preferences.commit();

                        updateSubscriptionStatus(user.getUserId());
                    }

                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                facebookSignIn();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_PHONE_SIGN_IN) {

            final IdpResponse response = IdpResponse.fromResultIntent(data);

            if (resultCode == RESULT_OK) {
                final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                if (!user.getPhoneNumber().isEmpty()) {
                    sendDataToServer();
                } else {
                    phoneSignIn();
                }
            } else {
                if (response == null) {
                    return;
                }
                if (response.getError().getErrorCode() == ErrorCodes.NO_NETWORK) {
                    return;
                }
                if (response.getError().getErrorCode() == ErrorCodes.UNKNOWN_ERROR) {
                    Toast.makeText(this, "Error !!", Toast.LENGTH_SHORT).show();
                    return;
                }

            }
        } else if (requestCode == RC_FACEBOOK_SIGN_IN) {
            final IdpResponse response = com.firebase.ui.auth.IdpResponse.fromResultIntent(data);
            if (resultCode == RESULT_OK) {
                final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                if (!user.getUid().isEmpty()) {
                    String username = user.getDisplayName();
                    String photoUrl = String.valueOf(user.getPhotoUrl());
                    String email = user.getEmail();

                    sendFacebookDataToServer(username, photoUrl, email);

                } else {
                    facebookSignIn();
                }
            } else {
                if (response == null) {
                    return;
                }
                if (response.getError().getErrorCode() == ErrorCodes.NO_NETWORK) {
                    return;
                }
                if (response.getError().getErrorCode() == ErrorCodes.UNKNOWN_ERROR) {
                    Toast.makeText(this, "Error !!", Toast.LENGTH_SHORT).show();
                    return;
                }
            }
        } else if (requestCode == RC_GOOGLE_SIGN_IN) {
            IdpResponse response = IdpResponse.fromResultIntent(data);
            if (resultCode == RESULT_OK) {
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                if (!user.getUid().isEmpty()) {
                    sendGoogleDataToServer();

                } else {
                    googleSignIn();
                }
            } else {
                if (response == null) {
                    Toast.makeText(this, "Canceled", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (response.getError().getErrorCode() == ErrorCodes.NO_NETWORK) {
                    Toast.makeText(this, "No internet", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (response.getError().getErrorCode() == ErrorCodes.UNKNOWN_ERROR) {
                    Toast.makeText(this, "Error !!", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    public void signInWithEmail(View view) {
        startActivity(new Intent(FirebaseSignUpActivity.this, LoginActivity.class));
    }

    public void signInWithFacebook(View view) {
        facebookSignIn();
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
                        DatabaseHelper db = new DatabaseHelper(FirebaseSignUpActivity.this);
                        db.deleteAllActiveStatusData();
                        db.insertActiveStatusData(activeStatus);

                        Intent intent = new Intent(FirebaseSignUpActivity.this, ActivityHome.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);

                        startActivity(intent);
                        finish();
                        progressBar.setVisibility(View.GONE);
                    }
                }
            }

            @Override
            public void onFailure(Call<ActiveStatus> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    public void signInWithGoogle(View view) {
        googleSignIn();
    }

    private void googleSignIn() {
        progressBar.setVisibility(View.VISIBLE);
        FirebaseAuth auth = FirebaseAuth.getInstance();
        if (auth.getCurrentUser() != null) {
            sendGoogleDataToServer();

        } else {
            progressBar.setVisibility(View.GONE);
            GoogleSignInOptions googleOptions = new GoogleSignInOptions.Builder(
                    GoogleSignInOptions.DEFAULT_SIGN_IN)
                    .requestIdToken(getString(R.string.default_web_client_id))
                    .requestEmail()
                    .requestProfile()
                    .build();


            List<AuthUI.IdpConfig> providers = Arrays.asList(
                    new AuthUI.IdpConfig.GoogleBuilder().setSignInOptions(googleOptions).build());


            startActivityForResult(
                    AuthUI.getInstance()
                            .createSignInIntentBuilder()
                            .setAvailableProviders(providers)
                            .setIsSmartLockEnabled(false)
                            .build(),
                    RC_GOOGLE_SIGN_IN);
        }
    }

    private void sendGoogleDataToServer() {
        progressBar.setVisibility(View.VISIBLE);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String username = user.getDisplayName();
        String email = user.getEmail();
        Uri image = user.getPhotoUrl();
        String uid = user.getUid();
        String phone = "";
        if (user.getPhoneNumber() != null) {
            phone = user.getPhoneNumber();
        }

        Log.d(TAG, "onActivityResult: " + user.getEmail());
        Log.d(TAG, "onActivityResult: " + user.getDisplayName());
        Log.d(TAG, "onActivityResult: " + user.getPhoneNumber());

        Retrofit retrofit = RetrofitClient.getRetrofitInstance();
        FirebaseAuthApi api = retrofit.create(FirebaseAuthApi.class);
        Call<User> call = api.getGoogleAuthStatus(Config.API_KEY, uid, email, username, image, phone);
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.code() == 200) {
                    if (response.body().getStatus().equals("success")) {
                        User user = response.body();
                        DatabaseHelper db = new DatabaseHelper(FirebaseSignUpActivity.this);
                        db.deleteUserData();
                        db.insertUserData(user);
                        ApiResources.USER_PHONE = user.getPhone();

                        SharedPreferences.Editor preferences = getSharedPreferences(Constants.USER_LOGIN_STATUS, MODE_PRIVATE).edit();
                        preferences.putBoolean(Constants.USER_LOGIN_STATUS, true);
                        preferences.apply();
                        preferences.commit();

                        updateSubscriptionStatus(user.getUserId());
                    }

                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                googleSignIn();
            }
        });

    }

    @Override
    public void onBackPressed() {
        if (databaseHelper.getConfigurationData().getAppConfig().getMandatoryLogin()) {
            System.exit(0);
        } else {
            startActivity(new Intent(FirebaseSignUpActivity.this, ActivityHome.class));
        }
        super.onBackPressed();
    }
}
