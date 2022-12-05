
package com.dexxaphotolab.app.Model.home_content;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class Slider {

    @SerializedName("slider_type")
    @Expose
    private String sliderType;
    @SerializedName("slide")
    @Expose
    private ArrayList<Slide> slide = null;

    public String getSliderType() {
        return sliderType;
    }

    public void setSliderType(String sliderType) {
        this.sliderType = sliderType;
    }

    public ArrayList<Slide> getSlide() {
        return slide;
    }

    public void setSlide(ArrayList<Slide> slide) {
        this.slide = slide;
    }

}
