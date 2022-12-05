package com.dexxaphotolab.app.payment.activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.dexxaphotolab.app.payment.Config;
import com.dexxaphotolab.app.payment.Network.RetrofitClient;
import com.dexxaphotolab.app.payment.Network.apis.DeactivateAccountApi;
import com.dexxaphotolab.app.payment.Network.apis.ProfileApi;
import com.dexxaphotolab.app.payment.Network.apis.SetPasswordApi;
import com.dexxaphotolab.app.payment.Network.apis.UserDataApi;
import com.dexxaphotolab.app.payment.Network.models.ResponseStatus;
import com.dexxaphotolab.app.payment.Network.models.User;
import com.dexxaphotolab.app.payment.Utils.Constants;
import com.dexxaphotolab.app.payment.Utils.FileUtil;
import com.dexxaphotolab.app.payment.Utils.PreferenceUtils;
import com.dexxaphotolab.app.payment.Utils.RtlUtils;
import com.dexxaphotolab.app.payment.Utils.ToastMsg;

import java.io.File;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class ProfileActivity extends AppCompatActivity {
    private static final String TAG = ProfileActivity.class.getSimpleName();
    private EditText etName, etEmail, etPhone, genderSpinner, et_company_name, et_website_add, et_company_add;
    private TextInputEditText etPass, etCurrentPassword;
    private Button btnUpdate, deactivateBt, setPasswordBtn;
    private ProgressDialog dialog;
    private String URL = "", strGender;
    private CircleImageView userIv;
    private static final int GALLERY_REQUEST_CODE = 1;
    private Uri imageUri;
    private ProgressBar progressBar;
    private String id;
    private boolean isDark;
    private String selectedGender = "Other";
    LinearLayout linearAds;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        RtlUtils.setScreenDirection(this);
        SharedPreferences sharedPreferences = getSharedPreferences("push", MODE_PRIVATE);
        isDark = sharedPreferences.getBoolean("dark", false);

        setTheme(R.style.AppThemeLight);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.pactivity_profile);
        Toolbar toolbar = findViewById(R.id.toolbar);
        SharedPreferences prefsss = getSharedPreferences("subscibe11", MODE_PRIVATE);
        String substatus = prefsss.getString("subscribe", "0");

        if (substatus.equals("0")) {

            linearAds = findViewById(R.id.banner_container);
            new Admanager(this).loadBanner(ProfileActivity.this, findViewById(R.id.banner_container));
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

        et_company_name = findViewById(R.id.company_name);
        et_company_add = findViewById(R.id.company_add);
        et_website_add = findViewById(R.id.website_add);
        etName = findViewById(R.id.name);
        etEmail = findViewById(R.id.email);
        etPhone = findViewById(R.id.phone);
        etPass = findViewById(R.id.password);
        etCurrentPassword = findViewById(R.id.currentPassword);
        btnUpdate = findViewById(R.id.signup);
        userIv = findViewById(R.id.user_iv);
        progressBar = findViewById(R.id.progress_bar);
        deactivateBt = findViewById(R.id.deactive_bt);
        genderSpinner = findViewById(R.id.gender_spinner);
        setPasswordBtn = findViewById(R.id.setPasswordBtn);

        id = PreferenceUtils.getUserId(ProfileActivity.this);


        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (etEmail.getText().toString().equals("")) {
                    Toast.makeText(ProfileActivity.this, "Please enter valid email.", Toast.LENGTH_LONG).show();
                    return;
                } else if (etName.getText().toString().equals("")) {
                    Toast.makeText(ProfileActivity.this, "Please enter name.", Toast.LENGTH_LONG).show();
                    return;
                } else if (etCurrentPassword.getText().toString().equals("")) {
                    new ToastMsg(ProfileActivity.this).toastIconError("Current password must not be empty.");
                    return;
                }
                progressBar.setVisibility(View.VISIBLE);

                String email = etEmail.getText().toString();
                String phone = etPhone.getText().toString();
                String pass = etPass.getText().toString();
                String currentPass = etCurrentPassword.getText().toString();
                String name = etName.getText().toString();
                String company_name = et_company_name.getText().toString();
                String add = et_company_add.getText().toString();
                String web = et_website_add.getText().toString();

                updateProfile(id, email, phone, name, company_name, add, web, pass, currentPass);

            }
        });

        setPasswordBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showSetPasswordDialog();
            }
        });

        //gender spinner setup
        final String[] genderArray = new String[94];
        genderArray[0] = "Real Estate";
        genderArray[1] = "Marble & CVeramic";
        genderArray[2] = "Clothes Business";
        genderArray[3] = "Daily & Sweets Store";
        genderArray[4] = "Electrical Services Provider";
        genderArray[5] = "FMCG & Grocery";
        genderArray[6] = "Hardware & Sanitaryware";
        genderArray[7] = "Tours & Travels";
        genderArray[8] = "Wood Buisness";
        genderArray[9] = "Resaurant,Cafe & Catering";
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
        genderArray[46] = "Steel and Aluminumn Business";
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
        genderArray[66] = "Painting & Color Services	";
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
        genderArray[83] = "Idian National Congress";
        genderArray[84] = "AAP - Aam Aadmi Party";
        genderArray[85] = "Shiv Sena Party";
        genderArray[86] = "Samajwadi Party";
        genderArray[87] = "Footware Store";
        genderArray[88] = "Tyre";
        genderArray[89] = "Stock Market Advisor";
        genderArray[90] = "Diamond";
        genderArray[91] = "Incense Sticks";
        genderArray[92] = "Vehicle GPS Tracker";
        genderArray[93] = "Helth Care";
        genderSpinner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(ProfileActivity.this);
                builder.setTitle("Select Gender");
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

        deactivateBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDeactivationDialog();
            }
        });
    }

    private void showDeactivationDialog() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = LayoutInflater.from(this).inflate(R.layout.playout_deactivate, null);
        builder.setView(view);
        final AlertDialog alertDialog = builder.create();
        alertDialog.show();

        final EditText passEt = view.findViewById(R.id.pass_et);
        final EditText reasonEt = view.findViewById(R.id.reason_et);
        final Button okBt = view.findViewById(R.id.ok_bt);
        Button cancelBt = view.findViewById(R.id.cancel_bt);
        ImageView closeIv = view.findViewById(R.id.close_iv);
        final ProgressBar progressBar = view.findViewById(R.id.progress_bar);
        LinearLayout topLayout = view.findViewById(R.id.top_layout);
        if (isDark) {
            topLayout.setBackgroundColor(getResources().getColor(R.color.overlay_dark_30));
        }

        okBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String pass = passEt.getText().toString();
                String reason = reasonEt.getText().toString();

                if (TextUtils.isEmpty(pass)) {
                    new ToastMsg(ProfileActivity.this).toastIconError("Please enter your password");
                    return;
                } else if (TextUtils.isEmpty(reason)) {
                    new ToastMsg(ProfileActivity.this).toastIconError("Please enter your reason");
                    return;
                }
                deactivateAccount(pass, reason, alertDialog, progressBar);
            }
        });

        cancelBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });

        closeIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });


    }

    private void deactivateAccount(String pass, String reason, final AlertDialog alertDialog, final ProgressBar progressBar) {
        Retrofit retrofit = RetrofitClient.getRetrofitInstance();
        DeactivateAccountApi api = retrofit.create(DeactivateAccountApi.class);
        Call<ResponseStatus> call = api.deactivateAccount(id, pass, reason, Config.API_KEY);
        call.enqueue(new Callback<ResponseStatus>() {
            @Override
            public void onResponse(Call<ResponseStatus> call, Response<ResponseStatus> response) {
                if (response.code() == 200) {
                    ResponseStatus resStatus = response.body();
                    if (resStatus.getStatus().equalsIgnoreCase("success")) {
                        logoutUser();

                        new ToastMsg(ProfileActivity.this).toastIconSuccess(resStatus.getData());

                        if (PreferenceUtils.isMandatoryLogin(ProfileActivity.this)) {
                            Intent intent = new Intent(ProfileActivity.this, LoginActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                        } else {
                            Intent intent = new Intent(ProfileActivity.this, ActivityHome.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                        }
                        alertDialog.dismiss();
                        finish();
                    } else {
                        new ToastMsg(ProfileActivity.this).toastIconError(resStatus.getData());
                        alertDialog.dismiss();
                    }

                } else {
                    new ToastMsg(ProfileActivity.this).toastIconError("Something went wrong");
                    alertDialog.dismiss();
                }
            }

            @Override
            public void onFailure(Call<ResponseStatus> call, Throwable t) {
                t.printStackTrace();
                new ToastMsg(ProfileActivity.this).toastIconError("Something went wrong");
                alertDialog.dismiss();
            }
        });
    }

    public void logoutUser() {
        DatabaseHelper databaseHelper = new DatabaseHelper(ProfileActivity.this);
        databaseHelper.deleteAllDownloadData();
        databaseHelper.deleteUserData();
        databaseHelper.deleteAllActiveStatusData();

        SharedPreferences.Editor sp = getSharedPreferences(Constants.USER_LOGIN_STATUS, MODE_PRIVATE).edit();
        sp.putBoolean(Constants.USER_LOGIN_STATUS, false);
        sp.apply();
        sp.commit();
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


                        Glide.with(ProfileActivity.this)
                                .load(user.getImageUrl())
                                .into(userIv);


                        etName.setText(user.getName());
                        etEmail.setText(user.getEmail());
                        etPhone.setText(user.getPhone());
                        et_company_name.setText(user.getCompany_name());
                        et_website_add.setText(user.getWebsite());
                        et_company_add.setText(user.getAdress());
                        if (user.getBusiness_type() != null) {
                            genderSpinner.setText(user.getBusiness_type());
                            selectedGender = user.getBusiness_type();

                        } else {
                            genderSpinner.setText(R.string.male);
                        }

                        if (!user.isPasswordAvailable()) {
                            btnUpdate.setVisibility(View.GONE);
                            etCurrentPassword.setVisibility(View.GONE);
                            etPass.setVisibility(View.GONE);
                            setPasswordBtn.setVisibility(View.VISIBLE);
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

    private void updateProfile(String idString, String emailString, String phoneString, String nameString, String cnameString, String addString, String webString, String passString, String currentPassString) {
        File file = null;
        RequestBody requestFile = null;
        MultipartBody.Part multipartBody = null;
        try {
            if (imageUri != null) {
                file = FileUtil.from(ProfileActivity.this, imageUri);
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
        RequestBody password = RequestBody.create(MediaType.parse("text/plain"), passString);
        RequestBody phone = RequestBody.create(MediaType.parse("text/plain"), phoneString);
        RequestBody c_name = RequestBody.create(MediaType.parse("text/plain"), cnameString);
        RequestBody c_address = RequestBody.create(MediaType.parse("text/plain"), addString);
        RequestBody c_website = RequestBody.create(MediaType.parse("text/plain"), webString);
        RequestBody c_businesstype = RequestBody.create(MediaType.parse("text/plain"), selectedGender);
        RequestBody currentPass = RequestBody.create(MediaType.parse("text/plain"), currentPassString);
        RequestBody gender = RequestBody.create(MediaType.parse("text/plain"), selectedGender);
        RequestBody key = RequestBody.create(MediaType.parse("text/plain"), Config.API_KEY);

        Retrofit retrofit = RetrofitClient.getRetrofitInstance();
        ProfileApi api = retrofit.create(ProfileApi.class);
        Call<ResponseStatus> call = api.updateProfile(Config.API_KEY, id, name, email, phone, c_name, c_address, c_website, c_businesstype, password, currentPass, multipartBody, gender);
        call.enqueue(new Callback<ResponseStatus>() {
            @Override
            public void onResponse(Call<ResponseStatus> call, Response<ResponseStatus> response) {
                if (response.code() == 200) {
                    if (response.body().getStatus().equalsIgnoreCase("success")) {
                        new ToastMsg(ProfileActivity.this).toastIconSuccess(response.body().getData());
                        getProfile();
                    } else {
                        new ToastMsg(ProfileActivity.this).toastIconError(response.body().getData());
                    }
                } else {
                    new ToastMsg(ProfileActivity.this).toastIconError(getString(R.string.something_went_wrong) + "1");
                }
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<ResponseStatus> call, Throwable t) {
                new ToastMsg(ProfileActivity.this).toastIconError(getString(R.string.something_went_wrong) + "2");
                progressBar.setVisibility(View.GONE);
                Log.e(TAG, t.getLocalizedMessage());
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

    private void showSetPasswordDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(ProfileActivity.this);
        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.ppassword_entry_layout, null);
        builder.setView(view);
        builder.setCancelable(false);
        final AlertDialog alertDialog = builder.create();
        alertDialog.show();

        final EditText passEt = view.findViewById(R.id.passwordEt);
        final EditText confirmPassEt = view.findViewById(R.id.confirmPasswordEt);
        Button setButton = view.findViewById(R.id.setButton);
        Button cancelButton = view.findViewById(R.id.cancelButton);
        setButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String password = passEt.getText().toString();
                String confirmPass = confirmPassEt.getText().toString();
                if (!password.isEmpty() && !confirmPass.isEmpty()) {
                    if (password.equals(confirmPass)) {
                        alertDialog.dismiss();
                        setPassword(password);
                    } else {
                        confirmPassEt.setError("Password mismatch.");
                    }
                }
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
            }
        });
    }

    private void setPassword(String password) {
        final ProgressDialog dialog = new ProgressDialog(ProfileActivity.this);
        dialog.setMessage("Please wait..");
        dialog.setCancelable(false);
        dialog.show();
        String uid = "";
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null)
            uid = user.getUid();
        else {
            dialog.dismiss();
            new ToastMsg(ProfileActivity.this).toastIconError(getString(R.string.something_went_text));
            return;
        }

        Retrofit retrofit = RetrofitClient.getRetrofitInstance();
        SetPasswordApi api = retrofit.create(SetPasswordApi.class);
        Call<ResponseStatus> call = api.setPassword(Config.API_KEY, id, password, uid);
        call.enqueue(new Callback<ResponseStatus>() {
            @Override
            public void onResponse(Call<ResponseStatus> call, Response<ResponseStatus> response) {
                if (response.code() == 200) {
                    if (response.body() != null) {
                        ResponseStatus status = response.body();
                        if (status.getStatus().equalsIgnoreCase("success")) {
                            new ToastMsg(ProfileActivity.this).toastIconSuccess("Password set successfully.");
                            setPasswordBtn.setVisibility(View.GONE);
                            btnUpdate.setVisibility(View.VISIBLE);
                            etCurrentPassword.setVisibility(View.VISIBLE);
                            etPass.setVisibility(View.VISIBLE);
                            getProfile();

                        } else {
                            new ToastMsg(ProfileActivity.this).toastIconError(getString(R.string.something_went_text));
                        }
                    } else {
                        new ToastMsg(ProfileActivity.this).toastIconError(getString(R.string.something_went_text));
                    }
                } else {
                    new ToastMsg(ProfileActivity.this).toastIconError(getString(R.string.something_went_text));
                }

                dialog.dismiss();
            }

            @Override
            public void onFailure(Call<ResponseStatus> call, Throwable t) {
                new ToastMsg(ProfileActivity.this).toastIconError(getString(R.string.something_went_text));
                Log.e("ProfileActivity", t.getLocalizedMessage());
                dialog.dismiss();
            }
        });
    }

}
