package com.dexxaphotolab.app.Model;

import java.io.Serializable;

public class CategoryModel implements Serializable {


    String catImg;
    String catName;
    String ispermium;

    public CategoryModel(boolean b) {
    }


    public String getCatImg() {
        return catImg;
    }

    public void setCatImg(String catImg) {
        this.catImg = catImg;
    }

    public String getCatName() {
        return catName;
    }

    public void setCatName(String catName) {
        this.catName = catName;
    }

    public String getIspermium() {
        return ispermium;
    }

    public void setIspermium(String ispermium) {
        this.ispermium = ispermium;
    }

}
