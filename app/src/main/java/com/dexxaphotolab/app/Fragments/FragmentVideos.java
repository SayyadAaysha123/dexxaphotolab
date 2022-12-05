package com.dexxaphotolab.app.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.dexxaphotolab.app.ModelRetrofit.VideoHomeData;
import com.dexxaphotolab.app.Adapters.AdapterVideoList;
import com.dexxaphotolab.app.Model.SubCategoryModel;
import com.dexxaphotolab.app.Model.home_content.Video;
import com.dexxaphotolab.app.NewData.model.CommonModels;
import com.dexxaphotolab.app.R;
import com.dexxaphotolab.app.Retrofit.Api;
import com.dexxaphotolab.app.Utills.Constance;
import com.dexxaphotolab.app.Utills.SharedPrefrenceConfig;
import com.dexxaphotolab.app.payment.Config;
import com.dexxaphotolab.app.payment.Network.RetrofitClient;
import com.dexxaphotolab.app.payment.Utils.NetworkInst;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.gms.analytics.Tracker;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;

public class
FragmentVideos extends Fragment {

    Context context;
    View view;
    SharedPrefrenceConfig sharedPrefrenceConfig;
    RecyclerView rv_viewalllist, rv_cat_alv;
    Tracker mTracker;
    String language;
    ArrayList<VideoHomeData> videoCategoriesDataArrayList;
    AdapterVideoList adapterVideoList;

    private ShimmerFrameLayout shimmerFrameLayout;
    private ProgressBar progressBar;
    private CoordinatorLayout coordinatorLayout;
    private SwipeRefreshLayout swipeRefreshLayout;
    private boolean isLoading = false;
    private int pageCount = 1;
    private List<CommonModels> list = new ArrayList<>();
    private List<SubCategoryModel> videos = new ArrayList<>();
    private RecyclerView recyclerView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_videos, container, false);
        context = getContext();
        sharedPrefrenceConfig = new SharedPrefrenceConfig(context);
        language = sharedPrefrenceConfig.getPrefString(context, Constance.language, "");

        bindView();
        rv_viewalllist.setLayoutManager(new GridLayoutManager(context, 3));
        rv_cat_alv.setLayoutManager(new GridLayoutManager(context, 3));


        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                coordinatorLayout.setVisibility(View.GONE);

                pageCount = 1;

                list.clear();
                recyclerView.removeAllViews();
                adapterVideoList.notifyDataSetChanged();

                if (new NetworkInst(getContext()).isNetworkAvailable()) {
                    getPosterByGenreId("93",pageCount);
//                    Apicat2();
                } else {
                    shimmerFrameLayout.stopShimmer();
                    shimmerFrameLayout.setVisibility(View.GONE);
                    swipeRefreshLayout.setRefreshing(false);
                    coordinatorLayout.setVisibility(View.VISIBLE);
                }
            }
        });


        return view;
    }

    public void bindView() {
        rv_viewalllist = view.findViewById(R.id.rv_viewalllist);
        rv_cat_alv = view.findViewById(R.id.rv_cat_alv);
        videoCategoriesDataArrayList = new ArrayList<>();

        coordinatorLayout = view.findViewById(R.id.coordinator_lyt);
        progressBar = view.findViewById(R.id.item_progress_bar);
        shimmerFrameLayout = view.findViewById(R.id.shimmer_view_container);
        shimmerFrameLayout.startShimmer();
        swipeRefreshLayout = view.findViewById(R.id.swipe_layout);

        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 3));

        getPosterByGenreId("93",pageCount);
