package com.dexxaphotolab.app.Retrofit;


import com.dexxaphotolab.app.ModelRetrofit.List_of_Categories_name;
import com.dexxaphotolab.app.ModelRetrofit.ResponseLogin;
import com.dexxaphotolab.app.ModelRetrofit.UserRegister;
import com.dexxaphotolab.app.ModelRetrofit.VideoHomeModel;
import com.dexxaphotolab.app.Model.home_content.HomeContent;
import com.dexxaphotolab.app.Model.home_content.Video;


import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

public interface Api {

    @GET("home_content_for_android")
    Call<HomeContent> getHomeContent(@Header("API-KEY") String apiKey);

    @POST
    Call<ResponseLogin> forgetPassword(@Query("contact") String contact);

    @POST("category/category_list")
    Call<List_of_Categories_name> getCategoriesNameList(@Query("api_token") String api_token);

    @Multipart
    @POST("user/register")
    Call<UserRegister> signUp(@Part("name") RequestBody name,
                              @Part("email") RequestBody email,
                              @Part("password") RequestBody password,
                              @Part("city") RequestBody city,
                              @Part MultipartBody.Part image,
                              @Part("business") RequestBody business,
                              @Part("contact") RequestBody contact);

    @FormUrlEncoded
    @POST("login")
    Call<ResponseLogin> signIn(
            @Field("email") String email,
            @Field("password") String password);

    @POST("category/category_list")
    Call<VideoHomeModel> festivaslVideoCatList(@Query("api_token") String api_token);

    @POST("greeting_category/greeting_images_list")
    Call<VideoHomeModel> getGreetingImageList(@Query("api_token") String api_token, @Query("greeting_id") String greeting_id, @Query("language") String language);

    @GET("movies")
    Call<List<Video>> getPoster(@Header("API-KEY") String apiKey,
                                @Query("page") int pahe);

    @GET("content_by_genre_id")
    Call<List<Video>> getPosterByGenreId(@Header("API-KEY") String apiKey,
                                         @Query("id") String id,
                                         @Query("page") int page);


}


