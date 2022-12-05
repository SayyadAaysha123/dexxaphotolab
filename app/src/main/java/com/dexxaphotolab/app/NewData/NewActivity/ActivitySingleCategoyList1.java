package com.dexxaphotolab.app.NewData.NewActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.dexxaphotolab.app.Activities.ActivityCreatePost;
import com.dexxaphotolab.app.Activities.ActivityHome;
import com.dexxaphotolab.app.Adapters.AdapterCategoryList;
import com.dexxaphotolab.app.Adapters.SubCategoryAdapter;
import com.dexxaphotolab.app.ModelRetrofit.CategoriesData;
import com.dexxaphotolab.app.ModelRetrofit.VideoHomeData;
import com.dexxaphotolab.app.Retrofit.Base_Url1;
import com.dexxaphotolab.app.payment.Utils.PreferenceUtils;
import com.dexxaphotolab.app.payment.activity.LoginActivity;
import com.dexxaphotolab.app.AnalyticsApplication;
import com.dexxaphotolab.app.Model.SubCategoryModel;
import com.dexxaphotolab.app.R;
import com.dexxaphotolab.app.Utills.Admanager;
import com.dexxaphotolab.app.Utills.Constance;
import com.dexxaphotolab.app.Utills.SharedPrefrenceConfig;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import cn.xm.weidongjian.popuphelper.PopupWindowHelper;

public class ActivitySingleCategoyList1 extends AppCompatActivity implements View.OnClickListener {

        public static String finalimage;
        public static ActivitySingleCategoyList1 instance = null;
        public static ArrayList<SubCategoryModel> cat1_list = new ArrayList<>();
        public PopupWindowHelper popupWindowHelper;
        public SubCategoryAdapter adapter;
        Context context;
        RelativeLayout progressBar;
        RecyclerView rv_singlecatlist;
        TextView tv_singlecatname;
        String childitemtittle, catnameid, catType;
        ImageView iv_backarrow, iv_firstimage, iv_al_language;
        ArrayList<CategoriesData> modelsingleChildList;
        ArrayList<VideoHomeData> businessList, greetingImage;
        LinearLayout ll_next_singlecatlist;
        SharedPrefrenceConfig sharedPrefrenceConfig;
        Tracker mTracker;
        String language;
        View popupview_down;
        String frameName[] = {"All", "English", "Hindi", "Gujarati"};
        String catid, i_image;
        TextView typeAll, typeEnglish, typeGujarati, typeHindi;
        private boolean status = false;
        LinearLayout ll_dialog;
        LinearLayout ll_subscribe;
        RelativeLayout rl_close;
        boolean dataLoad;
        String vType;


        public ActivitySingleCategoyList1() {
                instance = ActivitySingleCategoyList1.this;
        }

        public static synchronized ActivitySingleCategoyList1 getInstance() {
                if (instance == null) {
                        instance = new ActivitySingleCategoyList1();
                }
                return instance;
        }

        @Override
        public void onBackPressed() {
                if(ll_dialog.getVisibility() == View.VISIBLE) {
                        ll_dialog.setVisibility(View.GONE);
                } else {
                        super.onBackPressed();
                }
        }

