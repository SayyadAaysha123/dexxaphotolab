package com.dexxaphotolab.app.payment.Network;

import android.util.Log;

import java.io.IOException;

import okhttp3.Credentials;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class BasicAuthInterceptor implements Interceptor {
    private String credentials;

    public BasicAuthInterceptor(String user, String password) {

        this.credentials = Credentials.basic(user, password);
        Log.e("hhhhh", "BasicAuthInterceptor: "+credentials );
    }


    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        Request authenticatedRequest = request.newBuilder()
                .header("Authorization", credentials).build();
        return chain.proceed(authenticatedRequest);
    }
}
