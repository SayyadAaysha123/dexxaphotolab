package com.dexxaphotolab.app.Fragments;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.dexxaphotolab.app.Adapters.PlanAdapter;
import com.dexxaphotolab.app.R;
import com.dexxaphotolab.app.payment.Config;
import com.dexxaphotolab.app.payment.Network.RetrofitClient;
import com.dexxaphotolab.app.payment.Network.apis.PackageApi;
import com.dexxaphotolab.app.payment.Network.models.ActiveStatus;
import com.dexxaphotolab.app.payment.Network.models.AllPackage;
import com.dexxaphotolab.app.payment.Network.models.Package;
import com.dexxaphotolab.app.payment.Network.models.User;
import com.dexxaphotolab.app.payment.Utils.PreferenceUtils;
import com.dexxaphotolab.app.payment.activity.LoginActivity;
import com.dexxaphotolab.app.payment.activity.SubscriptionActivity;
import com.dexxaphotolab.app.payment.database.DatabaseHelper;
import com.google.android.flexbox.AlignItems;
import com.google.android.flexbox.FlexDirection;
import com.google.android.flexbox.FlexboxLayoutManager;
import com.google.android.flexbox.JustifyContent;

import static android.content.Context.MODE_PRIVATE;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;

public class Tab_Subscription extends Fragment {

    View view;
    LinearLayout rasbita_subscribek,subscribe_detail_plan;
    LinearLayout ll_subscribe1,ll_subscribe0;
    private boolean status = false;
    private TextView activeUserName, activeEmail, activeActivePlan, activeExpireDate;

    LinearLayoutManager linearLayoutManager;
    FlexboxLayoutManager manager;
    private List<Package> packages = new ArrayList<>();
    PlanAdapter adapter;
    RecyclerView plansDetail;

    public static Tab_Subscription getInstance() {
        Tab_Subscription tab_sub = new Tab_Subscription();

        return tab_sub;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


        view = inflater.inflate(R.layout.ptab_subsciption, container, false);

        status = PreferenceUtils.isLoggedIn(getContext());

        rasbita_subscribek = view.findViewById(R.id.rasbita_subscribe);
        activeUserName =  view.findViewById(R.id.active_user_name);
        activeEmail =  view.findViewById(R.id.active_email);
        activeActivePlan =  view.findViewById(R.id.active_active_plan);
        activeExpireDate =  view.findViewById(R.id.active_expire_date);
        subscribe_detail_plan =  view.findViewById(R.id.subscribe_detail_plan);
        ll_subscribe1 =  view.findViewById(R.id.ll_subscribe1);
        ll_subscribe0 =  view.findViewById(R.id.ll_subscribe0);
        plansDetail = view.findViewById(R.id.rv_plans);

        linearLayoutManager = new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL, false);

        manager = new FlexboxLayoutManager(getContext());
        manager.setAlignItems(AlignItems.CENTER);
        manager.setJustifyContent(JustifyContent.CENTER);
        manager.setFlexDirection(FlexDirection.ROW);

        getPlans();

        SharedPreferences prefsss = getActivity().getSharedPreferences("subscibe11", MODE_PRIVATE);
        String substatus = prefsss.getString("subscribe", "0");

        if (PreferenceUtils.isActivePlan(getContext())) {
            ll_subscribe0.setVisibility(View.GONE);
            ll_subscribe1.setVisibility(View.VISIBLE);
        }else{
            ll_subscribe0.setVisibility(View.VISIBLE);
            ll_subscribe1.setVisibility(View.GONE);
        }


        rasbita_subscribek.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.e("hhhhh", "onCreateView: "+status );
                if (status) {
                    Intent i = new Intent(getContext(), SubscriptionActivity.class);
                    startActivity(i);


                }else{
                    Intent i = new Intent(getContext(), LoginActivity.class);
                    startActivity(i);
                }


            }
        });

        subscribe_detail_plan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(getContext(), SubscriptionActivity.class);
                startActivity(i);
            }
        });


            DatabaseHelper db = new DatabaseHelper(getContext());
            if (db.getActiveStatusCount() > 0 && db.getUserDataCount() > 0) {

                ActiveStatus activeStatus = db.getActiveStatusData();
                User user = db.getUserData();
                activeUserName.setText(user.getName());
                activeEmail.setText(user.getEmail());
                activeActivePlan.setText(activeStatus.getPackageTitle());
                activeExpireDate.setText(activeStatus.getExpireDate());

            }

        return view;
    }

    private void getPlans() {
        Retrofit retrofit = RetrofitClient.getRetrofitInstance();
        final PackageApi packageApi = retrofit.create(PackageApi.class);
        Call<AllPackage> call = packageApi.getAllPackage(Config.API_KEY);
        call.enqueue(new Callback<AllPackage>() {
            @Override
            public void onResponse(Call<AllPackage> call, retrofit2.Response<AllPackage> response) {
                if (response.code() == 200) {

                    packages = response.body().getPackage();

                    if (packages.size() == 1) {
                        plansDetail.setLayoutManager(manager);
                    } else {
                        plansDetail.setLayoutManager(linearLayoutManager);
                    }

                    adapter = new PlanAdapter(getContext(), packages);
                    plansDetail.setAdapter(adapter);

                }

            }

            @Override
            public void onFailure(Call<AllPackage> call, Throwable t) {


            }
        });
    }

}
