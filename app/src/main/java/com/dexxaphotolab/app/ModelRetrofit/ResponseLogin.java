package com.dexxaphotolab.app.ModelRetrofit;

import com.google.gson.annotations.SerializedName;

public class ResponseLogin {
    @SerializedName("result")
    private String result;

    @SerializedName("message")
    private String message;

    @SerializedName("api_token")
    private String api_token;

    @SerializedName("record")
    private RecordRegister record;

    public ResponseLogin(String api_token) {
        this.api_token = api_token;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getApi_token() {
        return api_token;
    }

    public void setApi_token(String api_token) {
        this.api_token = api_token;
    }

    public RecordRegister getRecord() {
        return record;
    }

    public void setRecord(RecordRegister record) {
        this.record = record;
    }
}
