package com.dexxaphotolab.app.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;

import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;
import android.widget.VideoView;

import com.arthenica.mobileffmpeg.Config;
import com.arthenica.mobileffmpeg.FFmpeg;
import com.dexxaphotolab.app.AnalyticsApplication;
import com.dexxaphotolab.app.R;
import com.dexxaphotolab.app.Utills.Constance;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

import java.io.File;

public class ActivityVideoPreview extends AppCompatActivity {

    Context context;
    LinearLayout ll_content;
    final private int REQUEST_CODE_ASK_PERMISSIONS = 123;
    String filePath = "";
    String selectedvideopath;
    VideoView vv_playvideo;
    MediaController mediacontroller;
    ProgressDialog pDialog;
    ImageView iv_vp_play;
    RelativeLayout rl_frame;
    int framepath;
    ProgressBar pb_video_preview;
    Tracker mTracker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_preview);

        context = ActivityVideoPreview.this;
        bindView();

        AnalyticsApplication application = (AnalyticsApplication) getApplication();
        mTracker = application.getDefaultTracker();

        calculationForHeight();
        selectedvideopath = getIntent().getExtras().getString("selectedvideopath");
        framepath = getIntent().getExtras().getInt("framepath");
        Log.d("frsrsgh", "" + framepath);
        rl_frame.setBackgroundResource(framepath);
        Log.d("checkvideo", "2: " + selectedvideopath);

        requestPermission();
        playVideo();
    }

    public void bindView() {

        ll_content = findViewById(R.id.ll_content);
        iv_vp_play = findViewById(R.id.iv_vp_play);
        vv_playvideo = findViewById(R.id.vv_playvideo);
        rl_frame = findViewById(R.id.rl_frame);
        pb_video_preview = findViewById(R.id.pb_video_preview);

    }

    @Override
    protected void onResume() {
        super.onResume();
        mTracker.setScreenName("Image~" + "Google Analytics Testing");
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());
    }

    public void onclickVideoPreview(View view) {
        switch (view.getId()) {
            case R.id.iv_backarrow:
                onBackPressed();
                break;
            case R.id.ll_share:
                shareVideo(context, selectedvideopath);
                break;
            case R.id.ll_gotohome:
                startActivity(new Intent(context, ActivityHome.class));
                break;
            case R.id.ll_download:
                pb_video_preview.setVisibility(View.VISIBLE);
                new AsyncTaskExampleNew().execute();
                break;

        }
    }

    public void playVideo() {
        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Buffering...");
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(false);
        pDialog.show();
        iv_vp_play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pDialog.dismiss();
                Log.d("wseqwqwqwqwqwqwqw", "sdss");
                vv_playvideo.start();
                iv_vp_play.setVisibility(View.GONE);
            }
        });

        if (selectedvideopath != null) {
            vv_playvideo.setVideoPath(selectedvideopath);
        } else {
            Toast.makeText(context, "Not get the video path", Toast.LENGTH_LONG).show();
        }
        mediacontroller = new MediaController(context);
        mediacontroller.setMediaPlayer(vv_playvideo);
        vv_playvideo.setMediaController(mediacontroller);
        vv_playvideo.requestFocus();
        //  vv_playvideo.start();
        vv_playvideo.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            public void onPrepared(MediaPlayer mp) {
                pDialog.dismiss();
                //vv_playvideo.start();
            }
        });
        vv_playvideo.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            public void onCompletion(MediaPlayer mp) {
                if (pDialog.isShowing()) {
                    pDialog.dismiss();
                    mp.stop();
                    iv_vp_play.setVisibility(View.VISIBLE);

                }
                vv_playvideo.pause();
                iv_vp_play.setVisibility(View.VISIBLE);

            }
        });
    }

    public static void shareVideo(Context context, String filePath) {
        Uri mainUri = Uri.parse(filePath);
        Intent sharingIntent = new Intent(Intent.ACTION_SEND);
        sharingIntent.setType("video/mp4");
        sharingIntent.putExtra(Intent.EXTRA_STREAM, mainUri);
        sharingIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        try {
            context.startActivity(Intent.createChooser(sharingIntent, "Share Video using"));
        } catch (ActivityNotFoundException e) {
            Toast.makeText(context, "Application not found to open this file", Toast.LENGTH_LONG).show();
        }
    }


    private void requestPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat
                    .requestPermissions(ActivityVideoPreview.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_CODE_ASK_PERMISSIONS);
        } else {

        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE_ASK_PERMISSIONS:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(context, "Permission Granted", Toast.LENGTH_SHORT)
                            .show();
                } else {
                    Toast.makeText(context, "Permission Denied", Toast.LENGTH_SHORT)
                            .show();
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    public void calculationForHeight() {
        ViewTreeObserver vto = ll_content.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
                    ll_content.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                } else {
                    ll_content.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                }
                int heightOfImage;
                int widthOfImage;
                widthOfImage = ll_content.getMeasuredWidth();//1080 horizontalview
                heightOfImage = ll_content.getMeasuredHeight();//236

                ViewGroup.LayoutParams params = ll_content.getLayoutParams();
                params.height = widthOfImage;
                params.width = widthOfImage;
                ll_content.setLayoutParams(params);
            }
        });


    }

    public void saveVideo() {

    }

    class AsyncTaskExampleNew extends AsyncTask<Void, Void, Exception> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Exception e) {
            pb_video_preview.setVisibility(View.GONE);

            Toast.makeText(context, "Video Saved Successfully", Toast.LENGTH_SHORT)
                    .show();
            super.onPostExecute(e);
        }


        @Override
        protected Exception doInBackground(Void... voids) {
            try {
                saveVideoToInternalStorage();
            } catch (Exception e) {
            }
            return null;
        }
    }


    void saveVideoToInternalStorage() {
        File musicDirectory = new
                File(Constance.FileSaveVideoDirectory.getPath());
        musicDirectory.mkdirs();
        musicDirectory.mkdir();

        String inputCode1[] = new String[]{

                "-i",
                "/storage/emulated/0/"+ getString(R.string.app_name) + "/VideoDemo.mp4",
                "-i",
                "/storage/emulated/0/" + getString(R.string.app_name) + "/demoImage.png",
                "-filter_complex",
                "overlay=(W-w):(H-h)",
                "-codec:a",
                "copy",
                Constance.FileSaveVideoDirectory.getPath() + "/new_video_create.mp4"

        };


        int rc = FFmpeg.execute(inputCode1);

        if (rc == Config.RETURN_CODE_SUCCESS) {
            Log.i(Config.TAG, "Command execution completed successfully.");

        } else if (rc == Config.RETURN_CODE_CANCEL) {
            Log.i(Config.TAG, "Command execution cancelled by user.");

        } else {
            Log.i(
                    Config.TAG, String.format(
                            "Command execution failed with rc=%d and the output below.",
                            rc
                    )
            );
            Config.printLastCommandOutput(Log.INFO);
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

            sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse(Constance.FileSaveVideoDirectory.getPath() + "/new_video_create.mp4")));

        } else {
            sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse(Constance.FileSaveVideoDirectory.getPath() + "/new_video_create.mp4")));

        }
        MediaScannerConnection.scanFile(this,
                new String[]{Constance.FileSaveVideoDirectory.getPath() + "/new_video_create.mp4"},
                new String[]{"video/mp4"},
                new MediaScannerConnection.OnScanCompletedListener() {
                    public void onScanCompleted(String path, Uri uri) {
                        Log.i("ExternalStorage", "Scanned " + path + ":");
                        Log.i("ExternalStorage", "-> uri=" + uri);
                    }
                });

    }
}