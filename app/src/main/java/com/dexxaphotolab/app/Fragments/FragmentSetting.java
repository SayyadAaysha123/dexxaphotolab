package com.dexxaphotolab.app.Fragments;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.dexxaphotolab.app.Activities.ActivityHome;
import com.dexxaphotolab.app.R;
import com.dexxaphotolab.app.Utills.Constance;
import com.dexxaphotolab.app.payment.Config;
import com.dexxaphotolab.app.payment.Utils.Constants;
import com.dexxaphotolab.app.payment.Utils.PreferenceUtils;
import com.dexxaphotolab.app.payment.activity.Business_DetailActivity1;
import com.dexxaphotolab.app.payment.activity.FirebaseSignUpActivity;
import com.dexxaphotolab.app.payment.database.DatabaseHelper;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import static android.content.Context.MODE_PRIVATE;
import static android.provider.ContactsContract.Directory.PACKAGE_NAME;

public class FragmentSetting extends Fragment {

    Context context;
    View view;

    LinearLayout llPrivacyPolicy;
    LinearLayout llShare;
    LinearLayout llRateus;
    LinearLayout llLogin, llProfile, llSignout;
    private boolean status = false;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragmen_setting, container, false);
        context = getContext();
        bindView();


        status = PreferenceUtils.isLoggedIn(getContext());
        Log.e("hhhhh", "onCreateView: " + status);
        if (status) {
            llLogin.setVisibility(View.GONE);
            llProfile.setVisibility(View.VISIBLE);
            llSignout.setVisibility(View.VISIBLE);


        } else {
            llLogin.setVisibility(View.VISIBLE);
            llProfile.setVisibility(View.GONE);
            llSignout.setVisibility(View.GONE);

        }


        llLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent intent = new Intent(getContext(), FirebaseSignUpActivity.class);
                startActivity(intent);


            }
        });

        llProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Constance.deleteCache(getContext());

                Intent intent = new Intent(getContext(), Business_DetailActivity1.class);
                startActivity(intent);


            }
        });

        llSignout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                new AlertDialog.Builder(getContext()).setMessage("Are you sure to logout ?")
                        .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                                if (user != null) {
                                    FirebaseAuth.getInstance().signOut();
                                }

                                SharedPreferences.Editor editor = getActivity().getSharedPreferences(Constants.USER_LOGIN_STATUS, MODE_PRIVATE).edit();
                                editor.putBoolean(Constants.USER_LOGIN_STATUS, false);
                                editor.apply();
                                editor.commit();

                                SharedPreferences.Editor editor1 = getActivity().getSharedPreferences("subscibe11", MODE_PRIVATE).edit();
                                editor1.clear().commit();

                                DatabaseHelper databaseHelper = new DatabaseHelper(getContext());
                                databaseHelper.deleteUserData();

                                PreferenceUtils.clearSubscriptionSavedData(getContext());

                                Intent intent = new Intent(getContext(), ActivityHome.class);
                                startActivity(intent);
                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        }).create().show();


            }
        });

        llPrivacyPolicy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(Config.TERMS_URL));
                startActivity(intent);
            }
        });

        llShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Intent sharingIntent = new Intent(Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                String shareBody = getString(R.string.share_message) + "\n" +
                        "https://play.google.com/store/apps/details?id=" + PACKAGE_NAME +
                        " E-Paper, Save Trees, Save Earth";
                String shareSub = "Your subject here";
                sharingIntent.putExtra(Intent.EXTRA_SUBJECT, shareSub);
                sharingIntent.putExtra(Intent.EXTRA_TEXT, shareBody);
                startActivity(Intent.createChooser(sharingIntent, "Share using"));

            }
        });

        llRateus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {



                final String appPackageName = getActivity().getPackageName();
                try {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
                } catch (android.content.ActivityNotFoundException anfe) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
                }

            }
        });

        return view;
    }

    public void bindView() {

        llPrivacyPolicy = view.findViewById(R.id.llPrivacyPolicy);
        llShare = view.findViewById(R.id.llShare);
        llRateus = view.findViewById(R.id.llRateus);
        llLogin = view.findViewById(R.id.llLogin);
        llSignout = view.findViewById(R.id.llSignout);
        llProfile = view.findViewById(R.id.llProfile);

    }


    @Override
    public void onResume() {


        super.onResume();

    }


}
