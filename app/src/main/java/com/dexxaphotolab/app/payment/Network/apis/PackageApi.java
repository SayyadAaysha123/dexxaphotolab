package com.dexxaphotolab.app.payment.Network.apis;



import com.dexxaphotolab.app.payment.Network.models.AllPackage;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;

public interface PackageApi {

    @GET("all_package")
    Call<AllPackage> getAllPackage(@Header("API-KEY") String apiKey);

}
