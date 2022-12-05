package com.dexxaphotolab.app.Fragments;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.dexxaphotolab.app.Activities.WebsiteActivity;
import com.dexxaphotolab.app.R;
import com.dexxaphotolab.app.Utills.Constance;
import com.google.android.gms.analytics.Tracker;

public class FragmentContactUs extends Fragment {

    Context context;
    View view;
    LinearLayout ll_call_us,ll_email_us,ll_website;
    TextView tv_mobilenumber,tv_emailus;
    private static final int REQUEST_PHONE_CALL = 1;
    Tracker mTracker;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_contactus, container, false);
        context = getContext();
        bindView();

        tv_mobilenumber.setText(Constance.mobileNo);
        tv_emailus.setText(Constance.emailId);
        ll_call_us.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent callIntent = new Intent(Intent.ACTION_CALL);
                callIntent.setData(Uri.parse("tel:" + Constance.mobileNo));
                if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (ContextCompat.checkSelfPermission(context, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CALL_PHONE},REQUEST_PHONE_CALL);
                    }
                    else
                    {
                        startActivity(callIntent);
                    }
                }
                else
                {
                    startActivity(callIntent);
                }

            }
        });
        ll_email_us.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent emailSelectorIntent = new Intent(Intent.ACTION_SENDTO);
                emailSelectorIntent.setData(Uri.parse("mailto:"));

                final Intent emailIntent = new Intent(Intent.ACTION_SEND);
                emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{Constance.emailId});

                emailIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                emailIntent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                emailIntent.setSelector(emailSelectorIntent);
                startActivity(emailIntent);
            }
        });

        ll_website.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i=new Intent(context, WebsiteActivity.class);
                startActivity(i);
            }
        });
        return view;
    }
    public void bindView()
    {

        ll_call_us=view.findViewById(R.id.ll_call_us);
        ll_email_us=view.findViewById(R.id.ll_email_us);
        tv_mobilenumber=view.findViewById(R.id.tv_mobilenumber);
        tv_emailus=view.findViewById(R.id.tv_emailus);
        ll_website=view.findViewById(R.id.ll_website);

    }


}
