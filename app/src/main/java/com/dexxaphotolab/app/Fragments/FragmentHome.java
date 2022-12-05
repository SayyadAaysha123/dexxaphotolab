package com.dexxaphotolab.app.Fragments;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.dexxaphotolab.app.ModelRetrofit.CategoriesData;
import com.dexxaphotolab.app.ModelRetrofit.VideoHomeData;
import com.dexxaphotolab.app.Adapters.CategoryAdapter;
import com.dexxaphotolab.app.Config;
import com.dexxaphotolab.app.Model.CategoryModel;
import com.dexxaphotolab.app.Model.ModelHomeParent;
import com.dexxaphotolab.app.Model.home_content.FeaturesGenreAndMovie;
import com.dexxaphotolab.app.Model.home_content.HomeContent;
import com.dexxaphotolab.app.Model.home_content.Slider;
import com.dexxaphotolab.app.Model.home_content.Video;
import com.dexxaphotolab.app.NewData.Adapter.GenreHomeAdapter;
import com.dexxaphotolab.app.NewData.Adapter.SliderAdapter;
import com.dexxaphotolab.app.NewData.model.CommonModels;
import com.dexxaphotolab.app.NewData.model.GenreModel;
import com.dexxaphotolab.app.R;
import com.dexxaphotolab.app.Retrofit.Api;
import com.dexxaphotolab.app.Retrofit.Base_Url1;
import com.dexxaphotolab.app.Utills.Constance;
import com.dexxaphotolab.app.Utills.SharedPrefrenceConfig;
import com.dexxaphotolab.app.payment.Utils.PreferenceUtils;
import com.github.islamkhsh.CardSliderViewPager;
import com.google.android.gms.analytics.Tracker;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Timer;

import me.relex.circleindicator.CircleIndicator;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;


@RequiresApi(api = Build.VERSION_CODES.N)
public class FragmentHome extends Fragment {

        private static final Integer[] image = {R.drawable.pager_1, R.drawable.pager_2,
                R.drawable.pager_3};
        public static ArrayList<CategoryModel> cat1_list = new ArrayList<>();
        Context context;
        View view;
        ViewPager vp_home;
        SharedPrefrenceConfig sharedPrefrenceConfig;
        String language;
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        ArrayList<VideoHomeData> videoCategoriesDataArrayList, businessCatList, greetingCatList, bannerList;
        ArrayList<CategoriesData> categories_name;
        CircleIndicator indicator_home;
        Timer timer;
        Tracker mTracker;
        RecyclerView rv_maincatdata;
        CardSliderViewPager cViewPager;
        List<CommonModels> listGenreMovie;
        private CategoryAdapter adapter;
        private ArrayList<String> imagearray = new ArrayList();
        private List<GenreModel> listGenre = new ArrayList<>();
        private GenreHomeAdapter genreHomeAdapter;
        private RecyclerView recyclerViewGenre;

        public static ArrayList<ModelHomeParent> getHomeParentTittleList() {
                ArrayList<ModelHomeParent> data = new ArrayList<>();
                data.add(new ModelHomeParent("Categories"));

                return data;
        }

        @Override
        public void onResume() {
                super.onResume();
                PreferenceUtils.updateSubscriptionStatus(getContext());
        }

        @Override
        public void onCreate(@Nullable Bundle savedInstanceState) {
                super.onCreate(savedInstanceState);
        }

        @Nullable
        @Override
        public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

                view = inflater.inflate(R.layout.fragment_home, container, false);
                context = getContext();
                bindView();


                language = sharedPrefrenceConfig.getPrefString(context, Constance.language, "");

                recyclerViewGenre = view.findViewById(R.id.recyclerView_by_genre);
                recyclerViewGenre.setHasFixedSize(true);
                recyclerViewGenre.setLayoutManager(new LinearLayoutManager(getContext()));
                recyclerViewGenre.setNestedScrollingEnabled(false);
                genreHomeAdapter = new GenreHomeAdapter(getContext(), listGenre, getActivity());
                recyclerViewGenre.setAdapter(genreHomeAdapter);

                getHomeContent();


