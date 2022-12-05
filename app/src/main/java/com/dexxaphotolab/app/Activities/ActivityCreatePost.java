package com.dexxaphotolab.app.Activities;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.dexxaphotolab.app.StickerClasses.DrawableSticker;
import com.dexxaphotolab.app.StickerClasses.Sticker;
import com.dexxaphotolab.app.StickerClasses.StickerView;
import com.dexxaphotolab.app.StickerClasses.TextSticker;
import com.downloader.Error;
import com.downloader.OnCancelListener;
import com.downloader.OnDownloadListener;
import com.downloader.OnPauseListener;
import com.downloader.OnProgressListener;
import com.downloader.OnStartOrResumeListener;
import com.downloader.PRDownloader;
import com.downloader.PRDownloaderConfig;
import com.downloader.Progress;
import com.dexxaphotolab.app.Adapters.AdapterBackgroundImage;
import com.dexxaphotolab.app.Adapters.AdapterFontList;
import com.dexxaphotolab.app.Adapters.AdapterFrames;
import com.dexxaphotolab.app.Adapters.AdapterTextColourPicker;
import com.dexxaphotolab.app.Adapters.StickersListAdapter;
import com.dexxaphotolab.app.AnalyticsApplication;
import com.dexxaphotolab.app.BuildConfig;
import com.dexxaphotolab.app.Model.ModelBackgroundImage;
import com.dexxaphotolab.app.Model.ModelColorList;
import com.dexxaphotolab.app.Model.ModelFontDetail;
import com.dexxaphotolab.app.Model.ModelFramesDetails;
import com.dexxaphotolab.app.Model.StickersModel;
import com.dexxaphotolab.app.R;
import com.dexxaphotolab.app.Utills.Constance;
import com.dexxaphotolab.app.Utills.PaletteBar;
import com.dexxaphotolab.app.Utills.SharedPrefrenceConfig;
import com.dexxaphotolab.app.Utills.Utils_VideoDownload;
import com.dexxaphotolab.app.payment.Config;
import com.dexxaphotolab.app.payment.Network.RetrofitClient;
import com.dexxaphotolab.app.payment.Network.apis.UserDataApi;
import com.dexxaphotolab.app.payment.Network.models.User;
import com.dexxaphotolab.app.payment.Utils.PreferenceUtils;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class ActivityCreatePost extends AppCompatActivity {

        private static final int MY_CAMERA_PERMISSION_CODE = 100;
        private static final int NONE = 0;
        private static final int DRAG = 1;
        private static final int ZOOM = 2;
        public static ActivityCreatePost instance = null;
        final private int REQUEST_CODE_ASK_PERMISSIONS = 123;
        public AdapterFrames adapterFrame;
        public ArrayList<ModelFontDetail> modelFontDetailArrayList;
        public ArrayList<ModelFontDetail> arrayList;
        public boolean showingFirst = true;
        public boolean showingsecond = true;
        Context context;
        ImageView iv_customimage, iv_cp_pic;
        Tracker mTracker;
        String FromSinglecatActivity = "";
        View layout_custom_frame1, layout_custom_frame2, layout_custom_frame3, layout_custom_frame4, layout_custom_frame5, layout_custom_frame6, layout_custom_frame7, layout_custom_frame8, layout_custom_frame9, layout_custom_frame10, layout_custom_frame11, layout_custom_frame12, layout_custom_frame13, layout_custom_frame14, layout_custom_frame15, layout_custom_frame16;
        TextView tv_mobilenumberlayout1, tv_mobilenumberlayout2, tv_mobilenumberlayout3, tv_mobilenumberlayout4, tv_mobilenumberlayout5, tv_mobilenumberlayout6, tv_mobilenumberlayout01, tv_mobilenumberlayout02, tv_mobilenumberlayout03, tv_mobilenumberlayout04, tv_mobilenumberlayout05, tv_mobilenumberlayout06, tv_mobilenumberlayout07, tv_mobilenumberlayout08;
        TextView tv_websitelayout1, tv_websitelayout2, tv_websitelayout3, tv_websitelayout4, tv_websitelayout5, tv_websitelayout6, tv_websitelayout7, tv_websitelayout8, tv_websitelayout01, tv_websitelayout02, tv_websitelayout03, tv_websitelayout04, tv_websitelayout06, tv_websitelayout07, tv_websitelayout08;
        TextView tv_emaillayout1, tv_emaillayout2, tv_emaillayout3, tv_emaillayout4, tv_emaillayout5, tv_emaillayout6, tv_emaillayout8, tv_emaillayout01, tv_emaillayout02, tv_emaillayout03, tv_emaillayout04, tv_emaillayout05, tv_emaillayout06, tv_emaillayout07, tv_emaillayout08;
        ImageView iv_calllayout1, iv_calllayout2, iv_calllayout3, iv_calllayout4, iv_calllayout5, iv_calllayout6, iv_calllayout01, iv_calllayout02, iv_calllayout03, iv_calllayout04, iv_calllayout05, iv_calllayout06, iv_calllayout07, iv_calllayout08;
        ImageView iv_weblayout1, iv_weblayout2, iv_weblayout3, iv_weblayout4, iv_weblayout5, iv_weblayout6, iv_weblayout7, iv_weblayout8, iv_weblayout01, iv_weblayout02, iv_weblayout03, iv_weblayout04, iv_weblayout06, iv_weblayout07, iv_weblayout08;
        ImageView iv_emaillayout1, iv_emaillayout2, iv_emaillayout3, iv_emaillayout4, iv_emaillayout5, iv_emaillayout6, iv_emaillayout8, iv_emaillayout01, iv_emaillayout02, iv_emaillayout03, iv_emaillayout04, iv_emaillayout05, iv_emaillayout06, iv_emaillayout07, iv_emaillayout08;
        ImageView iv_logolayout1, iv_logolayout2, iv_logolayout3, iv_logolayout4, iv_logolayout5, iv_logolayout6, iv_logolayout7, iv_logolayout8, iv_logolayout01, iv_logolayout02, iv_logolayout03, iv_logolayout04, iv_logolayout05, iv_logolayout06, iv_logolayout07, iv_logolayout08;
        RecyclerView rv_framelist;
        TextView tvLocation1, tvLocation2, tvLocation3;
        ImageView ivLocation1, ivLocation2, ivLocation3;
        Bitmap bitmapsave;
        String[] fo = new String[]
                {"abhayalibre_bold.ttf", "abhayalibre_extrabold.ttf", "abhayalibre_medium.ttf", "artifika_regular.ttf", "archivo_black.ttf",
                        "ArchivoNarrow.otf", "ABeeZee.otf", "After_Shok.ttf", "AbrilFatface.otf", "Acknowledgement.otf",
                        "Acme.ttf", "AlfaSlabOne.ttf", "AlmendraDisplay.otf", "Almendra.otf", "alpha_echo.ttf",
                        "Amadeus.ttf", "AMERSN.ttf", "ANUDI.ttf", "AquilineTwo.ttf", "Arbutus.ttf", "AlexBrush.ttf",
                        "Alisandra.ttf", "Allura.ttf", "Amarillo.ttf", "BEARPAW.ttf", "bigelowrules.ttf", "BLACKR.ttf",
                        "BOYCOTT.ttf", "BebasNeue.ttf", "BLKCHCRY.TTF", "Carousel.ttf", "Caslon_Calligraphic.ttf",
                        "CroissantOne.ttf", "Carnevalee-Freakshow.ttf", "CAROBTN.TTF", "CaviarDreams.ttf",
                        "Cocogoose.ttf", "diplomata.ttf", "deftone stylus.ttf", "Dosis.ttf", "FONTL.TTF",
                        "Hugtophia.ttf", "ICE_AGE.ttf", "Kingthings_Calligraphica.ttf", "Love Like This.ttf",
                        "MADE Canvas.otf", "Merci-Heart-Brush.ttf", "Metropolis.otf", "Montserrat.otf",
                        "MontserratAlternates.otf",
                        "norwester.otf", "ostrich.ttf", "squealer.ttf", "Titillium.otf", "Ubuntu.ttf"};
        StickerView sticker_view;
        TextSticker txtsticker;
        LinearLayout ll_border, ll_camera_pic, ll_addimage, ll_flip, ll_main, ll_sticker, ll_text_sticker, ll_set_boder;
        EditText et_text_sticker;
        RelativeLayout rl_content, rl_createquote;
        int seekvalue;
        Float dx, dy;
        RelativeLayout opacitybg, rl_video;
        VideoView vv_videoshow;
        SharedPrefrenceConfig sharedPrefrenceConfig;
        String comeFrom;
        ProgressDialog pDialog;
        StickersModel[] stickerlist;
        Uri photouri;
        String mCurrentPhotoPath;
        String abc;
        float[] lastEvent = null;
        int count = 0, textcolor;
        int textStickerColor = R.color.colorBlack;
        PopupWindow mPopupWindow, mPopupWindowpw;
        ProgressDialog pDialog1;
        ProgressBar pb_sp;
        MediaController mediacontroller;
        int PERMISSION_ALL = 1;
        String[] PERMISSIONS = {
                Manifest.permission.READ_CONTACTS,
                Manifest.permission.WRITE_CONTACTS,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_SMS,
                Manifest.permission.CAMERA
        };
        TextView tv_remove_logo, tv_remove_email, tv_remove_call, tv_remove_web, tv_remove_address;
        LinearLayout ll_f1_call, ll_f2_call, ll_f3_call, ll_f4_call, ll_f5_call, ll_f6_call, ll_f01_call, ll_f02_call, ll_f03_call, ll_f04_call, ll_f05_call, ll_f06_call, ll_f07_call, ll_f08_call;
        LinearLayout ll_f1_email, ll_f2_email, ll_f3_email, ll_f4_email, ll_f5_email, ll_f6_email, ll_f8_email, ll_f01_email, ll_f02_email, ll_f03_email, ll_f04_email, ll_f05_email, ll_f06_email, ll_f07_email, ll_f08_email;
        LinearLayout ll_f1_web, ll_f2_web, ll_f3_web, ll_f4_web, ll_f5_web, ll_f6_web, ll_f7_web, ll_f8_web, ll_f01_web, ll_f02_web, ll_f03_web, ll_f04_web, ll_f06_web, ll_f07_web, ll_f08_web;
        LinearLayout llLocation1, llLocation2, llLocation3;
        Boolean logo = true, call = true, email = true, web = true, address = true;
        File imagesDir;
        String imgName, videoName;
        private boolean isZoomAndRotate;
        private boolean isOutSide;
        private PointF start = new PointF();
        private PointF mid = new PointF();
        private float xCoOrdinate, yCoOrdinate;
        private int mode = NONE;
        private float oldDist = 1f;
        private float d = 0f;
        private float newRot = 0f;
        private String id;
        public ActivityCreatePost() {
                instance = ActivityCreatePost.this;
        }

        public static synchronized ActivityCreatePost getInstance() {
                if (instance == null) {
                        instance = new ActivityCreatePost();
                }
                return instance;
        }

        public static boolean hasPermissions(Context context, String... permissions) {
                if (context != null && permissions != null) {
                        for (String permission : permissions) {
                                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                                        return false;
                                }
                        }
                }
                return true;
        }

        public static ArrayList<ModelColorList> getColorList() {
                ArrayList<ModelColorList> data = new ArrayList<>();
                data.add(new ModelColorList(R.color.color1));
                data.add(new ModelColorList(R.color.colorWhite));
                data.add(new ModelColorList(R.color.color6));
                data.add(new ModelColorList(R.color.color7));
                data.add(new ModelColorList(R.color.color3));
                data.add(new ModelColorList(R.color.color12));
                data.add(new ModelColorList(R.color.colorBlack));
                data.add(new ModelColorList(R.color.color2));
                data.add(new ModelColorList(R.color.color4));
                data.add(new ModelColorList(R.color.color5));
                data.add(new ModelColorList(R.color.color9));
                data.add(new ModelColorList(R.color.color8));
                data.add(new ModelColorList(R.color.color10));
                data.add(new ModelColorList(R.color.color11));


                return data;
        }

        public static ArrayList<ModelBackgroundImage> getLocalImageList() {
                ArrayList<ModelBackgroundImage> data = new ArrayList<>();
                data.add(new ModelBackgroundImage(R.drawable.img_1));
                data.add(new ModelBackgroundImage(R.drawable.img_2));
                data.add(new ModelBackgroundImage(R.drawable.img_3));
                data.add(new ModelBackgroundImage(R.drawable.pager_1));
                data.add(new ModelBackgroundImage(R.drawable.pager_2));

                return data;
        }

        public static ArrayList<ModelFramesDetails> getFramesList() {
                ArrayList<ModelFramesDetails> data = new ArrayList<>();
                data.add(new ModelFramesDetails(R.drawable.f1));
                data.add(new ModelFramesDetails(R.drawable.f2));
                data.add(new ModelFramesDetails(R.drawable.f3));
                data.add(new ModelFramesDetails(R.drawable.f4));
                data.add(new ModelFramesDetails(R.drawable.f5));
                data.add(new ModelFramesDetails(R.drawable.f6));
                data.add(new ModelFramesDetails(R.drawable.f7));
                data.add(new ModelFramesDetails(R.drawable.f8));
                data.add(new ModelFramesDetails(R.drawable.f_9));
                data.add(new ModelFramesDetails(R.drawable.f11));
                data.add(new ModelFramesDetails(R.drawable.f12));
                data.add(new ModelFramesDetails(R.drawable.f13));
                data.add(new ModelFramesDetails(R.drawable.f14));
                data.add(new ModelFramesDetails(R.drawable.f15));
                data.add(new ModelFramesDetails(R.drawable.f16));
                data.add(new ModelFramesDetails(R.drawable.f_10));

                return data;
        }

        public static ArrayList<ModelFontDetail> getfontList() {
                ArrayList<ModelFontDetail> data = new ArrayList<>();
                String[] fontnamelist = new String[]
                        {"abhayalibre_bold.ttf", "abhayalibre_extrabold.ttf", "abhayalibre_medium.ttf", "artifika_regular.ttf", "archivo_black.ttf",
                                "ArchivoNarrow.otf", "ABeeZee.otf", "After_Shok.ttf", "AbrilFatface.otf", "Acknowledgement.otf",
                                "Acme.ttf", "AlfaSlabOne.ttf", "AlmendraDisplay.otf", "Almendra.otf", "alpha_echo.ttf",
                                "Amadeus.ttf", "AMERSN.ttf", "ANUDI.ttf", "AquilineTwo.ttf", "Arbutus.ttf", "AlexBrush.ttf",
                                "Alisandra.ttf", "Allura.ttf", "Amarillo.ttf", "BEARPAW.ttf", "bigelowrules.ttf", "BLACKR.ttf",
                                "BOYCOTT.ttf", "BebasNeue.ttf", "BLKCHCRY.TTF", "Carousel.ttf", "Caslon_Calligraphic.ttf",
                                "CroissantOne.ttf", "Carnevalee-Freakshow.ttf", "CAROBTN.TTF", "CaviarDreams.ttf",
                                "Cocogoose.ttf", "diplomata.ttf", "deftone stylus.ttf", "Dosis.ttf", "FONTL.TTF",
                                "Hugtophia.ttf", "ICE_AGE.ttf", "Kingthings_Calligraphica.ttf", "Love Like This.ttf",
                                "MADE Canvas.otf", "Merci-Heart-Brush.ttf", "Metropolis.otf", "Montserrat.otf",
                                "MontserratAlternates.otf",
                                "norwester.otf", "ostrich.ttf", "squealer.ttf", "Titillium.otf", "Ubuntu.ttf"};


                for (int i = 0; i < fontnamelist.length; i++) {

                        data.add(new ModelFontDetail(fontnamelist[i], fontnamelist[i]));
                }
                return data;

        }

        @Override
        protected void onCreate(Bundle savedInstanceState) {
                super.onCreate(savedInstanceState);
                setContentView(R.layout.activity_create_post);
                context = ActivityCreatePost.this;

                id = PreferenceUtils.getUserId(ActivityCreatePost.this);
                getProfile();

                bindView();
                AnalyticsApplication application = (AnalyticsApplication) getApplication();
                mTracker = application.getDefaultTracker();
                if (!hasPermissions(this, PERMISSIONS)) {
                        ActivityCompat.requestPermissions(this, PERMISSIONS, PERMISSION_ALL);
                }

                Intent i = getIntent();
                imgName = i.getStringExtra("name");
                videoName = i.getStringExtra("videoName");

                sharedPrefrenceConfig = new SharedPrefrenceConfig(context);
                calculationForHeight();
//        getbusinessdetail();


                stickerlist = new StickersModel[]{
                        new StickersModel(R.drawable.pf_s1), new StickersModel(R.drawable.pf_s2), new StickersModel(R.drawable.pf_s3),
                        new StickersModel(R.drawable.pf_s4),
                        new StickersModel(R.drawable.pf_s10), new StickersModel(R.drawable.pf_s11),
                        new StickersModel(R.drawable.pf_s12), new StickersModel(R.drawable.pf_s13), new StickersModel(R.drawable.pf_s14), new StickersModel(R.drawable.pf_s15),
                        new StickersModel(R.drawable.pf_s16), new StickersModel(R.drawable.pf_s17), new StickersModel(R.drawable.pf_s18), new StickersModel(R.drawable.pf_s19),
                        new StickersModel(R.drawable.pf_s20), new StickersModel(R.drawable.pf_s21), new StickersModel(R.drawable.pf_s22), new StickersModel(R.drawable.pf_s23),
                        new StickersModel(R.drawable.pf_s24), new StickersModel(R.drawable.pf_s25), new StickersModel(R.drawable.pf_s26), new StickersModel(R.drawable.pf_s27),
                        new StickersModel(R.drawable.pf_s28), new StickersModel(R.drawable.pf_s29), new StickersModel(R.drawable.pf_s30), new StickersModel(R.drawable.pf_s31),
                        new StickersModel(R.drawable.pf_s32), new StickersModel(R.drawable.pf_s33), new StickersModel(R.drawable.pf_s34), new StickersModel(R.drawable.pf_s35),
                        new StickersModel(R.drawable.pf_s36), new StickersModel(R.drawable.pf_s37), new StickersModel(R.drawable.pf_s38), new StickersModel(R.drawable.pf_s39),
                        new StickersModel(R.drawable.pf_s40), new StickersModel(R.drawable.pf_s41), new StickersModel(R.drawable.pf_s42), new StickersModel(R.drawable.pf_s43),
                        new StickersModel(R.drawable.pf_s44), new StickersModel(R.drawable.pf_s45), new StickersModel(R.drawable.pf_s46), new StickersModel(R.drawable.pf_s47),
                        new StickersModel(R.drawable.pf_s48), new StickersModel(R.drawable.pf_s49), new StickersModel(R.drawable.pf_s50), new StickersModel(R.drawable.pf_s51),
                        new StickersModel(R.drawable.pf_s52), new StickersModel(R.drawable.pf_s53), new StickersModel(R.drawable.pf_s54),

                };

                tv_remove_email.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                                if (email) {
                                        tv_remove_email.setTextColor(getResources().getColor(R.color.colorTheame));
                                        tv_remove_email.setBackgroundDrawable(getResources().getDrawable(R.drawable.roundcorner_view_greyclr));
                                        email = false;
                                        removeEmail(email);
                                } else {
                                        tv_remove_email.setTextColor(getResources().getColor(R.color.colorWhite));
                                        tv_remove_email.setBackgroundDrawable(getResources().getDrawable(R.drawable.view_remove_bg));
                                        email = true;
                                        removeEmail(email);
                                }

                        }
                });

                tv_remove_logo.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                                if (logo) {
                                        tv_remove_logo.setTextColor(getResources().getColor(R.color.colorTheame));
                                        tv_remove_logo.setBackgroundDrawable(getResources().getDrawable(R.drawable.roundcorner_view_greyclr));
                                        logo = false;
                                        removeLogo(logo);
                                } else {
                                        tv_remove_logo.setTextColor(getResources().getColor(R.color.colorWhite));
                                        tv_remove_logo.setBackgroundDrawable(getResources().getDrawable(R.drawable.view_remove_bg));
                                        logo = true;
                                        removeLogo(logo);
                                }

                        }
                });

                tv_remove_call.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                                if (call) {
                                        tv_remove_call.setTextColor(getResources().getColor(R.color.colorTheame));
                                        tv_remove_call.setBackgroundDrawable(getResources().getDrawable(R.drawable.roundcorner_view_greyclr));
                                        call = false;
                                        removeCall(call);
                                } else {
                                        tv_remove_call.setTextColor(getResources().getColor(R.color.colorWhite));
                                        tv_remove_call.setBackgroundDrawable(getResources().getDrawable(R.drawable.view_remove_bg));
                                        call = true;
                                        removeCall(call);
                                }
                        }
                });

                tv_remove_web.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                                if (web) {
                                        tv_remove_web.setTextColor(getResources().getColor(R.color.colorTheame));
                                        tv_remove_web.setBackgroundDrawable(getResources().getDrawable(R.drawable.roundcorner_view_greyclr));
                                        web = false;
                                        removeWeb(web);
                                } else {
                                        tv_remove_web.setTextColor(getResources().getColor(R.color.colorWhite));
                                        tv_remove_web.setBackgroundDrawable(getResources().getDrawable(R.drawable.view_remove_bg));
                                        web = true;
                                        removeWeb(web);
                                }

                        }
                });

                tv_remove_address.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                                if (address) {
                                        tv_remove_address.setTextColor(getResources().getColor(R.color.colorTheame));
                                        tv_remove_address.setBackgroundDrawable(getResources().getDrawable(R.drawable.roundcorner_view_greyclr));
                                        address = false;
                                        removeAddr(address);
                                } else {
                                        tv_remove_address.setTextColor(getResources().getColor(R.color.colorWhite));
                                        tv_remove_address.setBackgroundDrawable(getResources().getDrawable(R.drawable.view_remove_bg));
                                        address = true;
                                        removeAddr(address);
                                }

                        }
                });

                sticker_view.setOnStickerOperationListener(new StickerView.OnStickerOperationListener() {
                        @Override
                        public void onStickerAdded(@NonNull Sticker PFSticker) {

                                sticker_view.hideIcons(false);
                                sticker_view.setLocked(false);
                        }

                        @Override
                        public void onStickerClicked(@NonNull Sticker PFSticker1) {
                                sticker_view.hideIcons(false);
                                sticker_view.setLocked(false);
                        }

                        @Override
                        public void onStickerDeleted(@NonNull Sticker PFSticker) {

                        }

                        @Override
                        public void onStickerDragFinished(@NonNull Sticker PFSticker) {

                        }

                        @Override
                        public void onStickerTouchedDown(@NonNull Sticker PFSticker) {

                        }

                        @Override
                        public void onStickerZoomFinished(@NonNull Sticker PFSticker) {

                        }

                        @Override
                        public void onStickerFlipped(@NonNull Sticker PFSticker) {

                        }

                        @Override
                        public void onStickerDoubleTapped(@NonNull Sticker PFSticker) {
                                Log.d("dfhsjhfhdshfd", "dfsfsdfko");
                                sticker_view.hideIcons(false);
                                sticker_view.setLocked(false);
                                et_text_sticker.setVisibility(View.VISIBLE);
                                PFSticker = (TextSticker) sticker_view.getCurrentSticker();
                                et_text_sticker.setText(((TextSticker) PFSticker).getText());
                                et_text_sticker.setTextColor(((TextSticker) PFSticker).getTextColor());
                                sticker_view.remove(PFSticker);
                        }
                });

                Constance.isStickerAvail = false;
                if (!Constance.isStickerAvail) {
                        Constance.isStickerTouch = false;
                        sticker_view.setLocked(true);
                }
                touchListener(rl_content);

                modelFontDetailArrayList = new ArrayList<>();
                pb_sp.setVisibility(View.GONE);
                LinearLayoutManager layoutManager
                        = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
                rv_framelist.setLayoutManager(layoutManager);

                adapterFrame = new AdapterFrames(context, getFramesList());
                adapterFrame.notifyDataSetChanged();
                rv_framelist.setAdapter(adapterFrame);

                if (Constance.activityName.equals("greeting")) {

                        iv_cp_pic.setVisibility(View.VISIBLE);
                        ll_border.setVisibility(View.VISIBLE);
                        ll_camera_pic.setVisibility(View.VISIBLE);
                        ll_addimage.setVisibility(View.VISIBLE);
                        ll_flip.setVisibility(View.VISIBLE);
                        ll_sticker.setVisibility(View.VISIBLE);
                        ll_text_sticker.setVisibility(View.VISIBLE);
                        ll_set_boder.setVisibility(View.VISIBLE);
                }
                ll_set_boder.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                                setBorder();
                        }
                });
                ll_flip.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                                LayoutInflater inflater = (LayoutInflater) context.getSystemService(LAYOUT_INFLATER_SERVICE);
                                View customView = inflater.inflate(R.layout.fliprotation_popup, null);

                                mPopupWindow = new PopupWindow(
                                        customView,
                                        ViewGroup.LayoutParams.MATCH_PARENT,
                                        ViewGroup.LayoutParams.WRAP_CONTENT
                                );
                                if (Build.VERSION.SDK_INT >= 21) {
                                        mPopupWindow.setElevation(5.0f);
                                }
                                TextView tvHorizonal = customView.findViewById(R.id.tvHorizontal);
                                TextView tvVertical = customView.findViewById(R.id.tvVertical);

                                tvHorizonal.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {

                                                if (showingFirst) {
                                                        iv_cp_pic.setRotationY(180f);
                                                        showingFirst = false;
                                                } else {
                                                        iv_cp_pic.setRotationY(0f);
                                                        showingFirst = true;
                                                }

                                                mPopupWindow.dismiss();
                                        }

                                });

                                tvVertical.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {


                                                if (showingsecond) {
                                                        iv_cp_pic.setRotationX(180f);
                                                        showingsecond = false;
                                                } else {
                                                        iv_cp_pic.setRotationX(0f);
                                                        showingsecond = true;
                                                }
                                                mPopupWindow.dismiss();


                                        }
                                });
                                mPopupWindow.setOutsideTouchable(true);

                                mPopupWindow.showAtLocation(ll_main, Gravity.BOTTOM, 0, 0);
                        }
                });
                ll_camera_pic.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                                        File photoFile = null;
                                        File photoThumbnailFile = null;
                                        try {
                                                photoFile = createImageFile();
                                        } catch (IOException ex) {

                                        }
                                        Log.e("ttttt1", "onClick: " + photoFile);
                                        if (photoFile != null) {
                                                photouri = FileProvider.getUriForFile(context,
                                                        BuildConfig.APPLICATION_ID+".provider",
                                                        photoFile);


                                                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photouri);
                                                startActivityForResult(takePictureIntent, 2);

                                        }
                                }
                        }
                });

                et_text_sticker.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                        public boolean onEditorAction(final TextView v, int actionId, KeyEvent event) {
                                if ((event != null && (event.getKeyCode() == android.view.KeyEvent.KEYCODE_ENTER)) || (actionId == EditorInfo.IME_ACTION_DONE)) {
                                        AlertDialog.Builder builder = new AlertDialog.Builder(context);
                                        builder.setMessage("Are you wants to Edit Sticker");
                                        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                        textStickerEditPopUp();
                                                }
                                        });

                                        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {


                                                        if (et_text_sticker.getText().toString().equals("") || et_text_sticker.getText().toString().equals(null)) {

                                                        } else {
                                                                et_text_sticker.setVisibility(View.GONE);
                                                                txtsticker = new TextSticker(context);

                                                                et_text_sticker.post(new Runnable() {
                                                                        @Override
                                                                        public void run() {
                                                                                InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                                                                                imm.hideSoftInputFromWindow(et_text_sticker.getWindowToken(), 0);
                                                                        }
                                                                });


                                                                txtsticker.setText("");
                                                                txtsticker.getText();
                                                                txtsticker.setText(et_text_sticker.getText().toString());
                                                                txtsticker.setTypeface(Typeface.createFromAsset(context.getAssets(), "fonts/" + Constance.FontStyle));
                                                                txtsticker.setTextColor(getResources().getColor(textStickerColor));
                                                                textStickerColor = R.color.colorBlack;
                                                                txtsticker.resizeText();
                                                                sticker_view.addSticker(txtsticker);

                                                        }

                                                }
                                        });
                                        AlertDialog alert = builder.create();
                                        alert.show();
                                        Button pbutton = alert.getButton(DialogInterface.BUTTON_POSITIVE);
                                        pbutton.setTextColor(Color.BLACK);
                                        pbutton.setBackgroundColor(Color.WHITE);

                                        Button nbutton = alert.getButton(DialogInterface.BUTTON_NEGATIVE);
                                        nbutton.setTextColor(Color.BLACK);
                                        nbutton.setBackgroundColor(Color.WHITE);

                                }
                                return false;
                        }
                });

                ll_sticker.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                                openStickers();
                        }
                });

                ll_text_sticker.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                                et_text_sticker.setVisibility(View.VISIBLE);
                                et_text_sticker.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                                        @Override
                                        public void onFocusChange(View v, boolean hasFocus) {
                                                et_text_sticker.post(new Runnable() {
                                                        @Override
                                                        public void run() {
                                                                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                                                                imm.showSoftInput(et_text_sticker, InputMethodManager.SHOW_IMPLICIT);
                                                        }
                                                });
                                        }
                                });
                                et_text_sticker.requestFocus();
                                et_text_sticker.getText().clear();
                                et_text_sticker.setTextColor(getResources().getColor(R.color.hint));
                                et_text_sticker.setTypeface(Typeface.createFromAsset(context.getAssets(), "fonts/" + "Acme.ttf"));
                                et_text_sticker.setHint("your text for sticker");

                        }
                });
                iv_cp_pic.setOnTouchListener(new View.OnTouchListener() {

                        RelativeLayout.LayoutParams parms;
                        int startwidth;
                        int startheight;
                        float dx = 0, dy = 0, x = 0, y = 0;
                        float angle = 0;

                        @Override
                        public boolean onTouch(View v, MotionEvent event) {
                                final ImageView view = (ImageView) v;

                                viewTransformation(view, event);

                                return true;

                        }
                });


        /*iv_vp_play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pDialog.dismiss();
                Log.d("wseqwqwqwqwqwqwqw", "sdss");
                vv_videoshow.start();
                iv_vp_play.setVisibility(View.GONE);
            }
        });*/


                PRDownloader.initialize(getApplicationContext());
                PRDownloaderConfig config = PRDownloaderConfig.newBuilder()
                        .setDatabaseEnabled(true)
                        .build();
                PRDownloader.initialize(getApplicationContext(), config);
        }

        public void removeLogo(Boolean logoLay) {

                if (logoLay) {
                        iv_logolayout1.setVisibility(View.VISIBLE);
                        iv_logolayout2.setVisibility(View.VISIBLE);
                        iv_logolayout3.setVisibility(View.VISIBLE);
                        iv_logolayout4.setVisibility(View.VISIBLE);
                        iv_logolayout5.setVisibility(View.VISIBLE);
                        iv_logolayout6.setVisibility(View.VISIBLE);
                        iv_logolayout7.setVisibility(View.VISIBLE);
                        iv_logolayout8.setVisibility(View.VISIBLE);
                        iv_logolayout01.setVisibility(View.VISIBLE);
                        iv_logolayout02.setVisibility(View.VISIBLE);
                        iv_logolayout03.setVisibility(View.VISIBLE);
                        iv_logolayout04.setVisibility(View.VISIBLE);
                        iv_logolayout05.setVisibility(View.VISIBLE);
                        iv_logolayout06.setVisibility(View.VISIBLE);
                        iv_logolayout07.setVisibility(View.VISIBLE);
                        iv_logolayout08.setVisibility(View.VISIBLE);
                } else {
                        iv_logolayout1.setVisibility(View.GONE);
                        iv_logolayout2.setVisibility(View.GONE);
                        iv_logolayout3.setVisibility(View.GONE);
                        iv_logolayout4.setVisibility(View.GONE);
                        iv_logolayout5.setVisibility(View.GONE);
                        iv_logolayout6.setVisibility(View.GONE);
                        iv_logolayout7.setVisibility(View.GONE);
                        iv_logolayout8.setVisibility(View.GONE);
                        iv_logolayout01.setVisibility(View.GONE);
                        iv_logolayout02.setVisibility(View.GONE);
                        iv_logolayout03.setVisibility(View.GONE);
                        iv_logolayout04.setVisibility(View.GONE);
                        iv_logolayout05.setVisibility(View.GONE);
                        iv_logolayout06.setVisibility(View.GONE);
                        iv_logolayout07.setVisibility(View.GONE);
                        iv_logolayout08.setVisibility(View.GONE);
                }
        }

        public void removeEmail(Boolean emailLay) {

                if (emailLay) {
                        ll_f1_email.setVisibility(View.VISIBLE);
                        ll_f2_email.setVisibility(View.VISIBLE);
                        ll_f3_email.setVisibility(View.VISIBLE);
                        ll_f4_email.setVisibility(View.VISIBLE);
                        ll_f5_email.setVisibility(View.VISIBLE);
                        ll_f6_email.setVisibility(View.VISIBLE);
                        ll_f8_email.setVisibility(View.VISIBLE);
                        ll_f01_email.setVisibility(View.VISIBLE);
                        ll_f02_email.setVisibility(View.VISIBLE);
                        ll_f03_email.setVisibility(View.VISIBLE);
                        ll_f04_email.setVisibility(View.VISIBLE);
                        ll_f05_email.setVisibility(View.VISIBLE);
                        ll_f06_email.setVisibility(View.VISIBLE);
                        ll_f07_email.setVisibility(View.VISIBLE);
                        ll_f08_email.setVisibility(View.VISIBLE);
                } else {
                        ll_f1_email.setVisibility(View.GONE);
                        ll_f2_email.setVisibility(View.GONE);
                        ll_f3_email.setVisibility(View.GONE);
                        ll_f6_email.setVisibility(View.GONE);
                        ll_f8_email.setVisibility(View.GONE);
                        ll_f4_email.setVisibility(View.INVISIBLE);
                        ll_f5_email.setVisibility(View.INVISIBLE);
                        ll_f01_email.setVisibility(View.GONE);
                        ll_f02_email.setVisibility(View.INVISIBLE);
                        ll_f03_email.setVisibility(View.INVISIBLE);
                        ll_f04_email.setVisibility(View.INVISIBLE);
                        ll_f05_email.setVisibility(View.INVISIBLE);
                        ll_f06_email.setVisibility(View.INVISIBLE);
                        ll_f07_email.setVisibility(View.INVISIBLE);
                        ll_f08_email.setVisibility(View.INVISIBLE);
                }
        }

        public void removeCall(Boolean callLay) {

                if (callLay) {
                        ll_f1_call.setVisibility(View.VISIBLE);
                        ll_f2_call.setVisibility(View.VISIBLE);
                        ll_f3_call.setVisibility(View.VISIBLE);
                        ll_f4_call.setVisibility(View.VISIBLE);
                        ll_f5_call.setVisibility(View.VISIBLE);
                        ll_f6_call.setVisibility(View.VISIBLE);
                        ll_f01_call.setVisibility(View.VISIBLE);
                        ll_f02_call.setVisibility(View.VISIBLE);
                        ll_f03_call.setVisibility(View.VISIBLE);
                        ll_f04_call.setVisibility(View.VISIBLE);
                        ll_f05_call.setVisibility(View.VISIBLE);
                        ll_f06_call.setVisibility(View.VISIBLE);
                        ll_f07_call.setVisibility(View.VISIBLE);
                        ll_f08_call.setVisibility(View.VISIBLE);
                } else {
                        ll_f1_call.setVisibility(View.GONE);
                        ll_f2_call.setVisibility(View.GONE);
                        ll_f3_call.setVisibility(View.GONE);
                        ll_f6_call.setVisibility(View.GONE);
                        ll_f4_call.setVisibility(View.INVISIBLE);
                        ll_f5_call.setVisibility(View.INVISIBLE);
                        ll_f01_call.setVisibility(View.INVISIBLE);
                        ll_f02_call.setVisibility(View.INVISIBLE);
                        ll_f03_call.setVisibility(View.INVISIBLE);
                        ll_f04_call.setVisibility(View.INVISIBLE);
                        ll_f05_call.setVisibility(View.INVISIBLE);
                        ll_f06_call.setVisibility(View.INVISIBLE);
                        ll_f07_call.setVisibility(View.INVISIBLE);
                        ll_f08_call.setVisibility(View.INVISIBLE);
                }
        }

        public void removeWeb(Boolean webLay) {

                if (webLay) {
                        ll_f1_web.setVisibility(View.VISIBLE);
                        ll_f2_web.setVisibility(View.VISIBLE);
                        ll_f3_web.setVisibility(View.VISIBLE);
                        ll_f4_web.setVisibility(View.VISIBLE);
                        ll_f5_web.setVisibility(View.VISIBLE);
                        ll_f6_web.setVisibility(View.VISIBLE);
                        ll_f7_web.setVisibility(View.VISIBLE);
                        ll_f8_web.setVisibility(View.VISIBLE);
                        ll_f01_web.setVisibility(View.VISIBLE);
                        ll_f02_web.setVisibility(View.VISIBLE);
                        ll_f03_web.setVisibility(View.VISIBLE);
                        ll_f04_web.setVisibility(View.VISIBLE);
                        ll_f06_web.setVisibility(View.VISIBLE);
                        ll_f07_web.setVisibility(View.VISIBLE);
                        ll_f08_web.setVisibility(View.VISIBLE);
                } else {
                        ll_f1_web.setVisibility(View.GONE);
                        ll_f2_web.setVisibility(View.GONE);
                        ll_f3_web.setVisibility(View.GONE);
                        ll_f4_web.setVisibility(View.GONE);
                        ll_f6_web.setVisibility(View.GONE);
                        ll_f7_web.setVisibility(View.GONE);
                        ll_f5_web.setVisibility(View.INVISIBLE);
                        ll_f8_web.setVisibility(View.GONE);
                        ll_f01_web.setVisibility(View.GONE);
                        ll_f02_web.setVisibility(View.GONE);
                        ll_f03_web.setVisibility(View.GONE);
                        ll_f04_web.setVisibility(View.GONE);
                        ll_f06_web.setVisibility(View.GONE);
                        ll_f07_web.setVisibility(View.GONE);
                        ll_f08_web.setVisibility(View.GONE);
                }
        }

        public void removeAddr(Boolean addr) {
                if(addr) {
                    llLocation1.setVisibility(View.VISIBLE);
                    llLocation2.setVisibility(View.VISIBLE);
                    llLocation3.setVisibility(View.VISIBLE);

                } else {
                        llLocation1.setVisibility(View.GONE);
                        llLocation2.setVisibility(View.INVISIBLE);
                        llLocation3.setVisibility(View.INVISIBLE);
                }
        }

        public void textStickerEditPopUp() {


                LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);

                final View customView = inflater.inflate(R.layout.edit_text_sticker_popup, null);


                mPopupWindowpw = new PopupWindow(
                        customView,
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT
                );

                if (Build.VERSION.SDK_INT >= 21) {
                        mPopupWindowpw.setElevation(5.0f);
                }
                mPopupWindowpw.setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                mPopupWindowpw.setOutsideTouchable(true);
                mPopupWindowpw.showAtLocation(ll_main, Gravity.BOTTOM, 0, 0);


                final TextView dialogTitle = (TextView) customView.findViewById(R.id.cp_accent_title);
                final LinearLayout ll_text_color = (LinearLayout) customView.findViewById(R.id.ll_text_color);
                final LinearLayout ll_text_style = (LinearLayout) customView.findViewById(R.id.ll_text_style);
                final Button btn_Ok = (Button) customView.findViewById(R.id.btn_Ok);


                ll_text_color.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                                textStickerColorPopUp();

                        }
                });


                ll_text_style.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                                openFontList();
                        }
                });
                dialogTitle.setText("Edit PF_Sticker");


                btn_Ok.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {


                                if (et_text_sticker.getText().toString().equals("") || et_text_sticker.getText().toString().equals(null)) {
                                        Toast.makeText(context, "Please enter pf_text", Toast.LENGTH_LONG).show();
                                        mPopupWindowpw.dismiss();


                                } else {
                                        et_text_sticker.setVisibility(View.GONE);
                                        txtsticker = new TextSticker(context);
                                        txtsticker.setText("");
                                        et_text_sticker.post(new Runnable() {
                                                @Override
                                                public void run() {
                                                        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                                                        imm.hideSoftInputFromWindow(et_text_sticker.getWindowToken(), 0);

                                                }
                                        });
                                        txtsticker.setText(et_text_sticker.getText().toString());
                                        txtsticker.setTypeface(Typeface.createFromAsset(context.getAssets(), "fonts/" + Constance.FontStyle));
                                        txtsticker.setTextColor(textcolor);
                                        textStickerColor = R.color.colorBlack;
                                        txtsticker.resizeText();
                                        sticker_view.addSticker(txtsticker);
                                        mPopupWindowpw.dismiss();

                                }
                        }

                });

        }

        public void openFontList() {

                LayoutInflater inflater = (LayoutInflater) context.getSystemService(LAYOUT_INFLATER_SERVICE);

                View customView = inflater.inflate(R.layout.dialog_fontstyle, null);


                mPopupWindow = new PopupWindow(
                        customView,
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT
                );


                if (Build.VERSION.SDK_INT >= 21) {
                        mPopupWindow.setElevation(5.0f);
                }
                mPopupWindow.setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                mPopupWindow.setOutsideTouchable(true);


                RecyclerView rvList = customView.findViewById(R.id.rv_font_style);
                Button btnOk = customView.findViewById(R.id.btnOk);

                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
                rvList.setLayoutManager(linearLayoutManager);

                AdapterFontList adapterFontList = new AdapterFontList(context, getfontList(), "greetingstyle");
                rvList.setAdapter(adapterFontList);

                btnOk.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                                mPopupWindow.dismiss();

                        }
                });

                mPopupWindow.showAtLocation(ll_main, Gravity.BOTTOM, 0, 0);

        }

        public void setFontStyle(String fontName) {
                Typeface font = Typeface.createFromAsset(context.getAssets(), "fonts/" + fontName);

                et_text_sticker.setTypeface(font);
        }

        public void textStickerColorPopUp() {


                LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);

                View customView = inflater.inflate(R.layout.select_color_popup, null);

                PaletteBar PFPaletteBar = customView.findViewById(R.id.paletteBar);
                PFPaletteBar.setListener(new PaletteBar.PaletteBarListener() {
                        @Override
                        public void onColorSelected(int color) {
                                et_text_sticker.setTextColor(color);
                                textcolor = color;

                        }
                });

                mPopupWindow = new PopupWindow(
                        customView,
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT
                );
                if (Build.VERSION.SDK_INT >= 21) {
                        mPopupWindow.setElevation(5.0f);
                }
                mPopupWindow.setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                mPopupWindow.setOutsideTouchable(true);


                final TextView dialogTitle = (TextView) customView.findViewById(R.id.cp_accent_title);
                final Button btnOk = (Button) customView.findViewById(R.id.btnOk);
                final Button btnCancel = (Button) customView.findViewById(R.id.btnCancel);


                dialogTitle.setText("Text Color");

                btnCancel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                                mPopupWindow.dismiss();
                        }
                });

                btnOk.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                                mPopupWindow.dismiss();

                        }
                });


                mPopupWindow.showAtLocation(ll_main, Gravity.BOTTOM, 0, 0);
        }

        public void setBorder() {


                LayoutInflater inflater = (LayoutInflater) context.getSystemService(LAYOUT_INFLATER_SERVICE);
                View customView = inflater.inflate(R.layout.border_size_popup, null);

                ImageView plusbtn, minusbtn;
                plusbtn = customView.findViewById(R.id.ivPlus);
                minusbtn = customView.findViewById(R.id.ivMinus);
                EditText etcount = customView.findViewById(R.id.etBordersize);
                PaletteBar PFPaletteBar = customView.findViewById(R.id.paletteBar);
                final LinearLayout ll_selectborder = customView.findViewById(R.id.ll_selectborder);


                mPopupWindow = new PopupWindow(
                        customView,
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT
                );
                if (Build.VERSION.SDK_INT >= 21) {
                        mPopupWindow.setElevation(5.0f);
                }


                PFPaletteBar.setListener(new PaletteBar.PaletteBarListener() {
                        @Override
                        public void onColorSelected(int color) {
                                ll_border.setBackgroundColor(color);

                        }
                });

                etcount.setCursorVisible(false);


                plusbtn.setOnClickListener(new View.OnClickListener() {

                        @Override
                        public void onClick(View v) {
                                boolean iscolor = true;


                                if (iscolor) {


                                        ll_selectborder.setBackgroundColor(Color.BLUE);
                                        count++;

                                        if (count <= 25) {


                                                ll_selectborder.setBackgroundColor(Color.parseColor("#000000"));
                                                etcount.setText(String.valueOf(count));
                                                int margin = Integer.parseInt(etcount.getText().toString());
                                                RelativeLayout.LayoutParams parameter = (RelativeLayout.LayoutParams) rl_createquote.getLayoutParams();
                                                parameter.setMargins(margin, margin, margin, margin); // left, top, right, bottom
                                                rl_createquote.setLayoutParams(parameter);
                                                abc = etcount.getText().toString();


                                        } else {
                                                count = 25;
                                                ll_selectborder.setBackgroundColor(Color.parseColor("#000000"));

                                        }
                                }


                        }
                });

                minusbtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                                boolean iscolor = true;

                                if (iscolor) {
                                        ll_selectborder.setBackgroundColor(Color.BLUE);
                                        count--;
                                        if (count >= 0) {

                                                ll_selectborder.setBackgroundColor(Color.parseColor("#000000"));
                                                etcount.setText(String.valueOf(count));
                                                int margin = Integer.parseInt(etcount.getText().toString());
                                                RelativeLayout.LayoutParams parameter = (RelativeLayout.LayoutParams) rl_createquote.getLayoutParams();
                                                parameter.setMargins(margin, margin, margin, margin); // left, top, right, bottom
                                                rl_createquote.setLayoutParams(parameter);
                                                abc = etcount.getText().toString();

                                        } else {
                                                count = 0;
                                                ll_selectborder.setBackgroundColor(Color.parseColor("#000000"));
                                        }

                                }


                        }
                });
                etcount.setText(abc);

                mPopupWindow.setOutsideTouchable(true);

                mPopupWindow.showAtLocation(ll_main, Gravity.BOTTOM, 0, 0);
        }

        public void openStickers() {

                LayoutInflater inflater = (LayoutInflater) context.getSystemService(LAYOUT_INFLATER_SERVICE);
                View customView = inflater.inflate(R.layout.sticker_pop_up, null);

                mPopupWindow = new PopupWindow(
                        customView,
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT
                );
                if (Build.VERSION.SDK_INT >= 21) {
                        mPopupWindow.setElevation(5.0f);
                }

                mPopupWindow.setOutsideTouchable(true);


                RecyclerView rvList = customView.findViewById(R.id.rvList);


                rvList.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, true));

                StickersListAdapter stickersListAdapter = new StickersListAdapter(context, stickerlist, "popup");
                rvList.setAdapter(stickersListAdapter);


                mPopupWindow.showAtLocation(ll_main, Gravity.BOTTOM, 0, 0);
        }

        public void setEmoji(int position) {

                sticker_view.addSticker(new DrawableSticker(getResources().getDrawable(stickerlist[position].getImgId())));
                mPopupWindow.dismiss();
        }

        private File createImageFile() throws IOException {

                String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
                String imageFileName = "JPEG_" + timeStamp + "_";
                File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
                File image = File.createTempFile(
                        imageFileName,  /* prefix */
                        ".jpg",         /* suffix */
                        storageDir      /* directory */
                );

                Log.e("tttt2", "createImageFile: " + image.getAbsolutePath());
                mCurrentPhotoPath = image.getAbsolutePath();
                return image;
        }

        private void viewTransformation(View view, MotionEvent event) {
                switch (event.getAction() & MotionEvent.ACTION_MASK) {
                        case MotionEvent.ACTION_DOWN:
                                xCoOrdinate = view.getX() - event.getRawX();
                                yCoOrdinate = view.getY() - event.getRawY();

                                start.set(event.getX(), event.getY());
                                isOutSide = false;
                                mode = DRAG;
                                lastEvent = null;
                                break;
                        case MotionEvent.ACTION_POINTER_DOWN:
                                oldDist = spacing(event);
                                if (oldDist > 10f) {
                                        midPoint(mid, event);
                                        mode = ZOOM;
                                }

                                lastEvent = new float[4];
                                lastEvent[0] = event.getX(0);
                                lastEvent[1] = event.getX(1);
                                lastEvent[2] = event.getY(0);
                                lastEvent[3] = event.getY(1);
                                d = rotation(event);
                                break;
                        case MotionEvent.ACTION_UP:
                                isZoomAndRotate = false;
                                if (mode == DRAG) {
                                        float x = event.getX();
                                        float y = event.getY();
                                }
                        case MotionEvent.ACTION_OUTSIDE:
                                isOutSide = true;
                                mode = NONE;
                                lastEvent = null;
                        case MotionEvent.ACTION_POINTER_UP:
                                mode = NONE;
                                lastEvent = null;
                                break;
                        case MotionEvent.ACTION_MOVE:
                                if (!isOutSide) {
                                        if (mode == DRAG) {
                                                isZoomAndRotate = false;
                                                view.animate().x(event.getRawX() + xCoOrdinate).y(event.getRawY() + yCoOrdinate).setDuration(0).start();
                                        }
                                        if (mode == ZOOM && event.getPointerCount() == 2) {
                                                float newDist1 = spacing(event);
                                                if (newDist1 > 10f) {
                                                        float scale = newDist1 / oldDist * view.getScaleX();
                                                        view.setScaleX(scale);
                                                        view.setScaleY(scale);
                                                }
                                                if (lastEvent != null) {
                                                        newRot = rotation(event);
                                                        view.setRotation((float) (view.getRotation() + (newRot - d)));
                                                }
                                        }
                                }
                                break;
                }
        }

        private float spacing(MotionEvent event) {
                float x = event.getX(0) - event.getX(1);
                float y = event.getY(0) - event.getY(1);
                return (float) Math.sqrt(x * x + y * y);
        }

        private float rotation(MotionEvent event) {
                double delta_x = (event.getX(0) - event.getX(1));
                double delta_y = (event.getY(0) - event.getY(1));
                double radians = Math.atan2(delta_y, delta_x);
                return (float) Math.toDegrees(radians);
        }

        private void midPoint(PointF point, MotionEvent event) {
                float x = event.getX(0) + event.getX(1);
                float y = event.getY(0) + event.getY(1);
                point.set(x / 2, y / 2);
        }

        private void requestPermission() {
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat
                                .requestPermissions(ActivityCreatePost.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_CODE_ASK_PERMISSIONS);
                } else {

                }
        }

        public void bindView() {

                arrayList = new ArrayList<>();
                iv_customimage = findViewById(R.id.iv_customimage);
                iv_cp_pic = findViewById(R.id.iv_cp_pic);
                sticker_view = findViewById(R.id.sticker_view);
                et_text_sticker = findViewById(R.id.et_text_sticker);
                et_text_sticker.setImeOptions(EditorInfo.IME_ACTION_DONE);
                rl_content = findViewById(R.id.rl_content);
                opacitybg = findViewById(R.id.opacitybg);
                rv_framelist = findViewById(R.id.rv_framelist);
                rl_video = findViewById(R.id.rl_video);
                vv_videoshow = findViewById(R.id.vv_videoshow);
//        iv_vp_play = findViewById(R.id.iv_vp_play);
                pb_sp = findViewById(R.id.pb_sp);
                ll_border = findViewById(R.id.ll_border);
                ll_addimage = findViewById(R.id.ll_addimage);
                ll_camera_pic = findViewById(R.id.ll_camera_pic);
                ll_flip = findViewById(R.id.ll_flip);
                ll_main = findViewById(R.id.ll_main);
                ll_sticker = findViewById(R.id.ll_sticker);
                ll_text_sticker = findViewById(R.id.ll_text_sticker);
                ll_set_boder = findViewById(R.id.ll_set_boder);
                rl_createquote = findViewById(R.id.rl_createquote);

                layout_custom_frame1 = findViewById(R.id.layout_custom_frame1);
                layout_custom_frame2 = findViewById(R.id.custom_frame_1);
                layout_custom_frame3 = findViewById(R.id.layout_custom_frame3);
                layout_custom_frame4 = findViewById(R.id.layout_custom_frame4);
                layout_custom_frame5 = findViewById(R.id.layout_custom_frame5);
                layout_custom_frame6 = findViewById(R.id.layout_custom_frame6);
                layout_custom_frame7 = findViewById(R.id.layout_custom_frame7);
                layout_custom_frame8 = findViewById(R.id.layout_custom_frame8);
                layout_custom_frame9 = findViewById(R.id.layout_custom_frame9);
                layout_custom_frame10 = findViewById(R.id.layout_custom_frame10);
                layout_custom_frame11 = findViewById(R.id.layout_custom_frame11);
                layout_custom_frame12 = findViewById(R.id.layout_custom_frame12);
                layout_custom_frame13 = findViewById(R.id.layout_custom_frame13);
                layout_custom_frame14 = findViewById(R.id.layout_custom_frame14);
                layout_custom_frame15 = findViewById(R.id.layout_custom_frame15);
                layout_custom_frame16 = findViewById(R.id.layout_custom_frame16);

                tv_mobilenumberlayout1 = findViewById(R.id.tv_mobilenumberlayout1);
                tv_mobilenumberlayout2 = findViewById(R.id.tv_mobilenumberlayout2);
                tv_mobilenumberlayout3 = findViewById(R.id.tv_mobilenumberlayout3);
                tv_mobilenumberlayout4 = findViewById(R.id.tv_mobilenumberlayout4);
                tv_mobilenumberlayout5 = findViewById(R.id.tv_mobilenumberlayout5);
                tv_mobilenumberlayout6 = findViewById(R.id.tv_mobilenumberlayout6);
                tv_mobilenumberlayout01 = findViewById(R.id.tv_mobilenumberlayout_01);
                tv_mobilenumberlayout02 = findViewById(R.id.tv_mobilenumberlayout_02);
                tv_mobilenumberlayout03 = findViewById(R.id.tv_mobilenumberlayout_03);
                tv_mobilenumberlayout04 = findViewById(R.id.tv_mobilenumberlayout_04);
                tv_mobilenumberlayout05 = findViewById(R.id.tv_mobilenumberlayout_05);
                tv_mobilenumberlayout06 = findViewById(R.id.tv_mobilenumberlayout_06);
                tv_mobilenumberlayout07 = findViewById(R.id.tv_mobilenumberlayout_07);
                tv_mobilenumberlayout08 = findViewById(R.id.tv_mobilenumberlayout_08);

                tvLocation1 = findViewById(R.id.tv_location_06);
                tvLocation2 = findViewById(R.id.tv_location_07);
                tvLocation3 = findViewById(R.id.tv_location_08);

                tv_websitelayout1 = findViewById(R.id.tv_websitelayout1);
                tv_websitelayout2 = findViewById(R.id.tv_websitelayout2);
                tv_websitelayout3 = findViewById(R.id.tv_websitelayout3);
                tv_websitelayout4 = findViewById(R.id.tv_websitelayout4);
                tv_websitelayout5 = findViewById(R.id.tv_websitelayout5);
                tv_websitelayout6 = findViewById(R.id.tv_websitelayout6);
                tv_websitelayout7 = findViewById(R.id.tv_websitelayout7);
                tv_websitelayout8 = findViewById(R.id.tv_websitelayout8);
                tv_websitelayout01 = findViewById(R.id.tv_websitelayout_01);
                tv_websitelayout02 = findViewById(R.id.tv_websitelayout_02);
                tv_websitelayout03 = findViewById(R.id.tv_websitelayout_03);
                tv_websitelayout04 = findViewById(R.id.tv_websitelayout_04);
                tv_websitelayout06 = findViewById(R.id.tv_websitelayout_06);
                tv_websitelayout07 = findViewById(R.id.tv_websitelayout_07);
                tv_websitelayout08 = findViewById(R.id.tv_websitelayout_08);

                tv_emaillayout1 = findViewById(R.id.tv_emaillayout1);
                tv_emaillayout2 = findViewById(R.id.tv_emaillayout2);
                tv_emaillayout3 = findViewById(R.id.tv_emaillayout3);
                tv_emaillayout4 = findViewById(R.id.tv_emaillayout4);
                tv_emaillayout5 = findViewById(R.id.tv_emaillayout5);
                tv_emaillayout6 = findViewById(R.id.tv_emaillayout6);
                tv_emaillayout8 = findViewById(R.id.tv_emaillayout8);
                tv_emaillayout01 = findViewById(R.id.tv_emaillayout_01);
                tv_emaillayout02 = findViewById(R.id.tv_emaillayout_02);
                tv_emaillayout03 = findViewById(R.id.tv_emaillayout_03);
                tv_emaillayout04 = findViewById(R.id.tv_emaillayout_04);
                tv_emaillayout05 = findViewById(R.id.tv_emaillayout_05);
                tv_emaillayout06 = findViewById(R.id.tv_emaillayout_06);
                tv_emaillayout07 = findViewById(R.id.tv_emaillayout_07);
                tv_emaillayout08 = findViewById(R.id.tv_emaillayout_08);

                iv_calllayout1 = findViewById(R.id.iv_calllayout1);
                iv_calllayout2 = findViewById(R.id.iv_calllayout2);
                iv_calllayout3 = findViewById(R.id.iv_calllayout3);
                iv_calllayout4 = findViewById(R.id.iv_calllayout4);
                iv_calllayout5 = findViewById(R.id.iv_calllayout5);
                iv_calllayout6 = findViewById(R.id.iv_calllayout6);
                iv_calllayout01 = findViewById(R.id.iv_calllayout_01);
                iv_calllayout02 = findViewById(R.id.iv_calllayout_02);
                iv_calllayout03 = findViewById(R.id.iv_calllayout_03);
                iv_calllayout04 = findViewById(R.id.iv_calllayout_04);
                iv_calllayout05 = findViewById(R.id.iv_calllayout_05);
                iv_calllayout06 = findViewById(R.id.iv_calllayout_06);
                iv_calllayout07 = findViewById(R.id.iv_calllayout_07);
                iv_calllayout08 = findViewById(R.id.iv_calllayout_08);

                ivLocation1 = findViewById(R.id.iv_location_06);
                ivLocation2 = findViewById(R.id.iv_location_07);
                ivLocation3 = findViewById(R.id.iv_location_08);

                iv_weblayout1 = findViewById(R.id.iv_weblayout1);
                iv_weblayout2 = findViewById(R.id.iv_weblayout2);
                iv_weblayout3 = findViewById(R.id.iv_weblayout3);
                iv_weblayout4 = findViewById(R.id.iv_weblayout4);
                iv_weblayout5 = findViewById(R.id.iv_weblayout5);
                iv_weblayout6 = findViewById(R.id.iv_weblayout6);
                iv_weblayout7 = findViewById(R.id.iv_weblayout7);
                iv_weblayout8 = findViewById(R.id.iv_weblayout8);
                iv_weblayout01 = findViewById(R.id.iv_weblayout_01);
                iv_weblayout02 = findViewById(R.id.iv_weblayout_02);
                iv_weblayout03 = findViewById(R.id.iv_weblayout_03);
                iv_weblayout04 = findViewById(R.id.iv_weblayout_04);
                iv_weblayout06 = findViewById(R.id.iv_weblayout_06);
                iv_weblayout07 = findViewById(R.id.iv_weblayout_07);
                iv_weblayout08 = findViewById(R.id.iv_weblayout_08);

                iv_emaillayout1 = findViewById(R.id.iv_emaillayout1);
                iv_emaillayout2 = findViewById(R.id.iv_emaillayout2);
                iv_emaillayout3 = findViewById(R.id.iv_emaillayout3);
                iv_emaillayout4 = findViewById(R.id.iv_emaillayout4);
                iv_emaillayout5 = findViewById(R.id.iv_emaillayout5);
                iv_emaillayout6 = findViewById(R.id.iv_emaillayout6);
                iv_emaillayout8 = findViewById(R.id.iv_emaillayout8);
                iv_emaillayout01 = findViewById(R.id.iv_emaillayout_01);
                iv_emaillayout02 = findViewById(R.id.iv_emaillayout_02);
                iv_emaillayout03 = findViewById(R.id.iv_emaillayout_03);
                iv_emaillayout04 = findViewById(R.id.iv_emaillayout_04);
                iv_emaillayout05 = findViewById(R.id.iv_emaillayout_05);
                iv_emaillayout06 = findViewById(R.id.iv_emaillayout_06);
                iv_emaillayout07 = findViewById(R.id.iv_emaillayout_07);
                iv_emaillayout08 = findViewById(R.id.iv_emaillayout_08);


                iv_logolayout1 = findViewById(R.id.iv_logolayout1);
                iv_logolayout2 = findViewById(R.id.iv_logolayout2);
                iv_logolayout3 = findViewById(R.id.iv_logolayout3);
                iv_logolayout4 = findViewById(R.id.iv_logolayout4);
                iv_logolayout5 = findViewById(R.id.iv_logolayout5);
                iv_logolayout6 = findViewById(R.id.iv_logolayout6);
                iv_logolayout7 = findViewById(R.id.iv_logolayout7);
                iv_logolayout8 = findViewById(R.id.iv_logolayout8);
                iv_logolayout01 = findViewById(R.id.iv_logolayout_01);
                iv_logolayout02 = findViewById(R.id.iv_logolayout_02);
                iv_logolayout03 = findViewById(R.id.iv_logolayout_03);
                iv_logolayout04 = findViewById(R.id.iv_logolayout_04);
                iv_logolayout05 = findViewById(R.id.iv_logolayout_05);
                iv_logolayout06 = findViewById(R.id.iv_logolayout_06);
                iv_logolayout07 = findViewById(R.id.iv_logolayout_07);
                iv_logolayout08 = findViewById(R.id.iv_logolayout_08);

                tv_remove_logo = findViewById(R.id.tv_remove_logo);
                tv_remove_email = findViewById(R.id.tv_remove_email);
                tv_remove_call = findViewById(R.id.tv_remove_call);
                tv_remove_web = findViewById(R.id.tv_remove_web);
                tv_remove_address = findViewById(R.id.tv_remove_address);


                ll_f1_call = findViewById(R.id.ll_f1_call);
                ll_f1_email = findViewById(R.id.ll_f1_email);
                ll_f1_web = findViewById(R.id.ll_f1_web);

                ll_f2_call = findViewById(R.id.ll_f2_call);
                ll_f2_email = findViewById(R.id.ll_f2_email);
                ll_f2_web = findViewById(R.id.ll_f2_web);

                ll_f3_call = findViewById(R.id.ll_f3_call);
                ll_f3_email = findViewById(R.id.ll_f3_email);
                ll_f3_web = findViewById(R.id.ll_f3_web);


                ll_f4_call = findViewById(R.id.ll_f4_call);
                ll_f4_email = findViewById(R.id.ll_f4_email);
                ll_f4_web = findViewById(R.id.ll_f4_web);

                ll_f5_call = findViewById(R.id.ll_f5_call);
                ll_f5_email = findViewById(R.id.ll_f5_email);
                ll_f5_web = findViewById(R.id.ll_f5_web);

                ll_f6_call = findViewById(R.id.ll_f6_call);
                ll_f6_email = findViewById(R.id.ll_f6_email);
                ll_f6_web = findViewById(R.id.ll_f6_web);

                ll_f7_web = findViewById(R.id.ll_f7_web);

                ll_f8_email = findViewById(R.id.ll_f8_email);
                ll_f8_web = findViewById(R.id.ll_f8_web);

                ll_f01_web = findViewById(R.id.ll_f01_web);
                ll_f01_email = findViewById(R.id.ll_f01_email);
                ll_f01_call = findViewById(R.id.ll_f01_call);

                ll_f02_web = findViewById(R.id.ll_f02_web);
                ll_f02_email = findViewById(R.id.ll_f02_email);
                ll_f02_call = findViewById(R.id.ll_f02_call);

                ll_f03_web = findViewById(R.id.ll_f03_web);
                ll_f03_email = findViewById(R.id.ll_f03_email);
                ll_f03_call = findViewById(R.id.ll_f03_call);

                ll_f04_web = findViewById(R.id.ll_f04_web);
                ll_f04_email = findViewById(R.id.ll_f04_email);
                ll_f04_call = findViewById(R.id.ll_f04_call);

                ll_f05_email = findViewById(R.id.ll_f05_email);
                ll_f05_call = findViewById(R.id.ll_f05_call);

                ll_f06_web = findViewById(R.id.ll_f06_web);
                ll_f06_email = findViewById(R.id.ll_f06_email);
                ll_f06_call = findViewById(R.id.ll_f06_call);
                llLocation1 = findViewById(R.id.ll_f06_location);

                ll_f07_web = findViewById(R.id.ll_f07_web);
                ll_f07_email = findViewById(R.id.ll_f07_email);
                ll_f07_call = findViewById(R.id.ll_f07_call);
                llLocation2 = findViewById(R.id.ll_f07_location);

                ll_f08_web = findViewById(R.id.ll_f08_web);
                ll_f08_email = findViewById(R.id.ll_f08_email);
                ll_f08_call = findViewById(R.id.ll_f08_call);
                llLocation3 = findViewById(R.id.ll_f08_location);

        }

        public void playVideo() {
                pDialog = new ProgressDialog(this);
                pDialog.setMessage("Buffering...");
                pDialog.setIndeterminate(false);
                pDialog.setCancelable(false);
                pDialog.show();
                pDialog.dismiss();


                if (FromSinglecatActivity != null) {
                        vv_videoshow.setVideoPath(Constance.SaveVideoDirectory + "/" + videoName + ".mp4");
                } else {
                        Toast.makeText(context, "Not get the video path", Toast.LENGTH_LONG).show();
                }
                mediacontroller = new MediaController(context);
                mediacontroller.setMediaPlayer(vv_videoshow);
                vv_videoshow.setMediaController(mediacontroller);
                vv_videoshow.requestFocus();
                vv_videoshow.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                        public void onPrepared(MediaPlayer mp) {
                                pDialog.dismiss();
                                vv_videoshow.start();
                        }
                });
                vv_videoshow.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                        public void onCompletion(MediaPlayer mp) {
                                if (pDialog.isShowing()) {
                                        pDialog.dismiss();
                                        mp.stop();
//                    iv_vp_play.setVisibility(View.VISIBLE);

                                }

                                vv_videoshow.pause();
//                iv_vp_play.setVisibility(View.VISIBLE);

                        }
                });


        }

        public void onclickCustomFrame(View view) {
                switch (view.getId()) {
                        case R.id.iv_backarrow:
                                onBackPressed();
                                break;
                        case R.id.ll_next:

                                if (!comeFrom.equals("image")) {
                                        iv_customimage.setVisibility(View.GONE);
//                    iv_vp_play.setVisibility(View.GONE);

                                } else {
                                        rl_video.setVisibility(View.GONE);
                                        sticker_view.hideIcons(true);
                                        sticker_view.setLocked(true);

                                }


                                sticker_view.hideIcons(true);
                                bitmapsave = viewToBitmap(rl_content);

                                if (!Constance.SaveVideoDirectory.exists()) {
                                        Constance.SaveVideoDirectory.mkdir();
                                }

                                Constance.createdBitmap = scaleBitmap(bitmapsave, 1080, 1080);

                                String timeImg = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());

                                try {
                                        Uri uri1 = saveImage(context, Constance.createdBitmap, getString(R.string.app_name), imgName + ".png");
                                } catch (IOException e) {
                                        e.printStackTrace();
                                }

                                if (comeFrom.equals("image")) {

                                        Intent save = new Intent(context, ActivityPreview.class);
                                        save.putExtra("name", "image");
                                        save.putExtra("imgName", this.imgName);
                                        startActivity(save);
                                } else {
                                        Constance.createdBitmap = scaleBitmap(bitmapsave, 1080, 1080);
                                        Log.d("dhfshsdf", "dfs" + Constance.createdBitmap);

                                        String location = Constance.SaveVideoDirectory + "/" + videoName + ".mp4";
                                        Constance.savedVideoPath = location;
                                        if (new File(location).exists()) {

                                                Toast.makeText(context, "Already Downloaded", Toast.LENGTH_SHORT).show();

                                                Intent save = new Intent(context, ActivityPreview.class);
                                                save.putExtra("name", "video");
                                                save.putExtra("videoName", location);
                                                context.startActivity(save);

                                        } else {


                                                new Utils_VideoDownload(context, FromSinglecatActivity, 0, 0, location);

                                        }

                                        Log.d("djjjjj", "onDownloadComplete" + Constance.savedVideoPath);

                                }

                                break;
                        case R.id.rlBackgroundColor:
                                openDailogForBackgroundcolour();
                                break;
                        case R.id.rlOverlay:
                                openDailogForOverlayBg();
                                break;
                        case R.id.rl_addlogo:
                                openGallery();
                                break;

                        case R.id.rlBackgroundImageLocal:
                                OpenDailogForLocalBackgroundImage();

                                break;
                        case R.id.ll_addimage:
                                PickImageFromMobileGallery();
                                break;

                }
        }

        private Uri saveImage(Context context, Bitmap bitmap, @NonNull String folderName, @NonNull String fileName) throws IOException {
                OutputStream fos = null;
                File imageFile = null;
                Uri imageUri = null;
                File imagesDir1 = new File(Environment.getExternalStorageDirectory().getPath() + File.separator + "girl");
                imagesDir = new File(Environment.getExternalStoragePublicDirectory(
                        Environment.DIRECTORY_PICTURES).toString() + File.separator + folderName);

                if (!imagesDir.exists())
                        imagesDir.mkdir();
                try {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                                ContentResolver resolver = context.getContentResolver();
                                ContentValues contentValues = new ContentValues();
                                contentValues.put(MediaStore.MediaColumns.DISPLAY_NAME, fileName);
                                contentValues.put(MediaStore.MediaColumns.MIME_TYPE, "image/*");
                                contentValues.put(
                                        MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_PICTURES + File.separator + folderName);
                                imageUri = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);


                                if (imageUri == null)
                                        throw new IOException("Failed to create new MediaStore record.");

                                fos = resolver.openOutputStream(imageUri);
                        } else {
                                imagesDir = new File(Environment.getExternalStoragePublicDirectory(
                                        Environment.DIRECTORY_PICTURES).toString() + File.separator + folderName);

                                if (!imagesDir.exists())
                                        imagesDir.mkdir();

                                imageFile = new File(imagesDir, fileName);
                                fos = new FileOutputStream(imageFile);
                        }


                        if (!bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos))
                                throw new IOException("Failed to save bitmap.");
                        fos.flush();
                } finally {
                        if (fos != null)
                                fos.close();
                }


                return imageUri;
        }

        Bitmap scaleBitmap(Bitmap bitmap, int wantedWidth, int wantedHeight) {
                float originalWidth = bitmap.getWidth();
                float originalHeight = bitmap.getHeight();
                Bitmap output = Bitmap.createBitmap(wantedWidth, wantedHeight, Bitmap.Config.ARGB_8888);
                Canvas canvas = new Canvas(output);
                Matrix m = new Matrix();
                float scalex = wantedWidth / originalWidth;
                float scaley = wantedHeight / originalHeight;
                float xTranslation = 0.0f;
                float yTranslation = (wantedHeight - originalHeight * scaley) / 2.0f;
                m.postTranslate(xTranslation, yTranslation);
                m.preScale(scalex, scaley);
                Paint paint = new Paint();
                paint.setFilterBitmap(true);
                canvas.drawBitmap(bitmap, m, paint);
                return output;
        }

        public void onclickCustomLayout(View view) {
                switch (view.getId()) {

                }
        }

        public void openGallery() {
                Intent pickPhoto = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(pickPhoto, 1);
        }

        @Override
        public void onActivityResult(int requestCode, int resultCode, Intent data) {
                super.onActivityResult(requestCode, resultCode, data);
                Log.d("abc", "createquote");

                if (requestCode == 2 && resultCode == RESULT_OK) {
                        Toast.makeText(this, "Image saved", Toast.LENGTH_SHORT).show();

                        Bitmap bitmap = null;
                        try {
                                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), photouri);
                                iv_cp_pic.setImageBitmap(bitmap);

                        } catch (IOException e) {
                                e.printStackTrace();
                        }

                }
                if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
                        CropImage.ActivityResult result = CropImage.getActivityResult(data);
                        if (resultCode == RESULT_OK) {
                                Uri resultUri = result.getUri();

                                Log.e("tttttt", "onActivityResult: " + resultUri);
                                if (Constance.activityName.equals("greeting")) {
                                        iv_cp_pic.setImageURI(resultUri);
                                } else {
                                        iv_customimage.setImageURI(resultUri);

                                }

                        } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                                Exception error = result.getError();
                        }
                }
                if (requestCode == 1 && resultCode == RESULT_OK) {
                        Uri photoUri = data.getData();
                        if (photoUri != null) {
                                try {
                                        Bitmap currentImage = MediaStore.Images.Media.getBitmap(this.getContentResolver(), photoUri);
                                        Drawable d = new BitmapDrawable(getResources(), currentImage);
                                        DrawableSticker drawableSticker = new DrawableSticker(d);
                                        sticker_view.addSticker(drawableSticker);
                                        Constance.isStickerAvail = true;
                                        Constance.isStickerTouch = true;
                                        sticker_view.setLocked(false);
                                } catch (Exception e) {
                                        e.printStackTrace();
                                }
                        }

                }


        }

        @Override
        public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
                if (requestCode == MY_CAMERA_PERMISSION_CODE) {
                        if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                                Toast.makeText(context, "camera permission granted", Toast.LENGTH_LONG).show();

                        } else {
                                Toast.makeText(context, "camera permission denied", Toast.LENGTH_LONG).show();
                        }
                }
        }

        @Override
        public void onResume() {
                super.onResume();

                mTracker.setScreenName("Image~" + "Google Analytics Testing");
                mTracker.send(new HitBuilders.ScreenViewBuilder().build());
                pb_sp.setVisibility(View.GONE);
                FromSinglecatActivity = Constance.FromSinglecatActivity;
                comeFrom = Constance.ComeFrom;
                Log.d("sdasjdiaisdi", "hhhhh" + comeFrom);
                if (comeFrom.equals("image")) {

                        rl_video.setVisibility(View.GONE);
                        if (FromSinglecatActivity != null) {
                                Log.d("sdasjdiaisdi", "sdhjsd");
                                iv_customimage.setVisibility(View.VISIBLE);
                                Glide.with(context).load(FromSinglecatActivity).into(iv_customimage);
                        } else {

                        }
                } else {

                        rl_video.setVisibility(View.VISIBLE);
                        if (FromSinglecatActivity != null) {
                                playVideo();
                        } else {

                        }
                }

        }

        public void openDailogForBackgroundcolour() {

                final Dialog dialog = new Dialog(context);
                dialog.setContentView(R.layout.dailog_bg_color);
                dialog.getWindow().setGravity(Gravity.BOTTOM);
                dialog.setCanceledOnTouchOutside(true);
                WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
                lp.copyFrom(dialog.getWindow().getAttributes());
                lp.width = WindowManager.LayoutParams.MATCH_PARENT;
                lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
                dialog.getWindow().setAttributes(lp);

                Button btnOk = (Button) dialog.findViewById(R.id.btnOk);
                final TextView tvOpacity = dialog.findViewById(R.id.tvOpacity);
                final SeekBar sbOpacity = dialog.findViewById(R.id.sbOpacity);
                RecyclerView rv_bg_color = dialog.findViewById(R.id.rv_bg_color);
                TextView tv_dailog_tittle = dialog.findViewById(R.id.tv_dailog_tittle);

                tv_dailog_tittle.setText("Change Text Color");
                GridLayoutManager linearLayoutManager = new GridLayoutManager(context, 7);
                rv_bg_color.setLayoutManager(linearLayoutManager);


                AdapterTextColourPicker adapterTextColourPicker = new AdapterTextColourPicker(context, getColorList(), "bgTextcolor");
                rv_bg_color.setAdapter(adapterTextColourPicker);


                sbOpacity.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                        @Override
                        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                                progress = ((int) Math.round(progress / 5)) * 5;
                                iv_customimage.setAlpha((Float.valueOf(progress) / Float.valueOf(100)));
                                tvOpacity.setText(progress + "%");
                        }

                        @Override
                        public void onStartTrackingTouch(SeekBar seekBar) {

                        }

                        @Override
                        public void onStopTrackingTouch(SeekBar seekBar) {

                        }
                });


                btnOk.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                                dialog.dismiss();
                        }
                });

                dialog.show();
        }

        public void PickImageFromMobileGallery() {
                CropImage.activity()
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .setAspectRatio(1, 1)
                        .start((Activity) context);

        }

        public void OpenDailogForLocalBackgroundImage() {
                final Dialog dialog = new Dialog(context);
                dialog.setContentView(R.layout.dailog_bg_local_image);
                dialog.getWindow().setGravity(Gravity.BOTTOM);
                dialog.setCanceledOnTouchOutside(true);
                WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
                lp.copyFrom(dialog.getWindow().getAttributes());
                lp.width = WindowManager.LayoutParams.MATCH_PARENT;
                lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
                dialog.getWindow().setAttributes(lp);
                Button btnok = dialog.findViewById(R.id.btnOk);
                RecyclerView rl_bglocalimage = dialog.findViewById(R.id.rl_bglocalimage);

                rl_bglocalimage.setLayoutManager(new GridLayoutManager(context, 2));
                AdapterBackgroundImage adapterBackgroundImage = new AdapterBackgroundImage(context, getLocalImageList(), "createpost");
                rl_bglocalimage.setAdapter(adapterBackgroundImage);
                btnok.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                                dialog.dismiss();
                        }
                });
                dialog.show();


        }

        public void setbackgroundLocalImage(int image) {
                iv_customimage.setImageResource(image);

        }

        public void openDailogForOverlayBg() {
                final Dialog dialog = new Dialog(context);
                dialog.setContentView(R.layout.dailog_bg_color);
                dialog.getWindow().setGravity(Gravity.BOTTOM);
                dialog.setCanceledOnTouchOutside(true);
                WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
                lp.copyFrom(dialog.getWindow().getAttributes());
                lp.width = WindowManager.LayoutParams.MATCH_PARENT;
                lp.height = WindowManager.LayoutParams.WRAP_CONTENT;

                dialog.getWindow().setAttributes(lp);

                final Button btnOk = (Button) dialog.findViewById(R.id.btnOk);
                final SeekBar sbOpacity = dialog.findViewById(R.id.sbOpacity);
                final RecyclerView rv_bg_color = dialog.findViewById(R.id.rv_bg_color);
                final TextView tvOpacity = dialog.findViewById(R.id.tvOpacity);
                final TextView tv_dailog_tittle = dialog.findViewById(R.id.tv_dailog_tittle);

                GridLayoutManager linearLayoutManager = new GridLayoutManager(context, 7);
                rv_bg_color.setLayoutManager(linearLayoutManager);
                AdapterTextColourPicker adapterTextColourPicker = new AdapterTextColourPicker(context, getColorList(), "overlay");
                rv_bg_color.setAdapter(adapterTextColourPicker);
                tv_dailog_tittle.setText("Overlay");

                sbOpacity.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                        @Override
                        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                                tvOpacity.setText(progress + "%");
                                opacitybg.setAlpha((Float.valueOf(progress) / Float.valueOf(100)));

                        }

                        @Override
                        public void onStartTrackingTouch(SeekBar seekBar) {

                        }

                        @Override
                        public void onStopTrackingTouch(SeekBar seekBar) {

                        }
                });
                sbOpacity.setProgress(20);

                btnOk.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                                dialog.dismiss();
                        }
                });

                dialog.show();


        }

        public Bitmap viewToBitmap(View view) {
                Bitmap bitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(), Bitmap.Config.ARGB_8888);
                Canvas canvas = new Canvas(bitmap);
                view.draw(canvas);
                return bitmap;
        }

        public void setOverlayBackground(int colour) {
                opacitybg.setBackgroundResource(colour);
        }

        private void touchListener(View view) {
                view.setOnTouchListener(new View.OnTouchListener() {
                        @Override
                        public boolean onTouch(View v, MotionEvent event) {
                                if (event.getActionMasked() == MotionEvent.ACTION_DOWN) {

                                        if (Constance.isStickerAvail) {

                                                if (Constance.isStickerTouch || !Constance.isStickerTouch) {
                                                        Constance.isStickerTouch = false;
                                                        sticker_view.setLocked(true);
                                                }
                                        }

                                } else if (event.getActionMasked() == MotionEvent.ACTION_UP) {
                                        if (Constance.isStickerAvail) {


                                                if (!Constance.isStickerTouch) {
                                                        Constance.isStickerTouch = true;
                                                        sticker_view.setLocked(false);
                                                }
                                        }
                                } else if (event.getActionMasked() == MotionEvent.ACTION_MOVE) {

                                        if (Constance.isStickerAvail) {
                                                if (!Constance.isStickerTouch || Constance.isStickerTouch) {
                                                        Log.e("image move", "sticker lock");
                                                        Constance.isStickerTouch = false;
                                                        sticker_view.setLocked(true);
                                                }
                                        }
                                }
                                return true;
                        }
                });
        }


        void dwonloadVideo() {
                if (!Constance.FileSaveVideoDirectory.exists()) {
                        Constance.FileSaveVideoDirectory.mkdir();
                }
                Log.d("djsj", "djd" + FromSinglecatActivity);
                PRDownloader.download(FromSinglecatActivity, Constance.FileSaveVideoDirectory.getPath(), "VideoDemo.mp4")
                        .build()
                        .setOnStartOrResumeListener(new OnStartOrResumeListener() {
                                @Override
                                public void onStartOrResume() {
                                        Log.d("djjjjj", "onStartOrResume");


                                }
                        })
                        .setOnPauseListener(new OnPauseListener() {
                                @Override
                                public void onPause() {
                                        Log.d("djjjjj", "setOnPauseListener");
                                }
                        })
                        .setOnCancelListener(new OnCancelListener() {
                                @Override
                                public void onCancel() {
                                        Log.d("djjjjj", "setOnCancelListener");

                                }
                        })
                        .setOnProgressListener(new OnProgressListener() {
                                @Override
                                public void onProgress(Progress progress) {
                                        Log.d("djjjjj", "setOnProgressListener" + progress.toString());

                                }
                        })
                        .start(new OnDownloadListener() {
                                @Override
                                public void onDownloadComplete() {
                                        pDialog1.dismiss();
                                        Constance.savedVideoPath = Constance.FileSaveVideoDirectory.getPath() + "/VideoDemo.mp4";
                                        Log.d("djjjjj", "onDownloadComplete" + Constance.savedVideoPath);
                                        Intent save = new Intent(context, ActivityPreview.class);
                                        save.putExtra("name", "video");
                                        startActivity(save);

                                }

                                @Override
                                public void onError(Error error) {
                                        Log.d("djjjjj", "onError" + error.toString());

                                }


                        });
        }


        public void setSelectedFrame(int position) {


                switch (position) {
                        case 0:

                                layout_custom_frame2.setVisibility(View.GONE);
                                layout_custom_frame1.setVisibility(View.VISIBLE);
                                layout_custom_frame3.setVisibility(View.GONE);
                                layout_custom_frame4.setVisibility(View.GONE);
                                layout_custom_frame5.setVisibility(View.GONE);
                                layout_custom_frame6.setVisibility(View.GONE);
                                layout_custom_frame7.setVisibility(View.GONE);
                                layout_custom_frame8.setVisibility(View.GONE);
                                layout_custom_frame9.setVisibility(View.GONE);
                                layout_custom_frame10.setVisibility(View.GONE);
                                layout_custom_frame11.setVisibility(View.GONE);
                                layout_custom_frame12.setVisibility(View.GONE);
                                layout_custom_frame13.setVisibility(View.GONE);
                                layout_custom_frame14.setVisibility(View.GONE);
                                layout_custom_frame15.setVisibility(View.GONE);
                                layout_custom_frame16.setVisibility(View.GONE);

                                break;
                        case 1:

                                layout_custom_frame1.setVisibility(View.GONE);
                                layout_custom_frame2.setVisibility(View.VISIBLE);
                                layout_custom_frame3.setVisibility(View.GONE);
                                layout_custom_frame4.setVisibility(View.GONE);
                                layout_custom_frame5.setVisibility(View.GONE);
                                layout_custom_frame6.setVisibility(View.GONE);
                                layout_custom_frame7.setVisibility(View.GONE);
                                layout_custom_frame8.setVisibility(View.GONE);
                                layout_custom_frame9.setVisibility(View.GONE);
                                layout_custom_frame10.setVisibility(View.GONE);
                                layout_custom_frame11.setVisibility(View.GONE);
                                layout_custom_frame12.setVisibility(View.GONE);
                                layout_custom_frame13.setVisibility(View.GONE);
                                layout_custom_frame14.setVisibility(View.GONE);
                                layout_custom_frame15.setVisibility(View.GONE);
                                layout_custom_frame16.setVisibility(View.GONE);

                                break;
                        // frame 3(f7)
                        case 2:

                                layout_custom_frame1.setVisibility(View.GONE);
                                layout_custom_frame2.setVisibility(View.GONE);
                                layout_custom_frame3.setVisibility(View.VISIBLE);
                                layout_custom_frame4.setVisibility(View.GONE);
                                layout_custom_frame5.setVisibility(View.GONE);
                                layout_custom_frame6.setVisibility(View.GONE);
                                layout_custom_frame7.setVisibility(View.GONE);
                                layout_custom_frame8.setVisibility(View.GONE);
                                layout_custom_frame9.setVisibility(View.GONE);
                                layout_custom_frame10.setVisibility(View.GONE);
                                layout_custom_frame11.setVisibility(View.GONE);
                                layout_custom_frame12.setVisibility(View.GONE);
                                layout_custom_frame13.setVisibility(View.GONE);
                                layout_custom_frame14.setVisibility(View.GONE);
                                layout_custom_frame15.setVisibility(View.GONE);
                                layout_custom_frame16.setVisibility(View.GONE);

                                break;
                        //frame 4 (f8)
                        case 3:

                                layout_custom_frame1.setVisibility(View.GONE);
                                layout_custom_frame2.setVisibility(View.GONE);
                                layout_custom_frame3.setVisibility(View.GONE);
                                layout_custom_frame4.setVisibility(View.VISIBLE);
                                layout_custom_frame5.setVisibility(View.GONE);
                                layout_custom_frame6.setVisibility(View.GONE);
                                layout_custom_frame7.setVisibility(View.GONE);
                                layout_custom_frame8.setVisibility(View.GONE);
                                layout_custom_frame9.setVisibility(View.GONE);
                                layout_custom_frame10.setVisibility(View.GONE);
                                layout_custom_frame11.setVisibility(View.GONE);
                                layout_custom_frame12.setVisibility(View.GONE);
                                layout_custom_frame13.setVisibility(View.GONE);
                                layout_custom_frame14.setVisibility(View.GONE);
                                layout_custom_frame15.setVisibility(View.GONE);
                                layout_custom_frame16.setVisibility(View.GONE);

                                break;
                        //frame 4 (f9)
                        case 4:

                                layout_custom_frame1.setVisibility(View.GONE);
                                layout_custom_frame2.setVisibility(View.GONE);
                                layout_custom_frame3.setVisibility(View.GONE);
                                layout_custom_frame4.setVisibility(View.GONE);
                                layout_custom_frame5.setVisibility(View.VISIBLE);
                                layout_custom_frame6.setVisibility(View.GONE);
                                layout_custom_frame7.setVisibility(View.GONE);
                                layout_custom_frame8.setVisibility(View.GONE);
                                layout_custom_frame9.setVisibility(View.GONE);
                                layout_custom_frame10.setVisibility(View.GONE);
                                layout_custom_frame11.setVisibility(View.GONE);
                                layout_custom_frame12.setVisibility(View.GONE);
                                layout_custom_frame13.setVisibility(View.GONE);
                                layout_custom_frame14.setVisibility(View.GONE);
                                layout_custom_frame15.setVisibility(View.GONE);
                                layout_custom_frame16.setVisibility(View.GONE);

                                break;
                        //frame 4 (f10)
                        case 5:

                                layout_custom_frame1.setVisibility(View.GONE);
                                layout_custom_frame2.setVisibility(View.GONE);
                                layout_custom_frame3.setVisibility(View.GONE);
                                layout_custom_frame4.setVisibility(View.GONE);
                                layout_custom_frame5.setVisibility(View.GONE);
                                layout_custom_frame6.setVisibility(View.VISIBLE);
                                layout_custom_frame7.setVisibility(View.GONE);
                                layout_custom_frame8.setVisibility(View.GONE);
                                layout_custom_frame9.setVisibility(View.GONE);
                                layout_custom_frame10.setVisibility(View.GONE);
                                layout_custom_frame11.setVisibility(View.GONE);
                                layout_custom_frame12.setVisibility(View.GONE);
                                layout_custom_frame13.setVisibility(View.GONE);
                                layout_custom_frame14.setVisibility(View.GONE);
                                layout_custom_frame15.setVisibility(View.GONE);
                                layout_custom_frame16.setVisibility(View.GONE);

                                break;
                        //frame 4 (f3)
                        case 6:

                                layout_custom_frame1.setVisibility(View.GONE);
                                layout_custom_frame2.setVisibility(View.GONE);
                                layout_custom_frame3.setVisibility(View.GONE);
                                layout_custom_frame4.setVisibility(View.GONE);
                                layout_custom_frame5.setVisibility(View.GONE);
                                layout_custom_frame6.setVisibility(View.GONE);
                                layout_custom_frame7.setVisibility(View.VISIBLE);
                                layout_custom_frame8.setVisibility(View.GONE);
                                layout_custom_frame9.setVisibility(View.GONE);
                                layout_custom_frame10.setVisibility(View.GONE);
                                layout_custom_frame11.setVisibility(View.GONE);
                                layout_custom_frame12.setVisibility(View.GONE);
                                layout_custom_frame13.setVisibility(View.GONE);
                                layout_custom_frame14.setVisibility(View.GONE);
                                layout_custom_frame15.setVisibility(View.GONE);
                                layout_custom_frame16.setVisibility(View.GONE);

                                break; //frame 4 (f3)
                        case 7:

                                layout_custom_frame1.setVisibility(View.GONE);
                                layout_custom_frame2.setVisibility(View.GONE);
                                layout_custom_frame3.setVisibility(View.GONE);
                                layout_custom_frame4.setVisibility(View.GONE);
                                layout_custom_frame5.setVisibility(View.GONE);
                                layout_custom_frame6.setVisibility(View.GONE);
                                layout_custom_frame7.setVisibility(View.GONE);
                                layout_custom_frame8.setVisibility(View.VISIBLE);
                                layout_custom_frame9.setVisibility(View.GONE);
                                layout_custom_frame10.setVisibility(View.GONE);
                                layout_custom_frame11.setVisibility(View.GONE);
                                layout_custom_frame12.setVisibility(View.GONE);
                                layout_custom_frame13.setVisibility(View.GONE);
                                layout_custom_frame14.setVisibility(View.GONE);
                                layout_custom_frame15.setVisibility(View.GONE);
                                layout_custom_frame16.setVisibility(View.GONE);
                                break;

                        case 8:

                                layout_custom_frame1.setVisibility(View.GONE);
                                layout_custom_frame2.setVisibility(View.GONE);
                                layout_custom_frame3.setVisibility(View.GONE);
                                layout_custom_frame4.setVisibility(View.GONE);
                                layout_custom_frame5.setVisibility(View.GONE);
                                layout_custom_frame6.setVisibility(View.GONE);
                                layout_custom_frame7.setVisibility(View.GONE);
                                layout_custom_frame8.setVisibility(View.GONE);
                                layout_custom_frame9.setVisibility(View.VISIBLE);
                                layout_custom_frame10.setVisibility(View.GONE);
                                layout_custom_frame11.setVisibility(View.GONE);
                                layout_custom_frame12.setVisibility(View.GONE);
                                layout_custom_frame13.setVisibility(View.GONE);
                                layout_custom_frame14.setVisibility(View.GONE);
                                layout_custom_frame15.setVisibility(View.GONE);
                                layout_custom_frame16.setVisibility(View.GONE);

                                break;
                        case 15:

                                layout_custom_frame1.setVisibility(View.GONE);
                                layout_custom_frame2.setVisibility(View.GONE);
                                layout_custom_frame3.setVisibility(View.GONE);
                                layout_custom_frame4.setVisibility(View.GONE);
                                layout_custom_frame5.setVisibility(View.GONE);
                                layout_custom_frame6.setVisibility(View.GONE);
                                layout_custom_frame7.setVisibility(View.GONE);
                                layout_custom_frame8.setVisibility(View.GONE);
                                layout_custom_frame9.setVisibility(View.GONE);
                                layout_custom_frame10.setVisibility(View.VISIBLE);
                                layout_custom_frame11.setVisibility(View.GONE);
                                layout_custom_frame12.setVisibility(View.GONE);
                                layout_custom_frame13.setVisibility(View.GONE);
                                layout_custom_frame14.setVisibility(View.GONE);
                                layout_custom_frame15.setVisibility(View.GONE);
                                layout_custom_frame16.setVisibility(View.GONE);

                                break;
                        case 9:

                                layout_custom_frame1.setVisibility(View.GONE);
                                layout_custom_frame2.setVisibility(View.GONE);
                                layout_custom_frame3.setVisibility(View.GONE);
                                layout_custom_frame4.setVisibility(View.GONE);
                                layout_custom_frame5.setVisibility(View.GONE);
                                layout_custom_frame6.setVisibility(View.GONE);
                                layout_custom_frame7.setVisibility(View.GONE);
                                layout_custom_frame8.setVisibility(View.GONE);
                                layout_custom_frame9.setVisibility(View.GONE);
                                layout_custom_frame10.setVisibility(View.GONE);
                                layout_custom_frame11.setVisibility(View.VISIBLE);
                                layout_custom_frame12.setVisibility(View.GONE);
                                layout_custom_frame13.setVisibility(View.GONE);
                                layout_custom_frame14.setVisibility(View.GONE);
                                layout_custom_frame15.setVisibility(View.GONE);
                                layout_custom_frame16.setVisibility(View.GONE);

                                break;
                        case 10:

                                layout_custom_frame1.setVisibility(View.GONE);
                                layout_custom_frame2.setVisibility(View.GONE);
                                layout_custom_frame3.setVisibility(View.GONE);
                                layout_custom_frame4.setVisibility(View.GONE);
                                layout_custom_frame5.setVisibility(View.GONE);
                                layout_custom_frame6.setVisibility(View.GONE);
                                layout_custom_frame7.setVisibility(View.GONE);
                                layout_custom_frame8.setVisibility(View.GONE);
                                layout_custom_frame9.setVisibility(View.GONE);
                                layout_custom_frame10.setVisibility(View.GONE);
                                layout_custom_frame11.setVisibility(View.GONE);
                                layout_custom_frame12.setVisibility(View.VISIBLE);
                                layout_custom_frame13.setVisibility(View.GONE);
                                layout_custom_frame14.setVisibility(View.GONE);
                                layout_custom_frame15.setVisibility(View.GONE);
                                layout_custom_frame16.setVisibility(View.GONE);

                                break;
                        case 11:

                                layout_custom_frame1.setVisibility(View.GONE);
                                layout_custom_frame2.setVisibility(View.GONE);
                                layout_custom_frame3.setVisibility(View.GONE);
                                layout_custom_frame4.setVisibility(View.GONE);
                                layout_custom_frame5.setVisibility(View.GONE);
                                layout_custom_frame6.setVisibility(View.GONE);
                                layout_custom_frame7.setVisibility(View.GONE);
                                layout_custom_frame8.setVisibility(View.GONE);
                                layout_custom_frame9.setVisibility(View.GONE);
                                layout_custom_frame10.setVisibility(View.GONE);
                                layout_custom_frame11.setVisibility(View.GONE);
                                layout_custom_frame12.setVisibility(View.GONE);
                                layout_custom_frame13.setVisibility(View.VISIBLE);
                                layout_custom_frame14.setVisibility(View.GONE);
                                layout_custom_frame15.setVisibility(View.GONE);
                                layout_custom_frame16.setVisibility(View.GONE);

                                break;
                        case 12:

                                layout_custom_frame1.setVisibility(View.GONE);
                                layout_custom_frame2.setVisibility(View.GONE);
                                layout_custom_frame3.setVisibility(View.GONE);
                                layout_custom_frame4.setVisibility(View.GONE);
                                layout_custom_frame5.setVisibility(View.GONE);
                                layout_custom_frame6.setVisibility(View.GONE);
                                layout_custom_frame7.setVisibility(View.GONE);
                                layout_custom_frame8.setVisibility(View.GONE);
                                layout_custom_frame9.setVisibility(View.GONE);
                                layout_custom_frame10.setVisibility(View.GONE);
                                layout_custom_frame11.setVisibility(View.GONE);
                                layout_custom_frame12.setVisibility(View.GONE);
                                layout_custom_frame13.setVisibility(View.GONE);
                                layout_custom_frame14.setVisibility(View.VISIBLE);
                                layout_custom_frame15.setVisibility(View.GONE);
                                layout_custom_frame16.setVisibility(View.GONE);

                                break;
                        case 13:

                                layout_custom_frame1.setVisibility(View.GONE);
                                layout_custom_frame2.setVisibility(View.GONE);
                                layout_custom_frame3.setVisibility(View.GONE);
                                layout_custom_frame4.setVisibility(View.GONE);
                                layout_custom_frame5.setVisibility(View.GONE);
                                layout_custom_frame6.setVisibility(View.GONE);
                                layout_custom_frame7.setVisibility(View.GONE);
                                layout_custom_frame8.setVisibility(View.GONE);
                                layout_custom_frame9.setVisibility(View.GONE);
                                layout_custom_frame10.setVisibility(View.GONE);
                                layout_custom_frame11.setVisibility(View.GONE);
                                layout_custom_frame12.setVisibility(View.GONE);
                                layout_custom_frame13.setVisibility(View.GONE);
                                layout_custom_frame14.setVisibility(View.GONE);
                                layout_custom_frame15.setVisibility(View.VISIBLE);
                                layout_custom_frame16.setVisibility(View.GONE);

                                break;
                        case 14:

                                layout_custom_frame1.setVisibility(View.GONE);
                                layout_custom_frame2.setVisibility(View.GONE);
                                layout_custom_frame3.setVisibility(View.GONE);
                                layout_custom_frame4.setVisibility(View.GONE);
                                layout_custom_frame5.setVisibility(View.GONE);
                                layout_custom_frame6.setVisibility(View.GONE);
                                layout_custom_frame7.setVisibility(View.GONE);
                                layout_custom_frame8.setVisibility(View.GONE);
                                layout_custom_frame9.setVisibility(View.GONE);
                                layout_custom_frame10.setVisibility(View.GONE);
                                layout_custom_frame11.setVisibility(View.GONE);
                                layout_custom_frame12.setVisibility(View.GONE);
                                layout_custom_frame13.setVisibility(View.GONE);
                                layout_custom_frame14.setVisibility(View.GONE);
                                layout_custom_frame15.setVisibility(View.GONE);
                                layout_custom_frame16.setVisibility(View.VISIBLE);

                                break;

                }
                //  iv_customimage.setImageResource(item);
                adapterFrame.notifyDataSetChanged();
        }

        /* public void setbackgroundcolor(int color) {
             tv_mobilenumberlayout1.setText(color);
            // iv_customimage.setImageResource(color);
         }*/
        @RequiresApi(api = Build.VERSION_CODES.M)
        public void setTextbackgroundcolor(int colour) {
                tv_mobilenumberlayout1.setTextColor(context.getColor(colour));
                tv_mobilenumberlayout2.setTextColor(context.getColor(colour));
                tv_mobilenumberlayout3.setTextColor(context.getColor(colour));
                tv_mobilenumberlayout4.setTextColor(context.getColor(colour));
                tv_mobilenumberlayout5.setTextColor(context.getColor(colour));
                tv_mobilenumberlayout6.setTextColor(context.getColor(colour));
                tv_mobilenumberlayout01.setTextColor(context.getColor(colour));
                tv_mobilenumberlayout02.setTextColor(context.getColor(colour));
                tv_mobilenumberlayout03.setTextColor(context.getColor(colour));
                tv_mobilenumberlayout04.setTextColor(context.getColor(colour));
                tv_mobilenumberlayout05.setTextColor(context.getColor(colour));
                tv_mobilenumberlayout06.setTextColor(context.getColor(colour));
                tv_mobilenumberlayout07.setTextColor(context.getColor(colour));
                tv_mobilenumberlayout08.setTextColor(context.getColor(colour));

                tv_emaillayout1.setTextColor(context.getColor(colour));
                tv_emaillayout2.setTextColor(context.getColor(colour));
                tv_emaillayout3.setTextColor(context.getColor(colour));
                tv_emaillayout4.setTextColor(context.getColor(colour));
                tv_emaillayout5.setTextColor(context.getColor(colour));
                tv_emaillayout6.setTextColor(context.getColor(colour));
                tv_emaillayout8.setTextColor(context.getColor(colour));
                tv_emaillayout01.setTextColor(context.getColor(colour));
                tv_emaillayout02.setTextColor(context.getColor(colour));
                tv_emaillayout03.setTextColor(context.getColor(colour));
                tv_emaillayout04.setTextColor(context.getColor(colour));
                tv_emaillayout05.setTextColor(context.getColor(colour));
                tv_emaillayout06.setTextColor(context.getColor(colour));
                tv_emaillayout07.setTextColor(context.getColor(colour));
                tv_emaillayout08.setTextColor(context.getColor(colour));

                tv_websitelayout1.setTextColor(context.getColor(colour));
                tv_websitelayout2.setTextColor(context.getColor(colour));
                tv_websitelayout3.setTextColor(context.getColor(colour));
                tv_websitelayout4.setTextColor(context.getColor(colour));
                tv_websitelayout5.setTextColor(context.getColor(colour));
                tv_websitelayout6.setTextColor(context.getColor(colour));
                tv_websitelayout7.setTextColor(context.getColor(colour));
                tv_websitelayout8.setTextColor(context.getColor(colour));
                tv_websitelayout01.setTextColor(context.getColor(colour));
                tv_websitelayout02.setTextColor(context.getColor(colour));
                tv_websitelayout03.setTextColor(context.getColor(colour));
                tv_websitelayout04.setTextColor(context.getColor(colour));
                tv_websitelayout06.setTextColor(context.getColor(colour));
                tv_websitelayout07.setTextColor(context.getColor(colour));
                tv_websitelayout08.setTextColor(context.getColor(colour));

                tv_mobilenumberlayout1.setTextColor(context.getColor(colour));
                tv_mobilenumberlayout2.setTextColor(context.getColor(colour));
                tv_mobilenumberlayout3.setTextColor(context.getColor(colour));
                tv_mobilenumberlayout4.setTextColor(context.getColor(colour));
                tv_mobilenumberlayout5.setTextColor(context.getColor(colour));
                tv_mobilenumberlayout6.setTextColor(context.getColor(colour));
                tv_mobilenumberlayout01.setTextColor(context.getColor(colour));
                tv_mobilenumberlayout02.setTextColor(context.getColor(colour));
                tv_mobilenumberlayout03.setTextColor(context.getColor(colour));
                tv_mobilenumberlayout04.setTextColor(context.getColor(colour));
                tv_mobilenumberlayout05.setTextColor(context.getColor(colour));
                tv_mobilenumberlayout06.setTextColor(context.getColor(colour));
                tv_mobilenumberlayout07.setTextColor(context.getColor(colour));
                tv_mobilenumberlayout08.setTextColor(context.getColor(colour));

                iv_emaillayout1.setColorFilter(context.getColor(colour));
                iv_emaillayout2.setColorFilter(context.getColor(colour));
                iv_emaillayout3.setColorFilter(context.getColor(colour));
                iv_emaillayout4.setColorFilter(context.getColor(colour));
                iv_emaillayout5.setColorFilter(context.getColor(colour));
                iv_emaillayout6.setColorFilter(context.getColor(colour));
                iv_emaillayout8.setColorFilter(context.getColor(colour));
                iv_emaillayout01.setColorFilter(context.getColor(colour));
                iv_emaillayout02.setColorFilter(context.getColor(colour));
                iv_emaillayout03.setColorFilter(context.getColor(colour));
                iv_emaillayout04.setColorFilter(context.getColor(colour));
                iv_emaillayout05.setColorFilter(context.getColor(colour));
                iv_emaillayout06.setColorFilter(context.getColor(colour));
                iv_emaillayout07.setColorFilter(context.getColor(colour));
                iv_emaillayout08.setColorFilter(context.getColor(colour));

                iv_calllayout1.setColorFilter(context.getColor(colour));
                iv_calllayout2.setColorFilter(context.getColor(colour));
                iv_calllayout3.setColorFilter(context.getColor(colour));
                iv_calllayout4.setColorFilter(context.getColor(colour));
                iv_calllayout5.setColorFilter(context.getColor(colour));
                iv_calllayout6.setColorFilter(context.getColor(colour));
                iv_calllayout01.setColorFilter(context.getColor(colour));
                iv_calllayout02.setColorFilter(context.getColor(colour));
                iv_calllayout03.setColorFilter(context.getColor(colour));
                iv_calllayout04.setColorFilter(context.getColor(colour));
                iv_calllayout05.setColorFilter(context.getColor(colour));
                iv_calllayout06.setColorFilter(context.getColor(colour));
                iv_calllayout07.setColorFilter(context.getColor(colour));
                iv_calllayout08.setColorFilter(context.getColor(colour));

                iv_weblayout1.setColorFilter(context.getColor(colour));
                iv_weblayout2.setColorFilter(context.getColor(colour));
                iv_weblayout3.setColorFilter(context.getColor(colour));
                iv_weblayout4.setColorFilter(context.getColor(colour));
                iv_weblayout5.setColorFilter(context.getColor(colour));
                iv_weblayout6.setColorFilter(context.getColor(colour));
                iv_weblayout7.setColorFilter(context.getColor(colour));
                iv_weblayout8.setColorFilter(context.getColor(colour));
                iv_weblayout01.setColorFilter(context.getColor(colour));
                iv_weblayout02.setColorFilter(context.getColor(colour));
                iv_weblayout03.setColorFilter(context.getColor(colour));
                iv_weblayout04.setColorFilter(context.getColor(colour));
                iv_weblayout06.setColorFilter(context.getColor(colour));
                iv_weblayout07.setColorFilter(context.getColor(colour));
                iv_weblayout08.setColorFilter(context.getColor(colour));

        }

        public void calculationForHeight() {
                ViewTreeObserver vto = rl_content.getViewTreeObserver();
                vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                        @Override
                        public void onGlobalLayout() {
                                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
                                        rl_content.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                                } else {
                                        rl_content.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                                }
                                Constance.widthOfImage = rl_content.getMeasuredWidth();//1080 horizontalview
                                Constance.heightOfImage = rl_content.getMeasuredHeight();//236

                                ViewGroup.LayoutParams params = rl_content.getLayoutParams();
                                params.height = Constance.widthOfImage;
                                params.width = Constance.widthOfImage;
                                rl_content.setLayoutParams(params);
                        }
                });


        }

        private void getProfile() {

                Retrofit retrofit = RetrofitClient.getRetrofitInstance();
                UserDataApi api = retrofit.create(UserDataApi.class);
                Log.e("bbb1", "getProfile: " + id);
                Call<User> call = api.getUserData(Config.API_KEY, id);
                call.enqueue(new Callback<User>() {
                        @Override
                        public void onResponse(Call<User> call, Response<User> response) {
                                if (response.code() == 200) {
                                    if (response.body() != null) {

                                        User user = response.body();
                                        Log.e("bbbbbb", "onResponse: " + user.getWebsite() + "----" + response);
                                        if (user.getWebsite() != null) {
                                            tv_websitelayout1.setText("" + user.getWebsite());
                                            tv_websitelayout2.setText("" + user.getWebsite());
                                            tv_websitelayout3.setText("" + user.getWebsite());
                                            tv_websitelayout4.setText("" + user.getWebsite());
                                            tv_websitelayout5.setText("" + user.getWebsite());
                                            tv_websitelayout6.setText("" + user.getWebsite());
                                            tv_websitelayout7.setText("" + user.getWebsite());
                                            tv_websitelayout8.setText("" + user.getWebsite());
                                            tv_websitelayout01.setText("" + user.getWebsite());
                                            tv_websitelayout02.setText("" + user.getWebsite());
                                            tv_websitelayout03.setText("" + user.getWebsite());
                                            tv_websitelayout04.setText("" + user.getWebsite());
                                            tv_websitelayout06.setText("" + user.getWebsite());
                                            tv_websitelayout07.setText("" + user.getWebsite());
                                            tv_websitelayout08.setText("" + user.getWebsite());

                                        } else {
                                            tv_websitelayout1.setText("");
                                            tv_websitelayout2.setText("");
                                            tv_websitelayout3.setText("");
                                            tv_websitelayout4.setText("");
                                            tv_websitelayout5.setText("");
                                            tv_websitelayout6.setText("");
                                            tv_websitelayout7.setText("");
                                            tv_websitelayout8.setText("");
                                            tv_websitelayout01.setText("");
                                            tv_websitelayout02.setText("");
                                            tv_websitelayout03.setText("");
                                            tv_websitelayout04.setText("");
                                            tv_websitelayout06.setText("");
                                            tv_websitelayout07.setText("");
                                            tv_websitelayout08.setText("");

                                            iv_weblayout1.setVisibility(View.GONE);
                                            iv_weblayout2.setVisibility(View.GONE);
                                            iv_weblayout3.setVisibility(View.GONE);
                                            iv_weblayout4.setVisibility(View.GONE);
                                            iv_weblayout5.setVisibility(View.GONE);
                                            iv_weblayout6.setVisibility(View.GONE);
                                            iv_weblayout7.setVisibility(View.GONE);
                                            iv_weblayout8.setVisibility(View.GONE);
                                            iv_weblayout01.setVisibility(View.GONE);
                                            iv_weblayout02.setVisibility(View.GONE);
                                            iv_weblayout03.setVisibility(View.GONE);
                                            iv_weblayout04.setVisibility(View.GONE);
                                            iv_weblayout06.setVisibility(View.GONE);
                                            iv_weblayout07.setVisibility(View.GONE);
                                            iv_weblayout08.setVisibility(View.GONE);

                                        }
                                        if (user.getEmail() != null) {
                                            tv_emaillayout1.setText("" + user.getEmail());
                                            tv_emaillayout2.setText("" + user.getEmail());
                                            tv_emaillayout3.setText("" + user.getEmail());
                                            tv_emaillayout4.setText("" + user.getEmail());
                                            tv_emaillayout5.setText("" + user.getEmail());
                                            tv_emaillayout6.setText("" + user.getEmail());
                                            tv_emaillayout8.setText("" + user.getEmail());
                                            tv_emaillayout01.setText("" + user.getEmail());
                                            tv_emaillayout02.setText("" + user.getEmail());
                                            tv_emaillayout03.setText("" + user.getEmail());
                                            tv_emaillayout04.setText("" + user.getEmail());
                                            tv_emaillayout05.setText("" + user.getEmail());
                                            tv_emaillayout06.setText("" + user.getEmail());
                                            tv_emaillayout07.setText("" + user.getEmail());
                                            tv_emaillayout08.setText("" + user.getEmail());
                                        } else {
                                            tv_emaillayout1.setText("");
                                            tv_emaillayout2.setText("");
                                            tv_emaillayout3.setText("");
                                            tv_emaillayout4.setText("");
                                            tv_emaillayout5.setText("");
                                            tv_emaillayout6.setText("");
                                            tv_emaillayout8.setText("");
                                            tv_emaillayout01.setText("");
                                            tv_emaillayout02.setText("");
                                            tv_emaillayout03.setText("");
                                            tv_emaillayout04.setText("");
                                            tv_emaillayout05.setText("");
                                            tv_emaillayout06.setText("");
                                            tv_emaillayout07.setText("");
                                            tv_emaillayout08.setText("");

                                            iv_emaillayout1.setVisibility(View.GONE);
                                            iv_emaillayout2.setVisibility(View.GONE);
                                            iv_emaillayout3.setVisibility(View.GONE);
                                            iv_emaillayout4.setVisibility(View.GONE);
                                            iv_emaillayout5.setVisibility(View.GONE);
                                            iv_emaillayout6.setVisibility(View.GONE);
                                            iv_emaillayout8.setVisibility(View.GONE);
                                            iv_emaillayout01.setVisibility(View.GONE);
                                            iv_emaillayout02.setVisibility(View.GONE);
                                            iv_emaillayout03.setVisibility(View.GONE);
                                            iv_emaillayout04.setVisibility(View.GONE);
                                            iv_emaillayout05.setVisibility(View.GONE);
                                            iv_emaillayout06.setVisibility(View.GONE);
                                            iv_emaillayout07.setVisibility(View.GONE);
                                            iv_emaillayout08.setVisibility(View.GONE);
                                        }
                                        if (user.getAdress() != null) {
                                            tvLocation1.setText(user.getAdress());
                                            tvLocation2.setText(user.getAdress());
                                            tvLocation3.setText(user.getAdress());
                                        } else {
                                            tvLocation1.setText("");
                                            tvLocation2.setText("");
                                            tvLocation3.setText("");

                                            ivLocation1.setVisibility(View.GONE);
                                            ivLocation2.setVisibility(View.GONE);
                                            ivLocation3.setVisibility(View.GONE);
                                        }
                                        if (user.getPhone() != null) {
                                            tv_mobilenumberlayout1.setText("" + user.getPhone());
                                            tv_mobilenumberlayout2.setText("" + user.getPhone());
                                            tv_mobilenumberlayout3.setText("" + user.getPhone());
                                            tv_mobilenumberlayout4.setText("" + user.getPhone());
                                            tv_mobilenumberlayout5.setText("" + user.getPhone());
                                            tv_mobilenumberlayout6.setText("" + user.getPhone());
                                            tv_mobilenumberlayout01.setText("" + user.getPhone());
                                            tv_mobilenumberlayout02.setText("" + user.getPhone());
                                            tv_mobilenumberlayout03.setText("" + user.getPhone());
                                            tv_mobilenumberlayout04.setText("" + user.getPhone());
                                            tv_mobilenumberlayout05.setText("" + user.getPhone());
                                            tv_mobilenumberlayout06.setText("" + user.getPhone());
                                            tv_mobilenumberlayout07.setText("" + user.getPhone());
                                            tv_mobilenumberlayout08.setText("" + user.getPhone());

                                        } else {
                                            tv_mobilenumberlayout1.setText("");
                                            tv_mobilenumberlayout2.setText("");
                                            tv_mobilenumberlayout3.setText("");
                                            tv_mobilenumberlayout4.setText("");
                                            tv_mobilenumberlayout5.setText("");
                                            tv_mobilenumberlayout6.setText("");
                                            tv_mobilenumberlayout01.setText("");
                                            tv_mobilenumberlayout02.setText("");
                                            tv_mobilenumberlayout03.setText("");
                                            tv_mobilenumberlayout04.setText("");
                                            tv_mobilenumberlayout05.setText("");
                                            tv_mobilenumberlayout06.setText("");
                                            tv_mobilenumberlayout07.setText("");
                                            tv_mobilenumberlayout08.setText("");

                                            iv_calllayout1.setVisibility(View.GONE);
                                            iv_calllayout2.setVisibility(View.GONE);
                                            iv_calllayout3.setVisibility(View.GONE);
                                            iv_calllayout4.setVisibility(View.GONE);
                                            iv_calllayout5.setVisibility(View.GONE);
                                            iv_calllayout6.setVisibility(View.GONE);
                                            iv_calllayout01.setVisibility(View.GONE);
                                            iv_calllayout02.setVisibility(View.GONE);
                                            iv_calllayout03.setVisibility(View.GONE);
                                            iv_calllayout04.setVisibility(View.GONE);
                                            iv_calllayout05.setVisibility(View.GONE);
                                            iv_calllayout06.setVisibility(View.GONE);
                                            iv_calllayout07.setVisibility(View.GONE);
                                            iv_calllayout08.setVisibility(View.GONE);

                                        }
                                        if (user.getImageUrl() != null) {
                                            RequestOptions requestOptions = new RequestOptions();
                                            requestOptions.placeholder(R.drawable.userholder);
                                            requestOptions.error(R.drawable.userholder);
                                            Glide.with(context).load(user.getImageUrl()).apply(requestOptions).into(iv_logolayout1);
                                            Glide.with(context).load(user.getImageUrl()).apply(requestOptions).into(iv_logolayout2);
                                            Glide.with(context).load(user.getImageUrl()).apply(requestOptions).into(iv_logolayout3);
                                            Glide.with(context).load(user.getImageUrl()).apply(requestOptions).into(iv_logolayout4);
                                            Glide.with(context).load(user.getImageUrl()).apply(requestOptions).into(iv_logolayout5);
                                            Glide.with(context).load(user.getImageUrl()).apply(requestOptions).into(iv_logolayout6);
                                            Glide.with(context).load(user.getImageUrl()).apply(requestOptions).into(iv_logolayout7);
                                            Glide.with(context).load(user.getImageUrl()).apply(requestOptions).into(iv_logolayout8);
                                            Glide.with(context).load(user.getImageUrl()).apply(requestOptions).into(iv_logolayout01);
                                            Glide.with(context).load(user.getImageUrl()).apply(requestOptions).into(iv_logolayout02);
                                            Glide.with(context).load(user.getImageUrl()).apply(requestOptions).into(iv_logolayout03);
                                            Glide.with(context).load(user.getImageUrl()).apply(requestOptions).into(iv_logolayout04);
                                            Glide.with(context).load(user.getImageUrl()).apply(requestOptions).into(iv_logolayout05);
                                            Glide.with(context).load(user.getImageUrl()).apply(requestOptions).into(iv_logolayout06);
                                            Glide.with(context).load(user.getImageUrl()).apply(requestOptions).into(iv_logolayout07);
                                            Glide.with(context).load(user.getImageUrl()).apply(requestOptions).into(iv_logolayout08);

                                        } else {

                                        }


                                    }
                                }
                        }

                        @Override
                        public void onFailure(Call<User> call, Throwable t) {

                        }
                });


        }

}