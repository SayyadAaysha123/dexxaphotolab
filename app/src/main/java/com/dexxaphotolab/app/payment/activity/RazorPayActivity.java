package com.dexxaphotolab.app.payment.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.dexxaphotolab.app.Activities.ActivityHome;
import com.dexxaphotolab.app.payment.database.DatabaseHelper;
import com.dexxaphotolab.app.R;
import com.dexxaphotolab.app.payment.Config;
import com.dexxaphotolab.app.payment.Network.RetrofitClient;
import com.dexxaphotolab.app.payment.Network.apis.PaymentApi;
import com.dexxaphotolab.app.payment.Network.apis.SubscriptionApi;
import com.dexxaphotolab.app.payment.Network.config.PaymentConfig;
import com.dexxaphotolab.app.payment.Network.models.ActiveStatus;
import com.dexxaphotolab.app.payment.Network.models.User;
import com.dexxaphotolab.app.payment.Utils.ApiResources;
import com.dexxaphotolab.app.payment.Utils.PreferenceUtils;
import com.dexxaphotolab.app.payment.Utils.ToastMsg;
import com.dexxaphotolab.app.payment.Network.models.Package;
import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;

import org.json.JSONObject;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class RazorPayActivity extends AppCompatActivity implements PaymentResultListener {
    private static final String TAG = "RazorPayActivity";
    private Package aPackage;
    private DatabaseHelper databaseHelper;
    private ProgressBar progressBar;
    private String amountPaidInRupee = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pactivity_razor_pay);
        progressBar = findViewById(R.id.progress_bar);
        aPackage = (Package) getIntent().getSerializableExtra("package");
        databaseHelper = new DatabaseHelper(this);
        startPayment();
    }

    public void startPayment() {
        PaymentConfig config = databaseHelper.getConfigurationData().getPaymentConfig();
        User user = databaseHelper.getUserData();

        final Activity activity = this;
        Checkout checkout = new Checkout();
        Log.e("kkkkk", "startPayment: "+config.getRazorpayKeyId() );
        checkout.setKeyID(config.getRazorpayKeyId());
        checkout.setImage(R.drawable.app_icon);


        JSONObject options = new JSONObject();
        try {
            options.put("name",  getString(R.string.app_name));
            options.put("description", aPackage.getName());
            options.put("currency", "INR");
            options.put("amount", currencyConvert(config.getCurrency(), aPackage.getPrice(), ApiResources.RAZORPAY_EXCHANGE_RATE));

            JSONObject prefill = new JSONObject();
            prefill.put("email", user.getEmail());
            options.put("prefill", prefill);

            checkout.open(activity, options);

            Log.e(TAG, config.getCurrency());
            Log.e(TAG, currencyConvert(config.getCurrency(), aPackage.getPrice(), ApiResources.RAZORPAY_EXCHANGE_RATE));
        } catch(Exception e) {
            Log.e(TAG, "Error in starting Razorpay Checkout", e);
        }
    }


    @Override
    public void onPaymentSuccess(String s) {
        saveChargeData(s);
    }

    @Override
    public void onPaymentError(int i, String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        Log.e(TAG, "Error: " + message);
        finish();
    }

    public void saveChargeData(String token) {
        progressBar.setVisibility(View.VISIBLE);
        Retrofit retrofit = RetrofitClient.getRetrofitInstance();
        PaymentApi paymentApi = retrofit.create(PaymentApi.class);
        Call<ResponseBody> call = paymentApi.savePayment(Config.API_KEY, aPackage.getPlanId(),
                databaseHelper.getUserData().getUserId(),
                amountPaidInRupee,
                token, "RazorPay");
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.code() == 200) {
                    updateActiveStatus();

                } else {
                    new ToastMsg(RazorPayActivity.this).toastIconError(getString(R.string.something_went_wrong));
                    finish();
                    progressBar.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                t.printStackTrace();
                new ToastMsg(RazorPayActivity.this).toastIconError(getString(R.string.something_went_wrong));
                finish();
                progressBar.setVisibility(View.GONE);
            }
        });


    }

    private void updateActiveStatus() {
        Retrofit retrofit = RetrofitClient.getRetrofitInstance();
        SubscriptionApi subscriptionApi = retrofit.create(SubscriptionApi.class);

        Call<ActiveStatus> call = subscriptionApi.getActiveStatus(Config.API_KEY, PreferenceUtils.getUserId(RazorPayActivity.this));
        call.enqueue(new Callback<ActiveStatus>() {
            @Override
            public void onResponse(Call<ActiveStatus> call, Response<ActiveStatus> response) {
                if (response.code() == 200) {
                    ActiveStatus activeStatus = response.body();
                    DatabaseHelper db = new DatabaseHelper(getApplicationContext());
                    db.deleteAllActiveStatusData();
                    db.insertActiveStatusData(activeStatus);
                    new ToastMsg(RazorPayActivity.this).toastIconSuccess(getResources().getString(R.string.payment_success));
                    SharedPreferences.Editor editor = getSharedPreferences("subscibe11", MODE_PRIVATE).edit();
                    editor.putString("subscribe", "1");
                    editor.apply();
                    Log.e("xxxx", "saveActiveStatus: "+"xxxxx" );
                    Intent i = new Intent(RazorPayActivity.this, ActivityHome.class);
                    i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    i.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    startActivity(i);
                    finish();
                    progressBar.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(Call<ActiveStatus> call, Throwable t) {
                t.printStackTrace();
                new ToastMsg(RazorPayActivity.this).toastIconError(getString(R.string.something_went_wrong));
                finish();
                progressBar.setVisibility(View.GONE);
            }
        });

    }

    private String currencyConvert(String currency, String value, String exchangeRate) {
        String convertedValue = "";
        if (!currency.equalsIgnoreCase("INR")) {
            double temp = Double.parseDouble(value) * Double.parseDouble(exchangeRate);
            convertedValue = String.valueOf(temp * 100); //convert to rupee
        } else {
            double temp = Double.parseDouble(value);
            convertedValue = String.valueOf(temp * 100);
        }
        amountPaidInRupee = convertedValue;
        return convertedValue;
    }

}
