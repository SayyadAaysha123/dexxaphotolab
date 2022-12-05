package com.dexxaphotolab.app.payment.Utils;


import com.dexxaphotolab.app.payment.Network.RetrofitClient;
import com.dexxaphotolab.app.payment.Config;

public class ApiResources {

    public static String CURRENCY; // must be valid currency code
    public static String EXCHSNGE_RATE;
    public static String PAYPAL_CLIENT_ID;
    public static String RAZORPAY_EXCHANGE_RATE;
    public static String USER_PHONE;


    String URL = Config.API_SERVER_URL + RetrofitClient.API_URL_EXTENSION;
    String searchUrl = URL+"search";

    String getAllReply = URL+"all_replay";
    String termsURL = Config.TERMS_URL;

    public String getTermsURL() {
        return termsURL;
    }

    public String getGetAllReply() {
        return getAllReply;
    }

    public String getSearchUrl() {
        return searchUrl;
    }


    }


