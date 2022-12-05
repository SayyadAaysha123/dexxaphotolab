package com.dexxaphotolab.app.Utills;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;

import com.dexxaphotolab.app.Activities.ActivitySplashScreen;
import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.google.android.gms.ads.nativead.NativeAd;

import org.jetbrains.annotations.NotNull;

import static android.content.Context.MODE_PRIVATE;

public class Admanager {

    static InterstitialAd interstitialAd;
    Context context;
    String TAG = "nnnnnnnnn";
    private NativeAd nativeAd;

    public Admanager(Context cc) {

        context = cc;

    }

    public void loadBanner(Activity cc, LinearLayout ll) {
        if(ActivitySplashScreen.adsEnable.equals("1")) {
            SharedPreferences prefsss = cc.getSharedPreferences("subscibe11", MODE_PRIVATE);
            String substatus = prefsss.getString("subscribe", "0");

            if (substatus.equals("0")) {

                AdView adView = new AdView(cc);
                adView.setAdUnitId(ActivitySplashScreen.Google_banner);
                ll.removeAllViews();
                ll.addView(adView);

                AdSize adSize = getAdSize(cc, ll);
                adView.setAdSize(adSize);

                AdRequest adRequest = new AdRequest.Builder().build();

                adView.loadAd(adRequest);

            }
        }



    }

    private AdSize getAdSize(Activity cc, LinearLayout ll) {
        Display display = cc.getWindowManager().getDefaultDisplay();
        DisplayMetrics outMetrics = new DisplayMetrics();
        display.getMetrics(outMetrics);

        float density = outMetrics.density;

        float adWidthPixels = ll.getWidth();

        if (adWidthPixels == 0) {
            adWidthPixels = outMetrics.widthPixels;
        }

        int adWidth = (int) (adWidthPixels / density);
        return AdSize.getCurrentOrientationAnchoredAdaptiveBannerAdSize(cc, adWidth);
    }

    public void loadAd() {
        AdRequest adRequest = new AdRequest.Builder().build();
        Log.e("0000", "loadAd: " + ActivitySplashScreen.Google_interstitial);
        InterstitialAd.load(context, ActivitySplashScreen.Google_interstitial, adRequest, new InterstitialAdLoadCallback() {
            @Override
            public void onAdLoaded(@NonNull InterstitialAd inter) {
                super.onAdLoaded(inter);
                Log.e(TAG, "onAdFailedToLoad: " + "2222");
                interstitialAd = inter;

            }

            @Override
            public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                super.onAdFailedToLoad(loadAdError);

                interstitialAd = null;
                Log.e(TAG, "onAdFailedToLoad: " + "1111");
                String error =
                        String.format(
                                "domain: %s, code: %d, message: %s",
                                loadAdError.getDomain(), loadAdError.getCode(), loadAdError.getMessage());

            }
        });
    }

    public void showInterstitial(Activity activity, final GetBackPointer getBackPointer) {
        if(ActivitySplashScreen.adsEnable.equals("1")) {
            SharedPreferences prefs = context.getSharedPreferences("click_counter", MODE_PRIVATE);
            int cc_count = prefs.getInt("count", 0);

            cc_count = cc_count + 1;
            int ads_click = Integer.parseInt(ActivitySplashScreen.ads_click_counrter);

            SharedPreferences.Editor editor11 = activity.getSharedPreferences("click_counter", MODE_PRIVATE).edit();
            editor11.putInt("count", cc_count);
            editor11.apply();

            SharedPreferences prefsss = activity.getSharedPreferences("subscibe11", MODE_PRIVATE);
            String substatus = prefsss.getString("subscribe", "0");

            if (substatus.equals("0")) {
                if (ads_click <= cc_count) {

                    editor11.putInt("count", 0);
                    editor11.apply();

                    if (interstitialAd != null) {
                        interstitialAd.show(activity);

                        interstitialAd.setFullScreenContentCallback(new FullScreenContentCallback() {
                            @Override
                            public void onAdFailedToShowFullScreenContent(@NonNull @NotNull AdError adError) {
                                super.onAdFailedToShowFullScreenContent(adError);

                                if (getBackPointer != null) {
                                    getBackPointer.returnAction();
                                }
                                Log.d(TAG, "onAdFailedToShowFullScreenContent: " + adError.getMessage());
                            }

                            @Override
                            public void onAdShowedFullScreenContent() {
                                super.onAdShowedFullScreenContent();
                                Log.d(TAG, "onAdShowedFullScreenContent: ");

                            }

                            @Override
                            public void onAdDismissedFullScreenContent() {
                                super.onAdDismissedFullScreenContent();
                                Log.d(TAG, "onAdDismissedFullScreenContent: ");

                                if (getBackPointer != null) {
                                    getBackPointer.returnAction();
                                }
                            }

                            @Override
                            public void onAdImpression() {
                                super.onAdImpression();
                                Log.d(TAG, "onAdImpression: ");
                            }
                        });
                    } else {
                        Log.e(TAG, "onAdFailedToLoad: " + "5555");
                        if (getBackPointer != null) {
                            getBackPointer.returnAction();
                        }

                    }


                } else {
                    if (getBackPointer != null) {
                        getBackPointer.returnAction();
                    }
                }

            } else {
                if (getBackPointer != null) {
                    getBackPointer.returnAction();
                }
            }
        } else {
            if (getBackPointer != null) {
                getBackPointer.returnAction();
            }
        }
    }

    public interface GetBackPointer {
        public void returnAction();
    }
}
