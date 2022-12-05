package com.dexxaphotolab.app.Activities;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.dexxaphotolab.app.Adapters.AdapterCategoryList;
import com.dexxaphotolab.app.AnalyticsApplication;
import com.dexxaphotolab.app.Fragments.CreateCustomImageFragment;
import com.dexxaphotolab.app.Fragments.FragmentHome;
import com.dexxaphotolab.app.Fragments.FragmentMyPost;
import com.dexxaphotolab.app.Fragments.FragmentSetting;
import com.dexxaphotolab.app.Fragments.FragmentVideos;
import com.dexxaphotolab.app.R;
import com.dexxaphotolab.app.Utills.Admanager;
import com.dexxaphotolab.app.Utills.Constance;
import com.dexxaphotolab.app.Utills.SharedPrefrenceConfig;
import com.dexxaphotolab.app.Fragments.Tab_Subscription;
import com.dexxaphotolab.app.payment.Utils.PreferenceUtils;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

import java.util.Timer;
import java.util.TimerTask;

import cn.xm.weidongjian.popuphelper.PopupWindowHelper;

public class ActivityHome extends AppCompatActivity {

    Context context;
    DrawerLayout drawerLayout;
    ImageView iv_menuhome, iv_userimg, iv_language;
    TextView tv_titletoolbar;
    String check_fragmentname;
    SharedPrefrenceConfig sharedprefconfig;
    View popupview_down;
    String frameName[] = {"All", "English", "Hindi", "Gujarati"};

    public PopupWindowHelper popupWindowHelper;

    LinearLayout ll_bnav_home, ll_bnav_category, ll_bnav_custom,
            ll_bnav_video, ll_bnav_post;

    LinearLayout navv_home, navv_video, navv_image, navv_setting, navv_createposter, navv_subsciption;
    public static ImageView iv_home, iv_video, iv_image, iv_setting, iv_subscript, iv_createposter;
    Timer timer;
    TimerTask hourlyTask;
    public static ActivityHome instance = null;
    ImageView iv_subscription, iv_createpost;

    TextView tv_bnav_home, tv_bnav_cat, tv_bnav_custom,
            tv_bnav_video, tv_bnav_post;
    private final String TAG = ActivityHome.class.getSimpleName();

    public ActivityHome() {
        instance = ActivityHome.this;
    }

    public static synchronized ActivityHome getInstance() {
        if (instance == null) {
            instance = new ActivityHome();
        }
        return instance;
    }

    Tracker mTracker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        context = ActivityHome.this;

        SharedPreferences prefs1 = getSharedPreferences("getpremium", MODE_PRIVATE);
        String status = prefs1.getString("status", "No name defined");//"No name defined" is the default value.


        SharedPreferences prefs = getSharedPreferences("Ads_bundle", MODE_PRIVATE);
        String adManage = prefs.getString("ads_enable", "NO name defined");
        String bb_google_banner = prefs.getString("google_banner", "No name defined");//"No name defined" is the default value.
        String bb_google_native = prefs.getString("google_native", "No name defined");//"No name defined" is the default value.
        String bb_google_interstitial = prefs.getString("google_interstitial", "No name defined");//"No name defined" is the default value.
        String bb_ads_click = prefs.getString("ads_click", "No name defined");//"No name defined" is the default value.

        ActivitySplashScreen.adsEnable = adManage;
        ActivitySplashScreen.Google_banner = bb_google_banner;
        ActivitySplashScreen.google_native = bb_google_native;
        ActivitySplashScreen.Google_interstitial = bb_google_interstitial;
        ActivitySplashScreen.ads_click_counrter = bb_ads_click;


        new Admanager(getApplicationContext()).loadAd();

        SharedPreferences.Editor editor = getSharedPreferences("firsttime", MODE_PRIVATE).edit();
        editor.putBoolean("firsttime", true);
        editor.apply();

        sharedprefconfig = new SharedPrefrenceConfig(context);

        bindView();

        Log.e("12121", "onCreate: " + status );

