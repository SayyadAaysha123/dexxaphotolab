package com.dexxaphotolab.app.payment.activity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.dexxaphotolab.app.Activities.ActivityHome;
import com.dexxaphotolab.app.payment.database.DatabaseHelper;
import com.dexxaphotolab.app.R;
import com.dexxaphotolab.app.Utills.Admanager;
import com.dexxaphotolab.app.Utills.Constance;
import com.dexxaphotolab.app.payment.Config;
import com.dexxaphotolab.app.payment.Network.RetrofitClient;
import com.dexxaphotolab.app.payment.Network.apis.ProfileApi;
import com.dexxaphotolab.app.payment.Network.apis.SubscriptionApi;
import com.dexxaphotolab.app.payment.Network.apis.UserDataApi;
import com.dexxaphotolab.app.payment.Network.models.ActiveStatus;
import com.dexxaphotolab.app.payment.Network.models.ResponseStatus;
import com.dexxaphotolab.app.payment.Network.models.User;
import com.dexxaphotolab.app.payment.Utils.ApiResources;
import com.dexxaphotolab.app.payment.Utils.Constants;
import com.dexxaphotolab.app.payment.Utils.FileUtil;
import com.dexxaphotolab.app.payment.Utils.PreferenceUtils;
import com.dexxaphotolab.app.payment.Utils.RtlUtils;
import com.dexxaphotolab.app.payment.Utils.ToastMsg;
import com.google.firebase.analytics.FirebaseAnalytics;

