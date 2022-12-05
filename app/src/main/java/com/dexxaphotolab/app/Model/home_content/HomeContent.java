
package com.dexxaphotolab.app.Model.home_content;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class HomeContent {

    @SerializedName("slider")
    @Expose
    private Slider slider;

    @SerializedName("features_genre_and_movie")
    @Expose
    private List<FeaturesGenreAndMovie> featuresGenreAndMovie = null;

    public Slider getSlider() {
        return slider;
    }

    public void setSlider(Slider slider) {
        this.slider = slider;
    }


    public List<FeaturesGenreAndMovie> getFeaturesGenreAndMovie() {
        return featuresGenreAndMovie;
    }

    public void setFeaturesGenreAndMovie(List<FeaturesGenreAndMovie> featuresGenreAndMovie) {
        this.featuresGenreAndMovie = featuresGenreAndMovie;
    }

}