                return view;
        }

//        List<CommonModels> sorted = listGenreMovie.stream().sorted(
//                (a,b)->
//                {
//                        try {
//                                return dateFormat.parse(a.getReleaseDate()).compareTo(dateFormat.parse(b.getReleaseDate()));
//                        } catch (ParseException e) {
//                                // TODO Auto-generated catch block
//                                e.printStackTrace();
//                        }
//                        return 0;
//                }
//        ).collect(Collectors.toList());


  /*  @RequiresApi(api = Build.VERSION_CODES.O)
    private void filter(String text) {
        ArrayList<CommonModels> filterList = new ArrayList<>();

        for (CommonModels models : listGenreMovie) {
                filterList.add(LocalDateTime.parse(models, dtf));


        }

        mMainAdapter.filterList(filterList);
    }*/

//    public void filterList(ArrayList<CommLockInfo> filterList) {
//        mLockInfos = filterList;
//        notifyDataSetChanged();
//    }

        private void getHomeContent() {
                Retrofit retrofit = Base_Url1.getRetrofitInstance();
                Api api = retrofit.create(Api.class);
                Call<HomeContent> call = api.getHomeContent(Config.API_KEY);
                call.enqueue(new Callback<HomeContent>() {
                        @Override
                        public void onResponse(Call<HomeContent> call, retrofit2.Response<HomeContent> response) {
                                if (response.code() == 200) {

                                        Slider slider = response.body().getSlider();


                                        SliderAdapter sliderAdapter = new SliderAdapter(slider.getSlide());
                                        cViewPager.setAdapter(sliderAdapter);
                                        sliderAdapter.notifyDataSetChanged();


                                        Log.e("kkkkkk", "onResponse: " + response.body().getFeaturesGenreAndMovie().size());


                                        for (int i = 0; i < response.body().getFeaturesGenreAndMovie().size(); i++) {
                                                FeaturesGenreAndMovie genreAndMovie = response.body().getFeaturesGenreAndMovie().get(i);
                                                GenreModel models = new GenreModel();
                                                Log.e("kkkk1111", "onResponse: " + genreAndMovie.getName());
                                                models.setName(genreAndMovie.getName());
                                                models.setId(genreAndMovie.getGenreId());
                                                listGenreMovie = new ArrayList<>();
                                                for (int j = 0; j < genreAndMovie.getVideos().size(); j++) {
                                                        Video video = genreAndMovie.getVideos().get(j);
                                                        CommonModels commonModels = new CommonModels();

                                                        commonModels.setId(video.getVideosId());
                                                        commonModels.setTitle(video.getTitle());
                                                        Log.e("1212", "home: " + video.getTitle());
                                                        commonModels.setIsPaid(video.getIsPaid());

                                                        if (video.getIsTvseries().equals("0")) {
                                                                commonModels.setVideoType("movie");
                                                        } else {
                                                                commonModels.setVideoType("tvseries");
                                                        }

                                                        commonModels.setReleaseDate(video.getRelease());
                                                        commonModels.setQuality(video.getVideoQuality());
                                                        commonModels.setImageUrl(video.getThumbnailUrl());

                                                        listGenreMovie.add(commonModels);

                                                        Collections.sort(listGenreMovie, CommonModels.comparator);
                                                }
                                                models.setList(listGenreMovie);
                                                Log.e("kkkk2222", "onResponse: " + genreAndMovie.getName());
                                                listGenre.add(models);
                                                genreHomeAdapter.notifyDataSetChanged();
                                        }

                                } else {

                                }

                        }

                        @Override
                        public void onFailure(Call<HomeContent> call, Throwable t) {


                        }
                });
        }

        public void bindView() {
                vp_home = view.findViewById(R.id.vp_home);
                indicator_home = view.findViewById(R.id.indicator_home);

                cViewPager = view.findViewById(R.id.c_viewPager);

                videoCategoriesDataArrayList = new ArrayList<>();
                businessCatList = new ArrayList<>();
                greetingCatList = new ArrayList<>();
                categories_name = new ArrayList<>();
                bannerList = new ArrayList<>();
        }


}