//Apicat2();
    }



    private void getPoster(int pageNum) {
        Retrofit retrofit = RetrofitClient.getRetrofitInstance();
        Api api = retrofit.create(Api.class);
        Call<List<Video>> call = api.getPoster(Config.API_KEY, pageNum);
        call.enqueue(new Callback<List<Video>>() {
            @Override
            public void onResponse(Call<List<Video>> call, retrofit2.Response<List<Video>> response) {
                if (response.code() == 200) {
                    Log.e("12121", "onResponse: "+ response.body() );
                    isLoading = false;
                    progressBar.setVisibility(View.GONE);
                    shimmerFrameLayout.stopShimmer();
                    shimmerFrameLayout.setVisibility(View.GONE);
                    swipeRefreshLayout.setRefreshing(false);

                    if (response.body().size() == 0 && pageCount == 1) {
                        coordinatorLayout.setVisibility(View.VISIBLE);
                    } else {
                        coordinatorLayout.setVisibility(View.GONE);
                    }
                    Log.e("666666", "onResponse: " + response.body().size());

                    for (int i = 0; i < response.body().size(); i++) {
                        Video video = response.body().get(i);
                        CommonModels models = new CommonModels();
                        models.setImageUrl(video.getThumbnailUrl());
                        models.setTitle(video.getTitle());
                        models.setQuality(video.getVideoQuality());
                        models.setReleaseDate(video.getRelease());
                        models.setIsPaid(video.getIsPaid());
//                        if (video.getIsTvseries().equals("1")) {
//                            models.setVideoType("tvseries");
//                        } else {
//                            models.setVideoType("movie");
//                        }


                        models.setId(video.getVideosId());
                        list.add(models);
                        Log.e("666666", "onResponse: " + list.size());
                    }

//                    adapterVideoList = new AdapterVideoList(context, list, "businessviewall");
//                    recyclerView.setAdapter(adapterVideoList);
                    adapterVideoList.notifyDataSetChanged();

//                if (response.body().getResult().equals("1") || response.body().getResult() != null) {
//                    if (response.body().getRecords() != null) {
//                        if (response.body().getRecords().getData() != null && response.body().getRecords().getData().size() != 0) {
//                            Log.d("fdfsdkfk", "dsfji" + response.body().getRecords().getData().size());
//                            //Toast.makeText(context,"not null data ",Toast.LENGTH_LONG).show();
//                            videoCategoriesDataArrayList.addAll(response.body().getRecords().getData());
//                            adapterVideoList = new AdapterVideoList(context, videoCategoriesDataArrayList, "businessviewall");
//                            rv_cat_alv.setAdapter(adapterVideoList);
//                            progressDialog.dismiss();
//
//                        } else {
//                            isLoading = false;
//                            progressBar.setVisibility(View.GONE);
//                            shimmerFrameLayout.stopShimmer();
//                            shimmerFrameLayout.setVisibility(View.GONE);
//                            swipeRefreshLayout.setRefreshing(false);
//                            if (pageCount == 1) {
//                                coordinatorLayout.setVisibility(View.VISIBLE);
//                            }
//                        }
//                    }
                }
            }

            @Override
            public void onFailure(Call<List<Video>> call, Throwable t) {

                Log.e("12121", "onFailure: "+ t.getMessage() );

                isLoading = false;
                progressBar.setVisibility(View.GONE);
                shimmerFrameLayout.stopShimmer();
                shimmerFrameLayout.setVisibility(View.GONE);
                swipeRefreshLayout.setRefreshing(false);
                if (pageCount == 1) {
                    coordinatorLayout.setVisibility(View.VISIBLE);
                }
            }
        });
    }


    private void getPosterByGenreId(String id, int pageNum) {
        Retrofit retrofit = RetrofitClient.getRetrofitInstance();
        Api api = retrofit.create(Api.class);
        Call<List<Video>> call = api.getPosterByGenreId(Config.API_KEY, id, pageNum);
        call.enqueue(new Callback<List<Video>>() {
            @Override
            public void onResponse(Call<List<Video>> call, retrofit2.Response<List<Video>> response) {
                if (response.code() == 200) {
                    isLoading = false;
                    progressBar.setVisibility(View.GONE);
                    shimmerFrameLayout.stopShimmer();
                    shimmerFrameLayout.setVisibility(View.GONE);
                    swipeRefreshLayout.setRefreshing(false);

                    if (response.body().size() == 0 && pageCount == 1) {
                        coordinatorLayout.setVisibility(View.VISIBLE);
                    } else {
                        coordinatorLayout.setVisibility(View.GONE);
                    }

                    for (int i = 0; i < response.body().size(); i++) {
                        Video video = response.body().get(i);
                        CommonModels models = new CommonModels();
                        models.setImageUrl(video.getThumbnailUrl());
                        models.setTitle(video.getTitle());
                        models.setQuality(video.getVideoQuality());
                        models.setReleaseDate(video.getRelease());
                        models.setIsPaid(video.getIsPaid());
                        if (video.getIsTvseries().equals("1")) {
                            models.setVideoType("tvseries");
                        } else {
                            models.setVideoType("movie");
                        }


                        models.setId(video.getVideosId());
                        list.add(models);
                    }
                    adapterVideoList = new AdapterVideoList(context, list, "businessviewall");
                    recyclerView.setAdapter(adapterVideoList);
                    adapterVideoList.notifyDataSetChanged();
                } else {
                    isLoading = false;
                    progressBar.setVisibility(View.GONE);
                    shimmerFrameLayout.stopShimmer();
                    shimmerFrameLayout.setVisibility(View.GONE);
                    swipeRefreshLayout.setRefreshing(false);
                    if (pageCount == 1) {
                        coordinatorLayout.setVisibility(View.VISIBLE);
                    }
                }
            }

            @Override
            public void onFailure(Call<List<Video>> call, Throwable t) {
                isLoading = false;
                progressBar.setVisibility(View.GONE);
                shimmerFrameLayout.stopShimmer();
                shimmerFrameLayout.setVisibility(View.GONE);
                swipeRefreshLayout.setRefreshing(false);
                if (pageCount == 1) {
                    coordinatorLayout.setVisibility(View.VISIBLE);
                }
            }
        });
    }


}