import java.io.File;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class Business_DetailActivity1 extends AppCompatActivity {
    private static final String TAG = Business_DetailActivity1.class.getSimpleName();
    private EditText etName, etEmail, etPhone, etPass;
    private EditText genderSpinner;
    private EditText etCompanyName, etCompanyAdd, etWebsite;
    private Button btnUpdate;
    private ProgressDialog dialog;
    private String URL = "", strGender;
    private CircleImageView userIv;
    private static final int GALLERY_REQUEST_CODE = 1;
    private Uri imageUri;
    private ProgressBar progressBar;
    private String id;
    private boolean isDark;
    LinearLayout linearAds;
    private String selectedGender = "Other";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        RtlUtils.setScreenDirection(this);
        SharedPreferences sharedPreferences = getSharedPreferences("push", MODE_PRIVATE);
        isDark = sharedPreferences.getBoolean("dark", false);

        setTheme(R.style.AppThemeLight);

        Constance.deleteCache(this );

        super.onCreate(savedInstanceState);
        setContentView(R.layout.pactivity_business_profile);
        Toolbar toolbar = findViewById(R.id.toolbar);
        SharedPreferences prefsss = getSharedPreferences("subscibe11", MODE_PRIVATE);
        String substatus = prefsss.getString("subscribe", "0");

        if (substatus.equals("0")) {

            linearAds = findViewById(R.id.banner_container);
            new Admanager(this).loadBanner(Business_DetailActivity1.this, findViewById(R.id.banner_container));

        }

        if (!isDark) {
            toolbar.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        }

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("My Profile");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        FirebaseAnalytics mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.ITEM_ID, "id");
        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, "profile_activity");
        bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "activity");
        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);

        dialog = new ProgressDialog(this);
        dialog.setMessage("Please wait");
        dialog.setCancelable(false);

        etName = findViewById(R.id.name);
        etEmail = findViewById(R.id.email);
        etPhone = findViewById(R.id.phone);
        etCompanyName = findViewById(R.id.company_name);
        etCompanyAdd = findViewById(R.id.company_add);
        etWebsite = findViewById(R.id.website_add);
        btnUpdate = findViewById(R.id.signup);
        etPass = findViewById(R.id.password);
        userIv = findViewById(R.id.user_iv);
        progressBar = findViewById(R.id.progress_bar);
        genderSpinner = findViewById(R.id.business_category_spinner);


        id = PreferenceUtils.getUserId(Business_DetailActivity1.this);


        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (etEmail.getText().toString().equals("") || !Patterns.EMAIL_ADDRESS.matcher(etEmail.getText()).matches()) {
                    etEmail.setError("Please enter valid email");
                    return;
                } else if (etName.getText().toString().equals("")) {
                    Toast.makeText(Business_DetailActivity1.this, "Please enter name.", Toast.LENGTH_LONG).show();
                    return;
                } else if (etCompanyName.getText().toString().equals("")) {
                    new ToastMsg(Business_DetailActivity1.this).toastIconError("Please enter company name.");
                    return;
                } else if (etCompanyAdd.getText().toString().equals("")) {
                    new ToastMsg(Business_DetailActivity1.this).toastIconError("Please enter company address.");
                    return;
                } else if (!etWebsite.getText().toString().equals("") && !Patterns.WEB_URL.matcher(etWebsite.getText()).matches()) {
                        etWebsite.setError("Please Enter valid Website");
                    return;
                }

                progressBar.setVisibility(View.VISIBLE);

                String email = etEmail.getText().toString();
                String number = etPhone.getText().toString();
                String phone = number.replace("+91 " ,"");
                String pass = etPass.getText().toString();
                String company_name = etCompanyName.getText().toString();
                String company_address = etCompanyAdd.getText().toString();
                String name = etName.getText().toString();
                String web = etWebsite.getText().toString();

                updateProfile(id, email, phone, name, company_name, company_address, web, pass);

            }
        });


        final String[] genderArray = new String[94];
        genderArray[0] = "Real Estate";
        genderArray[1] = "Marble & Ceramic";
        genderArray[2] = "Clothes Business";
        genderArray[3] = "Daily & Sweets Store";
        genderArray[4] = "Electrical Services Provider";
        genderArray[5] = "FMCG & Grocery";
        genderArray[6] = "Hardware & Sanitaryware";
        genderArray[7] = "Tours & Travels";
        genderArray[8] = "Wood Buisness";
        genderArray[9] = "Restaurant,Cafe & Catering";
        genderArray[10] = "Furniture";
        genderArray[11] = "Advocate";
        genderArray[12] = "Automobile Business";
        genderArray[13] = "Agriculture Business";
        genderArray[14] = "Chartered Accountant";
        genderArray[15] = "Chemical";
        genderArray[16] = "Clinic & Hospital";
        genderArray[17] = "Storage & Warehouse";
        genderArray[18] = "Cosmetic Store";
        genderArray[19] = "Education Business";
        genderArray[20] = "Event Agency";
        genderArray[21] = "Loan & Finance";
        genderArray[22] = "Fire Safety";
        genderArray[23] = "Flower";
        genderArray[24] = "Electronics";
        genderArray[25] = "Fruits & Vegetable";
        genderArray[26] = "Goggles & Spectacles";
        genderArray[27] = "Graphics Designing";
        genderArray[28] = "Gym & Yoga";
        genderArray[29] = "Home Appliances";
        genderArray[30] = "Home Automation";
        genderArray[31] = "Other";
        genderArray[32] = "Import & Export";
        genderArray[33] = "Interior Maker";
        genderArray[34] = "Jewellery";
        genderArray[35] = "Lighting Business";
        genderArray[36] = "Man Power Provider";
        genderArray[37] = "Pharmaceutical";
        genderArray[38] = "Photographer";
        genderArray[39] = "Politics";
        genderArray[40] = "Printing Service Provider";
        genderArray[41] = "Security Surveillance Service(CCTV)";
        genderArray[42] = "Social Activities";
        genderArray[43] = "Solar & Power Panel";
        genderArray[44] = "Sport Academy";
        genderArray[45] = "Stationery Store";
        genderArray[46] = "Steel and Aluminum Business";
        genderArray[47] = "Tailor";
        genderArray[48] = "Textile Industry";
        genderArray[49] = "Gift Articles & Toys";
        genderArray[50] = "Cycles";
        genderArray[51] = "Consultant";
        genderArray[52] = "Dental";
        genderArray[53] = "Salon, Spa & Beauty Parlour";
        genderArray[54] = "Mechanical Garage";
        genderArray[55] = "Stationery Store";
        genderArray[56] = "Doctor";
        genderArray[57] = "Astrologer";
        genderArray[58] = "Ayurvedic";
        genderArray[59] = "Hotel";
        genderArray[60] = "Immigration Visa Consultant";
        genderArray[61] = "Insurance";
        genderArray[62] = "Laundry Dry Cleaner";
        genderArray[63] = "Marketing Agency";
        genderArray[64] = "Musical Business";
        genderArray[65] = "Packaging";
        genderArray[66] = "Painting & Color Services";
        genderArray[67] = "Petrol Pump";
        genderArray[68] = "RO Water";
        genderArray[69] = "Mobile Sales & Repair";
        genderArray[70] = "Car Towing & Crane Service";
        genderArray[71] = "IT & Software";
        genderArray[72] = "Industrial Oils & Grease";
        genderArray[73] = "Pump Sales Services";
        genderArray[74] = "Nursery";
        genderArray[75] = "Electric Bikes";
        genderArray[76] = "Internet Service Provider";
        genderArray[77] = "Animal Food";
        genderArray[78] = "Civil Contractor";
        genderArray[79] = "Dance Class Academy";
        genderArray[80] = "Watch Manufacturing & Store";
        genderArray[81] = "Housekeeping & Cleaning";
        genderArray[82] = "BJP - Bharatiy Janta Party";
        genderArray[83] = "Indian National Congress";
        genderArray[84] = "AAP - Aam Aadmi Party";
        genderArray[85] = "Shiv Sena Party";
        genderArray[86] = "Samajwadi Party";
        genderArray[87] = "Footwear Store";
        genderArray[88] = "Tyre";
        genderArray[89] = "Stock Market Advisor";
        genderArray[90] = "Diamond";
        genderArray[91] = "Incense Sticks";
        genderArray[92] = "Vehicle GPS Tracker";
        genderArray[93] = "Health Care";
        genderSpinner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(Business_DetailActivity1.this);
                builder.setTitle("Select Business Category");
                builder.setSingleChoiceItems(genderArray, -1, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        ((TextView) v).setText(genderArray[i]);
                        selectedGender = genderArray[i];
                        dialogInterface.dismiss();
                    }
                });
                builder.show();
            }
        });

        getProfile();
    }


    @Override
    protected void onStart() {
        super.onStart();

        userIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGallery();
            }
        });

    }


    private void openGallery() {
        Intent pickPhoto = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(pickPhoto, GALLERY_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case 1:
                if (resultCode == RESULT_OK) {
                    Uri selectedImage = data.getData();
                    userIv.setImageURI(selectedImage);
                    imageUri = selectedImage;
                }
                break;
        }
    }

    private void getProfile() {
        progressBar.setVisibility(View.VISIBLE);
        Retrofit retrofit = RetrofitClient.getRetrofitInstance();
        UserDataApi api = retrofit.create(UserDataApi.class);
        Call<User> call = api.getUserData(Config.API_KEY, id);
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.code() == 200) {
                    if (response.body() != null) {
                        progressBar.setVisibility(View.GONE);
                        User user = response.body();

                        Glide.with(Business_DetailActivity1.this)
                                .load(user.getImageUrl())
                                .into(userIv);


                        etName.setText(user.getName());
                        etEmail.setText(user.getEmail());

                        if(user.getPhone() == null) {
                            etPhone.setText(String.format("+91 %s", ""));
                        } else {
                            etPhone.setText(String.format("+91 %s", user.getPhone()));
                        }

                        etCompanyName.setText(user.getCompany_name());
                        etCompanyAdd.setText(user.getAdress());
                        etWebsite.setText(user.getWebsite());

                        if (user.getGender() != null) {
                            genderSpinner.setText(user.getBusiness_type());
                            selectedGender = user.getBusiness_type();

                        } else {

                            genderSpinner.setText(R.string.male);
                        }


                    }
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
            }
        });


    }

    private void updateProfile(String idString, String emailString, String phoneString, String nameString, String c_name, String c_address, String web, String pass) {
        File file = null;
        RequestBody requestFile = null;
        MultipartBody.Part multipartBody = null;
        try {
            if (imageUri != null) {
                file = FileUtil.from(Business_DetailActivity1.this, imageUri);
                requestFile = RequestBody.create(MediaType.parse("multipart/form-data"),
                        file);

                multipartBody = MultipartBody.Part.createFormData("photo",
                        file.getName(), requestFile);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        RequestBody email = RequestBody.create(MediaType.parse("text/plain"), emailString);
        RequestBody id = RequestBody.create(MediaType.parse("text/plain"), idString);
        RequestBody name = RequestBody.create(MediaType.parse("text/plain"), nameString);
        RequestBody companyName = RequestBody.create(MediaType.parse("text/plain"), c_name);
        RequestBody phone = RequestBody.create(MediaType.parse("text/plain"), phoneString);
        RequestBody companyAdress = RequestBody.create(MediaType.parse("text/plain"), c_address);
        RequestBody website = RequestBody.create(MediaType.parse("text/plain"), web);
        RequestBody key = RequestBody.create(MediaType.parse("text/plain"), Config.API_KEY);
        RequestBody business_type = RequestBody.create(MediaType.parse("text/plain"), selectedGender);

        Retrofit retrofit = RetrofitClient.getRetrofitInstance();
        ProfileApi api = retrofit.create(ProfileApi.class);
        Call<ResponseStatus> call = api.updateProfile2(Config.API_KEY, id, name, email, phone, companyName, companyAdress, website, business_type, multipartBody);
        call.enqueue(new Callback<ResponseStatus>() {
            @Override
            public void onResponse(Call<ResponseStatus> call, Response<ResponseStatus> response) {
                if (response.code() == 200) {
                    if (response.body().getStatus().equalsIgnoreCase("success")) {
                        new ToastMsg(Business_DetailActivity1.this).toastIconSuccess(response.body().getData());
                        startActivity(new Intent(getApplicationContext(), ActivityHome.class));

                    } else {
                        new ToastMsg(Business_DetailActivity1.this).toastIconError(response.body().getData());
                    }
                } else {
                    new ToastMsg(Business_DetailActivity1.this).toastIconError(getString(R.string.something_went_wrong));
                }
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<ResponseStatus> call, Throwable t) {
                new ToastMsg(Business_DetailActivity1.this).toastIconError(getString(R.string.something_went_wrong));
                progressBar.setVisibility(View.GONE);
                Log.e(TAG, t.getLocalizedMessage());
            }
        });
    }

    public void saveUserInfo(User user, String name, String email, String id) {
        SharedPreferences.Editor editor = getSharedPreferences(Constants.USER_LOGIN_STATUS, MODE_PRIVATE).edit();
        editor.putString("name", name);
        editor.putString("email", email);
        editor.putString("id", id);
        editor.putBoolean("status", true);
        editor.putBoolean(Constants.USER_LOGIN_STATUS, true);
        editor.apply();

        DatabaseHelper db = new DatabaseHelper(Business_DetailActivity1.this);
        db.deleteUserData();
        db.insertUserData(user);
        ApiResources.USER_PHONE = user.getPhone();
        updateSubscriptionStatus(user.getUserId());

    }

    private void updateSubscriptionStatus(String userId) {
        Retrofit retrofit = RetrofitClient.getRetrofitInstance();
        SubscriptionApi subscriptionApi = retrofit.create(SubscriptionApi.class);

        Call<ActiveStatus> call = subscriptionApi.getActiveStatus(Config.API_KEY, userId);
        call.enqueue(new Callback<ActiveStatus>() {
            @Override
            public void onResponse(Call<ActiveStatus> call, Response<ActiveStatus> response) {
                if (response.code() == 200) {
                    if (response.body() != null) {
                        ActiveStatus activeStatus = response.body();

                        DatabaseHelper db = new DatabaseHelper(Business_DetailActivity1.this);
                        db.deleteAllActiveStatusData();
                        db.insertActiveStatusData(activeStatus);

                        Intent intent = new Intent(Business_DetailActivity1.this, Business_DetailActivity1.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);

                        startActivity(intent);
                        finish();
                        dialog.cancel();
                    }
                }
            }

            @Override
            public void onFailure(Call<ActiveStatus> call, Throwable t) {
                t.printStackTrace();
                new ToastMsg(Business_DetailActivity1.this).toastIconError(getResources().getString(R.string.something_went_wrong));
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


}
