package com.dexxaphotolab.app.Activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.media.MediaPlayer;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.PowerManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.dexxaphotolab.app.ModelRetrofit.VideoCategoriesData;
import com.dexxaphotolab.app.Adapters.AdapterCategoryList;
import com.dexxaphotolab.app.Adapters.AdapterFrames;
import com.dexxaphotolab.app.Adapters.AdapterSingleCatVideoList;
import com.dexxaphotolab.app.AnalyticsApplication;
import com.dexxaphotolab.app.BuildConfig;
import com.dexxaphotolab.app.Model.SubCategoryModel;
import com.dexxaphotolab.app.R;
import com.dexxaphotolab.app.Utills.Constance;
import com.dexxaphotolab.app.Utills.SharedPrefrenceConfig;
import com.dexxaphotolab.app.payment.Config;
import com.dexxaphotolab.app.payment.Network.RetrofitClient;
import com.dexxaphotolab.app.payment.Network.apis.UserDataApi;
import com.dexxaphotolab.app.payment.Network.models.User;
import com.dexxaphotolab.app.payment.Utils.PreferenceUtils;
import com.dexxaphotolab.app.payment.activity.LoginActivity;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;

import cn.xm.weidongjian.popuphelper.PopupWindowHelper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class ActivitySingleVideoList extends AppCompatActivity {

    Context context;
    AdView mAdView;
    LinearLayout facbook_ad_banner;
    RecyclerView rv_singlevideoitems;
    ArrayList<SubCategoryModel> cat1_list = new ArrayList<>();
    TextView tv_singlevideocatname;
    String childitemtittle, catnameid, videoUrl;
    ImageView iv_backarrow, iv_al_language;
    VideoView vv_playvideo;
    LinearLayout ll_selectedvideo, ll_next_singlevideo;
    SharedPrefrenceConfig sharedPrefrenceConfig;
    MediaController mediacontroller;

    String selectedvideopath, language;
    ArrayList<VideoCategoriesData> videoCategoriesDataArrayList;
    public AdapterSingleCatVideoList adapterSingleCatVideoList;
    boolean df = false;
    RelativeLayout rl_frame;
    int frame;
    ProgressBar pb_video;
    public AdapterFrames adapterFrame;
    Tracker mTracker;
    public PopupWindowHelper popupWindowHelper;
    View popupview_down;
    ProgressDialog mProgressDialog;
    String id;
    String frameName[] = {"All", "English", "Hindi", "Gujarati"};
    String location, folder;


    public static ActivitySingleVideoList instance = null;

    public ActivitySingleVideoList() {
        instance = ActivitySingleVideoList.this;
    }

    public static synchronized ActivitySingleVideoList getInstance() {
        if (instance == null) {
            instance = new ActivitySingleVideoList();
        }
        return instance;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_video_list);

        context = ActivitySingleVideoList.this;
        bindView();
        sharedPrefrenceConfig = new SharedPrefrenceConfig(context);
        language = sharedPrefrenceConfig.getPrefString(context, Constance.language, "");

        id = PreferenceUtils.getUserId(ActivitySingleVideoList.this);

        childitemtittle =  getIntent().getExtras().getString("childitemtittle");
        catnameid = getIntent().getExtras().getString("catnameid");
        videoUrl = getIntent().getExtras().getString("video");

        selectedvideopath = videoUrl;

        location = Environment.getExternalStorageDirectory().toString() + "/Android/media/" + getPackageName() + "/" + getString(R.string.app_name) + "/Media/video/" + childitemtittle +".mp4";

        folder = Environment.getExternalStorageDirectory().toString() + "/Android/media/" + getPackageName() + "/" + getString(R.string.app_name) + "/Media/video";

          if (!Constance.SaveVideoDirectory.exists()) {

              Constance.SaveVideoDirectory.mkdirs();

        } else {
              File[] listFiles = Constance.SaveVideoDirectory.listFiles();

              for(File listFile : listFiles) {
                  long diff = new Date().getTime() - new File(location).lastModified();
                  long cutoff = (7 * (24 * 60 * 60 * 1000));

                  if (diff > cutoff) {
                      listFile.delete();
                  }

              }
          }


        if(new File(location).exists()) {
            Toast.makeText(context, "Already Downloaded", Toast.LENGTH_SHORT).show();

            playVideo();
//            Intent i = new Intent(context, ActivityCreatePost.class);
//            i.putExtra("videoName", childitemtittle);
//            startActivity(i);
        } else {
            mProgressDialog = new ProgressDialog(context);
            mProgressDialog.getWindow().setBackgroundDrawable(context.getResources().getDrawable(R.drawable.gredient_dialog));
            mProgressDialog.setMessage("Video Downloading...");
            mProgressDialog.setIndeterminate(false);
            mProgressDialog.setCancelable(false);
            mProgressDialog.setMax(100);
            mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            ActivitySingleVideoList.DownloadTask downloadFile = new ActivitySingleVideoList.DownloadTask(context);
            downloadFile.execute(selectedvideopath);

            TextView tv1 = (TextView) mProgressDialog.findViewById(android.R.id.message);
            tv1.setTextSize(16);
            tv1.setTextColor(context.getResources().getColor(R.color.colorBlack));
        }


        Log.e("555555", "onCreate: "+ videoUrl + "----> " + childitemtittle);

        calculationForHeight();
//        videoApi();

        AnalyticsApplication application = (AnalyticsApplication) getApplication();
        mTracker = application.getDefaultTracker();




//        playVideo();
        tv_singlevideocatname.setText(childitemtittle);
        rv_singlevideoitems.setLayoutManager(new GridLayoutManager(context, 3));

      /*  frameListAdepter = new FrameListAdepter(context, arrayList);
        rv_frame_list.setLayoutManager(new GridLayoutManager(context, 3));
        rv_frame_list.setAdapter(frameListAdepter);
        setFrame(arrayList.get(0));
        frame = arrayList.get(0);
        Log.d("frsrsgh", "" + frame);*/


        videoCategoriesDataArrayList = new ArrayList<>();

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

                AdapterCategoryList adapterCategory = new AdapterCategoryList(context, frameName, "video");
                rv_cat_dd_raw.setAdapter(adapterCategory);
            }
        });

        iv_backarrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        ll_next_singlevideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Constance.ComeFrom = "video";
                Constance.FromSinglecatActivity = selectedvideopath;
                if (PreferenceUtils.isLoggedIn(ActivitySingleVideoList.this)) {

                    Intent i = new Intent(context, ActivityCreatePost.class);
                    i.putExtra("videoName", childitemtittle);
                    startActivity(i);

              /*  i.putExtra("FromSinglecatActivity", selectedvideopath);
                i.putExtra("ComeFrom", "video");*/

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
                    //Creating dialog box
                    AlertDialog alert = builder.create();
                    alert.show();
                }

              /*  pb_video.setVisibility(View.VISIBLE);
                rl_frame.setDrawingCacheEnabled(true);
                rl_frame.buildDrawingCache(true);


                Bitmap savedBmp = viewToBitmap(rl_frame);

                Bitmap newsaveBmp = scaleBitmap(savedBmp, 1000, 1000);
                try {
                    //Write file
                   *//* String root = Environment.getExternalStorageDirectory()
                            .toString();
                    File myDir = new File(root + "/kiki");
                    myDir.mkdirs();*//*

                    if (!Constance.FileSaveVideoDirectory.exists()) {
                        Constance.FileSaveVideoDirectory.mkdir();
                    }
                    File file = new File(Constance.FileSaveVideoDirectory, "demoImage.png");

                    if (file.exists())
                        file.delete();
                    try {
                        FileOutputStream out = new FileOutputStream(file);
                        newsaveBmp.compress(Bitmap.CompressFormat.PNG, 100, out);
                        out.flush();
                        out.close();
                        dwonloadVideo();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                Log.d("dfjhfh", "hfgd" + selectedvideopath);*/

            }
        });
    }


    public void bindView() {
        rv_singlevideoitems = findViewById(R.id.rv_singlevideoitems);
        tv_singlevideocatname = findViewById(R.id.tv_singlevideocatname);
        iv_backarrow = findViewById(R.id.iv_backarrow);
       /* iv_firstimage = findViewById(R.id.iv_firstimage);
        ll_next_singlecatlist = findViewById(R.id.ll_next_singlecatlist);*/
        ll_selectedvideo = findViewById(R.id.ll_selectedvideo);
        vv_playvideo = findViewById(R.id.iv_videoshow);
//        iv_vp_play = findViewById(R.id.iv_vp_play);
        ll_next_singlevideo = findViewById(R.id.ll_next_singlevideo);
        rl_frame = findViewById(R.id.rl_frame);
        pb_video = findViewById(R.id.pb_video);
        iv_al_language = findViewById(R.id.iv_al_language);


    }

    private void getProfile() {
        Retrofit retrofit = RetrofitClient.getRetrofitInstance();
        UserDataApi api = retrofit.create(UserDataApi.class);
        Call<User> call = api.getUserData(Config.API_KEY, id);
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.code() == 200) {
                    if (response.body() != null) {
                        User user = response.body();

//                        Glide.with(Business_DetailActivity1.this)
//                                .load(user.getImageUrl())
//                                .into(userIv);

                    }
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
            }
        });


    }

    public void loadList() {

        language = sharedPrefrenceConfig.getPrefString(context, Constance.language, "");
        if (Constance.ComeFrom.equals("festivalviewall")) {
            Log.d("dfdfsfd", "dfdfsfd1");
        } else {
//            getBusinessVideolist();
        }
    }


    public void playVideo() {

                vv_playvideo.start();


      /*  try {
            mediacontroller = new MediaController(this);
            mediacontroller.setAnchorView(vv_playvideo);
            vv_playvideo.setMediaController(mediacontroller);
            vv_playvideo.setVideoPath(videoCategoriesDataArrayList.get(0).getVideo_url());

        } catch (Exception e) {
            e.printStackTrace();
        }*/
        if (selectedvideopath != null) {
            if(new File(location).exists()) {
                vv_playvideo.setVideoPath(Constance.SaveVideoDirectory + "/" + childitemtittle + ".mp4");
            } else {
                vv_playvideo.setVideoPath(selectedvideopath);
            }
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
//                pDialog.dismiss();
                vv_playvideo.start();
            }
        });
        vv_playvideo.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            public void onCompletion(MediaPlayer mp) {
//                if (pDialog.isShowing()) {
//                    pDialog.dismiss();
//                    mp.stop();
//                    iv_vp_play.setVisibility(View.VISIBLE);
//
//                }
                //mp.stop();
                vv_playvideo.pause();
//                iv_vp_play.setVisibility(View.VISIBLE);

            }
        });
    }



    public void calculationForHeight() {
        ViewTreeObserver vto = ll_selectedvideo.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
                    ll_selectedvideo.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                } else {
                    ll_selectedvideo.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                }
                int heightOfImage;
                int widthOfImage;
                widthOfImage = ll_selectedvideo.getMeasuredWidth();//1080 horizontalview
                heightOfImage = ll_selectedvideo.getMeasuredHeight();//236

                ViewGroup.LayoutParams params = ll_selectedvideo.getLayoutParams();
                params.height = widthOfImage;
                params.width = widthOfImage;
                ll_selectedvideo.setLayoutParams(params);
            }
        });


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
        // m.setScale((float) wantedWidth / bitmap.getWidth(), (float) wantedHeight / bitmap.getHeight());
        Paint paint = new Paint();
        paint.setFilterBitmap(true);
        canvas.drawBitmap(bitmap, m, paint);
        return output;
    }

    public Bitmap viewToBitmap(View view) {
        Bitmap bitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        view.draw(canvas);
        return bitmap;
    }

    @Override
    protected void onResume() {
        super.onResume();
        mTracker.setScreenName("Image~" + "Google Analytics Testing");
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());
//        iv_vp_play.setVisibility(View.VISIBLE);
    }

    public void setselectedvideo(String video_url) {
        selectedvideopath = video_url;
        playVideo();
    }

    public void setFrame(int frame1) {
        rl_frame.setBackgroundResource(frame1);
        Log.d("frsrsgh", "" + frame);
        frame = frame1;
    }


    private class DownloadTask extends AsyncTask<String, Integer, String> {

        private Context context;
        private PowerManager.WakeLock mWakeLock;

        public DownloadTask(Context context) {
            this.context = context;
        }

        @Override
        protected String doInBackground(String... sUrl) {
            InputStream input = null;
            OutputStream output = null;
            HttpURLConnection connection = null;
            try {

                URL url = new URL(sUrl[0]);
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();

                if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                    return "Server returned HTTP " + connection.getResponseCode()
                            + " " + connection.getResponseMessage();
                }

                // this will be useful to display download percentage
                // might be -1: server did not report the length
                int fileLength = connection.getContentLength();

                // download the file
                input = connection.getInputStream();


                Log.e("888888", "doInBackground: " + location );


                File file = new File(location);
                if (!file.exists())
                    file.createNewFile();

                output = new FileOutputStream(file);

                byte data[] = new byte[4096];
                long total = 0;
                int count;
                while ((count = input.read(data)) != -1) {
                    // allow canceling with back button
                    if (isCancelled()) {
                        input.close();
                        return null;
                    }
                    total += count;
                    // publishing the progress....
                    if (fileLength > 0) // only if total length is known
                        publishProgress((int) (total * 100 / fileLength));
                    output.write(data, 0, count);
                }
            } catch (Exception e) {
                return e.toString();
            } finally {
                try {
                    if (output != null)
                        output.close();
                    if (input != null)
                        input.close();
                } catch (IOException ignored) {
                    ignored.getStackTrace();
                }

                if (connection != null)
                    connection.disconnect();
            }
            return null;
        }


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // take CPU lock to prevent CPU from going off if the user
            // presses the power button during download
            PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
            mWakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK,
                    getClass().getName());
            mWakeLock.acquire();

            if (mProgressDialog != null && !mProgressDialog.isShowing())
                mProgressDialog.show();


        }

        @Override
        protected void onProgressUpdate(Integer... progress) {
            super.onProgressUpdate(progress);
            // if we get here, length is known, now set indeterminate to false
            mProgressDialog.setIndeterminate(false);
            mProgressDialog.setMax(100);
            mProgressDialog.setProgress(progress[0]);
        }

        @Override
        protected void onPostExecute(String result) {
            mWakeLock.release();

            if (mProgressDialog != null && mProgressDialog.isShowing()) {
                mProgressDialog.dismiss();

            }


            try {
                if (Build.VERSION.SDK_INT >= 19) {
                    MediaScannerConnection.scanFile(context, new String[]{new File(location).getAbsolutePath()},
                            null, (path, uri) -> {
                            });
                } else {

                    Uri uri = FileProvider.getUriForFile(context, BuildConfig.APPLICATION_ID + ".provider", new File(location));

                    context.sendBroadcast(new Intent("android.intent.action.MEDIA_MOUNTED", uri));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            scanMedia(location);


            if (result != null){
                Toast.makeText(context, "Download error: " + result, Toast.LENGTH_LONG).show();
            Log.e("888888", "onPostExecute: " + result );}
            else
                Toast.makeText(context, "Downloaded", Toast.LENGTH_SHORT).show();
            playVideo();
        }


    }
    private void scanMedia(String path) {
        File file = new File(path);

        Uri uri = FileProvider.getUriForFile(context, BuildConfig.APPLICATION_ID + ".provider", file);

        Intent scanFileIntent = new Intent(
                Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, uri);
        context.sendBroadcast(scanFileIntent);
    }


/*
    private void videoApi() {
        Log.e("555555", "Api: "+ catnameid );
        StringRequest request = new StringRequest(Request.Method.GET, BASE_URL + "single_details?type=" + "tvseries" + "&id=" + catnameid,
                new com.android.volley.Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
//                        Log.e("000000", "onResponse: " + catid);

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
                                    String fileUrl = obj2.optString("file_url");


                                    SubCategoryModel bean1 = new SubCategoryModel(false);
                                    bean1.setSubcatImg(channelImg);
                                    bean1.setSubcatName(channelNamne);
                                    bean1.setLanguage(language);
                                    bean1.setPaid(paid);
                                    bean1.setFileUrl(fileUrl);
                                    cat1_list.add(bean1);
                                }


                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.e("1234", "onResponse: " + e.getMessage() );
                        }

//                        adapter = new SubCategoryVideoAdapter(getApplicationContext(), cat1_list, ActivitySingleVideoList.this);
//                        rv_singlevideoitems.setAdapter(adapter);
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
*/


}