package com.dexxaphotolab.app.Utills;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.appcompat.app.AlertDialog;

import com.dexxaphotolab.app.Activities.ActivityHome;
import com.dexxaphotolab.app.BuildConfig;
import com.dexxaphotolab.app.ModelRetrofit.CategoriesData;
import com.dexxaphotolab.app.R;
import java.io.File;
import java.util.ArrayList;

public class Constance {

    public static Activity context = ActivityHome.getInstance();
    public static String appName = context.getString(R.string.app_name);
    public static String CToken = "CToken";
    public static String FontStyle = "ABeeZee.otf";
    public static String language = "ABeeZee.otf";
    public static Bitmap createdBitmap;
    public static String savedVideoPath = "";
    public static String savedImagePath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + "/" + appName;
    public static boolean AllowToOpenAdvertise = false;
    public static boolean isFirstTimeOpen = true;
    public static int heightOfImage;
    public static int widthOfImage;
    public static String checkFragment = "";
    public static String ComeFrom = "";
    public static String activityName = "";
    public static String FromSinglecatActivity = "";
    public static String name_type;

    public static ArrayList<CategoriesData> childDataList = new ArrayList<>();
    public static ArrayList<File> mypostImageList = new ArrayList<>();
    public static ArrayList<File> mypostVideoList = new ArrayList<>();


    public static String FolderNameSave = "/"+ appName;
    public static File FileSaveDirectory = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + "/" + appName);
    public static File FileSaveVideoDirectory = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + "/" + appName);
    public static File SaveVideoDirectory = new File(Environment.getExternalStorageDirectory().toString() + "/Android/media/" + BuildConfig.APPLICATION_ID + "/" + appName + "/Media/video");
    public static String FolderNameShare = "/"+ appName;


    public static boolean isStickerTouch = false;
    public static boolean isStickerAvail = false;
    public static int selectedbackgroundcolor;
    public static String mobileNo = "8888444422";
    public static String emailId = "demo@gmail.com";

    public static String PolicyUrl = "https://www.google.com/";

    public static boolean isConnected(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
    }

    public static void privacyPolicy(Context context) {
        AlertDialog.Builder alert = new AlertDialog.Builder(context, R.style.AlertDialogCustom);
        alert.setTitle(R.string.app_name);
        final WebView wv = new WebView(context);
        wv.loadUrl(PolicyUrl);
        wv.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView webView, String url) {
                webView.loadUrl(url);
                return false;
            }

            @TargetApi(Build.VERSION_CODES.N)
            @Override
            public boolean shouldOverrideUrlLoading(WebView webView, WebResourceRequest request) {
                Uri uri = request.getUrl();
                webView.loadUrl(uri.toString());
                return false;
            }
        });
        alert.setView(wv);
        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
            }
        });
        alert.setPositiveButton("Agree", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
            }
        });
        alert.show();
    }


    public static String aboutUs = "Your content should be the voice of your brand. And, at Yasza Media, we make sure that we surpass your expectations while meeting your audience’s needs. We believe content which is engaging, relevant, and informative is the core to establishing your brand’s reputation online.\n" +
            "This is why we invest time in researching your target audience. We take into consideration several factors such as individual preferences, demographics, platform usage, and trends to build a content plan that yields results.\n" +
            "\n" +
            "Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book.\n" +
            "\n" +
            "It has survived not only five centuries, but also the leap into electronic typesetting, remaining essentially unchanged. It was popularised in the 1960s with the release of Letraset sheets containing Lorem Ipsum passages, and more recently with desktop publishing software like Aldus PageMaker including versions of Lorem Ipsum.";


    public static void deleteCache(Context context) {
        try {
            File dir = context.getCacheDir();
            deleteDir(dir);
        } catch (Exception e) {}
    }
    public static boolean deleteDir(File dir) {
        if (dir != null && dir.isDirectory()) {
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
            return dir.delete();
        } else if(dir!= null && dir.isFile()) {
            return dir.delete();
        } else {
            return false;
        }
    }

}