        @Override
        protected void onCreate(Bundle savedInstanceState) {

                super.onCreate(savedInstanceState);
                setContentView(R.layout.activity_single_categoy_list1);
                context = ActivitySingleCategoyList1.this;
                bindView();
                sharedPrefrenceConfig = new SharedPrefrenceConfig(context);
                language = sharedPrefrenceConfig.getPrefString(context, Constance.language, "");

                new Admanager(this).loadBanner(ActivitySingleCategoyList1.this, findViewById(R.id.banner_container));

                AnalyticsApplication application = (AnalyticsApplication) getApplication();
                mTracker = application.getDefaultTracker();


                Intent i = getIntent();
                catid = i.getStringExtra("id");
                i_image = i.getStringExtra("image");
                vType = i.getStringExtra("vType");
                childitemtittle = i.getStringExtra("cat_name");

                finalimage = i_image;

                Log.e("iiiii", "onCreate: " + i_image);
                Glide.with(context).load(i_image).placeholder(R.drawable.placeholder).into(iv_firstimage);

                catType = "1";
                catnameid = "1";


                tv_singlecatname.setText(childitemtittle);
                rv_singlecatlist.setLayoutManager(new GridLayoutManager(context, 3));

                modelsingleChildList = new ArrayList<>();
                cat1_list.clear();
                Apicat2();

                iv_backarrow.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                                onBackPressed();
                        }
                });
                ll_next_singlecatlist.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                                Constance.deleteCache(getApplicationContext());
                                Constance.ComeFrom = "image";
                                Constance.FromSinglecatActivity = finalimage;
                                Log.d("sdasjdiaisdi1111", "sdhjsd" + Constance.FromSinglecatActivity);

                                Constance.activityName = catType;

                                status = PreferenceUtils.isLoggedIn(getApplicationContext());
                                Log.e("hhhhh", "onCreateView: " + status);
                                if (status) {


                                        Constance.activityName = catType;
                                        Intent i = new Intent(context, ActivityCreatePost.class);
                                        i.putExtra("name", childitemtittle);
                                        startActivity(i);

                                } else {

                                        AlertDialog.Builder builder = new AlertDialog.Builder(context);
                                        builder.setMessage("you need a account to continue ... ")
                                                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                                        public void onClick(DialogInterface dialog, int id) {

                                                                Intent i = new Intent(context, LoginActivity.class);
                                                                startActivity(i);
                                                                dialog.cancel();
                                                        }
                                                })
                                                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                                        public void onClick(DialogInterface dialog, int id) {
                                                                dialog.cancel();

                                                        }
                                                });
                                        AlertDialog alert = builder.create();
                                        alert.show();


                                }


                        }
                });

                iv_al_language.setOnClickListener(new View.OnClickListener() {

                        @Override
                        public void onClick(View v) {


                                popupview_down = LayoutInflater.from(context).inflate(R.layout.category_dropdown_raw, null);
                                popupWindowHelper = new PopupWindowHelper(popupview_down);
                                popupWindowHelper.showAsDropDown(v, 0, 0);
                                RecyclerView rv_cat_dd_raw = popupview_down.findViewById(R.id.rv_cat_dd_raw);
                                GridLayoutManager gridLayoutManager = new GridLayoutManager(getApplicationContext(), 1);
                                rv_cat_dd_raw.setLayoutManager(gridLayoutManager);
                                rv_cat_dd_raw.setHasFixedSize(true);

                                AdapterCategoryList adapterCategory = new AdapterCategoryList(context, frameName, "allList");
                                rv_cat_dd_raw.setAdapter(adapterCategory);
                        }
                });

                ll_subscribe.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                                startActivity(new Intent(getApplicationContext(), ActivityHome.class));
                        }
                });

                rl_close.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                                ll_dialog.setVisibility(View.GONE);
                        }
                });
        }

        @Override
        protected void onResume() {
                super.onResume();
                dataLoad = false;
                Log.d("fhauhfdsfsdf", "dhasufhuashn");
                mTracker.setScreenName("Image~" + "Google Analytics Testing");
                mTracker.send(new HitBuilders.ScreenViewBuilder().build());
        }


        public void bindView() {

                rv_singlecatlist = findViewById(R.id.rv_singlecatlist);
                tv_singlecatname = findViewById(R.id.tv_singlecatname);
                iv_backarrow = findViewById(R.id.iv_backarrow);
                iv_firstimage = findViewById(R.id.iv_firstimage);
                ll_next_singlecatlist = findViewById(R.id.ll_next_singlecatlist);
                iv_al_language = findViewById(R.id.iv_al_language);
                progressBar = findViewById(R.id.rl_progress);
                businessList = new ArrayList<>();
                greetingImage = new ArrayList<>();

                ll_dialog = findViewById(R.id.ll_dialog);
                ll_subscribe = findViewById(R.id.ll_subscribe);
                rl_close = findViewById(R.id.rl_close);

                typeAll = findViewById(R.id.type_all);
                typeEnglish = findViewById(R.id.type_english);
                typeGujarati = findViewById(R.id.type_gujarati);
                typeHindi = findViewById(R.id.type_hindi);

                typeAll.setOnClickListener(this::onClick);
                typeEnglish.setOnClickListener(this::onClick);
                typeGujarati.setOnClickListener(this::onClick);
                typeHindi.setOnClickListener(this::onClick);

        }


        public void setselectedimage(String childitemimage) {
                finalimage = childitemimage;
                Glide.with(context).load(childitemimage).placeholder(R.drawable.placeholder).into(iv_firstimage);

        }

        private void Apicat2() {

                progressBar.setVisibility(View.VISIBLE);
                rv_singlecatlist.setVisibility(View.GONE);

                StringRequest request = new StringRequest(Request.Method.GET, Base_Url1.BASE_URL + "single_details?type=" + vType + "&id=" + catid,
                        new com.android.volley.Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                        Log.e("000000", "onResponse: " + catid);

                                        Log.e("llllll", "onResponse: " + response);
                                        JSONObject obj = null;

                                        try {

                                                obj = new JSONObject(response);
                                                JSONArray Object1 = obj.getJSONArray("season");

                                                for (int i = 0; i < Object1.length(); i++) {

                                                        JSONObject Object = Object1.getJSONObject(i);
                                                        JSONArray Object2 = Object.getJSONArray("episodes");

                                                        for (int j = 0; j < Object2.length(); j++) {

                                                                JSONObject obj2 = Object2.getJSONObject(j);

                                                                String channelImg = obj2.optString("image_url");
                                                                String channelNamne = obj2.optString("episodes_name");
                                                                String language = obj2.optString("language");
                                                                String paid = obj2.optString("paid_free");
                                                                String fileType = obj2.optString("Poster_Video");
                                                                String fileUrl = obj2.optString("file_url");

                                                                Log.e("kkkk", "onResponse: " + channelImg);

                                                                SubCategoryModel bean1 = new SubCategoryModel(false);
                                                                bean1.setSubcatImg(channelImg);
                                                                bean1.setSubcatName(channelNamne);
                                                                bean1.setLanguage(language);
                                                                bean1.setPaid(paid);
                                                                bean1.setFileType(fileType);
                                                                bean1.setFileUrl(fileUrl);
                                                                cat1_list.add(bean1);
                                                        }
                                                        progressBar.setVisibility(View.GONE);
                                                        rv_singlecatlist.setVisibility(View.VISIBLE);
                                                        dataLoad = true;
                                                }


                                        } catch (JSONException e) {
                                                e.printStackTrace();
                                        }

                                        LinearLayoutManager linearLayoutManager5 = new LinearLayoutManager(context, RecyclerView.HORIZONTAL, false);
                                        rv_singlecatlist.setLayoutManager(new GridLayoutManager(context, 3));
                                        adapter = new SubCategoryAdapter(getApplicationContext(), cat1_list, ActivitySingleCategoyList1.this);
                                        rv_singlecatlist.setAdapter(adapter);

                                        adapter.setEventListener(new SubCategoryAdapter.EventListener() {
                                                @Override
                                                public void onItemViewClick(int position) {
                                                        if (PreferenceUtils.isActivePlan(getApplicationContext())) {
                                                                ll_dialog.setVisibility(View.GONE);
                                                        } else {
                                                                ll_dialog.setVisibility(View.VISIBLE);
                                                        }
                                                }
                                        });

                                }
                        }, new com.android.volley.Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {


                                Log.e("error", error.toString());
                        }
                }) {


                        @Override
                        public Map<String, String> getHeaders() {
                                HashMap<String, String> headers = new HashMap<String, String>();
                                headers.put("API-KEY", "8e90d71140a94bb");
                                return headers;
                        }

                };

                RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
                queue.add(request);


        }


        @Override
        public void onClick(View view) {
                switch (view.getId()) {

                        case R.id.type_all:
                                filter("");
                                typeAll.setBackground(getDrawable(R.drawable.btn_rounded_accent));
                                typeEnglish.setBackground(getDrawable(R.drawable.btn_rounded_unselect));
                                typeGujarati.setBackground(getDrawable(R.drawable.btn_rounded_unselect));
                                typeHindi.setBackground(getDrawable(R.drawable.btn_rounded_unselect));
                                break;
                        case R.id.type_english:
                                filter("english");
                                typeAll.setBackground(getDrawable(R.drawable.btn_rounded_unselect));
                                typeEnglish.setBackground(getDrawable(R.drawable.btn_rounded_accent));
                                typeGujarati.setBackground(getDrawable(R.drawable.btn_rounded_unselect));
                                typeHindi.setBackground(getDrawable(R.drawable.btn_rounded_unselect));
                                break;
                        case R.id.type_gujarati:
                                filter("Gujarati");
                                typeAll.setBackground(getDrawable(R.drawable.btn_rounded_unselect));
                                typeEnglish.setBackground(getDrawable(R.drawable.btn_rounded_unselect));
                                typeGujarati.setBackground(getDrawable(R.drawable.btn_rounded_accent));
                                typeHindi.setBackground(getDrawable(R.drawable.btn_rounded_unselect));
                                break;
                        case R.id.type_hindi:
                                filter("Hindi");
                                typeAll.setBackground(getDrawable(R.drawable.btn_rounded_unselect));
                                typeEnglish.setBackground(getDrawable(R.drawable.btn_rounded_unselect));
                                typeGujarati.setBackground(getDrawable(R.drawable.btn_rounded_unselect));
                                typeHindi.setBackground(getDrawable(R.drawable.btn_rounded_accent));
                                break;

                }
        }


        private void filter(String text) {
         if (dataLoad) {
                 ArrayList<SubCategoryModel> filterList = new ArrayList<>();

                 for (SubCategoryModel info : cat1_list) {
                         Log.e("12121", "filter: "+ cat1_list.size() );
                         if (info.getLanguage().toLowerCase().contains(text.toLowerCase())) {
                                 filterList.add(info);
                         }
                 }

                 adapter.filterList(filterList);
        }
}


}