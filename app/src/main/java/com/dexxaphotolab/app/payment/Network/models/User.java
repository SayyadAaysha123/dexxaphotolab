package com.dexxaphotolab.app.payment.Network.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class User {

    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("user_id")
    @Expose
    private String userId;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("phone")
    @Expose
    private String phone;
    @SerializedName("company_name")
    @Expose
    private String company_name;
    @SerializedName("adress")
    @Expose
    private String adress;
    @SerializedName("website")
    @Expose
    private String website;
    @SerializedName("business_type")
    @Expose
    private String business_type;
    @SerializedName("gender")
    @Expose
    private String gender;
    @SerializedName("join_date")
    @Expose
    private String joinDate;
    @SerializedName("last_login")
    @Expose
    private String lastLogin;
    @SerializedName("data")
    @Expose
    private String data;
    @SerializedName("image_url")
    @Expose
    private String imageUrl;
    @SerializedName("password_available")
    @Expose
    private boolean isPasswordAvailable;

    public User() {
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getJoinDate() {
        return joinDate;
    }

    public void setJoinDate(String joinDate) {
        this.joinDate = joinDate;
    }

    public String getLastLogin() {
        return lastLogin;
    }

    public void setLastLogin(String lastLogin) {
        this.lastLogin = lastLogin;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public boolean isPasswordAvailable() {
        return isPasswordAvailable;
    }

    public void setPasswordAvailable(boolean passwordAvailable) {
        isPasswordAvailable = passwordAvailable;
    }

    public String getCompany_name() {
        return company_name;
    }

    public void setCompany_name(String company_name) {
        this.company_name = company_name;
    }

    public String getAdress() {
        return adress;
    }

    public void setAdress(String adress) {
        this.adress = adress;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getBusiness_type() {
        return business_type;
    }

    public void setBusiness_type(String business_type) {
        this.business_type = business_type;
    }

    @Override
    public String toString() {
        return "User{" +
                "status='" + status + '\'' +
                ", userId='" + userId + '\'' +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", company_name='" + company_name + '\'' +
                ", adress='" + adress + '\'' +
                ", website='" + website + '\'' +
                ", business_type='" + business_type + '\'' +
                ", gender='" + gender + '\'' +
                ", joinDate='" + joinDate + '\'' +
                ", lastLogin='" + lastLogin + '\'' +
                ", data='" + data + '\'' +
                ", image_url='" + imageUrl + '\'' +
                ", password_available='" + isPasswordAvailable + '\'' +
                '}';
    }
}
