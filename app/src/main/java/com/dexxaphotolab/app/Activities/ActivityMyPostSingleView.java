package com.dexxaphotolab.app.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.VideoView;

import com.bumptech.glide.Glide;
import com.dexxaphotolab.app.Adapters.PagerAdapterMySinglePostView;
import com.dexxaphotolab.app.AnalyticsApplication;
import com.dexxaphotolab.app.BuildConfig;
import com.dexxaphotolab.app.R;
import com.dexxaphotolab.app.Utills.Admanager;
import com.dexxaphotolab.app.Utills.Constance;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;

public class ActivityMyPostSingleView extends AppCompatActivity {

    Context context;
    private ArrayList<File> fileArrayList;
    PagerAdapterMySinglePostView pagerAdapterMySinglePostView;
    int mypostposition;

    Tracker mTracker;

    public Bitmap bitmapsave;
    public Date currentTime;
    public boolean isForShareGlobal;
    ProgressDialog dialog;
    String[] permissionsRequired = {Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE};
    private static final int PERMISSION_CALLBACK_CONSTANT = 200;
    public String type;
    VideoView vv_download_video;
    ImageView iv_download_image, iv_play_dv;
    MediaController mediacontroller;
    ProgressDialog pDialog;
    String selectedvideopath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_post_single_view);

        context = ActivityMyPostSingleView.this;
        bindView();

        new Admanager(this).loadBanner(ActivityMyPostSingleView.this, findViewById(R.id.banner_container));

        AnalyticsApplication application = (AnalyticsApplication) getApplication();
        mTracker = application.getDefaultTracker();

        fileArrayList = new ArrayList<>();
        type = getIntent().getExtras().getString("type");

        mypostposition = getIntent().getExtras().getInt("mypostposition", 0);

        if (type.equals("image")) {
            fileArrayList = Constance.mypostImageList;
            iv_play_dv.setVisibility(View.GONE);
            vv_download_video.setVisibility(View.GONE);
            iv_download_image.setVisibility(View.VISIBLE);
            Glide.with(context).load(fileArrayList.get(mypostposition).getAbsolutePath()).placeholder(R.drawable.placeholder).into(iv_download_image);
        } else {
            fileArrayList = Constance.mypostVideoList;
            iv_play_dv.setVisibility(View.VISIBLE);
            vv_download_video.setVisibility(View.VISIBLE);
            iv_download_image.setVisibility(View.VISIBLE);
            Glide.with(context).load(fileArrayList.get(mypostposition).getAbsolutePath()).placeholder(R.drawable.placeholder).into(iv_download_image);
            selectedvideopath = fileArrayList.get(mypostposition).getAbsolutePath();
            playVideo();
        }

        iv_play_dv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pDialog.dismiss();
                Log.d("wseqwqwqwqwqwqwqw", "sdss");
                vv_download_video.start();
                iv_play_dv.setVisibility(View.GONE);
                iv_download_image.setVisibility(View.GONE);

            }
        });
        vv_download_video.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            public void onPrepared(MediaPlayer mp) {
                pDialog.dismiss();
            }
        });
        vv_download_video.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            public void onCompletion(MediaPlayer mp) {
                if (pDialog.isShowing()) {
                    pDialog.dismiss();
                    mp.stop();
                    iv_play_dv.setVisibility(View.VISIBLE);
                    iv_download_image.setVisibility(View.VISIBLE);
                }
                vv_download_video.pause();
                iv_play_dv.setVisibility(View.VISIBLE);

            }
        });

    }


    @Override
    protected void onResume() {
        super.onResume();
        mTracker.setScreenName("Image~" + "Google Analytics Testing");
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());
    }

    public void bindView() {

        vv_download_video = findViewById(R.id.vv_download_video);
        iv_download_image = findViewById(R.id.iv_download_image);
        iv_play_dv = findViewById(R.id.iv_play_dv);
    }

    public void playVideo() {
        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Buffering...");
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(false);
        pDialog.show();
        mediacontroller = new MediaController(context);
        mediacontroller.setMediaPlayer(vv_download_video);
        vv_download_video.setMediaController(mediacontroller);
        vv_download_video.requestFocus();

        if (selectedvideopath != null) {
            vv_download_video.setVideoPath(selectedvideopath);
        } else {
            Toast.makeText(context, "Not get the video path", Toast.LENGTH_LONG).show();
        }


    }


    public void onclickMyPostSingleView(View view) {
        switch (view.getId()) {
            case R.id.ll_delete:
                dailogdelete();
                break;
            case R.id.ll_share:
                if (type.equals("image")) {
                    shareImage(context, fileArrayList.get(mypostposition).getPath());

                } else {
                    shareVideo(context, fileArrayList.get(mypostposition).getPath());
                }

                break;
            case R.id.iv_backarrow:
                onBackPressed();
                break;
        }
    }

    public static void shareImage(Context context, String filePath) {
        try {
            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.putExtra(Intent.EXTRA_TEXT, "download this Digital poster maker app  https://play.google.com/store/apps/details?id=" + context.getPackageName());
            String path = MediaStore.Images.Media.insertImage(context.getContentResolver(), filePath, "", null);
            Uri screenshotUri = Uri.parse(path);
            intent.putExtra(Intent.EXTRA_STREAM, screenshotUri);
            intent.setType("image/*");
            context.startActivity(Intent.createChooser(intent, "Share File via..."));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void shareVideo(Context context, String path) {

        Intent share = new Intent(Intent.ACTION_SEND);
        Uri uri = FileProvider.getUriForFile(context, BuildConfig.APPLICATION_ID+".provider", new File(path));
        share.setType("Video/*");
        share.putExtra(Intent.EXTRA_STREAM, uri);
        startActivity(Intent.createChooser(share, "Share via"));
    }

    public void dailogdelete() {
        final android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(context);
        final android.app.AlertDialog alertDialog = builder.create();
        builder.setMessage("Do you want to delete?")
                .setCancelable(false)
                .setTitle("Delete ?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        File fdelete = new File(fileArrayList.get(mypostposition).getPath());
                        if (fdelete.exists()) {
                            if (fdelete.delete()) {
                                Toast.makeText(context, "File Succesfully deleted ", Toast.LENGTH_LONG).show();
                                context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(new File(fileArrayList.get(mypostposition).getPath()))));

                            } else {
                                Toast.makeText(context, "File not deleted ", Toast.LENGTH_LONG).show();
                            }
                        }
                        dialog.dismiss();

                        if (fileArrayList.size() == 0) {
                            androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(context);
                            builder.setMessage("No Data Found")
                                    .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            dialog.cancel();
                                            onBackPressed();
                                        }
                                    });

                            androidx.appcompat.app.AlertDialog alert = builder.create();
                            alert.show();
                        } else {
                        }

                        finish();
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