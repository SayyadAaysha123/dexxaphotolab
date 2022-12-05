package com.dexxaphotolab.app.Model;

import java.io.Serializable;

public class SubCategoryModel implements Serializable {


    String subcatImg;
    String subcatName;
    String language;
    String paid;

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    String fileType;

    public String getFileUrl() {
        return fileUrl;
    }

    public void setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl;
    }

    String fileUrl;

    public String getPaid() {
        return paid;
    }

    public void setPaid(String paid) {
        this.paid = paid;
    }

    public SubCategoryModel(boolean b) {
    }


    public String getSubcatImg() {
        return subcatImg;
    }

    public void setSubcatImg(String subcatImg) {
        this.subcatImg = subcatImg;
    }

    public String getSubcatName() {
        return subcatName;
    }

    public void setSubcatName(String subcatName) {
        this.subcatName = subcatName;
    }

    public void setLanguage(String fileType) {
        this.language = fileType;
    }

    public String getLanguage() {
        return language;
    }
}
