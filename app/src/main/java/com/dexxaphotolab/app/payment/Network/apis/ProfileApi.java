package com.dexxaphotolab.app.payment.Network.apis;


import com.dexxaphotolab.app.payment.Network.models.ResponseStatus;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface ProfileApi {

    @Multipart
    @POST("update_profile")
    Call<ResponseStatus> updateProfile(@Header("API-KEY") String apiKey,
                                       @Part("id") RequestBody id,
                                       @Part("name") RequestBody name,
                                       @Part("email") RequestBody email,
                                       @Part("phone") RequestBody phone,
                                       @Part("company_name") RequestBody company_name,
                                       @Part("adress") RequestBody adress,
                                       @Part("website") RequestBody website,
                                       @Part("business_type") RequestBody business_type,
                                       @Part("password") RequestBody password,
                                       @Part("current_password") RequestBody currentPassword,
                                       @Part MultipartBody.Part photo,
                                       @Part("gender") RequestBody gender);

    @Multipart
    @POST("update_profile2")
    Call<ResponseStatus> updateProfile2(@Header("API-KEY") String apiKey,
                                        @Part("id") RequestBody id,
                                        @Part("name") RequestBody name,
                                        @Part("email") RequestBody email,
                                        @Part("phone") RequestBody phone,
                                        @Part("company_name") RequestBody company_name,
                                        @Part("adress") RequestBody adress,
                                        @Part("website") RequestBody website,
                                        @Part("business_type") RequestBody business_type,
                                        @Part MultipartBody.Part photo);

}