        if (!PreferenceUtils.isActivePlan(getApplicationContext())) {

            loadFragment(new Tab_Subscription());
            iv_home.setImageResource(R.drawable.nav_home);
            iv_video.setImageResource(R.drawable.nav_video);
            iv_image.setImageResource(R.drawable.nav_image);
            iv_subscript.setImageResource(R.drawable.nav_crown1);
            iv_createposter.setImageResource(R.drawable.nav_createpost);
            iv_setting.setImageResource(R.drawable.nav_setting);

        } else {

            loadFragment(new FragmentHome());

            tv_bnav_home.setVisibility(View.VISIBLE);
            tv_bnav_cat.setVisibility(View.GONE);
            tv_bnav_custom.setVisibility(View.GONE);
            tv_bnav_video.setVisibility(View.GONE);
            tv_bnav_post.setVisibility(View.GONE);

            iv_home.setImageResource(R.drawable.nav_home1);
            iv_video.setImageResource(R.drawable.nav_video);
            iv_image.setImageResource(R.drawable.nav_image);
            iv_subscript.setImageResource(R.drawable.nav_crown);
            iv_createposter.setImageResource(R.drawable.nav_createpost);
            iv_setting.setImageResource(R.drawable.nav_setting);
        }


        navv_createposter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new Admanager(getApplicationContext()).showInterstitial(ActivityHome.this, new Admanager.GetBackPointer() {
                    @Override
                    public void returnAction() {

                        loadFragment(new CreateCustomImageFragment());
                        iv_home.setImageResource(R.drawable.nav_home);
                        iv_video.setImageResource(R.drawable.nav_video);
                        iv_image.setImageResource(R.drawable.nav_image);
                        iv_subscript.setImageResource(R.drawable.nav_crown);
                        iv_createposter.setImageResource(R.drawable.nav_createpost1);
                        iv_setting.setImageResource(R.drawable.nav_setting);
                    }
                });


            }
        });
        navv_subsciption.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new Admanager(getApplicationContext()).showInterstitial(ActivityHome.this, new Admanager.GetBackPointer() {
                    @Override
                    public void returnAction() {
                        loadFragment(new Tab_Subscription());
                        iv_home.setImageResource(R.drawable.nav_home);
                        iv_video.setImageResource(R.drawable.nav_video);
                        iv_image.setImageResource(R.drawable.nav_image);
                        iv_subscript.setImageResource(R.drawable.nav_crown1);
                        iv_createposter.setImageResource(R.drawable.nav_createpost);
                        iv_setting.setImageResource(R.drawable.nav_setting);
                    }

                });


            }
        });


        navv_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                new Admanager(getApplicationContext()).showInterstitial(ActivityHome.this, new Admanager.GetBackPointer() {
                    @Override
                    public void returnAction() {
                        loadFragment(new FragmentHome());
                        iv_home.setImageResource(R.drawable.nav_home1);
                        iv_video.setImageResource(R.drawable.nav_video);
                        iv_image.setImageResource(R.drawable.nav_image);
                        iv_subscript.setImageResource(R.drawable.nav_crown);
                        iv_createposter.setImageResource(R.drawable.nav_createpost);
                        iv_setting.setImageResource(R.drawable.nav_setting);
                    }

                });


            }
        });

        navv_setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new Admanager(getApplicationContext()).showInterstitial(ActivityHome.this, new Admanager.GetBackPointer() {
                    @Override
                    public void returnAction() {
                        loadFragment(new FragmentSetting());
                        iv_home.setImageResource(R.drawable.nav_home);
                        iv_video.setImageResource(R.drawable.nav_video);
                        iv_image.setImageResource(R.drawable.nav_image);
                        iv_subscript.setImageResource(R.drawable.nav_crown);
                        iv_createposter.setImageResource(R.drawable.nav_createpost);
                        iv_setting.setImageResource(R.drawable.nav_setting1);
                    }

                });


            }
        });

        navv_video.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new Admanager(getApplicationContext()).showInterstitial(ActivityHome.this, new Admanager.GetBackPointer() {
                    @Override
                    public void returnAction() {
                        loadFragment(new FragmentVideos());
                        iv_home.setImageResource(R.drawable.nav_home);
                        iv_video.setImageResource(R.drawable.nav_video1);
                        iv_image.setImageResource(R.drawable.nav_image);
                        iv_subscript.setImageResource(R.drawable.nav_crown);
                        iv_createposter.setImageResource(R.drawable.nav_createpost);
                        iv_setting.setImageResource(R.drawable.nav_setting);
                    }

                });


            }
        });

        navv_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new Admanager(getApplicationContext()).showInterstitial(ActivityHome.this, new Admanager.GetBackPointer() {
                    @Override
                    public void returnAction() {
                        loadFragment(new FragmentMyPost());
                        iv_home.setImageResource(R.drawable.nav_home);
                        iv_video.setImageResource(R.drawable.nav_video);
                        iv_image.setImageResource(R.drawable.nav_image1);
                        iv_subscript.setImageResource(R.drawable.nav_crown);
                        iv_createposter.setImageResource(R.drawable.nav_createpost);
                        iv_setting.setImageResource(R.drawable.nav_setting);
                    }

                });

            }
        });

        ll_bnav_post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new Admanager(getApplicationContext()).showInterstitial(ActivityHome.this, new Admanager.GetBackPointer() {
                    @Override
                    public void returnAction() {
                        loadFragment(new FragmentMyPost());
                        tv_bnav_post.setVisibility(View.VISIBLE);
                        tv_bnav_video.setVisibility(View.GONE);
                        tv_bnav_custom.setVisibility(View.GONE);
                        tv_bnav_cat.setVisibility(View.GONE);
                        tv_bnav_home.setVisibility(View.GONE);
                    }

                });

            }
        });
        ll_bnav_video.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new Admanager(getApplicationContext()).showInterstitial(ActivityHome.this, new Admanager.GetBackPointer() {
                    @Override
                    public void returnAction() {
                        loadFragment(new FragmentVideos());
                        tv_bnav_video.setVisibility(View.VISIBLE);
                        tv_bnav_post.setVisibility(View.GONE);
                        tv_bnav_custom.setVisibility(View.GONE);
                        tv_bnav_cat.setVisibility(View.GONE);
                        tv_bnav_home.setVisibility(View.GONE);
                    }

                });

            }
        });
        ll_bnav_custom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new Admanager(getApplicationContext()).showInterstitial(ActivityHome.this, new Admanager.GetBackPointer() {
                    @Override
                    public void returnAction() {
                        loadFragment(new CreateCustomImageFragment());
                        tv_bnav_custom.setVisibility(View.VISIBLE);
                        tv_bnav_post.setVisibility(View.GONE);
                        tv_bnav_video.setVisibility(View.GONE);
                        tv_bnav_cat.setVisibility(View.GONE);
                        tv_bnav_home.setVisibility(View.GONE);
                    }

                });

            }
        });

        ll_bnav_category.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new Admanager(getApplicationContext()).showInterstitial(ActivityHome.this, new Admanager.GetBackPointer() {
                    @Override
                    public void returnAction() {
//                        loadFragment(new CategoryFragment());
                        tv_bnav_cat.setVisibility(View.VISIBLE);
                        tv_bnav_post.setVisibility(View.GONE);
                        tv_bnav_home.setVisibility(View.GONE);
                        tv_bnav_video.setVisibility(View.GONE);
                        tv_bnav_custom.setVisibility(View.GONE);
                    }

                });

            }
        });

        ll_bnav_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new Admanager(getApplicationContext()).showInterstitial(ActivityHome.this, new Admanager.GetBackPointer() {
                    @Override
                    public void returnAction() {

                        loadFragment(new FragmentHome());
                        tv_bnav_home.setVisibility(View.VISIBLE);
                        tv_bnav_post.setVisibility(View.GONE);
                        tv_bnav_video.setVisibility(View.GONE);
                        tv_bnav_cat.setVisibility(View.GONE);
                        tv_bnav_custom.setVisibility(View.GONE);
                    }

                });

            }
        });

        AnalyticsApplication application = (AnalyticsApplication) getApplication();
        mTracker = application.getDefaultTracker();

        iv_language.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupview_down = LayoutInflater.from(context).inflate(R.layout.category_dropdown_raw, null);
                popupWindowHelper = new PopupWindowHelper(popupview_down);
                popupWindowHelper.showAsDropDown(v, 0, 0);
                RecyclerView rv_cat_dd_raw = popupview_down.findViewById(R.id.rv_cat_dd_raw);
                GridLayoutManager gridLayoutManager = new GridLayoutManager(getApplicationContext(), 1);
                rv_cat_dd_raw.setLayoutManager(gridLayoutManager);
                rv_cat_dd_raw.setHasFixedSize(true);

                AdapterCategoryList adapterCategory = new AdapterCategoryList(context, frameName, "home");
                rv_cat_dd_raw.setAdapter(adapterCategory);
            }
        });

        Constance.childDataList.clear();


        tv_titletoolbar.setText(R.string.app_name);
        iv_userimg.setVisibility(View.GONE);
        check_fragmentname = String.valueOf(getIntent().getStringExtra("check_fragmentname"));

        if (check_fragmentname.equals("fragment_mypost")) {
            tv_titletoolbar.setText("My Post");
            loadFragment(new FragmentMyPost());
        }

        iv_menuhome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!drawerLayout.isDrawerOpen(Gravity.START)) {
                    drawerLayout.openDrawer(Gravity.START);
                } else {
                    drawerLayout.closeDrawer(Gravity.END);
                }
            }
        });


    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i(TAG, "Setting screen name: " + "Google Analytics Testing");
        mTracker.setScreenName("Image~" + "Google Analytics Testing");
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());

        new Admanager(getApplicationContext()).loadAd();


    }

    public void bindView() {

        drawerLayout = findViewById(R.id.drawerLayout);
        iv_menuhome = findViewById(R.id.iv_menuhome);
        iv_userimg = findViewById(R.id.iv_userimg);
        tv_titletoolbar = findViewById(R.id.tv_titletoolbar);
        iv_language = findViewById(R.id.iv_language);

        ll_bnav_home = findViewById(R.id.ll_bnav_home);
        tv_bnav_home = findViewById(R.id.tv_bnav_home);
        ll_bnav_category = findViewById(R.id.ll_bnav_category);
        tv_bnav_cat = findViewById(R.id.tv_bnav_cat);
        ll_bnav_custom = findViewById(R.id.ll_bnav_custom);
        tv_bnav_custom = findViewById(R.id.tv_bnav_custom);
        ll_bnav_video = findViewById(R.id.ll_bnav_video);
        tv_bnav_video = findViewById(R.id.tv_bnav_video);
        ll_bnav_post = findViewById(R.id.ll_bnav_post);
        tv_bnav_post = findViewById(R.id.tv_bnav_post);
        navv_home = findViewById(R.id.navv_home);
        navv_video = findViewById(R.id.navv_video);
        navv_image = findViewById(R.id.navv_image);
        navv_setting = findViewById(R.id.navv_setting);
        navv_createposter = findViewById(R.id.navv_createposter);
        navv_subsciption = findViewById(R.id.navv_subsciption);
        iv_home = findViewById(R.id.iv_home);
        iv_video = findViewById(R.id.iv_video);
        iv_image = findViewById(R.id.iv_image);
        iv_setting = findViewById(R.id.iv_setting);
        iv_subscription = findViewById(R.id.iv_subscription);
        iv_createpost = findViewById(R.id.iv_createpost);
        iv_subscript = findViewById(R.id.iv_subscript);
        iv_createposter = findViewById(R.id.iv_createposter);

    }

    public void loadFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction;
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.main_framelayout, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        fragmentTransaction.commit();
    }


    public void getUserImage() {
        RequestOptions requestOptions = new RequestOptions();
        requestOptions.placeholder(R.drawable.userholder);
        requestOptions.error(R.drawable.userholder);

        Glide.with(context).load(sharedprefconfig.getUser().getImage_url()).apply(requestOptions).into(iv_userimg);

    }

    private void loadPrivacy() {
        if (Constance.isConnected(ActivityHome.this)) {
            Constance.privacyPolicy(ActivityHome.this);
        } else {
            Toast.makeText(this, "Please check your internet connection", Toast.LENGTH_SHORT).show();
        }
    }


    public void StartTimer() {
        timer = new Timer();
        hourlyTask = new TimerTask() {
            @Override
            public void run() {
                if (!Constance.isFirstTimeOpen) {
                    Constance.AllowToOpenAdvertise = true;
                    stopTask();
                } else {
                    Constance.isFirstTimeOpen = false;
                }
            }
        };

        Constance.isFirstTimeOpen = true;
        if (timer != null) {
            timer.schedule(hourlyTask, 0, 1000 * 60);
        }
    }

    public void stopTask() {
        if (hourlyTask != null) {

            Log.d("TIMER", "timer canceled");
            hourlyTask.cancel();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d("abc", "createquote");
        if (Constance.checkFragment.equals("My_Profile")) {

        } else if (Constance.checkFragment.equals("CreateCustom")) {
            CreateCustomImageFragment.getInstance().onActivityResult(requestCode, resultCode, data);
        }

    }

    @Override
    public void onBackPressed() {

        new AlertDialog.Builder(this)
                .setTitle("Close App")
                .setMessage("Are you sure you want to close this App?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finishAffinity();
                    }

                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .show();
    }

}