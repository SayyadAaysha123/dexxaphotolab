package com.dexxaphotolab.app.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.dexxaphotolab.app.R;
import com.dexxaphotolab.app.Utills.Constance;
import com.google.android.gms.analytics.Tracker;

public class FragmentAboutUs extends Fragment {

    Context context;
    View view;
    Tracker mTracker;
    TextView tv_aboutus;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_aboutus, container, false);
        context = getContext();
        bindView();
        tv_aboutus.setText(Constance.aboutUs);
        return view;
    }
    public void bindView()
    {
        tv_aboutus=view.findViewById(R.id.tv_aboutus);

    }
}
